<template>
	<el-dialog title="修改客户" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">修改客户</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-tabs v-model="activeTab" @tab-click="handleTabClick">
			<el-tab-pane label="详细信息" name="basic"></el-tab-pane>
			<el-tab-pane label="收货地址" name="address"></el-tab-pane>
			<!-- <el-tab-pane label="合并信息" name="merge"></el-tab-pane> -->
			<el-tab-pane label="开票信息" name="invoice"></el-tab-pane>
			<!-- <el-tab-pane label="自定义属性" name="image"></el-tab-pane> -->
		</el-tabs>
		<div v-show="activeTab === 'basic'">
			<el-form size="small" :model="form" :rules="formRules" ref="form">
				<el-row>
					<el-col :span="8">
						<el-form-item label="其他平台" prop="name" :label-width="formLabelWidth">
							<el-select :disabled="isDetailMode" v-model="form.platformType" style="width: 100%"
								placeholder="选择平台">
								<el-option v-for="(item, index) in platformType" :key="index" :label="item.name"
									:value="item.value" />
							</el-select>
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item label="昵称" prop="nickName" :label-width="formLabelWidth">
							<el-input v-model="form.nickName" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="客户名称" prop="nickName" :label-width="formLabelWidth">
							<el-input v-model="form.realName" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8">
						<el-form-item label="国家名称" prop="name" :label-width="formLabelWidth">
							<el-select :disabled="isDetailMode" v-model="form.country" placeholder="请选择国家"
								style="width: 100%">
								<el-option label="中国" :value="0"></el-option>
								<el-option label="美国" :value="1"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="期初应付款" prop="nickName" :label-width="formLabelWidth">
							<el-input v-model="form.initial" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="客户编码" prop="nickName" :label-width="formLabelWidth">
							<el-input v-model="form.userCode" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8">
						<el-form-item label="省市区" prop="regions" :label-width="formLabelWidth">
							<el-cascader :disabled="isDetailMode" style="width: 100%" clearable v-model="form.regions"
								:options="areaData" :props="regionParams" @change="handleChange1"></el-cascader>
						</el-form-item>
					</el-col>

					<el-col :span="8">
						<el-form-item label="详细地址" prop="address" :label-width="formLabelWidth">
							<el-input v-model="form.address" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="E-mail" prop="email" :label-width="formLabelWidth">
							<el-input v-model="form.email" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8">
						<el-form-item label="客户类型" prop="name" :label-width="formLabelWidth">
							<el-select :disabled="isDetailMode" v-model="form.userType" style="width: 100%"
								placeholder="选择平台">
								<el-option label="零售" :value="0"></el-option>
								<el-option label="批发" :value="1"></el-option>
								<el-option label="吊牌价" :value="2"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="固话" prop="fixPhone" :label-width="formLabelWidth">
							<el-input v-model="form.fixPhone" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="手机" prop="phone" :label-width="formLabelWidth">
							<el-input v-model="form.phone" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8">
						<el-form-item label="指定价格" prop="name" :label-width="formLabelWidth">
							<el-select :disabled="isDetailMode" v-model="form.priceType" placeholder="请选择经营类型"
								style="width: 100%">
								<el-option label="标准售价" :value="0"></el-option>
								<el-option label="批发价" :value="1"></el-option>
								<el-option label="吊牌价" :value="2"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="业务员" prop="name" :label-width="formLabelWidth">
							<el-select :disabled="isDetailMode" v-model="form.supplierType" placeholder="请选择经营类型"
								style="width: 100%">
								<el-option label="直营店" :value="0"></el-option>
								<el-option label="联营店" :value="1"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="欠款额度" prop="fixPhone" :label-width="formLabelWidth">
							<el-input v-model="form.debtCredit" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-col :span="16">
					<el-form-item label="备注" prop="description" :label-width="formLabelWidth">
						<el-input type="textarea" v-model="form.description" :disabled="isDetailMode"
							autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="16">
					<el-form-item label="附件：" :label-width="formLabelWidth">
						<div class="draggable-list">
							<draggable class="wrapper" v-model="form.imageList">
								<template #item="{ element, index }">
									<div class="item">
										<img :src="element.filePath" />
										<a href="javascript:void(0);" class="delete-btn" @click.stop="deleteImg(index)">
											<el-icon>
												<CloseBold />
											</el-icon>
										</a>
									</div>
								</template>
							</draggable>
							<div class="item img-select" @click="openUpload">
								<el-icon>
									<Plus />
								</el-icon>
							</div>
						</div>
					</el-form-item>
				</el-col>
			</el-form>
			<Upload v-if="isProductUpload" :isProductUpload="isProductUpload" :config="config"
				@returnImgs="returnImgsFunc">上传图片</Upload>
		</div>
		<div v-show="activeTab === 'address'">
			<div class="common-level-rail">
				<el-button size="small" type="primary" icon="Plus" :disabled="isDetailMode"
					@click="addClick">新增</el-button>
			</div>

			<!--内容-->
			<div class="product-content">
				<div class="table-wrap">
					<el-table size="small" :data="addressList" border style="width: 100%" v-loading="loading">
						<el-table-column prop="#" label="序号" width="80">
							<template #default="{ $index }">
								{{ $index + 1 }}
							</template>
						</el-table-column>
						<el-table-column label="操作" width="60">
							<template #default="{ $index }">
								<el-button @click="removeOptions($index)" type="primary" link :disabled="isDetailMode"
									size="small">删除
								</el-button>
							</template>
						</el-table-column>
						<el-table-column prop="name" label="收件人">
							<template #default="scope">
								<el-input v-model="scope.row.name" :disabled="isDetailMode" autofocus></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机">
							<template #default="scope">
								<el-input v-model="scope.row.phone" :disabled="isDetailMode" autofocus></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="province" label="省份" width="150">
							<template #default="scope">
								<el-select v-model="scope.row.provinceId" placeholder="请选择省份" clearable>
									<el-option v-for="province in areaData" :key="province.id" :label="province.name"
										:value="province.id"></el-option>
								</el-select>
							</template>
						</el-table-column>
						<el-table-column prop="city" label="市">
							<template #default="scope">
								<el-select v-model="scope.row.cityId" placeholder="请选择市" clearable
									:disabled="!scope.row.provinceId">
									<el-option v-for="city in getCityList(scope.row.provinceId)" :key="city.id"
										:label="city.name" :value="city.id"></el-option>
								</el-select>
							</template>
						</el-table-column>
						<el-table-column prop="region" label="区">
							<template #default="scope">
								<el-select v-model="scope.row.regionId" placeholder="请选择区" clearable
									:disabled="!scope.row.cityId">
									<el-option v-for="region in getRegionList(scope.row.cityId)" :key="region.id"
										:label="region.name" :value="region.id"></el-option>
								</el-select>
							</template>
						</el-table-column>
						<el-table-column prop="detail" label="详细地址">
							<template #default="scope">
								<el-input v-model="scope.row.detail" :disabled="isDetailMode" autofocus></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="fixPhone" label="固话">
							<template #default="scope">
								<el-input v-model="scope.row.fixPhone" :disabled="isDetailMode" autofocus></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="zip" label="邮编">
							<template #default="scope">
								<el-input v-model="scope.row.zip" :disabled="isDetailMode" autofocus></el-input>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>
		<div v-show="activeTab === 'invoice'">
			<el-form size="small" :model="form" :rules="formRules" ref="form">
				<el-row>
					<el-col :span="8">
						<el-form-item label="抬头" prop="billHead" :label-width="formLabelWidth">
							<el-input v-model="form.billHead" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="税号" prop="tax_num" :label-width="formLabelWidth">
							<el-input v-model="form.taxNum" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8">
						<el-form-item label="开户行" prop="bank_name" :label-width="formLabelWidth">
							<el-input v-model="form.bankName" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="银行账户" prop="account_number" :label-width="formLabelWidth">
							<el-input v-model="form.accountNumber" :disabled="isDetailMode"
								autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="8">
						<el-form-item label="电话" prop="bill_phone" :label-width="formLabelWidth">
							<el-input v-model="form.billPhone" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="地址" prop="bill_address" :label-width="formLabelWidth">
							<el-input v-model="form.billAddress" :disabled="isDetailMode" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
		</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button type="primary" @click="addUser">保 存</el-button>
				<el-button @click="dialogFormVisible">取 消</el-button>
			</div>
		</template>
	</el-dialog>
