package gui;

import exceptions.AuthenticationException;
import model.User;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField email = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        Theme.applyDefaults();
        setTitle("Taskator | Smart Task Management");
        setSize(1060, 660); setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null); build();
    }

    private void build() {
        JPanel root = new JPanel(new GridLayout(1, 2)); root.setBackground(Theme.BG);
        JPanel hero = new Theme.GradientPanel(); hero.setLayout(new GridBagLayout());
        GridBagConstraints h = new GridBagConstraints(); h.gridx=0; h.weightx=1; h.fill=GridBagConstraints.HORIZONTAL;
        h.insets=new Insets(8,58,8,58);

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)); brand.setOpaque(false);
        JLabel mark = new JLabel("✓", SwingConstants.CENTER); mark.setOpaque(true); mark.setBackground(Theme.PRIMARY);
        mark.setForeground(Color.WHITE); mark.setFont(new Font("Segoe UI",Font.BOLD,24)); mark.setPreferredSize(new Dimension(50,50));
        JLabel name = new JLabel("TASKATOR"); name.setForeground(Color.WHITE); name.setFont(new Font("Segoe UI",Font.BOLD,30));
        brand.add(mark); brand.add(name); h.gridy=0; hero.add(brand,h);

        JLabel tag = new JLabel("Plan less. Accomplish more."); tag.setForeground(Color.WHITE); tag.setFont(new Font("Segoe UI",Font.BOLD,25));
        h.gridy=1; h.insets=new Insets(35,58,6,58); hero.add(tag,h);
        JLabel desc = new JLabel("<html><div style='width:360px'>A focused workspace for tasks, deadlines, priorities and time tracking — designed to keep your day clear and your work moving.</div></html>");
        desc.setForeground(new Color(182,193,216)); desc.setFont(new Font("Segoe UI",Font.PLAIN,14)); h.gridy=2; h.insets=new Insets(4,58,18,58); hero.add(desc,h);

        JPanel featuresPanel=new JPanel(); featuresPanel.setOpaque(false); featuresPanel.setLayout(new BoxLayout(featuresPanel,BoxLayout.Y_AXIS));
        featuresText(featuresPanel);
        h.gridy=3; h.insets=new Insets(5,58,8,58); hero.add(featuresPanel,h);
        JLabel footer=new JLabel("TASKATOR  •  PRODUCTIVITY WORKSPACE"); footer.setForeground(new Color(116,132,160)); footer.setFont(new Font("Segoe UI",Font.BOLD,9)); h.gridy=4; h.insets=new Insets(42,58,8,58); hero.add(footer,h);

        JPanel formArea=new JPanel(new GridBagLayout()); formArea.setBackground(Theme.BG);
        GridBagConstraints c=new GridBagConstraints(); c.gridx=0; c.weightx=1; c.fill=GridBagConstraints.HORIZONTAL; c.insets=new Insets(7,70,7,70);
        JLabel welcome=new JLabel("Welcome back"); welcome.setFont(new Font("Segoe UI",Font.BOLD,31)); welcome.setForeground(Theme.TEXT); c.gridy=0; formArea.add(welcome,c);
        JLabel sub=new JLabel("Sign in to continue to Taskator"); sub.setForeground(Theme.MUTED); c.gridy=1; formArea.add(sub,c);
        c.gridy=2;c.insets=new Insets(34,70,5,70);formArea.add(label("EMAIL ADDRESS"),c);
        Theme.styleInput(email); email.setPreferredSize(new Dimension(0,46)); c.gridy=3;c.insets=new Insets(3,70,12,70);formArea.add(email,c);
        c.gridy=4;c.insets=new Insets(5,70,5,70);formArea.add(label("PASSWORD"),c);
        Theme.styleInput(password); password.setPreferredSize(new Dimension(0,46)); c.gridy=5;c.insets=new Insets(3,70,17,70);formArea.add(password,c);
        JButton login=Theme.button("SIGN IN   →"); login.setPreferredSize(new Dimension(0,48)); login.addActionListener(e->login()); password.addActionListener(e->login()); c.gridy=6;c.insets=new Insets(8,70,14,70);formArea.add(login,c);
        JPanel demo=Theme.card(Theme.SURFACE); demo.setLayout(new BoxLayout(demo,BoxLayout.Y_AXIS));
        JLabel dh=new JLabel("DEMO ACCESS"); dh.setFont(new Font("Segoe UI",Font.BOLD,10)); dh.setForeground(Theme.MUTED); demo.add(dh); demo.add(Box.createVerticalStrut(8));
        JLabel du=new JLabel("User   •   harshit@example.com / user123"); du.setForeground(Theme.TEXT); du.setFont(Theme.SMALL); demo.add(du);
        JLabel da=new JLabel("Admin •   admin@timemanager.com / admin123"); da.setForeground(Theme.TEXT); da.setFont(Theme.SMALL); demo.add(da);
        c.gridy=7;c.insets=new Insets(6,70,8,70);formArea.add(demo,c);
        root.add(hero);root.add(formArea);add(root);
    }

    private java.util.List<JLabel> featuresText(JPanel panel){
        String[] fs={"Smart task prioritization","Live time tracking","Progress insights","Admin controls"};
        java.util.List<JLabel> list=new java.util.ArrayList<>();
        for(String s:fs){ JLabel l=new JLabel("  ✓  "+s); l.setForeground(new Color(218,225,240)); l.setFont(new Font("Segoe UI",Font.PLAIN,14)); panel.add(l); panel.add(Box.createVerticalStrut(9)); list.add(l); }
        return list;
    }
    private JLabel label(String t){JLabel l=new JLabel(t);l.setFont(new Font("Segoe UI",Font.BOLD,10));l.setForeground(Theme.MUTED);return l;}
    private void login(){try{User user=authService.login(email.getText().trim(),new String(password.getPassword()));dispose();new DashboardFrame(user).setVisible(true);}catch(AuthenticationException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Sign in failed",JOptionPane.ERROR_MESSAGE);}}
}
