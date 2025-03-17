<template>
  <el-dialog
    title="修改供应商"
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
        <div :id="titleId" :class="titleClass" class="flex-1">修改供应商</div>
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
                :disabled="isDetailMode"
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
                :disabled="isDetailMode"
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
              <el-input
                v-model="form.website"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
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
                :disabled="isDetailMode"
                placeholder="请选择供应商类型"
                style="width: 240px"
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
              <el-input
                v-model="form.initial"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="E-mail"
              prop="email"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.email"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="省市区"
              prop="address"
              :label-width="formLabelWidth"
            >
              <el-cascader
                :disabled="isDetailMode"
                clearable
                v-model="form.regionLists"
                :options="areaData"
                :props="regionParams"
                @change="handleChange1"
              ></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item
              label="详细地址"
              prop="address"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.address"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="联系人"
              prop="contacts"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.contacts"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="固话"
              prop="fixPhone"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.fixPhone"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="手机"
              prop="phone"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.phone"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="税号"
              prop="taxRate"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.taxRate"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="开户行及账户"
              prop="bankName"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.bankName"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="欠款额度"
              prop="debtCredit"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.debtCredit"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
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
                :disabled="isDetailMode"
                v-model="form.description"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="详情图片：" :label-width="formLabelWidth">
              {{ form.imageList }}
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
                        <el-icon><CloseBold /></el-icon>
                      </a>
                    </div>
                  </template>
                </draggable>
                <div class="item img-select" @click="openUpload">
                  <el-icon><Plus /></el-icon>
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

import { ElLoading } from "element-plus";
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
        regionLists: [],
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
        name: [
          {
            required: true,
            message: "请输入分类名称",
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
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      loading: false,
      /*是否上传图片*/
      isProductUpload: false,
      loadingStatus: false,
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
      loadings: null,
    };
  },
  props: ["open_edit", "editForm", "isDetailMode"],
  created() {
    this.loadings = ElLoading.service({
      lock: true,
      text: "Loading",
      background: "rgba(0, 0, 0, 0.7)",
    });
    setTimeout(() => {
      this.loadings.close();
      if (!this.loadingStatus) {
        this.dialogVisible = this.open_edit;
      }
    }, 10000);
    this.form = this.editForm;
    // this.form.imageList = JSON.parse(this.form.imageUrl);
    this.form.regionLists = [
      this.form.provinceId,
      this.form.cityId,
      this.form.regionId,
    ].join(",");
    this.form.regionLists = this.form.regionLists
      .split(",")
      .map((id) => parseInt(id));

    this.getdata();
    console.log("forms", this.form);
    // console.log("imageList", this.form.imageList);
  },
  methods: {
    getdata() {
      SettingApi.regionList().then((res) => {
        this.areaData = res.data;
        this.loadingStatus = true; // 设置加载状态为true，表示数据加载完成
        this.loadings.close(); // 关闭加载状态
        this.dialogVisible = this.open_edit; // 打开对话框
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
    addUser() {
      let self = this;
      let params = self.form;
      console.log(params, "params");
      this.form.imageUrl = "";
      self.$refs.form.validate((valid) => {
        if (valid) {
          self.loading = true;
          SettingApi.supplierEdit(params)
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
      console.log(e, "e");
      if (e != null) {
        let imgs = this.form.imageList.concat(e);
        // 兼容后端
        for (let i = 0; i < imgs.length; i++) {
          if (typeof imgs[i].imageId == "undefined") {
            imgs[i].imageId = imgs[i].fileId;
          }
        }
        this.form["imageList"] = imgs;
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
