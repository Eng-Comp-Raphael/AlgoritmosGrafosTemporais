package Algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		// Configuração do Cenário
		String[] vertices = { "A", "B", "C", "D" }; // Numero de Verteces
		String origem = "A";   //Vertece de partida
		int tAlpha = 10; // tempo inicial
		int tOmega = 50; // tempo final

		// Criando o fluxo de arestas (Edge Stream)
		List<Edge> stream = new ArrayList<>();
		stream.add(new Edge("A", "B", 15, 5)); // Chega em B às 20
		stream.add(new Edge("A", "C", 20, 10)); // Chega em C às 30
		stream.add(new Edge("B", "C", 22, 3)); // Chega em C às 25 (Melhor que o anterior!)
		stream.add(new Edge("C", "D", 40, 5)); // Chega em D às 45
		stream.add(new Edge("A", "D", 55, 10)); // Fora do intervalo (Gera break)

		// Instanciando e rodando o algoritmo
		Algoritmo1 algoritmo = new Algoritmo1();
		algoritmo.executar(vertices, origem, tAlpha, tOmega, stream);
	}
}