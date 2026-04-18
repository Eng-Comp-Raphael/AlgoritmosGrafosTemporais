package Algoritmos;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
//import java.util.Collection;
import java.util.Comparator;

public class Menu {
    //fluxo do menu
    private int numberCelect;
    private int celectOptions;
    private int opcao;
    
    //Criação dos contrutores para o algoritmo 1
    private List<String> nos = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();
    
    private String noOrigem;
    private int t_alpha;
    private int t_omega;
    
    private Main_menu_algoritmo_1 menu_algoritmo_1 = new Main_menu_algoritmo_1();
    //objetos recorrentes
    Scanner leitor = new Scanner(System.in);

// ==========================================

    // ==========================================
    // GETTERS E SETTERS - ALGORITMO 1
    // ==========================================

    public List<String> getNos() {
        return nos;
    }

    public void setNos(List<String> nos) {
        this.nos = nos;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String getNoOrigem() {
        return noOrigem;
    }

    public void setNoOrigem(String noOrigem) {
        this.noOrigem = noOrigem;
    }

    public int getT_alpha() {
        return t_alpha;
    }

    public void setT_alpha(int t_alpha) {
        this.t_alpha = t_alpha;
    }

    public int getT_omega() {
        return t_omega;
    }

    public void setT_omega(int t_omega) {
        this.t_omega = t_omega;
    }
    

    
    /*public Menu(){
        this.numberCelect = 0;
        this.celectOptions = 0;
    }*/

    public int menu_principal(){
        //Scanner opScanner = new Scanner(System.in);
        System.out.println("\n==== Menu principal ====\n");
        System.out.println("[1] - Algoritmo 1");
        System.out.println("[2] - Algoritmo 2");
        System.out.println("[3] - Algoritmo 3");
        System.out.println("[0] - Finalizar Programa");
        System.out.print("\nOpção: ");
        opcao = leitor.nextInt();
        leitor.nextLine(); // limpar sobra do enter
        return opcao;
    }

    public int menu_Algoritmo_1(){
        //Scanner opScanner = new Scanner(System.in);
        System.out.println("\n==== Menu Algoritmo ====\n");
        System.out.println("[1] - adicionar nós");
        System.out.println("[2] - Informar vertece de origem");
        System.out.println("[3] - Infomar tempo inicial");
        System.out.println("[4] - Informar tempo final");
        System.out.println("[5] - Definir edges");
        System.out.println("[6] - Imprimir detalhes ");
        System.out.println("[7] - Executar algoritmo 1");

        System.out.println("[0] - Voltar ao menu principal");
        System.out.print("\nOpção: ");
        opcao = leitor.nextInt();
        leitor.nextLine();
        return opcao;
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
                        
                        menu_algoritmo_1.main_algoritmo_1(celectOptions, this); //this = eu mesmo; passa a propria classe como parametro
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
