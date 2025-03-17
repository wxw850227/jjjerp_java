<template>
  <!--
          作者：luoyiming
          时间：2019-10-24
          描述：消息管理
      -->
  <div class="product">
    <!--添加消息模板-->
    <div class="common-level-rail"><el-button  type="primary" @click="addClick">添加消息</el-button></div>

    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table size="small" :data="tableData" border default-expand-all :tree-props="{ children: 'children' }" row-key="plus_id" v-loading="loading">
          <el-table-column prop="messageName" label="消息名称"></el-table-column>
          <el-table-column prop="messageEname" label="消息名称(英文)"></el-table-column>
          <el-table-column prop="messageTo" label="通知对象">
            <template #default="scope">
              <span v-if="scope.row.messageTo == 10">会员</span>
              <span v-if="scope.row.messageTo == 20">商家</span>
            </template>
          </el-table-column>
          <el-table-column prop="messageType" label="消息类型">
            <template #default="scope">
              <span v-if="scope.row.messageType == 10">订单</span>
              <span v-if="scope.row.messageType == 20">分销</span>
            </template>
          </el-table-column>
          <el-table-column label="字段">
            <template #default="scope">
              <el-button @click="fieldClick(scope.row)" type="text" size="small">字段管理</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="消息描述"></el-table-column>
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作">
            <template #default="scope">
              <el-button @click="editClick(scope.row)" type="text" size="small">编辑</el-button>
              <el-button @click="deleteClick(scope.row)" type="text" size="small">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!--添加-->
    <Add v-if="open_add" :open_add="open_add" @closeDialog="closeDialogFunc($event, 'add')"></Add>
    <!--编辑-->
    <Edit v-if="open_edit" :open_edit="open_edit" :form="messageModel" @closeDialog="closeDialogFunc($event, 'edit')"></Edit>
    <!--编辑-->
    <Field v-if="open_field" :open_field="open_field" :form="messageModel" @closeDialog="closeDialogFunc($event, 'field')"></Field>
  </div>
</template>

<script>
import MessageApi from '@/api/message.js';
import Edit from './dialog/Edit.vue';
import Add from './dialog/Add.vue';
import Field from './dialog/Field.vue';
import { deepClone } from '@/utils/base.js';
export default {
  components: {
    /*编辑组件*/
    Edit: Edit,
    Add: Add,
    Field: Field
  },
  data() {
    return {
      loading: true,
      /*表格数据*/
      tableData: [],
      /*分类列表*/
      categoryList: [],
      /*是否打开添加弹窗*/
      open_add: false,
      /*是否打开编辑弹窗*/
      open_edit: false,
      /*当前编辑的对象*/
      messageModel: {},
      /*是否打开字段弹窗*/
      open_field: false
    };
  },
  created() {
    /*获取列表*/
    this.getTableList();
  },
  methods: {
    /*获取列表*/
    getTableList() {
      let self = this;
      let Params = {};
      MessageApi.messageList(Params, true)
        .then(res => {
          self.loading = false;
          self.tableData = res.data;
        })
        .catch(error => {
          self.loading = false;
        });
    },

    /*打开添加*/
    addClick() {
      this.open_add = true;
    },
    /*打开编辑*/
    editClick(item) {
      this.messageModel = deepClone(item);
      this.open_edit = true;
    },
    /*打开字段管理*/
    fieldClick(item) {
      this.messageModel = item;
      this.open_field = true;
    },
    closeDialogFunc(e, f) {
      if (f == 'add') {
        this.open_add = e.openDialog;
        if (e.type == 'success') {
          this.getTableList();
        }
      }
      if (f == 'edit') {
        this.open_edit = e.openDialog;
        if (e.type == 'success') {
          this.getTableList();
        }
      }
      if (f == 'field') {
        this.open_field = e.openDialog;
        if (e.type == 'success') {
          this.getTableList();
        }
      }
    },
    /*删除用户*/
    deleteClick(row) {
      let self = this;
ElMessageBox.confirm('删除后不可恢复，确认删除该记录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(() => {
          self.loading = true;
          MessageApi.deleteMessage(
            {
              id: row.messageId
            },
            true
          )
            .then(res => {
              self.loading = false;
              ElMessage({
                message: res.msg,
                type: 'success'
              });
              self.getTableList();
            })
            .catch(error => {
              self.loading = false;
            });
        })
        .catch(() => {});
    }
  }
};
</script>

<style></style>
