<template>
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
      label-width="180px"
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

      <el-form-item label="权限列表：" v-model="form.accessId">
        <el-tree
          :data="data"
          show-checkbox
          node-key="accessId"
          :default-expand-all="false"
          :default-checked-keys="[]"
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
        sort: 1,
      },
      data: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  props: ["open"],
  watch: {
    open: function (n, o) {
      if (n != o) {
        this.dialogVisible = this.open;
      }
    },
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*添加角色*/
    onSubmit() {
      let self = this;
      let form = self.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          AuthApi.roleAdd(form, true)
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
    /*获取数据*/
    getData() {
      let self = this;
      AuthApi.roleAddInfo()
        .then((res) => {
          self.data = res.data;
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    //监听选中
    handleCheckChange(data, checked) {
      this.form.accessId = checked.checkedKeys.concat(checked.halfCheckedKeys);
    },

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
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
