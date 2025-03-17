<template>
	<div class="external">
		<div class="left-menu" :style="'width:' + width + 'px;'">
			<div class="d-b-c search-box">
				<span style="width: 90px">仓库(必选)：</span>
				<el-select v-model="selectedOption" placeholder="请选择" style="width: 120px" @change="handleDepotChange">
					<el-option v-for="(item, index) in selectOptions" :key="index" :label="item.name"
						:value="item.depotId"></el-option>
				</el-select>
				<el-button class="ml10 fb add-btn" icon="Plus" circle @click="addNode" :disabled="selectedNode.leaf">
				</el-button>
			</div>

			<el-tree style="max-width: 600px" :data="treeData" :props="treeProps" node-key="id" :lazy="true"
				:load="loadNode" default-expand-all @node-click="handleNodeClick">
				<template #default="{ node, data }">
					<span class="node-content">
						<!-- <sapn class="node-Folder"
              ><el-icon><Folder /></el-icon
            ></sapn> -->
						<span class="node-Folder icon iconfont icon-wenjian"></span>
						<span class="node-label" v-if="node.level === 1">【区域】{{ data.areaName }}</span>
						<span class="node-label" v-if="node.level === 2">【库区】{{ data.reservoirName }}</span>
						<span class="node-label" v-if="node.level === 3">【货架】{{ data.shelfName }}</span>
						<span class="node-icon">
							<el-icon @click="editNode(node)" class="edit-icon">
								<EditPen />
							</el-icon>
							<el-icon @click="deleteNode(node)" class="delete-icon">
								<Delete />
							</el-icon>
						</span>
					</span>
				</template>
			</el-tree>
			<el-dialog :title="dialogTitle" @close="dialogFormVisible" v-model="editDialogVisible"
				:fullscreen="fullscreen" :show-close="false" align-center width="380px">
				<template #header="{ close, titleId, titleClass }">
					<div class="my-header d-b-c">
						<div :id="titleId" :class="titleClass" class="flex-1">
							{{ dialogTitle }}
						</div>
						<!-- <el-icon @click="fullscreen = !fullscreen">
							<Plus />
						</el-icon> -->
						<el-icon @click="close">
							<Close />
						</el-icon>
					</div>
				</template>
				<el-form size="small" :model="form" :rules="formRules" ref="form" :label-width="formLabelWidth">
					<el-form-item label="编号" prop="areaCode" v-if="(!depotAreaIds && !depotReservoirIds) || areaStatus">
						<el-input v-model="form.areaCode" style="width: 100%;"></el-input>
					</el-form-item>
					<el-form-item label="编号" prop="reservoirCode" v-if="depotAreaIds || reservoirStatus">
						<el-input v-model="form.reservoirCode" style="width: 100%;"></el-input>
					</el-form-item>
					<el-form-item label="编号" prop="shelfCode" v-if="depotReservoirIds || shelfStatus">
						<el-input v-model="form.shelfCode" style="width: 100%;"></el-input>
					</el-form-item>
					<el-form-item label="名称" prop="areaName" v-if="(!depotAreaIds && !depotReservoirIds) || areaStatus">
						<el-input v-model="form.areaName" style="width: 100%;"></el-input>
					</el-form-item>
					<el-form-item label="名称" prop="reservoirName" v-if="depotAreaIds || reservoirStatus">
						<el-input v-model="form.reservoirName" style="width: 100%;"></el-input>
					</el-form-item>
					<el-form-item label="名称" prop="shelfName" v-if="depotReservoirIds || shelfStatus">
						<el-input v-model="form.shelfName" style="width: 100%;"></el-input>
					</el-form-item>
				</el-form>
				<template #footer>
					<div class="dialog-footer">
						<el-button @click="dialogFormVisible">取 消</el-button>
						<el-button type="primary" @click="addUser">确 定</el-button>
					</div>
				</template>
			</el-dialog>
		</div>

		<div class="all">
			<div class="right-container">
				<div class="common-seach-wrap">
					<el-form size="small" :inline="true" :model="formInline" class="demo-form-inline">
						<el-form-item label="货位编码">
							<el-input v-model="formInline.storageCode" size="small" placeholder="请输入货位编码查询"></el-input>
						</el-form-item>
						<el-form-item label="货位类型">
							<el-select v-model="formInline.storageType" size="small" placeholder="">
								<el-option label="全部" :value="0"></el-option>
								<el-option v-for="(item, index) in commentList" :key="index" :label="item.name"
									:value="item.val"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item>
							<el-button size="small" type="primary" @click="onSubmit">查询</el-button>
							<el-button size="small" @click="resetQuery">重置</el-button>
						</el-form-item>
					</el-form>
					<div class="common-level-rail  d-b-c">
						<div class="d-s-c flex-1">
						<el-button size="small" type="primary" icon="Plus" @click="addClick"
							:disabled="addIsOpen">新增</el-button>
						<el-button size="small" type="primary" @click="addClick"
							v-auth="'/setting/basic/goods/delete'">删除</el-button>
						<el-button size="small" type="primary" :disabled="batchAddIsOpen"
							@click="batchAddClick">批量新增</el-button>
						<el-button size="small" type="primary" @click="addClick"
							v-auth="'/setting/basic/goods/add'">货位转移</el-button>
						<el-button size="small" type="primary" @click="addClick"
							v-auth="'/setting/supplier/add'">导入货位库存</el-button>
						<el-button size="small" type="primary" @click="addClick"
							v-auth="'/setting/supplier/add'">导出</el-button>
						<el-button size="small" type="primary" @click="addClick"
							v-auth="'/setting/supplier/add'">导出货位库存</el-button>			
						</div>
						<div class="pagination">
							<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
								:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
								layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
							</el-pagination>
						</div>
					</div>
				</div>
				<el-table class="flex-1" scrollbar-always-on :data="staticTableData" v-loading="loading">
					<el-table-column prop="id" label="ID" width="55"></el-table-column>
					<el-table-column type="selection" width="55"></el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template #default="scope">
							<div style="white-space: nowrap" class="d-c-c">
								<el-button @click="editClick(scope.row)" type="primary" link size="small">修改</el-button>
								<el-dropdown size="small">
									<el-button type="primary" link size="small">更多<el-icon>
											<ArrowDown />
										</el-icon></el-button>
									<template #dropdown>
										<el-dropdown-menu>
											<el-dropdown-item @click="setClick(scope.row)">设置货位库存</el-dropdown-item>
											<el-dropdown-item @click="delClick(scope.row)">删除</el-dropdown-item>
										</el-dropdown-menu>
									</template>
								</el-dropdown>
							</div>
						</template>
					</el-table-column>

					<el-table-column label="库区" prop="reservoirName"></el-table-column>
					<el-table-column label="货位编码(同个仓库不能重复)" prop="storageCode" width="180"></el-table-column>

					<el-table-column label="货位类型" prop="storageType">
						<template #default="scope">
							<span v-if="scope.row.storageType == 0">全部</span>
							<span v-if="scope.row.storageType == 1">拣货货位</span>
							<span v-if="scope.row.storageType == 2">存储货位</span>
							<span v-if="scope.row.storageType == 3">异常货位</span>
						</template>
					</el-table-column>
					<el-table-column width="150" label="更新日期" prop="updateTime">
						<template #default="scope">
							<span>{{ parseTime(scope.row.updateTime) }}</span>
						</template>
					</el-table-column>
					<el-table-column label="存放商品" prop="stockStr" width="220"></el-table-column>
				</el-table>
			</div>
		</div>

		<Add v-if="open_add" :open_add="open_add" :addform="addform" @closeDialog="closeDialogFunc($event, 'add')">
		</Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel"
			@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
		<Set v-if="open_set" :open_set="open_set" :setForm="setmodel" @closeDialog="closeDialogFunc($event, 'set')">
		</Set>
		<Batch v-if="batch_add" :batch_add="batch_add" :batchAddform="batchAddform"
			@closeDialog="closeDialogFunc($event, 'batch')"></Batch>
	</div>
