package Model;

/**
 * The Order Class holds the specific fields for an Order: id, id of the Client, id of the Product
 * and the quantity of the product.
 */
public class OrderItem {
    private int id;
    private int idClient;
    private int idProduct;
    private int quantity;

    public OrderItem() {
    }

    public OrderItem(int idClient, int idProduct, int quantity) {
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public void setIdClient(int idClient) {this.idClient = idClient;}
    public void setIdProduct(int idProduct) {this.idProduct = idProduct;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public void setId(int idOrder) {this.id = idOrder;}
    public void setClient(int client) {this.idClient = client;}

    public int getId() {return id;}
    public int getClient() {return idClient;}
    public int getIdClient() {return idClient;}
    public int getIdProduct() {return idProduct;}
    public int getQuantity() {return quantity;}
}
