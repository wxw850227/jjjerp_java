<template>
  <el-dialog
    :title="titleName"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :fullscreen="fullscreen"
    :show-close="false"
    align-center
  >
    <template #header="{ close, titleId, titleClass }">
      <div class="my-header d-b-c">
        <div :id="titleId" :class="titleClass" class="flex-1">
          {{ titleName }}
        </div>
        <el-icon @click="fullscreen = !fullscreen"><Plus /></el-icon>
        <el-icon @click="close"><Close /></el-icon>
      </div>
    </template>
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
      <el-table-column prop="#" label="序号" width="60">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="depotName" label="仓库"></el-table-column>
      <el-table-column prop="totalStock" label="实际"></el-table-column>
      <el-table-column prop="stockNum" label="可用"></el-table-column>
      <el-table-column prop="freezeStock" label="锁定"></el-table-column>
    </el-table>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">我知道了</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import StockApi from "@/api/stock.js";
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
      open_purchase: false,
      fullscreen: false,
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
    };
  },
  computed: {
    titleName() {
      return "【商品】" + this.editForm.name + "【规格】" + this.editForm.sku;
    },
  },
  props: ["open_detail", "addForm", "editForm", "openDetail"],
  created() {
    this.dialogVisible = this.open_detail;
    this.searchForm = this.editForm;
    console.log(this.editForm, "this.editForm");
    this.getselect();
  },
  methods: {
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_purchase = e.openDialog;
        if (e.type == "success") {
          this.dialogVisible = false;
          this.$emit("closeDialog", {
            type: "success",
            openDialog: false,
          });
        }
      }
    },
    getselect() {
      let params = {};
      params.pageIndex = this.pageIndex;
      params.pageSize = this.pageSize;
      params.productExtendId = this.editForm.id;
      this.loading = true;
      StockApi.stockDepotIndex(params, true)
        .then((res) => {
          this.productData = res.data;

          this.loading = false;
        })
        .catch((e) => {
          console.log(e);
          this.loading = false;
        });
    },
    getSummarys({ columns, data }) {
      let sums = [];
      // 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
      sums.push("合计");
      columns.forEach((column, index) => {
        if (
          column.property == "stockNum" ||
          column.property == "freezeStock" ||
          column.property == "totalStock"
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
