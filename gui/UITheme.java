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
    }
}