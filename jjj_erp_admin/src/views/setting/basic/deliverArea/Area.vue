<template>
	<el-dialog :title="dialogTitle" v-model="dialogVisible" :fullscreen="fullscreen" :show-close="false" align-center
		append-to="#right-content-box" custom-class="area-dialog" :before-close="closeArea">
		<template #header="{ close, titleId, titleClass }">
			<div class="my-header d-b-c">
				<div :id="titleId" :class="titleClass" class="flex-1">
					{{ dialogTitle }}
				</div>
				<el-icon @click="fullscreen = !fullscreen">
					<Plus />
				</el-icon>
				<el-icon @click="close">
					<Close />
				</el-icon>
			</div>
		</template>
		<el-form :model="form" ref="form" :rules="rules">
			<el-form-item label="区域名称" prop="names">
				<el-input class="search-area form_content_width" v-model="form.names" placeholder="请输入区域名称"
					clearable></el-input>
			</el-form-item>
		</el-form>
		<div class="modal-wrapper">
			<!--选择省-->
			<div class="address-item scroll-box">
				<div class="province-item" :class="{ curr: index == province_index }" v-for="(item, index) in options"
					:key="index" @click.self="cityShow(index)">
					<el-checkbox :indeterminate="item.indeterminate" v-model="item.checked" :disabled="item.disabled"
						:label="item.value" @change="handleCheckedProvinceChange(index)">
						{{ item.name }}
					</el-checkbox>
				</div>
			</div>
			<!--选择市-->
			<!-- <div class="address-item scroll-box">
        <template v-if="province_index != null">
          <el-checkbox :indeterminate="options[province_index].indeterminate" v-model="options[province_index].checked"
            :disabled="options[province_index].disabled" @change="allCityFunc">
            全选
          </el-checkbox>
          <div class="province-item" :class="{ curr: index == province_index }"
            v-for="(item, index) in options[province_index]['children']" :key="index">
            <el-checkbox :label="item.name" v-model="item.checked" :disabled="item.disabled"
              @change="handleCheckedCityChange(index)">{{ item.name }}</el-checkbox>
          </div>
        </template>
      </div> -->
		</div>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="closeArea">取 消</el-button>
				<el-button type="primary" @click="confirmArea">确 定</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script>
	import {
		deepClone
	} from "@/utils/base";
	import SettingApi from "@/api/setting.js";
	export default {
		data() {
			return {
				/*弹窗是否打开*/
				dialogVisible: false,
				fullscreen: false,
				/*省*/
				provinceList: [],
				/*选择省的角标*/
				province_index: null,
				/*当前选中的省*/
				activeProvinceId: [],
				/*当前对象*/
				activeModel: {},
				/*区域*/
				options: [],
				/*是否全部选中*/
				all_active: false,
				/*不确定*/
				indeterminate: false,
				form: {
					names: "", // 保存区域名称
				},
				rules: {
					names: [{
						required: true,
						message: "请输入区域名称",
						trigger: "blur",
					}, ],
				},
			};
		},
		props: ["show_area", "areaModel", "areas", "filteredOptions"],
		computed: {
			dialogTitle() {
				if (this.areaModel.type == "add") {
					return "添加可配送区域";
				} else {
					return "编辑可配送区域";
				}
			},
		},

		created() {
			this.init();
		},
		methods: {
			/*初始化*/
			init() {
				this.dialogVisible = this.show_area;
				this.setList(this.areas, this.areaModel.provinceIds);
				if (this.areaModel.type == "edit") {
					this.form.names = this.areaModel.name;
				}
				if (this.areaModel.type == "add") {
					this.options = deepClone(this.filteredOptions);
				} else {
					this.options = deepClone(this.areas);
				}
				console.log(this.areaModel, "areamodel");
				this.isAll();
			},

			/*设置list*/
			setList(list, provinceIds) {
				for (let i = 0; i < list.length; i++) {
					let item = list[i];
					if (provinceIds && provinceIds.includes(item.id.toString())) {
						item.checked = true; // 设置勾选状态为 true
					} else {
						item.checked = false; // 否则设置为 false
					}
					// if (item.checked != true) {
					//   item.checked = false;
					// }
					if (item.indeterminate != true) {
						item.indeterminate = false;
					}
					if (item.index == null) {
						item.index = [];
						item.disabled = false;
					}
					if (Object.prototype.toString.call(item.children) == "[object Array]") {
						//判断子本索引个数，和其它索引个数
						let this_num = 0,
							other_num = 0,
							no_num = 0,
							count = item.children.length;
						for (let c = 0; c < count; c++) {
							let child = item.children[c];
							if (child.checked != true) {
								child.checked = false;
							}
							if (
								child.index == this.areaModel.index &&
								item.index.indexOf(this.areaModel.index) != -1
							) {
								child.checked = true;
								child.disabled = false;
								this_num++;
							} else if (
								child.index != null &&
								item.index.indexOf(child.index) != -1
							) {
								child.checked = true;
								child.disabled = true;
								other_num++;
							} else {
								child.checked = false;
								child.disabled = false;
								no_num++;
							}

							if (this_num == count || other_num == count) {
								item.checked = true;
								item.indeterminate = false;
								if (other_num == count) {
									item.disabled = true;
								} else {
									item.disabled = false;
								}
							} else if (this_num == 0 || other_num == 0) {
								item.checked = false;
								item.indeterminate = false;
								item.disabled = false;
							} else {
								item.disabled = false;
								if (this_num > 0) {
									item.checked = false;
									item.indeterminate = true;
								}
							}
						}
					}
				}
			},

			/*选择全部省和市*/
			allProvinceFunc() {
				if (!this.all_active) {
					this.all_active = false;
					this.indeterminate = false;
				} else {
					this.all_active = true;
					this.indeterminate = false;
				}

				for (let i = 0; i < this.options.length; i++) {
					let item = this.options[i];
					if (item.disabled != true) {
						item.checked = this.all_active;
						if (this.all_active) {
							item.index.push(this.areaModel.index);
						} else {
							let is_index = item.index.indexOf(this.areaModel.index);
							if (is_index != -1) {
								item.index.splice(is_index, 1);
							}
						}
						item.indeterminate = this.indeterminate;
						if (
							Object.prototype.toString.call(item.children) == "[object Array]"
						) {
							for (let c = 0; c < item.children.length; c++) {
								let child = item.children[c];
								if (child.disabled != true) {
									child.checked = this.all_active;
									if (this.all_active) {
										child.index = this.areaModel.index;
									} else {
										child.index = null;
									}
								}
							}
						}
					}
				}
			},

			/*选择省*/
			handleCheckedProvinceChange(i) {
				this.province_index = i;
				this.options[i].indeterminate = false;
				if (this.options[i].checked && this.options[i].disabled != true) {
					this.options[i].checked = true;
					this.options[i].index.push(this.areaModel.index);
				} else {
					let is_index = this.options[i].index.indexOf(this.areaModel.index);
					if (is_index != -1) {
						this.options[i].index.splice(is_index, 1);
					}
					if (this.options[i].disabled != true) {
						this.options[i].checked = false;
					}
				}
				if (
					Object.prototype.toString.call(this.options[i].children) ==
					"[object Array]"
				) {
					for (let c = 0; c < this.options[i].children.length; c++) {
						let child = this.options[i].children[c];
						if (child.disabled != true) {
							child.checked = this.options[i].checked;
							child.index = this.areaModel.index;
						}
					}
				}
				this.isAll();
			},

			/*选择全部的市*/
			allCityFunc() {
				let flag = this.options[this.province_index].checked;
				if (flag) {
					if (
						this.options[this.province_index].index.indexOf(
							this.areaModel.index
						) == -1
					) {
						this.options[this.province_index].index.push(this.areaModel.index);
					}
				} else {
					let is_index = this.options[this.province_index].index.indexOf(
						this.areaModel.index
					);
					if (is_index != -1) {
						this.options[this.province_index].index.splice(is_index, 1);
					}
				}
				this.options[this.province_index].indeterminate = false;
				for (
					let c = 0; c < this.options[this.province_index].children.length; c++
				) {
					let child = this.options[this.province_index].children[c];
					if (child.disabled != true) {
						child.checked = flag;
						if (flag) {
							child.index = this.areaModel.index;
						} else {
							child.index = null;
						}
					}
				}
				this.isAll();
			},

			/*选择市*/
			handleCheckedCityChange(i) {
				let flag = this.options[this.province_index].children[i].checked;
				let indeterminate =
					this.options[this.province_index].children[i].indeterminate;
				let _index = this.options[this.province_index].index.indexOf(
					this.areaModel.index
				);
				let n = this.isChooseAll(this.options[this.province_index].children);
				if (flag) {
					if (_index == -1) {
						this.options[this.province_index].index.push(this.areaModel.index);
					}
					this.options[this.province_index].children[i].index =
						this.areaModel.index;
				} else {
					if (_index != -1 && indeterminate == false) {
						this.options[this.province_index].index.splice(_index, 1);
					} else if (_index == -1 && indeterminate == true) {
						this.options[this.province_index].index.push(this.areaModel.index);
					}
					this.options[this.province_index].children[i].index = null;
				}
				if (n == 0) {
					this.options[this.province_index].checked = false;
					this.options[this.province_index].indeterminate = false;
				} else if (n == 2) {
					this.options[this.province_index].checked = true;
					this.options[this.province_index].indeterminate = false;
				} else {
					this.options[this.province_index].checked = false;
					this.options[this.province_index].indeterminate = true;
				}
				this.isAll();
			},

			/*判断是否选中了所有的省和市*/
			isAll() {
				let n = this.isChooseAll(this.options);
				if (n == 0) {
					this.all_active = false;
					this.indeterminate = false;
				} else if (n == 2) {
					this.all_active = true;
					this.indeterminate = false;
				} else {
					this.all_active = false;
					this.indeterminate = true;
				}
			},

			/*判断列表是否全选*/
			isChooseAll(list) {
				let allcount = 0;
				if (Object.prototype.toString.call(list) == "[object Array]") {
					for (let i = 0; i < list.length; i++) {
						if (
							Object.prototype.toString.call(list[i].index) == "[object Array]"
						) {
							if (
								(list[i]["checked"] == true &&
									list[i].index.indexOf(this.areaModel.index) != -1) ||
								list[i]["indeterminate"] == true
							) {
								allcount++;
							}
						} else {
							if (
								list[i]["checked"] == true ||
								list[i]["indeterminate"] == true
							) {
								allcount++;
							}
						}
					}
					if (allcount == list.length) {
						return 2;
					} else {
						if (allcount > 0) {
							return 1;
						} else {
							return 0;
						}
					}
				} else {
					return 2;
				}
			},

			/*选择省 - 只是展示城市列表*/
			cityShow(i) {
				this.province_index = i;
			},

			/*关闭弹窗*/
			closeArea() {
				this.dialogVisible = false;
				this.$emit("closeArea", {
					statu: false,
					type: "cancel",
				});
			},
			/*确认并关闭弹窗*/
			confirmArea() {
				this.$refs.form.validate((valid) => {
					if (valid) {
						const params = {
							name: this.form.names,
							deliverAreaId: this.areaModel.deliverAreaId,
							provinceIdList: [],
							provinceNameList: [],
						};
						// 遍历已选省份，获取id和name
						this.options.forEach((option) => {
							if (option.checked) {
								params.provinceIdList.push(option.id);
								params.provinceNameList.push(option.name);
							}
						});
						console.log(params, "params");
						if (this.areaModel.type == "add") {
							SettingApi.deliverAreaAdd(params)
								.then((data) => {
									this.dialogVisible = false;
									ElMessage({
										message: "添加成功",
										type: "success",
									});
									let obj = {
										statu: false,
										type: "confirm",
										this_area: this.options,
										total_area: [],
									};
									this.$emit("closeArea", obj);
								})
								.catch((error) => {});
						} else {
							SettingApi.deliverAreaEdit(params)
								.then((data) => {
									this.dialogVisible = false;
									ElMessage({
										message: "修改成功",
										type: "success",
									});
									let obj = {
										statu: false,
										type: "confirm",
										this_area: this.options,
										total_area: [],
									};
									this.$emit("closeArea", obj);
								})
								.catch((error) => {});
						}
					}
				});
			},
		},
	};
