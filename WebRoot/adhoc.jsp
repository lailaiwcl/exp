<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>Ad_Hoc网络</title>
	    <%@include file="/common/common.jsp" %>
	</head>
	<body>
	    <div id="container1" style="min-width: 300px; height: 600px; margin: 0 auto"></div>
	    <input type="button" value="开始模拟1" onclick="start1();"/>
	    <input type="button" value="开始模拟2" onclick="start2();"/>
	    <input type="button" value="停止模拟" onclick="stop1();"/>
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
            text: 'Ad_Hoc网络源节点发送流量速率变化'
        },
        subtitle: {
            text: '三个节点两条链路的Ad_Hoc网络源节点发送流量速率变化'
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 150
        },
        yAxis: {
            title: {
                text: '节点发送速率'
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
                Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
                Highcharts.numberFormat(this.y, 2);
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
            name: '节点1',
            data: (function() {
                var data1 = [],
                    time1 = (new Date()).getTime(),
                    i;
                for (i = -99; i <= 0; i++) {
                    data1.push({
                        x: time1 + i * 1000,
                        y: 0
                    });
                 }
                return data1;
            })()
        },
        {
            name: '节点2',
            data: (function() {
                var data2 = [],
                    time2 = (new Date()).getTime();
                for (i = -99; i <= 0; i++) {
                    data2.push({
                        x: time2 + i * 1000,
                        y: 0
                    });
                 }
                return data2;
            })()
        }]
    });      
    
});
    function flush(){
		$.ajax( {
			type : 'post',
			url : 'adhoc.action',
			data : {
				boundNum : ""
			},
			dataType : 'text',
			success : function(result) {
			    var str = result.split(",");
			    var x = (new Date()).getTime();
                var y1 = parseFloat(str[0]);
                var y2 = parseFloat(str[1]);
			    chart.series[0].addPoint([x, y1], true, true);
			    chart.series[1].addPoint([x, y2], true, true);
			}
		})
		
    }
    function flush2(){
		$.ajax( {
			type : 'post',
			url : 'adhoc2.action',
			data : {
				boundNum : ""
			},
			dataType : 'text',
			success : function(result) {
			    var str = result.split(",");
			    var x = (new Date()).getTime();
                var y1 = parseFloat(str[0]);
                var y2 = parseFloat(str[1]);
			    chart.series[0].addPoint([x, y1], true, true);
			    chart.series[1].addPoint([x, y2], true, true);
			}
		})
		
    }
    var timer;
    function start1(){
        timer = window.setInterval(flush,1000);
    }
    function start2(){
        timer = window.setInterval(flush2,1000);
    }
    function stop1(){
        window.clearInterval(timer);
    }
    
    function reset1(){
    		$.ajax( {
			type : 'post',
			url : 'initadhoc.action',
			data : {
				boundNum : ""
			},
			dataType : 'text',
			success : function(result) {
			  location.reload();
			}
		})
    }
   // reset1();
        </script>
    </body>
</html>
