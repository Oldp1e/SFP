API_DOCUMENTATION.md

# SFP API - Documentação Técnica Completa

## 1. Visão Geral da Arquitetura

```
┌─────────────────────────────────────────────────────┐
│                  CLIENT (Frontend)                  │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────────────┐
│            SWAGGER UI / POSTMAN / CURL               │
│      http://localhost:8080/swagger-ui.html          │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────────────┐
│              SPRING SECURITY FILTER CHAIN            │
│  - CSRF disabled (stateless)                        │
│  - Session Stateless (JWT)                          │
│  - Custom JWT Authentication Filter                 │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓ (após autenticação)
┌─────────────────────────────────────────────────────┐
│                  CONTROLLERS                        │
│  - AuthController (/auth/**)                        │
│  - UserController (/users/**)                       │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────────────┐
│                   SERVICES                          │
│  - AuthenticationService                            │
│  - UserService                                      │
└──────────────────┬──────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────────────┐
│            REPOSITORIES / DATABASE                  │
│  - UserRepository (JPA)                             │
│  - H2 (DEV) / PostgreSQL (PRD)                      │
│  - Flyway Migration                                 │
└─────────────────────────────────────────────────────┘
```

---

## 2. Fluxo de Autenticação JWT

```
1. Usuário envia credentials
   ├─ POST /auth/login
   │  └─ Payload: { email, password }

2. AuthenticationService valida
   ├─ Buscar user por email
   ├─ Comparar password com hash (BCrypt)
   └─ Se válido → continua

3. JwtService gera token
   ├─ Claims: { sub: email, iat, exp }
   ├─ Assinado com JWT_SECRET
   └─ Retorna: { token, type: "Bearer", user }

4. Cliente armazena token
   └─ localStorage.setItem('token', response.token)

5. Requisições protegidas
   ├─ Header: Authorization: Bearer {token}
   └─ JwtAuthenticationFilter extrai e valida

6. SecurityContext
   ├─ UsernamePasswordAuthenticationToken criado
   ├─ Principal: UserDetails (User entity)
   └─ Autoridades: [ROLE_USER | ROLE_ADMIN]

7. Expiração (24h)
   └─ Erro 401 ou 403 → Cliente refaz login
```

---

## 3. Estrutura de Requisições e Respostas

### 3.1 POST /auth/login

**Requisição**:
```json
{
  "email": "usuario@example.com",
  "password": "senhaSegura123"
}
```

**Validações**:
- `email`: Obrigatório, deve ser email válido
- `password`: Obrigatório, não vazio

**Resposta 200 (Sucesso)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "email": "usuario@example.com",
    "fullName": "João Silva",
    "isActive": true,
    "role": "USER",
    "createdAt": "2025-01-20T10:30:00",
    "lastLoginAt": "2025-01-20T15:45:00"
  }
}
```

**Resposta 401 (Falha de Autenticação)**:
```json
{}
```
- Causas: Email não existe, senha incorreta

**Resposta 400 (Validação)**:
```json
{}
```
- Causas: Email inválido, campo vazio

---

### 3.2 POST /auth/register

**Requisição**:
```json
{
  "email": "novousuario@example.com",
  "password": "senhaSegura123",
  "fullName": "Maria Santos",
  "role": "USER"
}
```

**Validações**:
- `email`: Obrigatório, deve ser email válido, único
- `password`: Obrigatório, mínimo 6 caracteres
- `fullName`: Obrigatório, 3-255 caracteres
- `role`: Obrigatório, USER ou ADMIN

**Resposta 201 (Criado)**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "email": "novousuario@example.com",
  "fullName": "Maria Santos",
  "isActive": true,
  "role": "USER",
  "createdAt": "2025-01-20T10:30:00",
  "lastLoginAt": null
}
```

**Resposta 400 (Validação)**:
```json
{}
```
- Causas: Email duplicado, senha < 6 chars, fullName vazio

---

### 3.3 GET /users/me

**Requisição**:
```http
GET /users/me HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Resposta 200 (Sucesso)**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "email": "usuario@example.com",
  "fullName": "João Silva",
  "isActive": true,
  "role": "USER",
  "createdAt": "2025-01-20T10:30:00",
  "lastLoginAt": "2025-01-20T15:45:00"
}
```

**Resposta 401 (Token ausente/inválido)**:
```json
{}
```

**Resposta 403 (Token expirado)**:
```json
{}
```

---

## 4. Códigos HTTP Utilizados

| Código | Situação | Causa |
|--------|----------|-------|
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Usuário criado com sucesso |
| 400 | Bad Request | Dados inválidos (validação) |
| 401 | Unauthorized | Credenciais inválidas / Token ausente |
| 403 | Forbidden | Token expirado / Sem permissão |
| 500 | Server Error | Erro interno do servidor |

---

## 5. Headers Importantes

### Requisição

| Header | Obrigatório | Exemplo |
|--------|------------|---------|
| `Content-Type` | Sim (POST/PUT) | `application/json` |
| `Authorization` | Sim (endpoints protegidos) | `Bearer eyJhbGciOi...` |

