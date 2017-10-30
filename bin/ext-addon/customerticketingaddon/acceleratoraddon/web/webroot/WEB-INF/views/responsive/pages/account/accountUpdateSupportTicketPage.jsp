<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="back-link border">
    <a href="../support-tickets">
        <span class="glyphicon glyphicon-chevron-left"></span>
    </a>
    <span class="label"><spring:theme code="text.account.supportTicket.updateTicket.heading" text="Request Customer Support"/></span>
</div>

<div class="well well-lg well-tertiary">
    <div class="col-sm-12 col-no-padding">
        <div class="col-sm-3 item-wrapper">
            <div class="item-group">
                <span class="item-label">
                    <spring:message code="text.account.supporttickets.createTicket.subject" text="Subject"/>
                </span>
                <span class="item-value">
                    ${fn:escapeXml(ticketData.subject)}
                </span>
            </div>
        </div>

        <div class="col-sm-3 item-wrapper">
            <div class="item-group">
                <span class="item-label">
                    <spring:theme code="text.account.supporttickets.dateUpdated" text="Date Updated" />
                </span>
                <span class="item-value">
                    <fmt:formatDate value="${ticketData.lastModificationDate}" pattern="dd-MM-yy hh:mm a" />
                </span>
            </div>
        </div>

        <div class="col-sm-3 item-wrapper">
            <div class="item-group">
                <span class="item-label">
                    <spring:theme code="text.account.supporttickets.dateCreated" text="Date Created" />
                </span>
                <span class="item-value">
                    <fmt:formatDate value="${ticketData.creationDate}" pattern="dd-MM-yy hh:mm a" />
                </span>
            </div>
        </div>

        <div class="col-sm-3 item-wrapper">
            <div class="item-group">
                <span class="item-label">
                    <spring:theme code="text.account.supporttickets.status" text="Status" />
                </span>
                <span class="item-value">
                    <spring:message code="ticketstatus.${fn:toUpperCase(ticketData.status.id)}"/>
                </span>
            </div>
        </div>
        
        <!-- Ticket Categories -->
        <c:if test="${not empty ticketData.ticketCategory }">
        	<div class="col-sm-3 item-wrapper">
	            <div class="item-group">
	                <span class="item-label">
        				<spring:theme code="text.account.supporttickets.createTicket.ticketCategory" text="Category"/>
        			</span>
        			<span class="item-value">
						<spring:theme code="text.account.supporttickets.createTicket.ticketCategory.${ticketData.ticketCategory}"/>
        			</span>
        		</div>
        	</div>
        </c:if>

		<!-- Associated Objects -->
        <c:if test="${not empty ticketData.associatedTo}">
	        <div class="col-sm-3 item-wrapper">
	            <div class="item-group">
	                <span class="item-label">
	                    <spring:theme code="text.account.supporttickets.createTicket.associatedTo" text="Associated To"/>
	                </span>
	                <span class="item-value">
	                    ${fn:substringBefore(fn:toUpperCase(ticketData.associatedTo), ';')}
	                </span>
	            </div>
	        </div>
		</c:if>
    </div>
</div>

