<template>
  <!--
    	作者：luoyiming
    	时间：2019-10-25
    	描述：商城管理-添加商城
    -->
  <el-dialog title="新增小程序商城" v-model="dialogVisible" @close='dialogFormVisible' :close-on-click-modal="false"
    :close-on-press-escape="false">
    <el-form size="small" :model="form" ref="form" :rules="rules">
      <!--去除浏览器默认-->
      <div style="height: 0; overflow: hidden;">
        <input type="password">
      </div>
      <el-form-item label="商城名称" :label-width="formLabelWidth" prop="appName">
        <el-input v-model="form.appName" autocomplete="off" placeholder="请输入商城名称"></el-input>
      </el-form-item>
      <el-form-item label="过期时间" :label-width="formLabelWidth" prop="expireTime">
        <el-date-picker v-model="form.expireTime" type="date" placeholder="过期时间" value-format="YYYY-MM-DD" :disabled="form.noExpire"></el-date-picker>
        <el-checkbox v-model="form.noExpire" class="pl16">永不过期</el-checkbox>
      </el-form-item>
      <el-form-item label="商家账户名" :label-width="formLabelWidth" prop="userName">
        <el-input v-model="form.userName" autocomplete="off" placeholder="请输入商家账户名"></el-input>
        <p class="gray">注：商家后台用户名</p>
      </el-form-item>
      <el-form-item label="商家账户密码" :label-width="formLabelWidth" prop="password">
        <el-input type="password" v-model="form.password" autocomplete="off" placeholder="请输入密码"></el-input>
        <p class="gray">注：商家后台用户密码</p>
      </el-form-item>
      <el-form-item label="确认密码" :label-width="formLabelWidth" prop="passwordConfirm">
        <el-input type="password" v-model="form.passwordConfirm" autocomplete="off" placeholder="请输入确认密码"></el-input>
      </el-form-item>
      <el-form-item label="微信服务商支付" :label-width="formLabelWidth" prop="weixinService">
        <el-checkbox v-model="form.weixinService">开启</el-checkbox>
      </el-form-item>
    </el-form>
    <template #footer>
    <div  class="dialog-footer">
      <el-button @click="dialogFormVisible">取 消</el-button>
      <el-button type="primary" @click="addClick()" :loading="loading">确 定</el-button>
    </div>
    </template>
  </el-dialog>
</template>

<script>
  import ShopApi from '@/api/shop.js';
  export default {
    data() {
      /*验证商城名称*/
      let checkAppName = (rule, value, callback) =>{
      	if(!value){
      		return callback(new Error('请输入商城名称'));
      	}else{
      		if(this.$filter.isAllSpace(value)){
      			return callback(new Error('不能全是空格'));
      		}else{
      			callback();
      		}
      	}
      };

      /*验证用户名*/
      let checkUsername = (rule, value, callback) =>{
      	if (!this.$filter.replaceSpace(value)) {
      		return callback(new Error('商家账户名'));
      	} else {
      		if(this.$filter.hasSpace(value)){
      			return callback(new Error('不能包含空格'));
      		}else{
      			callback();
      		}
      	}
      };

      /*密码验证*/
      let checkPassword = (rule, value, callback) => {
      	if (!value) {
      		callback(new Error('请输入密码'));
      	} else {
      		if(this.$filter.hasSpace(value)){
      			return callback(new Error('不能包含空格'));
      		}else if(value.length<6){
      			return callback(new Error('长度不能小于6位'));
      		}else{
      			callback();
      		}
      	}
      };

      /*确认新密码验证*/
      let checkPasswordConfirm = (rule, value, callback) => {
      	if (!value) {
      		callback(new Error('请填写确认密码'));
      	} else if (value !== this.form.password) {
      		callback(new Error('确认密码不一致'));
      	} else {
      		callback();
      	}
      };

      return {
        form: {
          noExpire: false,
          weixinService: false
        },
        /*左边长度*/
        formLabelWidth: '120px',
        /*是否显示*/
        dialogVisible: false,
        /*是否正在加载*/
        loading: false,
        /*验证规则*/
        rules: {
        	appName: [{ validator:checkAppName, required: true, trigger: 'blur' }],
        	userName: [{ validator:checkUsername, required: true, trigger: 'blur' }],
          password: [{ validator:checkPassword, required: true, trigger: 'change' }],
          passwordConfirm: [{ validator:checkPasswordConfirm, required: true, trigger: 'blur' }]
        },
      };
    },
    props: ['open_add'],
    created() {
      this.dialogVisible = this.open_add;
    },
    methods: {
      /*添加商城*/
      addClick() {
        let self = this;
        let params = this.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            ShopApi.addShop(params, true).then(res => {
                self.loading = false;
                if (res.code == 1) {
                 ElMessage({
                    message: '恭喜你，添加成功',
                    type: 'success'
                  });
                  self.dialogFormVisible(true);
                }
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });
      },
      /*关闭弹窗*/
      dialogFormVisible(e) {
        if (e) {
          this.$emit('closeDialog', {
            type: 'success',
            openDialog: false
          })
        } else {
          this.$emit('closeDialog', {
            type: 'error',
            openDialog: false
          })
        }
      }
    }
  };
</script>

<style>
  .pl16{
    padding-left: 16px;
  }
</style>
