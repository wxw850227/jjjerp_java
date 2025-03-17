<template>
	<el-dialog title="选择商品" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">选择商品</div>
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
					<el-col :span="8">
						<el-form-item label="快速选择商品:">
							<el-input size="small" v-model="searchs.productCode" placeholder="输入编码/名称(精确到规格)后按回车">
								<template #append>
									<el-button @click="reserch" icon="Search" />
								</template>
							</el-input> </el-form-item></el-col>
				</el-row>
			</el-form>
		</div>
		<el-table size="small" :data="productData" border style="width: 100%" @selection-change="handleSelectionChange"
			v-loading="loading" show-summary :summary-method="getSummarys">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="id" label="ID" width="55"></el-table-column>

			<el-table-column label="操作" width="60">
				<template #default="scope">
					<el-button @click="closeClick(scope.row)" type="primary" link size="small">关闭</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="图" width="80">
				<template #default="scope">
					<el-image v-if="scope.row.productExtend" style="width: 50px; height: 50px"
						:src="scope.row.productExtend.imageUrl" :preview-src-list="[scope.row.productExtend.imageUrl]">
					</el-image>
					<el-image v-else style="width: 50px; height: 50px" :src="scope.row.imageUrl"
						:preview-src-list="[scope.row.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.productCode" label="商品编码" width="100">
				<template #default="scope">
					<span>
						{{
              scope.row.productExtend
                ? scope.row.productExtend.productCode
                : scope.row.productCode
            }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.name" label="商品名称" width="100">
				<template #default="scope">
					<span>
						{{
              scope.row.productExtend
                ? scope.row.productExtend.name
                : scope.row.name
            }}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="100">
				<template #default="{ row }">
					<el-select v-if="row.productExtend" v-model="row.materialUnit" placeholder="请选择单位"
						@change="handleUnitChange(row)">
						<el-option v-for="(item, index) in row.productExtend.unitList" :key="index" :label="item.unit"
							:value="item.unit" />
					</el-select>
					<el-select v-else v-model="row.materialUnit" placeholder="请选择单位" @change="handleUnitChange(row)">
						<el-option v-for="(item, index) in row.unitList" :key="index" :label="item.unit"
							:value="item.unit" />
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="operNumber" label="数量" width="100">
				<template #default="{ row }">
					<el-input v-model="row.operNumber" label="数量" @blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="basicNumber" label="数量(pcs)" width="100">
				<template #default="{ row }">
					<el-input v-model="row.basicNumber" disabled label="数量" />
				</template>
			</el-table-column>
			<el-table-column prop="commodityDecimal" label="吊牌价" width="100">
			</el-table-column>
			<el-table-column prop="wholesaleDecimal" label="单价" width="100"><template #default="{ row }">
					<el-input v-model="row.wholesaleDecimal" label="单价" @blur="handleUnitPriceChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="discountRate" label="折扣率" width="120"><template #default="{ row }">
					<el-input v-model="row.discountRate" @blur="handleDiscountRateChange(row)" :precision="0"
						label="折扣率">
						<template #append>%</template>
					</el-input>
				</template>
			</el-table-column>
			<el-table-column prop="discountPrice" label="折后单价" width="100"><template #default="{ row }">
					<el-input v-model="row.discountPrice" disabled />
				</template>
			</el-table-column>
			<el-table-column prop="allotPrice" label="分摊金额" width="100"></el-table-column>
			<el-table-column prop="allPrice" label="总价" width="100"><template #default="{ row }">
					<el-input v-model="row.allPrice" disabled />
				</template>
			</el-table-column>
			<!-- <el-table-column prop="purchaseCycle" label="采购周期" width="120">
        <template #default="{ row }">
          <el-input
            v-model="row.purchaseCycle"
            @blur="handlePurchaseCycleChange(row)"
            label="采购周期"
          >
            <template #append>
              <span>天</span>
            </template>
          </el-input>
        </template>
      </el-table-column>
      <el-table-column prop="arrivalDate" label="预计到货日期" width="120"
        ><template #default="{ row }">
          <el-date-picker
            v-model="row.arrivalDate"
            type="date"
            placeholder="请选择"
          />
        </template>
      </el-table-column> -->
			<el-table-column prop="accountNum" label="货号"></el-table-column>
			<el-table-column prop="remark" label="备注">
				<template #default="{ row }">
					<el-input v-model="row.remark" label="备注" />
				</template>
			</el-table-column>
		</el-table>
		<Upload v-if="isupload" :isupload="isupload" :config="config" @returnImgs="returnImgsFunc">上传图片</Upload>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" @click="editUser" :loading="loading">确 定</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import MarketApi from "@/api/market.js";
	import FinanceApi from "@/api/finance.js";
	import Upload from "@/components/file/Upload.vue";
	import draggable from "vuedraggable";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	export default {
		components: {
			Upload,
			draggable,
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
				form: {},
				searchs: {},
				valueList: [],
				config: {},
				productData: [],
				fullscreen: false,
				radio: 0,
				accountList: [],
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
			};
		},
		props: ["open_edit", "addForm", "editForm"],
		created() {
			this.dialogVisible = this.open_edit;
			this.searchForm = {
				...this.editForm
			};
			if (this.searchForm.fileList) {
				const fileUrlsArray = this.searchForm.fileUrls.split(",");
				this.searchForm.fileList = fileUrlsArray.map((url) => ({
					filePath: url,
				}));
			}
			this.productData = this.editForm.billItemList;
			this.productData.forEach((row) => {
				this.handlePurchaseCycleChange(row);
			});
			console.log(this.editForm, "editForm");
			console.log(this.addForm, "addForm");
			this.depotList = this.addForm.depot;
			this.supplierList = this.addForm.supplier;
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
			handlePurchaseCycleChange(row) {
				// 确保采购周期只能输入数字
				row.purchaseCycle = parseInt(row.purchaseCycle) || 0;
				// 更新预计到货日期
				if (row.purchaseCycle > 0) {
					const today = new Date();
					today.setDate(today.getDate() + row.purchaseCycle);
					row.arrivalDate = today;
				} else {
					row.arrivalDate = ""; // 清空预计到货日期
				}
			},
			handleOperNumberChange(row) {
				if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
					row.basicNumber = row.operNumber * 1;
				} else if (row.materialUnit == row.productExtend.productUnit.otherUnit) {
					row.basicNumber = row.operNumber * row.productExtend.productUnit.ratio;
				} else if (
					row.materialUnit == row.productExtend.productUnit.otherUnitTwo
				) {
					row.basicNumber =
						row.operNumber * row.productExtend.productUnit.ratioTwo;
				} else if (
					row.materialUnit == row.productExtend.productUnit.otherUnitThree
				) {
					row.basicNumber =
						row.operNumber * row.productExtend.productUnit.ratioThree;
				}
				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = row.discountPrice * row.basicNumber;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleDiscountRateChange(row) {
				if (row.discountRate > 100) {
					row.discountRate = 100;
				}
				// 计算折后单价
				row.discountPrice = (row.wholesaleDecimal * row.discountRate) / 100;

				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = row.discountPrice * row.basicNumber;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleUnitPriceChange(row) {
				// 计算折后单价
				row.discountPrice = (row.wholesaleDecimal * row.discountRate) / 100;

				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = row.discountPrice * row.basicNumber;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleUnitChange(row) {
				if (!row.operNumber) {
					localStorage.setItem("costPrice", row.costPrice);
					row.operNumber = 1; // 默认设置为1
					// 设置默认值为1
					row.wholesaleDecimal = row.costPrice || 1;
					row.discountRate = row.discountRate || 10;
					// 计算折扣单价
					row.discountPrice = row.costPrice * row.discountRate;
					console.log(row.discountPrice, "discountPrice");
					// 计算总价
					if (row.discountPrice && row.operNumber) {
						row.allPrice = row.discountPrice * row.basicNumber;
						console.log(row.allPrice, "allproce");
					} else {
						row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
						console.log(row.allPrice, "1111222233");
					}
				}
				console.log(row, "row");
				if (row.productExtend) {
					if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
						row.basicNumber = row.operNumber * 1;
						row.wholesaleDecimal = row.wholesaleDecimal * 1;
						this.handleUnitPriceChange(row);
					} else if (
						row.materialUnit == row.productExtend.productUnit.otherUnit
					) {
						row.basicNumber =
							row.operNumber * row.productExtend.productUnit.ratio;
						row.wholesaleDecimal =
							row.wholesaleDecimal * row.productExtend.productUnit.ratio;
						this.handleUnitPriceChange(row);
					} else if (
						row.materialUnit == row.productExtend.productUnit.otherUnitTwo
					) {
						row.basicNumber =
							row.operNumber * row.productExtend.productUnit.ratioTwo;
						row.wholesaleDecimal =
							row.wholesaleDecimal * row.productExtend.productUnit.ratioTwo;
						this.handleUnitPriceChange(row);
					} else if (
						row.materialUnit == row.productExtend.productUnit.otherUnitThree
					) {
						row.basicNumber =
							row.operNumber * row.productExtend.productUnit.ratioThree;
						row.wholesaleDecimal =
							row.wholesaleDecimal * row.productExtend.productUnit.ratioThree;
						this.handleUnitPriceChange(row);
					}
				} else {
					if (row.materialUnit == row.productUnit.basicUnit) {
						row.basicNumber = row.operNumber * 1;
						row.wholesaleDecimal = row.wholesaleDecimal * 1;
						this.handleUnitPriceChange(row);
					} else if (row.materialUnit == row.productUnit.otherUnit) {
						row.basicNumber = row.operNumber * row.productUnit.ratio;
						row.wholesaleDecimal = row.wholesaleDecimal * row.productUnit.ratio;
						this.handleUnitPriceChange(row);
					} else if (row.materialUnit == row.productUnit.otherUnitTwo) {
						row.basicNumber = row.operNumber * row.productUnit.ratioTwo;
						row.wholesaleDecimal =
							row.wholesaleDecimal * row.productUnit.ratioTwo;
						this.handleUnitPriceChange(row);
					} else if (row.materialUnit == row.productUnit.otherUnitThree) {
						row.basicNumber = row.operNumber * row.productUnit.ratioThree;
						row.wholesaleDecimal =
							row.wholesaleDecimal * row.productUnit.ratioThree;
						this.handleUnitPriceChange(row);
					}
				}
			},
			deleteImg(index) {
				this.searchForm.fileList.splice(index, 1);
			},
			openProductUpload: function(file_type, file_name) {
				this.file_name = file_name;
				this.config = {
					total: 9,
				};
				this.isupload = true;
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
				let params = this.searchs;
				console.log(params, "params");
				ProductApi.productExtendIndex(params)
					.then((res) => {
						const newRecords = res.data.records;
						const mergedData = [...this.productData, ...newRecords];
						const uniqueData = Array.from(
							new Set(mergedData.map((item) => item.id))
						).map((id) => mergedData.find((item) => item.id === id));
						this.productData = uniqueData;
						ElMessage({
							message: "查询成功",
							type: "success",
						});
						if (this.productData.length > 0) {
							this.productData.forEach((row) => {
								row.materialUnit = row.productUnit.basicUnit;
								this.handleUnitChange(row);
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
					unitPrice: option.wholesaleDecimal,
					discountRate: option.discountRate,
					discountPrice: option.discountPrice,
					allPrice: option.allPrice,
					remark: option.remark,
					// purchaseCycle: option.purchaseCycle,
					// arrivalDate: formatDate(option.arrivalDate, "YYYY-MM-DD"),
					billItemId: option.billItemId,
				}));
				// let filePaths = [];
				// for (let file of self.searchForm.fileList) {
				//   filePaths.push(file.filePath);
				// }
				// self.searchForm.fileList = filePaths;
				// console.log(filePaths, "filePaths");
				// if (!filePaths) {
				//   ElMessage({
				//     message: "请选择图片",
				//     type: "warning",
				//   });
				//   return;
				// }
				let params = self.searchForm;

				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				MarketApi.marketOrderEdit(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "修改成功",
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