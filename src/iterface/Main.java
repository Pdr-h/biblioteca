package iterface;

import javax.swing.*;

/*O programa consiste em emprestimos de livro, tendo o mesmo como a classe pai e livroCompleto como classe filha. O ususario comum ele pode apenas pegar o livro emprestado
e devolver depois. ja o funcionario, ele pode adicionar, editar e remover algum livro.
Como era muita informaçao pra por em linhas, um botao chamado Dados do livro foi criado com o intuito de exibir todos os dados do livro selecionado.

login Funcionario:
admin
Senha:
admin
 */
public class Main {
    public static void main(String[] args) {
    //chamada da tela inicial, inicializando todo o programa
        SwingUtilities.invokeLater(TelaInicial::new);
        }
    }