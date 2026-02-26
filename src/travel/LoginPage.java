package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {

    private String role;
    private JTabbedPane tabbedPane;

    public LoginPage(String role) {
        this.role = role;
        String roleTitle = role.equals("customer") ? "Traveler Portal" : "Hotel Owner Portal";

        setTitle("TravelEase - " + roleTitle);
        setSize(460, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(15, 30, 60));

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(new Color(15, 30, 60));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;

        JLabel titleLabel = new JLabel(roleTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, g);

        g.gridy = 1;
        g.insets = new Insets(6, 0, 0, 0);
        String sub = role.equals("customer") ? "Find and book your perfect stay" : "Manage your hotel listings";
        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setForeground(new Color(160, 200, 255));
        header.add(subLabel, g);

        wrapper.add(header, BorderLayout.NORTH);

        // Styled tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(20, 45, 85));
        tabbedPane.setForeground(new Color(30, 30, 30));
        tabbedPane.addTab("  Login  ", createLoginPanel());
        tabbedPane.addTab("  Register  ", createRegisterPanel());

        JPanel tabWrapper = new JPanel(new BorderLayout());
        tabWrapper.setBackground(new Color(15, 30, 60));
        tabWrapper.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
        tabWrapper.add(tabbedPane);
        wrapper.add(tabWrapper, BorderLayout.CENTER);

        // Back button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(new Color(15, 30, 60));
        JButton backBtn = new JButton("<- Back to Home");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backBtn.setForeground(new Color(100, 180, 255));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> { new MainLandingPage().setVisible(true); dispose(); });
        bottom.add(backBtn);
        wrapper.add(bottom, BorderLayout.SOUTH);

        setContentPane(wrapper);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(22, 45, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;

        JTextField usernameField  = makeField();
        JPasswordField passField  = makePasswordField();

        g.gridy = 0; g.insets = new Insets(0, 0, 5, 0);  panel.add(makeLabel("Username"), g);
        g.gridy = 1; g.insets = new Insets(0, 0, 18, 0); panel.add(usernameField, g);
        g.gridy = 2; g.insets = new Insets(0, 0, 5, 0);  panel.add(makeLabel("Password"), g);
        g.gridy = 3; g.insets = new Insets(0, 0, 28, 0); panel.add(passField, g);

        Color btnColor = role.equals("customer") ? new Color(41, 128, 185) : new Color(39, 174, 96);
        JButton loginBtn = makeButton("Login", btnColor);
        g.gridy = 4; g.insets = new Insets(0, 0, 0, 0);
        panel.add(loginBtn, g);

        loginBtn.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passField.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }
            handleLogin(u, p);
        });

        passField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loginBtn.doClick();
            }
        });

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(22, 45, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;

        JTextField nameField     = makeField();
        JTextField usernameField = makeField();
        JTextField emailField    = makeField();
        JPasswordField passField = makePasswordField();
        JTextField phoneField    = makeField();

        int r = 0;
        g.gridy = r++; g.insets = new Insets(0,0,5,0);  panel.add(makeLabel("Full Name"), g);
        g.gridy = r++; g.insets = new Insets(0,0,12,0); panel.add(nameField, g);
        g.gridy = r++; g.insets = new Insets(0,0,5,0);  panel.add(makeLabel("Username"), g);
        g.gridy = r++; g.insets = new Insets(0,0,12,0); panel.add(usernameField, g);
        g.gridy = r++; g.insets = new Insets(0,0,5,0);  panel.add(makeLabel("Email"), g);
        g.gridy = r++; g.insets = new Insets(0,0,12,0); panel.add(emailField, g);
        g.gridy = r++; g.insets = new Insets(0,0,5,0);  panel.add(makeLabel("Password"), g);
        g.gridy = r++; g.insets = new Insets(0,0,12,0); panel.add(passField, g);

        if (role.equals("customer")) {
            g.gridy = r++; g.insets = new Insets(0,0,5,0);  panel.add(makeLabel("Phone (optional)"), g);
            g.gridy = r++; g.insets = new Insets(0,0,12,0); panel.add(phoneField, g);
        }

        Color btnColor = role.equals("customer") ? new Color(41, 128, 185) : new Color(39, 174, 96);
        JButton regBtn = makeButton("Create Account", btnColor);
        g.gridy = r; g.insets = new Insets(10, 0, 0, 0);
        panel.add(regBtn, g);

        regBtn.addActionListener(e -> {
            String name  = nameField.getText().trim();
            String user  = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String pass  = new String(passField.getPassword());
            String phone = phoneField.getText().trim();
            if (name.isEmpty() || user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Missing Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }
            handleRegister(name, user, email, pass, phone);
        });

        return panel;
    }

    private void handleLogin(String username, String password) {
        String table = role.equals("customer") ? "customers" : "hotel_owners";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("full_name");
                JOptionPane.showMessageDialog(this, "Welcome, " + name + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                if (role.equals("customer")) new CustomerDashboard(id, name).setVisible(true);
                else new OwnerDashboard(id, name).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(String fullName, String username, String email, String password, String phone) {
        String sql = role.equals("customer")
                ? "INSERT INTO customers (username, password, full_name, email, phone) VALUES (?,?,?,?,?)"
                : "INSERT INTO hotel_owners (username, password, full_name, email) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            if (role.equals("customer")) ps.setString(5, phone);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account created! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            tabbedPane.setSelectedIndex(0);
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists. Try another.", "Username Taken", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Registration error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(180, 210, 255));
        return l;
    }

    private JTextField makeField() {
        JTextField f = new JTextField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(40, 65, 110));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 110, 170), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    private JPasswordField makePasswordField() {
        JPasswordField f = new JPasswordField(20);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(40, 65, 110));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 110, 170), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(380, 42));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
