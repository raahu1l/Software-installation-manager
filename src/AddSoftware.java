import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Used for both Add and Update.
 * softwareId == -1  →  Insert mode
 * softwareId != -1  →  Update mode
 */
public class AddSoftware extends JDialog {

    private JTextField nameField,
            versionField,
            developerField,
            systemField,
            dateField;

    private JTextArea notesArea;

    private JComboBox<String> categoryCombo;

    private JTextField customCategoryField;

    private JCheckBox customCategoryCheck;

    private final int softwareId;

    private final String oldVersion;

    private static final String[] PRESET_CATEGORIES = {

        "IDE",
        "Browser",
        "Utility",
        "Antivirus",
        "Media Player",
        "Office Suite",
        "Database Tool",
        "Graphics",
        "Game",
        "Other"
    };

    public AddSoftware(
            JFrame parent,
            int id,
            String name,
            String version,
            String developer,
            String category,
            String date,
            String system
    ) {

        super(
            parent,
            id == -1
                ? "Add Software"
                : "Update Software",
            true
        );

        this.softwareId = id;

        this.oldVersion = version;

        setSize(520, 690);

        setResizable(false);

        setLocationRelativeTo(parent);

        setLayout(new BorderLayout());

        getContentPane().setBackground(
            Theme.WHITE
        );

        add(
            buildHeader(id == -1),
            BorderLayout.NORTH
        );

        JScrollPane scrollPane =
            new JScrollPane(buildForm());

        scrollPane.setBorder(null);

        scrollPane
            .getVerticalScrollBar()
            .setUnitIncrement(12);

        add(
            scrollPane,
            BorderLayout.CENTER
        );

        add(
            buildFooter(id == -1),
            BorderLayout.SOUTH
        );

        if (id != -1) {

            nameField.setText(name);

            versionField.setText(version);

            developerField.setText(developer);

            systemField.setText(system);

            dateField.setText(date);

            prefillCategory(category);

            loadNotes();

        } else {

            dateField.setText(
                LocalDate.now().toString()
            );
        }
    }

