import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class UpdateHistoryPanel extends JPanel {

    public UpdateHistoryPanel() {

        setLayout(new BorderLayout());

        setBackground(Theme.BG);

        add(

            Theme.makePageHeader(

                "Update History",

                "Track all version changes across your software"
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
                20,
                28,
                20,
                28
            )
        );

        JPanel cardList =
            new JPanel();

        cardList.setLayout(

            new BoxLayout(
                cardList,
                BoxLayout.Y_AXIS
            )
        );

        cardList.setOpaque(false);

        boolean hasRecords = false;

        try {

            Connection con =
                DBConnection.getConnection();

            // ✅ USER SPECIFIC HISTORY

            PreparedStatement pst =
                con.prepareStatement(

                    "SELECT h.*, s.id AS sid FROM update_history h " +

                    "LEFT JOIN software s ON h.software_id = s.id " +

                    "WHERE h.user_id=? " +

                    "ORDER BY h.id DESC"
                );

            pst.setInt(
                1,
                Session.userId
            );

            ResultSet rs =
                pst.executeQuery();

            while (rs.next()) {

                hasRecords = true;

                String name =
                    rs.getString(
                        "software_name"
                    );

                // ✅ MARK DELETED

                if (rs.getObject("sid") == null) {

                    name =
                        name + " (Deleted)";
                }

                cardList.add(

                    buildHistoryCard(

                        rs.getInt("id"),

                        name,

                        rs.getString(
                            "old_version"
                        ),

                        rs.getString(
                            "new_version"
                        ),

                        rs.getString(
                            "update_date"
                        )
                    )
                );

                cardList.add(
                    Box.createVerticalStrut(12)
                );
            }

        } catch (Exception e) {

            JLabel err =
                new JLabel(

                    "Could not load history. Make sure the update_history table exists."
                );

            err.setFont(
                Theme.FONT_BODY
            );

            err.setForeground(
                Theme.DANGER
            );

            cardList.add(err);
        }

        if (!hasRecords) {

            cardList.add(
                buildEmptyState()
            );
        }

        JScrollPane scroll =
            new JScrollPane(cardList);

        scroll.setBorder(null);

        scroll.getViewport()
            .setOpaque(false);

        scroll.setOpaque(false);

        wrapper.add(
            scroll,
            BorderLayout.CENTER
        );

        return wrapper;
    }

    private JPanel buildHistoryCard(

            int id,
            String name,
            String oldVer,
            String newVer,
            String date
    ) {

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

        card.setBorder(

            new CompoundBorder(

                new LineBorder(
                    Theme.BORDER,
                    1,
                    true
                ),

                new EmptyBorder(
                    16,
                    18,
                    16,
                    18
                )
            )
        );

        card.setMaximumSize(

            new Dimension(
                Integer.MAX_VALUE,
                80
            )
        );

        JPanel accent =
            new JPanel();

        accent.setBackground(
            Theme.PRIMARY
        );

        accent.setPreferredSize(
            new Dimension(4, 0)
        );

        JPanel center =
            new JPanel(

                new BorderLayout(
                    0,
                    4
                )
            );

        center.setOpaque(false);

        JLabel nameLabel =
            new JLabel(

                name == null
                    ? "Unknown"
                    : name
            );

        nameLabel.setFont(
            Theme.FONT_SECTION
        );

        nameLabel.setForeground(
            Theme.TEXT_DARK
        );

        JPanel versionRow =
            new JPanel(

                new FlowLayout(
                    FlowLayout.LEFT,
                    8,
                    0
                )
            );

        versionRow.setOpaque(false);

        JLabel oldLabel =
            makeVersionBadge(

                oldVer == null
                    ? "?"
                    : oldVer,

                new Color(
                    220,
                    60,
                    60
                )
            );

        JLabel arrow =
            new JLabel("→");

        arrow.setFont(

            new Font(
                "Segoe UI",
                Font.BOLD,
                13
            )
        );

        arrow.setForeground(
            Theme.TEXT_LIGHT
        );

        JLabel newLabel =
            makeVersionBadge(

                newVer == null
                    ? "?"
                    : newVer,

                Theme.SUCCESS
            );

        versionRow.add(oldLabel);

        versionRow.add(arrow);

        versionRow.add(newLabel);

        center.add(
            nameLabel,
            BorderLayout.NORTH
        );

        center.add(
            versionRow,
            BorderLayout.CENTER
        );

        JPanel right =
            new JPanel(

                new BorderLayout(
                    0,
                    4
                )
            );

        right.setOpaque(false);

        JLabel dateLabel =
            new JLabel(

                date == null
                    ? ""
                    : date
            );

        dateLabel.setFont(
            Theme.FONT_SMALL
        );

        dateLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        dateLabel.setHorizontalAlignment(
            SwingConstants.RIGHT
        );

        JLabel idLabel =
            new JLabel("#" + id);

        idLabel.setFont(
            Theme.FONT_SMALL
        );

        idLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        idLabel.setHorizontalAlignment(
            SwingConstants.RIGHT
        );

        right.add(
            dateLabel,
            BorderLayout.NORTH
        );

        right.add(
            idLabel,
            BorderLayout.SOUTH
        );

        card.add(
            accent,
            BorderLayout.WEST
        );

        card.add(
            center,
            BorderLayout.CENTER
        );

        card.add(
            right,
            BorderLayout.EAST
        );

        return card;
    }

    private JLabel makeVersionBadge(
            String text,
            Color color
    ) {

        JLabel lbl =
            new JLabel("v" + text);

        lbl.setFont(
            Theme.FONT_LABEL
        );

        lbl.setForeground(color);

        lbl.setOpaque(true);

        lbl.setBackground(

            new Color(

                color.getRed(),

                color.getGreen(),

                color.getBlue(),

                20
            )
        );

        lbl.setBorder(

            new CompoundBorder(

                new LineBorder(

                    new Color(

                        color.getRed(),

                        color.getGreen(),

                        color.getBlue(),

                        60
                    ),

                    1,
                    true
                ),

                new EmptyBorder(
                    3,
                    8,
                    3,
                    8
                )
            )
        );

        return lbl;
    }

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
                40,
                0,
                0,
                0
            )
        );

        JLabel icon =
            new JLabel("📋");

        icon.setFont(

            new Font(
                "Segoe UI Emoji",
                Font.PLAIN,
                42
            )
        );

        icon.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel msg =
            new JLabel(

                "No version updates recorded yet."
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

                "When you update a software's version, it will appear here."
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
            Box.createVerticalStrut(12)
        );

        panel.add(msg);

        panel.add(
            Box.createVerticalStrut(6)
        );

        panel.add(sub);

        return panel;
    }
}