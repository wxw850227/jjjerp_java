<template>
  <!--描述：设置-操作日志  -->
  <div class="user bgset set-main">
    <!--搜索表单-->
    <div class="common-seach-wrap d-b-c">
		<div class="d-s-c flex-1">
		  <el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
			<el-form-item><el-input size="small" v-model="searchForm.search"  class="form_content_width" placeholder="请输入用户名搜索"></el-input></el-form-item>
			<el-form-item>
			  <el-button size="small" type="primary" @click="searchSubmit">查询</el-button>
			</el-form-item>
		  </el-form>
		   </div>
	  <el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange"
	  		background :page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
	  		layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
	  </el-pagination>
    </div>
    <!--表格-->
    
        <el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
          <el-table-column prop="optLogId" label="ID" width="70"></el-table-column>
          <el-table-column prop="userName" label="用户名"></el-table-column>
          <el-table-column prop="realName" label="真实姓名"></el-table-column>
          <el-table-column prop="url" label="Url" width="300">
            <template #default="scope">
              <el-input size="small" placeholder="请输入内容" v-model="scope.row.url">
                <el-button @click="gotoUrl(scope.row.url)" icon="Link">
                  <template #append>
                    <el-button icon="Link" />
                  </template>
                </el-button>
              </el-input>
            </template>
          </el-table-column>
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="ip" label="IP" width="120"></el-table-column>
          <el-table-column prop="browser" label="Browser" width="120"></el-table-column>
          <el-table-column prop="createTime" label="添加时间"></el-table-column>
          <el-table-column fixed="right" label="操作" width="80">
            <template #default="scope">
              <el-button @click="openDetail(scope.row)"  type="primary"
					  link size="small">详情</el-button>
            </template>
          </el-table-column>
        </el-table>

    <Detail :open="open" :form="userModel" @close="closeDetail"></Detail>

  </div>
</template>

<script>
import AuthApi from '@/api/auth.js';
import Detail from './dialog/Detail.vue'
export default {
  components: {
    Detail
  },
  inject: ['reload'],
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*一页多少条*/
      pageSize: 20,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*横向表单数据模型*/
      searchForm: {
        search:''
      },
      /*是否打开弹窗*/
      open: false,
      /*编辑对象*/
      userModel: {}
    };
  },
  created() {
    /*获取列表*/
    this.getTableList();
  },
  methods: {

    /*搜索*/
    searchSubmit(){
      this.curPage = 1;
      this.getTableList();
    },

    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.loading = true;
      self.getTableList();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.curPage = 1;
      this.pageSize = val;
      this.getTableList();
    },

    /*获取列表*/
    getTableList() {
      let self = this;
      let Params = {
        pageIndex: self.curPage,
        pageSize: self.pageSize,
        username: self.searchForm.search
      };

      AuthApi.optlog(Params, true)
        .then(res => {
          self.loading = false;
          self.tableData = res.data.records;
          self.totalDataNumber = res.data.total;
        })
        .catch(error => {});
    },

    /*跳转链接*/
    gotoUrl(row) {
      let self = this;
      this.$router.push({
        path: row,
        query: {
        }
      });
    },

    /*刷新*/
    refresh() {
      this.reload();
    },

    /*打开详情*/
    openDetail(row){
      this.userModel=row;
      this.open=true;
    },

    /*关闭详情*/
    closeDetail(){
      this.open=false;
    }

  }
};
</script>

<style></style>
