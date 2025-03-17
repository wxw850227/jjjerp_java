<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item style="margin-right: 20px" label="日期">
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
				<el-form-item label="客户">
					<el-select size="small" v-model="searchForm.customerId" placeholder="请选择">
						<el-option v-for="(item, index) in customerList" :key="index" :label="item.realName"
							:value="item.customerId" />
					</el-select>
				</el-form-item>
				<el-form-item label="单据类型">
					<el-select size="small" v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
							:value="item.storeId" />
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button size="small"
						@click="resetForm">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
			</div>
			<!-- 分页 -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column prop="#" label="序号" width="60">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>	
			<el-table-column prop="typeName" label="单据类型"> </el-table-column>
			<el-table-column prop="codeNum" label="单据编号" show-overflow-tooltip></el-table-column>
			<el-table-column prop="totalNum" label="商品数量"></el-table-column>
			<el-table-column prop="totalPrice" label="应收余额"></el-table-column>
			<el-table-column prop="receivedMoney" label="已收金额" show-overflow-tooltip></el-table-column>
			<el-table-column prop="balanceMoney" label="单据应收余额" show-overflow-tooltip></el-table-column>
			<el-table-column prop="businessTime" label="业务日期" show-overflow-tooltip></el-table-column>
			<el-table-column prop="systemRemark" label="备注" show-overflow-tooltip></el-table-column>
		</el-table>
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
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
					showProductDetails: false,
				},
				status: -1,
				depots: [],
				suppliers: [],
				selectedBillId: null,
				openTitle: "",
				customerList: [],
				accountList: [],
				depotList: [],
				storeList: [],
				depotIdList: [],
				openDetail: false,
			};
		},
		computed: {},
		created() {
			/*获取列表*/
			// this.getData();
			this.getselect();
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
						this.searchForm.customerId = this.customerList[0].customerId;
						this.updateDateRange();
						console.log(this.searchForm.customerId, "this.customerList");
					})
					.catch((e) => {
						console.log(e);
					});
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
						column.property == "totalNum" ||
						column.property == "totalPrice" ||
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
				FinanceApi.customerList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.records;
						self.totalDataNumber = res.data.total;
					})
					.catch((error) => {
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
					showProductDetails: false,
				};
				this.updateDateRange();
				this.getData();
			},
		},
	};
</script>

<style scoped></style>
