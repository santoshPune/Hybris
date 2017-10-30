<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chinese PSP Alipay Payment Service Mock</title>
<script type="text/javascript" src="/${storefront}/_ui/addons/chinesepspalipaymockaddon/shared/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/${storefront}/_ui/addons/chinesepspalipaymockaddon/shared/js/chinesepspalipaymockaddon.js"></script>

</head>
<body style="font-family: arial;">

	<img alt="logo" src="/yacceleratorstorefront/_ui/addons/chinesepspalipaymockaddon/shared/images/mock/logo-hybris-responsive.png" />

	<h2>Chinese PSP Alipay Payment Service Mock</h2>
	<h4>Send order refund request</h4>

	

	<form id="refundMock" method="POST">
				
		<label>Order #:</label>
		<input id="orderCode" name="orderCode" type="text" /> 
		<input type="submit" name="action" value="Next" />

	</form>
	</body>
</html>

