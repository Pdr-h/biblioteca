package iterface;

import connections.Connect;
import connections.Livro;
import connections.LivroCompleto;
import connections.LivroDAO;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdquirirLivro extends JFrame {
    private JTextField campoPesquisa = new JTextField();
    private DefaultListModel<String> livroListModel;
    private JList<String> livroList;
    private String emailUsuario;
    private LivroDAO livroDAO;

    public AdquirirLivro(String emailUsuario) {
        this.emailUsuario = emailUsuario;
        livroDAO = new LivroDAO();

        // Configurações da janela
        setTitle("Adquirir Livro");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Container principal
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Lista de livros
        livroListModel = new DefaultListModel<>();
        livroList = new JList<>(livroListModel);
        JScrollPane scrollPane = new JScrollPane(livroList);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(campoPesquisa, BorderLayout.NORTH);

        //atualizacao da lista de livros cada vez que uma letra é digitada
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarListaLivros();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarListaLivros();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarListaLivros();
            }
        });

        // Botão Emprestimo do livro
        JButton botaoAdquirir = new JButton("EMPRESTIMO");
        botaoAdquirir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se algum livro foi selecionado na lista
                if (livroList.getSelectedIndex() != -1) {
                    int selectedIndex = livroList.getSelectedIndex();
                    String livroSelecionado = livroList.getSelectedValue();
                    dispose();
                    //Pega o id do usuario a partir do email em que esta logado
                    String sqlUsuario = "SELECT id_usuario FROM usuario WHERE email = ?";
                    int idUsuario = -1;
                    try (PreparedStatement stmtUsuario = Connect.getConnect().prepareStatement(sqlUsuario)) {
                        stmtUsuario.setString(1, emailUsuario);
                        try (ResultSet rsUsuario = stmtUsuario.executeQuery()) {
                            if (rsUsuario.next()) {
                                idUsuario = rsUsuario.getInt("id_usuario");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    if (idUsuario != -1) {
                        // Inserçao na tabela user_livro com o id_usuario
                        String[] partes = livroSelecionado.split(" - ");
                        String tituloLivro = partes[0]; //Pegando o título que está na primeira parte da string
                        String sql = "INSERT INTO user_livro (id_livro, id_usuario) " +
                                "VALUES ((SELECT id_livro FROM dados_dos_livros WHERE titulo = ?), ?)";
                        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
                            stmt.setString(1, tituloLivro);
                            stmt.setInt(2, idUsuario); // Usando o id_usuario encontrado
                            stmt.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    //caso o usuario seja encontrado no banco e o livro esteja selecionado, exibe mensagem e retorna a tela Home
                    JOptionPane.showMessageDialog(null, "Você adquiriu o livro: " + livroSelecionado);
                    livroListModel.remove(selectedIndex);
                    IndexHome indexHome = new IndexHome(emailUsuario);
                    indexHome.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton dadosButton = new JButton("Dados do Livro");
        dadosButton.addActionListener(e -> {
            int indiceSelecionado = livroList.getSelectedIndex();
            if (indiceSelecionado != -1) {
                String tituloSelecionado = livroListModel.getElementAt(indiceSelecionado);
                Livro livroSelecionado = LivroCompleto.getLivroPorTitulo(tituloSelecionado);

                if (livroSelecionado != null) {
                    // Verifica se o livro selecionado é uma instância de LivroCompleto
                    if (livroSelecionado instanceof LivroCompleto) {
                        LivroCompleto livroCompleto = (LivroCompleto) livroSelecionado;
                        // Construir mensagem com todas as informações do livro
                        String mensagem = "Título: " + livroCompleto.getTitulo() + "\n" +
                                "Autor: " + livroCompleto.getAutor() + "\n" +
                                "Classificação: " + livroCompleto.getClassificacao() + "\n" +
                                "Avaliação Público: " + livroCompleto.getAvaliacaoPub() + "\n" +
                                "Ano de Publicação: " + livroCompleto.getAnoPub() + "\n" +
                                "Local de Publicação: " + livroCompleto.getLocalPub() + "\n" +
                                "ISBN: " + livroCompleto.getIsbn();

                        JOptionPane.showMessageDialog(this, mensagem, "Detalhes do Livro", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao recuperar os dados do livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um livro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        //painel pra botoes e adicionando os botoes
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(dadosButton);
        botoesPanel.add(botaoAdquirir);
        contentPane.add(botoesPanel, BorderLayout.SOUTH);

        preencherListaLivros();
        setVisible(true);
    }

    //Metodo para atualizacao da lista com os livros enquanto pesquisa
    public void atualizarListaLivros() {
        String textoPesquisa = campoPesquisa.getText().toLowerCase();
        DefaultListModel<String> novoModelo = new DefaultListModel<>();
        for (int i = 0; i < livroListModel.size(); i++) {
            String item = livroListModel.getElementAt(i);
            if (item.toLowerCase().contains(textoPesquisa)) {
                novoModelo.addElement(item);
            }
        }
        livroList.setModel(novoModelo);
    }
    private void preencherListaLivros() {
        livroListModel = livroDAO.getLivros();
        livroList.setModel(livroListModel);
    }
}