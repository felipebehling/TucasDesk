-- MariaDB 10.6+ (compatível)
-- Acesse o MariaDB como root
-- mysql -u root -p

-- Criação do usuário (altere 'novo_root' e 'senha_segura' conforme necessário)
CREATE USER 'tucasdesk'@'%' IDENTIFIED BY 'tucasdesk123';

-- Concede todas as permissões em todos os bancos
GRANT ALL PRIVILEGES ON *.* TO 'tucasdesk'@'%' WITH GRANT OPTION;

-- Aplica as alterações
FLUSH PRIVILEGES;



-- Cria o banco com charset/collation recomendados
CREATE DATABASE IF NOT EXISTS tucasdesk
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Usar o banco
USE tucasdesk;

-- ==============================
-- 1. Tabela perfis
-- ==============================
CREATE TABLE IF NOT EXISTS perfis (
    id_perfil INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

-- ==============================
-- 2. Tabela usuarios
-- ==============================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil_id INT NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    ativo TINYINT(1) DEFAULT 1,
    CONSTRAINT fk_usuario_perfil
      FOREIGN KEY (perfil_id) REFERENCES perfis(id_perfil)
      ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

-- Índices úteis (FKs performáticas)
CREATE INDEX idx_usuarios_perfil_id ON usuarios (perfil_id);

-- ==============================
-- 3. Tabela categorias
-- ==============================
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

-- ==============================
-- 4. Tabela prioridade
-- ==============================
CREATE TABLE IF NOT EXISTS prioridade (
    id_prioridade INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(20) NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

-- ==============================
-- 5. Tabela status
-- ==============================
CREATE TABLE IF NOT EXISTS `status` (
    id_status INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

-- ==============================
-- 6. Tabela chamados
-- ==============================
CREATE TABLE IF NOT EXISTS chamados (
    id_chamado INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    categoria_id INT NOT NULL,
    status_id INT NOT NULL,
    prioridade INT NOT NULL,
    usuario_id INT NOT NULL,
    tecnico_id INT NULL,
    data_abertura DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_fechamento DATETIME NULL,
    CONSTRAINT fk_chamado_categoria
      FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria)
      ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_status
      FOREIGN KEY (status_id) REFERENCES `status`(id_status)
      ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_prioridade
      FOREIGN KEY (prioridade) REFERENCES prioridade(id_prioridade)
      ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_usuario
      FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
      ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chamado_tecnico
      FOREIGN KEY (tecnico_id) REFERENCES usuarios(id_usuario)
      ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

CREATE INDEX idx_chamados_categoria_id ON chamados (categoria_id);
CREATE INDEX idx_chamados_status_id    ON chamados (status_id);
CREATE INDEX idx_chamados_prioridade   ON chamados (prioridade);
CREATE INDEX idx_chamados_usuario_id   ON chamados (usuario_id);
CREATE INDEX idx_chamados_tecnico_id   ON chamados (tecnico_id);

-- ==============================
-- 7. Tabela interacoes
-- ==============================
CREATE TABLE IF NOT EXISTS interacoes (
    id_interacao INT PRIMARY KEY AUTO_INCREMENT,
    chamado_id INT NOT NULL,
    usuario_id INT NOT NULL,
    mensagem TEXT NOT NULL,
    anexo_url VARCHAR(255) NULL,
    data_interacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_interacao_chamado
      FOREIGN KEY (chamado_id) REFERENCES chamados(id_chamado)
      ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_interacao_usuario
      FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
      ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  ROW_FORMAT=DYNAMIC;

CREATE INDEX idx_interacoes_chamado_id ON interacoes (chamado_id);
CREATE INDEX idx_interacoes_usuario_id ON interacoes (usuario_id);

-- =====================================
-- INSERÇÃO DE DADOS
-- =====================================
USE tucasdesk;

-- 1. perfis
INSERT INTO perfis (nome) VALUES
('Administrador'),
('Técnico'),
('Usuário');

-- 2. usuarios (senhas já com BCrypt)
INSERT INTO usuarios (nome, email, senha, perfil_id, ativo) VALUES
('Felipe Behling', 'felipe@tucasdesk.com', '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 1, 1),
('Ana Souza',     'ana@tucasdesk.com',    '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 2, 1),
('João Silva',    'joao@tucasdesk.com',   '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 2, 1),
('Maria Oliveira','maria@tucasdesk.com',  '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 3, 1),
('Pedro Santos',  'pedro@tucasdesk.com',  '$2a$10$FviiXMUYGzrl.Lt0c0uXPe8nsvcmfCHrfi88JyxVlNzzURmqDRgVG', 3, 1);

-- 3. categorias
INSERT INTO categorias (nome) VALUES
('Infraestrutura'),
('Sistema'),
('Rede'),
('Software'),
('Hardware');

-- 4. prioridade
INSERT INTO prioridade (nome) VALUES
('Baixa'),
('Média'),
('Alta'),
('Crítica');

-- 5. status
INSERT INTO `status` (nome) VALUES
('Aberto'),
('Em Andamento'),
('Pendente'),
('Fechado');

-- 6. chamados
INSERT INTO chamados
(titulo, descricao, categoria_id, status_id, prioridade, usuario_id, tecnico_id, data_abertura) VALUES
('Erro no sistema de login', 'Não consigo acessar minha conta, aparece erro 500.', 2, 1, 3, 4, 2, NOW()),
('Internet lenta no setor financeiro', 'A conexão está muito instável e os relatórios não carregam.', 3, 2, 4, 5, 3, NOW()),
('Instalação de software', 'Preciso instalar o VSCode na máquina do RH.', 4, 1, 2, 4, 2, NOW()),
('Troca de monitor', 'Monitor queimou e preciso de substituição.', 5, 3, 2, 5, NULL, NOW());

-- 7. interacoes
INSERT INTO interacoes (chamado_id, usuario_id, mensagem, anexo_url) VALUES
(1, 2, 'Verifiquei os logs e o problema está no servidor de autenticação.', NULL),
(1, 4, 'Obrigado pelo retorno, aguardo solução.', NULL),
(2, 3, 'Já reiniciei o roteador e configurei novo DNS.', 'http://tucasdesk.com/anexos/dns_config.png'),
(2, 5, 'A internet melhorou um pouco, mas ainda oscila.', NULL),
(3, 2, 'Software instalado com sucesso.', NULL);
