# plp-project-26.1
IN1007 Project

# Escopo

Tendo em vista que a [linguagem funcional 1](https://augustosampaio.github.io/PLP/linguagens/funcional1) não possui tratamento para valores null (o próprio valor não existe, nem, portanto, null safety e uso de operadores), pensamos em implementar os seguintes conceitos na linguagem:
- **Nullable type:** declaração com keyword "optional" (ou "?").
- **Null coalescing:** operador binário ("??") que retorna o lado direito da operação caso o operador seja null, ou o esquerdo caso não seja null. Valor "default".

  Exemplo:
  ```java
  let optional var y = 2 in
    let var x = y ?? 3
  // y pode ser null (nesse caso tem um valor)
  // Caso y seja null, x recebe 3
  // Caso não, x recebe y, que é 2
  // x vale 2
  ```
- **Null assertion:** acesso com keyword "!" para forçar leitura de valor como não-nulo, independente da declaração dele.

  Exemplo:
  ```java
  let var? y = 1 in
    let var x = y! + 2
  // y pode ser null (nesse caso tem um valor)
  // x é o valor de y (aqui tomado como não-nulo) + 2
  // x vale 3

  let var? y in
    let var x = y! + 2
  // y pode ser null (nesse caso não tem um valor)
  // x é o valor de y (aqui tomado como não-nulo) + 2
  // erro de compilação por tentar acessar um valor que é nulo
  ```
- **Late keyword:** keyword usada para declarar variáveis nulas com a condição de que elas só serão usadas após declarar um valor a ela.

  Exemplo:
  ```java
  let late var y in
    let var y = 2 in
      x = y
  // declaramos y sem valor (mas com late, garantindo que vamos atribuir um valor antes de usá-lo)
  // atribuimos um valor a y (2)
  // só então usamos y, atribuindo seu valor a x
  // x vale 2
  ```
- **Operador de atribuição Se Nulo (Null-Aware Assignment Operator):** operador binário ("??=") que atribui um valor ao lado esquerdo se, e somente se, esse valor for nulo.

  Exemplo:
  ```java
  let optional var y in
    let var y ??= 5 in
      let var y ??= 10
  // Declaramos uma variável como possivelmente nula
  // Atribuímos 5 a ela, já que ela é nula
  // Como a variável agora tem um valor (5), ela não recebe 10
  // y vale 5
  ```

  # BNF

  ...
