package com.example.aop.proxy;

import com.example.aop.advice.UppercaseAdvice;
import com.example.aop.dynamic.Hello;
import com.example.aop.dynamic.HelloTarget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.assertj.core.api.Assertions.assertThat;

class NameMatchClassMethodPointcutTest {

    @DisplayName("확장 Custom 포인트 컷 테스트")
    @Test
    void testCase1() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchClassMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {}
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {}
        checkAdviced(new HelloToby(), classMethodPointcut, true);

    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("Seok")).isEqualTo("HELLO SEOK");
            assertThat(proxiedHello.sayHi("Seok")).isEqualTo("HI SEOK");
            assertThat(proxiedHello.sayThankYou("Seok")).isEqualTo("Thank You Seok");
        } else {
            assertThat(proxiedHello.sayHello("Seok")).isEqualTo("Hello Seok");
            assertThat(proxiedHello.sayHi("Seok")).isEqualTo("Hi Seok");
            assertThat(proxiedHello.sayThankYou("Seok")).isEqualTo("Thank You Seok");
        }
    }
}