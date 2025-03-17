<template>
	<el-dialog :title="titles" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">{{ titles }}</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<div class="common-seach-wrap column-dialog">
			<div>
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-row>
					<el-col :span="6">
						<el-form-item label="调拨单号">
							<el-input size="small" v-model="searchForm.code"
								placeholder="不填写则系统自动生成"></el-input></el-form-item></el-col>
					<el-col :span="6">
						<el-form-item label="业务时间" >
							<el-date-picker class="ww100" v-model="searchForm.businessTime" type="date" placeholder="请选择"
								:disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
						</el-form-item>
					</el-col>

					<el-col :span="6">
						<el-form-item label="调出仓库" class="ww100">
							<el-select v-model="searchForm.depotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="6">
						<el-form-item label="调入仓库" class="ww100">
							<el-select v-model="searchForm.anotherDepotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
				</el-row>
				<el-row>
					<el-col :span="10">
						<el-form-item label="备注" class="ww100">
							<el-input size="small" v-model="searchForm.remark"
								placeholder=""></el-input></el-form-item></el-col>
				</el-row>
				<el-row>
					<el-col :span="10">
						<el-form-item label="快速选择商品:">
							<el-input size="small" v-model="searchForm.productCode" @keyup.enter="reserch()"
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
			<el-table-column label="操作" width="60">
				<template #default="scope">
					<el-button @click="closeClick(scope.row)" type="primary" link size="small">删除</el-button>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="id" label="ID" width="55"></el-table-column> -->
			<el-table-column prop="imageUrl" label="图" width="80">
				<template #default="scope">
					<el-image style="width: 26px; height: 26px;display: block;" :src="scope.row.imageUrl"
						:preview-src-list="[scope.row.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="productCode" label="商品编码" width="100"></el-table-column>
			<el-table-column prop="name" label="商品名称" width="100"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="barCode" label="条码" width="100" ></el-table-column>
			<el-table-column prop="sku" label="规格" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="160">
				<template #default="{ row }">
					<el-select v-model="row.materialUnit" placeholder="请选择单位" @change="handleUnitChange(row)">
						<el-option :label="row.productUnit.basicUnit" :value="row.productUnit.basicUnit" />
						<el-option v-for="(item, index) in row.productUnit.deputyList" :key="index"
							:label="item.deputyUnitName" :value="item.deputyUnitName" />
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="operNumber" label="数量" width="100">
				<template #default="{ row }">
					<el-input v-model="row.operNumber" label="数量" @blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="basicNumber" label="数量(pcs)" width="100"></el-table-column>
			<el-table-column prop="remark" label="备注" width="100" show-overflow-tooltip>
			</el-table-column>
		</el-table>
		</div>
		<template #footer>
			<div class="dialog-footer">
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
	import {
		formatDate
	} from "@/utils/DateTime.js";
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
				titles: "",
				fullscreen: false,
			};
		},
		props: ["open_add", "addForm", "businessType"],
		created() {
			this.dialogVisible = this.open_add;
			console.log(this.addForm, "addform");
			this.depotList = this.addForm.depot;
			this.supplierList = this.addForm.supplier;
			if (this.businessType == 5) {
				this.titles = "实际调拨";
			} else if (this.businessType == 6) {
				this.titles = "虚拟调拨";
			} else {
				this.titles = "";
			}
			// this.getselect();
		},
		methods: {
			closeClick(row) {
				// 获取要移除的行的索引
				const index = this.productData.findIndex((item) => item === row);
				// 如果找到了对应的行
				if (index !== -1) {
					// 移除该行
					this.productData.splice(index, 1);
				}
			},
			handleOperNumberChange(row) {
				if (row.materialUnit == row.productUnit.basicUnit) {
					row.basicNumber = row.operNumber * 1;
				} else {
					const deputyUnit = row.productUnit.deputyList.find(
						(unit) => unit.deputyUnitName === row.materialUnit
					);
					if (deputyUnit) {
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
				if (row.materialUnit == row.productUnit.basicUnit) {
					row.basicNumber = row.operNumber * 1;
				} else {
					const deputyUnit = row.productUnit.deputyList.find(
						(unit) => unit.deputyUnitName === row.materialUnit
					);
					if (deputyUnit) {
						row.basicNumber = row.operNumber * deputyUnit.ratio;
					}
				}
			},
			editClick(row) {
				console.log(row, "row");
				this.allData = row.head;
				this.open_purchase = true;
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
			closeDialogFunc(e, f) {
				if (f == "Products") {
					this.open_product = e.openDialog;
					if (e.type == "success") {}
				}
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
				if (!self.searchForm.businessTime) {
					ElMessage.error("业务日期不能为空");
					return; // 不执行后续操作
				}
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
				params.businessType = self.searchForm.businessType;
				params.businessType = self.businessType;
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