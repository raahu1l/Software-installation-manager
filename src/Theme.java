import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Central design system — all colors, fonts, and component styles live here.
 * Every panel uses this so the UI stays consistent throughout.
 */
public class Theme {

    // ── Colors ────────────────────────────────────────────────
    public static final Color BG          = new Color(245, 247, 251);
    public static final Color SIDEBAR_BG  = new Color(24, 32, 48);
    public static final Color SIDEBAR_HOVER = new Color(40, 52, 74);
    public static final Color SIDEBAR_ACTIVE = new Color(58, 122, 255);
    public static final Color WHITE       = Color.WHITE;
    public static final Color PRIMARY     = new Color(58, 122, 255);
    public static final Color DANGER      = new Color(220, 53, 69);
    public static final Color SUCCESS     = new Color(40, 167, 69);
    public static final Color TEXT_DARK   = new Color(20, 25, 40);
    public static final Color TEXT_MID    = new Color(100, 110, 130);
    public static final Color TEXT_LIGHT  = new Color(160, 170, 190);
    public static final Color BORDER      = new Color(220, 225, 235);
    public static final Color ROW_EVEN    = new Color(250, 251, 255);
    public static final Color ROW_ODD     = Color.WHITE;
    public static final Color ROW_SEL     = new Color(210, 225, 255);

    // ── Fonts ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT   = new Font("Segoe UI", Font.PLAIN, 13);

    // ── Page header ───────────────────────────────────────────
    public static JPanel makePageHeader(String titleText, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(18, 28, 18, 28)
        ));

        JLabel title = new JLabel(titleText);
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_DARK);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_MID);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);
        text.add(title);
        text.add(Box.createVerticalStrut(3));
        text.add(sub);

        header.add(text, BorderLayout.WEST);
        return header;
    }

    // ── Primary action button ─────────────────────────────────
    public static JButton primaryButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(FONT_LABEL);
        btn.setBackground(PRIMARY);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.setOpaque(true);
        return btn;
    }

    // ── Danger button ─────────────────────────────────────────
    public static JButton dangerButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(FONT_LABEL);
        btn.setBackground(DANGER);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.setOpaque(true);
        return btn;
    }

    // ── Ghost / outline button ────────────────────────────────
    public static JButton ghostButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(FONT_LABEL);
        btn.setBackground(WHITE);
        btn.setForeground(TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(8, 18, 8, 18)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // ── Styled text field ─────────────────────────────────────
    public static JTextField inputField() {
        JTextField f = new JTextField();
        f.setFont(FONT_INPUT);
        f.setBackground(WHITE);
        f.setForeground(TEXT_DARK);
        f.setCaretColor(PRIMARY);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(7, 10, 7, 10)
        ));
        return f;
    }

    // ── Styled combo box ──────────────────────────────────────
    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(FONT_INPUT);
        box.setBackground(WHITE);
        box.setForeground(TEXT_DARK);
        box.setFocusable(false);
        box.setBorder(new LineBorder(BORDER, 1, true));
        return box;
    }

    // ── Form label ────────────────────────────────────────────
    public static JLabel formLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MID);
        return lbl;
    }

    // ── Card panel (white rounded box) ────────────────────────
    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    // ── Search bar ────────────────────────────────────────────
    public static JTextField searchField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setFont(FONT_INPUT);
        f.setForeground(TEXT_LIGHT);
        f.setBackground(WHITE);
        f.setCaretColor(PRIMARY);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        // Placeholder behaviour
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setText(placeholder);
                    f.setForeground(TEXT_LIGHT);
                }
            }
        });
        return f;
    }
}