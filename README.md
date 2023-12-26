# CarHub

Bem-vindo ao CarHub, uma aplicação que oferece uma API RESTful para gerenciar informações de usuários e carros.

O projeto se encontra rodando em uma instancia da ec2 da aws: 

Frontend: http://35.175.218.157/

Backend: http://52.207.243.170:8080/

Documentação Swegger: http://52.207.243.170:8080/swagger-ui/index.html


# Estórias de Usuário:

1 - Como usuário não autenticado, quero poder realizar login na aplicação fornecendo meu login e senha para obter um token de acesso JWT.

2 - Como usuário não autenticado, quero poder cadastrar uma nova conta fornecendo informações como nome, sobrenome, e-mail, data de nascimento, login, senha e telefone.

3 - Como usuário autenticado, quero poder visualizar minhas próprias informações, incluindo nome, sobrenome, e-mail, data de nascimento, login, senha, telefone, lista de carros, data de criação da conta e última data de login.

4 - Como usuário autenticado, quero poder visualizar todos os usuários cadastrados no sistema.

5 - Como usuário autenticado, quero poder buscar informações de um usuário específico pelo seu ID.

6 - Como usuário autenticado, quero poder remover minha própria conta.

7 - Como usuário autenticado, quero poder atualizar minhas próprias informações, como nome, sobrenome, e-mail, data de nascimento, login, senha e telefone.

8 - Como usuário autenticado, quero poder visualizar a lista de todos os carros associados à minha conta.

9 - Como usuário autenticado, quero poder cadastrar um novo carro fornecendo informações como ano de fabricação, placa, modelo e cor.

10 - Como usuário autenticado, quero poder buscar informações de um carro específico pelo seu ID.

11 - Como usuário autenticado, quero poder remover um carro associado à minha conta pelo seu ID.

12 - Como usuário autenticado, quero poder atualizar informações de um carro associado à minha conta pelo seu ID.


# Solução

A solução proposta para o Sistema de Usuários e Carros é fundamentada no ecossistema Spring, oferecendo um microserviço eficiente com um endpoint RESTful pronto para integração com diversos front-ends. Utilizei o framework Spring Boot, que inclui o Tomcat embarcado, facilitando tanto a manutenção quanto o deploy da aplicação.

A arquitetura do sistema segue o padrão MVC (Model-View-Controller), garantindo uma separação clara de responsabilidades. A camada de controller gerencia exclusivamente as requisições e respostas da API. Adotei o uso de DTOs (Data Transfer Objects) para representar os atributos transitados entre o front-end e a API. Essa escolha visa manter as entidades responsáveis apenas pela persistência no banco de dados, garantindo uma clara separação de preocupações.

Na camada de serviço, implementei todas as regras de negócio da aplicação, enquanto os repositórios são responsáveis pela interação com o banco de dados. Durante o desenvolvimento, priorizei os princípios SOLID e as práticas de clean code, buscando uma estrutura de código robusta, escalável e de fácil manutenção. Essa abordagem contribui para a construção de um sistema flexível e adaptável a futuras expansões e atualizações.

Utilizei o Spring Framework para proporcionar um ambiente de desenvolvimento ágil e eficiente, promovendo boas práticas e padrões de design que elevam a qualidade do código e a experiência de desenvolvimento.

No processo de construção da solução, implementei testes unitários para assegurar a qualidade do sistema. Esses testes são essenciais para validar o comportamento esperado das diversas partes da aplicação, garantindo que alterações futuras não comprometam o funcionamento correto do sistema.

# Passo a Passo para Executar o Projeto

Pré-requisitos
Antes de começar, certifique-se de ter o Maven instalado em sua máquina. 

1. Clonar o Repositório

git clone https://seu-repositorio.git
cd nome-do-projeto

2. Buildar o Projeto com Maven

mvn clean install

3. Executar o Projeto
Após o build ser concluído com sucesso, você pode executar o projeto com o comando:
java -jar target/nome-do-arquivo.jar

4. Acessar a Aplicação
A aplicação estará disponível em http://localhost:8080. Certifique-se de que a porta 8080 esteja disponível.

5. Testes
Para executar os testes do projeto, utilize o comando:
mvn test

