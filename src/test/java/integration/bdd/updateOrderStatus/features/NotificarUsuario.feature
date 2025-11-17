# language: pt
Funcionalidade: Notificar usuário quando o pedido estiver pronto para retirada
  Como CLIENTE que realizou um pedido
  Quero ser notificado quando o pedido estiver pronto
  Para que eu possa retirá-lo

  Contexto:
    Dado que existam pedidos

  Cenario: Notificar cliente com sucesso quando o pedido estiver pronto
    Dado que o email do cliente do pedido é "joaozin@email.com" e o nome é "João Lucas"
    Quando um pedido estiver pronto
    Então o sistema deve notificar o cliente
    E o email deve conter o nome do cliente
    E o email deve conter o número do pedido
    E o email deve conter o momento em que o pedido ficou pronto
