<template>
	<el-dialog title="编辑基本单位" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box" width="640px">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">编辑基本单位</div>
				<!-- <el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon> -->
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-form size="small" :model="form" :rules="formRules" ref="form">
			<el-form-item label="基本单位" prop="basicUnit">
				<el-input v-model="form.basicUnit" autocomplete="off" style="width: 320px"></el-input>
			</el-form-item>
			<div class="tip">注: 基本单位应为最小度量单位</div>
			<el-divider />
			<div class="titles">设置辅助单位(启用多计量单位后设置有效)</div>

			<div v-for="(unit, index) in form.deputyList" :key="index">
				<el-form-item :prop="'otherUnit' + index">
					<div class="fdw">
						<div style="display: flex; align-items: center; margin-right: 10px">
							<el-icon style="margin-right: 5px" :size="16" @click="addUnit(index)">
								<Plus />
							</el-icon>
							<el-icon v-if="form.deputyList.length > 1" :size="16" @click="removeUnit(index)">
								<Delete />
							</el-icon>
						</div>
						<span :style="{ margin: '0 5px' }">{{
              "副单位" + (index + 1)
            }}</span>
						<el-input v-model="unit.deputyUnitName" autocomplete="off" style="flex: 1"
							placeholder="请输入副单位(大单位)"></el-input>
						<span style="margin: 0 5px">=</span>
						<el-input v-model.number="unit.ratio" autocomplete="off" style="flex: 1" type="number"
							placeholder="请输入比例"><template #append>基本单位</template></el-input>
					</div>
				</el-form-item>
			</div>
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
					basicUnit: "",
					deleteList: [],
					deputyList: [{
						deputyUnitName: "",
						ratio: null,
						deputyUnitId: ""
					}],
				},
				fullscreen: false,
				formRules: {
					basicUnit: [{
						required: true,
						message: "请输入基本单位",
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
				delList: [],
			};
		},
		props: ["open_edit", "editForm"],
		created() {
			this.dialogVisible = this.open_edit;
			console.log(this.$props.editForm, "editForm");
			this.form = {
				...this.form,
				...this.$props.editForm
			};
		},
		methods: {
			/*添加用户*/
			addUser() {
				let self = this;
				self.form.deputyList.forEach((unit, index) => {
					unit.deputyNum = index + 1;
				});
				self.form.deleteList = self.delList;
				let params = self.form;
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						ProductApi.unitEdit(params, true)
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
			/*添加副单位*/
			addUnit(index) {
				this.form.deputyList.splice(index + 1, 0, {
					name: "",
					ratio: null
				});
			},
			/*删除副单位*/
			removeUnit(index) {
				if (this.form.deputyList.length > 1) {
					this.delList.push(this.form.deputyList[index].deputyUnitId);
					console.log(this.delList, "delList");
					this.form.deputyList.splice(index, 1);
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
	}
</style>