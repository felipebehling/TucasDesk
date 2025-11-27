# TucasDesk Backend

## Configuração de CORS

As origens permitidas para requisições ao backend são configuradas na propriedade `app.cors.allowed-origins` do arquivo [`src/main/resources/application.properties`](src/main/resources/application.properties).

```properties
app.cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

- **Ambiente de desenvolvimento:** mantenha as URLs locais utilizadas pelo frontend (por exemplo, `http://localhost:5173` para Vite e `http://localhost:3000` para React). Se desejar liberar apenas uma origem durante o desenvolvimento, remova as demais da lista.
- **Ambiente de produção:** substitua as URLs locais pelos domínios oficiais da aplicação (por exemplo, `https://app.exemplo.com`). Você pode informar múltiplas origens separando-as por vírgulas.

Após ajustar o arquivo, reinicie a aplicação para que as novas origens sejam carregadas.

## Como testar com Postman e LocalStack

Como o backend é protegido pelo Cognito (rodando no LocalStack em ambiente de desenvolvimento), é necessário um **Token JWT** válido para realizar requisições via Postman.

### Passo 1: Obter o Token via Frontend

A maneira mais simples de obter um token válido (assinado pelo LocalStack atual) é através do frontend da aplicação.

1.  **Certifique-se que a aplicação está rodando:**
    ```bash
    docker compose up
    ```
2.  **Acesse o Frontend:**
    Abra `http://localhost:5173` no seu navegador.
3.  **Faça Login:**
    Registre um usuário ou faça login.
4.  **Capture o Token:**
    *   Abra as **Ferramentas de Desenvolvedor** do navegador (F12).
    *   Vá na aba **Network** (Rede).
    *   Realize uma ação na página (ou recarregue-a).
    *   Clique em uma requisição feita para o backend.
    *   Na aba **Headers** (Cabeçalhos) da requisição, copie o valor do header `Authorization` (começa com `Bearer eyJ...`).

### Passo 2: Configurar o Postman

1.  Crie uma nova requisição no Postman apontando para o backend (ex: `http://localhost:8081/api/...`).
2.  Vá na aba **Authorization** (Autenticação).
3.  Selecione o tipo **Bearer Token**.
4.  Cole o token copiado no passo anterior.
5.  Envie a requisição.
