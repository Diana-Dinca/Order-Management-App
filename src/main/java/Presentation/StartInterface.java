package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class allows the user to select which subjects he wants to manage: Clients, Products or Orders.
 */

public class StartInterface extends JFrame implements ActionListener {
    private JButton manageClient;
    private JButton manageProduct;
    private JButton manageOrder;

    /**
     * The constructor creates the window and the default buttons for the operations.
     */
    public StartInterface() {
        setTitle("Start page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        manageClient = new JButton("Manage Client");
        manageProduct = new JButton("Manage Product");
        manageOrder = new JButton("Manage Order");
        buttonPanel.add(Box.createHorizontalStrut(100));
        buttonPanel.add(manageClient);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(manageProduct);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(manageOrder);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        manageClient.addActionListener(this);
        manageProduct.addActionListener(this);
        manageOrder.addActionListener(this);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Each button redirects the user to a new window which holds the specific operations for each subject.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageClient) {
            dispose();
            SwingUtilities.invokeLater(ClientOperations::new);
        }
        if (e.getSource() == manageProduct) {
            dispose();
            SwingUtilities.invokeLater(ProductOperations::new);
        }
        if (e.getSource() == manageOrder) {
            dispose();
            SwingUtilities.invokeLater(OrderOperations::new);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartInterface::new);
    }
}



