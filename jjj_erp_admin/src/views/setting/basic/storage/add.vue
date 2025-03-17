<template>
	<el-dialog title="新增货位" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box" width="480px">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">新增货位</div>
				<!-- <el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon> -->
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-form ref="form" :model="form" :rules="formRules" :label-width="formLabelWidth">
			<el-form-item label="货位编码" prop="storageCode">
				<el-input v-model="form.storageCode" autocomplete="off" style="width: 100%"></el-input>
			</el-form-item>
			<el-form-item label="货位类型" prop="storageType">
				<el-select v-model="form.storageType" placeholder="请选择"  style="width: 100%" >
					<el-option label="拣选货位" :value="1"></el-option>
					<el-option label="存储货位" :value="2"></el-option>
					<el-option label="异常货位" :value="3"></el-option>
				</el-select>
				<div class="gray9 f12">
					注：同个仓库的货位编码不能重复
				</div>
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
	import SettingApi from "@/api/setting.js";
	import Upload from "@/components/file/Upload.vue";
	export default {
		components: {
			Upload,
		},
		data() {
			return {
				form: {
					storageCode: "",
					storageType: "",
					reservoirId: "",
					sheifId: "",
				},
				formRules: {
					storageType: [{
						required: true,
						message: "请选择货位类型",
						trigger: "blur",
					}, ],
					storageCode: [{
						required: true,
						message: "请输入货位编码",
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
			};
		},
		props: ["open_add", "addform"],
		created() {
			this.dialogVisible = this.open_add;
			if (this.addform.depotShelfId) {
				this.form.sheifId = this.addform.depotShelfId;
			} else if (this.addform.depotReservoirId) {
				this.form.reservoirId = this.addform.depotReservoirId;
			}

			console.log(this.form, "addform");
		},
		methods: {
			/*添加用户*/
			addUser() {
				let self = this;
				let params = self.form;
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						SettingApi.depotStorageAdd(params, true)
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
	/* .img {
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
	} */
</style>