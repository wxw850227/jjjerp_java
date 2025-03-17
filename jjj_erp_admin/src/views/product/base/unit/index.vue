<template>
  <div class="product bgset set-main">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="searchForm"
        class="demo-form-inline"
      >
        <el-form-item label="计量单位">
          <el-input
            size="small"
            clearable
            class="form_content_width"
            v-model="searchForm.name"
            placeholder="请输入计量单位"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            size="small"
            v-model="searchForm.state"
            placeholder=""
            class="form_content_width"
          >
            <el-option label="全部" :value="-1"></el-option>
            <el-option
              v-for="(item, index) in commentList"
              :key="index"
              :label="item.name"
              :value="item.val"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button size="small" type="primary" @click="onSubmit"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="common-level-rail d-b-c">
      <div class="d-s-c flex-1">
        <el-button
          size="small"
          type="primary"
          icon="Plus"
          @click="addClick"
          v-auth="'/product/base/unit/add'"
          >新增</el-button
        ><el-button
          size="small"
          icon="Delete"
          @click="delClick"
          v-auth="'/product/base/unit/delete'"
          >删除</el-button
        ><el-button
          size="small"
          @click="openClick()"
          v-auth="'/product/base/unit/setStatus'"
          :disabled="selectedRows.length == 0 || selectedRows[0].enabled == 1"
          >启用</el-button
        ><el-button
          size="small"
          @click="openClick()"
          v-auth="'/product/base/unit/setStatus'"
          :disabled="
            selectedRows.length == 0 || selectedRows[0].enabled == null
          "
          >禁用</el-button
        >
      </div>
      <!-- 分页 -->
      <el-pagination
        size="small"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        background
        :page-sizes="[20, 50, 100]"
        :current-page="curPage"
        :page-size="pageSize"
        layout="total, prev, next, jumper,sizes"
        :total="totalDataNumber"
      >
      </el-pagination>
    </div>
    <!--表格-->

    <el-table
      class="flex-1"
      size="small"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
      v-loading="loading"
    >
      <el-table-column type="selection" width="54"></el-table-column>
      <el-table-column
        prop="basicUnitId"
        label="ID"
        width="42"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="60">
        <template #default="scope">
          <el-button @click="editClick(scope.row)"  type="primary"
					  link size="small"
            >编辑</el-button
          >
          <!-- <el-button @click="delClick(scope.row)"  type="primary"
					  link size="small">
                删除
              </el-button> -->
        </template>
      </el-table-column>
      <el-table-column prop="" label="状态" width="80">
        <template #default="scope">
          <el-text size="small" type="success" v-if="scope.row.enabled == 1"
            >开启</el-text
          >
          <el-text size="small" type="danger" v-else>关闭</el-text>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="计量单位"></el-table-column>
      <el-table-column
        prop="basicUnit"
        label="基本单位"
        width="100"
        show-
      ></el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="140"
      ></el-table-column>
    </el-table>

    <Add
      v-if="open_add"
      :open_add="open_add"
      @closeDialog="closeDialogFunc($event, 'add')"
    ></Add>
    <Edit
      v-if="open_edit"
      :open_edit="open_edit"
      :editForm="categoryModel"
      @closeDialog="closeDialogFunc($event, 'edit')"
    ></Edit>
  </div>
</template>

<script>
import ProductApi from "@/api/product.js";
import Add from "./add.vue";
import Edit from "./edit.vue";
export default {
  components: {
    Add,
    Edit,
  },
  data() {
    return {
      loading: true,
      activeName: "-1",
      num: 0,
      /*列表数据*/
      tableData: [],
      selectedRows: [], // 选中的行数据
      /*一页多少条*/
      pageSize: 20,
      /*总条数*/
      totalDataNumber: 0,
      open_add: false,
      open_edit: false,
      /*当前是第几页*/
      curPage: 1,
      /*搜索参数*/
      searchForm: {
        state: -1,
        name: "",
      },
      commentList: [
        {
          name: "启用",
          val: 1,
        },
        {
          name: "停用",
          val: 0,
        },
      ],
      status: -1,
    };
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      let Params = self.searchForm;
      self.getData(Params);
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection;
      console.log(this.selectedRows, "selectedRows");
    },
    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      let Params = self.searchForm;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      ProductApi.unitList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
          self.num = res.data.num;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    addClick() {
      this.open_add = true;
    },
    editClick(row) {
      this.open_edit = true;
      this.categoryModel = row;
    },
    closeDialogFunc(e, f) {
      if (f == "add") {
        this.open_add = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
      if (f == "edit") {
        this.open_edit = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },

    /*订单详情*/
    orderClick(row) {
      this.$router.push({
        path: "/product/comment/Order",
        query: {
          order_id: row.orderId,
        },
      });
    },

    /*删除*/
    delClick(row) {
      let self = this;
      if (self.selectedRows.length === 0) {
        ElMessage.warning("至少选择一条数据");
        return;
      }
      ElMessageBox.confirm("是否删除选中的数据", "提示", {
        type: "warning",
      }).then(() => {
        const ids = this.selectedRows.map((row) => row.basicUnitId).join(",");
        self.loading = true;
        ProductApi.unitDelete(
          {
            ids: ids,
          },
          true
        )
          .then((data) => {
            ElMessage({
              message: "删除成功",
              type: "success",
            });
            self.loading = false;
            self.getData();
          })
          .catch((error) => {
            self.loading = false;
          });
      });
    },
    openClick() {
      let self = this;
      let confirmMessage = "";
      if (self.selectedRows.length === 0) {
        ElMessage.warning("至少选择一条数据");
        return;
      }
      console.log(self.selectedRows, "selectedRows");
      if (self.selectedRows[0].enabled) {
        confirmMessage = "确认要禁用吗？";
      } else {
        confirmMessage = "确定要开启吗？";
      }
      const state = this.selectedRows[0].enabled ? 0 : 1;
      ElMessageBox.confirm(confirmMessage, "友情提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const ids = this.selectedRows.map((row) => row.basicUnitId).join(",");
          self.loading = true;
          ProductApi.unitSetStatus(
            {
              ids: ids,
              state: state,
            },
            true
          )
            .then((data) => {
              self.loading = false;
              if (data.code == 1) {
                ElMessage({
                  message: "修改成功",
                  type: "success",
                });
                self.getData();
              } else {
                self.loading = false;
              }
            })
            .catch((error) => {
              self.loading = false;
            });
        })
        .catch(() => {
          self.loading = false;
        });
    },
    /*搜索查询*/
    onSubmit() {
      let params = this.searchForm;
      this.loading = true;
      this.$nextTick(() => {
        this.getData(params);
      });
    },

    /*切换选项卡*/
    handleClick(tab) {
      let self = this;
      self.curPage = 1;
      self.loading = true;
      self.status = tab;
      self.getData();
    },
  },
};
</script>

<style></style>
