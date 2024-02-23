# Biblioteca-SpringBoot-Angular
 WebService *básica* de uma biblioteca, utilizando tecnologias como Angular, Spring Boot, Spring Security com autenticação por Token e Docker
## Tecnologias utilizadas
- Spring Boot
- Java
- Typescript
- Angular
- Angular Material
- Ionic
- Docker
- Postgresql
## Como executar
### Requisitos para executar a versão final
- Maven
- Docker Desktop
### Execução
  Após clonar o repositório, é necessário o build da aplicação Spring. Para isso, entre na pasta /library e abra-a no terminal e execute o seguinte comando: ```mvn clean install -DskiptTests```

  Com o docker em execução, vá para a pasta raiz do repositório e no terminal digite o comando: ```docker compose -d --build```. Após executar o comando, o docker irá baixar as imagens necessárias para rodar toda a aplicação.

  Com os containers inicializados, você pode acessar a aplicação através do navegador usando a URL ```http://localhost:8080```
### Versão de desenvolvedor
#### Back-end:
  Ao clonar o repositório, importe o projeto SpringBoot para a IDE desejada e tendo o Java instalado, você já pode inicia-lo.
#### Front-end:
  Para executar o front, é necessário ter o NodeJS e Angular CLI instalados. Após instalados, basta iniciar o comando ```npm i``` ou ```npm i --f```. Este é um projeto Ionic, sendo assim, é necessário executar o seguinte comando ```npm i -g @ionic/cli```. Com as pastas do node_modules instalada, inicie o projeto com ```ionic s```
### Endpoints
  Você pode acessar os endpoints da aplicação através do Swagger ```http://localhost:8080/swagger-ui/index.html```
