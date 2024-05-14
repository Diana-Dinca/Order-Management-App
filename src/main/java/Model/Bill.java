package Model;

import java.time.LocalDateTime;
/**
 * The Bill Record Class holds the specific fields for a Bill: id, id of the Order, time of the order and total amount of money.
 */
public record Bill(int id, int idOrder, LocalDateTime timestamp, double totalAmount) {
    public Bill(int id, int idOrder, LocalDateTime timestamp, double totalAmount) {
        this.id = id;
        this.idOrder = idOrder;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
    }
}
