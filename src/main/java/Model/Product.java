package Model;

/**
 * The Product Class holds the specific fields for a Product: id, name, price and stock.
 */
public class Product {
    private int id;
    private String name;
    private int price;
    private int stock;

    public Product() {
    }

    public Product( String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    public void setId(int idProduct) {this.id = idProduct;}
    public void setName(String name) {this.name = name;}
    public void setPrice(int price) {this.price = price;}
    public void setStock(int stock) {this.stock = stock;}

    public int getId() {return id;}

    public String getName() {return name;}
    public int getPrice() {return price;}
    public int getStock() {return stock;}
}
