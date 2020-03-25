CREATE TABLE motoristas(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	cpf VARCHAR(14) NOT NULL,
    nome VARCHAR(40) NOT NULL,
    data_nascimento DATE,
    login VARCHAR(12) NOT NULL,
    senha VARCHAR(12) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO motoristas(cpf, nome, data_nascimento, login, senha) VALUES ("11111111111","Umhum","1939-01-01","um","um");
INSERT INTO motoristas(cpf, nome, data_nascimento, login, senha) VALUES ("22222222222","Dois Hum","1939-02-02","dois","dois");
INSERT INTO motoristas(cpf, nome, data_nascimento, login, senha) VALUES ("33333333333","Rodrigo","1989-03-03","rodrigo","rodrigo");
INSERT INTO motoristas(cpf, nome, data_nascimento, login, senha) VALUES ("44444444444","Tarlles","1997-04-04","tarlles","tarlles");
INSERT INTO motoristas(cpf, nome, data_nascimento, login, senha) VALUES ("55555555555","Leonardo","1979-05-05","leonardo","leonardo");