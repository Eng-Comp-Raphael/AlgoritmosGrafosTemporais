## Transcrição dos Algoritmos de Grafos Temporais para Java 

**Changelog:**
* `29 de Março de 2026:` Implementação da classe `Edge`, `Algoritmo1` e `Main`.
* `17 de Abril de 2026:` Implementação da classe `Menu`.
* `18 de Abril de 2026:` Integração com banco de dados SQLite (persistência de dados), reestruturação do Algoritmo 1 para focar em nó destino e imprimir o caminho detalhado, formatação de saída de dados, e criação das funcionalidades de deleção de nós e arestas.
* `19 de Abril de 2026:` **Transformação para Full-Stack**. Criação de um Front-end web (SPA) reativo usando HTML/CSS/JS. Implementação da classe `ApiServer` no Java para fornecer rotas REST. Correção de *case sensitivity* (padronização de letras maiúsculas) no banco de dados e aprimoramento matemático da resposta visual (uso do símbolo '∞' para nós inalcançáveis).

> **Nota de Arquitetura:** Por questões de organização e para facilitar possíveis manutenções/alterações futuras, o projeto foi dividido visando criar o máximo de abstração possível, adotando agora um modelo Cliente-Servidor (Front-end Web comunicando-se com API Java).

### Estrutura de Classes (Back-end)

- **`Edge`**: Cria um objeto representando uma aresta temporal `<e = (u, v, t, lambda)>`, permitindo sua manipulação via *Getters*, *Setters* e formatação de saída com `toString()`.
- **`Algoritmo1`**: Responsável por encapsular e executar a lógica central do algoritmo de busca no grafo temporal (rastreando os predecessores para reconstruir o caminho).
- **`Main`**: Classe principal, responsável por instanciar e iniciar a execução do código (agora iniciando também o servidor web).
- **`Menu`**: Gerencia o fluxo do programa para testes via terminal.
- **`Options`**: Guarda os métodos de execução do fluxo gerenciado por **`Menu`**.
- **`SQLite`**: Gerencia a conexão via JDBC, criação de tabelas e todas as operações de persistência (CRUD) para os nós, arestas e configurações gerais com tratamento de *case sensitivity*.
- **`ApiServer`**: Servidor HTTP nativo do Java que atua como ponte de comunicação (API REST) entre o navegador do usuário e o sistema Java.

### Estrutura do Front-end
- **`index.html`**: Estrutura a interface em formato *Single Page Application* (SPA), dividida em abas de navegação.
- **`style.css`**: Define o *Design System* do painel (cores, espaçamentos, tipografia e responsividade).
- **`script.js`**: Gerencia o estado local, renderiza listas em tempo real e realiza as requisições HTTP (`fetch`) para a API Java.

### Tarefas Pendentes (To-Do)

- [ ] Definir todas as condições de saída como `-1` no terminal *(Parcialmente feito)*.
- [x] Adicionar um sistema de *log* ou *feedback* sempre que uma alteração for feita.
- [ ] Verificar a existência prévia de uma aresta antes de permitir a adição de uma nova.
- [x] Criar um método para remover nós e arestas.
- [ ] Implementar o Algoritmo 2.

### Alterações Futuras (Roadmap)

- [ ] Implementar tratamento de erros e exceções avançado (Try/Catch).
- [x] Criar persistência de dados (Salvar informações em um banco de dados).
- [x] Otimizar o algoritmo: criar um método para que, ao alcançar o vértice de destino com o caminho mais rápido, a execução seja finalizada imediatamente.
- [x] Melhorar o *output*: imprimir todo o caminho percorrido, o tempo gasto entre cada vértice e o tempo total do percurso.
- [x] Criar Front-end com HTML/CSS/JS.

---

### Detalhamento: A Classe `Main`

A classe `Main` funciona como o **ponto de partida (entry point)** de toda a aplicação. Quando o projeto é executado, a Máquina Virtual do Java (JVM) procura especificamente por esta classe.

**Métodos:**
* `public static void main(String[] args)`: É o método principal e obrigatório. Sua função neste projeto é dupla: ele aciona a classe `ApiServer` (para iniciar a escuta de requisições web na porta 8080) e cria uma instância do `Menu` para manter o controle via terminal rodando em paralelo.

---

### Detalhamento: A Classe `Edge`

A classe `Edge` funciona como o modelo de dados (entidade) fundamental do projeto. Ela é responsável por representar e estruturar as **arestas temporais** que compõem o grafo.

**Variáveis de Instância:**
Esta classe encapsula as quatro propriedades essenciais de uma aresta temporal em formato de tupla `<e = (u, v, t, lambda)>`:
* `u` (`String`): Representa o vértice de **origem** (ponto de partida).
* `v` (`String`): Representa o vértice de **destino** (ponto de chegada).
* `t` (`int`): Define o **tempo de partida** (o momento exato em que a viagem inicia).
* `lambda` (`int`): Define a **duração da viagem** (o tempo necessário para ir de *u* até *v*).

---

### Detalhamento: A Classe `Algoritmo1`

A classe `Algoritmo1` é o motor lógico do projeto. Ela encapsula a execução do algoritmo de **Tempo de Chegada Mais Cedo** (Earliest Arrival Time) em grafos temporais. 

**Métodos Principais:**
* **`executar(...)` e `executarParaWeb(...)`**: Recebem os parâmetros do grafo. O método inicializa os tempos, processa as arestas validando as condições matemáticas de viagem temporal (como `t + lambda <= tOmega` e `t >= t_u`) e atualiza os mapas. A versão web retorna uma string em formato **JSON** formatada com a resposta.
* **`reconstruirCaminho(...)`**: Método auxiliar que atua na pós-execução, usando o dicionário de predecessores para caminhar de trás para frente até a origem e retornar a rota exata. No caso de nós inalcançáveis, o algoritmo agora retorna o símbolo matemático infinito (`∞`).

---

### Detalhamento: A Classe `SQLite`

A classe `SQLite` funciona como a camada de **Persistência de Dados (DAO - Data Access Object)** do projeto. Utilizando a biblioteca JDBC (`java.sql`), ela é responsável pelo CRUD no arquivo `banco_algoritmo.db`.

**Mecânica Principal:**
* Além de gerenciar conexões e tabelas (`nos`, `edges`, `config`), a classe agora atua como um filtro de *Case Sensitivity*. Todos os métodos de Inserção, Consulta e Exclusão forçam o uso de `.toUpperCase()`. Isso garante que requisições vindas do Front-end Web e digitações no terminal se comuniquem perfeitamente, prevenindo erros de rotas inalcançáveis por divergência de maiúsculas/minúsculas.

---

### Detalhamento: A Classe `ApiServer` (Novo)

A classe `ApiServer` transformou a aplicação em um servidor Web. Utilizando a biblioteca nativa `com.sun.net.httpserver`, ela expõe as funcionalidades do Java para a rede local (Porta 8080).

**Rotas (Endpoints) Implementadas:**
* **`GET /api/grafo`**: Retorna um JSON contendo o estado completo e atual do banco de dados (Configurações, Nós e Arestas) para o navegador renderizar.
* **`GET /api/executar`**: Instancia o `Algoritmo1` e devolve o cálculo do menor caminho diretamente para a interface web.
* **`POST /api/config`, `/api/no/*`, `/api/aresta/*`**: Rotas que recebem dados (`x-www-form-urlencoded`) enviados pelo JavaScript quando o usuário interage com os botões, chamando a classe `SQLite` correspondente para gravar ou deletar as informações.