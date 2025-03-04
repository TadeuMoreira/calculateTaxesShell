package com.personal.calculateTaxes.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceImplTest {

    @InjectMocks private CalculatorServiceImpl service;

    private static final String INPUT = "[{\"operation\":\"buy\",\"unit-cost\":10.00,\"quantity\":10000}, {\"operation\":\"sell\",\"unit-cost\":2.00,\"quantity\":5000}, {\"operation\":\"sell\",\"unit-cost\":20.00,\"quantity\":2000}, {\"operation\":\"sell\",\"unit-cost\":20.00,\"quantity\":2000}, {\"operation\":\"sell\",\"unit-cost\":25.00,\"quantity\":1000}, {\"operation\":\"buy\",\"unit-cost\":20.00,\"quantity\":10000}, {\"operation\":\"sell\",\"unit-cost\":15.00,\"quantity\":5000}, {\"operation\":\"sell\",\"unit-cost\":30.00,\"quantity\":4350}, {\"operation\":\"sell\",\"unit-cost\":30.00,\"quantity\":650}]";

    @Test
    void should_return_taxes_string_when_valid_operation() throws JsonProcessingException {
        assertNotNull(service.processLine(INPUT));
    }

    @Test
    void should_throw_JsonProcessingException_when_invalid_json() {
        assertThrows(JsonProcessingException.class, () -> service.processLine(""));
    }
}
