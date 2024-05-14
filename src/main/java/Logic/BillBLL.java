package Logic;

import DataAccess.BillDAO;
import DataAccess.ClientDAO;
import Model.Bill;
import Model.Client;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class implements insert methods for Bills using DAO classes.
 */
public class BillBLL {
    private BillDAO billDAO;
    public BillBLL(BillDAO billDAO) {
        this.billDAO = billDAO;
    }
    public Bill insertBill(Bill bill) {return billDAO.insert(bill);}
    public List<Bill> findAllBill() {
        List<Bill> st = billDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("The Bill table is empty");
        }
        return st;
    }

}
