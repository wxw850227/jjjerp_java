<template>
	<!-- 描述：权限-管理员列表-添加管理员-->
	<el-dialog title="新增管理员" :fullscreen="fullscreen" :show-close="false" align-center v-model="dialogVisible"
		@close="dialogFormVisible" :close-on-click-modal="false" :close-on-press-escape="false"  width="640px">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">新增管理员</div>
				<!-- <el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon> -->
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<!--form表单-->
		<el-form ref="form" :model="form" :rules="formRules" :label-width="formLabelWidth">
			<el-form-item label="用户名" prop="userName"><el-input v-model="form.userName"
					placeholder="请输入用户名"></el-input></el-form-item>
			<el-form-item label="所属角色" prop="roleId">
				<el-select v-model="form.roleId" :multiple="true" style="width: 100%">
					<el-option v-for="item in roleList" :value="item.roleId" :key="item.roleId"
						:label="item.roleName"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="登录密码" prop="password">
				<el-input v-model="form.password" placeholder="请输入登录密码" type="password"></el-input>
			</el-form-item>
			<el-form-item label="确认密码" prop="confirmPassword">
				<el-input v-model="form.confirmPassword" placeholder="请输入确认密码" type="password"></el-input>
			</el-form-item>
			<el-form-item label="业务员" prop="isSales">
				<el-checkbox v-model="form.isSales"></el-checkbox>
			</el-form-item>
		</el-form>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="onSubmit" :loading="loading">确 定</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import AuthApi from "@/api/auth.js";
	export default {
		data() {
			return {
				/*左边长度*/
				formLabelWidth: "120px",
				/*是否显示*/
				loading: false,
				/*是否显示*/
				dialogVisible: false,
				fullscreen: false,
				/*form表单对象*/
				form: {
					userName: "",
				},
				/*form验证*/
				formRules: {
					userName: [{
						required: true,
						message: " ",
						trigger: "blur",
					}, ],
					roleId: [{
						required: true,
						message: " ",
						trigger: "blur",
					}, ],
					password: [{
						required: true,
						message: " ",
						trigger: "blur",
					}, ],
					confirmPassword: [{
						required: true,
						message: " ",
						trigger: "blur",
					}, ],
					realName: [{
						required: true,
						message: " ",
						trigger: "blur",
					}, ],
				},
			};
		},
		props: ["open", "roleList"],
		watch: {
			open: function(n, o) {
				if (n != o) {
					this.dialogVisible = this.open;
				}
			},
		},
		created() {},
		methods: {
			/*添加*/
			onSubmit() {
				let self = this;
				self.loading = true;
				let params = self.form;
				if (self.form.isSales) {
					params.isSales = 1;
				} else {
					params.isSales = 0;
				}
				AuthApi.userAdd(params, true)
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
			},

			/*关闭弹窗*/
			dialogFormVisible(e) {
				if (e) {
					this.$emit("close", {
						type: "success",
						openDialog: false,
					});
				} else {
					this.$emit("close", {
						type: "error",
						openDialog: false,
					});
				}
			},
		},
	};
</script>

<style>
	
</style>