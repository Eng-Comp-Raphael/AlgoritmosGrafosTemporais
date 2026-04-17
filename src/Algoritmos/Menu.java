package Algoritmos;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Collection;
import java.util.Comparator;

public class Menu {
    //fluxo do menu
    public int numberCelect;
    public int celectOptions;
    public int opcao;
    
    //Criação dos contrutores para o algoritmo 1
    public LinkedList<String> nos = new LinkedList<>();
    public String noOrigem;
    public int t_alpha;
    public int t_omega;
    public LinkedList<Edge> edges = new LinkedList<>();
    //objetos recorrentes
    public Scanner leitor = new Scanner(System.in);

    
    /*public Menu(){
        this.numberCelect = 0;
        this.celectOptions = 0;
    }*/

    public int menu_principal(){
        //Scanner opScanner = new Scanner(System.in);
        System.out.println("[1] - Algoritmo 1");
        System.out.println("[2] - Algoritmo 2");
        System.out.println("[3] - Algoritmo 3");
        System.out.println("[0] - Finalizar Programa");
        opcao = leitor.nextInt();
        leitor.nextLine(); // limpar sobra do enter
        return opcao;
    }

    public int menu_Algoritmo_1(){
        //Scanner opScanner = new Scanner(System.in);
        System.out.println("[1] - adicionar nós");
        System.out.println("[2] - Informar vertece de origem");
        System.out.println("[3] - Infomar tempo inicial");
        System.out.println("[4] - Informar tempo final");
        System.out.println("[5] - Definir edges");
        System.out.println("[6] - Imprimir detalhes ");
        System.out.println("[7] - Executar algoritmo 1");

        System.out.println("[0] - Voltar ao menu principal");
        opcao = leitor.nextInt();
        leitor.nextLine();
        return opcao;
    }
 
    public void main_algoritmo_1(int celectOptions){
        
        switch (celectOptions) {
            case 0: // Retornar ao menu principal
                break;

            case 1: // Adicionar nos
                System.out.println("==== Adicionando nós ====");
                System.out.println("[0] - Sair de adicionar nós");
                
                do{
                    System.out.println("Informe o nó e precione enter: ");
                    String no = leitor.nextLine();

                    if (no.equalsIgnoreCase("0")){
                        break;
                    }
            
                    nos.add(no); // adicionar a lista
                    nos.sort(null); // ordenar lista 

                }while(true);
                    break;
            
            case 2: //Informar nó de origem
                System.out.println("Informe o nó origem: ");
                noOrigem = leitor.nextLine();
                break;

            case 3:  // Informar tempo inicial
                System.out.println("Informe o tempo inicial: ");
                t_alpha = leitor.nextInt();
                leitor.nextLine();
                break;

            case 4: // Informar tempo final
                System.out.println("Informe o tempo final");
                t_omega = leitor.nextInt();
                leitor.nextLine();
                break;
            
            case 5: // Definindo edges
                
                System.out.println("==== Definindo Arestas ====");
                System.out.println("[0] - Voltar");

                do{
                    System.out.println("=== + Novo nó + ===");
                    System.out.println("Nó partida: ");
                    String u = leitor.nextLine();

                    if(u.equals("0")){
                        break;
                    }

                    System.out.println("Nó chegada: ");
                    String v = leitor.nextLine();

                    System.out.println("Tempo de inicio: ");
                    int t_iniciao = leitor.nextInt();
                    leitor.nextLine();

                    System.out.println("Tempo de duração: ");
                    int t_duracao = leitor.nextInt();
                    leitor.nextLine();

                    Edge newEdge = new Edge(u, v, t_iniciao, t_duracao);

                    edges.add(newEdge);
                    edges.sort(Comparator.comparingInt(Edge::getT)); // orndena pelo tempo inicial

                }while(true);

                break;

            case 6: // Inprimir detalhes
                System.out.println("Nos: " + nos);
                System.err.println("Nó origem: " + noOrigem);
                System.out.println("Tempo inicial: " + t_alpha);
                System.out.println("Tempo final: " + t_omega);
                System.out.println("Edges: " + edges);
                break;

            case 7: 
                
                Algoritmo1 algoritmo = new Algoritmo1();
                algoritmo.executar(nos, noOrigem, t_alpha, t_omega, edges);
                break;

            default:
                System.out.println("Erro: informe uma das alternativas");
                break;
        }
        
        //System.out.println("Nos: " + nos);
    }


//MENU GLOBAL ===================================================================================================================
    public void MenuGlobal(){

        do{
            numberCelect = menu_principal();
             // recolhe a informação dado pelo usuario no menu principal 
            switch (numberCelect) {
                case 1:
                    do{
                        celectOptions = menu_Algoritmo_1();
                        if(celectOptions == 0){
                            break;
                        }
                        main_algoritmo_1(celectOptions);

                    }while(true);
                    break;
                
                
                case 0:
                    System.out.println("Programa finalizado");
                    break;
                
                default:
                System.out.println("Erro: informe uma das alternativas");
                break;
            }
                
            //condição de saida
            if(numberCelect == 0){
                break;
            }   
        }while(true);
    }
}
