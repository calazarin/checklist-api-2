## Checklist API with SpringBoot 3 and Java 21

Versão **atualizada** da [API](https://github.com/calazarin/checklist-api) criada para prover recursos para a [SPA de Checklist](https://github.com/calazarin/checklist-spa).
Essa nova versão utiliza **SpringBoot 3, Java 21 e Gradle.**

## Principais alterações

- No SpringBoot 3 todas as classes do pacote `javax.*` foram movidos para o pacote Jakarta EE `jakarta.*`.
  - Portanto, para que as entidates funcionassem corretamente, os pacotes `javax.persistence.*` foram substituídos por `jakarta.persistence.*`

- A dependência relacionada à Open API foi atualizada;
- As interfaces do pacote `com.learning.springboot.checklistapi.repository` foram alteradas para extender a interface `org.springframework.data.jpa.repository.JpaRepository`
- O Maven foi substitúido por Gradle.

## Rodando a aplicação local

Utilizando uma janela do Prompt Comand (ou terminal de um sistema Unix), execute os seguintes comandos na raiz do projeto:

1. Construa a aplicação com o seguinte comando: `.\gradlew build -PbuildProfile=local`
2. Para rodar a aplicação digite o seguinte comando: `.\gradlew bootRun -Dspring.profiles.active=local`
3. Caso queira popular o banco de dados uma lista de categorias pré-existentes, adicione o profile `data-load`

### OpenAPI

Após iniciar a aplicação localmente, é possível acessar a especificação da API em `http://localhost:8080/v3/api-docs`

### Swagger UI

Após iniciar a aplicação localmente, é possível acessar a especificação da API em `http://localhost:8080/swagger-ui/index.html`


## Rodando a aplicação na AWS

Para rodar a API na AWS é necessário contruí-la utilizando o profile como 'aws'.
Utilizando uma janela do Prompt Comand (ou terminal de um sistema Unix) execute os seguintes comandos na raiz do projeto:

1. Construa a aplicação com o seguinte comando: `.\gradlew build -PbuildProfile=aws`
2. Faça o deploy da aplicação e inclua nas variáveis de ambiente o profile do Spring como 'aws'.