<template>
	<div class="product-list bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="规格名称"><el-input size="small" class="form_content_width" clearable
						v-model="searchForm.attributeName" placeholder="请输入规格名称"></el-input></el-form-item>
				<el-form-item><el-button size="small" type="primary" @click="onSubmit">查询</el-button></el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				<el-button size="small" type="primary" icon="Plus" @click="addClicks"
					v-auth="'/product/base/attribute/add'">新增</el-button>
			</div>
			<!-- 分页 -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--表格-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%" v-loading="loading">
			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="id" label="ID" width="55"></el-table-column>
			<el-table-column fixed="right" label="操作" width="120">
				<template #default="scope">
					<div style="white-space: nowrap" class="d-c-c">
						<el-button @click="editClick(scope.row)" type="primary" link size="small">编辑</el-button>
						<el-button @click="cancelClick(scope.row)" type="primary" link size="small">删除</el-button>
						
						<!-- <el-dropdown size="small">
							<el-button type="primary" link size="small">更多<el-icon>
									<ArrowDown />
								</el-icon></el-button>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="cancelClick(scope.row)">删除</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown> -->
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="attributeName" label="规格名称"></el-table-column>
			<el-table-column prop="attributeValue" label="规格值"> </el-table-column>
			<el-table-column prop="createTime" label="创建时间"></el-table-column>
			<el-table-column prop="updateTime" label="更新时间"></el-table-column>
		</el-table>
		<Add v-if="open_add" :open_add="open_add" @closeDialog="closeDialogFunc($event, 'add')"></Add>
		<Edit v-if="open_edit" :open_edit="open_edit" :form="categoryModel"
			@closeDialog="closeDialogFunc($event, 'edit')"></Edit>
	</div>
</template>
<script>
	import ProductApi from "@/api/product.js";
	import qs from "qs";
	import {
		useUserStore
	} from "@/store";
	import Add from "./add.vue";
	import Edit from "./Edit.vue";
	const {
		token
	} = useUserStore();

	export default {
		components: {
			Add,
			Edit,
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
				/*当前是第几页*/
				curPage: 1,
				/*横向表单数据模型*/
				searchForm: {},
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
				/*是否打开编辑弹窗*/
				open_edit: false,
				open_add: false,
				/*当前编辑的对象*/
				order_no: "",
				categoryModel: {},
			};
		},
		created() {
			/*获取列表*/
			this.getData();
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
			addClicks() {
				this.open_add = true;
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

			/*获取列表*/
			getData() {
				let self = this;
				let Params = this.searchForm;
				Params.dataType = self.activeName;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				self.loading = true;
				ProductApi.attributeList(Params, true)
					.then((res) => {
						self.tableData = res.data.attribute.records;
						self.totalDataNumber = res.data.attribute.total;
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
						ProductApi.attributeDelete({
									id: row.id,
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
			/*关闭弹窗*/
			closeDialogFunc(e, f) {
				if (f == "add") {
					this.open_add = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "edit") {
					this.open_edit = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
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

			/*选择图片之前*/
			onBeforeUploadImage(file) {
				var fileType = file.name.substring(file.name.lastIndexOf(".") + 1);
				const isEXCEL = fileType === "xls";
				const isLt10M = file.size / 1024 / 1024 < 10;
				if (!isEXCEL) {
					ElMessage.error("上传文件只能是xls格式!");
				}
				if (!isLt10M) {
					ElMessage.error("上传文件大小不能超过 10MB!");
				}
				return isEXCEL && isLt10M;
			},

			/*上传图片*/
			// UploadImage(param) {
			//   let self = this;
			//   const loading = ElLoading.service({
			//     lock: true,
			//     text: "正在处理,请等待",
			//     spinner: "el-icon-loading",
			//     background: "rgba(0, 0, 0, 0.7)",
			//   });
			//   const formData = new FormData();
			//   formData.append("iFile", param.file);
			//   OrderApi.batchDelivery(formData)
			//     .then((response) => {
			//       loading.close();
			//       self.dialogBatchDelivery = false;
			//       ElMessage({
			//         message: response.msg,
			//         type: "warning",
			//       });
			//     })
			//     .catch((response) => {
			//       loading.close();
			//       ElMessage({
			//         message: "本次处理失败",
			//         type: "warning",
			//       });
			//       param.onError();
			//     });
			// },
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