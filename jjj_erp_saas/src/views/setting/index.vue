<template>
  <!--
      作者 wangxw
      时间：2021-01-14
      描述：设置-商城设置
  -->
  <div class="product-add">
    <!--form表单-->
    <el-form size="small" ref="form" :model="form" label-width="150px">
      <div class="common-form">商城后台设置</div>
      <el-form-item label="商城系统名称" :rules="[{required: true,message: ' '}]" prop="shopName">
        <el-input v-model="form.shopName" placeholder="商城名称" class="max-w460"></el-input>
        <el-col class="tips">
          shop端商城名称，显示在登录页
        </el-col>
      </el-form-item>
      <el-form-item label="商城登录背景" prop="shopBgImg">
        <el-input v-model="form.shopBgImg" placeholder="商城登录背景" class="max-w460"></el-input>
        <el-col class="tips">
          shop端商城登录背景，不填则为系统默认登录背景，填写网络地址
        </el-col>
      </el-form-item>
      <div class="common-form">微信服务商支付设置</div>
      <el-form-item label="是否开启服务商支付">
        <div>
          <el-radio v-model="form.weixinService.isOpen" :label="true">开启</el-radio>
          <el-radio v-model="form.weixinService.isOpen" :label="false">关闭</el-radio>
        </div>
      </el-form-item>
      <template v-if="form.weixinService.isOpen">
        <el-form-item label="服务商户号">
          <el-input v-model="form.weixinService.mchId" placeholder="服务商户号" class="max-w460"></el-input>
          <el-col class="tips">
            填写服务商户号、10位数字
          </el-col>
        </el-form-item>
        <el-form-item label="服务商支付秘钥apiKey">
          <el-input v-model="form.weixinService.apiKey" placeholder="服务商支付秘钥apiKey" class="max-w460"></el-input>
          <el-col class="tips">
            填写服务商户支付秘钥apiKey(仅支持V3)
          </el-col>
        </el-form-item>
        <el-form-item label="服务商appid">
          <el-input v-model="form.weixinService.appId" placeholder="服务商appid" class="max-w460"></el-input>
          <el-col class="tips">
            填写服务商户号绑定的公众号appid
          </el-col>
        </el-form-item>
        <el-form-item label="apiclient_cert.pem">
          <el-input type="textarea" :rows="4" placeholder="使用文本编辑器打开apiclient_cert.pem文件，将文件的全部内容复制进来" v-model="form.weixinService.certPem" class="max-w460"></el-input>
          <el-col class="tips">使用文本编辑器打开apiclient_key.pem文件，将文件的全部内容复制进来</el-col>
        </el-form-item>
        <el-form-item label="apiclient_key.pem">
          <el-input type="textarea" :rows="4" placeholder="使用文本编辑器打开apiclient_cert.pem文件，将文件的全部内容复制进来" v-model="form.weixinService.keyPem" class="max-w460"></el-input>
          <el-col class="tips">使用文本编辑器打开apiclient_key.pem文件，将文件的全部内容复制进来</el-col>
        </el-form-item>
      </template>

      <div class="common-form">文件上传设置</div>
      <el-form-item label="最大图片上传">
        <el-input v-model="form.storageVo.maxImage" class="max-w460">
          <template #append>M</template>
        </el-input>
        <div class="tips">修改后请修改上传配置后生效</div>
      </el-form-item>
      <el-form-item label="最大视频上传">
        <el-input v-model="form.storageVo.maxVideo" class="max-w460">
          <template #append>M</template>
        </el-input>
        <div class="tips">修改后请修改上传配置后生效</div>
      </el-form-item>
      <el-form-item label="默认上传方式">
        <div>
          <el-radio v-model="form.storageVo.current" label="Local" @change="getRadio">本地 (不推荐)</el-radio>
          <el-radio v-model="form.storageVo.current" label="QiNiu" @change="getRadio">七牛云存储</el-radio>
          <el-radio v-model="form.storageVo.current" label="AliYun" @change="getRadio">阿里云OSS</el-radio>
          <el-radio v-model="form.storageVo.current" label="QCloud" @change="getRadio">腾讯云COS</el-radio>
          <el-radio v-model="form.storageVo.current" label="HwCloud" @change="getRadio">华为云OBS</el-radio>
        </div>
      </el-form-item>
      <!--七牛云存储-->
      <template v-if="form.storageVo.current == 'QiNiu'">
        <el-form-item label="存储空间名称 Bucket"><el-input v-model="form.storageVo.qiNiu.bucket" class="max-w460"></el-input></el-form-item>
        <el-form-item label="ACCESS_KEY AK"><el-input v-model="form.storageVo.qiNiu.accessKey" class="max-w460"></el-input></el-form-item>
        <el-form-item label="SECRET_KEY SK"><el-input v-model="form.storageVo.qiNiu.secretKey" class="max-w460"></el-input></el-form-item>
        <el-form-item label="空间域名 Domain">
          <el-input v-model="form.storageVo.qiNiu.domain" class="max-w460"></el-input>
          <div class="tips">请补全http:// 或 https://，例如：http://static.cloud.com</div>
        </el-form-item>
      </template>
      <!--阿里云OSS-->
      <template v-if="form.storageVo.current == 'AliYun'">
        <el-form-item label="存储空间名称 Bucket"><el-input v-model="form.storageVo.aliYun.bucket" class="max-w460"></el-input></el-form-item>
        <el-form-item label="AccessKeyId"><el-input v-model="form.storageVo.aliYun.accessKeyId" class="max-w460"></el-input></el-form-item>
        <el-form-item label="AccessKeySecret"><el-input v-model="form.storageVo.aliYun.accessKeySecret" class="max-w460"></el-input></el-form-item>
        <el-form-item label="外网endpoint">
          <el-input v-model="form.storageVo.aliYun.endpoint" class="max-w460"></el-input>
          <div class="tips">以杭州为例：oss-cn-hangzhou.aliyuncs.com</div>
        </el-form-item>
        <el-form-item label="空间域名 Domain">
          <el-input v-model="form.storageVo.aliYun.domain" class="max-w460"></el-input>
          <div class="tips">请补全http:// 或 https://，例如：http://static.cloud.com</div>
        </el-form-item>
      </template>
      <!--腾讯云COS-->
      <template v-if="form.storageVo.current == 'QCloud'">
        <el-form-item label="存储空间名称 Bucket"><el-input v-model="form.storageVo.qCloud.bucket"></el-input></el-form-item>
        <el-form-item label="所属地域 Region">
          <el-input v-model="form.storageVo.qCloud.region"></el-input>
          <div class="tips">请填写地域简称，例如：ap-beijing、ap-hongkong、eu-frankfurt</div>
        </el-form-item>
        <el-form-item label="SecretId"><el-input v-model="form.storageVo.qCloud.secretId"></el-input></el-form-item>
        <el-form-item label="SecretKey"><el-input v-model="form.storageVo.qCloud.secretKey"></el-input></el-form-item>
        <el-form-item label="空间域名 Domain">
          <el-input v-model="form.storageVo.qCloud.domain"></el-input>
          <div class="tips">请补全http:// 或 https://，例如：http://static.cloud.com</div>
        </el-form-item>
      </template>
      <!--华为云OBS-->
      <template v-if="form.storageVo.current == 'HwCloud'">
        <el-form-item label="存储空间地区节点 Endpoint"><el-input v-model="form.storageVo.hwCloud.endPoint" class="max-w460"></el-input></el-form-item>
        <el-form-item label="AccessKeyId"><el-input v-model="form.storageVo.hwCloud.accessKeyId" class="max-w460"></el-input></el-form-item>
        <el-form-item label="AccessKeySecret"><el-input v-model="form.storageVo.hwCloud.accessKeySecret" class="max-w460"></el-input></el-form-item>
        <el-form-item label="桶名称 BucketName"><el-input v-model="form.storageVo.hwCloud.bucketName" class="max-w460"></el-input></el-form-item>
        <el-form-item label="访问域名 Domain">
          <el-input v-model="form.storageVo.hwCloud.domain" class="max-w460"></el-input>
          <div class="tips">请补全http:// 或 https://，例如：http://static.cloud.com</div>
        </el-form-item>
      </template>

    </el-form>
  </div>
  <!--提交-->
  <div class="common-level-rail">
    <el-button  type="primary" @click="onSubmit" :loading="loading">提交</el-button>
  </div>
