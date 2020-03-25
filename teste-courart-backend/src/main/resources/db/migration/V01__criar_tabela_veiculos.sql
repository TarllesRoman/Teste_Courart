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

INSERT INTO veiculos(placa,ano_fabricacao,ano_modelo,chassi,data_cadastro,modelo,cor,consumo_medio) VALUES ("HBJ 1052","1997","1996","ASD1232ASDASD46578","2019-9-8","Celta","Branco",1.3);
INSERT INTO veiculos(placa,ano_fabricacao,ano_modelo,chassi,data_cadastro,modelo,cor,consumo_medio) VALUES ("JBH 1121","1998","1995","ASASSAAWEEEEASD465","2019-9-8","Celta","Preto",1.3);
INSERT INTO veiculos(placa,ano_fabricacao,ano_modelo,chassi,data_cadastro,modelo,cor,consumo_medio) VALUES ("HGG 1155","2003","2000","ACCVFE332ASDASD4OO","2019-9-8","Eco Sport","Prata",1.3);