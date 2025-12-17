import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Prenotazione extends JFrame {
    private JLabel lblPrenotazione;
    private JLabel lblScegliVolo;
    private JLabel lblScegliPosto;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JButton indietroButton;
    private JButton confermaButton;
    private JPanel panel;

    public Prenotazione() {
        super("Prenotazione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        panel = new JPanel();
        panel.setLayout(null);

        lblPrenotazione = new JLabel("Prenotazione");
        lblPrenotazione.setBounds(10, 10, 100, 20);

        lblScegliVolo = new JLabel("Scegli volo");
        lblScegliVolo.setBounds(10, 50, 100, 20);

        lblScegliPosto = new JLabel("Scegli posto");
        lblScegliPosto.setBounds(10, 130, 100, 20);

        comboBox1 = new JComboBox<>(new String[]{"Volo 1", "Volo 2", "Volo 3"});
        comboBox1.setBounds(10, 80, 150, 25);

        comboBox2 = new JComboBox<>(new String[]{"Posto 1A", "Posto 1B", "Posto 2A"});
        comboBox2.setBounds(10, 160, 150, 25);


        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(10, 250, 100, 30);
        confermaButton = new JButton("Conferma");
        confermaButton.setBounds(120, 250, 100, 30);

        panel.add(lblPrenotazione);
        panel.add(lblScegliVolo);
        panel.add(lblScegliPosto);
        panel.add(comboBox1);
        panel.add(comboBox2);
        panel.add(indietroButton);
        panel.add(confermaButton);

        setContentPane(panel);
        setVisible(true);
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UtenteGenerico().setVisible(true);
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Prenotazione::new);;
    }
}

