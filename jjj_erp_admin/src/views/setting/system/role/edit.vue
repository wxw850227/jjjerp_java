<template>
  <!--描述：权限-角色管理-编辑角色-->
  <el-dialog
    title="添加角色"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :fullscreen="fullscreen"
    :show-close="false"
    align-center
    append-to="#right-content-box"
  >
    <template #header="{ close, titleId, titleClass }">
      <div class="my-header d-b-c">
        <div :id="titleId" :class="titleClass" class="flex-1">添加角色</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <!--form表单-->
    <el-form
      size="small"
      ref="form"
      :model="form"
      :rules="formRules"
      label-width="140px"
    >
      <el-form-item
        label="角色名称："
        prop="roleName"
        :rules="[{ required: true, message: ' ' }]"
      >
        <el-input
          v-model="form.roleName"
          placeholder="请输入角色名称"
          class="max-w460"
        ></el-input>
      </el-form-item>
      <el-form-item label="权限列表：">
        <el-tree
          v-model="form.accessId"
          :data="data"
          show-checkbox
          node-key="accessId"
          :default-expand-all="false"
          :default-checked-keys="selectMenu"
          :props="defaultProps"
          @check="handleCheckChange"
        ></el-tree>
      </el-form-item>

      <el-form-item label="排序："
        ><el-input
          type="number"
          v-model="form.sort"
          placeholder="请输入排序"
          class="max-w460"
        ></el-input
      ></el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import AuthApi from "@/api/auth.js";

export default {
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      dialogVisible: false,
      fullscreen: false,
      /*表单数据对象*/
      form: {
        accessId: [],
      },
      data: [],
      /*角色列表*/
      roleList: [],
      /*权限选中*/
      selectMenu: [],
      /*权限树菜单重新自定义字段*/
      defaultProps: {
        children: "children",
        label: "name",
      },
      roleId: 0,
    };
  },
  props: ["open", "model"],
  watch: {
    open: function (n, o) {
      if (n != o) {
        this.roleId = this.model.roleId;
        this.getData();
        this.dialogVisible = this.open;
      }
    },
  },
  created() {
    /*获取列表*/
    // this.getData();
  },
  methods: {
    /*修改角色*/
    onSubmit() {
      let self = this;
      let form = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          AuthApi.roleEdit(
            {
              roleId: self.form.roleId,
              roleName: self.form.roleName,
              sort: self.form.sort,
              accessId: self.form.accessId,
            },
            true
          )
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
    /*获取所有的数据*/
    getData() {
      let self = this;
      AuthApi.roleEditInfo({
        roleId: self.roleId,
      })
        .then((data) => {
          let obj = self.clearData(data.data.menu, data.data.selectMenu);
          self.selectMenu = data.data.selectMenu;
          self.form = data.data.model;
          self.roleList = data.data.roleList;
          self.data = data.data.menu;
          self.form.accessId = self.selectMenu;
          if (self.form.parentId == 0) {
            self.form.parentId = 0 + "";
          }
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*清除数据*/
    clearData(list, authlist) {
      let total = 0;
      let leng = list.length;
      for (let i = 0; i < leng; i++) {
        let item = list[i];
        if (item.children != null) {
          let flag = this.clearData(item.children, authlist);
          if (!flag) {
            let _index = authlist.indexOf(item.accessId);
            if (_index >= 0) {
              authlist.splice(_index, 1);
            }
          }
        }
        if (authlist.indexOf(item.accessId) != -1) {
          total++;
        }
      }
      if (total < leng) {
        return false;
      } else {
        return true;
      }
    },

    /*监听选中*/
    handleCheckChange(data, checked) {
      this.form.accessId = checked.checkedKeys.concat(checked.halfCheckedKeys);
    },

    /*取消*/
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
  },
};
</script>

<style lang="scss" scoped>
.basic-setting-content {
}

.product-add {
  padding-bottom: 50px;
}

.img {
  margin-top: 10px;
}
</style>
