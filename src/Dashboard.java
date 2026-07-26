import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Dashboard extends JFrame {

    JPanel contentPanel;
    JButton activeNavBtn = null;

    public Dashboard() {

        setTitle("SoftManager");
        setSize(1150, 680);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Theme.BG);

        add(contentPanel, BorderLayout.CENTER);

        showDashboard();

        setVisible(true);
    }

    private JPanel buildSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(
            new Dimension(210, 0)
        );

        sidebar.setBackground(
            Theme.SIDEBAR_BG
        );

        sidebar.setLayout(
            new BoxLayout(
                sidebar,
                BoxLayout.Y_AXIS
            )
        );

        // ===== LOGO =====

        JPanel logoArea =
            new JPanel(new BorderLayout());

        logoArea.setBackground(
            Theme.SIDEBAR_BG
        );

        logoArea.setBorder(
            new EmptyBorder(24, 20, 20, 20)
        );

        logoArea.setMaximumSize(
            new Dimension(210, 80)
        );

        JLabel appName =
            new JLabel("SoftManager");

        appName.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                17
            )
        );

        appName.setForeground(Color.WHITE);

        JPanel logoText =
            new JPanel();

        logoText.setLayout(
            new BoxLayout(
                logoText,
                BoxLayout.Y_AXIS
            )
        );

        logoText.setOpaque(false);

        logoText.add(appName);

        logoArea.add(
            logoText,
            BorderLayout.CENTER
        );

        sidebar.add(logoArea);

        // ===== DIVIDER =====

        JSeparator sep =
            new JSeparator();

        sep.setForeground(
            new Color(50, 62, 84)
        );

        sep.setMaximumSize(
            new Dimension(210, 1)
        );

        sidebar.add(sep);

        sidebar.add(
            Box.createVerticalStrut(12)
        );

        // ===== NAV BUTTONS =====

        JButton dashBtn =
            navButton("  Overview");

        JButton listBtn =
            navButton("  Inventory");

        JButton deviceBtn =
            navButton("  Device View");

        JButton addBtn =
            navButton("  Add Software");

        JButton historyBtn =
            navButton("  Version History");

        JButton logsBtn =
            navButton("  Activity");

        sidebar.add(dashBtn);

        sidebar.add(listBtn);

        sidebar.add(deviceBtn);

        sidebar.add(addBtn);

        sidebar.add(historyBtn);

        sidebar.add(logsBtn);

        sidebar.add(
            Box.createVerticalGlue()
        );

        // ===== LOGOUT =====

        JButton logoutBtn =
            navButton("  Logout");

        JButton exitBtn =
            navButton("  Exit");

        sidebar.add(logoutBtn);

        sidebar.add(exitBtn);

        sidebar.add(
            Box.createVerticalStrut(16)
        );

        // ===== ACTIONS =====

        dashBtn.addActionListener(e -> {

            setActive(dashBtn);

            showDashboard();
        });

        listBtn.addActionListener(e -> {

            setActive(listBtn);

            showSoftwareList();
        });

        deviceBtn.addActionListener(e -> {

            setActive(deviceBtn);

            showDeviceView();
        });

        addBtn.addActionListener(e -> {

            new AddSoftware(
                this,
                -1,
                null,
                null,
                null,
                null,
                null,
                null
            ).setVisible(true);

            showSoftwareList();
        });

        historyBtn.addActionListener(e -> {

            setActive(historyBtn);

            showUpdateHistory();
        });

        logsBtn.addActionListener(e -> {

            setActive(logsBtn);

            showActivityLogs();
        });

        // ===== LOGOUT ACTION =====

        logoutBtn.addActionListener(e -> {

            int choice =
                JOptionPane.showConfirmDialog(

                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );

            if (choice ==
                    JOptionPane.YES_OPTION) {

                try {

                    java.sql.Connection con =
                        DBConnection.getConnection();

                    java.sql.PreparedStatement pst =
                        con.prepareStatement(

                        "INSERT INTO activity_logs(user_id, action_type, software_name) VALUES (?,?,?)"
                    );

                    pst.setInt(
                        1,
                        Session.userId
                    );

                    pst.setString(
                        2,
                        "User Logout"
                    );

                    pst.setString(
                        3,
                        "System Session"
                    );

                    pst.executeUpdate();

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                Session.userId = 0;

                dispose();

                new Login()
                    .setVisible(true);
            }
        });

        exitBtn.addActionListener(e ->
            System.exit(0)
        );

        setActive(dashBtn);

        return sidebar;
    }

    private JButton navButton(String text) {

        JButton btn =
            new JButton(text);

        btn.setFont(
            Theme.FONT_NAV
        );

        btn.setForeground(
            new Color(180, 195, 220)
        );

        btn.setBackground(
            Theme.SIDEBAR_BG
        );

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setContentAreaFilled(true);

        btn.setOpaque(true);

        btn.setHorizontalAlignment(
            SwingConstants.LEFT
        );

        btn.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        btn.setMaximumSize(
            new Dimension(210, 44)
        );

        btn.setPreferredSize(
            new Dimension(210, 44)
        );

        btn.setBorder(
            new EmptyBorder(0, 20, 0, 0)
        );

        btn.addMouseListener(
            new java.awt.event.MouseAdapter() {

                public void mouseEntered(
                    java.awt.event.MouseEvent e
                ) {

                    if (btn != activeNavBtn) {

                        btn.setBackground(
                            Theme.SIDEBAR_HOVER
                        );

                        btn.setForeground(
                            Color.WHITE
                        );
                    }
                }

                public void mouseExited(
                    java.awt.event.MouseEvent e
                ) {

                    if (btn != activeNavBtn) {

                        btn.setBackground(
                            Theme.SIDEBAR_BG
                        );

                        btn.setForeground(

                            new Color(
                                180,
                                195,
                                220
                            )
                        );
                    }
                }
            }
        );

        return btn;
    }

    private void setActive(JButton btn) {

        if (activeNavBtn != null) {

            activeNavBtn.setBackground(
                Theme.SIDEBAR_BG
            );

            activeNavBtn.setForeground(

                new Color(
                    180,
                    195,
                    220
                )
            );
        }

        activeNavBtn = btn;

        btn.setBackground(
            Theme.SIDEBAR_ACTIVE
        );

        btn.setForeground(
            Color.WHITE
        );
    }

    public void showDashboard() {

        swap(new DashboardPanel());
    }

    public void showSoftwareList() {

        swap(
            new SoftwareListPanel(this)
        );
    }

    public void showDeviceView() {

        swap(
            new DeviceSoftwarePanel()
        );
    }

    public void showUpdateHistory() {

        swap(new UpdateHistoryPanel());
    }

    public void showActivityLogs() {

        swap(new ActivityLogsPanel());
    }

    private void swap(JPanel panel) {

        contentPanel.removeAll();

        contentPanel.add(
            panel,
            BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(

                UIManager
                    .getSystemLookAndFeelClassName()
            );

        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(
            Login::new
        );
    }
}