# TucasDesk Backend

## Configuração de CORS

As origens permitidas para requisições ao backend são configuradas na propriedade `app.cors.allowed-origins` do arquivo [`src/main/resources/application.properties`](src/main/resources/application.properties).

```properties
app.cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

- **Ambiente de desenvolvimento:** mantenha as URLs locais utilizadas pelo frontend (por exemplo, `http://localhost:5173` para Vite e `http://localhost:3000` para React). Se desejar liberar apenas uma origem durante o desenvolvimento, remova as demais da lista.
- **Ambiente de produção:** substitua as URLs locais pelos domínios oficiais da aplicação (por exemplo, `https://app.exemplo.com`). Você pode informar múltiplas origens separando-as por vírgulas.

Após ajustar o arquivo, reinicie a aplicação para que as novas origens sejam carregadas.
