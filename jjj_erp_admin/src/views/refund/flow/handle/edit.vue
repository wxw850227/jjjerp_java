<template>
  <el-dialog
    title="添加单据"
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
        <div :id="titleId" :class="titleClass" class="flex-1">添加单据</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="searchForm"
        class="demo-form-inline"
      >
        <el-row>
          <el-col :span="4">
            <el-form-item label="业务时间">
              <el-date-picker
                v-model="searchForm.businessTime"
                type="date"
                placeholder="请选择"
                :disabled-date="disabledDate"
                :shortcuts="shortcuts"
                :size="size"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="店铺"
              prop="storeId"
              :label-width="formLabelWidths"
            >
              <el-select v-model="searchForm.storeId" placeholder="请选择">
                <el-option
                  v-for="(item, index) in storeList"
                  :key="index"
                  :label="item.storeName"
                  :value="item.storeId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="仓库">
              <el-select v-model="searchForm.depotId" placeholder="请选择">
                <el-option
                  v-for="(item, index) in depotList"
                  :key="index"
                  :label="item.name"
                  :value="item.depotId"
                />
              </el-select> </el-form-item
          ></el-col>
          <el-col :span="4">
            <el-form-item label="快递">
              <el-select v-model="searchForm.expressId" placeholder="请选择">
                <el-option
                  v-for="(item, index) in expressList"
                  :key="index"
                  :label="item.expressName"
                  :value="item.expressId"
                />
              </el-select> </el-form-item
          ></el-col>
          <el-col :span="4">
            <el-form-item label="快递单号">
              <el-input
                size="small"
                v-model="searchForm.expressNo"
                placeholder=""
              ></el-input></el-form-item
          ></el-col>
          <el-col :span="4">
            <el-form-item label="选择客户">
              <el-select
                v-model="searchForm.customerId"
                @change="fetchAddressList"
                placeholder="请选择"
              >
                <el-option
                  v-for="(item, index) in customerList"
                  :key="index"
                  :label="item.realName"
                  :value="item.customerId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            <el-form-item label="客户地址">
              <el-select
                v-model="searchForm.customerAddressId"
                clearable
                placeholder="请选择"
              >
                <el-option
                  v-for="(address, index) in addressList"
                  :key="index"
                  :label="
                    address.city +
                    address.province +
                    address.region +
                    address.detail
                  "
                  :value="address.addressId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" v-if="open_address">
            <el-form-item label="">
              <el-link type="primary" @click="adressClick">
                <el-icon><Pointer /></el-icon>去添加客户地址</el-link
              >
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="结算账户" prop="accountId">
              <el-select v-model="searchForm.accountId" placeholder="请选择">
                <el-option
                  v-for="(item, index) in accountList"
                  :key="index"
                  :label="item.accountName"
                  :value="item.accountId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="订单号">
              <el-input
                size="small"
                v-model="searchForm.codeNum"
                placeholder=""
              ></el-input></el-form-item
          ></el-col>
          <el-col :span="4">
            <el-form-item label="备注">
              <el-input
                size="small"
                v-model="searchForm.remark"
                placeholder=""
              ></el-input></el-form-item
          ></el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="快速选择商品:">
              <el-input
                size="small"
                v-model="searchs.productCode"
                placeholder="输入编码/名称(精确到规格)后按回车"
              >
                <template #append>
                  <el-button @click="reserch" icon="Search" />
                </template>
              </el-input> </el-form-item
          ></el-col>
        </el-row>
      </el-form>
    </div>
    <el-table
      size="small"
      :data="productData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
      v-loading="loading"
      show-summary
      :summary-method="getSummarys"
    >
      <!-- <el-table-column type="selection" width="55"></el-table-column> -->
      <el-table-column prop="#" label="序号" width="60">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <!-- <el-table-column prop="id" label="ID" width="55"></el-table-column> -->
      <el-table-column prop="productExtend.imageUrl" label="图" width="80">
        <template #default="scope">
          <el-image
            style="width: 50px; height: 50px"
            :src="scope.row.productExtend.imageUrl"
            :preview-src-list="[scope.row.productExtend.imageUrl]"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
        prop="productExtend.productCode"
        label="商品编码"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="productExtend.name"
        label="商品名称"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="productExtend.barCode"
        label="条码"
        width="100"
      ></el-table-column>
      <el-table-column prop="sku" label="规格" width="100"></el-table-column>
      <el-table-column prop="materialUnit" label="单位" width="100">
        <template #default="{ row }">
          <el-select
            v-if="row.productExtend"
            :disabled="openDetail"
            v-model="row.materialUnit"
            placeholder="请选择单位"
            @change="handleUnitChange(row)"
          >
            <el-option
              :label="row.productExtend.productUnit.basicUnit"
              :value="row.productExtend.productUnit.basicUnit"
            />
            <el-option
              v-for="(item, index) in row.productExtend.productUnit.deputyList"
              :key="index"
              :label="item.deputyUnitName"
              :value="item.deputyUnitName"
            />
          </el-select>
          <el-select
            v-else
            :disabled="openDetail"
            v-model="row.materialUnit"
            placeholder="请选择单位"
            @change="handleUnitChange(row)"
          >
            <el-option
              :label="row.productUnit.basicUnit"
              :value="row.productUnit.basicUnit"
            />
            <el-option
              v-for="(item, index) in row.productUnit.deputyList"
              :key="index"
              :label="item.deputyUnitName"
              :value="item.deputyUnitName"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="operNumber" label="数量" width="100">
        <template #default="{ row }">
          <el-input
            v-model="row.operNumber"
            label="数量"
            @blur="handleOperNumberChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="basicNumber"
        label="数量(pcs)"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="unitPrice"
        label="标准售价"
        width="100"
      ></el-table-column
      ><el-table-column prop="discountPrice" label="折扣单价" width="100">
        <template #default="{ row }">
          <el-input
            v-model="row.discountPrice"
            label="折扣单价"
            @blur="handleUnitPriceChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="commodityDecimal"
        label="批发价"
        width="100"
      ></el-table-column>
      <el-table-column prop="allPrice" label="销售金额小计" width="100">
        <template #default="{ row }">
          <el-input
            v-model="row.allPrice"
            label="销售金额小计"
            @blur="handleAllChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="depotStorage" label="货位编码" width="100">
      </el-table-column>
    </el-table>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="editUser">保 存</el-button>
        <el-button @click="dialogFormVisible">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import ProductApi from "@/api/product.js";
