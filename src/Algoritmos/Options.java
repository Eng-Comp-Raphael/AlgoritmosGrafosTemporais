package Algoritmos;

import java.util.Scanner;
import java.util.Comparator;

public class Options {

    Scanner scanner = new Scanner(System.in);

    public void op_1(Menu obj) { // Adicionar nos
        System.out.println("==== Adicionando nós ====");
        System.out.println("[0] - Sair de adicionar nós");

        do {
            System.out.print("\nInforme o nó e precione enter: ");
            String no = scanner.nextLine();

            if (no.equalsIgnoreCase("0")) { // se no == "0" break
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
                System.out.println("*Nó adicionado com sucesso");
            }

        } while (true);
    }

    // Informar nó de origem
    public void op_2(Menu obj) {

        System.out.println("Informe o nó origem: ");
        obj.setNoOrigem(scanner.nextLine());
    }

    // Informar tempo inicial
    public void op_3(Menu obj) {
        System.out.println("Informe o tempo inicial: ");
        obj.setT_alpha(scanner.nextInt());
        scanner.nextLine();
    }

    // Informar tempo final
    public void op_4(Menu obj) {
        System.out.println("Informe o tempo final");
        obj.setT_omega(scanner.nextInt());
        scanner.nextLine();
    }

    // Definindo edges
    public void op_5(Menu obj) {

        System.out.println("==== Definindo Arestas ====");
        System.out.println("[0] - Voltar");

        do {
            System.out.println("=== + Novo nó + ===");
            System.out.println("Nó partida: ");
            String u = scanner.nextLine();

            if (u.equals("0")) {
                break;
            }

            System.out.println("Nó chegada: ");
            String v = scanner.nextLine();

            System.out.println("Tempo de inicio: ");
            int t_iniciao = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Tempo de duração: ");
            int t_duracao = scanner.nextInt();
            scanner.nextLine();

            Edge newEdge = new Edge(u, v, t_iniciao, t_duracao);

            obj.getEdges().add(newEdge);
            obj.getEdges().sort(Comparator.comparingInt(Edge::getT)); // orndena pelo tempo inicial

        } while (true);
    }

    // Inprimir detalhes
    public void op_6(Menu obj) {
        System.out.println("Nos: " + obj.getNos());
        System.err.println("Nó origem: " + obj.getNoOrigem());
        System.out.println("Tempo inicial: " + obj.getT_alpha());
        System.out.println("Tempo final: " + obj.getT_omega());
        System.out.println("Edges: " + obj.getEdges());
    }

    // Executar algoritmo 1
    public void op_7(Menu obj) {
        Algoritmo1 algoritmo = new Algoritmo1();
        algoritmo.executar(obj.getNos(), obj.getNoOrigem(), obj.getT_alpha(), obj.getT_omega(), obj.getEdges());
    }
}
