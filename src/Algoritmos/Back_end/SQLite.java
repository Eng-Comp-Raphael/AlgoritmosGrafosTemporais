package Algoritmos.Back_end;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLite {

    private static final String URL = "jdbc:sqlite:banco_algoritmo.db";

    public SQLite() {
        criarTabelas();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no SQLite: " + e.getMessage());
        }
        return conn;
    }

    private void criarTabelas() {
        String sqlNos = "CREATE TABLE IF NOT EXISTS nos (no TEXT PRIMARY KEY);";
        String sqlEdges = "CREATE TABLE IF NOT EXISTS edges ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "u TEXT NOT NULL, "
                + "v TEXT NOT NULL, "
                + "t INTEGER NOT NULL, "
                + "lambda INTEGER NOT NULL);";
        String sqlConfig = "CREATE TABLE IF NOT EXISTS config ("
                + "id INTEGER PRIMARY KEY CHECK (id = 1), "
                + "noOrigem TEXT, "
                + "noDestino TEXT,"
                + "t_alpha INTEGER, "
                + "t_omega INTEGER);";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlNos);
            stmt.execute(sqlEdges);
            stmt.execute(sqlConfig);
            String initConfig = "INSERT OR IGNORE INTO config (id, noOrigem, t_alpha, t_omega) VALUES (1, NULL, 0, 0);";
            stmt.execute(initConfig);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public void adicionarNo(String no) {
        String sql = "INSERT OR IGNORE INTO nos(no) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, no.toUpperCase()); // Força maiúsculo ao salvar
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }

    public void adicionarEdge(String u, String v, int t, int lambda) {
        String sql = "INSERT INTO edges(u, v, t, lambda) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.toUpperCase()); // Força maiúsculo ao salvar
            pstmt.setString(2, v.toUpperCase()); // Força maiúsculo ao salvar
            pstmt.setInt(3, t);
            pstmt.setInt(4, lambda);
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }

    public void atualizarConfiguracao(String noOrigem, String noDestino, int t_alpha, int t_omega) {
        String sql = "UPDATE config SET noOrigem = ?, noDestino = ?, t_alpha = ?, t_omega = ? WHERE id = 1";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, noOrigem != null ? noOrigem.toUpperCase() : null);
            pstmt.setString(2, noDestino != null ? noDestino.toUpperCase() : null);
            pstmt.setInt(3, t_alpha);
            pstmt.setInt(4, t_omega);
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }

    // ==========================================
    //  AQUI ESTÁ A MÁGICA DA CORREÇÃO:
    //  Tudo que sai do banco recebe .toUpperCase()
    // ==========================================
    public List<String> carregarNos() {
        String sql = "SELECT no FROM nos ORDER BY no ASC";
        List<String> nos = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                nos.add(rs.getString("no").toUpperCase());
            }
        } catch (SQLException e) {}
        return nos;
    }

    public List<Edge> carregarEdges() {
        String sql = "SELECT u, v, t, lambda FROM edges ORDER BY t ASC";
        List<Edge> edges = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String u = rs.getString("u").toUpperCase();
                String v = rs.getString("v").toUpperCase();
                int t = rs.getInt("t");
                int lambda = rs.getInt("lambda");
                edges.add(new Edge(u, v, t, lambda));
            }
        } catch (SQLException e) {}
        return edges;
    }

    public void carregarConfiguracao(Menu menu) {
        String sql = "SELECT noOrigem, noDestino, t_alpha, t_omega FROM config WHERE id = 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String origem = rs.getString("noOrigem");
                String destino = rs.getString("noDestino");
                
                menu.setNoOrigem(origem != null ? origem.toUpperCase() : "");
                menu.setNoDestino(destino != null ? destino.toUpperCase() : "");
                menu.setT_alpha(rs.getInt("t_alpha"));
                menu.setT_omega(rs.getInt("t_omega"));
            }
        } catch (SQLException e) {}
    }

    public void deletarNo(String no) {
        String sql = "DELETE FROM nos WHERE UPPER(no) = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, no.toUpperCase());
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }

    public void deletarEdge(String u, String v, int t, int lambda) {
        String sql = "DELETE FROM edges WHERE UPPER(u) = ? AND UPPER(v) = ? AND t = ? AND lambda = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.toUpperCase());
            pstmt.setString(2, v.toUpperCase());
            pstmt.setInt(3, t);
            pstmt.setInt(4, lambda);
            pstmt.executeUpdate();
        } catch (SQLException e) {}
    }
}