</template>

<script>
	import {
		parseTime
	} from "@/utils/index";
	import SettingApi from "@/api/setting.js";
	import {
		ElLoading
	} from "element-plus";
	import Add from "./add.vue";
	import Edit from "./edit.vue";
	import Set from "./setShelf.vue";
	import Batch from "./batchAdd.vue";
	export default {
		components: {
			Add,
			Edit,
			Set,
			Batch,
		},
		data() {
			return {
				treeProps: {
					children: ["reservoirList", "shelfList"],
					label: (data, node) => {
						if (node.level === 0) {
							return data.areaName;
						} else if (node.level === 1) {
							return data.areaName;
						} else if (node.level === 2) {
							return data.reservoirName;
						} else if (node.level === 3) {
							return data.shelfName;
						}
						return "";
					},
				},
				filterFormText: "",
				treeData: [], // 树数据
				selectedOption: "", // 选择的选项值
				selectOptions: [],
				reservoirList: [],
				categoryModel: {},
				setmodel: {},
				shelfList: [],
				open_add: false,
				open_edit: false,
				open_set: false,
				fullscreen: false,
				batch_add: false,
				curPage: 1,
				pageSize: 20,
				totalDataNumber: 0,
				addform: {},
				batchAddform: {},
				form: {
					areaCode: "",
					depotId: "",
					areaName: "",
					reservoirCode: "",
					reservoirName: "",
					areaId: "",
					shelfCode: "",
					shelfName: "",
					reservoirId: "",
				},
				editDialogVisible: false,
				depotAreaIds: "",
				depotReservoirIds: "",
				depotIds: "",
				loadings: null,
				staticTableData: [],
				formInfo: {
					sysGenCode: "",
					formType: "",
				},
				formName: "",
				delLoading: false,
				loading: true,
				sup_this: this,
				selectedNode: "",
				areaStatus: false,
				reservoirStatus: false,
				addIsOpen: true,
				batchAddIsOpen: true,
				shelfStatus: false,
				openAdd: false,
				openEdit: false,
				formInline: {
					name: "",
					pageIndex: 1,
					pageSize: 10,
					depotId: "",
					areaId: "",
					reservoirId: "",
					depotShelfId: "",
					storageType: "",
					storageCode: "",
				},
				commentList: [{
						name: "拣货货位",
						val: 1,
					},
					{
						name: "存储货位",
						val: 2,
					},
					{
						name: "异常货位",
						val: 3,
					},
				],
			};
		},
		watch: {
			// selectedOption(newVal) {
			//   // 监听 selectedOption 的变化，当变化时调用 getTreeData 方法
			//   this.getTreeData(newVal);
			// },
			filterFormText(val) {
				const _thisForm = this.$refs.formTree;
				if (_thisForm) {
					_thisForm.filter(val);
				}
			},
		},
		computed: {
			dialogTitle() {
				if (this.depotAreaIds) {
					if (!this.reservoirStatus) {
						return "添加库区";
					} else {
						return "编辑库区";
					}
				} else if (this.depotReservoirIds) {
					if (!this.shelfStatus) {
						return "添加货架";
					} else {
						return "编辑货架";
					}
				} else if (!this.depotAreaIds && !this.depotReservoirIds) {
					if (!this.areaStatus) {
						return "添加区域";
					} else {
						return "编辑区域";
					}
				}
			},
		},
		created() {
			this.getData();
			// this.$nextTick(()=>{
			//    this.getStorageList();
			// })
		},
		methods: {
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.getData();
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getData();
			},
			delClick(row) {
				let self = this;
				console.log(row, "row");
				ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						self.delLoading = true;
						SettingApi.depotStorageDel({
								depotStorageId: row.depotStorageId
							})
							.then((res) => {
								self.delLoading = false;
								ElMessage({
									message: "删除成功",
									type: "success",
								});
								self.getStorageList(row.reservoirId, 2);
							})
							.catch((error) => {});
					})
					.catch(() => {
						ElMessage({
							type: "info",
							message: "已取消删除",
						});
					});
			},
			onSubmit() {
				this.getStorageList();
			},
			getStorageList(id, index) {
				let self = this;
				self.loading = true;
				let Params = self.formInline;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				if (index == 1) {
					Params.areaId = id;
				} else if (index == 2) {
					Params.reservoirId = id;
				} else if (index == 3) {
					Params.depotShelfId = id;
				} else {
					Params.depotId = self.depotIds;
				}
				SettingApi.depotStorageList(Params, true)
					.then((res) => {
						self.loading = false;
						self.staticTableData = res.data.records;
						self.totalDataNumber = res.data.total;
						self.formInline = {};
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			parseTime,
			setClick(row) {
				this.open_set = true;
				this.setmodel = row;
				console.log("row", row);
			},
			batchAddClick() {
				this.batch_add = true;
			},
			addClick() {
				this.open_add = true;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
			},
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_add = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "edit") {
					this.open_edit = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "set") {
					this.open_set = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "batch") {
					this.batch_add = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
			},
			handleNodeClick(node) {
				this.selectedNode = node;
				if (this.selectedNode.depotAreaId) {
					let depot = this.selectedNode.depotAreaId;
					this.getStorageList(depot, 1);
					this.addIsOpen = true;
					this.batchAddIsOpen = true;
				}
				if (this.selectedNode.depotReservoirId) {
					let area = this.selectedNode.depotReservoirId;
					this.getStorageList(area, 2);
					this.addIsOpen = false;
					this.batchAddIsOpen = false;
					this.addform = this.selectedNode;
					this.batchAddform = this.selectedNode;
				}
				if (this.selectedNode.depotShelfId) {
					let reservoir = this.selectedNode.depotShelfId;
					this.getStorageList(reservoir, 3);
					this.addIsOpen = false;
					this.batchAddIsOpen = false;
					this.addform = this.selectedNode;
					this.batchAddform = this.selectedNode;
				}
				console.log(node, "this.selectedNode11");
			},
			handleDepotChange() {
				this.getTreeData(this.selectedOption);
				this.depotIds = this.selectedOption;
				console.log("depotIds", this.depotIds);
			},
			addNode() {
				if (!this.selectedNode) {
					this.loadings = ElLoading.service({
						lock: true,
						text: "Loading",
						background: "rgba(0, 0, 0, 0.7)",
					});
					setTimeout(() => {
						this.loadings.close();
						//   this.editDialogVisible = true;
					}, 1000);
				}
				console.log("selectedNode", this.selectedNode);
				if (this.selectedNode.depotAreaId) {
					this.depotAreaIds = this.selectedNode.depotAreaId;
				}
				if (this.selectedNode.id) {
					this.depotReservoirIds = this.selectedNode.id;
				}
				this.openAdd = true;
				this.editDialogVisible = true;
			},
			addUser() {
				let self = this;
				let params = self.form;
				if (self.openAdd) {
					if (self.depotAreaIds && !self.shelfStatus) {
						self.form.areaId = self.depotAreaIds;
						console.log(params, "params");
						self.$refs.form.validate((valid) => {
							if (valid) {
								SettingApi.depotReservoirAdd(params)
									.then((data) => {
										ElMessage({
											message: "添加成功",
											type: "success",
										});
										self.dialogFormVisible();
										self.getData();
									})
									.catch((error) => {});
							}
						});
					} else if (self.depotReservoirIds && !self.reservoirStatus) {
						self.form.reservoirId = self.depotReservoirIds;
						console.log(params, "params");
						self.$refs.form.validate((valid) => {
							if (valid) {
								SettingApi.depotShelfAdd(params)
									.then((data) => {
										ElMessage({
											message: "添加成功",
											type: "success",
										});
										self.dialogFormVisible();
										self.getData();
									})
									.catch((error) => {});
							}
						});
					} else if (!self.areaStatus) {
						self.form.depotId = self.depotIds;
						console.log(params, "params");
						self.$refs.form.validate((valid) => {
							if (valid) {
								SettingApi.depotAreaAdd(params)
									.then((data) => {
										ElMessage({
											message: "添加成功",
											type: "success",
										});
										self.getTreeData(self.depotIds);
										self.dialogFormVisible();
									})
									.catch((error) => {});
							}
						});
					}
				}
				if (self.openEdit) {
					if (self.areaStatus) {
						console.log(params, "params");
						self.$refs.form.validate((valid) => {
							if (valid) {
								SettingApi.depotAreaEdit(params)
									.then((data) => {
										ElMessage({
											message: "修改成功",
											type: "success",
										});
										self.getTreeData(self.depotIds);
										self.dialogFormVisible();
									})
									.catch((error) => {});
							}
						});
					} else if (self.reservoirStatus) {
						console.log(params, "params");
						self.$refs.form.validate((valid) => {
							if (valid) {
								SettingApi.depotReservoirEdit(params)
									.then((data) => {
										ElMessage({
											message: "修改成功",
											type: "success",
										});
										self.dialogFormVisible();
										self.getData();
									})
									.catch((error) => {});
							}
						});
					} else if (self.shelfStatus) {
						console.log(params, "params");
						self.$refs.form.validate((valid) => {
							if (valid) {
								SettingApi.depotShelfEdit(params)
									.then((data) => {
										ElMessage({
											message: "修改成功",
											type: "success",
										});
										self.dialogFormVisible();
										self.getData();
									})
									.catch((error) => {});
							}
						});
					}
				}
			},
			editNode(node) {
				if (!this.selectedNode) {
					ElMessage.warning("请先选择一个节点");
					return;
				}

				// 根据节点的级别进行相应的处理
				if (node.level === 1) {
					// 编辑区域数据
					this.editAreaNode(this.selectedNode);
					console.log(this.selectedNode, "selectedNode1");
				} else if (node.level === 2) {
					// 编辑库区数据
					this.editReservoirNode(this.selectedNode);
					console.log(this.selectedNode, "selectedNode2");
				} else if (node.level === 3) {
					// 编辑货架数据
					this.editShelfNode(this.selectedNode);
					console.log(this.selectedNode, "selectedNode3");
				}
			},
			editAreaNode(data) {
				this.form = data;
				console.log(this.form, "form1");
				this.editDialogVisible = true;
				this.areaStatus = true;
				this.openEdit = true;
			},
			editReservoirNode(data) {
				this.form = data;
				console.log(this.form, "form2");
				this.editDialogVisible = true;
				this.reservoirStatus = true;
				this.openEdit = true;
				this.depotAreaIds = data.areaId;
			},
			editShelfNode(data) {
				this.form = data;
				console.log(this.form, "form3");
				this.editDialogVisible = true;
				this.shelfStatus = true;
				this.openEdit = true;
				this.depotReservoirIds = data.depotShelfId;
			},
			deleteNode(node) {
				if (!this.selectedNode) {
					ElMessage.warning("请先选择一个节点");
					return;
				}
				// 根据节点的级别进行相应的处理
				if (node.level === 1) {
					let self = this;
					ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						})
						.then(() => {
							SettingApi.depotAreaDel({
										depotAreaId: this.selectedNode.depotAreaId,
									},
									true
								)
								.then((data) => {
									ElMessage({
										message: data.msg,
										type: "success",
									});
									self.getData();
								})
								.catch((error) => {});
						})
						.catch(() => {});
					console.log(this.selectedNode, "nodeId1");
				} else if (node.level === 2) {
					let self = this;
					ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						})
						.then(() => {
							SettingApi.depotReservoirDel({
										depotReservoirId: this.selectedNode.depotReservoirId,
									},
									true
								)
								.then((data) => {
									ElMessage({
										message: data.msg,
										type: "success",
									});
									self.getData();
								})
								.catch((error) => {});
						})
						.catch(() => {});
					console.log(this.selectedNode, "nodeId2");
				} else if (node.level === 3) {
					let self = this;
					ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						})
						.then(() => {
							SettingApi.depotShelfDel({
										depotShelfId: this.selectedNode.depotShelfId,
									},
									true
								)
								.then((data) => {
									ElMessage({
										message: data.msg,
										type: "success",
									});
									self.getData();
								})
								.catch((error) => {});
						})
						.catch(() => {});
					console.log(this.selectedNode, "nodeId3");
				}
			},
			getTreeData(depotId) {
				let self = this;
				let Params = {
					depotId: depotId,
				};
				SettingApi.depotAreaList(Params)
					.then((res) => {
						self.treeData = res.data;
						console.log(self.treeData, "treeData");
					})
					.catch((error) => {});
			},
			loadNode(node, resolve) {
				if (node.level === 0) {
					console.log("一级", node);
					console.log("一级数据", node.level);
					// 加载第一级节点的数据
					// this.getDepotReservoirList(node).then((data) => {
					//   resolve(data);
					// });
				} else if (node.level === 1) {
					console.log("二级", node.data.depotAreaId);
					// 加载第二级节点的数据
					this.getDepotReservoirList(node.data.depotAreaId).then((data) => {
						resolve(data);
					});
				} else if (node.level === 2) {
					console.log("三", node.data.id);
					// 加载第二级节点的数据
					this.getDepotShelfList(node.data.id).then((data) => {
						resolve(data);
					});
				} else {
					// 其他情况不加载数据
					resolve([]);
				}
			},
			getDepotReservoirList(areaId) {
				console.log("areaId", areaId);
				let Params = {
					areaId: areaId,
				};
				return SettingApi.depotReservoirList(Params).then((res) => {
					return res.data.map((item) => ({
						id: item.depotReservoirId,
						depotReservoirId: item.depotReservoirId,
						depotId: item.depotId,
						areaId: item.areaId,
						reservoirName: item.reservoirName,
						reservoirCode: item.reservoirCode,
						leaf: false,
					}));
				});
			},
			getDepotShelfList(reservoirId) {
				console.log("reservoirId", reservoirId);
				let Params = {
					reservoirId: reservoirId,
				};
				return SettingApi.depotShelfList(Params).then((res) => {
					return res.data.map((item) => ({
						id: item.depotShelfId,
						depotShelfId: item.depotShelfId,
						reservoirId: item.reservoirId,
						depotId: item.depotId,
						shelfName: item.shelfName,
						shelfCode: item.shelfCode,
						reservoirName: item.reservoirName,
						leaf: true,
					}));
				});
			},
			getData(param = "") {
				let self = this;
				let Params = {};
				Params.name = self.name;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				SettingApi.depotList(Params, true)
					.then((res) => {
						self.selectOptions = res.data.records;
						if (self.selectOptions.length > 0) {
							self.selectedOption = self.selectOptions[0].depotId;
							self.depotIds = self.selectedOption;
							self.getTreeData(self.selectedOption);
						}
						self.getStorageList();
					})
					.catch((error) => {});
			},
			// checkPermission,
			dialogFormVisible(e) {
				this.editDialogVisible = false;
				this.form = {
					areaCode: "",
					depotId: "",
					areaName: "",
					reservoirCode: "",
					reservoirName: "",
					areaId: "",
					shelfCode: "", // 确保包含 shelfCode 属性并赋予初始值
					shelfName: "",
					reservoirId: "",
				};
				this.areaStatus = false;
				this.reservoirStatus = false;
				this.shelfStatus = false;
				this.selectedNode = "";
				this.openAdd = false;
				this.openEdit = false;
				this.depotIds = "";
				(this.depotReservoirIds = ""), (this.depotAreaIds = "");
			},
			afterInit(res) {},
			handleFormTreeClick(data) {
				this.formInfo = data;
				this.paramBean.formCode = data.sysGenCode;
				this.paramBean.formType = data.formType;
				this.init();
			},
			filterFormNode(value, data) {
				if (!value) return true;
				return data.formName.indexOf(value) !== -1;
			},
		},
	};
