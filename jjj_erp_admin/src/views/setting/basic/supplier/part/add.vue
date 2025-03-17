<template>
  <el-dialog
    title="添加供应商"
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
        <div :id="titleId" :class="titleClass" class="flex-1">添加供应商</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div>
      <el-form size="small" :model="form" :rules="formRules" ref="form">
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="供应商编码"
              prop="supplierCode"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.supplierCode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="供应商名称"
              prop="supplierName"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.supplierName"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="网站网址"
              prop="website"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.website" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="供应商类型"
              prop="supplierType"
              :label-width="formLabelWidth"
            >
              <el-select
                v-model="form.supplierType"
                placeholder="请选择供应商类型"
                style="width: 100%"
              >
                <el-option label="普通供应商" :value="0"></el-option>
                <el-option label="直供供应商" :value="1"></el-option>
                <el-option label="1688供应商" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="期初应付款"
              prop="initial"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.initial" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="E-mail"
              prop="email"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.email" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="省市区"
              prop="address"
              :label-width="formLabelWidth"
            >
              <el-cascader
                clearable
                v-model="form.address"
                :options="areaData"
                :props="regionParams"
                @change="handleChange1"
                style="width: 100%"
              ></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item
              label="详细地址"
              prop="detail"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.detail" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="联系人"
              prop="contacts"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.contacts" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="固话"
              prop="fixPhone"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.fixPhone" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="手机"
              prop="phone"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.phone" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="税号"
              prop="taxRate"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.taxRate" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="开户行及账户"
              prop="bankName"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.bankName" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="欠款金额"
              prop="debtCredit"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.debtCredit" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item
              label="备注"
              prop="description"
              :label-width="formLabelWidth"
            >
              <el-input
                type="textarea"
                v-model="form.description"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="详情图片：" :label-width="formLabelWidth">
              <div class="draggable-list">
                <draggable class="wrapper" v-model="form.imageList">
                  <template #item="{ element, index }">
                    <div class="item">
                      <img v-img-url="element.filePath" />
                      <a
                        href="javascript:void(0);"
                        class="delete-btn"
                        @click.stop="deleteImg(index)"
                      >
                        <el-icon>
                          <CloseBold />
                        </el-icon>
                      </a>
                    </div>
                  </template>
                </draggable>
                <div class="item img-select" @click="openUpload">
                  <el-icon>
                    <Plus />
                  </el-icon>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <Upload
        v-if="isProductUpload"
        :isProductUpload="isProductUpload"
        :config="config"
        @returnImgs="returnImgsFunc"
        >上传图片</Upload
      >
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
import SettingApi from "@/api/setting.js";
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
        imageList: [],
        barCode: "",
        skuOne: "",
        skuTwo: "",
        skuThree: "",
        categoryId: "",
      },
      value1: "",
      fullscreen: false,
      config: {},
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
        supplierType: [
          {
            required: true,
            message: "请输入供应商类型",
            trigger: "blur",
          },
        ],
        address: [
          {
            required: true,
            message: "请选择地址",
            trigger: "blur",
          },
        ],
        supplierName: [
          {
            required: true,
            message: "请输入供应商名称",
            trigger: "blur",
          },
        ],
        debtCredit: [
          {
            required: true,
            message: "请输入欠款金额",
            trigger: "blur",
          },
        ],
        initial: [
          {
            required: true,
            message: "请输入期初应付款",
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
    };
  },
  props: ["open_add", "addform"],
  created() {
    this.dialogVisible = this.open_add;
    this.getdata();
  },
  methods: {
    getdata() {
      SettingApi.regionList().then((res) => {
        this.areaData = res.data;
      });
    },
    handleChange1(val) {
      console.log(val, "val");
      const province = this.areaData.find((item) => item.id === val[0]);
      const city = province.children.find((item) => item.id === val[1]);
      const region = city.children.find((item) => item.id === val[2]);
      this.form.provinceId = province.id;
      this.form.cityId = city.id;
      this.form.regionId = region.id || "";
      this.form.province = province.name;
      this.form.city = city.name;
      this.form.region = region.name;
    },
    /*添加用户*/
    addUser() {
      let self = this;
      let params = self.form;
      console.log(params, "params");
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.supplierAdd(params, true)
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
    /*上传*/
    openUpload() {
      this.isProductUpload = true;
    },
    deleteImg(index) {
      this.form.imageList.splice(index, 1);
    },
    /*获取图片*/
    returnImgsFunc(e) {
      console.log(this.form);
      if (e != null) {
        let imgs = this.form.imageList.concat(e);
        this.form.imageList = imgs;
        // 兼容后端
        for (let i = 0; i < imgs.length; i++) {
          if (typeof imageId == "undefined") {
            imgs[i].imageId = imgs[i].fileId;
          }
        }
      }
      this.isProductUpload = false;
      console.log(this.form.imageList, "this.form.imageList ");
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
