<template>
	<!-- 弹框 -->
	<el-dialog title="选择商品与规格" v-model="dialogVisibles" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center>
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					选择商品与规格
				</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<div class="external" id="Main">
			<Categorys :data="categoryList" :defaultProps="{ children: 'children', label: 'name' }" :type="0"
				@change="changeCategory"></Categorys>
			<div class="all">
				<div class="right-container">
					<div class="common-seach-wrap">
						<el-form size="small" :model="searchForm" :inline="true" class="demo-form-inline">
							<el-form-item label="">
								<el-input style="width: 200px" v-model="searchForm.productCode" size="small"
									placeholder="请输入商品编码、名称、条码" clearable></el-input>
							</el-form-item>
							<el-form-item>
								<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
								<el-button size="small" @click="reset">重置</el-button>
							</el-form-item>
						</el-form>
						<div class="mb16 d-b-c">
							<div></div>
							<el-pagination small @size-change="handleSizeChange" @current-change="handleCurrentChange"
								background :page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
								layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
							</el-pagination>
						</div>
					</div>
					<el-table class="flex-1" v-loading="loading" :data="userList" highlight-current-row
						@selection-change="handleSelectionChange">
						<el-table-column prop="#" label="序号" width="60">
							<template #default="{ $index }">
								{{ $index + 1 }}
							</template>
						</el-table-column>
						<el-table-column type="selection" width="50" align="center"></el-table-column>
						<el-table-column label="图片" prop="image">
							<template #default="{ row }">
								<div style="display: flex; align-items: center">
									<el-popover placement="right-start" trigger="hover" width="24">
										<template #reference>
											<el-avatar v-if="row.imageUrl" shape="square" style="margin-left: 10px"
												:size="24" :src="row.imageUrl" />
											<el-avatar v-else :size="24" style="margin-left: 10px" shape="square"
												src="/static/imgs/imagerror.png" />
										</template>

										<template #default>
											<el-avatar v-if="row.imageUrl" :size="100" style="margin-left: 10px"
												shape="square" :src="row.imageUrl" />
											<el-avatar v-else :size="100" style="margin-left: 10px" shape="square"
												src="/static/imgs/imagerror.png" />
										</template>
									</el-popover>
								</div>
							</template>
						</el-table-column>
						<el-table-column label="商品编码" prop="productCode"></el-table-column>
						<el-table-column label="商品名称/规格" prop="name" width="130" show-overflow-tooltip>
							<template #default="{ row }">
								<span>{{ row.name
                  }}<span v-if="row.sku">({{ row.sku }})</span></span>
							</template>
						</el-table-column>

						<el-table-column label="吊牌价" prop="commodityDecimal" width="120"></el-table-column>
						<el-table-column label="标准售价" prop="wholesaleDecimal"></el-table-column>
						<el-table-column label="进价" prop="purchaseDecimal"></el-table-column>
						<el-table-column label="批发价" prop="lowDecimal"></el-table-column>
						<el-table-column label="库存" prop="stockNum"></el-table-column>
						<el-table-column label="备注" prop="remark" width="120"></el-table-column>
					</el-table>
				</div>
			</div>
		</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button type="primary" @click="selectProduct" :loading="loading">确 定</el-button>
				<el-button @click="dialogFormVisible">取 消</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import Categorys from "@/components/category/categorys.vue";
	export default {
		components: {
			Categorys,
		},
		data() {
			return {
				fullscreen: false,
				loading: true,
				userList: [],
				selectedRows: [], // 选中的行数据
				totalDataNumber: 0,
				curPage: 1,
				pageSize: 20,
				addform: {
					attribute: "",
				},
				defaultProps: {
					children: "children",
					label: "label",
				},
				searchForm: {
					productCode: "",
					barCode: "",
				},
				categoryModel: {},
				options: [],
				unitList: [],
				mfrsList: [],
				categoryList: [],
				selectedRows: [],
			};
		},
		props: ["open_product", "searchs"],
		created() {
			console.log(this.searchs, "this.searchs");
			this.dialogVisibles = this.open_product;
			if (this.searchs) {
				this.searchForm.productCode = this.searchs;
			}
			this.getData();
			this.categoryLists();
		},
		methods: {
			handleSelectionChange(selection) {
				this.selectedRows = selection;
				console.log(this.selectedRows, "selectedRows");
			},
			clickCategory(e) {
				this.getData(e);
			},
			changeCategory(name, e) {
				console.log(name);
				console.log(e);
				if (name == "click") {
					this.clickCategory(e);
				}
				if (name == "empty") {
					this.clickCategorydelete(e);
				}
			},
			clickCategorydelete(e) {
				this.getData(e);
			},
			categoryLists(row) {
				let self = this;
				let Params = {};

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
			reset() {
				this.searchForm = {};
				this.getData();
			},
			selectProduct() {
				let self = this;
				self.$emit("selectProduct", {
					value: self.selectedRows,
					openDialog: false,
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
			getData(row) {
				let self = this;
				self.loading = true;
				let params = self.searchForm;
				params.pageIndex = self.curPage;
				params.pageSize = self.pageSize;
				if (row) {
					params.categoryId = row.categoryId;
				} else {
					params.categoryId = 0;
				}
				console.log(params, "params");
				ProductApi.productExtendIndex(params)
					.then((res) => {
						self.userList = res.data.records;
						self.totalDataNumber = res.data.total;
						self.loading = false;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			onSubmit() {
				this.getData(this.searchForm);
			},
		},
	};
</script>

<style scoped>
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
</style>