package com.personal.calculateTaxes.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**Classe representando o modelo de imposto com o valor a ser pago - tax */
@Data
@Builder
public class Tax {
    private BigDecimal tax;
}
