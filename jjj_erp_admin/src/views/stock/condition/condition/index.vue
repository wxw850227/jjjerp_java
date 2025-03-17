<template>
	<div class="product bgset set-main">
		<!--搜索表单-->
		<div class="common-seach-wrap">
			<el-form size="small" :inline="true" :model="searchForm" class="demo-form-inline">
				<el-form-item label="商品">
					<el-input size="small" v-model="searchForm.name" placeholder="请输入商品编号/名称"></el-input>
				</el-form-item>
				<el-form-item label="供应商">
					<el-select v-model="searchForm.supplierId" placeholder="请选择">
						<el-option v-for="(item, index) in suppliers" :key="index" :label="item.supplierName"
							:value="item.supplierId" />
					</el-select>
				</el-form-item>
				<el-form-item label="仓库">
					<el-select size="default" v-model="searchForm.depotIdList" placeholder="请选择" multiple filterable
						clearable style="width: 285px" @change="handleSelectionChange">
						<template #header>
							<el-checkbox v-model="checkAll" :indeterminate="indeterminate"
								@change="handleCheckAllChange">
								全选
							</el-checkbox>
						</template>
						<el-option v-for="(item, index) in depotList" :key="index" :label="item.name"
							:value="item.depotId" />
					</el-select>
				</el-form-item>
				<el-form-item label="品牌">
					<el-select size="small" v-model="searchForm.expressId" placeholder="请选择">
						<el-option v-for="(item, index) in expressList" :key="index" :label="item.name"
							:value="item.id" />
					</el-select>
				</el-form-item>
				<el-form-item label="">
					<el-input size="small" v-model="searchForm.linkBillNum" placeholder="">
						<template #prepend>
							<el-select v-model="select" placeholder="Select" style="width: 115px">
								<el-option label="实际库存" :value="1" />
								<el-option label="可用库存" :value="2" />
							</el-select>
						</template>
					</el-input>
				</el-form-item>
				<!-- <span style="text-align: center; display: inline-block">~</span>
        <el-form-item label="">
          <el-input size="small" v-model="searchForm.linkBillNum"> </el-input>
        </el-form-item> -->
				<el-form-item>
					<el-button size="small" type="primary" @click="onSubmit">查询</el-button><el-button
						size="small">重置</el-button>
				</el-form-item>
			</el-form>
		</div>
		<div class="common-level-rail d-b-c">
			<div class="d-s-c flex-1">
				当前商品:共<el-text type="primary" size="large">{{
          totalDataNumber
        }}</el-text>件，总计：<el-text type="primary" size="large">￥4889721.00</el-text>
			</div>
			<!-- <div class="alltitle">

      </div> -->
			<el-pagination size="small" @size-change="handleSizeChange" @current-change="handleCurrentChange" background
				:page-sizes="[20, 50, 100]" :current-page="curPage" :page-size="pageSize"
				layout="total, prev, next, jumper,sizes" :total="totalDataNumber">
			</el-pagination>
		</div>
		<!--内容-->
		<el-table class="flex-1" size="small" :data="tableData" border style="width: 100%" v-loading="loading"
			show-summary :summary-method="getSummarys">
			<el-table-column prop="id" label="ID" width="52"></el-table-column>

			<el-table-column prop="imageUrl" label="图片" width="52">
				<template #default="{ row }">
					<div style="display: flex; align-items: center">
						<el-popover placement="right-start" trigger="hover" width="276">
							<template #reference>
								<el-avatar v-if="row.imageUrl" shape="square" :size="26" :src="row.imageUrl" />
								<el-avatar v-else :size="26" shape="square" src="/static/imgs/imagerror.png" />
							</template>
							<template #default>
								<el-avatar v-if="row.imageUrl" :size="250" shape="square" :src="row.imageUrl" />
								<el-avatar v-else :size="250" shape="square" src="/static/imgs/imagerror.png" />
							</template>
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="name" label="商品名称" width="100"  show-overflow-tooltip></el-table-column>

			<el-table-column prop="sku" label="规格" width="150"></el-table-column>
			<el-table-column prop="productParentCode" label="商品编码"></el-table-column>
			<el-table-column prop="productCode" label="规格编码"></el-table-column>
			<el-table-column label="库存数量">
				<el-table-column prop="totalStock" label="实际"></el-table-column>
				<el-table-column prop="freezeStock" label="锁定"></el-table-column>
				<el-table-column prop="stockNum" label="可用"></el-table-column>
			</el-table-column>
			<el-table-column label="库存流水">
				<template #default="scope">
					<el-button @click="editClick(scope.row)"  type="primary"
					  link size="small">查看</el-button>
				</template>
			</el-table-column>
			<el-table-column label="各仓库库存">
				<template #default="scope">
					<el-button @click="detailClick(scope.row)"  type="primary"
					  link size="small">仓库</el-button>
				</template>
			</el-table-column>
			<el-table-column label="规格条码" prop="barCode"></el-table-column>
			<el-table-column label="货号" prop="accountNum"></el-table-column>
			<el-table-column label="品牌" prop="mfrsName"></el-table-column>
			<el-table-column label="商品分类" prop="categoryName"></el-table-column>
			<el-table-column label="标准售价" prop="wholesaleDecimal"></el-table-column>
			<el-table-column label="进价" prop="purchaseDecimal"></el-table-column>
			<el-table-column label="批发价" prop="lowDecimal"></el-table-column>
			<el-table-column label="吊牌价" prop="commodityDecimal" width="120"></el-table-column>
      <el-table-column label="重量" prop="weight"> </el-table-column>
			<el-table-column label="体积" prop="volume"> </el-table-column>
		</el-table>
	</div>
	<!-- <div
    class="product-list"
    style="background-color: white; padding: 20px; margin-top: 20px"
  > -->
	<div class="product-content">
		<Edit v-if="open_edit" :open_edit="open_edit" :editForm="categoryModel" :addForm="addForm"
			:openDetail="openDetail" @closeDialog="closeDialogFunc($event, 'edit')"></Edit>
		<Detail v-if="open_detail" :open_detail="open_detail" :editForm="deatilModel"
			@closeDialog="closeDialogFunc($event, 'detail')"></Detail>
	</div>
	<!-- </div> -->
