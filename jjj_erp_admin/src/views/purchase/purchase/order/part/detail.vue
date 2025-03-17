<template>
  <el-dialog
    title="详情"
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
        <div :id="titleId" :class="titleClass" class="flex-1">详情</div>
        <el-icon @click="fullscreen = !fullscreen">
          <Plus />
        </el-icon>
        <el-icon @click="close">
          <Close />
        </el-icon>
      </div>
    </template>
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-tabs v-model="activeName" @tab-change="handleClick">
        <el-tab-pane label="采购明细" name="detail">
          <template #label>
            <span>采购明细 </span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="操作日志" name="log">
          <template #label>
            <span>操作日志 </span>
          </template>
        </el-tab-pane>
      </el-tabs>
    </div>
    <!-- 表格 -->
    <el-table
      class="flex-1"
      size="small"
      :data="filteredBillItems"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
      v-loading="loading"
      show-summary
      :summary-method="getSummary"
      v-if="activeName == 'detail'"
    >
      <!-- <el-table-column prop="#"  width="55" ></el-table-column> -->
      <el-table-column prop="billId" label="ID" width="55"></el-table-column>
      <el-table-column prop="imageUrl" label="图">
        <template #default="{ row }">
          <div style="display: flex; align-items: center">
            <el-popover placement="right-start" trigger="hover" width="276">
              <template #reference>
                <el-avatar
                  v-if="row.productExtend.imageUrl"
                  shape="square"
                  :size="26"
                  :src="row.productExtend.imageUrl"
                />
                <el-avatar
                  v-else
                  :size="26"
                  shape="square"
                  src="/static/imgs/imagerror.png"
                />
              </template>
              <template #default>
                <el-avatar
                  v-if="row.productExtend.imageUrl"
                  :size="250"
                  shape="square"
                  :src="row.productExtend.imageUrl"
                />
                <el-avatar
                  v-else
                  :size="250"
                  shape="square"
                  src="/static/imgs/imagerror.png"
                />
              </template>
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="productExtend.productCode"
        label="商品编码"
      ></el-table-column>
      <el-table-column
        prop="productExtend.name"
        label="商品名称"
      ></el-table-column>
      <el-table-column prop="sku" label="规格名称"></el-table-column>
      <el-table-column prop="materialUnit" label="单位"></el-table-column>
      <el-table-column prop="operNumber" label="数量"></el-table-column>
      <el-table-column prop="basicNumber" label="数量(pcs)"></el-table-column>
      <el-table-column
        prop="productExtend.commodityDecimal"
        label="吊牌价"
      ></el-table-column>
      <el-table-column prop="unitPrice" label="单价"></el-table-column>
      <el-table-column prop="discountRate" label="折扣率"></el-table-column>
      <!-- <el-table-column
        prop="productExtend.allotPrice"
        label="分摊金额"
      ></el-table-column> -->
      <el-table-column prop="allPrice" label="总价"></el-table-column>
      <el-table-column prop="purchaseCycle" label="采购周期"></el-table-column>
      <el-table-column prop="arrivalDate" width="130" label="预计到货日期">
        <template #default="{ row }">
          <el-date-picker
            disabled
            v-model="row.arrivalDate"
            type="date"
            placeholder="请选择"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="productExtend.accountNum"
        label="货号"
      ></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
    </el-table>
    <el-table
      size="small"
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
      v-loading="loading"
      v-if="activeName == 'log'"
    >
      <!-- <el-table-column prop="#"  width="55" ></el-table-column> -->
      <el-table-column prop="id" label="ID" width="55"></el-table-column>
      <el-table-column
        prop="amount2"
        label="操作时间"
        width="130"
      ></el-table-column>
      <el-table-column
        prop="amount3"
        label="操作人"
        width="130"
      ></el-table-column>
      <el-table-column prop="amount3" label="操作内容"></el-table-column>
    </el-table>

    <Upload
      v-if="isupload"
      :isupload="isupload"
      :config="config"
      @returnImgs="returnImgsFunc"
      >上传图片</Upload
    >
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <!-- <el-button type="primary" @click="editUser" :loading="loading"
          >确 定</el-button
        > -->
      </div>
    </template>
  </el-dialog>
</template>

