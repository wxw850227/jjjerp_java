<template>
	<el-dialog :fullscreen="fullscreen" :show-close="false" align-center title="添加商品" v-model="dialogVisible"
		@close="dialogFormVisible" :close-on-click-modal="false" :close-on-press-escape="false" close-icon="CloseBold">
		<!-- 弹窗标题 -->
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">添加商品</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-tabs v-model="activeTab" @tab-click="handleTabClick">
			<el-tab-pane label="基本信息" name="basic"></el-tab-pane>
			<el-tab-pane label="图片" name="image"></el-tab-pane>
		</el-tabs>
		<div v-show="activeTab === 'basic'" class="common-seach-wrap column-dialog">
			<div>
				<el-form size="small" :inline="true" :model="form" :rules="formRules" ref="form"
					class="demo-form-inline">
					<el-row  :gutter="20">
						<el-col :span="5">
							<el-form-item label="商品编码" prop="productCode">
								<el-input v-model="form.productCode" placeholder="不填自动生成" autocomplete="off"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="名称" prop="name">
								<el-input v-model="form.name" autocomplete="off"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="单位" prop="unitId">
								<el-select v-model="form.unitId" placeholder="请选择">
									<el-option v-for="(item, index) in unitList" :key="index" :label="item.name"
										:value="item.basicUnitId" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="4">
							<el-form-item label="分类" prop="categoryId">
								<el-tree-select v-model="form.categoryId" :data="categoryList" check-strictly
									:render-after-expand="false" :props="{ value: 'categoryId', label: 'name' }"
									:default-expand-all="true" filterable clearable />
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item label="保质期" prop="expiryNum">
								<el-input v-model="form.expiryNum" autocomplete="off">
									<template #append>天</template>
								</el-input>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row :gutter="20">
						<el-col :span="6">
							<el-form-item label="货号" prop="accountNum" class="ww100">
								<el-input v-model="form.accountNum" autocomplete="off"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="6">
							<el-form-item label="重量" prop="weight"  class="ww100">
								<el-input v-model="form.weight" autocomplete="off">
									<template #append>千克(kg)</template>
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :span="6">
							<el-form-item label="体积" prop="volume"  class="ww100">
								<el-input v-model="form.volume" autocomplete="off">
									<template #append>立方米(m³)</template>
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :span="6">
							<el-form-item label="条码" prop="barCode"  class="ww100">
								<el-input v-model="form.barCode" autocomplete="off"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row  :gutter="20">
						<el-col :span="16">
							<el-form-item label="长宽高" prop="grow"  class="ww100">
								<div class="d-s-c">
									<el-input v-model="form.grow" style="margin-right: 10px" autocomplete="off">
										<template #append>米(m)</template>
									</el-input>
									<el-input v-model="form.width" style="margin-right: 10px" autocomplete="off">
										<template #append>米(m)</template></el-input>
									<el-input v-model="form.height" autocomplete="off"><template
											#append>米(m)</template></el-input>
								</div>
							</el-form-item>
						</el-col>
						<el-col :span="8">
							<el-form-item label="品牌" prop="mfrs"  class="ww100">
								<el-select v-model="form.mfrs" placeholder="选择品牌">
									<el-option v-for="(item, index) in mfrsList" :key="index" :label="item.name"
										:value="item.id" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="8">
							<el-form-item label="规格" prop="">
								<el-radio-group v-model="skuRadio">
									<el-radio label="1" size="small">单规格</el-radio>
									<el-radio label="2" size="small">多规格</el-radio>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col :span="6" v-if="skuRadio == 1">
							<el-form-item label="" prop="">
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="24" v-if="skuRadio == 2">
							<el-form-item label="多规格" prop="" class="ww100">
								<el-select v-model="selectedSpecs" multiple placeholder="请选择"
									style="margin-right: 16px; vertical-align: middle" @change="handleSpecChange"
									@remove-tag="removeChange">
									<el-option v-for="(item, index) in options" :key="index" :label="item.attributeName"
										:value="index" :disabled="item.disabled" />
								</el-select>
							</el-form-item>
						</el-col>
						<el-col v-for="(spec, index) in dynamicSpecs" :key="index" :span="6">
							<el-form-item :label="spec.title">
								<el-select v-model="spec.value" multiple placeholder="请选择" @change="onSkuChange"
									@remove-tag="onSkuDeSelect(index, $event)"
									style="margin-right: 16px; vertical-align: middle">
									<el-option v-for="(item, index) in spec.list" :label="item" :key="index"
										:value="item" />
								</el-select>
							</el-form-item>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="12">
							<el-form-item label="备注" prop="remark" class="ww100">
								<el-input v-model="form.remark" type="textarea" autocomplete="off"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
				<el-table :data="form.options" border style="margin-top: 6px">
					<el-table-column type="selection" width="55"></el-table-column>
					<el-table-column prop="#" label="编码">
						<template #default="{ row }">
							<el-input v-model="row.productCode" label="" disabled />
						</template>
					</el-table-column>
					<el-table-column label="条码">
						<template #default="{ row }">
							<el-input v-model="row.barCode" label="条码" />
						</template>
					</el-table-column>
					<el-table-column label="多属性">
						<template #default="{ row }">
							<el-input disabled v-model="row.sku" label="多属性" />
						</template>
					</el-table-column>
					<el-table-column label="批发价">
						<template #default="{ row }">
							<el-input v-model="row.lowDecimal" label="批发价" />
						</template>
					</el-table-column>
					<el-table-column label="进价">
						<template #default="{ row }">
							<el-input v-model="row.purchaseDecimal" label="进价" />
						</template>
					</el-table-column>
					<el-table-column label="吊牌价">
						<template #default="{ row }">
							<el-input v-model="row.commodityDecimal" label="吊牌价" />
						</template>
					</el-table-column>
					<el-table-column label="标准售价">
						<template #default="{ row }">
							<el-input v-model="row.wholesaleDecimal" label="标准售价" />
						</template>
					</el-table-column>
					<el-table-column prop="imageUrl" label="图片">
						<template #default="{ row, $index }">
							<div class="draggable-list small">
								<div v-if="row.imageUrl == ''" @click="openProductUpload('image', 'sku', $index)">
									<el-icon>
										<Plus />
									</el-icon>
								</div>
								<div v-if="row.imageUrl != ''" class="item"
									@click="openProductUpload('image', 'sku', $index)">
									<img :src="row.imageUrl" />
								</div>
							</div>
						</template>
						
						
						
					</el-table-column>
				</el-table>
			</div>
		</div>
		
		<div v-show="activeTab === 'image'">
			<el-form-item label="图片信息：" :rules="[{ required: true, message: '请上传图片' }]" prop="image">
				<div class="draggable-list small">
					<draggable class="wrapper small" v-model="form.image">
						<template #item="{ element, index }">
							<div class="item">
								<img v-img-url="element.filePath" />
								<a href="javascript:void(0);" class="delete-btn" @click.stop="deleteImg(index)">
									<el-icon>
										<CloseBold />
									</el-icon>
								</a>
							</div>
						</template>
					</draggable>
					<div class="item img-select" @click="openProductUpload('image', 'image')">
						<el-icon>
							<Plus />
						</el-icon>
					</div>
				</div>
				<div class="gray9">建议上传尺寸相同的图片，建议尺寸比例为1:1</div>
			</el-form-item>
			<!-- 图片上传组件 -->
		</div>
		<template #footer>
			<div class="dialog-footer">
				<el-button @click="dialogFormVisible">取 消</el-button>
				<el-button type="primary" :disabled="editOpen" @click="addUser">确 定</el-button>
			</div>
		</template>
		<Upload v-if="isProductUpload" :isProductUpload="isProductUpload" :config="config" @returnImgs="returnImgsFunc">
			上传图片</Upload>
	</el-dialog>
