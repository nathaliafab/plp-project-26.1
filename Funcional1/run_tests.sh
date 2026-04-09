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

    # REMOVIDO o -q para permitir que a mensagem de erro seja capturada no arquivo
    mvn exec:java -Dexec.args="$TEST_FILE" > "$TMP_OUT" 2>&1
    EXIT_CODE=$?

    # Verificação de Sucesso
    if [ $EXIT_CODE -eq 0 ] && ! grep -qiE "exception|erro|error" "$TMP_OUT"; then
        echo -e "${GREEN}Status: [OK]${NC}"
        grep "resultado=" "$TMP_OUT" | sed 's/^/  /'
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
    echo -e "${CYAN}======================================${NC}\n"
done

# Resumo
echo -e "${BOLD}Resumo: ${GREEN}Passou $PASSED${NC} / ${RED}Falhou $((TOTAL-PASSED))${NC}\n"

[ $PASSED -eq $TOTAL ] || exit 1