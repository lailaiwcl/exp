<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title>Kdd Cup99 入侵检测数据集分析</title>
        <%@include file="/common/common.jsp" %>
<style type="text/css">
#container, #sliders {
	min-width: 310px; 
	max-width: 98%;
	margin: 0 auto;
}
#container {
	height: 400px; 
}
</style>
	</head>
	<body>
	    <div id="container"></div>
	    <div id="sliders">
	<table>
		<tr><td>Alpha Angle</td><td><input id="R0" type="range" min="0" max="45" value="15"/> <span id="R0-value" class="value"></span></td><td> 
		<input id="step1" name="step1" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step2" name="step2" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step3" name="step3" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step4" name="step4" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step5" name="step5" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step6" name="step6" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step7" name="step7" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step8" name="step8" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step9" name="step9" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step10" name="step10" class="nui-textbox" vtype="float" required="true" width="30px"/>-
		<input id="step11" name="step11" class="nui-textbox" vtype="float" required="true" width="30px"/>
		</td></tr>
	    <tr><td>Beta Angle</td><td><input id="R1" type="range" min="0" max="45" value="15"/> <span id="R1-value" class="value"></span></td><td>
	    <a class="nui-button" iconCls="icon-ok" onclick="getAttributeType()">计算</a>
	    </td></tr>
	</table>
</div>
    <div id="tabs2" class="nui-tabs" tabAlign="left" activeIndex="0" style="width:100%;height:150px;">
    <div title="属性列表" >
           <div id="attributeType" class="nui-radiobuttonlist" repeatItems="10" repeatLayout="table"
                textField="value" valueField="key" value="protocol_type"
                url="<%=basePath%>kddcup99/listAttribute" >
            </div>    
    </div>
    <div title="入侵类型">
           <div id="intrustType" class="nui-checkboxlist" repeatItems="12" repeatLayout="table"
                textField="text" valueField="text" value="normal,snmpgetattack,named,xlock,smurf,ipsweep,multihop,xsnoop,sendmail,guess_passwd,saint,buffer_overflow,portsweep,pod,apache2,phf,udpstorm,warezmaster,perl,satan,xterm,mscan,processtable,ps,nmap,rootkit,neptune,loadmodule,imap,back,httptunnel,worm,mailbomb,ftp_write,teardrop,land,sqlattack,snmpguess"
                url="<%=basePath%>kddcup99/listLabel" >
            </div>
            <hr/>
             <div id="intrustTypeButton" name="intrustTypeButton" class="nui-checkbox" checked="true" readOnly="false" text="全选" onvaluechanged="onValueChanged"></div>
    </div>
</div>  
<script type="text/javascript">
var series0;
var xaxis;
var msg;
var attributeName
nui.parse();
//根据属性类型来选择加载数据路径
function getAttributeType(){
     msg = nui.loading("正在读取属性类型...", "提示");
     attributeName = nui.get("attributeType").getValue();
     $.ajax({
        url: "<%=basePath%>kddcup99/getAttributeType?attributeName="+attributeName,
        success: function (text) {
            nui.hideMessageBox(msg);
            if("12" == text){
            //离散属性
               clearInput();
               UnContinueValue();
            }else{
            //连续属性
               continueValue();
            }       
        },
        error: function () {
            nui.alert("加载属性类型数据失败");
        }
    });
}
//getAttributeType();
//离散类型数据处理入口
function UnContinueValue(){
     msg = nui.loading("正在加载离散属性类型分布数据...", "提示");
     var labels = nui.get("intrustType").getValue();
     $.ajax({
        url: "<%=basePath%>kddcup99/getUnContinueValue?attributeName="+attributeName + "&labels=" + labels,
        success: function (text) {
            nui.hideMessageBox(msg); 
            var obj = eval('(' + text + ')');
            var data = [];
            var xData = [];
            for(i = 0 ; i < obj.length; i++){
               data[i] = obj[i].value;
               xData[i] = obj[i].key;
            }   
            series0.setData(data);
            xaxis.setCategories(xData);
        },
        error: function () {
            nui.alert("加载离散属性数据失败");
        }
    });
}

//清除区间文本框中的数据
function clearInput(){
    for(i = 0 ; i < 11; i++){
       nui.get("step" + (i+1)).setValue("");
    }  
}

//连续类型数据处理入口
function continueValue(){
      msg = nui.loading("正在读取数据步长数据...", "加载");
      var labels = nui.get("intrustType").getValue();
      $.ajax({
        url: "<%=basePath%>kddcup99/getAttributeStep?attributeName=" + attributeName + "&labels=" + labels,
        success: function (text) {
            nui.hideMessageBox(msg);
            var obj = eval('(' + text + ')');
            for(i = 0 ; i < obj.length; i++){
               nui.get("step" + (i+1)).setValue(obj[i]);
            }   
            continueValueProcess();         
        },
        error: function () {
            nui.alert("加载步长数据失败");
        }
    });  
}
 
function continueValueProcess(){
    msg = nui.loading("正在加载连续类型分布数据...", "加载");
    var valueStep = "";
    for(i = 0;i<11;i++){
         valueStep = valueStep + nui.get("step" + (i+1)).getValue() + ",";
    }
     var labels = nui.get("intrustType").getValue();
     $.ajax({
        url: "<%=basePath%>kddcup99/getContinueValue?attributeName=" + attributeName + "&valueStep=" + valueStep + "&labels=" + labels,
        success: function (text) {
            var obj = eval('(' + text + ')');
            var data = [];
            var xData = [];
            for(i = 0 ; i < obj.length; i++){
               data[i] = obj[i].value;
               xData[i] = obj[i].key;
            }   
            series0.setData(data);
            xaxis.setCategories(xData);
            nui.hideMessageBox(msg);
        },
        error: function () {
            nui.alert("加载分布数据失败");
        }
    });
}
$(function () {
    // Set up the chart
    var chart = new Highcharts.Chart({
        chart: {
            renderTo: 'container',
            type: 'column',
            margin: 75,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 50,
                viewDistance: 25
            },
            events : {
				load : function() {
					series0 = this.series[0];
					xaxis = this.xAxis[0];
				}
			}
        },
        title: {
            text: 'kdd cup99 数据集数据分布'
        },
        xAxis: {
            categories: []
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        series: [{
            data: []
        }]
    });

    function showValues() {
        $('#R0-value').html(chart.options.chart.options3d.alpha);
        $('#R1-value').html(chart.options.chart.options3d.beta);
    }

    // Activate the sliders
    $('#R0').on('change', function () {
        chart.options.chart.options3d.alpha = this.value;
        showValues();
        chart.redraw(false);
    });
    $('#R1').on('change', function () {
        chart.options.chart.options3d.beta = this.value;
        showValues();
        chart.redraw(false);
    });

    showValues();
});

function onValueChanged(){
    if(!nui.get("intrustTypeButton").checked){
        nui.get("intrustType").setValue("");
    }else{
       nui.get("intrustType").setValue("normal,snmpgetattack,named,xlock,smurf,ipsweep,multihop,xsnoop,sendmail,guess_passwd,saint,buffer_overflow,portsweep,pod,apache2,phf,udpstorm,warezmaster,perl,satan,xterm,mscan,processtable,ps,nmap,rootkit,neptune,loadmodule,imap,back,httptunnel,worm,mailbomb,ftp_write,teardrop,land,sqlattack,snmpguess");
    }
}
</script>
    </body>
</html>
