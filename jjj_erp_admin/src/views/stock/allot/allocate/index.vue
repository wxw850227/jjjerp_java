<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item style="margin-right: 20px" label="业务日期">
					<el-radio-group v-model="searchForm.operTimes" size="small">
						<el-radio label="1">今天</el-radio>
						<el-radio label="2">近七天</el-radio>
						<el-radio label="3">近30天</el-radio>
						<el-radio label="4">自定义时间</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="" v-if="searchForm.operTimes == 4">
					<div class="block">
						<span class="demonstration"></span>
						<el-date-picker size="small" v-model="searchForm.operTime" type="daterange"
							value-format="YYYY-MM-DD" range-separator="~" start-placeholder="开始日期"
							end-placeholder="结束日期"></el-date-picker>
					</div>
				</el-form-item>

				<el-form-item label="调出仓库">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="调入仓库">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="单据编号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder="请输入单据编号"></el-input>
				</el-form-item>
				<el-form-item label="调拨类型">
					<el-select size="small" v-model="searchForm.billType" placeholder="请选择">
						<el-option label="待审核" :value="1"></el-option>
						<el-option label="带处理" :value="2"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="调拨状态">
					<el-select size="small" v-model="searchForm.billType" placeholder="请选择">
						<el-option label="待审核" :value="1"></el-option>
						<el-option label="带处理" :value="2"></el-option>
						<el-option label="处理中" :value="3"></el-option>
						<el-option label="已关闭" :value="4"></el-option>
						<el-option label="已完成" :value="5"></el-option>
						<el-option label="审核中" :value="6"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClick"
					v-auth="'/product/base/unit/add'">虚拟调拨</el-button>
				<el-button size="small" icon="Plus" @click="addClicks"
					v-auth="'/product/base/unit/add'">实际调拨</el-button>
				<el-button size="small" icon="Printer" @click="printClick"
					v-auth="'/product/base/unit/delete'">打印出库单</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<el-table size="small" class="flex-1" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" @row-click="handleRowClick" v-loading="loading" show-summary
			:summary-method="getSummarys">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="billId" label="ID" width="55"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<div style="white-space: nowrap" class="d-c-c">
						<el-button @click="detailClick(scope.row)" type="primary" link size="small">详情</el-button>
						<el-dropdown size="small">
							<el-button type="primary" link size="small">更多<el-icon>
									<ArrowDown />
								</el-icon></el-button>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="copyClick(scope.row)">复制</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</template>
			</el-table-column>

			<el-table-column prop="businessTime" label="业务日期" width="130"></el-table-column>
			<el-table-column prop="businessType" label="调拨类型">
				<template #default="scope">
					<span v-if="scope.row.businessType == 5">实际调拨</span>
					<span v-if="scope.row.businessType == 6">虚拟调拨</span>
				</template>
			</el-table-column>
			<el-table-column prop="codeNum" label="单据编号" width="150"></el-table-column>
			<el-table-column prop="typeName" label="单据类型"></el-table-column>
			<el-table-column prop="stateText" label="状态"> </el-table-column>

			<el-table-column prop="operNumber" label="商品总数"></el-table-column>

			<el-table-column prop="depotName" label="调出仓库" width="100"></el-table-column>
			<el-table-column prop="depotName" label="调入仓库" width="100"></el-table-column>
			<el-table-column prop="remark" label="备注"></el-table-column>

			<el-table-column prop="customerId" label="差异数量"></el-table-column>
			<el-table-column prop="creatorId" label="创建人"></el-table-column>
			<el-table-column prop="createTime" label="创建时间"></el-table-column>
		</el-table>
	</div>
	<!-- <div
    class="product-list"
    style="background-color: white; padding: 20px; margin-top: 20px"
  >-->
	<div class="product-content">
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :addForm="addForm"
			:openDetail="openDetail" @closeDialog="closeDialogFunc($event, 'edit')"></Edit>
		<Add v-if="open_add" :open_add="open_add" :addForm="addForm" :businessType="businessType"
			@closeDialog="closeDialogFunc($event, 'add')"></Add>
	</div>
	<!--</div> -->
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import StockApi from "@/api/stock.js";
	import Edit from "./edit.vue";
	// import Print from "./part/Print.vue";
	import Add from "./add.vue";
	import SettingApi from "@/api/setting.js";
	export default {
		components: {
			Edit,
			// Print,
			Add,
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
					operTimes: "1",
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
				openDetail: false,
			};
		},
		created() {
			/*获取列表*/
			this.getData();
			this.getselect();
		},
		computed: {
			filteredBillItems() {
				if (this.selectedBillId === null) {
					return [];
				}
				return this.billItemList.filter(
					(item) => item.headerId == this.selectedBillId
				);
			},
		},
		methods: {
			getselect() {
				SettingApi.userList({})
					.then((res) => {
						this.customerList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
					});
			},
			handleRowClick(row) {
				console.log(row, "row");
				this.selectedBillId = row.billId;
			},
			logisticsClick(row) {
				this.open_logistic = true;
				this.categoryModel = row;
			},
			printClick() {
				if (this.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据进行打印");
					return;
				}
				this.printData = this.selectedRows;
				this.addForm = this.otherData;
				this.open_print = true;
			},
			getSummarys({
				columns,
				data
			}) {
				let sums = [];
				// 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
				sums.push("总计");
				columns.forEach((column, index) => {
					if (
						column.property == "purchaseMoney" ||
						column.property == "otherMoney" ||
						column.property == "deposit"
					) {
						const values = data.map((item) => Number(item[column.property]));
						const sum = values.reduce((prev, curr) => prev + curr, 0);
						sums[index] = sum; // 将合计值添加到新数组中
					} else {
						sums.push(""); // 其他列的合计值为空
					}
				});

				return sums;
			},
			getSummary({
				columns,
				data
			}) {
				let sums = [];
				// 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
				sums.push("合计");
				columns.forEach((column, index) => {
					if (
						column.property == "operNumber" ||
						column.property == "basicNumber" ||
						column.property == "allPrice"
					) {
						const values = data.map((item) => Number(item[column.property]));
						const sum = values.reduce((prev, curr) => prev + curr, 0);
						sums[index] = sum; // 将合计值添加到新数组中
					} else {
						sums.push(""); // 其他列的合计值为空
					}
				});

				return sums;
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
			getData(param = "") {
				let self = this;
				let Params = {};
				Params.status = self.status;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				if (param != "") {
					Params.score = param.score;
					Params.name = param.name;
				}
				StockApi.stockAllotList(Params, true)
					.then((res) => {
						self.loading = false;
						self.otherData = res.data;
						self.storeList = res.data.store;
						self.depotList = res.data.depot;
						self.suppliers = res.data.supplier;
						self.tableData = res.data.bill.records;
						self.openTitle = self.tableData[0].typeName;
						// self.billItemList = self.tableData.flatMap((item) => {
						//   return item.billItemList;
						// });
						console.log("billItemList", self.billItemList);

						console.log("depotList", self.depotList);
						this.billItemList.forEach((item) => {
							// 找到对应的订单
							const order = this.tableData.find(
								(order) => order.billId === item.billId
							);
						});
						self.totalDataNumber = res.data.bill.total;
						self.num = res.data.num;
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
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				Params.status = self.status;
				PorductApi.getCommentList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.comments.records;
						self.totalDataNumber = res.data.comments.total;
					})
					.catch((error) => {
						self.loading = false;
					});
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