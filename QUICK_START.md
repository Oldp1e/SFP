# 🚀 QUICK START - Começar em 5 Minutos

## 1️⃣ Iniciar em Desenvolvimento

```bash
cd D:\Projetos Pessoais\SFP Data\Projects\sfp\sfp
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Aguarde até ver: `Started SfpApplication in X seconds`

## 2️⃣ Acessar Swagger UI

Abra no navegador:
```
http://localhost:8080/swagger-ui/index.html
```

## 3️⃣ Testar Endpoints

### Opção A: Via Swagger UI (Recomendado)
1. Clique em "Autenticação" (grupo de endpoints)
2. Clique em "POST /auth/register" → "Try it out"
3. Preencha o formulário:
   ```json
   {
     "email": "teste@example.com",
     "password": "Teste@123456",
     "fullName": "Usuário Teste",
     "role": "USER"
   }
   ```
4. Clique "Execute"
5. Faça login em "POST /auth/login"
6. Copie o token da resposta
7. Clique no cadeado "Authorize" no topo
8. Cole: `Bearer {seu_token}`
9. Teste "GET /users/me"

### Opção B: Via cURL
```bash
# 1. Register
curl -X POST http://localhost:8080/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"teste@example.com\",\"password\":\"Teste@123456\",\"fullName\":\"Usuário Teste\",\"role\":\"USER\"}"

# 2. Login
curl -X POST http://localhost:8080/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"teste@example.com\",\"password\":\"Teste@123456\"}" > response.json

# 3. Extrair token (Windows PowerShell)
$token = (Get-Content response.json | ConvertFrom-Json).token

# 4. Usar token
curl -X GET http://localhost:8080/users/me ^
  -H "Authorization: Bearer $token"
```

### Opção C: Via Postman
1. Abra Postman
2. Crie Nova Requisição
3. **POST** → `http://localhost:8080/auth/register`
4. Body → JSON:
   ```json
   {
     "email": "teste@example.com",
     "password": "Teste@123456",
     "fullName": "Usuário Teste",
     "role": "USER"
   }
   ```
5. Send
6. Repita para `/auth/login`
7. Copie token → Vá para `/users/me`
8. Header → Authorization: `Bearer {token}`
9. Send

---

## 📖 Documentação

| Documento | Conteúdo |
|-----------|----------|
| **HELP.md** | 📋 Guia de uso da API (estrutura, exemplos, JWT) |
| **README.md** | 🚀 Guia de desenvolvimento e deployment |
| **API_DOCUMENTATION.md** | 📚 Documentação técnica completa (arquitetura, fluxos) |
| **IMPLEMENTATION_SUMMARY.md** | ✅ Sumário do que foi implementado |

---

## 🔍 Endpoints Disponíveis

### 🔐 Autenticação (Público)
```
POST /auth/login
  - Email e senha
  - Retorna: token JWT + dados do usuário
  
POST /auth/register
  - Cria novo usuário
  - Retorna: dados do novo usuário
```

### 👤 Usuários (Protegido)
```
GET /users/me
  - Requer: Authorization: Bearer {token}
  - Retorna: Dados do usuário logado
```

---

## 🔧 Parar a Aplicação

Pressione **Ctrl + C** no terminal

---

## ❓ Dúvidas?

- Erro `java.lang.UnsupportedClassVersionError`? → Java 21+ instalado?
- Port 8080 em uso? → `netstat -ano | findstr :8080` e kill
- Token expirado? → Faça login novamente
- Veja `HELP.md` para mais troubleshooting

---

## 🎯 Próximas Passos

1. ✅ Testar endpoints (acima)
2. 📖 Ler `HELP.md` para entender a API
3. 📚 Ler `README.md` para deployment
4. 🔧 Adicionar seus próprios endpoints
5. 🚀 Deploy em produção

---

**Aproveite! 🎉**

Dúvidas sobre a API? Veja `HELP.md`  
Dúvidas sobre desenvolvimento? Veja `README.md`  
Dúvidas técnicas? Veja `API_DOCUMENTATION.md`
