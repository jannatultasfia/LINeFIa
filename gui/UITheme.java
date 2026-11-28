package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UITheme {
    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);

    // Color Palette - Slate & Indigo Modern Theme
    public static final Color NAV_DARK = new Color(15, 23, 42);       // Slate 900
    public static final Color NAV_BAR = new Color(30, 41, 59);        // Slate 800
    public static final Color NAV_HOVER = new Color(51, 65, 85);      // Slate 700
    public static final Color BG_MAIN = new Color(241, 245, 249);      // Slate 100
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER = new Color(226, 232, 240);       // Slate 200
    public static final Color BORDER_DARK = new Color(203, 213, 225);  // Slate 300

    // Text Colors
    public static final Color TEXT_DARK = new Color(15, 23, 42);       // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);   // Slate 500
    public static final Color TEXT_LIGHT = new Color(248, 250, 252);   // Slate 50

    // Action Colors
    public static final Color PRIMARY = new Color(37, 99, 235);        // Blue 600
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216);  // Blue 700
    public static final Color SUCCESS = new Color(16, 185, 129);       // Emerald 500
    public static final Color SUCCESS_HOVER = new Color(5, 150, 105);  // Emerald 600
    public static final Color DANGER = new Color(239, 68, 68);         // Red 500
    public static final Color DANGER_HOVER = new Color(220, 38, 38);   // Red 600
    public static final Color WARNING = new Color(245, 158, 11);       // Amber 500
    public static final Color PURPLE = new Color(139, 92, 246);        // Purple 500

    public static JButton createButton(String text, Color bgColor, Color fgColor, int width, int height) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (width > 0 && height > 0) {
            btn.setPreferredSize(new Dimension(width, height));
        } else {
            btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        }

        // Hover Effect
        Color hoverColor = darkenColor(bgColor, 0.88f);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    public static JButton createButton(String text, Color bgColor, Color fgColor) {
        return createButton(text, bgColor, fgColor, -1, -1);
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(34);
        table.setShowGrid(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setSelectionBackground(new Color(219, 234, 254)); // Soft Blue
        table.setSelectionForeground(TEXT_DARK);
        table.setFillsViewportHeight(true);

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_SUBHEADER);
        header.setBackground(NAV_BAR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_DARK));

        // Alternating row background renderer
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(TEXT_DARK);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    public static JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);

        Border outer = BorderFactory.createLineBorder(BORDER, 1);
        Border inner = new EmptyBorder(16, 18, 16, 18);

        if (title != null && !title.isEmpty()) {
            Border titled = BorderFactory.createTitledBorder(outer, title,
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    FONT_HEADER, TEXT_DARK);
            card.setBorder(BorderFactory.createCompoundBorder(titled, inner));
        } else {
            card.setBorder(BorderFactory.createCompoundBorder(outer, inner));
        }
        return card;
    }

    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return field;
    }

    public static JScrollPane createScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    private static Color darkenColor(Color c, float factor) {
        return new Color(
                Math.max((int)(c.getRed() * factor), 0),
                Math.max((int)(c.getGreen() * factor), 0),
                Math.max((int)(c.getBlue() * factor), 0)
        );
    }
}
        
