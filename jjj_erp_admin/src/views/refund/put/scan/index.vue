<template>
  <el-form size="small" ref="form" :model="form">
    <el-container>
      <el-aside class="aside-box" width="260px">
        <!-- <div class="left-list">
          <div class="tc f20 fb">发货工作台</div>
        </div> -->
        <div class="left-list2">
          <div class="mb18 f16 fb">快递单号</div>
          <el-input
            v-model="form.search"
            type="text"
            clearable
            class="max-w460 mb18 el-input__inner"
			style="height: 38px"
            placeholder="请输入快递单号按回车搜索"
            @keyup.enter="handleSubmit"
          />
          <!-- <div class="mb18 f16">商品条码</div>
          <el-input
            type="text"
            class="max-w460 mb18 el-input__inner"
            @keyup.enter="serachProduct"
          /> -->
          <!-- <div class="d-e-c">
            <el-switch
              class="mb18"
              v-model="value"
              size="small"
              inactive-text="自动提交"
            />
          </div> -->

          <div class="d-c-c">
            <el-button
              style="width: 248px; height: 38px"
              type="primary"
              @click="editUser"
              size="small"
              >收货入库</el-button
            >
          </div>
        </div>
      </el-aside>
      <el-main
        style="height: 100vh; background-color: #fff; margin-left: 12px;padding:12px;"
        v-loading="orderLoading"
      >
        <div>
          <div class="Infobox mb18">
            <div class="common-form">基本信息</div>
           <!-- <el-form
              size="small"
              :inline="true"
              :model="searchForm"
              class="demo-form-inline"
			  label-width="auto"
            > -->
			<el-form
			  :label-position="labelPosition"
			      label-width="100px"
			      :model="formLabelAlign"
			      
			>
              <el-row>
                <!-- 左边显示图片 -->
                <el-col :span="2">
                  <el-image
                    style="width: 150px; height: 150px"
                    v-if="searchForm.fileUrls"
                    :src="searchForm.fileUrls"
                    :preview-src-list="[searchForm.fileUrls]"
                  />
                  <el-image v-else src="/static/imgs/imagerror.png" />
                </el-col>
                <!-- 中间显示收货信息 -->
                <el-col :span="16">
                  <div class="common-forms">收货信息补充</div>
                  <el-row>
                    <el-col :span="8">
                      <el-form-item label="店铺:">
                        <el-select
                          v-model="searchForm.storeId"
                          placeholder="请选择"
						  style="width: 100%"
                        >
                          <el-option
                            v-for="(item, index) in storeList"
                            clearable
                            :key="index"
                            :label="item.storeName"
                            :value="item.storeId"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="收货仓:">
                        <el-select
                          clearable
                          v-model="searchForm.depotId"
                          placeholder="请选择"
						  style="width: 100%"
                        >
                          <el-option
                            v-for="(item, index) in depotList"
                            :key="index"
                            :label="item.name"
                            :value="item.depotId"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="姓名:">
                        <el-input
						   style="width: 100%"
                          v-model="searchForm.realName"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="买家ID:">
                        <el-input
                          v-model="searchForm.customerId"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="快递公司:">
                        <el-select
                          clearable
                          v-model="searchForm.expressId"
                          placeholder="请选择"
						  style="width: 100%"
                        >
                          <el-option
                            v-for="(item, index) in deliveryList"
                            :key="index"
                            :label="item.expressName"
                            :value="item.expressId"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="快递单号:">
                        <el-input
                          v-model="searchForm.expressNo"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="电话:">
                        <el-input
                          v-model="searchForm.phone"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="16">
                      <el-form-item label="原单备注:">
                        <el-input
                          style="width: 400px"
                          v-model="searchForm.remark"
                          type="textarea"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-col>
                <!-- 右边显示退货信息 -->
                <el-col :span="6">
                  <div class="common-forms">退货信息</div>
                  <el-row>
                    <el-col>
                      <el-form-item label="类型:">
                        <el-input
                          v-model="searchForm.refundType"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col>
                      <el-form-item label="原因:">
                        <el-select
                          v-model="searchForm.problemCause"
                          clearable
                          placeholder="请选择"
						  style="width: 100%"

                        >
                          <el-option
                            label="物流赔付"
                            value="物流赔付"
                          ></el-option>
                          <el-option label="维修" value="维修"></el-option>
                          <el-option label="其他" value="其他"></el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col>
                      <el-form-item label="详情:">
                        <el-input
                          v-model="searchForm.problemDescribe"
                          type="textarea"
                          clearable
                        ></el-input>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-col>
              </el-row>
            </el-form>
          </div>
          <!--活动商品-->
          <div>
            <!-- <div class="common-form" style="margin-left: 20px">订单明细</div> -->
            <div class="pt16">
              <el-table
                size="mini"
                :data="tableData"
                border
                style="width: 100%"
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="操作" width="60">
                  <template #default="scope">
                    <el-button
                      @click="closeClick(scope.row)"
                      type="primary"
					  link
                      size="small"
                      >关闭</el-button
                    >
                  </template>
                </el-table-column>
                <el-table-column label="实到数量" :span="1">
                  <template #default="scope">
                    <el-input
                      v-model="scope.row.basicNumber"
                      class="text-ellipsis-2"
                    ></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="应到数量" :span="1">
                  <template #default="scope">
                    <p class="text-ellipsis-2">{{ scope.row.expected }}</p>
                  </template>
                </el-table-column>
                <el-table-column label="可入库数量" :span="1">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ scope.row.availableAllNumber }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="仓库" :span="1">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ searchForm.depotName }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="单据编号" width="150">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ searchForm.codeNum }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="售后类型" :span="1">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ searchForm.refundType }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="商品编号" :span="2">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ scope.row.productExtend.productCode }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="商品名称" width="100">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ scope.row.productExtend.name }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="规格" width="120">
                  <template #default="scope">
                    <p class="text-ellipsis-2">{{ scope.row.sku }}</p>
                  </template>
                </el-table-column>
                <el-table-column label="总可用库存" width="100">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ scope.row.productExtend.stockNum }}
                    </p>
                  </template>
                </el-table-column>
                <el-table-column label="货位" :span="1">
                  <template #default="scope">
                    <p class="text-ellipsis-2">
                      {{ scope.row.productExtend.storage }}
                    </p>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
    <!--打印-->
    <Print
      v-if="open_print"
      :open_print="open_print"
      :order_no="pop_order_no"
      @closeDialog="closeDialogFunc($event, 'print')"
    ></Print>
  </el-form>
