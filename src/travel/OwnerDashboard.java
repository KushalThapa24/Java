package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class OwnerDashboard extends JFrame {

    private int ownerId;
    private String ownerName;
    private JPanel hotelsPanel;

    public OwnerDashboard(int ownerId, String ownerName) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;

        setTitle("TravelEase - Hotel Owner Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 32, 58));
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createContentArea(), BorderLayout.CENTER);

        setContentPane(mainPanel);
        loadHotels();
    }

    // Left sidebar showing app name, owner details and navigation
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 750));
        sidebar.setBackground(new Color(10, 20, 45));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(createSidebarTop(), BorderLayout.NORTH);
        sidebar.add(createSidebarNav(), BorderLayout.CENTER);
        return sidebar;
    }

    private JPanel createSidebarTop() {
        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(10, 15, 30, 15));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;

        g.gridy = 0;
        JLabel appName = new JLabel("TravelEase");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appName.setForeground(new Color(100, 200, 255));
        top.add(appName, g);

        g.gridy = 1;
        g.insets = new Insets(20, 0, 0, 0);
        JLabel nameLabel = new JLabel(ownerName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(Color.WHITE);
        top.add(nameLabel, g);

        g.gridy = 2;
        g.insets = new Insets(4, 0, 0, 0);
        JLabel roleLabel = new JLabel("Hotel Owner");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(39, 174, 96));
        top.add(roleLabel, g);

        return top;
    }

    private JPanel createSidebarNav() {
        JPanel nav = new JPanel(new GridLayout(2, 1, 0, 5));
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        nav.add(createNavButton("My Hotels", true));

        JButton logoutBtn = createNavButton("Logout", false);
        logoutBtn.addActionListener(e -> {
            new MainLandingPage().setVisible(true);
            dispose();
        });
        nav.add(logoutBtn);

        return nav;
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(isActive ? Color.WHITE : new Color(160, 180, 220));
        btn.setBackground(new Color(41, 128, 185));
        btn.setContentAreaFilled(isActive);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Right side: page title, add button, scrollable hotel card grid
    private JPanel createContentArea() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(22, 40, 70));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel pageTitle = new JLabel("My Hotel Listings");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pageTitle.setForeground(Color.WHITE);
        topBar.add(pageTitle, BorderLayout.WEST);

        JButton addHotelBtn = createColorButton("Add New Hotel", new Color(39, 174, 96));
        addHotelBtn.setPreferredSize(new Dimension(160, 38));
        addHotelBtn.addActionListener(e -> showAddHotelDialog());
        topBar.add(addHotelBtn, BorderLayout.EAST);

        content.add(topBar, BorderLayout.NORTH);

        hotelsPanel = new JPanel();
        hotelsPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15));
        hotelsPanel.setBackground(new Color(22, 40, 70));

        JScrollPane scrollPane = new JScrollPane(hotelsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane, BorderLayout.CENTER);

        return content;
    }

    // Fetch all hotels for this owner from database and show them
    private void loadHotels() {
        hotelsPanel.removeAll();

        String sql = "SELECT * FROM hotels WHERE owner_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            boolean hasHotels = false;
            while (rs.next()) {
                hasHotels = true;
                hotelsPanel.add(buildHotelCard(rs));
            }

            if (!hasHotels) {
                JLabel emptyMsg = new JLabel("No hotels listed yet. Click Add New Hotel to get started.");
                emptyMsg.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                emptyMsg.setForeground(new Color(120, 150, 200));
                emptyMsg.setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 0));
                hotelsPanel.add(emptyMsg);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        hotelsPanel.revalidate();
        hotelsPanel.repaint();
    }

    // Build one hotel card - view only, no edit or delete
    private JPanel buildHotelCard(ResultSet rs) throws SQLException {
        String name        = rs.getString("name");
        String location    = rs.getString("location");
        String description = rs.getString("description");
        double price       = rs.getDouble("price_per_night");
        int availableRooms = rs.getInt("available_rooms");
        int totalRooms     = rs.getInt("total_rooms");
        String imagePath   = rs.getString("image_path");
        String amenities   = rs.getString("amenities");

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(340, 355));
        card.setBackground(new Color(30, 55, 95));
        card.setBorder(BorderFactory.createLineBorder(new Color(50, 80, 130), 1));

        card.add(buildImagePanel(imagePath), BorderLayout.NORTH);
        card.add(buildCardInfo(card, name, location, description, price, availableRooms, totalRooms, amenities), BorderLayout.CENTER);

        return card;
    }

    // Load hotel image from disk, or show a plain placeholder if not found
    private JPanel buildImagePanel(String imagePath) {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(340, 165));
        imagePanel.setBackground(new Color(20, 40, 80));

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(imagePath));
                if (img != null) {
                    Image scaled = img.getScaledInstance(340, 165, Image.SCALE_SMOOTH);
                    imagePanel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);
                }
            } catch (IOException ignored) {}
        }

        if (imagePanel.getComponentCount() == 0) {
            JLabel placeholder = new JLabel("No Image", SwingConstants.CENTER);
            placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            placeholder.setForeground(new Color(100, 130, 180));
            imagePanel.add(placeholder, BorderLayout.CENTER);
        }

        return imagePanel;
    }

    // Text info section inside each hotel card
    private JPanel buildCardInfo(JPanel card, String name, String location, String description,
                                  double price, int availableRooms, int totalRooms, String amenities) {
        JPanel info = new JPanel(new GridBagLayout());
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;

        // Hotel name
        g.gridy = 0;
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        info.add(nameLabel, g);

        // Location
        g.gridy = 1;
        g.insets = new Insets(3, 0, 0, 0);
        JLabel locationLabel = new JLabel("Location: " + location);
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        locationLabel.setForeground(new Color(130, 170, 230));
        info.add(locationLabel, g);

        // Short description preview
        g.gridy = 2;
        g.insets = new Insets(6, 0, 0, 0);
        String shortDesc = (description != null && description.length() > 80)
                ? description.substring(0, 80) + "..."
                : (description != null ? description : "");
        JLabel shortDescLabel = new JLabel("<html><body style='width:270px'>" + shortDesc + "</body></html>");
        shortDescLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        shortDescLabel.setForeground(new Color(170, 195, 230));
        info.add(shortDescLabel, g);

        // Read More expands the full description below
        g.gridy = 3;
        g.insets = new Insets(3, 0, 0, 0);
        JButton readMoreBtn = new JButton("Read More");
        readMoreBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        readMoreBtn.setForeground(new Color(100, 180, 255));
        readMoreBtn.setContentAreaFilled(false);
        readMoreBtn.setBorderPainted(false);
        readMoreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        readMoreBtn.setHorizontalAlignment(SwingConstants.LEFT);
        readMoreBtn.setBorder(BorderFactory.createEmptyBorder());
        info.add(readMoreBtn, g);

        // Full description panel, hidden by default
        g.gridy = 4;
        JPanel expandedSection = new JPanel(new BorderLayout());
        expandedSection.setOpaque(false);
        String fullText = (description != null ? description : "")
                + (amenities != null && !amenities.isEmpty() ? "<br><b>Amenities:</b> " + amenities : "");
        JLabel fullDescLabel = new JLabel("<html><body style='width:270px'>" + fullText + "</body></html>");
        fullDescLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fullDescLabel.setForeground(new Color(170, 195, 230));
        fullDescLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 100, 160)),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        expandedSection.add(fullDescLabel);
        expandedSection.setVisible(false);

        readMoreBtn.addActionListener(e -> {
            boolean show = !expandedSection.isVisible();
            expandedSection.setVisible(show);
            readMoreBtn.setText(show ? "Read Less" : "Read More");
            card.revalidate();
            card.repaint();
        });
        info.add(expandedSection, g);

        // Room availability count
        g.gridy = 5;
        g.insets = new Insets(10, 0, 0, 0);
        JLabel roomsLabel = new JLabel("Rooms: " + availableRooms + " / " + totalRooms + " available");
        roomsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomsLabel.setForeground(new Color(200, 220, 255));
        info.add(roomsLabel, g);

        // Price displayed in NPR
        g.gridy = 6;
        g.insets = new Insets(4, 0, 0, 0);
        JLabel priceLabel = new JLabel("Price: NPR " + String.format("%,.0f", price) + " / night");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        priceLabel.setForeground(new Color(255, 200, 50));
        info.add(priceLabel, g);

        return info;
    }

    // Dialog to add a new hotel with all required fields
    private void showAddHotelDialog() {
        JDialog dialog = new JDialog(this, "Add New Hotel", true);
        dialog.setSize(580, 600);
        dialog.setLocationRelativeTo(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(20, 38, 65));
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Add New Hotel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        main.add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(5, 0, 5, 0);
        g.gridx = 0;

        JTextField nameField       = makeInputField("");
        JTextField locationField   = makeInputField("");
        JTextField priceField      = makeInputField("");
        JTextField totalRoomsField = makeInputField("");
        JTextField amenitiesField  = makeInputField("");

        JTextArea descArea = new JTextArea(3, 20);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setBackground(new Color(40, 65, 110));
        descArea.setForeground(Color.WHITE);
        descArea.setCaretColor(Color.WHITE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 100, 160), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        final String[] selectedImagePath = {""};
        JLabel imageNameLabel = new JLabel("No image selected");
        imageNameLabel.setForeground(new Color(130, 170, 230));
        imageNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton browseBtn = createColorButton("Browse Image", new Color(41, 128, 185));
        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));
            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                selectedImagePath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                imageNameLabel.setText(fileChooser.getSelectedFile().getName());
            }
        });

        addFormRow(form, g, 0, "Hotel Name", nameField);
        addFormRow(form, g, 1, "Location", locationField);
        addFormRow(form, g, 2, "Price Per Night (NPR)", priceField);
        addFormRow(form, g, 3, "Total Rooms", totalRoomsField);
        addFormRow(form, g, 4, "Amenities (comma separated)", amenitiesField);

        g.gridy = 10; form.add(makeFormLabel("Description"), g);
        g.gridy = 11; form.add(new JScrollPane(descArea), g);

        g.gridy = 12;
        JPanel imageRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        imageRow.setOpaque(false);
        imageRow.add(browseBtn);
        imageRow.add(Box.createHorizontalStrut(10));
        imageRow.add(imageNameLabel);
        form.add(imageRow, g);

        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formScroll.setBorder(null);
        main.add(formScroll, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton cancelBtn = createColorButton("Cancel", new Color(100, 100, 120));
        JButton saveBtn   = createColorButton("Add Hotel", new Color(39, 174, 96));

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String hotelName = nameField.getText().trim();
            String location  = locationField.getText().trim();
            String desc      = descArea.getText().trim();
            String priceStr  = priceField.getText().trim();
            String roomsStr  = totalRoomsField.getText().trim();
            String amenities = amenitiesField.getText().trim();

            if (hotelName.isEmpty() || location.isEmpty() || priceStr.isEmpty() || roomsStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                double price   = Double.parseDouble(priceStr);
                int totalRooms = Integer.parseInt(roomsStr);
                saveHotelToDatabase(hotelName, location, desc, price, totalRooms, selectedImagePath[0], amenities);
                dialog.dispose();
                loadHotels();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Price and rooms must be numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonRow.add(cancelBtn);
        buttonRow.add(saveBtn);
        main.add(buttonRow, BorderLayout.SOUTH);

        dialog.setContentPane(main);
        dialog.setVisible(true);
    }

    // Insert a new hotel row into the database
    private void saveHotelToDatabase(String name, String location, String description,
                                      double price, int totalRooms, String imagePath, String amenities) {
        String sql = "INSERT INTO hotels (owner_id, name, location, description, price_per_night, " +
                     "available_rooms, total_rooms, image_path, amenities) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ps.setString(2, name);
            ps.setString(3, location);
            ps.setString(4, description);
            ps.setDouble(5, price);
            ps.setInt(6, totalRooms);
            ps.setInt(7, totalRooms);
            ps.setString(8, imagePath);
            ps.setString(9, amenities);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Hotel added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Could not save hotel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Adds a label above and a field below as one form row
    private void addFormRow(JPanel form, GridBagConstraints g, int rowIndex, String labelText, JComponent field) {
        g.gridy = rowIndex * 2;
        form.add(makeFormLabel(labelText), g);
        g.gridy = rowIndex * 2 + 1;
        form.add(field, g);
    }

    private JLabel makeFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(160, 200, 255));
        return label;
    }

    private JTextField makeInputField(String defaultText) {
        JTextField field = new JTextField(defaultText, 20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(new Color(40, 65, 110));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 100, 160), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return field;
    }

    private JButton createColorButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