</template>

<script>
	import PurchaseApi from "@/api/purchase.js";
	import StockApi from "@/api/stock.js";
	import Edit from "./detail.vue";
	import Detail from "./depotDtail.vue";
	import SettingApi from "@/api/setting.js";
	export default {
		components: {
			Edit,
			Detail,
		},
		data() {
			return {
				loading: true,
				open_detail: false,
				activeName: "detail",
				num: 0,
				/*列表数据*/
				tableData: [],
				deatilModel: {},
				select: 1,
				billItemList: [],
				otherData: {},
				selectedRows: [], // 选中的行数据
				/*一页多少条*/
				pageSize: 10,
				/*总条数*/
				addForm: {},
				allData: {},
				totalDataNumber: 0,
				open_add: false,
				open_edit: false,
				open_logistic: false,
				printData: {},
				/*当前是第几页*/
				curPage: 1,
				/*搜索参数*/
				searchForm: {
					operTimes: "1",
					name: "",
					depotIdList: [],
				},
				status: -1,
				depots: [],
				suppliers: [],
				selectedBillId: null,
				openTitle: "",
				customerList: [],
				depotList: [],
				expressList: [],
				openDetail: false,
				isDepotListInitialized: false,
				checkAll: false,
				isIndeterminate: false,
			};
		},
		created() {
			/*获取列表*/
			this.getData();
		},
		computed: {},
		methods: {
			handleCheckAllChange(val) {
				this.searchForm.depotIdList = val ?
					this.depotList.map((item) => item.depotId) :
					[];
				this.isIndeterminate = false;
			},
			handleSelectionChange(val) {
				console.log(val, "val");
				this.searchForm.depotIdList = val;
				this.loading = true;
				this.onSubmit();
				const checkedCount = val.length;
				this.checkAll = checkedCount === this.depotList.length;
				this.isIndeterminate =
					checkedCount > 0 && checkedCount < this.depotList.length;
			},
			getSummarys({
				columns,
				data
			}) {
				let sums = [];
				// 在计算合计值之前创建一个新的数组，并在开头插入 '合计' 字符串
				sums.push("总计");
				columns.forEach((column, index) => {
					if (
						column.property == "purchaseMoney" ||
						column.property == "otherMoney" ||
						column.property == "deposit"
					) {
						const values = data.map((item) => Number(item[column.property]));
						const sum = values.reduce((prev, curr) => prev + curr, 0);
						sums[index] = sum; // 将合计值添加到新数组中
					} else {
						sums.push(""); // 其他列的合计值为空
					}
				});

				return sums;
			},
			/*选择第几页*/
			handleCurrentChange(val) {
				let self = this;
				self.curPage = val;
				self.loading = true;
				self.getData();
			},
			/*每页多少条*/
			handleSizeChange(val) {
				this.curPage = 1;
				this.pageSize = val;
				this.getData();
			},

			/*获取列表*/
			getData() {
				let self = this;
				let Params = self.searchForm;
				Params.pageIndex = self.curPage;
				Params.pageSize = self.pageSize;
				StockApi.stockList(Params, true)
					.then((res) => {
						self.loading = false;
						self.tableData = res.data.extend.records;

						console.log("tableData", self.tableData);
						self.otherData = res.data;
						self.expressList = res.data.express;
						self.depotList = res.data.depot;
						self.suppliers = res.data.supplier;
						self.totalDataNumber = res.data.extend.total;
						if (!self.isDepotListInitialized && self.depotList) {
							self.searchForm.depotIdList = [self.depotList[0].depotId]; // 设置默认选中第一项
							self.isDepotListInitialized = true;
							self.onSubmit(); // 再次调用getData以更新列表
						}
					})
					.catch((error) => {
						self.loading = false;
					});
			},
			addClick() {
				this.open_add = true;
				this.addForm = this.otherData;
				this.openDetail = true;
			},
			editClick(row) {
				this.open_edit = true;
				this.categoryModel = row;
				this.categoryModel.depotIdList = this.searchForm.depotIdList;
				this.addForm = this.otherData;
				this.openDetail = true;
			},
			detailClick(row) {
				this.open_detail = true;
				this.deatilModel = row;
			},
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
				if (f == "print") {
					this.open_print = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
				if (f == "detail") {
					this.open_detail = e.openDialog;
					if (e.type == "success") {
						this.getData();
					}
				}
			},
			/*搜索查询*/
			onSubmit() {
				this.getData();
			},
		},
	};
</script>

<style scoped>
	.alltitle {
		width: 301px;
		height: 24px;
		color: rgba(16, 16, 16, 1);
		font-size: 14px;
		text-align: left;
	}

	.form-item-container {
		display: flex;
		flex-direction: column;
	}

	.form-item-label {
		font-weight: bold;
		margin-bottom: 8px;
	}

	.form-item-content {
		background-color: rgba(239, 239, 239, 1);
		padding: 20px;
		width: 294px;
		margin: 20px;
	}

	.form-item-content1 {
		background-color: rgba(239, 239, 239, 1);
		padding: 20px;
		width: 100%;
		margin: 20px;
	}

	.content-box {
		display: flex;
		flex-direction: column;
		background-color: rgba(239, 239, 239, 1);
		padding: 20px;
	}

	.content-box-container {
		width: 100%;
		display: flex;
		flex-direction: row;
	}

	.titles {
		color: rgba(16, 16, 16, 1);
		font-size: 14px;
		margin: 20px;
		text-align: right;
	}
</style>
