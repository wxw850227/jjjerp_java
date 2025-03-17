<template>
	<div class="user bgset set-main">
		<!-- <div class="common-form">物流公司列表</div> -->
		<!--添加-->
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClick"
					v-auth="'/setting/delivery/express/add'">添加物流公司</el-button>
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
			<el-table-column prop="expressId" label="物流公司ID"></el-table-column>
			<el-table-column prop="expressName" label="物流公司名称"></el-table-column>
			<el-table-column prop="expressCode" label="物流公司代码"></el-table-column>

			<el-table-column prop="sort" label="排序"></el-table-column>
			<el-table-column prop="createTime" label="添加时间"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<el-button @click="editClick(scope.row)" type="primary" link size="small"
						v-auth="'/setting/delivery/express/edit'">编辑</el-button>
					<el-button @click="deleteClick(scope.row)" type="primary" link size="small"
						v-auth="'/setting/delivery/express/delete'">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<Add v-if="open_add" :open_add="open_add" @closeDialog="closeDialogFunc($event, 'add')"></Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel"
			@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
	</div>
</template>

<script>
	import SettingApi from "@/api/setting.js";
	import Add from "./add.vue";
	import Edit from "./edit.vue";
	export default {
		components: {
			Add,
			Edit,
		},
		data() {
			return {
				/*是否加载完成*/
				loading: true,
				/*列表数据*/
				tableData: [],
				/*一页多少条*/
				pageSize: 20,
				/*一共多少条数据*/
				totalDataNumber: 0,
				/*当前是第几页*/
				curPage: 1,
				/*横向表单数据模型*/
				formInline: {
					user: "",
					region: "",
				},
				/*是否打开添加弹窗*/
				open_add: false,
				/*是否打开编辑弹窗*/
				open_edit: false,
				/*当前编辑的对象*/
				userModel: {},
				categoryModel: {},
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
				self.getData();
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
				let Params = {};
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				SettingApi.expressList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.records;
						self.totalDataNumber = res.data.total;
					})
					.catch((error) => {});
			},
			/*打开添加*/
			addClick() {
				this.open_add = true;
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
			/*打开编辑*/
			editClick(item) {
				this.open_edit = true;
				this.categoryModel = item;
			},

			/*删除用户*/
			deleteClick(row) {
				let self = this;
				ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						self.loading = true;
						SettingApi.expressDelete({
									id: row.expressId,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								ElMessage({
									message: data.msg,
									type: "success",
								});
								self.getData();
							})
							.catch((error) => {
								self.loading = false;
							});
					})
					.catch(() => {});
			},
		},
	};
</script>

<style></style>