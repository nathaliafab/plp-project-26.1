# plp-project-26.1
IN1007 Project

# Equipe
- Nathalia Fernanda de Araújo Barbosa (nfab) 
- Thales de Oliveira Bezerra (tob2)

# Como rodar o projeto

1. **Clone o repositório** e acesse a pasta do projeto.
2. **Construa a imagem Docker**:
   ```bash
   docker build -t javacc-jdk25 .
   ```
3. **Inicie o container** (isso abrirá um terminal interativo):
   ```bash
   docker run --rm -it -v "$PWD":/workspace javacc-jdk25
   ```
4. **Compile e execute um código fonte** (dentro do container):

   Para rodar um arquivo específico (input) presente na pasta `Imperativa1`:
   ```bash
   mvn clean generate-sources compile exec:java -Dexec.args="input 1 true"
   ```

### Executando os Testes

Para rodar todos os testes automatizados da pasta `Testes` e visualizar os resultados, basta executar o seguinte script dentro do container:
```bash
./run_tests.sh
```

# Escopo

Esse projeto tem como objetivo adicionar à [Linguagem Imperativa 1](https://augustosampaio.github.io/PLP/linguagens/imperativa1) o operador Null, a capacidade de Null safety, e operadores presentes em linguagens modernas que estão relacionados ao conceito de Null ("??", "!", "??="), além do operador ternário.

Abaixo temos o que será implementado pela linguagem em mais detalhes:

- [ ] **Nullable type:** Variáveis podem ser reassinaladas para o valor Null, desde que tenham a declaração com keyword "optional".

  Exemplo:
  ```java
  { var optional y = null, var optional z = 2 ; 
    Skip 
  }
  ```

- [ ] **Null safety:** Caso uma operação tenha risco de causar erro de execução por causa do Null, ela lança um erro de compilação em vez disso.

  Exemplo 1:
  ```java
  { var optional y = 2 ;
    { var x = y + 3 ;
      write(x)
    }
  }
  // y pode ser null, potencialmente causando erro de execução
  // Logo, o ambiente de compilação vai perceber o problema e lançar um erro de compilação
  ```

  Exemplo 2:
  ```java
  { var optional y = 2 ;
    if not (y == null) then
      { var x = y + 3 ;
        write(x)
      }
    else
      Skip
  }
  // Com o if, garantimos que y não pode ser null, excluindo a possibilidade de um erro de execução causado pelo null
  // Logo, o ambiente de compilação não vai lançar um erro
  ```


- [ ] **Null coalescing:** Operador binário ("??") que retorna o lado direito da operação caso o operador seja null, ou o esquerdo caso não seja null. Valor "default".

  Exemplo:
  ```java
  { var optional y = 2 ;
    { var x = y ?? 3 ;
      write(x)
    }
  }
  // y pode ser null (nesse caso tem um valor)
  // Caso y seja null, x recebe 3
  // Caso não, x recebe y, que é 2
  // x vale 2
  ```

- [ ] **Operador Ternário:** Operador "? :" que atua como um if-then-else.

    Exemplo:
    ```java
    { var a = 1, var b = 2 ;
      { var c = a == b ? 3 : 4 ;
        write(c)
      }
    }
    ```


- [ ] **Null assertion:** Colocando a keyword "!" após acessar a variável, garatimos ao compilador que o valor dela não é nulo, essencialmente permitindo ignorar o Null safety.

  Exemplo:
  ```java
  { var optional y = 1 ;
    { var x = y! + 2 ;
      write(x)
    }
  }
  // y pode ser null (nesse caso tem um valor)
  // x é o valor de y (aqui tomado como não-nulo) + 2
  // x vale 3

  { var optional y = null ;
    { var x = y! + 2 ;
      write(x)
    }
  }
  // y pode ser null (nesse caso não tem um valor)
  // x é o valor de y (aqui tomado como não-nulo) + 2
  // erro de execução por tentar acessar um valor que é nulo
  ```
- [ ] **Operador de atribuição Se Nulo (Null-Aware Assignment Operator):** operador binário ("??=") que atribui um valor ao lado esquerdo se, e somente se, esse valor for nulo. Na prática, seria um *shadowing* condicional.

  Exemplo:
  ```java
  { var optional y = null ;
    y ??= 5 ;
    y ??= 10 ;
    write(y)
  }
  // Declaramos uma variável como possivelmente nula
  // Atribuímos 5 a ela, já que ela é nula
  // Como a variável agora tem um valor (5), ela não recebe 10
  // y vale 5
  ```

# BNF
```
Programa ::= Comando

Comando ::= Atribuicao
       | AtribuicaoSeNulo
       | ComandoDeclaracao
       | While
       | IfThenElse
       | IO
       | Comando “;” Comando
       | Skip

Skip ::=

Atribuicao ::= Id “:=” Expressao

AtribuicaoSeNulo := Id "??=" Expressao

Expressao ::= Valor
       | ExpUnaria
       | ExpBinaria
       | ExpTernaria
       | Id

Valor ::= ValorConcreto

ValorConcreto ::= ValorInteiro
       | ValorBooleano
       | ValorString
       | ValorNulo

ExpUnaria ::= “-“ Expressao
       | “not” Expressao
       | “length” Expressao
       | Expressao "!"

ExpBinaria ::= Expressao “+” Expressao
       | Expressao “-“ Expressao
       | Expressao “and” Expressao
       | Expressao “or” Expressao
       | Expressao “==” Expressao
       | Expressao “++” Expressao
       | Expressao "??" Expressao

ExpTernaria ::= Expressao "?" Expressao ":" Expressao

ComandoDeclaracao ::= “{“ Declaracao “;” Comando “}”

Declaracao ::= DeclaracaoVariavel
       | DeclaracaoComposta

DeclaracaoVariavel ::= “var” Id “=” Expressao
       | DeclaracaoOptional

DeclaracaoOptional ::= "var" "optional" Id "=" Expressao

DeclaracaoComposta ::= Declaracao “,” Declaracao

While ::= “while” Expressao “do” Comando

IfThenElse ::= “if” Expressao “then” Comando “else” Comando

IO ::= “write” “(“ Expressao “)”
       | “read” “(“ Id “)”
```
