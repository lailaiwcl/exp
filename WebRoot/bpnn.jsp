<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>BP神经网络样本训练实验</title>
	    <%@include file="/common/common.jsp" %>
	</head>
	<body>

		<div id="container" style="min-width: 600px; height: 400px; margin: 0 auto"></div>
		样本数量：
		<input type="text" name="trainCount" id="trainCount" value="200" />
		重复训练次数：
		<input type="text" name="reaptTimes" id="reaptTimes" value="5" />
		<input type="button" value="执行一次" width="200px" name="reset" id="reset" onclick="setData();"/>
		自动执行次数：
		<input type="text" name="exeTimes" id="exeTimes" value="10" />
		<input type="button" value="自动执行" width="200px" name="reset" id="reset" onclick="exe();"/>
		已执行总次数：<span style="font-size: 20px; color: #ff0000" id="exptimes">0</span>
		<hr/>
         <strong>自动测试网络结果：</strong><br/>
                        测试正确样本数：<span style="font-size: 20px; color: #ff0000" id="corrNum"></span> <br/>
                        测试样本总数：<span style="font-size: 20px; color: #ff0000" id="totalNum"></span> <br/>
                        当前正确率：<span style="font-size: 20px; color: #ff0000" id="rate"></span><br/>
                        平均正确率：<span style="font-size: 20px; color: #ff0000" id="ratesum"></span><br/> 
                        历史正确率：<div style="font-size: 20px; color: #ff0000" id="prerate"></div>
		<script type="text/javascript">
	var series;
	var ratesum = 0;
	var prerate = '';
	var exptimes = 0;
	$(function() {
		var chart;
		$(document).ready(
				function() {
					Highcharts.setOptions( {
						global : {
							useUTC : false
						}
					});

					chart = new Highcharts.Chart( {
						chart : {
							renderTo : 'container',
							type : 'line',
							backgroundColor : '#ffffff',
							events : {
								load : function() {
									series = this.series[0];
								}
							}
						},
						title : {
							text : '<strong>BP神经网络样本训练实验</strong>'
						},
						xAxis : {
							tickWidth : 1,
							lineWidth : 1,
							lineColor : '#ffffff',
							title : {
								text : '训练次数'
							}
						},
						yAxis : {
							title : {
								text : '输出层错误和'
							},
							max : 0.4,
							min : 0
						},
						tooltip : {
							enabled : false,
							formatter : function() {
								return '<strong>' + this.series.name
										+ '<strong>' + this.x
										+ ': ' + this.y;
							}
						},
						legend : {
							enabled : false
						},
						plotOptions : {
							line : {
								dataLabels : {
									enabled : false
								},
								lineWidth : 2,
								states : {
									hover : {
										lineWidth : 3
									}
								},
								marker : {
									enabled : false,
									states : {
										hover : {
											enabled : false,
											symbol : 'circle',
											radius : 3,
											lineWidth : 2
										}
									}
								}
							}
						},
						series : [ {
							name : 'Tokyo',
							data : (function() {
								var data = [], time = 0, i;

								for ( var i = 1; i < 1000; i++) {
									data.push( {
										x : time++,
										y : 0
									});
								}
								return data;
							})()
						} ]
					});
				});

	});
	function setData() {
		$.ajax( {
			type : 'post',
			url : 'bpnn.action',
			data : {
				trainCount : $("#trainCount").val(),
				reaptTimes : $("#reaptTimes").val()
			},
			dataType : 'text',
			success : function(result) {
				var dataArray = result.split(",");
				var data = new Array();
				var length = dataArray.length;
				for (i = 0; i < length-1; i++) {
					data[i] = parseFloat(dataArray[i]);
				}
				series.setData(data);
				
				var str = dataArray[length-1];
				ratesum += parseFloat(str.substring(str.indexOf("=") + 1,str.length));
				prerate += str.substring(str.indexOf("=") + 1,str.length);
				exptimes++;
				
				$("#corrNum").html(str.substring(0,str.indexOf("/")));
				$("#totalNum").html(str.substring(str.indexOf("/")+1,str.indexOf("=")));
				$("#rate").html(str.substring(str.indexOf("=") + 1,str.length));
				$("#ratesum").html(ratesum/exptimes);
				$("#prerate").html(prerate);
				$("#exptimes").html(exptimes);
				prerate += '+';
				
				
			}
		})
	}
	//setData();
	
	function exe(){
	   for(i = 0;i < parseInt($("#exeTimes").val());i++){
	      setData();
	   }
	}
</script>

	</body>
</html>
