<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="名称">
					<el-input size="small" clearable v-model="searchForm.supplierName" placeholder="请输入名称"
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item label="编码">
					<el-input size="small" clearable v-model="searchForm.supplierCode" placeholder="请输入编码"
						class="form_content_width"></el-input>
				</el-form-item>
				<el-form-item label="查询状态">
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
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClick"
					v-auth="'/setting/basic/supplier/add'">新增</el-button>
				<el-button size="small" type="primary" v-auth="'/product/unit/delete'">导入供应商信息</el-button>
				<el-button size="small" type="primary" v-auth="'/product/unit/setStatus'">导出供应商信息</el-button>
				<el-button size="small" type="primary" v-auth="'/product/unit/setStatus'">导入供货商品</el-button>
				<el-button size="small" type="primary" v-auth="'/product/unit/setStatus'">导出供货商品</el-button><el-button
					size="small" type="primary" v-auth="'/product/unit/setStatus'">供货商品查询</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--内容-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column prop="supplierId" label="ID" width="42"></el-table-column>
			<el-table-column type="selection" width="55"></el-table-column>
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

			<el-table-column prop="supplierCode" width="130" label="供应商编码"></el-table-column>
			<el-table-column prop="supplierName" label="供应商名称" width="100"></el-table-column>
			<el-table-column prop="supplierType" label="供应商类型" width="100">
				<template #default="scope">
					<el-text size="small" v-if="scope.row.supplierType == 0">普通供应商</el-text>
					<el-text size="small" v-if="scope.row.supplierType == 1">直供供应商</el-text>
					<el-text size="small" v-if="scope.row.supplierType == 2">1688供应商</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="website" label="网站网址" width="100"></el-table-column>
			<el-table-column prop="allNeedPay" label="当前应付款" width="100"></el-table-column>
			<el-table-column prop="advancePay" label="预付款"></el-table-column>
			<el-table-column prop="province" label="省"></el-table-column>
			<el-table-column prop="city" show-overflow-tooltip label="市"></el-table-column>
			<el-table-column prop="region" show-overflow-tooltip label="区"></el-table-column>
			<el-table-column show-overflow-tooltip prop="addressd" label="详细地址" width="130"></el-table-column>
			<el-table-column prop="contacts" label="联系人"></el-table-column>
			<el-table-column prop="fixPhone" label="固话"></el-table-column>
			<el-table-column prop="phone" label="手机"></el-table-column>
			<el-table-column prop="description" label="备注" width="130"></el-table-column>
			<el-table-column prop="" label="状态" width="80">
				<template #default="scope">
					<el-tag v-if="scope.row.enabled == 1">启用</el-tag>
					<el-tag type="danger" v-else>停用</el-tag>
				</template>
			</el-table-column>
		</el-table>
		<Add v-if="open_add" :open_add="open_add" @closeDialog="closeDialogFunc($event, 'add')"></Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :isDetailMode="isDetailMode"
			@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
		<!--分页-->
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
					supplierCode: "",
					enabled: -1,
					supplierName: "",
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
					Params.supplierCode = param.supplierCode;
					Params.supplierName = param.supplierName;
					Params.enabled = param.enabled;
				}
				SettingApi.supplierList(Params, true)
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
				ElMessageBox.confirm("是否删除选中的数据", "提示", {
					type: "warning",
				}).then(() => {
					self.loading = true;
					SettingApi.supplierDel({
						supplierId: row.supplierId,
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
						SettingApi.supplierSet({
									supplierId: row.supplierId,
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
				this.loading = true;
				this.getData(params);
			},
		},
	};
</script>

<style></style>