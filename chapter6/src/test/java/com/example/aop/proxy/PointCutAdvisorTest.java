package com.example.aop.proxy;

import com.example.aop.dynamic.Hello;
import com.example.aop.dynamic.HelloTarget;
import com.example.aop.dynamic.UppercaseAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.assertj.core.api.Assertions.assertThat;

public class PointCutAdvisorTest {

    @DisplayName("포인트컷 어드바이저 테스트")
    @Test
    void testCase1() {
        ProxyFactoryBean factoryBean = new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) factoryBean.getObject();

        assertThat(proxiedHello.sayHello("SR")).isEqualTo("HELLO SR");
        assertThat(proxiedHello.sayHi("SR")).isEqualTo("HI SR");
        assertThat(proxiedHello.sayThankYou("SR")).isEqualTo("Thank You SR");
    }
}
