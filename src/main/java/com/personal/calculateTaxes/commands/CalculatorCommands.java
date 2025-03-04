package com.personal.calculateTaxes.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.calculateTaxes.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**Classe responsável por criar os comandos que serão executados a partir do terminal*/
@ShellComponent
@Slf4j
public class CalculatorCommands {

    @Autowired
    CalculatorService calculatorService;

    /**Método responsável por ler um arquivo a partir do terminal contendo compras e vendas e calcular os impostos
     * @param file - arquivo contendo operaçÕes de compra e venda em formato json
     * @return String - retorna uma string em formato json com o valor dos impostos de cada operação
     */
    @ShellMethod(key = "calculateTaxesFile", value = "Provides a json output of taxes to be paid given a file input with buy and sell operations")
    public String calculateTaxesFile(@ShellOption File file) throws FileNotFoundException, JsonProcessingException {
        Scanner reader = new Scanner(file);
        String taxes = "";
        while (reader.hasNextLine()) {
            taxes = taxes.concat(this.calculateTaxes(reader.nextLine())).concat("\n");
        }
        return taxes;
    }

    /**Método responsável por recer um json a partir do terminal contendo compras e vendas e calcular os impostos
     * @param operation - String contendo operaçÕes de compra e venda em formato json
     * @return String - retorna uma string em formato json com o valor dos impostos de cada operação
     */
    @ShellMethod(key = "calculateTaxes", value = "Provides a json output of taxes to be paid given a json input with buy and sell operations")
    public String calculateTaxes(@ShellOption String operation) throws JsonProcessingException {
        return calculatorService.processLine(operation);
    }
}
