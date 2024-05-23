package iterface;

import connections.Connect;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdquirirLivro extends JFrame {
    private JTextField campoPesquisa = new JTextField();
    private DefaultListModel<String> livroListModel;
    private JList<String> livroList;
    private String emailUsuario;

    public AdquirirLivro(String emailUsuario) {
        this.emailUsuario = emailUsuario;
        // Configurações da janela
        setTitle("Adquirir Livro");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela
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

        // Botão adicionar
        JButton botaoAdquirir = new JButton("EMPRESTIMO");
        botaoAdquirir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se algum livro foi selecionado na lista
                if (livroList.getSelectedIndex() != -1) {
                    int selectedIndex = livroList.getSelectedIndex();
                    String livroSelecionado = livroList.getSelectedValue();
                    dispose();

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
                        // Inserção na tabela user_livro com o id_usuario correto
                        String sql = "INSERT INTO user_livro (id_livro, id_usuario) " +
                                "VALUES ((SELECT id_livro FROM dados_dos_livros WHERE titulo = ?), ?)";

                        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
                            stmt.setString(1, livroSelecionado);
                            stmt.setInt(2, idUsuario); // Usando o id_usuario encontrado
                            stmt.executeUpdate();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        // Lidar com o caso em que o id_usuario não foi encontrado
                        System.err.println("ID de usuário não encontrado para o email: " + emailUsuario);
                    }
                    JOptionPane.showMessageDialog(null, "Você adquiriu o livro: " + livroSelecionado);
                    livroListModel.remove(selectedIndex);
                    IndexHome indexHome = new IndexHome(emailUsuario);
                    indexHome.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecione um livro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        contentPane.add(botaoAdquirir, BorderLayout.SOUTH);
        // Preenche a lista de livros
        preencherListaLivros(livroListModel);
        // Torna a janela visível
        setVisible(true);
    }

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

    // Método para preencher a lista de livros com os livros cadastrados no banco de dados
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

    public void livroAtribuido(String titulo) {

    }
}