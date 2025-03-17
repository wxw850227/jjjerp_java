<template>
	<div class="external" id="Main">
		<Category :data="categoryList" :defaultProps="{ children: 'children', label: 'name' }" :type="0"
			@change="changeCategory" @search="searchCategory"></Category>
		<div class="all">
			<div class="right-container">
				<div class="common-seach-wrap">
					<el-form size="small" :model="searchForm" :inline="true" class="demo-form-inline">
						<el-form-item label="商品名称">
							<el-input v-model="searchForm.name" size="small" placeholder="请输入商品名称"></el-input>
						</el-form-item>
						<!-- <el-form-item label="规格条码">
							<el-input v-model="searchForm.productExtendCode" size="small"
								placeholder="请输入规格条码"></el-input>
						</el-form-item> -->
						<el-form-item label="商品条码">
							<el-input v-model="searchForm.barCode" size="small" placeholder="请输入商品条码"></el-input>
						</el-form-item>
						<el-form-item label="商品编码">
							<el-input v-model="searchForm.productCode" size="small" placeholder="请输入商品编码"></el-input>
						</el-form-item>
						<el-form-item label="状态">
							<el-select class="select-w" v-model="searchForm.enabled" size="small" placeholder="">
								<el-option label="全部" :value="-1"></el-option>
								<el-option v-for="(item, index) in commentList" :key="index" :label="item.name"
									:value="item.val"></el-option>
							</el-select>
						</el-form-item>

						<el-form-item>
							<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
							<el-button size="small" @click="reset">重置</el-button>
						</el-form-item>
					</el-form>
					<div class="mb16 d-b-c">
						<el-button icon="Plus" type="primary" size="small" @click="handleAdd()">新增</el-button>
						<el-pagination size="small" @size-change="handleSizeChange"
							@current-change="handleCurrentChange" background :page-sizes="[20, 50, 100]"
							:current-page="curPage" :page-size="pageSize" layout="total, prev, next, jumper,sizes"
							:total="totalDataNumber">
						</el-pagination>
					</div>
				</div>
				<!-- el-table 表格 -->

				<el-table class="flex-1" scrollbar-always-on v-loading="loading" :data="userList" row-key="productId"
					:default-expand-all="false" :tree-props="{ children: 'skuList' }"
					@selection-change="handleSelectionChange">
					<!-- 表格列 -->
					<el-table-column type="selection" width="50" align="center"></el-table-column>
					<el-table-column label="" prop="" width="50"></el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template #default="scope">
							<div class="d-c-c" style="white-space: nowrap">
								<el-button v-if="isRootNode(scope.row)" @click="editClick(scope.row)" type="primary"
									link size="small">修改</el-button>
								<el-dropdown size="small" v-if="isRootNode(scope.row)">
									<el-button type="primary" link size="small">更多<el-icon>
											<ArrowDown />
										</el-icon></el-button>
									<template #dropdown>
										<el-dropdown-menu>
											<el-dropdown-item @click="copyClick(scope.row)"
												v-if="isRootNode(scope.row)">复制</el-dropdown-item>
											<el-dropdown-item divided @click="delClick(scope.row)"
												v-if="isRootNode(scope.row)">删除</el-dropdown-item>
											<el-dropdown-item divided @click="handleUpdate(scope.row)"
												v-if="isRootNode(scope.row) && scope.row.enabled == 1">停用</el-dropdown-item>
											<el-dropdown-item divided @click="handleUpdate(scope.row)"
												v-if="isRootNode(scope.row) && scope.row.enabled == 0">启用</el-dropdown-item>
										</el-dropdown-menu>
									</template>
								</el-dropdown>
							</div>
						</template>
					</el-table-column>

					<el-table-column label="图片" prop="image" width="60">
						<template #default="{ row }">
							<div style="display: flex; align-items: center;" v-if="isRootNode(row)">
								<el-popover placement="right-start" trigger="hover" width="276">
									<template #reference>	
										<el-avatar v-if="row.imagePath" shape="square"
											:size="26" :src="row.imagePath" />
										<el-avatar v-else :size="26" shape="square"
											src="/static/imgs/imagerror.png" />		
									</template>
									<template #default>
										<el-avatar v-if="row.imagePath" :size="250"
											shape="square" :src="row.imagePath" />
										<el-avatar v-else :size="250"  shape="square"
											src="/static/imgs/imagerror.png" />
									
									</template>
								</el-popover>
							</div>
							<div style="display: flex; align-items: center" v-else>
								<el-popover placement="right-start" trigger="hover" width="276">
									<template #reference>
										<el-avatar v-if="row.imageUrl" shape="square" :size="26"
											:src="row.imageUrl" />
										<el-avatar v-else :size="26" shape="square" 
											src="/static/imgs/imagerror.png" />
									</template>

									<template #default>
										<el-avatar v-if="row.imageUrl" :size="250" 
											shape="square" :src="row.imageUrl" />
										<el-avatar v-else :size="250" shape="square"
											src="/static/imgs/imagerror.png" />
									</template>
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="商品编码" prop="productCode"></el-table-column>
					<el-table-column label="条码" prop="barCode"></el-table-column>
					<el-table-column label="商品名称/规格" prop="name" width="130" show-overflow-tooltip>
						<template #default="{ row }">
							<span>{{ isRootNode(row) ? row.name : row.sku }}</span>
						</template>
					</el-table-column>
					<el-table-column label="品牌" prop="mfrsName"></el-table-column>
					<el-table-column label="货号" prop="accountNum"></el-table-column>
					<el-table-column label="单位" prop="unitName" width="130" show-overflow-tooltip></el-table-column>
					<el-table-column label="重量" prop="weight">
						<template #default="{ row }">
							<span v-if="row.weight">{{ row.weight }}kg</span>
						</template>
					</el-table-column>
					<el-table-column label="体积" prop="volume">
						<template #default="{ row }">
							<span v-if="row.volume">{{ row.volume }}m³</span>
						</template>
					</el-table-column>
					<el-table-column label="更新时间" prop="updateTime" width="140"></el-table-column>
					<el-table-column label="吊牌价" prop="commodityDecimal" width="120"></el-table-column>
					<el-table-column label="标准售价" prop="wholesaleDecimal"></el-table-column>
					<el-table-column label="进价" prop="purchaseDecimal"></el-table-column>
					<el-table-column label="批发价" prop="lowDecimal"></el-table-column>
					<!-- <el-table-column label="成本价" prop="costPrice"></el-table-column> -->
					<el-table-column label="库存" prop="stockNum"></el-table-column>
					<el-table-column label="备注" prop="remark" width="120"></el-table-column>
				</el-table>
				<Add v-if="open_add" :open_add="open_add" :addform="addform"
					@closeDialog="closeDialogFunc($event, 'add')"></Add>
				<Edit v-if="open_edit" :open_edit="open_edit" :editform="categoryModel" :copy="copy"
					@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
				<EditCategory v-if="edit_category" :edit_category="edit_category" :editform="editCategoryForm"
					@closeDialog="closeDialogFunc($event, 'editCategory')"></EditCategory>
				<AddCategorys v-if="add_category" :add_category="add_category" :addform="addCategoryForm"
					@closeDialog="closeDialogFunc($event, 'addCategory')"></AddCategorys>
			</div>
		</div>
	</div>
