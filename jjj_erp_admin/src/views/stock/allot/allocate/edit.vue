<template>
	<el-dialog :title="titleName" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					{{ titleName }}
				</div>
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
					<el-col :span="5">
						<el-form-item label="调拨单号">
							<el-input :disabled="openDetail" size="small" v-model="searchForm.code"
								placeholder="不填写则系统自动生成"></el-input></el-form-item></el-col>
					<el-col :span="5">
						<el-form-item label="业务时间">
							<el-date-picker :disabled="openDetail" v-model="searchForm.businessTime" type="date"
								placeholder="请选择" :disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
						</el-form-item>
					</el-col>

					<el-col :span="4">
						<el-form-item label="调出仓库">
							<el-select :disabled="openDetail" v-model="searchForm.depotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="4">
						<el-form-item label="调入仓库">
							<el-select :disabled="openDetail" v-model="searchForm.anotherDepotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="6">
						<el-form-item label="备注" class="ww100">
							<el-input :disabled="openDetail" size="small" v-model="searchForm.remark"
								placeholder=""></el-input></el-form-item></el-col>
				</el-row>
				<el-row v-if="!openDetail">
					<el-col :span="10">
						<el-form-item label="快速选择商品:" class="ww100">
							<el-input size="small" v-model="searchs.productCode" @keyup.enter="reserch()"
								placeholder="输入编码/名称(精确到规格)后按回车">
								<template #append>
									<el-button @click="reserch" icon="Search" />
								</template>
							</el-input> </el-form-item></el-col>
				</el-row>
			</el-form>
		</div>
		<el-table size="small" :data="productData" border style="width: 100%" @selection-change="handleSelectionChange"
			v-loading="loading" show-summary :summary-method="getSummarys">
			<!-- <el-table-column type="selection" width="55"></el-table-column> -->
			<el-table-column prop="#" label="序号" width="60">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<!-- <el-table-column prop="id" label="ID" width="55"></el-table-column> -->
			<el-table-column prop="productExtend.imageUrl" label="图" width="42">
				<template #default="scope">
					<el-image v-if="scope.row.productExtend" style="width: 26px; height: 26px;display: block;"
						:src="scope.row.productExtend.imageUrl" :preview-src-list="[scope.row.productExtend.imageUrl]">
					</el-image>
					<el-image v-else style="width: 60px; height: 60px" :src="scope.row.imageUrl"
						:preview-src-list="[scope.row.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.productCode" label="商品编码">
				<template #default="scope">
					<span v-if="scope.row.productExtend">{{
            scope.row.productExtend.productCode
          }}</span>
					<span v-else>{{ scope.row.productCode }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.name" label="商品名称"  show-overflow-tooltip>
				<template #default="scope">
					<span v-if="scope.row.productExtend">{{
            scope.row.productExtend.name
          }}</span>
					<span v-else>{{ scope.row.name }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.barCode" label="条码"   show-overflow-tooltip>
				<template #default="scope">
					<span v-if="scope.row.productExtend">{{
            scope.row.productExtend.barCode
          }}</span>
					<span v-else>{{ scope.row.barCode }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="sku" label="规格" ></el-table-column>
			<el-table-column v-if="!openDetail" prop="materialUnit" label="单位" >
				<template #default="{ row }">
					<el-select v-if="row.productExtend" :disabled="openDetail" v-model="row.materialUnit"
						placeholder="请选择单位" @change="handleUnitChange(row)">
						<el-option :label="row.productExtend.productUnit.basicUnit"
							:value="row.productExtend.productUnit.basicUnit" />
						<el-option v-for="(item, index) in row.productExtend.productUnit.deputyList" :key="index"
							:label="item.deputyUnitName" :value="item.deputyUnitName" />
					</el-select>
					<el-select v-else :disabled="openDetail" v-model="row.materialUnit" placeholder="请选择单位"
						@change="handleUnitChange(row)">
						<el-option :label="row.productUnit.basicUnit" :value="row.productUnit.basicUnit" />
						<el-option v-for="(item, index) in row.productUnit.deputyList" :key="index"
							:label="item.deputyUnitName" :value="item.deputyUnitName" />
					</el-select>
				</template>
			</el-table-column>
			<el-table-column v-if="!openDetail" prop="operNumber" label="数量">
				<template #default="{ row }">
					<el-input v-model="row.operNumber" label="数量" @blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column v-if="openDetail" prop="materialUnit" label="单位" width="100"></el-table-column>
			<el-table-column v-if="openDetail" prop="operNumber" label="数量" width="100"></el-table-column>
			<el-table-column prop="basicNumber" label="数量(pcs)" width="100"></el-table-column>
			<el-table-column v-if="openDetail" prop="unitPrice" label="调出数量" width="100"></el-table-column>
			<el-table-column v-if="openDetail" prop="stockNum" label="调出数量(pcs)"
				width="100"></el-table-column><el-table-column v-if="openDetail" prop="discountRate" label="调入数量"
				width="100"></el-table-column>
			<el-table-column v-if="openDetail" prop="head.operTime" label="调入数量(pcs)" width="100"></el-table-column>
			<el-table-column prop="remark" label="备注" width="100"  show-overflow-tooltip>
			</el-table-column>
		</el-table>
		<template #footer>
			<div v-if="openDetail" class="dialog-footer">
				<el-button @click="dialogFormVisible">关 闭</el-button>
			</div>
			<div v-if="!openDetail" class="dialog-footer">
				<el-button type="primary" @click="editUser">保 存</el-button>
				<el-button @click="dialogFormVisible">取 消</el-button>
			</div>
		</template>
		<Products v-if="open_product" :open_product="open_product" :searchs="serachValue"
			@selectProduct="selectProducts($event)" @closeDialog="closeDialogFunc($event, 'Products')"></Products>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import StockApi from "@/api/stock.js";
	import Products from "@/components/table/products.vue";
	export default {
		components: {
			Products
		},
		data() {
			return {
				searchForm: {
					barCode: "",
					fileList: [],
					pageIndex: "",
					pageSize: "",
					imageId: "",
				},
				open_product: false,
				serachValue: "",
				form: {},
				searchs: {},
				valueList: [],
				allData: {},
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
				titleName: "",
				fullscreen: false,
			};
		},
		props: ["open_edit", "addForm", "editForm", "openDetail"],
		created() {
			this.dialogVisible = this.open_edit;
			this.searchForm = this.editForm;
			if (!this.openDetail) {
				let dialog = "";
				if (this.editForm.businessType == 5) {
					dialog = "【复制】实际调拨";
				} else if (this.editForm.businessType == 6) {
					dialog = "【复制】虚拟调拨";
				}
				this.titleName = dialog;
			} else {
				this.titleName = "调拨单详情";
			}
			this.productData = this.editForm.billItemList;
			console.log(this.addForm, "addform");
			this.depotList = this.addForm.depot;
			this.supplierList = this.addForm.supplier;
			// this.getselect();
		},
		methods: {
			handleOperNumberChange(row) {
				if (row.productExtend) {
					if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
						row.basicNumber = row.operNumber * 1;
					} else {
						const deputyUnit = row.productExtend.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						row.basicNumber = row.operNumber * deputyUnit.ratio;
					}
				} else {
					if (row.materialUnit == row.productUnit.basicUnit) {
						row.basicNumber = row.operNumber * 1;
					} else {
						const deputyUnit = row.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						row.basicNumber = row.operNumber * deputyUnit.ratio;
					}
				}
			},
			handleUnitChange(row) {
				if (!row.operNumber) {
					row.operNumber = 1; // 默认设置为1
					row.discountRate = row.discountRate || 10;
				}
				console.log(row, "row");
				if (row.productExtend) {
					if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
						row.basicNumber = row.operNumber * 1;
					} else {
						const deputyUnit = row.productExtend.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						row.basicNumber = row.operNumber * deputyUnit.ratio;
					}
				} else {
					if (row.materialUnit == row.productUnit.basicUnit) {
						row.basicNumber = row.operNumber * 1;
					} else {
						const deputyUnits = row.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						row.basicNumber = row.operNumber * deputyUnits.ratio;
					}
				}
			},
			editClick(row) {
				console.log(row, "row");
				this.allData = row.head;
				this.open_purchase = true;
			},
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_purchase = e.openDialog;
					if (e.type == "success") {
						this.$emit("closeDialog", {
							type: "success",
							openDialog: false,
						});
					}
				}
				if (f == "Products") {
					this.open_product = e.openDialog;
					if (e.type == "success") {}
				}
			},
			getselect() {
				let params = {};
				params.pageIndex = this.pageIndex;
				params.pageSize = this.pageSize;
				params.productExtendId = this.editForm.id;
				this.loading = true;
				StockApi.stockStockIndex(params, true)
					.then((res) => {
						this.productData = res.data.stock.records;

						this.loading = false;
					})
					.catch((e) => {
						console.log(e);
						this.loading = false;
					});
			},
			getSummarys({
				columns,
				data
			}) {
				let sums = [];
				// 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
				sums.push("总计");
				columns.forEach((column, index) => {
					if (
						column.property == "basicNumber" ||
						column.property == "operNumber" ||
						column.property == "allPrice"
					) {
						const values = data.map((item) => Number(item[column.property]));
						const sum = values.reduce((prev, curr) => prev + curr, 0);
						sums[index] = sum; // 将合计值添加到新数组中
					} else {
						sums.push(""); // 其他列的合计值为空
					}
				});

				return sums;
			},
			reserch() {
				let self = this;
				self.serachValue = self.searchForm.productCode;
				self.open_product = true;
			},
			selectProducts(m) {
				console.log(m, "m");
				this.open_product = m.openDialog;
				const newRecords = m.value;
				const mergedData = [...this.productData, ...newRecords];
				const uniqueData = Array.from(
					new Set(mergedData.map((item) => item.productExtendId))
				).map((id) => mergedData.find((item) => item.productExtendId === id));
				this.productData = uniqueData;
			},
			getStorageList(id) {
				let self = this;
				SettingApi.depotStorageToSet({
						depotStorageId: id
					})
					.then((res) => {})
					.catch((error) => {});
			},
			handNum(index) {
				this.form.options[index].optionId = index + 65;
				return String.fromCharCode(index + 65);
			},
			addOptions() {
				this.form.options = this.form.options || [];
				this.form.options.push({});
			},
			delImg(type, index) {
				if (type == 1) {
					delete this.form.imagePath;
					delete this.form.imageId;
				} else if (type == 2) {
					delete this.form.options[index].imagePath;
					delete this.form.options[index].imageId;
				}
			},
			changOptions(index) {
				let flag = this.form.options.every((v) => {
					return !(v.isAnswer == 1);
				});
				if (flag) {
					this.form.options[index].isAnswer = 1;
				} else {
					this.form.options.forEach((v) => {
						v.isAnswer = 0;
					});
					this.form.options[index].isAnswer = 1;
				}
			},
			removeOptions(index) {
				this.form.options.splice(index, 1);
				this.isDelete = 1;
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
					billItemId: option.billItemId,
				}));
				let params = {};
				params.depotId = self.searchForm.depotId;
				params.businessTime = self.searchForm.businessTime;
				params.businessType = self.searchForm.businessType;
				params.anotherDepotId = self.searchForm.anotherDepotId;
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				StockApi.stockAllotAdd(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "复制成功",
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
			/*上传*/
			openUpload(e) {
				this.type = e;
				this.isupload = true;
			},
			/*获取图片*/
			returnImgsFunc(e) {
				if (e != null && e.length > 0) {
					for (let img of e) {
						if (!this.searchForm.fileList) {
							this.searchForm.fileList = [];
						}
						this.searchForm.fileList.push({
							fileId: img.fileId,
							filePath: img.filePath,
						});
					}
				}
				this.isupload = false;
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