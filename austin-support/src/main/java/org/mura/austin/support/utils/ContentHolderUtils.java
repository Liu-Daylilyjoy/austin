package org.mura.austin.support.utils;

import org.jetbrains.annotations.Nullable;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.PropertyPlaceholderHelper;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 *
 * 内容占位符替换，用于消息的动态参数
 *
 * austin占位符格式{$var}
 */
public class ContentHolderUtils {
    /**
     * 占位符前缀
     */
    private static final String PLACEHOLDER_PREFIX = "{$";

    /**
     * 占位符后缀
     */
    private static final String PLACEHOLDER_SUFFIX = "}";

    /**
     * TODO spring_SPEL表达式
     */
    private static final StandardEvaluationContext EVALUATION_CONTEXT;

    private static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER_HELPER = new PropertyPlaceholderHelper(
            PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX
    );

    static {
        EVALUATION_CONTEXT = new StandardEvaluationContext();
        EVALUATION_CONTEXT.addPropertyAccessor(new MapAccessor());
    }

    /**
     * 用Map中的参数代替template的占位符
     */
    public static String replacePlaceHolder(final String template, final Map<String, String> paramMap) {
        return PROPERTY_PLACEHOLDER_HELPER.replacePlaceholders(template,
                new CustomPlaceholderResolver(paramMap));
    }

    private static class CustomPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private final Map<String, String> PARAM_MAP;

        public CustomPlaceholderResolver(Map<String, String> paramMap) {
            super();
            this.PARAM_MAP = paramMap;
        }

        /**
         * 会将template中找到的占位符作为参数传入
         * 会将返回值作为填充值代替占位符
         */
        @Override
        public String resolvePlaceholder(@Nullable String placeholderName) {
            String value = PARAM_MAP.get(placeholderName);
            if (null == value) {
                String errorStr = MessageFormat.format("template:{} not exist! paramMap:{}",
                        placeholderName, PARAM_MAP.toString());

                throw new IllegalArgumentException(errorStr);
            }

            return value;
        }
    }
}