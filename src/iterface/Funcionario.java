package iterface;

import connections.Connect;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Funcionario extends JFrame {
    private static DefaultListModel<String> livroListModel;
    private JList<String> livroList;

    public Funcionario() {
        // Configurações da janela
        setTitle("Funcionário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Lista de livros
        livroListModel = new DefaultListModel<>();
        livroList = new JList<>(livroListModel);
        JScrollPane scrollPane = new JScrollPane(livroList);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Botões no sul
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton adicionarButton = new JButton("Adicionar");
        JButton removerButton = new JButton("Remover");
        JButton editarButton = new JButton("Editar");
        botoesPanel.add(adicionarButton);
        botoesPanel.add(removerButton);
        botoesPanel.add(editarButton);
        contentPane.add(botoesPanel, BorderLayout.SOUTH);
        //add livro
        adicionarButton.addActionListener(e -> {
            String titulo = JOptionPane.showInputDialog(this, "Digite o título do livro:");
            String autor = JOptionPane.showInputDialog(this, "Digite o autor do livro:");
            String classificacao = JOptionPane.showInputDialog(this, "Digite a classificação do livro:");

            if (titulo != null && autor != null && classificacao != null) {
                adicionarLivro(titulo, autor, classificacao);
                livroListModel.addElement(titulo);
            }
        });
        //remover livro
        removerButton.addActionListener(e -> removerLivro());

        //editar livro
        editarButton.addActionListener(e -> {
            int indiceSelecionado = livroList.getSelectedIndex();
            if (indiceSelecionado != -1) {
                String tituloAtual = livroListModel.getElementAt(indiceSelecionado);
                String novoTitulo = JOptionPane.showInputDialog(this, "Digite o novo título do livro:", tituloAtual);
                String novoAutor = JOptionPane.showInputDialog(this, "Digite o novo autor do livro:", getAutorLivro(tituloAtual));
                String novaClassificacao = JOptionPane.showInputDialog(this, "Digite a nova classificação do livro:", getClassificacaoLivro(tituloAtual));

                if (novoTitulo != null && novoAutor != null && novaClassificacao != null) {
                    atualizarLivro(tituloAtual, novoTitulo, novoAutor, novaClassificacao);
                    livroListModel.setElementAt(novoTitulo, indiceSelecionado);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Preenche a lista de livros
        preencherListaLivros(livroListModel);
        setVisible(true);
    }
    //Metodo pra preencher a lista com os livros do banco de dados
    private void preencherListaLivros(DefaultListModel<String> livrosModel) {
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
    }
    //Metodo pra adicionar um livro no banco
    private void adicionarLivro(String titulo, String autor, String classificacao) {
        // Inserir o livro no banco de dados
        String sql = "INSERT INTO dados_dos_livros (titulo, autor, classificacao) VALUES (?, ?, ?)";
        try (
                PreparedStatement statement = Connect.getConnect().prepareStatement(sql)) {
            statement.setString(1, titulo);
            statement.setString(2, autor);
            statement.setString(3, classificacao);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar o livro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    //Metodo de escolha do livro na lista para depois chamar o metodo de remoçao no banco
    private void removerLivro() {

        int indiceSelecionado = livroList.getSelectedIndex();
        if (indiceSelecionado != -1) {
            String tituloRemovido = livroListModel.getElementAt(indiceSelecionado);
            livroListModel.remove(indiceSelecionado);
            removerLivroDoBanco(tituloRemovido);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    //Metodo pra remover um livro do banco
    private void removerLivroDoBanco(String titulo) {
        try (
                PreparedStatement stmt = Connect.getConnect().prepareStatement("DELETE FROM dados_dos_livros WHERE titulo = ?")) {
            stmt.setString(1, titulo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao remover livro do banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodo pra atualizar o livro no banco
    private void atualizarLivro(String tituloAtual, String novoTitulo, String novoAutor, String novaClassificacao) {
        // Atualizar as informações do livro no banco de dados
        String sql = "UPDATE dados_dos_livros SET titulo = ?, autor = ?, classificacao = ? WHERE titulo = ?;";
        try (PreparedStatement statement = Connect.getConnect().prepareStatement(sql)) {
            statement.setString(1, novoTitulo);
            statement.setString(2, novoAutor);
            statement.setString(3, novaClassificacao);
            statement.setString(4, tituloAtual);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o livro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodo pra pegar o autor de um livro selecionado da lista
    private String getAutorLivro(String titulo) {
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
    //Metodo pra pegar a classificaçao de um livro selecionado da lista
    private String getClassificacaoLivro(String titulo) {
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

    //Lista dos livros
    public static DefaultListModel<String> getLivroListModel() {
        return livroListModel;
    }

}
