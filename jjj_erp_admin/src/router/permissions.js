/**
 * @description 路由守卫，目前两种模式：all模式与intelligence模式
 */
import {
	useUserStore
} from '@/store';
import dealWithRoute from './dealWithRoute.js';
export async function setupPermissions(router) {
	const {
		bus_emit,
		setHistoryMenus
	} = useUserStore();
	let load = 0;
	router.beforeEach(async (to, from, next) => {
		const {
			historyMenus
		} = useUserStore();
		if(historyMenus && to.meta && to.meta.topTree){
			if (historyMenus.some(item => item.path === to.meta.topTree)) {
				to.meta.keepAlive = true;
			}
		}
		bus_emit('MenuName', to.meta && to.meta.title);
		const {
			token,
			menus
		} = useUserStore();
		const whiteList = ['/login'];
		if (!token) {
			if (whiteList.includes(to.path)) {
				next();
				return;
			}
			next('/login');
		} else {
			if (to.path == '/login') {
				next({
					path: '/home'
				});
				return;
			}
			if (to.meta.title && to.meta.topTree) {
				setHistoryMenus({
					name: to.meta.title,
					path: to.meta.topTree,
				})
			}
			if (menus && load == 0) {
				load++;
				dealWithRoute(menus);

				next({
					...to,
					replace: true
				});
				return;
			}
			next();
		}
	});

}