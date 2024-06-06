package iterface;

import connections.UserDao;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {
    private JTextField campoUsuario;
    public TelaInicial() {
        // Configurações da janela
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setResizable(false);

        // Container principal
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 2, 20, 20)); // Espaçamento entre os componentes

        // Painel esquerdo
        JPanel painelEsquerdo = new JPanel();
        painelEsquerdo.setBackground(Color.decode("#55AA98")); // Cor de fundo
        painelEsquerdo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Título "Bem-Vindo!"
        JLabel labelBemVindo = new JLabel("Bem-Vindo!");
        labelBemVindo.setFont(new Font("Times New Roman", Font.BOLD, 36));
        labelBemVindo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 90, 0);
        painelEsquerdo.add(labelBemVindo, gbc);

        // Subtítulo
        JLabel labelSubtitulo = new JLabel("Um livro é um sonho que você segura na mão");
        labelSubtitulo.setFont(new Font("Verdana", Font.ITALIC, 18));
        labelSubtitulo.setForeground(Color.WHITE);
        gbc.gridy = 1;
        painelEsquerdo.add(labelSubtitulo, gbc);

        // Botão "CADASTRAR"
        JButton botaoCadastrar = new JButton("CADASTRAR");
        botaoCadastrar.setPreferredSize(new Dimension(200, 40)); // Definindo a altura
        gbc.gridy = 2;
        botaoCadastrar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoCadastrar.setBackground(Color.decode("#55AA98"));
        botaoCadastrar.setForeground(Color.WHITE);
        painelEsquerdo.add(botaoCadastrar, gbc);
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                CadastroUsuario cadastroUsuario = new CadastroUsuario();
                cadastroUsuario.setVisible(true);
            }
        });

        // Painel direito
        JPanel painelDireito = new JPanel();
        painelDireito.setLayout(new GridBagLayout());
        GridBagConstraints gbcDireito = new GridBagConstraints();
        gbcDireito.anchor = GridBagConstraints.CENTER;
        gbcDireito.insets = new Insets(10, 10, 50, 10); // Espaçamento entre os componentes

        // Título "Login"
        JLabel labelLogin = new JLabel("Login");
        labelLogin.setFont(new Font("Times New Roman", Font.BOLD, 36));
        labelLogin.setForeground(Color.decode("#55AA98"));
        gbcDireito.gridx = 0;
        gbcDireito.gridy = 0;
        gbcDireito.gridwidth = 2;
        painelDireito.add(labelLogin, gbcDireito);

        // Campo "Usuário"
        JLabel labelEmail = new JLabel("Email: ");
        gbcDireito.gridy = 1;
        painelDireito.add(labelEmail, gbcDireito);
        campoUsuario = new JTextField(20);
        campoUsuario.setPreferredSize(new Dimension(200, 40)); // Definindo a altura
        gbcDireito.gridx = 1;
        gbcDireito.gridy = 1;
        painelDireito.add(campoUsuario, gbcDireito);


        // Campo "Senha"
        JLabel labelSenha = new JLabel("Senha: ");
        gbcDireito.gridx = 0;
        gbcDireito.gridy = 2;
        painelDireito.add(labelSenha, gbcDireito);
        JPasswordField campoSenha = new JPasswordField(20);
        campoSenha.setPreferredSize(new Dimension(200, 40)); // Definindo a altura
        gbcDireito.gridx = 1;
        gbcDireito.gridy = 2;
        painelDireito.add(campoSenha, gbcDireito);

        // Botão "Entrar"
        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.setPreferredSize(new Dimension(150, 40)); // Definindo a altura
        gbcDireito.gridx = 1;
        gbcDireito.gridy = 3;
        botaoEntrar.setFont(new Font("Arial", Font.BOLD, 16));
        botaoEntrar.setBackground(Color.decode("#55AA98"));
        botaoEntrar.setForeground(Color.BLACK);
        painelDireito.add(botaoEntrar, gbcDireito);

        botaoEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obtendo o email e senha digitados pelo usuário
                String email = campoUsuario.getText();
                String senha = new String(campoSenha.getPassword());

                // Verificando a autenticação no banco de dados
                UserDao userDao = new UserDao();
                int opc = userDao.autenticarUsuario(email, senha);
                if (opc == 1) {//usuario
                    dispose();
                    IndexHome indexHome = new IndexHome(email);
                    indexHome.setVisible(true);
                } else if (opc == 2) {//admin
                    dispose();
                    Funcionario funcionario = new Funcionario();
                    funcionario.setVisible(true);
                } else {
                    // Usuário ou senha incorretos
                    JOptionPane.showMessageDialog(null, "Email ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(painelEsquerdo);
        contentPane.add(painelDireito);
        setVisible(true);
    }
}
