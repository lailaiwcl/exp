<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>人工蜂群算法平面选址实现</title>
	    <%@include file="/common/common.jsp" %>
	</head>
	<body>
	<table><tr>
	<td><div id="container" style="width: 600px; height: 600px; margin: 0 auto"></div></td>
	<td valign="top">
	基础设施坐标：
<textarea id="orginalSite" name="orginalSite" style="width:200px; height:200px">10,20
10,60
20,80
30,100
40,50
50,30
60,90
70,10
80,0
90,60</textarea>
	<input type="button" value="重置原始点" width="100" onclick="reset();"/>
	&nbsp;
	<div>选址点个数：<input type="text" name=boundNum id="boundNum" value="5" />
	<input type="button" value="开始计算" width="100" onclick="flush();"/>
	</div>
	<div>
</div>
	<br/>
	<br/>
	<div>原始点：<span style="font-size: 20px; color: #ff0000" id="site"></span></div>
	<div>选址点：<span style="font-size: 20px; color: #ff0000" id="ss"></span></div>
	<div>距离和：<span style="font-size: 20px; color: #ff0000" id="s2"></span></div>
	</td>
	</tr>
	</table>
		<script type="text/javascript">
    var chart;
    var dd = "";
$(function () {
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container',
                type: 'scatter',
                zoomType: 'xy'
            },
            title: {
                text: '<strong>人工蜂群算法平面选址实现</strong>'
            },
            xAxis: {
                title: {
                    enabled: true,
                    text: null
                },
                startOnTick: true,
                endOnTick: true,
                showLastLabel: true,
                min: 0,
                max: 100
            },
            yAxis: {
                title: {
                    text: null
                },
                min: 0,
                max: 100
            },
            tooltip: {
                formatter: function() {
                        return ''+
                        this.x +',' + this.y;
                }
            },
            legend: {
                enabled: true,
                layout: 'vertical',
                align: 'left',
                verticalAlign: 'top',
                x: 100,
                y: 70,
                floating: true,
                backgroundColor: '#FFFFFF',
                borderWidth: 1
            },
            plotOptions: {
                scatter: {
                    marker: {
                        radius: 5,
                        states: {
                            hover: {
                                enabled: true,
                                lineColor: 'rgb(100,100,100)'
                            }
                        }
                    },
                    states: {
                        hover: {
                            marker: {
                                enabled: false
                            }
                        }
                    }
                }
            },
            series: [{
                name: '基础设施位置',
                color: 'rgba(223, 83, 83, .5)',
                data: []    
            },{
                name: '选址位置',
                color: 'rgba(119, 152, 191, .8)',
                data: []    
            }]
        });
    });
    
});
    function reset(){
    var s = $("#orginalSite").val().split("\n");
    dd = "";
    for(var i = 0;i<s.length -1;i++){
        if(s[i].length > 0){
           dd += s[i] + ",";
        }
    }
    dd += s[i];
    chart.series[0].setData([]);
    chart.series[1].setData([]);
    var arrStr = dd.split(",");
    var aa = "";
	for(var i = 0;i<arrStr.length-1;){
		var x = parseFloat(arrStr[i]);
	    var y = parseFloat(arrStr[++i]);
	    aa +="[" + x + "," + y + "],";
		++i;
        chart.series[0].addPoint([x, y]);
	}
	$("#site").html(aa);
	$("#ss").html("");
	$("#s2").html("");
    
    }
    function flush(){
        reset();
		chart.series[1].setData([]);
		$.ajax( {
			type : 'post',
			url : 'abc.action',
			data : {
				boundNum : $("#boundNum").val(),
				orginalSite : dd
			},
			dataType : 'text',
			success : function(result) {
				var jsonData = eval('('+result+')');
				var str = "";
				for(var i = 0;i<jsonData.length-1;){
				var x = jsonData[i];
				var y = jsonData[++i];
				++i;
                chart.series[1].addPoint([x, y]);
                str+="[" + x + "," + y + "],"
				}
				$("#ss").html(str.substring(0,str.length-1));
				$("#s2").html(jsonData[i]);
			}
		})
        } 
</script>
	</body>
</html>
