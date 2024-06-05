package iterface;

import connections.Livro;
import connections.LivroCompleto;
import connections.LivroDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Funcionario extends JFrame {
    private JTextField campoPesquisa = new JTextField();
    private static DefaultListModel<String> livroListModel;
    private JList<String> livroList;
    private LivroDAO livroDAO;
    private LivroCompleto livroCompleto;
    public Funcionario() {
        livroDAO = new LivroDAO();

        // Configurações da janela
        setTitle("Funcionário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

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

        // Botões no sul
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton adicionarButton = new JButton("Adicionar");
        JButton removerButton = new JButton("Remover");
        JButton editarButton = new JButton("Editar");
        JButton dadosButton = new JButton("Dados do Livro");
        JButton sair = new JButton("Sair");
        botoesPanel.add(adicionarButton);
        botoesPanel.add(removerButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(dadosButton);
        botoesPanel.add(sair);
        contentPane.add(botoesPanel, BorderLayout.SOUTH);

        // Adicionar livro
        adicionarButton.addActionListener(e -> {
            String titulo = JOptionPane.showInputDialog(this, "Digite o título do livro:");
            String autor = JOptionPane.showInputDialog(this, "Digite o autor do livro:");
            String classificacao = JOptionPane.showInputDialog(this, "Digite a classificação do livro:");
            String avPubStr = JOptionPane.showInputDialog(this, "Digite a avaliação do Livro:");
            String anoPubStr = JOptionPane.showInputDialog(this, "Digite o ano de publicação do Livro:");
            String localPub = JOptionPane.showInputDialog(this, "Digite o local de publicação do Livro:");
            String isbn = JOptionPane.showInputDialog(this, "Digite o ISBN do Livro:");

            if (titulo != null && autor != null && classificacao != null && avPubStr != null && anoPubStr != null && localPub != null && isbn != null) {
                try {
                    int avPub = Integer.parseInt(avPubStr);
                    int anoPub = Integer.parseInt(anoPubStr);
                    livroDAO.adicionarLivro(titulo, autor, classificacao, avPub, anoPub, localPub, isbn);
                    livroListModel.addElement(titulo);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ano de publicação ou Avaliação devem ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Remover livro
        removerButton.addActionListener(e -> {
            int indiceSelecionado = livroList.getSelectedIndex();
            Object[] options = {"Sim", "Não"}; // Opções para o diálogo
            int result = JOptionPane.showOptionDialog(this, "Deseja mesmo remover este livro?", "Remover", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            if (result == 0) { // "Sim"
                if (indiceSelecionado != -1) {
                    String tituloRemovido = livroListModel.getElementAt(indiceSelecionado);
                    livroListModel.remove(indiceSelecionado);
                    livroDAO.removerLivro(tituloRemovido);
                } else {
                    JOptionPane.showMessageDialog(this, "Selecione um livro para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } else if (result == 1) { // "Não" ou fechar a caixa d mensagem, cancela a remoçao
                return;
            }
        });


        // Editar livro
        editarButton.addActionListener(e -> {
            int indiceSelecionado = livroList.getSelectedIndex();
            if (indiceSelecionado != -1) {
                String tituloAtual = livroListModel.getElementAt(indiceSelecionado);
                String novoTitulo = JOptionPane.showInputDialog(this, "Novo título do livro:", tituloAtual);
                String novoAutor = JOptionPane.showInputDialog(this, "Novo autor do livro:", livroDAO.getAutorLivro(tituloAtual));
                String novaClassificacao = JOptionPane.showInputDialog(this, "Nova classificação do livro:", livroDAO.getClassificacaoLivro(tituloAtual));
                String novoAvPubStr = JOptionPane.showInputDialog(this, "Avaliação do Livro:", livroDAO.getAvaliacaoLivro(tituloAtual));
                String novoAnoPubStr = JOptionPane.showInputDialog(this, "Novo ano de publicação do Livro:", livroDAO.getAnoPublicacao(tituloAtual));
                String novoLocalPub = JOptionPane.showInputDialog(this, "Local de publicação do Livro:", livroDAO.getLocalPublicacao(tituloAtual));
                String novoISBN = JOptionPane.showInputDialog(this, "Novo ISBN do Livro:", livroDAO.getIsbn(tituloAtual));

                if (novoTitulo != null && novoAutor != null && novaClassificacao != null && novoAvPubStr != null && novoAnoPubStr != null && novoLocalPub != null && novoISBN != null) {
                    try {
                        int novoAvPub = Integer.parseInt(novoAvPubStr);
                        int novoAnoPub = Integer.parseInt(novoAnoPubStr);

                        livroDAO.atualizarLivro(tituloAtual, novoTitulo, novoAutor, novaClassificacao, novoAvPub, novoAnoPub, novoLocalPub, novoISBN);
                        livroListModel.setElementAt(novoTitulo, indiceSelecionado);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Ano de publicação deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });


        // Botão Dados
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

        //botao SAIR
        sair.addActionListener(e -> {
            new Sair(Funcionario.this);
        });




        // Preenche a lista de livros
        preencherListaLivros();
        setVisible(true);
    }
    //Metodo pra retornar os livros
    private void preencherListaLivros() {
        livroListModel = livroDAO.getLivros();
        livroList.setModel(livroListModel);
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
}