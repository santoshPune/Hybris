<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="b2b-order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order"%>
<%--
    ~ /*
    ~  * [y] hybris Platform
    ~  *
    ~  * Copyright (c) 2000-2016 hybris AG
    ~  * All rights reserved.
    ~  *
    ~  * This software is the confidential and proprietary information of hybris
    ~  * ("Confidential Information"). You shall not disclose such Confidential
    ~  * Information and shall use it only in accordance with the terms of the
    ~  * license agreement you entered into with hybris.
    ~  *
    ~  */
--%>

<spring:url value="${url}" var="orderApprovalDecisionURL" htmlEscape="false" />

<b2b-order:orderApprovalDecisionPopup orderApprovalDecisionForm="${orderApprovalDecisionForm}" orderApprovalData="${orderApprovalData}" 
    orderApprovalDecisionURL="${orderApprovalDecisionURL}" 
    decision="APPROVE"
    actionButtonLabel="text.account.orderApproval.approveButton.displayName"
    actionButtonClass="btn btn-primary btn-block approverDecisionApprovalButton"
    modalPopupClass="orderApprovalApproveCommentModal"
    commentLabel="text.account.orderApproval.approve.reason" />