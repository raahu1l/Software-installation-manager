import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class DeviceSoftwarePanel extends JPanel {

    private JComboBox<String> deviceCombo;

    private JTable table;

    private DefaultTableModel model;

    public DeviceSoftwarePanel() {

        setLayout(new BorderLayout());

        setBackground(Theme.BG);

        add(
            buildTop(),
            BorderLayout.NORTH
        );

        add(
            buildTable(),
            BorderLayout.CENTER
        );

        loadDevices();
    }

    // =====================================================

    private JPanel buildTop() {

        JPanel wrapper =
            new JPanel(
                new BorderLayout()
            );

        wrapper.setOpaque(false);

        wrapper.add(

            Theme.makePageHeader(

                "Device Software View",

                "View installed software per system"
            ),

            BorderLayout.NORTH
        );

        JPanel controls =
            new JPanel(
                new FlowLayout(
                    FlowLayout.LEFT,
                    24,
                    16
                )
            );

        controls.setBackground(
            Theme.WHITE
        );

        controls.setBorder(

            new CompoundBorder(

                new MatteBorder(
                    0,
                    0,
                    1,
                    0,
                    Theme.BORDER
                ),

                new EmptyBorder(
                    0,
                    18,
                    14,
                    18
                )
            )
        );

        JLabel label =
            new JLabel("Select Device");

        label.setFont(
            Theme.FONT_LABEL
        );

        label.setForeground(
            Theme.TEXT_MID
        );

        deviceCombo =
            Theme.comboBox(
                new String[]{}
            );

        deviceCombo.setPreferredSize(
            new Dimension(
                260,
                36
            )
        );

        deviceCombo.addActionListener(e -> {

            String selected =
                (String)
                deviceCombo
                    .getSelectedItem();

            if (selected != null) {

                loadSoftware(selected);
            }
        });

        controls.add(label);

        controls.add(deviceCombo);

        wrapper.add(
            controls,
            BorderLayout.CENTER
        );

        return wrapper;
    }

    // =====================================================

    private JScrollPane buildTable() {

        String[] cols = {

            "No.",
            "Software",
            "Version",
            "Developer",
            "Category",
            "Install Date"
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

        table.setRowHeight(30);

        table.setFont(
            Theme.FONT_BODY
        );

        table.setGridColor(
            Theme.BORDER
        );

        table.setShowVerticalLines(false);

        table.setIntercellSpacing(
            new Dimension(0, 1)
        );

        table.getTableHeader()
            .setFont(
                Theme.FONT_LABEL
            );

        table.getTableHeader()
            .setBackground(

                new Color(
                    245,
                    247,
                    250
                )
            );

        JScrollPane scroll =
            new JScrollPane(table);

        scroll.setBorder(
            new EmptyBorder(0,0,0,0)
        );

        return scroll;
    }

    // =====================================================

    private void loadDevices() {

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                "SELECT DISTINCT system_name FROM software WHERE user_id=? ORDER BY system_name"
            );

            pst.setInt(
                1,
                Session.userId
            );

            ResultSet rs =
                pst.executeQuery();

            deviceCombo.removeAllItems();

            while (rs.next()) {

                deviceCombo.addItem(

                    rs.getString(
                        "system_name"
                    )
                );
            }

            if (deviceCombo.getItemCount() > 0) {

                loadSoftware(

                    deviceCombo
                        .getItemAt(0)
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================

    private void loadSoftware(
            String device
    ) {

        model.setRowCount(0);

        try {

            Connection con =
                DBConnection.getConnection();

            PreparedStatement pst =
                con.prepareStatement(

                "SELECT * FROM software WHERE user_id=? AND system_name=? ORDER BY id DESC"
            );

            pst.setInt(
                1,
                Session.userId
            );

            pst.setString(
                2,
                device
            );

            ResultSet rs =
                pst.executeQuery();

            int no = 1;

            while (rs.next()) {

                model.addRow(

                    new Object[] {

                        no++,

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
                        )
                    }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}