#!/bin/bash

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m'

TEST_DIR="../Testes"
TMP_OUT=".tmp_test_output"
TOTAL=0
PASSED=0

trap "rm -f $TMP_OUT" EXIT

clear
echo -e "${BOLD}${CYAN}=== Iniciando Testes do Compilador ===${NC}\n"

# 1. Compilação (Mantemos o quiet aqui para não poluir o início)
echo -ne "${YELLOW}Compilando projeto... ${NC}"
if mvn clean compile -q; then
    echo -e "${GREEN}[OK]${NC}\n"
else
    echo -e "${RED}[ERRO NA COMPILAÇÃO]${NC}"
    exit 1
fi

# 2. Execução dos Testes
for TEST_FILE in "$TEST_DIR"/*; do
    [ -e "$TEST_FILE" ] || continue
    
    TEST_NAME=$(basename "$TEST_FILE")
    ((TOTAL++))

    echo -e "${BOLD}${CYAN}▶ Teste: $TEST_NAME${NC}"
    echo -e "${CYAN}--------------------------------------${NC}"

    FIRST_LINE=$(head -n 1 "$TEST_FILE")
    SECOND_LINE=$(head -n 2 "$TEST_FILE" | tail -n 1)
    
    INPUT_ARGS=""
    EXPECTED_OUT=""
    CODE_START_LINE=1

    if [[ "$FIRST_LINE" == inputs:* ]]; then
        INPUT_ARGS=$(echo "$FIRST_LINE" | sed 's/^inputs: //')
        CODE_START_LINE=2
        if [[ "$SECOND_LINE" == expect:* ]]; then
            EXPECTED_OUT=$(echo "$SECOND_LINE" | sed 's/^expect: //')
            CODE_START_LINE=3
        fi
    elif [[ "$FIRST_LINE" == expect:* ]]; then
        EXPECTED_OUT=$(echo "$FIRST_LINE" | sed 's/^expect: //')
        CODE_START_LINE=2
    fi

    if [ $CODE_START_LINE -gt 1 ]; then
        tail -n +$CODE_START_LINE "$TEST_FILE" > "$TMP_OUT.code"
        TEST_FILE_TO_RUN="$TMP_OUT.code"
    else
        TEST_FILE_TO_RUN="$TEST_FILE"
    fi

    # REMOVIDO o -q para permitir que a mensagem de erro seja capturada no arquivo
    mvn -q exec:java -Dexec.args="$TEST_FILE_TO_RUN $INPUT_ARGS" > "$TMP_OUT" 2>&1
    EXIT_CODE=$?

    # Verificação de Sucesso ou Falha Esperada
    # Remove as linhas indesejadas e também remove a string do Parser do começo para extrair só a mensagem útil ou os outputs.
    ACTUAL_OUT=$(grep -vE "\[INFO\]|\[WARNING\]|WARNING:|sun.misc.Unsafe|Reading from file|parsed successfully" "$TMP_OUT" | sed -E 's/Imperativa 1 PLP Parser Version [0-9\.]+:[ \t]*//g' | tr '\n' ' ' | xargs)
    EXPECTED_OUT=$(echo "$EXPECTED_OUT" | xargs)

    if [ -n "$EXPECTED_OUT" ]; then
        # Removemos logs desnecessarios e checamos se a saida contem ou eh igual ao esperado
        # para lidar com falhas de parse, testamos match parcial
        if echo "$ACTUAL_OUT" | grep -qi "$EXPECTED_OUT" || [ "$ACTUAL_OUT" = "$EXPECTED_OUT" ]; then
            echo -e "${GREEN}Status: [OK]${NC}"
            echo -e "  Resultado Esperado Alcançado: $EXPECTED_OUT"
            ((PASSED++))
        else
            echo -e "${RED}Status: [FALHOU]${NC}"
            echo -e "${YELLOW}  Esperado: $EXPECTED_OUT${NC}"
            echo -e "${YELLOW}  Obtido:   $ACTUAL_OUT${NC}"
        fi
    else
        if [ $EXIT_CODE -eq 0 ] && ! grep -qiE "exception|erro|error" "$TMP_OUT"; then
            echo -e "${GREEN}Status: [OK]${NC}"
            # Se não tem Expected output tira a string do parser log tbm para printar
            ACTUAL_OUT=$(grep -vE "\[INFO\]|\[WARNING\]|WARNING:|Imperativa 1 PLP Parser|sun.misc.Unsafe" "$TMP_OUT" | tr '\n' ' ' | xargs)
            echo -e "  Resultado: $ACTUAL_OUT"
            ((PASSED++))
        else
            # Verificação de Falha
            echo -e "${RED}Status: [FALHOU]${NC}"
            echo -ne "${YELLOW}  Motivo: ${NC}"
            
            ERR_MSG=$(grep -iE "exception|erro de tipo|erro" "$TMP_OUT" | \
                      grep -vE "http://|Help [0-9]|For more information|\[ERROR\]" | head -n 1 | \
                      sed -E 's/.*Java class\. //; s/ -> \[Help 1\]//; s/\[ERROR\] //; s/Funcional 1 PLP Parser Version 0\.0\.1: //')

            if [ -z "$ERR_MSG" ]; then
                echo -e "Erro desconhecido (verifique o log manual)"
            else
                echo -e "${RED}$ERR_MSG${NC}"
            fi
        fi
    fi
    echo -e "${CYAN}======================================${NC}\n"
done

# Resumo
echo -e "${BOLD}Resumo: ${GREEN}Passou $PASSED${NC} / ${RED}Falhou $((TOTAL-PASSED))${NC}\n"

[ $PASSED -eq $TOTAL ] || exit 1