</template>

<script>
// import Print from "@/views/warehouse/scan/part/Print";
import RefundApi from "@/api/refund.js";
export default {
  components: {
    // Print,
  },
  data() {
    var validateNum = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入数字"));
      } else {
        callback();
      }
    };
    return {
      searchForm: {},
      validateNum,
      orderLoading: false,
      productLoading: false,
      stuffLoading: false,
      subloading: false,
      order_no: "",
      bar_code: "",
      orderInfo: {},
      tableData: [],
      expressList: [],
      storeList: [],
      depotList: [],
      deliveryList: [],
      form: {
        product_list: [],
        stuff_list: [],
      },
      outbound_order_id: "",
      open_print: false,
      pop_order_no: "",
      bar_codes: "",
      value: "",
      selectedRows: {},
    };
  },
  mounted() {},
  methods: {
    handleSelectionChange(selection) {
      this.selectedRows = selection;
      console.log(this.selectedRows, "selectedRows");
    },
    closeClick(row) {
      const index = this.tableData.findIndex((item) => item === row);
      if (index !== -1) {
        this.tableData.splice(index, 1);
      }
    },
    handleSubmit(e) {
      console.log(e);
      this.serachOrder();
    },
    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      this.open_print = e.openDialog;
      this.pop_order_no = "";
    },
    serachOrder() {
      let self = this;
      self.orderLoading = true;
      RefundApi.refundPutScan(
        {
          search: self.form.search,
        },
        true
      )
        .then((res) => {
          // let accData = res.data.bill.reduce((acc, curr) => {
          //   acc[curr.billId] = curr;
          //   return acc;
          // }, {});
          self.searchForm = res.data.bill[0];
          self.expressList = res.data.express;
          self.storeList = res.data.store;
          self.depotList = res.data.depot;
          self.tableData = self.searchForm.billItemList.map((record) => ({
            ...record,
            expected: record.basicNumber,
          }));
          self.deliveryList = this.searchForm.deliveryList;
          if (this.deliveryList.length > 0) {
            this.searchForm.expressId = this.deliveryList[0].expressId;
            this.searchForm.expressNo = this.deliveryList[0].expressNo;
          }
          self.orderLoading = false;
        })
        .catch((error) => {
          self.orderLoading = false;
        });
    },
    editUser() {
      let self = this;
      let billItemList = self.tableData.map((option) => ({
        productId: option.productId,
        productExtendId: option.productExtendId,
        sku: option.sku,
        materialUnit: option.materialUnit,
        operNumber: option.operNumber,
        basicNumber: option.basicNumber,
        linkItemId: option.id,
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
      self.orderLoading = true;
      RefundApi.refundFlowHandlePut(params, true)
        .then((data) => {
          self.orderLoading = false;
          ElMessage({
            message: "入库成功",
            type: "success",
          });
          self.form.search = "";
          self.serachOrder();
        })
        .catch((error) => {
          console.log("错误提示", error);
          self.orderLoading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.aside-box {
  background-color: #fff;
  height: 100vh;
}

.left-list {
  padding: 20px;
  border-bottom: 2px solid #eee;
}

.left-list2 {
  padding: 20px 12px 0 12px;
}

.Infobox {
  background-color: #fff;
  // padding: 20px;
}
.common-forms {
  color: #3b64fc;
  text-align: left;
  position: relative;
  height: 40px;
  // padding-left: 20px;
  line-height: 40px;
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 12px;
}
</style>
