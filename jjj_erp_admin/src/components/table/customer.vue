<template>
	<!-- 弹框 -->
	<el-dialog :title="openTitle" v-model="dialogVisibles" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center>
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					{{ openTitle }}
				</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm">
				<el-form-item label="">
					<el-input size="small" clearable v-model="searchForm.search" placeholder="请输入编码、名称或者手机号"
						style="width: 200px"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" icon="Search" @click="onSubmit">查询</el-button>
					<el-button size="small" @click="resetForm">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1"></div>
			<el-pagination small @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber"></el-pagination>
		</div>

		<!--内容-->
		<el-table class="flex-1" size="small" :data="tableData" border row-key="addressId" highlight-current-row
			:tree-props="{ children: 'addressList' }" :default-expand-all="false" @current-change="handleChange"
			v-loading="loading">
			<el-table-column prop="customerId" label="ID" width="70"></el-table-column>
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="userCode" label="客户编码" width="120"></el-table-column>
			<el-table-column prop="realName" label="客户名称" width="100"></el-table-column>
			<el-table-column prop="nickName" label="昵称" width="100"></el-table-column>
			<el-table-column prop="platformType" label="平台" width="150" show-overflow-tooltip></el-table-column>
			<el-table-column prop="phone" label="手机"></el-table-column>
			<el-table-column prop="province" label="省"></el-table-column>
			<el-table-column prop="city" label="市"></el-table-column>
			<el-table-column prop="region" label="区"></el-table-column>
			<el-table-column prop="addressd" label="详细地区" width="120" show-overflow-tooltip></el-table-column>
			<el-table-column prop="email" label="E-mail" width="130"></el-table-column>
			<el-table-column prop="fixPhone" label="固话" width="130"></el-table-column>
			<el-table-column prop="expiryTime" label="业务员" width="130"></el-table-column>
			<el-table-column prop="initial" label="当前应收款" width="130"></el-table-column>
		</el-table>
	</el-dialog>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import StockApi from "@/api/stock.js";
	import SettingApi from "@/api/setting.js";

	export default {
		data() {
			return {
				loading: true,
				openTitle: "选择客户",
				tableData: [],
				searchForm: {
					search: "",
				},
				pageSize: 20,
				curPage: 1,
				fullscreen: false,
				dialogVisibles: false,
				totalDataNumber: 0,
			};
		},
		props: ["isCustomer"],
		created() {
			console.log(this.isCustomer, "this.isCustomer");
			this.dialogVisibles = this.isCustomer;
			this.getData();
		},
		methods: {
			dialogFormVisible(e) {
				if (e) {
					this.$emit("closeDialog", {
						type: "success",
						openDialog: false,
					});
				} else {
					this.$emit("closeDialog", {
						type: "error",
						openDialog: false,
					});
				}
			},
			resetForm() {
				this.searchForm = {};
			},
			handleChange(val) {
				this.$emit("selectValue", {
					value: val,
					openDialog: false,
					all: this.tableData,
				});
			},
			handleCurrentChange(val) {
				this.curPage = val;
				this.loading = true;
				this.getData();
			},
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getData();
			},
			getData() {
				let self = this;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				SettingApi.userList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.records.map((record) => ({
							...record,
							addressId: record.customerId,
						}));
						self.tableData.forEach((item) => {
							item.addressd =
								item.province + item.city + item.region + item.address;
						});
						self.totalDataNumber = res.data.total;
						self.num = res.data.num;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			indexFormatter(row, column, cellValue, index) {
				return index + 1;
			},
			onSubmit() {
				this.getData(this.searchForm);
			},
		},
	};
</script>

<style scoped>
	.custom-table .el-table__expand-icon {
		display: none;
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