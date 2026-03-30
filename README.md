# plp-project-26.1
IN1007 Project

# Escopo

Tendo em vista que a [linguagem funcional 1](https://augustosampaio.github.io/PLP/linguagens/funcional1) não possui tratamento para valores null (o próprio valor não existe, nem, portanto, null safety e uso de operadores), pensamos em implementar os seguintes conceitos na linguagem:
- **Nullable type:** declaração com keyword "optional" (ou "?").
- **Null coalescing:** operador binário ("??") que retorna o lado direito da operação caso o operador seja null, ou o esquerdo caso não seja null. Valor "default".
  - **Operador Ternário:** operador "? :" que atua como um if-then-else. 

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
- **Null assertion:** acesso com keyword "!" para forçar leitura de valor como não-nulo, independente da declaração dele.

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

  ...
