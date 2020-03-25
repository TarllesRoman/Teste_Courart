CREATE DATABASE courartapi;

CREATE TABLE veiculos (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(10) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT True,
    ano_fabricacao VARCHAR(4) NOT NULL,
    ano_modelo VARCHAR(4) NOT NULL,
    chassi VARCHAR(40) NOT NULL,
    data_cadastro DATE,
    data_desativacao DATE,
    modelo VARCHAR(30) NOT NULL,
    cor VARCHAR(20),
    consumo_medio DECIMAL(10,2) NOT NULL,
    qtd_passageiros INTEGER DEFAULT 4 NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 CREATE TABLE motoristas(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	cpf VARCHAR(14) NOT NULL,
    nome VARCHAR(40) NOT NULL,
    data_nascimento DATE,
    login VARCHAR(12) NOT NULL,
    senha VARCHAR(12) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE direcao(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	cpf VARCHAR(14) NOT NULL,
    placa VARCHAR(10) NOT NULL,
    inicio_direcao DATE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;