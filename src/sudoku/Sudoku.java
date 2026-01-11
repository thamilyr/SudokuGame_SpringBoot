package sudoku;

import java.util.Scanner;

public class Sudoku {

    private static final int SIZE = 9;
    private static int[][] fixedBoard = new int[SIZE][SIZE];  // números fixos
    private static int[][] board = new int[SIZE][SIZE];       // números do jogador
    private static boolean started = false;                   // status do jogo

    public static void main(String[] args) {
        inicializarFixos(args);

        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            mostrarMenu();
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    iniciarNovoJogo();
                    break;
                case 2:
                    colocarNumero(sc);
                    break;
                case 3:
                    removerNumero(sc);
                    break;
                case 4:
                    visualizarJogo();
                    break;
                case 5:
                    verificarStatus();
                    break;
                case 6:
                    limpar();
                    break;
                case 7:
                    finalizarJogo();
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        sc.close();
    }

    // ============================
    //        FUNÇÕES
    // ============================

    private static void inicializarFixos(String[] args) {
        for (String arg : args) {
            try {
                String[] partes = arg.split(",");
                int l = Integer.parseInt(partes[0]);
                int c = Integer.parseInt(partes[1]);
                int v = Integer.parseInt(partes[2]);
                fixedBoard[l][c] = v;
            } catch (Exception e) {
                System.out.println("Erro ao ler argumento: " + arg);
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENU DO SUDOKU =====");
        System.out.println("1. Iniciar novo jogo");
        System.out.println("2. Colocar número");
        System.out.println("3. Remover número");
        System.out.println("4. Visualizar jogo");
        System.out.println("5. Verificar status");
        System.out.println("6. Limpar respostas");
        System.out.println("7. Finalizar jogo");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");
    }

    private static void iniciarNovoJogo() {
        started = true;
        // copia os números fixos para o tabuleiro de jogo
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = fixedBoard[i][j];

        System.out.println("Novo jogo iniciado!");
        visualizarJogo();
    }

    private static void colocarNumero(Scanner sc) {
        if (!started) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Linha (0-8): ");
        int l = sc.nextInt();
        System.out.print("Coluna (0-8): ");
        int c = sc.nextInt();

        if (fixedBoard[l][c] != 0) {
            System.out.println("Não pode alterar um número fixo!");
            return;
        }

        if (board[l][c] != 0) {
            System.out.println("Posição já preenchida!");
            return;
        }

        System.out.print("Número (1-9): ");
        int v = sc.nextInt();
        if (v < 1 || v > 9) {
            System.out.println("Número inválido!");
            return;
        }

        board[l][c] = v;
        System.out.println("Número inserido!");
    }

    private static void removerNumero(Scanner sc) {
        if (!started) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        System.out.print("Linha (0-8): ");
        int l = sc.nextInt();
        System.out.print("Coluna (0-8): ");
        int c = sc.nextInt();

        if (fixedBoard[l][c] != 0) {
            System.out.println("Não pode remover número fixo!");
            return;
        }

        if (board[l][c] == 0) {
            System.out.println("A posição já está vazia.");
            return;
        }

        board[l][c] = 0;
        System.out.println("Número removido!");
    }

    private static void visualizarJogo() {
        System.out.println("\n===== SUDOKU =====");
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0) System.out.println("-------------------------");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(board[i][j] == 0 ? ". " : board[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-------------------------");
    }

    private static boolean temErros() {
        // checa linhas
        for (int i = 0; i < SIZE; i++) {
            boolean[] usado = new boolean[10];
            for (int j = 0; j < SIZE; j++) {
                int v = board[i][j];
                if (v != 0) {
                    if (usado[v]) return true;
                    usado[v] = true;
                }
            }
        }

        // checa colunas
        for (int i = 0; i < SIZE; i++) {
            boolean[] usado = new boolean[10];
            for (int j = 0; j < SIZE; j++) {
                int v = board[j][i];
                if (v != 0) {
                    if (usado[v]) return true;
                    usado[v] = true;
                }
            }
        }

        // checa subquadros 3x3
        for (int bi = 0; bi < SIZE; bi += 3) {
            for (int bj = 0; bj < SIZE; bj += 3) {
                boolean[] usado = new boolean[10];
                for (int i = bi; i < bi + 3; i++) {
                    for (int j = bj; j < bj + 3; j++) {
                        int v = board[i][j];
                        if (v != 0) {
                            if (usado[v]) return true;
                            usado[v] = true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean estaCompleto() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == 0)
                    return false;
        return true;
    }

    private static void verificarStatus() {
        if (!started) {
            System.out.println("Status: NÃO INICIADO (sem erros)");
            return;
        }

        boolean completo = estaCompleto();
        boolean erros = temErros();

        if (completo && !erros) {
            System.out.println("Status: COMPLETO (sem erros)");
        } else if (completo && erros) {
            System.out.println("Status: COMPLETO (com erros)");
        } else if (!completo && !erros) {
            System.out.println("Status: INCOMPLETO (sem erros)");
        } else {
            System.out.println("Status: INCOMPLETO (com erros)");
        }
    }

    private static void limpar() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (fixedBoard[i][j] == 0)
                    board[i][j] = 0;

        System.out.println("Jogo limpo! Apenas números fixos permanecem.");
    }

    private static void finalizarJogo() {
        if (!started) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        if (estaCompleto() && !temErros()) {
            System.out.println("Parabéns! Jogo finalizado com sucesso!");
            System.exit(0);
        } else {
            System.out.println("O jogo ainda não está correto ou completo.");
        }
    }
}
