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
				<el-form-item label="单据编号">
					<el-input size="small" v-model="searchForm.codeNum" placeholder="请输入单据编号"></el-input>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="经手人">
					<el-input size="small" v-model="searchForm.codeNum" placeholder=""></el-input>
				</el-form-item>
				<el-form-item label="备注">
					<el-input size="small" v-model="searchForm.remark" placeholder=""></el-input>
				</el-form-item>

				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" @click="addClick"
					v-auth="'/product/base/unit/add'">直接入库</el-button>
				<el-button size="small" icon="Printer" @click="printClick"
					v-auth="'/product/base/unit/delete'">打印入库单</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" @row-click="handleRowClick" v-loading="loading" show-summary
			:summary-method="getSummarys">
			<el-table-column prop="#" label="序号" width="60">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<el-table-column type="selection" width="55"></el-table-column>
			<!-- <el-table-column
            prop="billId"
            label="ID"
            width="55"
          ></el-table-column> -->

			<el-table-column  fixed="right" label="操作" width="80">
				<template #default="scope">
					<el-button @click="detailClick(scope.row)" type="primary" link size="small">详情</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="codeNum" label="单据编号" width="120"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="depotName" label="单据类型" width="100"></el-table-column>
			<el-table-column prop="depotName" label="仓库" width="100"></el-table-column>
			<el-table-column prop="typeName" label="业务原因" show-overflow-tooltip></el-table-column>
			<el-table-column prop="expressId" label="快递" width="160">
				<template #header>
					<div>
						<span>快递</span>
						<el-icon style="margin-left: 10px">
							<EditPen />
						</el-icon>
					</div>
				</template>
				<template #default="{ row }">
					<div class="express-wrapper">
						<el-select :ref="'expressSelect' + row.billId" v-if="row.editingExpress" v-model="row.expressId"
							placeholder="请选择" @change="handleExpressChange(row)" @blur="handleBlur(row)">
							<el-option v-for="(item, index) in otherData.express" :key="index" :label="item.expressName"
								:value="item.expressId" />
						</el-select>
						<div class="express-display" v-else-if="row.deliveryList.length == 0 && row.expressId == null"
							@click="editExpressId(row)">
							{{ "" }}
						</div>
						<div v-else class="express-display" @click="editExpressId(row)">
							{{
                row.expressId != null
                  ? getExpressName(row.expressId)
                  : getExpressName(row.deliveryList[0].expressId)
              }}
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="operNumber" label="商品总数"></el-table-column>
			<el-table-column prop="customerId" label="商品总额"></el-table-column>
			<el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" show-overflow-tooltip></el-table-column>
			<el-table-column prop="creatorId" label="创建人"></el-table-column>
			<el-table-column prop="creatorId" label="经手人"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" show-overflow-tooltip></el-table-column>
		</el-table>
	</div>
	<div class="product-content">
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :addForm="addForm"
			:openDetail="openDetail" @closeDialog="closeDialogFunc($event, 'edit')"></Edit>
		<Add v-if="open_add" :open_add="open_add" :addForm="addForm" :businessType="businessType"
			@closeDialog="closeDialogFunc($event, 'add')"></Add>
	</div>
	<!-- </div> -->
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
				open_express: false,
				open_expressno: false,
				printData: {},
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					operTimes: "1",
					name: "",
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
			handleBlur(row) {
				row.editingExpress = false;
			},
			editExpressId(row) {
				row.editingExpress = true;
				this.$nextTick(() => {
					const selectElement = this.$refs["expressSelect" + row.billId];
					if (selectElement) {
						selectElement.focus();
					}
				});
			},
			getExpressName(expressId) {
				const express = this.otherData.express.find(
					(item) => item.expressId === expressId
				);
				console.log(expressId, "express");
				return express ? express.expressName : "";
			},
			handleExpressChange(row) {
				localStorage.setItem("expressId", row.expressId);
				this.getselects(row);
			},
			getselects(row) {
				let params = {};
				params.billId = row.billId;
				params.expressId = localStorage.getItem("expressId");
				params.expressNo = row.expressNo;
				StockApi.stockInEditDeliver(params, true)
					.then((res) => {
						ElMessage({
							message: "修改成功",
							type: "success",
						});
						this.open_express = false;
						this.open_expressno = false;
						localStorage.removeItem("expressId");
						this.getData();
					})
					.catch((e) => {
						console.log(e);
					});
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
				StockApi.stockAllotOtherInList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
						// self.openTitle = self.tableData[0].typeName;
						// self.billItemList = self.tableData.flatMap((item) => {
						//   return item.billItemList;
						// });
						// console.log("billItemList", self.billItemList);
						self.otherData = res.data;
						self.storeList = res.data.store;
						self.depotList = res.data.depot;
						self.suppliers = res.data.supplier;
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
	.express-select {
		width: 100%;
	}

	.express-display {
		display: inline-block;
		cursor: pointer;
		width: 100%;
		min-height: 30px;
		/* 添加一个最小高度 */
		padding: 4px 8px;
		/* 内边距确保点击区域 */
		box-sizing: border-box;
		/* 包括内边距和边框 */
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