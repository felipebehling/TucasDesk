# TucasDesk

<p align="center">
  <img src="apps/frontend/public/tucas-icon-nobg.png" alt="TucasDesk Logo" width="120">
</p>

TucasDesk é uma plataforma open-source de helpdesk que centraliza o atendimento e facilita o acompanhamento de chamados. Ideal para equipes que precisam organizar solicitações, priorizar demandas e manter o suporte sob controle.

## Visão Geral

TucasDesk oferece uma experiência completa para usuários, técnicos e administradores. Com ele é possível registrar, acompanhar e encerrar chamados em poucos cliques, garantindo transparência em todo o ciclo de suporte.

## Arquitetura do Sistema

A arquitetura do TucasDesk foi desenhada para ser escalável, desacoplada e resiliente, combinando serviços síncronos e assíncronos para otimizar a experiência do usuário e a eficiência operacional.

O diagrama abaixo detalha os principais componentes e o fluxo de comunicação entre eles:

![Arquitetura do TucasDesk](docs/images/architecture-diagram.jpeg)

### Componentes Principais

1.  **Frontend/Cliente (React + TypeScript)**:
    *   Interface web onde o usuário (cliente ou técnico) interage com o sistema.
    *   Responsável por consumir os endpoints da API para criar, visualizar e gerenciar chamados e interações.
    *   Realiza a validação de autenticação com a API, que delega a verificação para o AWS Cognito.

2.  **API e Serviços Síncronos (Spring Boot)**:
    *   **Endpoint-Tickets**: recebe requisições para criar (`TicketCreated`), fechar (`TicketClosed`) ou interagir (`TicketInteracted`) em um chamado.
    *   **Service-Tickets e Service-Interações**: contêm a lógica de negócio principal. Eles orquestram as operações de CRUD (criar, ler, atualizar, deletar) no banco de dados e publicam eventos para notificação.
    *   **Publica Evento**: ao criar, fechar ou adicionar uma interação a um chamado, a API publica eventos (ex: `TicketCreated`, `TicketClosed`) em um tópico do AWS SNS. Essa abordagem desacopla a API da lógica de notificação.

3.  **Middleware e Serviços Assíncronos (AWS)**:
    *   **AWS SNS (Simple Notification Service)**: atua como um tópico de distribuição de eventos. A API publica mensagens no SNS, que as encaminha para todas as filas SQS inscritas.
    *   **AWS SQS (Simple Queue Service)**: fila que recebe os eventos do SNS. O `Service: Notifier` consome mensagens desta fila para processá-las de forma assíncrona. Isso garante que, mesmo em caso de falha no serviço de notificação, a mensagem não será perdida.
    *   **Service: Notifier**: serviço que processa as mensagens da fila SQS. Ele é responsável por formatar e enviar e-mails utilizando o AWS SES.

4.  **Persistência (MariaDB)**:
    *   Banco de dados relacional onde todos os dados de chamados, usuários e interações são armazenados. As operações de CRUD são executadas pela API Spring Boot.

5.  **APIs Externas**:
    *   **AWS Cognito**: serviço de gerenciamento de identidade da AWS. A API o utiliza para validar os tokens de autenticação (`JWT`) enviados pelo frontend, garantindo que apenas usuários autenticados acessem os recursos.
    *   **AWS SES (Simple Email Service)**: serviço de envio de e-mails da AWS. O `Service: Notifier` o utiliza para enviar notificações por e-mail quando eventos importantes ocorrem (ex: confirmação de abertura de chamado).

### Fluxo de um Novo Chamado

1.  O usuário cria um novo chamado no **Frontend**.
2.  O **Frontend** envia uma requisição para o **Endpoint-Ticket** na API Spring Boot.
3.  O **Service-Tickets** processa a requisição, salva os dados no **MariaDB** (operação de CRUD) e publica um evento `TicketCreated` no tópico **AWS SNS**.
4.  O **AWS SNS** distribui o evento para a fila **AWS SQS**.
5.  O **Service: Notifier** consome a mensagem da fila SQS.
6.  O **Service: Notifier** utiliza o **AWS SES** para enviar um e-mail de notificação ao usuário.
7.  O **Frontend** recebe a resposta da API e atualiza a interface para o usuário.

