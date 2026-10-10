package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/** Central visual system for Taskator. */
public final class Theme {
    public static final Color BG = new Color(8, 13, 27);
    public static final Color SURFACE = new Color(16, 24, 43);
    public static final Color SURFACE_2 = new Color(21, 31, 53);
    public static final Color SURFACE_3 = new Color(27, 38, 64);
    public static final Color SIDEBAR = new Color(7, 12, 25);
    public static final Color TEXT = new Color(242, 245, 252);
    public static final Color MUTED = new Color(151, 163, 186);
    public static final Color BORDER = new Color(39, 52, 80);
    public static final Color PRIMARY = new Color(111, 76, 255);
    public static final Color PRIMARY_2 = new Color(139, 92, 246);
    public static final Color SUCCESS = new Color(52, 211, 153);
    public static final Color WARNING = new Color(251, 171, 60);
    public static final Color DANGER = new Color(251, 92, 102);
    public static final Color INFO = new Color(55, 154, 255);
    public static final Color CYAN = new Color(46, 211, 183);
    public static final Font H1 = new Font("Segoe UI", Font.BOLD, 29);
    public static final Font H2 = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font H3 = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font MONO = new Font("Consolas", Font.BOLD, 34);

    private Theme() {}

    public static void applyDefaults() {
        UIManager.put("Panel.background", BG);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("TextField.font", BODY);
        UIManager.put("PasswordField.font", BODY);
        UIManager.put("TextArea.font", BODY);
        UIManager.put("ComboBox.font", BODY);
        UIManager.put("Spinner.font", BODY);
        UIManager.put("Table.font", BODY);
        UIManager.put("TableHeader.font", BODY_BOLD);
        UIManager.put("Table.rowHeight", 38);
        UIManager.put("ScrollBar.width", 9);
        UIManager.put("ScrollBar.thumb", SURFACE_3);
        UIManager.put("ScrollBar.track", BG);
    }

    public static JPanel card() {
        GlowPanel p = new GlowPanel(16, SURFACE, BORDER);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    public static JPanel card(Color fill) {
        GlowPanel p = new GlowPanel(16, fill, BORDER);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(BODY_BOLD); b.setForeground(Color.WHITE); b.setBackground(PRIMARY);
        b.setFocusPainted(false); b.setBorder(new EmptyBorder(10, 17, 10, 17));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static JButton outlineButton(String text) {
        JButton b = new JButton(text);
        b.setFont(BODY_BOLD); b.setForeground(TEXT); b.setBackground(SURFACE_2);
        b.setFocusPainted(false); b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(9, 14, 9, 14)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton navButton(String icon, String text) {
        JButton b = new JButton(icon + "  " + text);
        b.setFont(BODY_BOLD); b.setForeground(MUTED); b.setBackground(SIDEBAR);
        b.setHorizontalAlignment(SwingConstants.LEFT); b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 14, 10, 10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static void styleInput(JComponent c) {
        c.setFont(BODY); c.setBackground(SURFACE_2); c.setForeground(TEXT);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(9, 11, 9, 11)));
    }

    public static JScrollPane scroll(Component view) {
        JScrollPane s = new JScrollPane(view);
        s.setBorder(BorderFactory.createEmptyBorder()); s.getViewport().setBackground(SURFACE);
        s.getVerticalScrollBar().setUnitIncrement(16);
        return s;
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY); table.setForeground(TEXT); table.setBackground(SURFACE);
        table.setGridColor(BORDER); table.setSelectionBackground(new Color(52, 45, 102));
        table.setSelectionForeground(Color.WHITE); table.setRowHeight(42);
        table.getTableHeader().setFont(BODY_BOLD); table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setBackground(SURFACE_2); table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        table.setShowVerticalLines(false); table.setShowHorizontalLines(true);
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static void statusRenderer(JTable table, int statusColumn) {
        table.getColumnModel().getColumn(statusColumn).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                                                                      boolean focused, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, value, selected, focused, row, col);
                l.setHorizontalAlignment(SwingConstants.CENTER); l.setFont(new Font("Segoe UI", Font.BOLD, 10));
                if (!selected) {
                    String s = String.valueOf(value);
                    l.setForeground("COMPLETED".equalsIgnoreCase(s) ? SUCCESS :
                            "IN_PROGRESS".equalsIgnoreCase(s) ? INFO : WARNING);
                }
                return l;
            }
        });
    }

    public static JLabel eyebrow(String text) {
        JLabel l = new JLabel(text.toUpperCase()); l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(MUTED); return l;
    }

    public static JLabel pill(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER); l.setOpaque(true); l.setForeground(color);
        l.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 35));
        l.setFont(new Font("Segoe UI", Font.BOLD, 10)); l.setBorder(new EmptyBorder(5, 9, 5, 9));
        return l;
    }

    public static class GlowPanel extends JPanel {
        private final int radius; private final Color fill; private final Color border;
        public GlowPanel(int radius, Color fill, Color border) { this.radius = radius; this.fill = fill; this.border = border; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0,0,0,35)); g2.fillRoundRect(2, 3, getWidth()-4, getHeight()-3, radius, radius);
            g2.setColor(fill); g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-4, radius, radius);
            g2.setColor(border); g2.drawRoundRect(0, 0, getWidth()-2, getHeight()-4, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
    }

    public static class GradientPanel extends JPanel {
        public GradientPanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(21, 12, 55), getWidth(), getHeight(), new Color(7, 22, 43));
            g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
            g2.setColor(new Color(111,76,255,28)); g2.fillOval(-130, getHeight()-320, 430, 430);
            g2.setColor(new Color(0,188,255,16)); g2.fillOval(getWidth()-320, -160, 420, 420);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
