<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" :rules="formRules" ref="form"
				class="demo-form-inline">
				<el-form-item prop="storeId" label="店铺名称">
					<el-select v-model="searchForm.storeId" placeholder="请选择">
						<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
							:value="item.storeId" />
					</el-select>
				</el-form-item>
				<el-form-item prop="orderTime" label="下单时间">
					<el-date-picker v-model="searchForm.orderTime" type="date" placeholder="请选择"
						:disabled-date="disabledDate" :shortcuts="shortcuts" :size="size" />
				</el-form-item>
				<el-form-item label="订单号">
					<el-input size="small" v-model="searchForm.linkBillNum" placeholder="请输入订单号"></el-input>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select v-model="searchForm.depotId" placeholder="请选择">
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="供应商">
					<el-select v-model="searchForm.supplierId" placeholder="请选择">
						<el-option v-for="(item, index) in supplierList" :key="index" :label="item.supplierName"
							:value="item.supplierId" />
					</el-select>
				</el-form-item>

				<el-form-item label="配送方式">
					<el-select size="small" v-model="searchForm.deliveryType" placeholder="">
						<el-option label="全部" :value="0"></el-option>
						<el-option label="快递发货" :value="10"></el-option>
						<el-option label="上门自提" :value="20"></el-option>
						<el-option label="无需物流" :value="30"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item prop="nickName" label="选择客户">
					<el-input v-model="searchForm.nickName" clearable style="max-width: 600px" placeholder="">
						<template #append>
							<el-button @click="openCusomerList">
								<el-icon>
									<More />
								</el-icon>
							</el-button>
						</template>
					</el-input>
				</el-form-item>
				<el-form-item label="收件人">
					<el-input size="small" v-model="searchForm.name" clearable placeholder="">
					</el-input>
				</el-form-item>
				<el-form-item prop="phone" label="手机">
					<el-input size="small" v-model="searchForm.phone" clearable placeholder="">
					</el-input>
				</el-form-item>
				<el-form-item prop="billAddress" label="所在地区">
					<el-cascader style="width: 100%" clearable v-model="searchForm.regions" :options="areaData"
						:props="regionParams" @change="handleChange1"></el-cascader>
				</el-form-item>
				<el-form-item label="固话">
					<el-input size="small" v-model="searchForm.fixPhone" clearable placeholder="">
					</el-input>
				</el-form-item>
				<el-form-item prop="detail" label="详细地址">
					<el-input size="small" v-model="searchForm.detail" clearable placeholder="">
					</el-input>
				</el-form-item>
				<el-form-item label="邮编">
					<el-input size="small" v-model="searchForm.zip" clearable placeholder="">
					</el-input>
				</el-form-item>
				<el-form-item label="快速选择商品">
					<el-input size="small" v-model="searchForm.productCode" @keyup.enter="reserch()" clearable
						style="width: 240px" placeholder="输入编码/条码/名称按回车搜索">
						<template #append>
							<el-button size="small" type="primary" @click="reserch" icon="Search" />
						</template>
					</el-input>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1"></div>
			<!-- 分页 -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableDataWithEmptyRow" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading" show-summary :summary-method="getSummarys">
			<el-table-column prop="#" label="ID" width="54">
				<template #default="{ $index }">
					{{ $index + 1 }}
				</template>
			</el-table-column>
			<el-table-column label="操作" width="60">
				<template #default="scope">
					<el-button @click="closeClick(scope.row)" type="primary" link size="small">关闭</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="图" width="42">
				<template #default="{ row }">
					<div style="display: flex; align-items: center">
						<el-popover placement="right-start" trigger="hover" width="26">
							<template #reference>
								<el-avatar v-if="row.imageUrl" shape="square" :size="26" :src="row.imageUrl" />
							</template>
							<template #default>
								<el-avatar v-if="row.imageUrl" :size="26" shape="square" :src="row.imageUrl" />
							</template>
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="productCode" label="商品编码">
				<template #default="{ row }">
					<el-input @blur="ChangeShop(row)" v-model="row.productCode" />
					<div style="display: flex; align-items: center">
						<el-popover v-model="row.popoverVisible" placement="bottom" trigger="click" :width="400">
							<template #reference>
								<el-button ref="activateButton"
									style="opacity: 0; pointer-events: none; height: 1px"></el-button>
							</template>
							<div style="max-height: 300px; overflow-y: auto">
								<el-table :data="productList" border @row-click="handleRowDblClick">
									<el-table-column prop="imageUrl" label="图" width="42">
										<template #default="{ row }">
											<div style="display: flex; align-items: center">
												<el-popover placement="right-start" trigger="hover" width="26">
													<template #reference>
														<el-avatar v-if="row.imageUrl" shape="square" :size="26"
															:src="row.imageUrl" />
													</template>
													<template #default>
														<el-avatar v-if="row.imageUrl" :size="26" shape="square"
															:src="row.imageUrl" />
													</template>
												</el-popover>
											</div>
										</template>
									</el-table-column>
									<el-table-column prop="productCode" label="商品编码"></el-table-column>
									<el-table-column prop="name" label="商品名称"></el-table-column>
									<el-table-column prop="sku" label="规格名称"></el-table-column>
								</el-table>
							</div>
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="商品名称"></el-table-column>
			<el-table-column prop="sku" label="规格名称"></el-table-column>
			<el-table-column prop="weight" label="重量"></el-table-column>
			<el-table-column prop="materialUnit" label="单位">
				<template #default="{ row }">
					<el-select v-model="row.materialUnit" @change="handleUnitChange(row)" placeholder="请选择"
						v-if="row.productUnit">
						<el-option :label="row.productUnit.basicUnit" :value="row.productUnit.basicUnit" />
						<el-option v-for="(item, index) in row.productUnit.deputyList" :key="index"
							:label="item.deputyUnitName" :value="item.deputyUnitName" />
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="operNumber" label="数量">
				<template #default="{ row }">
					<el-input :disabled="!row.materialUnit" v-model="row.operNumber" label="数量"
						@blur="handleOperNumberChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="basicNumber" label="数量(pcs)"></el-table-column>
			<el-table-column prop="wholesaleDecimal" label="单价">
				<template #default="{ row }">
					<el-input v-model="row.wholesaleDecimal" label="单价" @blur="handleUnitPriceChange(row)" />
				</template>
			</el-table-column>
			<el-table-column prop="discountRate" label="折扣率" width="120">
				<template #default="{ row }">
					<el-input v-model="row.discountRate" style="width: 100px" @blur="handleDiscountRateChange(row)"
						:precision="0" label="折扣率">
						<template #append>%</template>
					</el-input>
				</template>
			</el-table-column>
			<el-table-column prop="discountPrice" label="折后金额"></el-table-column>
			<el-table-column prop="allPrice" label="应收款">
				<template #default="{ row }">
					<el-input v-model="row.allPrice" disabled />
				</template>
			</el-table-column>
			<el-table-column prop="remark" label="备注">
				<template #default="{ row }">
					<el-input v-model="row.remark" label="单价" />
				</template>
			</el-table-column>
		</el-table>
	</div>
	<div class="product-list bgset mt12">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-tabs v-model="activeName" @tab-change="handleClick">
				<el-tab-pane label="表单信息" name="detail">
					<template #label>
						<span>表尾信息 </span>
					</template>
				</el-tab-pane>
			</el-tabs>
		</div>
		<div class="product-content">
			<div class="table-wrap">
				<div v-if="activeName == 'detail'">
					<el-form size="small" :inline="true" :model="searchForm" :rules="formRules" ref="form"
						class="demo-form-inline dialogo-form-item">
						<el-row>
							<el-col :span="24">
								<el-form-item prop="purchaseMoney" label="运费:">
									<el-input style="width: 190px" @blur="changePurchaseMoney()"
										v-model="searchForm.purchaseMoney" placeholder="运费"></el-input>
								</el-form-item>
								<el-form-item>
									<el-select v-model="searchForm.purchaseAllotType" placeholder="请选择">
										<el-option label="不分摊" :value="0"></el-option>
										<el-option label="按金额分摊" :value="1"></el-option>
										<el-option label="按重量分摊" :value="2"></el-option>
									</el-select>
								</el-form-item>
								<el-form-item prop="discountMoney" label="优惠金额:">
									<el-input style="width: 190px" @blur="changeMoney()"
										v-model="searchForm.discountMoney" placeholder="优惠金额"></el-input>
								</el-form-item>
								<el-form-item>
									<el-select v-model="searchForm.otherAllotType" placeholder="请选择">
										<el-option label="不分摊" :value="0"></el-option>
										<el-option label="按金额分摊" :value="1"></el-option>
										<el-option label="按重量分摊" :value="2"></el-option>
									</el-select>
								</el-form-item>
								<el-form-item label="优惠后金额:">
									<el-input style="width: 190px" v-model="discountMoneys" disabled></el-input>
								</el-form-item>
								<el-form-item label="应收合计:">
									<el-input style="width: 190px" v-model="searchForm.totalPrice" disabled></el-input>
								</el-form-item>
								<el-form-item prop="deposit" label="实收金额:">
									<el-input style="width: 190px" @blur="changeDepositMoney()"
										v-model="searchForm.deposit" placeholder="实收金额"></el-input>
								</el-form-item>
								<el-form-item label="本次欠款:">
									<el-input style="width: 190px" v-model="searchForm.deptMoney" disabled></el-input>
								</el-form-item>
								<el-form-item label="总欠款:">
									<el-input style="width: 190px" v-model="searchForm.deptMoney" disabled></el-input>
								</el-form-item>

								<el-form-item label="付款类型">
									<el-select v-model="searchForm.payType" placeholder="请选择">
										<el-option label="现金" :value="0"></el-option>
										<el-option label="预付款" :value="1"></el-option>
									</el-select>
								</el-form-item>
								<el-form-item prop="accountId" label="结算账户">
									<el-select v-model="searchForm.accountId" placeholder="请选择">
										<el-option v-for="(item, index) in accountList" :key="index"
											:label="item.accountName" :value="item.accountId" />
									</el-select>
								</el-form-item>
							</el-col>
							<el-col>
								<el-form-item label="附件：" :rules="[{ required: true, message: '请上传附件' }]"
									prop="fileList">
									<div class="draggable-list small">
										<draggable class="wrapper" v-model="searchForm.fileList">
											<template #item="{ element, index }">
												<div class="item">
													<img v-img-url="element.filePath" />
													<a href="javascript:void(0);" class="delete-btn"
														@click.stop="deleteImg(index)">
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
									<div class="gray9">
										建议上传尺寸相同的图片，建议尺寸比例为1:1
									</div>
								</el-form-item>
								<!--上传图片组件-->
								<Upload v-if="isupload" :isupload="isupload" :config="config"
									@returnImgs="returnImgsFunc">上传图片</Upload>
							</el-col>
						</el-row>
					</el-form>
				</div>
				<div class="dialog-footer">
					<el-button size="small" type="primary" @click="addUser">新 增</el-button><el-button size="small"
						@click="reset">重 置</el-button>
				</div>
			</div>
		</div>
		<Customers v-if="isCustomer" :isCustomer="isCustomer" @selectValue="selectCustomer($event)"
			@closeDialog="closeDialogFunc($event, 'customers')"></Customers>
		<Products v-if="open_product" :open_product="open_product" :searchs="serachValue"
			@selectProduct="selectProducts($event)" @closeDialog="closeDialogFunc($event, 'Products')"></Products>
	</div>
