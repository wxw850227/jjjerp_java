<template>
	<div class="product-list bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-tabs v-model="activeName" @tab-change="handleClick">
				<el-tab-pane label="品牌管理" name="sell">
					<template #label>
						<span>品牌管理 </span>
					</template>
				</el-tab-pane>
			</el-tabs>
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="品牌名称"><el-input size="small" class="form_content_width" v-model="searchForm.name"
						placeholder="请输入品牌名称"></el-input></el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClick">新增</el-button>
			</div>
			<!-- 分页 -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>

		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%" v-loading="loading">
			<el-table-column prop="#" label="序号" width="54">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<el-table-column label="操作" width="60">
				<template #default="{ $index }">
					<el-button @click="removeOptions($index)" type="primary" link size="small">
						删除
					</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="品牌名称">
				<template #default="scope">
					<template v-if="!scope.row.editing">
						<div @click="editUnit(scope.row)">{{ scope.row.name }}</div>
					</template>
					<template v-else>
						<el-input v-model="scope.row.newName" @blur="saveUnit(scope.row)" autofocus></el-input>
					</template>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间"></el-table-column>
		</el-table>
	</div>
</template>

<script>
	import ProductApi from "@/api/product.js";
	export default {
		components: {},
		data() {
			return {
				/*切换菜单*/
				activeName: "0",
				/*切换选中值*/
				activeIndex: "0",
				/*是否正在加载*/
				loading: true,
				/*一页多少条*/
				pageSize: 15,
				/*一共多少条数据*/
				totalDataNumber: 0,
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					name: "",
					categoryId: "",
				},
				editingRow: null,
				/*列表数据*/
				tableData: [],
				/*全部分类*/
				categoryList: [],
				productCount: {},
			};
		},
		created() {
			/*获取列表*/
			this.getData();
		},
		methods: {
			editUnit(row) {
				// 标记当前行为编辑状态
				row.editing = true;
				// 保存编辑前的值
				row.newName = row.name;
				// 保存正在编辑的行
				this.editingRow = row;
			},
			saveUnit(row) {
				if (row.id) {
					ProductApi.mfrsEdit({
							id: row.id,
							name: row.newName,
						})
						.then(() => {
							ElMessage({
								message: "修改成功",
								type: "success",
							});
							this.getData();
						})
						.catch(() => {
							ElMessage.error("保存失败，请稍后重试");
							// 恢复编辑前的值
							row.newName = row.name;
						})
						.finally(() => {
							// 结束编辑状态
							row.editing = false;
							this.editingRow = null;
						});
				} else {
					ProductApi.mfrsAdd({
							name: row.newName,
						})
						.then(() => {
							ElMessage({
								message: "添加成功",
								type: "success",
							});
							this.getData();
						})
						.catch(() => {
							ElMessage.error("保存失败，请稍后重试");
						})
						.finally(() => {
							// 结束编辑状态
							row.editing = false;
							this.editingRow = null;
						});
				}
				// 调用接口保存修改后的值
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.loading = true;
				self.curPage = val;
				self.getData();
			},

			/*每页多少条*/
			handleSizeChange(val) {
				this.pageSize = val;
				this.getData();
			},

			/*切换菜单*/
			handleClick(tab) {
				let self = this;
				self.curPage = 1;
				self.activeName = tab;
				// self.getData();
			},

			/*获取列表*/
			getData() {
				let self = this;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				Params.type = self.activeName;
				self.loading = true;
				ProductApi.mfrsList(Params, true)
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
				this.curPage = 1;
				this.getData();
			},
			/*打开添加*/
			addClick() {
				if (this.tableData.some((row) => row.editing)) {
					ElMessage.warning("请先保存当前正在编辑的行");
					return;
				}
				this.tableData = this.tableData || [];
				this.tableData.unshift({
					editing: true,
					newName: "",
				});
			},
			removeOptions(index) {
				const row = this.tableData[index];
				if (row.id) {
					// 如果该行存在 id，则调用接口删除数据
					ProductApi.mfrsDelete({
							id: row.id,
						})
						.then(() => {
							ElMessage({
								message: "删除成功",
								type: "success",
							});
							// 删除成功后再删除对应的行
							this.tableData.splice(index, 1);
						})
						.catch(() => {
							ElMessage.error("删除失败，请稍后重试");
						});
				} else {
					// 如果该行不存在 id，则直接在删除对应的行
					this.tableData.splice(index, 1);
				}
			},
		},
	};
</script>

<style></style>