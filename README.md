## Transcrição dos Algoritmos de Grafos Temporais para Java 

**Changelog:**
* `29 de Março de 2026:` Implementação da classe `Edge`, `Algoritmo1` e `Main`.
* `17 de Abril de 2026:` Implementação da classe `Menu`.

> **Nota de Arquitetura:** Por questões de organização e para facilitar possíveis manutenções/alterações futuras, o projeto foi dividido visando criar o máximo de abstração possível.

### Estrutura de Classes

- **`Edge`**: Cria um objeto representando uma aresta temporal `<e = (u, v, t, lambda)>` e permite sua manipulação a partir dos métodos *Getters* e *Setters*.
- **`Algoritmo1`**: Responsável por encapsular e executar a lógica central do algoritmo de busca no grafo temporal.
- **`Main`**: Classe principal, responsável por instanciar e iniciar a execução do código.
- **`Menu`**: Gerencia o fluxo do programa e a interface com o usuário.

### Tarefas Pendentes (To-Do)

- [ ] Definir todas as condições de saída como `-1`.
- [ ] Adicionar um sistema de *log* ou *feedback* sempre que uma alteração for feita através do menu.
- [ ] Verificar a existência prévia de uma aresta antes de permitir a adição de uma nova.
- [ ] Criar um método para remover uma aresta, passando o vértice de origem e o vértice de destino.
- [ ] Implementar o Algoritmo 2.

### Alterações Futuras (Roadmap)

- [ ] Implementar tratamento de erros e exceções (Try/Catch).
- [ ] Criar persistência de dados (Salvar informações em um banco de dados).
- [ ] Otimizar o algoritmo: criar um método para que, ao alcançar o vértice de destino com o caminho mais rápido, a execução seja finalizada imediatamente.
- [ ] Melhorar o *output*: imprimir todo o caminho percorrido, o tempo gasto entre cada vértice e o tempo total do percurso.