package iterface;

import javax.swing.*;

public class LogoutAction {

    public LogoutAction(JFrame parentFrame) {
        Object[] options = {"LOGIN", "SAIR"};
        int resposta = JOptionPane.showOptionDialog(parentFrame,
                "Deseja ir para a Tela de Login ou Fechar a Aplicação?",
                "Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (resposta == JOptionPane.YES_OPTION) {
            // Fecha a janela principal
            parentFrame.dispose();
            // Abre a tela de login
            TelaInicial telaInicial = new TelaInicial();
            telaInicial.setVisible(true);
        } else if (resposta == JOptionPane.NO_OPTION) {
            // Fechar a aplicação
            System.exit(0);
        }
    }
}
