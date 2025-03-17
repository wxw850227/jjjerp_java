<template>
  <div class="common-header-box">
    <div class="common-header">
      <div class="breadcrumb">
        <!--一般的标题显示-->
        <div class="baseInfo-left-base d-s-c">
          <div class="home-btn">
            <!-- <span
              class="mr10 icon iconfont icon-biaoqiankuozhan_shouye-146"
            ></span> -->
            <span class="mr20">首页</span>
          </div>
        </div>
        <div class="header-navbar">
          <div class="header-navbar-icon">
            <span class="text">当前版本：{{ userInfo.version }}</span>
          </div>
          <div class="header-navbar-icon">
            <span class="ml4 icon iconfont icon-guanliyuan"></span>
            <span class="text ml4">{{ userInfo.userName }}，欢迎您！</span>
          </div>
          <div class="header-navbar-icon"><span class="">|</span></div>
          <div class="header-navbar-icon" @click="passwordFunc()">
            <span class="text">修改密码</span>
          </div>
          <div class="header-navbar-icon login-out" @click="exit()">
            <span class="icon iconfont icon-tuichu"></span>
            <span class="text ml4">退出</span>
          </div>
        </div>
      </div>
      <!--修改密码-->
      <UpdatePassword v-if="is_password" @close="closeFunc"></UpdatePassword>
    </div>
    <el-tabs
      v-model="activeName"
      type="card"
      closable
      class="head-tabs"
      @edit="handleTabsEdit"
      @tab-click="handleClick"
    >
      <el-tab-pane
        v-for="item in editableTabs()"
        :key="item.path"
        :label="item.name"
        :name="item.path"
      ></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  reactive,
  toRefs,
  defineComponent,
  onBeforeUnmount,
  ref,
  watch,
} from "vue";
import { useUserStore } from "@/store";
import { useRouter, useRoute } from "vue-router";
import UpdatePassword from "./part/UpdatePassword.vue";
export default defineComponent({
  components: {
    UpdatePassword,
  },
  setup() {
    const router = useRouter();
    const route = useRoute();
    const currentPath = ref(route.path);
    const { userInfo, bus_on, bus_emit, bus_off, afterLogout } = useUserStore();
    const state = reactive({
      /*菜单名称*/
      menu_title: "菜单",
      /*切换菜单*/
      tabList: [],
      /*切换选中*/
      activeValue: 0,
      /*是否修改密码*/
      is_password: false,
      /*tab切换类别*/
      tab_type: "",
      activeName: useRoute().path,
    });
    watch(
      () => route.path,
      (newPath, oldPath) => {
        currentPath.value = newPath;
        state.activeName = newPath;
      },
      {
        immediate: true,
      }
    );
    bus_on("MenuName", (res) => {
      state.menu_title = res;
    });
    bus_on("tabData", (res) => {
      state.tabList = res.list;
      state.activeValue = res.active;
      state.tab_type = res.tab_type;
    });
    bus_on("activeValue", (res) => {
      state.activeValue = res;
    });
    bus_on("noTarget", (res) => {
      state.activeValue = res;
    });
    //发送给其它组件头部加载完成
    bus_emit("headLoad", true);
    onBeforeUnmount(() => {
      bus_off("menuName");
      bus_off("tabData");
    });
    return {
      ...toRefs(state),
      userInfo,
      afterLogout,
      router,
      bus_emit,
    };
  },
  methods: {
    handleClick(tab, event) {
      this.$router.push({
        path: tab.paneName,
      });
    },
    handleTabsEdit(targetName, action) {
      let list = this.editableTabs();
      let n = list.findIndex((item) => item.path === targetName);
      const { delHistory } = useUserStore();
      let url = delHistory(n);
      this.$router.push({ path: url });
    },
    isSelect(e) {
      let path = useRoute().path;
      return path == e;
    },
    editableTabs() {
      const { historyMenus } = useUserStore();
      return historyMenus;
    },
    /*点击菜单跳转*/
    gotoPage(path) {
      this.$router.push(path);
    },
    exit() {
      ElMessageBox.confirm("此操作将退出登录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.logout();
          localStorage.removeItem("rememberLogin");
        })
        .catch(() => {
          ElMessage({
            type: "info",
            message: "已取消退出",
          });
        });
    },
    async logout() {
      await this.afterLogout();
      this.router.push("/login");
    },

    /*点击跳转*/
    tabClick(event) {
      let e = event.props;
      /* 提货码 */
      if (this.tab_type == "card") {
        this.router.push({
          path: "/plus/card/event",
          query: {
            type: e.name,
          },
        });
      }
      /*分销*/
      if (this.tab_type == "agent") {
        this.router.push({
          path: "/plus/agent/index",
          query: {
            type: e.name,
          },
        });
      }
      /*优惠券*/
      if (this.tab_type == "coupon") {
        this.router.push({
          path: "/plus/coupon/index",
          query: {
            type: e.name,
          },
        });
      }
      /*积分商城*/
      if (this.tab_type == "points") {
        this.router.push({
          path: "/plus/points/index",
          query: {
            type: e.name,
          },
        });
      }
      /* 现时秒杀*/
      if (this.tab_type == "seckill") {
        this.router.push({
          path: "/plus/seckill/index",
          query: {
            type: e.name,
          },
        });
      }
      /* 限时拼团*/
      if (this.tab_type == "assemble") {
        this.router.push({
          path: "/plus/assemble/index",
          query: {
            type: e.name,
          },
        });
      }
      /* 砍价*/
      if (this.tab_type == "bargain") {
        this.router.push({
          path: "/plus/bargain/index",
          query: {
            type: e.name,
          },
        });
      }
      /*门店*/
      if (this.tab_type == "store") {
        this.router.push({
          path: "/store/index",
          query: {
            type: e.name,
          },
        });
      }
      /*app设置*/
      if (this.tab_type == "appopen") {
        this.router.push({
          path: "/appsetting/appopen/event",
          query: {
            type: e.name,
          },
        });
      }
      this.activeValue = e.name;
      this.bus_emit("activeValue", e.name);
    },

    /*修改密码*/
    passwordFunc() {
      this.is_password = true;
    },

    /*关闭修改密码*/
    closeFunc() {
      this.is_password = false;
    },
  },
});
</script>

