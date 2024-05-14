package Logic;

import DataAccess.ClientDAO;
import Model.Client;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class implements all CRUD methods for Clients using DAO classes.
 */
public class ClientBLL {
    private ClientDAO clientDAO;
    public ClientBLL(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }
    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }
    public List<Client> findAllClient() {
        List<Client> st = clientDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("The client table is empty");
        }
        return st;
    }
    public void deleteClientById(int id) {
        String st = clientDAO.deleteById(id);
        if (st.equals("Delete could not be done!")) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
    }
    public Client insertClient(Client client) {
        return clientDAO.insert(client);
    }
    public Client updateClient(Client client) {
        return clientDAO.update(client);
    }
}