</script>

<style lang="scss" scoped>
	.search-box {
		margin-bottom: 16px;
	}

	::v-deep(.el-tree-node__content:hover > .custom-tree-node .hover-icons) {
		display: inline;
	}

	.icon.icon-yemian,
	.icon.icon-wenjian {
		font-size: 12px;
		// color: var(--el-color-primary);
		color: #e9d047;
		margin-right: 3px;
	}

	.hover-icons {
		display: none;
		position: absolute;
		right: 0;
		background: var(--el-tree-node-hover-bg-color);

		.el-icon {
			font-size: 14px;
			color: #999;
			margin-left: 3px;
		}

		.el-icon:hover {
			color: var(--el-color-primary);
		}
	}

	// .icon.icon-yemian,
	// .icon.icon-wenjian {
	//   font-size: 12px;
	//   color: var(--el-color-primary);
	//   margin-right: 3px;
	// }

	.add-btn {
		flex-shrink: 0;
		width: 32px;
		height: 32px;
		font-weight: bold;
	}

	.left-menu {
		width: 257px;
		height: calc(100vh - 84px - 24px);
		flex-shrink: 0;
		border-right: 5px solid #f6f8fb;
		background-color: #fff;
		padding: 14px 9px 14px 13px;
		box-sizing: border-box;
	}

	.table-wrap {
		height: 100px;
	}

	.external {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		height: calc(100vh - 84px - 24px);
		overflow: hidden;
	}

	.all {
		flex: 1;
		width: 100%;
		height: calc(100vh - 84px - 24px);
		box-sizing: border-box;
		padding: 15px;
		display: flex;
		background-color: white;
		overflow: auto;

		.app-container {
			width: 250px;
		}

		.right-container {
			flex: 1;
			width: calc(100% - 250px);
			height: calc(100vh - 84px - 24px - 30px);
			display: flex;
			flex-direction: column;
		}
	}

	::v-deep .node-icon {
		position: absolute;
		top: 50%;
		right: 0;
		transform: translateY(-50%);
		opacity: 0;
		/* 初始状态下隐藏图标 */
		transition: opacity 0.3s ease;
		/* 添加过渡效果 */
	}

	::v-deep .node-label {
		margin-right: 50px;
	}

	::v-deep.node-content:hover .node-icon {
		opacity: 1;
		/* 鼠标悬停时显示图标 */
	}

	::v-deep .node-content {
		position: relative;
	}

	.node-content:hover .delete-icon,
	.node-content:hover .edit-icon {
		opacity: 1;
		/* 鼠标悬停时显示按钮 */
	}

	.node-Folder {
		margin-right: 10px;
	}

	.delete-icon,
	.edit-icon {
		cursor: pointer;
		margin-right: 5px;
		opacity: 0;
		/* 初始状态下隐藏按钮 */
		transition: opacity 0.3s ease;
		/* 添加过渡效果 */
	}

	::v-deep .pagination {
		text-align: right;
		margin-top: 18px;
	}
</style>