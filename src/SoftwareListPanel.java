import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class SoftwareListPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JButton updateBtn,
                    deleteBtn,
                    exportBtn,
                    refreshBtn,
                    pinBtn;

    private JTextField searchField;

    private JFrame parent;

    private int lastClickedRow = -1;

    private JLabel countLabel;

    private java.util.List<Integer> realIds =
        new java.util.ArrayList<>();

    public SoftwareListPanel(JFrame parent) {

        this.parent = parent;

        setLayout(new BorderLayout());

        setBackground(Theme.BG);

        add(
            buildHeader(),
            BorderLayout.NORTH
        );

        add(
            buildTableArea(),
            BorderLayout.CENTER
        );

        loadData(null);
    }

    // =========================================================

    private JPanel buildHeader() {

        JPanel wrapper =
            new JPanel(new BorderLayout());

        wrapper.setOpaque(false);

        wrapper.add(

            Theme.makePageHeader(

                "Software List",

                "Manage all installed software records"
            ),

            BorderLayout.NORTH
        );

        wrapper.add(
            buildTopToolbar(),
            BorderLayout.CENTER
        );

        return wrapper;
    }

    // =========================================================

    private JPanel buildTopToolbar() {

        JPanel outer =
            new JPanel();

        outer.setLayout(

            new BoxLayout(
                outer,
                BoxLayout.Y_AXIS
            )
        );

        outer.setBackground(
            Theme.WHITE
        );

        outer.setBorder(

            new CompoundBorder(

                new MatteBorder(
                    0,
                    0,
                    1,
                    0,
                    Theme.BORDER
                ),

                new EmptyBorder(
                    16,
                    28,
                    16,
                    28
                )
            )
        );

        // ===== SEARCH =====

        searchField =
            Theme.searchField(
                "Search software..."
            );

        searchField.setPreferredSize(
            new Dimension(0, 34)
        );

        searchField.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                34
            )
        );

        searchField
            .getDocument()
            .addDocumentListener(

                new javax.swing.event.DocumentListener() {

                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {

                        triggerSearch();
                    }

                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {

                        triggerSearch();
                    }

                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {

                        triggerSearch();
                    }
                }
            );

        // ===== ACTION BAR =====

        JPanel actions =
            new JPanel(
                new BorderLayout()
            );

        actions.setOpaque(false);

        JPanel left =
            new JPanel(
                new FlowLayout(
                    FlowLayout.LEFT,
                    0,
                    0
                )
            );

        left.setOpaque(false);

        countLabel =
            new JLabel("0 software records");

        countLabel.setFont(
            Theme.FONT_SMALL
        );

        countLabel.setForeground(
            Theme.TEXT_LIGHT
        );

        left.add(countLabel);

        JPanel right =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    10,
                    0
                )
            );

        right.setOpaque(false);

        exportBtn =
            Theme.primaryButton(
                "Export CSV"
            );

        refreshBtn =
            Theme.ghostButton(
                "Refresh"
            );

        updateBtn =
            Theme.ghostButton(
                "Update"
            );

        pinBtn =
            Theme.ghostButton(
                "Pin"
            );

        deleteBtn =
            Theme.dangerButton(
                "Delete"
            );

        updateBtn.setEnabled(false);

        deleteBtn.setEnabled(false);

        pinBtn.setEnabled(false);

        // ===== ACTIONS =====

        exportBtn.addActionListener(
            e -> exportCSV()
        );

        refreshBtn.addActionListener(e -> {

            table.clearSelection();

            lastClickedRow = -1;

            loadData(null);
        });

        updateBtn.addActionListener(
            e -> openUpdateForm()
        );

        pinBtn.addActionListener(
            e -> togglePin()
        );

        deleteBtn.addActionListener(
            e -> deleteSelected()
        );

        right.add(refreshBtn);

        right.add(updateBtn);

        right.add(pinBtn);

        right.add(deleteBtn);

        right.add(exportBtn);

        actions.add(
            left,
            BorderLayout.WEST
        );

        actions.add(
            right,
            BorderLayout.EAST
        );

        outer.add(searchField);

        outer.add(
            Box.createVerticalStrut(14)
        );

        outer.add(actions);

        return outer;
    }

    // =========================================================

    private JScrollPane buildTableArea() {

        String[] cols = {

            "No.",
            "Name",
            "Version",
            "Developer",
            "Category",
            "Install Date",
            "System"
        };

        model =
            new DefaultTableModel(cols, 0) {

                @Override
                public boolean isCellEditable(
                        int row,
                        int col
                ) {

                    return false;
                }
            };

        table =
            new JTable(model);

        table.setFont(
            Theme.FONT_BODY
        );

        table.setRowHeight(30);

        table.setShowVerticalLines(false);

        table.setGridColor(
            Theme.BORDER
        );

        table.setSelectionBackground(
            new Color(
                235,
                243,
                255
            )
        );

        table.setSelectionForeground(
            Theme.TEXT_DARK
        );

        table.setFocusable(false);

        table.setIntercellSpacing(
            new Dimension(0, 1)
        );

        table.getTableHeader()
            .setReorderingAllowed(false);

        JTableHeader header =
            table.getTableHeader();

        header.setBackground(
            new Color(
                245,
                247,
                250
            )
        );

        header.setForeground(
            Theme.TEXT_MID
        );

        header.setFont(
            Theme.FONT_LABEL
        );

        header.setBorder(

            new MatteBorder(
                0,
                0,
                1,
                0,
                Theme.BORDER
            )
        );

        header.setPreferredSize(
            new Dimension(0, 34)
        );

        int[] widths = {

            50,
            190,
            90,
            150,
            120,
            120,
            130
        };

        for (int i = 0;
                i < widths.length;
                i++) {

            table.getColumnModel()
                .getColumn(i)
                .setPreferredWidth(widths[i]);
        }

        table.setDefaultRenderer(

            Object.class,

            new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(

                        JTable t,
                        Object val,
                        boolean sel,
                        boolean foc,
                        int row,
                        int col
                ) {

                    super.getTableCellRendererComponent(

                        t,
                        val,
                        sel,
                        foc,
                        row,
                        col
                    );

                    setBorder(
                        new EmptyBorder(
                            0,
                            12,
                            0,
                            12
                        )
                    );

                    if (sel) {

                        setBackground(

                            new Color(
                                235,
                                243,
                                255
                            )
                        );

                    } else {

                        setBackground(
                            Theme.WHITE
                        );
                    }

                    setForeground(
                        Theme.TEXT_DARK
                    );

                    if (col == 4) {

                        setHorizontalAlignment(
                            CENTER
                        );

                        setFont(

                            new Font(
                                "Segoe UI",
                                Font.BOLD,
                                11
                            )
                        );

                    } else {

                        setHorizontalAlignment(
                            LEFT
                        );

                        setFont(
                            Theme.FONT_BODY
                        );
                    }

                    return this;
                }
            }
        );

        table.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseClicked(
                        MouseEvent e
                ) {

                    int clickedRow =
                        table.rowAtPoint(
                            e.getPoint()
                        );

                    // ===== DOUBLE CLICK =====

                    if (e.getClickCount() == 2
                            && clickedRow != -1) {

                        showDetailsDialog(clickedRow);

                        return;
                    }

                    // ===== SINGLE CLICK =====

                    if (clickedRow
                            == lastClickedRow) {

                        table.clearSelection();

                        lastClickedRow = -1;

                        updateActionButtons(false);

                    } else {

                        lastClickedRow =
                            clickedRow;

                        updateActionButtons(true);
                    }
                }
            }
        );

        table.getSelectionModel()
            .addListSelectionListener(e -> {

                if (!e.getValueIsAdjusting()) {

                    updateActionButtons(

                        table.getSelectedRow()
                            != -1
                    );
                }
            });

        JScrollPane scroll =
            new JScrollPane(table);

        scroll.setBorder(
            new EmptyBorder(0, 0, 0, 0)
        );

        scroll.getViewport()
            .setBackground(Theme.WHITE);

        scroll.getVerticalScrollBar()
            .setUnitIncrement(16);

        return scroll;
    }

    // =========================================================

    private void updateActionButtons(
            boolean selected
    ) {

        updateBtn.setEnabled(selected);

        deleteBtn.setEnabled(selected);

        pinBtn.setEnabled(selected);

        if (selected) {

            int row =
                table.getSelectedRow();

            if (row != -1) {

                String name =
                    model.getValueAt(
                        row,
                        1
                    ).toString();

                if (name.startsWith("★ ")) {

                    pinBtn.setText("Unpin");

                } else {

                    pinBtn.setText("Pin");
                }
            }

        } else {

            pinBtn.setText("Pin");
        }
    }

    // =========================================================

    private void loadData(String keyword) {

        model.setRowCount(0);

        realIds.clear();

        lastClickedRow = -1;

        updateActionButtons(false);

        try {

            Connection con =
                DBConnection.getConnection();

            ResultSet rs;

            if (keyword == null
                    || keyword.isEmpty()) {

                PreparedStatement pst =
                    con.prepareStatement(

                    "SELECT * FROM software WHERE user_id=? ORDER BY id DESC"
                );

                pst.setInt(
                    1,
                    Session.userId
                );

                rs = pst.executeQuery();

            } else {

                PreparedStatement pst =
                    con.prepareStatement(

                    "SELECT * FROM software WHERE user_id=? AND (software_name LIKE ? OR developer LIKE ? OR category LIKE ?) ORDER BY id DESC"
                );

                String kw =
                    "%" + keyword + "%";

                pst.setInt(
                    1,
                    Session.userId
                );

                pst.setString(2, kw);
                pst.setString(3, kw);
                pst.setString(4, kw);

                rs = pst.executeQuery();
            }

            int displayId = 1;

            while (rs.next()) {

                realIds.add(
                    rs.getInt("id")
                );

                model.addRow(

                    new Object[] {

                        displayId++,

                        (rs.getBoolean("pinned")
                            ? "★ "
                            : "") +

                        rs.getString(
                            "software_name"
                        ),

                        rs.getString(
                            "version"
                        ),

                        rs.getString(
                            "developer"
                        ),

                        rs.getString(
                            "category"
                        ),

                        rs.getString(
                            "install_date"
                        ),

                        rs.getString(
                            "system_name"
                        )
                    }
                );
            }

            countLabel.setText(

                model.getRowCount() +
                " software records"
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(

                this,

                "Failed to load data: "
                    + e.getMessage(),

                "Error",

                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================

    private void triggerSearch() {

        String text =
            searchField.getText();

        if (text.equals(
                "Search software..."
            )) {

            text = "";
        }

        loadData(text);
    }

    // =========================================================

    private void openUpdateForm() {

        int row =
            table.getSelectedRow();

        if (row == -1)
            return;

        int realId =
            realIds.get(row);

        String cleanName =
            model.getValueAt(
                row,
                1
            ).toString()
             .replace("★ ", "");

        new AddSoftware(

            parent,

            realId,

            cleanName,

            model.getValueAt(
                row,
                2
            ).toString(),

            model.getValueAt(
                row,
                3
            ).toString(),

            model.getValueAt(
                row,
                4
            ).toString(),

            model.getValueAt(
                row,
                5
            ).toString(),

            model.getValueAt(
                row,
                6
            ).toString()

        ).setVisible(true);

        loadData(null);
    }

    // =========================================================

    private void deleteSelected() {

        int row =
            table.getSelectedRow();

        if (row == -1)
            return;

        int realId =
            realIds.get(row);

        String softwareName =
            model.getValueAt(
                row,
                1
            ).toString()
             .replace("★ ", "");

        int confirm =
            JOptionPane.showConfirmDialog(

                this,

                "Delete selected software?",

                "Confirm Delete",

                JOptionPane.YES_NO_OPTION
            );

        if (confirm != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                    "DELETE FROM software WHERE id=? AND user_id=?"
                );

            pst.setInt(1, realId);

            pst.setInt(
                2,
                Session.userId
            );

            pst.executeUpdate();

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
                "Deleted Software"
            );

            log.setString(
                3,
                softwareName
            );

            log.executeUpdate();

            loadData(null);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Delete failed"
            );
        }
    }

    // =========================================================

    private void togglePin() {

        int row =
            table.getSelectedRow();

        if (row == -1)
            return;

        int realId =
            realIds.get(row);

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement get =
                con.prepareStatement(

                "SELECT pinned FROM software WHERE id=?"
            );

            get.setInt(
                1,
                realId
            );

            ResultSet rs =
                get.executeQuery();

            boolean current =
                false;

            if (rs.next()) {

                current =
                    rs.getBoolean(
                        "pinned"
                    );
            }

            PreparedStatement pst =
                con.prepareStatement(

                "UPDATE software SET pinned=? WHERE id=?"
            );

            pst.setBoolean(
                1,
                !current
            );

            pst.setInt(
                2,
                realId
            );

            pst.executeUpdate();

            loadData(null);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================

    private JPanel detailRow(
            String label,
            String value
    ) {

        JPanel row =
            new JPanel(
                new BorderLayout()
            );

        row.setOpaque(false);

        row.setMaximumSize(

            new Dimension(
                Integer.MAX_VALUE,
                28
            )
        );

        JLabel left =
            new JLabel(label);

        left.setFont(
            Theme.FONT_LABEL
        );

        left.setForeground(
            Theme.TEXT_MID
        );

        JLabel right =
            new JLabel(

                value == null || value.isEmpty()

                ? "-"
                : value
            );

        right.setFont(
            Theme.FONT_BODY
        );

        right.setForeground(
            Theme.TEXT_DARK
        );

        row.add(
            left,
            BorderLayout.WEST
        );

        row.add(
            right,
            BorderLayout.EAST
        );

        return row;
    }

    // =========================================================


private JPanel modernInfoCard(
        String label,
        String value
) {

    JPanel card =
        new JPanel();

    card.setLayout(

        new BoxLayout(
            card,
            BoxLayout.Y_AXIS
        )
    );

    card.setBackground(Color.WHITE);

    card.setBorder(

        new CompoundBorder(

            new LineBorder(
                new Color(228, 232, 238),
                1,
                true
            ),

            new EmptyBorder(
                14,
                16,
                14,
                16
            )
        )
    );

    JLabel top =
        new JLabel(label);

    top.setFont(

        new Font(
            "Segoe UI",
            Font.PLAIN,
            12
        )
    );

    top.setForeground(
        new Color(120, 130, 150)
    );

    JLabel bottom =
        new JLabel(

            value == null || value.isEmpty()

            ? "-"

            : value
        );

    bottom.setFont(

        new Font(
            "Segoe UI",
            Font.BOLD,
            16
        )
    );

    bottom.setForeground(
        new Color(25, 35, 52)
    );

    card.add(top);

    card.add(
        Box.createVerticalStrut(6)
    );

    card.add(bottom);

    return card;
}

    private void showDetailsDialog(
        int row
) {

    int realId =
        realIds.get(row);

    try {

        Connection con =
            DBConnection.getConnection();

        PreparedStatement pst =
            con.prepareStatement(

            "SELECT * FROM software WHERE id=?"
        );

        pst.setInt(
            1,
            realId
        );

        ResultSet rs =
            pst.executeQuery();

        if (rs.next()) {

            JDialog dialog =
                new JDialog(
                    parent,
                    "Software Details",
                    true
                );

            dialog.setSize(520, 520);

            dialog.setLocationRelativeTo(parent);

            dialog.setLayout(
                new BorderLayout()
            );

            dialog.getContentPane()
                .setBackground(
                    new Color(245, 247, 250)
                );

            // =================================================
            // MAIN PANEL
            // =================================================

            JPanel main =
                new JPanel();

            main.setLayout(

                new BoxLayout(
                    main,
                    BoxLayout.Y_AXIS
                )
            );

            main.setBackground(
                new Color(245, 247, 250)
            );

            main.setBorder(

                new EmptyBorder(
                    24,
                    24,
                    24,
                    24
                )
            );

            // =================================================
            // HEADER CARD
            // =================================================

            JPanel headerCard =
                new JPanel(
                    new BorderLayout()
                );

            headerCard.setBackground(
                Color.WHITE
            );

            headerCard.setBorder(

                new CompoundBorder(

                    new LineBorder(
                        new Color(228, 232, 238),
                        1,
                        true
                    ),

                    new EmptyBorder(
                        22,
                        24,
                        22,
                        24
                    )
                )
            );

            JLabel title =
                new JLabel(

                    rs.getString(
                        "software_name"
                    )
                );

            title.setFont(

                new Font(
                    "Segoe UI",
                    Font.BOLD,
                    28
                )
            );

            title.setForeground(
                new Color(20, 28, 45)
            );

            JLabel version =
                new JLabel(

                    "Version "
                    + rs.getString("version")
                );

            version.setFont(

                new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    14
                )
            );

            version.setForeground(
                new Color(120, 130, 150)
            );

            JPanel titleWrap =
                new JPanel();

            titleWrap.setOpaque(false);

            titleWrap.setLayout(

                new BoxLayout(
                    titleWrap,
                    BoxLayout.Y_AXIS
                )
            );

            titleWrap.add(title);

            titleWrap.add(
                Box.createVerticalStrut(4)
            );

            titleWrap.add(version);

            JLabel categoryBadge =
                new JLabel(
                    rs.getString("category")
                );

            categoryBadge.setOpaque(true);

            categoryBadge.setBackground(

                new Color(
                    235,
                    243,
                    255
                )
            );

            categoryBadge.setForeground(

                new Color(
                    40,
                    90,
                    180
                )
            );

            categoryBadge.setFont(

                new Font(
                    "Segoe UI",
                    Font.BOLD,
                    12
                )
            );

            categoryBadge.setBorder(

                new EmptyBorder(
                    8,
                    14,
                    8,
                    14
                )
            );

            headerCard.add(
                titleWrap,
                BorderLayout.WEST
            );

            headerCard.add(
                categoryBadge,
                BorderLayout.EAST
            );

            // =================================================
            // INFO GRID
            // =================================================

            JPanel infoGrid =
                new JPanel(
                    new GridLayout(
                        2,
                        2,
                        14,
                        14
                    )
                );

            infoGrid.setOpaque(false);

            infoGrid.setBorder(

                new EmptyBorder(
                    18,
                    0,
                    0,
                    0
                )
            );

            infoGrid.add(

                modernInfoCard(

                    "Developer",

                    rs.getString(
                        "developer"
                    )
                )
            );

            infoGrid.add(

                modernInfoCard(

                    "System",

                    rs.getString(
                        "system_name"
                    )
                )
            );

            infoGrid.add(

                modernInfoCard(

                    "Install Date",

                    rs.getString(
                        "install_date"
                    )
                )
            );

            infoGrid.add(

                modernInfoCard(

                    "Pinned",

                    rs.getBoolean("pinned")

                    ? "Yes"

                    : "No"
                )
            );

            // =================================================
            // NOTES CARD
            // =================================================

            JPanel notesCard =
                new JPanel(
                    new BorderLayout()
                );

            notesCard.setBackground(
                Color.WHITE
            );

            notesCard.setBorder(

                new CompoundBorder(

                    new LineBorder(
                        new Color(228, 232, 238),
                        1,
                        true
                    ),

                    new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                    )
                )
            );

            JLabel notesTitle =
                new JLabel("Notes");

            notesTitle.setFont(

                new Font(
                    "Segoe UI",
                    Font.BOLD,
                    16
                )
            );

            notesTitle.setForeground(
                Theme.TEXT_DARK
            );

            JTextArea notes =
                new JTextArea(

                    rs.getString("notes") == null
                    || rs.getString("notes").trim().isEmpty()

                    ? "No notes added."

                    : rs.getString("notes")
                );

            notes.setWrapStyleWord(true);

            notes.setLineWrap(true);

            notes.setEditable(false);

            notes.setFocusable(false);

            notes.setOpaque(false);

            notes.setFont(
                Theme.FONT_BODY
            );

            notes.setForeground(
                Theme.TEXT_MID
            );

            notes.setBorder(

                new EmptyBorder(
                    10,
                    0,
                    0,
                    0
                )
            );

            notesCard.add(
                notesTitle,
                BorderLayout.NORTH
            );

            notesCard.add(
                notes,
                BorderLayout.CENTER
            );

            // =================================================
            // CLOSE BUTTON
            // =================================================

            JButton closeBtn =
                Theme.primaryButton(
                    "Close"
                );

            closeBtn.setPreferredSize(
                new Dimension(
                    110,
                    38
                )
            );

            closeBtn.addActionListener(
                e -> dialog.dispose()
            );

            JPanel btnWrap =
                new JPanel(
                    new FlowLayout(
                        FlowLayout.RIGHT,
                        0,
                        0
                    )
                );

            btnWrap.setOpaque(false);

            btnWrap.setBorder(

                new EmptyBorder(
                    18,
                    0,
                    0,
                    0
                )
            );

            btnWrap.add(closeBtn);

            // =================================================
            // ADD ALL
            // =================================================

            main.add(headerCard);

            main.add(infoGrid);

            main.add(
                Box.createVerticalStrut(18)
            );

            main.add(notesCard);

            main.add(btnWrap);

            dialog.add(main);

            dialog.setVisible(true);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    // =========================================================

    private void exportCSV() {

        JFileChooser chooser =
            new JFileChooser();

        chooser.setDialogTitle(
            "Save CSV File"
        );

        chooser.setSelectedFile(
            new File("software_export.csv")
        );

        int option =
            chooser.showSaveDialog(this);

        if (option != JFileChooser.APPROVE_OPTION) {

            return;
        }

        File file =
            chooser.getSelectedFile();

        try {

            FileWriter writer =
                new FileWriter(file);

            writer.write(

                "ID,Software Name,Version,Developer,Category,Install Date,System\n"
            );

            for (int i = 0;
                    i < table.getRowCount();
                    i++) {

                for (int j = 0;
                        j < table.getColumnCount();
                        j++) {

                    String value =
                        table.getValueAt(i, j)
                            .toString()
                            .replace("★ ", "");

                    writer.write(value);

                    if (j != table.getColumnCount() - 1) {

                        writer.write(",");
                    }
                }

                writer.write("\n");
            }

            writer.close();

            JOptionPane.showMessageDialog(

                this,

                "CSV exported successfully!",

                "Export Complete",

                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(

                this,

                "CSV export failed",

                "Error",

                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}