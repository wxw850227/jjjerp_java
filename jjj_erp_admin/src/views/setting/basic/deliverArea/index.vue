<template>
	<div class="product-list bgset set-main">
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClicks"
					v-auth="'/setting/basic/deliverArea/add'">添加配送区域</el-button>
			</div>
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%"
			@selection-change="handleSelectionChange" v-loading="loading">
			<el-table-column prop="deliverAreaId" label="ID" width="55"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<el-button @click="editAreaClick(scope.row)" type="primary" link size="small">编辑</el-button>
					<el-button @click="cancelClick(scope.row)" type="primary" link size="small">
						删除
					</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="区域名称"></el-table-column>
			<el-table-column prop="provinceNames" label="覆盖地区">
			</el-table-column>
		</el-table>
		<Areas v-if="show_area" :show_area="show_area" :areaModel="areaModel" :areas="options"
			:filteredOptions="filteredOptions" @closeArea="closeAreaFunc">
		</Areas>
	</div>
</template>
<script>
	import SettingApi from "@/api/setting.js";
	import qs from "qs";
	import Areas from "./Area.vue";
	import {
		useUserStore
	} from "@/store";

	const {
		token
	} = useUserStore();

	export default {
		components: {
			Areas,
		},
		data() {
			return {
				token,
				/*切换菜单*/
				activeName: "all",
				/*是否加载完成*/
				loading: true,
				/*列表数据*/
				tableData: [],
				/*一页多少条*/
				pageSize: 10,
				/*一共多少条数据*/
				totalDataNumber: 0,
				areaModel: {
					index: 0,
				},
				/*当前是第几页*/
				curPage: 1,
				/*横向表单数据模型*/
				searchForm: {
					orderNo: "",
					style_id: "",
					store_id: "",
					createTime: "",
				},
				/*配送方式*/
				exStyle: [],
				/*门店列表*/
				shopList: [],
				/*时间*/
				create_time: "",
				/*统计*/
				order_count: {
					payment: 0,
					delivery: 0,
					received: 0,
				},
				dialogBatchDelivery: false,
				/*显示区域选中*/
				show_area: false,
				/*是否打开编辑弹窗*/
				open_edit: false,
				open_add: false,
				/*当前编辑的对象*/
				order_no: "",
				categoryModel: {},
				cityCount: "",
				/*区域数据源*/
				options: [],
				areaLists: [],
				/*区域数据源*/
				optionsMap: {},
			};
		},
		computed: {
			filteredOptions() {
				return this.options.filter(
					(option) => !this.inArray(option.id, this.cityCount)
				);
			},
		},
		created() {
			/*获取列表*/
			this.getData();
			this.getAreaData();
		},
		methods: {
			/*跨多列*/
			arraySpanMethod(row) {
				if (row.rowIndex % 2 == 0) {
					if (row.columnIndex === 0) {
						return [1, 8];
					}
				}
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.getData();
			},
			inArray: function(val, array) {
				return array.indexOf(val) > -1;
			},
			addClicks() {
				//   this.areaModel.index = this.form.rule.length;
				this.areaModel.type = "add";
				this.show_area = true;
			},
			/*编辑区域*/
			editAreaClick(e) {
				this.areaModel = e;
				this.areaModel.type = "edit";
				this.show_area = true;
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getData();
			},

			/*切换菜单*/
			handleClick(tab) {
				let self = this;
				self.curPage = 1;
				self.activeName = tab;
				self.getData();
			},
			getAreaData() {
				let self = this;
				SettingApi.deliverAreaToAdd({}, true)
					.then((res) => {
						self.options = res.data.provinceList;
						self.cityCount = res.data.allAddIdList;
						console.log("self.cityCount", self.cityCount);
						//区域转数组
						self.options.forEach(function(area) {
							self.optionsMap[area.id] = area;
							if (self.tableData.provinceIds.includes(province.id)) {
								province.checked = true;
							}
						});
						console.log("self.options", self.options);
					})
					.catch((error) => {
						console.log(error);
					});
			},
			/*获取列表*/
			getData() {
				let self = this;
				let search = this.searchForm;
				let Params = {};
				Params.dataType = self.activeName;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				Params.orderNo = search.orderNo;
				Params.deliveryType = search.style_id;
				self.loading = true;
				SettingApi.deliverAreaList(Params, true)
					.then((res) => {
						self.tableData = res.data;
						self.totalDataNumber = res.data.total;
						console.log(self.tableData, "tableData");
						self.loading = false;
					})
					.catch((error) => {});
			},
			/*删除*/
			cancelClick(row) {
				let self = this;
				ElMessageBox.confirm("此操作将永久删除该记录, 是否继续?", "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
					.then(() => {
						self.loading = true;
						SettingApi.deliverAreaDel({
									deliverAreaId: row.deliverAreaId,
								},
								true
							)
							.then((data) => {
								self.loading = false;
								if (data.code == 1) {
									ElMessage({
										message: data.msg,
										type: "success",
									});
									self.getData();
								} else {
									ElMessage.error("错了哦，这是一条错误消息");
								}
							})
							.catch((error) => {
								self.loading = false;
							});
					})
					.catch(() => {});
			},
			/*关闭区域选中弹窗*/
			closeAreaFunc(e) {
				let self = this;
				this.show_area = e.statu;
				this.getData();
			},

			/*打开添加*/
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
			},

			/*搜索查询*/
			onSubmit() {
				this.curPage = 1;
				this.tableData = [];
				this.getData();
			},
		},
	};
</script>
<style lang="scss">
	.product-info {
		padding: 10px 0;
		border-top: 1px solid #eeeeee;
	}

	.order-code .state-text {
		padding: 2px 4px;
		border-radius: 4px;
		background: #808080;
		color: #ffffff;
		margin-right: 6px;
	}

	.order-code .state-text-red {
		background: red;
	}

	.table-wrap .product-info:first-of-type {
		border-top: none;
	}

	.table-wrap .el-table__body tbody .el-table__row:nth-child(odd) {
		background: #f5f7fa;
	}

	::v-deep .pagination {
		text-align: right;
		margin-top: 18px;
	}
</style>