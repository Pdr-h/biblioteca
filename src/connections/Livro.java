package connections;
//getters e setters do livro
public class Livro{
    private String classificacao;
    private String autor;
    private String titulo;
    private int avaliacaoPub;
    private String isbn;

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

    public int getAvaliacaoPub() {
        return avaliacaoPub;
    }
    public void setAvaliacaoPub(int avaliacaoPub) {
        this.avaliacaoPub = avaliacaoPub;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}