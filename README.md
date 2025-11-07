# TucasDesk

<p align="center">
  <img src="tucasdesk-frontend/public/tucas-icon-nobg.png" alt="TucasDesk Logo" width="120">
</p>

TucasDesk é uma plataforma open-source de helpdesk que centraliza o atendimento e facilita o acompanhamento de chamados. Ideal para equipes que precisam organizar solicitações, priorizar demandas e manter o suporte sob controle.

## Visão Geral

TucasDesk oferece uma experiência completa para usuários, técnicos e administradores. Com ele é possível registrar, acompanhar e encerrar chamados em poucos cliques, garantindo transparência em todo o ciclo de suporte.

### Principais Recursos

- **Gestão de chamados:** criação, atualização, categorização e encerramento.
- **Autenticação segura:** login, registro e perfis com controle de acesso.
- **Dashboard em tempo real:** indicadores de chamados abertos, fechados e tarefas pendentes.
- **Administração de usuários:** gerenciamento de permissões e perfis.

### Estrutura Essencial do Projeto

- `tucasdesk-backend/`: API em Spring Boot responsável pelas regras de negócio e integrações com o banco de dados.
- `tucasdesk-frontend/`: interface web em React + TypeScript com componentes reutilizáveis e navegação protegida.
- `compose.yaml`: orquestração dos serviços (frontend, backend e banco MySQL) via Docker Compose.

## Tecnologias Utilizadas

- **Java 21 + Spring Boot 3:** linguagem e framework escolhidos para entregar uma API robusta, segura e fácil de manter.
- **Spring Data JPA:** abstrai o acesso ao banco de dados, agilizando consultas e persistência de entidades.
- **Spring Security + JWT:** garante autenticação e autorização com tokens, mantendo o acesso protegido.
- **MySQL:** banco relacional utilizado para armazenar chamados, usuários e configurações de forma confiável.
- **React 19 + TypeScript:** interface moderna, tipada e reativa que melhora a experiência do usuário e a produtividade do time.
- **Vite:** ferramenta de build e dev server que acelera o desenvolvimento frontend.
- **Axios:** cliente HTTP que simplifica a comunicação entre frontend e backend.
- **Docker & Docker Compose:** padronizam o ambiente, possibilitando subir toda a stack com um único comando.

## Como Começar

### Pré-requisitos

Instale as ferramentas abaixo antes de iniciar:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Subindo tudo com Docker Compose (recomendado)

1. Clone o repositório e acesse a pasta do projeto:

   ```sh
   git clone https://github.com/felipebehling/tucasdesk.git
   cd tucasdesk
   ```

2. Crie o `.env` compartilhado na raiz com os valores obrigatórios. Comece copiando o arquivo de exemplo e, em seguida, ajuste cada variável conforme o ambiente desejado:

   ```sh
   cp .env.example .env
   ```

   Você pode usar o `.env.example` como base para os segredos do backend e complementar com as variáveis exigidas pelo Docker Compose:

   | Variável | Descrição |
   | --- | --- |
   | `DB_VENDOR` | Define o driver utilizado pelo backend (`mysql` ou `mariadb`). |
   | `DB_HOST` | Hostname utilizado pelos serviços para se conectar ao banco (geralmente `database`). |
   | `DB_PORT` | Porta interna exposta pelo banco (padrão `3306`). |
   | `DB_ROOT_PASSWORD` | Senha do usuário administrador do banco. |
   | `DB_USER` | Usuário de aplicação que será criado na inicialização. |
   | `DB_PASSWORD` | Senha do usuário de aplicação. |
   | `DB_NAME` | Nome do banco utilizado pela aplicação. |
   | `DATABASE_URL` | URL de conexão no formato aceito pelo backend (`mysql://` ou `mariadb://`). |
   | `SPRING_DATASOURCE_URL` | URL JDBC utilizada pela API. |
   | `SPRING_DATASOURCE_USERNAME` | Usuário JDBC do Spring. |
   | `SPRING_DATASOURCE_PASSWORD` | Senha JDBC do Spring. |
   | `SPRING_ACTIVE_DATABASE_PROFILE` | Perfil complementar para alternar entre `mysql` (padrão) e `mariadb`. |
   | `VITE_API_URL` | URL interna usada pelo frontend para chamar a API. |
   | `JWT_SECRET` | Segredo para assinar tokens JWT. |
   | `JWT_EXPIRATION` | Tempo de expiração dos tokens JWT em milissegundos. |

   > 💡 Utilize `.env.mariadb` como referência se quiser partir de um conjunto pronto para o perfil MariaDB.

3. Gere o arquivo de variáveis do frontend copiando o modelo padrão:

   ```sh
   cp tucasdesk-frontend/.env.example tucasdesk-frontend/.env
   ```

   O valor de `VITE_API_URL` precisa apontar para a URL onde o backend estará acessível (por padrão, `http://tucasdesk-backend:8080` nos containers ou `http://localhost:8080` para execução local).

4. Suba os serviços desejados:

   ```sh
   # Perfil padrão utilizando MySQL
   docker compose up --build

   # Perfil alternativo com MariaDB
   docker compose --profile mariadb up --build
   ```

   Finalize a execução com `docker compose down`.

Serviços disponíveis:

