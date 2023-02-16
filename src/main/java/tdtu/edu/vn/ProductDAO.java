package tdtu.edu.vn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements Repository<Product, Integer> {
    private final Connection con;
    static final String url = "jdbc:mysql://localhost:3306";
    static final String user = "root";
    static final String passwords = "admin";

    static Statement stmt;
    public ProductDAO() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, passwords);
        stmt = con.createStatement();

        // Create the ProductManagement database if it doesn't exist
        String createDB = "CREATE DATABASE IF NOT EXISTS ProductManagement";
        stmt.executeUpdate(createDB);

        // Use the ProductManagement database
        String useDB = "USE ProductManagement";
        stmt.executeUpdate(useDB);

        // Delete and recreate the Product table if it exists
        //String deleteTable = "DROP TABLE IF EXISTS Product";
        //stmt.executeUpdate(deleteTable);
        String createTable = "CREATE TABLE product (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description VARCHAR(255), price FLOAT)";
        stmt.executeUpdate(createTable);
        this.con = con;
    }

    @Override
    public Integer add(Product item) {
        String sql = "INSERT INTO Product (name, description, price) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setFloat(3, (float) item.getPrice());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while adding a product: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> readAll() {
        String sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                float price = rs.getFloat("price");
                products.add(new Product(id, name, description, price));
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while reading all products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public Product read(Integer id) {
        String sql = "SELECT * FROM Product WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                float price = rs.getFloat("price");
                return new Product(id, name, description, price);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while reading a product: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Product product) {
        boolean success = false;
        try {
            PreparedStatement statement = con.prepareStatement(
                    "UPDATE product SET name=?, description=?, price=? WHERE id=?");
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setFloat(3, (float) product.getPrice());
            statement.setInt(4, product.getId());
            int result = statement.executeUpdate();
            success = result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }


    @Override
    public boolean delete(Integer id) {
        boolean success = false;
        try {
            PreparedStatement statement = con.prepareStatement("DELETE FROM product WHERE id=?");
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            success = result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

}
