<template>
  <div class="upload-wrap">
    <div class="upload-wrap-inline mb16 clearfix">
      <div class="leval-item flex-1">
        <el-button icon="Plus" @click="addCategory">添加分类</el-button>
        <el-button type="danger" icon="Delete" @click="deleteFileFunc(false)">批量删除</el-button>
      </div>
      <div class="leval-item upload-btn">
        <el-upload
          class="avatar-uploader"
          multiple
          ref="upload"
          action="string"
          :accept="accept"
          :before-upload="onBeforeUploadImage"
          :http-request="UploadImage"
          :show-file-list="false"
          :on-change="fileChange"
        >
          <el-button icon="Upload" type="primary">点击上传</el-button>
        </el-upload>
      </div>
    </div>

    <!--我的资源库-->
    <div class="fileContainer">
      <div class="file-type">
        <ul>
          <li :class="activeType == item.categoryId ? 'item active' : 'item'" v-for="(item, index) in typeList" :key="index" @click="selectTypeFunc(item.categoryId)">
            {{ item.name }}
            <div class="operation" @click.stop v-if="item.categoryId != null">
              <p @click="editCategoryFunc(item)">
                <el-icon><Edit /></el-icon>
              </p>
              <p @click="deleteCategoryFunc(item.categoryId)">
                <el-icon><Delete /></el-icon>
              </p>
            </div>
          </li>
        </ul>
      </div>
      <div class="file-content" >
        <ul class="fileContainerUI clearfix">
          <li class="file" v-for="(item, index) in fileList" :key="index" @click="selectFileFunc(item, index)">
            <div v-if="item.selected == true" class="selectedIcon">
              <el-icon><Check /></el-icon>
            </div>
            <img v-if="this_config.fileType == 'image'" :scr="item.image" :style="'background-image:url(' + item.image + ')'" alt="" />
            <p class="desc">{{ item.name }}</p>
            <div class="bottom-shade" @click.stop>
              <a href="javascript:void(0)" @click="deleteFileFunc(item)">
                <el-icon><Delete /></el-icon>
              </a>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <!--分页-->
    <div class="pagination-wrap">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="curPage"
        :page-sizes="[12, 24, 36, 42, 48]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalDataNumber"
      ></el-pagination>
    </div>

    <!--图片类别-->
    <AddCategory v-if="isShowCategory" :category="category" :fileType="this_config.fileType" @closeCategory="closeCategoryFunc"></AddCategory>
  </div>
</template>

