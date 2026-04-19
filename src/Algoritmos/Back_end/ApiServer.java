package Algoritmos.Back_end;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApiServer {

    // Método auxiliar para responder ao navegador e liberar o CORS
    private static void handleCorsAndResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        byte[] responseBytes = responseText.getBytes("UTF-8");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    // Método auxiliar para ler os dados enviados pelo Front-end
    private static Map<String, String> parseFormData(HttpExchange exchange) {
        Map<String, String> params = new HashMap<>();
        try (Scanner scanner = new Scanner(exchange.getRequestBody(), "UTF-8").useDelimiter("\\A")) {
            String body = scanner.hasNext() ? scanner.next() : "";
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2) {
                    params.put(kv[0], kv[1]);
                }
            }
        }
        return params;
    }

    public static void iniciar() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // ==========================================
            // ROTA 1: Carregar todos os dados (GET)
            // ==========================================
            server.createContext("/api/grafo", exchange -> {
                SQLite db = new SQLite();
                Menu tempMenu = new Menu();
                db.carregarConfiguracao(tempMenu);
                List<String> nos = db.carregarNos();
                List<Edge> edges = db.carregarEdges();

                StringBuilder json = new StringBuilder("{");
                json.append(String.format("\"config\": {\"origem\": \"%s\", \"destino\": \"%s\", \"tAlpha\": %d, \"tOmega\": %d},", 
                        tempMenu.getNoOrigem(), tempMenu.getNoDestino(), tempMenu.getT_alpha(), tempMenu.getT_omega()));
                
                json.append("\"nos\": [");
                for (int i = 0; i < nos.size(); i++) {
                    json.append("\"").append(nos.get(i)).append("\"");
                    if (i < nos.size() - 1) json.append(",");
                }
                json.append("],\"arestas\": [");
                for (int i = 0; i < edges.size(); i++) {
                    Edge e = edges.get(i);
                    json.append(String.format("{\"u\":\"%s\", \"v\":\"%s\", \"t\":%d, \"lambda\":%d}", e.getU(), e.getV(), e.getT(), e.getLambda()));
                    if (i < edges.size() - 1) json.append(",");
                }
                json.append("]}");
                handleCorsAndResponse(exchange, 200, json.toString());
            });

            // ==========================================
            // ROTA 2: Executar Algoritmo 1 (GET)
            // ==========================================
            server.createContext("/api/executar", exchange -> {
                SQLite db = new SQLite();
                Menu tempMenu = new Menu();
                db.carregarConfiguracao(tempMenu);
                Algoritmo1 alg = new Algoritmo1();
                String jsonResult = alg.executarParaWeb(db.carregarNos(), tempMenu.getNoOrigem(), tempMenu.getNoDestino(), tempMenu.getT_alpha(), tempMenu.getT_omega(), db.carregarEdges());
                handleCorsAndResponse(exchange, 200, jsonResult);
            });

            // ==========================================
            // ROTA 3: Salvar Configurações (POST)
            // ==========================================
            server.createContext("/api/config", exchange -> {
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    Map<String, String> params = parseFormData(exchange);
                    new SQLite().atualizarConfiguracao(
                        params.get("origem"), params.get("destino"),
                        Integer.parseInt(params.get("tAlpha")), Integer.parseInt(params.get("tOmega"))
                    );
                    handleCorsAndResponse(exchange, 200, "{\"status\":\"ok\"}");
                } else handleCorsAndResponse(exchange, 200, "");
            });

            // ==========================================
            // ROTA 4: Adicionar e Deletar Nós (POST)
            // ==========================================
            server.createContext("/api/no/add", exchange -> {
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    new SQLite().adicionarNo(parseFormData(exchange).get("no"));
                    handleCorsAndResponse(exchange, 200, "{\"status\":\"ok\"}");
                } else handleCorsAndResponse(exchange, 200, "");
            });

            server.createContext("/api/no/delete", exchange -> {
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    new SQLite().deletarNo(parseFormData(exchange).get("no"));
                    handleCorsAndResponse(exchange, 200, "{\"status\":\"ok\"}");
                } else handleCorsAndResponse(exchange, 200, "");
            });

            // ==========================================
            // ROTA 5: Adicionar e Deletar Arestas (POST)
            // ==========================================
            server.createContext("/api/aresta/add", exchange -> {
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    Map<String, String> p = parseFormData(exchange);
                    new SQLite().adicionarEdge(p.get("u"), p.get("v"), Integer.parseInt(p.get("t")), Integer.parseInt(p.get("lambda")));
                    handleCorsAndResponse(exchange, 200, "{\"status\":\"ok\"}");
                } else handleCorsAndResponse(exchange, 200, "");
            });

            server.createContext("/api/aresta/delete", exchange -> {
                if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    Map<String, String> p = parseFormData(exchange);
                    new SQLite().deletarEdge(p.get("u"), p.get("v"), Integer.parseInt(p.get("t")), Integer.parseInt(p.get("lambda")));
                    handleCorsAndResponse(exchange, 200, "{\"status\":\"ok\"}");
                } else handleCorsAndResponse(exchange, 200, "");
            });

            server.setExecutor(null);
            server.start();
            System.out.println("\n[API] Servidor FULL STACK rodando na porta 8080!\n");

        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}