<style lang="scss" scoped>
.home-btn {
  //cursor: pointer;
}

.top-menu-list {
  height: 50px;
  display: flex;
  justify-content: flex-start;
  align-items: center;

  .top-menu-item {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 12px;
    position: relative;

    .el-icon {
      font-size: 14px;
      margin-left: 8px;
    }
  }

  .top-menu-item::after {
    content: "";
    position: absolute;
    top: 0;
    bottom: 0;
    margin: auto;
    left: 1px;
    width: 1px;
    height: 16px;
    background-color: rgba(153, 153, 153, 1);
  }

  .top-menu-item:hover::after,
  .top-menu-item.active::after {
    width: 0;
  }

  .top-menu-item.active,
  .top-menu-item:hover {
    background-color: rgba(16, 16, 16, 0.05);
  }
}

.common-header .el-tabs__nav-wrap::after {
  display: none;
}

.common-header .el-tabs-container {
  margin-left: 20px;
  padding-left: 20px;
  border-left: 1px solid #eeeeee;
}

.common-header .el-tabs__header {
  margin-bottom: 0;
}

.login-out .icon-tuichu {
  color: #999;
  font-size: 18px;
}

.header-navbar-icon .icon-guanliyuan {
  font-size: 18px;
  color: #999;
}
.icon-biaoqiankuozhan_shouye-146 {
  font-size: 20px;
  color: #333;
}

.common-header-box .head-tabs {
  background-color: #fff;
}

.common-header-box .head-tabs::v-deep {
  .el-tabs__header {
    height: 34px;
    margin: 0;
    // border-top: 1px solid var(--el-border-color-light);
	border-top: 1px solid #eee;
  }

  .el-tabs__header .el-tabs__item .is-icon-close {
    width: 14px;
  }

  .el-tabs__nav {
    border-top: none;
    border-bottom: none;
    border-radius: 0;
  }

  .el-tabs__item {
    height: 32px;
    line-height: 32px;
    border-bottom: none;
  }

  .el-tabs__nav-next,
  .el-tabs__nav-prev {
  }
}
</style>
