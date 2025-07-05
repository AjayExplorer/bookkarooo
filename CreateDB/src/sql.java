import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class sql extends JFrame {
    static Connection conn;

    public sql() {
        setTitle("Canteen Management - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel roleLabel = new JLabel("Select Role:");
        String[] roles = {"Admin", "Student"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.add(roleLabel);
        panel.add(roleBox);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel());
        panel.add(loginBtn);

        add(panel);

        loginBtn.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            login(role, user, pass);
        });

        connectDB();
    }

    void login(String role, String user, String pass) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=? AND role=?"
            );
            stmt.setString(1, user);
            stmt.setString(2, pass);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (role.equals("Admin")) {
                    new AdminPanel().setVisible(true);
                } else {
                    new StudentPanel(user).setVisible(true);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static void connectDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/canteen_db";
            String username = "root";
            String password = "ajay@2005"; // Replace with your actual password

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database Connected");
        } catch (SQLException ex) {
            System.err.println("❌ Failed to connect to the database.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new sql().setVisible(true);
    }
}

// ---------------- Admin Panel ----------------
class AdminPanel extends JFrame {
    JTextField mealsField, itemField, priceField, timeField;
    JTextArea output;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(550, 400);
        setLocationRelativeTo(null);

        mealsField = new JTextField(10);
        itemField = new JTextField(10);
        priceField = new JTextField(10);
        timeField = new JTextField(10);
        output = new JTextArea(10, 40);
        output.setEditable(false);

        JButton addMealBtn = new JButton("Update Meal Count");
        JButton addMenuBtn = new JButton("Add Menu Item");

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        inputPanel.add(new JLabel("Total Meals Today:"));
        inputPanel.add(mealsField);
        inputPanel.add(addMealBtn);
        inputPanel.add(new JLabel());

        inputPanel.add(new JLabel("Menu Item:"));
        inputPanel.add(itemField);
        inputPanel.add(new JLabel("Price (₹):"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Timing:"));
        inputPanel.add(timeField);
        inputPanel.add(addMenuBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);

        addMealBtn.addActionListener(e -> {
            try {
                int meals = Integer.parseInt(mealsField.getText());
                PreparedStatement ps = sql.conn.prepareStatement("UPDATE meal_info SET total_meals=?");
                ps.setInt(1, meals);
                ps.executeUpdate();
                output.append("✅ Meal count updated to: " + meals + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addMenuBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = sql.conn.prepareStatement("INSERT INTO menu(item, price, timing) VALUES (?, ?, ?)");
                ps.setString(1, itemField.getText());
                ps.setDouble(2, Double.parseDouble(priceField.getText()));
                ps.setString(3, timeField.getText());
                ps.executeUpdate();
                output.append("✅ Menu item added: " + itemField.getText() + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}

// ---------------- Student Panel ----------------
class StudentPanel extends JFrame {
    JTextArea area;
    String username;

    public StudentPanel(String user) {
        this.username = user;
        setTitle("Student Panel");
        setSize(550, 400);
        setLocationRelativeTo(null);

        area = new JTextArea(15, 40);
        area.setEditable(false);

        JButton viewBtn = new JButton("View Meals/Menu");
        JButton bookBtn = new JButton("Book Meal");

        JTextField specialField = new JTextField(15);

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Special Request:"));
        panel.add(specialField);
        panel.add(bookBtn);
        panel.add(viewBtn);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            try {
                area.setText("");
                Statement s = sql.conn.createStatement();
                ResultSet rs1 = s.executeQuery("SELECT * FROM meal_info");
                if (rs1.next()) {
                    area.append("🍽️ Total Meals Available: " + rs1.getInt("total_meals") + "\n");
                }

                area.append("\n📋 Today's Menu:\n");
                ResultSet rs2 = s.executeQuery("SELECT * FROM menu");
                while (rs2.next()) {
                    area.append("• " + rs2.getString("item") + " - ₹" + rs2.getDouble("price") +
                                " at " + rs2.getString("timing") + "\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        bookBtn.addActionListener(e -> {
            try {
                PreparedStatement ps = sql.conn.prepareStatement("INSERT INTO bookings(student, special_request) VALUES (?, ?)");
                ps.setString(1, username);
                ps.setString(2, specialField.getText());
                ps.executeUpdate();
                area.append("\n✅ Booking successful for " + username + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