import SettingApi from "@/api/setting.js";
import RefundApi from "@/api/refund.js";
import FinanceApi from "@/api/finance.js";
export default {
  components: {},
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
      allData: {},
      config: {},
      productData: [],
      radio: 0,
      accountList: [],
      storeList: [],
      open_purchase: false,
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
      loading: false,
      /*是否上传图片*/
      isupload: false,
      fullscreen: false,
      isDelete: 0,
      depotList: [],
      supplierList: [],
      expressList: [],
      titleName: "",
      customerList: [],
      addressList: [],
      open_address: false,
    };
  },
  props: ["open_edit", "addForm", "editForm"],
  created() {
    this.dialogVisible = this.open_edit;
    this.searchForm = this.editForm;
    this.productData = this.editForm.billItemList;
    console.log(this.addForm, "addform");
    if (this.addForm) {
      this.depotList = this.addForm.depot;
      this.supplierList = this.addForm.supplier;
      this.expressList = this.addForm.express;
      this.storeList = this.addForm.store;
    }

    this.getselect();
  },
  methods: {
    handleUnitPriceChange(row) {
      row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
    },
    handleAllChange(row) {
      const allPrice = Number(row.allPrice);
      const basicNumber = Number(row.basicNumber);

      if (isNaN(allPrice) || isNaN(basicNumber) || basicNumber === 0) {
        row.discountPrice = "0.00"; // 或者其他适当的默认值或处理方式
      } else {
        row.discountPrice = (allPrice / basicNumber).toFixed(2);
      }
    },
    adressClick() {
      this.$router.push({
        path: "/setting/basic/user/index",
      });
    },
    getselect() {
      FinanceApi.accountList({})
        .then((res) => {
          this.accountList = res.data.records;
        })
        .catch((e) => {
          console.log(e);
        });
      SettingApi.userList({})
        .then((res) => {
          this.customerList = res.data.records;
        })
        .catch((e) => {
          console.log(e);
        });
    },
    fetchAddressList() {
      const selectedCustomer = this.customerList.find(
        (customer) => customer.customerId === this.form.customerId
      );
      if (selectedCustomer) {
        if (selectedCustomer.addressList != "") {
          this.addressList = selectedCustomer.addressList;
          this.open_address = false;
        } else {
          this.$forceUpdate();
          this.form.customerAddressId = null;
          this.open_address = true;
        }
      }
    },
    handleOperNumberChange(row) {
      if (row.productExtend) {
        if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
          row.basicNumber = row.operNumber * 1;
          this.handleUnitPriceChange(row);
        } else {
          const deputyUnit = row.productExtend.productUnit.deputyList.find(
            (unit) => unit.deputyUnitName === row.materialUnit
          );
          row.basicNumber = row.operNumber * deputyUnit.ratio;
          this.handleUnitPriceChange(row);
        }
      } else {
        if (row.materialUnit == row.productUnit.basicUnit) {
          row.basicNumber = row.operNumber * 1;
          this.handleUnitPriceChange(row);
        } else {
          const deputyUnit = row.productUnit.deputyList.find(
            (unit) => unit.deputyUnitName === row.materialUnit
          );
          row.basicNumber = row.operNumber * deputyUnit.ratio;
          this.handleUnitPriceChange(row);
        }
      }
    },
    handleUnitChange(row) {
      if (!row.operNumber) {
        row.operNumber = 1; // 默认设置为1
        row.discountRate = row.discountRate || 10;
      }
      console.log(row, "row");
      if (row.productExtend) {
        if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
          row.basicNumber = row.operNumber * 1;
          this.handleUnitPriceChange(row);
        } else {
          const deputyUnit = row.productExtend.productUnit.deputyList.find(
            (unit) => unit.deputyUnitName === row.materialUnit
          );
          row.basicNumber = row.operNumber * deputyUnit.ratio;
          row.unitPrice = row.unitPrice * deputyUnit.ratio;
          this.handleUnitPriceChange(row);
        }
      } else {
        if (row.materialUnit == row.productUnit.basicUnit) {
          row.basicNumber = row.operNumber * 1;
          this.handleUnitPriceChange(row);
        } else {
          const deputyUnits = row.productUnit.deputyList.find(
            (unit) => unit.deputyUnitName === row.materialUnit
          );
          row.basicNumber = row.operNumber * deputyUnits.ratio;
          row.unitPrice = row.unitPrice * deputyUnits.ratio;
          this.handleUnitPriceChange(row);
        }
      }
    },
    editClick(row) {
      console.log(row, "row");
      this.allData = row.head;
      this.open_purchase = true;
    },
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_purchase = e.openDialog;
        if (e.type == "success") {
          this.$emit("closeDialog", {
            type: "success",
            openDialog: false,
          });
        }
      }
    },
    getSummarys({ columns, data }) {
      let sums = [];
      // 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
      sums.push("总计");
      columns.forEach((column, index) => {
        if (
          column.property == "basicNumber" ||
          column.property == "operNumber" ||
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
      if (!this.searchForm.depotId) {
        ElMessage.error("仓库不能为空");
        return; // 不执行后续操作
      }
      let params = this.searchs;
      console.log(params, "params");
      ProductApi.productExtendIndex(params)
        .then((res) => {
          this.productData = res.data.records;
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
      SettingApi.depotStorageToSet({ depotStorageId: id })
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
        linkItemId: option.billItemId,
      }));
      let params = self.searchForm;
      params.linkBillId = self.searchForm.billId;
      params.linkBillNum = self.searchForm.codeNum;
      params.billId = "";
      params.codeNum = "";
      params.depotId = self.searchForm.depotId;
      params.businessTime = self.searchForm.businessTime;
      params.businessType = self.searchForm.businessType;
      params.anotherDepotId = self.searchForm.anotherDepotId;
      params.billItemList = billItemList;
      console.log(params, "params");
      self.loading = true;
      RefundApi.refundFlowHandlePut(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "入库成功",
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