Este design garante que a API principal permaneça rápida e responsiva, enquanto tarefas mais lentas, como o envio de e-mails, são executadas em segundo plano de forma confiável.


### Estrutura Essencial do Projeto

- `apps/backend/`: API em Spring Boot responsável pelas regras de negócio e integrações com o banco de dados.
- `apps/frontend/`: interface web em React + TypeScript com componentes reutilizáveis e navegação protegida.
- `infra/docker/`: arquivos do Docker Compose, incluindo `compose.yaml` e scripts de inicialização do banco.
- `config/env/`: variáveis de ambiente compartilhadas utilizadas durante a orquestração.

## Tecnologias Utilizadas

- **Java 21 + Spring Boot 3:** linguagem e framework escolhidos para entregar uma API robusta, segura e fácil de manter.
- **Spring Data JPA:** abstrai o acesso ao banco de dados, agilizando consultas e persistência de entidades.
- **Spring Security + AWS Cognito:** integra autenticação gerenciada com suporte a MFA, recuperação de senha e rotação de tokens.
- **MariaDB:** banco relacional principal para armazenar chamados, usuários e configurações com alto desempenho e confiabilidade.
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

2. Crie o `.env` compartilhado em `config/env/` com os valores obrigatórios. Comece copiando o arquivo de exemplo e, em seguida, ajuste cada variável conforme o ambiente desejado:

   ```sh
   cp config/env/.env.example config/env/.env
   ```

   Você pode usar o `config/env/.env.example` como base para os segredos do backend e complementar com as variáveis exigidas pelo Docker Compose:

   | Variável | Descrição |
   | --- | --- |
   | `DB_HOST` | Hostname utilizado pelos serviços para se conectar ao banco (geralmente `database`). |
   | `DB_PORT` | Porta interna exposta pelo banco (padrão `3306`). |
   | `DB_ROOT_PASSWORD` | Senha do usuário administrador do banco. |
   | `DB_USER` | Usuário de aplicação que será criado na inicialização. |
   | `DB_PASSWORD` | Senha do usuário de aplicação. |
   | `DB_NAME` | Nome do banco utilizado pela aplicação. |
   | `DATABASE_URL` | URL de conexão no formato aceito pelo backend (por exemplo, `mariadb://user:password@database:3306/tucasdesk`). |
   | `SPRING_DATASOURCE_URL` | URL JDBC utilizada pela API (por exemplo, `jdbc:mariadb://database:3306/tucasdesk`). |
   | `SPRING_DATASOURCE_USERNAME` | Usuário JDBC do Spring. |
   | `SPRING_DATASOURCE_PASSWORD` | Senha JDBC do Spring. |
   | `SPRING_ACTIVE_DATABASE_PROFILE` | Perfil complementar para ajustar a configuração do Spring (mantém `mariadb`, perfil hoje suportado). |
   | `VITE_API_URL` | URL interna usada pelo frontend para chamar a API. |
   | `AWS_COGNITO_REGION` | Região da AWS onde o User Pool está provisionado. |
   | `AWS_COGNITO_USER_POOL_ID` | Identificador do User Pool utilizado pela aplicação. |
   | `AWS_COGNITO_APP_CLIENT_ID` | ID do App Client configurado no Cognito. |
   | `AWS_COGNITO_ISSUER_URI` | (Opcional) Issuer URI público do User Pool. |
   | `AWS_COGNITO_JWK_SET_URI` | (Opcional) Endpoint JWKS. Caso não informado, é derivado do issuer. |

   > 💡 Utilize valores compatíveis com MariaDB como base (por exemplo, URLs `mariadb://` e `jdbc:mariadb://`). Caso escolha outra engine, adapte manualmente as variáveis para o driver correspondente.

3. Gere o arquivo de variáveis do frontend copiando o modelo padrão:

   ```sh
   cp apps/frontend/.env.example apps/frontend/.env
   ```

   O valor de `VITE_API_URL` precisa apontar para a URL onde o backend estará acessível (por padrão, `http://tucasdesk-backend:8080` nos containers ou `http://localhost:8080` para execução local).

