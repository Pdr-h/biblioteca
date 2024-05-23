package iterface;
import connections.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroUsuario extends JFrame {
    public CadastroUsuario() {
        // Configurações da janela
        setTitle("Cadastro de Usuário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setResizable(false);

        // Container principal
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBackground(Color.decode("#55AA98"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Título "CADASTRE-SE"
        JLabel labelTitulo = new JLabel("CADASTRO");
        labelTitulo.setFont(new Font("Times New Roman", Font.BOLD, 30));
        labelTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(labelTitulo, gbc);

        // Campo de texto "Nome"
        JLabel labelNome = new JLabel("Nome:");
        gbc.gridy = 1;
        contentPane.add(labelNome, gbc);
        JTextField campoNome = new JTextField(20);
        campoNome.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 2;
        contentPane.add(campoNome, gbc);

        // Campo de texto "Email"
        JLabel labelEmail = new JLabel("Email:");
        gbc.gridy = 3;
        contentPane.add(labelEmail, gbc);
        JTextField campoEmail = new JTextField(20);
        campoEmail.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 4;
        contentPane.add(campoEmail, gbc);

        // Campo de senha "Senha"
        JLabel labelSenha = new JLabel("Senha:");
        gbc.gridy = 5;
        contentPane.add(labelSenha, gbc);
        JPasswordField campoSenha = new JPasswordField(20);
        campoSenha.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 6;
        contentPane.add(campoSenha, gbc);

        // Botão
        JButton botao = new JButton("Cadastrar");
        botao.setPreferredSize(new Dimension(150, 40));
        botao.setBackground(Color.decode("#55AA98"));
        botao.setForeground(Color.WHITE);
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
        contentPane.add(botao, gbc);

        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtendo os valores dos campos de texto email senha
                String nome = campoNome.getText();
                String email = campoEmail.getText();
                String senha = new String(campoSenha.getPassword());

                // Criando um novo objeto usuário com os dados inseridos
                Connect.User novoUsuario = new Connect.User();
                novoUsuario.setNome(nome);
                novoUsuario.setEmail(email);
                novoUsuario.setSenha(senha);

                // Cadastrando o novo usuário no banco de dados
                Connect.UserDao userDao = new Connect.UserDao();
                userDao.cadastrarUser(novoUsuario);

                // Exibindo mensagem de cadastro concluído
                JOptionPane.showMessageDialog(null,"CADASTRO CONCLUÍDO", "CADASTRO", JOptionPane.INFORMATION_MESSAGE);

                // Fechando a janela de cadastro e abrindo a tela inicial
                dispose();
                TelaInicial inicio = new TelaInicial();
                inicio.setVisible(true);
            }
        });



        // Torna a janela visível
        setVisible(true);
    }
}

