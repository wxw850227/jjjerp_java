<template>
	<el-dialog title="查看其他入库单单据详情" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					查看其他入库单单据详情
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
						<el-form-item label="业务日期">
							<el-date-picker :disabled="openDetail" v-model="searchForm.businessTime" type="date"
								placeholder="请选择" :disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label="业务原因">
							<el-select :disabled="openDetail" v-model="searchForm.businessCause" placeholder="请选择">
								<el-option label="报损" value="报损"></el-option>
								<el-option label="差异出库" value="差异出库"></el-option>
								<el-option label="其他" value="其他"></el-option>
								<el-option label="返厂维修" value="返厂维修"></el-option>
								<el-option label="借用出库" value="借用出库"></el-option>
							</el-select> </el-form-item></el-col>
					<el-col :span="4">
						<el-form-item label="经手人">
							<el-input :disabled="openDetail" size="small" v-model="searchForm.handleId"
								placeholder=""></el-input></el-form-item></el-col>

					<el-col :span="6">
						<el-form-item label="仓库">
							<el-select :disabled="openDetail" v-model="searchForm.depotId" placeholder="">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="4">
						<el-form-item label="快递">
							<el-select :disabled="openDetail" v-model="searchForm.expressId" placeholder="">
								<el-option v-for="(item, index) in expressList" :key="index" :label="item.expressName"
									:value="item.expressId" />
							</el-select> </el-form-item></el-col>					
				</el-row>
				<el-row>
					<el-col :span="6">
						<el-form-item label="备注">
							<el-input :disabled="openDetail" size="small" v-model="searchForm.remark"
								placeholder=""></el-input></el-form-item></el-col>
					<el-col :span="6">
						<el-form-item label="选择客户">
							<el-select :disabled="openDetail" v-model="searchForm.customerId" placeholder="请选择">
								<el-option v-for="(item, index) in customerList" :key="index" :label="item.realName"
									:value="item.customerId" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="客户地址">
							<el-select :disabled="openDetail" v-model="searchForm.customerAddressId" clearable
								placeholder="请选择">
								<el-option v-for="(address, index) in addressList" :key="index" :label="
                    address &&
                    address.city +
                      address.province +
                      address.region +
                      address.detail
                  " :value="address && address.addressId" />
							</el-select>
						</el-form-item>
					</el-col>
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
					<el-image style="width: 26px; height: 26px;display: block;" :src="scope.row.productExtend.imageUrl"
						:preview-src-list="[scope.row.productExtend.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="productExtend.productCode" label="商品编码" width="100"></el-table-column>
			<el-table-column prop="productExtend.name" label="商品名称" width="100" show-overflow-tooltip></el-table-column>
			<el-table-column prop="productExtend.barCode" label="条码" width="100"></el-table-column>
			<el-table-column prop="sku" label="规格" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="100"></el-table-column>
			<el-table-column prop="operNumber" label="数量" width="100"></el-table-column>
			<el-table-column prop="basicNumber" label="数量(pcs)" width="100"></el-table-column>
			<el-table-column prop="unitPrice" label="单价" width="100"></el-table-column>
			<el-table-column prop="allPrice" label="总价" width="100"></el-table-column>
			<el-table-column prop="depotStorageId" label="货位编码" width="100"></el-table-column>
			<el-table-column prop="remark" label="备注" width="100">
			</el-table-column>
		</el-table>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">关 闭</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import StockApi from "@/api/stock.js";
	import SettingApi from "@/api/setting.js";

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
				fullscreen: false,
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
				expressList: [],
				supplierList: [],
				customerList: [],
				addressList: [],
				titleName: "",
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
			this.expressList = this.addForm.express;
			this.supplierList = this.addForm.supplier;
			this.getselects();
			// setTimeout(() => {

			// }, 10000);
		},
		methods: {
			getselects() {
				SettingApi.userList({})
					.then((res) => {
						this.customerList = res.data.records;
						const selectedCustomer = this.customerList.find(
							(customer) => customer.customerId === this.searchForm.customerId
						);
						if (selectedCustomer) {
							this.addressList = selectedCustomer.addressList.find(
								(Id) => Id.addressId == this.searchForm.customerAddressId
							);
						}
						console.log(this.addressList, "this.addressList");
					})
					.catch((e) => {
						console.log(e);
					});
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