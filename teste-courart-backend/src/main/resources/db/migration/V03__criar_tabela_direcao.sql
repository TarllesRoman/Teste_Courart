CREATE TABLE direcao(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	cpf VARCHAR(14) NOT NULL,
    placa VARCHAR(10) NOT NULL,
    inicio_direcao DATE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO direcao(cpf, placa, inicio_direcao) VALUES ("33333333333", "HGG 1155", "2019-01-01");
INSERT INTO direcao(cpf, placa, inicio_direcao) VALUES ("22222222222", "JBH 1121", "2019-02-02");
INSERT INTO direcao(cpf, placa, inicio_direcao) VALUES ("55555555555", "HBJ 1052", "2019-03-03");