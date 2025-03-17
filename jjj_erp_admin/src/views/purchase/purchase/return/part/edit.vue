<template>
	<el-dialog title="修改退货订单" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center>
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">修改退货订单</div>
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
							<el-date-picker v-model="searchForm.businessTime" type="date" placeholder="请选择"
								:disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
						</el-form-item>
					</el-col>
					<el-col :span="4">
						<el-form-item label="供应商">
							<el-select v-model="searchForm.supplierId" placeholder="请选择">
								<el-option :disabled="openDetail" v-for="(item, index) in supplierList" :key="index"
									:label="item.supplierName" :value="item.supplierId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="5">
						<el-form-item label="仓库">
							<el-select :disabled="openDetail" v-model="searchForm.depotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="5">
						<el-form-item label="业务员">
							<el-select v-model="searchForm.shopUserId" placeholder="请选择">
								<el-option v-for="(item, index) in salesList" :key="index" :label="item.userName"
									:value="item.shopUserId" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label="收货货位">
							<el-select v-model="searchForm.depotStorageId" placeholder="请选择">
								<el-option label="拣选货位" :value="0"></el-option>
								<el-option label="存储货位" :value="1"></el-option>
								<el-option label="异常货位" :value="2"></el-option>
							</el-select> </el-form-item></el-col>
				</el-row>
				<el-row>
					<el-col :span="9"><el-form-item label="备注" class="ww100">
							<el-input :disabled="openDetail" size="small" v-model="searchForm.remark"
								placeholder="请输入备注"></el-input> </el-form-item></el-col>
				</el-row>
				<el-row>
					<el-col :span="3">
						<el-form-item>
							<el-button size="small" type="primary" @click="reserch">导入商品明细</el-button>
						</el-form-item></el-col>
					<el-col :span="10">
						<el-form-item label="快速选择商品:" class="ww100">
							<el-input :disabled="openDetail" size="small" v-model="searchs.productCode"
								placeholder="输入编码/名称(精确到规格)后按回车">
								<template #append>
									<el-button :disabled="openDetail" @click="reserch" icon="Search" />
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
					<el-button :disabled="openDetail" @click="closeClick(scope.row)" type="primary" link
						size="small">关闭</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="图" width="42">
				<template #default="scope">
					<el-image v-if="scope.row.productExtend && scope.row.productExtend.imageUrl"
						style="width: 26px; height: 26px;display: block;" :src="scope.row.productExtend.imageUrl"
						:preview-src-list="[scope.row.productExtend.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.productCode" label="商品编码" width="100"></el-table-column>
			<el-table-column prop="productExtend.name" label="商品名称" width="100"></el-table-column>
			<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="100">
				<template #default="{ row }">
					<el-select v-if="row.productExtend && row.productExtend.productUnit" :disabled="openDetail"
						v-model="row.materialUnit" placeholder="请选择单位" @change="handleUnitChange(row)">
						<el-option :label="row.productExtend.productUnit.basicUnit"
							:value="row.productExtend.productUnit.basicUnit" />
						<el-option v-for="(item, index) in row.productExtend.productUnit.deputyList" :key="index"
							:label="item.deputyUnitName" :value="item.deputyUnitName" />
					</el-select>
					<el-select v-if="row.productUnit" :disabled="openDetail" v-model="row.materialUnit"
						placeholder="请选择单位" @change="handleUnitChange(row)">
						<el-option :label="row.productUnit.basicUnit" :value="row.productUnit.basicUnit" />
						<el-option v-for="(item, index) in row.productUnit.deputyList" :key="index"
							:label="item.deputyUnitName" :value="item.deputyUnitName" />
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="availableAllNumber" label="可出库总数" width="100"></el-table-column>
			<el-table-column prop="operNumber" label="实退数量" width="100">
				<template #default="{ row }">
					<el-input :disabled="openDetail" v-model="row.operNumber" label="数量"
						@blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="basicNumber" label="总数(pcs)" width="100">
				<template #default="{ row }">
					<el-input :disabled="openDetail" v-model="row.basicNumber" label="数量" />
				</template>
			</el-table-column>
			<el-table-column prop="commodityDecimal" label="吊牌价" width="100">
			</el-table-column>
			<el-table-column prop="purchaseDecimal" label="单价" width="100"><template #default="{ row }">
					<el-input :disabled="openDetail" v-model="row.purchaseDecimal" label="单价"
						@blur="handleUnitPriceChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="allPrice" label="总价" width="100"><template #default="{ row }">
					<el-input v-model="row.allPrice" disabled />
				</template>
			</el-table-column>
			<el-table-column prop="discountRate" label="折扣率" width="120"><template #default="{ row }">
					<el-input :disabled="openDetail" v-model="row.discountRate" @blur="handleDiscountRateChange(row)"
						:precision="0" label="折扣率">
						<template #append>%</template>
					</el-input>
				</template>
			</el-table-column>
			<el-table-column prop="discountPrice" label="折后单价" width="100"><template #default="{ row }">
					<el-input v-model="row.discountPrice" disabled />
				</template>
			</el-table-column>
			<el-table-column prop="allPrice" label="折后总价" width="100"><template #default="{ row }">
					<el-input v-model="row.allPrice" disabled />
				</template>
			</el-table-column>
			<el-table-column prop="remark" label="备注">
				<template #default="{ row }">
					<el-input :disabled="openDetail" v-model="row.remark" label="备注" />
				</template>
			</el-table-column>
		</el-table>
		<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline  dialogo-form-item">
			<el-row>
				<el-col :span="6">
					<el-form-item label="优惠类型">
						<el-select :disabled="openDetail" v-model="searchForm.discountType" placeholder="请选择">
							<el-option label="固定金额" :value="1"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="6">
					<el-form-item label="优惠金额">
						<el-input :disabled="openDetail" v-model="searchForm.discountMoney"
							@blur="discountMoneyChange(searchForm)" placeholder="请输入优惠金额"></el-input>
					</el-form-item>
				</el-col>
					<el-col :span="6">
					<el-form-item label="本次付款">
						<el-input :disabled="openDetail" v-model="searchForm.deposit"
							@blur="discountMoneyChange(searchForm)" placeholder="请输入本次付款"></el-input>
					</el-form-item>
				   </el-col>
					<el-col :span="6">
					<el-form-item label="欠款金额">
						<el-input v-model="searchForm.deptMoney" disabled></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="6">
					<el-form-item label="采购运费">
						<el-input :disabled="openDetail" v-model="searchForm.purchaseMoney"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="6">
					<el-form-item label="结算方式">
						<el-select :disabled="openDetail" v-model="searchForm.payType" placeholder="请选择">
							<el-option label="按金额分摊" :value="0"></el-option>
							<el-option label="按重量分摊" :value="1"></el-option>
							<el-option label="不分摊" :value="2"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="6">
					<el-form-item label="结算账户">
						<el-select :disabled="openDetail" v-model="searchForm.accountId" 
							placeholder="请选择">
							<el-option v-for="(item, index) in accountList" :key="index" :label="item.accountName"
								:value="item.accountId" />
						</el-select>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button v-if="!openDetail" type="primary" @click="editUser" :loading="loading">确 定</el-button>
			</div>
		</template>
		<Products v-if="open_product" :open_product="open_product" :searchs="serachValue"
			@selectProduct="selectProducts($event)" @closeDialog="closeDialogFunc($event, 'Products')"></Products>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
	import Products from "@/components/table/products.vue";
	import {
		formatDate
	} from "@/utils/DateTime.js";
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
					depotId: "",
					supplierId: "",
					imageId: "",
					deptMoney: 0.0,
				},
				open_product: false,
				serachValue: "",
				form: {},
				searchs: {},
				fullscreen: false,
				valueList: [],
				config: {},
				totalPrice: "",
				productData: [],
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
				salesList: [],
				billIds: [],
				codeNums: "",
				ids: "",
				number: "",
				basicnumbers: "",
			};
		},
		props: [
			"open_edit",
			"addForm",
			"businessType",
			"allData",
			"editForm",
			"openDetail",
		],
		created() {
			this.dialogVisible = this.open_edit;
			console.log(this.editForm, "editForm");
			console.log(this.addForm, "addForm");
			this.productData = this.editForm.billItemList.map(
				(item) => item.productExtend
			);
			// let billItemObject = this.editForm.reduce((obj, item) => {
			//   // 使用商品 ID 作为对象的键，商品对象作为值
			//   obj[item.billId] = item;
			//   return obj;
			// }, {});
			this.billIds = this.editForm.billId;
			this.searchForm = this.editForm;
			this.productData = this.editForm.billItemList;
			console.log(this.productData, "productData");
			console.log(this.searchForm, "searchForm");
			if (this.addForm) {
				this.depotList = this.addForm.depot;
				this.salesList = this.addForm.sales;
				this.supplierList = this.addForm.supplier;
			}
			this.codeNums = this.editForm.codeNum;
			this.ids = this.productData.map((item) => item.linkItemId);
			this.number = this.productData.map((item) => item.operNumber);
			this.basicnumbers = this.productData.map((item) => item.basicNumber);
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
			discountMoneyChange(row) {
				row.deptMoney = (
					this.totalPrice -
					(parseFloat(row.discountMoney) + parseFloat(row.deposit))
				).toFixed(2);
			},
			handleOperNumberChange(row) {
				if (
					row.productExtend &&
					row.productExtend.productUnit &&
					row.materialUnit == row.productExtend.productUnit.basicUnit
				) {
					row.basicNumber = row.operNumber * 1;
				} else {
					if (row.productExtend) {
						const deputyUnit = row.productExtend.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						row.basicNumber = row.operNumber * deputyUnit.ratio;
					} else {
						const deputyUnit = row.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
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
					row.purchaseDecimal = row.costPrice || 1;
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
				if (
					row.productExtend &&
					row.productExtend.productUnit &&
					row.materialUnit == row.productExtend.productUnit.basicUnit
				) {
					row.basicNumber = row.operNumber * 1;
					row.purchaseDecimal = row.purchaseDecimal * 1;
					this.handleUnitPriceChange(row);
				} else {
					if (row.productExtend) {
						const deputyUnit = row.productExtend.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						row.basicNumber = row.operNumber * deputyUnit.ratio;
						row.purchaseDecimal = row.purchaseDecimal * deputyUnit.ratio;
						this.handleUnitPriceChange(row);
					} else {
						const deputyUnits = row.productUnit.deputyList.find(
							(unit) => unit.deputyUnitName === row.materialUnit
						);
						console.log(deputyUnits, "deputyUnit2");
						row.basicNumber = row.operNumber * deputyUnits.ratio;
						row.purchaseDecimal = row.purchaseDecimal * deputyUnits.ratio;
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
				let totalPrice = 0;
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
						if (column.property === "allPrice") {
							totalPrice = sum; // 如果当前列是总价，则将合计值赋给 totalPrice
						}
					} else {
						sums.push(""); // 其他列的合计值为空
					}
				});
				this.totalPrice = totalPrice;
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
				let billItemList = self.productData.map((option) => ({
					productId: option.productId,
					productExtendId: option.productExtendId,
					sku: option.sku,
					materialUnit: option.materialUnit,
					operNumber: option.operNumber,
					basicNumber: option.basicNumber,
					unitPrice: option.purchaseDecimal,
					discountRate: option.discountRate,
					discountPrice: option.discountPrice,
					allPrice: option.allPrice,
					purchaseCycle: option.purchaseCycle,
					totalPrice: self.totalPrice,
					availableNumber: option.operNumber,
					availableAllNumber: option.basicNumber,
					linkItemId: option.linkItemId,
					billItemId: option.billItemId,
				}));
				self.searchForm.linkBillId = self.billIds;
				self.searchForm.linkBillNum = self.codeNums;
				self.searchForm.businessTime = formatDate(
					self.searchForm.businessTime,
					"YYYY-MM-DD"
				);
				//   self.searchForm.billId = "";
				//   self.searchForm.codeNum = "";
				let params = self.searchForm;
				params.businessType = 3;
				params.totalPrice = this.totalPrice;
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				PurchaseApi.returnEdit(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "修改成功",
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