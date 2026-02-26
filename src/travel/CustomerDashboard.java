package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class CustomerDashboard extends JFrame {

    private int customerId;
    private String customerName;
    private JPanel hotelsPanel;
    private JTextField searchField;

    public CustomerDashboard(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;

        setTitle("TravelEase - Find Your Perfect Stay");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 32, 58));
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createContentArea(), BorderLayout.CENTER);

        setContentPane(mainPanel);
        loadHotels("");
    }

    // Left sidebar with app name, customer info and nav buttons
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
        JLabel nameLabel = new JLabel(customerName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(Color.WHITE);
        top.add(nameLabel, g);

        g.gridy = 2;
        g.insets = new Insets(4, 0, 0, 0);
        JLabel roleLabel = new JLabel("Traveler");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(100, 200, 255));
        top.add(roleLabel, g);

        return top;
    }

    private JPanel createSidebarNav() {
        JPanel nav = new JPanel(new GridLayout(3, 1, 0, 5));
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        nav.add(createNavButton("Browse Hotels", true));

        JButton bookingsBtn = createNavButton("My Bookings", false);
        bookingsBtn.addActionListener(e -> showMyBookings());
        nav.add(bookingsBtn);

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

    // Right side: page title, search bar, scrollable hotel grid
    private JPanel createContentArea() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(22, 40, 70));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        content.add(createSearchPanel(), BorderLayout.NORTH);

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

    // Search bar - search by hotel name only
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Find Your Perfect Stay");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchRow.setOpaque(false);

        JLabel searchLabel = new JLabel("Search by Hotel Name:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(new Color(160, 200, 255));
        searchRow.add(searchLabel);

        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(new Color(40, 65, 110));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 100, 160), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        searchField.setPreferredSize(new Dimension(300, 38));
        searchRow.add(searchField);

        JButton searchBtn = createColorButton("Search", new Color(41, 128, 185));
        JButton clearBtn  = createColorButton("Clear", new Color(100, 100, 120));

        searchBtn.addActionListener(e -> loadHotels(searchField.getText().trim()));
        clearBtn.addActionListener(e -> { searchField.setText(""); loadHotels(""); });

        // Also search when Enter is pressed
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loadHotels(searchField.getText().trim());
            }
        });

        searchRow.add(searchBtn);
        searchRow.add(clearBtn);
        panel.add(searchRow, BorderLayout.CENTER);

        return panel;
    }

    // Load hotels from database - filtered by name if query is provided
    private void loadHotels(String query) {
        hotelsPanel.removeAll();

        String sql;
        if (query.isEmpty()) {
            sql = "SELECT h.*, o.full_name as owner_name FROM hotels h " +
                  "JOIN hotel_owners o ON h.owner_id = o.id ORDER BY h.id DESC";
        } else {
            sql = "SELECT h.*, o.full_name as owner_name FROM hotels h " +
                  "JOIN hotel_owners o ON h.owner_id = o.id WHERE h.name LIKE ? ORDER BY h.id DESC";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!query.isEmpty()) ps.setString(1, "%" + query + "%");

            ResultSet rs = ps.executeQuery();
            boolean found = false;

            while (rs.next()) {
                found = true;
                hotelsPanel.add(buildHotelCard(rs));
            }

            if (!found) {
                JLabel noResult = new JLabel("No hotels found for '" + query + "'. Try a different name.");
                noResult.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noResult.setForeground(new Color(120, 150, 200));
                noResult.setBorder(BorderFactory.createEmptyBorder(50, 30, 0, 0));
                hotelsPanel.add(noResult);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        hotelsPanel.revalidate();
        hotelsPanel.repaint();
    }

    // Build one hotel card for the customer view
    private JPanel buildHotelCard(ResultSet rs) throws SQLException {
        int hotelId        = rs.getInt("id");
        String name        = rs.getString("name");
        String location    = rs.getString("location");
        String description = rs.getString("description");
        double price       = rs.getDouble("price_per_night");
        int availableRooms = rs.getInt("available_rooms");
        double rating      = rs.getDouble("rating");
        String imagePath   = rs.getString("image_path");
        String amenities   = rs.getString("amenities");
        String ownerName   = rs.getString("owner_name");

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(340, 400));
        card.setBackground(new Color(30, 55, 95));
        card.setBorder(BorderFactory.createLineBorder(new Color(50, 80, 130), 1));

        card.add(buildImagePanel(imagePath), BorderLayout.NORTH);
        card.add(buildCardInfo(card, hotelId, name, location, description, price, availableRooms, rating, amenities, ownerName), BorderLayout.CENTER);

        return card;
    }

    // Load hotel image or show placeholder text
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

    // Hotel info section with read more, price in NPR and book button
    private JPanel buildCardInfo(JPanel card, int hotelId, String name, String location,
                                  String description, double price, int availableRooms,
                                  double rating, String amenities, String ownerName) {
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

        // Star rating
        g.gridy = 2;
        g.insets = new Insets(3, 0, 0, 0);
        StringBuilder stars = new StringBuilder();
        int starCount = (int) Math.round(rating);
        for (int i = 0; i < 5; i++) stars.append(i < starCount ? "*" : "-");
        JLabel ratingLabel = new JLabel(stars.toString() + "  " + String.format("%.1f", rating));
        ratingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ratingLabel.setForeground(new Color(255, 200, 50));
        info.add(ratingLabel, g);

        // Short description preview
        g.gridy = 3;
        g.insets = new Insets(5, 0, 0, 0);
        String shortDesc = (description != null && description.length() > 75)
                ? description.substring(0, 75) + "..."
                : (description != null ? description : "");
        JLabel shortDescLabel = new JLabel("<html><body style='width:270px'>" + shortDesc + "</body></html>");
        shortDescLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        shortDescLabel.setForeground(new Color(170, 195, 230));
        info.add(shortDescLabel, g);

        // Read More button
        g.gridy = 4;
        g.insets = new Insets(2, 0, 0, 0);
        JButton readMoreBtn = new JButton("Read More");
        readMoreBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        readMoreBtn.setForeground(new Color(100, 180, 255));
        readMoreBtn.setContentAreaFilled(false);
        readMoreBtn.setBorderPainted(false);
        readMoreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        readMoreBtn.setHorizontalAlignment(SwingConstants.LEFT);
        readMoreBtn.setBorder(BorderFactory.createEmptyBorder());
        info.add(readMoreBtn, g);

        // Full description hidden panel
        g.gridy = 5;
        JPanel expandedSection = new JPanel(new BorderLayout());
        expandedSection.setOpaque(false);
        String fullText = (description != null ? description : "")
                + (amenities != null && !amenities.isEmpty() ? "<br><b>Amenities:</b> " + amenities : "")
                + "<br><i>Managed by: " + ownerName + "</i>";
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

        // Rooms available
        g.gridy = 6;
        g.insets = new Insets(8, 0, 0, 0);
        JLabel roomsLabel = new JLabel("Rooms available: " + availableRooms);
        roomsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomsLabel.setForeground(availableRooms > 0 ? new Color(46, 204, 113) : new Color(231, 76, 60));
        info.add(roomsLabel, g);

        // Price in NPR
        g.gridy = 7;
        g.insets = new Insets(3, 0, 0, 0);
        JLabel priceLabel = new JLabel("Price: NPR " + String.format("%,.0f", price) + " / night");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        priceLabel.setForeground(new Color(255, 200, 50));
        info.add(priceLabel, g);

        // Book Now button
        g.gridy = 8;
        g.insets = new Insets(10, 0, 0, 0);
        JButton bookBtn = createColorButton(
                availableRooms > 0 ? "Book Now" : "Fully Booked",
                availableRooms > 0 ? new Color(41, 128, 185) : new Color(100, 100, 120));
        bookBtn.setPreferredSize(new Dimension(300, 36));
        bookBtn.setEnabled(availableRooms > 0);

        final int fHotelId   = hotelId;
        final double fPrice  = price;
        final int fAvail     = availableRooms;
        bookBtn.addActionListener(e -> showBookingDialog(fHotelId, name, location, fPrice, fAvail));

        info.add(bookBtn, g);

        return info;
    }

    // Booking dialog where customer picks dates and number of rooms
    private void showBookingDialog(int hotelId, String hotelName, String location, double price, int availableRooms) {
        JDialog dialog = new JDialog(this, "Book - " + hotelName, true);
        dialog.setSize(460, 430);
        dialog.setLocationRelativeTo(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(20, 38, 65));
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Book " + hotelName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        main.add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;

        JTextField checkInField  = makeInputField("2025-03-01");
        JTextField checkOutField = makeInputField("2025-03-05");

        g.gridy = 0; g.insets = new Insets(10, 0, 4, 0);
        form.add(makeFormLabel("Location: " + location), g);

        g.gridy = 1; g.insets = new Insets(10, 0, 4, 0);
        form.add(makeFormLabel("Check-in Date (YYYY-MM-DD)"), g);
        g.gridy = 2; g.insets = new Insets(0, 0, 0, 0);
        form.add(checkInField, g);

        g.gridy = 3; g.insets = new Insets(10, 0, 4, 0);
        form.add(makeFormLabel("Check-out Date (YYYY-MM-DD)"), g);
        g.gridy = 4; g.insets = new Insets(0, 0, 0, 0);
        form.add(checkOutField, g);

        g.gridy = 5; g.insets = new Insets(10, 0, 4, 0);
        form.add(makeFormLabel("Number of Rooms (max " + availableRooms + ")"), g);
        g.gridy = 6; g.insets = new Insets(0, 0, 0, 0);
        SpinnerNumberModel spinModel = new SpinnerNumberModel(1, 1, availableRooms, 1);
        JSpinner roomsSpinner = new JSpinner(spinModel);
        roomsSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomsSpinner.setPreferredSize(new Dimension(200, 36));
        form.add(roomsSpinner, g);

        // Live total price label updates when rooms spinner changes
        g.gridy = 7; g.insets = new Insets(15, 0, 0, 0);
        JLabel totalLabel = new JLabel("Price per night: NPR " + String.format("%,.0f", price));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(255, 200, 50));
        form.add(totalLabel, g);

        roomsSpinner.addChangeListener(e -> {
            int rooms = (int) roomsSpinner.getValue();
            totalLabel.setText("Per night x " + rooms + " rooms: NPR " + String.format("%,.0f", price * rooms));
        });

        main.add(form, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);

        JButton cancelBtn  = createColorButton("Cancel", new Color(100, 100, 120));
        JButton confirmBtn = createColorButton("Confirm Booking", new Color(39, 174, 96));
        confirmBtn.setPreferredSize(new Dimension(160, 32));

        cancelBtn.addActionListener(e -> dialog.dispose());

        confirmBtn.addActionListener(e -> {
            String checkIn  = checkInField.getText().trim();
            String checkOut = checkOutField.getText().trim();
            int numRooms    = (int) roomsSpinner.getValue();

            if (!checkIn.matches("\\d{4}-\\d{2}-\\d{2}") || !checkOut.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(dialog, "Please use date format YYYY-MM-DD.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (checkOut.compareTo(checkIn) <= 0) {
                JOptionPane.showMessageDialog(dialog, "Check-out date must be after check-in date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                java.time.LocalDate inDate  = java.time.LocalDate.parse(checkIn);
                java.time.LocalDate outDate = java.time.LocalDate.parse(checkOut);
                long nights = java.time.temporal.ChronoUnit.DAYS.between(inDate, outDate);
                double total = price * numRooms * nights;

                int confirm = JOptionPane.showConfirmDialog(dialog,
                        String.format("Confirm your booking?\n\nHotel: %s\nCheck-in: %s\nCheck-out: %s\nRooms: %d\nNights: %d\nTotal: NPR %,.0f",
                                hotelName, checkIn, checkOut, numRooms, nights, total),
                        "Confirm Booking", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    saveBookingToDatabase(hotelId, checkIn, checkOut, numRooms, total);
                    dialog.dispose();
                    loadHotels(searchField.getText().trim());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date. Use format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonRow.add(cancelBtn);
        buttonRow.add(confirmBtn);
        main.add(buttonRow, BorderLayout.SOUTH);

        dialog.setContentPane(main);
        dialog.setVisible(true);
    }

    // Save booking to database and reduce the hotel's available room count
    private void saveBookingToDatabase(int hotelId, String checkIn, String checkOut, int numRooms, double total) {
        String insertBooking = "INSERT INTO bookings (customer_id, hotel_id, check_in, check_out, num_rooms, total_price) VALUES (?,?,?,?,?,?)";
        String reduceRooms   = "UPDATE hotels SET available_rooms = available_rooms - ? WHERE id = ? AND available_rooms >= ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Reduce room count first
                PreparedStatement updatePs = conn.prepareStatement(reduceRooms);
                updatePs.setInt(1, numRooms);
                updatePs.setInt(2, hotelId);
                updatePs.setInt(3, numRooms);
                int rowsUpdated = updatePs.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Not enough rooms available. Please try again.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Then insert the booking record
                PreparedStatement bookingPs = conn.prepareStatement(insertBooking);
                bookingPs.setInt(1, customerId);
                bookingPs.setInt(2, hotelId);
                bookingPs.setString(3, checkIn);
                bookingPs.setString(4, checkOut);
                bookingPs.setInt(5, numRooms);
                bookingPs.setDouble(6, total);
                bookingPs.executeUpdate();

                conn.commit();

                JOptionPane.showMessageDialog(this,
                        String.format("Booking confirmed!\n\nTotal: NPR %,.0f\n\nEnjoy your stay!", total),
                        "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Booking error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Show all bookings made by this customer in a table
    private void showMyBookings() {
        JDialog dialog = new JDialog(this, "My Bookings", true);
        dialog.setSize(800, 450);
        dialog.setLocationRelativeTo(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(20, 38, 65));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        main.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Hotel", "Location", "Check-in", "Check-out", "Rooms", "Total (NPR)", "Status"};
        java.util.List<Object[]> rows = new java.util.ArrayList<>();

        String sql = "SELECT b.id, h.name, h.location, b.check_in, b.check_out, b.num_rooms, b.total_price, b.status " +
                     "FROM bookings b JOIN hotels h ON b.hotel_id = h.id " +
                     "WHERE b.customer_id = ? ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getInt("num_rooms"),
                        String.format("%,.0f", rs.getDouble("total_price")),
                        rs.getString("status")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Object[][] data = rows.toArray(new Object[0][]);
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(30, 55, 95));
        table.setGridColor(new Color(50, 80, 130));
        table.setRowHeight(30);
        table.setEnabled(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(20, 45, 85));
        table.getTableHeader().setForeground(new Color(100, 180, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 55, 95));
        main.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setOpaque(false);
        JButton closeBtn = createColorButton("Close", new Color(41, 128, 185));
        closeBtn.addActionListener(e -> dialog.dispose());
        buttonRow.add(closeBtn);
        main.add(buttonRow, BorderLayout.SOUTH);

        dialog.setContentPane(main);
        dialog.setVisible(true);
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
