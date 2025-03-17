<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="店铺名称">
					<el-input size="small" v-model="searchForm.name" clearable placeholder="请输入名称"
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item label="店铺状态">
					<el-select size="small" v-model="searchForm.enabled" placeholder="" class="form_content_width">
						<el-option label="全部" :value="-1"></el-option>
						<el-option v-for="(item, index) in commentList" :key="index" :label="item.name"
							:value="item.val"></el-option>
					</el-select>
				</el-form-item>

				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<el-button size="small" type="primary" icon="Plus" @click="addClick"
				v-auth="'/setting/basic/store/add'">新增店铺</el-button>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!-- el-table 表格 -->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
			<!-- 表格列 -->
			<el-table-column prop="storeId" label="ID" width="55"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<div style="white-space: nowrap" class="d-c-c">
						<el-button @click="editClick(scope.row, true)" type="primary" link size="small">详情</el-button>
						<el-dropdown size="small">
							<el-button type="primary" link size="small">更多<el-icon>
									<ArrowDown />
								</el-icon></el-button>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="editClick(scope.row)">修改</el-dropdown-item>
									<el-dropdown-item @click="openClick(scope.row)">{{
                    scope.row.enabled ? "停用" : "启用"
                  }}</el-dropdown-item>
									<el-dropdown-item v-if="scope.row.enabled == 0"
										@click="delClick(scope.row)">删除</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</template>
			</el-table-column>

			<el-table-column prop="storeType" label="类型" width="100">
				<template #default="scope">
					<el-text size="small" v-if="scope.row.storeType == 0">其他平台</el-text>
					<el-text size="small" v-else>线下门店</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="nickName" label="掌柜旺旺/账号ID" width="120"></el-table-column>
			<el-table-column prop="storeName" label="店铺名称" width="100"></el-table-column>
			<el-table-column prop="addressd" label="地址" width="150" show-overflow-tooltip></el-table-column>
			<el-table-column prop="contacts" label="联系人"></el-table-column>
			<el-table-column prop="fixPhone" label="固话"></el-table-column>
			<el-table-column prop="phone" label="手机"></el-table-column>
			<el-table-column prop="expiryTime" label="授权到期时间" width="130"></el-table-column>
		</el-table>
		<Add v-if="open_add" :open_add="open_add" @closeDialog="closeDialogFunc($event, 'add')"></Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :isDetailMode="isDetailMode"
			@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
	</div>
</template>

<script>
	import SettingApi from "@/api/setting.js";
	import Add from "./part/add.vue";
	import Edit from "./part/edit.vue";
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
				isDetailMode: false,
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					enabled: -1,
					name: "",
				},
				/*评价等级*/
				commentList: [{
						name: "启用",
						val: 1,
					},
					{
						name: "停用",
						val: 0,
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
					Params.enabled = param.enabled;
					Params.name = param.name;
				}
				SettingApi.storeList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.records;
						self.tableData.forEach((item) => {
							let addressd = "";
							if (item.province) {
								addressd += item.province;
							}
							if (item.city) {
								addressd += item.city;
							}
							if (item.region) {
								addressd += item.region;
							}
							if (item.address) {
								addressd += item.address;
							}
							item.addressd = addressd;
						});
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
			editClick(row, isDetail) {
				this.open_edit = true;
				this.categoryModel = row;
				this.isDetailMode = isDetail;
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

			/*删除*/
			delClick(row) {
				let self = this;
				ElMessageBox.confirm("是否删除选中的数据", "提示", {
					type: "warning",
				}).then(() => {
					self.loading = true;
					SettingApi.storeDel({
						storeId: row.storeId,
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
				if (!row.enabled) {
					confirmMessage = "确定要启用吗？";
				} else {
					confirmMessage = "确认要停用吗？";
				}
				const state = row.enabled ? 0 : 1;
				ElMessageBox.confirm(confirmMessage, "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						self.loading = true;
						SettingApi.storeSet({
									storeId: row.storeId,
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
			/*搜索查询*/
			onSubmit() {
				let params = this.searchForm;
				this.getData(params);
			},
		},
	};
</script>

<style></style>