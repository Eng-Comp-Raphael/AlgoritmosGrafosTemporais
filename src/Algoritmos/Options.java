package Algoritmos;

import java.util.Scanner;
import java.util.Comparator;

public class Options {

    Scanner scanner = new Scanner(System.in);
    
    // Instância do banco de dados
    SQLite db = new SQLite(); 

    public void op_1(Menu obj) { // Adicionar nos
        System.out.println("==== Adicionando nós ====");
        System.out.println("[-1] - Voltar");

        do {
            System.out.print("\nInforme o nó e precione enter: ");
            String no = scanner.nextLine();

            if (no.equalsIgnoreCase("-1")) { // se no == "0" break
                break;
            }
            // impedir nos repetidos
            if (obj.getNos().contains(no)) {
                System.out.println("O nó informado ja foi adicionado");
            }

            else {
                obj.getNos().add(no); // adicionar nó a lista
                obj.getNos().sort(null); // ordenar lista (ordena tando letras quanto numeros e combinações de letra e
                                         // numeros com a1)
                //Salvando no banco de dados
                db.adicionarNo(no);
                System.out.println("*Nó adicionado com sucesso");
            }

        } while (true);
    }

    // Informar nó de origem
    public void op_2(Menu obj) {
        
        System.out.print("Informe o nó origem: ");
        obj.setNoOrigem(scanner.nextLine());
        
        System.out.print("Informe o nó destino: ");
        obj.setNoDestino(scanner.nextLine());
        
        // Atualiza o banco com os dois nós
        db.atualizarConfiguracao(obj.getNoOrigem(), obj.getNoDestino(), obj.getT_alpha(), obj.getT_omega());
        System.out.println("* Origem e Destino definidos.");
    }

    // Informar tempo inicial e final
    public void op_3(Menu obj) {
        System.out.print("Informe o tempo inicial: ");
        obj.setT_alpha(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Informe o tempo final: ");
        obj.setT_omega(scanner.nextInt());
        scanner.nextLine();

        //Salvando no banco de dados
        db.atualizarConfiguracao(obj.getNoOrigem(), obj.getNoDestino(), obj.getT_alpha(), obj.getT_omega());
    }

    // Definindo edges
    public void op_4(Menu obj) {

        System.out.println("==== Definindo Arestas ====");
        System.out.println("[-1] - Voltar");

        do {
            System.out.println("=== + Novo nó + ===");
            System.out.print("Nó partida: ");
            String u = scanner.nextLine();

            if (u.equals("-1")) {
                break;
            }

            System.out.print("Nó chegada: ");
            String v = scanner.nextLine();

            if (v.equals("-1")) {
                break;
            }

            System.out.print("Tempo de inicio: ");
            int t_iniciao = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Tempo de duração: ");
            int t_duracao = scanner.nextInt();
            scanner.nextLine();

            Edge newEdge = new Edge(u, v, t_iniciao, t_duracao);

            obj.getEdges().add(newEdge);
            obj.getEdges().sort(Comparator.comparingInt(Edge::getT)); // orndena pelo tempo inicial
            
            //Salvando no banco de dados
            db.adicionarEdge(u, v, t_iniciao, t_duracao);

            System.out.println("*Aresta adicionada com sucesso!");
        } while (true);
    }

    // Inprimir detalhes
    public void op_5(Menu obj) {
        System.out.println("Nos: " + obj.getNos());
        System.out.println("Nó origem: " + obj.getNoOrigem());
        System.out.println("Nó destino: " + obj.getNoDestino());
        System.out.println("Tempo inicial: " + obj.getT_alpha());
        System.out.println("Tempo final: " + obj.getT_omega());
        System.out.println("Edges:");
        if (obj.getEdges().isEmpty()) {
            System.out.println("  Nenhuma aresta cadastrada.");
        } else {
            System.out.println("==== Arestas ====");
            for (int i = 0; i < obj.getEdges().size(); i++) {
                System.out.println("  [" + i + "] - " + obj.getEdges().get(i).toString());
            }
        }
    }

    // Deletar nó
    public void op_6(Menu obj) {
        System.out.println("==== Deletar Nó ====");
        System.out.println("[-1] -Voltar");
        System.out.println("Nós atuais: " + obj.getNos());
        System.out.print("Informe o nó que deseja deletar: ");
        String no = scanner.nextLine();

        if (!no.equals("-1")) {
            if (obj.getNos().contains(no)) {
                obj.getNos().remove(no); // Remove da memória
                db.deletarNo(no);        // Remove do SQLite
                System.out.println("* Nó '" + no + "' deletado com sucesso!");
            } else {
                System.out.println("Erro: O nó informado não existe na lista.");
            }
        }
    }

    // Deletar aresta
    public void op_7(Menu obj) {
        System.out.println("==== Deletar Aresta ====");
        System.out.println("[-1] -Voltar");
        if (obj.getEdges().isEmpty()) {
            System.out.println("Nenhuma aresta cadastrada no momento.");
            return;
        }

        // Imprime as arestas com um índice na frente [0], [1], etc.
        for (int i = 0; i < obj.getEdges().size(); i++) {
            System.out.println("[" + i + "] - " + obj.getEdges().get(i).toString());
        }

        System.out.print("\nInforme o número da aresta que deseja deletar: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (index >= 0 && index < obj.getEdges().size()) {
            // Pega a aresta selecionada
            Edge edgeRemovida = obj.getEdges().get(index);
            
            // Remove da lista em memória
            obj.getEdges().remove(index);
            
            // Remove do banco de dados enviando os 4 parâmetros exatos
            db.deletarEdge(edgeRemovida.getU(), edgeRemovida.getV(), edgeRemovida.getT(), edgeRemovida.getLambda());
            
            System.out.println("* Aresta deletada com sucesso!");
        } else if (index != -1) {
            System.out.println("Erro: Opção inválida.");
        }
    }
    // Executar algoritmo 1
    public void op_8(Menu obj) {
        Algoritmo1 algoritmo = new Algoritmo1();
        algoritmo.executar(obj.getNos(), obj.getNoOrigem(), obj.getNoDestino(), obj.getT_alpha(), obj.getT_omega(), obj.getEdges());
    }
}