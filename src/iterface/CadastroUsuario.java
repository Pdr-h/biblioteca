package iterface;

import connections.User;
import connections.UserDao;
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
        setLocationRelativeTo(null);
        setResizable(false);

        // Container principal
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBackground(Color.decode("#55AA98"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Título CADASTRE-SE
        JLabel labelTitulo = new JLabel("CADASTRO");
        labelTitulo.setFont(new Font("Times New Roman", Font.BOLD, 30));
        labelTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(labelTitulo, gbc);

        // Campo de Nome
        JLabel labelNome = new JLabel("Nome:");
        gbc.gridy = 1;
        contentPane.add(labelNome, gbc);
        JTextField campoNome = new JTextField(20);
        campoNome.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 2;
        contentPane.add(campoNome, gbc);

        // Campo de Email
        JLabel labelEmail = new JLabel("Email:");
        gbc.gridy = 3;
        contentPane.add(labelEmail, gbc);
        JTextField campoEmail = new JTextField(20);
        campoEmail.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 4;
        contentPane.add(campoEmail, gbc);

        // Campo de senha
        JLabel labelSenha = new JLabel("Senha:");
        gbc.gridy = 5;
        contentPane.add(labelSenha, gbc);
        JPasswordField campoSenha = new JPasswordField(20);
        campoSenha.setPreferredSize(new Dimension(100, 30));
        gbc.gridy = 6;
        contentPane.add(campoSenha, gbc);

        // Botão CADASTRAR
        JButton botao = new JButton("Cadastrar");
        botao.setPreferredSize(new Dimension(150, 40));
        botao.setBackground(Color.decode("#55AA98"));
        botao.setForeground(Color.WHITE);
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
        contentPane.add(botao, gbc);
        //Evento do botao Cadastro, para cadastrar ususario no banco
        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Obtendo os valores dos campos de texto
                String nome = campoNome.getText();
                String email = campoEmail.getText();
                String senha = new String(campoSenha.getPassword());

                // Interrompe o cadastro se estiver vazio algum campo
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Digite um nome válido!");
                    return;
                }
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Digite um email válido!");
                    return;
                }
                if (senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "O campo senha precisa ser preenchido!");
                    return;
                }

                // Cadastrando o novo usuario dentro do banco de dados
                User novoUsuario = new User();
                UserDao userDao = new UserDao();
                userDao.cadastrarUser(novoUsuario);
                JOptionPane.showMessageDialog(null, "CADASTRO CONCLUÍDO", "CADASTRO", JOptionPane.INFORMATION_MESSAGE);

                // Fechando a janela de cadastro e abrindo a tela inicial
                dispose();
                TelaInicial inicio = new TelaInicial();
                inicio.setVisible(true);
            }
        });
        // Botão VOLTAR
        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setPreferredSize(new Dimension(150, 40));
        botaoVoltar.setBackground(Color.decode("#55AA98"));
        botaoVoltar.setForeground(Color.WHITE);
        gbc.gridy = 8; // Ajuste o número de acordo com a posição desejada
        contentPane.add(botaoVoltar, gbc);
        // Evento do botão Voltar
        botaoVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Fecha a janela de cadastro e abre a tela inicial
                dispose();
                TelaInicial telaInicial = new TelaInicial();
                telaInicial.setVisible(true);
            }
        });
        setVisible(true);
    }

}
