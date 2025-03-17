<template>
	<el-dialog title="新增其他出库单" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					新增其他出库单
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
			<el-form size="small" :inline="true" :model="searchForm" :rules="formRules" ref="form"
				class="demo-form-inline">
				<el-row>
					<el-col :span="6">
						<el-form-item label="业务日期">
							<el-date-picker v-model="searchForm.businessTime" type="date" placeholder="请选择"
								:disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
						</el-form-item>
					</el-col>

					<el-col :span="6">
						<el-form-item prop="depotId" label="仓库">
							<el-select v-model="searchForm.depotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="6">
						<el-form-item prop="businessCause" label="业务原因">
							<el-select v-model="searchForm.businessCause" placeholder="请选择">
								<el-option label="报损" value="报损"></el-option>
								<el-option label="差异出库" value="差异出库"></el-option>
								<el-option label="其他" value="其他"></el-option>
								<el-option label="返厂维修" value="返厂维修"></el-option>
								<el-option label="借用出库" value="借用出库"></el-option>
							</el-select> </el-form-item></el-col>
					<el-col :span="6">
						<el-form-item prop="handleId" label="经手人">
							<el-input size="small" v-model="searchForm.handleId"
								placeholder=""></el-input></el-form-item></el-col>
				</el-row>
				<el-row>
					
					<el-col :span="6">
						<el-form-item prop="nickName" label="选择客户">
							<el-input v-model="searchForm.nickName" clearable placeholder="">
								<template #append>
									<el-button @click="openCusomerList">
										<el-icon>
											<More />
										</el-icon>
									</el-button>
								</template>
							</el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="收件人">
							<el-input size="small" v-model="searchForm.name" clearable placeholder="">
							</el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item prop="billAddress" label="所在地区">
							<el-cascader style="width: 100%" clearable v-model="searchForm.regions" :options="areaData"
								:props="regionParams" @change="handleChange1"></el-cascader>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item prop="detail" label="详细地址">
							<el-input size="small" v-model="searchForm.detail" clearable placeholder="">
							</el-input>
						</el-form-item>
					</el-col>
					</el-row>
					<el-row>
					<el-col :span="6">
						<el-form-item label="备注" class="ww100">
							<el-input size="small" v-model="searchForm.remark"
								placeholder=""></el-input></el-form-item></el-col>
					<el-col :span="6">
						<el-form-item prop="phone" label="手机"  class="ww100">
							<el-input size="small" v-model="searchForm.phone" clearable placeholder="">
							</el-input>
						</el-form-item>
					</el-col>
					
					<el-col :span="6">
						<el-form-item label="固话"  class="ww100">
							<el-input size="small" v-model="searchForm.fixPhone" clearable placeholder="">
							</el-input>
						</el-form-item>
					</el-col>
					
					<el-col :span="6">
						<el-form-item label="邮编"  class="ww100">
							<el-input size="small" v-model="searchForm.zip" clearable placeholder="">
							</el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="10">
						<el-form-item label="快速选择商品:"  class="ww100">
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
			<el-table-column prop="productCode" label="商品编码" width="100"></el-table-column>
			<el-table-column prop="name" label="商品名称" width="100"></el-table-column>
			<el-table-column prop="barCode" label="条码" width="100"></el-table-column>
			<el-table-column prop="sku" label="规格" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="100">
				<template #default="{ row }">
					<span>{{ row.productUnit.basicUnit }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="basicNumber" label="数量" width="100">
				<template #default="{ row }">
					<el-input v-model="row.basicNumber" label="数量" @blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="wholesaleDecimal" label="成本单价" width="100"></el-table-column>
			<el-table-column prop="allPrice" label="成本总价" width="100"></el-table-column>
			<el-table-column prop="remark" label="备注" width="100">
			</el-table-column>
		</el-table>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="editUser" type="primary">保 存</el-button>
				<el-button @click="dialogFormVisible">取 消</el-button>
			</div>
		</template>
		<Customers v-if="isCustomer" :isCustomer="isCustomer" @selectValue="selectCustomer($event)"
			@closeDialog="closeDialogFunc($event, 'customers')"></Customers>
		<Products v-if="open_product" :open_product="open_product" :searchs="serachValue"
			@selectProduct="selectProducts($event)" @closeDialog="closeDialogFunc($event, 'Products')"></Products>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import StockApi from "@/api/stock.js";
	import Customers from "@/components/table/customer.vue";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	import SettingApi from "@/api/setting.js";
	import Products from "@/components/table/products.vue";
	export default {
		components: {
			Customers,
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
					regions: [],
				},
				open_product: false,
				serachValue: "",
				form: {},
				fullscreen: false,
				isCustomer: false,
				searchs: {},
				valueList: [],
				allData: {},
				config: {},
				productData: [],
				radio: 0,
				accountList: [],
				open_purchase: false,
				open_address: false,
				formRules: {
					depotId: [{
						required: true,
						message: "请选择仓库",
						trigger: "blur",
					}, ],
					businessCause: [{
						required: true,
						message: "请选择业务原因",
						trigger: "blur",
					}, ],
					handleId: [{
						required: true,
						message: "请输入经手人",
						trigger: "blur",
					}, ],
					nickName: [{
						required: true,
						message: "请选择客户",
						trigger: "blur",
					}, ],
					phone: [{
						required: true,
						message: "请输入手机号",
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
				regionParams: {
					label: "name",
					value: "id",
					children: "children",
					expandTrigger: "hover",
				},
				supplierList: [],
				areaData: [],
				customerList: [],
				addressList: [],
			};
		},
		props: ["open_add", "addForm", "businessType"],
		created() {
			this.dialogVisible = this.open_add;
			console.log(this.addForm, "addform");
			this.depotList = this.addForm.depot;
			this.supplierList = this.addForm.supplier;
			this.getselect();
		},
		methods: {
			closeDialogFunc(e, f) {
				if (f == "customers") {
					this.isCustomer = e.openDialog;
					if (e.type == "success") {}
				}
				if (f == "Products") {
					this.open_product = e.openDialog;
					if (e.type == "success") {}
				}
			},
			handleChange(val) {
				const province = this.areaData.find((item) => item.id === val.provinceId);
				const city = province.children.find((item) => item.id === val.cityId);
				const region = city.children.find((item) => item.id === val.regionId);
				this.searchForm.provinceId = province.id;
				this.searchForm.cityId = city.id;
				this.searchForm.regionId = region.id || "";
				this.searchForm.province = province.name;
				this.searchForm.city = city.name;
				this.searchForm.region = region.name;
			},
			selectCustomer(e) {
				console.log(e, "e");
				this.isCustomer = e.openDialog;
				let customerList = e.value;
				this.handleChange(customerList);
				this.searchForm.regions = [
					this.searchForm.provinceId,
					this.searchForm.cityId,
					this.searchForm.regionId,
				].join(",");
				this.searchForm.regions = this.searchForm.regions
					.split(",")
					.map((id) => parseInt(id));

				this.searchForm.nickName = e.all.find(
					(item) => item.customerId == customerList.customerId
				).nickName;
				this.searchForm.customerId = customerList.customerId;
				this.searchForm.name =
					customerList.realName != undefined ?
					customerList.realName :
					customerList.name;
				this.searchForm.phone = customerList.phone;
				this.searchForm.detail = customerList.address;
				this.searchForm.zip = customerList.zip;
				console.log(this.searchForm, "this.searchForm");
			},
			openCusomerList() {
				this.isCustomer = true;
			},
			handleChange1(val) {
				console.log(val, "val");
				const province = this.areaData.find((item) => item.id === val[0]);
				const city = province.children.find((item) => item.id === val[1]);
				const region = city.children.find((item) => item.id === val[2]);
				this.searchForm.provinceId = province.id;
				this.searchForm.cityId = city.id;
				this.searchForm.regionId = region.id || "";
				this.searchForm.province = province.name;
				this.searchForm.city = city.name;
				this.searchForm.region = region.name;
			},
			handleOperNumberChange(row) {
				row.allPrice = parseInt(row.wholesaleDecimal) * parseInt(row.basicNumber);
			},
			editClick(row) {
				console.log(row, "row");
				this.allData = row.head;
				this.open_purchase = true;
			},
			adressClick() {
				this.$router.push({
					path: "/setting/basic/user/index",
				});
			},
			fetchAddressList() {
				const selectedCustomer = this.customerList.find(
					(customer) => customer.customerId === this.searchForm.customerId
				);
				if (selectedCustomer) {
					if (selectedCustomer.addressList != "") {
						this.addressList = selectedCustomer.addressList;
						this.open_address = false;
					} else {
						this.$forceUpdate();
						this.searchForm.customerAddressId = null;
						this.open_address = true;
					}
				}
			},
			getselect() {
				SettingApi.userList({})
					.then((res) => {
						this.customerList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
					});
				SettingApi.regionList().then((res) => {
					this.areaData = res.data;
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
					materialUnit: option.productUnit.basicUnit,
					operNumber: option.basicNumber,
					basicNumber: option.basicNumber,
					linkItemId: option.productUnit.basicUnitId,
				}));
				let params = {};
				let AddressList = {};
				AddressList.provinceId = self.searchForm.provinceId;
				AddressList.cityId = self.searchForm.cityId;
				AddressList.regionId = self.searchForm.regionId;
				AddressList.province = self.searchForm.province;
				AddressList.city = self.searchForm.city;
				AddressList.region = self.searchForm.region;
				AddressList.name = self.searchForm.name;
				AddressList.phone = self.searchForm.phone;
				AddressList.fixPhone = self.searchForm.fixPhone;
				AddressList.detail = self.searchForm.detail;
				params.billAddress = AddressList;
				params.depotId = self.searchForm.depotId;
				params.businessTime = formatDate(
					self.searchForm.businessTime,
					"YYYY-MM-DD"
				);
				params.businessCause = self.searchForm.businessCause;
				params.remark = self.searchForm.remark;
				params.handleId = self.searchForm.handleId;
				params.customerId = self.searchForm.customerId;
				params.customerAddressId = self.searchForm.customerAddressId;
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				StockApi.stockAllotOtherOutAdd(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "添加成功",
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