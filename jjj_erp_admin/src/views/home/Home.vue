<template>
	<div class="d-b-s page-mine-box">
		<!-- 左边三个卡片 -->
		<div class="flex-1 mr14">
			<div>
				<el-card shadow="never" :padding="0" class="ivu-mb dashboard-console-visit">
					<p slot="header">
						<span class="common-form">常用应用</span>
					</p>
					<div class="common-apps">
						<div v-for="app in commonApps" :key="app.name" @click="handleAppClick(app.link)"
							style="cursor: pointer">
							<div class="app">
								<el-image :src="app.img" size="large" style="border-radius: 15px"></el-image>
								<div class="app-name">{{ app.name }}</div>
							</div>
						</div>
					</div>
				</el-card>
				<el-card class="ivu-mb dashboard-console-visit" shadow="never" :padding="0">
					<p slot="header">
						<span class="common-form">数据概况</span>
						<span style="float: right">
							<el-text style="margin-right: 10px; color: #999999">{{
                currentTime
              }}</el-text>
						</span>
					</p>
					<div>
						<div class="d-b-c d-r">
							<div class="d-b-c d-r">
								<div class="mb18 ml10 mr10">
									<el-image :src="image1" :size="50" style="
                      color: var(--prev-color-primary);
                      border-radius: 15px;
                    "></el-image>
								</div>
								<div class="ml20">
									<div class="number">支付笔数</div>
									<div class="" style="
                      height: 52px;
                      text-align: center;
                      font-weight: bold;
                      font-size: 16px;
                    ">
										<span>{{ sketch.depositNum }}</span>
									</div>
								</div>
								<div class="ml50">
									<div class="number1">支付金额(元)</div>
									<div class="" style="
                      height: 52px;
                      text-align: center;
                      font-weight: bold;
                      font-size: 16px;
                    ">
										<span>{{ sketch.depositMoney }}</span>
									</div>
								</div>
								<div class="ml50">
									<div class="number1">支付客户数</div>
									<div class="" style="
                      height: 52px;
                      text-align: center;
                      font-weight: bold;
                      font-size: 16px;
                    ">
										<span>{{ sketch.customerNum }}</span>
									</div>
								</div>
							</div>
							<div class="d-b-c d-r">
								<div class="mb18 ml10 mr10">
									<el-image :src="image2" :size="50" style="
                      color: var(--prev-color-primary);
                      border-radius: 15px;
                    "></el-image>
								</div>
								<div class="ml20">
									<div class="number">平均客单价(元)</div>
									<div class="" style="
                      height: 52px;
                      text-align: center;
                      font-weight: bold;
                      font-size: 16px;
                    ">
										<span>{{ sketch.customerMoney }}</span>
									</div>
								</div>
								<div class="ml50">
									<div class="number1">发货笔数</div>
									<div class="" style="
                      height: 52px;
                      text-align: center;
                      font-weight: bold;
                      font-size: 16px;
                    ">
										<span>{{ sketch.deliverNum }}</span>
									</div>
								</div>
								<div class="ml50">
									<div class="number1">发货金额(元)</div>
									<div class="" style="
                      height: 52px;
                      text-align: center;
                      font-weight: bold;
                      font-size: 16px;
                    ">
										<span>{{ sketch.deliverMoney }}</span>
									</div>
								</div>
							</div>
							<div></div>
						</div>
						<el-divider />
					</div>
					<div>
						<div>
							<p slot="header">
								<span style="font-weight: bold; font-size: 15px; color: #333333">销售概况</span>
							</p>
							<div class="d-b-c d-r ml10 mr30 mt20">
								<span class="d-c-c d-c"><text class="text1">待审核</text><text
										class="text2">{{ backlog.daiShenHe || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">待发货</text><text
										class="text2">{{ backlog.daiFaHuo || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">待配货</text><text
										class="text2">{{ backlog.daiPeiHuo || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">待处理售后</text><text class="text2">{{
                    backlog.daiShouHou || 0
                  }}</text></span>

								<span class="d-c-c d-c"><text class="text1">异常订单</text><text
										class="text3">{{ backlog.yiChang || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">退货退款</text><text
										class="text4">{{ backlog.tuiHuo || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">仅退款</text><text
										class="text5">{{ backlog.tuiKuan || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">换货</text><text
										class="text3">{{ backlog.huanHuo || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">补发</text><text
										class="text4">{{ backlog.buFa || 0 }}</text></span>
								<span class="d-c-c d-c"><text class="text1">其他</text><text
										class="text5">{{ backlog.qiTa || 0 }}</text></span>
							</div>
						</div>
					</div>
				</el-card>
				<el-card class="ivu-mb dashboard-console-visit" shadow="never" :padding="0">
					<p slot="header">
						<span class="common-form">订单趋势</span>
					</p>
					<PieBox v-if="!loading"></PieBox>
				</el-card>
			</div>
		</div>

		<!-- 右边两个卡片 -->
		<div>
			<el-card :bordered="false" shadow="never" class="dashboard-console-visit mb12">
				<p slot="header">
					<span class="common-form">产品手册</span>
					<span style="float: right; margin-top: 10px">
						<text style="color: #3b64fc">更多</text>
					</span>
				</p>
				<div>
					<ul>
						<li v-for="(item, index) in products" :key="index" class="product-item">
							<div class="square"></div>
							<a :href="item.link" class="product-link" target="_blank">{{
                item.name
              }}</a>
							<span class="product-date">{{ item.date }}</span>
						</li>
					</ul>
				</div>
			</el-card>
			<el-card shadow="never" :padding="0">
				<p slot="header">
					<span class="common-form">专属QQ客服</span>
				</p>
				<div class="d-b-s d-r">
					<span>3211941389(售前)</span>
					<span>970137749(售后)</span>
				</div>
			</el-card>
		</div>
	</div>
</template>

<script>
	import IndexApi from "@/api/index.js";
	import PieBox from "@/components/echarts/pieBox.vue";
	import image1 from "/src/assets/img/home_08.png";
	import image2 from "/src/assets/img/home_09.png";
	import image3 from "/src/assets/img/home_01.png";
	import image4 from "/src/assets/img/home_02.png";
	import image5 from "/src/assets/img/home_03.png";
	import image6 from "/src/assets/img/home_04.png";
	import image7 from "/src/assets/img/home_05.png";
	import image8 from "/src/assets/img/home_06.png";
	import image9 from "/src/assets/img/home_07.png";
	export default {
		name: "PieChartComponent",
		components: {
			PieBox,
		},
		data() {
			return {
				image1: image1,
				image2: image2,
				line: "line",
				circle: "circle",
				infoList: {},
				series: [],
				xData: [],
				y1Data: [],
				y2Data: [],
				lists: [],
				bing_data: [],
				bing_xdata: [],
				loading: false,
				loadings: false,
				sketch: {},
				backlog: {},
				currentTime: this.getCurrentTime(), // 当前时间
				commonApps: [{
						name: "采购订单",
						img: image3,
						link: "/purchase/purchase/order/index",
					},
					{
						name: "销售订单",
						img: image4,
						link: "/market/audit/order/index",
					},
					{
						name: "库存信息",
						img: image5,
						link: "/stock/condition/condition/index",
					},
					{
						name: "销售收款",
						img: image6,
						link: "/path1",
					},
					{
						name: "采购收款",
						img: image7,
						link: "/path1",
					},
					{
						name: "库存日志",
						img: image8,
						link: "/stock/allot/stockRecord/index",
					},
					{
						name: "商品信息",
						img: image9,
						link: "/product/base/product/index",
					},
				],
				products: [{
						name: "安装目录更新说明 ",
						link: "https://doc.jjjshop.net/ErpJava?category_id=0&document_id=1372",
						date: "06-24",
					},
					{
						name: "系统开发框架按照模块开发，admin、saas、job..",
						link: "https://doc.jjjshop.net/ErpJava?category_id=10042&document_id=1370",
						date: "06-23",
					},
					{
						name: "宝塔部署-nginx+mysql+redis使用宝塔安装",
						link: "https://doc.jjjshop.net/ErpJava?category_id=10042&document_id=1381",
						date: "06-15",
					},
					{
						name: "SAAS开发功能文档说明",
						link: "https://doc.jjjshop.net/ErpJava?category_id=10042&document_id=1388",
						date: "06-04",
					},
					{
						name: "下载常见问题报错",
						link: "https://doc.jjjshop.net/ErpJava?category_id=10042&document_id=1391",
						date: "06-03",
					},
				],
			};
		},
		created() {
			this.getData();
			this.updateTime();
		},
		methods: {
			handleAppClick(link) {
				this.$router.push(link);
			},
			getData() {
				let self = this;
				self.loading = true;
				IndexApi.getCount({}, true)
					.then((res) => {
						self.loading = false;
						console.log(res.data, "data");
						self.sketch = res.data.sketch;
						self.backlog = res.data.backlog;
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			getCurrentTime() {
				const now = new Date();
				return now.toLocaleString();
			},
			updateTime() {
				setInterval(() => {
					this.currentTime = this.getCurrentTime();
				}, 100);
			},
			refreshData() {
				this.getData();
			},
		},
	};
</script>

<style lang="scss">
	.mr14 {
		margin-right: 14px;
	}

	.text1 {
		font-weight: 400;
		font-size: 14px;
		color: #666666;
	}

	.text2 {
		font-weight: bold;
		font-size: 16px;
		color: #333333;
	}

	.text3 {
		font-weight: bold;
		font-size: 16px;
		color: #ee594c;
	}

	.text4 {
		font-weight: bold;
		font-size: 16px;
		color: #ffaf50;
	}

	.text5 {
		font-weight: bold;
		font-size: 16px;
		color: #ee594c;
	}

	.bdBox {
		width: 500px;
		min-height: 526px;
		white-space: nowrap;
	}

	.demo-progress .el-progress--line {
		margin-top: 50px;
		width: 200px;
	}

	.progress-animation-enter-active,
	.progress-animation-leave-active {
		transition: width 0.5s ease;
		/* 定义过渡效果 */
	}

	.progress-animation-enter,
	.progress-animation-leave-to

	/* .progress-animation-leave-active 以下版本生效 */
		{
		width: 0;
		/* 设置元素初始宽度为 0 */
	}

	.chart {
		width: 370px;
		margin: 10px;
	}

	.charts {
		width: 100%;
		margin: 10px;
	}

	.dashboard-console-visit {
		ul {
			li {
				list-style-type: none;
				margin-top: 12px;
				display: flex;
				align-items: center;
			}

			.product-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
			}

			.square {
				width: 5px;
				height: 5px;
				min-width: 5px;
				min-height: 5px;
				background-color: #999999;
				margin-right: 10px;
				flex-shrink: 0;
			}

			.product-link {
				color: #333333;
				flex-grow: 1;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.product-date {
				color: #999999;
				white-space: nowrap;
			}
		}
	}

	.card-title {
		display: flex;
		align-items: center;
	}

	.ivu-mb {
		margin-bottom: 14px;
	}

	.number {
		font-size: 14px;
		font-weight: bold;
		color: #333333;
		line-height: 40px;
		white-space: nowrap;
	}

	.number1 {
		white-space: nowrap;
		overflow-x: auto;
		font-size: 14px;
		font-weight: 400;
		color: #666666;
		line-height: 40px;
	}

	.iconColor {
		color: #f53f3f;
	}

	.iconColor.on {
		color: #0fc6c2;
	}

	.ivu-mr {
		display: inline-block;
		margin-left: 16px !important;
	}

	.ivu-text-right {
		text-align: right;
	}

	::v-deep(.el-card__header) {
		border-bottom: none !important;
		padding-bottom: 0;
	}

	.el-divider--horizontal {
		margin: 0 0 12px 0;
	}

	.total {
		font-size: 14px;
		font-weight: 400;
		color: #999999;
		line-height: 22px;
	}

	.common-apps {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
	}

	.app {
		text-align: center;
		flex-grow: 1;
		margin: 10px 40px;
	}

	.app-name {
		margin-top: 5px;
		font-size: 14px;
		color: #333;
		white-space: nowrap;
	}
</style>