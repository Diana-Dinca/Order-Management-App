import Connection.ConnectionFactory;
import DataAccess.ClientDAO;
import Logic.ClientBLL;
import Model.Client;
import Presentation.StartInterface;

import javax.swing.*;
import java.sql.*;

/**
 * Here I call for the StartInterface class to launch my application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartInterface::new);
    }
}