</template>

<script>
	import SettingApi from "@/api/setting.js";
	import Upload from "@/components/file/Upload.vue";
	import draggable from "vuedraggable";
	import {
		ElLoading
	} from "element-plus";
	export default {
		components: {
			Upload,
			draggable,
		},
		data() {
			return {
				form: {
					parentId: "0",
					name: "",
					sort: 100,
					imageId: "",
					type: 0,
					multiUnit: false, // 添加多单位字段
					barCode: "",
					skuOne: "",
					skuTwo: "",
					skuThree: "",
					categoryId: "",
					storeName: "",
					nickName: "",
					regions: [],
				},
				value1: "",
				fullscreen: false,
				config: {},
				unitList: [],
				categoryList: [],
				mfrsList: [],
				specOptions2: [], // 与 skuTwo 相关的选择值数组
				specOptions3: [], // 与 skuThree 相关的选择值数组
				options: [],
				options1: [],
				tableData: [],
				options2: [],
				options3: [],
				formRules: {
					platformType: [{
						required: true,
						message: "请选择平台",
						trigger: "blur",
					}, ],
					realName: [{
						required: true,
						message: "请输入客户名称",
						trigger: "blur",
					}, ],
					nickName: [{
						required: true,
						message: "请输入昵称",
						trigger: "blur",
					}, ],
					regions: [{
						required: true,
						message: "请选择地址",
						trigger: "blur",
					}, ],
					userType: [{
						required: true,
						message: "请选择客户类型",
						trigger: "blur",
					}, ],
					phone: [{
						required: true,
						message: "请输入手机",
						trigger: "blur",
					}, ],
				},
				/*左边长度*/
				formLabelWidth: "110px",
				/*是否显示*/
				dialogVisible: false,
				loading: false,
				/*是否上传图片*/
				isProductUpload: false,
				skuOneTitle: "属性1",
				skuTwoTitle: "属性2",
				skuThreeTitle: "属性3",
				regionParams: {
					label: "name",
					value: "id",
					children: "children",
					expandTrigger: "hover",
				},
				skuOneList: [],
				skuTwoList: [],
				skuThreeList: [],
				activeTab: "basic", // 默认显示基本信息标签页
				specNames: [], // 存储多规格选项名称
				specOptions: [], // 存储多规格选项的选择值
				manySkuSelected: 0,
				areaData: [],
				platformType: [],
				addressList: [],
				loadings: null,
				loadingStatus: false,
			};
		},
		props: ["open_edit", "editForm", "isDetailMode"],
		created() {
			this.loadings = ElLoading.service({
				lock: true,
				text: "Loading",
				background: "rgba(0, 0, 0, 0.7)",
			});
			setTimeout(() => {
				this.loadings.close();
				if (!this.loadingStatus) {
					this.dialogVisible = this.open_edit;
				}
			}, 10000);
			// this.dialogVisible = this.open_edit;
			this.form = this.editForm;
			this.form.regions = [
				this.form.provinceId,
				this.form.cityId,
				this.form.regionId,
			].join(",");
			this.form.regions = this.form.regions.split(",").map((id) => parseInt(id));
			console.log(this.form, "form");
			this.getdata();
		},
		methods: {
			getCityList(provinceId) {
				const province = this.areaData.find((item) => item.id === provinceId);
				return province ? province.children : [];
			},
			getRegionList(cityId) {
				for (let province of this.areaData) {
					const city = province.children.find((item) => item.id === cityId);
					if (city) return city.children;
				}
				return [];
			},
			addClick() {
				const newData = {};
				this.addressList.push(newData);
			},
			removeOptions(index) {
				// 删除对应的行
				this.addressList.splice(index, 1);
			},
			getdata() {
				const customerId = this.form.customerId;
				SettingApi.regionList().then((res) => {
					this.areaData = res.data;
					this.loadingStatus = true; // 设置加载状态为true，表示数据加载完成
					this.loadings.close(); // 关闭加载状态
					this.dialogVisible = this.open_edit; // 打开对话框
				});
				SettingApi.userToEdit({
					userId: customerId,
				}).then((res) => {
					this.platformType = res.data.platformType;
					this.addressList = res.data.model.addressList;
				});
			},

			handleChange1(val) {
				console.log(val, "val");
				const province = this.areaData.find((item) => item.id === val[0]);
				const city = province.children.find((item) => item.id === val[1]);
				const region = city.children.find((item) => item.id === val[2]);
				this.form.provinceId = province.id;
				this.form.cityId = city.id;
				this.form.regionId = region.id || "";
				this.form.province = province.name;
				this.form.city = city.name;
				this.form.region = region.name;
			},
			/*添加用户*/
			addUser() {
				let self = this;
				let params = self.form;
				params.addressList = self.addressList.map((item) => {
					const province = self.areaData.find(
						(prov) => prov.id === item.provinceId
					);
					const city = province.children.find((cty) => cty.id === item.cityId);
					const region = city.children.find((reg) => reg.id === item.regionId);
					return {
						...item,
						province: province.name,
						city: city.name,
						region: region.name,
					};
				});
				console.log(params, "params");
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						SettingApi.userEdit(params, true)
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
			openUpload() {
				this.isProductUpload = true;
			},
			deleteImg(index) {
				this.form.imageList.splice(index, 1);
			},
			/*获取图片*/
			returnImgsFunc(e) {
				console.log(this.form);
				if (e != null) {
					let imgs = this.form.imageList.concat(e);
					// 兼容后端
					for (let i = 0; i < imgs.length; i++) {
						if (typeof imgs[i].imageId == "undefined") {
							imgs[i].imageId = imgs[i].fileId;
						}
					}
					this.form["imageList"] = imgs;
				}
				this.isProductUpload = false;
				console.log(this.form.imageList, "this.form.imageList ");
			},
		},
	};
</script>

<style scoped>
	.img {
		margin-top: 10px;
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
</style>