# StudyFlow: Sistema de Gerenciamento de Estudos

O **StudyFlow** é uma aplicação web voltada ao gerenciamento e organização da rotina acadêmica. Ele permite ao estudante cadastrar disciplinas, controlar tarefas com prazos e prioridades, acompanhar métricas de desempenho por meio de gráficos dinâmicos e manter o foco em suas metas de estudo através de uma interface intuitiva e responsiva.

Projetado como **Single Page Application (SPA)**, o sistema utiliza **Vue 3** no front-end e uma API REST em **Spring Boot** no back-end.

---

## 🔗 Links de Acesso

* **Acesso à Aplicação (Front-end):** [study-flow-eta-eight.vercel.app](https://study-flow-eta-eight.vercel.app)
* **Serviço de API (Back-end):** Publicado na plataforma [Render](https://render.com)
* **Especificação OpenAPI JSON:** [studyflow-2ntr.onrender.com/swagger-ui/index.html](https://studyflow-2ntr.onrender.com/swagger-ui/index.html#/)

---

## 🚀 Funcionalidades Principais

* **Minhas Disciplinas:** Cadastro, edição, listagem e exclusão de disciplinas com personalização de cores e tags visuais.
* **Gestão de Tarefas:** Adição de atividades vinculadas a disciplinas com controle detalhado via Quadro Kanban de **prazos**, **status** (*A Fazer*, *Em Andamento*, *Concluído*) e níveis de **prioridade** (*Baixa*, *Média*, *Alta*).
* **Filtros e Visualização:** Organização de tarefas e navegação fluida por disciplinas e estados de conclusão.
* **Painel Dashboard:** Indicadores em tempo real e visualização gráfica (*Chart.js*) com a distribuição do total de tarefas por status, nível de prioridade e por disciplina.
* **Experiência Responsiva:** Layout adaptável com navegação otimizada para dispositivos móveis e desktops, incluindo menus colapsáveis (Offcanvas) e gráficos com rolagem horizontal automática.

---

## 🛠️ Tecnologias Utilizadas

### Front-end
* **Vue 3** (Script Setup e Composition API)
* **Vue Router** (Gerenciamento de rotas e navegação)
* **Bootstrap 5** (Grid responsivo, utilitários CSS e componentes acessíveis)
* **Chart.js** & **Vue-Chartjs** (Painéis visuais dinâmicos)
* **Axios** (Integração HTTP com interceptores JWT para autenticação)
* **HTML5 Semântico & CSS Customizado** (Estilização via variáveis CSS e escopo local)

### Back-end & Infraestrutura
* **Java 21 / Spring Boot** (API RESTful)
* **Spring Security + JWT** (Autenticação e controle de sessão)
* **Swagger** (Documentação da API)
* **PostgreSQL** (Banco de dados relacional)
* **Docker** (Containerização do ambiente de produção/back-end)
* **Vercel** (Hospedagem estática contínua do Front-end)
* **Render** (Hospedagem em nuvem do serviço de API)

---

## 💻 Como Rodar o Projeto Localmente

### Pré-requisitos
* **Node.js** (v18 ou superior)
* **Java JDK 21**
* **Maven 3.9+**
* **Git**

### 1. Clonar o Repositório
```bash
git clone https://github.com/RosanaCeline/StudyFlow.git
cd studyflow
```

### 2. Configurar e Executar o Back-end (Spring Boot)
Certifique-se de ter um banco de dados PostgreSQL ativo.

Navegue até a pasta do back-end:

```bash
cd backend
```

Configure as variáveis de ambiente no arquivo application.properties (ou passe via ambiente):

```Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studyflow_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=sua_chave_secreta_jwt
```

Execute o servidor:

```bash
mvn spring-boot:run
```

A API estará rodando em: http://localhost:8080

### 3. Configurar e Executar o Front-end (Vue 3)
Em um novo terminal, navegue até a pasta do front-end:

```bash
cd frontend
```

Instale as dependências do projeto:

```bash
npm install
```

Crie um arquivo .env.local na raiz da pasta frontend e aponte para a API local:

```Snippet de código
VITE_API_URL=http://localhost:8080/api
```

Inicie o servidor de desenvolvimento:

```bash
npm run dev
```

Acesse a aplicação no seu navegador: http://localhost:5173

### 🤖 Uso de Inteligência Artificial no Desenvolvimento
Em conformidade com as diretrizes do projeto, ferramentas de Inteligência Artificial (LLMs) foram empregadas de forma assistiva durante todo o ciclo de vida do software:

Resolução de Problemas CSS e Responsividade: Auxílio na correção de loops de renderização do Chart.js em telas menores, aplicando calculadores dinâmicos CSS (max()) e controle do comportamento de reflow/scrollbars no Bootstrap 5.

Acessibilidade e Boas Práticas: Diagnóstico e refatoração de manipuladores do DOM.
