package connections;

import java.sql.*;

public class Connect {
    private static final String url = "jdbc:mysql://monorail.proxy.rlwy.net:26158/railway";
    private static final String user = "root";
    private static final String password = "fqghDHgiBrmAkwSkWVQcyCmxfvTVbLLp";
    private static Connection conn;

    public static Connection getConnect() {
        try {

            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, user, password);
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class UserDao {
        public void cadastrarUser(User usuario) {
            String sql = "INSERT INTO usuario (NOME, EMAIL, SENHA) VALUES (?, ?, ?)";

            PreparedStatement ps = null;

            try {
                ps = Connect.getConnect().prepareStatement(sql);
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

            try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
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
            try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
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

    public static class LivroDao {
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

    public static class User {
        private String nome;
        private String email;
        private String senha;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    public static class Livro{
        private String classificacao;
        private String autor;
        private String titulo;

        public String getClassificacao() {
            return classificacao;
        }
        public void setClassificacao(String classificacao) {
            this.classificacao = classificacao;
        }

        public String getAutor() {
            return autor;
        }
        public void setAutor(String autor) {
            this.autor = autor;
        }

        public String getTitulo() {
            return titulo;
        }
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
    }

    public static class Funcionario{
        private String nome;
        private String senha;

        public String getNome() {
            return nome;
        }
        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getSenha() {
            return senha;
        }
        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

}