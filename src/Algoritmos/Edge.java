package Algoritmos;

public class Edge {
	private String u; // Origem
	private String v; // Destino
	private int t; // Tempo de partida
	private int lambda; // Duração da viagem

	public Edge(String u, String v, int t, int lambda) {
		this.u = u;
		this.v = v;
		this.t = t;
		this.lambda = lambda;
	}

	// Getters
	public String getU() {
		return u;
	}

	public String getV() {
		return v;
	}

	public int getT() {
		return t;
	}

	public int getLambda() {
		return lambda;
	}

	// Setters
	public void setU(String u) {
		this.u = u;
	}

	public void setV(String v) {
		this.v = v;
	}

	public void setT(int t) {
		this.t = t;
	}

	public void setLambda(int lambda) {
		this.lambda = lambda;
	}
}