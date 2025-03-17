<template>
  <div class="left-menu-wrapper">
    <!--主菜单-->
    <div class="menu-wrapper">
      <div class="menu-logo d-c d-c-c">
        <div>
          <el-image
            :src="logo1"
            style="width: 39px; height: 28px; object-fit: cover"
          >
          </el-image>
        </div>
        <div class="home-login home-icon">
          <div
            :class="active_menu != null ? '' : 'home-icon'"
            @click="choseMenu(1, null, null)"
          >
            <div class="item-box1">
              <div class="imgspan">
                <span
                  class="name icon iconfont icon-biaoqiankuozhan_shouye-146"
                ></span>
              </div>
              <span class="iconname1">首页</span>
            </div>
          </div>
        </div>
      </div>
      <div class="nav-wrapper mb10">
        <div class="first-menu-content" ref="myElement">
          <div class="nav-ul rightmenu-list">
            <div
              v-for="(item, index) in menuList"
              :key="index"
              class="menuBox"
              :class="'menuBox-' + index"
            >
              <div
                :class="
                  firstMenu(item) ? 'menu-item router-link-active' : 'menu-item'
                "
                @click="choseMenu(2, item, index)"
                v-if="item.isMenu == 1"
              >
                <div class="item-box">
                  <span
                    :class="'icon iconfont menu-item-icon ' + item.icon"
                  ></span>
                  <span class="iconname">{{ item.name }}</span>
                </div>
              </div>
              <div class="d-b-c child-menu-wrapper-box">
                <div class="menu-leftsanjian-box">
                  <div class="menu-leftsanjian"></div>
                </div>
                <div class="child-menu right-animation">
                  <div
                    class="mb10"
                    v-for="(menuitem, menuindex) in item['children']"
                    :key="menuindex"
                  >
                    <div class="secondary-title d-s-c mb10">
                      <div class="secondary-title-icon">
                        <span
                          :class="
                            'icon iconfont menu-item-icon ' + menuitem.icon
                          "
                        ></span>
                      </div>
                      <span class="f14 theme-font">{{ menuitem.name }}</span>
                    </div>
                    <template
                      v-for="(citem, cindex) in menuitem['children']"
                      :key="cindex"
                    >
                      <div
                        :class="
                          secondaryMenu(citem)
                            ? 'router-link router-link-active'
                            : 'router-link'
                        "
                        @click="choseMenu(3, citem, cindex)"
                        v-if="citem.isMenu == 1"
                      >
                        <span class="name">{{ citem.name }}</span>
                      </div>
                    </template>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthApi from "@/api/auth.js";
import logo1 from "/src/assets/img/logo2.png";
import { useUserStore } from "@/store";
import { reactive, toRefs, defineComponent, nextTick, watchEffect } from "vue";
import { useRoute } from "vue-router";
export default defineComponent({
  components: {},
  setup(props, { emit }) {
    const { userInfo, bus_emit, menus, renderMenus } = useUserStore();
    const route = useRoute();
    const state = reactive({
      route,
      /*传到顶部的标题*/
      munu_name: "首页",
      /*选中的菜单*/
      active_menu: null,
      /*子菜单选择*/
      active_child: 0,
      /*菜单数据*/
      menuList: renderMenus,
      /*商城名称*/
      shop_name: "",
      menus,
      hoverMenu: null,
      logo1: logo1,
    });
    /*菜单*/
    var selectMenu = (to) => {
      let menupath = to.path;
      let active_menu = null;
      let active_child = null;
      if (state.menuList && state.menuList.length > 0) {
        for (let i = 0; i < state.menuList.length; i++) {
          if (state.menuList[i].path == menupath) {
            active_menu = i;
            break;
          }

          if (
            state.menuList[i].children &&
            state.menuList[i].children.length > 0
          ) {
            for (let j = 0; j < state.menuList[i].children.length; j++) {
              if (state.menuList[i].children[j].path == menupath) {
                active_menu = i;
                active_child = j;
                break;
              }
            }
          }

          if (!active_menu && !active_child) {
            if (state.menuList[i].childrenList.includes(menupath)) {
              active_menu = i;
              break;
            }
          }
        }
      }

      state.active_menu = active_menu;
      state.active_child = active_child;

      bus_emit("MenuName", (to.meta && to.meta.showMenuTitle) || to.meta.title);
      emit("selectMenu", active_menu);
    };
    // selectMenu(route);
    // watchEffect(() => {
    //   selectMenu(route);
    // });
    return {
      ...toRefs(state),
      userInfo,
      selectMenu,
      bus_emit,
    };
  },
  beforeRouteUpdate(to, from, next) {
    this.selectMenu(to);
    next();
  },
  mounted() {
    this.$nextTick(() => {
      this.getUserInfo();
      this.selectMenu(this.$route);
    });
  },
  methods: {
    firstMenu(e) {
      let routeList = e.childrenList;
      let path = useRoute().path;
      return routeList.indexOf(path) != -1;
    },
    secondaryMenu(e) {
      let routeList = e.childrenList;
      let path = useRoute().path;
      return routeList.indexOf(path) != -1;
    },
    getUserInfo() {
      let self = this;
      AuthApi.getUserInfo({})
        .then((res) => {
          const { data } = res;
          self.shop_name = data.shop_name;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
    /*点击菜单跳转*/
    choseMenu(type, item, index) {
      if (type == 1) {
        this.active_menu = null;
        this.active_child = null;
        this.$router.push("/");
        this.$emit("selectMenu", null);
      } else if (type == 2) {
        this.active_menu = index;
        this.active_child = 0;
        if (item.childrenList.includes(item.redirectName)) {
          this.$router.push(item.redirectName);
        } else {
          this.$router.push(item.childrenList[0]);
        }
        // this.$router.push(item.redirectName);
        if (item.children) {
          this.$emit("selectMenu", false);
        }
      } else if (type == 3) {
        let path = item.path;
        if (item.redirectName) {
          path = item.redirectName;
        }
        if (this.active_menu == null) {
          for (let i = 0; i < this.menuList.length; i++) {
            if (this.menuList[i].childrenList.includes(item.path)) {
              this.active_menu = i;
              break;
            }
          }
        }
        this.active_child = index;
        this.$router.push(path);
      }
    },
  },
});
</script>
<style lang="scss">
.home-login .icon-tubiaozhizuomoban- {
  color: #3a8ee6;
  font-size: 20px;
 
}
.name.icon {
  font-size: 20px;
  
}
.menu-item-icon.icon.iconfont {
  font-size: 20px;
  
}

.menu-item .item-box {
  display: flex;
  flex-direction: column;
  width: 74px;
  height: 58px;
}
.item-box1 {
  display: flex;
  cursor: pointer;
  flex-direction: column;
  height: 58px;
  margin-top: 56px;
  margin-bottom: 20px;
}
.iconname1 {
  font-weight: 400;
  font-size: 12px;
  color: #333333;
  line-height: 10px;
  text-align: center;
}
.imgspan {
  display: inline-block;
  font-weight: normal;
  width: 74px;
  margin-top: 4px;
  // height: 58px;
  text-align: center;
  .iconfont {
    font-size: 20px;
  }
}

.iconname {
  font-weight: 400;
  font-size: 12px;
  color: #333333;
  margin-top: -33px;
  text-align: center;
}
</style>
