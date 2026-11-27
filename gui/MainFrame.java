package gui;

import model.CartItem;
import model.Order;
import model.User;
import service.ProductService;
import service.ShoppingCart;

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
        setTitle("🛒 LINeFla");
        setSize(1140, 760);
        setMinimumSize(new Dimension(960, 640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        updateUserUI();
        updateCartBadge();
        highlightActiveNav("PRODUCTS");
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // TOP: Header & Navigation
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(UITheme.NAV_DARK);

        // Top Header Bar
        JPanel authBar = new JPanel(new BorderLayout());
        authBar.setOpaque(false);
        authBar.setBorder(new EmptyBorder(12, 24, 12, 24));

        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);

        JLabel brandTitle = new JLabel("🛒 NEXUS STORE");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandTitle.setForeground(Color.WHITE);

        JLabel brandSub = new JLabel("Enterprise E-Commerce & Retail Management");
        brandSub.setFont(UITheme.FONT_SMALL);
        brandSub.setForeground(new Color(148, 163, 184)); // Slate 400

        brandPanel.add(brandTitle);
        brandPanel.add(brandSub);
        authBar.add(brandPanel, BorderLayout.WEST);

        // Right side: User profile button and actions
        JPanel authRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        authRight.setOpaque(false);

        userProfileChip = new JButton("Guest User");
        userProfileChip.setFont(UITheme.FONT_BOLD);
        userProfileChip.setForeground(new Color(241, 245, 249));
        userProfileChip.setBackground(new Color(51, 65, 85));
        userProfileChip.setFocusPainted(false);
        userProfileChip.setBorderPainted(false);
        userProfileChip.setOpaque(true);
        userProfileChip.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userProfileChip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        userProfileChip.setToolTipText("Click to view and edit your profile");
        userProfileChip.addActionListener(e -> {
            if (productService.isLoggedIn()) {
                showPanel("PROFILE");
            } else {
                promptLogin();
            }
        });

        Color chipBg = new Color(51, 65, 85);
        Color chipHover = new Color(71, 85, 105);
        userProfileChip.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                userProfileChip.setBackground(chipHover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                userProfileChip.setBackground(chipBg);
            }
        });
        authRight.add(userProfileChip);

        loginBtn = UITheme.createButton("Sign In", UITheme.PRIMARY, Color.WHITE);
        loginBtn.addActionListener(e -> promptLogin());

        registerBtn = UITheme.createButton("Register", UITheme.SUCCESS, Color.WHITE);
        registerBtn.addActionListener(e -> promptRegister());

        logoutBtn = UITheme.createButton("Logout", UITheme.DANGER, Color.WHITE);
        logoutBtn.addActionListener(e -> handleLogout());

        authRight.add(loginBtn);
        authRight.add(registerBtn);
        authRight.add(logoutBtn);
        authBar.add(authRight, BorderLayout.EAST);

        topContainer.add(authBar, BorderLayout.NORTH);

        // Lower Bar: Navigation Tabs
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        navBar.setBackground(UITheme.NAV_BAR);
        navBar.setBorder(new EmptyBorder(0, 18, 6, 18));

        navProductsBtn = createNavButton("🛍️ Product Catalog", "PRODUCTS");
        navCartBtn = createNavButton("🛒 Cart (0)", "CART");
        navCheckoutBtn = createNavButton("💳 Checkout", "CHECKOUT");
        navOrdersBtn = createNavButton("📦 Order History", "ORDERS");
        navProfileBtn = createNavButton("👤 User Profile", "PROFILE");

        navBar.add(navProductsBtn);
        navBar.add(navCartBtn);
        navBar.add(navCheckoutBtn);
        navBar.add(navOrdersBtn);
        navBar.add(navProfileBtn);

        topContainer.add(navBar, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        //CardLayout
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);

        productPanel = new ProductPanel(productService, shoppingCart, this);
        cartPanel = new CartPanel(productService, shoppingCart, this);
        checkoutPanel = new CheckoutPanel(productService, shoppingCart, this);
        orderHistoryPanel = createOrderHistoryPanel();
        profilePanel = createProfilePanel();

        contentContainer.add(productPanel, "PRODUCTS");
        contentContainer.add(cartPanel, "CART");
        contentContainer.add(checkoutPanel, "CHECKOUT");
        contentContainer.add(orderHistoryPanel, "ORDERS");
        contentContainer.add(profilePanel, "PROFILE");

        add(contentContainer, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BOLD);
        btn.setForeground(new Color(226, 232, 240));
        btn.setBackground(UITheme.NAV_HOVER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> showPanel(cardName));
        navButtons.add(btn);
        return btn;
    }

    private void highlightActiveNav(String cardName) {
        this.activeCard = cardName;
        for (JButton btn : navButtons) {
            btn.setBackground(UITheme.NAV_HOVER);
            btn.setForeground(new Color(226, 232, 240));
        }

        JButton target = switch (cardName) {
            case "PRODUCTS" -> navProductsBtn;
            case "CART" -> navCartBtn;
            case "CHECKOUT" -> navCheckoutBtn;
            case "ORDERS" -> navOrdersBtn;
            case "PROFILE" -> navProfileBtn;
            default -> null;
        };

        if (target != null) {
            target.setBackground(UITheme.PRIMARY);
            target.setForeground(Color.WHITE);
        }
    }

    public void showPanel(String cardName) {
        highlightActiveNav(cardName);
        if ("CART".equals(cardName)) {
            cartPanel.refreshCartTable();
        } else if ("CHECKOUT".equals(cardName)) {
            checkoutPanel.refreshCheckoutData();
        } else if ("ORDERS".equals(cardName)) {
            refreshOrderHistory();
        } else if ("PROFILE".equals(cardName)) {
            refreshProfileView();
        } else if ("PRODUCTS".equals(cardName)) {
            productPanel.refreshProductTable();
        }
        cardLayout.show(contentContainer, cardName);
    }

    public void updateCartBadge() {
        int count = shoppingCart.getTotalItemCount();
        navCartBtn.setText("🛒 Cart (" + count + ")");
    }

    public void refreshProductView() {
        productPanel.refreshProductTable();
        productPanel.refreshCategories();
    }

    //AUTHENTICATION & PROFILE UI

    public void updateUserUI() {
        User user = productService.getCurrentUser();
        if (user != null) {
            String roleBadge = user.isAdmin() ? "ADMIN" : "Customer";
            userProfileChip.setText(user.getFullName() + "  [" + roleBadge + "]  ↗");
            userProfileChip.setToolTipText("Logged in as " + user.getFullName() + " (" + roleBadge + "). Click to open Profile.");
            loginBtn.setVisible(false);
            registerBtn.setVisible(false);
            logoutBtn.setVisible(true);
            navOrdersBtn.setText(user.isAdmin() ? "📦 All Orders (Admin)" : "📦 Order History");
        } else {
            userProfileChip.setText("Guest (Click to Login)");
            userProfileChip.setToolTipText("Not signed in. Click to Login.");
            loginBtn.setVisible(true);
            registerBtn.setVisible(true);
            logoutBtn.setVisible(false);
            navOrdersBtn.setText("📦 Order History");
        }
        productPanel.updateAdminControls();
    }

    public void promptLogin() {
        JDialog dialog = new JDialog(this, "User / Admin Login", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(440, 320);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(UITheme.BG_MAIN);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 14));
        formPanel.setBorder(new EmptyBorder(18, 24, 10, 24));
        formPanel.setOpaque(false);

        JTextField usernameField = UITheme.createTextField(14);
        usernameField.setText("john_doe");
        JPasswordField passwordField = new JPasswordField("user123");
        passwordField.setFont(UITheme.FONT_REGULAR);
        passwordField.setPreferredSize(new Dimension(150, 32));

        JLabel uLbl = new JLabel("Username:");
        uLbl.setFont(UITheme.FONT_BOLD);
        JLabel pLbl = new JLabel("Password:");
        pLbl.setFont(UITheme.FONT_BOLD);

        formPanel.add(uLbl);
        formPanel.add(usernameField);
        formPanel.add(pLbl);
        formPanel.add(passwordField);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Preset quick-fill buttons for test convenience
        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        quickPanel.setOpaque(false);

        JButton adminPresetBtn = UITheme.createButton("🔑 Fill Admin", UITheme.BORDER_DARK, UITheme.TEXT_DARK);
        adminPresetBtn.setFont(UITheme.FONT_SMALL);
        adminPresetBtn.addActionListener(e -> {
            usernameField.setText("admin");
            passwordField.setText("admin123");
        });

        JButton userPresetBtn = UITheme.createButton("👤 Fill User", UITheme.BORDER_DARK, UITheme.TEXT_DARK);
        userPresetBtn.setFont(UITheme.FONT_SMALL);
        userPresetBtn.addActionListener(e -> {
            usernameField.setText("john_doe");
            passwordField.setText("user123");
        });

        quickPanel.add(adminPresetBtn);
        quickPanel.add(userPresetBtn);

        // Action Buttons
        JPanel btnPanel = new JPanel(new BorderLayout(8, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(5, 24, 18, 24));
        btnPanel.add(quickPanel, BorderLayout.NORTH);

        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actPanel.setOpaque(false);

        JButton cancelBtn = UITheme.createButton("Cancel", UITheme.BORDER_DARK, UITheme.TEXT_DARK);
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton submitBtn = UITheme.createButton("Sign In", UITheme.PRIMARY, Color.WHITE);
        submitBtn.addActionListener(e -> {
            String uname = usernameField.getText().trim();
            String pwd = new String(passwordField.getPassword());
            User u = productService.authenticate(uname, pwd);
            if (u != null) {
                dialog.dispose();
                updateUserUI();
                JOptionPane.showMessageDialog(this, "Welcome back, " + u.getFullName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                showPanel("PRODUCTS");
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        actPanel.add(cancelBtn);
        actPanel.add(submitBtn);
        btnPanel.add(actPanel, BorderLayout.SOUTH);

        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void promptRegister() {
        JDialog dialog = new JDialog(this, "Customer Registration", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(460, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(UITheme.BG_MAIN);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 8, 12));
        formPanel.setBorder(new EmptyBorder(18, 24, 10, 24));
        formPanel.setOpaque(false);

        JTextField userField = UITheme.createTextField(14);
        JPasswordField passField = new JPasswordField();
        passField.setFont(UITheme.FONT_REGULAR);
        JTextField nameField = UITheme.createTextField(14);
        JTextField emailField = UITheme.createTextField(14);
        JTextField phoneField = UITheme.createTextField(14);
        JTextField addressField = UITheme.createTextField(14);

        formPanel.add(new JLabel("Username:"));
        formPanel.add(userField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passField);
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email Address:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Delivery Address:"));
        formPanel.add(addressField);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        actPanel.setOpaque(false);

        JButton cancelBtn = UITheme.createButton("Cancel", UITheme.BORDER_DARK, UITheme.TEXT_DARK);
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton registerSubmitBtn = UITheme.createButton("Create Account", UITheme.SUCCESS, Color.WHITE);
        registerSubmitBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            String fn = nameField.getText().trim();
            String em = emailField.getText().trim();
            String ph = phoneField.getText().trim();
            String addr = addressField.getText().trim();

            if (u.isEmpty() || p.isEmpty() || fn.isEmpty() || em.isEmpty() || addr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User newUser = new User(0, u, p, fn, em, addr, ph, false);
            boolean created = productService.register(newUser);
            if (created) {
                dialog.dispose();
                updateUserUI();
                JOptionPane.showMessageDialog(this, "Account created successfully! Welcome, " + fn + ".", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                showPanel("PRODUCTS");
            } else {
                JOptionPane.showMessageDialog(dialog, "Username already exists. Please choose another.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        actPanel.add(cancelBtn);
        actPanel.add(registerSubmitBtn);
        dialog.add(actPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to sign out?", "Confirm Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productService.logout();
            updateUserUI();
            JOptionPane.showMessageDialog(this, "You have been signed out.", "Signed Out", JOptionPane.INFORMATION_MESSAGE);
            showPanel("PRODUCTS");
        }
    }

    // ORDER HISTORY VIEW

    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(16, 16));
        panel.setBorder(new EmptyBorder(18, 22, 18, 22));
        panel.setBackground(UITheme.BG_MAIN);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel("📦 Order History & Invoices");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);
        topBar.add(title, BorderLayout.WEST);

        JButton viewReceiptBtn = UITheme.createButton("🔍 View Full Invoice", UITheme.PRIMARY, Color.WHITE);
        viewReceiptBtn.addActionListener(e -> showSelectedOrderReceipt());
        topBar.add(viewReceiptBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Order Date", "Customer", "Ordered Items", "Total Amount", "Payment Method", "Status"};
        ordersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        ordersTable = new JTable(ordersTableModel);
        UITheme.styleTable(ordersTable);

        ordersTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        ordersTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        ordersTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        ordersTable.getColumnModel().getColumn(3).setPreferredWidth(280);
        ordersTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        ordersTable.getColumnModel().getColumn(5).setPreferredWidth(160);
        ordersTable.getColumnModel().getColumn(6).setPreferredWidth(90);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        ordersTable.getColumnModel().getColumn(0).setCellRenderer(center);
        ordersTable.getColumnModel().getColumn(1).setCellRenderer(center);
        ordersTable.getColumnModel().getColumn(6).setCellRenderer(center);

        // Price renderer
        DefaultTableCellRenderer priceRenderer = new DefaultTableCellRenderer();
        priceRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        priceRenderer.setFont(UITheme.FONT_BOLD);
        priceRenderer.setForeground(new Color(22, 101, 52));
        ordersTable.getColumnModel().getColumn(4).setCellRenderer(priceRenderer);

        JScrollPane scroll = UITheme.createScrollPane(ordersTable);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public void refreshOrderHistory() {
        ordersTableModel.setRowCount(0);
        User current = productService.getCurrentUser();
        if (current == null) return;

        List<Order> orderList = current.isAdmin() ? productService.getAllOrders() : productService.getUserOrders(current);
        for (Order o : orderList) {
            ordersTableModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getOrderDate(),
                    o.getUser().getFullName() + " (" + o.getUser().getUsername() + ")",
                    o.getItemsSummary(),
                    o.getFormattedTotal(),
                    o.getPaymentMethodName(),
                    "● " + o.getStatus()
            });
        }
    }

    private void showSelectedOrderReceipt() {
        int row = ordersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order from the table to view the invoice.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String orderId = (String) ordersTableModel.getValueAt(row, 0);
        List<Order> all = productService.getAllOrders();
        Order found = null;
        for (Order o : all) {
            if (o.getOrderId().equals(orderId)) {
                found = o;
                break;
            }
        }

        if (found == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("                     OFFICIAL ORDER INVOICE                      \n");
        sb.append("=================================================================\n\n");
        sb.append(String.format("Order Reference:  %s\n", found.getOrderId()));
        sb.append(String.format("Date Placed:      %s\n", found.getOrderDate()));
        sb.append(String.format("Current Status:   %s\n\n", found.getStatus()));
        sb.append("CUSTOMER INFORMATION:\n");
        sb.append(String.format("Full Name:        %s\n", found.getUser().getFullName()));
        sb.append(String.format("Account Username: %s\n", found.getUser().getUsername()));
        sb.append(String.format("Email Address:    %s\n", found.getUser().getEmail()));
        sb.append(String.format("Contact Phone:    %s\n", found.getUser().getPhone()));
        sb.append(String.format("Shipping Address: %s\n\n", found.getShippingAddress()));
        sb.append("PAYMENT INFORMATION:\n");
        sb.append(String.format("Payment Method:   %s\n", found.getPaymentMethodName()));
        sb.append(String.format("Details / Info:   %s\n\n", found.getPaymentDetails()));
        sb.append("-----------------------------------------------------------------\n");
        sb.append(String.format("%-30s %-6s %-12s %-12s\n", "Item Name", "Qty", "Unit Price", "Subtotal"));
        sb.append("-----------------------------------------------------------------\n");
        for (CartItem it : found.getItems()) {
            sb.append(String.format("%-30s %-6d %-12s %-12s\n",
                    it.getProduct().getName(),
                    it.getQuantity(),
                    it.getProduct().getFormattedPrice(),
                    it.getFormattedTotal()));
        }
        sb.append("-----------------------------------------------------------------\n");
        sb.append(String.format("TOTAL AMOUNT PAID: %43s\n", found.getFormattedTotal()));
        sb.append("=================================================================\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setFont(UITheme.FONT_MONO);
        ta.setBackground(new Color(248, 250, 252));
        ta.setForeground(UITheme.TEXT_DARK);
        ta.setBorder(new EmptyBorder(12, 14, 12, 14));

        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(540, 440));

        JOptionPane.showMessageDialog(this, sp, "Invoice - " + found.getOrderId(), JOptionPane.PLAIN_MESSAGE);
    }

    //PROFILE VIEW
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));
        panel.setBackground(UITheme.BG_MAIN);

        JPanel card = UITheme.createCard("👤 User Account & Profile Settings");
        card.setLayout(new BorderLayout(18, 18));

        JPanel fields = new JPanel(new GridLayout(7, 2, 12, 14));
        fields.setOpaque(false);

        profileRoleLabel = new JLabel("Customer");
        profileRoleLabel.setFont(UITheme.FONT_BOLD);
        profileRoleLabel.setForeground(UITheme.PRIMARY);

        profileUsernameField = UITheme.createTextField(15);
        profileUsernameField.setEditable(false);
        profileUsernameField.setBackground(new Color(241, 245, 249));

        profileFullNameField = UITheme.createTextField(15);
        profileEmailField = UITheme.createTextField(15);
        profilePhoneField = UITheme.createTextField(15);

        profileAddressArea = new JTextArea(2, 20);
        profileAddressArea.setFont(UITheme.FONT_REGULAR);
        profileAddressArea.setLineWrap(true);
        profileAddressArea.setWrapStyleWord(true);
        profileAddressArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_DARK, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));

        profilePasswordField = new JPasswordField();
        profilePasswordField.setFont(UITheme.FONT_REGULAR);
        profilePasswordField.setPreferredSize(new Dimension(150, 32));

        fields.add(new JLabel("Account Role:"));
        fields.add(profileRoleLabel);

        fields.add(new JLabel("Username (Read-Only):"));
        fields.add(profileUsernameField);

        fields.add(new JLabel("Full Name:"));
        fields.add(profileFullNameField);

        fields.add(new JLabel("Email Address:"));
        fields.add(profileEmailField);

        fields.add(new JLabel("Phone Number:"));
        fields.add(profilePhoneField);

        fields.add(new JLabel("Default Delivery Address:"));
        fields.add(new JScrollPane(profileAddressArea));

        fields.add(new JLabel("New Password (leave empty to keep current):"));
        fields.add(profilePasswordField);

        card.add(fields, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setOpaque(false);

        JButton saveBtn = UITheme.createButton("💾 Save Profile Changes", UITheme.SUCCESS, Color.WHITE, 200, 38);
        saveBtn.addActionListener(e -> handleSaveProfile());

        actionPanel.add(saveBtn);
        card.add(actionPanel, BorderLayout.SOUTH);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    public void refreshProfileView() {
        User u = productService.getCurrentUser();
        if (u == null) {
            profileRoleLabel.setText("Not signed in");
            profileUsernameField.setText("");
            profileFullNameField.setText("");
            profileEmailField.setText("");
            profilePhoneField.setText("");
            profileAddressArea.setText("");
            profilePasswordField.setText("");
            return;
        }

        profileRoleLabel.setText(u.getRoleName());
        profileUsernameField.setText(u.getUsername());
        profileFullNameField.setText(u.getFullName());
        profileEmailField.setText(u.getEmail());
        profilePhoneField.setText(u.getPhone());
        profileAddressArea.setText(u.getAddress());
        profilePasswordField.setText("");
    }

    private void handleSaveProfile() {
        if (!productService.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please log in first.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String fn = profileFullNameField.getText().trim();
        String em = profileEmailField.getText().trim();
        String ph = profilePhoneField.getText().trim();
        String addr = profileAddressArea.getText().trim();
        String pwd = new String(profilePasswordField.getPassword());

        if (fn.isEmpty() || em.isEmpty() || addr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name, email, and address cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        productService.updateProfile(fn, em, addr, ph, pwd);
        updateUserUI();
        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
