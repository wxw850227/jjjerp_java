<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item style="margin-right: 10px" label="下单时间">
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
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" class="form_content_width" />
					</el-select>
				</el-form-item>
				<el-form-item label="店铺">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择" class="form_content_width">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
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
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" @click="addClick" icon="plus"
					v-auth="'/product/base/unit/add'">新建售后单</el-button>
				<el-button icon="plus" size="small" @click="addClicks" v-auth="'/product/base/unit/add'">新建售后单(仅退款)</el-button>
			</div>

			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--内容-->
		<el-table class="flex-1" size="small" :data="tableData" style="width: 100%" row-key="billItemId" ref="table"
			:tree-props="{ children: 'billItemList' }" @select="handleSelect" @selection-change="handleSelectionChange"
			v-loading="loading">
			<el-table-column prop="#" width="42"></el-table-column>
			<el-table-column type="selection" width="42"></el-table-column>
			<el-table-column prop="storeName" label="店铺">
				<template #default="{ row }">
					<div v-if="isRootNode(row)">
						<span>{{ row.storeName }}</span>
					</div>
					<div v-else>
						<el-popover placement="right-start" trigger="hover" width="276">
							<template #reference>
								<el-avatar v-if="row.productExtend.imageUrl" shape="square" :size="26"
									:src="row.productExtend.imageUrl" />
								<el-avatar v-else :size="26" shape="square" src="/static/imgs/imagerror.png" />
							</template>

							<template #default>
								<el-avatar v-if="row.productExtend.imageUrl" :size="250" shape="square"
									:src="row.productExtend.imageUrl" />
								<el-avatar v-else :size="250" shape="square" src="/static/imgs/imagerror.png" />
							</template>
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column width="200" prop="codeNum" label="订单号" show-overflow-tooltip>
				<template #default="{ row }">
					<div v-if="isRootNode(row)">
						<span>{{ row.codeNum }}</span>
					</div>
					<div v-else>
						<div class="d-s-c d-r">
							<span style="white-space: nowrap">【{{ row.productExtend.productCode }}】</span>
							<span>{{ row.productExtend.name }}</span>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column width="130" prop="stateText" label="订单状态">
			</el-table-column>
			<el-table-column prop="buyerRemark" label="买家留言" show-overflow-tooltip></el-table-column>
			<el-table-column prop="systemRemark" label="系统备注" show-overflow-tooltip></el-table-column>
			<el-table-column prop="customerId" label="买家ID"></el-table-column>
			<el-table-column prop="address.name" label="收件人"></el-table-column>
			<el-table-column width="130" prop="address.phone" label="联系电话"></el-table-column>
			<el-table-column prop="address.detail" label="收货地址" show-overflow-tooltip></el-table-column>
			<el-table-column prop="totalPrice" width="130" label="实付金额">
				<template #default="{ row }">
					<div v-if="isRootNode(row)">
						<span>{{ row.totalPrice }}</span>
					</div>
					<div v-else>
						<div class="d-e-c d-c">
							<span>x{{ row.operNumber }}</span>
							<span>￥{{ row.unitPrice }}</span>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="purchaseMoney" label="邮费" width="130"></el-table-column>
			<el-table-column prop="discountMoney" label="优惠金额"></el-table-column>
			<el-table-column prop="orderTime" label="下单时间" show-overflow-tooltip></el-table-column>
			<el-table-column prop="expressName" label="快递公司"></el-table-column>
			<el-table-column prop="expressNo" label="快递单号" show-overflow-tooltip></el-table-column>
		</el-table>
		<ReturnAdd v-if="open_returnAdd" :open_returnAdd="open_returnAdd" :selectDate="selectDate" :addForm="addForm"
			@closeDialog="closeDialogFunc($event, 'returnAdd')"></ReturnAdd>
		<Add v-if="open_add" :open_add="open_add" :selectDate="selectDate" :addForm="addForm"
			@closeDialog="closeDialogFunc($event, 'add')"></Add>
	</div>
</template>

