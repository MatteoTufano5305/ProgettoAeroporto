import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CercaVolo extends JFrame {
    private JLabel Titolo;
    private JLabel Partenze;
    private JLabel Destinazione;
    private JLabel Data;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton indietroButton;
    private JButton confermaButton;
    private JPanel panel;

    public CercaVolo() {
        super("Cerca Volo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        panel = new JPanel();
        panel.setLayout(null);

        Titolo = new JLabel("Titolo");
        Titolo.setBounds(10, 10, 100, 20);

        Partenze = new JLabel("Partenze");
        Partenze.setBounds(10, 50, 100, 20);

        textField1 = new JTextField();
        textField1.setBounds(10, 80, 100, 20);

        Destinazione = new JLabel("Destinazione");
        Destinazione.setBounds(10, 100, 100, 20);

        textField2 = new JTextField();
        textField2.setBounds(10, 130, 100, 20);

        Data = new JLabel("Data");
        Data.setBounds(10, 150, 100, 20);

        textField3 = new JTextField();
        textField3.setBounds(10, 180, 100, 20);

        ButtonGroup buttonGroup = new ButtonGroup();
        indietroButton = new JButton("Indietro");
        confermaButton = new JButton("Conferma");
        indietroButton.setBounds(10, 230, 100, 20);
        confermaButton.setBounds(150, 230, 100, 20);
        panel.add(Titolo);
        panel.add(Partenze);
        panel.add(Destinazione);
        panel.add(Data);
        panel.add(textField1);
        panel.add(textField2);
        panel.add(textField3);
        panel.add(indietroButton);
        panel.add(confermaButton);

        setContentPane(panel);
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UtenteGenerico utente = new UtenteGenerico();
                utente.setVisible(true);
                dispose();
            }
        });
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String partenza = textField1.getText().trim();
                String arrivo = textField2.getText().trim();
                String giorno = textField3.getText().trim();
                if(partenza.isEmpty() || arrivo.isEmpty() || giorno.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Completa tutti i campi");
                }
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> {
            new CercaVolo().setVisible(true);

        });
    }
}
