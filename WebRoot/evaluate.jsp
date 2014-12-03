<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>淘宝信用评价</title>
	    <%@include file="/common/common.jsp" %>
	</head>
	<body>
	<table>
	    <tr>
	    <td width="80%">
	    <div id="container1" style="min-width: 300px; height: 600px; margin: 0 auto"></div>
	    </td>
	    <td valign="top">
	    <form action="" method="post">
	    <br/>
	     <br/>
	            总体评价
	     <input type="radio" name="v0" value="1" checked="checked" />好评
	     <input type="radio" name="v0" value="0" />中评
	     <input type="radio" name="v0" value="-1" />差评
	     <hr/>
	     <fieldset style="border: solid 1px #aaa; padding: 4px;">
			<legend>商品</legend>
			性&nbsp;&nbsp;价&nbsp;&nbsp;比：<input type="text" id="v1" name="v1" value="5"/><br/>
			正品保证：<input type="text" id="v2" name="v2" value="5"/><br/>
			描述相符：<input type="text" id="v3" name="v3" value="5"/><br/>
		</fieldset>
		<hr/>
	     <fieldset style="border: solid 1px #aaa; padding: 4px;">
			<legend>服务</legend>
			发货速度：<input type="text" id="v4" name="v4" value="5"/><br/>
			售前服务：<input type="text" id="v5" name="v5" value="5"/><br/>
			售后保障：<input type="text" id="v6" name="v6" value="5"/><br/>
		</fieldset>
		<hr/>
	     <fieldset style="border: solid 1px #aaa; padding: 4px;">
			<legend>交易信息</legend>
			交易权重：<input type="text" id="v7" name="v7" value="0.4"/><br/>
			评价能力：<input type="text" id="v8" name="v8" value="0.5"/>
		</fieldset>
		<br/><br/>
		<input type="button" onclick="clickBnt()" value="提交评价">&nbsp;&nbsp;<input type="reset" value="重置表单"/>
	    </form>
	    </td></tr>
	</table>
	    <input type="button" value="模拟数据1" onclick="start1();"/>
	    <input type="button" value="停止" onclick="stop1();"/>
	    <input type="button" value="重置数据" onclick="reset1();"/>
<script type="text/javascript">
var chart;
$(function () {
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    
    chart = new Highcharts.Chart({
        chart: {
            renderTo: 'container1',
            type: 'line',
            marginRight: 10
        },
        title: {
            text: '淘宝信用评价'
        },
        xAxis: {
                title: {
                    enabled: true,
                    text: '评价客户数'
                },
                min: 0
        },
        yAxis: {
            title: {
                text: '评价值'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }],
            min: 0
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+
                '<br/>'+ Highcharts.numberFormat(this.y, 4);
            }
        },
        plotOptions: {
                spline: {
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 2
                        }
                    },
                    marker: {
                        enabled: true,
                        states: {
                            hover: {
                                enabled: true,
                                symbol: 'circle',
                                radius: 3,
                                lineWidth: 1
                            }
                        }
                    },
                    pointInterval: 3600000
                }
            },
        legend: {
            enabled: true
        },
        exporting: {
            enabled: true
        },
        series: [{
            name: '改进后',
            data: []
        },
        {
            name: '改进前',
            data: []
        }]
    });      
    
});
    function clickBnt(){
		$.ajax( {
			type : 'post',
			url : 'evaluate.action',
			data : {
				v0 : $('input[name="v0"]').filter(':checked').val(),
				v1 : $("#v1").val(),
				v2 : $("#v2").val(),
				v3 : $("#v3").val(),
				v4 : $("#v4").val(),
				v5 : $("#v5").val(),
				v6 : $("#v6").val(),
				v7 : $("#v7").val(),
				v8 : $("#v8").val()
			},
			dataType : 'text',
			success : function(result) {
			    var str = result.split(",");
			    var x =  parseInt(str[0]);
			    var y1 =  parseFloat(str[1]);
			    var y2 =  parseFloat(str[2]);
			    chart.series[0].addPoint([x, y1],true,false,true);
			    chart.series[1].addPoint([x, y2],true,false,true);
			}
		})
		
    }
    
        function flush(){
		$.ajax( {
			type : 'post',
			url : 'readfromfileevaluate.action',
			data : {
				boundNum : ""
			},
			dataType : 'text',
			success : function(result) {
			    var str = result.split(",");
			    var x =  parseInt(str[0]);
			    var y1 =  parseFloat(str[1]);
			    var y2 =  parseFloat(str[2]);
			    chart.series[0].addPoint([x, y1],true,false,true);
			    chart.series[1].addPoint([x, y2],true,false,true);
			}
		})
		
    }
    
    var timer;
    function start1(){
        timer = window.setInterval(flush,500);
    }
   function stop1(){
        window.clearInterval(timer);
    }
    
        function reset1(){
    		$.ajax( {
			type : 'post',
			url : 'initevaluate.action',
			data : {
				boundNum : ""
			},
			dataType : 'text',
			success : function(result) {
			  location.reload();
			}
		})
    }
        </script>
    </body>
</html>