</template>

<script>
	import MarketApi from "@/api/market.js";
	import ProductApi from "@/api/product.js";
	import Upload from "@/components/file/Upload.vue";
	import Customers from "@/components/table/customer.vue";
	import Products from "@/components/table/products.vue";
	import FinanceApi from "@/api/finance.js";
	import draggable from "vuedraggable";
	import SettingApi from "@/api/setting.js";
	export default {
		components: {
			Upload,
			draggable,
			Customers,
			Products,
		},
		data() {
			return {
				loading: true,
				open_print: false,
				activeName: "detail",
				num: 0,
				/*列表数据*/
				tableData: [],
				dialogVisible: false, // 控制浮框显示隐藏
				productList: [], // 从接口获取的商品列表数据
				billItemList: [],
				depotList: [],
				storeList: [],
				otherData: {},
				selectedRows: [], // 选中的行数据
				accountList: [],
				/*一页多少条*/
				pageSize: 20,
				/*总条数*/
				addForm: {},
				totalDataNumber: 0,
				allotPrice: "",
				open_add: false,
				open_edit: false,
				open_logistic: false,
				isCustomer: false,
				discountMoneys: "",
				printData: {},
				serachValue: "",
				/*搜索参数*/
				searchForm: {
					pageIndex: 1,
					pageSize: 20,
					barCode: "",
					fileList: [],
					billAddress: {},
					regions: [],
					pageIndex: "",
					pageSize: "",
					imageId: "",
					customerId: "", // 选择的客户ID
					phone: "",
					fixPhone: "",
					name: "",
					detail: "",
					nickName: "",
				},
				depots: [],
				areaData: [],
				supplierList: [],
				customerList: [],
				addressList: [],
				fileLists: [],
				isupload: false,
				openDetail: false,
				open_product: false,
				visible: false,
				regionParams: {
					label: "name",
					value: "id",
					children: "children",
					expandTrigger: "hover",
				},
				totalPrices: "",
				open_address: false,
				formRules: {
					storeId: [{
						required: true,
						message: "请选择店铺",
						trigger: "blur",
					}, ],
					orderTime: [{
						required: true,
						message: "请选择下单时间",
						trigger: "blur",
					}, ],
					phone: [{
						required: true,
						message: "请输入手机号",
						trigger: "blur",
					}, ],
					nickName: [{
						required: true,
						message: "请选择客户",
						trigger: "blur",
					}, ],

					detail: [{
						required: true,
						message: "请输入详情地址",
						trigger: "blur",
					}, ],
					billAddress: [{
						required: true,
						message: "请选择所在地区",
						trigger: "blur",
					}, ],
					purchaseMoney: [{
						required: true,
						message: "请输入运费",
						trigger: "blur",
					}, ],
					discountMoney: [{
						required: true,
						message: "请输入优惠金额",
						trigger: "blur",
					}, ],

					deposit: [{
						required: true,
						message: "请输入实收金额",
						trigger: "blur",
					}, ],
					accountId: [{
						required: true,
						message: "请输入结算账户",
						trigger: "blur",
					}, ],
				},
			};
		},
		computed: {
			tableDataWithEmptyRow() {
				return [
					...this.tableData,
					{
						productCode: "",
						allPrice: 0,
					},
				];
			},
			totalDataNumber() {
				return this.tableData.length;
			},
		},
		created() {
			/*获取列表*/
			this.getData();
			this.getselect();
		},
		methods: {
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
			selectProducts(m) {
				console.log(m, "m");
				this.open_product = m.openDialog;
				const newRecords = m.value;
				const mergedData = [...this.tableData, ...newRecords];
				const uniqueData = Array.from(
					new Set(mergedData.map((item) => item.productExtendId))
				).map((id) => mergedData.find((item) => item.productExtendId === id));
				this.tableData = uniqueData;
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
			isPopoverVisible(row) {
				return row;
			},
			handleRowDblClick(row) {
				this.tableData.push(row);
				row.popoverVisible = false;
				console.log(row, "row");
			},
			handleDialogClose() {
				this.dialogVisible = false;
			},
			ChangeShop(row) {
				this.loading = true;
				this.visible = true;
				ProductApi.productExtendIndex({
						productCode: row.productCode,
					})
					.then((res) => {
						this.loading = false;
						const newRecords = res.data.records;
						const mergedData = [...this.productList, ...newRecords];
						const uniqueData = Array.from(
							new Set(mergedData.map((item) => item.id))
						).map((id) => mergedData.find((item) => item.id === id));
						this.productList = uniqueData;
						this.$nextTick(() => {
							if (this.$refs.activateButton && this.$refs.activateButton.$el) {
								row.popoverVisible = true;
								this.$refs.activateButton.$el.click();
							}
						});
						// this.dialogVisible = true;
					})
					.catch((error) => {
						this.loading = false;
						console.error("Error fetching product data:", error);
					});
			},
			closeClick(row) {
				const index = this.tableData.findIndex((item) => item === row);
				if (index !== -1) {
					// 确保不删除最后一个空行
					if (!this.isEmptyRow(row) || index !== this.tableData.length - 1) {
						this.tableData.splice(index, 1);
					} else {
						ElMessage.error("无法删除最后一行空数据");
					}
				}
			},
			ensureEmptyRow() {
				if (
					this.tableData.length === 0 ||
					!this.isEmptyRow(this.tableData[this.tableData.length - 1])
				) {
					this.tableData.push({
						productCode: "",
					});
				}
			},
			isEmptyRow(row) {
				return !row.productCode;
			},
			adressClick() {
				this.$router.push({
					path: "/setting/basic/user/index",
				});
			},
			changeDepositMoney() {
				const totalPriced = localStorage.getItem("totalPriced");
				this.searchForm.deptMoney =
					Number(totalPriced) -
					Number(this.searchForm.deposit) -
					Number(this.searchForm.discountMoney);
				if (isNaN(this.searchForm.deptMoney)) {
					this.searchForm.deptMoney = 0;
				}
			},
			changePurchaseMoney() {
				const totalPriced = localStorage.getItem("totalPriced");
				if (this.discountMoneys != "") {
					this.searchForm.totalPrice =
						Number(this.discountMoneys) + Number(this.searchForm.purchaseMoney);
				} else {
					this.searchForm.totalPrice =
						Number(totalPriced) + Number(this.searchForm.purchaseMoney);
				}
				if (isNaN(this.searchForm.totalPrice)) {
					this.searchForm.totalPrice = 0;
				}
			},
			changeMoney() {
				const totalPriced = localStorage.getItem("totalPriced");
				this.allotPrice = this.searchForm.discountMoney;
				this.discountMoneys =
					Number(totalPriced) - Number(this.searchForm.discountMoney);
				this.searchForm.totalPrice =
					Number(this.searchForm.totalPrice) -
					Number(this.searchForm.discountMoney);
				if (isNaN(this.searchForm.totalPrice)) {
					this.searchForm.totalPrice = 0;
				}
				if (isNaN(this.discountMoneys)) {
					this.discountMoneys = 0;
				}
			},
			/*搜索查询*/
			reserch() {
				let self = this;
				self.serachValue = self.searchForm.productCode;
				self.open_product = true;
			},
			reset() {
				this.searchForm = {};
				this.tableData = [];
				this.totalDataNumber = 0;
				this.discountMoneys = "";
				this.getData();
			},
			addUser() {
				let self = this;
				let billItemList = self.tableData.map((option) => ({
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
					purchaseCycle: option.purchaseCycle,
					remark: option.remark,
					allotPrice: self.allotPrice,
				}));
				let filePaths = [];
				self.fileLists = JSON.parse(JSON.stringify(self.searchForm.fileList));
				for (let file of self.fileLists) {
					filePaths.push(file.filePath);
				}
				self.fileLists = filePaths;
				let params = self.searchForm;
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
				self.searchForm.billAddress = AddressList;

				params.fileList = self.fileLists;
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				MarketApi.marketOrderAdd(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "添加成功",
							type: "success",
						});
						self.reset();
					})
					.catch((error) => {
						console.log("错误提示");
						// self.searchForm.fileList = [];
						self.loading = false;
					});
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
				FinanceApi.accountList({})
					.then((res) => {
						this.accountList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
					});
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
					this.searchForm.totalPrice = row.allPrice;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
					this.searchForm.totalPrice = row.allPrice;
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
					row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
					this.searchForm.totalPrice = row.allPrice;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
					this.searchForm.totalPrice = row.allPrice;
				}
			},
			handleUnitPriceChange(row) {
				// 计算折后单价
				row.discountPrice = (row.wholesaleDecimal * row.discountRate) / 100;

				// 计算总价
				if (row.discountPrice && row.operNumber) {
					row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
					this.searchForm.totalPrice = row.allPrice;
				} else {
					row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
					this.searchForm.totalPrice = row.allPrice;
				}
			},
			handleUnitChange(row) {
				if (!row.operNumber) {
					localStorage.setItem("costPrice", row.costPrice);
					row.operNumber = 1; // 默认设置为1
					// 设置默认值为1
					row.wholesaleDecimal = row.costPrice || 1;
					row.discountRate = row.discountRate || 100;
					row.purchaseCycle = row.purchaseCycle || 0;
					// 计算折扣单价
					row.discountPrice = row.costPrice * row.discountRate;
					console.log(row.discountPrice, "discountPrice");
					// 计算总价
					if (row.discountPrice && row.operNumber) {
						row.allPrice = row.discountPrice * row.basicNumber;
						this.searchForm.totalPrice = row.allPrice;

						console.log(row.allPrice, "allproce");
					} else {
						row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
						console.log(row.allPrice, "1111222233");
						this.searchForm.totalPrice = row.allPrice;
					}
				}
				if (row.materialUnit == row.productUnit.basicUnit) {
					row.basicNumber = row.operNumber * 1;
					row.wholesaleDecimal = row.wholesaleDecimal * 1;
					this.handleUnitPriceChange(row);
				} else {
					const deputyUnit = row.productUnit.deputyList.find(
						(unit) => unit.deputyUnitName === row.materialUnit
					);
					if (deputyUnit) {
						row.basicNumber = row.operNumber * deputyUnit.ratio;
						row.wholesaleDecimal = row.wholesaleDecimal * deputyUnit.ratio;
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
			returnImgsFunc(e) {
				if (e != null && e.length > 0) {
					// 将新文件添加到 searchForm.fileList 中
					if (!this.searchForm.fileList) {
						console.log(this.searchForm, "serach");
						this.searchForm.fileList = [];
					}
					console.log(this.searchForm, "serach1");
					for (let img of e) {
						this.searchForm.fileList.push({
							fileId: img.fileId,
							filePath: img.filePath,
						});
					}
				}
				this.isupload = false;
			},
			logisticsClick(row) {
				this.open_logistic = true;
				this.categoryModel = row;
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
			getSummarys({
				columns,
				data
			}) {
				let sums = [];
				let totalPrice = 0;
				// 在计算合计值之前创建一个新的数组，并在开头插入 '总计' 字符串
				sums.push("总计");
				columns.forEach((column, index) => {
					if (
						column.property === "purchaseMoney" ||
						column.property === "otherMoney" ||
						column.property === "allPrice"
					) {
						const values = data.map((item) => Number(item[column.property]));
						const sum = values.reduce((prev, curr) => prev + curr, 0);
						if (isNaN(sum)) {
							console.warn(`合计值为 NaN，列名：${column.property}`);
							sums[index] = 0; // 将 NaN 替换为 0
						} else {
							sums[index] = sum; // 将合计值添加到新数组中
							if (column.property === "allPrice") {
								totalPrice = sum; // 如果当前列是总价，则将合计值赋给 totalPrice
							}
						}
					} else {
						sums.push(""); // 其他列的合计值为空
					}
				});
				console.log("总价:", totalPrice);
				if (!isNaN(totalPrice)) {
					this.totalPrices = totalPrice;
					console.log(this.totalPrices, "this.totalPrices");
					localStorage.setItem("totalPriced", this.totalPrices);
					this.searchForm.totalPrice = localStorage.getItem("totalPriced");
				} else {
					// 在这里你可以将 totalPriced 设置为默认值，比如 0
					this.totalPrices = 0;
					localStorage.setItem("totalPriced", this.totalPrices);
					this.searchForm.totalPrice = localStorage.getItem("totalPriced");
				}
				return sums;
			},
			getSummary({
				columns,
				data
			}) {
				let sums = [];
				let totalPrice = 0;
				// 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
				sums.push("合计");
				columns.forEach((column, index) => {
					if (
						column.property == "operNumber" ||
						column.property == "basicNumber" ||
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
				this.totalPrices = totalPrice;
				return sums;
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.searchForm.pageIndex = val;
				self.searchForm.pageSize = 20;
				self.loading = true;
				// let Params = self.searchForm;
				self.reserch();
			},
			handleSelectionChange(selection) {
				this.selectedRows = selection;
				console.log(this.selectedRows, "selectedRows");
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.pageIndex = 1;
				this.pageSize = val;
				this.reserch();
			},

			/*获取列表*/
			getData(param = "") {
				let self = this;
				let Params = {};
				Params.pageIndex = self.pageIndex;
				Params.pageSize = self.pageSize;
				if (param != "") {
					Params.score = param.score;
					Params.name = param.name;
				}
				MarketApi.marketOrderIndex(Params, true)
					.then((res) => {
						self.loading = false;
						self.storeList = res.data.store;
						self.depotList = res.data.depot;
						self.supplierList = res.data.supplier;
						// self.totalDataNumber = res.data.bill.total;
						self.num = res.data.num;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClick() {
				this.open_add = true;
				this.addForm = this.otherData;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
				this.openDetail = false;
			},
			detailClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.addForm = this.otherData;
				this.openDetail = true;
			},
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_add = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "edit") {
					this.open_edit = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "Products") {
					this.open_product = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "customers") {
					this.isCustomer = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
			},

			/*订单详情*/
			orderClick(row) {
				this.$router.push({
					path: "/product/comment/Order",
					query: {
						order_id: row.orderId,
					},
				});
			},
			authClick(row) {
				let self = this;
				// 弹出确认框，供用户选择同意或拒绝
				ElMessageBox.confirm("确定要审核改订单吗？", "审核采购单", {
						confirmButtonText: "同意",
						cancelButtonText: "拒绝",
						type: "warning",
					})
					.then(() => {
						// 用户选择同意，执行审核操作
						self.loading = true;
						MarketApi.orderAudit({
									billId: row.billId,
									auditStatus: 20,
									refuseReason: "", // 同意时不需要拒绝原因
								},
								true
							)
							.then((data) => {
								ElMessage({
									message: "审核成功",
									type: "success",
								});
								self.getData();
							})
							.catch((error) => {
								self.loading = false;
								self.getData();
							});
					})
					.catch(() => {
						// 用户选择拒绝，弹出输入拒绝原因的对话框
						ElMessageBox.prompt("拒绝原因", "审核采购单", {
								confirmButtonText: "确定",
								cancelButtonText: "取消",
								inputType: "textarea", // 将输入框类型设置为文本域
								inputPlaceholder: "请输入拒绝原因",
								inputPattern: /.+/,
								inputErrorMessage: "拒绝原因不能为空",
								roundButton: true,
							})
							.then(({
								value
							}) => {
								// 用户输入拒绝原因并点击确定
								if (value) {
									self.loading = true;
									MarketApi.orderAudit({
												billId: row.billId,
												auditStatus: 30,
												refuseReason: value, // 将输入的拒绝原因传递给接口
											},
											true
										)
										.then((data) => {
											ElMessage({
												message: "拒绝成功",
												type: "success",
											});
											self.getData();
										})
										.catch((error) => {
											self.loading = false;
											self.getData();
										});
								}
							})
							.catch(() => {});
					});
			},
			/*删除*/
			delClick(row) {
				let self = this;
				ElMessageBox.prompt("关闭原因", "关闭采购单", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						inputType: "textarea", // 将输入框类型设置为文本域
						inputPlaceholder: "请输入关闭原因",
						inputPattern: /.+/,
						inputErrorMessage: "关闭原因不能为空",
						roundButton: true,
					})
					.then(({
						value
					}) => {
						if (value) {
							self.loading = true;
							MarketApi.orderClose({
										billId: row.billId,
										closeReason: value, // 将输入的关闭原因传递给接口
									},
									true
								)
								.then((data) => {
									ElMessage({
										message: "关闭成功",
										type: "success",
									});
									self.getData();
								})
								.catch((error) => {
									self.loading = false;
									self.getData();
								});
						}
					})
					.catch(() => {});
			},
			openClick() {
				let self = this;
				let confirmMessage = "";
				if (self.selectedRows.length === 0) {
					ElMessage.warning("至少选择一条数据");
					return;
				}
				console.log(self.selectedRows, "selectedRows");
				if (self.selectedRows[0].enabled) {
					confirmMessage = "确认要禁用吗？";
				} else {
					confirmMessage = "确定要开启吗？";
				}
				const state = this.selectedRows[0].enabled ? 0 : 1;
				ElMessageBox.confirm(confirmMessage, "友情提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						const ids = this.selectedRows.map((row) => row.id).join(",");
						self.loading = true;
						PorductApi.unitSetStatus({
									ids: ids,
									state: state,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: "修改成功",
										type: "success",
									});
									self.getData();
								} else {
									self.loading = false;
								}
							})
							.catch((error) => {
								self.loading = false;
							});
					})
					.catch(() => {
						self.loading = false;
					});
			},

			onSubmit() {},

			/*切换选项卡*/
			handleClick(tab) {
				let self = this;
				self.pageIndex = 1;
				// self.loading = true;
				self.status = tab;
				// self.getData();
			},
		},
	};
</script>

<style scoped>
	/* .form-item-container {
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
	} */
</style>