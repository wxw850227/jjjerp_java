<template>
  <el-dialog
    title="售后记账单据详情"
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
        <div :id="titleId" :class="titleClass" class="flex-1">
          售后记账单据详情
        </div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div>
      <el-form size="small" :model="form" :rules="formRules" ref="form">
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="业务时间"
              prop="businessTime"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.billTime"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="单据编号"
              prop="codeNum"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.codeNum"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="操作员"
              prop="creator_id"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.creatorId"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="备注"
              prop="remark"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.description"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item
              label="单位类型"
              prop="bill_type"
              :label-width="formLabelWidth"
            >
              <el-select
                disabled
                size="small"
                v-model="form.billType"
                placeholder="请选择"
              >
                <el-option
                  v-for="(item, index) in billType"
                  :key="index"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="往来单位"
              prop="tradingUnitName"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.tradingUnitName"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="结算账户"
              prop="account_name"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.accountName"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="金额"
              prop="money"
              :label-width="formLabelWidth"
            >
              <el-input
                disabled
                v-model="form.money"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-col :span="24">
          <el-form-item
            label="返利金额:"
            prop="remark"
            :label-width="formLabelWidth"
          >
            <el-input
              type="textarea"
              v-model="form.remark"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-col> -->
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">关闭</el-button>
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
      form: {},
      value1: "",
      config: {},
      unitList: [],
      categoryList: [],
      fullscreen: false,
      mfrsList: [],
      specOptions2: [], // 与 skuTwo 相关的选择值数组
      specOptions3: [], // 与 skuThree 相关的选择值数组
      options: [],
      options1: [],
      options2: [],
      options3: [],
      formRules: {
        supplierType: [
          {
            required: true,
            message: "请输入供应商类型",
            trigger: "blur",
          },
        ],
        unitId: [
          {
            required: true,
            message: "请输入单位",
            trigger: "blur",
          },
        ],
        sort: [
          {
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
      formLabelWidth: "110px",
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
      billType: [],
      accountList: [],
    };
  },
  props: ["open_edit", "editForm", "otherDatas"],
  created() {
    this.dialogVisible = this.open_edit;
    this.form = this.editForm;
    this.form.codeNum = this.form.description.replace(/[^a-zA-Z0-9]/g, "");
    this.billType = this.otherDatas.billType;
    this.accountList = this.otherDatas.account;
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
          FinanceApi.accountEdit(params)
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
