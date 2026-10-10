package gui;

import model.Category;
import model.PriorityRule;
import model.SystemLog;
import service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {
    private final AdminService service=new AdminService();private final JTable table=new JTable();private final DefaultTableModel model=new DefaultTableModel();private final JLabel summary=new JLabel();
    public AdminPanel(int adminId){setLayout(new BorderLayout());setOpaque(false);build();showCategories();}
    private void build(){
        JPanel card=Theme.card();card.setLayout(new BorderLayout(12,12));JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);JPanel tx=new JPanel();tx.setOpaque(false);tx.setLayout(new BoxLayout(tx,BoxLayout.Y_AXIS));JLabel h=new JLabel("Administration center");h.setFont(Theme.H2);JLabel s=new JLabel("Monitor categories, prioritization rules and system activity.");s.setForeground(Theme.MUTED);s.setFont(Theme.SMALL);tx.add(h);tx.add(Box.createVerticalStrut(4));tx.add(s);top.add(tx,BorderLayout.WEST);JPanel tabs=new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));tabs.setOpaque(false);JButton cat=Theme.outlineButton("Categories");JButton rules=Theme.outlineButton("Priority Rules");JButton logs=Theme.outlineButton("System Logs");cat.addActionListener(e->showCategories());rules.addActionListener(e->showRules());logs.addActionListener(e->showLogs());tabs.add(cat);tabs.add(rules);tabs.add(logs);top.add(tabs,BorderLayout.EAST);card.add(top,BorderLayout.NORTH);Theme.styleTable(table);card.add(Theme.scroll(table),BorderLayout.CENTER);summary.setForeground(Theme.MUTED);summary.setFont(Theme.SMALL);card.add(summary,BorderLayout.SOUTH);add(card,BorderLayout.CENTER);
    }
    private void cols(String...c){model.setDataVector(new Object[][]{},c);table.setModel(model);Theme.styleTable(table);}
    private void showCategories(){cols("ID","CATEGORY","DESCRIPTION");int n=0;for(Category c:service.getCategories()){model.addRow(new Object[]{c.getCategoryId(),c.getCategoryName(),c.getDescription()});n++;}summary.setText(n+" categories configured");}
    private void showRules(){cols("ID","RULE","DEADLINE","IMPORTANCE","ACTIVE");int n=0;for(PriorityRule r:service.getPriorityRules()){model.addRow(new Object[]{r.getRuleId(),r.getRuleName(),r.getDeadlineWeight()+"%",r.getImportanceWeight()+"%",r.isActive()?"YES":"NO"});n++;}summary.setText(n+" prioritization rules configured");}
    private void showLogs(){cols("DATE","LEVEL","ACTION");int n=0;for(SystemLog l:service.getSystemLogs()){model.addRow(new Object[]{l.getCreatedAt(),l.getLogLevel(),l.getAction()});n++;}summary.setText(n+" system log entries");}
}
