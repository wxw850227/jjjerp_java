<template>
  <!--
          作者：luoyiming
          时间：2019-10-24
          描述：统计-销售统计-交易统计
      -->
  <div class="mt30">
    <div class="">
      <div class="Echarts">
        <div id="TransactionChart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import StatisticsApi from "@/api/statistics.js";
import * as echarts from "echarts";
import { formatDate } from "@/utils/DateTime.js";
let myChart;
export default {
  data() {
    let endDate = new Date();
    let startDate = new Date();
    startDate.setTime(startDate.getTime() - 3600 * 1000 * 24 * 7);
    return {
      /*是否正在加载*/
      loading: true,
      /*类别*/
      activeName: "order",
      /*时间快捷选项*/
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const endDate = new Date();
              const startDate = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      startDate: formatDate(startDate, "YYYY-MM-DD"),
      endDate: formatDate(endDate, "YYYY-MM-DD"),
      /*数据对象*/
      dataList: null,
      /*交易统计图表对象*/
      // myChart: null,
      /*图表数据*/
      option: {
        title: {
          //text: 'ECharts 入门示例'
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
        },
        yAxis: {},
      },
    };
  },
  created() {},
  mounted() {
    // this.myEcharts();
  },
  methods: {
    /*切换*/
    handleClick(e) {
      this.getData();
    },

    /*选择时间*/
    changeDate() {
      this.getData();
    },

    /*创建图表对象*/
    myEcharts() {
      // 基于准备好的dom，初始化echarts实例

      myChart = echarts.init(document.getElementById("TransactionChart"));
      /*获取列表*/
      this.getData();
    },

    /*格式数据*/
    createOption() {
      if (!this.loading) {
        let names = [];
        let xAxis = this.dataList.days;
        let series1 = [];
        let series2 = [];
        this.dataList.data.forEach((item) => {
          series1.push(item.totalMoney);
          series2.push(item.totalNum);
        });
        if (this.activeName == "order") {
          names = ["成交额", "成交量"];
        } else if (this.activeName == "refund") {
          names = ["退单额", "退单量"];
        }

        // 指定图表的配置项和数据
        this.option.xAxis = {
          type: "category",
          boundaryGap: false,
          data: xAxis,
        };
        this.option.color = ["#5E73E3", "#31C0D0"];

        this.option.legend = {
          data: [
            {
              name: names[0],
              color: "#ccc",
            },
            {
              name: names[1],
            },
          ],
        };
        this.option.series = [
          {
            name: names[0],
            type: "line",
            data: series1,
            lineStyle: {
              color: "#5E73E3",
            },
          },
          {
            name: names[1],
            type: "line",
            data: series2,
            lineStyle: {
              color: "#31C0D0",
            },
          },
        ];

        myChart.setOption(this.option);
        myChart.resize();
      }
    },

    /*获取列表*/
    getData() {
      let self = this;
      self.loading = true;
      StatisticsApi.getOrderByDate(
        {
          startDate: self.startDate,
          endDate: self.endDate,
          type: self.activeName,
        },
        true
      )
        .then((res) => {
          self.dataList = res.data;
          self.loading = false;
          self.createOption();
        })
        .catch((error) => {});
    },
  },
};
</script>

<style>
.Echarts {
  box-sizing: border-box;
}

.Echarts > div {
  width: 100%;
  height: 400px;
  box-sizing: border-box;
}
</style>
