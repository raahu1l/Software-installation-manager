import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.*;

public class ActivityLogsPanel extends JPanel {

    public ActivityLogsPanel() {

        setLayout(new BorderLayout());

        setBackground(Theme.BG);

        add(

            Theme.makePageHeader(

                "Activity Logs",

                "Track all user actions performed inside the system"
            ),

            BorderLayout.NORTH
        );

        add(
            buildContent(),
            BorderLayout.CENTER
        );
    }

    private JPanel buildContent() {

        JPanel wrapper =
            new JPanel(new BorderLayout());

        wrapper.setBackground(
            Theme.BG
        );

        wrapper.setBorder(

            new EmptyBorder(
                22,
                28,
                22,
                28
            )
        );

        JPanel timeline =
            new JPanel();

        timeline.setLayout(

            new BoxLayout(
                timeline,
                BoxLayout.Y_AXIS
            )
        );

        timeline.setOpaque(false);

        boolean hasRecords = false;

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                    "SELECT * FROM activity_logs " +

                    "WHERE user_id=? " +

                    "ORDER BY id DESC"
                );

            pst.setInt(
                1,
                Session.userId
            );

            ResultSet rs =
                pst.executeQuery();

            while (rs.next()) {

                hasRecords = true;

                timeline.add(

                    buildActivityRow(

                        rs.getString(
                            "action_type"
                        ),

                        rs.getString(
                            "software_name"
                        ),

                        rs.getString(
                            "action_time"
                        )
                    )
                );

                timeline.add(
                    Box.createVerticalStrut(12)
                );
            }

        } catch (Exception e) {

            JLabel err =
                new JLabel(
                    "Could not load activity logs."
                );

            err.setFont(
                Theme.FONT_BODY
            );

            err.setForeground(
                Theme.DANGER
            );

            timeline.add(err);
        }

        if (!hasRecords) {

            timeline.add(
                buildEmptyState()
            );
        }

        JScrollPane scroll =
            new JScrollPane(timeline);

        scroll.getVerticalScrollBar()
            .setUnitIncrement(16);

        scroll.setBorder(null);

        scroll.setOpaque(false);

        scroll.getViewport()
            .setOpaque(false);

        wrapper.add(
            scroll,
            BorderLayout.CENTER
        );

        return wrapper;
    }

    // ===== MODERN CARD =====

    private JPanel buildActivityRow(

            String action,
            String software,
            String time
    ) {

        JPanel row =
            new JPanel(
                new BorderLayout()
            );

        row.setOpaque(false);

        JPanel card =
            new JPanel(
                new BorderLayout(
                    16,
                    0
                )
            );

        card.setBackground(
            Theme.WHITE
        );

        card.setMaximumSize(

            new Dimension(
                Integer.MAX_VALUE,
                68
            )
        );

        card.setBorder(

            new CompoundBorder(

                new LineBorder(
                    Theme.BORDER,
                    1,
                    true
                ),

                new EmptyBorder(
                    10,
                    14,
                    10,
                    14
                )
            )
        );

        // ===== ICON =====

        JPanel iconWrap =
            new JPanel(
                new GridBagLayout()
            );

        iconWrap.setPreferredSize(
            new Dimension(38, 38)
        );

        iconWrap.setMaximumSize(
            new Dimension(38, 38)
        );

        iconWrap.setBackground(
            getSoftColor(action)
        );

        iconWrap.setBorder(

            new LineBorder(
                getSoftColor(action),
                1,
                true
            )
        );

        JLabel icon =
            new JLabel(
                getSymbol(action)
            );

        icon.setFont(

            new Font(
                "Segoe UI",
                Font.BOLD,
                15
            )
        );

        icon.setForeground(
            getDotColor(action)
        );

        iconWrap.add(icon);

        // ===== CENTER =====

        JPanel center =
            new JPanel();

        center.setOpaque(false);

        center.setLayout(

            new BoxLayout(
                center,
                BoxLayout.Y_AXIS
            )
        );

        JLabel title =
            new JLabel(action);

        title.setFont(

            new Font(
                "Segoe UI",
                Font.BOLD,
                16
            )
        );

        title.setForeground(
            Theme.TEXT_DARK
        );

        JLabel sub =
            new JLabel(
                buildDescription(
                    action,
                    software
                )
            );

        sub.setFont(
            Theme.FONT_SMALL
        );

        sub.setForeground(
            Theme.TEXT_MID
        );

        center.add(title);

        center.add(
            Box.createVerticalStrut(4)
        );

        center.add(sub);

        // ===== TIME =====

        JLabel timeLabel =
            new JLabel(
                formatTimeAgo(time)
            );

        timeLabel.setFont(

            new Font(
                "Segoe UI",
                Font.BOLD,
                12
            )
        );

        timeLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        // ===== ADD =====

        card.add(
            iconWrap,
            BorderLayout.WEST
        );

        card.add(
            center,
            BorderLayout.CENTER
        );

        card.add(
            timeLabel,
            BorderLayout.EAST
        );

        row.add(
            card,
            BorderLayout.CENTER
        );

        return row;
    }

    // ===== DESCRIPTION =====

    private String buildDescription(
            String action,
            String software
    ) {

        if (software == null) {

            software = "System";
        }

        if (action.contains("Add")) {

            return "Added • " + software;

        } else if (
            action.contains("Delete")
        ) {

            return "Deleted • " + software;

        } else if (
            action.contains("Update")
        ) {

            return "Updated • " + software;

        } else if (
            action.contains("Login")
        ) {

            return "User signed into dashboard";

        } else if (
            action.contains("Register")
        ) {

            return "New account registered";
        }

        return software;
    }

    // ===== COLORS =====

    private Color getDotColor(String action) {

        if (action.contains("Add")) {

            return new Color(
                34,
                197,
                94
            );

        } else if (
            action.contains("Delete")
        ) {

            return new Color(
                239,
                68,
                68
            );

        } else if (
            action.contains("Update")
        ) {

            return new Color(
                59,
                130,
                246
            );

        } else if (
            action.contains("Login")
        ) {

            return new Color(
                168,
                85,
                247
            );
        }

        return Theme.TEXT_MID;
    }

    private Color getSoftColor(String action) {

        if (action.contains("Add")) {

            return new Color(
                240,
                253,
                244
            );

        } else if (
            action.contains("Delete")
        ) {

            return new Color(
                254,
                242,
                242
            );

        } else if (
            action.contains("Update")
        ) {

            return new Color(
                239,
                246,
                255
            );

        } else if (
            action.contains("Login")
        ) {

            return new Color(
                250,
                245,
                255
            );
        }

        return new Color(
            245,
            245,
            245
        );
    }

    // ===== SYMBOLS =====

    private String getSymbol(String action) {

        if (action.contains("Add")) {

            return "+";

        } else if (
            action.contains("Delete")
        ) {

            return "−";

        } else if (
            action.contains("Update")
        ) {

            return "↻";

        } else if (
            action.contains("Login")
        ) {

            return "→";
        }

        return "•";
    }

    // ===== TIME AGO =====

    private String formatTimeAgo(String dbTime) {

        try {

            LocalDateTime time =
                LocalDateTime.parse(

                    dbTime.replace(" ", "T")
                );

            Duration duration =
                Duration.between(
                    time,
                    LocalDateTime.now()
                );

            long mins =
                duration.toMinutes();

            long hours =
                duration.toHours();

            long days =
                duration.toDays();

            if (mins < 1) {

                return "Just now";

            } else if (mins < 60) {

                return mins + " min ago";

            } else if (hours < 24) {

                return hours + " hr ago";

            } else if (days < 7) {

                return days + " day ago";

            } else {

                return time.format(

                    DateTimeFormatter.ofPattern(
                        "dd MMM yyyy"
                    )
                );
            }

        } catch (Exception e) {

            return dbTime;
        }
    }

    // ===== EMPTY =====

    private JPanel buildEmptyState() {

        JPanel panel =
            new JPanel();

        panel.setLayout(

            new BoxLayout(
                panel,
                BoxLayout.Y_AXIS
            )
        );

        panel.setOpaque(false);

        panel.setBorder(

            new EmptyBorder(
                70,
                0,
                0,
                0
            )
        );

        JLabel icon =
            new JLabel("📜");

        icon.setFont(

            new Font(
                "Segoe UI Emoji",
                Font.PLAIN,
                52
            )
        );

        icon.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel msg =
            new JLabel(
                "No activity logs available."
            );

        msg.setFont(
            Theme.FONT_SECTION
        );

        msg.setForeground(
            Theme.TEXT_MID
        );

        msg.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel sub =
            new JLabel(

                "System actions will appear here automatically."
            );

        sub.setFont(
            Theme.FONT_BODY
        );

        sub.setForeground(
            Theme.TEXT_LIGHT
        );

        sub.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        panel.add(icon);

        panel.add(
            Box.createVerticalStrut(16)
        );

        panel.add(msg);

        panel.add(
            Box.createVerticalStrut(8)
        );

        panel.add(sub);

        return panel;
    }
}