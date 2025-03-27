# 📌 Guia de Instalação - JDK, Angular, VS Code, PostgreSQL, Git, Spring Boot e React

## 🖥️ Distros GNOME | Arch Linux | Windows 10 & 11

Este guia fornece instruções detalhadas para instalar as ferramentas essenciais em distros Linux baseadas no GNOME, Arch Linux e Windows.

---

## 🟢 1. Instalar JDK (Java Development Kit)

### 🔹 Distros GNOME

```sh
sudo apt update && sudo apt upgrade -y && \
sudo apt install -y openjdk-21-jdk && \
java -version && javac -version
```

### 🔹 Arch Linux

```sh
sudo pacman -Syu --noconfirm && \
sudo pacman -S --noconfirm jdk21-openjdk && \
java -version && javac -version
```

### 🔹 Windows

1. Baixe e instale o OpenJDK ou Oracle JDK:
   - [OpenJDK](https://jdk.java.net/)
   - [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

2. Configure a variável de ambiente `JAVA_HOME` no Painel de Controle.
3. Verifique a instalação no Prompt de Comando:

```sh
java -version
javac -version
```

---

## 🟠 2. Instalar Angular e React

### 🔹 Distros GNOME e Arch Linux
```sh
npm install -g @angular/cli create-react-app && \
ng version && create-react-app --version
```

### 🔹 Windows

1. Baixe e instale o Node.js: [Node.js](https://nodejs.org/)
2. Instale Angular e React:
```sh
npm install -g @angular/cli create-react-app
ng version
create-react-app --version
```

---

## 🟢 3. Instalar VS Code

### 🔹 Distros GNOME
```sh
sudo apt update && sudo apt install -y wget gpg && \
wget -qO- https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor | sudo tee /usr/share/keyrings/packages.microsoft.gpg > /dev/null && \
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/packages.microsoft.gpg] https://packages.microsoft.com/repos/code stable main" | sudo tee /etc/apt/sources.list.d/vscode.list && \
sudo apt update && sudo apt install -y code
```

### 🔹 Arch Linux
```sh
sudo pacman -S --noconfirm code
```
Ou, se usar o AUR:
```sh
yay -S visual-studio-code-bin
```

### 🔹 Windows
- Baixe o instalador do VS Code: [VS Code](https://code.visualstudio.com/)

---

## 🕐 4. Instalar PostgreSQL

### 🔹 Distros GNOME
```sh
sudo apt update && sudo apt install -y postgresql postgresql-contrib && \
sudo systemctl enable postgresql && sudo systemctl start postgresql
```

### 🔹 Arch Linux
```sh
sudo pacman -S --noconfirm postgresql && \
sudo -iu postgres initdb --locale en_US.UTF-8 -D /var/lib/postgres/data && \
sudo systemctl enable postgresql && sudo systemctl start postgresql
```

### 🔹 Windows
- Baixe e instale o PostgreSQL: [PostgreSQL](https://www.postgresql.org/download/)

---

## 🔴 5. Instalar Git

### 🔹 Distros GNOME e Arch Linux
```sh
sudo apt install -y git || sudo pacman -S --noconfirm git && \
git --version && \
git config --global user.name "Seu Nome" && \
git config --global user.email "seuemail@example.com"
```

### 🔹 Windows
- Baixe o instalador do Git: [Git para Windows](https://git-scm.com/download/win)
- Configure no Prompt de Comando ou Git Bash:
```sh
git --version
git config --global user.name "Seu Nome"
git config --global user.email "seuemail@example.com"
```

---

## 🔷 6. Instalar Spring Boot

### 🔹 Distros GNOME e Arch Linux
```sh
curl -s "https://get.sdkman.io" | bash && \
source "$HOME/.sdkman/bin/sdkman-init.sh" && \
sdk install springboot && \
spring --version
```

### 🔹 Windows
- Baixe diretamente do [Spring Initializr](https://start.spring.io/).

---

## 🛠️ Comandos Únicos para Instalação Completa

### 🔹 GNOME
```sh
sudo apt update && sudo apt upgrade -y && \
sudo apt install -y openjdk-21-jdk nodejs npm wget gpg code postgresql postgresql-contrib git && \
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install springboot && \
npm install -g @angular/cli create-react-app && \
sudo systemctl enable postgresql && sudo systemctl start postgresql && \
git config --global user.name "Seu Nome" && git config --global user.email "seuemail@example.com"
```

### 🔹 Arch Linux
```sh
sudo pacman -Syu --noconfirm && \
sudo pacman -S --noconfirm jdk21-openjdk nodejs npm code postgresql git && \
sudo -iu postgres initdb --locale en_US.UTF-8 -D /var/lib/postgres/data && \
sudo systemctl enable postgresql && sudo systemctl start postgresql && \
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install springboot && \
npm install -g @angular/cli create-react-app && \
git config --global user.name "Seu Nome" && git config --global user.email "seuemail@example.com"
```

Agora você pode rodar seu projeto em qualquer sistema operacional! 🚀
