package com.personal.calculateTaxes.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.calculateTaxes.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CalculatorCommandsTest {

    @InjectMocks
    private CalculatorCommands commands;

    @Mock
    private CalculatorService service;

    private static final String INPUT = "[{\"operation\":\"buy\",\"unit-cost\":10.00,\"quantity\":10000}, {\"operation\":\"sell\",\"unit-cost\":2.00,\"quantity\":5000}, {\"operation\":\"sell\",\"unit-cost\":20.00,\"quantity\":2000}, {\"operation\":\"sell\",\"unit-cost\":20.00,\"quantity\":2000}, {\"operation\":\"sell\",\"unit-cost\":25.00,\"quantity\":1000}, {\"operation\":\"buy\",\"unit-cost\":20.00,\"quantity\":10000}, {\"operation\":\"sell\",\"unit-cost\":15.00,\"quantity\":5000}, {\"operation\":\"sell\",\"unit-cost\":30.00,\"quantity\":4350}, {\"operation\":\"sell\",\"unit-cost\":30.00,\"quantity\":650}]";

    private static final String OUTPUT = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00}]";

    private final File INPUT_FILE = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("test.txt")).getFile());

    private static final String OUTPUT_FILE = OUTPUT.concat("\n").concat(OUTPUT.concat("\n")).concat(OUTPUT.concat("\n"));

    @Test
    void should_return_json_when_valid_file() throws FileNotFoundException, JsonProcessingException {
        when(service.processLine(any())).thenReturn(OUTPUT);
        assertEquals(OUTPUT_FILE, commands.calculateTaxesFile(INPUT_FILE));
    }

    @Test
    void should_throw_FileNotFoundException_when_invalid_file() {
        assertThrows(FileNotFoundException.class, () -> commands.calculateTaxesFile(new File("")));
    }

    @Test
    void should_throw_JsonProcessingException_when_invalid_file() throws JsonProcessingException {
        when(service.processLine(any())).thenThrow(JsonProcessingException.class);
        assertThrows(JsonProcessingException.class, () -> commands.calculateTaxesFile(INPUT_FILE));
    }

    @Test
    void should_return_json_when_valid_line() throws JsonProcessingException {
        when(service.processLine(any())).thenReturn(OUTPUT);
        assertEquals(OUTPUT, commands.calculateTaxes(INPUT));
    }

    @Test
    void should_throw_JsonProcessingException_when_invalid_line() throws JsonProcessingException {
        when(service.processLine(any())).thenThrow(JsonProcessingException.class);
        assertThrows(JsonProcessingException.class, () -> commands.calculateTaxes(INPUT));
    }
}
