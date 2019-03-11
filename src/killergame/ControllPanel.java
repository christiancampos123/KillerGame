package killergame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ControllPanel extends JFrame implements ActionListener {

    // attr
    private KillerGame kg;
    private GridBagConstraints constraints;

    private JTextField serverPort;
    private JTextField ipLef;
    private JTextField portLef;
    private JTextField ipRig;
    private JTextField portRig;
    private JLabel showPort;
    private int port;
    private Musica m;

    // const
    public ControllPanel(KillerGame game) {
        super("Control Panel");
        this.kg = game;
        this.configureWindow();
        this.configureConstraints();
        this.addComponents();
        this.setVisible(true);
    }

    // meth
    private void configureWindow() {
        this.setSize(300, 630);
        this.setLocation(this.kg.getX() + this.kg.getWidth(), this.kg.getY());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());
        this.setResizable(false);

    }

    private void configureConstraints() {
        this.constraints = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = 1;
    }

    private void editConstraints(int x, int y, int width, int height) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
    }

    private void addComponents() {

        this.constraints.weightx = 0;
        this.editConstraints(0, 0, 1, 1);
        this.add(new Label("Puerto del servidor : "), constraints);

        this.editConstraints(1, 0, 2, 1);
        this.serverPort = new JTextField("");
        this.add(serverPort, constraints);

        this.addButton("Activar servidor.", 0, 1);

        this.constraints.weightx = 0;
        this.editConstraints(0, 2, 1, 1);
        this.add(new Label("IP Izquierda : "), constraints);

        this.editConstraints(1, 2, 2, 1);
        this.ipLef = new JTextField("");
        this.add(ipLef, constraints);

        this.constraints.weightx = 0;
        this.editConstraints(0, 3, 1, 1);
        this.add(new Label("Puerto Izquierdo : "), constraints);

        this.editConstraints(1, 3, 2, 1);
        this.portLef = new JTextField("");
        this.add(portLef, constraints);

        this.addButton("Activar cliente izquierdo.", 0, 4);

        this.constraints.weightx = 0;
        this.editConstraints(0, 5, 1, 1);
        this.add(new Label("IP Derecha : "), constraints);

        this.editConstraints(1, 5, 2, 1);
        this.ipRig = new JTextField("");
        this.add(ipRig, constraints);

        this.constraints.weightx = 0;
        this.editConstraints(0, 6, 1, 1);
        this.add(new Label("Puerto Derecho : "), constraints);

        this.editConstraints(1, 6, 2, 1);
        this.portRig = new JTextField("");
        this.add(portRig, constraints);

        this.addButton("Activar cliente derecho.", 0, 7);

        this.addButton("Añadir pelota test.", 0, 8);
        this.addButton("Up", 9, 9);
        this.addButton("Down", 0, 9);
        this.addButton("Right", 10, 10);
        this.addButton("Left", 0, 10);

        this.port = port;
        this.showPort = new JLabel("new Port: " + this.port);
        this.constraints.weightx = 0;
        this.editConstraints(0, 12, 1, 1);
        this.add(showPort, this.constraints);
    }

    private void addButton(String text, int x, int y) {
        JButton button = new JButton(text);
        this.editConstraints(x, y, 3, 1);
        constraints.weightx = 1;
        this.add(button, this.constraints);
        button.addActionListener(this);
    }

    // Listen Activity
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Activar servidor.")) {

            this.kg.getServer().setPort(Integer.parseInt(this.serverPort.getText()));

        }

        if (ae.getActionCommand().equals("Activar cliente izquierdo.")) {

            try {
                this.kg.getPrevModule().setPort(Integer.parseInt(this.portLef.getText()));
                this.kg.getPrevModule().setHost(this.ipLef.getText());
            } catch (NumberFormatException error) {

            }

        }

        if (ae.getActionCommand().equals("Activar cliente derecho.")) {
            try {
                this.kg.getNextModule().setPort(Integer.parseInt(this.portRig.getText()));
                this.kg.getNextModule().setHost(this.ipRig.getText());
            } catch (NumberFormatException error) {

            }
        }

        if (ae.getActionCommand().equals("Añadir pelota test.")) {
            this.m.music();
        }

    }

    public void refreshPort(int port) {
        this.remove(showPort);
        this.port = port;
        this.showPort = new JLabel("new Port: " + this.port);
        this.constraints.weightx = 0;
        this.editConstraints(0, 12, 1, 1);
        this.add(showPort, this.constraints);
        this.setVisible(true);
    }

}
