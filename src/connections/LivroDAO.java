package connections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;

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
            e.printStackTrace();
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

    public DefaultListModel<String> getLivros() {
        DefaultListModel<String> livrosModel = new DefaultListModel<>();
        String sql = "SELECT * FROM dados_dos_livros";
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
    public void preencherListaLivros(DefaultListModel<String> livrosModel, String emailUsuario) {
        String sql = "SELECT titulo, autor, classificacao FROM dados_dos_livros " +
                "INNER JOIN user_livro ON dados_dos_livros.id_livro = user_livro.id_livro " +
                "INNER JOIN usuario ON user_livro.id_usuario = usuario.id_usuario " +
                "WHERE usuario.email = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, emailUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String classificacao = rs.getString("classificacao");
                String livroInfo = String.format("%s - %s - %s", titulo, autor, classificacao);
                livrosModel.addElement(livroInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
