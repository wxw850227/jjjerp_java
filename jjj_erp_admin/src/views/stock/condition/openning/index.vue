<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="商品">
					<el-input size="small" v-model="searchForm.name" placeholder="请输入商品编号/名称"></el-input>
				</el-form-item>
				<el-form-item label="供应商">
					<el-select v-model="searchForm.supplierId" placeholder="请选择">
						<el-option v-for="(item, index) in suppliers" :key="index" :label="item.supplierName"
							:value="item.supplierId" />
					</el-select>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select size="default" v-model="searchForm.depotIdList" placeholder="请选择" multiple filterable
						clearable style="width: 285px" @change="handleSelectionChange">
						<template #header>
							<el-checkbox v-model="checkAll" :indeterminate="indeterminate"
								@change="handleCheckAllChange">
								全选
							</el-checkbox>
						</template>
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="品牌">
					<el-select size="small" v-model="searchForm.expressId" placeholder="请选择">
						<el-option v-for="(item, index) in expressList" :key="index" :label="item.name"
							:value="item.id" />
					</el-select>
				</el-form-item>
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" @click="saveClick" icon="DocumentChecked"
					v-auth="'/product/base/unit/add'">保存</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<el-table class="flex-1" size="small" :data="tableData" border row-key="productId"
			:tree-props="{ children: 'skuList' }" :default-expand-all="false" style="width: 100%" v-loading="loading">
			<el-table-column prop="index" label="序号" width="80" :formatter="indexFormatter"></el-table-column>

			<!-- <el-table-column prop="productId" label="ID" width="80">
            <template #default="{ row }">
              <span v-if="isRootNode(row)">{{ row.productId }}</span>
              <span v-else>{{ row.id }}</span>
            </template>
          </el-table-column> -->
			<el-table-column prop="imagePath" label="图片" width="52">
				<template #default="{ row }">
					<div style="display: flex; align-items: center" v-if="isRootNode(row)">
						<el-popover placement="right-start" trigger="hover" width="276">
							<template #reference>
								<el-avatar v-if="row.imagePath" shape="square" :size="26" :src="row.imagePath" />
								<el-avatar v-else :size="26" shape="square" src="/static/imgs/imagerror.png" />
							</template>

							<template #default>
								<el-avatar v-if="row.imagePath" :size="250" shape="square" :src="row.imagePath" />
								<el-avatar v-else :size="250" shape="square" src="/static/imgs/imagerror.png" />
							</template>
						</el-popover>
					</div>
					<div v-else>
						<el-popover placement="right-start" trigger="hover" width="276">
							<template #reference>
								<el-avatar v-if="row.imageUrl" shape="square" :size="26" :src="row.imageUrl" />
								<el-avatar v-else :size="26" shape="square" src="/static/imgs/imagerror.png" />
							</template>

							<template #default>
								<el-avatar v-if="row.imageUrl" :size="250" shape="square" :src="row.imageUrl" />
								<el-avatar v-else :size="250" shape="square" src="/static/imgs/imagerror.png" />
							</template>
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
			<el-table-column prop="sku" label="规格"></el-table-column>
			<el-table-column prop="productCode" label="商品编码"></el-table-column>
			<el-table-column prop="openningStock" label="期初数量">
				<template #default="{ row }">
					<el-input v-if="isRootNode(row) || row.initialStatus == 0" v-model="row.openningStock"
						disabled></el-input>
					<el-input v-model="row.openningStock" v-else></el-input>
				</template>
			</el-table-column>
			<el-table-column label="期初成本价" prop="costPrice">
				<template #default="{ row }">
					<el-input v-if="isRootNode(row) || row.initialStatus == 0" v-model="row.costPrice"
						disabled></el-input>
					<el-input v-else v-model="row.costPrice"></el-input>
				</template>
			</el-table-column>
		</el-table>
	</div>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import StockApi from "@/api/stock.js";
	import SettingApi from "@/api/setting.js";
	export default {
		components: {},
		data() {
			return {
				loading: true,
				open_detail: false,
				activeName: "detail",
				num: 0,
				/*列表数据*/
				tableData: [],
				deatilModel: {},
				select: 1,
				skuList: [],
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
					operTimes: "1",
					name: "",
				},
				status: -1,
				depots: [],
				suppliers: [],
				selectedBillId: null,
				openTitle: "",
				customerList: [],
				depotList: [],
				expressList: [],
				openDetail: false,
				sequenceNumbers: {},
				isDepotListInitialized: false,
				checkAll: false,
				isIndeterminate: false,
			};
		},
		created() {
			/*获取列表*/
			this.getData();
			// this.getselect();
		},
		computed: {},
		methods: {
			handleCheckAllChange(val) {
				this.searchForm.depotIdList = val ?
					this.depotList.map((item) => item.depotId) :
					[];
				this.isIndeterminate = false;
			},
			handleSelectionChange(val) {
				console.log(val, "val");
				this.searchForm.depotIdList = val;
				this.loading = true;
				this.onSubmit();
				const checkedCount = val.length;
				this.checkAll = checkedCount === this.depotList.length;
				this.isIndeterminate =
					checkedCount > 0 && checkedCount < this.depotList.length;
			},
			saveClick() {
				const collectData = (items) => {
					return items.flatMap((item) => {
						if (item.skuList) {
							return item.skuList
								.filter((subItem) => {
									const originalSubItem = item.originalExtendList.find(
										(orig) => orig.id === subItem.id
									);

									// 检查是否修改过openningStock或costPrice
									const hasModifiedOpenningStock =
										subItem.openningStock !== originalSubItem.openningStock;
									const hasModifiedCostPrice =
										subItem.costPrice !== originalSubItem.costPrice;

									// 如果有修改，返回这个子项，否则过滤掉
									return hasModifiedOpenningStock || hasModifiedCostPrice;
								})
								.map((subItem) => ({
									productExtendId: subItem.id,
									productId: subItem.productId,
									sku: subItem.sku,
									materialUnit: subItem.materialUnit,
									operNumber: subItem.operNumber,
									basicNumber: subItem.openningStock,
									unitPrice: subItem.unitPrice,
									discountRate: subItem.discountRate,
									discountPrice: subItem.discountPrice,
									allotPrice: subItem.allotPrice,
									allPrice: subItem.allPrice,
									// openningStock: subItem.openningStock,
									costPrice: subItem.costPrice,
								}));
						}
						return [];
					});
				};
				const skuList = collectData(this.tableData);
				const updatedData = {
					supplierId: this.searchForm.supplierId,
					depotId: this.searchForm.depotId,
					billItemList: skuList,
				};
				if (!this.searchForm.depotId && !this.searchForm.supplierId) {
					ElMessage.error("仓库和供应商不能为空");
					return;
				}
				if (skuList.length === 0) {
					ElMessageBox.alert("没有需要提交的数据。", "系统通知", {
						confirmButtonText: "确定",
						type: "error",
					});
					return;
				}
				this.loading = true;
				StockApi.stockOpenningAdd(updatedData, true)
					.then((res) => {
						ElMessageBox.alert("保存成功", "提示", {
								confirmButtonText: "确定",
								type: "success",
							})
							.then(() => {
								this.getData();
								this.loading = true;
							})
							.catch(() => {
								console.log("取消");
								this.loading = false;
							});
					})
					.catch((e) => {
						console.log(e);
						this.loading = false;
					});
			},
			isRootNode(row) {
				return row.skuList;
			},
			getselect() {
				SettingApi.userList({})
					.then((res) => {
						this.customerList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
					});
			},

			printClick() {
				if (this.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据进行打印");
					return;
				}
				this.printData = this.selectedRows;
				this.addForm = this.otherData;
				this.open_print = true;
			},

			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.loading = true;
				let Params = self.searchForm;
				self.getData(Params);
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
				const params = {
					pageSize: this.pageSize,
					pageIndex: this.curPage,
					name: this.searchForm.name,
					supplierId: this.searchForm.supplierId,
					depotId: this.searchForm.depotId,
				};
				StockApi.stockOpenning(params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.product.records.map((item) => ({
							...item,
							originalExtendList: item.skuList.map((subItem) => ({
								...subItem,
							})),
						}));

						self.otherData = res.data;
						self.expressList = res.data.express;
						self.depotList = res.data.depot;
						self.suppliers = res.data.supplier;
						self.totalDataNumber = res.data.product.total;
						self.generateSequenceNumbers();
						if (!self.isDepotListInitialized && self.depotList) {
							self.searchForm.depotIdList = [self.depotList[0].depotId]; // 设置默认选中第一项
							self.isDepotListInitialized = true;
							self.onSubmit(); // 再次调用getData以更新列表
						}
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			generateSequenceNumbers() {
				this.sequenceNumbers = {};
				let rootIndex = 1;
				this.tableData.forEach((item) => {
					// 为父列表项生成序号
					this.sequenceNumbers[`parent_${item.productId}`] = `${rootIndex}`;
					if (item.skuList) {
						item.skuList.forEach((subItem, subIndex) => {
							// 为子列表项生成序号
							this.sequenceNumbers[`child_${subItem.id}`] = `${rootIndex}.${
              subIndex + 1
            }`;
						});
					}
					rootIndex++;
				});
			},
			indexFormatter(row, column, cellValue, index) {
				if (this.isRootNode(row)) {
					// 获取父列表项的序号
					return (
						this.sequenceNumbers[`parent_${row.productId}`] || `${index + 1}`
					);
				} else {
					// 查找父项并获取子项的序号
					const parentItem = this.tableData.find(
						(item) =>
						item.skuList &&
						item.skuList.some((subItem) => subItem.id === row.id)
					);
					if (parentItem) {
						const parentIndex =
							this.sequenceNumbers[`parent_${parentItem.productId}`];
						const subIndex =
							parentItem.skuList.findIndex((subItem) => subItem.id === row.id) +
							1;
						return `${parentIndex}.${subIndex}`;
					} else {
						return `${index + 1}`;
					}
				}
			},
			/*搜索查询*/
			onSubmit() {
				this.getData(this.searchForm);
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