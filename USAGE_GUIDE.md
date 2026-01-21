# GUIA DE USO - API SFP

## 🚀 Iniciando o Projeto

### Pré-requisitos
- Java 21+
- Maven 3.8.1+

### Rodar em Desenvolvimento
```bash
cd sfp
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

A API estará disponível em: `http://localhost:8080`

## 📖 Acessar Documentação Interativa

1. Abra: `http://localhost:8080/swagger-ui/index.html`
2. Você verá todos os 40+ endpoints organizados por domínio
3. Clique em qualquer endpoint para expandir e ver detalhes
4. Use "Try it out" para testar direto no navegador

## 🔐 Autenticação

### 1. Registrar um novo usuário

**Endpoint:** `POST /auth/register`

```json
{
  "email": "seu@email.com",
  "password": "Senha123!",
  "fullName": "Seu Nome",
  "role": "USER"
}
```

**Resposta:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "seu@email.com",
  "fullName": "Seu Nome",
  "role": "USER"
}
```

### 2. Fazer Login

**Endpoint:** `POST /auth/login`

```json
{
  "email": "seu@email.com",
  "password": "Senha123!"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400
}
```

### 3. Usar o Token

Copie o token e adicione no header de todas as requisições:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**No Swagger:** Clique no botão 🔒 "Authorize" no topo e cole o token.

## 💰 Fluxo Típico de Uso

### 1️⃣ Criar uma Conta

**POST `/accounts`**

```json
{
  "name": "Conta Corrente",
  "initialBalance": 5000.00
}
```

**Resposta:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Conta Corrente",
  "initialBalance": 5000.00,
  "createdAt": "2025-01-20T10:30:00"
}
```

Guarde este `id` para usar nas próximas operações.

### 2️⃣ Registrar uma Receita

**POST `/incomes`**

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440001",
  "description": "Salário Líquido",
  "amount": 3500.50,
  "incomeType": "SALARY",
  "referenceDate": "2025-01-20"
}
```

**Tipos de Receita:**
- `SALARY` - Salário
- `BENEFIT` - Benefícios (VR, VA, etc)
- `EXTRA` - Extras (bônus, freelance, etc)

### 3️⃣ Registrar uma Despesa

**POST `/expenses`**

```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440001",
  "description": "Aluguel",
  "amount": 1500.00,
  "expenseType": "FIXED",
  "referenceDate": "2025-01-20"
}
```

**Tipos de Despesa:**
- `FIXED` - Despesas fixas (aluguel, internet, etc)
- `VARIABLE` - Despesas variáveis (supermercado, restaurante)
- `CREDIT_CARD` - Faturas de cartão de crédito
- `INVESTMENT` - Aplicações financeiras

### 4️⃣ Ver Resumo do Mês

**GET `/balance/current`**

Retorna:
```json
{
  "month": 1,
  "year": 2025,
  "totalIncome": 3500.50,
  "totalExpense": 1500.00,
  "balance": 2000.50
}
```

### 5️⃣ Criar um Cartão de Crédito

**POST `/credit-cards`**

```json
{
  "name": "Nubank",
  "limitAmount": 10000.00,
  "closingDay": 15,
  "dueDay": 25
}
```

**Explicação:**
- `closingDay`: Dia que a fatura fecha (15 = 15º de cada mês)
- `dueDay`: Dia de vencimento da fatura (25 = 25º de cada mês)

### 6️⃣ Registrar uma Compra no Cartão (Parcelada)

**POST `/credit-cards/{creditCardId}/transactions`**

```json
{
  "description": "TV Samsung 55\"",
  "totalAmount": 2400.00,
  "installments": 3,
  "purchaseDate": "2025-01-20"
}
```

Sistema calcula automaticamente:
- Valor por parcela: 2400 ÷ 3 = 800.00
- Data da 1ª parcela: Lógica de fechamento do cartão

### 7️⃣ Registrar um Investimento

**POST `/investments`**

```json
{
  "description": "CDB Banco X - 10% ao ano",
  "amount": 5000.00,
  "investmentType": "CDB",
  "referenceDate": "2025-01-20"
}
```

### 8️⃣ Simular uma Compra

Quer saber o impacto de uma compra de R$ 2000 em 12 parcelas?

**POST `/simulations`**

```json
{
  "simulationType": "INSTALLMENT",
  "totalValue": 2000.00,
  "installments": 12,
  "startDate": "2025-02-01"
}
```

**Resposta:**
```json
{
  "simulationType": "INSTALLMENT",
  "totalValue": 2000.00,
  "installments": 12,
  "monthlyValue": 166.67,
  "impactOnBalance": -2000.00
}
```

## 📊 Filtros Disponíveis

### Listar Receitas de Janeiro/2025

**GET `/incomes?month=1&year=2025`**

### Listar Despesas Fixas de Janeiro/2025

**GET `/expenses?month=1&year=2025&type=FIXED`**

### Listar Transações do Cartão em Janeiro/2025

**GET `/credit-cards/{creditCardId}/transactions?month=1&year=2025`**

### Ver Balanço de Dezembro/2024

**GET `/balance/monthly?month=12&year=2024`**

### Ver Todos os Meses de 2024

**GET `/balance/yearly?year=2024`**

## 🔄 Exemplos com cURL

### Registrar e Login

```bash
# Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "password": "Teste123!",
    "fullName": "Teste User",
    "role": "USER"
  }'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "password": "Teste123!"
  }' > token.json

