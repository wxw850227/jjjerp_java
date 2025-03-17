<template>
	<div>
		<el-row :gutter="10">
			<el-col :span="18" class="ivu-mb dashboard-console-visit">
				<el-card shadow="never" :padding="0">
					<p slot="header">
						<span class="f14 fb">作业看板</span>
						<span style="float: right">
							<el-text type="error" style="margin-right: 10px">{{
                currentTime
              }}</el-text>
							<el-icon @click="refreshData">
								<Refresh />
							</el-icon>
						</span>
					</p>

					<div>
						<div class="d-b-c d-r">
							<div class="d-b-c d-r">
								<div class="mb18 ml10 mr10">
									<el-avatar src="/static/imgs/home_10.png" :size="50" 
                    ></el-avatar>
								</div>
								<div class="mt20">
									<div class="number">￥{{ payScale.fuKuan }}</div>
									<div class="pt16" style="height: 42px">
										<span>今日已付款 {{ payScale.fuKuan }}单</span>
										<span class="ivu-mr"> </span>
									</div>
								</div>
							</div>
							<div class="d-b-c d-r">
								<div class="mb18 ml10 mr10">
									<el-avatar src="/static/imgs/home_11.png" :size="50" ></el-avatar>
								</div>
								<div class="mt20">
									<div>
										<span class="number">{{ payScale.yiFaHuo }}</span>单
									</div>
									<div class="pt16" style="height: 42px">
										<span>今日已发货 </span>
										<span class="ivu-mr">
											日环比 {{ Number(0) }}%
											<i class="iconColor" :class="[
                          Number(0) >= 0 ? ' ' : 'on',
                          Number(0) >= 0
                            ? 'el-icon-caret-top'
                            : 'el-icon-caret-bottom\n',
                        ]" />
										</span>
									</div>
								</div>
							</div>
							<div class="d-b-c d-c">
								<!-- <pie-box class="charts" v-if="!loading"> </pie-box> -->
								<div class="d-c-c d-r" style="white-space: nowrap" v-if="!loading" :key="percentage1">
									<transition name="progress-animation">
										<el-progress class="mb16" type="circle" :percentage="percentage1" width="80" />
									</transition>
									<span class="mb18 ml10">待审核 {{ payScale.daiShenHe }}</span>
								</div>
								<div class="d-c-c d-r" style="white-space: nowrap" v-if="!loading" :key="percentage2">
									<transition name="progress-animation">
										<el-progress width="80" class="mb16" type="circle" :percentage="percentage2" />
									</transition>
									<span class="mb18 ml10">配货中 {{ payScale.peiHuoZ }}</span>
								</div>
								<div class="d-c-c d-r" style="white-space: nowrap" v-if="!loading" :key="percentage3">
									<transition name="progress-animation">
										<el-progress width="80" class="mb16" type="circle" :percentage="percentage3" />
									</transition>
									<span class="mb18 ml10">异常 {{ payScale.yiChang }}</span>
								</div>
							</div>
							<div>
								<echarts-from class="chart" v-if="!loading"></echarts-from>
							</div>
						</div>

						<el-divider />
					</div>
					<p slot="header">
						<span class="f14 fb">交易趋势</span>
					</p>
					<div>
						<LineChart v-if="!loading"></LineChart>
					</div>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card :bordered="false" shadow="never" class="dashboard-console-visit">
					<p slot="header">
						<span class="f14 fb">库存看板<span class="gray9 f12 ml4">(本月)</span></span>
					</p>
					<div class="d-s-s d-c">
						<div class="d-s-s d-c mt20">
							<span class="f24">{{ Number(kucun.rate) * 100 }}%</span>
							<span class="f12 gray9">动销率</span>
						</div>
						<div class="demo-progress mb140">
							<div class="d-s-s d-r">
								<span class="mr10 mt50" style="white-space: nowrap">入库</span>
								<transition name="progress-animation">
									<el-progress :percentage="percentage4" />
								</transition>
							</div>
							<div class="d-s-s d-r">
								<span class="mr10 mt50" style="white-space: nowrap">出库</span>
								<transition name="progress-animation">
									<el-progress :percentage="percentage5" />
								</transition>
							</div>
						</div>
						<el-divider />
						<div class="bdBox">
							<Ranking></Ranking>
						</div>
					</div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import StatementApi from "@/api/statement.js";
	import echartsFrom from "@/components/echarts/index.vue";
	import LineChart from "@/components/echarts/lineChart.vue";
	import Ranking from "@/components/echarts/Ranking.vue";
	// import pieBox from "@/components/echarts/pieBox.vue";
	export default {
		name: "PieChartComponent",
		components: {
			echartsFrom,
			LineChart,
			Ranking,
			// pieBox,
		},
		data() {
			return {
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
				payScale: {},
				kucun: {},
				percentage1: 0,
				percentage2: 0,
				percentage3: 0,
				percentage4: 0,
				percentage5: 0,
				currentTime: this.getCurrentTime(), // 当前时间
			};
		},
		created() {
			this.getData();
			this.getkucun();
			this.updateTime();
		},
		methods: {
			getData() {
				let self = this;
				self.loading = true;
				StatementApi.getBoardJob({}, true)
					.then((res) => {
						self.loading = false;
						self.payScale = res.data;
						this.calculatePercentages();
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			getkucun() {
				let self = this;
				self.loading = true;
				StatementApi.getBoardStock({}, true)
					.then((res) => {
						self.loading = false;
						self.kucun = res.data;
						this.calculateKucun();
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			calculateKucun() {
				let total = this.kucun.rkNum + this.kucun.ckNum;
				this.percentage4 =
					total === 0 ? 0 : (this.kucun.rkNum / total).toFixed(1) * 100;
				this.percentage5 =
					total === 0 ? 0 : (this.kucun.ckNum / total).toFixed(1) * 100;
			},
			calculatePercentages() {
				let total =
					this.payScale.daiShenHe + this.payScale.peiHuoZ + this.payScale.yiChang;
				this.percentage1 =
					total === 0 ? 0 : (this.payScale.daiShenHe / total).toFixed(1) * 100;
				this.percentage2 =
					total === 0 ? 0 : (this.payScale.peiHuoZ / total).toFixed(1) * 100;
				this.percentage3 =
					total === 0 ? 0 : (this.payScale.yiChang / total).toFixed(1) * 100;
			},
			getCurrentTime() {
				const now = new Date();
				return now.toLocaleString();
			},
			updateTime() {
				setInterval(() => {
					this.currentTime = this.getCurrentTime();
				}, 1000);
			},
			refreshData() {
				this.getData();
			},
		},
	};
</script>

<style scoped lang="scss">
	.bdBox {
		width: 100%;
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
		margin-bottom: 10px;
		font-size: 32px;
		font-weight: 400;
		color: #333333;
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

	::v-deep .el-card__header {
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
</style>