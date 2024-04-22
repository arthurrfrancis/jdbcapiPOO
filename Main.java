import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:sqlite:VENDA_ELETRONICOS";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);

            // Criar tabela "produtos" se não existir
            criarTabelaProdutos(conn);

            // Inserção de um novo produto na tabela
            System.out.println("Inserindo produto na tabela...");
            inserirProduto(conn, new Produto("Smartphone", "Samsung", "Galaxy S20", 1500.0));
            System.out.println("Produto inserido com sucesso!");

            // Atualização de um produto na tabela
            System.out.println("Atualizando produto na tabela...");
            atualizarPrecoProduto(conn, "Smartphone", 1600.0);
            System.out.println("Produto atualizado com sucesso!");

            // Exclusão de um produto na tabela
            System.out.println("Excluindo produto da tabela...");
            excluirProduto(conn, "Smartphon");
            System.out.println("Produto excluído com sucesso!");

            // Consulta dos produtos na tabela
            System.out.println("Consultando produtos na tabela...");
            consultarProdutos(conn);

        } catch (SQLException se) {
            // Tratamento de erros do JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Tratamento de outros erros
            e.printStackTrace();
        } finally {
            // Fechando os recursos em um bloco finally
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                // Nada a fazer
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Fim do programa");
    }

    // Método para criar a tabela "produtos" se não existir
    private static void criarTabelaProdutos(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                    "id_produto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome_produto TEXT," +
                    "marca TEXT," +
                    "modelo TEXT," +
                    "preco REAL)";
            stmt.executeUpdate(sql);
        }
    }

    // Método para inserir um novo produto na tabela
    private static void inserirProduto(Connection conn, Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome_produto, marca, modelo, preco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNomeProduto());
            stmt.setString(2, produto.getMarca());
            stmt.setString(3, produto.getModelo());
            stmt.setDouble(4, produto.getPreco());
            stmt.executeUpdate();
        }
    }

    // Método para atualizar o preço de um produto na tabela
    private static void atualizarPrecoProduto(Connection conn, String nomeProduto, double novoPreco) throws SQLException {
        String sql = "UPDATE produtos SET preco=? WHERE nome_produto=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novoPreco);
            stmt.setString(2, nomeProduto);
            stmt.executeUpdate();
        }
    }

    // Método para excluir um produto da tabela
    private static void excluirProduto(Connection conn, String nomeProduto) throws SQLException {
        String sql = "DELETE FROM produtos WHERE nome_produto=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeProduto);
            stmt.executeUpdate();
        }
    }

    // Método para consultar os produtos na tabela e imprimir os registros
    private static void consultarProdutos(Connection conn) throws SQLException {
        String sql = "SELECT * FROM produtos";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_produto");
                String nomeProduto = rs.getString("nome_produto");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                double preco = rs.getDouble("preco");

                System.out.println("ID: " + id);
                System.out.println("Nome do Produto: " + nomeProduto);
                System.out.println("Marca: " + marca);
                System.out.println("Modelo: " + modelo);
                System.out.println("Preço: $" + preco);
                System.out.println();
            }
        }
    }
}

// Classe Produto
class Produto {
    private String nomeProduto;
    private String marca;
    private String modelo;
    private double preco;

    public Produto(String nomeProduto, String marca, String modelo, double preco) {
        this.nomeProduto = nomeProduto;
        this.marca = marca;
        this.modelo = modelo;
        this.preco = preco;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getPreco() {
        return preco;
    }
}