TOKEN=$(cat token.json | jq -r '.token')

# Criar conta (usando token)
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Conta Principal",
    "initialBalance": 10000.00
  }'
```

## ⚠️ Comportamentos Importantes

### 1. Isolamento de Dados

- Cada usuário vê apenas seus próprios dados
- Não há vazamento de informações entre usuários
- Deletar uma conta deleta automaticamente todas as receitas, despesas, etc.

### 2. Cálculo de Mês/Ano

- O `month` e `year` são extraídos automaticamente da `referenceDate`
- Se você criar uma receita em 2025-01-15, ela aparecerá em janeiro/2025

### 3. Datas de Vencimento do Cartão

- Sistema calcula automaticamente quando a parcela vence
- Leva em conta o dia de fechamento e vencimento do cartão
- Exemplo:
  - Fechamento: 15º
  - Vencimento: 25º
  - Compra em 16/jan → 1ª parcela vence em 25/fev

### 4. BigDecimal e Arredondamento

- Todos os valores monetários usam precisão de 2 casas (centavos)
- Divisões (como parcelamentos) usam arredondamento correto
- Exemplo: 1000 ÷ 3 = 333.33 + 333.33 + 333.34

## 🛠️ Troubleshooting

### "Token expirado"
- Faça login novamente para obter novo token
- Validade do token: 24 horas

### "Conta não encontrada"
- Verifique se o `accountId` existe (GET /accounts)
- Verifique se a conta pertence ao seu usuário

### "CORS Error" (ao integrar com frontend)
- A API permite por padrão apenas requests do mesmo host
- Configurar CORS em `SecurityConfig.java` para seu frontend

### Banco de dados vazio ao iniciar
- Normal! Flyway cria as tabelas automaticamente
- Migrations rodadas: V1 a V10
- Se houver erro em alguma migration, verifique os logs

## 📚 Mais Informações

- **Documentação Completa:** http://localhost:8080/swagger-ui/index.html
- **API Docs JSON:** http://localhost:8080/v3/api-docs
- **H2 Console:** http://localhost:8080/h2 (apenas em dev)

## 🎯 Casos de Uso Comuns

### Caso 1: Acompanhar mensalidade

1. Cria conta
2. Registra receita (salário)
3. Registra despesas (fixas, variáveis)
4. Confere balanço do mês
5. Projeta para próximos meses

### Caso 2: Controlar cartão de crédito

1. Registra cartão de crédito (Nubank, Itaú, etc)
2. Cada compra = 1 transação (com parcelas se for)
3. Sistema calcula data de vencimento de cada parcela
4. Balanço mostra o impacto na disponibilidade

### Caso 3: Simular compra grande

1. Simula parcelamento em 12x
2. Vê o impacto mensal (166.67 por mês)
3. Verifica se cabe no orçamento
4. Se aprovado, registra a compra
5. Se não, descarta a simulação

---

**Aproveite o SFP! 💪💰**
