<template>
	<el-dialog title="详情" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">详情</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<div class="product-list">
			<!--搜索表单-->
			<div class="common-seach-wrap">
				<el-tabs v-model="activeName" @tab-change="handleClick">
					<el-tab-pane label="退回商品明细" name="detail">
						<template #label>
							<span>退回商品明细 </span>
						</template>
					</el-tab-pane>
					<el-tab-pane label="换出商品明细" name="details">
						<template #label>
							<span>换出商品明细 </span>
						</template>
					</el-tab-pane>
					<el-tab-pane label="收件地址" name="addressed">
						<template #label>
							<span>收件地址 </span>
						</template>
					</el-tab-pane>
					<el-tab-pane label="备注信息" name="remarks">
						<template #label>
							<span>备注信息 </span>
						</template>
					</el-tab-pane>
				</el-tabs>
			</div>

			<div class="product-content">
				<div class="table-wrap">
					<div v-if="activeName == 'detail'">
						<el-table size="small" :data="filteredBillItems" border style="width: 100%"
							@selection-change="handleSelectionChange" v-loading="loading" show-summary
							:summary-method="getSummary">
							<!-- <el-table-column prop="#"  width="55" ></el-table-column> -->
							<el-table-column prop="productId" label="ID" width="55"></el-table-column>
							<el-table-column prop="imageUrl" label="图" width="42">
								<template #default="{ row }">
									<div style="display: flex; align-items: center">
										<el-popover placement="right-start" trigger="hover" width="26">
											<template #reference>
												<el-avatar v-if="row.productExtend.imageUrl" shape="square" :size="26"
													:src="row.productExtend.imageUrl" />
												<el-avatar v-else :size="26" shape="square"
													src="/static/imgs/imagerror.png" />
											</template>
											<template #default>
												<el-avatar v-if="row.productExtend.imageUrl" :size="60" shape="square"
													:src="row.productExtend.imageUrl" />
												<el-avatar v-else :size="60" shape="square"
													src="/static/imgs/imagerror.png" />
											</template>
										</el-popover>
									</div>
								</template>
							</el-table-column>
							<el-table-column prop="productExtend.productCode" label="规格编码"></el-table-column>
							<el-table-column width="120" prop="productExtend.name" label="商品名称"  show-overflow-tooltip></el-table-column>
							<el-table-column prop="sku" label="规格名称"></el-table-column>
							<el-table-column prop="materialUnit" label="单位"></el-table-column>

							<el-table-column prop="barCode" label="规格条码"></el-table-column>
							<el-table-column prop="unitPrice" label="单价"></el-table-column>

							<el-table-column prop="discountPrice" label="折后单价"></el-table-column>
							<el-table-column prop="operNumber" label="数量"></el-table-column>
							<el-table-column prop="allPrice" label="应收款"></el-table-column>

							<el-table-column prop="productExtend.purchaseDecimal" label="参考进价"></el-table-column>

							<el-table-column prop="remark" label="备注"  show-overflow-tooltip></el-table-column>
						</el-table>
					</div>
					<div v-if="activeName == 'details'">
						<el-table size="small" :data="filteredBillItem" border style="width: 100%"
							@selection-change="handleSelectionChange" v-loading="loading" show-summary
							:summary-method="getSummary">
							<!-- <el-table-column prop="#"  width="55" ></el-table-column> -->
							<el-table-column prop="productId" label="ID" width="55"></el-table-column>
							<el-table-column prop="imageUrl" label="图"  width="42">
								<template #default="{ row }">
									<div style="display: flex; align-items: center">
										<el-popover placement="right-start" trigger="hover" width="26">
											<template #reference>
												<el-avatar v-if="row.productExtend.imageUrl" shape="square" :size="26"
													:src="row.productExtend.imageUrl" />
												<el-avatar v-else :size="26" shape="square"
													src="/static/imgs/imagerror.png" />
											</template>
											<template #default>
												<el-avatar v-if="row.productExtend.imageUrl" :size="60" shape="square"
													:src="row.productExtend.imageUrl" />
												<el-avatar v-else :size="60" shape="square"
													src="/static/imgs/imagerror.png" />
											</template>
										</el-popover>
									</div>
								</template>
							</el-table-column>
							<el-table-column prop="productExtend.productCode" label="规格编码"></el-table-column>
							<el-table-column width="120" prop="productExtend.name" label="商品名称"  show-overflow-tooltip></el-table-column>
							<el-table-column prop="sku" label="规格名称"></el-table-column>
							<el-table-column prop="materialUnit" label="单位"></el-table-column>

							<el-table-column prop="barCode" label="规格条码"></el-table-column>
							<el-table-column prop="unitPrice" label="单价"></el-table-column>

							<el-table-column prop="discountPrice" label="折后单价"></el-table-column>
							<el-table-column prop="operNumber" label="数量"></el-table-column>
							<el-table-column prop="allPrice" label="应收款"></el-table-column>

							<el-table-column prop="productExtend.purchaseDecimal" label="参考进价"></el-table-column>

							<el-table-column prop="remark" label="备注"  show-overflow-tooltip></el-table-column>
						</el-table>
					</div>
					<div v-if="activeName == 'addressed'" class="address-infor">
						<el-form size="small" :inline="true" :model="addressForm" class="demo-form-inline">
							<el-row :gutter="20">
								<el-col :span="8">
									<el-form-item label="收货人" class="ww100">
										<el-input v-model="addressForm.name" size="small" placeholder="请输入收货人"
											></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="手机"  class="ww100">
										<el-input size="small" v-model="addressForm.phone" placeholder="请输入手机"
											></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="8">
									<el-form-item label="固话"  class="ww100">
										<el-input size="small" v-model="addressForm.fixPhone" placeholder="请输入固话"
											></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="8">
									<el-form-item label="地区" prop="address"  class="ww100">
										<el-input v-model="regions" disabled size="small" placeholder=""
											></el-input>
									</el-form-item>
								</el-col>
								<el-col :span="16">
									<el-form-item label="详细地址" prop="address"  class="ww100">
										<el-input  v-model="addressForm.address" autocomplete="off"
											placeholder="请输入详细地址"></el-input>
									</el-form-item>
								</el-col>
							</el-row>
							<!-- <el-row   :gutter="20">
              <el-col :span="8"
                ><el-form-item label="真实姓名">
                  <el-input
                    size="small"
                    v-model="addressForm.remark"
                    placeholder="请输入真实姓名"
                    class="form_content_width"
                  ></el-input> </el-form-item
              ></el-col>
              <el-col :span="8">
                <el-form-item label="身份证号">
                  <el-input
                    size="small"
                    v-model="addressForm.barCode"
                    placeholder="请输入身份证号"
                    class="form_content_width"
                  ></el-input> </el-form-item
              ></el-col>
            </el-row> -->
						</el-form>
					</div>
					<div v-if="activeName == 'remarks'">
						<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
							<el-row :gutter="20">
								<el-col :span="8">
									<div class="form-item-container">
										<label class="form-item-label">买家留言/附加备注</label>
										<div class="form-item-content">
											<div class="content-box">
												<!-- 买家留言/附加备注的内容 -->
												<el-form-item label="内容1"></el-form-item>
												<el-form-item label="内容3"></el-form-item>
											</div>
										</div>
									</div>
								</el-col>
								<el-col :span="8">
									<div class="form-item-container">
										<label class="form-item-label">系统备注</label>
										<div class="form-item-content">
											<div class="content-box">
												<!-- 系统备注的内容 -->
												<el-input type="textarea" :rows="6" v-model="searchForm.barCode"
													placeholder="请输入系统备注"></el-input>
											</div>
										</div>
									</div>
								</el-col>
								<el-col :span="8">
									<div class="form-item-container">
										<label class="form-item-label">打印备注</label>
										<div class="form-item-content">
											<div class="content-box">
												<!-- 系统备注的内容 -->
												<el-input type="textarea" :rows="6" v-model="searchForm.barCode"
													placeholder="请输入打印备注"></el-input>
											</div>
										</div>
									</div>
								</el-col>
							</el-row>
							<el-row :gutter="20">
								<el-col :span="20">
									<div class="form-item-container">
										<label class="form-item-label">图片/附件
											<el-button size="small" type="primary"
												icon="UploadFilled">点击上传</el-button></label>
										<div class="form-item-content1">
											<div class="content-box">
												<!-- 买家留言/附加备注的内容 -->
												<el-form-item label="">
													<div class="content-box-container">
														<el-image style="
                                width: 48px;
                                height: 48px;
                                margin-right: 12px;
                              " src="/static/imgs/logo.png" preview-src-list="[/static/imgs/logo.png]">
														</el-image>
														<el-input type="textarea" v-model="searchForm.barCode"
															placeholder="请输入打印备注"></el-input>
													</div>
												</el-form-item>
												<el-form-item label="">
													<div class="content-box-container">
														<el-image style="
                                width: 48px;
                                height: 48px;
                                margin-right: 12px;
                              " src="/static/imgs/logo.png" preview-src-list="[/static/imgs/logo.png]">
														</el-image>
														<el-input type="textarea" v-model="searchForm.barCode"
															placeholder="请输入打印备注"></el-input>
													</div>
												</el-form-item>
											</div>
										</div>
									</div>
								</el-col>
							</el-row>
						</el-form>
					</div>
				</div>
			</div>
		</div>
		<Upload v-if="isupload" :isupload="isupload" :config="config" @returnImgs="returnImgsFunc">上传图片</Upload>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<!-- <el-button type="primary" @click="editUser" :loading="loading"
          >确 定</el-button
        > -->
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
				fullscreen: false,
				searchs: {},
				valueList: [],
				config: {},
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
				status: -1,
				activeName: "detail",
				loading: false,
				/*是否上传图片*/
				isupload: false,
				isDelete: 0,
				depotList: [],
				supplierList: [],
			};
		},
		props: ["open_detail", "addForm", "editForm", "filteredBillItem"],
		created() {
			this.dialogVisible = this.open_detail;
			this.addressForm = this.addForm;
			this.filteredBillItems = this.editForm;
		},
		methods: {
			handleClick(tab) {
				let self = this;
				self.status = tab;
			},
			handleSelectionChange(selection) {
				this.selectedRows = selection;
				console.log(this.selectedRows, "selectedRows");
			},
			getSummary({
				columns,
				data
			}) {
				let sums = [];
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
					unitPrice: option.unitPrice,
					discountRate: option.discountRate,
					discountPrice: option.discountPrice,
					allPrice: option.allPrice,
					remark: option.remark,
					purchaseCycle: option.purchaseCycle,
					arrivalDate: formatDate(option.arrivalDate, "YYYY-MM-DD"),
					id: option.id,
				}));
				// let filePaths = [];
				// if (self.searchForm.fileList) {
				//   for (let file of self.searchForm.fileList) {
				//     filePaths.push(file.filePath);
				//   }
				//   self.searchForm.fileList = filePaths;
				//   console.log(filePaths, "filePaths");
				// }

				// if (filePaths == "") {
				//   ElMessage({
				//     message: "请选择图片",
				//     type: "warning",
				//   });
				//   return;
				// }
				let params = self.searchForm;
				params.fileList = self.searchForm.fileList;
				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				MarketApi.marketOrderEdit(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "编辑成功",
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
/* 	.text-detail {
		width: 10px;
		height: 100%;
		margin: 5px;
		line-height: 20px;
		background-color: rgba(239, 239, 239, 1);
		text-align: center;
	}

	.text-center {
		white-space: nowrap;
		margin: 10px;
		padding: 10px;
	}

	.text-name {
		width: 47px;
		height: 20px;
		margin: 10px;
		color: rgba(16, 16, 16, 1);
		font-size: 14px;
		text-align: left;
		white-space: nowrap;
	}

	.text-product {
		margin: 10px;
		padding: 10px;
	}

	.imgurl {
		width: 58px;
		height: 58px;
		border-radius: 5px;
	}

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
	} */
.el-textarea{
		width: auto;
	}
	.form-item-container {
			display: flex;
			flex-direction: column;
		}
	
		.form-item-label {
			font-weight: bold;
			margin-bottom: 8px;
			font-size: 12px;
		}
	
		.form-item-content {
			background-color: #f2f2f2;
			padding: 12px;
			height: 144px;
			overflow: auto;
		}
	
		.form-item-content1 {
			background-color: #f2f2f2;
			width: 100%;
			padding: 12px;
		}
	
		.content-box {
			display: flex;
			flex-direction: column;
			background-color: #f2f2f2;
		}
	
		.content-box-container {
			width: auto;
			display: flex;
			flex-direction: row;
		}
	
</style>