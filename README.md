# HelpDesk — Back-End

Sistema de gerenciamento de chamados de suporte técnico desenvolvido para o Desafio Técnico da Solutis.

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada em microsserviços, composta por:

* **Gateway Service** — ponto de entrada da aplicação e roteamento das requisições.
* **User Service** — gerenciamento de usuários.
* **Ticket Service** — criação, consulta, atualização, atribuição e encerramento de chamados.
* **Notification Service** — processamento e registro de notificações através de eventos.
* **Shared Service** — componentes compartilhados entre os microsserviços.

Fluxo principal:

```text
Cliente
   ↓
API Gateway
   ↓
┌──────────────┬───────────────┬────────────────────┐
│ User Service │ Ticket Service│ Notification       │
│              │               │ Service            │
└──────┬───────┴───────┬───────┴────────────────────┘
       │                │
    User DB          Ticket DB
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

## ▶️ Execução

Clone o projeto:

```bash
git clone https://github.com/HelpDesk-Desafio-Solutis/Back-End.git
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

O Docker Compose inicializa os quatro microsserviços, três bancos MySQL, RabbitMQ e MailHog.

## 🔌 Principais endpoints

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

| Serviço              | Banco                   |
| -------------------- | ----------------------- |
| User Service         | `helpdesk_user`         |
| Ticket Service       | `helpdesk_ticket`       |
| Notification Service | `helpdesk_notification` |

A persistência é realizada utilizando **JPA/Hibernate + MySQL**.

Os serviços não acessam diretamente o banco de dados pertencente a outro microsserviço.

## 🧠 Principais decisões arquiteturais

* Separação dos domínios em microsserviços.
* Banco de dados independente por serviço.
* API Gateway como ponto único de entrada.
* RabbitMQ para comunicação assíncrona e processamento de notificações.
* JWT para autenticação.
* Docker Compose para execução completa do ambiente.
* MailHog para testes de envio de e-mails sem utilização de um servidor SMTP real.
