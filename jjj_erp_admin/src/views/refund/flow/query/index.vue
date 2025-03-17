<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="店铺名称">
					<el-select v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
							:value="item.storeId" class="form_content_width" />
					</el-select>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" class="form_content_width" />
					</el-select>
				</el-form-item>
				<el-form-item label="订单号">
					<el-input size="small" v-model="searchForm.linkBillNum" placeholder="请输入订单号"
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small">重置</el-button>
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
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading" show-summary :summary-method="getSummarys">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="billId" label="ID" width="55"></el-table-column>
			<el-table-column prop="typeName" label="提醒" width="100"></el-table-column>
			<el-table-column prop="codeNum" label="单据编号"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="refundType" label="售后类型">
				<template #default="{ row }">
					<el-text effect="plain" type="warning" size="small" v-if="row.refundType == 0">无售后</el-text>
					<el-text effect="plain" type="success" size="small" v-if="row.refundType == 10">退货退款</el-text>
					<el-text effect="plain" type="warning" size="small" v-if="row.refundType == 20">换货</el-text>
					<el-text effect="plain" type="success" size="small" v-if="row.refundType == 30">退款</el-text>
					<el-text effect="plain" type="warning" size="small" v-if="row.refundType == 40">补发</el-text>
					<el-text effect="plain" type="success" size="small" v-if="row.refundType == 50">维修</el-text>
					<el-text effect="plain" type="success" size="small" v-if="row.refundType == 60">其他</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="deposit" label="退款金额" width="100">
			</el-table-column>
			<el-table-column prop="problemCause" label="问题原因" width="120">
			</el-table-column>
			<el-table-column prop="problemDescribe" label="问题详情" width="100"></el-table-column>
			<el-table-column prop="problemDescribe" label="应退数量" width="100"></el-table-column>
			<el-table-column prop="customerId" label="买家ID" width="100"></el-table-column>
			<el-table-column prop="phone" label="电话" width="100"></el-table-column>
			<el-table-column label="快递" prop="deliveryList[0].expressName" width="100">
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<div style="white-space: nowrap">
						<el-button @click="detailClicks(scope.row)" type="primary" link size="small">详情</el-button>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="快递单号" prop="deliveryList[0].expressNo" width="100">
			</el-table-column>
			<el-table-column prop="customerId" label="申请人" width="100"></el-table-column>
			<el-table-column prop="customerId" label="收件人" width="100"></el-table-column>
			<el-table-column prop="customerAddressId" label="收货地址" show-overflow-tooltip></el-table-column>
			<el-table-column prop="createTime" label="创建时间"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="storeName" label="店铺"></el-table-column>
			<el-table-column prop="linkBillNum" label="系统订单号"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="totalPrice" label="售后单号"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="totalPrice" label="平台退款"></el-table-column>
			<el-table-column prop="totalPrice" label="应退"></el-table-column>
			<el-table-column prop="totalPrice" label="服务费"></el-table-column>
			<el-table-column prop="zip" label="邮费"></el-table-column>
			<el-table-column prop="depotId" label="退回仓库" width="120">
			</el-table-column>
			<el-table-column prop="payee" width="100" label="客户收款账户"></el-table-column>
			<el-table-column prop="accountId" label="赔付方"> </el-table-column>
			<el-table-column prop="compensateMoney" label="赔付金额">
			</el-table-column>
			<el-table-column prop="createTime" label="付款时间"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="createTime" label="退款时间"  show-overflow-tooltip></el-table-column>
		</el-table>
	</div>
	<Detail v-if="open_detail" :open_detail="open_detail" :editForm="filteredBillItems"
		:filteredBillItem="filteredBillItem" :addForm="addressForm" :regions="regions"
		@closeDialog="closeDialogFunc($event, 'detail')"></Detail>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import MarketApi from "@/api/market.js";
	import RefundApi from "@/api/refund.js";
	import Detail from "./detail.vue";
	export default {
		components: {
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
				express: [],
				storeList: [],
				depotList: [],
				deliveryMessage: [],
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
			};
		},
		created() {
			/*获取列表*/
			this.getData();
			// this.getselect();
		},
		computed: {
			filteredBillItems() {
				if (this.selectedBillId === null) {
					return [];
				}
				return this.billItemList.filter(
					(item) => item.billId == this.selectedBillId && item.isExchange == 0
				);
			},
			filteredBillItem() {
				if (this.selectedBillId === null) {
					return [];
				}
				return this.billItemList.filter(
					(item) => item.billId == this.selectedBillId && item.isExchange == 1
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
			editExpress() {
				this.open_express = true;
			},
			editExpressno() {
				this.open_expressno = true;
			},
			handleExpressChange(row) {
				localStorage.setItem("expressId", row.expressId);
			},
			handleDeliveryChange(row) {
				this.getselect(row);
			},
			getselect(row) {
				let params = {};
				params.billId = row.billId;
				params.expressId = localStorage.getItem("expressId");
				params.expressNo = row.expressNo;
				RefundApi.refundFlowEditDeliver(params, true)
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
			detailClicks(row) {
				console.log(row, "row");
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
					Params.score = param.score;
					Params.name = param.name;
				}
				RefundApi.refundFlowQueryList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
						self.billItemList = self.tableData.flatMap((item) => {
							return item.billItemList;
						});
						console.log("billItemList", self.billItemList);
						self.otherData = res.data;
						self.depotList = res.data.depot;
						self.storeList = res.data.store;
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

			/*搜索查询*/
			onSubmit() {
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