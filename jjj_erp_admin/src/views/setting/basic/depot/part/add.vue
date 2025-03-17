<template>
  <el-dialog
    title="添加仓库"
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
        <div :id="titleId" :class="titleClass" class="flex-1">添加仓库</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div>
      <el-form size="small" :model="form" :rules="formRules" ref="form">
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="仓库类型"
              prop="type"
              :label-width="formLabelWidth"
            >
              <el-select
                v-model="form.type"
                style="width: 208px"
                placeholder="选择平台"
              >
                <el-option label="正品仓" :value="0"></el-option>
                <el-option label="次品仓" :value="1"></el-option>
                <el-option label="门店仓" :value="2"></el-option>
              </el-select>
              <div style="color: #999; font-size: 12px">
                注：次品仓不参与自动匹配
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="仓库名称"
              prop="name"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="仓库编码"
              prop="code"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.code" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
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
            <el-form-item label="邮编" prop="zip" :label-width="formLabelWidth">
              <el-input v-model="form.zip" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item
              label="省市区"
              prop="regions"
              :label-width="formLabelWidth"
            >
              <el-cascader
                style="width: 208px"
                clearable
                v-model="form.regions"
                :options="areaData"
                :props="regionParams"
                @change="handleChange1"
              ></el-cascader>
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
        </el-row>
        <el-row>
          <el-col :span="16">
            <el-form-item
              label="详细地址"
              prop="address"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.address" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-col :span="16">
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
import SettingApi from "@/api/setting.js";
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
        type: [
          {
            required: true,
            message: "请选择仓库类型",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "请输入仓库名称",
            trigger: "blur",
          },
        ],
        contacts: [
          {
            required: true,
            message: "请输入联系人",
            trigger: "blur",
          },
        ],
        regions: [
          {
            required: true,
            message: "请选择地址",
            trigger: "blur",
          },
        ],
        phone: [
          {
            required: true,
            message: "请输入手机",
            trigger: "blur",
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
      // SettingApi.storeToAdd().then((res) => {
      //   this.storeType = res.data.storeType;
      // });
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
          SettingApi.depotAdd(params)
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
