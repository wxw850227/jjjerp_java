<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item style="margin-right: 20px" label="出入库时间">
					<el-radio-group v-model="searchForm.operTimes" size="small" @change="updateDateRange">
						<el-radio label="1">今天</el-radio>
						<el-radio label="2">近七天</el-radio>
						<el-radio label="3">近30天</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="起始日期">
					<el-date-picker v-model="searchForm.time[0]" type="datetime" placeholder="开始日期"
						class="form_content_width"></el-date-picker>
				</el-form-item>

				<el-form-item label="结束日期">
					<el-date-picker v-model="searchForm.time[1]" type="datetime" placeholder="结束日期"
						class="form_content_width"></el-date-picker>
				</el-form-item>

				<el-form-item label="仓库">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择" class="form_content_width">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="店铺">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" class="form_content_width" />
					</el-select>
				</el-form-item>

				<el-form-item label="订单号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder=""
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item label="快递单号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder=""
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item label="电话">
					<el-input size="small" v-model="searchForm.phone" placeholder=""
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small" @click="resetForm">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1"></div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--内容-->
		<el-table class="flex-1" size="small" :data="tableData" style="width: 100%" v-loading="loading">
			<el-table-column prop="#" label="序号" width="52">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<el-table-column prop="codeNum" label="单据编号" show-overflow-tooltip>
			</el-table-column>
			<el-table-column prop="typeName" label="单据类型"> </el-table-column>
			<el-table-column prop="depotName" label="仓库"> </el-table-column>
			<el-table-column prop="customerName" label="客户"> </el-table-column>
			<el-table-column prop="totalPrice" label="总金额"> </el-table-column>
			<el-table-column prop="storeName" label="店铺"> </el-table-column>
			<el-table-column prop="linkBillNum" label="订单号" show-overflow-tooltip>
			</el-table-column>
			<el-table-column prop="expressNo" label="快递单号" show-overflow-tooltip></el-table-column>
			<el-table-column prop="codeNum" label="售后单号" show-overflow-tooltip></el-table-column>
			<el-table-column prop="stateText" label="售后原因"> </el-table-column>
			<el-table-column prop="typeName" label="电话"></el-table-column>
			<el-table-column prop="expressName" label="快递公司"></el-table-column>
			<el-table-column prop="creatorId" label="操作员"></el-table-column>
			<el-table-column prop="salesId" label="业务员"></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" show-overflow-tooltip>
			</el-table-column>
			<el-table-column prop="createTime" label="下单时间" show-overflow-tooltip></el-table-column>
			<el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
		</el-table>
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import RefundApi from "@/api/refund.js";
	import SettingApi from "@/api/setting.js";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	export default {
		components: {},
		data() {
			return {
				loading: true,
				open_print: false,
				activeName: "detail",
				num: 0,
				/*列表数据*/
				tableData: [],
				billItemList: [],
				otherData: {},
				selectedRows: [], // 选中的行数据
				/*一页多少条*/
				pageSize: 20,
				/*总条数*/
				addForm: {},
				allData: {},
				totalDataNumber: 0,
				open_add: false,
				open_returnAdd: false,
				open_edit: false,
				open_logistic: false,
				printData: {},
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					operTimes: "1",
					time: [null, null],
					name: "",
				},
				status: -1,
				depots: [],
				suppliers: [],
				selectedBillId: null,
				openTitle: "",
				customerList: [],
				depotList: [],
				storeList: [],
				depotIdList: [],
				openDetail: false,
				parentSelected: false,
				selectDate: {},
			};
		},
		watch: {
			parentSelected(newValue) {
				// 父列表取消勾选时，取消所有子列表的勾选状态
				if (!newValue && this.selectedRows.length > 0) {
					this.cancelAllChildSelection();
				}
			},
		},
		created() {
			/*获取列表*/
			// this.getData();
			this.getselect();
			this.updateDateRange();
		},

		computed: {},
		methods: {
			isRootNode(row) {
				return Array.isArray(row.billItemList) && row.billItemList.length > 0;
			},

			updateDateRange() {
				const today = new Date();
				const startOfToday = new Date(today.setHours(0, 0, 0, 0));
				const endOfToday = new Date(today.setHours(23, 59, 59, 999));

				if (this.searchForm.operTimes === "1") {
					this.searchForm.time = [
						formatDate(startOfToday, "YYYY-MM-DD"),
						formatDate(endOfToday, "YYYY-MM-DD"),
					];
				} else if (this.searchForm.operTimes === "2") {
					const startOfSevenDaysAgo = new Date(
						today.setDate(today.getDate() - 6)
					);
					startOfSevenDaysAgo.setHours(0, 0, 0, 0);
					this.searchForm.time = [
						formatDate(startOfSevenDaysAgo, "YYYY-MM-DD"),
						formatDate(endOfToday, "YYYY-MM-DD"),
					];
				} else if (this.searchForm.operTimes === "3") {
					const startOfThirtyDaysAgo = new Date(
						today.setDate(today.getDate() - 29)
					);
					startOfThirtyDaysAgo.setHours(0, 0, 0, 0);
					this.searchForm.time = [
						formatDate(startOfThirtyDaysAgo, "YYYY-MM-DD"),
						formatDate(endOfToday, "YYYY-MM-DD"),
					];
				}
				this.onSubmit(); // 自动触发搜索
			},
			getselect() {
				SettingApi.userList({})
					.then((res) => {
						this.customerList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
					});
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.loading = true;
				let Params = self.searchForm;
				self.getData(Params);
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getData();
			},

			/*获取列表*/
			getData() {
				let self = this;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				RefundApi.refundPutMarket(Params, true)
					.then((res) => {
						self.loading = false;
						self.otherData = res.data;
						self.tableData = res.data.bill.records.map((record) => ({
							...record,
							id: record.billId,
						}));
						console.log(this.tableData, "self.tableData");

						self.totalDataNumber = res.data.bill.total;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
				this.openDetail = true;
			},
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_add = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "edit") {
					this.open_edit = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "print") {
					this.open_print = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "returnAdd") {
					this.open_returnAdd = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
			},

			/*搜索查询*/
			onSubmit() {
				let self = this;
				self.loading = true;
				self.getData();
			},
			resetForm() {
				this.searchForm = {
					operTimes: "1",
					name: "",
					time: [null, null],
				};
				this.updateDateRange();
				this.getData();
			},
		},
	};
</script>

<style scoped>
	::v-depp.no-border .el-table__row--striped,
	::v-depp.no-border .el-table__row {
		border: none !important;
	}

	.form-item-container {
		display: flex;
		flex-direction: column;
	}

	.form-item-label {
		font-weight: bold;
		margin-bottom: 8px;
	}

	.form-item-content {
		background-color: rgba(239, 239, 239, 1);
		padding: 20px;
		width: 294px;
		margin: 20px;
	}

	.form-item-content1 {
		background-color: rgba(239, 239, 239, 1);
		padding: 20px;
		width: 100%;
		margin: 20px;
	}

	.content-box {
		display: flex;
		flex-direction: column;
		background-color: rgba(239, 239, 239, 1);
		padding: 20px;
	}

	.content-box-container {
		width: 100%;
		display: flex;
		flex-direction: row;
	}

	.titles {
		color: rgba(16, 16, 16, 1);
		font-size: 14px;
		margin: 20px;
		text-align: right;
	}
</style>