# 📌 Guia de Instalação - JDK, Angular, VS Code, PostgreSQL, Git e Spring Boot

## 🖥️ Ubuntu 22.1 | Arch Linux | Windows

Este guia fornece instruções detalhadas para instalar as ferramentas essenciais no Ubuntu 22.1, Arch Linux e Windows.

---

## 🟢 1. Instalar JDK (Java Development Kit)

### 🔹 Ubuntu
```sh
sudo apt update && sudo apt upgrade -y
sudo apt install openjdk-17-jdk -y  # Ou substitua por openjdk-21-jdk se desejar a versão mais recente
java -version
javac -version
```

### 🔹 Arch Linux
```sh
sudo pacman -Syu
sudo pacman -S jdk17-openjdk  # Ou jdk21-openjdk para a versão mais recente
java -version
javac -version
```

### 🔹 Windows
1. Baixe o instalador do OpenJDK ou Oracle JDK:
   - [OpenJDK](https://jdk.java.net/)
   - [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

2. Instale e configure a variável de ambiente `JAVA_HOME` no Painel de Controle → Sistema → Configurações Avançadas → Variáveis de Ambiente.

3. Verifique a instalação no Prompt de Comando:
```sh
java -version
javac -version
```

---

## 🔵 2. Instalar Angular

### 🔹 Ubuntu e Arch Linux
```sh
sudo apt install -y nodejs npm  # Para Ubuntu
sudo pacman -S nodejs npm       # Para Arch Linux
node -v
npm -v
npm install -g @angular/cli
ng version
```

### 🔹 Windows
1. Baixe e instale o Node.js: [Node.js](https://nodejs.org/)

2. Após a instalação, abra o Prompt de Comando e execute:
```sh
node -v
npm -v
npm install -g @angular/cli
ng version
```

---

## 🟠 3. Instalar VS Code

### 🔹 Ubuntu
```sh
sudo apt update
sudo apt install wget gpg -y
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor | sudo tee /usr/share/keyrings/packages.microsoft.gpg > /dev/null
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/packages.microsoft.gpg] https://packages.microsoft.com/repos/code stable main" | sudo tee /etc/apt/sources.list.d/vscode.list
sudo apt update
sudo apt install code -y
```

### 🔹 Arch Linux
```sh
sudo pacman -S code
```
Ou, se usar o AUR:
```sh
yay -S visual-studio-code-bin
```

### 🔹 Windows
1. Baixe o instalador do VS Code: [VS Code](https://code.visualstudio.com/)

2. Instale normalmente e abra com:
```sh
code
```

---

## 🕖 4. Instalar PostgreSQL

### 🔹 Ubuntu
```sh
sudo apt update
sudo apt install postgresql postgresql-contrib -y
sudo systemctl enable postgresql
sudo systemctl start postgresql
```

### 🔹 Arch Linux
```sh
sudo pacman -S postgresql
sudo -iu postgres initdb --locale en_US.UTF-8 -D /var/lib/postgres/data
sudo systemctl enable postgresql
sudo systemctl start postgresql
```

### 🔹 Windows
1. Baixe o instalador do PostgreSQL: [PostgreSQL](https://www.postgresql.org/download/)

2. Instale com o pgAdmin ou configure manualmente via psql.

---

## 🔴 5. Instalar e Configurar Git

### 🔹 Ubuntu e Arch Linux
```sh
sudo apt install git -y   # Ubuntu
sudo pacman -S git        # Arch Linux
git --version
git config --global user.name "Seu Nome"
git config --global user.email "seuemail@example.com"
```

### 🔹 Windows
1. Baixe e instale o Git: [Git para Windows](https://git-scm.com/download/win)

2. Configure no Git Bash ou Prompt de Comando:
```sh
git --version
git config --global user.name "Seu Nome"
git config --global user.email "seuemail@example.com"
```

---

## 🟣 6. Instalar Spring Boot

### 🔹 Ubuntu e Arch Linux
```sh
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install springboot
spring --version
```

### 🔹 Windows
1. Instale o SDKMAN para Windows usando o Scoop:
```sh
iwr -useb get.scoop.sh | iex
scoop install springboot
spring --version
```

Ou baixe diretamente do [Spring Initializr](https://start.spring.io/).

---

Agora você pode rodar o projetoX em qualquer sistema operacional! 🚀

