<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="店铺名称">
					<el-select v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
							:value="item.storeId" />
					</el-select>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="订单号">
					<el-input size="small" v-model="searchForm.linkBillNum" placeholder="请输入订单号"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button @click="reset"
						size="small">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" @click="authClick"
					v-auth="'/product/base/unit/add'">确认配货</el-button>
				<el-button size="small" @click="returnAuthClick" v-auth="'/product/base/unit/delete'">打回审核</el-button>
				<el-button size="small" @click="delClick" v-auth="'/product/base/unit/delete'">提交到异常</el-button>
				<el-button size="small" icon="Printer" @click="printClick"
					v-auth="'/product/base/unit/delete'">打印快递单</el-button>
			</div>
			<el-pagination small @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading" show-summary :summary-method="getSummarys">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="billId" label="ID" width="55"></el-table-column>

			<el-table-column fixed="right" label="操作">
				<template #default="scope">
					<el-button @click="detailClick(scope.row)" type="primary" link size="small">详情</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="typeName" label="提醒" width="100"></el-table-column>
			<el-table-column prop="availableAllNumber" label="商品总数"></el-table-column>
			<el-table-column prop="fileUrls" label="系统图片" >
				<template #default="scope">
					<el-image style="width: 26px; height: 26px;display: block;" :src="scope.row.fileUrls"
						:preview-src-list="[scope.row.fileUrls]"></el-image>
				</template>
				
			</el-table-column>
			<!-- <el-table-column prop="codeNum" label="商品概况"></el-table-column> -->
			<el-table-column prop="systemRemark" label="系统备注"></el-table-column>
			<el-table-column prop="depotName" label="仓库" width="100"></el-table-column>
			<el-table-column prop="deliveryType" label="配送方式" width="100">
				<template #default="scope">
					<el-text effect="plain" size="small" type="warning"
						v-if="scope.row.deliveryType == 10">快递发货</el-text>
					<el-text effect="plain" size="small" type="success"
						v-if="scope.row.deliveryType == 20">上门自提</el-text>
					<el-text effect="plain" type="danger" size="small"
						v-if="scope.row.deliveryType == 30">无需物流</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="depotName" label="快递" width="160">
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
			<el-table-column prop="deliveryList[0].expressNo" width="160">
				<template #header>
					<span>快递单号</span>
					<el-icon style="margin-left: 10px">
						<EditPen />
					</el-icon>
				</template>
				<template #default="{ row }">
					<div class="express-wrapper">
						<el-input v-if="row.editingExpressNo" ref="expressNoInput" @blur="handleDeliveryChange(row)"
							v-model="row.expressNo" />
						<div class="expressno-display" v-else-if="row.deliveryList.length == 0 && row.expressNo == ''"
							@click="editExpressNo(row)">
							{{ "" }}
						</div>
						<div v-else class="expressno-display" @click="editExpressNo(row)">
							{{ row.deliveryList[0].expressNo || "" }}
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="customerId" label="买家ID" width="100"></el-table-column>

			<el-table-column prop="customerAddressId" label="收货地址" width="100"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="totalPrice" label="买家实付" width="100"></el-table-column>
			<el-table-column prop="discountMoney" label="优惠"></el-table-column>
			<el-table-column prop="totalPrice" label="应收"></el-table-column>
			<el-table-column prop="purchaseMoney" label="运费"></el-table-column>
			<el-table-column prop="codeNum" label="订单号"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="codeNum" label="系统单号"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="storeName" label="店铺"></el-table-column>
			<el-table-column prop="orderTime" label="下单时间"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="createTime" label="付款时间"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="salesId" label="业务员" width="100"></el-table-column>
		</el-table>
	</div>
	<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :addForm="addForm"
		@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
	<Detail v-if="open_detail" :open_detail="open_detail" :editForm="filteredBillItems" :addForm="addressForm"
		:regions="regions" :selectedRows="dataList" @closeDialog="closeDialogFunc($event, 'detail')"></Detail>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import MarketApi from "@/api/market.js";
	import Detail from "./detail.vue";
	import Edit from "./edit.vue";
	export default {
		components: {
			Edit,
			Detail,
		},
		data() {
			return {
				loading: true,
				open_print: false,
				open_detail: false,
				activeName: "detail",
				num: 0,
				/*列表数据*/
				tableData: [],
				dataList: [],
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
				addressForm: {},
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					score: 0,
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
				open_express: false,
				open_expressno: false,
				express: [],
				depotList: [],
				storeList: [],
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
			addressForm() {
				if (this.selectedBillId == "") {
					return [];
				}
				if (
					!this.tableData.filter((item) => item.billId == this.selectedBillId)[0]
					.address
				) {
					return [];
				}
				return this.tableData.filter(
					(item) => item.billId == this.selectedBillId
				)[0].address;
			},
			regions() {
				if (this.selectedBillId == "") {
					return [];
				}
				if (
					!this.tableData.filter((item) => item.billId == this.selectedBillId)[0]
					.address
				) {
					return [];
				}
				this.addressForm = this.tableData.filter(
					(item) => item.billId == this.selectedBillId
				)[0].address;
				if (this.addressForm == "") {
					return [];
				} else {
					return (this.regions =
						this.addressForm.province +
						this.addressForm.city +
						this.addressForm.region);
				}
			},
		},
		methods: {
			handleBlur(row) {
				row.editingExpress = false;
			},
			handleExpressChange(row) {
				localStorage.setItem("expressId", row.expressId);
			},
			editExpressNo(row) {
				row.editingExpressNo = true;
				this.$nextTick(() => {
					const inputElement = this.$refs.expressNoInput;
					if (inputElement && inputElement.$el) {
						inputElement.$el.querySelector("input").focus();
					}
				});
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
			handleDeliveryChange(row) {
				row.editingExpressNo = false;
				this.getselect(row);
			},
			getselect(row) {
				let params = {};
				params.billId = row.billId;
				params.expressId = localStorage.getItem("expressId");
				params.expressNo = row.expressNo;
				MarketApi.editDeliver(params, true)
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
			detailClick(row) {
				console.log(row, "row");
				this.selectedBillId = row.billId;
				this.dataList = row;
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
			getData() {
				let self = this;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				MarketApi.pickingOrderIndex(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
						self.billItemList = self.tableData.flatMap((item) => {
							return item.billItemList;
						});
						console.log("billItemList", self.billItemList);
						self.otherData = res.data;
						self.depots = res.data.depot;
						self.storeList = res.data.store;
						self.depotList = res.data.depot;
						self.suppliers = res.data.supplier;

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
									self.loading = false;
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
			returnAuthClick() {
				let self = this;
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				console.log(self.selectedRows, "selectedRows");
				ElMessageBox.confirm("确定将当前订单打回审核吗?", "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						const billIds = this.selectedRows.map((row) => row.billId).join(",");
						self.loading = true;
						MarketApi.pickingOrderRepulseAudit({
									billId: billIds,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "成功打回",
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
			reset() {
				let self = this;
				self.loading = true;
				self.searchForm = {};
				self.getData();
			},
		},
	};
</script>

<style scoped>
	/* .express-select {
		width: 100%;
	}

	.express-display {
		display: inline-block;
		cursor: pointer;
		width: 100%;
		min-height: 30px;	
		padding: 4px 8px;	
		box-sizing: border-box;	
	}

	.expressno-display {
		display: inline-block;
		cursor: pointer;
		width: 100%;
		min-height: 30px;
		padding: 4px 8px;
		box-sizing: border-box;	
	} */

	/* .form-item-container {
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
	} */
</style>