    private JPanel buildHeader(boolean isAdd) {

        JPanel header =
            new JPanel(new BorderLayout());

        header.setBackground(
            Theme.PRIMARY
        );

        header.setBorder(
            new EmptyBorder(
                20,
                24,
                20,
                24
            )
        );

        JLabel title =
            new JLabel(
                isAdd
                    ? "Add New Software"
                    : "Update Software"
            );

        title.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                18
            )
        );

        title.setForeground(Color.WHITE);

        JLabel sub =
            new JLabel(
                isAdd
                    ? "Fill in the details below"
                    : "Edit the details and save"
            );

        sub.setFont(
            Theme.FONT_SMALL
        );

        sub.setForeground(
            new Color(180, 200, 255)
        );

        JPanel text =
            new JPanel();

        text.setLayout(
            new BoxLayout(
                text,
                BoxLayout.Y_AXIS
            )
        );

        text.setOpaque(false);

        text.add(title);

        text.add(
            Box.createVerticalStrut(4)
        );

        text.add(sub);

        header.add(text);

        return header;
    }

    private JPanel buildForm() {

        JPanel form =
            new JPanel();

        form.setLayout(
            new BoxLayout(
                form,
                BoxLayout.Y_AXIS
            )
        );

        form.setBackground(
            Theme.WHITE
        );

        form.setBorder(
            new EmptyBorder(
                22,
                28,
                10,
                28
            )
        );

        nameField =
            Theme.inputField();

        versionField =
            Theme.inputField();

        developerField =
            Theme.inputField();

        systemField =
            Theme.inputField();

        dateField =
            Theme.inputField();

        categoryCombo =
            Theme.comboBox(
                PRESET_CATEGORIES
            );

        customCategoryCheck =
            new JCheckBox(
                "Use custom category"
            );

        customCategoryCheck.setFont(
            Theme.FONT_SMALL
        );

        customCategoryCheck.setForeground(
            Theme.TEXT_MID
        );

        customCategoryCheck.setBackground(
            Theme.WHITE
        );

        customCategoryCheck.setFocusPainted(false);

        customCategoryField =
            Theme.inputField();

        customCategoryField.setVisible(false);

        customCategoryField.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                36
            )
        );

        customCategoryCheck.addActionListener(e -> {

            boolean custom =
                customCategoryCheck
                    .isSelected();

            categoryCombo.setVisible(
                !custom
            );

            customCategoryField.setVisible(
                custom
            );

            revalidate();

            repaint();
        });

        form.add(
            fieldRow(
                "Software Name *",
                nameField
            )
        );

        form.add(
            Box.createVerticalStrut(14)
        );

        form.add(
            fieldRow(
                "Version *",
                versionField
            )
        );

        form.add(
            Box.createVerticalStrut(14)
        );

        form.add(
            fieldRow(
                "Developer",
                developerField
            )
        );

        form.add(
            Box.createVerticalStrut(14)
        );

        JPanel catWrapper =
            new JPanel(
                new BorderLayout()
            );

        catWrapper.setOpaque(false);

        catWrapper.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                80
            )
        );

        JPanel catLabel =
            new JPanel(
                new BorderLayout()
            );

        catLabel.setOpaque(false);

        JLabel lbl =
            Theme.formLabel(
                "Category"
            );

        catLabel.add(
            lbl,
            BorderLayout.WEST
        );

        catLabel.add(
            customCategoryCheck,
            BorderLayout.EAST
        );

        categoryCombo.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                36
            )
        );

        catWrapper.add(
            catLabel,
            BorderLayout.NORTH
        );

        catWrapper.add(
            categoryCombo,
            BorderLayout.CENTER
        );

        catWrapper.add(
            customCategoryField,
            BorderLayout.SOUTH
        );

        form.add(catWrapper);

        form.add(
            Box.createVerticalStrut(14)
        );

        form.add(
            fieldRow(
                "System / Machine",
                systemField
            )
        );

        form.add(
            Box.createVerticalStrut(14)
        );

        form.add(
            fieldRow(
                "Install Date (YYYY-MM-DD)",
                dateField
            )
        );

        form.add(
            Box.createVerticalStrut(14)
        );

        JPanel notesRow =
            new JPanel(
                new BorderLayout(0, 5)
            );

        notesRow.setOpaque(false);

        notesRow.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                150
            )
        );

        notesRow.add(

            Theme.formLabel(
                "Developer Notes"
            ),

            BorderLayout.NORTH
        );

        notesArea =
            new JTextArea();

        notesArea.setLineWrap(true);

        notesArea.setWrapStyleWord(true);

        notesArea.setFont(
            Theme.FONT_BODY
        );

        notesArea.setBorder(

            new CompoundBorder(

                new LineBorder(
                    Theme.BORDER,
                    1,
                    true
                ),

                new EmptyBorder(
                    10,
                    10,
                    10,
                    10
                )
            )
        );

        JScrollPane notesScroll =
            new JScrollPane(notesArea);

        notesScroll.setBorder(null);

        notesScroll.setPreferredSize(
            new Dimension(
                0,
                110
            )
        );

        notesRow.add(
            notesScroll,
            BorderLayout.CENTER
        );

        form.add(notesRow);

        return form;
    }

    private JPanel fieldRow(
            String labelText,
            JTextField field
    ) {

        JPanel row =
            new JPanel(
                new BorderLayout(0, 5)
            );

        row.setOpaque(false);

        row.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                62
            )
        );

        row.add(
            Theme.formLabel(labelText),
            BorderLayout.NORTH
        );

        field.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                36
            )
        );

        row.add(
            field,
            BorderLayout.CENTER
        );

        return row;
    }

    private void prefillCategory(String category) {

        for (String preset :
                PRESET_CATEGORIES) {

            if (preset.equalsIgnoreCase(category)) {

                categoryCombo.setSelectedItem(
                    preset
                );

                return;
            }
        }

        customCategoryCheck.setSelected(true);

        categoryCombo.setVisible(false);

        customCategoryField.setVisible(true);

        customCategoryField.setText(category);
    }

    private void loadNotes() {

        if (softwareId == -1)
            return;

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                "SELECT notes FROM software WHERE id=?"
            );

            pst.setInt(
                1,
                softwareId
            );

            ResultSet rs =
                pst.executeQuery();

            if (rs.next()) {

                String notes =
                    rs.getString("notes");

                if (notes != null) {

                    notesArea.setText(notes);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private JPanel buildFooter(boolean isAdd) {

        JPanel footer =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    12,
                    0
                )
            );

        footer.setBackground(
            new Color(
                248,
                249,
                252
            )
        );

        footer.setBorder(

            new CompoundBorder(

                new MatteBorder(
                    1,
                    0,
                    0,
                    0,
                    Theme.BORDER
                ),

                new EmptyBorder(
                    14,
                    24,
                    14,
                    24
                )
            )
        );

        JButton cancelBtn =
            Theme.ghostButton("Cancel");

        JButton saveBtn =
            Theme.primaryButton(

                isAdd
                    ? "Add Software"
                    : "Save Changes"
            );

        cancelBtn.addActionListener(
            e -> dispose()
        );

        saveBtn.addActionListener(
            e -> save()
        );

        footer.add(cancelBtn);

        footer.add(saveBtn);

        return footer;
    }

    private void save() {

        String name =
            nameField.getText().trim();

        String version =
            versionField.getText().trim();

        String dev =
            developerField.getText().trim();

        String system =
            systemField.getText().trim();

        String date =
            dateField.getText().trim();

        String notes =
            notesArea.getText().trim();

        String cat =
            customCategoryCheck.isSelected()

                ? customCategoryField
                    .getText()
                    .trim()

                : categoryCombo
                    .getSelectedItem()
                    .toString();

        if (name.isEmpty()) {

            showError(
                "Software name is required."
            );

            return;
        }

        if (version.isEmpty()) {

            showError(
                "Version is required."
            );

            return;
        }

        if (cat.isEmpty()) {

            showError(
                "Category is required."
            );

            return;
        }

        try {

            Connection con =
                DBConnection.getConnection();

            // =====================================================
            // DUPLICATE DETECTION
            // =====================================================

            PreparedStatement check =
                con.prepareStatement(

                softwareId == -1

                ? "SELECT id FROM software WHERE software_name=? AND system_name=? AND user_id=?"

                : "SELECT id FROM software WHERE software_name=? AND system_name=? AND user_id=? AND id!=?"
            );

            check.setString(1, name);

            check.setString(2, system);

            check.setInt(
                3,
                Session.userId
            );

            if (softwareId != -1) {

                check.setInt(
                    4,
                    softwareId
                );
            }

            ResultSet duplicate =
                check.executeQuery();

            if (duplicate.next()) {

                JOptionPane.showMessageDialog(

                    this,

                    "This software already exists on the selected system.",

                    "Duplicate Software",

                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // =====================================================
            // INSERT
            // =====================================================

            if (softwareId == -1) {

                PreparedStatement pst =
                    con.prepareStatement(

                    "INSERT INTO software (software_name, version, developer, category, install_date, system_name, notes, user_id) VALUES (?,?,?,?,?,?,?,?)"
                );

                pst.setString(1, name);
                pst.setString(2, version);
                pst.setString(3, dev);
                pst.setString(4, cat);
                pst.setString(5, date);
                pst.setString(6, system);
                pst.setString(7, notes);

                pst.setInt(
                    8,
                    Session.userId
                );

                pst.executeUpdate();

                // ===== ACTIVITY LOG =====

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
                    "Added Software"
                );

                log.setString(
                    3,
                    name
                );

                log.executeUpdate();

            } else {

                // =================================================
                // UPDATE
                // =================================================

                PreparedStatement pst =
                    con.prepareStatement(

                    "UPDATE software SET software_name=?, version=?, developer=?, category=?, install_date=?, system_name=?, notes=? WHERE id=?"
                );

                pst.setString(1, name);
                pst.setString(2, version);
                pst.setString(3, dev);
                pst.setString(4, cat);
                pst.setString(5, date);
                pst.setString(6, system);
                pst.setString(7, notes);
                pst.setInt(8, softwareId);

                pst.executeUpdate();

                // ===== UPDATE HISTORY =====

                if (oldVersion != null
                        && !oldVersion.equals(version)) {

                    PreparedStatement hist =
                        con.prepareStatement(

                        "INSERT INTO update_history (software_id, software_name, old_version, new_version, update_date, user_id) VALUES (?,?,?,?,?,?)"
                    );

                    hist.setInt(1, softwareId);
                    hist.setString(2, name);
                    hist.setString(3, oldVersion);
                    hist.setString(4, version);

                    hist.setString(
                        5,
                        LocalDate.now().toString()
                    );

                    hist.setInt(
                        6,
                        Session.userId
                    );

                    hist.executeUpdate();
                }

                // ===== ACTIVITY LOG =====

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
                    "Updated Software"
                );

                log.setString(
                    3,
                    name
                );

                log.executeUpdate();
            }

            JOptionPane.showMessageDialog(

                this,

                softwareId == -1

                    ? "Software added successfully!"

                    : "Software updated successfully!",

                "Success",

                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (Exception e) {

            showError(
                "Database error: "
                    + e.getMessage()
            );
        }
    }

    private void showError(String msg) {

        JOptionPane.showMessageDialog(

            this,
            msg,
            "Validation Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}