<template>
  <el-dialog
    title="打印预览"
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
        <div :id="titleId" :class="titleClass" class="flex-1">打印预览</div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
    <div id="printCont">
      <div class="pb50 main-content">
        <h2 style="text-align: center">{{ open_title }}</h2>
        <el-form size="small" ref="form" :model="orderInfo">
          <div>
            <div class="print-title1">
              <div>购货单</div>
            </div>
            <div class="print-title">
              <div>供货商：{{ supplierNames }}</div>
              <div>单据日期：2024-05-07</div>
              <div>单据编号：{{ codeNums }}</div>
              <div>币种：RMB</div>
            </div>

            <div class="table-a table-b">
              <table
                id="table2"
                align="center"
                width="100%"
                class="table xunw_table_form"
                border="0"
                cellspacing="0"
                cellpadding="0"
              >
                <tbody>
                  <tr>
                    <th class="zxstyle">序号</th>
                    <th class="zxstyle">商品</th>
                    <th class="zxstyle">单位</th>
                    <th class="zxstyle">数量</th>
                    <th class="zxstyle">购货单价</th>
                    <th class="zxstyle">折扣率(%)</th>
                    <th class="zxstyle">折扣额</th>
                    <th class="zxstyle">购货金额</th>
                    <th class="zxstyle">仓库</th>
                    <!-- <th class="zxstyle">退货理由</th>
                    <th class="zxstyle">退货状态</th> -->
                    <!-- <th class="zxstyle">审核意见</th> -->
                  </tr>
                  <tr v-for="(item, index) in productList" :key="index">
                    <td>
                      <p class="text-ellipsis-2">{{ index + 1 }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">
                        {{ item.productExtend.name }}
                      </p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ item.materialUnit }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ item.operNumber }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ item.unitPrice }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ item.discountRate }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ item.discountPrice }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ item.allPrice }}</p>
                    </td>
                    <td>
                      <p class="text-ellipsis-2">{{ depotname }}</p>
                    </td>
                  </tr>
                  <!-- 合计行 -->
                  <tr>
                    <td colspan="3" class="zxstyle">合计：</td>
                    <td class="zxstyle">{{ totalQuantity }}</td>
                    <td class="zxstyle"></td>
                    <td class="zxstyle"></td>
                    <td class="zxstyle"></td>
                    <td class="zxstyle">{{ totalAmount }}</td>
                    <td class="zxstyle"></td>
                  </tr>

                  <!-- 大写金额行 -->
                  <tr>
                    <td colspan="10" style="text-align: left">
                      合计大写金额：{{ capitalAmount }}
                    </td>
                  </tr>
                </tbody>
              </table>
              <div class="print-title2">
                <div>折扣率：100%</div>
                <div>折后金额：{{ totalAmount }}</div>
                <div>本次付款：{{ totalAmount }}</div>
                <div>本次欠款：{{ formattedDeptMoney }}</div>
              </div>
              <div class="print-title2">
                <div>备注：测试名称</div>
              </div>
              <div class="print-title2">
                <div>制单人：359.96</div>
                <div>收款人签名：__________</div>
                <div>供应商签名：__________</div>
              </div>
            </div>
          </div>
          <div style="text-align: center" v-if="isShow">
            <el-button
              @click="goReturnClick(orderInfo.id)"
              type="primary"
              size="small"
              v-auth="'/purchase/purchasereturn/add'"
              >退库处理</el-button
            >
            <el-button
              @click="goClick(orderInfo.id)"
              type="primary"
              size="small"
              v-auth="'/purchase/purchase/do_entry'"
              >入库处理</el-button
            >
          </div>
          <div style="text-align: center; margin-top: 20px" id="no-print">
            <el-button @click="goPrint" type="primary" size="small"
              >打印</el-button
            >
          </div>
          <!-- <div style="text-align:center"><el-button @click="dialogFormVisible" type="" size="small" v-auth="'/product/category/delete'" >返回</el-button> </div> -->
        </el-form>
      </div>
    </div>
  </el-dialog>
