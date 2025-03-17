<template>
  <div class="right-box d-s-s d-c">
    <div class="tabs-container">
      <!-- <el-tabs v-model="activeName" type="card">
        <el-tab-pane label="热销" name="sale"></el-tab-pane>
      </el-tabs> -->
    </div>
    <div class="lh30 tl d-b-s ranking-header">
      <span class="f18">商品排行榜</span>
      <span class="gray9 ml10">(近七天)</span>
      <span class="ml10">销售数量</span>
      <span class="ml10">销售金额</span>
    </div>
    <div class="list">
      <ul v-if="listData.length > 0">
        <li
          v-for="(item, index) in listData"
          :key="index"
          class="d-s-c p-6-0 border-b-d"
        >
          <span>
            <template v-if="activeName == 'sale'">
              <img v-img-url="item.imagePath" alt="" class="ml10" />
            </template>
          </span>
          <span class="text-ellipsis flex-1 ml10">{{ item.productName }}</span>
          <span class="gray9 tr">
            <template v-if="activeName == 'sale'">
              {{ item.marketNum }}
            </template>
          </span>
          <span class="gray9 tr">
            <template v-if="activeName == 'sale'">
              {{ item.marketPrice }}
            </template>
          </span>
        </li>
      </ul>
      <div v-else class="tc pt30">暂无上榜记录</div>
    </div>
  </div>
</template>

<script>
import StatementApi from "@/api/statement.js";
export default {
  data() {
    return {
      loading: true,
      activeName: "sale",
      listData: [],
    };
  },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      let self = this;
      StatementApi.getBoardProduct({}, true)
        .then((res) => {
          self.listData = res.data.hot;
          console.log("res.data", res.data);
          self.loading = false;
        })
        .catch((error) => {
          self.loading = false;
        });
    },
  },
};
</script>

<style scoped>
.right-box {
  width: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.tabs-container {
  width: 100%;
  display: flex;
/*  justify-content: center; */
  margin-top: 10px;
}

.ranking-header {
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-top: 10px;
  /* padding: 10px; */
}
.ranking-header span{ display: block; }

.list {
  width: 100%;
}

.list li {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 6px 0;
  border-bottom: 1px solid #ddd;
}
.list li span{ display: inline-block;}
.key-box {
  width: 20px;
  height: 20px;
  line-height: 20px;
  border-radius: 50%;
  font-weight: bold;
  text-align: center;
  color: #ffffff;
  background: red;
}

.list img {
  width: 30px;
  height: 30px;
}

.text-ellipsis {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tc {
  text-align: center;
}

.tr {
  text-align: right;
}

.ml10 {
  margin-left: 10px;
}

.gray9 {
  color: #999;
}
</style>
