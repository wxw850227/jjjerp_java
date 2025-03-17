<template>
	<el-dialog title="设置货位库存" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">设置货位库存</div>
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
				<el-form-item label="" class="select">
					<el-radio-group v-model="radio">
						<el-radio :label="0">编码</el-radio>
						<el-radio :label="1">条码</el-radio>
					</el-radio-group>
				</el-form-item>

				<el-form-item label="选择编码" v-if="radio == 0">
					<el-input size="small" v-model="searchForm.productCode" placeholder="请输入编码"></el-input>
				</el-form-item>
				<el-form-item label="选择条码" v-if="radio == 1">
					<el-input size="small" v-model="searchForm.barCode" placeholder="请输入条码"></el-input>
				</el-form-item>

				<el-form-item>
					<el-button size="small" type="primary" @click="reserch">查询</el-button>
				</el-form-item>
			</el-form>
		</div>
		<el-table :data="form.options" border style="margin-top: 6px">
			<el-table-column prop="id" label="ID" width="55"></el-table-column>
			<el-table-column label="操作" width="50">
				<template #default="{ $index }">
					<div>
						<el-icon :size="16" @click="removeOptions($index)">
							<Delete />
						</el-icon>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="productCode" label="商品编码"> </el-table-column>
			<el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
			<el-table-column prop="sku" label="规格"></el-table-column>
			<el-table-column label="货位库存">
				<template #default="{ row }">
					<el-input v-model="row.stockNum" />
				</template>
			</el-table-column>
		</el-table>

		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" @click="editUser" :loading="loading">确 定</el-button>
			</div>
		</template>
		<!--上传图片组件-->
		<Upload v-if="isupload" :isupload="isupload" :type="type" @returnImgs="returnImgsFunc">上传图片</Upload>
		<Products v-if="open_product" :open_product="open_product" :searchs="serachValue"
			@selectProduct="selectProducts($event)" @closeDialog="closeDialogFunc($event, 'Products')"></Products>
	</el-dialog>
</template>

<script>
	import SettingApi from "@/api/setting.js";
	import ProductApi from "@/api/product.js";
	import Upload from "@/components/file/Upload.vue";
	import Products from "@/components/table/products.vue";
	export default {
		components: {
			Upload,
			Products,
		},
		data() {
			return {
				searchForm: {
					barCode: "",
					productCode: "",
					pageIndex: "",
					pageSize: "",
				},
				open_product: false,
				serachValue: "",
				form: {
					options: [],
				},
				valueList: [],
				radio: 0,
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
			};
		},
		props: ["open_set", "setForm"],
		created() {
			// this.forms = { ...this.forms, ...this.$props.form };
			// if (this.form.valueList && Array.isArray(this.form.valueList)) {
			//   this.form.options = this.form.valueList.map((name) => ({ name }));
			// }
			this.dialogVisible = this.open_set;
			this.getStorageList(this.setForm.depotStorageId);
			console.log(this.setForm, "setForm");
			// this.form.options = this.setmodel;
		},
		methods: {
			reserch() {
				let self = this;
				self.serachValue = self.searchForm.productCode;
				self.open_product = true;
			},
			selectProducts(m) {
				console.log(m, "m");
				this.open_product = m.openDialog;
				const newRecords = m.value;
				const mergedData = [...this.form.options, ...newRecords];
				const uniqueData = Array.from(
					new Set(mergedData.map((item) => item.id))
				).map((id) => mergedData.find((item) => item.id === id));
				this.form.options = uniqueData;
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
				let stockList = self.form.options.map((option) => ({
					stockNum: option.stockNum,
					productExtendId: option.id,
					isDelete: self.isDelete,
				}));
				let params = {};
				params.stockList = stockList;
				params.depotStorageId = this.setForm.depotStorageId;
				console.log(params, "params");
				self.loading = true;
				SettingApi.depotStorageSet(params, true)
					.then((data) => {
						self.loading = false;
						ElMessage({
							message: "添加成功",
							type: "success",
						});
						self.dialogFormVisible(true);
					})
					.catch((error) => {
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
					this.filePath = e[0].filePath;
					this.form.imageId = e[0].fileId;
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