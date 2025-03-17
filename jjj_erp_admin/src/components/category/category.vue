<template>
  <div
    class="left-menu"
    :style="'width:' + width + 'px;'"
    @click="cahngeFunc('empty')"
  >
    <div class="d-b-c search-box">
      <el-input
        @blur="searchFunc"
        prefix-icon="Search"
        v-model="search"
        placeholder="搜索商品分类"
        clearable
      />
      <el-button
        @click.stop="cahngeFunc('add')"
        class="ml10 fb add-btn"
        icon="Plus"
        circle
      />
    </div>
    <el-tree
      style="max-width: 600px"
      :data="data"
      :props="defaultProps"
      node-key="categoryId"
      :default-expanded-keys="[0]"
      @node-click="handleNodeClick"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span class="name icon iconfont icon-wenjian"></span>
          <span>{{ node.label }}</span>
          <span v-if="!data.isRoot" class="hover-icons">
            <el-icon @click.stop="cahngeFunc('edit', node.data)">
              <Edit />
            </el-icon>
            <el-icon @click.stop="cahngeFunc('delete', node.data)">
              <Delete />
            </el-icon>
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script>
export default {
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      listData: [],
      search: "",
    };
  },
  props: {
    data: {
      type: Array,
      default: [],
    },
    width: {
      type: Number,
      default: 207,
    },
    type: {
      /* 0 商品分类 */
      type: Number,
      default: 0,
    },
    defaultProps: {
      type: Object,
      default: {
        children: "children",
        label: "label",
      },
    },
  },
  created() {
    console.log(this.$props.data, "data");
  },
  methods: {
    searchFunc(e) {
      this.$emit("search", this.search);
    },
    cahngeFunc(name, item) {
      this.$emit("change", name, item);
    },
    handleNodeClick(e) {
      this.$emit("change", "click", e);
    },
  },
};
</script>

<style lang="scss" scoped>
.search-box {
  margin-bottom: 16px;
}

::v-deep(.el-tree-node__content > .el-tree-node__expand-icon) {
  // position: absolute;
  // right: 0;
}

::v-deep(.el-tree-node__content:hover > .custom-tree-node .hover-icons) {
  display: inline;
}

.hover-icons {
  display: none;
  position: absolute;
  right: 0;
  background: var(--el-tree-node-hover-bg-color);

  .el-icon {
    font-size: 14px;
    color: #999;
    margin-left: 3px;
  }

  .el-icon:hover {
    color: var(--el-color-primary);
  }
}

.icon.icon-yemian,
.icon.icon-wenjian {
  font-size: 12px;
  // color: var(--el-color-primary);
  color:#e9d047;
  margin-right: 3px;
}

.add-btn {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  font-weight: bold;
}

.left-menu {
  width: 207px;
  height: calc(100vh - 84px - 24px);
  flex-shrink: 0;
  border-right: 5px solid #f6f8fb;
  background-color: #fff;
  padding: 14px 9px 14px 13px;
  box-sizing: border-box;
}
</style>
