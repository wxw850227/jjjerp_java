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
        <el-form-item label="仓库编码">
          <el-input
            size="small"
            v-model="searchForm.name"
            placeholder="请输入编码"
          ></el-input>
        </el-form-item>
        <el-form-item label="仓库名称">
          <el-input
            size="small"
            v-model="searchForm.name"
            placeholder="请输入名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="委外仓">
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
        <el-form-item label="状态">
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
      <el-button
        size="small"
        type="primary"
        icon="Plus"
        @click="addClick"
        v-auth="'/setting/supplier/add'"
        >导出仓库信息</el-button
      >
      <el-button
        size="small"
        type="primary"
        icon="Plus"
        @click="addClick"
        v-auth="'/setting/supplier/add'"
        >复制仓库费用</el-button
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
            prop="depotId"
            label="ID"
            width="55"
          ></el-table-column>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button @click="editClick(scope.row)"  type="primary"
					  link size="small"
                >编辑</el-button
              >
              <el-button @click="openClick(scope.row)"  type="primary"
					  link size="small">
                {{ scope.row.enabled ? "停用" : "启用" }}
              </el-button>
              <el-button
                v-if="scope.row.enabled == 0"
                @click="delClick(scope.row)"
                type="primary"
                link
                size="small"
              >
                删除
              </el-button>
            </template>
          </el-table-column>

          <el-table-column
            prop="code"
            label="仓库编码"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="仓库名称"
            width="100"
          ></el-table-column>
          <el-table-column prop="type" label="仓库类型">
            <template #default="scope">
              <el-tag v-if="scope.row.type == 0">正品仓</el-tag>
              <el-tag v-if="scope.row.type == 1">次品仓</el-tag>
              <el-tag v-if="scope.row.type == 2">门店仓</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="contacts" label="委外仓"></el-table-column>
          <el-table-column
            prop="province"
            label="省"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="city"
            label="市"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="region"
            label="区"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="addressd"
            label="详细地区"
            width="120"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column prop="contacts" label="联系人"></el-table-column>
          <el-table-column prop="fixPhone" label="固话"></el-table-column>
          <el-table-column
            prop="phone"
            label="手机"
            width="100"
          ></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="130">
          </el-table-column>
          <el-table-column
            prop="updateTime"
            label="最后修改时间"
            width="130"
          ></el-table-column>
          <el-table-column prop="remark" label="备注"></el-table-column>
          <el-table-column prop="enabled" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.enabled == 1">启用</el-tag>
              <el-tag v-else>停用</el-tag>
            </template>
          </el-table-column>
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
      SettingApi.depotList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.records;
          self.tableData.forEach((item) => {
            let addressd = "";
            if (item.province) {
              addressd += item.province;
            }
            if (item.city) {
              addressd += item.city;
            }
            if (item.region) {
              addressd += item.region;
            }
            if (item.address) {
              addressd += item.address;
            }
            item.addressd = addressd;
          });
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
        SettingApi.depotDel({
          depotId: row.depotId,
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
          SettingApi.depotSet(
            {
              depotId: row.depotId,
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
