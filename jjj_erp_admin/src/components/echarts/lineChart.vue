<template>
  <div class="ww100 mb30">
    <div class="d-e-c mb18">
      <div>
        <el-date-picker
          size="small"
          v-model="datePicker"
          type="daterange"
          align="right"
          unlink-panels
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="changeDate"
          :shortcuts="shortcuts"
        ></el-date-picker>
      </div>
    </div>
    <div class="">
      <div class="Echarts"><div id="LineChart"></div></div>
    </div>
  </div>
</template>

<script>
import StatementApi from "@/api/statement.js";
import { formatDate } from "@/utils/DateTime.js";
import * as echarts from "echarts";
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
      activeName: "new",
      DateType: 1,
      /*时间快捷选项*/

      shortcuts: [
        {
          text: "最近一周",
          value: () => {
            const end = new Date();
            const start = new Date();
            this.DateType = 1;
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            return [start, end];
          },
        },
        {
          text: "最近一个月",
          value: () => {
            const end = new Date();
            const start = new Date();
            this.DateType = 1;
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            return [start, end];
          },
        },
        {
          text: "最近三个月",
          value: () => {
            const end = new Date();
            const start = new Date();
            this.DateType = 1;
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            return [start, end];
          },
        },
        {
          text: "本年",
          value: () => {
            this.DateType = 3;
            return [];
          },
        },
      ],
      datePicker: [],
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
    this.myEcharts();
    window.addEventListener("resize", this.resizeChart);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.resizeChart);
  },
  methods: {
    /*切换*/
    handleClick(e) {
      this.activeName = e;
      this.getData();
    },

    /*选择时间*/
    changeDate() {
      if (this.DateType !== 3) {
        this.startDate = this.datePicker[0];
        this.endDate = this.datePicker[1];
      }
      this.getData();
    },

    /*创建图表对象*/
    myEcharts() {
      // 基于准备好的dom，初始化echarts实例
      myChart = echarts.init(document.getElementById("LineChart"));
      /*获取列表*/
      this.getData();
    },
    resizeChart() {
      if (myChart) {
        myChart.resize();
      }
    },
    /*格式数据*/
    createOption() {
      if (!this.loading) {
        let names = [
          "支付笔数",
          "支付金额",
          "客户数",
          "平均客单价",
          "发货笔数",
          "发货金额",
        ];
        let xAxis = this.dataList.days;
        let series1 = [];
        let series2 = [];
        let series3 = [];
        let series4 = [];
        let series5 = [];
        let series6 = [];
        this.dataList.data.forEach((item) => {
          series1.push(item.depositNum);
          series2.push(item.depositMoney);
          series3.push(item.customerNum);
          series4.push(item.customerMoney);
          series5.push(item.deliverNum);
          series6.push(item.deliverMoney);
        });
        // 指定图表的配置项和数据
        this.option.xAxis = {
          type: "category",
          data: xAxis,
        };
        this.option.color = [
          "#1495EB",
          "#00CC66",
          "#F9D249",
          "#ff9900",
          "#9860DF",
          "#02b5f7",
        ];

        this.option.legend = {
          data: [
            { name: names[0], color: "#ccc" },
            { name: names[1], color: "#ccc" },
            { name: names[2], color: "#ccc" },
            { name: names[3], color: "#ccc" },
            { name: names[4], color: "#ccc" },
            { name: names[5], color: "#ccc" },
          ],
        };
        this.option.yAxis = [
          {
            type: "value",
            position: "left",
          },
          {
            type: "value",
            position: "right",
          },
        ];
        this.option.series = [
          {
            name: names[0],
            type: "bar",
            data: series1,
            yAxisIndex: 0,
            itemStyle: {
              color: "#1495EB",
            },
          },
          {
            name: names[1],
            type: "bar",
            data: series2,
            yAxisIndex: 1,
            itemStyle: {
              color: "#00CC66",
            },
          },
          {
            name: names[2],
            type: "bar",
            data: series3,
            yAxisIndex: 0,
            itemStyle: {
              color: "#F9D249",
            },
          },
          {
            name: names[3],
            type: "bar",
            data: series4,
            yAxisIndex: 1,
            itemStyle: {
              color: "#ff9900",
            },
          },
          {
            name: names[4],
            type: "bar",
            data: series5,
            yAxisIndex: 0,
            itemStyle: {
              color: "#9860DF",
            },
          },
          {
            name: names[5],
            type: "bar",
            data: series6,
            yAxisIndex: 1,
            itemStyle: {
              color: "#02b5f7",
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
      const params =
        self.DateType == 3
          ? { dateType: self.DateType }
          : {
              DateType: self.DateType,
              startDate: self.startDate,
              endDate: self.endDate,
            };
      StatementApi.getBoardDeal(params, true)
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

<style scoped="scoped">
.Echarts {
  box-sizing: border-box;
}
.Echarts > div {
  height: 360px;
  box-sizing: border-box;
}
</style>