<script>
	import Add from "./add.vue";
	import ReturnAdd from "./returnAdd.vue";
	import PurchaseApi from "@/api/purchase.js";
	import RefundApi from "@/api/refund.js";
	import SettingApi from "@/api/setting.js";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	export default {
		components: {
			Add,
			ReturnAdd,
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
			// parentSelected(newValue) {
			//   // 父列表取消勾选时，取消所有子列表的勾选状态
			//   if (!newValue && this.selectedRows.length > 0) {
			//     this.cancelAllChildSelection();
			//   }
			// },
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
			handleSelectionChange(selection) {
				if (this.updating) return;
				this.updating = true;

				this.selectedRows = [...selection];
				console.log(selection, "selection");

				this.tableData.forEach((row) => {
					if (this.isRootNode(row)) {
						const allChildrenSelected = row.billItemList.every((child) =>
							selection.includes(child)
						);
						const anyChildSelected = row.billItemList.some((child) =>
							selection.includes(child)
						);
						if (anyChildSelected && !selection.includes(row)) {
							this.$refs.table.toggleRowSelection(row, true);
							if (!this.selectedRows.includes(row)) {
								this.selectedRows.push(row);
							}
						} else if (!anyChildSelected && selection.includes(row)) {
							this.$refs.table.toggleRowSelection(row, false);
							this.selectedRows = this.selectedRows.filter(
								(item) => item.billItemId !== row.billItemId
							);
						}
					}
				});

				console.log(this.selectedRows, "selectedRows");
				this.updating = false;
			},
			handleSelect(selection, row) {
				if (this.updating) return;
				this.updating = true;

				if (this.isRootNode(row)) {
					row.billItemList.forEach((child) => {
						this.$refs.table.toggleRowSelection(child, selection.includes(row));
					});
					if (selection.includes(row)) {
						row.billItemList.forEach((child) => {
							if (!this.selectedRows.includes(child)) {
								this.selectedRows.push(child);
							}
						});
						if (!this.selectedRows.includes(row)) {
							this.selectedRows.push(row);
						}
					} else {
						this.selectedRows = this.selectedRows.filter(
							(item) =>
							!row.billItemList.includes(item) &&
							item.billItemId !== row.billItemId
						);
					}
				} else {
					const parentRow = this.findParentRow(this.tableData, row);
					if (parentRow) {
						const allChildrenSelected = parentRow.billItemList.every((child) =>
							selection.includes(child)
						);
						const anyChildSelected = parentRow.billItemList.some((child) =>
							selection.includes(child)
						);
						if (allChildrenSelected) {
							this.$refs.table.toggleRowSelection(parentRow, true);
							if (!this.selectedRows.includes(parentRow)) {
								this.selectedRows.push(parentRow);
							}
						} else if (!anyChildSelected) {
							this.$refs.table.toggleRowSelection(parentRow, false);
							this.selectedRows = this.selectedRows.filter(
								(item) => item.billItemId !== parentRow.billItemId
							);
						}
					}
				}

				console.log(this.selectedRows, "selectedRows");
				this.updating = false;
			},
			findParentRow(tableData, childRow) {
				for (let row of tableData) {
					if (row.billItemList && row.billItemList.includes(childRow)) {
						return row;
					}
					if (row.billItemList) {
						const parentRow = this.findParentRow(row.billItemList, childRow);
						if (parentRow) {
							return parentRow;
						}
					}
				}
				return null;
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
				RefundApi.refundFlowMarketIndex(Params, true)
					.then((res) => {
						self.loading = false;
						self.otherData = res.data;
						self.tableData = res.data.bill.records.map((record) => ({
							...record,
							billItemId: record.billId,
						}));
						console.log(this.tableData, "self.tableData");

						self.totalDataNumber = res.data.bill.total;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClicks() {
				this.businessType = 17;
				this.selectDate = this.selectedRows;
				this.addForm = this.otherData;
				console.log(this.addForm, "this.addForm");
				if (this.selectDate == "") {
					ElMessageBox.confirm("您未勾选订单!", "友情提示", {
							// confirmButtonText: "确定",
							// cancelButtonText: "取消",
							type: "warning",
						})
						.then(() => {
							this.open_returnAdd = false;
						})
						.catch(() => {});
				} else {
					this.open_returnAdd = true;
				}
			},
			addClick() {
				this.businessType = 17;
				this.selectDate = this.selectedRows;
				this.addForm = this.otherData;
				console.log(this.addForm, "this.addForm");
				if (this.selectDate == "") {
					ElMessageBox.confirm("您未勾选订单!", "友情提示", {
							// confirmButtonText: "确定",
							// cancelButtonText: "取消",
							type: "warning",
						})
						.then(() => {
							this.open_add = false;
						})
						.catch(() => {});
				} else {
					this.open_add = true;
				}
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