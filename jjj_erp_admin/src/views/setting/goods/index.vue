<template>
  <div class="app-container">
    <el-row :gutter="6">
      <el-col :span="6">
        <span>仓库(必选)：</span>
        <el-select
          v-model="selectedOption"
          placeholder="请选择"
          @change="handleDepotChange"
        >
          <el-option
            v-for="(item, index) in selectOptions"
            :key="index"
            :label="item.name"
            :value="item.depotId"
          ></el-option>
        </el-select>
        <el-button circle @click="addNode" :disabled="selectedNode.leaf">
          <el-icon :size="20"><CirclePlus /></el-icon>
        </el-button>
        <el-tree
          :data="treeData"
          :props="treeProps"
          node-key="id"
          :lazy="true"
          :load="loadNode"
          default-expand-all
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <span class="node-content">
              <sapn class="node-Folder"
                ><el-icon><Folder /></el-icon
              ></sapn>
              <span class="node-label" v-if="node.level === 1"
                >【区域】{{ data.areaName }}</span
              >
              <span class="node-label" v-if="node.level === 2"
                >【库区】{{ data.reservoirName }}</span
              >
              <span class="node-label" v-if="node.level === 3"
                >【货架】{{ data.shelfName }}</span
              >
              <span class="node-icon">
                <el-icon @click="editNode(node)" class="edit-icon"
                  ><EditPen
                /></el-icon>
                <el-icon @click="deleteNode(node)" class="delete-icon">
                  <Delete
                /></el-icon>
              </span>
            </span>
          </template>
        </el-tree>
        <el-dialog
          :title="dialogTitle"
          @close="dialogFormVisible"
          v-model="editDialogVisible"
          :fullscreen="fullscreen"
          :show-close="false"
          align-center
          append-to="#right-content-box"
        >
          <template #header="{ close, titleId, titleClass }">
            <div class="my-header d-b-c">
              <div :id="titleId" :class="titleClass" class="flex-1">
                {{ dialogTitle }}
              </div>
              <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
              <el-icon @click="close"><Close /></el-icon>
            </div>
          </template>
          <el-form size="small" :model="form" :rules="formRules" ref="form">
            <el-form-item
              label="编号"
              prop="areaCode"
              v-if="(!depotAreaIds && !depotReservoirIds) || areaStatus"
            >
              <el-input v-model="form.areaCode" class="max-w460"></el-input>
            </el-form-item>
            <el-form-item
              label="编号"
              prop="reservoirCode"
              v-if="depotAreaIds || reservoirStatus"
            >
              <el-input
                v-model="form.reservoirCode"
                class="max-w460"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="编号"
              prop="shelfCode"
              v-if="depotReservoirIds || shelfStatus"
            >
              <el-input v-model="form.shelfCode" class="max-w460"></el-input>
            </el-form-item>
            <el-form-item
              label="名称"
              prop="areaName"
              v-if="(!depotAreaIds && !depotReservoirIds) || areaStatus"
            >
              <el-input v-model="form.areaName" class="max-w460"></el-input>
            </el-form-item>
            <el-form-item
              label="名称"
              prop="reservoirName"
              v-if="depotAreaIds || reservoirStatus"
            >
              <el-input
                v-model="form.reservoirName"
                class="max-w460"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="名称"
              prop="shelfName"
              v-if="depotReservoirIds || shelfStatus"
            >
              <el-input v-model="form.shelfName" class="max-w460"></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogFormVisible">取 消</el-button>
              <el-button type="primary" @click="addUser">确 定</el-button>
            </div>
          </template>
        </el-dialog>
      </el-col>
      <el-col :span="18">
        <div class="common-seach-wrap">
          <el-form
            size="small"
            :inline="true"
            :model="searchForm"
            class="demo-form-inline"
          >
            <el-form-item label="货位编码">
              <el-input
                size="small"
                placeholder="请输入货位编码查询"
              ></el-input>
            </el-form-item>
            <el-form-item label="货位类型">
              <el-select size="small" placeholder="">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="(item, index) in commentList"
                  :key="index"
                  :label="item.name"
                  :value="item.val"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="选择商品">
              <el-select size="small" placeholder="">
                <el-option label="全部" :value="0"></el-option>
                <el-option
                  v-for="(item, index) in commentList"
                  :key="index"
                  :label="item.name"
                  :value="item.val"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                size="small"
                type="primary"
                icon="Search"
                @click="onSubmit"
                >查询</el-button
              >
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
                >重置</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div class="common-level-rail">
          <el-button
            size="small"
            type="primary"
            icon="Plus"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >新增</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >删除</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >批量新增</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >货位转移</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >导入货位库存</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >导出</el-button
          >
          <el-button
            size="small"
            type="primary"
            @click="addClick"
            v-auth="'/setting/supplier/add'"
            >导出货位库存</el-button
          >
        </div>
        <el-table :data="staticTableData" row-key="id">
          <el-table-column prop="id" label="ID" width="55"></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button  type="primary"
					  link size="small" @click="editClick(scope.row)"
                >修改</el-button
              >
              <el-button @click="delClick(scope.row)"  type="primary"
					  link size="small">
                删除
              </el-button>
              <el-button @click="setClick(scope.row)"  type="primary"
					  link size="small">
                设置货位库存
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="库区" prop="applyName"></el-table-column>
          <el-table-column
            label="货位编码(同个仓库不能重复)"
            prop="selfStateName"
          ></el-table-column>
          <el-table-column
            label="货位类型"
            prop="selfStateName"
          ></el-table-column>
          <el-table-column label="货位类型" prop="createDate">
            <template #default="scope">
              <span>{{ parseTime(scope.row.createDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="更新日期" prop="lastUpdateDate">
            <template #default="scope">
              <span>{{ parseTime(scope.row.lastUpdateDate) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <Add
      v-if="open_add"
      :open_add="open_add"
      @closeDialog="closeDialogFunc($event, 'add')"
    ></Add>
    <Edit
      v-if="open_edit"
      :open_edit="open_edit"
      :editForm="categoryModel"
      @closeDialog="closeDialogFunc($event, 'edit')"
    ></Edit>
    <Set
      v-if="open_set"
      :open_set="open_set"
      @closeDialog="closeDialogFunc($event, 'set')"
    ></Set>
  </div>
</template>

<script>
import { parseTime } from "@/utils/index";
import SettingApi from "@/api/setting.js";
import { ElLoading } from "element-plus";
import Add from "./add.vue";
import Edit from "./edit.vue";
import Set from "./setShelf.vue";
export default {
  components: {
    Add,
    Edit,
    Set,
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
      shelfList: [],
      open_add: false,
      open_edit: false,
      fullscreen: false,
      open_set: false,
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
      staticTableData: [
        {
          id: 1,
          applyName: "申请1",
          selfStateName: "业务类型1",
          createDate: "2024-04-25",
          lastUpdateDate: "2024-04-26",
        },
        {
          id: 2,
          applyName: "申请2",
          selfStateName: "业务类型2",
          createDate: "2024-04-24",
          lastUpdateDate: "2024-04-25",
        },
        {
          id: 3,
          applyName: "申请3",
          selfStateName: "业务类型3",
          createDate: "2024-04-23",
          lastUpdateDate: "2024-04-24",
        },
      ],
      formInfo: {
        sysGenCode: "",
        formType: "",
      },
      formName: "",
      delLoading: false,
      sup_this: this,
      selectedNode: "",
      areaStatus: false,
      reservoirStatus: false,
      shelfStatus: false,
      openAdd: false,
      openEdit: false,
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
  },
  methods: {
    parseTime,
    setClick() {
      this.open_set = true;
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
    },
    handleNodeClick(node) {
      this.selectedNode = node;
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
            SettingApi.depotAreaDel(
              {
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
            SettingApi.depotReservoirDel(
              {
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
            SettingApi.depotShelfDel(
              {
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

<style scoped>
::v-deep .node-icon {
  position: absolute;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  opacity: 0; /* 初始状态下隐藏图标 */
  transition: opacity 0.3s ease; /* 添加过渡效果 */
}
::v-deep .node-label {
  margin-right: 50px;
}
::v-deep.node-content:hover .node-icon {
  opacity: 1; /* 鼠标悬停时显示图标 */
}
::v-deep .node-content {
  position: relative;
}
.node-content:hover .delete-icon,
.node-content:hover .edit-icon {
  opacity: 1; /* 鼠标悬停时显示按钮 */
}
.node-Folder {
  margin-right: 10px;
}
.delete-icon,
.edit-icon {
  cursor: pointer;
  margin-right: 5px;
  opacity: 0; /* 初始状态下隐藏按钮 */
  transition: opacity 0.3s ease; /* 添加过渡效果 */
}
</style>
