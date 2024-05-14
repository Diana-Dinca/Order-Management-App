package Presentation;

import DataAccess.BillDAO;
import DataAccess.ClientDAO;
import DataAccess.OrderDAO;
import DataAccess.ProductDAO;
import Logic.BillBLL;
import Logic.ClientBLL;
import Logic.OrderBLL;
import Logic.ProductBLL;
import Model.Bill;
import Model.Client;
import Model.OrderItem;
import Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class implements a window for creating product orders. The user will be able to select an
 * existing product, select an existing client, and insert a desired quantity for the product to
 * create a valid order. In case there are not enough products, an under-stock message will be
 * displayed. After the order is finalized, the product stock is decremented.
 */

public class OrderOperations extends JFrame implements ActionListener {
    private JComboBox<Integer> productComboBox;
    private JComboBox<Integer> clientComboBox;
    private JTextField quantityField;
    private JButton createOrderButton;
    private JButton backButton;

    /**
     * The constructor creates the window and the default buttons for the operations.
     */
    public OrderOperations() {
        setTitle("Create Product Order");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel productLabel = new JLabel("Select Product:");
        ProductDAO productDAO = new ProductDAO();
        ProductBLL productBLL = new ProductBLL(productDAO);
        List<Product> productsList = productBLL.findAllProduct();
        Integer[] products = new Integer[productsList.size()];
        for (int i = 0; i < productsList.size(); i++) {
            products[i] = productsList.get(i).getId();
        }
        productComboBox = new JComboBox<>(products);

        JLabel clientLabel = new JLabel("Select Client:");
        ClientDAO clientDAO= new ClientDAO();
        ClientBLL clientBLL= new ClientBLL(clientDAO);
        List<Client> clientList = clientBLL.findAllClient();
        Integer[] clients= new Integer[clientList.size()];
        for (int i = 0; i < clientList.size(); i++) {
            clients[i] = clientList.get(i).getId();
        }
        clientComboBox = new JComboBox<>(clients);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        createOrderButton = new JButton("Create Order");
        createOrderButton.addActionListener(this);
        backButton = new JButton("Back");
        backButton.addActionListener(this);

        mainPanel.add(productLabel);
        mainPanel.add(productComboBox);
        mainPanel.add(clientLabel);
        mainPanel.add(clientComboBox);
        mainPanel.add(quantityLabel);
        mainPanel.add(quantityField);
        mainPanel.add(createOrderButton);
        mainPanel.add(backButton);

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
        if (e.getSource() == createOrderButton) {
            createOrder();
        }
        revalidate();
        repaint();
    }

    /**
     * This method creates the order for a specific Client with a specific quantity of a Product, that must be in stock.
     */
    private void createOrder() {
        try {
            int product = (int) productComboBox.getSelectedItem();
            int client = (int) clientComboBox.getSelectedItem();
            int quantity = 0;
            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(OrderOperations.this, "Quantity must be a positive integer.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(OrderOperations.this, "Quantity must be a valid integer.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ProductDAO productDAO = new ProductDAO();
            ProductBLL productBLL = new ProductBLL(productDAO);
            Product productObj = productBLL.findProductById(product);
            int availableStock = productObj.getStock();
            if (quantity > availableStock) {
                JOptionPane.showMessageDialog(OrderOperations.this, "Not enough products in stock.", "Under-stock", JOptionPane.ERROR_MESSAGE);
                return;
            }
            OrderDAO orderDAO = new OrderDAO();
            OrderBLL orderBLL = new OrderBLL(orderDAO);
            OrderItem orderItem = new OrderItem(client,product,quantity);
            orderBLL.insertOrder(orderItem);

            BillDAO billDAO= new BillDAO();
            BillBLL billBLL= new BillBLL(billDAO);
            int id= orderItem.getId();
            int total= quantity* productObj.getPrice();
            billBLL.insertBill(new Bill(0,id, LocalDateTime.now(),total));

            productObj.setStock(availableStock - quantity);
            productBLL.updateProduct(productObj);

            JOptionPane.showMessageDialog(OrderOperations.this, "Order created successfully!\n Bill generated!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(OrderOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OrderOperations();
            }
        });
    }
}
