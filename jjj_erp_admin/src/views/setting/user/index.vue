<template>
  <div class="product">
    <!--搜索表单-->
    <div class="common-seach-wrap">
      <el-form
        size="small"
        :inline="true"
        :model="searchForm"
        class="demo-form-inline"
      >
        <el-form-item label="客户编码">
          <el-input
            size="small"
            v-model="searchForm.name"
            placeholder="请输入客户编码"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input
            size="small"
            v-model="searchForm.name"
            placeholder="请输入姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="昵称/手机号">
          <el-input
            size="small"
            v-model="searchForm.name"
            placeholder="请输入昵称/手机号"
          ></el-input>
        </el-form-item>
        <el-form-item label="平台">
          <el-select size="small" v-model="searchForm.score" placeholder="">
            <el-option label="全部" :value="0"></el-option>
            <el-option
              v-for="(item, index) in commentList"
              :key="index"
              :label="item.name"
              :value="item.val"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button size="small" type="primary" icon="Search" @click="onSubmit"
            >查询</el-button
          >
          <el-button size="small" icon="Search" @click="onSubmit"
            >重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="common-level-rail">
      <el-button
        size="small"
        type="primary"
        icon="Plus"
        @click="addClick"
        v-auth="'/setting/supplier/add'"
        >新增</el-button
      >
    </div>
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          border
          style="width: 100%"
          @selection-change="handleSelectionChange"
          v-loading="loading"
        >
          <el-table-column
            prop="userId"
            label="ID"
            width="55"
          ></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button
                @click="editClick(scope.row, true)"
                type="primary"
                link
                size="small"
                >查看</el-button
              >
              <el-button @click="editClick(scope.row)"  type="primary"
					  link size="small"
                >修改</el-button
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="userCode"
            label="客户编码"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="realName"
            label="客户名称"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="platformType"
            label="平台"
            width="150"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column prop="phone" label="手机"></el-table-column>
          <el-table-column prop="country" label="国家"></el-table-column>
          <el-table-column prop="province" label="省"></el-table-column>
          <el-table-column prop="city" label="市"></el-table-column>
          <el-table-column prop="region" label="区"></el-table-column>
          <el-table-column
            prop="addressd"
            label="详细地区"
            width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="email"
            label="E-mail"
            width="130"
          ></el-table-column>
          <el-table-column
            prop="fixPhone"
            label="固话"
            width="130"
          ></el-table-column>
          <el-table-column
            prop="expiryTime"
            label="业务员"
            width="130"
          ></el-table-column>
          <el-table-column
            prop="initial"
            label="当前应收款"
            width="130"
          ></el-table-column>
        </el-table>
      </div>
      <Add
        v-if="open_add"
        :open_add="open_add"
        @closeDialog="closeDialogFunc($event, 'add')"
      ></Add>
      <Edit
        v-if="open_edit"
        :open_edit="open_edit"
        :editForm="categoryModel"
        :isDetailMode="isDetailMode"
        @closeDialog="closeDialogFunc($event, 'edit')"
      ></Edit>
      <!--分页-->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page="curPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="totalDataNumber"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import SettingApi from "@/api/setting.js";
import Add from "./part/add.vue";
import Edit from "./part/edit.vue";
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
      isDetailMode: false,
      isAddressAppended: false,
      /*当前是第几页*/
      curPage: 1,
      /*搜索参数*/
      searchForm: {
        score: 0,
        name: "",
      },
      /*评价等级*/
      commentList: [
        {
          name: "好评",
          val: 10,
        },
        {
          name: "中评",
          val: 20,
        },
        {
          name: "差评",
          val: 30,
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
    },
    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getData();
    },

    /*获取列表*/
    getData(param = "") {
      let self = this;
      let Params = {};
      Params.status = self.status;
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      if (param != "") {
        Params.score = param.score;
        Params.name = param.name;
      }
      SettingApi.userList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.tableData.forEach((item) => {
            item.addressd =
              item.province + item.city + item.region + item.address;
          });
          console.log(self.addressd, "self.addressd");
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
    editClick(row, isDetail) {
      this.open_edit = true;
      this.categoryModel = row;
      this.isDetailMode = isDetail;
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

    /*删除*/
    delClick(row) {
      let self = this;
      ElMessageBox.confirm("是否删除选中的数据", "提示", {
        type: "warning",
      }).then(() => {
        self.loading = true;
        SettingApi.storeDel({
          storeId: row.storeId,
        }).then((data) => {
          ElMessage({
            message: "删除成功",
            type: "success",
          });
          self.getData();
        });
      });
    },
    openClick(row) {
      let self = this;
      let confirmMessage = "";
      if (!row.enabled) {
        confirmMessage = "确定要启用吗？";
      } else {
        confirmMessage = "确认要停用吗？";
      }
      const state = row.enabled ? 0 : 1;
      ElMessageBox.confirm(confirmMessage, "友情提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          self.loading = true;
          SettingApi.storeSet(
            {
              storeId: row.storeId,
              enabled: state,
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
    onSubmit() {},
  },
};
</script>

<style></style>
