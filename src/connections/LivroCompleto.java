package connections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LivroCompleto extends Livro {
    private String localPub;
    private String anoPub;

    public String getLocalPub() {
        return localPub;
    }

    public void setLocalPub(String localPub) {
        this.localPub = localPub;
    }

    public String getAnoPub() {
        return anoPub;
    }

    public void setAnoPub(String anoPub) {
        this.anoPub = anoPub;
    }

    public static Livro getLivroPorTitulo(String titulo) {
        String sql = "SELECT * FROM dados_dos_livros WHERE titulo = ?";
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Recupera os dados do ResultSet
                String autor = rs.getString("autor");
                String classificacao = rs.getString("classificacao");
                int avaliacaoPublico = rs.getInt("avaliacao_publico");
                String anoPublicacao = rs.getString("ano_publicacao");
                String localPublicacao = rs.getString("local_publicacao");
                String isbn = rs.getString("isbn");

                // Cria e retorna um objeto Livro com as informações recuperadas
                LivroCompleto livroCompleto = new LivroCompleto();
                livroCompleto.setTitulo(titulo);
                livroCompleto.setAutor(autor);
                livroCompleto.setClassificacao(classificacao);
                livroCompleto.setAvaliacaoPub(avaliacaoPublico);
                livroCompleto.setAnoPub(anoPublicacao);
                livroCompleto.setLocalPub(localPublicacao);
                livroCompleto.setIsbn(isbn);

                return livroCompleto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Se o livro não for encontrado, retorna null
        return null;
    }
}
