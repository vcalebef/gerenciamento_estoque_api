# gerenciamento-estoque

O objetivo deste projeto foi simplificar ao máximo a utilização da API para o usuário.

Como o sistema representa um estoque com cinco seções, decidi centralizar o gerenciamento diretamente nessas seções. Dessa forma, o responsável pela inclusão de bebidas pode escolher livremente a seção desejada, enquanto todas as regras de negócio — como tipo de bebida e volume — são aplicadas no processamento.

Além disso, foram implementados todos os endpoints de consulta e um endpoint específico para retirada de bebidas, permitindo visualizar o fluxo completo do estoque por meio do histórico.

---

## O que achou do teste? Grau de dificuldade, desafios encontrados, etc.

Considerei o teste de dificuldade intermediária. Embora o desenvolvimento da API em si não tenha exigido o uso de técnicas avançadas, foi necessário interpretar e aplicar corretamente as regras de negócio, o que exigiu atenção aos detalhes e um bom planejamento da estrutura.

Os principais desafios envolveram a definição da modelagem adequada para garantir flexibilidade no gerenciamento das seções e a criação de uma API intuitiva e funcional. Busquei manter o código organizado, seguindo boas práticas e pensando na escalabilidade da aplicação.

---

## Alteraria algo no teste para analisar alguma outra habilidade?

Acredito que o teste poderia incluir outras propriedades para a bebida, como **nome**, **valor** e **fornecedor**. Isso permitiria um gerenciamento mais completo, exigindo a criação de novas tabelas e regras de negócio, e explorando habilidades adicionais como modelagem de dados e relacionamentos.

---

## 📄 Documentação Swagger

A documentação da API foi criada manualmente em formato YAML para manter o projeto simples e facilmente compreensível.

Você pode acessar o arquivo do Swagger neste diretório do repositório:  
🔗 [documentacao/swagger.yaml](https://gitlab.com/vcalebef/gerenciamento-estoque/-/blob/main/documentacao/swagger.yaml?ref_type=heads)

Essa abordagem permite controle total sobre a documentação e facilita ajustes rápidos durante o desenvolvimento.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **Spring Validation**
- **Lombok**
- **ModelMapper**
- **H2 Database (ambiente de dev)**
- **MySQL (ambiente de produção)**
- **Junit e mockito**

---

## 🧪 Requisitos

- Java 17+
- Maven 3.8+
- IDE (IntelliJ, VSCode ou Eclipse)

---

## 🎲 Caso deseje utilizar MySQL, configure no application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/gerenciamento_estoque \
spring.datasource.username=root \
spring.datasource.password=suasenha \
spring.jpa.hibernate.ddl-auto=update