<script>
import FileApi from '@/api/file.js';
import AddCategory from './part/AddCategory.vue';
export default {
	components: {
		AddCategory
	},
	data() {
		return {
			/*宽度*/
			dialogWidth: '910px',
			/*类别*/
			activeType: null,
			categoryId: '',
			/*图片类别*/
			typeList: [],
			/*图标路径*/
			imageUrl: null,
			/*弹窗显示*/
			dialogVisible: true,
			/*文件列表*/
			fileList: [],
			/*分类添加loading*/
			addLoading: false,
			/*默认值*/
			this_config: {
				/*上传个数*/
				total: 1,
				fileType: 'image'
			},
			/*记录选中的个数*/
			record: 0,
			/*是否加载完成*/
			loading: true,
			/*列表数据*/
			tableData: [],
			/*一页多少条*/
			pageSize: 32,
			/*一共多少条数据*/
			totalDataNumber: 0,
			/*当前是第几页*/
			curPage: 1,
			/*是否显示图片类别编辑框*/
			isShowCategory: false,
			/*当前类别model*/
			category: null,
			/*选中图片*/
			fileIds: [],
			accept: ''
		};
	},
	props: ['config'],
	created() {
		/*覆盖默认值*/
		if (Object.prototype.toString.call(this.config).toLowerCase() === '[object object]') {
			for (let key in this.config) {
				if (this.config[key]) {
					this.this_config[key] = this.config[key];
				}
			}
			if (this.this_config['fileType'] == 'image') {
				this.accept = 'image/jpeg,image/png,image/jpg';
			} else {
				this.accept = 'video/mp4';
			}
		}

		this.getFileCategory();
		this.getData();
	},
	methods: {
		/*获取图片列表*/
		getFileCategory() {
			let self = this;
			let param = {
				pageIndex: self.curPage,
				pageSize: self.pageSize,
				parentId: self.activeType,
				fileType: self.this_config.fileType
			};
			FileApi.SystemPictureList(param, true)
				.then(res => {
					self.loading = false;
					self.fileList = res.data.records;
					self.totalDataNumber = res.data.total;
				})
				.catch(error => {
					self.loading = false;
				});
		},
		/*获取分类列表*/
		getData: function() {
			let self = this;
			FileApi.PictureIndex(
				{
					type: self.this_config.fileType
				},
				true
			)
				.then(res => {
					let type = [
						{
							categoryId: null,
							name: '全部'
						}
					];
					self.typeList = type.concat(res.data);
					self.categoryId = self.typeList.categoryId;
					console.log('index', self.typeList);
				})
				.catch(error => {
					self.loading = false;
				});
		},

		/*添加图片类别*/
		addCategory() {
			this.isShowCategory = true;
			this.category = null;
		},

		/*修改类别*/
		editCategoryFunc(item) {
			this.isShowCategory = true;
			this.category = item;
		},

		/*关闭类别层*/
		closeCategoryFunc(e) {
			if (e != null) {
				this.getFileCategory();
			}
			this.isShowCategory = false;
		},

		/*删除类别提示*/
		deleteCategoryFunc(e) {
			ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(() => {
					this.deleteCategory(e);
				})
				.catch(() => {
					ElMessage({
						type: 'info',
						message: '已取消删除'
					});
				});
		},

		/*删除类别*/
		deleteCategory(e) {
			let self = this;
			FileApi.deleteCategory({
				categoryId: e
			})
				.then(data => {
					ElMessage({
						message: '删除成功',
						type: 'success'
					});
					self.activeType = null;
					self.getFileCategory();
					self.getData();
				})
				.catch(error => {
					ElMessage.error('删除失败');
				});
		},

		/*选择类别*/
		selectTypeFunc(id) {
			this.activeType = id;
			this.curPage = 1;
			this.getFileCategory();
		},

		/*选择第几页*/
		handleCurrentChange(val) {
			this.curPage = val;
			this.getFileCategory();
		},

		/*每页多少条*/
		handleSizeChange(val) {
			this.curPage = 1;
			this.pageSize = val;
			this.getFileCategory();
		},

		/*选择上传图片*/
		fileChange(e) {
			console.log(e);
		},

		/*选择图片之前*/
		onBeforeUploadImage(file) {
			return true;
		},

		/*上传图片*/
		UploadImage(param) {
			let self = this;
			const loading = ElLoading.service({
				lock: true,
				text: '文件上传中,请等待',
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
			const formData = new FormData();
			formData.append('iFile', param.file);
			formData.append('categoryId', self.activeType == null ? 0 : self.activeType);
			formData.append('fileType', 'bank');
			FileApi.uploadFile(formData)
				.then(response => {
					loading.close();
					self.getFileCategory();
					param.onSuccess(); // 上传成功的图片会显示绿色的对勾
					// 但是我们上传成功了图片， fileList 里面的值却没有改变，还好有on-change指令可以使用
				})
				.catch(response => {
					loading.close();
					ElMessage({
						message: '本次上传文件失败',
						type: 'warning'
					});
					param.onError();
				});
		},

		/*选择图片*/
		selectFileFunc(item, index) {
			if (item.selected) {
				item.selected = false;
				this.record--;
			} else {
				item.selected = true;
				this.record++;
			}
			this.fileList[index] = item;
			console.log('1', item.categoryId);
		},

		/*删除图片*/
		deleteFileFunc(e) {
			let self = this;
			let loading = null;
			ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(() => {
					loading = ElLoading.service({
						lock: true,
						text: '正在删除',
						spinner: 'el-icon-loading',
						background: 'rgba(0, 0, 0, 0.7)'
					});
					let temp_list = [];
					if (e) {
						temp_list.push(e.categoryId);
					} else {
						for (let i = 0; i < self.fileList.length; i++) {
							let item = self.fileList[i];
							if (item.selected) {
								temp_list.push(item.categoryId);
							}
						}
					}
					if (temp_list.length == 0) {
						loading.close();
						ElMessage({
							message: '请选择文件',
							type: 'warning'
						});
						return;
					}
					FileApi.deleteFiles(
						{
							imageIds: temp_list.join(',')
						},
						true
					)
						.then(data => {
							loading.close();
							ElMessage({
								message: '删除成功',
								type: 'success'
							});
							self.getFileCategory();
						})
						.catch(error => {
							loading && loading.close();
						});
				})
				.catch(() => {
					loading && loading.close();
					console.log('id', self.categoryId);
					ElMessage({
						message: '删除失败',
						type: 'warning'
					});
				});
		},

		/*选择图片确认*/
		confirmFunc() {
			let list = [];
			let leng = 0;
			for (let i = 0; i < this.fileList.length; i++) {
				let item = this.fileList[i];
				if (item.selected) {
					let obj = {
						fileId: item.fileId,
						filePath: item.filePath
					};
					list.push(obj);
					leng++;
				}
				if (leng > this.this_config.total) {
					break;
				}
			}
			this.$emit('returnImgs', list);
		},

		/*取消*/
		cancelFunc() {
			this.$emit('returnImgs', null);
		}
	}
};
</script>

<style lang="scss">
.upload-dialog .el-dialog__body {
  padding-top: 0;
  padding-bottom: 0;
}

.upload-wrap {
  min-height: calc(100vh - 122px);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  flex: 1;
}
.upload-wrap-inline {
  width: 100%;
  display: flex;
}
.upload-wrap-inline .leval-item {
  display: inline-block;
}

.upload-wrap-inline .upload-btn {
  float: right;
}

.fileContainer {
  position: relative;
  padding-left: 120px;
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
}

.fileContainer .file-type {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  overflow-y: auto;
  width: 120px;
  border-right: 1px solid #c6c6c6;
  border-bottom: 1px solid #c6c6c6;
}

.fileContainer .file-type li {
  padding: 10px 0;
  cursor: pointer;
  text-align: center;
  padding-right: 20px;
  min-height: 20px;
  position: relative;
}

.fileContainer .file-type li.active {
  background: #b4b4b4;
  color: #ffffff;
}

.fileContainer .file-type li .operation {
  display: none;
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  width: 20px;
}

.fileContainer .file-type li:hover .operation {
  display: block;
}

.fileContainer .file-content {
  // height: 300px;
  text-align: center;
  overflow: hidden;
  padding: 10px;
  margin: 0;
  overflow-y: auto;
  flex: 1;
  // border: 1px solid #c6c6c6;
}

.fileContainer li.file {
  float: left;
  margin: 10px 9px 0px;
  position: relative;
  width: 100px;
}

.fileContainer li.file img {
  display: inline-block;
  width: 100px;
  height: 100px;
  cursor: pointer;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  background-color: #fff;
}

.fileContainer li.file p.desc {
  font-size: 12px;
  height: 15px;
  line-height: 15px;
  overflow: hidden;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  word-wrap: break-word;
}

.fileContainer li.file:hover .bottom-shade {
  display: block;
}

.fileContainer .bottom-shade {
  position: absolute;
  bottom: 16px;
  left: 0;
  background: rgba(0, 0, 0, 0.7);
  height: 26px;
  line-height: 26px;
  width: 100%;
  display: none;
  transform: all 0.2s ease-out 0s;
  color: #ffffff;
}
.fileContainer .bottom-shade a {
  color: #ffffff;
}

.fileContainer .selectedIcon {
  width: 20px;
  height: 20px;
  position: absolute;
  top: 0;
  left: 0;
  background: #ff9900;
  z-index: 1;
  color: #ffffff;
  cursor: pointer;
}

// :focus {
//   outline: -webkit-focus-ring-color auto 1px;
// }

.upload-dialog .pagination-wrap {
  margin-top: 16px;
  text-align: right;
}

.move-type {
  max-height: 200px;
  overflow-y: auto;
}
.move-type li {
  padding: 0 10px;
  height: 30px;
  line-height: 30px;
  cursor: pointer;
}
.move-type li:hover {
  background: #c6e2ff;
}
</style>
