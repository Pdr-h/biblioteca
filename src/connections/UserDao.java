package connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public void cadastrarUser(User usuario) {
        String sql = "INSERT INTO usuario (NOME, EMAIL, SENHA) VALUES (?, ?, ?)";
        try (Connection conn = Connect.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int autenticarUsuario(String email, String senha) {
        String sql = "SELECT 1 AS tipo FROM usuario WHERE email = ? AND senha = ? " +
                "UNION ALL " +
                "SELECT 2 AS tipo FROM funcionario WHERE nome = ? AND senha = ?";
        try (Connection conn = Connect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            stmt.setString(3, email);
            stmt.setString(4, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tipo"); // Retorna o tipo de usuário (1 para usuário, 2 para funcionário)
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Retorna 0 se não encontrar correspondências ou ocorrer um erro
    }

    public String getNomeUsuario(String email) {
        String nomeUsuario = null;
        String sql = "SELECT nome FROM usuario WHERE email = ?";
        try (Connection conn = Connect.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nomeUsuario = rs.getString("nome");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomeUsuario;
    }
}
