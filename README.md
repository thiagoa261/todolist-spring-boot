# 📝 Todo List API

Uma API RESTful para gerenciamento de tarefas desenvolvida com Spring Boot e PostgreSQL.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.4**
  - Spring Web
  - Spring Data JPA
  - Spring Boot DevTools
- **PostgreSQL** - Banco de dados relacional
- **Lombok** - Redução de código boilerplate
- **BCrypt** - Criptografia de senhas
- **Maven** - Gerenciamento de dependências
- **Docker Compose** - Containerização do banco de dados

## 📋 Funcionalidades

### Gerenciamento de Usuários
- ✅ Cadastro de novos usuários
- ✅ Validação de username único
- ✅ Criptografia de senhas com BCrypt
- ✅ Autenticação HTTP Basic

### Gerenciamento de Tarefas
- ✅ Criação de tarefas
- ✅ Listagem de tarefas do usuário autenticado
- ✅ Atualização de tarefas
- ✅ Validação de propriedade de tarefas
- ✅ Validação de datas (início e término)

## 📡 Endpoints da API

### Usuários

#### Criar Usuário
```http
POST /users/create
Content-Type: application/json

{
  "username": "joaosilva",
  "name": "João Silva",
  "password": "senha123"
}
```

---

### Tarefas

> ⚠️ **Atenção:** Todos os endpoints de tarefas requerem autenticação HTTP Basic

#### Listar Tarefas
```http
GET /tasks
Authorization: Basic <base64(username:password)>
```

#### Criar Tarefa
```http
POST /tasks/create
Authorization: Basic <base64(username:password)>
Content-Type: application/json

{
  "title": "Estudar Spring Boot",
  "description": "Aprender sobre JPA e autenticação",
  "startAt": "2026-01-20T09:00:00",
  "endAt": "2026-01-20T12:00:00",
  "priority": "ALTA"
}
```

#### Atualizar Tarefa
```http
PUT /tasks/update/{id}
Authorization: Basic <base64(username:password)>
Content-Type: application/json

{
  "title": "Novo título",
  "description": "Nova descrição",
  "priority": "MÉDIA"
}
```

## 🔐 Autenticação

A API utiliza HTTP Basic Authentication para proteger os endpoints de tarefas.

### Como autenticar:

1. Crie um usuário através do endpoint `/users/create`
2. Para acessar os endpoints de tarefas, envie o header `Authorization`:
   ```
   Authorization: Basic <base64(username:password)>
   ```

**Exemplo com cURL:**
```bash
curl -X GET http://localhost:8000/tasks \
  -u "joaosilva:senha123"
```

**Exemplo manual do header:**
```
username: joaosilva
password: senha123
Base64: am9hb3NpbHZhOnNlbmhhMTIz

Authorization: Basic am9hb3NpbHZhOnNlbmhhMTIz
```