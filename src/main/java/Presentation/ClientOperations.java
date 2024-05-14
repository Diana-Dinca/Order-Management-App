package Presentation;

import DataAccess.ClientDAO;
import Logic.ClientBLL;
import Model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * This class implements a window for client operations: add new client, edit client, delete client,
 * view all clients in a table.
 */
public class ClientOperations extends JFrame implements ActionListener {
    private JButton addClientButton;
    private JButton editClientButton;
    private JButton deleteClientButton;
    private JButton viewClientsButton;
    private JButton backButton;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton addButton;

    private JTextField idField;
    private JButton confirmDeleteButton;

    private JButton confirmEditButton;

    private JPanel addPanel;
    private JPanel deletePanel;
    private JPanel editPanel;

    private JTable clientTable;

    /**
     * The constructor creates the window and the default buttons for the operations.
     */
    public ClientOperations() {
        setTitle("Client Operations");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        addClientButton = new JButton("Add Client");
        editClientButton = new JButton("Edit Client");
        deleteClientButton = new JButton("Delete Client");
        viewClientsButton = new JButton("View Clients");
        backButton = new JButton("Back");

        addClientButton.addActionListener(this);
        editClientButton.addActionListener(this);
        deleteClientButton.addActionListener(this);
        viewClientsButton.addActionListener(this);
        backButton.addActionListener(this);

        buttonPanel.add(addClientButton);
        buttonPanel.add(editClientButton);
        buttonPanel.add(deleteClientButton);
        buttonPanel.add(viewClientsButton);
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
        if (e.getSource() == addClientButton) {
            addClientFields();
        }
        if (e.getSource() == editClientButton) {
            editClientFields();
        }
        if (e.getSource() == deleteClientButton) {
            deleteClientFields();
        }
        if (e.getSource() == viewClientsButton) {
            viewClients();
        }

        revalidate();
        repaint();
    }

    /**
     * This method is called when the user desires to add a new Client. It uncovers 3 labels with 3 text fields,
     * where the user can write the necessary data for a Client (name, email, phone). The method checks if the input
     * is valid: the name must contain only letters and the phone must contain only 10 digits.
     */
    private void addClientFields() {
        if (deletePanel != null) {
            remove(deletePanel);
        } if (editPanel != null) {
            remove(editPanel);
        } if (clientTable != null) {
            remove(clientTable);
        }

        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        addButton = new JButton("Add");

        addPanel = new JPanel(new GridLayout(4, 2));
        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Email:"));
        addPanel.add(emailField);
        addPanel.add(new JLabel("Phone:"));
        addPanel.add(phoneField);
        addPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                if (!name.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(ClientOperations.this, "Name must contain only letters.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(ClientOperations.this, "Phone number must contain exactly 10 digits.", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ClientDAO clientDAO= new ClientDAO();
                ClientBLL clientBLL= new ClientBLL(clientDAO);
                Client client= new Client(name, email, phone);
                clientBLL.insertClient(client);

                remove(addPanel);
                addPanel = null;
                revalidate();
                repaint();
            }
        });

        add(addPanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called when the user desires to delete a Client. It uncovers 1 label with 1 text field,
     * where the user can input the id of the Client, soon to be deleted. The method validates the input, the id
     * must be an int, and it must correspond to an existing Client.
     */
    private void deleteClientFields() {
        if (addPanel != null) {
            remove(addPanel);
        } if (editPanel != null) {
            remove(editPanel);
        } if (clientTable != null) {
            remove(clientTable);
        }

        idField = new JTextField();
        confirmDeleteButton = new JButton("Confirm");

        deletePanel = new JPanel(new GridLayout(2, 2));
        deletePanel.add(new JLabel("Client ID:"));
        deletePanel.add(idField);
        deletePanel.add(confirmDeleteButton);

        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());

                    ClientDAO clientDAO = new ClientDAO();
                    ClientBLL clientBLL = new ClientBLL(clientDAO);
                    try {
                        Client client = clientBLL.findClientById(id);

                        if (client != null) {
                            clientBLL.deleteClientById(id);
                            JOptionPane.showMessageDialog(ClientOperations.this, "Deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ClientOperations.this, "The ID is not valid", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch(IndexOutOfBoundsException ex){
                        JOptionPane.showMessageDialog(ClientOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    remove(deletePanel);
                    deletePanel = null;
                    revalidate();
                    repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ClientOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(deletePanel, BorderLayout.SOUTH);
    }

    /**
     * This method is called when the user desires edit an existing Client. It uncovers 4 labels with 4 text fields,
     * where the user can write the necessary data for a Client (id, name, email, phone). The method checks the id
     * of the Client, and it replaces the existing data with the data provided in the other 3 text fields. All the
     * text fields are being checked after making an update.
     */
    private void editClientFields() {
        if (addPanel != null) {
            remove(addPanel);
        } if (deletePanel != null) {
            remove(deletePanel);
        } if (clientTable != null) {
            remove(clientTable);
        }

        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        confirmEditButton = new JButton("Confirm");

        editPanel = new JPanel(new GridLayout(5, 2));
        editPanel.add(new JLabel("Client ID:"));
        editPanel.add(idField);
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Email:"));
        editPanel.add(emailField);
        editPanel.add(new JLabel("Phone:"));
        editPanel.add(phoneField);
        editPanel.add(confirmEditButton);

        confirmEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (!name.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(ClientOperations.this, "Name must contain only letters.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(ClientOperations.this, "Phone number must contain exactly 10 digits.", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ClientDAO clientDAO = new ClientDAO();
                ClientBLL clientBLL = new ClientBLL(clientDAO);
                Client client= new Client(name, email, phone);
                client.setId(id);
                try {
                    Client c = clientBLL.findClientById(id);

                    if (c != null) {
                        clientBLL.updateClient(client);
                        JOptionPane.showMessageDialog(ClientOperations.this, "Updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(ClientOperations.this, "The ID is not valid", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch(IndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(ClientOperations.this, "Please enter a valid client ID.", "Error", JOptionPane.ERROR_MESSAGE);
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
     * This method is called when the user desires to view all existing Clients. It uncovers a table containing
     * all Clients and all their fields: id, name, email, phone.
     */
    private void viewClients() {
        if (addPanel != null) {
        remove(addPanel);
        } if (deletePanel != null) {
            remove(deletePanel);
        } if (editPanel != null) {
            remove(editPanel);
        }
        ClientDAO clientDAO = new ClientDAO();
        ClientBLL clientBLL = new ClientBLL(clientDAO);
        List<Client> clients = clientBLL.findAllClient();
        if (clients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No clients found.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFrame frame = new JFrame("Clients");
        clientTable = new JTable();
        frame.setSize(500,400);
        frame.setVisible(true);

        Field[] fields = Client.class.getDeclaredFields();
        DefaultTableModel model = new DefaultTableModel();
        for (Field field : fields) {
            model.addColumn(field.getName());
        }
        for (Client c : clients) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    row[i] = fields[i].get(c);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            model.addRow(row);
        }
        frame.add(new JScrollPane(clientTable));
        revalidate();
        repaint();
        clientTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientOperations::new);
    }
}
