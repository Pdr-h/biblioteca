package connections;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LivroDAO {
        public void cadastrarLivro(Livro livros) {
            String sql = "INSERT INTO DADOS_DOS_LIVROS (CLASSIFICACAO, AUTOR, TITULO) VALUES (?, ?, ?)";

            PreparedStatement ps = null;

            try {
                ps = Connect.getConnect().prepareStatement(sql);
                ps.setString(1, livros.getClassificacao());
                ps.setString(2, livros.getAutor());
                ps.setString(3, livros.getTitulo());

                ps.execute();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
