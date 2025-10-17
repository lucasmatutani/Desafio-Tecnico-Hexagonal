#!/bin/bash

echo "🔍 Verificando estrutura do projeto Hexagonal..."
echo ""

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

check_directory() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1 (FALTANDO)"
        return 1
    fi
}

check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
        return 0
    else
        echo -e "${RED}✗${NC} $1 (FALTANDO)"
        return 1
    fi
}

echo -e "${BLUE}📁 Estrutura de Diretórios:${NC}"
echo ""

# Domain
echo "Domain Layer:"
check_directory "src/main/java/com/inventory/domain/model"
check_directory "src/main/java/com/inventory/domain/event"
check_directory "src/main/java/com/inventory/domain/policy"
check_directory "src/main/java/com/inventory/domain/service"
check_directory "src/main/java/com/inventory/domain/exception"
echo ""

# Application
echo "Application Layer:"
check_directory "src/main/java/com/inventory/application/port/input"
check_directory "src/main/java/com/inventory/application/port/output"
check_directory "src/main/java/com/inventory/application/service"
echo ""

# Adapters
echo "Adapters Layer:"
check_directory "src/main/java/com/inventory/adapters/input/rest"
check_directory "src/main/java/com/inventory/adapters/output/persistence"
check_directory "src/main/java/com/inventory/adapters/output/messaging"
echo ""

echo -e "${BLUE}📄 Arquivos de Configuração:${NC}"
echo ""
check_file "pom.xml"
check_file "src/main/resources/application.yml"
check_file "src/main/java/com/inventory/InventoryServiceApplication.java"
check_file ".gitignore"
check_file "README.md"
check_file "ARCHITECTURE.md"
echo ""

echo -e "${BLUE}🔧 Verificando dependências do Maven:${NC}"
echo ""

if mvn dependency:tree > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} Dependências Maven OK"
else
    echo -e "${RED}✗${NC} Erro nas dependências Maven"
fi

echo ""
echo -e "${BLUE}🏗️  Tentando compilar o projeto:${NC}"
echo ""

if mvn clean compile -q > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} Compilação bem-sucedida!"
else
    echo -e "${RED}✗${NC} Erro na compilação"
fi

echo ""
echo -e "${BLUE}📊 Estatísticas:${NC}"
echo ""

total_dirs=$(find src/main/java/com/inventory -type d | wc -l)
total_gitkeep=$(find src/main/java/com/inventory -name ".gitkeep" | wc -l)

echo "Total de diretórios: $total_dirs"
echo "Arquivos .gitkeep: $total_gitkeep"
echo ""

echo "✅ Verificação concluída!"
echo ""
echo "📚 Para mais informações, consulte:"
echo "   - README.md - Visão geral do projeto"
echo "   - ARCHITECTURE.md - Guia de implementação"

