package com.mrlonis.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AssociationTests {
    @Test
    public void shouldReturnKeyValue_whenConstructedWithKayValue() {
        int key = 0;
        long value = 1L;
        Association association = Association.builder()
                                             .key(key)
                                             .value(value)
                                             .build();
        assertEquals(key, association.getKey());
        assertEquals(value, association.getValue());
    }
}
