package com.example.aop.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ObjectInstanceOfUppercaseHandler implements InvocationHandler {
    // 어떤 종류의 인터페이스를 구현한 타깃에도 적용 가능하도록 Object 타입으로 수정
    private final Object target;

    public ObjectInstanceOfUppercaseHandler(Hello target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 호출한 메서드의 리턴 타입이 String인 경우에만 대문자 변경 기능을 적용하도록 수정
        Object ret = method.invoke(target, args);
        if (ret instanceof String) {
            return ((String) ret).toUpperCase();
        } else {
            return ret;
        }
    }
}
