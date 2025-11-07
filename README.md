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

### Subindo tudo com Docker Compose

```sh
git clone https://github.com/felipebehling/tucasdesk.git
cd tucasdesk
cp tucasdesk-frontend/.env.example tucasdesk-frontend/.env
docker compose up --build
```

Serviços disponíveis:

- Frontend: [http://localhost:3000](http://localhost:3000)
- Backend: [http://localhost:8080](http://localhost:8080)
- Banco de dados: porta `3307`, com credenciais definidas em `compose.yaml`

Finalize a execução com `docker compose down`.

### Executando o backend localmente

```sh
docker compose up -d db
cd tucasdesk-backend
./mvnw spring-boot:run
```

A API ficará disponível em [http://localhost:8080](http://localhost:8080) e utiliza o banco configurado em `src/main/resources/application.properties`.

### Executando o frontend localmente

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

