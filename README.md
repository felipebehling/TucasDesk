Perfeito! Vou atualizar seu guia para:

* Remover **PostgreSQL**
* Adicionar **MySQL/MariaDB**
* Adicionar **GitHub Desktop**

Segue a versão revisada:

---

# 📌 Guia de Instalação - JDK, Angular, VS Code, Git, GitHub Desktop, Spring Boot, MySQL/MariaDB e React

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

   * [OpenJDK](https://jdk.java.net/)
   * [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
2. Configure a variável de ambiente `JAVA_HOME`.
3. Verifique no Prompt de Comando:

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

* Baixe o instalador do VS Code: [VS Code](https://code.visualstudio.com/)

---

## 🟣 4. Instalar Git

### 🔹 Distros GNOME e Arch Linux

```sh
sudo apt install -y git || sudo pacman -S --noconfirm git && \
git --version && \
git config --global user.name "Seu Nome" && \
git config --global user.email "seuemail@example.com"
```

### 🔹 Windows

* Baixe o instalador do Git: [Git para Windows](https://git-scm.com/download/win)
* Configure no Prompt de Comando ou Git Bash:

```sh
git --version
git config --global user.name "Seu Nome"
git config --global user.email "seuemail@example.com"
```

---

## 🔷 5. Instalar GitHub Desktop

### 🔹 Windows

* Baixe e instale: [GitHub Desktop](https://desktop.github.com/)

### 🔹 Linux (Ubuntu / Arch via AUR)

```sh
# Ubuntu / GNOME
sudo apt install -y github-desktop
# Arch via AUR
yay -S github-desktop-bin
```

---

## 🔵 6. Instalar MySQL ou MariaDB

### 🔹 MySQL (Community Server)

#### GNOME / Ubuntu

```sh
sudo apt update && sudo apt install -y wget && \
wget https://dev.mysql.com/get/mysql-apt-config_0.8.33-1_all.deb && \
sudo dpkg -i mysql-apt-config_0.8.33-1_all.deb && \
sudo apt update && sudo apt install -y mysql-server && \
sudo systemctl enable mysql && sudo systemctl start mysql
```

#### Arch Linux

```sh
sudo pacman -S --noconfirm mysql && \
sudo systemctl enable mysqld && sudo systemctl start mysqld
```

#### Windows

* Baixe e instale: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
* Use o **MySQL Installer** para configurar o servidor e o Workbench.

---

### 🔹 MariaDB

#### GNOME / Ubuntu

```sh
sudo apt update && sudo apt install -y mariadb-server mariadb-client && \
sudo systemctl enable mariadb && sudo systemctl start mariadb
```

#### Arch Linux

```sh
sudo pacman -S --noconfirm mariadb && \
sudo systemctl enable mariadb && sudo systemctl start mariadb
```

#### Windows

* Baixe e instale: [MariaDB](https://mariadb.org/download/)

---

## 🔷 7. Instalar Spring Boot

### 🔹 Distros GNOME e Arch Linux

```sh
curl -s "https://get.sdkman.io" | bash && \
source "$HOME/.sdkman/bin/sdkman-init.sh" && \
sdk install springboot && \
spring --version
```

### 🔹 Windows

* Use o [Spring Initializr](https://start.spring.io/) para gerar projetos.

---

## 🛠️ 8. Comandos Únicos para Instalação Completa

### 🔹 GNOME

```sh
sudo apt update && sudo apt upgrade -y && \
sudo apt install -y openjdk-21-jdk nodejs npm wget gpg code git mariadb-server mariadb-client && \
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install springboot && \
npm install -g @angular/cli create-react-app && \
sudo systemctl enable mariadb && sudo systemctl start mariadb && \
git config --global user.name "Seu Nome" && git config --global user.email "seuemail@example.com"
```

### 🔹 Arch Linux

```sh
sudo pacman -Syu --noconfirm && \
sudo pacman -S --noconfirm jdk21-openjdk nodejs npm code git mariadb && \
sudo systemctl enable mariadb && sudo systemctl start mariadb && \
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install springboot && \
npm install -g @angular/cli create-react-app && \
git config --global user.name "Seu Nome" && git config --global user.email "seuemail@example.com"
```