</template>

<script>
	import ProductApi from "@/api/product.js";
	import Upload from "@/components/file/Upload.vue";
	import draggable from "vuedraggable";
	import {
		setStorage,
		getStorage
	} from "@/utils/storageData";
	export default {
		components: {
			Upload,
			draggable,
		},
		data() {
			return {
				form: {
					options: [],
					parentId: "0",
					name: "",
					sort: 100,
					imageId: "",
					type: 0,
					multiUnit: false, // 添加多单位字段
					image: [],
					barCode: "",
					skuOne: "",
					skuTwo: "",
					skuThree: "",
					categoryId: "",
					skuValues: [],
				},
				showSku: false,
				value1: [],
				editOpen: false,
				unitList: [],
				categoryList: [],
				skuRadio: "1",
				mfrsList: [],
				specOptions2: [], // 与 skuTwo 相关的选择值数组
				specOptions3: [], // 与 skuThree 相关的选择值数组
				options: [], // 规格选项列表
				selectedSpecs: [], // 已选择的规格
				dynamicSpecs: [], // 动态生成的规格
				formRules: {
					name: [{
						required: true,
						message: "请输入名称",
						trigger: "blur",
					}, ],
					expiryNum: [{
						required: true,
						message: "请输入保质期",
						trigger: "blur",
					}, ],
					accountNum: [{
						required: true,
						message: "请输入货号",
						trigger: "blur",
					}, ],
					mfrs: [{
						required: true,
						message: "请输入品牌",
						trigger: "blur",
					}, ],

					unitId: [{
						required: true,
						message: "请输入单位",
						trigger: "blur",
					}, ],
					categoryId: [{
						required: true,
						message: "请输入分类",
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
				formLabelWidth: "120px",
				/*是否显示*/
				dialogVisible: false,
				loading: false,

				skuOneTitle: "属性1",
				skuTwoTitle: "属性2",
				skuThreeTitle: "属性3",
				skuOneList: [],
				skuTwoList: [],
				skuThreeList: [],
				activeTab: "basic", // 默认显示基本信息标签页
				specNames: [],
				specOptions: [],
				manySkuSelected: 0,
				/*是否上传图片*/
				isProductUpload: false,
				config: {},
				file_name: "",
				file_index: 0,
				/* 弹窗全屏 */
				fullscreen: false,
			};
		},
		props: ["open_add", "addform"],

		watch: {
			skuRadio: function(n, o) {
				console.log(n, o, "123");
				if (n == 1) {
					this.addOptions();
				} else {
					this.form.options.splice(0, 1);
				}
			},
		},
		created() {
			this.dialogVisible = this.open_add;
			this.options = this.addform.attribute;
			this.unitList = this.addform.unit;
			this.mfrsList = this.addform.mfrs;
			this.categoryList = this.addform.category;
			console.log(this.value1, "value1");
			this.$nextTick(() => {
				this.productGetMaxProductCode();
			});
		},
		methods: {
			productGetMaxProductCode() {
				let self = this;
				ProductApi.productGetMaxProductCode()
					.then((res) => {
						localStorage.removeItem("firstBarCode");
						self.firstBarCode = res.data;
						localStorage.setItem("firstBarCode", self.firstBarCode);
						self.addOptions();
					})
					.catch((error) => {});
			},
			someFunctionToGetIndex(skuType) {
				if (skuType == "skuOne") {
					return 0; // 返回 skuOne 对应的索引值
				} else if (skuType == "skuTwo") {
					return 1; // 返回 skuTwo 对应的索引值
				} else if (skuType == "skuThree") {
					return 2; // 返回 skuThree 对应的索引值
				} else {
					return -1; // 如果未匹配到任何规格类型，则返回 -1 或者其他默认值
				}
			},
			removeByVal(arr, val) {
				const index = arr.indexOf(val);
				if (index !== -1) {
					arr.splice(index, 1);
				}
			},
			deleteImg(index) {
				this.form.image.splice(index, 1);
			},
			openProductUpload: function(file_type, file_name, file_index) {
				this.file_name = file_name;
				this.file_index = file_index || 0;
				if (file_type == "image") {
					if (file_name == "sku") {
						this.config = {
							total: 1,
							fileType: "image",
						};
					} else {
						this.config = {
							total: 9,
							fileType: "image",
						};
					}
				} else {
					this.config = {
						total: 1,
						fileType: "video",
					};
				}
				this.isProductUpload = true;
			},
			handNum(index) {
				this.form.options[index].optionId = index + 65;
				return String.fromCharCode(index + 65);
			},
			addOptions() {
				this.form.options = this.form.options || [];
				const lastItem = this.form.options[this.form.options.length - 1];
				let newTiaoma = localStorage.getItem("firstBarCode");
				console.log(newTiaoma, "newTiaoma");
				if (lastItem && lastItem.productCode) {
					newTiaoma = parseInt(lastItem.productCode) + 1;
				}
				this.form.options.push({
					productCode: newTiaoma,
				});
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
				if (this.form.options.length === 1) {
					ElMessage.warning("至少需要保留一项选项！");
					return;
				}
				this.form.options.splice(index, 1);
			},
			async UploadImage(param, type, index) {
				let data = await upImg(param);
				if (type == 1) {
					this.form.imageId = data.fileId;
					this.form.imagePath = data.filePath;
				} else if (type == 2) {
					this.form.options[index].imageId = data.fileId;
					this.form.options[index].imagePath = data.filePath;
				}
			},
			removeChange() {},
			handleSpecChange(value) {
				this.dynamicSpecs = value.map((index) => {
					const spec = this.options[index];
					return {
						title: spec.attributeName,
						list: spec.valueList,
						value: [],
					};
				});
				this.autoSkuList();
			},
			onSkuChange() {
				this.autoSkuList();
			},
			onSkuDeSelect(index, value) {
				this.dynamicSpecs[index].value = this.dynamicSpecs[index].value.filter(
					(v) => v !== value
				);
				this.autoSkuList();
			},
			autoSkuList() {
				let skuData = this.dynamicSpecs.map((spec) => spec.value);
				let combinations = this.cartesianProduct(skuData);

				this.form.options = combinations.map((combination, index) => ({
					sku: Array.isArray(combination) ? combination.join("/") : combination,
					productCode: this.generateProductCode(index),
				}));
			},
			cartesianProduct(arr) {
				return arr.reduce((a, b) =>
					a.flatMap((d) => b.map((e) => [d, e].flat()))
				);
			},
			generateProductCode(index) {
				let currentBarCode = localStorage.getItem("firstBarCode") || 1000;
				return parseInt(currentBarCode) + index;
			},
			handleUnitChange() {
				if (this.form.multiUnit) {
					// 如果勾选了多单位，将输入框改为选择框
					this.form.region = ""; // 清空选择框的值
				} else {
					// 如果取消了多单位，将选择框改为输入框
					this.form.name = ""; // 清空输入框的值
				}
			},
			handleTabClick(tab) {
				// 点击标签页时，根据标签名判断是否显示上传图片组件
				this.isProductUpload = tab.name === "image";
			},
			/*添加用户*/
			addUser() {
				let self = this;
				// if(!self.form.skuList){
				//    ElMessage.error("属性不能为空");
				//    return;
				// }
				let params = self.form;

				console.log(params, "params");
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.editOpen = true;
						self.loading = true;
						params.skuList = params.options;
						ProductApi.productAdd(params, true)
							.then((data) => {
								self.loading = false;
								ElMessage({
									message: "添加成功",
									type: "success",
								});
								self.editOpen = false;
								self.dialogFormVisible(true);
							})
							.catch((error) => {
								self.loading = false;
								self.editOpen = false;
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
			/*获取图片*/
			returnImgsFunc(e) {
				console.log(this.form);
				if (e != null) {
					if (this.file_name == "image") {
						let imgs = this.form.image.concat(e);
						// 兼容后端
						for (let i = 0; i < imgs.length; i++) {
							if (typeof imgs[i].imageId == "undefined") {
								imgs[i].imageId = imgs[i].fileId;
							}
						}
						this.form["image"] = imgs;
					} else if (this.file_name == "sku") {
						let imgurls = this.form.options;
						console.log(e, "e.filePath");
						console.log("file_index:" + this.file_index);
						imgurls[this.file_index].imageUrl = e[0].filePath;
						console.log(imgurls, "this.form.options.imageUrl");
					}
				}
				this.isProductUpload = false;
				console.log(this.form.image, "this.form.image ");
			},
		},
	};
</script>

<style scoped lang="scss">
	// .img {
	// 	margin-top: 10px;
	// }

	// .btns {
	// 	display: flex;
	// 	flex-direction: row;
	// 	width: 96px;
	// 	height: 36px;
	// 	font-size: 14px;
	// 	text-align: center;
	// }

	// .thebtn1 {
	// 	color: #ffffff;
	// 	background-color: var(--el-color-primary);
	// 	min-width: 96px;
	// 	height: 30px;
	// 	line-height: 30px;
	// 	border-radius: 0;
	// }
</style>