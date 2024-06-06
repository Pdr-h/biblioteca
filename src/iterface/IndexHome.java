package iterface;

import connections.Livro;
import connections.LivroCompleto;
import connections.LivroDAO;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.HashMap;

public class IndexHome extends JFrame {

    private JTree directoryTree;
    private DefaultListModel<String> livroListModel;
    private JList<String> livroList;
    private HashMap<String, Class<?>> classMap;
    private JTextField campoPesquisa = new JTextField();
    private LivroDAO livroDAO;

    public IndexHome(String nomeUsuario) {
        livroDAO = new LivroDAO();
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
        classMap.put("Sair", Sair.class);
        DefaultMutableTreeNode Tronco = new DefaultMutableTreeNode("MENU");
        //pegando o nome declarado acima pra classe/exibicao no index e adicionando no tronco como um galho
        DefaultMutableTreeNode galhoEmprestimo = new DefaultMutableTreeNode("Emprestimo de Livro");
        Tronco.add(galhoEmprestimo);
        DefaultMutableTreeNode galhoSair = new DefaultMutableTreeNode("Sair");
        Tronco.add(galhoSair);
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
                        if (selectedClass == AdquirirLivro.class) {
                            AdquirirLivro adquirirLivro = new AdquirirLivro(nomeUsuario);
                            adquirirLivro.setVisible(true);
                        }
                        if (selectedClass == Sair.class) {
                            new Sair(IndexHome.this);
                        }
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

        //preenchimento da lista de acordo com os livros emprestados por um usuario
        livroDAO.preencherLivroUser(livroListModel, nomeUsuario);
        painelCentro.add(scrollPane, BorderLayout.CENTER);

        //Botão dados do livro
        JButton dadosButton = new JButton("Dados do Livro");
        dadosButton.addActionListener(e -> {
            int indiceSelecionado = livroList.getSelectedIndex(); // Obtém o índice selecionado da lista atualizada
            if (indiceSelecionado != -1) {
                String tituloSelecionado = livroList.getModel().getElementAt(indiceSelecionado); // Obtém o título do livro da lista atualizada
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

        //botao devolver livro
        JButton devolverButton = new JButton("Devolver Livro");
        devolverButton.addActionListener(e -> {
            int indiceSelecionado = livroList.getSelectedIndex();
            if (indiceSelecionado != -1) {
                livroListModel.removeElementAt(indiceSelecionado);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um livro para devolver!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });


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
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(dadosButton);
        botoesPanel.add(devolverButton);
        botoesPanel.add(devolverButton);

        // Adiciona o painel de botões ao painel principal
        painelCentro.add(botoesPanel, BorderLayout.SOUTH);
        mainPanel.add(painelCentro, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(directoryTree), BorderLayout.WEST); // Adiciona a arvore em um JScrollPane para rolagem
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






