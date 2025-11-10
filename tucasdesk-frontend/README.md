# TucasDesk Frontend

## Variáveis de ambiente

O processo de build utiliza o script `npm run build`, que carrega `VITE_API_URL` a partir de:

1. Variáveis de ambiente fornecidas na execução do comando.
2. Um arquivo `.env` local (se existir).
3. Um valor padrão (`http://tucasdesk-backend:8080`) quando nenhuma das opções anteriores estiver disponível.

### Qual é a função do `.env`?

O arquivo `.env`, quando presente na raiz do projeto, permite definir `VITE_API_URL` localmente durante o desenvolvimento sem precisar exportar a variável manualmente a cada execução. O script `scripts/build.mjs` lê esse arquivo apenas para recuperar o valor da chave `VITE_API_URL` e encaminhá-lo para o processo de build.

### E se o `.env` não for criado?

Nada falha: o build ignora a ausência do arquivo e usa a variável de ambiente fornecida externamente ou o valor padrão `http://tucasdesk-backend:8080`. Isso significa que:

- Em ambientes como CI/CD ou Docker, basta informar `VITE_API_URL` via `ARG`/`ENV` para personalizar a URL do backend.
- Em ambientes locais, você pode executar `VITE_API_URL=https://sua-api npm run build` sem manter um arquivo `.env`.

Crie o `.env` apenas se quiser guardar uma configuração padrão para os builds locais; caso contrário, ele é totalmente opcional.

## Docker

Ao construir a imagem Docker, defina `VITE_API_URL` como `ARG`/`ENV` para ajustar o backend consumido pelo frontend, por exemplo:

```bash
docker build \
  --build-arg VITE_API_URL="https://api.exemplo.com" \
  -t tucasdesk-frontend .
```

Se precisar parametrizar a URL em tempo de execução (e não apenas no build), utilize um entrypoint que gere o arquivo esperado pelo Nginx via `envsubst` (ou ferramenta similar) antes de iniciar o servidor.
