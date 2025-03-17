<template>
	<el-dialog title="库存变化详情" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">库存变化详情</div>
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
					<el-col :span="6">
						<el-form-item label="出库时间" class="ww100">
							<el-date-picker @change="handleStartDateChange" size="small" v-model="startDate" type="date"
								value-format="YYYY-MM-DD" placeholder="出库时间">
							</el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="入库时间" class="ww100">
						<el-date-picker @change="handleEndDateChange" size="small" v-model="endDate" type="date"
							value-format="YYYY-MM-DD" placeholder="入库时间">
						</el-date-picker>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="商品" >
							<el-input :disabled="openDetail" size="small" v-model="searchForm.name"
								placeholder="请输入商品编号/名称"></el-input></el-form-item></el-col>
					<el-col :span="6">
						<el-form-item label="仓库"  class="ww100">
							<el-select :disabled="openDetail" v-model="searchForm.depotIdList" placeholder="请选择"
								 multiple>
								<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
									:value="item.depotId" />
							</el-select> </el-form-item></el-col>
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
			<!-- <el-table-column prop="imageUrl" label="图" width="80">
        <template #default="scope">
          <el-image
            style="width: 50px; height: 50px"
            :src="scope.row.imageUrl"
            :preview-src-list="[scope.row.imageUrl]"
          >
          </el-image>
        </template>
      </el-table-column> -->
			<el-table-column prop="head.businessTime" label="业务日期" width="130"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="head.typeName" label="单据类型" width="100"></el-table-column>
			<el-table-column prop="head.codeNum" label="单据编号" width="100"   show-overflow-tooltip></el-table-column>
			<el-table-column prop="head.depotName" label="仓库" width="100"></el-table-column>
			<el-table-column prop="productExtend.name" label="商品名称" width="100"   show-overflow-tooltip></el-table-column>
			<el-table-column prop="sku" label="规格名称" width="100"></el-table-column>
			<el-table-column prop="differenceNum" label="发生数量" width="100"></el-table-column>
			<el-table-column prop="unitPrice" label="发生成本" width="100"></el-table-column>
			<el-table-column prop="stockNum" label="库存余量" width="100"></el-table-column><el-table-column
				prop="discountRate" label="库存成本" width="100"></el-table-column>
			<el-table-column prop="head.operTime" label="出入库时间" width="130"  show-overflow-tooltip></el-table-column>
			<el-table-column prop="" label="单据" width="100">
				<template #default="scope">
					<el-button @click="editClick(scope.row)" type="primary" link size="small">单据</el-button>
				</template>
			</el-table-column>
		</el-table>
		
		<detailProduct v-if="open_purchase" :open_purchase="open_purchase" :addForm="addForm" :allData="allData"
			:businessType="businessType" @closeDialogs="closeDialogFunc($event, 'add')"></detailProduct>
			</div>
		<div class="common-level-rail d-b-c mt10">
					<div class="d-s-c">					
					</div>				
					<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
						:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
						layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
					</el-pagination>
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
	import StockApi from "@/api/stock.js";
	import detailProduct from "./detailProduct.vue";
	import {
		formatDate
	} from "@/utils/DateTime.js";
	export default {
		components: {
			detailProduct,
		},
		data() {
			return {
				searchForm: {
					barCode: "",
					fileList: [],
					pageIndex: "",
					pageSize: "",
					imageId: "",
					time: [],
				},
				form: {},
				searchs: {},
				valueList: [],
				allData: {},
				config: {},
				startDate: "",
				endDate: "",
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
				supplierList: [],
				totalDataNumber: 0,
				fullscreen: false,
				pageSize: 10,
				curPage: 1,
			};
		},
		props: ["open_edit", "addForm", "editForm", "openDetail"],
		created() {
			this.dialogVisible = this.open_edit;
			this.searchForm = this.editForm;
			this.depotList = this.addForm.depot;
			this.getselect();
		},
		methods: {
			handleStartDateChange(val) {
				this.checkAndTriggerGetParams();
			},
			handleEndDateChange(val) {
				this.checkAndTriggerGetParams();
			},
			checkAndTriggerGetParams() {
				if (this.startDate && this.endDate) {
					if (!Array.isArray(this.searchForm.time)) {
						this.searchForm.time = [];
					}
					this.searchForm.time[0] = this.startDate;
					this.searchForm.time[1] = this.endDate;
					this.getselect();
				}
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.loading = true;
				self.getselect();
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getselect();
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
				let params = this.searchForm;
				params.pageIndex = this.curPage;
				params.pageSize = this.pageSize;
				params.productExtendId = this.editForm.id;
				this.loading = true;
				StockApi.stockStockIndex(params, true)
					.then((res) => {
						this.productData = res.data.stock.records;
						this.totalDataNumber = res.data.stock.total;
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
					purchaseCycle: option.purchaseCycle,
					arrivalDate: formatDate(option.arrivalDate, "YYYY-MM-DD"),
					id: option.id,
				}));
				let filePaths = [];
				for (let file of self.searchForm.fileList) {
					filePaths.push(file.filePath);
				}
				self.searchForm.fileList = filePaths;
				console.log(filePaths, "filePaths");
				if (!filePaths) {
					ElMessage({
						message: "请选择图片",
						type: "warning",
					});
					return;
				}
				let params = self.searchForm;

				params.billItemList = billItemList;
				console.log(params, "params");
				self.loading = true;
				PurchaseApi.orderEdit(params, true)
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