</template>

<script>
  import SettingApi from '@/api/setting.js';
  export default {
    data() {
      return {
        /*是否正在加载*/
        loading: false,
        /*form表单数据*/
        form: {
          weixinService: {},
          storageVo: {
            current: 'Local',
            maxImage: '',
            maxVideo: '',
            qiNiu: {

            },
            aliYun: {

            },
            qCloud: {

            },
            hwCloud: {

            },
          },
        },
        hw: {
          endPoint:"",
          accessKeyId:"",
          accessKeySecret:"",
          bucketName:"",
          domain:""
        },
      };
    },
    created() {
      this.getParams()
    },

    methods: {

      /*获取配置数据*/
      getParams() {
        let self = this;
        SettingApi.serviceDetail({}, true).then(res => {
            self.form = res.data;
          if(res.data.storageVo.hwCloud == null){
            self.form.storageVo.hwCloud = self.hw;
          }
            self.loading = false;
          })
          .catch(error => {
            self.loading = false;
          });

      },
      /*提交*/
      onSubmit() {
        let self = this;
        let params = this.form;
        self.$refs.form.validate((valid) => {
          if (valid) {
            self.loading = true;
            SettingApi.editService(params, true)
              .then(data => {
                self.loading = false;
                ElMessage({
                  message: '恭喜你，设置成功',
                  type: 'success'
                });
                self.$router.push('/setting/Index');
              })
              .catch(error => {
                self.loading = false;
              });
          }
        });
      },
    }
  };
</script>
<style>
  .tips {
    color: #ccc;
  }
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }
  input[type="number"]{
    -moz-appearance: textfield;
  }
  .button-wrapper{
    display: flex;
  }
</style>
