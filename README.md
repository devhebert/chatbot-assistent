
# Assistente Especializado em Almoxarifado

Este projeto implementa um assistente de chat especializado em gestão de almoxarifado(ou qualquer contexto que quiser), utilizando Java e a API da OpenAI. O assistente é projetado para responder exclusivamente perguntas relacionadas a controle de estoque, entrada e saída de materiais, localização, validade e outras operações típicas de almoxarifado.


## Recursos Principais


Contextualização Específica: O assistente responde somente perguntas pertinentes ao ambiente de almoxarifado.

Dados Personalizados: Utiliza informações previamente cadastradas em um arquivo local (dados.txt).

Seleção Dinâmica de Modelos: Escolhe automaticamente entre modelos OpenAI (GPT-3 e GPT-4o) com base na quantidade de tokens utilizados.

Formatação Padronizada de Respostas: As respostas seguem um padrão específico contendo nome e posição dos itens.


## Estrutura do Projeto

src/main/java/assistent/Main.java: Arquivo principal contendo toda a lógica do assistente.

src/main/resources/dados.txt: Arquivo de dados contendo informações sobre os itens armazenados no almoxarifado.
## Como Executar

#### Pré-requisitos

- Java 21 ou superior

- Chave de API da OpenAI

#### Configuração Inicial

Clone o repositório:
```

git clone <https://github.com/devhebert/chatbot-assistent>
```

Insira a sua chave API OpenAI em uma variável de ambiente chamada API_KEY.

Exemplo (Linux/Mac):
```

export API_KEY="sua-chave-openai"
```

Exemplo (Windows):
```

set API_KEY="sua-chave-openai"
```

Certifique-se de que os dados do almoxarifado estão disponíveis em src/main/resources/dados.txt. Cada linha deve conter informações no formato desejado, por exemplo:

Nome: Hélice - Posição Estoque: 286.

## Como Usar

Ao executar o programa, você será solicitado a inserir perguntas relacionadas ao almoxarifado.

Exemplo:
```

Digite sua pergunta (ou 'sair' para finalizar):
Qual a posição da Hélice?
```

Resposta:
```
Nome: Hélice - Posição Estoque: 286
```

Caso a pergunta não esteja relacionada ao almoxarifado, o assistente irá informar educadamente que não pode ajudar com outros assuntos.

#### Tecnologias Utilizadas

- Java

- API OpenAI (GPT-3 e GPT-4o)

- SimpleOpenAI: Biblioteca Java para interação com a API OpenAI.

- JTokkit: Para gerenciamento e contagem de tokens.

