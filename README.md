# 📌 Guia de Instalação - JDK, Angular, VS Code, PostgreSQL, Git, Spring Boot e React

## 🖥️ Distros GNOME | Arch Linux | Windows 10 & 11

Este guia fornece instruções detalhadas para instalar as ferramentas essenciais em distros Linux baseadas no GNOME, Arch Linux e Windows.

---

## 🟢 1. Instalar JDK (Java Development Kit)

### 🔹 Distros GNOME
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

### 🔹 Distros GNOME e Arch Linux
```sh
sudo apt install -y nodejs npm  # Para distros GNOME
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

## 🟠 3. Instalar React

### 🔹 Distros GNOME e Arch Linux
```sh
npm install -g create-react-app
create-react-app --version
```

### 🔹 Windows
```sh
npm install -g create-react-app
create-react-app --version
```

---

## 🟠 4. Instalar VS Code

### 🔹 Distros GNOME
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

## 🕖 5. Instalar PostgreSQL

### 🔹 Distros GNOME
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

## 🔴 6. Instalar e Configurar Git

### 🔹 Distros GNOME e Arch Linux
```sh
sudo apt install git -y   # Distros GNOME
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

## 🔷 7. Instalar Spring Boot

### 🔹 Distros GNOME e Arch Linux
```sh
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install springboot
spring --version
```

### 🔹 Windows
Baixe diretamente do [Spring Initializr](https://start.spring.io/).

---

Agora você pode rodar seu projeto em qualquer sistema operacional! 🚀

### Caso queira baixar tudo de uma vez
## GNOME
```
sudo apt update && sudo apt upgrade -y && \
sudo apt install -y openjdk-17-jdk nodejs npm wget gpg code postgresql postgresql-contrib git && \
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install springboot && \
npm install -g @angular/cli create-react-app && \
sudo systemctl enable postgresql && sudo systemctl start postgresql && \
git config --global user.name "Seu Nome" && git config --global user.email "seuemail@example.com"
```

## ARCH
```
sudo pacman -Syu --noconfirm && \
sudo pacman -S --noconfirm jdk17-openjdk nodejs npm code postgresql git && \
sudo -iu postgres initdb --locale en_US.UTF-8 -D /var/lib/postgres/data && \
sudo systemctl enable postgresql && sudo systemctl start postgresql && \
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install springboot && \
npm install -g @angular/cli create-react-app && \
git config --global user.name "Seu Nome" && git config --global user.email "seuemail@example.com"
```
