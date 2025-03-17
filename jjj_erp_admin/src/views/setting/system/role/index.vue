<template>
	<!--描述：权限-角色管理-->
	<div class="user bgset set-main">
		<div class="common-level-rail">
			<el-button size="small" type="primary" icon="Plus" @click="addClick"
				v-auth="'/setting/system/role/add'">添加角色</el-button>
		</div>

		<!--表格-->
				<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
					<el-table-column prop="roleId" label="角色ID"></el-table-column>
					<el-table-column prop="roleName" label="角色名称"></el-table-column>
					<el-table-column prop="sort" label="排序"></el-table-column>
					<el-table-column prop="createTime" label="添加时间"></el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template #default="scope">
							<el-button @click="editClick(scope.row)"  type="primary"
					  link size="small"
								v-auth="'/setting/system/role/edit'">编辑</el-button>
							<el-button @click="deleteClick(scope.row)"  type="primary"
					  link size="small"
								v-auth="'/setting/system/role/delete'">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
		<Add :open="open_add" :roleList="roleList" @closeDialog="closeAdd"></Add>
		<Edit :open="open_edit" :roleList="roleList" :model="curModel" @closeDialog="closeEdit"></Edit>
	</div>
</template>

<script>
	import AuthApi from "@/api/auth.js";
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
				curModel: {},
				/*是否打开添加弹窗*/
				open_add: false,
				/*是否打开编辑弹窗*/
				open_edit: false,
			};
		},
		created() {
			/*获取列表*/
			this.getTableList();
		},
		methods: {
			/*获取列表*/
			getTableList() {
				let self = this;
				self.loading = true;
				AuthApi.roleList({}, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data;
					})
					.catch((error) => {
						self.loading = false;
					});
			},

			/*打开添加*/
			addClick() {
				this.open_add = true;
			},

			/*打开编辑*/
			editClick(row) {
				this.curModel = row;
				console.log(this.curModel, "this.curModel ");
				this.open_edit = true;
			},

			/*刷新心也*/
			refresh() {
				this.reload();
				// 直接使用reload方法
			},
			closeAdd(e) {
				this.open_add = e.openDialog;
				if (e && e.type == "success") {
					this.getTableList();
				}
			},
			closeEdit(e) {
				this.open_edit = e.openDialog;
				if (e && e.type == "success") {
					this.getTableList();
				}
			},
			/*删除*/
			deleteClick(row) {
				let self = this;
				ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						self.loading = true;
						AuthApi.roleDelete({
									roleId: row.roleId,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "恭喜你，该角色删除成功",
										type: "success",
									});
									self.getTableList();
								} else {
									self.loading = false;
								}
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