<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>考勤转指纹</title>
    <style type="text/css">
    	.title{
    		font-size: 36px;
    	}
    	.subtitle{
    		font-size: 14px;
    		color: red;
    	}
    </style>
</head>
<body>
<form action="/download" method="post" enctype="multipart/form-data">
	
    <label class='title'>考勤转指纹</label><label class='subtitle'>(日期用文本格式)</label><br/>
    <label>请输入年：</label>
    <input type="text" name="year"><br/>
    <label>请输入月：</label>
    <input type="text" name="month"><br/>
    <label>请选择考勤：</label>
    <input type="file" name="file"><br/><br/>
    <button type="submit">确定</button>
</form>
</body>
</html>