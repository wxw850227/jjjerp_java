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
					<el-date-picker v-model="searchForm.time[0]" type="datetime" placeholder="开始日期"></el-date-picker>
				</el-form-item>

				<el-form-item label="结束日期">
					<el-date-picker v-model="searchForm.time[1]" type="datetime" placeholder="结束日期"></el-date-picker>
				</el-form-item>

				<el-form-item label="仓库">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="店铺">
					<el-select size="small" v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.nickName"
							:value="item.storeId" />
					</el-select>
				</el-form-item>
				<el-form-item label="出入库单号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder=""></el-input>
				</el-form-item>
				<el-form-item label="订单号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder=""></el-input>
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
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column prop="index" label="序号" width="55" :formatter="indexFormatter"></el-table-column>
			<el-table-column width="150" prop="head.codeNum" label="出入库单号"></el-table-column>
			<el-table-column width="130" prop="head.operTime" label="出入库时间"></el-table-column>
			<el-table-column prop="head.businessTime" label="业务日期" width="130"></el-table-column>
			<el-table-column prop="head.typeName" label="出入库类型"></el-table-column>
			<el-table-column prop="head.depotName" label="仓库"></el-table-column>
			<el-table-column prop="productExtend.productCode" label="商品编号"></el-table-column>
			<el-table-column prop="productExtend.productCode" label="规格编码"></el-table-column>
			<el-table-column width="130" prop="productExtend.name" label="商品名称" show-overflow-tooltip></el-table-column>
			<el-table-column prop="productExtend.categoryName" label="商品分类"></el-table-column>
			<el-table-column prop="sku" label="规格" width="130"></el-table-column>
			<el-table-column prop="operNumber" label="出入库数量"></el-table-column>
			<el-table-column prop="stockNum" label="库存结余"></el-table-column>
			<el-table-column prop="unitPrice" label="成本单价"></el-table-column>
			<el-table-column prop="allPrice" label="成本总价"></el-table-column>
			<el-table-column prop="handleId" label="经手人"></el-table-column>
			<el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
		</el-table>
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import StockApi from "@/api/stock.js";
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
			};
		},
		created() {
			/*获取列表*/
			// this.getData();
			this.getselect();
			this.updateDateRange();
		},
		computed: {},
		methods: {
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
				StockApi.stockStockRecordList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.stock.records;
						self.depotList = res.data.depot;
						self.storeList = res.data.store;
						self.totalDataNumber = res.data.stock.total;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClicks() {
				this.businessType = 5;
				this.open_add = true;
				this.addForm = this.otherData;
			},
			addClick() {
				this.businessType = 6;
				this.open_add = true;
				this.addForm = this.otherData;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
				this.openDetail = true;
			},
			copyClick(row) {
				this.openDetail = false;
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
			},
			detailClick(row) {
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
				if (f == "logistic") {
					this.open_logistic = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
			},

			/*订单详情*/
			orderClick(row) {
				this.$router.push({
					path: "/product/comment/Order",
					query: {
						order_id: row.orderId,
					},
				});
			},
			/*删除*/
			delClick(row) {
				let self = this;
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				ElMessageBox.prompt("异常原因", "提交到异常", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						inputType: "textarea", // 将输入框类型设置为文本域
						inputPlaceholder: "请输入异常原因",
						inputPattern: /.+/,
						inputErrorMessage: "异常原因不能为空",
						roundButton: true,
					})
					.then(({
						value
					}) => {
						const billIds = this.selectedRows.map((row) => row.billId).join(",");
						if (value) {
							self.loading = true;
							PurchaseApi.pickingOrderClose({
										billId: billIds,
										anomalousReason: value, // 将输入的关闭原因传递给接口
									},
									true
								)
								.then((data) => {
									ElMessage({
										message: "提交成功",
										type: "success",
									});
									self.getData();
								})
								.catch((error) => {
									self.loading = false;
									self.getData();
								});
						}
					})
					.catch(() => {});
			},
			authClick() {
				let self = this;
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				console.log(self.selectedRows, "selectedRows");
				ElMessageBox.confirm("确认要为该订单配货吗?", "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						const billIds = this.selectedRows.map((row) => row.billId).join(",");
						self.loading = true;
						MarketApi.pickingOrderPicking({
									billId: billIds,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "配货成功",
										type: "success",
									});
									self.getData();
								} else {
									self.loading = false;
								}
							})
							.catch((error) => {
								self.loading = false;
							});
					})
					.catch(() => {
						self.loading = false;
					});
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
			/*切换选项卡*/
			handleClick(tab) {
				let self = this;
				self.curPage = 1;
				// self.loading = true;
				self.status = tab;
				// self.getData();
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