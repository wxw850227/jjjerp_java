<template>
	<el-dialog title="批量新增货位" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">批量新增货位</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-row :gutter="20">
			<el-col :span="14">
				<el-form size="small" :model="form" :rules="formRules" ref="form">
					<el-form-item label="货位类型" prop="basicUnit" :label-width="formLabelWidth">
						<el-select v-model="form.storageType" style="width: 100%" placeholder="请选择">
							<el-option label="拣选货位" :value="1"></el-option>
							<el-option label="存储货位" :value="2"></el-option>
							<el-option label="异常货位" :value="3"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="库区" prop="basicUnit" :label-width="formLabelWidth">
						<el-select v-model="form.reservoirName" style="width: 100%" placeholder="请选择" disabled>
						</el-select>
						<!-- <el-select v-model="form.ware_id" filterable>
          <el-option :label="item.name" :value="item.id" v-for="(item,index) in wareList" :key='index'></el-option>
        </el-select> -->
					</el-form-item>
					<el-form-item label="巷道" prop="row" :label-width="formLabelWidth">
						<div class="fdw">
							<el-input v-model="form.row" autocomplete="off" style="flex: 1" :max="99"
								placeholder="数字号，最大99"></el-input>
							<span style="margin: 0 5px"></span>
							<el-input v-model="form.addRow" autocomplete="off" style="flex: 0.5" :max="99"
								placeholder="个数(最大99)"></el-input>
						</div>
					</el-form-item>
					<div class="tip">自动补0，补齐2位</div>
					<el-form-item label="架号" prop="columns" :label-width="formLabelWidth">
						<div class="fdw">
							<el-input v-model="form.columns" autocomplete="off" style="flex: 1" :max="99"
								placeholder="数字号，最大99"></el-input>
							<span style="margin: 0 5px"></span>
							<el-input v-model="form.addCol" autocomplete="off" style="flex: 0.5" :max="99"
								placeholder="个数(最大99)"></el-input>
						</div>
					</el-form-item>
					<div class="tip">自动补0，补齐2位</div>
					<el-form-item label="层位" prop="floor" :label-width="formLabelWidth">
						<div class="fdw">
							<el-input v-model="form.floor" autocomplete="off" style="flex: 1" :max="99"
								placeholder="数字号，最大99"></el-input>
							<span style="margin: 0 5px"></span>
							<el-input v-model="form.addFloor" autocomplete="off" style="flex: 0.5" :max="99"
								placeholder="个数(最大99)"></el-input>
						</div>
					</el-form-item>
					<div class="tip">自动补0，补齐2位</div>
					<el-form-item label="格位" prop="cell" :label-width="formLabelWidth">
						<div class="fdw">
							<el-input v-model="form.cell" autocomplete="off" style="flex: 1" :max="99"
								placeholder="数字号，最大99"></el-input>
							<span style="margin: 0 5px"></span>
							<el-input v-model="form.addCell" autocomplete="off" style="flex: 0.5" :max="99"
								placeholder="个数(最大99)"></el-input>
						</div>
					</el-form-item>
					<div class="tip">自动补0，补齐2位</div>
					<div class="titles">货位编码示例</div>
					<el-form-item label="" prop="otherUnit" :label-width="formLabelWidth">
						<div class="fdw">
							<el-input v-model="PreviewData" autocomplete="off" disabled style="flex: 1"
								placeholder=""></el-input>
							<span style="margin: 0 5px"></span>
							<el-button type="primary" @click="generate()">点击生成</el-button>
						</div>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="10">
				<div class="models">
					<div class="title2">货位编号(同仓库下不能重复)</div>
					<ul class="generated-list">
						<li v-for="(item, index) in generatedData" :key="index">
							<div class="content">{{ item }}</div>
						</li>
						<div class="divider" v-if="index !== generatedData.length - 1"></div>
					</ul>
				</div>
			</el-col>
		</el-row>

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
					depotId: "",
					reservoirId: "",
					type: "",
					storageType: "",
					row: "",
					addRow: "",
					columns: "",
					addCol: "",
					floor: "",
					addFloor: "",
					cell: "",
					addCell: "",
				},
				formRules: {
					storageType: [{
						required: true,
						message: "请选择货位类型",
						trigger: "blur",
					}, ],
					reservoirName: [{
						required: true,
						message: "请选择库区",
						trigger: "blur",
					}, ],
					row: [{
							required: true,
							message: "请输入巷道",
							trigger: "blur"
						},
						{
							validator: this.validateNumber,
							trigger: "blur"
						},
					],
					columns: [{
							required: true,
							message: "请输入架号",
							trigger: "blur"
						},
						{
							validator: this.validateNumber,
							trigger: "blur"
						},
					],
					floor: [{
							required: true,
							message: "请输入层位",
							trigger: "blur"
						},
						{
							validator: this.validateNumber,
							trigger: "blur"
						},
					],
					cell: [{
							required: true,
							message: "请输入格位",
							trigger: "blur"
						},
						{
							validator: this.validateNumber,
							trigger: "blur"
						},
					],
				},
				/*左边长度*/
				formLabelWidth: "100px",
				/*是否显示*/
				dialogVisible: false,
				fullscreen: false,
				PreviewData: "",
				loading: false,
				/*是否上传图片*/
				isupload: false,
				generatedData: [],
			};
		},
		props: ["batch_add", "batchAddform"],
		created() {
			this.dialogVisible = this.batch_add;
			this.form.depotId = this.batchAddform.depotId;
			if (this.batchAddform.leaf) {
				this.form.reservoirId = this.batchAddform.reservoirId;
				this.form.sheifId = this.batchAddform.id;
			} else {
				this.form.reservoirId = this.batchAddform.depotReservoirId;
			}
			this.form.reservoirName = this.batchAddform.reservoirName;
		},
		methods: {
			validateNumber(rule, value, callback) {
				if (!/^\d+$/.test(value)) {
					callback(new Error("请输入数字"));
				} else {
					callback();
				}
			},
			generate() {
				let self = this;
				let params = self.form;
				self.form.type = 0;
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						SettingApi.depotStorageBatchAdd(params)
							.then((res) => {
								self.loading = false;
								self.generatedData = res.data;
								self.PreviewData = res.data[0];
								ElMessage({
									message: "成功生成",
									type: "success",
								});
							})
							.catch((error) => {
								self.loading = false;
							});
					}
				});
			},
			/*添加用户*/
			addUser() {
				let self = this;
				let params = self.form;
				self.form.type = 1;
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						SettingApi.depotStorageBatchAdd(params, true)
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
		color: #f56c6c;
		font-size: 12px;
		text-align: left;
		margin-left: 100px;
		margin-bottom: 10px;
	}

	.titles {
		left: 424px;
		top: 418px;
		width: 262px;
		height: 21px;
		color: rgba(16, 16, 16, 1);
		font-size: 12px;
		text-align: left;
		margin-bottom: 10px;
		margin-left: 100px;
	}

	.models {
		/* left: 749px;
		top: 244px;
		width: 345px;
		height: 526px; */
		line-height: 20px;
		background-color: rgba(239, 239, 239, 1);
		text-align: center;
		/* margin-left: 20px; */
	}

	.title2 {
		font-size: 14px;
		text-align: center;
		padding-top: 12px;
		color: rgba(16, 16, 16, 1);
	}

	.generated-list {
		width: 100%;
		height: 372px;
		/* margin-left: 20px;*/
		margin: 10px 0; 
		list-style: none;
		overflow-y: auto !important;
	}

	.generated-list li {
		width: 80%;
		margin: 0 auto;
		height: 32px;
		line-height: 32px;
		margin-bottom: 5px;
		position: relative;

		background-color: white;
		/* 设置白色背景 */
	}

	.content {
		height: 100%;
		/* 让内容占满 li 的高度 */
		text-align: center;
	}
</style>