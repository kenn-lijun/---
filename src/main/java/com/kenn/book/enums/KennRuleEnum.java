package com.kenn.book.enums;

import com.kenn.book.domain.Constants;
import com.kenn.book.rule.KennAbstractRule;
import com.kenn.book.rule.KennHtmlRule;
import com.kenn.book.rule.KennJsonRule;

/**
 * @Description TODO
 * @ClassName KennRuleEnum
 * @Author kenn
 * @Version 1.0.0
 * @Date 2024年06月24日 09:25:00
 */
public enum KennRuleEnum {

    HTML_RULE(Constants.RULE_TYPE_HTML, KennHtmlRule.class),
    JSON_RULE(Constants.RULE_TYPE_JSON, KennJsonRule.class)
    ;

    public Class<? extends KennAbstractRule> getRuleClazz() {
        return ruleClazz;
    }

    KennRuleEnum(String type, Class<? extends KennAbstractRule> ruleClazz) {
        this.type = type;
        this.ruleClazz = ruleClazz;
    }

    private final String type;

    private final Class<? extends KennAbstractRule> ruleClazz;

    public static Class<? extends KennAbstractRule> typeResolveToRule(String type) {
        Class<? extends KennAbstractRule> result = KennHtmlRule.class;
        for (KennRuleEnum modelTypeEnum : values()) {
            if (modelTypeEnum.type.equalsIgnoreCase(type)) {
                result = modelTypeEnum.getRuleClazz();
                break;
            }
        }
        return result;
    }

}
