package tdtu.edu.vn;
import java.sql.*;
import java.util.Scanner;
import java.util.List;

public class Program {

    static Connection con;

    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);
            // Show the menu and get user input
            int userInput = -1;
            while (userInput != 0) {
                System.out.println("Product Management System");
                System.out.println("1. Read product list");
                System.out.println("2. Read product list by ID");
                System.out.println("3. Add a new product");
                System.out.println("4. Update a product");
                System.out.println("5. Delete a product");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                userInput = scan.nextInt();

                switch (userInput) {
                    case 1 -> {
                        ProductDAO productDAO = new ProductDAO();
                        List<Product> products = productDAO.readAll();
                        if (products.isEmpty()){
                            System.out.println("The list is empty, please add at least 1 product!");
                        } else {
                            System.out.println("ID" + " | " + "Name" + " | " + "Description" + " | " + "Price");
                            for (Product product : products) {
                                System.out.println(product.toString());
                            }
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter product ID: ");
                        ProductDAO productDAO2 = new ProductDAO();
                        int id_Read = scan.nextInt();
                        Product product = productDAO2.read(id_Read);
                        if (product == null) {
                            System.out.println("Product not found.");
                        } else {
                            System.out.println(product.toString());
                        }
                    }
                    case 3 -> {
                        scan.nextLine();
                        System.out.print("Enter product name: ");
                        String name = scan.nextLine();
                        System.out.print("Enter product description: ");
                        String description = scan.nextLine();
                        System.out.print("Enter product price: ");
                        float price = scan.nextFloat();
                        ProductDAO productDAO3 = new ProductDAO();
                        Product product3 = new Product(0, name, description, price);
                        Integer id_Add = productDAO3.add(product3);
                        if (id_Add != null) {
                            System.out.println("New product added with id: " + id_Add);
                        } else {
                            System.out.println("Failed to add new product, product with the same id already exists.");
                        }
                    }
                    case 4 -> {
                        System.out.print("Enter the product id to update: ");
                        int id = scan.nextInt();
                        ProductDAO productDAO_update = new ProductDAO();
                        Product product_update = productDAO_update.read(id);
                        if (product_update == null) {
                            System.out.println("Product not found");
                            break;
                        }
                        scan.nextLine();
                        System.out.print("Enter new name: ");
                        String name_update = scan.nextLine();
                        System.out.print("Enter new description: ");
                        String desc_update = scan.nextLine();
                        System.out.print("Enter new price: ");
                        double price_update = scan.nextDouble();
                        product_update.setName(name_update);
                        product_update.setDescription(desc_update);
                        product_update.setPrice(price_update);
                        if (productDAO_update.update(product_update)) {
                            System.out.println("Product updated successfully");
                        } else {
                            System.out.println("Product update failed");
                        }
                    }
                    case 5 -> {
                        System.out.print("Enter the product id to delete: ");
                        int id_del = scan.nextInt();
                        ProductDAO productDAO_del = new ProductDAO();
                        boolean isDeleted = productDAO_del.delete(id_del);
                        if (isDeleted) {
                            System.out.println("Product deleted successfully.");
                        } else {
                            System.out.println("Product deletion failed.");
                        }
                    }
                    case 6 -> System.exit(0);
                }
            }
    }
}
