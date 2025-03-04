package com.personal.calculateTaxes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

/**Serviço responsável por calcular os impostos para um arquivo contendo operaçÕes*/
@Service
public interface CalculatorService {

    /**Metódo responsável por processar uma linha contendo operações de compra e venda
     * @param line String contendo operaçÕes de compra e venda em formato json
     * @return String - retorna uma string em formato json com o valor dos impostos de cada operação
     */
    String processLine(String line) throws JsonProcessingException;

}