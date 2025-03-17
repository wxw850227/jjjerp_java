<template>
	<el-dialog :title="titleName" v-model="dialogVisible" @close="dialogFormVisible" :close-on-click-modal="false"
		:close-on-press-escape="false" :fullscreen="fullscreen" :show-close="false" align-center>
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					{{ titleName }}
				</div>
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
			<el-form size="small" :model="form" :rules="formRules" ref="form">
				<el-row  :gutter="20">
					<el-col :span="5">
						<el-form-item label="商品编码" prop="productCode">
							<el-input v-model="form.productCode" autocomplete="off" placeholder="不填自动生成"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label="名称" prop="name">
							<el-input v-model="form.name" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="4">
						<el-form-item label="单位" prop="unitId">
							<el-select v-model="form.unitId" placeholder="请选择">
								<el-option v-for="(item, index) in unitList" :key="index" :label="item.name"
									:value="item.basicUnitId" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label="分类" prop="categoryId">
							<el-tree-select v-model="form.categoryId" :data="categoryList" check-strictly
								:render-after-expand="false" :props="{ value: 'categoryId', label: 'name' }"
								:default-expand-all="true" filterable clearable />
						</el-form-item>
					</el-col>
					<el-col :span="5">
						<el-form-item label="保质期" prop="expiryNum">
							<el-input v-model="form.expiryNum" autocomplete="off">
								<template #append>天</template></el-input>
						</el-form-item>
					</el-col>
					</el-row>
						<el-row :gutter="20">
					<el-col :span="6">
						<el-form-item label="货号" prop="accountNum"  class="ww100">
							<el-input v-model="form.accountNum" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="重量" prop="weight"  class="ww100">
							<el-input v-model="form.weight" autocomplete="off">
								<template #append>千克(kg)</template></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="体积" prop="volume"  class="ww100">
							<el-input v-model="form.volume" autocomplete="off">
								<template #append>立方米(m³)</template></el-input>
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
					<el-col v-if="manySkuSelected >= 1" :span="12">
						<el-form-item :label="skuOneTitle">
							<el-select v-model="form.skuOne" multiple placeholder="请选择" @change="onSkuChange"
								@remove-tag="onSkuOneDeSelect"
								style="width: 240px; margin-right: 16px; vertical-align: middle">
								<el-option v-for="(item, index) in skuOneList" :label="item" :key="index"
									:value="item" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col v-if="manySkuSelected >= 2" :span="12">
						<el-form-item :label="skuTwoTitle">
							<el-select v-model="form.skuTwo" multiple placeholder="请选择" @change="onSkuChange"
								@remove-tag="onSkuOneDeSelect"
								style="width: 240px; margin-right: 16px; vertical-align: middle">
								<el-option v-for="(item, index) in skuTwoList" :label="item" :key="index"
									:value="item" />
							</el-select>
						</el-form-item>
					</el-col>
					<el-col v-if="manySkuSelected >= 3" :span="8">
						<el-form-item :label="skuThreeTitle">
							<el-select v-model="form.skuThree" multiple placeholder="请选择" @change="onSkuChange"
								@remove-tag="onSkuOneDeSelect"
								style="width: 240px; margin-right: 16px; vertical-align: middle">
								<el-option v-for="(item, index) in skuThreeList" :label="item" :key="index"
									:value="item" />
							</el-select>
						</el-form-item>
					</el-col>
					</el-row>
					<el-row>	
					<el-col :span="12">
						<el-form-item label="备注" prop="remark">
							<el-input v-model="form.remark" type="textarea" autocomplete="off"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<!-- <div class="btns">
				<el-button class="thebtn1" @click="handleButtonClick">批发价-批量</el-button>
				<el-button class="thebtn1" @click="handleButtonClick">进价-批量</el-button>
				<el-button class="thebtn1" @click="handleButtonClick">吊牌价-批量</el-button>
				<el-button class="thebtn1" @click="handleButtonClick">标准售价-批量</el-button>
			</div> -->
			<el-table :data="form.options" border style="margin-top: 6px">
				<el-table-column type="selection" width="55"></el-table-column>

				<el-table-column label="操作" width="80">
					<template #default="{ row, $index }">
						<div>
							<el-icon style="margin-right: 8px" :size="20" @click="addOptions">
								<Plus />
							</el-icon>
							<el-icon v-if="row.deleteFlag == 0" :size="20" @click="removeOptions($index)">
								<Lock />
							</el-icon>
							<el-icon v-else :size="20" @click="removeOptions($index)">
								<Unlock />
							</el-icon>
						</div>
					</template>
				</el-table-column>
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
				<!-- <el-table-column label="单位">
          <template #default="{ row }">
            <el-input v-model="row.description" label="单位" />
          </template>
        </el-table-column> -->
				<el-table-column label="多属性">
					<template #default="{ row }">
						<el-input v-model="row.sku" label="多属性" disabled />
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
				<!-- <el-table-column prop="imageUrl" label="图片" width="150">
          <template #default="{ row }">
            <div class="draggable-list">
              <div
                v-if="row.imageUrl == ''"
                @click="openProductUpload('image', 'sku')"
              >
                <el-icon>
                  <Plus />
                </el-icon>
              </div>
              <div
                v-if="row.imageUrl != ''"
                class="item"
                @click="openProductUpload('image', 'sku')"
              >
                <img :src="row.imageUrl" />
              </div>
            </div>
          </template>
        </el-table-column> -->
			</el-table>
			</div>
		</div>
		<div v-show="activeTab === 'image'">
			<el-form-item label="图片信息：" :rules="[{ required: true, message: '请上传图片' }]" prop="image">
				<div class="draggable-list small">
					<draggable class="wrapper" v-model="form.image">
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
				<el-button type="primary" @click="editUser" :loading="loading">确 定</el-button>
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
					image: [],
					barCode: "",
					skuOne: "",
					skuTwo: "",
					skuThree: "",
					categoryId: "",
					skuList: [],
				},
				value1: "",
				unitList: [],
				categoryList: [],
				firstBarCode: "",
				fullscreen: false,
				mfrsList: [],
				specOptions2: [], // 与 skuTwo 相关的选择值数组
				specOptions3: [], // 与 skuThree 相关的选择值数组
				options: [],
				titleName: "编辑商品",
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
				/*是否上传图片*/
				isProductUpload: false,
				config: {},
				file_name: "",
				file_index: 0,
				skuOneTitle: "属性1",
				skuTwoTitle: "属性2",
				skuThreeTitle: "属性3",
				skuOneList: [],
				skuTwoList: [],
				skuThreeList: [],
				activeTab: "basic", // 默认显示基本信息标签页
				specNames: [], // 存储多规格选项名称
				specOptions: [], // 存储多规格选项的选择值
				manySkuSelected: 0,
			};
		},
		props: ["open_edit", "editform", "copy"],
		created() {
			this.dialogVisible = this.open_edit;
			if (this.dialogVisible) {
				console.log(this.dialogVisible, "this.form");
				this.$nextTick(async () => {
					this.form = this.editform;
					this.productToAdd();
					if (this.copy) {
						await this.productGetMaxProductCode();
						this.form.productCode = "";
						let firstBarCode = parseInt(localStorage.getItem("firstBarCode"), 10);
						this.form.skuList.forEach((option) => {
							option.productCode = firstBarCode.toString();
							firstBarCode += 1;
						});
						this.titleName = "复制商品";
					}
					this.form.options = this.form.skuList;
					console.log(this.form.options, "this.options");
				});
			}
		},
		methods: {
			productGetMaxProductCode() {
				return new Promise((resolve, reject) => {
					ProductApi.productGetMaxProductCode()
						.then((res) => {
							localStorage.removeItem("firstBarCode");
							this.firstBarCode = res.data;
							localStorage.setItem("firstBarCode", this.firstBarCode);
							resolve();
						})
						.catch((error) => {
							reject(error);
						});
				});
			},
			productToAdd() {
				let self = this;
				ProductApi.productToAdd()
					.then((res) => {
						self.options = res.data.attribute;
						self.unitList = res.data.unit;
						self.mfrsList = res.data.mfrs;
						self.categoryList = res.data.category;
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
			onSkuChange(selectedValue, selectedOption) {
				let skuOneData = this.form.skuOne;
				let skuTwoData = this.form.skuTwo;
				let skuThreeData = this.form.skuThree;
				this.autoSkuList(skuOneData, skuTwoData, skuThreeData);
			},
			onSkuOneDeSelect(value) {
				let skuOneData = this.form.skuOne;
				let skuTwoData = this.form.skuTwo;
				let skuThreeData = this.form.skuThree;
				this.removeByVal(skuOneData, value);
				this.autoSkuList(skuOneData, skuTwoData, skuThreeData);
			},
			onSkuTwoDeSelect(value) {
				let skuOneData = this.form.skuOne;
				let skuTwoData = this.form.skuTwo;
				let skuThreeData = this.form.skuThree;
				this.removeByVal(skuTwoData, value);
				this.autoSkuList(skuOneData, skuTwoData, skuThreeData);
			},
			onSkuThreeDeSelect(value) {
				let skuOneData = this.form.skuOne;
				let skuTwoData = this.form.skuTwo;
				let skuThreeData = this.form.skuThree;
				this.removeByVal(skuThreeData, value);
				this.autoSkuList(skuOneData, skuTwoData, skuThreeData);
			},
			autoSkuList(skuOneData, skuTwoData, skuThreeData) {
				let firstBarCodes = localStorage.getItem("firstBarCode");
				// 清空现有的商品表格数据
				let meTableData = [];
				let currentBarCode = firstBarCodes;
				// 计算可能的组合，并生成商品表格数据
				skuOneData.forEach((sku1) => {
					if (skuTwoData) {
						skuTwoData.forEach((sku2) => {
							if (skuThreeData) {
								skuThreeData.forEach((sku3) => {
									// 生成新的商品数据对象
									let newData = {
										sku: `${sku1}/${sku2}/${sku3}`, // 多属性字段值
										productCode: currentBarCode,
									};
									// 将新的商品数据对象添加到商品表格数据数组中
									meTableData.push(newData);
									currentBarCode++;
								});
							} else {
								// 生成新的商品数据对象（无第三个属性）
								let newData = {
									sku: `${sku1}/${sku2}`, // 多属性字段值
									productCode: currentBarCode,
									// 其他字段值根据需要添加
								};
								// 将新的商品数据对象添加到商品表格数据数组中
								meTableData.push(newData);
								currentBarCode++;
							}
						});
					} else {
						// 生成新的商品数据对象（无第二、第三个属性）
						let newData = {
							sku: `${sku1}`, // 多属性字段值
							productCode: currentBarCode,
						};
						// 将新的商品数据对象添加到商品表格数据数组中
						meTableData.push(newData);
						currentBarCode++;
					}
				});
				console.log(this.form.sku, "this.form.sku");
				console.log(this.form.options, "option");
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
				this.productGetMaxProductCode();
				this.form.options = this.form.options || [];
				const lastItem = this.form.options[this.form.options.length - 1];
				let newTiaoma = localStorage.getItem("firstBarCode");
				let newbianma = Number(newTiaoma) + 1;
				// if (lastItem && lastItem.productCode) {
				//   newTiaoma = parseInt(lastItem.productCode) + 1;
				// }
				this.form.options.push({
					productCode: newbianma,
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
				const optionToRemove = this.form.options[index];
				// 将选项的 deleteFlag 属性设置为 1
				if (optionToRemove.deleteFlag == 0) {
					optionToRemove.deleteFlag = 1;
					ElMessage.success("该属性已禁用");
				} else {
					this.form.options.splice(index, 1);
				}
			},
			async UploadImage(param, type, index) {
				let data = await upImg(param);
				console.log(data, "data");
				if (type == 1) {
					this.form.imageId = data.fileId;
					this.form.imagePath = data.filePath;
				} else if (type == 2) {
					this.form.options[index].imageId = data.fileId;
					this.form.options[index].imagePath = data.filePath;
				}
			},
			handleSpecChange(value) {
				this.manySkuSelected = value.length;
				console.log(this.manySkuSelected, "manySkuSelected");
				this.options.forEach((item) => {
					if (value.length < 3) {
						item.disabled = false;
					} else {
						item.disabled = value.indexOf(item.value) === -1;
					}
				});
				if (value.length <= 3) {
					let skuOneId = value[0];
					let skuTwoId = value[1];
					let skuThreeId = value[2];
					console.log(skuOneId, "skuOneId");
					this.options.forEach((item, index) => {
						console.log(item, "index");
						if (index == skuOneId) {
							this.skuOneTitle = item.attributeName;
							this.skuOneList = this.options[index].valueList;
						}
						if (index == skuTwoId) {
							this.skuTwoTitle = item.attributeName;
							this.skuTwoList = this.options[index].valueList;
						}
						if (index == skuThreeId) {
							this.skuThreeTitle = item.attributeName;
							this.skuThreeList = this.options[index].valueList;
						}
					});
				}
				this.form.options = [];
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
			editUser() {
				let self = this;
				let params = self.form;
				console.log(params, "params");
				self.$refs.form.validate((valid) => {
					if (valid) {
						self.loading = true;
						params.skuList = params.options;
						console.log(self.copy, "copy");
						if (self.copy) {
							console.log(params, "params");
							ProductApi.productAdd(params, true)
								.then((data) => {
									self.loading = false;
									ElMessage({
										message: "复制成功",
										type: "success",
									});
									self.dialogFormVisible(true);
								})
								.catch((error) => {
									self.loading = false;
								});
						} else {
							ProductApi.productEdit(params, true)
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
					}
				});
			},

			/*关闭弹窗*/
			dialogFormVisible(e) {
				this.form.options = [];
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
				this.isProductUpload = true;
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

<style scoped>
	/* .img {
		margin-top: 10px;
	}

	.btns {
		display: flex;
		flex-direction: row;
		width: 96px;
		height: 30px;
		font-size: 14px;
		text-align: center;

		.thebtn1 {
			color: #ffffff;
			background-color: var(--el-color-primary);
		}
	} */
</style>