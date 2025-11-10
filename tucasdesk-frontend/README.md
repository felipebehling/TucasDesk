# TucasDesk Frontend

## Variáveis de ambiente

O processo de build utiliza o script `npm run build`, que carrega `VITE_API_URL` a partir de:

1. Variáveis de ambiente fornecidas na execução do comando.
2. Um arquivo `.env` local (se existir).
3. Um valor padrão (`http://tucasdesk-backend:8080`) quando nenhuma das opções anteriores estiver disponível.

## Docker

Ao construir a imagem Docker, defina `VITE_API_URL` como `ARG`/`ENV` para ajustar o backend consumido pelo frontend, por exemplo:

```bash
docker build \
  --build-arg VITE_API_URL="https://api.exemplo.com" \
  -t tucasdesk-frontend .
```

Se precisar parametrizar a URL em tempo de execução (e não apenas no build), utilize um entrypoint que gere o arquivo esperado pelo Nginx via `envsubst` (ou ferramenta similar) antes de iniciar o servidor.
