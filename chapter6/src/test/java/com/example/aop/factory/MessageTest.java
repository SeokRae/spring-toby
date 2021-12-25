package com.example.aop.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @DisplayName("팩토리 메서드를 통한 객체 생성")
    @Test
    void testCase1() {
        Message message = Message.newMessage("메시지");
        assertThat(message.getText()).isEqualTo("메시지");
    }
}