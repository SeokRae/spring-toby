package com.example.aop.proxy;

import com.example.aop.dynamic.Hello;
import com.example.aop.dynamic.HelloTarget;
import com.example.aop.advice.UppercaseAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicProxyTest {

    @DisplayName("스프링의 ProxyFactoryBean을 통한 부가기능 추가 테스트")
    @Test
    void testCase1() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());
        factoryBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) factoryBean.getObject();

        assertThat(proxiedHello.sayHello("SR")).isEqualTo("HELLO SR");
        assertThat(proxiedHello.sayHi("SR")).isEqualTo("HI SR");
        assertThat(proxiedHello.sayThankYou("SR")).isEqualTo("THANK YOU SR");
    }
}
