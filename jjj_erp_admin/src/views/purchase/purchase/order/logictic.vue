<template>
	<el-dialog title="采购订单物流单号" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box" class="titles" width="640px">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					采购订单物流单号
				</div>
				<!-- <el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon> -->
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-form size="small" :model="form" :rules="formRules" ref="form">
			<!-- <el-form-item
        label="规格名"
        prop="attributeName"
        :label-width="formLabelWidth"
      >
        <el-input v-model="form.attributeName" autocomplete="off"></el-input>
      </el-form-item> -->
			<div class="btns">
				<el-button type="primary" @click="addOptions"><el-icon>
						<Plus />
					</el-icon>新增</el-button>
			</div>
			<el-table :data="filteredDeliveryData" border style="margin-top: 6px">
				<el-table-column prop="#" label="编号" width="60">
					<template #default="{ $index }">
						{{ $index + 1 }}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="55">
					<template #default="{ $index }">
								<el-text type="primary" size="small" @click="removeOptions($index)">删除</el-text>
								<!-- <el-icon @click="removeOptions($index)" :size="16"
					  ><Delete
					/></el-icon> -->
							</template>
				</el-table-column>
				<el-table-column label="承运商">
					<template #default="{ row }">
						<el-select v-model="row.selectID" :disabled="isFromEditForm(row)" placeholder="请选择承运商">
							<el-option v-for="(item, index) in deliveryList" :key="index" :label="item.expressName"
								:value="item.expressId" />
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="物流单号">
					<template #default="{ row }">
						<el-input v-model="row.expressNo" :disabled="isFromEditForm(row)" label="物流单号" />
					</template>
				</el-table-column>
			</el-table>
		</el-form>
		<template #footer>
			<div class="dialog-footer">
				<el-button type="primary" @click="addUser" :loading="loading">保 存</el-button>
				<el-button @click="dialogFormVisible">关 闭</el-button>
			</div>
		</template>
		<!--上传图片组件-->
		<Upload v-if="isupload" :isupload="isupload" :type="type" @returnImgs="returnImgsFunc">上传图片</Upload>
	</el-dialog>
</template>

<script>
	import SettingApi from "@/api/setting.js";
	import PurchaseApi from "@/api/purchase.js";
	import Upload from "@/components/file/Upload.vue";
	export default {
		components: {
			Upload,
		},
		data() {
			return {
				form: {
					billId: "",
				},
				valueList: [],
				deliveryData: [],
				deletedRowIndex: "",
				fullscreen: false,
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
				isNewRow: false,
				isShow: true,
				billId: "",
				deliveryList: [],
			};
		},
		props: ["open_logistic", "editForm"],
		computed: {
			filteredDeliveryData() {
				return this.deliveryData.filter((row) => row.isDelete !== 1);
			},
		},
		created() {
			this.dialogVisible = this.open_logistic;
			this.billId = this.editForm.billId;
			if (this.editForm.deliveryList && this.editForm.deliveryList.length > 0) {
				this.deliveryData = [];
				this.editForm.deliveryList.forEach((delivery) => {
					this.deliveryData.push({
						selectID: delivery.expressId,
						expressNo: delivery.expressNo,
						isDelete: delivery.isDelete,
						billDeliveryId: delivery.billDeliveryId,
					});
				});
			} else {
				this.addOptions();
			}
			console.log(this.editForm, "editForm");
			this.getselect();
		},
		methods: {
			isFromEditForm(row) {
				return this.editForm.deliveryList.some(
					(delivery) =>
					delivery.expressId == row.selectID &&
					delivery.expressNo == row.expressNo
				);
			},
			getselect() {
				SettingApi.expressList({})
					.then((res) => {
						this.deliveryList = res.data.records;
					})
					.catch((e) => {
						console.log(e);
					});
			},
			handNum(index) {
				this.deliveryData[index].optionId = index + 65;
				return String.fromCharCode(index + 65);
			},
			addOptions() {
				this.deliveryData = this.deliveryData || [];
				this.deliveryData.push({
					isNewRow: true,
				});
			},
			delImg(type, index) {
				if (type == 1) {
					delete this.form.imagePath;
					delete this.form.imageId;
				} else if (type == 2) {
					delete this.deliveryData[index].imagePath;
					delete this.deliveryData[index].imageId;
				}
			},
			changOptions(index) {
				let flag = this.deliveryData.every((v) => {
					return !(v.isAnswer == 1);
				});
				if (flag) {
					this.deliveryData[index].isAnswer = 1;
				} else {
					this.deliveryData.forEach((v) => {
						v.isAnswer = 0;
					});
					this.deliveryData[index].isAnswer = 1;
				}
			},
			removeOptions(index) {
				let deletedRow = this.deliveryData[index];
				let deletedIndex = index;

				if (!deletedRow.isNewRow) {
					let matchedDelivery = this.editForm.deliveryList.find(
						(d) =>
						d.expressId === deletedRow.selectID &&
						d.expressNo === deletedRow.expressNo
					);
					if (matchedDelivery) {
						matchedDelivery.isDelete = 1;
					}
				}
				deletedRow.isDelete = 1;
				this.isShow = false;

				// 保存被删除行的索引
				this.deletedRowIndex = deletedIndex;
			},
			/*添加用户*/
			addUser() {
				let self = this;
				let params = self.form;
				params.billId = self.billId;
				params.deliveryList = [];
				this.deliveryData.forEach((row) => {
					console.log("row.isDelete ", row.isDelete);
					if (row.isNewRow || row.isDelete == 1) {
						let selectID = row.selectID;
						let expressNo = row.expressNo;
						let matchedDelivery = self.deliveryList.find(
							(d) => d.expressId === selectID
						);
						if (matchedDelivery && (row.isNewRow || row.isDelete == 1)) {
							let delivery = {
								expressId: matchedDelivery.expressId,
								expressName: matchedDelivery.expressName,
								expressNo: expressNo,
								isDelete: row.isDelete,
								billDeliveryId: row.billDeliveryId,
							};

							// 将处理后的 delivery 添加到 params.deliveryList 数组中
							params.deliveryList.push(delivery);
						}
					}
				});
				console.log(params, "params");
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						PurchaseApi.orderDelivery(params)
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

	.btns {
		display: flex;
		flex-direction: row;
		font-size: 14px;
		text-align: center;
		/* margin: 20px; 
	} */
</style>