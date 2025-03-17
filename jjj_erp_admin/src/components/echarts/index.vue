<template>
  <div class="pie-container d-b-s d-c">
    <div class="ww100 d-b-c lh30">
      <span class="f12">待处理售后单</span>
    </div>
    <div class="ww100" style="white-space: nowrap">
      <div class="Echarts">
        <div id="PieBox" style="white-space: nowrap" class="ww100"></div>
      </div>
    </div>
    <div></div>
  </div>
</template>

<script>
import StatementApi from "@/api/statement.js";
import * as echarts from "echarts";
let myChart;
export default {
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      /*天数*/
      /*选择的天数*/
      selectDay: 7,
      /*数据对象*/
      payScale: {},
      /*图表对象*/
      // myChart: null,
      /*图表配置*/
      option: {
        title: {
          text: "",
          subtext: "",
          left: "center",
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
      },
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.myEcharts();
    });
  },
  beforeDestroy() {
    if (myChart) {
      myChart.dispose();
    }
  },
  methods: {
    /*选择天数*/
    changeFunc() {
      this.getData();
    },

    /*创建图表对象*/
    myEcharts() {
      if (myChart) {
        myChart.dispose();
      }
      const pieBox = document.getElementById("PieBox");
      if (pieBox) {
        myChart = echarts.init(pieBox);
        this.getData();
      } else {
        console.error("PieBox element not found");
      }
    },
    // myEcharts() {
    //   // 基于准备好的dom，初始化echarts实例
    //   myChart = echarts.init(document.getElementById("PieBox"));
    //   this.getData();
    // },

    /*格式数据*/
    createOption() {
      // if (!this.loading) {
      const data = [
        { value: this.payScale.qiTa, name: "其他" },
        { value: this.payScale.buFa, name: "补发" },
        { value: this.payScale.huanHuo, name: "换货" },
        { value: this.payScale.tuiHuo, name: "退货" },
        { value: this.payScale.tuiKuan, name: "退款" },
      ];

      this.option.legend = {
        orient: "vertical",
        left: "right",
        data: data.map((item) => item.name),
      };

      this.option.series = [
        {
          name: "售后单",
          type: "pie",
          radius: ["30%", "50%"],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: "#fff",
            borderWidth: 2,
          },
          label: {
            show: false,
            position: "center",
          },
          emphasis: {
            label: {
              show: false,
              fontSize: 20,
              fontWeight: "bold",
            },
          },
          labelLine: {
            show: false,
          },
          data: data,
        },
      ];

      this.option.graphic = {
        type: "text",
        left: "center",
        top: "center",
        style: {
          text: "",
          textAlign: "center",
          fill: "#000",
          fontSize: 20,
          fontWeight: "bold",
        },
      };
      console.log(this.option, "option");
      myChart.setOption(this.option);
      myChart.on("mouseover", (params) => {
        if (params.seriesType === "pie") {
          myChart.setOption({
            graphic: {
              style: {
                text: `${params.name}\n${params.percent}%`,
              },
            },
          });
        }
      });

      myChart.resize();
      // }
    },
    /*获取数据*/
    getData() {
      let self = this;
      StatementApi.getBoardJob({}, true)
        .then((res) => {
          self.payScale = res.data;
          self.loading = false;
          self.createOption();
        })
        .catch((error) => {});
    },
  },
};
</script>

<style scoped="scoped">
.pie-container {
  box-sizing: border-box;
}
.Echarts > div {
  height: 400px;
}
</style>
