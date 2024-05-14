package Logic;

import DataAccess.OrderDAO;
import Model.OrderItem;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class implements all CRUD methods for Orders using DAO classes.
 */

public class OrderBLL {
    private OrderDAO orderDAO;
    public OrderBLL(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }
    public OrderItem findOrderById(int id) {
        OrderItem st = (OrderItem) orderDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return st;
    }
    public List<OrderItem> findAllOrder() {
        List<OrderItem> st = orderDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("The order table is empty");
        }
        return st;
    }
    public void deleteOrderById(int id) {
        String st = orderDAO.deleteById(id);
        if (st.equals("Delete could not be done!")) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
    }
    public OrderItem insertOrder(OrderItem orderItem) {
        return orderDAO.insert(orderItem);
    }
    public OrderItem updateOrder(OrderItem orderItem) {
        return orderDAO.update(orderItem);
    }
}
