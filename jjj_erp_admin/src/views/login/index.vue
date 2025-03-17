<template>
  <div class="wrap">
    <div class="box">
      <div class="box_l" @click="gotohome">
        <img src="/static/imgs/login1.png" class="img" />
      </div>
      <div class="box_r">
        <div class="logo-title">
          <img src="/static/imgs/logo2.png" class="logo-img" />
          <span class="title">玖玖云电商ERP</span>
        </div>
        <!-- <div class="desc">{{ shop_name  }}</div> -->
        <!-- <div class="desc">密码登录</div> -->
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-position="left"
          label-width="0px"
          size="large"
        >
          <!--用户名-->
          <el-form-item prop="account"
            ><el-input
              style="border-radius: 24px"
              @keyup.enter.native="SubmitFunc"
              type="primary"
              link
              v-model="ruleForm.account"
              :prefix-icon="'User'"
              auto-complete="off"
              placeholder="用户名"
            >
            </el-input
          ></el-form-item>
          <!--密码-->
          <el-form-item prop="checkPass">
            <el-input
              style="border-radius: 24px"
              @keyup.enter.native="SubmitFunc"
              type="password"
              :prefix-icon="'Lock'"
              v-model="ruleForm.checkPass"
              auto-complete="off"
              placeholder="密码"
              show-password
            ></el-input
          ></el-form-item>
          <div class="login-options">
            <el-checkbox class="remember-login" v-model="rememberLogin"
              >下次直接登录</el-checkbox
            >
            <span class="forget-password">忘记密码?</span>
          </div>
          <!--登录-->
          <el-form-item>
            <el-button
              class="login-btn"
              type="primary"
              style="width: 100%"
              @click.native.prevent="SubmitFunc"
              :loading="logining"
              >登录</el-button
            >
          </el-form-item>
        </el-form>
        <div class="contact-info">
          <div>
            联系方式: QQ：<el-link type="primary">3211941389</el-link
            >(售前)<el-link type="primary">1947141988</el-link>
            (售后)
          </div>
          <div>
            Copyright©2019 jjjshop.net 武汉玖玖珈网络科技有限公司
            鄂ICP备17003996号-10
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import IndexApi from "@/api/index.js";
// import bgimg from "@/assets/img/login_bg.png";
import UserApi from "@/api/user.js";
import { useUserStore } from "@/store";
import { setStorage, getStorage } from "@/utils/storageData";
const { afterLogin } = useUserStore();
export default {
  data() {
    return {
      rememberLogin: false,
      /*是否正在加载*/
      loading: true,
      /*系统名称*/
      shop_name: "",
      /*背景图片*/
      bgimg_url: "",
      /*是否正在提交*/
      logining: false,
      /*表单对象*/
      ruleForm: {
        /*用户名*/
        account: "admin",
        /*密码*/
        checkPass: "123456",
      },
      /*验证规则*/
      rules: {
        /*用户名*/
        account: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        /*密码*/
        checkPass: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
        ],
      },
      /*基础配置*/
      baseData: {},
    };
  },
  created() {
    console.log(import.meta.env);
    this.getData();
    if (this.$route.query.from && this.$route.query.from == "admin") {
      this.saasLogin();
    }
  },
  // mounted() {
  //   if (localStorage.getItem("rememberLogin") == "true") {
  //     if (this.$route.query.jjjerpToken) {
  //       const token = this.$route.query.jjjerpToken;
  //       setStorage(JSON.stringify(token), strongToken);
  //       this.$nextTick(() => {
  //         this.SubmitFunc();
  //       });
  //     }
  //   }
  // },
  methods: {
    gotohome() {
      window.open("https://www.jjjshop.net", "_blank");
    },
    /*获取基础配置*/
    getData() {
      let self = this;
      IndexApi.base(true)
        .then((res) => {
          self.loading = false;
          const { data } = res;
          self.shop_name = data.shopName;
          if (data.shopBgImg != "") {
            self.bgimg_url = data.shopBgImg;
          } else {
            // self.bgimg_url = bgimg;
          }
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*登录方法*/
    SubmitFunc(ev) {
      var self = this;
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.logining = true;
          var Params = {
            username: this.ruleForm.account,
            password: this.ruleForm.checkPass,
          };
          /*调用登录接口*/
          UserApi.login(Params, true)
            .then(async (res) => {
              this.logining = false;
              if (res.code == 1) {
                await afterLogin(res);
                this.logining = false;
                self.$router.push({
                  path: "/home",
                });
                if (this.rememberLogin) {
                  localStorage.setItem("rememberLogin", true);
                } else {
                  localStorage.setItem("rememberLogin", false);
                  // localStorage.removeItem("rememberLogin");
                }
              } else {
                ElMessage({
                  message: "登录失败",
                  type: "error",
                });
              }
            })
            .catch((error) => {
              //接口调用方法统一处理
              this.logining = false;
            });
        }
      });
    },
    saasLogin() {
      /*调用登录接口*/
      UserApi.saasLogin({}, true)
        .then((res) => {
          console.log(res);
          if (res.code == 1) {
            delCookie("baseInfo");
            /*保存个人信息*/
            setCookie("userinfo", res.data);
            /*设置一个登录状态*/
            setCookie("isLogin", true);
            /*跳转到首页*/
            this.$router.push({
              path: "/",
            });
          } else {
            ElMessage({
              message: "登录失败",
              type: "error",
            });
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.contact-info {
  position: absolute;
  bottom: 20px;
  right: 20px;
  font-size: 12px;
  color: #a1a6bb;
  padding-right: 30px;
}

.contact-info div {
  margin-bottom: 5px;
  /* 调整行间距 */
}

.login-options {
  display: flex;
  justify-content: space-between;
  padding-bottom: 20px;

  .forget-password {
    width: 80px;
    height: 17px;
    font-family: Source Han Sans CN;
    font-weight: 400;
    font-size: 12px;
    color: #a1a6bb;
    line-height: 36px;
  }
}

::v-deep .el-checkbox.el-checkbox--large .el-checkbox__label {
  font-size: 10px;
}

// ::v-deep(.el-input){
//   --el-input-border-color: #F4F6FA !important;
//   --el-input-bg-color: #EEEEEE !important;
// }
.wrap {
  /* width: 1200px;
  margin: 0 auto; */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.box {
  // min-width: 1200px;
  width: 100%;
  height: 100%;
  background: #ffff;
  display: flex;
  align-items: center;

  .box_l {
    flex: 1;
    height: 100%;
    cursor: pointer;

    .img {
      width: 100%;
      height: 100%;

      position: relative;
      display: inline-block;
      overflow: hidden;

      object-fit: cover;
    }
  }

  .box_r {
    width: 600px;
    padding: 0 148px;
    box-sizing: border-box;
    text-align: center;

    .title {
      font-size: 25px;
      font-weight: bold;
      color: #282828;
    }

    .desc {
      padding: 12px 0;
      font-size: 14px;
      color: #333333;
      font-weight: bold;
      letter-spacing: 2px;
    }

    .el-input {
      border: 1px solid #eeeeee;
      background: #f4f6fa;
      border-radius: 4px;
      overflow: hidden;
    }

    ::v-deep(.el-input__wrapper) {
      box-shadow: none !important;
      background: transparent !important;
      font-size: 12px;
    }

    ::v-deep(.el-form-item.is-error .el-input) {
      border-color: var(--el-color-danger);
    }
  }
}

.login-btn {
  --el-button-font-weight: bold;
  border-radius: 24px;
}

.logo-title {
  display: flex;
  align-items: center;
  /* 让图片和文字垂直居中 */
  padding-left: 20px;
  padding-bottom: 20px;
}

.logo-img {
  width: 50px;
  /* 调整图片的宽度 */
  height: auto;
  /* 自适应高度 */
  margin-right: 10px;
  /* 可以根据需要调整图片和文字之间的间距 */
}

::v-deep .el-input__prefix .el-input__icon {
  transform: scale(1.7);
  /* 这里调整放大倍数，可以根据需要进行调整 */
  margin-right: 20px;
}
</style>
