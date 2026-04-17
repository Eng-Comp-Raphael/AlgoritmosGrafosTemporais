package Algoritmos;
//Algoritmo 1

import java.util.HashMap;

import java.util.Map;
import java.util.LinkedList;

public class Algoritmo1 {

	private final int INFINITO = Integer.MAX_VALUE;
	private Map<String, Integer> temposChegada = new HashMap<>();
	private LinkedList<Edge> edgeStream = new LinkedList<>();

	// Executa o Algoritmo 1 conforme o pseudocódigo
	public void executar(LinkedList<String> nos, String origem, int tAlpha, int tOmega, LinkedList<Edge> stream) {
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
				}
			}
			// Linha 6 e 7: else if t >= t_omega break
			else if (t >= tOmega) {
				break;
			}
		}

		// Linha 8: Return
		imprimirResultados();
	}

	private void imprimirResultados() {
		System.out.println("--- Tempos de Chegada Mais Cedo ---");
		temposChegada.forEach((vertice, tempo) -> {
			String tempoStr = (tempo == INFINITO) ? "Inalcançável" : String.valueOf(tempo);
			System.out.println("Vértice " + vertice + ": " + tempoStr);
		});
	}
}