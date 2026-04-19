package Algoritmos.Back_end;

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

    public String toString() {
        return "(" + u + ", " + v + ", " + t + ", " + lambda + ")";
    }
}