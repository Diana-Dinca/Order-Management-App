package Presentation;

import DataAccess.ClientDAO;
import DataAccess.ProductDAO;
import Logic.ClientBLL;
import Logic.ProductBLL;
import Model.Client;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * This class implements a window for product operations: add new product, edit product, delete product,
 * view all products in a table.
 */

public class ProductOperations extends JFrame implements ActionListener {
    private JButton addProductButton;
    private JButton editProductButton;
    private JButton deleteProductButton;
    private JButton viewProductsButton;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton addButton;

    private JTextField idField;
    private JButton confirmDeleteButton;

    private JButton confirmEditButton;

    private JPanel addPanel;
    private JPanel deletePanel;
    private JPanel editPanel;

    private JButton backButton;
    private JTable productsTable;
    private DefaultTableModel tableModel;

    /**
     * The constructor creates the window and the default buttons for the operations.
     */
    public ProductOperations() {
        setTitle("Product Operations");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        addProductButton = new JButton("Add Product");
        editProductButton = new JButton("Edit Product");
        deleteProductButton = new JButton("Delete Product");
        viewProductsButton = new JButton("View Products");
        backButton = new JButton("Back");

        addProductButton.addActionListener(this);
        editProductButton.addActionListener(this);
        deleteProductButton.addActionListener(this);
        viewProductsButton.addActionListener(this);
        backButton.addActionListener(this);

        buttonPanel.add(addProductButton);
        buttonPanel.add(editProductButton);
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(viewProductsButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel);
        setVisible(true);
    }

