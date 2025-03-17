<template>
  <div class="product-add bgset">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <!--添加门店-->

      <div class="common-form">日志记录</div>
      <el-form-item label="是否记录查询日志" prop="customer">
        <el-checkbox v-model="form.isGetLog">是否记录查询日志</el-checkbox>
        <div class="tips">如果记录，日志量会有点大</div>
      </el-form-item>
      <!-- <div class="common-form">物流查询api</div>
      <el-form-item
        label="快递100 Customer"
        :rules="[{ required: true, message: ' ' }]"
        prop="kuaiDi100.customer"
      >
        <el-input
          v-model="form.kuaiDi100.customer"
          placeholder=""
          class="max-w460"
        ></el-input>
        <div class="tips">
          用于查询物流信息,<el-link
            :underline="false"
            href="https://www.kuaidi100.com/openapi/"
            target="_blank"
            type="primary"
            >快递100申请</el-link
          >
        </div>
      </el-form-item>
      <el-form-item label="快递100 secret" prop="kuaiDi100.secret">
        <el-input
          v-model="form.kuaiDi100.secret"
          placeholder=""
          class="max-w460"
        ></el-input>
        <div class="tips">
          用于电子面单下单,<el-link
            :underline="false"
            href="https://www.kuaidi100.com/openapi/"
            target="_blank"
            type="primary"
            >快递100申请</el-link
          >
        </div>
      </el-form-item>
      <el-form-item
        label="快递100 Key"
        :rules="[{ required: true, message: ' ' }]"
        prop="kuaiDi100.key"
      >
        <el-input
          v-model="form.kuaiDi100.key"
          placeholder=""
          class="max-w460"
        ></el-input>
      </el-form-item> -->

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit" :loading="loading"
          >提交</el-button
        >
      </div>
    </el-form>
    <!--上传图片-->
    <Upload
      v-if="isupload"
      :isupload="isupload"
      :config="{ total: 3 }"
      @returnImgs="returnImgsFunc"
    ></Upload>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";
import Upload from "@/components/file/Upload.vue";
import { formatModel } from "@/utils/base.js";
export default {
  components: {
    Upload,
  },
  data() {
    return {
      ueditor: {
        text: "",
        config: {
          initialFrameWidth: 400,
          initialFrameHeight: 500,
        },
        config1: {
          initialFrameWidth: 400,
          initialFrameHeight: 500,
        },
      },
      /*是否正在加载*/
      loading: false,
      /*form表单数据*/
      form: {
        name: "",
        kuaiDi100: {
          customer: "",
          key: "",
        },
        deliveryType: [],
        isGetLog: 0,
        policy: {
          service: "",
          privacy: "",
        },
      },
      all_type: [],
      delivery_type: [],
      /*是否打开图片选择*/
      isupload: false,
      editorid1: "editor-demo-01",
      editorid2: "editor-demo-02",
      activeTab: "service",
    };
  },
  created() {
    this.getParams();
    this.form.policy.privacy = localStorage.getItem("contens");
    this.form.policy.service = localStorage.getItem("contens1");
  },
  methods: {
    contentChangeFuncs(e) {
      this.form.policy.service = e;
      localStorage.setItem("contens1", e);
    },
    contentChangeFunc(e) {
      this.form.policy.privacy = e;
      localStorage.setItem("contens", e);
    },
    /*获取配置数据*/
    getParams() {
      let self = this;
      SettingApi.storeDetail({}, true)
        .then((res) => {
          // self.form = formatModel(self.form, res.data);
          self.form = Object.assign(self.form, res.data);
          self.all_type = res.data.allType;
          self.loading = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    /*提交*/
    onSubmit() {
      let self = this;
      let params = this.form;
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.editStore(params, true)
            .then((data) => {
              self.loading = false;
              ElMessage({
                message: "恭喜你，设置成功",
                type: "success",
              });
              self.getParams();
              location.reload();
            })
            .catch((error) => {
              self.loading = false;
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>
<style scoped>
.tips {
  color: #ccc;
}
.edit_container {
  position: relative !important;
}
</style>
