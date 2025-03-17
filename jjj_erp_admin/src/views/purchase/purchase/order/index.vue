<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="供应商">
					<el-select v-model="searchForm.supplierId" placeholder="请选择" class="form_content_width">
						<el-option v-for="(item, index) in suppliers" :key="index" :label="item.supplierName"
							:value="item.supplierId" />
					</el-select>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select v-model="searchForm.depotId" placeholder="请选择" class="form_content_width">
						<el-option v-for="(item, index) in depots" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="单据编号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder="请填写单据编号"
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" @click="addClick"
					v-auth="'/product/base/unit/add'">新增采购订单</el-button><el-button size="small" icon="Document-add"
					@click="" v-auth="'/product/base/unit/delete'">快速导入</el-button><el-button size="small"
					icon="Download" @click="" v-auth="'/product/base/unit/setStatus'">导出</el-button><el-button
					size="small" icon="Printer" @click="printClick()"
					v-auth="'/product/base/unit/setStatus'">打印采购单</el-button>
			</div>
			<!-- 分页 -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableData" border stripe style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading" show-summary :summary-method="getSummarys">
			<el-table-column type="selection" width="54"></el-table-column>
			<el-table-column prop="billId" label="ID" width="42"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<div  class="d-c-c" style="white-space: nowrap">
						<el-button @click="detailClick(scope.row)" type="primary" link size="small">详情</el-button>
						<el-dropdown size="small">
							<el-button type="primary" link size="small">更多<el-icon>
									<ArrowDown />
								</el-icon></el-button>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="editClick(scope.row)"
										v-if="scope.row.auditStatus == 10">修改</el-dropdown-item>
									<el-dropdown-item divided @click="detailedClick(scope.row)">采购明细</el-dropdown-item>
									<el-dropdown-item divided @click="delClick(scope.row)"
										v-if="scope.row.auditStatus == 10">关闭</el-dropdown-item>
									<el-dropdown-item divided @click="authClick(scope.row)"
										v-if="scope.row.auditStatus == 10">审核</el-dropdown-item>
									<el-dropdown-item divided @click="logisticsClick(scope.row)">物流单号</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</template>
			</el-table-column>		
			<el-table-column prop="codeNum" label="单据编号" show-overflow-tooltip></el-table-column>
			<el-table-column prop="supplierName" label="供应商" width="100"></el-table-column>
			<el-table-column prop="salesId" label="业务员" width="100"></el-table-column>
			<el-table-column prop="depotName" label="收货仓库" width="100"></el-table-column>
			<el-table-column prop="typeName" label="单据类型名称" width="100">
				<template #default="scope">
					<el-text effect="plain" type="primary" size="small">{{
            scope.row.typeName
          }}</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="auditStatus" label="审核状态" width="100">
				<template #default="scope">
					<el-text effect="plain" type="warning" size="small" v-if="scope.row.auditStatus == 10">待审核</el-text>
					<el-text effect="plain" type="success" size="small" v-if="scope.row.auditStatus == 20">通过</el-text>
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.auditStatus == 30">拒绝</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="stateText" label="状态" width="80">
				<template #default="scope">
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.purchaseStatus == 10">{{
              scope.row.stateText == "待审核" ? "待审核" : scope.row.stateText
            }}</el-text>
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.purchaseStatus == 0">{{
              scope.row.stateText == "未到货" ? "未到货" : scope.row.stateText
            }}</el-text>
					<el-text effect="plain" type="success" size="small"
						v-if="scope.row.purchaseStatus == 1">完成</el-text>
					<el-text effect="plain" type="info" size="small" v-if="scope.row.purchaseStatus == 2">{{
              scope.row.stateText == "部分到货"
                ? "部分到货"
                : scope.row.stateText
            }}</el-text><el-text effect="plain" type="warning" size="small" v-if="scope.row.purchaseStatus == 3">{{
              scope.row.stateText == "已关闭" ? "已关闭" : scope.row.stateText
            }}</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="totalPrice" label="商品总额" width="100"></el-table-column>
			<el-table-column prop="purchaseMoney" label="采购运费" width="100"></el-table-column>
			<el-table-column prop="otherMoney" label="其他费用"></el-table-column>
			<el-table-column prop="deposit" label="预付金额"></el-table-column>
			<el-table-column prop="expressNo" label="物流单号"></el-table-column>
			<el-table-column prop="systemRemark" label="备注" show-overflow-tooltip></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" show-overflow-tooltip></el-table-column>
			<el-table-column prop="creatorName" label="创建人"></el-table-column>
			<el-table-column prop="createTime" width="120" label="创建时间" show-overflow-tooltip></el-table-column>
			<el-table-column prop="updateTime" label="最后修改时间" width="120" show-overflow-tooltip></el-table-column>
		</el-table>
		<Add v-if="open_add" :open_add="open_add" :addForm="addForm" @closeDialog="closeDialogFunc($event, 'add')">
		</Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :addForm="addForm"
			:openDetail="openDetail" @closeDialog="closeDialogFunc($event, 'edit')"></Edit>
		<!--打印-->
		<Print open_title="xxx公司" v-if="open_print" :open_print="open_print" :printData="printData" :addForm="addForm"
			@closeDialog="closeDialogFunc($event, 'print')"></Print>
		<Logictic v-if="open_logistic" :open_logistic="open_logistic" :editForm="categoryModel"
			@closeDialog="closeDialogFunc($event, 'logistic')"></Logictic>
		<Detail v-if="open_detail" :open_detail="open_detail" :editForm="filteredBillItems" :addForm="addressForm"
			@closeDialog="closeDialogFunc($event, 'detail')"></Detail>
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import Add from "./add.vue";
	import Edit from "./edit.vue";
	import Print from "./part/Print.vue";
	import Logictic from "./logictic.vue";
	import Detail from "./part/detail.vue";
	export default {
		components: {
			Add,
			Print,
			Logictic,
			Edit,
			Detail,
		},
		data() {
			return {
				loading: true,
				open_print: false,
				activeName: "detail",
				open_detail: false,
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
				totalDataNumber: 0,
				open_add: false,
				open_edit: false,
				open_logistic: false,
				printData: {},
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					supplierId: "",
					codeNum: "",
					depotId: "",
				},
				/*评价等级*/
				commentList: [{
						name: "好评",
						val: 10,
					},
					{
						name: "中评",
						val: 20,
					},
					{
						name: "差评",
						val: 30,
					},
				],
				status: -1,
				depots: [],
				suppliers: [],
				openDetail: false,
				selectedBillId: null,
				open_delete: false,
				open_auth: false,
			};
		},
		created() {
			/*获取列表*/
			this.getData();
		},
		computed: {
			filteredBillItems() {
				if (this.selectedBillId === null) {
					return [];
				}
				return this.billItemList.filter(
					(item) => item.billId == this.selectedBillId
				);
			},
		},
		methods: {
			detailedClick(row) {
				this.selectedBillId = row.billId;
				this.open_detail = true;
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
					Params = self.searchForm;
					Params.pageIndex = self.curPage;
					Params.pageSize = self.pageSize;
				}
				PurchaseApi.orderList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
						self.billItemList = self.tableData.flatMap((item) => {
							return item.billItemList;
						});
						console.log("billItemList", self.billItemList);
						self.otherData = res.data;
						self.depots = res.data.depot;
						self.suppliers = res.data.supplier;
						this.billItemList.forEach((item) => {
							// 找到对应的订单
							const order = this.tableData.find(
								(order) => order.billId === item.billId
							);
							// if (order && order.fileUrls && order.fileUrls.length > 0) {
							//   // 假设只有一个图片，取第一个图片的URL赋给productExtend.imageUrl
							//   item.productExtend.imageUrl = order.fileUrls;
							// }
						});
						self.totalDataNumber = res.data.bill.total;
						self.num = res.data.num;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClick() {
				this.open_add = true;
				this.addForm = this.otherData;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
				this.openDetail = false;
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
				if (f == "detail") {
					this.open_detail = e.openDialog;
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
			authClick(row) {
				let self = this;
				self.open_auth = true;
				// 弹出确认框，供用户选择同意或拒绝
				ElMessageBox.confirm("确定要审核该订单吗？", "审核采购单", {
						distinguishCancelAndClose: true,
						confirmButtonText: "同意",
						cancelButtonText: "拒绝",
						type: "warning",
					})
					.then(() => {
						// 用户选择同意，执行审核操作
						self.loading = true;
						self.open_auth = false;
						PurchaseApi.orderAudit({
									billId: row.billId,
									auditStatus: 20,
									refuseReason: "", // 同意时不需要拒绝原因
								},
								true
							)
							.then((data) => {
								ElMessage({
									message: "审核成功",
									type: "success",
								});
								self.getData();
							})
							.catch((error) => {
								self.loading = false;
								self.getData();
							});
					})
					.catch((action) => {
						self.open_auth = false;
						console.log(action, "action");
						if (action === "cancel") {
							// 用户选择拒绝，弹出输入拒绝原因的对话框
							ElMessageBox.prompt("拒绝原因", "审核采购单", {
									confirmButtonText: "确定",
									cancelButtonText: "取消",
									inputType: "textarea", // 将输入框类型设置为文本域
									inputPlaceholder: "请输入拒绝原因",
									inputPattern: /.+/,
									inputErrorMessage: "拒绝原因不能为空",
									roundButton: true,
								})
								.then(({
									value
								}) => {
									// 用户输入拒绝原因并点击确定
									if (value) {
										self.loading = true;
										PurchaseApi.orderAudit({
													billId: row.billId,
													auditStatus: 30,
													refuseReason: value, // 将输入的拒绝原因传递给接口
												},
												true
											)
											.then((data) => {
												ElMessage({
													message: "拒绝成功",
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
						}
					});
			},
			/*删除*/
			delClick(row) {
				let self = this;
				self.open_delete = true;
				ElMessageBox.prompt("关闭原因", "关闭采购单", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						inputType: "textarea", // 将输入框类型设置为文本域
						inputPlaceholder: "请输入关闭原因",
						inputPattern: /.+/,
						inputErrorMessage: "关闭原因不能为空",
						roundButton: true,
					})
					.then(({
						value
					}) => {
						if (value) {
							self.loading = true;
							self.open_delete = false;
							PurchaseApi.orderClose({
										billId: row.billId,
										closeReason: value, // 将输入的关闭原因传递给接口
									},
									true
								)
								.then((data) => {
									ElMessage({
										message: "关闭成功",
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
					.catch(() => {
						self.open_delete = false;
					});
			},
			openClick() {
				let self = this;
				let confirmMessage = "";
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				console.log(self.selectedRows, "selectedRows");
				if (self.selectedRows[0].enabled) {
					confirmMessage = "确认要禁用吗？";
				} else {
					confirmMessage = "确定要开启吗？";
				}
				const state = this.selectedRows[0].enabled ? 0 : 1;
				ElMessageBox.confirm(confirmMessage, "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						const ids = this.selectedRows.map((row) => row.id).join(",");
						self.loading = true;
						PorductApi.unitSetStatus({
									ids: ids,
									state: state,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "修改成功",
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
				this.loading = true;
				let params = this.searchForm;
				this.getData(params);
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
	::v-deep .el-table__footer-wrapper {
		background-color: red !important;
		/* 设置合计行的背景色为红色 */
		color: white !important;
		/* 设置合计行文字颜色为白色 */
		font-weight: bold !important;
		/* 设置合计行文字加粗 */
	}
</style>