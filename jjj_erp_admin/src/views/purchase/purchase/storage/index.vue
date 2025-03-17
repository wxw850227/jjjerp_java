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
				<el-button size="small" type="primary" icon="Plus" @click="addClick"
					v-auth="'/product/base/unit/add'">直接入库</el-button><el-button size="small" icon="Plus"
					@click="procureAddClick" v-auth="'/product/base/unit/delete'">采购入库</el-button><el-button
					size="small" icon="Download" @click="openClick()"
					v-auth="'/product/base/unit/setStatus'">导出</el-button><el-button size="small" icon="Printer"
					@click="printClick()" v-auth="'/product/base/unit/setStatus'">打印采购单</el-button>
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
					<div class="d-c-c" style="white-space: nowrap">
						<el-button v-if="scope.row.businessType == 1" @click="detailClicks(scope.row)" type="primary"
							link size="small">详情</el-button>
						<el-button v-if="scope.row.businessType == 2" @click="detailClick(scope.row)" type="primary"
							link size="small">详情</el-button>
						<el-dropdown size="small">
							<el-button type="primary" link size="small">更多<el-icon>
									<ArrowDown />
								</el-icon></el-button>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="editClicks(scope.row)"
										v-if="scope.row.businessType == 1">修改</el-dropdown-item>
									<el-dropdown-item v-if="
                      scope.row.businessType == 2 && scope.row.auditStatus == 10
                    " divided @click="editClick(scope.row)">修改</el-dropdown-item>
									<el-dropdown-item divided @click="delClick(scope.row)"
										v-if="scope.row.auditStatus == 10">关闭</el-dropdown-item>
									<el-dropdown-item divided @click="authClick(scope.row)"
										v-if="scope.row.auditStatus == 10">审核</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</template>
			</el-table-column>
			
			<el-table-column prop="businessType" label="业务类型" width="100">
				<template #default="scope">
					<el-text effect="plain" type="danger" size="small"
						v-if="scope.row.businessType == 1">直接采购入库</el-text>
					<el-text effect="plain" type="success" size="small"
						v-if="scope.row.businessType == 2">订单采购入库</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="codeNum" label="单据编号" show-overflow-tooltip></el-table-column>
			<el-table-column prop="settlement_status" label="结算状态" width="100">
				<template #default="scope">
					<el-text effect="plain" type="warning" size="small"
						v-if="scope.row.settlementStatus == 0">待结算</el-text>
					<el-text effect="plain" type="success" size="small"
						v-if="scope.row.settlementStatus == 1">已结算</el-text>
					<el-text effect="plain" type="danger" size="small"
						v-if="scope.row.settlementStatus == 2">已结清</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="auditStatus" label="审核状态" width="100">
				<template #default="scope">
					<el-text effect="plain" type="warning" size="small" v-if="scope.row.auditStatus == 10">待审核</el-text>
					<el-text effect="plain" type="success" size="small" v-if="scope.row.auditStatus == 20">通过</el-text>
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.auditStatus == 30">拒绝</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="stateText" label="订单状态" width="100"><template #default="scope">
					<el-text effect="plain" type="warning" size="small"
						v-if="scope.row.stateText == '待审核'">待审核</el-text>
					<el-text effect="plain" type="success" size="small" v-if="scope.row.stateText == '通过'">通过</el-text>
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.stateText == '拒绝'">拒绝</el-text>
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.stateText == '已关闭'">已关闭
					</el-text>
				</template></el-table-column>
			<el-table-column prop="depotName" label="仓库" width="100"></el-table-column>
			<el-table-column prop="supplierName" label="供应商" width="100"></el-table-column>
			<el-table-column prop="salesName" label="业务员" width="100"></el-table-column>
			<el-table-column prop="totalNum" label="商品总量" width="100"></el-table-column>
			<el-table-column prop="totalPrice" label="商品总额" width="100"></el-table-column>
			<el-table-column prop="discountMoney" label="优惠金额" width="100"></el-table-column>
			<el-table-column prop="purchaseMoney" label="采购运费" width="100"></el-table-column>
			<el-table-column show-overflow-tooltip prop="linkBillNum" label="采购订单号" width="160">
			</el-table-column>
			<el-table-column prop="systemRemark" label="系统备注" show-overflow-tooltip></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" width="120" show-overflow-tooltip></el-table-column>
			<el-table-column prop="creatorName" label="操作员"></el-table-column>
			<el-table-column prop="auditName" label="审核员"></el-table-column>
			<el-table-column prop="updateTime" label="最后修改时间" width="120" show-overflow-tooltip></el-table-column>
		</el-table>
		<Add v-if="open_add" :open_add="open_add" :addForm="addForm" :businessType="businessType"
			@closeDialog="closeDialogFunc($event, 'add')"></Add>
		<procureAdd v-if="open_procureAdd" :open_procureAdd="open_procureAdd" :addForm="addForm"
			:businessType="businessType" @closeDialog="closeDialogFunc($event, 'procureAdd')"></procureAdd>
		<Edit v-if="open_edit" :open_edit="open_edit" :addForm="addForm" :editForm="categoryModel"
			:businessType="businessType" :openDetail="openDetail" @closeDialog="closeDialogFunc($event, 'edit')">
		</Edit>
		<Print open_title="xxx公司" v-if="open_print" :open_print="open_print" :printData="printData" :addForm="addForm"
			@closeDialog="closeDialogFunc($event, 'print')"></Print>
	</div>
