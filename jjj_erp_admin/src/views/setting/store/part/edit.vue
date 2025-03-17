<template>
  <el-dialog
    title="修改店铺"
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
        <div :id="titleId" :class="titleClass" class="flex-1">修改店铺</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div>
      <el-form size="small" :model="form" :rules="formRules" ref="form">
        <el-row>
          <el-col :span="10">
            <el-form-item
              label="其他平台"
              prop="name"
              :label-width="formLabelWidth"
            >
              <el-select
                :disabled="isDetailMode"
                v-model="form.storeType"
                style="width: 208px"
                placeholder="选择平台"
              >
                <el-option
                  v-for="(item, index) in storeType"
                  :key="index"
                  :label="item.name"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="经营类型"
              prop="name"
              :label-width="formLabelWidth"
            >
              <el-select
                :disabled="isDetailMode"
                v-model="form.supplierType"
                placeholder="请选择经营类型"
                style="width: 208px"
              >
                <el-option label="直营店" :value="0"></el-option>
                <el-option label="联营店" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item
              label="店铺昵称"
              prop="nickName"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.nickName"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="省市区"
              prop="regions"
              :label-width="formLabelWidth"
            >
              <el-cascader
                :disabled="isDetailMode"
                style="width: 208px"
                clearable
                v-model="form.regionLists"
                :options="areaData"
                :props="regionParams"
                @change="handleChange1"
              ></el-cascader>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item
              label="店铺名称"
              prop="store_name"
              :label-width="formLabelWidth"
            >
              <el-input
                v-model="form.storeName"
                :disabled="isDetailMode"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
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
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item
              label="店铺网址"
              prop="website"
              :label-width="formLabelWidth"
            >
              <el-input
                :disabled="isDetailMode"
                v-model="form.website"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
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
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item
              label="对应仓库"
              prop="name"
              :label-width="formLabelWidth"
            >
              <el-select
                v-model="form.supplierType"
                :disabled="isDetailMode"
                placeholder="请选择经营类型"
                style="width: 208px"
              >
                <el-option label="直营店" :value="0"></el-option>
                <el-option label="联营店" :value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
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
        </el-row>
        <el-col :span="10">
          <el-form-item label="手机" prop="phone" :label-width="formLabelWidth">
            <el-input
              v-model="form.phone"
              :disabled="isDetailMode"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item
            label="宣传标语"
            prop="description"
            :label-width="formLabelWidth"
          >
            <el-input
              type="textarea"
              v-model="form.description"
              :disabled="isDetailMode"
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
import SettingApi from "@/api/setting.js";
import { ElLoading } from "element-plus";
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
        regionLists: [],
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
      loadings: null,
      loadingStatus: false,
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
    this.form.regionLists = [
      this.form.provinceId,
      this.form.cityId,
      this.form.regionId,
    ].join(",");
    this.form.regionLists = this.form.regionLists
      .split(",")
      .map((id) => parseInt(id));
    this.$nextTick(() => {
      this.getdata();
    });

    console.log(this.form, "this.form");
    console.log("forms", this.form.regionLists);
  },
  methods: {
    getdata() {
      SettingApi.regionList().then((res) => {
        this.areaData = res.data;
        this.loadingStatus = true; // 设置加载状态为true，表示数据加载完成
        this.loadings.close(); // 关闭加载状态
        this.dialogVisible = this.open_edit; // 打开对话框
      });
      SettingApi.storeToAdd().then((res) => {
        this.storeType = res.data.storeType;
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
          SettingApi.storeEdit(params)
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
