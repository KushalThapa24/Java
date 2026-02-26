package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MainLandingPage extends JFrame {

    public MainLandingPage() {
        setTitle("TravelEase - Discover Nepal");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//When you click ❌ (close button), the program will completely exit.
        setLocationRelativeTo(null);//Centers the window on the screen.
        setResizable(true);//Prevents user from resizing the window.

        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(1100, 70));

        JLabel logo = new JLabel(" TravelEase");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        header.add(logo, BorderLayout.WEST);
        bg.add(header, BorderLayout.NORTH);

        // Center content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);

        JLabel title = new JLabel("EXPLORE NEPAL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 52));
        title.setForeground(Color.WHITE);
        centerPanel.add(title, gbc);

        gbc.gridy = 1;
        JLabel subtitle = new JLabel("Find & Book the Perfect Hotel for Your Adventure");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(new Color(220, 240, 255, 220));
        centerPanel.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel chooseLabel = new JLabel("— Choose Your Role —");
        chooseLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        chooseLabel.setForeground(new Color(255, 220, 100));
        centerPanel.add(chooseLabel, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        btnPanel.setOpaque(false);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 0, 0);

        JButton customerBtn = createRoleButton("I'm a Traveler",
                "Search & Book Hotels", new Color(41, 128, 185), new Color(52, 152, 219));
        JButton ownerBtn = createRoleButton("I'm a Hotel Owner",
                "Manage Your Properties", new Color(39, 174, 96), new Color(46, 204, 113));

        customerBtn.addActionListener(e -> { new LoginPage("customer").setVisible(true); dispose(); });
        ownerBtn.addActionListener(e -> { new LoginPage("owner").setVisible(true); dispose(); });

        btnPanel.add(customerBtn);
        btnPanel.add(ownerBtn);
        centerPanel.add(btnPanel, gbc);

        bg.add(centerPanel, BorderLayout.CENTER);
        setContentPane(bg);
    }

    private JButton createRoleButton(String title, String subtitle, Color c1, Color c2) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color top = getModel().isRollover() ? c2.brighter() : c2;
                Color bot = getModel().isRollover() ? c1.brighter() : c1;
                g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bot));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setLayout(new BorderLayout());
        btn.setPreferredSize(new Dimension(240, 110));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel(subtitle, SwingConstants.CENTER);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLbl.setForeground(new Color(220, 240, 255));
        subLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        btn.add(titleLbl, BorderLayout.CENTER);
        btn.add(subLbl, BorderLayout.SOUTH);
        return btn;
    }

    // Simple image background - no maths, just loads background.jpg
    static class BackgroundPanel extends JPanel {
        private BufferedImage bgImage;

        public BackgroundPanel() {
            try {
                bgImage = ImageIO.read(new File("background.jpg"));
            } catch (Exception e) {
                bgImage = null;
            }
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth(), h = getHeight();

            if (bgImage != null) {
                // Stretch image to fill entire window
                g2.drawImage(bgImage, 0, 0, w, h, this);
            } else {
                // Fallback dark blue if image not found
                g2.setColor(new Color(15, 30, 60));
                g2.fillRect(0, 0, w, h);
            }

            // Semi-dark overlay so white text stays readable
            g2.setColor(new Color(0, 0, 0, 130));
            g2.fillRect(0, 0, w, h);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        DatabaseConnection.initializeDatabase();
        SwingUtilities.invokeLater(() -> new MainLandingPage().setVisible(true));
    }
}
/*javac -cp ".;mysql-connector-j-9.6.0.jar" -d out src/travel/*.java
java -cp ".;out;mysql-connector-j-9.6.0.jar" travel.MainLandingPage*/