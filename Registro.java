package recuperacaohashing;

import java.util.Random;
import java.util.Scanner;

public class Registro {

    public int[] tabela;
    public int[] codigos;
    public int tamanhoTabela;
    public int chave;
    public int posicao;

    public int tentativas = 0;
    public int contadorColisoes = 0;
    public int contadorComparacoes = 0;

    public Registro(int quantidadeElementos) {
        this.tamanhoTabela = quantidadeElementos;
        this.tabela = new int[quantidadeElementos];
        this.codigos = new int[quantidadeElementos];
    }

    public static void main(String[] args) {
        long seed = 123;
        Random random = new Random(seed);
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "\t>>> Digite qual serÃ¡ o TAMANHO DO VETOR: \n[20 mil, 100 mil, 500 mil, 1 milhÃ£o e 5 milhÃµes]");
        int quantidadeElementos = scanner.nextInt();
        Registro registro = new Registro(quantidadeElementos);  // criando uma instancia de elemento com a quantidade definida

        while (true) {
            System.out.println("TABELA ATUALMENTE: ");
            for (int i : registro.tabela) {
                System.out.print(i + ", "); // printando a tabela
            }

            System.out.print("\n");
            System.out.print("\n>>> QUANTIDADE DE COLISÃ•ES : " + registro.contadorColisoes);
            System.out.print("\n>>> NÃšMERO DE COMPARACOES: " + registro.contadorComparacoes);
            System.out.println("\n\n>>> 1 - Inserir elementos em CONJUNTO DE DADOS");
            System.out.println("\n>>> 2 - Buscar elemento em CONJUNTO DE DADOS");
            System.out.println("\n>>> 3 - Sair\n\nEscolha uma opcao: ");
            int respostaUsuario = scanner.nextInt();

            if (respostaUsuario < 1 || respostaUsuario > 3) {
                System.out.println("Digite uma resposta vÃ¡lida!");
            }

            switch (respostaUsuario) {
                case 1:
                    System.out.println("\nQuantos elementos deverao ser inseridos: ");
                    int quantosElementosInserir = scanner.nextInt();
                    registro.codigos = gerarCodigos9Digitos(quantosElementosInserir, random);

                    long tempoAntesI = System.currentTimeMillis(); // tempo antes de usar o metodo
    
                        for (int j : registro.codigos) {
                            registro.insere_hashing(j, registro.tabela, quantosElementosInserir);
                            // inserindo as chaves ja com o hash na tabela
                        }
                    long tempoDepoisI = System.currentTimeMillis(); // tempo apos utilizar o metodo

                    System.out.println(">>> MÃ©dia de tempo em milissegundos para executar a funcao: \n\t"
                            + (tempoDepoisI - tempoAntesI) + "ms");
                    break;
                case 2:
                    System.out.println("\nDigite o elemento que deseja buscar: ");
                    int qualElementoBuscar = scanner.nextInt();
                    long tempoAntesB = System.currentTimeMillis(); // tempo antes de usar o metodo
                    int posicao = registro.busca_hashing(qualElementoBuscar);

                    long tempoDepoisB = System.currentTimeMillis(); // tempo apos utilizar o metodo
                    System.out.println("\n\n>>> MÃ©dia de tempo em milissegundos para executar a funcao: \n\t"
                            + (tempoDepoisB - tempoAntesB) + "ms"); // calculando a media de tempo

                    if (posicao == -1) {
                        System.out.println("Essa posicao esta vazia!");
                    } else {
                        System.out.println("O elemento " + qualElementoBuscar + " estÃ¡ na posicao " + posicao);
                    }
                    break;
                case 3:
                    System.out.println("\tSaindo...");
                    System.exit(0);
            }
        }
    }

    public static int[] gerarCodigos9Digitos(int quantidadeCodigos, Random random) {
        int[] numeros = new int[quantidadeCodigos];
        for (int i = 0; i < quantidadeCodigos; i++) {
            numeros[i] = random.nextInt(900000000) + 100000000 + 1; // todos que tiverem valor 0 estao vazios
        }   // gerando codigos de 9 digitos, com valores aleatorios 
        return numeros;
    }

    public int busca_hashing(int chave) {
        // mudar quando for outras variacoes
        //int i = hashDIVISAO(chave);
        int i = hashMULTIPLICACAO(chave);
        //int i = hashDOBRAMENTO(chave);

        while (tabela[i] != chave && tabela[i] != 0) {
            i = rehashing(i);
            contadorComparacoes++;
            // enquanto a posicao nao for a chave e nem estiver fazia, faz rehashing
        }
        
        if (tabela[i] == 0) {   // se a posicao estiver vazia
            System.out.print("busca_hashing: posicao vazia");
            return 0;
        } else {    // se nessa posicao existe a chave que estamos procurando, retornara a chave
            contadorComparacoes++;
            return i;
        }
    }

    public void insere_hashing(int chave, int[] tabela, int quantosElementosInserir) {
        // mudar quando for outras variacoes
        //int i = hashDIVISAO(chave);            // posicao
        int i = hashMULTIPLICACAO(chave);
        //int i = hashDOBRAMENTO(chave);

        while (tabela[i] != chave && tabela[i] != 0) {
            // enquanto a posicao na tabela nao for a chave e nem estiver vazia, sera feito rehashing
            i = rehashing(i);   
            contadorColisoes++;
            contadorComparacoes++;
            //System.out.println(contadorComparacoes);            
        }

        if (tabela[i] == 0) {   // se a posicao estiver vazia, ela recebera a chave
            tabela[i] = chave;
            contadorComparacoes++;
        }
        //tabela[7] = 726010966;
        //tabela[8] = 26010966;
    }
    
    public int rehashing(int posicao) {
        int proximaPosicao = (posicao + 1) % tamanhoTabela;
        // funcao de rehashing, armazenamento interno
        return proximaPosicao;  
    }

    public int hashDIVISAO(int chave) {
        posicao = chave % tamanhoTabela;
        return posicao;
    }

    public int hashMULTIPLICACAO(int chave) {
        float floatposicao = (chave * 0.63f % 1) * tamanhoTabela;   // divindo por um numero float entre 0 e 1, e em seguida obtendo resultado
        posicao = (int) floatposicao;                               // e transformando em int
        return posicao;
    }

    public int hashDOBRAMENTO(int chave) {
        int parte1 = chave / 3; // metade 1
        int parte2 = (chave - parte1); // metade 2, dividindo para as partes serem diferentes
        int parte3 = chave - parte2; // metade 3, como tem 9 posicoes

        int somaPartes = ((parte1 + parte2 + parte3) / 100) % tamanhoTabela;
        // para ser um numero que existe dentro do tamanho da tabela
        posicao = somaPartes;

        return posicao;
    }
}
