package com.gb.store.constant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ConstantTest {

    @Test
    void testConstantSuccess() {
        assertEquals("Success", Constant.SUCCESS);
    }
}