</template>
<script>
// import PurchaseApi from "@/api/purchase.js";
import printCss from "@/assets/css.ts";
// import SupplierApi from "@/api/supplier.js";
// import WarehouseApi from "@/api/warehouse.js";
import printJS from "print-js";
export default {
  components: {},
  data() {
    return {
      dialogVisible: false,
      isShow: false,
      fullscreen: false,
      // 供应商列表数组
      supplierList: [],
      //仓库列表
      warehouseList: [],
      /*表单对象*/
      form: {
        check_result: 1,
        id: "",
        stage: "",
        remark: "",
      },
      orderInfo: {
        id: "",
        status: "",
        title: "",
        order_id: "",
        supplier_id: "",
        /*下单日期*/
        order_date: "",
        /*交货日期*/
        delivery_date: "",
        /*经办人*/
        operator: "",
        /*入库仓库*/
        warehouse_id: "",
        level: 2,
        /*商品源数据*/
        tableData: [],
        /*商品列表*/
        product_list: [],
        amount: 0,
        warehouse: {},
      },
      /*是否正在加载*/
      loading: false,
      entry: null,
      productList: [],
      totalQuantity: 0,
      deptMoney: "",
      totalAmount: 0,
      depotname: "",
      codeNums: [],
      supplierNames: [],
      capitalAmount: "壹拾玖万玖仟玖佰捌拾元整",
    };
  },
  computed: {
    formattedDeptMoney() {
      if (!isNaN(this.deptMoney)) {
        return this.deptMoney;
      } else {
        return 0.0;
      }
    },
  },
  props: ["open_print", "printData", "open_title", "addForm"],
  created() {
    this.dialogVisible = this.open_print;
    this.productList =
      this.printData.map((item) => item.billItemList).flat() || [];
    console.log("this.printData", this.printData);
    this.warehouseList = this.addForm.depot;
    this.codeNums = this.printData.map((item) => item.codeNum).join(",");
    this.supplierNames = this.printData
      .map((item) => item.supplierName)
      .join(",");
    this.totalQuantity = this.productList.reduce((total, item) => {
      return total + item.operNumber;
    }, 0);
    this.totalAmount = this.productList
      .reduce((total, item) => {
        return total + parseFloat(item.allPrice);
      }, 0)
      .toFixed(2);
    this.deptMoney = this.productList
      .reduce((total, item) => {
        return total + parseFloat(item.deptMoney);
      }, 0)
      .toFixed(2);
    console.log("this.deptMoney", this.deptMoney);

    // this.depotname= this.warehouseList.find(item)
    /*获取列表*/
    // this.orderInfo.id = this.print_id;
    // this.form.id = this.print_id;
    // this.getData();
  },
  methods: {
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

    /*取消*/
    cancelFunc() {
      this.$router.back(-1);
    },
    goPrint() {
      console.log("打印");
      console.log(printCss);
      printJS({
        printable: "printCont",
        type: "html",
        //properties: [
        //    { field: 'name', displayName: '姓名', columnSize:`50%`},
        //    { field: 'sex', displayName: '性别',columnSize:`50%`},
        //],
        // header: `<p class="custom-p"> 名单 </p>`,
        // style: '#printCons {width: 600px;} .no-print{width: 0px} .itemText1 { width: 200px } .itemText2 { width: 200px } .itemText3 { width: 200px } .itemText4 { width: 200px }',
        // gridHeaderStyle:'font-size:12px; padding:3px; border:1px solid; font-weight: 100; text-align:left;',
        // gridStyle:'font-size:12px; padding:3px; border:1px solid; text-align:left;',
        // repeatTableHeader: true,
        scanStyles: false,
        style: printCss(),
        targetStyles: ["*"],
        ignoreElements: ["no-print", "bc", "gb"],
      });
    },
  },
};
</script>
<style>
.t-01 {
  text-align: center;
}

.main-content .el-col {
  border: 1px solid #ebeef5;
  padding: 6px 5px;
}

.s_order_detail {
  padding-left: 2%;
  max-width: 760px;
}

.amount {
  padding: 10px 0;
  font-size: 14px;
}
.t-01 {
  text-align: center;
}

.main-content .el-col {
  border: 1px solid #ebeef5;
  padding: 6px 5px;
}

.s_order_detail {
  padding-left: 2%;
  max-width: 760px;
}

.amount {
  padding: 10px 0;
  font-size: 14px;
}
.table-a table {
  border-top: 1px solid #eee;
  border-left: 1px solid #eee;
  table-layout: fixed !important;
}
.table-a table tbody {
  width: 100% !important;
}
.table-a table td,
.table-a table th {
  padding: 6px 10px;
  border-bottom: 1px solid #eee;
  border-right: 1px solid #eee;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  text-overflow: ellipsis;
  vertical-align: middle;
  position: relative;
  text-align: left;
  width: 16.67%;
}
.table-b.table-a table td,
.table-b.table-a table th {
  width: 50%;
}
.table-c.table-a table td,
.table-c.table-a table th {
  width: 14.2%;
}
.table-a table th {
  color: #909399;
  font-weight: bold;
  font-size: 12px;
  white-space: normal;
  word-break: break-all;
}
.table-a table td,
.table-a table td p {
  line-height: 23px;
  color: #606266;
  font-size: 12px;
  line-height: 23px;
}
.table-a table {
  border-top: 1px solid #eee;
  border-left: 1px solid #eee;
  border-spacing: 0; /*去掉单元格间隙*/
}
.print-title {
  display: flex;
  justify-content: space-between;
  position: relative;
  height: 40px;
  padding: 10px;
  line-height: 40px;
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 12px;
}
.print-title1 {
  display: flex;
  justify-content: center;
  text-align: center;
  padding-left: 0;
  line-height: 40px;
  font-size: 14px;
  font-weight: 700;
}
.print-title2 {
  display: flex;
  justify-content: space-between;
  position: relative;
  height: 40px;
  padding: 10px;
  line-height: 40px;
  font-size: 14px;
  font-weight: 500;
}
.print-info-list {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}
.print-info-item:nth-child(1),
.print-info-item:nth-child(2) {
  border-top: 1px solid #eee;
}
.print-info-item:nth-of-type(2n-1) {
  border-left: 1px solid #eee;
  width: calc(50% - 1px);
}
.print-info-item {
  width: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.print-info-title,
.print-info-center {
  padding: 6px 5px;
  box-sizing: border-box;
  color: #606266;
  line-height: 1.5;
  font-size: 12px;
  min-height: 31px;
  height: 100%;
}
.print-info-title {
  width: 33%;
  display: flex;
  align-items: center;
}
.print-info-center {
  flex: 1;
  border-right: 1px solid #eee;
  border-left: 1px solid #eee;
  word-break: break-all;
}
.d-b-s {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.d-b-c {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.d-s-c {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.d-c-c {
  display: flex;
  justify-content: center;
  align-items: center;
}
.flex-1 {
  flex: 1;
}
.merchant-list.print-info-list .print-info-item,
.merchant-list.print-info-list .print-info-center {
  border: none !important;
}
.d-c {
  display: flex;
  flex-direction: column;
}
.mt16 {
  margin-top: 16px;
}
.f14 {
  font-size: 14px;
}
.f18 {
  font-size: 18px;
}
</style>
