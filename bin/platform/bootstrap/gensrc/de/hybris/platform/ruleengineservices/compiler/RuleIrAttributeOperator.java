package de.hybris.platform.ruleengineservices.compiler;
 
public enum RuleIrAttributeOperator   
{
 
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.EQUAL("==")</code> value defined at extension <code>ruleengineservices</code>. */
    EQUAL("==") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.NOT_EQUAL("!=")</code> value defined at extension <code>ruleengineservices</code>. */
    NOT_EQUAL("!=") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.GREATER_THAN(">")</code> value defined at extension <code>ruleengineservices</code>. */
    GREATER_THAN(">") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.GREATER_THAN_OR_EQUAL(">=")</code> value defined at extension <code>ruleengineservices</code>. */
    GREATER_THAN_OR_EQUAL(">=") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.LESS_THAN("<")</code> value defined at extension <code>ruleengineservices</code>. */
    LESS_THAN("<") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.LESS_THAN_OR_EQUAL("<=")</code> value defined at extension <code>ruleengineservices</code>. */
    LESS_THAN_OR_EQUAL("<=") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.IN("in")</code> value defined at extension <code>ruleengineservices</code>. */
    IN("IN") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.NOT_IN("not in")</code> value defined at extension <code>ruleengineservices</code>. */
    NOT_IN("NOT IN") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.CONTAINS("contains")</code> value defined at extension <code>ruleengineservices</code>. */
    CONTAINS("CONTAINS") ,  
    /** <i>Generated enum value</i> for <code>RuleIrAttributeOperator.NOT_CONTAINS("not contains")</code> value defined at extension <code>ruleengineservices</code>. */
    NOT_CONTAINS("NOT CONTAINS");  
 
    final private String code;
 
    private RuleIrAttributeOperator(String code) {
        this.code = code;
    }
 
    public String getCode() {
        return code;
    }
 
    public static RuleIrAttributeOperator fromCode(String code) {
        switch (code) {
            case "==":
                return EQUAL;
            case "!=":
                return NOT_EQUAL;
            case ">":
                return GREATER_THAN;
            case ">=":
                return GREATER_THAN_OR_EQUAL;
            case "<":
                return LESS_THAN;
            case "<=":
                return LESS_THAN_OR_EQUAL;
            case "in":
                return IN;
            case "not in":
                return NOT_IN;
            case "contains":
                return CONTAINS;
            case "not contains":
                return NOT_CONTAINS;
                 
        }
 
        throw new IllegalArgumentException("Unknown code \"" + code + "\"");
    }
}