## Transcrição dos Algoritmos de Grafos Temporais para Java 

**Changelog:**
* `29 de Março de 2026:` Implementação da classe `Edge`, `Algoritmo1` e `Main`.
* `17 de Abril de 2026:` Implementação da classe `Menu`.
* `19 de Abril de 2026:` Integração com banco de dados SQLite (persistência de dados), reestruturação do Algoritmo 1 para focar em nó destino e imprimir o caminho detalhado, formatação de saída de dados, e criação das funcionalidades de deleção de nós e arestas.

> **Nota de Arquitetura:** Por questões de organização e para facilitar possíveis manutenções/alterações futuras, o projeto foi dividido visando criar o máximo de abstração possível.

### Estrutura de Classes

- **`Edge`**: Cria um objeto representando uma aresta temporal `<e = (u, v, t, lambda)>`, permitindo sua manipulação via *Getters*, *Setters* e formatação de saída com `toString()`.
- **`Algoritmo1`**: Responsável por encapsular e executar a lógica central do algoritmo de busca no grafo temporal (rastreando os predecessores para reconstruir o caminho).
- **`Main`**: Classe principal, responsável por instanciar e iniciar a execução do código.
- **`Menu`**: Gerencia o fluxo do programa, a interface com o usuário e o carregamento inicial dos dados salvos.
- **`Options`**: Guarda os métodos de execução do fluxo gerenciado por **`Menu`**.
- **`SQLite`**: Gerencia a conexão via JDBC, criação de tabelas e todas as operações de persistência (CRUD) para os nós, arestas e configurações gerais.

### Tarefas Pendentes (To-Do)

- [ ] Definir todas as condições de saída como `-1` *(Parcialmente feito)*.
- [x] Adicionar um sistema de *log* ou *feedback* sempre que uma alteração for feita através do menu.
- [ ] Verificar a existência prévia de uma aresta antes de permitir a adição de uma nova.
- [x] Criar um método para remover nós e arestas (Implementado através de menu de deleção com índices).
- [ ] Implementar o Algoritmo 2.

### Alterações Futuras (Roadmap)

- [ ] Implementar tratamento de erros e exceções (Try/Catch).
- [x] Criar persistência de dados (Salvar informações em um banco de dados).
- [ ] Otimizar o algoritmo: criar um método para que, ao alcançar o vértice de destino com o caminho mais rápido, a execução seja finalizada imediatamente.
- [x] Melhorar o *output*: imprimir todo o caminho percorrido, o tempo gasto entre cada vértice e o tempo total do percurso.
- [ ] Criar Front-end com HTML/CSS/JS
---

### Detalhamento: A Classe `Main`

A classe `Main` funciona como o **ponto de partida (entry point)** de toda a aplicação. Quando o projeto é executado, a Máquina Virtual do Java (JVM) procura especificamente por esta classe e seu método principal para iniciar a execução do software.

**Métodos:**
* `public static void main(String[] args)`: É o método principal e obrigatório. Sua única função neste projeto é agir como o "botão de ligar": ele cria uma instância do sistema de menu interativo e chama o método `MenuGlobal()`, transferindo o controle do programa para a interface do usuário e inicializando a conexão com o banco de dados.

**Variáveis:**
Para manter o código limpo e coeso, esta classe não possui variáveis globais ou de instância. Ela utiliza apenas variáveis locais dentro do método `main`:
* `args` (`String[]`): Parâmetro padrão do Java projetado para receber argumentos de linha de comando direto do terminal. (Não utilizado na lógica atual deste projeto).
* `meuMain` (`Menu`): Objeto local que armazena a instância da classe `Menu`, permitindo a inicialização do fluxo principal do programa.---

### Detalhamento: A Classe `Edge`

A classe `Edge` funciona como o modelo de dados (entidade) fundamental do projeto. Ela é responsável por representar e estruturar as **arestas temporais** que compõem o grafo, mapeando não apenas a conexão entre dois pontos, mas também a dimensão do tempo envolvida na viagem.

**Variáveis de Instância:**
Esta classe encapsula as quatro propriedades essenciais de uma aresta temporal em formato de tupla $<e = (u, v, t, \lambda)>$:
* `u` (`String`): Representa o vértice de **origem** (ponto de partida).
* `v` (`String`): Representa o vértice de **destino** (ponto de chegada).
* `t` (`int`): Define o **tempo de partida** (o momento exato em que a viagem inicia).
* `lambda` (`int`): Define a **duração da viagem** (o tempo necessário para ir de *u* até *v*).

**Métodos:**
* **Construtor `Edge(...)`**: Inicializa a aresta exigindo que todas as quatro propriedades ($u, v, t, \lambda$) sejam fornecidas no momento de sua criação.
* **Getters (`getU`, `getV`, `getT`, `getLambda`)**: Métodos de acesso que permitem a leitura segura das propriedades da aresta por outras classes (como os algoritmos e a interface), garantindo o encapsulamento, já que as variáveis são privadas.
* **`toString()`**: Método sobrescrito do Java. Ele "ensina" ao programa como transformar o objeto `Edge` em um texto legível, retornando a formatação exata da tupla `(u, v, t, lambda)` para exibir as arestas de forma limpa e organizada nos menus do terminal.

