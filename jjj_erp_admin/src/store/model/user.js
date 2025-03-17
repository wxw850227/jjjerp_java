import {
	defineStore
} from 'pinia';
import {
	setCookie,
	getCookie,
	delCookie
} from '@/utils/base.js';
import {
	setStorage,
	getStorage
} from '@/utils/storageData';
import AuthApi from '@/api/auth.js';
import configObj from "@/config";
let {
	strongToken,
	renderMenu,
	menu,
	tokenName
} = configObj;
import {
	handRouterTable,
	handMenuData
} from '@/utils/router';
export const useUserStore = defineStore('main', {
	state: () => {
		return {
			token: getStorage(strongToken),
			userInfo: getStorage('userInfo'),
			list: {},
			menus: getStorage(menu),
			renderMenus: getStorage(renderMenu),
			historyMenus: []
		};
	},
	getters: {},
	actions: {
		setHistoryMenus(obj) {
			let self = this;
			let falg = self.historyMenus.find(item => item.path === obj.path);
			if (!falg && obj.path != '/home') {
				self.historyMenus.push(obj);
			}

		},
		delHistory(n) {
			let self = this;
			let url = '/'
			self.historyMenus.splice(n, 1);
			if (self.historyMenus.length > 0) {
				if (n == 0) {
					url = self.historyMenus[0].path;
				} else {
					url = self.historyMenus[n - 1].path;
				}
			}
			return url

		},
		bus_on(name, fn) {
			let self = this;
			self.list[name] = self.list[name] || [];
			self.list[name].push(fn);
		},
		// 发布
		bus_emit(name, data) {
			let self = this;
			if (self.list[name]) {
				self.list[name].forEach((fn) => {
					fn(data);
				});
			}
		},
		// 取消订阅
		bus_off(name) {
			let self = this;
			if (self.list[name]) {
				delete self.list[name];
			}
		},
		/**
		 * @description 登录
		 * @param {*} token
		 */
		async afterLogin(info) {
			this.userInfo = this.userInfo || {};
			const {
				data: {
					token,
					loginShopUserVo: {
						appId,
						isSuper,
						adminUserId,
						userName
					}
				}
			} = info;
			this.token = token;
			const {
				data
			} = await AuthApi.getRoleList({});
			const {
				data: {
					version,
					shop_name
				}
			} = await AuthApi.getUserInfo({});
			let renderMenusList = handMenuData(JSON.parse(JSON.stringify(data)));
			let menusList = handRouterTable(JSON.parse(JSON.stringify(data)));
			setStorage(JSON.stringify(menusList), menu);
			setStorage(JSON.stringify(renderMenusList), renderMenu);
			this.userInfo.version = version;
			this.userInfo.shop_name = shop_name;
			this.userInfo.userName = userName;
			this.userInfo.adminUserId = adminUserId;
			this.userInfo.isSuper = isSuper;
			this.userInfo.AppID = appId;
			this.renderMenus = renderMenusList;
			this.menus = menusList;
			setStorage(JSON.stringify(token), strongToken);
			setStorage(JSON.stringify(this.userInfo), 'userInfo');
		},
		/**
		 * @description 退出登录
		 * @param {*} token
		 */
		afterLogout() {
			sessionStorage.clear();
			// deleteSessionStorage(null);
			// delCookie(strongToken,null);
			this.token = null;
			this.menus = null;
			this.historyMenus = [];
		},
	}
});
export default useUserStore;