</template>
<script>
	import Add from "./add.vue";
	import Edit from "./edit.vue";
	import EditCategory from "./part/editCategory.vue";
	import AddCategorys from "./part/addCategory.vue";
	import Category from "@/components/category/category.vue";
	import ProductApi from "@/api/product.js";
	export default {
		components: {
			Add,
			Edit,
			Category,
			EditCategory,
			AddCategorys,
		},
		data() {
			return {
				loading: false,
				deptName: undefined,
				userList: [],
				selectedRows: [], // 选中的行数据
				totalDataNumber: 0,
				curPage: 1,
				pageSize: 20,
				open_add: false,
				open_edit: false,
				add_category: false,
				editCategoryForm: {},
				addCategoryForm: {},
				addform: {
					attribute: "",
				},
				queryParams: {
					// pageIndex: 1,
					// pageSize: 50,
					search: "",
				},
				commentList: [{
						name: "启用",
						val: 1,
					},
					{
						name: "停用",
						val: 0,
					},
				],
				defaultProps: {
					children: "children",
					label: "label",
				},
				searchForm: {
					productCode: "",
					enabled: -1,
					name: "",
					productExtendCode: "",
					barCode: "",
				},
				categoryModel: {},
				options: [],
				unitList: [],
				mfrsList: [],
				categoryList: [],
				copy: false,
				edit_category: false,
				// skuList: [],
			};
		},
		mounted() {},
		created() {
			this.productToAdd();
			this.getData();
			this.categoryLists();
		},
		methods: {
			searchCategory(e) {
				this.categoryLists(e);
			},
			changeCategory(name, e) {
				console.log(name);
				console.log(e);
				if (name == "add") {
					this.addCategory(e);
				}
				if (name == "edit") {
					this.editCategory(e);
				}
				if (name == "delete") {
					this.deleteCategory(e);
				}
				if (name == "click") {
					this.clickCategory(e);
				}
				if (name == "empty") {
					this.clickCategorydelete(e);
				}
			},
			addCategory(e) {
				this.add_category = true;
				console.log(123);
			},
			editCategory(e) {
				this.edit_category = true;
				this.editCategoryForm = e;
			},
			deleteCategory(e) {
				let self = this;
				if (e.children != null) {
					ElMessageBox.alert("此分类下还有子集不能删除", "提示", {
						confirmButtonText: "确定",
						type: "warning",
					});
					return;
				}
				ElMessageBox.confirm("是否删除此分类", "提示", {
					type: "warning",
				}).then(() => {
					ProductApi.categoryDelete({
						categoryId: e.categoryId,
					}).then((data) => {
						ElMessage({
							message: "删除成功",
							type: "success",
						});
						self.categoryLists();
					});
				});
			},
			clickCategory(e) {
				this.addCategoryForm = e;
				this.getData(e);
			},
			clickCategorydelete(e) {
				this.addCategoryForm = e;
				this.getData(e);
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.getData();
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getData();
			},
			copyClick(item) {
				this.categoryModel = item;
				this.open_edit = true;
				this.copy = true;
				console.log("触发");
			},
			editClick(item) {
				this.categoryModel = item;
				this.open_edit = true;
				this.copy = false;
				console.log("触发");
			},
			isRootNode(row) {
				return row.skuList;
			},
			handleAdd() {
				this.open_add = true;
			},
			handleClose() {
				this.dialogVisible = false;
			},
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_add = e.openDialog;
					if (e.type == "success") {
						this.getData();
					} else {
						this.getData();
					}
				}
				if (f == "edit") {
					this.open_edit = e.openDialog;
					if (e.type == "success") {
						this.getData();
					} else {
						this.getData();
					}
				}
				if (f == "editCategory") {
					this.edit_category = e.openDialog;
					if (e.type == "success") {
						this.categoryLists();
					} else {
						this.categoryLists();
					}
				}
				if (f == "addCategory") {
					this.add_category = e.openDialog;
					if (e.type == "success") {
						this.categoryLists();
					} else {
						this.categoryLists();
					}
				}
			},
			productToAdd() {
				let self = this;
				ProductApi.productToAdd()
					.then((res) => {
						self.addform = res.data;
						self.options = res.data.attribute;
						self.unitList = res.data.unit;
						self.mfrsList = res.data.mfrs;
						// self.categoryList = res.data.category;
					})
					.catch((error) => {});
			},
			categoryLists(row) {
				let self = this;
				let Params = {};
				if (row) {
					Params.name = row;
				}
				ProductApi.categoryList(Params, true)
					.then((res) => {
						const allCategories = {
							name: "所有系统分类",
							categoryId: 0,
							isRoot: true,
							children: res.data,
						};
						self.categoryList = [allCategories];
						console.log(self.categoryList, "categoryList");
						self.getData(self.categoryList[0]);
					})
					.catch((error) => {});
			},
			onSubmit() {
				this.getData();
			},
			reset() {
				this.userList = [];
				(this.searchForm = {
					search: "",
				}),
				this.getData();
			},
			getData(row) {
				let self = this;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				if (row) {
					Params.categoryId = row.categoryId;
				} else {
					Params.categoryId = 0;
				}
				// Params.type = self.activeName;
				self.loading = true;
				ProductApi.productList(Params, true)
					.then((res) => {
						self.loading = false;
						self.userList = res.data.productList.records;

						self.totalDataNumber = res.data.productList.total;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			handleSelectionChange(selection) {
				this.selectedRows = selection;
			},
			/*删除*/
			delClick(row) {
				let self = this;
				ElMessageBox.confirm("是否删除此条的数据", "提示", {
					type: "warning",
				}).then(() => {
					ProductApi.productDelete({
						productId: row.productId,
					}).then((data) => {
						ElMessage({
							message: "删除成功",
							type: "success",
						});
						self.getData();
					});
				});
			},
			handleUpdate(row) {
				console.log(row, "row");
				let self = this;
				let state = row.enabled ? 0 : 1;
				let confirmMessage = "";
				if (row.enabled) {
					confirmMessage = "确定要禁用吗？";
				} else {
					confirmMessage = "确认要启用吗？";
				}
				ElMessageBox.confirm(confirmMessage, "友情提示", {
					type: "warning",
				}).then(() => {
					self.loading = true;
					ProductApi.productSetStatus({
								productId: row.productId,
								enabled: state,
							},
							true
						)
						.then((data) => {
							if (row.enabled) {
								ElMessage({
									message: "禁用成功",
									type: "success",
								});
							} else {
								ElMessage({
									message: "启用成功",
									type: "success",
								});
							}

							self.loading = false;
							self.getData();
						})
						.catch((error) => {
							self.loading = false;
						});
				});
			},
		},
	};
	
</script>

<style scoped lang="scss">
	.table-wrap {
		height: 100px;
	}

	.external {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		height: calc(100vh - 84px - 24px);
		overflow: hidden;
	}

	.all {
		flex: 1;
		width: 100%;
		height: calc(100vh - 84px - 24px);
		box-sizing: border-box;
		padding: 15px;
		display: flex;
		background-color: white;
		overflow: auto;

		.app-container {
			width: 250px;
		}

		.right-container {
			flex: 1;
			width: calc(100% - 250px);
			height: calc(100vh - 84px - 24px - 30px);
			display: flex;
			flex-direction: column;
		}
	}

	.el-divider-custom {
		border-color: #e34602;
		height: 100%;
	}

	::v-deep .node-icon {
		position: absolute;
		top: 50%;
		right: 0;
		transform: translateY(-50%);
		opacity: 0;
		/* 初始状态下隐藏图标 */
		transition: opacity 0.3s ease;
		/* 添加过渡效果 */
	}

	::v-deep .node-label {
		margin-right: 50px;
	}

	::v-deep.node-content:hover .node-icon {
		opacity: 1;
		/* 鼠标悬停时显示图标 */
	}

	::v-deep .node-content {
		position: relative;
	}

	.node-content:hover .delete-icon,
	.node-content:hover .edit-icon {
		opacity: 1;
		/* 鼠标悬停时显示按钮 */
	}

	.node-Folder {
		margin-right: 10px;
	}

	.delete-icon,
	.edit-icon {
		cursor: pointer;
		margin-right: 5px;
		opacity: 0;
		/* 初始状态下隐藏按钮 */
		transition: opacity 0.3s ease;
		/* 添加过渡效果 */
	}
	// .customPopper{
	// 	width: 248px!important;
	// 	background-color: #e34602;
	// }
</style>