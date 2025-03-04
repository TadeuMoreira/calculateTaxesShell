Bem vindo a calculadora de impostos!

Projeto desenvolvido utilizando java 17 + spring shell.

Através do uso do graalVM foi criado executável do projeto, contido na pasta target,
que facilita a execução do mesmo.

Foi utilizado junit para execução dos testes unitários e Jackson para trabalhar com json.

Para compilar o projeto é necessário utilizar java 17 e maven para gerenciamento das 
dependências.

Para executar basta abrir o projeto em sua IDE preferida e rodar a classe 
CalculateTaxesApplication.java. Isso fará com que um terminal seja exibido onde o 
usuário poderá digitar os comandos padrão do spring shell (help, stacktrace, clear,
quit, history e version) ou os comandos  criados para a calculadora de impostos, 
calculateTaxesFile e calculateTaxes.

O comando para ler um arquivo contendo operações, onde cada linha
do arquivo seria um json diferente, foi criado para facilitar o teste de várias
entradas de uma só vez. Ao utilizar esse comando, o programa retornará o cálculo de 
impostos para cada linha de entrada em uma linha diferente. Se no arquivo de entrada 
existirem 3 linhas, o programa retornará 3 linhas com impostos.

Os comandos criados para calcular impostos e que podem ser utilizados no terminal ao
executar o projeto são:

1 - calculateTaxes: executa o cálculo de impostos de uma única linha de operações.
Ex.:

calculateTaxes '[{\"operation\":\"buy\",\"unit-cost\":10.00,\"quantity\":100},{\"operation\":\"sell\",\"unit-cost\":15.00,\"quantity\":50},{\"operation\":\"sell\",\"unit-cost\":15.00,\"quantity\":50}]'

retornando uma linha com os impostos:
[{"tax":0.00},{"tax":0.00},{"tax":0.00}]

2 - calculateTaxesFile: executa o cálculo de impostos de um arquivo com várias linhas
de operaçÕes.
Ex.:

calculateTaxesFile operations.txt

O caminho do arquivo deve ser inserido a  partir da raíz do projeto. Ou, para facilitar,
o arquivo pode ser inserido diretamente na raíz do projeto.

O retorno segue o modelo:
[{"tax":0.00},{"tax":80000.00},{"tax":0.00},{"tax":60000.00}]
[{"tax":0.00},{"tax":0.00},{"tax":0.00},{"tax":0.00},{"tax":3000.00},{"tax":0.00},{"tax":0.00},{"tax":3700.00},{"tax":0.00}]
retornando um array de jsons, em que cada linha consta o cálculo de impostos para a respectiva
linha do arquivo.

Muito obrigado e até breve!