---

### Detalhamento: A Classe `Algoritmo1`

A classe `Algoritmo1` é o motor lógico do projeto. Ela encapsula a execução do algoritmo de **Tempo de Chegada Mais Cedo** (Earliest Arrival Time) em grafos temporais. Além de descobrir se é possível chegar ao destino no tempo estipulado, ela foi aprimorada para rastrear a rota exata que o usuário deve fazer.

**Variáveis de Instância:**
* `INFINITO` (`int`): Constante que armazena o valor máximo de um inteiro (`Integer.MAX_VALUE`), usada para representar que um vértice ainda não foi alcançado.
* `temposChegada` (`Map<String, Integer>`): Um dicionário (tabela hash) que mapeia o nome de cada vértice ao tempo mais rápido conhecido para chegar até ele ($t_{v}$).
* `predecessores` (`Map<String, String>`): Um dicionário que atua como o "rastro de migalhas". Cada vez que o algoritmo encontra um caminho mais rápido para um vértice, ele anota de qual nó ele veio. Isso é essencial para reconstruir a rota no final.
* `edgeStream` (`List<Edge>`): A lista contendo o fluxo de arestas temporais, que deve ser processada em ordem cronológica.

**Métodos:**
* **`executar(...)`**: É o coração da classe. Ele recebe os parâmetros do grafo (nós, origem, destino, tempo inicial $t_{\alpha}$, limite $t_{\Omega}$ e o fluxo de arestas). O método inicializa os tempos, processa as arestas validando as condições matemáticas de viagem temporal (como $t + \lambda \le t_{\Omega}$ e $t \ge t_{u}$) e atualiza os mapas de tempos e predecessores.
* **`reconstruirCaminho(...)`**: Método auxiliar que atua na pós-execução. Ele recebe o nó de destino e, usando o mapa de `predecessores`, caminha de trás para frente até a origem. Depois, inverte essa lista para retornar o caminho na ordem correta (ex: `A -> B -> G -> K`).
* **`imprimirResultados(...)`**: Responsável por formatar e exibir o *output* final no terminal. Ele verifica se o tempo de chegada ao destino permanece `INFINITO` (inalcançável) ou imprime o caminho percorrido e o tempo total exato do relógio em que o destino foi alcançado.

---

### Detalhamento: A Classe `Menu`

A classe `Menu` atua como o **gerenciador de estado e interface** da aplicação. Ela mantém os dados do grafo em memória durante a execução do programa e fornece o painel interativo (via console) para que o usuário interaja com o sistema.

**Variáveis de Instância:**
* **Controle de Fluxo:** `celectOptions` e `opcao` (`int`) são usadas para ler e rotear a escolha do usuário dentro do loop do menu.
* **Estrutura do Grafo:**
  * `nos` (`List<String>`): Lista contendo os vértices cadastrados.
  * `edges` (`List<Edge>`): Lista contendo as arestas temporais cadastradas.
* **Configurações de Execução:**
  * `noOrigem` e `noDestino` (`String`): Guardam os pontos de partida e chegada para a busca.
  * `t_alpha` e `t_omega` (`int`): Representam, respectivamente, o tempo inicial (abertura da janela de tempo) e o tempo final (limite de encerramento da busca).
* **Objetos Auxiliares:**
  * `algoritmo` (`Options`): Instância da classe que contém as lógicas de cada opção do menu.
  * `leitor` (`Scanner`): Objeto global da classe para capturar as entradas do teclado.

**Métodos:**
* **`Getters` e `Setters`**: Garantem o encapsulamento, permitindo que as classes `Options` e `SQLite` acessem e modifiquem o estado atual do grafo, da origem/destino e das janelas de tempo de forma segura.
* **`menu_app()`**: É o componente visual da classe. Ele limpa o buffer, exibe as opções disponíveis (de 1 a 8, além da opção de saída -1), captura a entrada numérica do usuário e a retorna para ser processada.
* **`MenuGlobal()`**: É o laço principal (loop infinito) da aplicação. Suas funções principais incluem:
  1. **Inicialização e Persistência:** Instancia o banco de dados `SQLite` e automaticamente preenche as listas (`nos`, `edges`) e configurações carregando os dados salvos em execuções anteriores.
  2. **Roteamento:** Utiliza a estrutura `switch-case` para chamar o método correspondente dentro da classe `Options` (como `op_1`, `op_2`, etc.) passando a si mesma (`this`) como parâmetro para que seu estado possa ser modificado.
  3. **Condição de Saída:** Encerra graciosamente o programa quando o usuário escolhe a opção `-1`.

---

### Detalhamento: A Classe `Options`

