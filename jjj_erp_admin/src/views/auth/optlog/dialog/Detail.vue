<template>
  <el-dialog
    title="详情"
    :fullscreen="fullscreen"
    :show-close="false"
    align-center
    append-to="#right-content-box"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-press-escape="false"
  >
    <template #header="{ close, titleId, titleClass }">
      <div class="my-header d-b-c">
        <div :id="titleId" :class="titleClass" class="flex-1">{{ title }}</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <el-form size="small" :model="form">
      <el-form-item label="标题：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.title }}</div>
      </el-form-item>
      <el-form-item label="id：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.optLogId }}</div>
      </el-form-item>
      <el-form-item label="用户名：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.userName }}</div>
      </el-form-item>
      <el-form-item label="真实姓名：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.realName }}</div>
      </el-form-item>
      <el-form-item label="url：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.url }}</div>
      </el-form-item>
      <el-form-item label="内容：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.content }}</div>
      </el-form-item>
      <el-form-item label="ip：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.ip }}</div>
      </el-form-item>
      <el-form-item label="agent：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.agent }}</div>
      </el-form-item>
      <el-form-item label="添加时间：" :label-width="formLabelWidth">
        <div class="item-content-box">{{ form.createTime }}</div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      /*左边长度*/
      formLabelWidth: "140px",
      /*是否显示*/
      dialogVisible: false,
      fullscreen: false,
    };
  },
  props: ["open", "form"],
  created() {},
  watch: {
    open: function (n, o) {
      if (n != o && n) {
        this.dialogVisible = this.open;
      }
    },
  },
  methods: {
    /*关闭弹窗*/
    dialogFormVisible(e) {
      this.$emit("close", {});
    },
  },
};
</script>

<style scoped="scoped">
.item-content-box {
  padding-top: 4px;
  line-height: 20px;
  min-height: 20px;
  color: #333333;
  border-bottom: 1px solid #eeeeee;
}
</style>