4. Suba os serviços a partir da raiz do repositório:

   ```sh
   docker compose --env-file config/env/.env -f infra/docker/compose.yaml up --build
   ```

   O comando acima inicializa frontend, backend e um banco MariaDB 12.0 prontos para uso. Utilize `docker compose --env-file config/env/.env -f infra/docker/compose.yaml down` para parar e remover os containers quando terminar os testes.

Serviços disponíveis:

| Serviço | Porta | Observações |
| --- | --- | --- |
| Frontend (Nginx) | `3000` | Interface web do TucasDesk. |
| Backend (Spring Boot) | `8080` | API REST da aplicação. |
| Banco de dados (MariaDB) | `3307` → `3306` | MariaDB 12.0 com credenciais configuradas via `config/env/.env`. |

Resumo das credenciais padrão sugeridas:

| Perfil | Imagem | Porta exposta (host → container) | Usuário root | Senha root | Usuário de aplicação | Senha de aplicação | Observação |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `mariadb` (padrão) | `mariadb:12.0` | `3307` → `3306` | `root` | `DB_ROOT_PASSWORD` | `DB_USER` | `DB_PASSWORD` | Perfil recomendado e habilitado por padrão. |

> ℹ️ O Compose já está preparado para MariaDB 12.0 e não disponibiliza mais perfis MySQL. Se precisar utilizar outra engine, ajuste manualmente a configuração do Docker Compose e do backend.

> 📌 O Docker Compose é o caminho principal para executar a stack completa. A execução local (sem containers) é opcional e está detalhada na seção a seguir apenas para quem precisar personalizar ou depurar serviços individualmente.

### Provisionando o User Pool Cognito

O diretório [`infra/aws`](infra/aws) inclui o template CloudFormation [`cognito-user-pool.yaml`](infra/aws/cognito-user-pool.yaml), responsável por criar o User Pool, o app client com os fluxos `USER_SRP_AUTH`, `ALLOW_REFRESH_TOKEN_AUTH` e OAuth (Code + Implicit) habilitados, além dos grupos padrão (`Administrador`, `Técnico` e `Usuário`). Para realizar o deploy em uma conta AWS, execute:

```sh
aws cloudformation deploy \
  --template-file infra/aws/cognito-user-pool.yaml \
  --stack-name tucasdesk-cognito \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
  --parameter-overrides ProjectName=tucasdesk DomainPrefix=tucasdesk-helpdesk
```

Ao final da criação copie os outputs `UserPoolId`, `UserPoolClientId` e, se tiver configurado `DomainPrefix`, também `UserPoolDomainUrl` para as variáveis `AWS_COGNITO_USER_POOL_ID`, `AWS_COGNITO_APP_CLIENT_ID` e `AWS_COGNITO_ISSUER_URI` (ou `AWS_COGNITO_JWK_SET_URI`). Ajuste os parâmetros `CallbackUrls` e `LogoutUrls` conforme os domínios do frontend para garantir que o Hosted UI aceite o fluxo OAuth configurado.

### Habilitando o fluxo de refresh de tokens

Para que o endpoint `POST /auth/refresh` funcione corretamente é necessário configurar o App Client do Cognito com suporte à renovação de sessões. Garanta os seguintes pontos no User Pool utilizado pelo TucasDesk:

1. **Fluxo `ALLOW_REFRESH_TOKEN_AUTH` habilitado** – na configuração do App Client marque a opção *Enable username password auth for admin APIs for authentication (ALLOW_ADMIN_USER_PASSWORD_AUTH, ALLOW_REFRESH_TOKEN_AUTH)* ou ajuste o template CloudFormation para incluir o fluxo.
2. **Validade do Refresh Token** – defina o tempo de expiração conforme a política de segurança da organização (padrão sugerido: 30 dias). O frontend utiliza o refresh token armazenado para renovar o `idToken`/`accessToken` antes de expirar.
3. **Variáveis de ambiente** – informe no backend os valores `AWS_COGNITO_REGION`, `AWS_COGNITO_USER_POOL_ID`, `AWS_COGNITO_APP_CLIENT_ID`, `AWS_COGNITO_ISSUER_URI` (opcional) e `AWS_COGNITO_JWK_SET_URI` (opcional). O frontend consome o endpoint `/auth/refresh` automaticamente quando recebe `401` do backend, portanto mantenha as URLs (`VITE_API_URL`) apontando para a instância correta.

