<template>
	<el-dialog title="生成盘点单" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">生成盘点单</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-row>
					<el-col :span="6">
						<el-form-item label="业务时间">
							<el-date-picker disabled v-model="searchForm.businessTime" type="date" placeholder="请选择"
								:disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="仓库">
							<el-select disabled v-model="searchForm.depotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item>
					</el-col>
					<el-col :span="6">
					<el-form-item label="盘点方式">
						<el-select v-model="searchForm.supplierId" placeholder="请选择">
							<el-option label="全仓盘点" value="1"></el-option>
							<el-option label="负库存盘点" value="2"></el-option>
						</el-select>
					</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="备注">
							<el-input size="small" v-model="searchForm.remark"
								placeholder=""></el-input></el-form-item></el-col>
				</el-row>
				<el-row>
					<el-col :span="10">
						<el-form-item label="快速选择商品:" class="ww100">
							<el-input size="small" v-model="searchs.productCode" placeholder="输入编码/名称(精确到规格)后按回车">
								<template #append>
									<el-button @click="reserch" icon="Search" />
								</template>
							</el-input> 
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
		</div>
		<el-table size="small" :data="productData" border style="width: 100%" @selection-change="handleSelectionChange"
			v-loading="loading">
			<el-table-column prop="#" label="序号" width="60">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="图" width="42">
				<template #default="scope">
					<el-image style="width: 26px; height: 26px;display: block;" :src="scope.row.imageUrl"
						:preview-src-list="[scope.row.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="productCode" label="商品编码" width="100"></el-table-column>
			<el-table-column prop="name" label="商品名称" width="100" show-overflow-tooltip></el-table-column>
			<el-table-column prop="barCode" label="条码" width="100"></el-table-column>
			<el-table-column prop="sku" label="规格" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="100">
			</el-table-column>
			<el-table-column prop="stockNum" label="库存数量" width="100"></el-table-column>
			<el-table-column prop="totalNumber" label="盘点后数量" width="100">
				<template #default="{ row }">
					<el-input v-model="row.totalNumber" label="盘点后数量" @blur="handleTotalNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="basicNumber" label="差异数量" width="100">
				<template #default="{ row }">
					<el-input v-model="row.basicNumber" label="数量" @blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="remark" label="备注" width="100" show-overflow-tooltip>
			</el-table-column>
		</el-table>
		<template #footer>
			<div class="dialog-footer">
				<el-button type="primary" @click="editUser">保 存</el-button>
				<el-button @click="dialogFormVisible">取 消</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import StockApi from "@/api/stock.js";
	import {
		formatDate
	} from "@/utils/DateTime.js";

	export default {
		components: {},
		data() {
			return {
				searchForm: {
					barCode: "",
					fileList: [],
					pageIndex: "",
					pageSize: "",
					imageId: "",
				},
				form: {},
				fullscreen: false,
				searchs: {},
				valueList: [],
				config: {},
				productData: [],
				radio: 0,
				accountList: [],
				open_purchase: false,
				formRules: {
					attributeName: [{
						required: true,
						message: "请输入分类名称",
						trigger: "blur",
					}, ],
					imageId: [{
						required: true,
						message: "请上传分类图片",
						trigger: "blur",
					}, ],
					sort: [{
							required: true,
							message: "分类排序不能为空",
						},
						{
							type: "number",
							message: "分类排序必须为数字",
						},
					],
				},
				/*左边长度*/
				formLabelWidth: "100px",
				/*是否显示*/
				dialogVisible: false,
				loading: false,
				/*是否上传图片*/
				isupload: false,
				isDelete: 0,
				depotList: [],
				supplierList: [],
				titles: "",
			};
		},
		props: ["open_add", "addForm", "allData"],
		created() {
			this.dialogVisible = this.open_add;

			console.log(this.allData, "allData");
			this.depotList = this.addForm.depot;
			this.supplierList = this.addForm.supplier;
			this.productData = this.allData.billItemList;
			this.searchForm = this.allData;
			this.productData.map((product) => {
				return this.handleOperNumberChange(product);
			});
			// this.getselect();
		},
		methods: {
			handleOperNumberChange(row) {
				console.log(row, "row");
				row.totalNumber = Number(row.stockNum) + Number(row.basicNumber);
			},
			handleTotalNumberChange(row) {
				row.basicNumber = Number(row.totalNumber) - Number(row.stockNum);
			},
			reserch() {
				if (!this.searchForm.depotId) {
					ElMessage.error("仓库不能为空");
					return; // 不执行后续操作
				}
				let params = this.searchs;
				console.log(params, "params");
				ProductApi.productExtendIndex(params)
					.then((res) => {
						this.productData = res.data.records;
						ElMessage({
							message: "查询成功",
							type: "success",
						});
						if (this.productData.length > 0) {
							this.productData.forEach((row) => {
								row.materialUnit = row.productUnit.basicUnit;
								// this.handleUnitChange(row);
							});
						}
						this.searchs = {};
					})
					.catch((error) => {});
			},
			getStorageList(id) {
				let self = this;
				SettingApi.depotStorageToSet({
						depotStorageId: id
					})
					.then((res) => {})
					.catch((error) => {});
			},
			/*添加用户*/
			editUser() {
				let self = this;
				let billItemList = self.productData.map((option) => ({
					productId: option.productId,
					productExtendId: option.productExtendId,
					sku: option.sku,
					materialUnit: option.materialUnit,
					operNumber: option.operNumber,
					basicNumber: option.basicNumber,
					// id: option.id,
				}));
				let params = {};
				params.depotId = self.searchForm.depotId;
				params.businessTime = formatDate(
					self.searchForm.businessTime,
					"YYYY-MM-DD"
				);
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				StockApi.stockStockCheckAdd(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "保存成功",
							type: "success",
						});
						self.dialogFormVisible(true);
					})
					.catch((error) => {
						console.log("错误提示", error);
						self.loading = false;
					});
			},

			/*关闭弹窗*/
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
		},
	};
</script>

<style scoped>
	.img {
		margin-top: 10px;
	}

	.fdw {
		display: flex;
		align-items: center;
	}

	.tip {
		width: 190px;
		height: 21px;
		color: rgba(153, 153, 153, 1);
		font-size: 14px;
		text-align: left;
		margin-left: 100px;
		margin-bottom: 20px;
	}

	.titles {
		left: 424px;
		top: 418px;
		width: 262px;
		height: 21px;
		color: rgba(16, 16, 16, 1);
		font-size: 14px;
		text-align: left;
		margin-left: 40px;
		margin-bottom: 20px;
	}

	.btns {
		display: flex;
		flex-direction: row;
		width: 109px;
		height: 36px;
		font-size: 14px;
		text-align: center;

		.thebtn1 {
			color: #ffffff;
			background: #43aa8b;
		}
	}

	.select {
		padding-right: 40px;
	}
</style>