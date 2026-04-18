package Algoritmos;

import java.util.Scanner;
import java.util.Comparator;

public class Main_menu_algoritmo_1 {

    Scanner scanner = new Scanner(System.in);

    public void main_algoritmo_1(int celectOptions, Menu obj){
        
        
        switch (celectOptions) {
            case 0: // Retornar ao menu principal
                break;

            case 1: // Adicionar nos
                System.out.println("==== Adicionando nós ====");
                System.out.println("[0] - Sair de adicionar nós\n");
                
                do{
                    System.out.println("Informe o nó e precione enter: ");
                    String no = scanner.nextLine();

                    if (no.equalsIgnoreCase("0")){
                        break;
                    }
            
                    obj.getNos().add(no); // adicionar a lista
                    obj.getNos().sort(null); // ordenar lista 

                }while(true);
                    break;
            
            case 2: //Informar nó de origem
                System.out.println("Informe o nó origem: ");
                obj.setNoOrigem(scanner.nextLine());
                break;

            case 3:  // Informar tempo inicial
                System.out.println("Informe o tempo inicial: ");
                obj.setT_alpha(scanner.nextInt());
                scanner.nextLine();
                break;

            case 4: // Informar tempo final
                System.out.println("Informe o tempo final");
                obj.setT_omega(scanner.nextInt());
                scanner.nextLine();
                break;
            
            case 5: // Definindo edges
                
                System.out.println("==== Definindo Arestas ====");
                System.out.println("[0] - Voltar");

                do{
                    System.out.println("=== + Novo nó + ===");
                    System.out.println("Nó partida: ");
                    String u = scanner.nextLine();

                    if(u.equals("0")){
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

                }while(true);

                break;

            case 6: // Inprimir detalhes
                System.out.println("Nos: " + obj.getNos());
                System.err.println("Nó origem: " + obj.getNoOrigem());
                System.out.println("Tempo inicial: " + obj.getT_alpha());
                System.out.println("Tempo final: " + obj.getT_omega());
                System.out.println("Edges: " + obj.getEdges());
                break;

            case 7: 
                
                Algoritmo1 algoritmo = new Algoritmo1();
                algoritmo.executar(obj.getNos(), obj.getNoOrigem(), obj.getT_alpha(), obj.getT_omega(), obj.getEdges());
                break;

            default:
                System.out.println("Erro: informe uma das alternativas");
                break;
        }
        
        //System.out.println("Nos: " + nos);
    }
    
}