### Resposta

| Header | Valor |
|--------|-------|
| `Content-Type` | `application/json; charset=utf-8` |
| `X-Content-Type-Options` | `nosniff` |

---

## 6. Documentação de Segurança

### Esquema JWT

**Header**:
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

**Payload**:
```json
{
  "sub": "usuario@example.com",
  "iat": 1611824000,
  "exp": 1611910400
}
```
- `sub`: Subject (email do usuário)
- `iat`: Issued at (timestamp)
- `exp`: Expiration (24 horas depois)

**Signature**:
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

### Variáveis de Ambiente

**Desenvolvimento**:
```bash
JWT_SECRET=dev-secret-minimo-16-chars
```

**Produção** (mínimo 32 caracteres):
```bash
JWT_SECRET=sua-chave-muito-secreta-minimo-32-caracteres-aleatorios
```

### Boas Práticas

1. ✅ Sempre usar HTTPS em produção
2. ✅ Armazenar JWT em localStorage/sessionStorage (Frontend)
3. ✅ Renovar token antes de expirar (24h)
4. ✅ Implementar logout (remover token no cliente)
5. ✅ Usar CORS apenas com origens confiáveis
6. ❌ Não expor JWT em URLs
7. ❌ Não armazenar passwords em plain text

---

## 7. Configuração por Ambiente

### Desenvolvimento (application-dev.yaml)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:sfp_dev
  jpa:
    show-sql: true
  h2:
    console:
      enabled: true
      path: /h2

api:
  doc:
    version: 1.0.0-DEV
```

**Acessos**:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html
- H2: http://localhost:8080/h2
- Dados: Em memória (limpo ao reiniciar)

### Produção (application-prd.yaml)

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASS}
  jpa:
    show-sql: false
```

**Acessos**:
- API: https://seu-dominio.com
- Swagger: https://seu-dominio.com/swagger-ui/index.html
- Dados: PostgreSQL persistente

---

## 8. Exemplos de Uso via cURL

### Registrar novo usuário

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "novo@example.com",
    "password": "Senha@123456",
    "fullName": "Novo Usuário",
    "role": "USER"
  }'
```

### Fazer login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "novo@example.com",
    "password": "Senha@123456"
  }' \
  | jq '.' > response.json

# Extrair token
TOKEN=$(jq -r '.token' response.json)
echo "Token: $TOKEN"
```

### Acessar endpoint protegido

```bash
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer $TOKEN" \
  | jq '.'
```

---

## 9. Migração de Banco de Dados (Flyway)

### Criar nova migração

1. Arquivo: `src/main/resources/db/migration/V3__create_transactions_table.sql`

```sql
CREATE TABLE transactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id),
  description VARCHAR(255),
  amount NUMERIC(15, 2),
  type VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. Flyway executa automaticamente no startup
3. Versionamento: V1__, V2__, V3__, etc.

---

## 10. Troubleshooting

### Erro: "Invalid JWT token"
- ✅ Verificar se token foi copiado corretamente
- ✅ Verificar se JWT_SECRET é igual em todas as instâncias
- ✅ Verificar se token não expirou (24h)

### Erro: "Email already exists"
- ✅ Usar email único no registro
- ✅ Em DEV: Deletar banco (H2 é in-memory)

### Erro: "CORS error"
- ✅ Frontend em origem diferente? Implementar CORS em SecurityConfig
- ✅ Verificar header `Access-Control-Allow-Origin`

### Erro: "Connection refused (PostgreSQL)"
- ✅ Verificar se PostgreSQL está rodando
- ✅ Verificar DB_URL, DB_USER, DB_PASS
- ✅ Verificar firewall/network

---

## 11. Roadmap Futuro

### v1.1.0
- [ ] Endpoint para atualizar perfil do usuário
- [ ] Endpoint para mudar senha
- [ ] Soft delete de usuários
- [ ] Rate limiting

### v1.2.0
- [ ] Autenticação com 2FA
- [ ] Refresh token
- [ ] Google OAuth integration
- [ ] Auditoria de logs

### v2.0.0
- [ ] API versioning `/api/v2/**`
- [ ] GraphQL endpoint
- [ ] WebSocket para notificações

---

## 12. Referências e Links Úteis

### Documentação Oficial
- [Spring Boot 4.0.1](https://docs.spring.io/spring-boot/4.0.1/reference/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [JWT.io](https://jwt.io/)

### Ferramentas
- [Postman](https://www.postman.com/)
- [cURL](https://curl.se/)
- [jq (JSON processor)](https://stedolan.github.io/jq/)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)

### Padrões
- [OpenAPI 3.0 Spec](https://spec.openapis.org/oas/v3.0.0)
- [REST Best Practices](https://restfulapi.net/)
- [HTTP Status Codes](https://httpwg.org/specs/rfc7231.html#status.codes)

---

**Versão do Documento**: 1.0.0  
**Última Atualização**: 20 de janeiro de 2025  
**Mantido por**: SFP Team
