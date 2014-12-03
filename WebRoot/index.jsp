<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>自开发实验平台集</title>
    <style type="text/css">
    body{
        margin:10;padding:10;border:0;width:100%;height:100%;overflow:hidden;
    }
    ul{
        width: 100%;
    }
    ul li{
        height:40px;
        width:100%;
    }   
    </style> 
	</head>

	<body>
	<br>
	<h3>自开发实验平台集</h3>
		<ul style="padding: 10px">
			<li>
				<a href="<%=basePath%>bpnn.jsp">BP神经网络样本训练实验</a>
			</li>
			<li>
				<a href="<%=basePath%>abc.jsp">人工蜂群算法平面选址实现</a>
			</li>
			<li>
				<a href="<%=basePath%>adhoc.jsp">Ad-Hoc移动网络渐进稳定性分析</a>
			</li>
			<li>
				<a href="<%=basePath%>evaluate.jsp">淘宝信用评价</a>
			</li>
			<li>
				<a href="<%=basePath%>kddcup99.jsp">Kdd Cup99 入侵检测数据集分析</a>
			</li>
		</ul>
	</body>
</html>