    /**
     * When pressed a specific button there must appear some specific fields and functionalities.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
            SwingUtilities.invokeLater(StartInterface::new);
        }
        if (e.getSource() == addProductButton) {
            addProductFields();
        }
        if (e.getSource() == editProductButton) {
            editProductFields();
        }
        if (e.getSource() == deleteProductButton) {
            deleteProducFields();
        }
        if (e.getSource() == viewProductsButton) {
            viewProducts();
        }
        revalidate();
        repaint();
    }

    /**
     * This method is called when the user desires to add a new Product. It uncovers 3 labels with 3 text fields,
     * where the user can write the necessary data for a Product (name, price, stock). The method checks if the input
     * is valid: the name must contain only letters, the price and the stock must contain only digits.
     */
    private void addProductFields() {
        if (deletePanel != null) {
            remove(deletePanel);
        } if (editPanel != null) {
            remove(editPanel);
        } if (productsTable != null) {
            remove(productsTable);
        }

        nameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        addButton = new JButton("Add");

        addPanel = new JPanel(new GridLayout(4, 2));
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Price:"));
        addPanel.add(priceField);
        addPanel.add(new JLabel("Stock:"));
        addPanel.add(stockField);
        addPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (!name.matches("[a-zA-Z\\s-]+")) {
                    JOptionPane.showMessageDialog(ProductOperations.this, "Name must contain only letters.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String priceText = priceField.getText();
                String stockText = stockField.getText();
                if (!priceText.matches("\\d+") || !stockText.matches("\\d+")) {
                    JOptionPane.showMessageDialog(ProductOperations.this, "Price and stock must contain only digits.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int price = Integer.parseInt(priceText);
                int stock = Integer.parseInt(stockText);
                ProductDAO productDAO = new ProductDAO();
                ProductBLL productBLL = new ProductBLL(productDAO);
                Product product = new Product(name, price, stock);
                productBLL.insertProduct(product);

                remove(addPanel);
                addPanel = null;
                revalidate();
                repaint();
            }
        });

        add(addPanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called when the user desires to delete a Product. It uncovers 1 label with 1 text field,
     * where the user can input the id of the Product, soon to be deleted. The method validates the input, the id
     * must be an int, and it must correspond to an existing Product.
     */
    private void deleteProducFields() {
        if (addPanel != null) {
            remove(addPanel);
        } if (editPanel != null) {
            remove(editPanel);
        } if (productsTable != null) {
            remove(productsTable);
        }
        idField = new JTextField();
        confirmDeleteButton = new JButton("Confirm");

        deletePanel = new JPanel(new GridLayout(2, 2));
        deletePanel.add(new JLabel("Product ID:"));
        deletePanel.add(idField);
        deletePanel.add(confirmDeleteButton);

        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());

                    ProductDAO productDAO = new ProductDAO();
                    ProductBLL productBLL = new ProductBLL(productDAO);
                    try {
                        Product product = productBLL.findProductById(id);

                        if (product != null) {
                            productBLL.deleteProductById(id);
                            JOptionPane.showMessageDialog(ProductOperations.this, "Deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ProductOperations.this, "The ID is not valid", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch(IndexOutOfBoundsException ex){
                        JOptionPane.showMessageDialog(ProductOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    remove(deletePanel);
                    deletePanel = null;
                    revalidate();
                    repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(deletePanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called when the user desires edit an existing Product. It uncovers 4 labels with 4 text fields,
     * where the user can write the necessary data for a Product (id, name, price, stock). The method checks the id
     * of the Product, and it replaces the existing data with the data provided in the other 3 text fields. All the
     * text fields are being checked after making an update.
     */
    private void editProductFields() {
        if (addPanel != null) {
            remove(addPanel);
        } if (deletePanel != null) {
            remove(deletePanel);
        } if (productsTable != null) {
            remove(productsTable);
        }

        idField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        confirmEditButton = new JButton("Confirm");

        editPanel = new JPanel(new GridLayout(5, 2));
        editPanel.add(new JLabel("Product ID:"));
        editPanel.add(idField);
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Stock:"));
        editPanel.add(stockField);
        editPanel.add(confirmEditButton);

        confirmEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                if (!name.matches("[a-zA-Z\\s-]+")) {
                    JOptionPane.showMessageDialog(ProductOperations.this, "Name must contain only letters.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String priceText = priceField.getText();
                String stockText = stockField.getText();
                if (!priceText.matches("\\d+") || !stockText.matches("\\d+")) {
                    JOptionPane.showMessageDialog(ProductOperations.this, "Price and stock must contain only digits.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int price = Integer.parseInt(priceText);
                int stock = Integer.parseInt(stockText);
                ProductDAO productDAO = new ProductDAO();
                ProductBLL productBLL = new ProductBLL(productDAO);
                Product product= new Product(name, price, stock);
                product.setId(id);
                try {
                    Product p = productBLL.findProductById(id);

                    if (p != null) {
                        productBLL.updateProduct(product);
                        JOptionPane.showMessageDialog(ProductOperations.this, "Updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ProductOperations.this, "The ID is not valid", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch(IndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(ProductOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                remove(editPanel);
                editPanel = null;
                revalidate();
                repaint();
            }
        });
        add(editPanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called when the user desires to view all existing Products. It uncovers a table containing
     * all Products and all their fields: id, name, price, stock.
     */
    private void viewProducts() {
        if (addPanel != null) {
            remove(addPanel);
        } if (deletePanel != null) {
            remove(deletePanel);
        } if (editPanel != null) {
            remove(editPanel);
        }
        ProductDAO productDAO = new ProductDAO();
        ProductBLL productBLL = new ProductBLL(productDAO);
        List<Product> products = productBLL.findAllProduct();
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products found.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFrame frame= new JFrame("Products");
        productsTable = new JTable();
        frame.setSize(500,400);
        frame.setVisible(true);

        Field[] fields = Product.class.getDeclaredFields();
        DefaultTableModel model = new DefaultTableModel();
        for (Field field : fields) {
            model.addColumn(field.getName());
        }
        for (Product p : products) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    row[i] = fields[i].get(p);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            model.addRow(row);
        }
        frame.add(new JScrollPane(productsTable));
        revalidate();
        repaint();
        productsTable.setModel(model);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductOperations::new);
    }
}
