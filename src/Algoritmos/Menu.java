package Algoritmos;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    // fluxo do menu
    private int celectOptions;
    private int opcao;

    // Criação dos contrutores para o algoritmo 1
    private List<String> nos = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    private String noOrigem;
    private int t_alpha;
    private int t_omega;

    private Options algoritmo = new Options();
    // objetos recorrentes
    Scanner leitor = new Scanner(System.in);

    // ==========================================
    //      GETTERS E SETTERS - ALGORITMO 1
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

    // =================================
    //             METODOS
    // =================================

    public int menu_app() {
        // Scanner opScanner = new Scanner(System.in);
        System.out.println("\n==== Menu Algoritmo ====\n");
        System.out.println("[1] - adicionar nós");
        System.out.println("[2] - Informar vertece de origem");
        System.out.println("[3] - Infomar tempo inicial");
        System.out.println("[4] - Informar tempo final");
        System.out.println("[5] - Definir edges");
        System.out.println("[6] - Imprimir detalhes ");
        System.out.println("[7] - Executar algoritmo 1");

        System.out.println("[-1] - Finalizar programa");
        System.out.print("\nOpção: ");
        opcao = leitor.nextInt();
        leitor.nextLine();
        return opcao;
    }

    public void MenuGlobal() {

        do {
            celectOptions = menu_app();

            switch (celectOptions) {
                case 1:
                    algoritmo.op_1(this); // this passa a propria classe como parametro
                    break;
                case 2:
                    algoritmo.op_2(this);
                    break;

                case 3:
                    algoritmo.op_3(this);
                    break;
                case 4:
                    algoritmo.op_4(this);
                    break;
                case 5:
                    algoritmo.op_5(this);
                    break;
                case 6:
                    algoritmo.op_6(this);
                    break;
                case 7:
                    algoritmo.op_7(this);
                    break;

                default:
                    System.out.println("Erro: informe uma das alternativas");
                    break;
            }

            // Condição de saida
            if (celectOptions == -1) {
                System.out.println("\nPrograma finalizado\n");
                break;
            }
        } while (true);
    }
}