A classe `Options` atua como o **controlador de ações** (Controller) do projeto. Enquanto a classe `Menu` desenha a interface e roteia as escolhas, é a classe `Options` que contém a lógica real do que acontece quando o usuário escolhe uma opção. Ela faz a ponte entre a entrada do usuário, a memória do programa e o banco de dados.

**Variáveis de Instância:**
* `scanner` (`Scanner`): Objeto utilizado para capturar as entradas de texto e números digitadas pelo usuário no terminal.
* `db` (`SQLite`): Instância ativa da classe de banco de dados, usada para garantir que cada alteração feita na memória também seja salva fisicamente.

**Métodos (Lógica de Negócios):**
Os métodos desta classe (`op_1` a `op_8`) recebem a própria instância do `Menu` (chamada de `obj`) como parâmetro, permitindo que modifiquem o estado global do grafo.
* **Gerenciamento de Nós (`op_1`, `op_6`)**: 
  * O `op_1` permite a inserção contínua de novos vértices. Ele impede a criação de nós duplicados, ordena a lista alfabeticamente/numericamente e salva no banco de dados. 
  * O `op_6` permite a exclusão de um nó específico, removendo-o simultaneamente da lista em memória e do SQLite.
* **Configuração de Parâmetros (`op_2`, `op_3`)**: Responsáveis por definir as regras da busca. O `op_2` capta os vértices de origem e destino, enquanto o `op_3` capta a janela de tempo da viagem ($t_{\alpha}$ e $t_{\Omega}$). Ambos atualizam automaticamente a tabela de configurações no banco.
* **Gerenciamento de Arestas (`op_4`, `op_7`)**: 
  * O `op_4` permite a criação contínua de arestas temporais, exigindo origem, destino, tempo de partida e duração. Após a inserção, ele reordena a lista cronologicamente baseada no tempo de partida (`t`) e salva no banco.
  * O `op_7` lista todas as arestas com um índice numérico `[i]` e permite que o usuário delete uma conexão específica do sistema digitando seu respectivo número.
* **Exibição e Execução (`op_5`, `op_8`)**: 
  * O `op_5` atua como um *dashboard*, imprimindo de forma formatada e organizada todos os dados atuais do grafo e das configurações.
  * O `op_8` é o gatilho final que instancia a classe `Algoritmo1` e repassa a ela todos os dados coletados e estruturados para que a matemática dos caminhos mínimos temporais seja calculada.

---

### Detalhamento: A Classe `SQLite`

A classe `SQLite` funciona como a camada de **Persistência de Dados (DAO - Data Access Object)** do projeto. Utilizando a biblioteca JDBC (`java.sql`), ela é responsável por criar, ler, atualizar e deletar (CRUD) todas as informações em um arquivo de banco de dados local (`banco_algoritmo.db`), garantindo que nenhum progresso seja perdido quando o programa for fechado.

**Variáveis de Instância (Constantes):**
* `URL` (`String`): Uma constante estática que define a string de conexão JDBC (`jdbc:sqlite:banco_algoritmo.db`). Ela indica exatamente onde o arquivo do banco de dados será criado e lido (na raiz do projeto).

**Métodos:**
* **Construtor `SQLite()` e Conexão**:
  * Ao instanciar a classe, o construtor chama automaticamente o método `criarTabelas()`, que garante a existência estrutural das tabelas `nos`, `edges` e `config` usando comandos SQL `CREATE TABLE IF NOT EXISTS`.
  * O método `connect()` encapsula a abertura da conexão com o banco de dados, sendo utilizado em bloco `try-with-resources` por todos os outros métodos para evitar vazamento de memória.
* **Inserção e Atualização (Create / Update)**:
  * `adicionarNo(...)`: Insere um novo vértice na tabela `nos`. Possui proteção `IGNORE` para evitar erros caso o nó já exista.
  * `adicionarEdge(...)`: Salva os atributos exatos de uma nova aresta temporal (`u`, `v`, `t`, `lambda`) na tabela `edges`.
  * `atualizarConfiguracao(...)`: Mantém a tabela `config` atualizada. Como ela possui apenas uma linha (controlada por `id = 1`), este método faz um `UPDATE` com os nós de origem/destino e as janelas de tempo ($t_{\alpha}$ e $t_{\Omega}$) escolhidos pelo usuário.
* **Carregamento de Dados (Read)**:
  * `carregarNos()` e `carregarEdges()`: Executam consultas (`SELECT`) no banco de dados e retornam listas preenchidas (ordenadas alfabeticamente e cronologicamente, respectivamente) para popular a memória da classe `Menu` ao iniciar o programa.
  * `carregarConfiguracao(...)`: Lê a linha única de configurações e utiliza os métodos *Setters* do próprio objeto `Menu` para restaurar o estado anterior.
* **Exclusão de Dados (Delete)**:
  * `deletarNo(...)`: Executa o comando `DELETE` para remover permanentemente um vértice do banco de dados com base no seu nome.
  * `deletarEdge(...)`: Remove uma aresta específica da tabela validando a combinação exata de seus quatro atributos (origem, destino, partida e duração) para garantir que apenas a aresta correta seja apagada.