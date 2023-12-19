# Avaliação Técnica - Publicações Online

## Apresentação

Este projeto contém um microsserviço referente a avaliação técnica cujo objetivo é desenvolver uma aplicação REST sobre o domínio de processos jurídicos

## Funcionalidades

Para a realização deste projeto, três entidades (classes) foram utilizadas como modelo: `User`, `Prosecution` e `Defendant` que representam o usuário logado, o processo a ser cadastrado e o réu a ser adicionado.
As premissas foram:

- Através do endpoint `\users` é possível criar um usuário, através do método POST;
- Para realizar o login é necessário enviar, através do método POST, uma requisição para o endpoint `\login` o e-mail e a senha. É obtido como resposta um token para acesso dos conteúdos privados;
- É necessário estar logado para adicionar e remover processos através do endpoint `\prosecutions`. Alguns campos como data de abertura do processo, protocolo e descrição foram desenvolvidos;
- Caso o processo já esteja cadastrado, é apresentada uma mensagem de erro (utilizando handlers do Spring apropriadamente);
- Também é possível filtrar os processos que incluam alguma palavra em sua descrição, por exemplo `\prosecutions?description=palavra` irá retornar todos os processos cadastrados por aquele usuário que possuam **palavra** em sua descrição;
- Com o endpoint `\prosecutions\1234` é possível acessar o processo cujo protocolo é **1234**, desde que o processo esteja cadastrado pelo usuário logado;
- De maneira análoga, é possível cadastrar os réus no sistema através do endpoint `\defendants`. No entanto, não é necessário estar logado;
- Com o endpoint `\prosecutions\1234\defendants` é possível obter os réus associados ao processo **1234**;
- Usando o método POST é possível vincular um réu a um processo através do endpoint `\prosecutions\1234\defendants\12345678900`, onde é adicionado ao processo **1234** o réu com CPF/CNPJ **12345678900**;
- Arquivo XML utilizado pelo Liquibase disponível no projeto;
- Testes desenvolvidos com JUnit end-to-end (E2E) disponíveis no pacote de testes.
