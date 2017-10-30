<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Verification Code</title>

</head>
<body style="font-family: arial;">

	<img alt="logo"
		src="/yacceleratorstorefront/_ui/addons/chineseprofileaddon/responsive/common/images/mock/logo-hybris-responsive.png" />

<br/><br/><br/><br/><br/><br/>
<c:if test="${not empty verificationData.mobileNumber}">
	Mobile Number : ${verificationData.mobileNumber}<br/>
</c:if>
Verification Code : ${verificationData.verificationCode}

</body>