<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item style="margin-right: 20px" label="记账时间">
					<el-radio-group v-model="searchForm.operTimes" size="small" @change="updateDateRange">
						<el-radio label="1">今天</el-radio>
						<el-radio label="2">近七天</el-radio>
						<el-radio label="3">近30天</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="起始日期">
					<el-date-picker v-model="searchForm.time[0]" type="datetime" placeholder="开始日期"></el-date-picker>
				</el-form-item>

				<el-form-item label="结束日期">
					<el-date-picker v-model="searchForm.time[1]" type="datetime" placeholder="结束日期"></el-date-picker>
				</el-form-item>

				<el-form-item label="结算账户">
					<el-select size="small" v-model="searchForm.accountId" placeholder="请选择">
						<el-option v-for="(item, index) in accountList" :key="index" :label="item.accountName"
							:value="item.accountId" />
					</el-select>
				</el-form-item>

				<el-form-item label="收支方向">
					<el-select size="small" v-model="searchForm.directionType" placeholder="请选择">
						<el-option label="收入" value="1"></el-option>
						<el-option label="支出" value="2"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="单据编号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder=""></el-input>
				</el-form-item>
				<el-form-item label="交易往来单位">
					<el-select size="small" v-model="searchForm.tradingUnit" placeholder="请选择">
						<el-option label="其他" :value="0"></el-option>
						<el-option label="客户" :value="1"></el-option>
						<el-option label="供应商" :value="2"></el-option>
						<el-option label="快递" :value="3"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="店铺">
					<el-select size="small" v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
							:value="item.storeId" />
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button size="small"
						@click="resetForm">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
			</div>
			<!-- 分页 -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column prop="#" label="序号" width="60">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<el-table-column prop="billTime" label="记账时间" width="130"></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" width="130"></el-table-column>
			<el-table-column prop="storeName" label="店铺"></el-table-column>
			<el-table-column prop="typeName" label="单据类型"> </el-table-column>
			<el-table-column prop="directionTypeName" label="收支项目"></el-table-column>
			<el-table-column prop="tradingUnitName" label="往来单位"></el-table-column>
			<el-table-column width="130" prop="description" label="摘要" show-overflow-tooltip></el-table-column>
			<el-table-column prop="money" label="变动金额"></el-table-column>
			<el-table-column prop="fileUrls" label="凭证"></el-table-column>
			<el-table-column prop="balanceMoney" label="账户余额"></el-table-column>
			<el-table-column prop="accountName" label="账户名称" width="130"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="130"></el-table-column>
			<el-table-column prop="createTime" label="详情">
				<template #default="scope">
					<el-button type="primary" link size="small" @click="showDetail(scope.row)">
						查看详情
					</el-button>
				</template>
			</el-table-column>
		</el-table>

		<Detail v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :otherDatas="otherDatas"
			@closeDialog="closeDialogFunc($event, 'edit')"></Detail>
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
	import SettingApi from "@/api/setting.js";
	import Detail from "./detail.vue";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	export default {
		components: {
			Detail
		},
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
				open_edit: false,
				open_logistic: false,
				printData: {},
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					operTimes: "2",
					time: [null, null],
					name: "",
				},
				status: -1,
				depots: [],
				suppliers: [],
				selectedBillId: null,
				openTitle: "",
				customerList: [],
				accountList: [],
				depotList: [],
				storeList: [],
				depotIdList: [],
				openDetail: false,
				otherDatas: {},
			};
		},
		created() {
			/*获取列表*/
			// this.getData();
			// this.getselect();
			this.updateDateRange();
		},
		computed: {},
		methods: {
			showDetail(row) {
				this.categoryModel = row;
				this.otherDatas = this.otherData;
				this.open_edit = true;
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
			handleSelectionChange(selection) {
				this.selectedRows = selection;
				console.log(this.selectedRows, "selectedRows");
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
				FinanceApi.logList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.log.records;
						self.accountList = res.data.account;
						self.storeList = res.data.store;
						this.otherData = res.data;
						self.totalDataNumber = res.data.log.total;
					})
					.catch((error) => {
						self.loading = false;
					});
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
				if (f == "logistic") {
					this.open_logistic = e.openDialog;
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