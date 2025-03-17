<template>
	<div class="product bgset set-main">
		<!-- 搜索表单 -->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="状态">
					<el-select placeholder="请选择" v-model="searchForm.enabled" @change="handleStatusChange" size="small"
						class="form_content_width">
						<el-option label="启用" :value="1"></el-option>
						<el-option label="禁用" :value="0"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item>
					<span class="total-balance">
						当前余额合计￥<span class="total-balance-value">{{
              calculateTotalBalance()
            }}</span>
					</span>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClick"
					v-auth="'/product/base/unit/add'">新增账户</el-button>
				<el-button size="small" icon="Download" @click="onSubmit">导出</el-button>
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
			<el-table-column prop="accountId" label="ID" width="55"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<div style="white-space: nowrap" class="d-c-c">
						<el-button @click="editClick(scope.row)" type="primary" link size="small">编辑</el-button>
						<el-dropdown size="small">
							<el-button type="primary" link size="small">更多<el-icon>
									<ArrowDown />
								</el-icon></el-button>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="defaultClick(scope.row)">默认账户</el-dropdown-item>
									<el-dropdown-item divided @click="openClick(scope.row)"
										v-if="scope.row.enabled == 0">启用</el-dropdown-item>
									<el-dropdown-item divided @click="openClick(scope.row)"
										v-if="scope.row.enabled == 1">停用</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</template>
			</el-table-column>

			<el-table-column prop="accountName" label="账户名称"></el-table-column>
			<el-table-column prop="accountType" label="机构类型">
				<template #default="scope">
					<span v-if="scope.row.accountType == 1">现金</span>
					<span v-if="scope.row.accountType == 2">银行</span>
					<span v-if="scope.row.accountType == 3">第三方</span>
				</template>
			</el-table-column>
			<el-table-column prop="organName" label="机构名称"></el-table-column>
			<el-table-column prop="accountNumber" label="账号"></el-table-column>
			<el-table-column prop="openingMoney" label="期初余额"></el-table-column>
			<el-table-column prop="nowMoney" label="当前余额">
				<template #default="{ row }">
					<span :class="row.nowMoney < 0 ? 'negative' : 'positive'">{{
            row.nowMoney
          }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="remark" label="备注" width="130"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="130"></el-table-column>
		</el-table>
		<Add v-if="open_add" :open_add="open_add" @closeDialog="closeDialogFunc($event, 'add')"></Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel"
			@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
	</div>
</template>

<script>
	import FinanceApi from "@/api/finance.js";
	import Add from "./add.vue";
	import Edit from "./edit.vue";
	import {
		ElLoading
	} from "element-plus";
	export default {
		components: {
			Add,
			Edit,
		},
		data() {
			return {
				loading: true,
				activeName: "-1",
				num: 0,
				/*列表数据*/
				tableData: [],
				selectedRows: [], // 选中的行数据
				/*一页多少条*/
				pageSize: 20,
				/*总条数*/
				totalDataNumber: 0,
				open_add: false,
				open_edit: false,
				loadings: null,
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					score: 0,
					name: "",
					enabled: 1,
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
			};
		},
		created() {
			/*获取列表*/
			this.getData();
		},
		methods: {
			calculateTotalBalance() {
				let total = 0;
				this.tableData.forEach((item) => {
					total += parseFloat(item.nowMoney);
				});
				return total.toFixed(2);
			},
			handleStatusChange() {
				this.loadings = ElLoading.service({
					lock: true,
					text: "Loading",
					background: "rgba(0, 0, 0, 0.7)",
				});
				setTimeout(() => {
					this.loadings.close();
					this.getData();
				}, 500);
			},
			defaultClick(row) {
				let self = this;
				// 更新所有账户的 isDefault 字段为 false
				self.tableData.forEach((account) => {
					account.isDefault = false;
				});
				// 将当前行的 isDefault 字段设置为 true
				row.isDefault = true;
				ElMessageBox.confirm("是否将该账户设置为默认账户", "提示", {
					type: "warning",
				}).then(() => {
					self.loading = true;
					FinanceApi.accountDefaultAccount({
						accountId: row.accountId,
					}).then((data) => {
						ElMessage({
							message: "设置成功",
							type: "success",
						});
						self.getData();
					});
				});
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
				Params.enabled = self.searchForm.enabled;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				if (param != "") {
					Params.score = param.score;
					Params.name = param.name;
				}
				FinanceApi.accountList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.records;
						self.totalDataNumber = res.data.total;
						self.num = res.data.num;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClick() {
				this.open_add = true;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
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
				ElMessageBox.confirm("是否删除选中的数据", "提示", {
					type: "warning",
				}).then(() => {
					const ids = this.selectedRows.map((row) => row.id).join(",");
					self.loading = true;
					FinanceApi.accountDelete({
						ids: ids,
					}).then((data) => {
						ElMessage({
							message: "删除成功",
							type: "success",
						});
						self.getData();
					});
				});
			},
			openClick(row) {
				let self = this;
				let confirmMessage = "";
				if (row.enabled) {
					confirmMessage = "确认要停用吗？";
				} else {
					confirmMessage = "确定要开启吗？";
				}
				const state = row.enabled ? 0 : 1;
				ElMessageBox.confirm(confirmMessage, "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						self.loading = true;
						FinanceApi.accountState({
									accountId: row.accountId,
									enabled: state,
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
		},
	};
</script>

<style scoped>
	.negative {
		color: red;
	}

	.positive {
		color: green;
	}

	.total-balance {
		font-weight: 400;
		text-align: left;

		.total-balance-value {
			color: red;
			font-size: 18px;
		}
	}
</style>