package com.personal.calculateTaxes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**Classe representando o modelo de operaçÕes com tipo de operação - operation (buy/sell), valor unitário - unitCost
 * e quantidade de ativos transacionados - quantity
 * */
@Data
public class Operation {
    private String operation;

    @JsonProperty("unit-cost")
    private BigDecimal unitCost;

    private Long quantity;
}
