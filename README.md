# Multi-Locadora Galaxy — Sistema de Vendas de Mídias

> Projeto Integrador — ADS3MA · Programação Orientada a Objetos  
> Centro Universitário Senac Santo Amaro · 2024

---

## Sobre o Projeto

Sistema desktop de vendas de mídias digitais (filmes, jogos e álbuns) desenvolvido em Java com arquitetura MVC. O projeto simula a transição de uma locadora física para um modelo digital, permitindo que clientes pesquisem e comprem mídias e que vendedores gerenciem o catálogo completo.

---

## Equipe

| Função | Membro |
|---|---|
| Código | Edson de Oliveira Correia |
| Código | José Elias Gomes Camargo |
| Código | José Joaquin Julcamoro Bustamante |
| Documentação | Gustavo da Silva de Brito |

**Professores orientadores:** Carlos Veríssimo · Marcos Monteiro

---

## Funcionalidades

### Vendedor (Locador)
- Cadastrar, editar e remover filmes, jogos e álbuns
- Gerenciar estoque e preços de mídias
- Preview de capa via URL antes de cadastrar
- Busca por nome de mídia

### Cliente
- Visualizar catálogo completo com cards de mídias
- Buscar mídias por nome
- Adicionar itens ao carrinho
- Finalizar compra com **Cartão de Crédito**, **PIX** ou **Boleto Bancário**
- Atualizar e excluir perfil

### Autenticação
- Tela de escolha de perfil (Cliente ou Vendedor) como porta de entrada
- Login com validação de tipo de usuário contra o banco de dados
- Redirecionamento automático para a tela correta conforme o perfil

---

## Tecnologias e Ferramentas

| Camada | Tecnologia |
|---|---|
| Linguagem | Java (JDK 11+) |
| Interface gráfica | Java Swing (JFrame) |
| Banco de dados | MySQL |
| Conector BD | MySQL Connector/J (JDBC) |
| Arquitetura | MVC (Model-View-Controller) |
| IDEs utilizadas | Eclipse · NetBeans · Replit |
| Banco (modelagem) | MySQL Workbench |
| Versionamento | Git + GitHub |
| Metodologia | Ágil (Scrum/Kanban via Notion) |

---

## Arquitetura MVC

```
src/
├── model/
│   ├── beans/          # Entidades: Usuario, Cliente, Vendedor, Midia, Filme, Jogo, Album,
│   │                   #            Carrinho, Pagamento, Cartao, Pix, Boleto
│   └── dao/            # Acesso ao banco: MidiaDAO, FilmeDAO, JogoDAO, AlbumDAO,
│                       #                  ClienteDAO, UsuarioDAO, ConectaBD
└── view/               # Telas Swing: Login, EscolhaPerfil, Home, TelaInicialCliente,
                        #              CadastroFilmeFrame, CadastroJogoFrame, CadastroAlbumFrame,
                        #              ListagemFrame, ListagemMidiaFrame, Cadastro
```

**Conceitos de POO aplicados:** encapsulamento, herança, polimorfismo e classe abstrata (`Pagamento`).

---

## Como Configurar e Executar

### Pré-requisitos
- Java JDK 11 ou superior
- MySQL Server 8.0+
- MySQL Connector/J no classpath do projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/multilocadora-galaxy.git
cd multilocadora-galaxy
```

### 2. Configurar o banco de dados

Execute o script SQL para criar as tabelas (`usuario`, `cliente`, `vendedor`, `midia`, `filme`, `jogo`, `album`) conforme o Modelo de Entidade e Relacionamento do projeto.

### 3. Criar o arquivo `config.properties`

Na raiz do projeto, crie o arquivo `config.properties` com as credenciais do seu banco. **Este arquivo não é versionado** (está no `.gitignore`) por questões de segurança:

```properties
db.url=jdbc:mysql://localhost:3306/nome_do_banco
db.user=seu_usuario
db.password=sua_senha
```

A classe `ConectaBD` lê essas propriedades em tempo de execução via `FileInputStream`, evitando que credenciais sejam expostas no código-fonte.

### 4. Executar

Execute a classe `Login.java` (que redireciona para `EscolhaPerfil`) como ponto de entrada da aplicação.

---


## 📅 Planejamento

O projeto foi gerenciado com metodologia ágil utilizando Notion para organização das tarefas (WBS), com entregas divididas em: Planejamento, Produção, Execução, Controle e Encerramento. Todas as tarefas foram concluídas dentro do prazo de maio de 2024.

---

## 📚 Aprendizados

Principais desafios superados durante o desenvolvimento:

- Conexão e operações CRUD com banco de dados MySQL via JDBC
- Implementação de transações com `commit` e `rollback` para consistência dos dados
- Construção de interfaces gráficas responsivas com Java Swing
- Trabalho colaborativo remoto com Git/GitHub (branches, commits, pull)
- Aplicação prática dos pilares de POO em um sistema real

---

*Projeto acadêmico desenvolvido no Centro Universitário Senac Santo Amaro — 2024*