<script>
import ProductApi from "@/api/product.js";
import MarketApi from "@/api/market.js";
import FinanceApi from "@/api/finance.js";
import Upload from "@/components/file/Upload.vue";
import draggable from "vuedraggable";
import { formatDate } from "@/utils/DateTime.js";
export default {
  components: {
    Upload,
    draggable,
  },
  data() {
    return {
      searchForm: {
        barCode: "",
        fileList: [],
        pageIndex: "",
        pageSize: "",
        imageId: "",
      },
      form: {},
      searchs: {},
      valueList: [],
      fullscreen: false,
      config: {},
      productData: [],
      radio: 0,
      accountList: [],
      formRules: {
        attributeName: [
          {
            required: true,
            message: "请输入分类名称",
            trigger: "blur",
          },
        ],
        imageId: [
          {
            required: true,
            message: "请上传分类图片",
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
      formLabelWidth: "100px",
      /*是否显示*/
      dialogVisible: false,
      status: -1,
      activeName: "detail",
      loading: false,
      /*是否上传图片*/
      isupload: false,
      isDelete: 0,
      depotList: [],
      supplierList: [],
    };
  },
  props: ["open_detail", "addForm", "editForm"],
  created() {
    this.dialogVisible = this.open_detail;
    this.addressForm = this.addForm;
    this.filteredBillItems = this.editForm;
  },
  methods: {
    handleClick(tab) {
      let self = this;
      self.status = tab;
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection;
      console.log(this.selectedRows, "selectedRows");
    },
    getSummary({ columns, data }) {
      let sums = [];
      // 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
      sums.push("合计");
      columns.forEach((column, index) => {
        if (
          column.property == "operNumber" ||
          column.property == "basicNumber" ||
          column.property == "allPrice"
        ) {
          const values = data.map((item) => Number(item[column.property]));
          const sum = values.reduce((prev, curr) => prev + curr, 0);
          sums[index] = sum; // 将合计值添加到新数组中
        } else {
          sums.push(""); // 其他列的合计值为空
        }
      });

      return sums;
    },
    reserch() {
      let params = this.searchs;
      console.log(params, "params");
      ProductApi.productExtendIndex(params)
        .then((res) => {
          const newRecords = res.data.records;
          const mergedData = [...this.productData, ...newRecords];
          const uniqueData = Array.from(
            new Set(mergedData.map((item) => item.id))
          ).map((id) => mergedData.find((item) => item.id === id));
          this.productData = uniqueData;
          ElMessage({
            message: "查询成功",
            type: "success",
          });
          if (this.productData.length > 0) {
            this.productData.forEach((row) => {
              row.materialUnit = row.productUnit.basicUnit;
              this.handleUnitChange(row);
            });
          }
          this.searchs = {};
        })
        .catch((error) => {});
    },
    getStorageList(id) {
      let self = this;
      SettingApi.depotStorageToSet({
        depotStorageId: id,
      })
        .then((res) => {})
        .catch((error) => {});
    },
    handNum(index) {
      this.form.options[index].optionId = index + 65;
      return String.fromCharCode(index + 65);
    },
    addOptions() {
      this.form.options = this.form.options || [];
      this.form.options.push({});
    },
    delImg(type, index) {
      if (type == 1) {
        delete this.form.imagePath;
        delete this.form.imageId;
      } else if (type == 2) {
        delete this.form.options[index].imagePath;
        delete this.form.options[index].imageId;
      }
    },
    changOptions(index) {
      let flag = this.form.options.every((v) => {
        return !(v.isAnswer == 1);
      });
      if (flag) {
        this.form.options[index].isAnswer = 1;
      } else {
        this.form.options.forEach((v) => {
          v.isAnswer = 0;
        });
        this.form.options[index].isAnswer = 1;
      }
    },
    removeOptions(index) {
      this.form.options.splice(index, 1);
      this.isDelete = 1;
    },
    /*添加用户*/
    editUser() {
      let self = this;
      let billItemList = self.productData.map((option) => ({
        productId: option.productId,
        productExtendId: option.productExtendId,
        sku: option.sku,
        materialUnit: option.materialUnit,
        operNumber: option.operNumber,
        basicNumber: option.basicNumber,
        unitPrice: option.unitPrice,
        discountRate: option.discountRate,
        discountPrice: option.discountPrice,
        allPrice: option.allPrice,
        remark: option.remark,
        purchaseCycle: option.purchaseCycle,
        arrivalDate: formatDate(option.arrivalDate, "YYYY-MM-DD"),
        id: option.id,
      }));
      // let filePaths = [];
      // if (self.searchForm.fileList) {
      //   for (let file of self.searchForm.fileList) {
      //     filePaths.push(file.filePath);
      //   }
      //   self.searchForm.fileList = filePaths;
      //   console.log(filePaths, "filePaths");
      // }

      // if (filePaths == "") {
      //   ElMessage({
      //     message: "请选择图片",
      //     type: "warning",
      //   });
      //   return;
      // }
      let params = self.searchForm;
      params.fileList = self.searchForm.fileList;
      params.billItemList = billItemList;
      console.log(params, "params");
      self.loading = true;
      MarketApi.marketOrderEdit(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "编辑成功",
            type: "success",
          });
          self.dialogFormVisible(true);
        })
        .catch((error) => {
          console.log("错误提示", error);
          self.loading = false;
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
    openUpload(e) {
      this.type = e;
      this.isupload = true;
    },
    /*获取图片*/
    returnImgsFunc(e) {
      if (e != null && e.length > 0) {
        for (let img of e) {
          if (!this.searchForm.fileList) {
            this.searchForm.fileList = [];
          }
          this.searchForm.fileList.push({
            fileId: img.fileId,
            filePath: img.filePath,
          });
        }
      }
      this.isupload = false;
    },
  },
};
</script>

<style scoped>
.text-detail {
  width: 10px;
  height: 100%;
  margin: 5px;
  line-height: 20px;
  background-color: rgba(239, 239, 239, 1);
  text-align: center;
}

.text-center {
  white-space: nowrap;
  margin: 10px;
  padding: 10px;
}

.text-name {
  width: 47px;
  height: 20px;
  margin: 10px;
  color: rgba(16, 16, 16, 1);
  font-size: 14px;
  text-align: left;
  white-space: nowrap;
}

.text-product {
  margin: 10px;
  padding: 10px;
}

.imgurl {
  width: 58px;
  height: 58px;
  border-radius: 5px;
}

.img {
  margin-top: 10px;
}

.fdw {
  display: flex;
  align-items: center;
}

.tip {
  width: 190px;
  height: 21px;
  color: rgba(153, 153, 153, 1);
  font-size: 14px;
  text-align: left;
  margin-left: 100px;
  margin-bottom: 20px;
}

.titles {
  left: 424px;
  top: 418px;
  width: 262px;
  height: 21px;
  color: rgba(16, 16, 16, 1);
  font-size: 14px;
  text-align: left;
  margin-left: 40px;
  margin-bottom: 20px;
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

.select {
  padding-right: 40px;
}
</style>