| Serviço | Perfil | Porta | Observações |
| --- | --- | --- | --- |
| Frontend (Nginx) | `mysql` / `mariadb` | `3000` | Interface web do TucasDesk. |
| Backend (Spring Boot) | `mysql` / `mariadb` | `8080` | API REST da aplicação. |
| Banco de dados | `mysql` | `3307` → `3306` | Credenciais configuradas via `.env` (MySQL 8). |
| Banco de dados | `mariadb` | `3307` → `3306` | Credenciais configuradas via `.env` (MariaDB 10.11). |

Resumo das credenciais padrão sugeridas:

| Perfil | Imagem | Porta exposta (host → container) | Usuário root | Senha root | Usuário de aplicação | Senha de aplicação |
| --- | --- | --- | --- | --- | --- | --- |
| `mysql` | `mysql:8.0` | `3307` → `3306` | `root` | `DB_ROOT_PASSWORD` | `DB_USER` | `DB_PASSWORD` |
| `mariadb` | `mariadb:10.11` | `3307` → `3306` | `root` | `DB_ROOT_PASSWORD` | `DB_USER` | `DB_PASSWORD` |

> 📌 O Docker Compose é o caminho principal para executar a stack completa. A execução local (sem containers) é opcional e está detalhada na seção a seguir apenas para quem precisar personalizar ou depurar serviços individualmente.

### Executando o backend localmente (opcional)

```sh
docker compose up -d db
cd tucasdesk-backend
./mvnw spring-boot:run
```

A API ficará disponível em [http://localhost:8080](http://localhost:8080) e utiliza o banco configurado em `src/main/resources/application.properties`.

### Variáveis de ambiente do backend

O backend lê as configurações sensíveis a partir de variáveis de ambiente. Todas elas possuem defaults pensados para o desenvolvimento local e podem ser sobrescritas conforme o ambiente.

| Variável | Descrição | Valor padrão |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC do banco de dados. | `jdbc:mysql://localhost:3307/tucasdesk?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | Driver JDBC utilizado pelo Spring. | `com.mysql.cj.jdbc.Driver` (perfil `mariadb` troca para `org.mariadb.jdbc.Driver`) |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados. | `user` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco de dados. | `password` |
| `SPRING_JPA_DATABASE_PLATFORM` | Dialeto do Hibernate utilizado pelo JPA. | `org.hibernate.dialect.MySQLDialect` (perfil `mariadb` troca para `org.hibernate.dialect.MariaDBDialect`) |
| `APP_CORS_ALLOWED_ORIGINS` | Lista de origens liberadas para o CORS (separadas por vírgula). | `http://localhost:5173,http://localhost:3000` (no perfil `docker`, o padrão é `http://localhost:3000`) |
| `SPRING_PROFILES_ACTIVE` | Perfis ativos do Spring Boot. Utilize `docker` ao executar via Compose. | *(sem padrão)* |
| `SPRING_ACTIVE_DATABASE_PROFILE` | Complemento do perfil ativo usado no Docker Compose para alternar entre MySQL (`mysql`) e MariaDB (`mariadb`). | *(sem padrão — `mysql` é aplicado como fallback)* |
| `JWT_SECRET` | Segredo usado para assinar os tokens JWT. | *(sem padrão — configure no `.env`)* |
| `JWT_EXPIRATION` | Tempo de expiração do token JWT em milissegundos. | *(sem padrão — configure no `.env`)* |
| `AWS_REGION` | Região padrão da AWS para integrações de mensageria. | `us-east-1` |
| `AWS_SNS_TOPIC_ARN` | ARN do tópico SNS utilizado para envio de mensagens. | *(vazio)* |
| `AWS_SQS_QUEUE_NAME` | Nome da fila SQS que receberá as mensagens. | *(vazio)* |

> 💡 Crie um arquivo `.env` na raiz do projeto (pode usar `.env.example` como base) para informar `JWT_SECRET` e `JWT_EXPIRATION` antes de subir os containers com Docker Compose. Para testar com MariaDB, utilize o arquivo `.env.mariadb` como referência (por exemplo, `docker compose --profile mariadb --env-file .env.mariadb up`).

### Executando o frontend localmente (opcional)

```sh
cd tucasdesk-frontend
cp .env.example .env
npm install
npm run dev
```

O Vite servirá o frontend em [http://localhost:5173](http://localhost:5173) e encaminhará as chamadas para a API configurada no `.env`. Ajuste o valor de `VITE_API_URL` conforme necessário (por exemplo, `http://localhost:8080` ao executar o backend fora do Docker).

### Testes e verificações

```sh
# Backend
cd tucasdesk-backend
./mvnw test

# Frontend
cd tucasdesk-frontend
npm run lint
```

## Uso Rápido

1. Acesse `http://localhost:3000/registro` para criar sua conta.
2. Faça login em `http://localhost:3000/login`.
3. Utilize o dashboard para visualizar o panorama dos chamados.
4. Abra novos chamados ou gerencie os existentes pelo menu "Chamados".
5. Administre usuários e permissões pela página "Usuários" (perfil administrador).

## Como contribuir

1. Faça um fork do repositório.
2. Crie uma branch com a sua feature ou correção.
3. Envie um pull request descrevendo as mudanças.

## Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.

