package com.houkunlin.system.applog;

import com.houkunlin.system.applog.bean.Form;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppLogTemplateParserDefaultImplTest {
    private final AppLogTemplateParser<EvaluationContext> templateParser = new AppLogTemplateParserDefaultImpl(new TemplateParserContext());
    @Autowired
    private BeanFactory beanFactory;

    @Test
    void parseExpression() {
        RootObject rootObject = new RootObject(null, new Object[0], null, null, new Form(1L, "姓名"), null);
        final StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        context.setVariable("id", "测试方法值（变量）");
        try {
            context.registerFunction("hasText", StringUtils.class.getMethod("hasText", CharSequence.class));
            context.registerFunction("trimWhitespace", StringUtils.class.getMethod("trimWhitespace", String.class));
        } catch (NoSuchMethodException ignored) {
        }

        assertEquals("测试内容", templateParser.parseExpression("测试内容", context));
        assertEquals("增加用户 姓名", templateParser.parseExpression("增加用户 #{result.name}", context));
        assertEquals("增加用户 姓名", templateParser.parseExpression("增加用户 #{#root.result.name}", context));
        assertEquals("测试 无方法", templateParser.parseExpression("测试 #{method?.name?:'无方法'}", context));
        assertEquals("测试 测试方法值（变量）", templateParser.parseExpression("测试 #{#id}", context));
        assertEquals("测试 2024-01-01 00:00:00", templateParser.parseExpression("测试 #{@testBean.now()}", context));
        assertEquals("测试 否", templateParser.parseExpression("测试 #{#hasText('')?'是':'否'}", context));
        assertEquals("测试 是", templateParser.parseExpression("测试 #{#hasText('123')?'是':'否'}", context));
    }
}
