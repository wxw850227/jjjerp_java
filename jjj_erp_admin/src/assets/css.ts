export default function(){
  return `
  .t-01 {
    text-align: center;
  }

  .main-content .el-col {
    border: 1px solid #ebeef5;
    padding: 6px 5px;
  }

  .s_order_detail {
    padding-left: 2%;
    max-width: 760px;
  }

  .amount {
    padding: 10px 0;
    font-size: 14px;
  }
  .table-a table {
    border-top: 1px solid #eee;
    border-left: 1px solid #eee;
    table-layout: fixed !important;
  }
  .table-a table tbody {
    width: 100% !important;
  }
  .table-a table td,
  .table-a table th {
    padding: 6px 10px;
    border-bottom: 1px solid #eee;
    border-right: 1px solid #eee;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    text-overflow: ellipsis;
    vertical-align: middle;
    position: relative;
    text-align: left;
    width: 16.67%;
  }
  .table-b.table-a table td,
  .table-b.table-a table th {
    width: 50%;
  }
  .table-c.table-a table td,
  .table-c.table-a table th {
    width: 14.2%;
  }
  .table-a table th {
    color: #909399;
    font-weight: bold;
    font-size: 12px;
    white-space: normal;
    word-break: break-all;
  }
  .table-a table td,
  .table-a table td p {
    line-height: 23px;
    color: #606266;
    font-size: 12px;
    line-height: 23px;
  }
  .table-a table {
    border-top: 1px solid #eee;
    border-left: 1px solid #eee;
    border-spacing: 0; /*去掉单元格间隙*/
  }
 .print-title {
  display: flex;
  justify-content: space-between;
  position: relative;
  height: 40px;
  padding: 10px;
  line-height: 40px;
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 12px;
}
.print-title1 {
  display: flex;
  justify-content: center;
  text-align: center;
  padding-left: 0;
  line-height: 40px;
  font-size: 14px;
  font-weight: 700;
}
.print-title2 {
  display: flex;
  justify-content: space-between;
  position: relative;
  height: 40px;
  padding: 10px;
  line-height: 40px;
  font-size: 14px;
  font-weight: 500;
}
  .print-info-list {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
  }
  .print-info-item:nth-child(1),
  .print-info-item:nth-child(2) {
    border-top: 1px solid #eee;
  }
  .print-info-item:nth-of-type(2n-1) {
    border-left: 1px solid #eee;
    width: calc(50% - 1px);
  }
  .print-info-item {
    width: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    border-bottom: 1px solid #eee;
  }
  .print-info-title,
  .print-info-center {
    padding: 6px 5px;
    box-sizing: border-box;
    color: #606266;
    line-height: 1.5;
    font-size: 12px;
    min-height: 31px;
    height: 100%;
  }
  .print-info-title {
    width: 33%;
    display: flex;
    align-items: center;
  }
  .print-info-center {
    flex: 1;
    border-right: 1px solid #eee;
    border-left: 1px solid #eee;
    word-break: break-all;
  }
  .d-b-s{
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }
  .d-b-c{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .d-s-c {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  .d-c-c {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .flex-1{
    flex:1;
  }
  .merchant-list.print-info-list .print-info-item,.merchant-list.print-info-list .print-info-center{
    border: none !important;
  }
  .d-c{
    display: flex;
    flex-direction: column;
  }
  .mt16{
    margin-top:16px;
  }
  .f14{
    font-size:14px;
  }
  .f18{
    font-size: 18px;
  }
  `
}
