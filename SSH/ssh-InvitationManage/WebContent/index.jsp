<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>论坛管理系统</title>
<link rel="stylesheet" href="static/css/bootstrap.css">
<link rel="stylesheet" href="static/css/index.css">
</head>
<body>
	<h1 class="title">帖子列表</h1>
	<div class="search-box">
		<span>帖子标题：</span>
		<input type="text" id="title">
		<input type="button" value="搜索" id="search">
	</div>
	<table class="table table-striped">
		<thead>
			<tr>
				<td>标题</td>
				<td>内容摘要</td>
				<td>作者</td>
				<td>发布时间</td>
				<td>操作</td>
			</tr>
		</thead>
		<tbody id="dataBox">
		
		</tbody>
	</table>
	<div class="pageBox">
		第<span id="pageIndex"></span>/<span id="totalPage"></span>页
		<div style="float:right;">
			<a href="#" id="first">首页</a>
			<a href="#" id="previous">上一页</a>
			<a href="#" id="next">下一页</a>
			<a href="#" id="last">末页</a>
		</div>
	</div>
</body>
<script type="text/javascript" src="static/js/jquery-2.1.4.js"></script>
<script type="text/javascript" src="static/js/index.js"></script>
</html>