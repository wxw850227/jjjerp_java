<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item style="margin-right: 20px" label="业务日期">
					<el-radio-group v-model="searchForm.operTimes" size="small" @change="updateDateRange">
						<el-radio label="2">近七天</el-radio>
						<el-radio label="3">近30天</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="起始日期">
					<el-date-picker v-model="searchForm.time[0]" type="date" @change="changeDateRange"
						placeholder="开始日期"></el-date-picker>
				</el-form-item>

				<el-form-item label="结束日期">
					<el-date-picker v-model="searchForm.time[1]" type="date" placeholder="结束日期"></el-date-picker>
				</el-form-item>
				<el-form-item label="账户名称">
					<el-select size="small" v-model="searchForm.accountId" placeholder="请选择">
						<el-option v-for="(item, index) in accountList" :key="index" :label="item.accountName"
							:value="item.accountId" />
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small" @click="resetForm">重置</el-button>
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
					<el-table-column prop="accountName" label="账户名称"  show-overflow-tooltip></el-table-column>
					<el-table-column prop="organName" label="机构名称"  show-overflow-tooltip>
					</el-table-column>
					<el-table-column prop="accountType" label="类型">
						<template #default="scope">
							<el-text effect="plain" size="small" type="warning"
								v-if="scope.row.accountType == 1">现金</el-text>
							<el-text effect="plain" size="small" type="success"
								v-if="scope.row.accountType == 2">银行</el-text>
							<el-text effect="plain" size="small" type="danger"
								v-if="scope.row.accountType == 3">第三方</el-text>
						</template>
					</el-table-column>
					<el-table-column prop="accountNumber" label="账户" show-overflow-tooltip></el-table-column>
					<el-table-column prop="openingMoney" label="期初余额"></el-table-column>
					<el-table-column prop="receipt" label="本期收款" show-overflow-tooltip></el-table-column>
					<el-table-column prop="payment" label="本期付款"></el-table-column>
					<el-table-column prop="change" label="本期变化金额"></el-table-column>
					<el-table-column prop="balance" label="期末余额"></el-table-column>
				</el-table>	
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
	import SettingApi from "@/api/setting.js";
	import {
		formatDate
	} from "@/utils/dateTimes.js";
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
					operTimes: "2",
					time: [null, null],
					name: "",
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
		created() {
			/*获取列表*/
			// this.getData();
			this.getselect();
		},
		computed: {},
		methods: {
			changeDateRange(val) {
				console.log(val, "val");
			},
			updateDateRange() {
				const today = new Date();
				if (this.searchForm.operTimes === "2") {
					const startOfSevenDaysAgo = new Date(today);
					startOfSevenDaysAgo.setDate(today.getDate() - 6);
					this.searchForm.time = [
						formatDate(startOfSevenDaysAgo, "YYYY-MM-DD"),
						formatDate(today, "YYYY-MM-DD"),
					];
				} else if (this.searchForm.operTimes === "3") {
					const startOfThirtyDaysAgo = new Date(today);
					startOfThirtyDaysAgo.setDate(today.getDate() - 29);
					this.searchForm.time = [
						formatDate(startOfThirtyDaysAgo, "YYYY-MM-DD"),
						formatDate(today, "YYYY-MM-DD"),
					];
				}
				this.onSubmit(); // 自动触发搜索
			},
			getselect() {
				FinanceApi.accountList({})
					.then((res) => {
						this.accountList = res.data.records;
						this.searchForm.accountId = this.accountList[0].accountId;
						this.updateDateRange();
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
				FinanceApi.collectList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.collect.records;
						self.accountList = res.data.account;
						self.totalDataNumber = res.data.collect.total;
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
					operTimes: "2",
					name: "",
					time: [null, null],
				};
				this.updateDateRange();
				this.getData();
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