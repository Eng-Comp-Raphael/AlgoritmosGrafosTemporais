package Algoritmos.Back_end;
//Algoritmo 1
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Algoritmo1 {

	private final int INFINITO = Integer.MAX_VALUE;
	private Map<String, Integer> temposChegada = new HashMap<>();
	private List<Edge> edgeStream = new ArrayList<>();

	// Rastrea de qual nó viemos para montar o caminho final
    private Map<String, String> predecessores = new HashMap<>();

	// Executa o Algoritmo 1 conforme o pseudocódigo
	public void executar(List<String> nos, String origem, String destino, int tAlpha, int tOmega, List<Edge> stream) {
		this.edgeStream = stream;

		// Linha 1: Inicialização t[x] = tAlpha e t[v] = infinito
		for (String v : nos) {
			temposChegada.put(v, INFINITO);
		}
		temposChegada.put(origem, tAlpha);

		// Linha 2: Arestas de entrada e = (u, v, t, lambda) no fluxo de aresta
		for (Edge e : edgeStream) {
			
			String u = e.getU();
			String v = e.getV();
			int t = e.getT();
			int lambda = e.getLambda();

			int tempoEmU = temposChegada.getOrDefault(u, INFINITO);

			// Linha 3: if t + lambda <= t_omega AND t >= t[u]
			if ((t + lambda <= tOmega) && (t >= tempoEmU) && (tempoEmU != INFINITO)){

				// Linha 4: if t + lambda < t[v]
				if (t + lambda < temposChegada.get(v)) {
					// Linha 5: t[v] = t + lambda
					temposChegada.put(v, t + lambda);

					// Salvar de onde veio para onde foi (acionado ao algoritmo, não interfere em nada nele)
					predecessores.put(v, u);
				}
			}
			// Linha 6 e 7: else if t >= t_omega break
			else if (t >= tOmega) {
				break;
			}
		}

		// Linha 8: Return
		imprimirResultados(origem, destino);
	}

	// Percorre caminho
	private String reconstruirCaminho(String destino, String origem) {
        List<String> caminho = new ArrayList<>();
        String atual = destino;

        // Volta de trás para frente usando o mapa de predecessores
        while (atual != null) {
            caminho.add(atual);
            if (atual.equals(origem)) {
                break; // Chegou na origem, pode parar
            }
            atual = predecessores.get(atual);
        }

        // Como adicionamos do destino para a origem, precisamos inverter a lista
        Collections.reverse(caminho);

        // Junta os nós usando " -> "
        return String.join(" -> ", caminho);
    }

	private void imprimirResultados(String origem, String destino) {
        System.out.println("\n--- Resultado do Caminho ---\n");
        
        Integer tempoFinal = temposChegada.get(destino);
        
        // Verifica se o destino existe e se foi alcançado
        if (tempoFinal == null || tempoFinal == INFINITO) {
            System.out.println("O nó " + destino + " é inalcançável a partir do nó " + origem + " no tempo limite.");
        } else {
            // Monta a string do caminho (ex: A -> B -> C)
            String caminhoCompleto = reconstruirCaminho(destino, origem);
            
            // Imprime o caminho
            System.out.println(caminhoCompleto);
            
            // Pula uma linha
            System.out.println();
            
            // Informa o tempo gasto (tempo de chegada final)
            System.out.println("Tempo gasto: " + tempoFinal);
        }
        
        System.out.println("----------------------------\n");
    }
}