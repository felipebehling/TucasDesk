# Etapa 1: build do frontend
FROM node:22-bullseye AS build
WORKDIR /app

ARG VITE_API_URL
ENV VITE_API_URL=${VITE_API_URL}

COPY package*.json ./
RUN npm ci --legacy-peer-deps
COPY . .
RUN if [ -z "${VITE_API_URL}" ] && [ -f .env ]; then \
      set -a; \
      . ./.env; \
      set +a; \
    fi; \
    export VITE_API_URL="${VITE_API_URL:-http://tucasdesk-backend:8080}"; \
    npm run build

# Etapa 2: produção com Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
