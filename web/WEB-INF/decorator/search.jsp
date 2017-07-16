<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<title>search</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
v\:* { behavior: url(#default#VML); }
</style>
<link rel="shortcut icon" href="/favicon.ico" />

<decorator:head />
</head>
<body <decorator:getProperty property="body.onload" writeEntireProperty="true" /> />
<div id="wrap">
    <page:applyDecorator name="panel" page="/com/top.htm?type=menu" />
	<page:applyDecorator name="panel" page="/com/subtop.htm?type=submenu" />
	<hr class="clear" />
	<div id="contents"> 
	<decorator:body />
	</div>
</div>
</body>
</html>