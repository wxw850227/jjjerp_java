<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="店铺">
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
				<el-form-item label="快递单号">
					<el-input size="small" v-model="searchForm.expressNo" placeholder="请输入快递单号"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" @click="restoreClick"
					v-auth="'/product/base/unit/add'">恢复正常</el-button>
					<el-button size="small"
					@click="closeClick" v-auth="'/product/base/unit/delete'">关闭异常</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--内容-->
		<el-table size="small" class="flex-1" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" @row-click="handleRowClick" v-loading="loading" show-summary
			:summary-method="getSummarys">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="billId" label="ID" width="55"></el-table-column>

			<el-table-column prop="typeName" label="订单提醒" width="100"></el-table-column>
			<!-- <el-table-column
            prop="otherUnit"
            label="打印状态"
            width="100"
          ></el-table-column> -->
			<!-- <el-table-column prop="codeNum" label="称重"></el-table-column> -->
			<el-table-column prop="storeId" label="店铺"></el-table-column>
			<el-table-column prop="depotName" label="仓库"></el-table-column>
			<!-- <el-table-column prop="codeNum" label="线上状态"></el-table-column> -->
			<!-- <el-table-column
            prop="codeNum"
            label="系统状态"
          ></el-table-column> -->
			<!-- <el-table-column
            prop="depotName"
            label="系统单号"
            width="100"
          ></el-table-column> -->
			<el-table-column prop="linkBillNum" label="订单号" width="100"></el-table-column>
			<!-- <el-table-column
            prop="depotName"
            label="标记"
            width="100"
          ></el-table-column> -->
			<!-- <el-table-column
            prop="depotName"
            label="挂起原因"
            width="100"
          ></el-table-column> -->
			<!-- <el-table-column
            prop="depotName"
            label="买家留言"
            width="100"
          ></el-table-column> -->
			<el-table-column prop="remark" label="备注" width="100"></el-table-column>
			<el-table-column prop="deliveryList[0].expressName" label="快递" width="100"></el-table-column>
			<el-table-column prop="deliveryList[0].expressNo" label="快递单号" width="100"></el-table-column>
			<el-table-column prop="deliveryType" label="配送方式" width="100">
				<template #default="scope">
					<el-tag effect="plain" type="warning" v-if="scope.row.deliveryType == 10">快递发货</el-tag>
					<el-tag effect="plain" type="success" v-if="scope.row.deliveryType == 20">上门自提</el-tag>
					<el-tag effect="plain" type="danger" v-if="scope.row.deliveryType == 30">无需物流</el-tag>
				</template>
			</el-table-column>

			<el-table-column prop="totalPrice" label="应收款" width="100"></el-table-column>
			<el-table-column prop="totalPrice" label="买家实付"></el-table-column>
			<el-table-column prop="discountMoney" label="优惠"></el-table-column>
			<el-table-column prop="purchaseMoney" label="运费"></el-table-column>
			<!-- <el-table-column prop="discountMoney" label="仓库成本"></el-table-column> -->
			<!-- <el-table-column prop="purchaseMoney" label="服务费"></el-table-column>
          <el-table-column prop="purchaseMoney" label="快递成本"></el-table-column>
          <el-table-column prop="purchaseMoney" label="估重"></el-table-column>
          <el-table-column prop="purchaseMoney" label="体积"></el-table-column> -->
			<el-table-column prop="customerId" label="买家ID" width="100"></el-table-column>
			<el-table-column prop="customerId" label="收件人"></el-table-column>

			<el-table-column prop="customerAddressId" label="收货地址" width="100"></el-table-column>
			<el-table-column prop="phone" label="手机" width="100"></el-table-column>

			<!-- <el-table-column prop="amount1" label="下单距今"></el-table-column> -->
			<el-table-column prop="createTime" label="付款时间"></el-table-column>
			<el-table-column prop="orderTime" label="打单时间"></el-table-column>
			<!-- <el-table-column prop="name" label="发货时间"></el-table-column>
          <el-table-column prop="name" label="完成时间"></el-table-column> -->
			<!-- <el-table-column prop="storeId" label="波次号"></el-table-column> -->
			<!-- <el-table-column prop="orderTime" label="批次流水号"></el-table-column> -->
			<el-table-column prop="linkBillNum" label="配货单号"></el-table-column>
			<!-- <el-table-column
            prop="updateTime"
            label="原始平台"
          ></el-table-column> -->
		</el-table>
	</div>
	<Detail v-if="open_detail" :open_detail="open_detail" :editForm="filteredBillItems" :addForm="addressForm"
		:regions="regions" @closeDialog="closeDialogFunc($event, 'detail')"></Detail>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import MarketApi from "@/api/market.js";
	import Detail from "./part/detail.vue";
	export default {
		components: {
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
					score: 0,
					name: "",
				},
				addressForm: {},
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
			handleRowClick(row) {
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
				MarketApi.otherOrderAbnormalIndexr(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
						self.billItemList = self.tableData.flatMap((item) => {
							return item.billItemList;
						});
						console.log("billItemList", self.billItemList);
						self.otherData = res.data;
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
			closeClick(row) {
				let self = this;
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				ElMessageBox.prompt("关闭异常", "警告(关闭后将无法再恢复)", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						inputType: "textarea", // 将输入框类型设置为文本域
						inputPlaceholder: "请输入关闭异常原因",
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
							MarketApi.otherOrderClose({
										billId: billIds,
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
			restoreClick() {
				let self = this;
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				console.log(self.selectedRows, "selectedRows");
				ElMessageBox.confirm("确定要恢复正常并打回到审核环节吗?", "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						const billIds = this.selectedRows.map((row) => row.billId).join(",");
						self.loading = true;
						MarketApi.otherOrderRecover({
									billId: billIds,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "恢复成功",
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
				ElMessageBox.confirm("确认打回到审核环节吗?", "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						const billIds = this.selectedRows.map((row) => row.billId).join(",");
						self.loading = true;
						MarketApi.deliverOrderDeliver({
									billId: billIds,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "发货成功",
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