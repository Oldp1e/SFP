# 🚀 GUIA DE INICIALIZAÇÃO RÁPIDO - SFP

## ⚡ Comece em 3 passos

### 1️⃣ Compilar o Projeto
```bash
cd D:\Projetos Pessoais\SFP Data\Projects\sfp\sfp
.\mvnw.cmd clean package -DskipTests
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
[INFO] JAR criado em: target/sfp-0.0.1-SNAPSHOT.jar
```

### 2️⃣ Rodar em Desenvolvimento
```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Resultado esperado:**
```
Started SfpApplication in 10 seconds
Application running on: http://localhost:8080
```

### 3️⃣ Acessar a API
Abra no navegador:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📖 PRIMEIRO USO

### 1. Registrar Usuário
No Swagger, abra a seção **Autenticação** e clique em `POST /auth/register`:

```json
{
  "email": "seu@email.com",
  "password": "Senha123!",
  "fullName": "Seu Nome",
  "role": "USER"
}
```

### 2. Fazer Login
Clique em `POST /auth/login`:

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

### 3. Autorizar no Swagger
Clique no botão 🔒 **Authorize** e cole:
```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 4. Criar uma Conta
Abra **Contas** → `POST /accounts`:

```json
{
  "name": "Conta Corrente",
  "initialBalance": 5000.00
}
```

### 5. Registrar uma Receita
Abra **Receitas** → `POST /incomes`:

```json
{
  "accountId": "ID_DA_CONTA_CRIADA",
  "description": "Salário",
  "amount": 3500.00,
  "incomeType": "SALARY",
  "referenceDate": "2025-01-20"
}
```

### 6. Ver Balanço
Abra **Balanço** → `GET /balance/current`

---

## 🛠️ TROUBLESHOOTING

### Porta 8080 já está em uso
```bash
# Windows - Encontrar processo
netstat -ano | findstr :8080

# Matar processo (substitua PID)
taskkill /PID 12345 /F
```

### Erro de compilação
```bash
# Limpar cache e tentar novamente
.\mvnw.cmd clean install -DskipTests
```

### H2 Database vazio
Normal! Flyway cria tudo automaticamente. Verifique logs para erros.

### Token expirado
Faça login novamente. Tokens duram 24 horas.

---

## 📊 EXEMPLO COMPLETO: Controlar Gastos Mensais

```
┌─ Janeiro/2025
│
├─ Receitas
│  ├─ Salário: R$ 3.500,00 (SALARY)
│  └─ VR: R$ 400,00 (BENEFIT)
│  = Total: R$ 3.900,00
│
├─ Despesas
│  ├─ Aluguel: R$ 1.500,00 (FIXED)
│  ├─ Supermercado: R$ 800,00 (VARIABLE)
│  ├─ Cartão:
│  │  └─ TV 55" parcelada 3x: R$ 800,00/mês (CREDIT_CARD)
│  └─ Total: R$ 3.100,00
│
├─ Balanço: R$ 800,00 (positivo! 🎉)
│
└─ Próximo passo: Simular maior compra?
```

---

## 🎯 CASOS DE USO COMUNS

### Caso 1: Acompanhar Salário
1. POST `/incomes` com SALARY
2. GET `/balance/current` para ver balanço

### Caso 2: Controlar Despesas Variáveis
1. POST `/expenses` com VARIABLE e data
2. GET `/expenses?month=1&year=2025&type=VARIABLE`
3. Veja soma no `/balance/monthly`

### Caso 3: Parcelar Compra no Cartão
1. POST `/credit-cards` para registrar cartão
2. POST `/credit-cards/{id}/transactions` com compra
3. Sistema calcula parcelas e datas automaticamente

### Caso 4: Simular Grande Compra
1. POST `/simulations` com INSTALLMENT
2. Veja impacto mensal na resposta
3. Decida se compra ou não

---

## 📱 ENDPOINTS MAIS ÚTEIS

| Ação | Endpoint | Método |
|------|----------|--------|
| Ver balanço de hoje | `/balance/current` | GET |
| Ver balanço de janeiro | `/balance/monthly?month=1&year=2025` | GET |
| Ver todos meses de 2025 | `/balance/yearly?year=2025` | GET |
| Listar receitas de janeiro | `/incomes?month=1&year=2025` | GET |
| Listar despesas fixas | `/expenses?month=1&year=2025&type=FIXED` | GET |
| Registrar compra parcelada | `/credit-cards/{id}/transactions` | POST |
| Simular compra | `/simulations` | POST |

---

## 🔐 SEGURANÇA

Cada endpoint:
- ✅ Requer token JWT válido
- ✅ Mostra apenas dados do usuário autenticado
- ✅ Não permite acesso a dados de outros usuários
- ✅ Token expira em 24 horas (faça login novamente)

---

## 💡 DICAS

- Use o filtro `month` e `year` para organizar dados
- Sempre use `?month=X&year=Y` em `/balance/monthly`
- Simulações não alteram dados (são apenas cálculos)
- Parcelamentos são calculados automaticamente
- Balanço = soma receitas - soma despesas

---

## 📚 DOCUMENTAÇÃO COMPLETA

Dentro do projeto, leia em ordem:
1. `README.md` - Visão geral
2. `USAGE_GUIDE.md` - Como usar (com exemplos)
3. `FINAL_REPORT.md` - Resumo técnico
4. `PROJECT_SUMMARY.md` - Arquitetura detalhada

---

## 🎉 PRONTO!

Você tem um **Sistema Financeiro Pessoal** profissional rodando! 

**Próximos passos:**
- [ ] Explorar todos os endpoints no Swagger
- [ ] Criar suas contas e registrar dados reais
- [ ] Desenvolver um frontend (React, Angular, etc)
- [ ] Fazer testes automatizados
- [ ] Deployar em produção com PostgreSQL

---

**Sucesso! 🚀**
