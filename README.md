# HelpDesk — Back-End

Sistema de gerenciamento de chamados de suporte técnico desenvolvido para o Desafio Técnico da Solutis.

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada em microsserviços, composta por:

* **Gateway Service** — ponto de entrada da aplicação e roteamento das requisições.
* **User Service** — gerenciamento de usuários.
* **Ticket Service** — criação, consulta, atualização, atribuição e encerramento de chamados.
* **Notification Service** — processamento e registro de notificações através de eventos.
* **Shared** — componentes compartilhados entre os microsserviços.

Fluxo principal:

```text
React
   ↓
API Gateway
   ↓
┌──────────────┬───────────────┬────────────────────┐
│ User Service │ Ticket Service│ Notification       │
│              │               │ Service            │
└──────┬───────┴───────┬───────┴────────────────────┘
       │                │                  │
       ↓                ↓                  ↓
    User DB         Ticket DB      Notification DB
                         │
                         ↓
                     RabbitMQ
                         │
                         ↓
                 Notification Service
```

## 🛠️ Tecnologias

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA / Hibernate
* Spring Security
* JWT
* Spring Cloud Gateway
* Maven
* MySQL 8
* RabbitMQ
* MailHog
* Docker / Docker Compose
* Swagger / OpenAPI
* Lombok

## 📋 Pré-requisitos

* Git
* Docker
* Docker Compose

Java 21 e Maven são necessários apenas para execução/desenvolvimento fora dos containers.

## 📁 Estrutura dos repositórios

Para executar a aplicação completa através do Docker Compose, os dois repositórios devem estar dentro da mesma pasta:

```text
HelpDesk/
├── Back-End/
└── Front-End/
```

O `docker-compose.yml` do Back-End realiza a construção dos serviços e do Front-End.

## ▶️ Execução

Clone os dois projetos:

```bash
git clone https://github.com/HelpDesk-Desafio-Solutis/Back-End.git
git clone https://github.com/HelpDesk-Desafio-Solutis/Front-End.git
```

Entre na pasta do Back-End:

```bash
cd Back-End
```

Suba todo o ambiente:

```bash
docker compose up --build
```

Para executar em segundo plano:

```bash
docker compose up --build -d
```

O Docker Compose inicializa:

* Front-End
* API Gateway
* User Service
* Ticket Service
* Notification Service
* Três bancos MySQL
* RabbitMQ
* MailHog

### Portas

| Serviço | Porta |
|---|---:|
| Front-End | `5173` |
| API Gateway | `8089` |
| User Service | `8081` |
| Ticket Service | `8082` |
| Notification Service | `8080` |
| RabbitMQ | `5672` |
| RabbitMQ Management | `15672` |
| MailHog SMTP | `1025` |
| MailHog Web | `8025` |

## 🔐 Autenticação

A aplicação utiliza **JWT** para autenticação e autorização baseada em perfil.

Perfis disponíveis:

* `CLIENT`
* `TECHNICIAN`
* `ADMIN`

O API Gateway é responsável pela validação do token e pelo controle de acesso às rotas.

## 🔌 Principais endpoints

### Auth

```text
POST /api/auth/login
```

### Users

```text
POST   /api/users
GET    /api/users
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Tickets

```text
POST   /api/tickets
GET    /api/tickets
GET    /api/tickets/{id}
PUT    /api/tickets/{id}
DELETE /api/tickets/{id}
GET    /api/tickets/available
```

### Notifications

```text
GET /api/notifications
GET /api/notifications/{id}
```

As APIs são documentadas através de Swagger/OpenAPI.

## 🐇 Eventos RabbitMQ

O RabbitMQ é utilizado para comunicação assíncrona entre os serviços.

Principais eventos:

* `TicketCreated`
* `TicketAssigned`
* `TicketStatusChanged`

Exemplo:

```text
Ticket Service
      ↓
TicketCreated
      ↓
RabbitMQ
      ↓
Notification Service
      ↓
Criação da notificação
```

## 🗄️ Persistência

Cada microsserviço possui responsabilidade sobre seus próprios dados e banco:

| Serviço | Banco |
|---|---|
| User Service | `helpdesk_user` |
| Ticket Service | `helpdesk_ticket` |
| Notification Service | `helpdesk_notification` |

A persistência é realizada utilizando **JPA/Hibernate + MySQL**.

Os serviços não acessam diretamente o banco de dados pertencente a outro microsserviço.

## 🧪 Testes

O projeto possui testes automatizados utilizando **JUnit, Mockito e Spring Boot Test**, cobrindo regras importantes e endpoints principais dos microsserviços.

## 🧠 Principais decisões arquiteturais

* Separação dos domínios em microsserviços.
* Banco de dados independente por serviço.
* API Gateway como ponto único de entrada.
* RabbitMQ para comunicação assíncrona e processamento de notificações.
* JWT para autenticação e autorização por perfil.
* Docker Compose para execução completa do ambiente.
* MailHog para testes de envio de e-mails sem utilização de um servidor SMTP real.
* Organização dos microsserviços seguindo princípios de Clean Architecture.