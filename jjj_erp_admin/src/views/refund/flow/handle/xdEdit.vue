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
            <el-form-item label="下单时间">
              <el-date-picker
                v-model="searchForm.orderTime"
                type="date"
                placeholder="请选择"
                :disabled-date="disabledDate"
                :shortcuts="shortcuts"
                :size="size"
              />
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
      <el-table-column label="操作" width="60">
        <template #default="scope">
          <el-button @click="closeClick(scope.row)"  type="primary"
					  link size="small"
            >关闭</el-button
          >
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
      <el-table-column prop="unitPrice" label="单价">
        <template #default="{ row }">
          <el-input
            v-model="row.unitPrice"
            label="单价"
            @blur="handleUnitPriceChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="discountRate" label="折扣率" width="120">
        <template #default="{ row }">
          <el-input
            v-model="row.discountRate"
            style="width: 100px"
            @blur="handleDiscountRateChange(row)"
            :precision="0"
            label="折扣率"
          >
            <template #append>%</template>
          </el-input>
        </template>
      </el-table-column>
      <el-table-column prop="allPrice" label="应收款">
        <template #default="{ row }">
          <el-input v-model="row.allPrice" disabled />
        </template>
      </el-table-column>
      <el-table-column prop="allPrice" label="商品成本"></el-table-column>
      <el-table-column prop="allPrice" label="惠后金额"></el-table-column>
      <el-table-column prop="remark" label="备注"></el-table-column>
    </el-table>
    <el-form
      size="small"
      :inline="true"
      :model="searchForm"
      class="demo-form-inline"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item label="采购运费:">
            <el-input
              style="width: 190px"
              @blur="changePurchaseMoney()"
              v-model="searchForm.purchaseMoney"
              placeholder="采购运费"
            ></el-input>
          </el-form-item>

          <el-form-item label="应收合计:">
            <el-input
              style="width: 190px"
              v-model="searchForm.totalPrice"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="付款金额:">
            <el-input
              style="width: 190px"
              @blur="changeDepositMoney()"
              v-model="searchForm.deposit"
              placeholder="付款金额"
            ></el-input>
          </el-form-item>
          <el-form-item label="本次欠款:">
            <el-input
              style="width: 190px"
              v-model="searchForm.deptMoney"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="总欠款:">
            <el-input
              style="width: 190px"
              v-model="searchForm.deptMoney"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col>
          <el-form-item
            label="附件："
            :rules="[{ required: true, message: '请上传附件' }]"
            prop="fileList"
          >
            <div class="draggable-list">
              <draggable class="wrapper" v-model="searchForm.fileList">
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
              <div
                class="item img-select"
                @click="openProductUpload('image', 'image')"
              >
                <el-icon>
                  <Plus />
                </el-icon>
              </div>
            </div>
            <div class="gray9">建议上传尺寸相同的图片，建议尺寸比例为1:1</div>
          </el-form-item>
          <!--上传图片组件-->
          <Upload
            v-if="isupload"
            :isupload="isupload"
            :config="config"
            @returnImgs="returnImgsFunc"
            >上传图片</Upload
          >
        </el-col>
      </el-row>
    </el-form>
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
import Upload from "@/components/file/Upload.vue";
import draggable from "vuedraggable";
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
      fullscreen: false,
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
  props: ["open_xd", "addForm", "editForm"],
  created() {
    this.dialogVisible = this.open_xd;
    this.searchForm = this.editForm;
    this.productData = this.editForm.billItemList;
    console.log(this.searchForm, "searchForm");
    if (this.addForm) {
      this.depotList = this.addForm.depot;
      this.supplierList = this.addForm.supplier;
      this.expressList = this.addForm.express;
      this.storeList = this.addForm.store;
    }

    this.getselect();
  },
  methods: {
    changeDepositMoney() {
      const totalPriced = localStorage.getItem("totalPriced");
      this.searchForm.deptMoney =
        Number(totalPriced) - Number(this.searchForm.deposit);
    },
    changePurchaseMoney() {
      const totalPriced = localStorage.getItem("totalPriced");
      this.searchForm.totalPrice =
        Number(totalPriced) + Number(this.searchForm.purchaseMoney);
    },
    handleUnitPriceChange(row) {
      // 计算折后单价
      row.discountPrice = (row.unitPrice * row.discountRate) / 100;

      // 计算总价
      if (row.discountPrice && row.operNumber) {
        row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
        this.searchForm.totalPrice = row.allPrice;
      } else {
        row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
        this.searchForm.totalPrice = row.allPrice;
      }
    },
    handleDiscountRateChange(row) {
      if (row.discountRate > 100) {
        row.discountRate = 100;
      }
      // 计算折后单价
      row.discountPrice = (row.unitPrice * row.discountRate) / 100;

      // 计算总价
      if (row.discountPrice && row.operNumber) {
        row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
        this.searchForm.totalPrice = row.allPrice;
      } else {
        row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
        this.searchForm.totalPrice = row.allPrice;
      }
    },
    closeClick(row) {
      const index = this.productData.findIndex((item) => item === row);
      if (index !== -1) {
        this.productData.splice(index, 1);
      }
    },
    deleteImg(index) {
      this.searchForm.fileList.splice(index, 1);
    },
    openProductUpload: function (file_type, file_name) {
      this.file_name = file_name;
      if (file_type == "image") {
        this.config = {
          total: 9,
          fileType: "image",
        };
      } else {
        this.config = {
          total: 1,
          fileType: "video",
        };
      }
      this.isupload = true;
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
      if (row.materialUnit == row.productExtend.productUnit.basicUnit) {
        row.basicNumber = row.operNumber * 1;
      } else {
        if (row.productExtend) {
          const deputyUnit = row.productExtend.productUnit.deputyList.find(
            (unit) => unit.deputyUnitName === row.materialUnit
          );
          row.basicNumber = row.operNumber * deputyUnit.ratio;
        } else {
          const deputyUnit = row.productUnit.deputyList.find(
            (unit) => unit.deputyUnitName === row.materialUnit
          );
          row.basicNumber = row.operNumber * deputyUnit.ratio;
        }
      }
      // 计算总价
      if (row.discountPrice && row.operNumber) {
        row.allPrice = (row.discountPrice * row.basicNumber).toFixed(2);
        this.searchForm.totalPrice = row.allPrice;
      } else {
        row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
        this.searchForm.totalPrice = row.allPrice;
      }
    },
    handleUnitChange(row) {
      if (!row.operNumber) {
        localStorage.setItem("costPrice", row.costPrice);
        row.operNumber = 1; // 默认设置为1
        // 设置默认值为1
        row.unitPrice = row.costPrice || 1;
        row.discountRate = row.discountRate || 100;
        row.purchaseCycle = row.purchaseCycle || 0;
        // 计算折扣单价
        row.discountPrice = row.costPrice * row.discountRate;
        console.log(row.discountPrice, "discountPrice");
        // 计算总价
        if (row.discountPrice && row.operNumber) {
          row.allPrice = row.discountPrice * row.basicNumber;
          this.searchForm.totalPrice = row.allPrice;

          console.log(row.allPrice, "allproce");
        } else {
          row.allPrice = 0; // 如果没有折扣价或数量，则将总价设为0
          console.log(row.allPrice, "1111222233");
          this.searchForm.totalPrice = row.allPrice;
        }
      }
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
      let totalPrice = 0;
      // 在计算合计值之前创建一个新的数组，并在开头插入 '总计' 字符串
      sums.push("总计");
      columns.forEach((column, index) => {
        if (
          column.property === "purchaseMoney" ||
          column.property === "otherMoney" ||
          column.property === "allPrice"
        ) {
          const values = data.map((item) => Number(item[column.property]));
          const sum = values.reduce((prev, curr) => prev + curr, 0);
          if (isNaN(sum)) {
            console.warn(`合计值为 NaN，列名：${column.property}`);
            sums[index] = 0; // 将 NaN 替换为 0
          } else {
            sums[index] = sum; // 将合计值添加到新数组中
            if (column.property === "allPrice") {
              totalPrice = sum; // 如果当前列是总价，则将合计值赋给 totalPrice
            }
          }
        } else {
          sums.push(""); // 其他列的合计值为空
        }
      });
      console.log("总价:", totalPrice);
      if (!isNaN(totalPrice)) {
        this.totalPrices = totalPrice;
        console.log(this.totalPrices, "this.totalPrices");
        localStorage.setItem("totalPriced", this.totalPrices);
        this.searchForm.totalPrice = localStorage.getItem("totalPriced");
      } else {
        // 在这里你可以将 totalPriced 设置为默认值，比如 0
        this.totalPrices = 0;
        localStorage.setItem("totalPriced", this.totalPrices);
        this.searchForm.totalPrice = localStorage.getItem("totalPriced");
      }
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
          localStorage.removeItem("totalPriced");
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
      RefundApi.refundFlowHandleOrder(params, true)
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "下单成功",
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
        // 将新文件添加到 searchForm.fileList 中
        for (let img of e) {
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
