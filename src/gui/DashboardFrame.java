package gui;

import model.Task;
import model.User;
import service.TaskService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {
    private final User user;
    private final TaskService taskService = new TaskService();
    private final JPanel content = new JPanel(new BorderLayout());
    private final JPanel navPanel = new JPanel();
    private JButton activeNav;

    public DashboardFrame(User user) {
        this.user=user; Theme.applyDefaults();
        if (user.getUserId() > 0) taskService.ensureDemoTasks(user.getUserId());
        setTitle("Taskator | Time Management & Task Prioritization");
        setSize(1440,850); setMinimumSize(new Dimension(1150,700)); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); build();
    }

    private void build(){
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(Theme.BG);
        root.add(sidebar(),BorderLayout.WEST);
        JPanel main=new JPanel(new BorderLayout()); main.setBackground(Theme.BG); main.add(topbar(),BorderLayout.NORTH);
        content.setBackground(Theme.BG); main.add(content,BorderLayout.CENTER); root.add(main,BorderLayout.CENTER); add(root);
        showDashboard();
    }

    private JPanel sidebar(){
        JPanel side=new JPanel(new BorderLayout()); side.setBackground(Theme.SIDEBAR); side.setPreferredSize(new Dimension(235,0));
        JPanel top=new JPanel();top.setOpaque(false);top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));top.setBorder(new EmptyBorder(28,20,18,20));
        JPanel brand=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));brand.setOpaque(false);
        JLabel mark=new JLabel("✓",SwingConstants.CENTER);mark.setOpaque(true);mark.setBackground(Theme.PRIMARY);mark.setForeground(Color.WHITE);mark.setFont(new Font("Segoe UI",Font.BOLD,20));mark.setPreferredSize(new Dimension(40,40));
        JLabel name=new JLabel("TASKATOR");name.setForeground(Color.WHITE);name.setFont(new Font("Segoe UI",Font.BOLD,23));brand.add(mark);brand.add(name);top.add(brand);
        JLabel sub=new JLabel("  STAY FOCUSED • ACHIEVE MORE");sub.setForeground(new Color(103,119,149));sub.setFont(new Font("Segoe UI",Font.BOLD,8));top.add(sub);top.add(Box.createVerticalStrut(28));side.add(top,BorderLayout.NORTH);
        navPanel.setOpaque(false);navPanel.setLayout(new BoxLayout(navPanel,BoxLayout.Y_AXIS));navPanel.setBorder(new EmptyBorder(0,12,10,12));
        addNav("⌂","Dashboard",this::showDashboard); addNav("□","My Tasks",()->showPage(new TaskPanel(user.getUserId()),"My Tasks","Manage, prioritize and update your tasks")); addNav("+","Add Task",()->showPage(new AddTaskPanel(user.getUserId()),"Create Task","Add a new task with deadline and importance")); addNav("◷","Time Tracking",()->showPage(new TimeTrackingPanel(user.getUserId()),"Time Tracking","Record the time you spend on active work")); addNav("▥","Progress",()->showPage(new ProgressPanel(user.getUserId()),"Progress Overview","See completion and workload at a glance"));
        if("ADMIN".equalsIgnoreCase(user.getRole())) addNav("⚙","Admin Center",()->showPage(new AdminPanel(user.getUserId()),"Admin Center","Manage categories, rules and system activity"));
        side.add(navPanel,BorderLayout.CENTER);side.add(profileBox(),BorderLayout.SOUTH);return side;
    }

    private void addNav(String icon,String text,Runnable action){
        JButton b=Theme.navButton(icon,text); b.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));b.setAlignmentX(Component.LEFT_ALIGNMENT);b.addActionListener(e->{action.run();setActive(b);});navPanel.add(b);navPanel.add(Box.createVerticalStrut(5));if(activeNav==null)activeNav=b;
    }
    private void setActive(JButton b){if(activeNav!=null){activeNav.setBackground(Theme.SIDEBAR);activeNav.setForeground(Theme.MUTED);}activeNav=b;b.setBackground(new Color(43,34,91));b.setForeground(Color.WHITE);}
    private JPanel profileBox(){
        JPanel p=new JPanel(new BorderLayout(10,8));p.setOpaque(false);p.setBorder(new EmptyBorder(12,18,18,18));
        JLabel av=new JLabel(initials(user.getFullName()),SwingConstants.CENTER);av.setOpaque(true);av.setBackground(Theme.PRIMARY);av.setForeground(Color.WHITE);av.setFont(new Font("Segoe UI",Font.BOLD,12));av.setPreferredSize(new Dimension(40,40));
        JPanel t=new JPanel();t.setOpaque(false);t.setLayout(new BoxLayout(t,BoxLayout.Y_AXIS));JLabel n=new JLabel(user.getFullName());n.setForeground(Color.WHITE);n.setFont(Theme.BODY_BOLD);JLabel r=new JLabel(user.getRole());r.setForeground(Theme.MUTED);r.setFont(Theme.SMALL);t.add(n);t.add(r);
        JButton logout=new JButton("Logout");logout.setForeground(Theme.MUTED);logout.setBackground(Theme.SIDEBAR);logout.setBorder(new EmptyBorder(4,5,4,5));logout.setFocusPainted(false);logout.addActionListener(e->logout());p.add(av,BorderLayout.WEST);p.add(t,BorderLayout.CENTER);p.add(logout,BorderLayout.SOUTH);return p;
    }
    private JPanel topbar(){
        JPanel p=new JPanel(new BorderLayout(15,0));p.setBackground(new Color(9,15,29));p.setBorder(new EmptyBorder(13,26,13,26));
        JLabel title=new JLabel("My workspace");title.setForeground(Color.WHITE);title.setFont(Theme.H3);
        JTextField search=new JTextField("  Search tasks, categories...");search.setForeground(Theme.MUTED);search.setBackground(Theme.SURFACE_2);search.setBorder(BorderFactory.createLineBorder(Theme.BORDER));search.setPreferredSize(new Dimension(280,38));
        JPanel right=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));right.setOpaque(false);JLabel bell=new JLabel("◉");bell.setForeground(Theme.WARNING);JLabel signed=new JLabel("Signed in as  "+user.getEmail());signed.setForeground(Theme.MUTED);signed.setFont(Theme.SMALL);right.add(search);right.add(bell);right.add(signed);p.add(title,BorderLayout.WEST);p.add(right,BorderLayout.EAST);return p;
    }

    private void showDashboard(){
        JPanel page=new JPanel(new BorderLayout(0,18));page.setBackground(Theme.BG);page.setBorder(new EmptyBorder(24,28,24,28));
        Map<String,Integer> s=taskService.getStatusSummary(user.getUserId()); List<Task> tasks=taskService.getUserTasks(user.getUserId());
        JPanel hero=new JPanel(new BorderLayout(15,0));hero.setOpaque(false);
        JPanel ht=new JPanel();ht.setOpaque(false);ht.setLayout(new BoxLayout(ht,BoxLayout.Y_AXIS));
        JLabel greet=new JLabel("Good afternoon, "+firstName(user.getFullName())+"!  ✦");greet.setFont(Theme.H1);greet.setForeground(Theme.TEXT);JLabel hs=new JLabel("Let's make today productive. You have "+s.get("TOTAL")+" task(s) planned.");hs.setForeground(Theme.MUTED);hs.setFont(Theme.BODY);ht.add(greet);ht.add(Box.createVerticalStrut(5));ht.add(hs);hero.add(ht,BorderLayout.WEST);
        JButton add=Theme.button("+  Add Task");add.addActionListener(e->showPage(new AddTaskPanel(user.getUserId()),"Create Task","Add a new task with deadline and importance"));hero.add(add,BorderLayout.EAST);page.add(hero,BorderLayout.NORTH);

        JPanel center=new JPanel(new BorderLayout(14,14));center.setOpaque(false);
        JPanel stats=new JPanel(new GridLayout(1,4,12,0));stats.setOpaque(false);stats.add(stat("TOTAL TASKS",s.get("TOTAL"),"All planned work",Theme.PRIMARY));stats.add(stat("PENDING",s.get("PENDING"),"Waiting to start",Theme.WARNING));stats.add(stat("IN PROGRESS",s.get("IN_PROGRESS"),"Currently active",Theme.INFO));stats.add(stat("COMPLETED",s.get("COMPLETED"),"Finished work",Theme.SUCCESS));center.add(stats,BorderLayout.NORTH);

        JPanel lower=new JPanel(new GridLayout(1,3,14,0));lower.setOpaque(false);lower.add(todayTasks(tasks));lower.add(schedule(tasks));lower.add(quickActions());center.add(lower,BorderLayout.CENTER);page.add(center,BorderLayout.CENTER);showContent(page);setActive((JButton)navPanel.getComponent(0));
    }

    private JPanel stat(String label,int value,String caption,Color accent){
        JPanel p=Theme.card();p.setLayout(new BorderLayout(10,0));JPanel icon=new JPanel(new GridBagLayout());icon.setBackground(new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),45));icon.setPreferredSize(new Dimension(48,48));JLabel i=new JLabel(label.substring(0,1));i.setForeground(accent);i.setFont(new Font("Segoe UI",Font.BOLD,18));icon.add(i);p.add(icon,BorderLayout.WEST);
        JPanel t=new JPanel();t.setOpaque(false);t.setLayout(new BoxLayout(t,BoxLayout.Y_AXIS));JLabel l=Theme.eyebrow(label);JLabel v=new JLabel(String.valueOf(value));v.setForeground(Theme.TEXT);v.setFont(new Font("Segoe UI",Font.BOLD,27));JLabel c=new JLabel(caption);c.setForeground(Theme.MUTED);c.setFont(Theme.SMALL);t.add(l);t.add(Box.createVerticalStrut(4));t.add(v);t.add(c);p.add(t,BorderLayout.CENTER);return p;
    }

    private JPanel todayTasks(List<Task> tasks){
        JPanel p=Theme.card();p.setLayout(new BorderLayout(8,12));JPanel head=new JPanel(new BorderLayout());head.setOpaque(false);JLabel h=new JLabel("Today's Tasks");h.setFont(Theme.H2);h.setForeground(Theme.TEXT);head.add(h,BorderLayout.WEST);JLabel d=new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));d.setForeground(Theme.MUTED);d.setFont(Theme.SMALL);head.add(d,BorderLayout.SOUTH);p.add(head,BorderLayout.NORTH);
        JPanel list=new JPanel();list.setOpaque(false);list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));
        tasks.sort(Comparator.comparing(Task::getPriorityScore).reversed());int shown=0;for(Task t:tasks){if(shown++>=5)break;list.add(taskRow(t));list.add(Box.createVerticalStrut(8));}
        if(tasks.isEmpty()) list.add(empty("No tasks yet. Add your first task."));p.add(Theme.scroll(list),BorderLayout.CENTER);return p;
    }
    private JPanel taskRow(Task t){
        JPanel row=new JPanel(new BorderLayout(10,5));row.setBackground(Theme.SURFACE_2);row.setBorder(new EmptyBorder(9,10,9,10));
        JLabel marker=new JLabel("○");marker.setForeground("COMPLETED".equalsIgnoreCase(t.getStatus())?Theme.SUCCESS:Theme.MUTED);marker.setFont(new Font("Segoe UI",Font.BOLD,18));row.add(marker,BorderLayout.WEST);
        JPanel text=new JPanel();text.setOpaque(false);text.setLayout(new BoxLayout(text,BoxLayout.Y_AXIS));JLabel title=new JLabel(t.getTitle());title.setForeground(Theme.TEXT);title.setFont(Theme.BODY_BOLD);JLabel meta=new JLabel(t.getDeadline().format(DateTimeFormatter.ofPattern("dd MMM • HH:mm"))+"   •   "+t.getProgress()+"% complete");meta.setForeground(Theme.MUTED);meta.setFont(Theme.SMALL);text.add(title);text.add(meta);row.add(text,BorderLayout.CENTER);
        Color pc=t.getImportance()>=4?Theme.DANGER:t.getImportance()==3?Theme.WARNING:Theme.SUCCESS;row.add(Theme.pill(t.getStatus().replace('_',' '),pc),BorderLayout.EAST);return row;
    }
    private JPanel schedule(List<Task> tasks){
        JPanel p=Theme.card();p.setLayout(new BorderLayout(8,12));JLabel h=new JLabel("Today • Schedule");h.setFont(Theme.H2);p.add(h,BorderLayout.NORTH);
        JPanel body=new JPanel();body.setOpaque(false);body.setLayout(new BoxLayout(body,BoxLayout.Y_AXIS));
        int[] hours={9,11,13,15,17,19,21};for(int hour:hours){JPanel r=new JPanel(new BorderLayout(8,0));r.setOpaque(false);r.setBorder(new EmptyBorder(3,0,3,0));JLabel time=new JLabel(String.format("%02d:00",hour));time.setForeground(Theme.MUTED);time.setFont(Theme.SMALL);time.setPreferredSize(new Dimension(42,32));r.add(time,BorderLayout.WEST);JPanel line=new JPanel(new BorderLayout());line.setBackground(Theme.SURFACE_2);line.setBorder(new EmptyBorder(7,9,7,9));Task nearest=findNearHour(tasks,hour);if(nearest!=null){JLabel tl=new JLabel(nearest.getTitle());tl.setForeground(Theme.TEXT);tl.setFont(Theme.BODY_BOLD);line.add(tl,BorderLayout.CENTER);}else{JLabel tl=new JLabel("Free focus time");tl.setForeground(new Color(88,103,132));tl.setFont(Theme.SMALL);line.add(tl,BorderLayout.CENTER);}r.add(line,BorderLayout.CENTER);body.add(r);}p.add(body,BorderLayout.CENTER);return p;
    }
    private Task findNearHour(List<Task> tasks,int hour){for(Task t:tasks){if(t.getDeadline().getHour()>=hour&&t.getDeadline().getHour()<hour+2)return t;}return null;}
    private JPanel quickActions(){
        JPanel p=Theme.card();p.setLayout(new BorderLayout(8,12));JLabel h=new JLabel("Quick Actions");h.setFont(Theme.H2);p.add(h,BorderLayout.NORTH);JPanel grid=new JPanel(new GridLayout(2,2,10,10));grid.setOpaque(false);
        grid.add(action("+","Add Task",Theme.PRIMARY,()->showPage(new AddTaskPanel(user.getUserId()),"Create Task","Add a new task with deadline and importance")));
        grid.add(action("◷","Start Timer",Theme.INFO,()->showPage(new TimeTrackingPanel(user.getUserId()),"Time Tracking","Record the time you spend on active work")));
        grid.add(action("□","View Tasks",Theme.SURFACE_3,()->showPage(new TaskPanel(user.getUserId()),"My Tasks","Manage, prioritize and update your tasks")));
        grid.add(action("▥","View Progress",Theme.SURFACE_3,()->showPage(new ProgressPanel(user.getUserId()),"Progress Overview","See completion and workload at a glance")));p.add(grid,BorderLayout.CENTER);return p;
    }
    private JButton action(String icon,String text,Color color,Runnable r){JButton b=new JButton("  "+icon+"  "+text);b.setHorizontalAlignment(SwingConstants.LEFT);b.setFont(Theme.BODY_BOLD);b.setForeground(Theme.TEXT);b.setBackground(color);b.setFocusPainted(false);b.setBorder(new EmptyBorder(12,12,12,12));b.addActionListener(e->r.run());return b;}
    private JLabel empty(String s){JLabel l=new JLabel(s);l.setForeground(Theme.MUTED);l.setBorder(new EmptyBorder(20,5,20,5));return l;}

    private void showPage(JPanel panel,String title,String subtitle){
        JPanel page=new JPanel(new BorderLayout(0,16));page.setBackground(Theme.BG);page.setBorder(new EmptyBorder(24,28,24,28));JPanel head=new JPanel();head.setOpaque(false);head.setLayout(new BoxLayout(head,BoxLayout.Y_AXIS));JLabel h=new JLabel(title);h.setFont(Theme.H1);h.setForeground(Theme.TEXT);JLabel s=new JLabel(subtitle);s.setForeground(Theme.MUTED);s.setFont(Theme.BODY);head.add(h);head.add(Box.createVerticalStrut(4));head.add(s);page.add(head,BorderLayout.NORTH);page.add(panel,BorderLayout.CENTER);showContent(page);
    }
    private void showContent(JPanel page){content.removeAll();content.add(page,BorderLayout.CENTER);content.revalidate();content.repaint();}
    private void logout(){dispose();new LoginFrame().setVisible(true);}
    private String firstName(String s){int i=s.indexOf(' ');return i>0?s.substring(0,i):s;}
    private String initials(String s){String[] p=s.trim().split("\\s+");return p.length==1?p[0].substring(0,Math.min(2,p[0].length())).toUpperCase():(""+p[0].charAt(0)+p[p.length-1].charAt(0)).toUpperCase();}
}
