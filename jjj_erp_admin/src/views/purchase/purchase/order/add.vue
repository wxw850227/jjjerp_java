<template>
	<el-dialog title="新增采购订单" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center>
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">新增采购订单</div>
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
				<el-form size="small" :inline="true" :model="searchForm" :rules="formRules" ref="form"
					class="demo-form-inline">
					<el-row>
						<el-col :span="5">
							<el-form-item label="单据编号">
								<el-input size="small" v-model="searchForm.codeNum" placeholder="不填写则系统自动生成"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="业务日期">
								<el-date-picker v-model="searchForm.businessTime" type="date" placeholder="请选择"
									:disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item prop="supplierId" label="供应商">
								<el-select v-model="searchForm.supplierId" placeholder="请选择">
									<el-option v-for="(item, index) in supplierList" :key="index"
										:label="item.supplierName" :value="item.supplierId" />
								</el-select> </el-form-item></el-col>
						<el-col :span="5">
							<el-form-item prop="depotId" label="仓库">
								<el-select v-model="searchForm.depotId" placeholder="请选择">
									<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
										:value="item.depotId" />
								</el-select> </el-form-item></el-col>
						<el-col :span="4">
							<el-form-item label="业务员">
								<el-select v-model="searchForm.shopUserId" placeholder="请选择">
									<el-option v-for="(item, index) in salesList" :key="index" :label="item.userName"
										:value="item.shopUserId" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="10">
							<el-form-item label="备注" class="ww100">
								<el-input size="small" v-model="searchForm.systemRemark" placeholder="请输入备注"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="银行账号" class="ww100">
								<el-input size="small" v-model="searchForm.barCode" placeholder="请输入银行账号"></el-input>
							</el-form-item></el-col>
					</el-row>
					<el-row>
						<el-col :span="8">
							<el-form-item label="快速选择商品:" class="ww100">
								<el-input size="small" v-model="searchForm.productCode" @keyup.enter="reserch()"
									clearable placeholder="输入编码/名称后按回车">
									<template #append>
										<el-button @click="reserch" icon="Search" />
									</template>
								</el-input> </el-form-item></el-col>
					</el-row>
				</el-form>
			</div>
			<el-table size="small" :data="productData" border style="width: 100%"
				@selection-change="handleSelectionChange" v-loading="loading" show-summary
				:summary-method="getSummarys">
				<el-table-column type="selection" width="55"></el-table-column>
				<el-table-column prop="id" label="ID" width="55"></el-table-column>

				<el-table-column label="操作" width="60">
					<template #default="scope">
						<el-button @click="closeClick(scope.row)" type="primary" link size="small">删除</el-button>
						<!-- <el-button @click="delClick(scope.row)"  type="primary"
								  link size="small">
			          删除
			        </el-button> -->
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
				<el-table-column prop="name" label="商品名称" width="100" show-overflow-tooltip>
					<template #default="scope">
						<el-tooltip class="box-item" effect="dark" :content="scope.row.name" placement="top-start">
							<div class="text-ellipsis">{{scope.row.name}}</div>
						</el-tooltip>
					</template>
				</el-table-column>
				<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
				<el-table-column prop="materialUnit" label="单位" width="100">
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
				<el-table-column prop="basicNumber" label="数量(pcs)" width="100">
					<template #default="{ row }">
						<el-input v-model="row.basicNumber" label="数量" disabled />
					</template>
				</el-table-column>
				<el-table-column prop="commodityDecimal" label="吊牌价" width="100">
				</el-table-column>
				<el-table-column prop="purchaseDecimal" label="单价" width="100"><template #default="{ row }">
						<el-input v-model="row.purchaseDecimal" label="单价" @blur="handleUnitPriceChange(row)" />
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
				<!-- <el-table-column
			  prop="allotPrice"
			  label="分摊金额"
			  width="100"
			></el-table-column> -->
				<el-table-column prop="allPrice" label="总价" width="100"><template #default="{ row }">
						<el-input v-model="row.allPrice" disabled />
					</template>
				</el-table-column>
				<el-table-column prop="purchaseCycle" label="采购周期" width="120">
					<template #default="{ row }">
						<el-input v-model="row.purchaseCycle" @blur="handlePurchaseCycleChange(row)" label="采购周期">
							<template #append>
								<span>天</span>
							</template>
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="arrivalDate" label="预计到货日期" width="120"><template #default="{ row }">
						<el-date-picker v-model="row.arrivalDate" type="date" placeholder="请选择" />
					</template>
				</el-table-column>
				<el-table-column prop="accountNum" label="货号"></el-table-column>
				<el-table-column prop="remark" label="备注">
					<template #default="{ row }">
						<el-input v-model="row.remark" label="备注" />
					</template>
				</el-table-column>
			</el-table>
			<el-form size="small" :inline="true" :model="searchForm" :rules="formRules" ref="form"
				class="demo-form-inline dialogo-form-item">
				<el-row>
					<el-col :span="9">
						<el-form-item label="采购运费" class="ww100">
							<el-input size="small" v-model="searchForm.purchaseMoney" placeholder="请输入采购运费"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item>
							<el-select size="small" v-model="searchForm.purchaseAllotType" placeholder="请选择">
								<el-option label="不分摊" :value="0"></el-option>
								<el-option label="按金额分摊" :value="1"></el-option>
								<el-option label="按重量分摊" :value="2"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="9">
						<el-form-item prop="accountId" label="结算账户">
							<el-select size="small" v-model="searchForm.accountId" placeholder="请选择">
								<el-option v-for="(item, index) in accountList" :key="index" :label="item.accountName"
									:value="item.accountId" />
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="9">
						<el-form-item label="其他费用">
							<el-input size="small" v-model="searchForm.otherMoney" placeholder="请输入其他费用"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item>
							<el-select size="small" v-model="searchForm.otherAllotType" placeholder="请选择">
								<el-option label="不分摊" :value="0"></el-option>
								<el-option label="按金额分摊" :value="1"></el-option>
								<el-option label="按重量分摊" :value="2"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="9">
						<el-form-item label="预付金额">
							<el-input size="small" v-model="searchForm.deposit" placeholder="预付金额"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-form-item label="附件：" :rules="[{ required: true, message: '请上传附件' }]" prop="fileList" class="d-b-c">
					<div class="draggable-list small">
						<draggable class="wrapper" v-model="searchForm.fileList">
							<template #item="{ element, index }">
								<div class="item">
									<img v-img-url="element.filePath" />
									<a href="javascript:void(0);" class="delete-btn" @click.stop="deleteImg(index)">
										<el-icon>
											<CloseBold />
										</el-icon>
									</a>
								</div>
							</template>
						</draggable>
						<div class="item img-select" @click="openProductUpload('image', 'image')">
							<el-icon>
								<Plus />
							</el-icon>
						</div>
					</div>
				</el-form-item>
			</el-form>
		</div>


		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" @click="editUser" :loading="loading">确 定</el-button>
			</div>
		</template>
		<Products v-if="open_product" :open_product="open_product" :searchs="serachValue"
			@selectProduct="selectProducts($event)" @closeDialog="closeDialogFunc($event, 'Products')"></Products>

		<!--上传图片组件-->
		<Upload v-if="isupload" :isupload="isupload" :config="config" @returnImgs="returnImgsFunc">上传图片
		</Upload>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
	import Upload from "@/components/file/Upload.vue";
	import Products from "@/components/table/products.vue";
	import draggable from "vuedraggable";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	export default {
		components: {
			Upload,
			draggable,
			Products,
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
				fullscreen: false,
				open_product: false,
				valueList: [],
				config: {},
				productData: [],
				serachValue: "",
				radio: 0,
				accountList: [],
				formRules: {
					accountId: [{
						required: true,
						message: "请选择结算账户",
						trigger: "blur",
					}, ],
					supplierId: [{
						required: true,
						message: "请选择供应商",
						trigger: "blur",
					}, ],
					depotId: [{
						required: true,
						message: "请选择仓库",
						trigger: "blur",
					}, ],
					fileList: [{
						required: true,
						message: "请上传分类图片",
						trigger: "blur",
					}, ],
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
				salesList: [],
				supplierList: [],
			};
		},
		props: ["open_add", "addForm"],
		created() {
			this.dialogVisible = this.open_add;
			console.log(this.addForm, "addForm");
			this.depotList = this.addForm.depot;
			this.salesList = this.addForm.sales;
			this.supplierList = this.addForm.supplier;
			this.getselect();
		},
		methods: {
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
				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleDiscountRateChange(row) {
				if (row.discountRate > 100) {
					row.discountRate = 100;
				}
				// 计算折后单价
				row.discountPrice = (row.purchaseDecimal * row.discountRate) / 100;

				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleUnitPriceChange(row) {
				// 计算折后单价
				row.discountPrice = (row.purchaseDecimal * row.discountRate) / 100;

				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
				}
			},
			handleUnitChange(row) {
				if (!row.operNumber) {
					localStorage.setItem("costPrice", row.costPrice);
					row.operNumber = 1; // 默认设置为1
					// 设置默认值为1
					if (!row.purchaseDecimal) {
						row.purchaseDecimal = row.costPrice || 1;
					}
					row.discountRate = row.discountRate || 100;
					row.purchaseCycle = row.purchaseCycle || 0;
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
				if (row.materialUnit == row.productUnit.basicUnit) {
					row.basicNumber = row.operNumber * 1;
					row.purchaseDecimal = row.purchaseDecimal * 1;
					this.handleUnitPriceChange(row);
				} else {
					const deputyUnit = row.productUnit.deputyList.find(
						(unit) => unit.deputyUnitName === row.materialUnit
					);
					if (deputyUnit) {
						row.basicNumber = row.operNumber * deputyUnit.ratio;
						row.purchaseDecimal = row.purchaseDecimal * deputyUnit.ratio;
						this.handleUnitPriceChange(row);
					}
				}
			},
			deleteImg(index) {
				this.searchForm.fileList.splice(index, 1);
			},
			openProductUpload: function(file_type, file_name) {
				this.file_name = file_name;
				if (file_type == "image") {
					this.config = {
						total: 9,
						fileType: "image",
					};
				} else {
					this.config = {
						total: 1,
						fileType: "video",
					};
				}
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
				let self = this;
				self.serachValue = self.searchForm.productCode;
				self.open_product = true;
			},
			// reserch() {
			//   // 检查是否选择了供应商和仓库
			//   if (!this.searchForm.supplierId || !this.searchForm.depotId) {
			//     // 如果未选择供应商或仓库，则给出提示信息
			//     ElMessage.error("供应商和仓库不能为空");
			//     return; // 不执行后续操作
			//   }
			//   let params = this.searchs;
			//   console.log(params, "params");
			//   ProductApi.productExtendIndex(params)
			//     .then((res) => {
			//       const newRecords = res.data.records;
			//       const mergedData = [...this.productData, ...newRecords];
			//       const uniqueData = Array.from(
			//         new Set(mergedData.map((item) => item.id))
			//       ).map((id) => mergedData.find((item) => item.id === id));
			//       this.productData = uniqueData;
			//       ElMessage({
			//         message: "查询成功",
			//         type: "success",
			//       });
			//       if (this.productData.length > 0) {
			//         this.productData.forEach((row) => {
			//           row.materialUnit = row.productUnit.basicUnit;
			//           this.handleUnitChange(row);
			//         });
			//       }
			//       this.searchs = {};
			//     })
			//     .catch((error) => {});
			// },
			getStorageList(id) {
				let self = this;
				SettingApi.depotStorageToSet({
						depotStorageId: id,
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
					purchaseDecimal: option.purchaseDecimal,
					discountRate: option.discountRate,
					discountPrice: option.discountPrice,
					allPrice: option.allPrice,
					purchaseCycle: option.purchaseCycle,
					remark: option.remark,
					arrivalDate: option.purchaseCycle == 0 ?
						new Date() : formatDate(option.arrivalDate, "YYYY-MM-DD"),
				}));
				let filePaths = [];
				for (let file of self.searchForm.fileList) {
					filePaths.push(file.filePath);
				}
				self.searchForm.fileList = filePaths;
				let params = self.searchForm;
				params.businessTime = formatDate(
					self.searchForm.businessTime,
					"YYYY-MM-DD"
				);
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				PurchaseApi.orderAdd(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "添加成功",
							type: "success",
						});
						self.dialogFormVisible(true);
					})
					.catch((error) => {
						console.log("错误提示");
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
					// 将新文件添加到 searchForm.fileList 中
					for (let img of e) {
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
	/* .img {
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

	

	.select {
		padding-right: 40px;
	} */
</style>