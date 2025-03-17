<template>
	<el-dialog title="编辑物流公司" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box"  width="640px">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">编辑物流公司</div>
				<!-- <el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon> -->
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<!-- <el-form size="small" ref="form" :model="form"> -->
		<el-form ref="ruleFormRef" style="max-width: 600px" :model="ruleForm" :rules="rules" label-width="auto"
			class="demo-ruleForm" :size="formSize" status-icon>
			<!--添加门店-->
			<el-form-item label="物流公司名称 " prop="expressName" :rules="[{ required: true, message: ' ' }]">
				<el-input v-model="form.expressName"></el-input>
				<div class="tips">
					请对照
					<el-link type="primary" :underline="false" @click="gotoCompany()">
						物流公司编码表
					</el-link>
					填写
				</div>
			</el-form-item>
			<el-form-item label="物流公司代码 " prop="expressCode" :rules="[{ required: true, message: ' ' }]">
				<el-input v-model="form.expressCode"></el-input>
				<div class="tips">用于快递100API查询物流信息，务必填写正确</div>
			</el-form-item>
			<el-form-item label="排序">
				<el-input v-model="form.sort" type="number"></el-input>
				<div class="tips">数字越小越靠前</div>
			</el-form-item>
		</el-form>
		<!--提交-->
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" @click="onSubmit" :loading="loading">提 交</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import SettingApi from "@/api/setting.js";
	export default {
		data() {
			return {
				dialogVisible: false,
				fullscreen: false,
				loading: false,
				/*form表单数据*/
				form: {
					expressName: "",
					expressCode: "",
					wxCode: "",
					sort: 1,
				},
			};
		},
		props: ["open_edit", "editForm"],
		created() {
			this.dialogVisible = this.open_edit;
			this.form = {
				...this.form,
				...this.$props.editForm,
			};
		},

		methods: {
			//提交表单
			onSubmit() {
				let self = this;
				let form = this.form;
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						SettingApi.expressEdit(form, true)
							.then((data) => {
								self.loading = false;
								ElMessage({
									message: "恭喜你，添加成功",
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
			/*跳转物流编码*/
			gotoCompany() {
				//let baseUrl = window.location.protocol + '//' + window.location.host;
				//window.location.href = baseUrl + '/express.xlsx';
				window.location.href =
					"https://qn-cdn.jjjshop.net/20230918111820133.xlsx";
			},
			/*跳转微信物流编码*/
			gotoWxCompany() {
				//let baseUrl = window.location.protocol + '//' + window.location.host;
				//window.location.href = baseUrl + '/wx_express.xlsx';
				window.location.href =
					"https://qn-cdn.jjjshop.net/20230918111831794.xlsx";
			},
		},
	};
</script>

<style>
	.tips {
		color: #ccc;
	}
</style>