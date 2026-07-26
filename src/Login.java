import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public Login() {

        setTitle("Software Management System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);

        JPanel card = Theme.card();

        card.setLayout(
            new BoxLayout(
                card,
                BoxLayout.Y_AXIS
            )
        );

        card.setBorder(
            new EmptyBorder(55, 55, 55, 55)
        );

        card.setPreferredSize(
            new Dimension(500, 430)
        );

        card.setMaximumSize(
            card.getPreferredSize()
        );

        // ===== TITLE =====

        JLabel title =
            new JLabel("Welcome");

        title.setFont(
            Theme.FONT_TITLE
        );

        title.setForeground(
            Theme.TEXT_DARK
        );

        title.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        JLabel sub =
            new JLabel(
                "Login or register to continue"
            );

        sub.setFont(
            Theme.FONT_BODY
        );

        sub.setForeground(
            Theme.TEXT_MID
        );

        sub.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        card.add(Box.createVerticalStrut(42));

        // ===== EMAIL =====

        JLabel emailLabel =
            Theme.formLabel("Email");

        emailLabel.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        emailField =
            Theme.inputField();

        emailField.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                44
            )
        );

        emailField.setPreferredSize(
            new Dimension(0, 44)
        );

        emailField.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        // ===== PASSWORD =====

        JLabel passLabel =
            Theme.formLabel("Password");

        passLabel.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        passwordField =
            new JPasswordField();

        passwordField.setFont(
            Theme.FONT_INPUT
        );

        passwordField.setBorder(
            emailField.getBorder()
        );

        passwordField.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                44
            )
        );

        passwordField.setPreferredSize(
            new Dimension(0, 44)
        );

        passwordField.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        // ===== BUTTONS =====

        JButton loginBtn =
            Theme.primaryButton("Login");

        loginBtn.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                48
            )
        );

        loginBtn.setPreferredSize(
            new Dimension(0, 48)
        );

        loginBtn.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        JButton registerBtn =
            Theme.ghostButton("Register");

        registerBtn.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                48
            )
        );

        registerBtn.setPreferredSize(
            new Dimension(0, 48)
        );

        registerBtn.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        // ===== ACTIONS =====

        loginBtn.addActionListener(
            e -> login()
        );

        registerBtn.addActionListener(
            e -> registerUser()
        );

        // ===== ADD COMPONENTS =====

        card.add(emailLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(emailField);

        card.add(Box.createVerticalStrut(24));

        card.add(passLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);

        card.add(Box.createVerticalStrut(42));

        card.add(loginBtn);
        card.add(Box.createVerticalStrut(14));
        card.add(registerBtn);

        JPanel wrapper =
            new JPanel(
                new GridBagLayout()
            );

        wrapper.setBackground(
            Theme.BG
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(
                20,
                20,
                20,
                20
            );

        wrapper.add(card, gbc);

        main.add(
            wrapper,
            BorderLayout.CENTER
        );

        add(main);

        setVisible(true);
    }

    // ===== LOGIN =====

    private void login() {

        String email =
            emailField
                .getText()
                .trim();

        String password =
            String.valueOf(
                passwordField
                    .getPassword()
            );

        if (email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill all fields"
            );

            return;
        }

        try {

            Connection con =
                DBConnection
                    .getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                "SELECT * FROM users WHERE email=? AND password=?"
            );

            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs =
                pst.executeQuery();

            if (rs.next()) {

                // ✅ SAVE USER ID

                Session.userId =
                    rs.getInt("id");

                // ✅ ACTIVITY LOG

                PreparedStatement log =
                    con.prepareStatement(

                    "INSERT INTO activity_logs(user_id, action_type, software_name) VALUES (?,?,?)"
                );

                log.setInt(
                    1,
                    Session.userId
                );

                log.setString(
                    2,
                    "User Login"
                );

                log.setString(
                    3,
                    "Account Access"
                );

                log.executeUpdate();

                dispose();

                new Dashboard()
                    .setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ===== REGISTER =====

    private void registerUser() {

        String email =
            emailField
                .getText()
                .trim();

        String password =
            String.valueOf(
                passwordField
                    .getPassword()
            );

        if (email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill all fields"
            );

            return;
        }

        try {

            Connection con =
                DBConnection
                    .getConnection();

            PreparedStatement check =
                con.prepareStatement(

                "SELECT * FROM users WHERE email=?"
            );

            check.setString(1, email);

            ResultSet rs =
                check.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Email already registered"
                );

                return;
            }

            PreparedStatement pst =
                con.prepareStatement(

                "INSERT INTO users(email,password) VALUES(?,?)"
            );

            pst.setString(1, email);
            pst.setString(2, password);

            pst.executeUpdate();

            // ✅ FETCH USER ID

            PreparedStatement fetch =
                con.prepareStatement(

                "SELECT id FROM users WHERE email=?"
            );

            fetch.setString(1, email);

            ResultSet userRs =
                fetch.executeQuery();

            int newUserId = -1;

            if (userRs.next()) {

                newUserId =
                    userRs.getInt("id");
            }

            // ✅ ACTIVITY LOG

            PreparedStatement log =
                con.prepareStatement(

                "INSERT INTO activity_logs(user_id, action_type, software_name) VALUES (?,?,?)"
            );

            log.setInt(
                1,
                newUserId
            );

            log.setString(
                2,
                "User Registered"
            );

            log.setString(
                3,
                "New Account Created"
            );

            log.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Registration successful. Click Login to continue."
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            Login::new
        );
    }
}