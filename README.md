# projetox
Projeto de aprendizado em conjunto


# 📌 Guia de Instalação - JDK, Angular, VS Code, PostgreSQL, Git e Spring Boot no Linux Mint 22.1

Este guia fornece instruções detalhadas para instalar as ferramentas essenciais no **Linux Mint 22.1**.

---

## 🟢 1. Instalar JDK (Java Development Kit)

### 🔹 Atualizar pacotes do sistema

```bash
sudo apt update && sudo apt upgrade -y
```

### 🔹 Instalar OpenJDK

```bash
sudo apt install openjdk-17-jdk -y  # Ou substitua por openjdk-21-jdk se desejar a versão mais recente
```

### 🔹 Verificar instalação

```bash
java -version
javac -version
```

Se precisar do **Oracle JDK**, baixe-o do site oficial: [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html).

---

## 🔵 2. Instalar Angular

### 🔹 Instalar Node.js e npm

```bash
sudo apt install -y nodejs npm
```

Verifique a instalação:

```bash
node -v
npm -v
```

### 🔹 Instalar Angular CLI

```bash
npm install -g @angular/cli
```

Verifique:

```bash
ng version
```

### 🔹 Criar e rodar um projeto Angular

```bash
ng new meu-projeto
cd meu-projeto
ng serve
```

Acesse [http://localhost:4200](http://localhost:4200) no navegador.

---

## 🟠 3. Instalar VS Code

### 🔹 Instalar via repositório oficial (recomendado)

```bash
sudo apt update
sudo apt install wget gpg -y
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor | sudo tee /usr/share/keyrings/packages.microsoft.gpg > /dev/null
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/packages.microsoft.gpg] https://packages.microsoft.com/repos/code stable main" | sudo tee /etc/apt/sources.list.d/vscode.list
sudo apt update
sudo apt install code -y
```

### 🔹 Alternativa: Instalar via Snap

```bash
sudo snap install code --classic
```

### 🔹 Verificar instalação

```bash
code --version
```

Para abrir o VS Code:

```bash
code
```

---

## 🕖 4. Instalar PostgreSQL

### 🔹 Instalar PostgreSQL

```bash
sudo apt update
sudo apt install postgresql postgresql-contrib -y
```

### 🔹 Verificar status do serviço

```bash
sudo systemctl status postgresql
```

Se não estiver rodando, inicie:

```bash
sudo systemctl start postgresql
```

### 🔹 Criar um banco de dados e usuário

```bash
sudo -i -u postgres
psql
```

Dentro do `psql`, rode:

```sql
CREATE DATABASE meu_banco;
CREATE USER meu_usuario WITH ENCRYPTED PASSWORD 'minha_senha';
GRANT ALL PRIVILEGES ON DATABASE meu_banco TO meu_usuario;
\q
```

Agora, você pode conectar-se ao PostgreSQL usando `psql`, DBeaver ou pgAdmin!

---

## 🔴 5. Instalar e Configurar Git

### 🔹 Instalar Git

```bash
sudo apt install git -y
```

### 🔹 Verificar instalação

```bash
git --version
```

### 🔹 Configurar nome e e-mail

```bash
git config --global user.name "Seu Nome"
git config --global user.email "seuemail@example.com"
```

### 🔹 Criar um repositório Git no projeto

```bash
cd meu-projeto
git init
git add .
git commit -m "Primeiro commit"
```

### 🔹 Conectar ao GitHub (ou GitLab, Bitbucket)

Crie um repositório no GitHub e copie a URL. Depois, rode:

```bash
git remote add origin https://github.com/seuusuario/meu-projeto.git
git branch -M main
git push -u origin main
```

Agora seu projeto está versionado e pronto para colaboração!

---

## 🟣 6. Instalar Spring Boot

### 🔹 Instalar Spring Boot CLI

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install springboot
```

### 🔹 Verificar instalação

```bash
spring --version
```

### 🔹 Criar um novo projeto Spring Boot

```bash
spring init --dependencies=web,data-jpa,h2,lombok meu-projeto-spring
cd meu-projeto-spring
```

### 🔹 Rodar o projeto Spring Boot

```bash
./mvnw spring-boot:run
```

O servidor iniciará em `http://localhost:8080`.

---

Agora sua máquina está totalmente preparada para desenvolvimento! 🚀