<div class="row customer-ticketing-body">
    <div class="col-xs-12">
        <div class="account-section-header account-section-header-secondary">
            <spring:theme code="text.account.supporttickets.messages" text="Messages" />
            <div class="account-section-header-add pull-right">
                <a href="#" class="ct-add-new-msg-btn"><spring:theme code="text.account.supporttickets.updateTicket.addMessage" text="Add Message" /></a>
            </div>
        </div>

        <div class="cts-msg-history">
            <div style="display:none" id="ct-overlay-title">
                <div class="headline"><span class="headline-text"><spring:theme code="text.account.supporttickets.updateTicket.addMessage" text="Add Message" /></span></div>
            </div>
            
            <spring:hasBindErrors name="supportTicketForm">
				<div class="updateSupportTicketValidationErrors" ></div>
			</spring:hasBindErrors>

            <div style="display:none;" id="ct-add-new-msg">
                <form:form method="post" commandName="supportTicketForm" enctype="multipart/form-data">
                    <form:hidden path="subject" value="${ticketData.subject}"/>
                    <input id="currentTicketStatus" type="hidden" value="${ticketData.status.id}">
                    <c:if test="${ticketData.status.id ne 'CLOSED'}">
                        <form:hidden path="id" value="${ticketData.id}"/>
                        <formElement:formTextArea idKey="message" labelKey="text.account.supporttickets.createTicket.message" path="message" areaCSS="form-control js-add-message" labelCSS="control-label"/>
                    </c:if>
                    <div class="form-group">
                        <label for="text.account.supportTicket.updateTicket.status" class="control-label"><spring:message code="text.account.supportTicket.updateTicket.status" text="Status"/> </label>
                        <form:select path="status"  cssClass="form-control js-add-message-status">
                            <form:option value="${ticketData.status.id}"><spring:message code="ticketstatus.${fn:toUpperCase(ticketData.status.id)}"/> (<spring:message code="text.account.supporttickets.currentStatus" text="Current Status"/>)</form:option>
                            <c:forEach items="${ticketData.availableStatusTransitions}" var="status">
                                <form:option value="${status.id}" ><spring:message code="ticketstatus.box.${status.id}"/> </form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                    
                    <c:if test="${ticketData.status.id ne 'COMPLETED'}">
	                    <div style="display:block;" class="cs_file_upload form-group file-upload js-file-upload">
	                        <label class="control-label file-upload__label" for="files"> <spring:theme code="text.account.supporttickets.createTicket.selectFile" text="Select a file"/> </label>
	                        <div class="file-upload__wrapper btn btn-default btn-small">
	                            <span> <spring:theme code="text.account.supporttickets.createTicket.chooseFile" text="Choose file"/> </span>
	                            <input type="file" name="files" multiple size="60" class="file-upload__input js-file-upload__input"/>
	                        </div>
	                        <span class="file-upload__file-name js-file-upload__file-name">
	                            <spring:theme code="text.account.supporttickets.createTicket.noFileChosen"/>
	                        </span>
	                    </div>
                    </c:if>

                    <div class="modal-actions row">
                        <div class="col-xs-12 col-sm-6 col-sm-push-6">
                            <c:if test="${ticketData.status.id ne 'CLOSED'}">
                                <ycommerce:testId code="supportTicket_update_button">
                                    <button class="btn btn-primary btn-block" id="updateTicket" type="submit" disabled>
                                        <spring:theme code="text.account.supporttickets.createTicket.submit" text="Submit"/>
                                    </button>
                                </ycommerce:testId>
                            </c:if>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-sm-pull-6">
                            <a class="btn btn-default btn-block closeColorBox">
                            Cancel</a>
                        </div>

                    </div>
                </form:form>
            </div>


            <c:choose>
                <c:when test="${not empty ticketData.messageHistory}">
                    <div class="form-group">
                        <label class="control-label"><spring:message code="text.account.supporttickets.createTicket.message.history"/></label>
                        <p class="form-control-static">${ticketData.messageHistory}</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:set var="lastModificationDate" scope="page">
                            <fmt:formatDate value="${ticketData.lastModificationDate}" pattern="dd-MM-yy hh:mm a" />
                        </c:set>
                        <c:set var="lastModificationDateDetailed" scope="page">
                            <fmt:formatDate value="${ticketData.lastModificationDate}" pattern="dd-MM-yy hh:mm:ss" />
                        </c:set>


                        <c:forEach items="${ticketData.ticketEvents}" var="ticketEventData" varStatus="loop">

                            <c:set var="msgCreationDate" scope="page">
                                <fmt:formatDate value="${ticketEventData.startDateTime}" pattern="dd-MM-yy hh:mm a" />
                            </c:set>
                            <c:set var="msgCreationDateDetailed" scope="page">
                                <fmt:formatDate value="${ticketEventData.startDateTime}" pattern="dd-MM-yy hh:mm:ss" />
                            </c:set>

                            <c:set var="cssLine2" scope="page" value="well well-sm well-quaternary"/>
                            <c:if test="${(lastModificationDateDetailed eq msgCreationDateDetailed)}">
                                <c:set var="cssLine2" scope="page" value="well well-sm well-secondary"/>
                            </c:if>

                            <c:choose>
                                <c:when test="${ticketEventData.author eq 'Customer Service'}">
                                    <c:set var="cssLine1" scope="page" value="cts-msg-history-item cts-msg-history-item-agent col-md-7 col-md-offset-5 col-sm-9 col-sm-offset-3 ct-msg-visible"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="cssLine1" scope="page" value="cts-msg-history-item cts-msg-history-item-customer col-md-7 col-sm-9 ct-msg-visible"/>
                                </c:otherwise>
                            </c:choose>
                            <div class="${cssLine1}">
                                <div class="cts-msg-history-item-info">
                                    <b>${fn:escapeXml(ticketEventData.author)}</b>
                                    <spring:theme code="text.account.supporttickets.updateTicket.on" text="on" />&nbsp;${msgCreationDate}
                                    <c:if test="${not empty ticketEventData.modifiedFields }">
                                        <c:forEach items="${ticketEventData.modifiedFields }" var="modifiedField">
                                            <span class="cts-msg-history-item-modified-field">${fn:escapeXml(modifiedField.key)}: <span class="cts-msg-history-item-modified-field--status cts-msg-history-item-modified-field--status-${fn:toLowerCase(modifiedField.value.get(1).id)}"><spring:message code="ticketstatus.${fn:toUpperCase(modifiedField.value.get(1).id)}"/></span> </span>
                                        </c:forEach>
                                    </c:if>
                                </div>
                                <div class="${cssLine2}">
                                    <c:if test="${not empty ticketEventData.text }">
                                    	<div class="cts-msg-history-item-msg">${fn:escapeXml(ticketEventData.text)}</div>
                                    </c:if>

                                    <c:if test="${not empty ticketEventData.attachments}">
                                        <ul class="cts-attach-list">
                                            <c:forEach items="${ticketEventData.attachments}" var="attachment">
                                                <li class="cts-attach-file">
                                                    <a href="${attachment.URL}" target="_new" download="${fn:escapeXml(attachment.filename)}"><span class="glyphicon glyphicon-paperclip"></span>${fn:escapeXml(attachment.filename)}</a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>