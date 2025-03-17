<template>
	<el-dialog title="添加分类" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :show-close="false" align-center
		append-to="#right-content-box" width="420px">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">添加分类</div>
				<!-- <el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon> -->
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-form size="small" :model="form" :rules="formRules" ref="form">
			<el-form-item label="分类名称" prop="name">
				<el-input style="width: 320px" v-model="form.name" autocomplete="off"></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" @click="addUser" :loading="loading">确 定</el-button>
			</div>
		</template>
		<!--上传图片组件-->
		<Upload v-if="isupload" :isupload="isupload" :type="type" @returnImgs="returnImgsFunc">上传图片</Upload>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import Upload from "@/components/file/Upload.vue";
	export default {
		components: {
			Upload,
		},
		data() {
			return {
				form: {
					parentId: 0,
					name: "",
				},
				formRules: {
					name: [{
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
				formLabelWidth: "200px",
				/*是否显示*/
				dialogVisible: false,
				fullscreen: false,
				loading: false,
				/*是否上传图片*/
				isupload: false,
			};
		},
		props: ["add_category", "addform"],
		created() {
			this.dialogVisible = this.add_category;
			console.log(this.addform, "addform");
		},
		methods: {
			/*添加用户*/
			addUser() {
				let self = this;
				if (self.addform != undefined) {
					self.form.parentId = self.addform.categoryId;
				}
				console.log(self.form.parentId, "parentId");
				let params = self.form;
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						ProductApi.categoryAdd(params, true)
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
		color: rgba(153, 153, 153, 1);
		font-size: 12px;
		text-align: left;
		margin-left: 60px;
	}

	.titles {
		color: rgba(16, 16, 16, 1);
		font-size: 12px;
		text-align: left;
		margin-bottom: 20px;
	} */
</style>