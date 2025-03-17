<template>

  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="200px">
      <!--添加门店-->
      <div class="common-form">新增物流公司</div>
      <el-form-item label="物流公司名称 " prop="expressName" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.expressName" class="max-w460"></el-input>
        <div class="tips">请对照
          <el-link type="primary" :underline="false" @click="gotoCompany()">
            物流公司编码表
          </el-link>
          填写
        </div>
      </el-form-item>
      <el-form-item label="物流公司代码 " prop="expressCode" :rules="[{required: true,message: ' '}]">
        <el-input v-model="form.expressCode" class="max-w460"></el-input>
        <div class="tips">用于快递100API查询物流信息，务必填写正确</div>
      </el-form-item>
      <el-form-item label="微信物流公司编码 " prop="wxCode">
        <el-input v-model="form.wxCode" class="max-w460"></el-input>
        <div class="tips">请对照
          <el-link type="primary" :underline="false" @click="gotoWxCompany()">
            微信物流公司编码表
          </el-link>
          填写
        </div>
        <div class="tips">用于向小程序发货的物流公司id，下载微信物流表格对比快递100物流公司编码表，务必填写正确，没有找到编码不填</div>
      </el-form-item>
      <el-form-item label="排序">
        <el-input v-model="form.sort" type="number" class="max-w460"></el-input>
        <div class="tips">数字越小越靠前</div>
      </el-form-item>

      <!--提交-->
      <div class="common-button-wrapper">
        <el-button type="primary" @click="onSubmit" :loading="loading">提交</el-button>
      </div>


    </el-form>


  </div>

</template>

<script>
  import SettingApi from '@/api/setting.js';
  export default {
    data() {
      return {
        loading: false,
        /*form表单数据*/
        form: {
          expressName: '',
          expressCode: '',
          wxCode: '',
          sort: 1,
        },


      };
    },
    created() {},

    methods: {
      //提交表单
      onSubmit() {
        let self = this;
        let form = this.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            SettingApi.addExpress(form, true)
              .then(data => {
                self.loading = false;
                ElMessage ({
                  message: '恭喜你，添加成功',
                  type: 'success'
                });
                self.$router.push('/setting/express/index');
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });

      },

      /*跳转物流编码*/
      gotoCompany() {
        //let baseUrl = window.location.protocol + '//' + window.location.host;
        //window.location.href = baseUrl + '/express.xlsx';
        window.location.href = 'https://qn-cdn.jjjshop.net/20230918111820133.xlsx';
      },
      /*跳转微信物流编码*/
      gotoWxCompany() {
        //let baseUrl = window.location.protocol + '//' + window.location.host;
        //window.location.href = baseUrl + '/wx_express.xlsx';
        window.location.href = 'https://qn-cdn.jjjshop.net/20230918111831794.xlsx';
      }

    }

  };
</script>

<style>
  .tips {
    color: #ccc;
  }
</style>
