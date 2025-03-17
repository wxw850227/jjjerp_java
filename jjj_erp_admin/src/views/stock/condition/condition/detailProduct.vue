<template>
	<el-dialog title="单据详情" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">单据详情</div>
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
						<el-col :span="5">
							<el-form-item label="业务日期">
								<el-date-picker :disabled="openDetail" v-model="searchForm.businessTime" type="date"
									placeholder="请选择" :disabled-date="disabledDate" :shortcuts="shortcuts"
									:size="size" />
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="仓库">
								<el-select :disabled="openDetail" v-model="searchForm.depotId" placeholder="请选择">
									<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
										:value="item.depotId" />
								</el-select> </el-form-item></el-col>
						<el-col :span="5">
							<el-form-item label="单据类型">
								<el-input size="small" v-model="searchForm.typeName" :disabled="openDetail"
									placeholder=""></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="快递费用">
								<el-input size="small" v-model="searchForm.purchaseMoney" :disabled="openDetail"
									placeholder=""></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="4">
							<el-form-item label="业务员">
								<el-input size="small" v-model="searchForm.salesName" :disabled="openDetail"
									placeholder=""></el-input></el-form-item></el-col>
					</el-row>
					<el-row>
						<el-col :span="5"><el-form-item label="店铺">
								<el-input :disabled="openDetail" size="small" v-model="searchForm.storeName"
									placeholder=""></el-input> </el-form-item></el-col>
						<el-col :span="6">
							<el-form-item label="单据编号">
								<el-input :disabled="openDetail" size="small" v-model="searchForm.codeNum"
									placeholder=""></el-input> </el-form-item></el-col>
						<el-col :span="4">
							<el-form-item label="收货人">
								<el-input size="small" v-model="searchForm.address.name" :disabled="openDetail"
									placeholder=""></el-input> </el-form-item></el-col>
						<el-col :span="4">
							<el-form-item label="操作员">
								<el-input size="small" v-model="searchForm.creatorName" :disabled="openDetail"
									placeholder=""></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="单据总额">
								<el-input size="small" v-model="searchForm.totalPrice" :disabled="openDetail"
									placeholder=""></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="5">
							<el-form-item label="买家欠款">
								<el-input size="small" v-model="searchForm.deptMoney" :disabled="openDetail"
									placeholder=""></el-input></el-form-item></el-col>

						<el-col :span="5"><el-form-item label="买家名称">
								<el-input :disabled="openDetail" size="small" v-model="searchForm.customerName"
									placeholder=""></el-input> </el-form-item></el-col>
						<el-col :span="5">
							<el-form-item label="结算账户">
								<el-input :disabled="openDetail" size="small" v-model="searchForm.accountName"
									placeholder=""></el-input> </el-form-item></el-col>
						<el-col :span="4">
							<el-form-item label="优惠金额">
								<el-input :disabled="openDetail" size="small" v-model="searchForm.discountMoney"
									placeholder=""></el-input> </el-form-item></el-col>
							<el-col :span="5">
										<el-form-item label="收款金额">
											<el-input size="small" v-model="searchForm.deposit" :disabled="openDetail"
												placeholder=""></el-input>
										</el-form-item>
									</el-col>
					</el-row>
					<el-row>
						<!--          <el-col :span="6">-->
						<!--            <el-form-item label="服务费">-->
						<!--              <el-input-->
						<!--                size="small"-->
						<!--                v-model="searchForm.codeNum"-->
						<!--                :disabled="openDetail"-->
						<!--                placeholder=""-->
						<!--              ></el-input>-->
						<!--            </el-form-item>-->
						<!--          </el-col>-->
						<el-col :span="10">
							<el-form-item label="备注" class="ww100">
								<el-input size="small" v-model="searchForm.systemRemark" :disabled="openDetail"
									placeholder=""></el-input> </el-form-item></el-col>
						
						<!--          <el-col :span="6">-->
						<!--            <el-form-item label="预收款支付">-->
						<!--              <el-input-->
						<!--                size="small"-->
						<!--                v-model="searchForm.deposit"-->
						<!--                :disabled="openDetail"-->
						<!--                placeholder=""-->
						<!--              ></el-input>-->
						<!--            </el-form-item>-->
						<!--          </el-col>-->
					</el-row>
				</el-form>
			</div>
			<el-table size="small" :data="productData" border style="width: 100%"
				@selection-change="handleSelectionChange" v-loading="loading" show-summary
				:summary-method="getSummarys">
				<el-table-column prop="#" label="序号" width="60">
					<template #default="{ $index }">
						{{ $index + 1 }}
					</template>
				</el-table-column>

				<el-table-column prop="productExtend.productCode" label="规格编码" width="100"></el-table-column>
				<el-table-column prop="productExtend.name" label="商品名称" width="100"   show-overflow-tooltip></el-table-column>
				<el-table-column prop="sku" label="规格名称" width="100"   show-overflow-tooltip></el-table-column>
				<el-table-column prop="basicNumber" label="数量" width="100"></el-table-column>
				<el-table-column prop="productExtend.productUnit.basicUnit" label="基本单位" width="100"></el-table-column>
				<el-table-column prop="unitPrice" label="单价" width="100"></el-table-column>
				<el-table-column prop="discountRate" label="折扣率" width="100"></el-table-column>
				<el-table-column prop="discountPrice" label="折后单价" width="100"></el-table-column>
				<el-table-column prop="allPrice" label="折后金额小计" width="100"></el-table-column>
			</el-table>
		</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">我知道了</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
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
				searchs: {},
				valueList: [],
				config: {},
				productData: [],
				radio: 0,
				accountList: [],
				openDetail: true,
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
				fullscreen: false,
				loading: false,
				/*是否上传图片*/
				isupload: false,
				isDelete: 0,
				depotList: [],
				supplierList: [],
			};
		},
		props: ["open_purchase", "addForm", "allData"],
		created() {
			this.dialogVisible = this.open_purchase;
			this.searchForm = this.allData;
			console.log(this.allData, "allData");
			this.productData = this.allData.billItemList;
			this.productData.forEach((row) => {
				this.handlePurchaseCycleChange(row);
			});

			console.log(this.addForm, "addForm");
			this.depotList = this.addForm.depot;
			this.supplierList = this.addForm.supplier;
			this.getselect();
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
				row.discountPrice = (row.unitPrice * row.discountRate) / 100;

				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = row.discountPrice * row.basicNumber;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleUnitPriceChange(row) {
				// 计算折后单价
				row.discountPrice = (row.unitPrice * row.discountRate) / 100;

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
					row.unitPrice = row.costPrice || 1;
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
				if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
					row.basicNumber = row.operNumber * 1;
					row.unitPrice = row.unitPrice * 1;
					this.handleUnitPriceChange(row);
				} else if (row.materialUnit == row.productExtend.productUnit.otherUnit) {
					row.basicNumber = row.operNumber * row.productExtend.productUnit.ratio;
					row.unitPrice = row.unitPrice * row.productExtend.productUnit.ratio;
					this.handleUnitPriceChange(row);
				} else if (
					row.materialUnit == row.productExtend.productUnit.otherUnitTwo
				) {
					row.basicNumber =
						row.operNumber * row.productExtend.productUnit.ratioTwo;
					row.unitPrice = row.unitPrice * row.productExtend.productUnit.ratioTwo;
					this.handleUnitPriceChange(row);
				} else if (
					row.materialUnit == row.productExtend.productUnit.otherUnitThree
				) {
					row.basicNumber =
						row.operNumber * row.productExtend.productUnit.ratioThree;
					row.unitPrice =
						row.unitPrice * row.productExtend.productUnit.ratioThree;
					this.handleUnitPriceChange(row);
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
			getselect() {
				FinanceApi.accountList({})
					.then((res) => {
						this.accountList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
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
				// 检查是否选择了供应商和仓库
				if (!this.searchForm.supplierId || !this.searchForm.depotId) {
					// 如果未选择供应商或仓库，则给出提示信息
					ElMessage.error("供应商和仓库不能为空");
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
			/*关闭弹窗*/
			dialogFormVisible(e) {
				this.$emit("closeDialogs", {
					type: "error",
					openDialog: false,
				});
				// if (e) {
				//   this.$emit("closeDialogs", {
				//     type: "success",
				//     openDialog: false,
				//   });
				// } else {
				//   this.$emit("closeDialogs", {
				//     type: "error",
				//     openDialog: false,
				//   });
				// }
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