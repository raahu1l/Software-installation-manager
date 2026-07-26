import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {

        setLayout(new BorderLayout());

        setBackground(Theme.BG);

        add(

            Theme.makePageHeader(

                "Overview",

                "Monitor installed packages, systems, and activity"
            ),

            BorderLayout.NORTH
        );

        add(
            buildBody(),
            BorderLayout.CENTER
        );
    }

    // =========================================================

    private JPanel buildBody() {

        JPanel body =
            new JPanel(new BorderLayout());

        body.setBackground(
            Theme.BG
        );

        body.setBorder(

            new EmptyBorder(
                20,
                24,
                20,
                24
            )
        );

        // ===== STATS =====

        JPanel statsRow =
            new JPanel(

                new GridLayout(
                    1,
                    3,
                    14,
                    0
                )
            );

        statsRow.setOpaque(false);

        int[] counts = fetchCounts();

        statsRow.add(

            statCard(

                "Installed Packages",

                String.valueOf(counts[0]),

                new Color(58, 122, 255),

                "tracked software"
            )
        );

        statsRow.add(

            statCard(

                "Package Types",

                String.valueOf(counts[1]),

                new Color(40, 167, 69),

                "active categories"
            )
        );

        statsRow.add(

            statCard(

                "Registered Systems",

                String.valueOf(counts[2]),

                new Color(220, 100, 50),

                "connected devices"
            )
        );

        // =====================================================
        // LEFT COLUMN
        // =====================================================

        JPanel leftColumn =
            new JPanel();

        leftColumn.setOpaque(false);

        leftColumn.setLayout(

            new BoxLayout(
                leftColumn,
                BoxLayout.Y_AXIS
            )
        );

        leftColumn.add(
            buildPinnedPackages()
        );

        leftColumn.add(
            Box.createVerticalStrut(14)
        );

        leftColumn.add(
            buildRecentPackages()
        );

        // =====================================================
        // RIGHT COLUMN
        // =====================================================

        JPanel rightColumn =
            new JPanel(
                new BorderLayout()
            );

        rightColumn.setOpaque(false);

        rightColumn.add(
            buildCategorySummary(),
            BorderLayout.NORTH
        );

        // =====================================================
        // MAIN GRID
        // =====================================================

        JPanel grid =
            new JPanel(

                new GridLayout(
                    1,
                    2,
                    14,
                    0
                )
            );

        grid.setOpaque(false);

        grid.add(leftColumn);

        grid.add(rightColumn);

        JPanel wrapper =
            new JPanel();

        wrapper.setLayout(

            new BoxLayout(
                wrapper,
                BoxLayout.Y_AXIS
            )
        );

        wrapper.setOpaque(false);

        wrapper.add(statsRow);

        wrapper.add(
            Box.createVerticalStrut(14)
        );

        wrapper.add(grid);

        JPanel outer =
            new JPanel(new BorderLayout());

        outer.setOpaque(false);

        outer.add(
            wrapper,
            BorderLayout.NORTH
        );

        return outer;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel statCard(

            String title,
            String value,
            Color accent,
            String sub
    ) {

        JPanel card =
            Theme.card();

        card.setLayout(
            new BorderLayout()
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

        JLabel titleLabel =
            new JLabel(title);

        titleLabel.setFont(
            Theme.FONT_SMALL
        );

        titleLabel.setForeground(
            Theme.TEXT_MID
        );

        JLabel valueLabel =
            new JLabel(value);

        valueLabel.setFont(

            new Font(
                "Segoe UI",
                Font.BOLD,
                30
            )
        );

        valueLabel.setForeground(accent);

        JLabel subLabel =
            new JLabel(sub);

        subLabel.setFont(
            Theme.FONT_SMALL
        );

        subLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        JPanel text =
            new JPanel();

        text.setOpaque(false);

        text.setLayout(

            new BoxLayout(
                text,
                BoxLayout.Y_AXIS
            )
        );

        text.add(titleLabel);

        text.add(
            Box.createVerticalStrut(6)
        );

        text.add(valueLabel);

        text.add(
            Box.createVerticalStrut(2)
        );

        text.add(subLabel);

        card.add(text);

        return card;
    }

    // =========================================================
    // PINNED PACKAGES
    // =========================================================

    private JPanel buildPinnedPackages() {

        JPanel card =
            Theme.card();

        card.setLayout(
            new BorderLayout()
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

        JLabel title =
            new JLabel("Pinned Packages");

        title.setFont(
            Theme.FONT_SECTION
        );

        title.setForeground(
            Theme.TEXT_DARK
        );

        title.setBorder(

            new EmptyBorder(
                0,
                0,
                12,
                0
            )
        );

        JPanel list =
            new JPanel();

        list.setOpaque(false);

        list.setLayout(

            new BoxLayout(
                list,
                BoxLayout.Y_AXIS
            )
        );

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                    "SELECT software_name, category, version FROM software WHERE user_id=? AND pinned=TRUE ORDER BY id DESC LIMIT 5"
                );

            pst.setInt(
                1,
                Session.userId
            );

            ResultSet rs =
                pst.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                list.add(

                    packageRow(

                        rs.getString(
                            "software_name"
                        ),

                        rs.getString(
                            "category"
                        ),

                        rs.getString(
                            "version"
                        ),

                        true
                    )
                );

                list.add(
                    Box.createVerticalStrut(10)
                );
            }

            if (!found) {

                JLabel empty =
                    new JLabel(
                        "No pinned software."
                    );

                empty.setForeground(
                    Theme.TEXT_LIGHT
                );

                empty.setFont(
                    Theme.FONT_BODY
                );

                list.add(empty);
            }

        } catch (Exception e) {

            list.add(
                new JLabel(
                    "Could not load data."
                )
            );
        }

        card.add(
            title,
            BorderLayout.NORTH
        );

        card.add(
            list,
            BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // RECENT PACKAGES
    // =========================================================

    private JPanel buildRecentPackages() {

        JPanel card =
            Theme.card();

        card.setLayout(
            new BorderLayout()
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

        JLabel title =
            new JLabel("Recently Added");

        title.setFont(
            Theme.FONT_SECTION
        );

        title.setForeground(
            Theme.TEXT_DARK
        );

        title.setBorder(

            new EmptyBorder(
                0,
                0,
                12,
                0
            )
        );

        JPanel list =
            new JPanel();

        list.setOpaque(false);

        list.setLayout(

            new BoxLayout(
                list,
                BoxLayout.Y_AXIS
            )
        );

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                    "SELECT software_name, category, version FROM software WHERE user_id=? ORDER BY id DESC LIMIT 3"
                );

            pst.setInt(
                1,
                Session.userId
            );

            ResultSet rs =
                pst.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                list.add(

                    packageRow(

                        rs.getString(
                            "software_name"
                        ),

                        rs.getString(
                            "category"
                        ),

                        rs.getString(
                            "version"
                        ),

                        false
                    )
                );

                list.add(
                    Box.createVerticalStrut(10)
                );
            }

            if (!found) {

                JLabel empty =
                    new JLabel(
                        "No packages available."
                    );

                empty.setForeground(
                    Theme.TEXT_LIGHT
                );

                empty.setFont(
                    Theme.FONT_BODY
                );

                list.add(empty);
            }

        } catch (Exception e) {

            list.add(
                new JLabel(
                    "Could not load data."
                )
            );
        }

        card.add(
            title,
            BorderLayout.NORTH
        );

        card.add(
            list,
            BorderLayout.CENTER
        );

        return card;
    }

    private JPanel packageRow(
            String name,
            String category,
            String version,
            boolean pinned
    ) {

        JPanel row =
            new JPanel(
                new BorderLayout()
            );

        row.setOpaque(false);

        row.setMaximumSize(

            new Dimension(
                Integer.MAX_VALUE,
                40
            )
        );

        JLabel nameLabel =
            new JLabel(

                pinned
                    ? "★ " + name
                    : name
            );

        nameLabel.setFont(
            Theme.FONT_BODY
        );

        nameLabel.setForeground(
            Theme.TEXT_DARK
        );

        JPanel right =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    8,
                    0
                )
            );

        right.setOpaque(false);

        JLabel badge =
            new JLabel(category);

        badge.setOpaque(true);

        badge.setBackground(

            new Color(
                240,
                244,
                250
            )
        );

        badge.setForeground(
            Theme.TEXT_MID
        );

        badge.setBorder(

            new EmptyBorder(
                4,
                8,
                4,
                8
            )
        );

        badge.setFont(

            new Font(
                "Segoe UI",
                Font.BOLD,
                10
            )
        );

        JLabel versionLabel =
            new JLabel("v" + version);

        versionLabel.setFont(
            Theme.FONT_SMALL
        );

        versionLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        right.add(badge);

        right.add(versionLabel);

        row.add(
            nameLabel,
            BorderLayout.WEST
        );

        row.add(
            right,
            BorderLayout.EAST
        );

        return row;
    }

    // =========================================================
    // CATEGORY SUMMARY
    // =========================================================

    private JPanel buildCategorySummary() {

        JPanel card =
            Theme.card();

        card.setLayout(
            new BorderLayout()
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

        JLabel title =
            new JLabel(
                "Category Breakdown"
            );

        title.setFont(
            Theme.FONT_SECTION
        );

        title.setForeground(
            Theme.TEXT_DARK
        );

        title.setBorder(

            new EmptyBorder(
                0,
                0,
                12,
                0
            )
        );

        JPanel list =
            new JPanel();

        list.setOpaque(false);

        list.setLayout(

            new BoxLayout(
                list,
                BoxLayout.Y_AXIS
            )
        );

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                    "SELECT category, COUNT(*) as cnt FROM software WHERE user_id=? GROUP BY category ORDER BY cnt DESC"
                );

            pst.setInt(
                1,
                Session.userId
            );

            ResultSet rs =
                pst.executeQuery();

            while (rs.next()) {

                list.add(

                    categoryRow(

                        rs.getString(
                            "category"
                        ),

                        rs.getInt("cnt")
                    )
                );

                list.add(
                    Box.createVerticalStrut(10)
                );
            }

        } catch (Exception e) {

            list.add(
                new JLabel(
                    "Could not load data."
                )
            );
        }

        card.add(
            title,
            BorderLayout.NORTH
        );

        card.add(
            list,
            BorderLayout.CENTER
        );

        return card;
    }

    private JPanel categoryRow(
            String category,
            int count
    ) {

        JPanel row =
            new JPanel(
                new BorderLayout(
                    10,
                    0
                )
            );

        row.setOpaque(false);

        row.setMaximumSize(

            new Dimension(
                Integer.MAX_VALUE,
                28
            )
        );

        JLabel name =
            new JLabel(category);

        name.setFont(
            Theme.FONT_BODY
        );

        name.setForeground(
            Theme.TEXT_DARK
        );

        JProgressBar bar =
            new JProgressBar();

        bar.setValue(
            Math.min(
                count * 20,
                100
            )
        );

        bar.setBorderPainted(false);

        bar.setPreferredSize(
            new Dimension(80, 6)
        );

        JLabel countLabel =
            new JLabel(
                String.valueOf(count)
            );

        countLabel.setFont(
            Theme.FONT_SMALL
        );

        countLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        JPanel right =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    8,
                    0
                )
            );

        right.setOpaque(false);

        right.add(bar);

        right.add(countLabel);

        row.add(
            name,
            BorderLayout.WEST
        );

        row.add(
            right,
            BorderLayout.EAST
        );

        return row;
    }

    // =========================================================
    // COUNTS
    // =========================================================

    private int[] fetchCounts() {

        int[] r = {0, 0, 0};

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst1 =
                con.prepareStatement(

                    "SELECT COUNT(*) FROM software WHERE user_id=?"
                );

            pst1.setInt(
                1,
                Session.userId
            );

            ResultSet rs1 =
                pst1.executeQuery();

            if (rs1.next()) {

                r[0] = rs1.getInt(1);
            }

            PreparedStatement pst2 =
                con.prepareStatement(

                    "SELECT COUNT(DISTINCT category) FROM software WHERE user_id=?"
                );

            pst2.setInt(
                1,
                Session.userId
            );

            ResultSet rs2 =
                pst2.executeQuery();

            if (rs2.next()) {

                r[1] = rs2.getInt(1);
            }

            PreparedStatement pst3 =
                con.prepareStatement(

                    "SELECT COUNT(DISTINCT system_name) FROM software WHERE user_id=?"
                );

            pst3.setInt(
                1,
                Session.userId
            );

            ResultSet rs3 =
                pst3.executeQuery();

            if (rs3.next()) {

                r[2] = rs3.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return r;
    }
}