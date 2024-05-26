package iterface;

import connections.Connect;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class IndexHome extends JFrame {

    private JTree directoryTree;
    private DefaultListModel<String> livroListModel;
    private JList<String> livroList;
    private HashMap<String, Class<?>> classMap;
    private JPanel centerPanel = new JPanel();
    private JTextField campoPesquisa = new JTextField();

    public IndexHome(String nomeUsuario) {

        // Configurações da janela
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setResizable(false);
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Mapeamento dos nomes dos itens da barra lateral e inicio do index
        classMap = new HashMap<>();
        classMap.put("Emprestimo de Livro", AdquirirLivro.class);
        DefaultMutableTreeNode Tronco = new DefaultMutableTreeNode("MENU");
        //pegando o nome declarado acima pra classe/exibicao no index e adicionando no tronco como um galho
        for (String itemName : classMap.keySet()) {
            DefaultMutableTreeNode galhos = new DefaultMutableTreeNode(itemName);
            Tronco.add(galhos);
        }
        //Pegando o tronco e transformando em arvore
        directoryTree = new JTree(Tronco);
        directoryTree.setBackground(Color.decode("#55AA98"));
        directoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        directoryTree.setCellRenderer(new CustomTreeCellRenderer()); //Chamada de classe para imagem customizada
        directoryTree.addTreeSelectionListener(e -> { //evento para seleçao do galho com a classe
            DefaultMutableTreeNode selectedGalho = (DefaultMutableTreeNode) directoryTree.getLastSelectedPathComponent();
            if (selectedGalho != null && selectedGalho.isLeaf()) { // se a classe instanciada for apenas ela, sem flihos, retorna true e executa o metodo
                String itemName = selectedGalho.getUserObject().toString();
                Class<?> selectedClass = classMap.get(itemName); //busca a classe de acordo com o ItemName que seria o valor em String digitado no classMap.put
                if (selectedClass != null) {
                    try {
                        // Cria uma instância da classe selecionada e exibe
                        dispose();
                        AdquirirLivro adquirirLivro = new AdquirirLivro(nomeUsuario);
                        adquirirLivro.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        //PESQUISA
        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.add(campoPesquisa, BorderLayout.NORTH);
        livroListModel = new DefaultListModel<>();
        livroList = new JList<>(livroListModel);
        JScrollPane scrollPane = new JScrollPane(livroList);
        preencherListaLivros(livroListModel, nomeUsuario);
        painelCentro.add(scrollPane, BorderLayout.CENTER);

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
        add(mainPanel);
        // Adiciona a árvore ao painel principal
        mainPanel.add(painelCentro, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(directoryTree), BorderLayout.WEST); // Adiciona a árvore em um JScrollPane para rolagem
        setVisible(true);
    }

    // Método para preencher a lista de livros
    private void preencherListaLivros(DefaultListModel<String> livrosModel, String emailUsuario) {
        String sql = "SELECT titulo FROM dados_dos_livros " +
                "INNER JOIN user_livro ON dados_dos_livros.id_livro = user_livro.id_livro " +
                "INNER JOIN usuario ON user_livro.id_usuario = usuario.id_usuario " +
                "WHERE usuario.email = ?"; // Corrigido para usar o email do usuário
        try (PreparedStatement stmt = Connect.getConnect().prepareStatement(sql)) {
            stmt.setString(1, emailUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tituloLivro = rs.getString("titulo");
                livrosModel.addElement(tituloLivro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // Classe para renderizar os galhos da arvore com imagens customizada
    private class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                      boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            // Obtendo o galho atual da árvore
            DefaultMutableTreeNode galho = (DefaultMutableTreeNode) value;

            //verifica se está retornando uma string, caso sim atribui a uma variavel pra verificacao de igualdade e atribuicao da imagem de acordo com o galho
            if (galho.getUserObject() instanceof String) {
                String itemName = (String) galho.getUserObject();
                if (itemName.equals("Emprestimo de Livro")) {
                    ImageIcon icon = new ImageIcon("src/assets/addLivro.png");
                    label.setIcon(icon);
                }
                if (itemName.equals("Sair")) {
                    ImageIcon icon = new ImageIcon("src/assets/sair.png");
                    label.setIcon(icon);
                }
            }
            return label;
        }
    }
}
