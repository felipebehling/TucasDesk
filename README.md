# TucasDesk

<p align="center">
  <img src="apps/frontend/public/tucas-icon-nobg.png" alt="TucasDesk Logo" width="120">
</p>

O TucasDesk é uma plataforma de helpdesk de código aberto, projetada para centralizar o atendimento e simplificar o rastreamento de chamados. É a solução ideal para equipes que buscam organizar solicitações, priorizar demandas e manter um controle eficiente do suporte.

## Índice

- [Visão Geral](#visão-geral)
- [Arquitetura do Sistema](#arquitetura-do-sistema)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Como Começar](#como-começar)
- [Uso Rápido](#uso-rápido)
- [Como Contribuir](#como-contribuir)
- [Licença](#licença)

## Visão Geral

O TucasDesk oferece uma experiência de usuário completa, atendendo às necessidades de usuários, técnicos e administradores. Com esta plataforma, é possível registrar, acompanhar e resolver chamados com apenas alguns cliques, assegurando transparência total em todo o ciclo de vida do suporte.

## Arquitetura do Sistema

A arquitetura do TucasDesk foi concebida para ser escalável, desacoplada e resiliente, utilizando uma combinação de serviços síncronos e assíncronos para otimizar tanto a experiência do usuário quanto a eficiência operacional.

O diagrama a seguir ilustra os principais componentes e o fluxo de comunicação entre eles:

![Arquitetura do TucasDesk](docs/images/architecture-diagram.jpeg)

### Componentes Principais

1.  **Frontend/Cliente (React + TypeScript)**:
    *   Interface web onde os usuários (clientes ou técnicos) interagem com o sistema.
    *   Responsável por consumir os endpoints da API para criar, visualizar e gerenciar chamados e interações.
    *   Realiza a validação de autenticação com a API, que, por sua vez, delega a verificação ao AWS Cognito.

2.  **API e Serviços Síncronos (Spring Boot)**:
    *   **Endpoint-Tickets**: Recebe requisições para criar (`TicketCreated`), fechar (`TicketClosed`) ou interagir (`TicketInteracted`) com um chamado.
    *   **Service-Tickets e Service-Interações**: Contêm a lógica de negócio principal, orquestrando as operações de CRUD (criar, ler, atualizar, deletar) no banco de dados e publicando eventos para notificação.
    *   **Publica Evento**: Ao criar, fechar ou adicionar uma interação, a API publica eventos (ex: `TicketCreated`, `TicketClosed`) em um tópico do AWS SNS, desacoplando a API da lógica de notificação.

3.  **Middleware e Serviços Assíncronos (AWS)**:
    *   **AWS SNS (Simple Notification Service)**: Atua como um tópico para distribuição de eventos, encaminhando mensagens da API para todas as filas SQS inscritas.
    *   **AWS SQS (Simple Queue Service)**: Fila que recebe os eventos do SNS, permitindo que o `Service: Notifier` os processe de forma assíncrona. Isso garante que nenhuma mensagem seja perdida, mesmo em caso de falha no serviço de notificação.
    *   **Service: Notifier**: Serviço que processa as mensagens da fila SQS, responsável por formatar e enviar e-mails através do AWS SES.

4.  **Persistência (MariaDB)**:
    *   Banco de dados relacional onde todos os dados de chamados, usuários e interações são armazenados. As operações de CRUD são gerenciadas pela API Spring Boot.

5.  **APIs Externas**:
    *   **AWS Cognito**: Serviço de gerenciamento de identidade da AWS, utilizado pela API para validar tokens de autenticação (`JWT`) enviados pelo frontend.
    *   **AWS SES (Simple Email Service)**: Serviço de envio de e-mails da AWS, utilizado pelo `Service: Notifier` para enviar notificações por e-mail quando eventos importantes ocorrem.

### Fluxo de um Novo Chamado

1.  O usuário cria um novo chamado no **Frontend**.
2.  O **Frontend** envia uma requisição para o **Endpoint-Ticket** na API Spring Boot.
3.  O **Service-Tickets** processa a requisição, salva os dados no **MariaDB** e publica um evento `TicketCreated` no tópico **AWS SNS**.
4.  O **AWS SNS** distribui o evento para a fila **AWS SQS**.
5.  O **Service: Notifier** consome a mensagem da fila SQS.
6.  O **Service: Notifier** utiliza o **AWS SES** para enviar um e-mail de notificação ao usuário.
7.  O **Frontend** recebe a resposta da API e atualiza a interface para o usuário.

Este design assegura que a API principal permaneça rápida e responsiva, enquanto tarefas mais demoradas, como o envio de e-mails, são executadas de forma confiável em segundo plano.

### Estrutura Essencial do Projeto

- `apps/backend/`: API em Spring Boot que gerencia as regras de negócio e a integração com o banco de dados.
- `apps/frontend/`: Interface web em React + TypeScript, com componentes reutilizáveis e navegação protegida.
- `infra/docker/`: Arquivos do Docker Compose, incluindo `compose.yaml` e scripts de inicialização do banco de dados.
- `config/env/`: Variáveis de ambiente compartilhadas, utilizadas durante a orquestração dos contêineres.

## Tecnologias Utilizadas

- **Java 21 + Spring Boot 3**: Linguagem e framework escolhidos para fornecer uma API robusta, segura e de fácil manutenção.
- **Spring Data JPA**: Abstrai o acesso ao banco de dados, agilizando as consultas e a persistência de entidades.
- **Spring Security + AWS Cognito**: Integra um sistema de autenticação gerenciada com suporte a MFA, recuperação de senha e rotação de tokens.
- **MariaDB**: Banco de dados relacional de alto desempenho, utilizado para armazenar chamados, usuários e configurações.
- **React 19 + TypeScript**: Proporciona uma interface moderna, tipada e reativa, melhorando a experiência do usuário e a produtividade da equipe.
- **Vite**: Ferramenta de build e servidor de desenvolvimento que acelera o ciclo de desenvolvimento do frontend.
- **Axios**: Cliente HTTP que simplifica a comunicação entre o frontend e o backend.
- **Docker & Docker Compose**: Padronizam o ambiente de desenvolvimento, permitindo que toda a stack seja iniciada com um único comando.

## Como Começar

### Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Subindo o Ambiente com Docker Compose (Recomendado)

1.  **Clone o repositório** e acesse o diretório do projeto:

    ```sh
    git clone https://github.com/felipebehling/tucasdesk.git
    cd tucasdesk
    ```

2.  **Crie o arquivo `.env`** em `config/env/`, utilizando o arquivo de exemplo como base. Em seguida, ajuste as variáveis conforme necessário.

    ```sh
    cp config/env/.env.example config/env/.env
    ```

    | Variável                      | Descrição                                                                                             |
    | ----------------------------- | ----------------------------------------------------------------------------------------------------- |
    | `DB_HOST`                     | Hostname para conexão com o banco de dados (geralmente `database`).                                   |
    | `DB_PORT`                     | Porta interna do banco de dados (padrão `3306`).                                                      |
    | `DB_ROOT_PASSWORD`            | Senha do usuário root do banco de dados.                                                              |
    | `DB_USER`                     | Usuário da aplicação, que será criado na inicialização.                                               |
    | `DB_PASSWORD`                 | Senha do usuário da aplicação.                                                                        |
    | `DB_NAME`                     | Nome do banco de dados utilizado pela aplicação.                                                      |
    | `SPRING_DATASOURCE_URL`       | URL JDBC para a API (ex: `jdbc:mariadb://database:3306/tucasdesk`).                                     |
    | `VITE_API_URL`                | URL interna utilizada pelo frontend para se comunicar com a API.                                      |
    | `AWS_COGNITO_REGION`          | Região da AWS onde o User Pool do Cognito está provisionado.                                          |
    | `AWS_COGNITO_USER_POOL_ID`    | ID do User Pool utilizado pela aplicação.                                                             |
    | `AWS_COGNITO_APP_CLIENT_ID`   | ID do App Client configurado no Cognito.                                                              |

3.  **Gere o arquivo de variáveis do frontend**:

    ```sh
    cp apps/frontend/.env.example apps/frontend/.env
    ```

    O valor de `VITE_API_URL` deve apontar para a URL onde o backend estará acessível (por padrão, `http://tucasdesk-backend:8080` para contêineres, ou `http://localhost:8080` para execução local).

4.  **Inicie os serviços** a partir da raiz do repositório:

    ```sh
    docker compose --env-file config/env/.env -f infra/docker/compose.yaml up --build
    ```

    Este comando inicializa o frontend, o backend e um banco de dados MariaDB. Para parar e remover os contêineres, utilize `docker compose --env-file config/env/.env -f infra/docker/compose.yaml down`.

### Executando Testes

Para garantir a qualidade e a estabilidade do código, execute os testes para o backend e o frontend:

```sh
# Testes do Backend
cd apps/backend
./mvnw test

# Linter do Frontend
cd apps/frontend
npm run lint
```

## Uso Rápido

1.  Acesse `http://localhost:3000/registro` para criar uma nova conta.
2.  Faça login em `http://localhost:3000/login`.
3.  Utilize o dashboard para ter uma visão geral dos chamados.
4.  Abra novos chamados ou gerencie os existentes através do menu "Chamados".
5.  Administre usuários e permissões na página "Usuários" (requer perfil de administrador).

## Como Contribuir

1.  Faça um fork do repositório.
2.  Crie uma nova branch para sua feature ou correção.
3.  Envie um pull request com uma descrição detalhada das suas alterações.

## Licença

Este projeto é distribuído sob a licença MIT. Para mais detalhes, consulte o arquivo `LICENSE`.
