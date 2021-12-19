package com.example.aop.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MethodInstanceOfUppercaseHandler implements InvocationHandler {
    private final Object target;

    public MethodInstanceOfUppercaseHandler(Hello target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        // 리턴 타입과 메서드 이름이 일치하는 경우에만 부가기능을 적용
        if(ret instanceof String && method.getName().startsWith("say")) {
            return ((String) ret).toUpperCase();
        } else {
            return ret;
        }
    }
}