Com essa configuração o backend poderá trocar o refresh token por novos `idToken`/`accessToken`, mantendo a sessão do usuário alinhada entre frontend e Cognito.

### Executando o backend localmente (opcional)

```sh
docker compose --env-file config/env/.env -f infra/docker/compose.yaml up -d db
cd apps/backend
./mvnw spring-boot:run
```

A API ficará disponível em [http://localhost:8080](http://localhost:8080) e utiliza o banco configurado em `src/main/resources/application.properties`.

### Variáveis de ambiente do backend

O backend lê as configurações sensíveis a partir de variáveis de ambiente. Todas elas possuem defaults pensados para o desenvolvimento local e podem ser sobrescritas conforme o ambiente.

| Variável | Descrição | Valor padrão |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC do banco de dados. | `jdbc:mariadb://localhost:3307/tucasdesk?useSSL=true&serverTimezone=UTC` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | Driver JDBC utilizado pelo Spring. | `org.mariadb.jdbc.Driver` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados. | `user` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco de dados. | `password` |
| `SPRING_JPA_DATABASE_PLATFORM` | Dialeto do Hibernate utilizado pelo JPA. | `org.hibernate.dialect.MariaDBDialect` |
| `APP_CORS_ALLOWED_ORIGINS` | Lista de origens liberadas para o CORS (separadas por vírgula). | `http://localhost:5173,http://localhost:3000` (no perfil `docker`, o padrão é `http://localhost:3000`) |
| `SPRING_PROFILES_ACTIVE` | Perfis ativos do Spring Boot. Utilize `docker` ao executar via Compose. | *(sem padrão)* |
| `SPRING_ACTIVE_DATABASE_PROFILE` | Complemento do perfil ativo usado no Docker Compose. | *(sem padrão — `mariadb` é aplicado como fallback e é o único perfil disponível atualmente)* |
| `AWS_COGNITO_REGION` | Região da AWS onde o User Pool está provisionado. | *(sem padrão — configure em `config/env/.env`)* |
| `AWS_COGNITO_USER_POOL_ID` | Identificador do User Pool utilizado pela aplicação. | *(sem padrão — configure em `config/env/.env`)* |
| `AWS_COGNITO_APP_CLIENT_ID` | ID do App Client utilizado para autenticação. | *(sem padrão — configure em `config/env/.env`)* |
| `AWS_COGNITO_ISSUER_URI` | (Opcional) Issuer URI público do User Pool. | *(vazio)* |
| `AWS_COGNITO_JWK_SET_URI` | (Opcional) Endpoint JWKS do Cognito. | *(vazio)* |
| `AWS_REGION` | Região padrão da AWS para integrações de mensageria. | `sa-east-1` |
| `AWS_SNS_TOPIC_ARN` | ARN genérico utilizado como fallback quando tópicos dedicados não estão configurados. | *(vazio)* |
| `AWS_SQS_QUEUE_NAME` | Nome da fila SQS que receberá as mensagens legadas. | *(vazio)* |
| `AWS_SNS_TICKET_CREATED_TOPIC_ARN` | ARN do tópico SNS exclusivo para eventos `TicketCreated`. | *(vazio)* |
| `AWS_SNS_TICKET_CLOSED_TOPIC_ARN` | ARN do tópico SNS exclusivo para eventos `TicketClosed`. | *(vazio)* |
| `AWS_SES_ENABLED` | Habilita o envio real de e-mails pelo AWS SES (`true`/`false`). | `false` |
| `AWS_SES_REGION` | Região onde as identidades do SES foram verificadas. | *(herda `AWS_REGION` quando vazio)* |
| `AWS_SES_FROM_ADDRESS` | Endereço verificado no SES que aparecerá como remetente. | *(vazio — obrigatório quando o SES estiver habilitado)* |
| `AWS_SES_REPLY_TO_ADDRESS` | Endereço que receberá as respostas do e-mail. | *(vazio)* |
| `AWS_SES_TO_ADDRESSES` | Lista de destinatários separados por vírgula para receber notificações. | *(vazio — obrigatório quando o SES estiver habilitado)* |
| `AWS_SES_TEMPLATE_NAME` | Nome do template do SES usado pelo `Notifier`. | `tucasdesk-ticket-update` |
| `AWS_SES_CONFIGURATION_SET` | Configuration Set opcional para métricas no SES. | *(vazio)* |
| `AWS_ACCESS_KEY_ID` | Chave de acesso utilizada pelo SDK ao invocar SES/SNS/SQS. | *(vazio — utilize perfis IAM ou variáveis seguras)* |
| `AWS_SECRET_ACCESS_KEY` | Segredo associado à chave de acesso. | *(vazio — utilize perfis IAM ou variáveis seguras)* |

> 💡 Mantenha as variáveis sensíveis no arquivo `config/env/.env` (utilize `config/env/.env.example` como base) para configurar Cognito (`AWS_COGNITO_REGION`, `AWS_COGNITO_USER_POOL_ID` e `AWS_COGNITO_APP_CLIENT_ID`) e, quando for enviar e-mails de verdade, informe também as credenciais/identidades do SES (`AWS_SES_ENABLED`, `AWS_SES_FROM_ADDRESS`, `AWS_SES_TO_ADDRESSES`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`). Ajuste variáveis como `SPRING_DATASOURCE_URL` e `DATABASE_URL` para o formato `mariadb` (por exemplo, `jdbc:mariadb://...`).

Para provisionar rapidamente os tópicos SNS dedicados e a role com permissão de publicação, utilize o template CloudFormation localizado em `infra/aws/ticket-notifications.yaml`.

#### Configuração do AWS SES

1. **Verifique o domínio ou remetente:** no console do SES, acesse *Verified identities* e adicione o domínio corporativo (recomendado) ou os e-mails individuais que serão usados em `AWS_SES_FROM_ADDRESS` e `AWS_SES_TO_ADDRESSES`. Conclua o processo de verificação DNS antes de habilitar o envio em produção.
2. **Saia do sandbox (se necessário):** para ambientes novos solicite o aumento de limite (Production access) informando o domínio verificado e o tipo de tráfego esperado.
3. **Crie o template de e-mail:** ainda no SES, registre um template com o nome configurado em `AWS_SES_TEMPLATE_NAME` contendo os placeholders `{{subject}}`, `{{body}}`, `{{eventType}}`, `{{ticketId}}` e `{{#interacao}}...{{/interacao}}` (para dados opcionais). Utilize HTML para a versão rica e inclua uma versão de texto puro se desejar compatibilidade com clientes legados.
4. **Configure as credenciais:** defina `AWS_ACCESS_KEY_ID` e `AWS_SECRET_ACCESS_KEY` (ou utilize um perfil IAM/role) com permissão `ses:SendEmail`, `ses:SendTemplatedEmail`, `sns:*` e `sqs:*` conforme o ambiente. Para uso local, armazene-as no `.env` e nunca faça commit desses valores.
5. **Ajuste as variáveis do backend:** atualize o arquivo `config/env/.env` com os valores de `AWS_SES_ENABLED=true`, `AWS_SES_FROM_ADDRESS`, `AWS_SES_TO_ADDRESSES` e `AWS_SES_REGION` (se diferente da região padrão). Reinicie o backend para que as propriedades sejam recarregadas.

> 💡 O serviço de notificação coleta métricas básicas (`notifier.ses.deliveries` e `notifier.ses.throttled`) via Micrometer. Ao integrar com Prometheus/CloudWatch você poderá acompanhar a taxa de sucesso e eventuais respostas de quota do SES.

### Executando o frontend localmente (opcional)

```sh
cd apps/frontend
cp .env.example .env
npm install
npm run dev
```

O Vite servirá o frontend em [http://localhost:5173](http://localhost:5173) e encaminhará as chamadas para a API configurada no arquivo `.env` localizado em `apps/frontend`. Ajuste o valor de `VITE_API_URL` conforme necessário (por exemplo, `http://localhost:8080` ao executar o backend fora do Docker).

### Testes e verificações

```sh
# Backend
cd apps/backend
./mvnw test

# Frontend
cd apps/frontend
npm run lint
```

## Uso Rápido

1. Acesse `http://localhost:3000/registro` para criar sua conta.
2. Faça login em `http://localhost:3000/login`.
3. Utilize o dashboard para visualizar o panorama dos chamados.
4. Abra novos chamados ou gerencie os existentes pelo menu "Chamados".
5. Administre usuários e permissões pela página "Usuários" (perfil administrador).

## Tutorial de Configuração de Credenciais

Para executar o TucasDesk na sua máquina local, é necessário configurar as credenciais do banco de dados (MariaDB), dos serviços AWS e da API. Siga este passo a passo para deixar tudo pronto.

### 1. Crie o arquivo `config/env/.env`

O primeiro passo é criar um arquivo de variáveis compartilhadas dentro da pasta `config/env/`. Você pode fazer isso copiando o arquivo de exemplo:

```sh
cp config/env/.env.example config/env/.env
```

Esse arquivo é utilizado pelo Docker Compose para gerenciar as variáveis de ambiente dos serviços de backend e banco de dados.

### 2. Credenciais do Banco de Dados (MariaDB)

Em seguida, configure as credenciais do banco MariaDB. Abra o arquivo `config/env/.env` e defina as variáveis a seguir:

- `DB_ROOT_PASSWORD`: senha do usuário root da instância MariaDB.
- `DB_NAME`: nome do banco que será utilizado pelo TucasDesk.
- `DB_USER`: usuário que a aplicação TucasDesk utilizará para se conectar.
- `DB_PASSWORD`: senha do usuário de aplicação.
- `DATABASE_URL`: URL completa de conexão com o banco. Exemplo: `mariadb://user:password@localhost:3307/tucasdesk`.
- `SPRING_DATASOURCE_URL`: URL JDBC utilizada pelo backend. Exemplo: `jdbc:mariadb://localhost:3307/tucasdesk`.
- `SPRING_DATASOURCE_USERNAME`: deve ser o mesmo valor de `DB_USER`.
- `SPRING_DATASOURCE_PASSWORD`: deve ser o mesmo valor de `DB_PASSWORD`.

### 3. Credenciais AWS (Cognito e SES)

O TucasDesk utiliza o AWS Cognito para autenticação de usuários e o AWS SES para envio de notificações por e-mail.

#### AWS Cognito

- `AWS_COGNITO_REGION`: região AWS onde seu User Pool está localizado (por exemplo, `us-east-1`).
- `AWS_COGNITO_USER_POOL_ID`: identificador do seu User Pool Cognito.
- `AWS_COGNITO_APP_CLIENT_ID`: ID do App Client configurado no Cognito.

Você também pode informar `AWS_COGNITO_ISSUER_URI` e `AWS_COGNITO_JWK_SET_URI` caso utilize URIs personalizadas.

#### AWS SES (para notificações por e-mail)

Se for utilizar notificações por e-mail, configure o AWS SES com os seguintes valores:

- `AWS_SES_ENABLED`: defina como `true` para habilitar o envio de e-mails.
- `AWS_SES_REGION`: região AWS onde o SES está configurado.
- `AWS_SES_FROM_ADDRESS`: endereço de e-mail do remetente.
- `AWS_SES_TO_ADDRESSES`: lista, separada por vírgulas, de e-mails que receberão as notificações.
- `AWS_ACCESS_KEY_ID`: chave de acesso AWS.
- `AWS_SECRET_ACCESS_KEY`: chave secreta correspondente.

**Observação:** para maior segurança, prefira utilizar roles do IAM em vez de chaves de acesso, especialmente em ambientes de produção.

### 4. URL da API no Frontend

O frontend precisa da URL da API para se comunicar corretamente com o backend.

1.  Crie o arquivo `.env` do frontend copiando o arquivo de exemplo:

    ```sh
    cp apps/frontend/.env.example apps/frontend/.env
    ```

2.  Abra `apps/frontend/.env` e configure `VITE_API_URL`.
    -   Se estiver executando o backend com Docker Compose, o valor padrão (`http://tucasdesk-backend:8080`) deve funcionar.
    -   Se estiver executando o backend localmente, altere para `http://localhost:8080`.

Com essas configurações definidas, você poderá executar a aplicação com o Docker Compose conforme descrito na seção "Como Começar".

## Como contribuir

1. Faça um fork do repositório.
2. Crie uma branch com a sua feature ou correção.
3. Envie um pull request descrevendo as mudanças.

## Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.

