# SFP - Sistema Financeiro Particular
## Guia de Desenvolvimento e Deployment

---

## 🎯 Visão Geral

O **SFP** é uma API REST completa com:
- ✅ Autenticação JWT de 24 horas
- ✅ Documentação Swagger/OpenAPI dinâmica
- ✅ Grupos de endpoints por domínio
- ✅ Suporte para DEV (H2) e PRD (PostgreSQL)
- ✅ Anotações descritivas em português

---

## 📋 Pré-requisitos

- Java 25+
- Maven 3.8.1+
- PostgreSQL 12+ (apenas para produção)
- Git

---

## 🚀 Iniciando em Desenvolvimento

### 1. Clonar e preparar
```bash
cd /path/to/sfp
mvn clean install
```

### 2. Rodar em DEV
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Ou via JAR:
```bash
mvn clean package
java -jar target/sfp-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### 3. Acessar recursos
- **API Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API Docs JSON**: http://localhost:8080/v3/api-docs
- **H2 Console**: http://localhost:8080/h2 (user: sa, pass: vazio)

---

## 🔐 Variáveis de Ambiente

### DEV
```bash
# .env ou system env
JWT_SECRET=dev-secret-key-change-in-production
DB_USER_PASSWORD_HASH=$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36DxJwFa
```

### PRD
```bash
# Variáveis obrigatórias
DB_URL=jdbc:postgresql://localhost:5432/sfp_prod
DB_USER=sfp_user
DB_PASS=secure_password
JWT_SECRET=sua-chave-secreta-longa-minimo-32-caracteres
DB_USER_PASSWORD_HASH=$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36DxJwFa
```

---

## 📦 Build e Deployment

### Build JAR
```bash
mvn clean package -DskipTests
```

### Deploy em PRD
```bash
# 1. Transferir JAR
scp target/sfp-0.0.1-SNAPSHOT.jar user@server:/opt/sfp/

# 2. SSH para servidor
ssh user@server

# 3. Rodar com variáveis de ambiente
cd /opt/sfp
java -jar sfp-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prd \
  --spring.datasource.url=$DB_URL \
  --spring.datasource.username=$DB_USER \
  --spring.datasource.password=$DB_PASS \
  --jwt.secret=$JWT_SECRET \
  --server.port=8080
```

### Usar systemd (recomendado)
```bash
# /etc/systemd/system/sfp.service
[Unit]
Description=SFP - Sistema Financeiro Particular
After=network.target

[Service]
Type=simple
User=sfp
ExecStart=/usr/bin/java -jar /opt/sfp/sfp-0.0.1-SNAPSHOT.jar --spring.profiles.active=prd
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target

# Ativar
sudo systemctl enable sfp
sudo systemctl start sfp
sudo systemctl status sfp
```

---

## 🧪 Testes

### Testar Autenticação
```bash
# 1. Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@123456",
    "fullName": "Test User",
    "role": "USER"
  }' | jq .

# 2. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@123456"
  }' | jq . > login_response.json

# 3. Extrair token
TOKEN=$(jq -r '.token' login_response.json)

# 4. Acessar endpoint protegido
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer $TOKEN" | jq .
```

### Testar via Swagger UI
1. Abra http://localhost:8080/swagger-ui/index.html
2. Clique em `/auth/login`
3. Execute "Try it out"
4. Copie o token da resposta
5. Clique no botão "Authorize" (cadeado)
6. Cole: `Bearer {token_aqui}`
7. Teste endpoints protegidos

---

## 📚 Estrutura de Documentação

### Metadados Dinâmicos
Arquivo: `src/main/resources/application.yaml`
```yaml
api:
  doc:
    title: SFP - Sistema Financeiro Particular
    description: API REST com autenticação JWT
    version: 1.0.0
    contact-name: Suporte SFP
    contact-email: support@sfp.com
```

### Grupos de Endpoints
Arquivo: `src/main/java/com/oldp1e/sfp/config/OpenApiConfig.java`
```java
@Bean
public GroupedOpenApi authApi() {
    return GroupedOpenApi.builder()
            .group("Autenticação")
            .pathsToMatch("/auth/**")
            .build();
}
```

### Anotações em Endpoints
```java
@PostMapping("/login")
@Operation(
    summary = "Realizar login",
    description = "Autentica um usuário..."
)
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Login realizado"),
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
})
public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request)
```

---

## 🔄 Fluxo de Atualização de Documentação

1. **Código muda** → anotações @Operation, @Schema, etc.
2. **Aplicação reinicia** → springdoc lê as anotações
3. **Swagger UI regenera** → `/v3/api-docs` é atualizado em tempo real
4. **Navegador refresh** → UI mostra mudanças automaticamente

**Não é necessário** gerar nada manualmente!

---

## 🛠️ Troubleshooting

### Porta 8080 já em uso
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID {PID} /F

# Linux
lsof -i :8080
kill -9 {PID}
```

### Token expirado
Faça login novamente para obter novo token. Validade: 24 horas.

### Erro de CORS
Se integrar com frontend, adicionar CORS em SecurityConfig:
```java
@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*");
        }
    };
}
```

### Banco de dados não inicializa
Verificar variáveis de ambiente e permissões Flyway em `src/main/resources/db/migration/`

---

## 📊 Monitoramento em Produção

### Logs
```bash
# Ver logs em tempo real
tail -f /var/log/sfp/app.log

# Grep por erros
grep "ERROR" /var/log/sfp/app.log
```

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Métricas (se habilitadas)
```bash
curl http://localhost:8080/actuator/metrics
```

---

## 🔄 Versionamento de API

### Versões Suportadas
- **1.0.0**: Autenticação JWT, CRUD básico
- **Futuro**: Rate limiting, dois fatores, etc.

### Mudanças Futuras (Breaking Changes)
- Versionar via `/api/v2/auth/login`
- Manter `/v1/**` por 6 meses antes de deprecar
- Documentar em CHANGELOG.md

---

## 📖 Documentação Adicional

- [HELP.md](./HELP.md) - Guia de uso da API
- [Spring Boot Docs](https://docs.spring.io/spring-boot/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [JWT.io](https://jwt.io/)

---

## 📧 Suporte e Contribuições

Para contribuir:
1. Fork o repositório
2. Crie uma branch: `git checkout -b feature/sua-feature`
3. Commit: `git commit -am 'Adiciona feature'`
4. Push: `git push origin feature/sua-feature`
5. Abra um Pull Request

**Email de suporte**: support@sfp.com

---

**Última atualização**: 20 de janeiro de 2025  
**Versão da API**: 1.0.0  
**Java**: 25  
**Spring Boot**: 4.0.1
