# plp-project-26.1
IN1007 Project

# Escopo

Esse projeto tem como objetivo adicionar à [Linguagem Funcional 1](https://augustosampaio.github.io/PLP/linguagens/funcional1) o operador Null, a capacidade de Null safety, e operadores presentes em linguagens modernas que estão relacionados ao conceito de Null ("??", "!", "??="), além do operador ternário.

Abaixo temos o que será implementado pela linguagem em mais detalhes:

- **Nullable type:** Variáveis podem ser reassinaladas para o valor Null, desde que tenham a declaração com keyword "optional".

  Exemplo
  ```java
  let optional var y = 2, var y = null
  ```

- **Null safety:** Caso uma operação tenha risco de causar erro de execução por causa do Null, ela lança um erro de compilação em vez disso.

  Exemplo 1:
  ```java
  let optional var y = 2 in
    let var x = y + 3 in
      x
  // y pode ser null, potencialemente causando erro de execução
  // Logo, o ambiente de compilação vai perceber o problema e lançar um erro de compilação
  ```

  Exemplo 2:
  ```java
  let optional var y = 2 in
    if y !== null then
      let var x = y + 3 in
        x
  // Com o if, garantimos que y não pode ser null, excluindo a possibilidade de um erro de execução causado pelo null
  // Logo, o ambiente de compilação não vai lançar um erro
  ```


- **Null coalescing:** Operador binário ("??") que retorna o lado direito da operação caso o operador seja null, ou o esquerdo caso não seja null. Valor "default".

  Exemplo:
  ```java
  let optional var y = 2 in
    let var x = y ?? 3 in
      x
  // y pode ser null (nesse caso tem um valor)
  // Caso y seja null, x recebe 3
  // Caso não, x recebe y, que é 2
  // x vale 2
  ```

 - **Operador Ternário:** Operador "? :" que atua como um if-then-else.

    Exemplo:
    ```java
    let var a = 1, var b = 2, var c = a == b ? 3 : 4 
    ```
  
  
- **Null assertion:** Colocando a keyword "!" após acessar a variável, garatimos ao compilador que o valor dela não é nulo, essencialmente permitindo ignorar o Null safety.

  Exemplo:
  ```java
  let optional var y = 1 in
    let var x = y! + 2 in
      x
  // y pode ser null (nesse caso tem um valor)
  // x é o valor de y (aqui tomado como não-nulo) + 2
  // x vale 3

  let optional var y = null in
    let var x = y! + 2 in
      x
  // y pode ser null (nesse caso não tem um valor)
  // x é o valor de y (aqui tomado como não-nulo) + 2
  // erro de execução por tentar acessar um valor que é nulo
  ```
- **Operador de atribuição Se Nulo (Null-Aware Assignment Operator):** operador binário ("??=") que atribui um valor ao lado esquerdo se, e somente se, esse valor for nulo. Na prática, seria um *shadowing* condicional.

  Exemplo:
  ```java
  let optional var y = null in
    let var y ??= 5 in
      let var y ??= 10 in
        y
  // Declaramos uma variável como possivelmente nula
  // Atribuímos 5 a ela, já que ela é nula
  // Como a variável agora tem um valor (5), ela não recebe 10
  // y vale 5
  ```

# BNF
```
  Programa ::= Expressao

  Expressao ::= Valor
        | ExpUnaria
        | ExpBinaria
        | ExpTernaria
        | ExpDeclaracao
        | Id
        | Aplicacao
        | IfThenElse
  
  Valor ::= ValorConcreto
  
  ValorConcreto ::= ValorInteiro
        | ValorBooleano
        | ValorString
        | ValorNulo
  
  ExpUnaria ::= “-“ Expressao
        | “not” Expressao
        | “length” Expressao
        | Expressao “!”
  
  ExpBinaria ::= Expressao “+” Expressao
        | Expressao “-“ Expressao
        | Expressao “and” Expressao
        | Expressao “or” Expressao
        | Expressao “==” Expressao
        | Expressao “++” Expressao
        | Expressao “??” Expressao
  
  ExpTernaria ::= Expressao “?” Expressao “:” Expressao
  
  ExpDeclaracao ::= “let” DeclaracaoFuncional “in” Expressao
  
  DeclaracaoFuncional ::= DecVariavel
         | DecFuncao
         | DecComposta
  
  DecVariavel ::= “var” Id “=” Expressao
         | “var” Id “??=” Expressao
         | “optional” DecVariavel
  
  DecFuncao ::= “fun” ListId “=” Expressao
  
  DecComposta ::= DeclaracaoFuncional “,” DeclaracaoFuncional
  
  ListId ::= Id | Id ListId
  
  Aplicacao ::= Id”(“ ListExp “)”
  
  ListExp ::= Expressao | Expressao, ListExp
  
  IfThenElse ::= “if” Expressao “then” Expressao “else” Expressao
```
