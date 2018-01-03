package com.zsm.sb.controller;

import com.zsm.sb.model.User;
import com.zsm.sb.util.AbstractSpringTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.naming.SelectorContext.prefix;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.collection.IsMapContaining.hasValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/1/2 10:40.
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest
public class SimpleControllerTest extends AbstractSpringTest
{
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    private MockHttpSession session;

    @Before
    public void setupMockMvc()
    {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession();
        User user = new User();
        user.setUserName("root");
        user.setPassword("123456");
        session.setAttribute("user", user);
    }

    /**
     * mockMvc.perform执行一个请求
     * MockMvcRequestBuilders.get(“/user/userLogin”)构造一个请求，Post请求就用.post方法
     * contentType(MediaType.APPLICATION_JSON_UTF8)代表发送端发送的数据格式是application/json;charset=UTF-8
     * accept(MediaType.APPLICATION_JSON_UTF8)代表客户端希望接受的数据类型为application/json;charset=UTF-8
     * session(session)注入一个session，这样拦截器才可以通过
     * ResultActions.andExpect添加执行完成后的断言
     * ResultActions.andExpect(MockMvcResultMatchers.status().isOk())方法看请求的状态响应码是否为200如果不是则抛异常，测试不通过
     * andExpect(MockMvcResultMatchers.jsonPath(“$.author”).value(“嘟嘟MD独立博客”))这里jsonPath用来获取author字段比对是否为嘟嘟MD独立博客,不是就测试不通过
     * ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息
     *
     * @throws Exception
     */
    @Ignore
    @Test
    @Transactional
    @Rollback(false)
    public void userLogin()
        throws Exception
    {
        //post test method
        String json = "{\"user\":\"root\",\"password\":\"123456\",\"url\":\"http://127.0.0.1:8080/\"}";
        mvc.perform(MockMvcRequestBuilders.post("user/userLogin")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .content(json.getBytes())
            .session(session))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());

        mvc.perform(MockMvcRequestBuilders.get("/user/userLogin")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .session(session)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user").value("root"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("123456"))
            .andDo(MockMvcResultHandlers.print());

        // 想判断某个字符串 s 是否含有子字符串 "user" 或 "password" 中间的一个
        assertThat("user", anyOf(containsString("user"), containsString("password")));
        // 联合匹配符not和equalTo表示“不等于”
        assertThat("user", not(equalTo("user")));
        // 联合匹配符not和containsString表示“不包含子字符串”
        assertThat("user", not(containsString("user")));
        // 联合匹配符anyOf和containsString表示“包含任何一个子字符串”
        assertThat("user", anyOf(containsString("user"), containsString("password")));
        String testValue = "user";
        String expectValue = "user";
        User user = new User();
        //字符相关匹配符
        /**equalTo匹配符断言被测的testedValue等于expectedValue，
         * equalTo可以断言数值之间，字符串之间和对象之间是否相等，相当于Object的equals方法
         */
        assertThat(testValue, equalTo(expectValue));
        /**equalToIgnoringCase匹配符断言被测的字符串testedString
         *在忽略大小写的情况下等于expectedString
         */
        assertThat(testValue, equalToIgnoringCase(expectValue));
        /**equalToIgnoringWhiteSpace匹配符断言被测的字符串testValue
         *在忽略头尾的任意个空格的情况下等于expectedString，
         *注意：字符串中的空格不能被忽略
         */
        assertThat(testValue, equalToIgnoringWhiteSpace(expectValue));
        /**containsString匹配符断言被测的字符串testValue包含子字符串subString**/
        assertThat(testValue, containsString(expectValue));
        /**endsWith匹配符断言被测的字符串testValue以子字符串suffix结尾*/
        assertThat(testValue, endsWith(expectValue));
        /**startsWith匹配符断言被测的字符串testValue以子字符串prefix开始*/
        assertThat(testValue, startsWith(prefix));
        //一般匹配符
        /**nullValue()匹配符断言被测object的值为null*/
        assertThat(testValue, nullValue());
        /**notNullValue()匹配符断言被测object的值不为null*/
        assertThat(testValue, notNullValue());
        /**is匹配符断言被测的object等于后面给出匹配表达式*/
        assertThat(testValue, is(equalTo(expectValue)));
        /**is匹配符简写应用之一，is(equalTo(x))的简写，断言testedValue等于expectedValue*/
        assertThat(testValue, is(expectValue));
        /**is匹配符简写应用之二，is(instanceOf(SomeClass.class))的简写，断言testedObject为Cheddar的实例*/
        assertThat(user, is(instanceOf(User.class)));
        /**not匹配符和is匹配符正好相反，断言被测的object不等于后面给出的object*/
        assertThat(testValue, not(expectValue));
        /**allOf匹配符断言符合所有条件，相当于“与”（&&）*/
        assertThat(12, allOf(greaterThan(8), lessThan(16)));
        /**anyOf匹配符断言符合条件之一，相当于“或”（||）*/
        assertThat(12, anyOf(greaterThan(16), lessThan(8)));
        //数值相关匹配符
        /**closeTo匹配符断言被测的浮点型数testedDouble在20.0¡À0.5范围之内*/
        assertThat(12.0, closeTo(20.0, 0.5));
        /**greaterThan匹配符断言被测的数值testedNumber大于16.0*/
        assertThat(12.0, greaterThan(16.0));
        /** lessThan匹配符断言被测的数值testedNumber小于16.0*/
        assertThat(12.0, lessThan(16.0));
        /** greaterThanOrEqualTo匹配符断言被测的数值testedNumber大于等于16.0*/
        assertThat(12.0, greaterThanOrEqualTo(16.0));
        /** lessThanOrEqualTo匹配符断言被测的testedNumber小于等于16.0*/
        assertThat(12.0, lessThanOrEqualTo(16.0));

        /**集合匹配**/
        User test1 = new User();
        User test2 = new User();
        List<User> user1 = new ArrayList<User>();
        user1.add(test1);
        user1.add(test2);
        Map<String, User> userMap = new HashMap<String, User>();
        userMap.put(test1.getUserName(), test1);
        userMap.put(test2.getUserName(), test2);
        //测试集合中是否含有指定元素
        assertThat(user1, hasItem(test1));
        //测试map中是否还有指定键值对
        assertThat(userMap, hasEntry(test1.getUserName(), test1));
        //测试map中是否还有指定键
        assertThat(userMap, hasKey(test2.getUserName()));
        //测试map中是否还有指定值
        assertThat(userMap, hasValue(test2));
    }

}
