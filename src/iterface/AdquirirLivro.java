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
    private JComboBox<String> criterioComboBox;

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

        JOptionPane.showMessageDialog(null, "Classificações Disponíveis:\n" +
                "- Ficção Científica\n" +
                "- Romance\n" +
                "- Fantasia\n" +
                "- Policial\n" +
                "- Clássico\n" +
                "- Ficção\n" +
                "- Mistério\n" +
                "- Horror\n\n" +
                "Avaliações do Público: 1-5 estrelas", "Informações", JOptionPane.INFORMATION_MESSAGE);

        // Adicione um JComboBox para os critérios de pesquisa ao lado da barra de pesquisa
        String[] criterios = {"Titulo", "Autor", "Classificaçao", "Avaliaçao Publico", "Ano Publicaçao", "Local Publicaçao", "ISBN"};
        criterioComboBox = new JComboBox<>(criterios);
        criterioComboBox.setBackground(Color.decode("#55AA98"));
        criterioComboBox.setFont(new Font("ARIAL", Font.BOLD, 17));
        contentPane.add(criterioComboBox, BorderLayout.WEST);
        //atualizacao da lista de livros cada vez que uma letra é digitada
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                pesquisarLivros();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                pesquisarLivros();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                pesquisarLivros();
            }
        });

        // Botão Emprestimo do livro
        JButton botaoAdquirir = new JButton("Emprestimo");
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

    //botao voltar
    JButton voltarButton = new JButton("Voltar");
    voltarButton.addActionListener(e -> {
        // Instancia a classe IndexHome
        IndexHome indexHome = new IndexHome(emailUsuario);
        // Torna a janela IndexHome visível
        indexHome.setVisible(true);
        // Fecha a janela atual (AdquirirLivro)
        dispose();
    });

        //botao dados do livro
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
        botoesPanel.add(voltarButton);
        contentPane.add(botoesPanel, BorderLayout.SOUTH);

        preencherListaLivros();
        setVisible(true);
    }

    private void preencherListaLivros() {
        livroListModel = livroDAO.getTitulo();
        livroList.setModel(livroListModel);
    }

    // Método para pesquisar livros com base no critério selecionado e no texto de pesquisa
    private void pesquisarLivros() {
        String textoPesquisa = campoPesquisa.getText().toLowerCase();
        String criterioSelecionado = (String) criterioComboBox.getSelectedItem();
        String sqlQuery = "";

        // consulta SQL com base no critério selecionado
        switch (criterioSelecionado) {
            case "Titulo":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE titulo LIKE ?";
                break;
            case "Classificaçao":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE classificacao LIKE ?";
                break;
            case "Autor":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE autor LIKE ?";
                break;
            case "Avaliaçao Publico":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE avaliacao_publico LIKE ?";
                 break;
            case "Ano Publicaçao":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE ano_publicacao LIKE ?";
                break;
            case "Local Publicaçao":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE local_publicacao LIKE ?";
                break;
            case "ISBN":
                sqlQuery = "SELECT * FROM dados_dos_livros WHERE isbn LIKE ?";
                break;
        }
        // Executa a consulta SQL no banco de dados e atualize a lista de livros com os resultados
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sqlQuery)) {
            stmt.setString(1, "%" + textoPesquisa + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                livroListModel.clear();
                while (rs.next()) {
                    // Adiciona os resultados atualziados da pesquisa à lista de livros
                    String tituloLivro = rs.getString("titulo");
                    livroListModel.addElement(tituloLivro);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}