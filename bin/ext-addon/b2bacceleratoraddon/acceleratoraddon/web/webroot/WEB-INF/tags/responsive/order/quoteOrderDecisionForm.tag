<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="quoteOrderDecisionURL" required="true" type="java.lang.String"%>
<%@ attribute name="orderCode" required="true" type="java.lang.String"%>
<%@ attribute name="selectedQuoteDecision" required="true" type="java.lang.String"%>
<%@ attribute name="modalTitleLabelKey" required="false" type="java.lang.String"%>
<%@ attribute name="modalInputLabelKey" required="false" type="java.lang.String"%>
<%@ attribute name="modalCommentRequired" required="false" type="java.lang.Boolean"%>
<%@ attribute name="decisionLabelKey" required="true" type="java.lang.String"%>
<%@ attribute name="decisionButtonCSSClass" required="false" type="java.lang.String"%>
<%@ attribute name="displayAsLink" required="true" type="java.lang.Boolean"%>
<%@ attribute name="mobileText" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="commentMaxChars" value="255" />
<c:set var="element" value="${displayAsLink ? 'a' : 'button'}"/>
<c:set var="commentRequired" value="${(empty modalCommentRequired) ? false : modalCommentRequired}"/>
<c:set var="isAcceptAndOrder" value="${selectedQuoteDecision eq 'ACCEPTQUOTE'}"/>

<form:form method="post" cssClass="quoteOrderDecisionForm" commandName="quoteOrderDecisionForm" action="${quoteOrderDecisionURL}">
    <input type="hidden" name="orderCode" value="${orderCode}" />
    <input type="hidden" name="selectedQuoteDecision" value="${selectedQuoteDecision}" />
        <${element} ${displayAsLink ? ' href="#"' : ''} class="${decisionButtonCSSClass} ${isAcceptAndOrder ? '' : ' decisionButton'}"
                data-modal-title-label="<spring:theme code='${modalTitleLabelKey}' />"
                data-modal-input-label="<spring:theme code='${modalInputLabelKey}' />"
                data-modal-comment-required="${commentRequired}">
            <span><spring:theme code="${decisionLabelKey}" /></span>
            <c:if test="${not empty mobileText}">
                <span><spring:theme code="${mobileText}" /></span>
            </c:if>
        </${element}>
    <c:choose>
        <c:when test="${not isAcceptAndOrder}">
            <div style="display:none">
                <div class="quoteCommentModal comment-modal">
                    <div class="headline">&nbsp;</div>

                    <textarea name="comments" maxlength="${commentMaxChars}"></textarea>

                    <div class="help-block">
                        <spring:theme code="text.quotes.comment.maxChars.label" arguments="${commentMaxChars}"/>
                    </div>
                    <div class="modal-actions">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 col-sm-push-6">
                                <button type="submit" class="btn btn-primary btn-block submitQuoteCommentButton">
                                    <spring:theme code="text.quotes.submitButton.displayName"/>
                                </button>
                            </div>
                            <div class="col-xs-12 col-sm-6 col-sm-pull-6">
                                <button type="button" class="btn btn-default btn-block cancelQuoteCommentButton">
                                    <spring:theme code="text.quotes.cancelButton.displayName"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <input type="hidden" name="comments" value="" />
        </c:otherwise>
    </c:choose>
</form:form>