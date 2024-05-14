package Model;

/**
 * The Client Class holds the specific fields for a Client: id, name, email and phone.
 */
public class Client {
    private int id;
    private String name;
    private String email;
    private String phone;

    public Client() {
    }

    public Client(String name, String email, String phone) {
        //this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}

    public int getId() {return id;}

    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
}
