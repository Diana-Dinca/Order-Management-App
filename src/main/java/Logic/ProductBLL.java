package Logic;

import DataAccess.ProductDAO;
import Model.Product;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class implements all CRUD methods for Products using DAO classes.
 */
public class ProductBLL {
    private ProductDAO productDAO;
    public ProductBLL(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    public Product findProductById(int id) {
        Product st = productDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }
    public List<Product> findAllProduct() {
        List<Product> st = productDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("The product table is empty");
        }
        return st;
    }
    public void deleteProductById(int id) {
        String st = productDAO.deleteById(id);
        if (st.equals("Delete could not be done!")) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
    }
    public Product insertProduct(Product product) {
        return productDAO.insert(product);
    }
    public Product updateProduct(Product product) {
        return productDAO.update(product);
    }

}
