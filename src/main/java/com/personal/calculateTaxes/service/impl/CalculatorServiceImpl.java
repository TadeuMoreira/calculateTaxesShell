package com.personal.calculateTaxes.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.personal.calculateTaxes.model.Operation;
import com.personal.calculateTaxes.model.Tax;
import com.personal.calculateTaxes.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**Serviço responsável por calcular os impostos para um arquivo contendo operaçÕes*/
@Slf4j
@Service
public class CalculatorServiceImpl implements CalculatorService {

    private static final BigDecimal ZERO = BigDecimal.valueOf(0, 2);

    /**Metódo responsável por processar uma linha contendo operações de compra e venda
     * @param line String contendo operaçÕes de compra e venda em formato json
     * @return String - retorna uma string em formato json com o valor dos impostos de cada operação
     */
    public String processLine(String line) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();
        Operation[] operations = new ObjectMapper().readValue(line, Operation[].class);
        BigDecimal averagePrice = ZERO;
        BigDecimal accumulatedLoss = ZERO;
        long stockQuantity = 0;
        List<Tax> result = new ArrayList<>();
        for (Operation o : operations) {
            Tax tax = Tax.builder().build();
            if (o.getOperation().equals("buy")) {
                tax.setTax(ZERO);
                averagePrice = calculateAveragePrice(o, averagePrice, stockQuantity);
                stockQuantity += o.getQuantity();
            } else if (isNotTaxable(o, averagePrice)) {
                tax.setTax(ZERO);
                accumulatedLoss = addLoss(o, accumulatedLoss, averagePrice);
                stockQuantity -= o.getQuantity();
            } else {
                BigDecimal profit = calculateProfit(o, averagePrice);
                if (profit.compareTo(accumulatedLoss) > 0) {
                    tax.setTax(this.calculateTax(profit, accumulatedLoss));
                } else {
                    tax.setTax(ZERO);
                }
                accumulatedLoss = deduceLoss(accumulatedLoss, profit);
                stockQuantity -= o.getQuantity();
            }
            result.add(tax);
        }
        return ow.writeValueAsString(result);
    }

    /**Metódo que verifica se uma operação não é passível de imposto ou é
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @return boolean - retorna true caso não seja passível de imposto e false caso seja
     */
    private boolean isNotTaxable(Operation o, BigDecimal averagePrice) {
        return BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost()).compareTo(BigDecimal.valueOf(20000)) <= 0 ||
                BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost()).compareTo(BigDecimal.valueOf(o.getQuantity()).multiply(averagePrice)) < 0;
    }

    /**Metódo responsável por calcular o preço médio ponderado de uma operação de compra
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @param stockQuantity long com a quantidade de ativos em estoque até o momento
     * @return BigDecimal - retorna valor atualizado do preço médio ponderado
     */
    private BigDecimal calculateAveragePrice(Operation o, BigDecimal averagePrice, long stockQuantity) {
        return ((BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost())).add(BigDecimal.valueOf(stockQuantity).multiply(averagePrice))).divide(BigDecimal.valueOf(o.getQuantity() + stockQuantity), RoundingMode.HALF_UP);
    }

    /**Metódo responsável por adicionar perdas ao prejuízo acumulado
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param accumulatedLoss BigDecimal com o valor do prejuízo acumulado até o momento
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @return BigDecimal - retorna valor atualizado do prejuízo acumulado
     */
    private BigDecimal addLoss(Operation o, BigDecimal accumulatedLoss, BigDecimal averagePrice) {
        return accumulatedLoss.add((BigDecimal.valueOf(o.getQuantity()).multiply(averagePrice)).subtract(BigDecimal.valueOf(o.getQuantity()).multiply(o.getUnitCost())));
    }

    /**Metódo responsável por deduzir lucros do prejuízo acumulado
     * @param accumulatedLoss BigDecimal com o valor do prejuízo acumulado até o momento
     * @param profit BigDecimal com o valor do lucro da operação
     * @return BigDecimal - retorna valor atualizado do prejuízo acumulado
     */
    private BigDecimal deduceLoss(BigDecimal accumulatedLoss, BigDecimal profit) {
        return accumulatedLoss.subtract(profit).compareTo(ZERO) > 0 ? accumulatedLoss.subtract(profit) : ZERO;
    }

    /**Metódo que calcular o valor de lucro de uma operação de venda
     * @param o Operation: operação contendo tipo (buy,sell), valor unitário e quantidade
     * @param averagePrice BigDecimal com o valor do preço médio até o momento
     * @return BigDecimal - retorna o valor do lucro da operação
     */
    private BigDecimal calculateProfit(Operation o, BigDecimal averagePrice) {
        return o.getUnitCost().multiply(BigDecimal.valueOf(o.getQuantity())).subtract(averagePrice.multiply(BigDecimal.valueOf(o.getQuantity())));
    }

    /**Metódo responsável por calcular os impostos de uma operação de venda
     * @param profit BigDecimal com o valor do lucro da operação
     * @param accumulatedLoss BigDecimal com o valor do prejuízo acumulado até o momento
     * @return BigDecimal - retorna valor de imposto a ser pago sobre a operação de venda
     */
    private BigDecimal calculateTax(BigDecimal profit, BigDecimal accumulatedLoss) {
        return profit.subtract(accumulatedLoss).multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
    }
}
