<template>
  <el-dialog
    title="添加账户"
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
        <div :id="titleId" :class="titleClass" class="flex-1">添加账户</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div>
      <el-form size="small" :model="form" :rules="formRules" ref="form">
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="账户名称"
              prop="accountName"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.accountName"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="类型"
              prop="accountType"
              :label-width="formLabelWidth"
            >
              <el-select v-model="form.accountType" style="width: 100%">
                <el-option label="现金" :value="1"></el-option>
                <el-option label="银行" :value="2"></el-option>
                <el-option label="第三方" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="期初余额"
              prop="openingMoney"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.openingMoney"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="机构名称"
              prop="organName"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.organName" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="账号"
              prop="accountNumber"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.accountNumber"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-col :span="24">
          <el-form-item
            label="备注"
            prop="remark"
            :label-width="formLabelWidth"
          >
            <el-input
              type="textarea"
              v-model="form.remark"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="addUser">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import FinanceApi from "@/api/finance.js";
export default {
  components: {},
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
      config: {},
      fullscreen: false,
      unitList: [],
      categoryList: [],
      mfrsList: [],
      specOptions2: [], // 与 skuTwo 相关的选择值数组
      specOptions3: [], // 与 skuThree 相关的选择值数组
      options: [],
      options1: [],
      options2: [],
      options3: [],
      formRules: {
        accountName: [
          {
            required: true,
            message: "请输入账户名称",
            trigger: "blur",
          },
        ],
        accountType: [
          {
            required: true,
            message: "请选择类型",
            trigger: "blur",
          },
        ],
        openingMoney: [
          {
            required: true,
            message: "请输入期初余额",
            trigger: "blur",
          },
        ],
        accountNumber: [
          {
            required: true,
            message: "请输入账号",
            trigger: "blur",
          },
        ],
        organName: [
          {
            required: true,
            message: "请输入机构名称",
            trigger: "blur",
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
      storeType: [],
    };
  },
  props: ["open_add", "addform"],
  created() {
    this.dialogVisible = this.open_add;
  },
  methods: {
    /*添加用户*/
    addUser() {
      let self = this;
      let params = self.form;
      console.log(params, "params");
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          FinanceApi.accountAdd(params)
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
