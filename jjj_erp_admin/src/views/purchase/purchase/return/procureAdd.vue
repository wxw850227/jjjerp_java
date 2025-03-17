<template>
	<el-dialog title="采购退货出库" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center>
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">采购退货出库</div>
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
								<el-option v-for="(item, index) in supplierList" :key="index" :label="item.supplierName"
									:value="item.supplierId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="5">
						<el-form-item label="仓库">
							<el-select v-model="searchForm.depotId" placeholder="请选择">
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
					<el-col :span="5">
						<el-form-item label="业务员">
							<el-select v-model="searchForm.sales_id" placeholder="请选择">
								<el-option label="拣选货位" :value="0"></el-option>
								<el-option label="存储货位" :value="1"></el-option>
								<el-option label="异常货位" :value="2"></el-option>
							</el-select> </el-form-item></el-col>
					<el-col :span="5">
						<el-form-item label="收货货位">
							<el-select v-model="searchForm.sales_id" placeholder="请选择">
								<el-option label="拣选货位" :value="0"></el-option>
								<el-option label="存储货位" :value="1"></el-option>
								<el-option label="异常货位" :value="2"></el-option>
							</el-select> </el-form-item></el-col>
				</el-row>
				<el-row>
					<el-col :span="9"><el-form-item label="备注" class="ww100">
							<el-input size="small" v-model="searchForm.remark" placeholder="请输入备注"></el-input> </el-form-item></el-col>
				</el-row>
			</el-form>
		</div>
		<el-table size="small" ref="table" :data="productData" border row-key="billItemId" :default-expand-all="false"
			style="width: 100%" :tree-props="{ children: 'billItemList' }" @select="handleSelect"
			@selection-change="handleMainListSelectionChange" v-loading="loading">
			<el-table-column type="selection" width="42"></el-table-column>
			<el-table-column prop="billId" label="ID" width="90"></el-table-column>
			<el-table-column prop="hint" label="提醒" width="80">
				<template #default="scope">
					<el-text effect="plain" type="danger" size="small" v-if="scope.row.hint == '未到货'">未到货</el-text>
					<el-text effect="plain" type="warning" size="small" v-if="scope.row.hint == '部分到货'">部分到货</el-text>
					<el-text effect="plain" type="success" size="small" v-if="scope.row.hint == '完成'">完成</el-text>
				</template>
			</el-table-column>
			<el-table-column prop="imageUrl" label="图" width="42">
				<template #default="{ row }">
					<el-image v-if="!isRootNode(row) && row.productExtend" style="width: 26px; height: 26px;display: block;"
						:src="row.productExtend.imageUrl" :preview-src-list="[row.productExtend.imageUrl]">
					</el-image>
				</template>
			</el-table-column>
			<el-table-column prop="codeNum" label="采购订单号" width="160"></el-table-column>
			<el-table-column prop="supplierName" label="供应商" width="100"></el-table-column>
			<el-table-column prop="depotName" label="收货仓库" width="100"></el-table-column>

			<el-table-column prop="productExtend.productCode" label="商品编码" width="100"></el-table-column>
			<el-table-column prop="productExtend.name" label="商品名称" width="100"></el-table-column>
			<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
			<el-table-column prop="productExtend.accountNum" label="货号" width="100"></el-table-column>
			<el-table-column prop="materialUnit" label="单位" width="100">
			</el-table-column>
			<el-table-column prop="operNumber" label="数量" width="100">
			</el-table-column>
			<el-table-column prop="basicNumber" label="数量(pcs)" width="100">
			</el-table-column>
			<!-- <el-table-column prop="availableNumber" label="可到货总数" width="100">
      </el-table-column>
      <el-table-column prop="availableNumber" label="发货数量" width="100">
      </el-table-column> -->
			<el-table-column prop="discountRate" label="折扣率" width="120">
			</el-table-column>
			<el-table-column prop="discountPrice" label="折后单价" width="100">
			</el-table-column>

			<el-table-column prop="remark" label="备注"> </el-table-column>
			<el-table-column prop="salesId" label="业务员" width="100">
			</el-table-column>
			<el-table-column prop="creatorId" label="创建人" width="100">
			</el-table-column>
		</el-table>
		<purchaseAdd v-if="open_purchase" :open_purchase="open_purchase" :addForm="addForm" :allData="allData"
			:businessType="businessType" @closeDialogs="closeDialogFunc($event, 'add')"></purchaseAdd>
	</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" @click="editUser" :loading="loading">确 定</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import FinanceApi from "@/api/finance.js";
	import purchaseAdd from "./part/add.vue";
	export default {
		components: {
			purchaseAdd
		},
		data() {
			return {
				searchForm: {
					barCode: "",
					fileList: [],
					pageIndex: "",
					pageSize: "",
					imageId: "",
					deptMoney: 0.0,
					operTime: "",
				},
				form: {},
				searchs: {},
				valueList: [],
				config: {},
				totalPrice: "",
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
				open_purchase: false,
				isDelete: 0,
				depotList: [],
				supplierList: [],
				selectedItems: [],
				allData: {},
			};
		},
		watch: {
			parentSelected(newValue) {
				// 父列表取消勾选时，取消所有子列表的勾选状态
				if (!newValue && this.selectedItems.length > 0) {
					this.cancelAllChildSelection();
				}
			},
		},
		props: ["open_procureAdd", "addForm", "businessType"],
		created() {
			this.dialogVisible = this.open_procureAdd;
			this.getselect();
			console.log(this.addForm, "addForm");
			if (this.addForm) {
				this.depotList = this.addForm.depot;
				this.supplierList = this.addForm.supplier;
			}
		},
		methods: {
			handleMainListSelectionChange(selection) {
				if (this.updating) return;
				this.updating = true;

				this.selectedItems = [...selection];
				console.log(selection, "selection");

				this.productData.forEach((row) => {
					if (this.isRootNode(row)) {
						const allChildrenSelected = row.billItemList.every((child) =>
							selection.includes(child)
						);
						const anyChildSelected = row.billItemList.some((child) =>
							selection.includes(child)
						);
						if (anyChildSelected && !selection.includes(row)) {
							this.$refs.table.toggleRowSelection(row, true);
							if (!this.selectedItems.includes(row)) {
								this.selectedItems.push(row);
							}
						} else if (!anyChildSelected && selection.includes(row)) {
							this.$refs.table.toggleRowSelection(row, false);
							this.selectedItems = this.selectedItems.filter(
								(item) => item.billItemId !== row.billItemId
							);
						}
					}
				});

				console.log(this.selectedItems, "selectedItems");
				this.updating = false;
			},
			handleSelect(selection, row) {
				if (this.updating) return;
				this.updating = true;

				if (this.isRootNode(row)) {
					row.billItemList.forEach((child) => {
						this.$refs.table.toggleRowSelection(child, selection.includes(row));
					});
					if (selection.includes(row)) {
						row.billItemList.forEach((child) => {
							if (!this.selectedItems.includes(child)) {
								this.selectedItems.push(child);
							}
						});
						if (!this.selectedItems.includes(row)) {
							this.selectedItems.push(row);
						}
					} else {
						this.selectedItems = this.selectedItems.filter(
							(item) =>
							!row.billItemList.includes(item) &&
							item.billItemId !== row.billItemId
						);
					}
				} else {
					const parentRow = this.findParentRow(this.productData, row);
					if (parentRow) {
						const allChildrenSelected = parentRow.billItemList.every((child) =>
							selection.includes(child)
						);
						const anyChildSelected = parentRow.billItemList.some((child) =>
							selection.includes(child)
						);
						if (allChildrenSelected) {
							this.$refs.table.toggleRowSelection(parentRow, true);
							if (!this.selectedItems.includes(parentRow)) {
								this.selectedItems.push(parentRow);
							}
						} else if (!anyChildSelected) {
							this.$refs.table.toggleRowSelection(parentRow, false);
							this.selectedItems = this.selectedItems.filter(
								(item) => item.billItemId !== parentRow.billItemId
							);
						}
					}
				}

				console.log(this.selectedItems, "selectedItems");
				this.updating = false;
			},
			findParentRow(productData, childRow) {
				for (let row of productData) {
					if (row.billItemList && row.billItemList.includes(childRow)) {
						return row;
					}
					if (row.billItemList) {
						const parentRow = this.findParentRow(row.billItemList, childRow);
						if (parentRow) {
							return parentRow;
						}
					}
				}
				return null;
			},
			isRootNode(row) {
				return row.billItemList;
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
				PurchaseApi.storageBill({
						billType: 2
					})
					.then((res) => {
						this.productData = res.data.bill.records.map((record) => ({
							...record,
							billItemId: record.billId,
						}));
						// this.productData = res.data.bill.records;
						console.log(this.productData, "this.productData");
					})
					.catch((e) => {
						console.log(e);
					});
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
				console.log(self.selectedItems)
				if (self.selectedItems.length == 0) {
					ElMessage.error("请选择至少一个订单");
					return;
				}
				let allHaveSupplierAndDepot = self.selectedItems.every(
					(item) => item.supplierId && item.depotId
				);
				if (allHaveSupplierAndDepot) {
					let firstSupplierId = self.selectedItems[0].supplierId;
					let firstDepotId = self.selectedItems[0].depotId;

					for (let i = 1; i < self.selectedItems.length; i++) {
						if (
							self.selectedItems[i].supplierId !== firstSupplierId ||
							self.selectedItems[i].depotId !== firstDepotId
						) {
							ElMessage.error("选择的采购订单供应商和仓库必须保持一致！");
							return;
						}
					}
				}

				let params = self.searchForm;
				params.businessType = this.businessType;
				params.totalPrice = this.totalPrice;
				params.billItemList = self.productData;
				params.selectedData = this.selectedItems;
				self.allData = params;
				console.log(self.allData, "params");
				self.open_purchase = true;
			},
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_purchase = e.openDialog;
					if (e.type == "success") {
						this.dialogVisible = false;
						this.$emit("closeDialog", {
							type: "success",
							openDialog: false,
						});
					}
				}
			},
			/*关闭弹窗*/
			dialogFormVisible(e) {
				if (e) {
					console.log(e, "e");
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