</script>

<style>
	.area-dialog .el-dialog__header {
		border-bottom: 1px solid #dddddd;
	}

	.area-dialog .el-dialog__body {
		padding: 20px;
	}

	.area-dialog .modal-wrapper {
		display: -webkit-box;
		display: -ms-flexbox;
		display: flex;
	}

	.area-dialog .scroll-box {
		overflow-y: auto;
	}

	.area-dialog .address-item {
		display: flex;
		flex-wrap: wrap;
		border: 1px solid #eee;
		padding: 6px 0;
		/* height: 400px; */
		/* 保持容器高度 */
		box-sizing: border-box;
	}

	.select-all {
		height: 40px;
		/* 设置全选区域高度 */
		border-bottom: 1px solid #ddd;
		/* 添加底部边框 */
		padding: 10px 0;
		/* 设置内边距 */
	}

	.search-area {
		margin: 0 auto;
		/* width: 200px; */
	}

	.province-list {
		overflow-y: auto;
		/* 允许滚动 */
		max-height: calc(400px - 40px);
		/* 设置最大高度，减去全选区域高度 */
	}

	.area-dialog .el-checkbox {
		padding: 8px 10px 8px 22px;
		min-height: 20px;
		box-sizing: border-box;
		margin-right: 0;
		position: relative;
	}

	.area-dialog .province-item {
		cursor: pointer;
		width: calc(33.33% - 2px);
		/* 每个省份名称元素占据1/3宽度 */
		margin-right: 2px;
		/* 间距为2px */
		margin-bottom: 2px;
		/* 底部间距为2px */
		/* text-align: center; */
		/* 居中对齐 */
		font-size: 14px;
		/* 字体大小 */
		line-height: 30px;
		/* 行高 */
	}

	.area-dialog .province-item:hover,
	.area-dialog .province-item.curr {
		background: #eeeeee;
	}
</style>