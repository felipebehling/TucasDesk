-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS tucasdesk
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar o banco
USE tucasdesk;

-- ==============================
-- 1. Tabela perfis
-- ==============================
CREATE TABLE perfis (
    id_perfil INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================
-- 2. Tabela usuarios
-- ==============================
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil_id INT NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_usuario_perfil FOREIGN KEY (perfil_id) REFERENCES perfis(id_perfil)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================
-- 3. Tabela categorias
-- ==============================
CREATE TABLE categorias (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================
-- 4. Tabela prioridade
-- ==============================
CREATE TABLE prioridade (
    id_prioridade INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================
-- 5. Tabela status
-- ==============================
CREATE TABLE status (
    id_status INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================
-- 6. Tabela chamados
-- ==============================
CREATE TABLE chamados (
    id_chamado INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    categoria_id INT NOT NULL,
    status_id INT NOT NULL,
    prioridade INT NOT NULL,
    usuario_id INT NOT NULL,
    tecnico_id INT,
    data_abertura DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_fechamento DATETIME,
    CONSTRAINT fk_chamado_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_status FOREIGN KEY (status_id) REFERENCES status(id_status)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_prioridade FOREIGN KEY (prioridade) REFERENCES prioridade(id_prioridade)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_tecnico FOREIGN KEY (tecnico_id) REFERENCES usuarios(id_usuario)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================
-- 7. Tabela interacoes
-- ==============================
CREATE TABLE interacoes (
    id_interacao INT PRIMARY KEY AUTO_INCREMENT,
    chamado_id INT NOT NULL,
    usuario_id INT NOT NULL,
    mensagem TEXT NOT NULL,
    anexo_url VARCHAR(255),
    data_interacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_interacao_chamado FOREIGN KEY (chamado_id) REFERENCES chamados(id_chamado)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_interacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;







-- =====================================
-- INSERÇÃO DE DADOS
-- =====================================
USE tucasdesk;

-- =====================================
-- 2. Inserir perfis
-- =====================================
INSERT INTO perfis (nome) VALUES
('Administrador'),
('Técnico'),
('Usuário');

-- =====================================
-- 3. Inserir usuários
-- (Senha fictícia: será "123456" - depois aplicaremos BCrypt no sistema)
-- (Senha fictícia: "123456" - As senhas são hasheadas com BCrypt)
-- =====================================
INSERT INTO usuarios (nome, email, senha, perfil_id, ativo)
VALUES
('Felipe Behling', 'felipe@tucasdesk.com', '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 1, TRUE), -- Admin
('Ana Souza', 'ana@tucasdesk.com', '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 2, TRUE),        -- Técnica
('João Silva', 'joao@tucasdesk.com', '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 2, TRUE),      -- Técnico
('Maria Oliveira', 'maria@tucasdesk.com', '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 3, TRUE), -- Usuária
('Pedro Santos', 'pedro@tucasdesk.com', '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 3, TRUE);   -- Usuário

-- =====================================
-- 4. Inserir categorias de chamados
-- =====================================
INSERT INTO categorias (nome) VALUES
('Infraestrutura'),
('Sistema'),
('Rede'),
('Software'),
('Hardware');

-- =====================================
-- 5. Inserir níveis de prioridade
-- =====================================
INSERT INTO prioridade (nome) VALUES
('Baixa'),
('Média'),
('Alta'),
('Crítica');

-- =====================================
-- 6. Inserir status de chamados
-- =====================================
INSERT INTO status (nome) VALUES
('Aberto'),
('Em Andamento'),
('Pendente'),
('Fechado');

-- =====================================
-- 7. Inserir chamados
-- =====================================
INSERT INTO chamados
(titulo, descricao, categoria_id, status_id, prioridade, usuario_id, tecnico_id, data_abertura)
VALUES
('Erro no sistema de login', 'Não consigo acessar minha conta, aparece erro 500.', 2, 1, 3, 4, 2, NOW()),
('Internet lenta no setor financeiro', 'A conexão está muito instável e os relatórios não carregam.', 3, 2, 4, 5, 3, NOW()),
('Instalação de software', 'Preciso instalar o VSCode na máquina do RH.', 4, 1, 2, 4, 2, NOW()),
('Troca de monitor', 'Monitor queimou e preciso de substituição.', 5, 3, 2, 5, NULL, NOW());

-- =====================================
-- 8. Inserir interações nos chamados
-- =====================================
INSERT INTO interacoes (chamado_id, usuario_id, mensagem, anexo_url)
VALUES
(1, 2, 'Verifiquei os logs e o problema está no servidor de autenticação.', NULL),
(1, 4, 'Obrigado pelo retorno, aguardo solução.', NULL),
(2, 3, 'Já reiniciei o roteador e configurei novo DNS.', 'http://tucasdesk.com/anexos/dns_config.png'),
(2, 5, 'A internet melhorou um pouco, mas ainda oscila.', NULL),
(3, 2, 'Software instalado com sucesso.', NULL);