</template>

<script>
	// import PorductApi from "@/api/product.js";
	import PurchaseApi from "@/api/purchase.js";
	import Add from "./add.vue";
	import procureAdd from "./procureAdd.vue";
	import Edit from "./part/edit.vue";
	import Print from "./part/Print.vue";
	// import Logictic from "./logictic.vue";
	export default {
		components: {
			Add,
			procureAdd,
			Print,
			// Logictic,
			Edit,
		},
		data() {
			return {
				loading: false,
				print_id: "",
				open_print: false,
				activeName: "detail",
				num: 0,
				/*列表数据*/
				tableData: [],
				printData: {},
				selectedRows: [], // 选中的行数据
				/*一页多少条*/
				pageSize: 20,
				/*总条数*/
				totalDataNumber: 0,
				addForm: {},
				open_add: false,
				open_procureAdd: false,
				open_edit: false,
				open_logistic: false,
				openDetail: false,
				businessType: "",
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
			};
		},
		created() {
			/*获取列表*/
			this.getData();
		},
		methods: {
			logisticsClick() {
				this.open_logistic = true;
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
						column.property == "totalPrice" ||
						column.property == "availableNumber"
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
						column.property == "amount1" ||
						column.property == "amount2" ||
						column.property == "amount3"
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
				PurchaseApi.storageList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
						self.otherData = res.data;
						self.totalDataNumber = res.data.bill.total;
						self.num = res.data.num;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClick() {
				this.open_add = true;
				this.businessType = 1;
				this.addForm = this.otherData;
			},
			procureAddClick() {
				this.open_procureAdd = true;
				this.businessType = 2;
				this.addForm = this.otherData;
			},
			editClicks(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.businessType = 1;
				this.addForm = this.otherData;
				this.openDetail = false;
			},
			detailClicks(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.businessType = 1;
				this.openDetail = true;
				this.addForm = this.otherData;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.businessType = 2;
				this.addForm = this.otherData;
				this.openDetail = false;
			},
			detailClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.businessType = 2;
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
				if (f == "procureAdd") {
					this.open_procureAdd = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "print") {
					this.open_print = e.openDialog;
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
				// 弹出确认框，供用户选择同意或拒绝
				ElMessageBox.confirm("确定要审核该订单吗？", "审核入库单", {
						distinguishCancelAndClose: true,
						confirmButtonText: "同意",
						cancelButtonText: "拒绝",
						type: "warning",
					})
					.then(() => {
						// 用户选择同意，执行审核操作
						self.loading = true;
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
						if (action === "cancel") {
							// 用户选择拒绝，弹出输入拒绝原因的对话框
							ElMessageBox.prompt("拒绝原因", "审核入库单", {
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
										PurchaseApi.storageAudit({
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
			/*关闭*/
			delClick(row) {
				let self = this;
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
							PurchaseApi.storageClose({
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
					.catch(() => {});
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