package connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class LivroDAO {

    public void adicionarLivro(String titulo, String autor, String classificacao,int avaliacao_publico,int ano_publicacao,String local_publicacao,  String isbn) {
        String sql = "INSERT INTO dados_dos_livros (titulo, autor, classificacao, avaliacao_publico, ano_publicacao, local_publicacao, isbn) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = Connect.getConnect().prepareStatement(sql)) {
            statement.setString(1, titulo);
            statement.setString(2, autor);
            statement.setString(3, classificacao);
            statement.setInt(4, avaliacao_publico);
            statement.setInt(5, ano_publicacao);
            statement.setString(6,local_publicacao);
            statement.setString(7, isbn);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removerLivro(String titulo) {
        String sql = "DELETE FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "LIVRO EMPRESTADO A UM USUARIO!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarLivro(String tituloAtual, String novoTitulo, String novoAutor, String novaClassificacao, int novoAvPub, int novoAnoPub, String novoLocalPub, String novoIsbn) {
        String sql = "UPDATE dados_dos_livros SET titulo = ?, autor = ?, classificacao = ?, avaliacao_publico = ?, ano_publicacao = ?, local_publicacao = ?, isbn = ? WHERE titulo = ?";
        try (PreparedStatement statement = Connect.getConnect().prepareStatement(sql)) {
            statement.setString(1, novoTitulo);
            statement.setString(2, novoAutor);
            statement.setString(3, novaClassificacao);
            statement.setInt(4, novoAvPub);
            statement.setInt(5, novoAnoPub);
            statement.setString(6,novoLocalPub);
            statement.setString(7, novoIsbn);
            statement.setString(8, tituloAtual);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DefaultListModel<String> getTitulo() {
        DefaultListModel<String> livrosModel = new DefaultListModel<>();
        String sql = "SELECT titulo FROM dados_dos_livros";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String tituloLivro = rs.getString("titulo");
                livrosModel.addElement(tituloLivro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livrosModel;
    }

    public String getAutorLivro(String titulo) {
        String sql = "SELECT autor FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("autor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getClassificacaoLivro(String titulo) {
        String sql = "SELECT classificacao FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("classificacao");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAvaliacaoLivro(String titulo) {
        String sql = "SELECT avaliacao_publico FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("avaliacao_publico");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getIsbn(String titulo) {
        String sql = "SELECT isbn FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("isbn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAnoPublicacao(String titulo) {
        String sql = "SELECT ano_publicacao FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ano_publicacao");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLocalPublicacao(String titulo) {
        String sql = "SELECT local_publicacao FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("local_publicacao");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //preenchimento da lista de acordo com os livros emprestados por um usuario
    public void preencherLivroUser(DefaultListModel<String> livrosModel, String emailUsuario) {
        String sql = "SELECT titulo FROM dados_dos_livros " +
                "INNER JOIN user_livro ON dados_dos_livros.id_livro = user_livro.id_livro " +
                "INNER JOIN usuario ON user_livro.id_usuario = usuario.id_usuario " +
                "WHERE usuario.email = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, emailUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                livrosModel.addElement(titulo); // Adiciona apenas o título do livro à lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void devolverLivro(String nomeUsuario, String tituloLivro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = Connect.getConnect();
            String sql = "DELETE ul " +
                    "FROM user_livro ul " +
                    "JOIN usuario u ON ul.id_usuario = u.id_usuario " +
                    "JOIN dados_dos_livros l ON ul.id_livro = l.id_livro " +
                    "WHERE u.nome = ? AND l.titulo = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeUsuario);
            stmt.setString(2, tituloLivro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar conexão e declarações
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
