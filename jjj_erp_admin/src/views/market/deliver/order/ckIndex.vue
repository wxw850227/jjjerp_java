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
						<el-date-picker size="small" v-model="searchForm.time" type="daterange"
							value-format="YYYY-MM-DD" range-separator="~" start-placeholder="开始日期"
							end-placeholder="结束日期"></el-date-picker>
					</div>
				</el-form-item>
				<el-form-item label="店铺名称">
					<el-select size="small" v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
							:value="item.storeId" />
					</el-select>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select size="small" v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="选择客户">
					<el-select size="small" v-model="searchForm.customerId" @change="fetchAddressList"
						placeholder="请选择">
						<el-option v-for="(item, index) in customerList" :key="index" :label="item.realName"
							:value="item.customerId" />
					</el-select>
				</el-form-item>
				<el-form-item label="单据">
					<el-select size="small" v-model="searchForm.billType" placeholder="请选择">
						<el-option label="采购订单" :value="1"></el-option>
						<el-option label="采购入库单" :value="2"></el-option>
						<el-option label="采购退货订单" :value="3"></el-option>
						<el-option label="采购退货单" :value="4"></el-option>
						<el-option label="线上销售单" :value="5"></el-option>
						<el-option label="线下销售单" :value="6"></el-option>
						<el-option label="预售销售单" :value="7"></el-option>
						<el-option label="销售出库单" :value="8"></el-option>
						<el-option label="线下出库单" :value="9"></el-option>
						<el-option label="库存调拨单" :value="10"></el-option>
						<el-option label="调拨出库单" :value="11"></el-option>
						<el-option label="调拨入库单" :value="12"></el-option>
						<el-option label="其他出库单" :value="13"></el-option>
						<el-option label="其他入库单" :value="14"></el-option>
						<el-option label="盘盈盘亏单" :value="15"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="订单号">
					<el-input size="small" v-model="searchForm.linkBillNum" placeholder="请输入订单号"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
					<el-button size="small"
						>重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClick"
					v-auth="'/product/base/unit/add'">添加线下出库单</el-button><el-button size="small" icon="Printer"
					@click="printClick" v-auth="'/product/base/unit/delete'">打印出库单</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--内容-->
		<el-table size="small" class="flex-1" :data="tableData" border stripe style="width: 100%"
			@selection-change="handleSelectionChange" @row-click="handleRowClick" v-loading="loading" show-summary
			:summary-method="getSummarys">
			<el-table-column type="selection" width="54"></el-table-column>
			<el-table-column prop="billId" label="ID" width="42"></el-table-column>

			<el-table-column  fixed="right" label="操作">
				<template #default="scope">
					<el-button @click="editClick(scope.row)" type="primary" link size="small">详情</el-button>
				</template>
			</el-table-column>
			<!-- <el-table-column
            prop="otherUnit"
            label="打印标记"
            width="100"
          ></el-table-column> -->
			

			<el-table-column prop="codeNum" label="单据编号" width="120" show-overflow-tooltip></el-table-column>
			<el-table-column prop="typeName" label="单据类型"></el-table-column>
			<el-table-column prop="depotName" label="仓库" width="100"></el-table-column>
			<el-table-column prop="customerId" label="买家ID"></el-table-column>
			<el-table-column prop="customerId" label="客户"></el-table-column>
			<el-table-column prop="totalPrice" label="单据总金额"></el-table-column>
			<el-table-column prop="deptMoney" label="买家欠款" width="100"></el-table-column>
			<el-table-column prop="purchaseMoney" label="运费收入" width="100"></el-table-column>
			<!-- <el-table-column
            prop="depotName"
            label="服务费"
            width="100"
          ></el-table-column> -->

			<el-table-column prop="discountMoney" label="优惠金额" width="100"></el-table-column>
			<el-table-column prop="totalPrice" label="商品成本" width="100"></el-table-column>
			<!-- <el-table-column prop="discountMoney" label="毛利"></el-table-column> -->
			<el-table-column prop="storeName" label="店铺"></el-table-column>
			<el-table-column prop="linkBillNum" label="订单号" width="160"></el-table-column>
			<!-- <el-table-column prop="amount1" label="系统单号"></el-table-column> -->
			<el-table-column prop="salesId" label="业务员"></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" width="140"></el-table-column>
			<el-table-column prop="orderTime" label="打单时间" width="140"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="140"></el-table-column>
			<el-table-column prop="remark" label="备注"></el-table-column>
		</el-table>
	</div>

	<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :addForm="addForm" :openDetail="openDetail"
		@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
	<Print :open_title="openTitle" v-if="open_print" :open_print="open_print" :printData="printData" :addForm="addForm"
		@closeDialog="closeDialogFunc($event, 'print')"></Print>
	<Add v-if="open_add" :open_add="open_add" :addForm="addForm" :customerList="customerList"
		@closeDialogs="closeDialogFunc($event, 'add')"></Add>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import MarketApi from "@/api/market.js";
	import Edit from "./detail.vue";
	import Print from "./part/Print.vue";
	import Add from "./part/add.vue";
	import SettingApi from "@/api/setting.js";
	export default {
		components: {
			Edit,
			Print,
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
					operTimes: "",
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
			getData() {
				let self = this;
				self.loading = true;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				MarketApi.deliverOrderCkIndex(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.bill.records;
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
				this.openDetail = true;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
				this.openDetail = true;
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