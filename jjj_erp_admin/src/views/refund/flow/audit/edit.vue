<template>
	<el-dialog title="修改售后单" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">修改售后单</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-form size="small" :model="form" :rules="formRules" ref="form">
			<el-row :gutter="20">
				<el-col :span="24">
					<el-form-item label="售后类型">
						<el-radio-group v-model="form.refundType" @change="changeType" size="small">
							<el-radio :label="30">仅退款</el-radio>
							<el-radio :label="10">退货</el-radio>
							<el-radio :label="20">换货</el-radio>
							<el-radio :label="40">补发</el-radio>
							<el-radio :label="50">维修</el-radio>
							<el-radio :label="60">其他</el-radio>
						</el-radio-group>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="12">
					<el-form-item label="问题原因" prop="problemCause">
						<el-select v-model="form.problemCause" placeholder="请选择">
							<el-option label="物流赔付" value="物流赔付"></el-option>
							<el-option label="维修" value="维修"></el-option>
							<el-option label="其他" value="其他"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="问题描述" prop="problemDescribe" >
						<el-input v-model="form.problemDescribe" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="20">
				<el-col :span="8">
					<el-form-item label="订单号" prop="codeNum">
						<el-input disabled v-model="form.codeNum" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>

				<el-col :span="8">
					<el-form-item label="退款金额" prop="deposit">
						<el-input v-model="form.deposit" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="客户收款账户" prop="payee">
						<el-input v-model="form.payee" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="8">
					<el-form-item label="退入仓库" prop="depotId">
						<el-select disabled v-model="form.depotId" placeholder="请选择">
							<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
								:value="item.depotId" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="买家退回快递" prop="expressId">
						<el-select disabled v-model="form.expressId" placeholder="请选择快递公司">
							<el-option v-for="(item, index) in deliveryList" :label="item.expressName" :key="index"
								:value="item.expressId"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="买家退回单号" prop="express_no" >
						<el-input disabled v-model="form.expressNo" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="2">
					<el-link type="primary" class="links">客户信息</el-link>
				</el-col>
				<el-col :span="2">
					<el-link type="primary" class="links" @click="open_custom = !open_custom">点击修改</el-link>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="5">
					<el-form-item label="店铺" prop="storeId">
						<el-select :disabled="open_custom" v-model="form.storeId" placeholder="请选择">
							<el-option v-for="(item, index) in storeList" :key="index" :label="item.storeName"
								:value="item.storeId" />
						</el-select>
					</el-form-item>
				</el-col>
				<!--        <el-col :span="4">-->
				<!--          <el-form-item-->
				<!--            label="昵称/ID"-->
				<!--            prop="nick_name"-->
				<!--            :label-width="formLabelWidths"-->
				<!--          >-->
				<!--            <el-input-->
				<!--              :disabled="open_custom"-->
				<!--              v-model="form.nickName"-->
				<!--              autocomplete="off"-->
				<!--            ></el-input>-->
				<!--          </el-form-item>-->
				<!--        </el-col>-->
				<el-col :span="5">
					<el-form-item label="姓名" prop="name">
						<el-input :disabled="open_custom" v-model="form.address.name" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="5">
					<el-form-item label="手机" prop="phone">
						<el-input :disabled="open_custom" v-model="form.address.phone" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="5">
					<el-form-item label="固话" prop="fixPhone">
						<el-input :disabled="open_custom" v-model="form.address.fixPhone" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="4">
					<el-form-item label="邮编" prop="zip">
						<el-input :disabled="open_custom" v-model="form.address.zip" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row  :gutter="20">
				<el-col :span="8">
					<el-form-item label="选择客户">
						<el-select :disabled="open_custom" v-model="form.customerId" @change="fetchAddressList"
							placeholder="请选择">
							<el-option v-for="(item, index) in customerList" :key="index" :label="item.realName"
								:value="item.customerId" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item label="客户地址">
						<el-select :disabled="open_custom" v-model="form.customerAddressId" clearable placeholder="请选择">
							<el-option v-for="(address, index) in addressList" :key="index" :label="
                  address.city +
                  address.province +
                  address.region +
                  address.detail
                " :value="address.addressId" />
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="4" v-if="open_address">
					<el-form-item label="">
						<el-link type="primary" @click="adressClick">
							<el-icon>
								<Pointer />
							</el-icon>去添加客户地址</el-link>
					</el-form-item>
				</el-col>
				<el-col :span="8">
					<el-form-item prop="detail" label="详细地址" :label-width="formLabelWidths">
						<el-input :disabled="open_custom" v-model="form.address.detail" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<el-tabs v-model="activeTab" @tab-click="handleTabClick">
			<el-tab-pane v-if="form.refundType != 40" label="退款关联商品明细" name="address"></el-tab-pane>
			<el-tab-pane label="换出商品明细" name="reissue" v-if="
          form.refundType == 20 ||
          form.refundType == 40 ||
          form.refundType == 50 ||
          form.refundType == 60
        "></el-tab-pane>
			<el-tab-pane label="凭证" name="basic"></el-tab-pane>
		</el-tabs>

		<div v-show="activeTab === 'address'">
			<div class="common-level-rail">
				<el-button size="small" type="primary" icon="Plus" @click="addClick">新增</el-button>
			</div>

			<!--内容-->
			<div class="product-content">
				<div class="table-wrap">
					<el-table size="small" :data="tableDataWithEmptyRow" border style="width: 100%" v-loading="loading">
						<el-table-column prop="#" label="序号" width="60">
							<template #default="{ $index }">
								{{ $index + 1 }}
							</template>
						</el-table-column>
						<el-table-column label="操作" width="60">
							<template #default="scope">
								<el-button @click="closeClick(scope.row)" type="primary" link
									size="small">关闭</el-button>
							</template>
						</el-table-column>
						<el-table-column prop="imageUrl" label="图" width="42">
							<template #default="scope">
								<el-image v-if="
                    scope.row.productExtend && scope.row.productExtend.imageUrl
                  " style="width: 26px; height: 26px;display: block;" :src="scope.row.productExtend.imageUrl"
									:preview-src-list="[scope.row.productExtend.imageUrl]">
								</el-image>
								<el-image v-else style="width: 26px; height: 26px;display: block;" :src="scope.row.imageUrl"
									:preview-src-list="[scope.row.imageUrl]">
								</el-image>
							</template>
						</el-table-column>
						<el-table-column prop="productCode" label="商品编码">
							<template #default="{ row }">
								<div v-if="row.productExtend && row.productExtend.productCode">
									{{ row.productExtend.productCode }}
								</div>
								<div v-else>
									<el-input @blur="ChangeShop(row)" v-model="row.productCode" />
									<div style="display: flex; align-items: center">
										<el-popover v-model="row.popoverVisible" placement="bottom" trigger="click"
											:width="380">
											<template #reference>
												<el-button ref="activateButton"
													style="opacity: 0; pointer-events: none; height: 1px"></el-button>
											</template>
											<div style="max-height: 300px; overflow-y: auto">
												<el-table :data="productList" border @row-click="handleRowDblClick">
													<el-table-column prop="imageUrl" label="图" width="42">
														<template #default="{ row }">
															<div style="display: flex; align-items: center">
																<el-popover placement="right-start" trigger="hover"
																	width="26">
																	<template #reference>
																		<el-avatar v-if="row.imageUrl" shape="square"
																			:size="26" :src="row.imageUrl" />
																	</template>
																	<template #default>
																		<el-avatar v-if="row.imageUrl" :size="60"
																			shape="square" :src="row.imageUrl" />
																	</template>
																</el-popover>
															</div>
														</template>
													</el-table-column>
													<el-table-column prop="productCode" label="商品编码"></el-table-column>
													<el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
													<el-table-column prop="sku" label="规格名称"></el-table-column>
												</el-table>
											</div>
										</el-popover>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="name" label="商品名称" width="100" show-overflow-tooltip>
							<template #default="scope">
								<span
									v-if="scope.row.productExtend && scope.row.productExtend.name">{{ scope.row.productExtend.name }}</span>
								<span v-else>{{ scope.row.name }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
						<el-table-column prop="unitPrice" label="单价" width="100"></el-table-column>
						<el-table-column prop="discountPrice" label="折后单价" width="100"><template #default="{ row }">
								<el-input v-model="row.discountPrice" label="单价" @blur="handleUnitPriceChanges(row)" />
							</template>
						</el-table-column>
						<el-table-column prop="basicNumber" label="数量" width="100">
							<template #default="{ row }">
								<el-input v-model="row.basicNumber" label="数量" @blur="handleOperNumberChanges(row)" />
							</template>
						</el-table-column>
						<el-table-column prop="deposit" label="应退金额" width="100"><template #default="{ row }">
								<el-input v-model="row.deposit" @blur="handleDepositChanges(row)" />
							</template>
						</el-table-column>
						<el-table-column prop="remark" label="备注" show-overflow-tooltip>
							<template #default="{ row }">
								<el-input v-model="row.remark" label="备注" />
							</template>
						</el-table-column>
						<el-table-column label="操作" width="60">
							<template #default="{ $index }">
								<el-button @click="removeOptions($index)" type="primary" link size="small">
									<el-icon>
										<Delete />
									</el-icon>
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>
		<div v-if="activeTab === 'reissue'">
			<div class="common-level-rail">
				<el-button size="small" type="primary" icon="Plus" @click="addClick">新增</el-button>
			</div>

			<!--内容-->
			<div class="product-content">
				<div class="table-wrap">
					<el-table size="small" :data="tableDataWithEmptyRowS" border style="width: 100%"
						v-loading="loading">
						<el-table-column prop="#" label="序号" width="60">
							<template #default="{ $index }">
								{{ $index + 1 }}
							</template>
						</el-table-column>
						<el-table-column prop="imageUrl" label="图" width="42">
							<template #default="scope">
								<el-image v-if="
                    scope.row.productExtend && scope.row.productExtend.imageUrl
                  " style="width: 26px; height: 26px;display: block;" :src="scope.row.productExtend.imageUrl"
									:preview-src-list="[scope.row.productExtend.imageUrl]">
								</el-image>
								<el-image v-else style="width: 26px; height: 26px;display: block;" :src="scope.row.imageUrl"
									:preview-src-list="[scope.row.imageUrl]">
								</el-image>
							</template>
						</el-table-column>
						<el-table-column prop="productCode" label="商品编码">
							<template #default="{ row }">
								<div v-if="row.productExtend && row.productExtend.productCode">
									{{ row.productExtend.productCode }}
								</div>
								<div v-else>
									<el-input @blur="ChangeShop(row)" v-model="row.productCode" />
									<div style="display: flex; align-items: center">
										<el-popover v-model="row.popoverVisible" placement="bottom" trigger="click"
											:width="380">
											<template #reference>
												<el-button ref="activateButton"
													style="opacity: 0; pointer-events: none; height: 1px"></el-button>
											</template>
											<div style="max-height: 300px; overflow-y: auto">
												<el-table :data="productList" border @row-click="handleRowDblClickS">
													<el-table-column prop="imageUrl" label="图" width="42">
														<template #default="{ row }">
															<div style="display: flex; align-items: center">
																<el-popover placement="right-start" trigger="hover"
																	width="42">
																	<template #reference>
																		<el-avatar v-if="row.imageUrl" shape="square"
																			:size="26" :src="row.imageUrl" />
																	</template>
																	<template #default>
																		<el-avatar v-if="row.imageUrl" :size="60"
																			shape="square" :src="row.imageUrl" />
																	</template>
																</el-popover>
															</div>
														</template>
													</el-table-column>
													<el-table-column prop="productCode" label="商品编码" show-overflow-tooltip></el-table-column>
													<el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
													<el-table-column prop="sku" label="规格名称" show-overflow-tooltip></el-table-column>
												</el-table>
											</div>
										</el-popover>
									</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="name" label="商品名称" width="100" show-overflow-tooltip>
							<template #default="scope">
								<span
									v-if="scope.row.productExtend && scope.row.productExtend.name">{{ scope.row.productExtend.name }}</span>
								<span v-else>{{ scope.row.name }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
						<el-table-column prop="sku" label="单价" width="100"></el-table-column>
						<el-table-column prop="unitPrice" label="折后单价" width="100"><template #default="{ row }">
								<el-input v-model="row.unitPrice" label="单价" @blur="handleUnitPriceChange(row)" />
							</template>
						</el-table-column>
						<el-table-column prop="basicNumber" label="数量" width="100">
							<template #default="{ row }">
								<el-input v-model="row.basicNumber" label="数量" @blur="handleOperNumberChange(row)" />
							</template>
						</el-table-column>
						<el-table-column prop="total_price" label="金额" width="100"><template #default="{ row }">
								<el-input v-model="row.totalPrice" disabled />
							</template>
						</el-table-column>
						<el-table-column prop="remark" label="备注" show-overflow-tooltip>
							<template #default="{ row }">
								<el-input v-model="row.remark" label="备注" />
							</template>
						</el-table-column>
						<el-table-column label="操作" width="60">
							<template #default="{ $index }">
								<el-button @click="removeOptions($index)" type="primary" link size="small">
									<el-icon>
										<Delete />
									</el-icon>
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>
		<div v-show="activeTab === 'basic'">
			<el-form size="small" :model="form" :rules="formRules" ref="form">
				<el-col :span="16">
					<el-form-item label="附件：">
						<div class="draggable-list small">
							<draggable class="wrapper" v-model="form.fileList">
								<template #item="{ element, index }">
									<div class="item">
										<img v-if="element.filePath" :src="element.filePath" />
										<img v-else :src="element" />
										<a href="javascript:void(0);" class="delete-btn" @click.stop="deleteImg(index)">
											<el-icon>
												<CloseBold />
											</el-icon>
										</a>
									</div>
								</template>
							</draggable>
							<div class="item img-select" @click="openUpload">
								<el-icon>
									<Plus />
								</el-icon>
							</div>
						</div>
					</el-form-item>
				</el-col>
			</el-form>
			<Upload v-if="isProductUpload" :isProductUpload="isProductUpload" :config="config"
				@returnImgs="returnImgsFunc">上传图片</Upload>
		</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button type="primary" @click="addUser">保 存</el-button>
				<el-button @click="dialogFormVisible">取 消</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import SettingApi from "@/api/setting.js";
	import RefundApi from "@/api/refund.js";
	import FinanceApi from "@/api/finance.js";
	import Upload from "@/components/file/Upload.vue";
	import draggable from "vuedraggable";
	export default {
		components: {
			Upload,
			draggable,
		},
		data() {
			return {
				form: {
					regions: [],
					fileList: [],
				},
				value1: "",
				fullscreen: false,
				config: {},
				unitList: [],
				categoryList: [],
				mfrsList: [],
				specOptions2: [], // 与 skuTwo 相关的选择值数组
				specOptions3: [], // 与 skuThree 相关的选择值数组
				options: [],
				options1: [],
				tableData: [],
				tableDataS: [],
				options2: [],
				options3: [],
				deliveryList: [],
				formRules: {
					supplierType: [{
						required: true,
						message: "请输入供应商类型",
						trigger: "blur",
					}, ],
					unitId: [{
						required: true,
						message: "请输入单位",
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
				formLabelWidth: "110px",
				formLabelWidths: "60px",
				/*是否显示*/
				dialogVisible: false,
				loading: false,
				/*是否上传图片*/
				isProductUpload: false,
				skuOneTitle: "属性1",
				skuTwoTitle: "属性2",
				skuThreeTitle: "属性3",
				regionParams: {
					label: "name",
					value: "id",
					children: "children",
					expandTrigger: "hover",
				},
				skuOneList: [],
				skuTwoList: [],
				skuThreeList: [],
				activeTab: "address", // 默认显示基本信息标签页
				specNames: [], // 存储多规格选项名称
				specOptions: [], // 存储多规格选项的选择值
				manySkuSelected: 0,
				areaData: [],
				storeType: [],
				open_custom: true,
				productList: [],
				customerList: [],
				addressList: [],
				open_address: false,
				accountList: [],
				depotList: [],
				storeList: [],
			};
		},
		props: ["open_edit", "addForm", "editForm"],
		computed: {
			tableDataWithEmptyRow() {
				return [...this.tableData, {
					productCode: ""
				}];
			},
			tableDataWithEmptyRowS() {
				return [...this.tableDataS, {
					productCode: ""
				}];
			},
		},
		created() {
			this.dialogVisible = this.open_edit;
			if (this.addForm) {
				this.depotList = this.addForm.depot;

				this.storeList = this.addForm.store;
			}
			this.form = this.editForm;
			this.deliveryList = this.editForm.deliveryList;
			if (this.deliveryList.length > 0) {
				this.form.expressId = this.deliveryList[0].expressId;
				console.log(this.deliveryList[0].expressId, "deliveryList");
			}

			console.log(this.editForm, "editForm");
			let deposit = this.editForm.deposit;
			this.tableData = this.editForm.billItemList;
			// this.tableData = this.allData.filter(
			//   (item) => item.id && !item.billItemList
			// );
			// this.form.codeNum = this.allData
			//   .filter((item) => item.billItemList)
			//   .map((item) => item.codeNum)
			//   .join(", ");
			// this.form.billId = this.allData
			//   .filter((item) => item.billItemList)
			//   .map((item) => item.billId)
			//   .join(", ");

			console.log(this.tableData, "tableData");

			this.getselect();
		},
		methods: {
			closeClick(row) {
				const index = this.tableData.findIndex((item) => item === row);
				if (index !== -1) {
					this.tableData.splice(index, 1);
				}
			},
			handleUnitPriceChanges(row) {
				console.log(row);
				row.deposit = Number(row.discountPrice) * Number(row.basicNumber);
			},
			handleUnitPriceChange(row) {
				row.totalPrice = Number(row.discountPrice) * Number(row.basicNumber);
			},
			handleOperNumberChanges(row) {
				row.deposit = Number(row.discountPrice) * Number(row.basicNumber);
			},
			handleDepositChanges(row) {
				const deposit = Number(row.deposit);
				const operNumber = Number(row.basicNumber);

				if (isNaN(deposit) || isNaN(operNumber) || operNumber === 0) {
					row.discountPrice = "0.00"; // 或者其他适当的默认值或处理方式
				} else {
					row.discountPrice = (deposit / operNumber).toFixed(2);
				}
			},
			handleOperNumberChange(row) {
				row.totalPrice = Number(row.discountPrice) * Number(row.basicNumber);
			},
			changeType() {
				if (this.form.refundType == 10) {
					this.activeTab = "address";
				}
				if (this.form.refundType == 40) {
					this.activeTab = "reissue";
				}
			},
			fetchAddressList() {
				const selectedCustomer = this.customerList.find(
					(customer) => customer.customerId === this.form.customerId
				);
				if (selectedCustomer) {
					if (selectedCustomer.addressList != "") {
						this.addressList = selectedCustomer.addressList;
						this.open_address = false;
					} else {
						this.$forceUpdate();
						this.form.customerAddressId = null;
						this.open_address = true;
					}
				}
			},
			handleRowDblClick(row) {
				this.tableData.push(row);
				//   this.dialogVisible = false;
			},
			handleRowDblClickS(row) {
				this.tableDataS.push(row);
				//   this.dialogVisible = false;
			},
			ChangeShop(row) {
				this.loading = true;
				ProductApi.productExtendIndex({
						productCode: row.productCode
					})
					.then((res) => {
						this.loading = false;
						this.productList = res.data.records;
						this.$nextTick(() => {
							if (this.$refs.activateButton && this.$refs.activateButton.$el) {
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
			addClick() {
				const newData = {};
				// 将新数据添加到 tableData 数组中
				this.tableData.push(newData);
			},
			addClickS() {
				const newData = {};
				// 将新数据添加到 tableData 数组中
				this.tableDataS.push(newData);
			},
			removeOptions(index) {
				// 删除对应的行
				this.tableData.splice(index, 1);
			},
			removeOptionsS(index) {
				// 删除对应的行
				this.tableDataS.splice(index, 1);
			},
			getdata() {
				SettingApi.regionList().then((res) => {
					this.areaData = res.data;
				});
				SettingApi.userToAdd().then((res) => {
					this.storeType = res.data.platformType;
				});
			},
			adressClick() {
				this.$router.push({
					path: "/setting/basic/user/index",
				});
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
			handleChange1(val) {
				console.log(val, "val");
				const province = this.areaData.find((item) => item.id === val[0]);
				const city = province.children.find((item) => item.id === val[1]);
				const region = city.children.find((item) => item.id === val[2]);
				this.form.provinceId = province.id;
				this.form.cityId = city.id;
				this.form.regionId = region.id || "";
				this.form.province = province.name;
				this.form.city = city.name;
				this.form.region = region.name;
			},
			/*添加用户*/
			addUser() {
				let self = this;
				let billItemList = self.tableData.map((option) => ({
					productId: option.productId,
					productExtendId: option.productExtendId,
					sku: option.sku,
					materialUnit: option.materialUnit,
					operNumber: option.operNumber,
					basicNumber: option.basicNumber,
					unitPrice: option.unitPrice,
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
				let params = self.form;
				params.fileList = self.form.fileList.map((file) => {
					return file.filePath;
				});
				params.refundType = self.form.refundType;
				params.billItemList = billItemList;
				console.log(params, "params");
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						RefundApi.refundFlowEdit(params, true)
							.then((data) => {
								self.loading = false;
								ElMessage({
									message: "修改成功",
									type: "success",
								});
								self.dialogFormVisible(true);
							})
							.catch((error) => {
								self.loading = false;
							});
					}
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
			openUpload() {
				this.isProductUpload = true;
			},
			deleteImg(index) {
				this.form.fileList.splice(index, 1);
			},
			/*获取图片*/
			returnImgsFunc(e) {
				console.log(this.form);
				if (e != null) {
					this.form.fileList = [];
					let imgs = this.form.fileList.concat(e);
					// 兼容后端
					for (let i = 0; i < imgs.length; i++) {
						if (typeof imgs[i].imageId == "undefined") {
							imgs[i].imageId = imgs[i].fileId;
						}
					}
					this.form["fileList"] = imgs;
				}
				this.isProductUpload = false;
			},
		},
	};
</script>

<style scoped>
	/* .img {
		margin-top: 10px;
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
*/
	.links {
		font-size: 14px;
		font-weight: 400;
		margin: 0 6px 6px 6px;
		white-space: nowrap;
	} 
</style>