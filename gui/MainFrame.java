package gui;

import java.awt.CardLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.swing.awt.*;
import java.util.ArrayList;
import java.util.List;

//user session control
//order history and profile view
public class MainFrame extends JFrame{
    private final ProductService prodectService;
    private final ShoppingCart shoppingCart;

    //cardlayout
    private JPanel contentContainer;
    private CardLayout cardLayout;

    private ProductPanel productPanel;
    private CardPanel cardPanel;
    private CheckoutPanel checkoutPanel;
    private JPanel orderHistoryPanel;
    private JPanel profilePanel;

    //header labels and buttons
    private JButton userProfileChip;
    private JButton loginBtn;
    private JButton registerBtn;
    private JButton logoutBtn;

    //navbar buttons
    private final List<JButton> navButtons = new ArrayList<>();
    private JButton navProductsBtn;
    private JButton navCartBtn;
    private JButton navCheckoutBtn;
    private JButton navOrdersBtn;
    private JButton navProfileBtn;
    private String activeCard = "PRODUCTS";

    //order history ui
    private JTable ordersTable;
    private DefaultTableModel orderTableModel;

    //profile ui
    private JTextField profileFullNameField;
    private JTextField profileUserNameField;
    private JTextField profileEmailField;
    private JTextField profilePhoneField;
    private JTextArea profileAddressArea;
    private JPasswordField profilePasswordField;
    private JLabel profileRoleLabel;


    public MainFrame(ProductService productService, ShoppingCart shoppingCart){
        this.prodectService = productService;
        this.shoppingCart = shoppingCart;
    }
}