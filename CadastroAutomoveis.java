import java.io.*;
import java.util.*;

public class CadastroAutomoveis {
    private static final String ARQUIVO = "automoveis.txt";
    private static ArrayList<Automovel> lista = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        carregarDoArquivo();
        int opcao;
        do {
            System.out.println("\nMenu:");
            System.out.println("1 - Incluir automóvel");
            System.out.println("2 - Remover automóvel");
            System.out.println("3 - Alterar dados de automóvel");
            System.out.println("4 - Consultar automóvel por placa");
            System.out.println("5 - Listar automóveis (ordenado)");
            System.out.println("6 - Salvar e sair");
            System.out.print("Opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> incluirAutomovel();
                case 2 -> removerAutomovel();
                case 3 -> alterarAutomovel();
                case 4 -> consultarAutomovel();
                case 5 -> listarAutomoveis();
                case 6 -> salvarNoArquivo();
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 6);
    }

    private static void incluirAutomovel() {
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        if (buscarPorPlaca(placa) != null) {
            System.out.println("Placa já cadastrada!");
            return;
        }
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Ano: ");
        int ano = Integer.parseInt(scanner.nextLine());
        System.out.print("Valor: ");
        double valor = Double.parseDouble(scanner.nextLine());

        lista.add(new Automovel(placa, modelo, marca, ano, valor));
        System.out.println("Automóvel incluído com sucesso.");
    }

    private static void removerAutomovel() {
        System.out.print("Placa do automóvel a remover: ");
        String placa = scanner.nextLine();
        Automovel a = buscarPorPlaca(placa);
        if (a != null) {
            lista.remove(a);
            System.out.println("Automóvel removido.");
        } else {
            System.out.println("Automóvel não encontrado.");
        }
    }

    private static void alterarAutomovel() {
        System.out.print("Placa do automóvel a alterar: ");
        String placa = scanner.nextLine();
        Automovel a = buscarPorPlaca(placa);
        if (a != null) {
            System.out.print("Novo modelo: ");
            a.setModelo(scanner.nextLine());
            System.out.print("Nova marca: ");
            a.setMarca(scanner.nextLine());
            System.out.print("Novo ano: ");
            a.setAno(Integer.parseInt(scanner.nextLine()));
            System.out.print("Novo valor: ");
            a.setValor(Double.parseDouble(scanner.nextLine()));
            System.out.println("Dados alterados com sucesso.");
        } else {
            System.out.println("Automóvel não encontrado.");
        }
    }

    private static void consultarAutomovel() {
        System.out.print("Placa do automóvel: ");
        String placa = scanner.nextLine();
        Automovel a = buscarPorPlaca(placa);
        if (a != null) {
            System.out.println(a);
        } else {
            System.out.println("Automóvel não encontrado.");
        }
    }

    private static void listarAutomoveis() {
        System.out.print("Ordenar por (placa/modelo/marca): ");
        String criterio = scanner.nextLine().toLowerCase();
        Comparator<Automovel> comparador = switch (criterio) {
            case "modelo" -> Comparator.comparing(Automovel::getModelo);
            case "marca" -> Comparator.comparing(Automovel::getMarca);
            default -> Comparator.comparing(Automovel::getPlaca);
        };
        lista.stream()
             .sorted(comparador)
             .forEach(System.out::println);
    }

    private static Automovel buscarPorPlaca(String placa) {
        for (Automovel a : lista) {
            if (a.getPlaca().equalsIgnoreCase(placa)) return a;
        }
        return null;
    }

    private static void carregarDoArquivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                lista.add(Automovel.fromCSV(linha));
            }
        } catch (IOException e) {
            System.out.println("Arquivo não encontrado. Um novo será criado ao salvar.");
        }
    }

    private static void salvarNoArquivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Automovel a : lista) {
                bw.write(a.toCSV());
                bw.newLine();
            }
            System.out.println("Dados salvos. Encerrando...");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
