# Teste Courart Back-end

Aplicação front-end que utiliza essa api https://teste-courart-trs.herokuapp.com

## Spring Boot Application
- Use o arquivo "scrip.sql" para criar o banco de dados
- O nome de usuário e senha do banco de dados deve ser alterado em: `/src/main/resources/application.properties`
  - spring.datasource.username="nome_de_usuario"
  - spring.datasource.password="senha"
  
- É preciso fornecer permissão de origem das chamadas.
  - Para isso vá em: `/src/main/java/com/courart/backend/api/cors/CorsFilter.java` e altere o atributo origemPermitida.
  - Exemplos:
    - private String origemPermitida = "https://teste-courart-trs.herokuapp.com";
    - private String origemPermitida = "http://localhost:4200";
    
- Baixe as dependencias utilizadas pela aplicação utilizando o plug-in Maven.
