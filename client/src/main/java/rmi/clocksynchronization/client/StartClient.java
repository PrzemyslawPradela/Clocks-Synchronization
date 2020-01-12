package rmi.clocksynchronization.client;

import java.awt.EventQueue;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import rmi.clocksynchronization.common.IServer;
import rmi.clocksynchronization.utils.ClockUpdater;
import rmi.clocksynchronization.utils.DigitsValidator;
import rmi.clocksynchronization.utils.IpAddressValidator;
import rmi.clocksynchronization.utils.SystemIpAddress;

class StartClient extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final StartClient frame = new StartClient();
    private IServer server;
    private Client client;

    /**
     * Create the frame.
     */
    private StartClient() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUstawDatI = new JLabel("Ustaw datę i czas");
        lblUstawDatI.setBounds(46, 0, 126, 15);
        contentPane.add(lblUstawDatI);

        JLabel lblRok = new JLabel("Rok");
        lblRok.setBounds(29, 27, 26, 15);
        contentPane.add(lblRok);

        JLabel lblMiesic = new JLabel("Miesiąc");
        lblMiesic.setBounds(78, 27, 53, 15);
        contentPane.add(lblMiesic);

        JLabel lblDzie = new JLabel("Dzień");
        lblDzie.setBounds(145, 27, 40, 15);
        contentPane.add(lblDzie);

        JComboBox<String> yearsComboBox = new JComboBox<>();
        yearsComboBox.setBounds(12, 46, 64, 24);
        contentPane.add(yearsComboBox);
        yearsComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"2019", "2018", "2017", "2016", "2015",
                "2014", "2013", "2012", "2011", "2010", "2009"}));

        JComboBox<String> monthsComboBox = new JComboBox<>();
        monthsComboBox.setBounds(88, 46, 44, 24);
        contentPane.add(monthsComboBox);
        monthsComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));

        JComboBox<String> daysComboBox = new JComboBox<>();
        daysComboBox.setBounds(153, 46, 44, 24);
        contentPane.add(daysComboBox);
        daysComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                "25", "26", "27", "28", "29", "30", "31"}));

        JLabel lblGodzina = new JLabel("Godzina");
        lblGodzina.setBounds(12, 81, 58, 15);
        contentPane.add(lblGodzina);

        JLabel lblMinuta = new JLabel("Minuta");
        lblMinuta.setBounds(82, 81, 49, 15);
        contentPane.add(lblMinuta);

        JLabel lblSekunda = new JLabel("Sekunda");
        lblSekunda.setBounds(145, 81, 70, 15);
        contentPane.add(lblSekunda);

        JComboBox<String> hoursComboBox = new JComboBox<>();
        hoursComboBox.setBounds(23, 100, 44, 24);
        contentPane.add(hoursComboBox);
        hoursComboBox.setModel(
                new DefaultComboBoxModel<>(new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08",
                        "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));

        JComboBox<String> minutesComboBox = new JComboBox<>();
        minutesComboBox.setBounds(88, 100, 44, 24);
        contentPane.add(minutesComboBox);
        minutesComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                        "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                        "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));

        JComboBox<String> secondsComboBox = new JComboBox<>();
        secondsComboBox.setBounds(153, 100, 44, 24);
        contentPane.add(secondsComboBox);
        secondsComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                        "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                        "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));

        JLabel lblKonfiguracja = new JLabel("Konfiguracja klienta");
        lblKonfiguracja.setBounds(30, 179, 142, 15);
        contentPane.add(lblKonfiguracja);

        JTextField ipAddrTextField = new JTextField();
        ipAddrTextField.setBounds(35, 226, 150, 19);
        contentPane.add(ipAddrTextField);
        try {
            ipAddrTextField.setText(new SystemIpAddress().getInet4AddresString());
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        JLabel lblAdresIp = new JLabel("Adres IP");
        lblAdresIp.setBounds(78, 206, 58, 15);
        contentPane.add(lblAdresIp);

        JLabel lblNewLabel = new JLabel("Połączenie z serwerem");
        lblNewLabel.setBounds(265, 0, 164, 15);
        contentPane.add(lblNewLabel);

        JLabel lblAdresIpSerwera = new JLabel("Adres IP serwera");
        lblAdresIpSerwera.setBounds(291, 27, 121, 15);
        contentPane.add(lblAdresIpSerwera);

        JTextField serverIpAddrTextField = new JTextField();
        serverIpAddrTextField.setBounds(279, 49, 150, 19);
        contentPane.add(serverIpAddrTextField);

        JLabel lblPortSerwera = new JLabel("Port serwera");
        lblPortSerwera.setBounds(308, 81, 93, 15);
        contentPane.add(lblPortSerwera);

        JTextField serverPortTextField = new JTextField();
        serverPortTextField.setBounds(308, 103, 94, 19);
        contentPane.add(serverPortTextField);

        JLabel lblNazwaSerwea = new JLabel("Nazwa serwera");
        lblNazwaSerwea.setBounds(301, 135, 111, 15);
        contentPane.add(lblNazwaSerwea);

        JTextField serverNameTextField = new JTextField();
        serverNameTextField.setBounds(265, 158, 183, 19);
        contentPane.add(serverNameTextField);
        serverNameTextField.setColumns(10);

        JSeparator separator = new JSeparator();
        separator.setBounds(12, 169, 193, 1);
        contentPane.add(separator);

        JSeparator separator_1 = new JSeparator();
        separator_1.setOrientation(SwingConstants.VERTICAL);
        separator_1.setBounds(213, 0, 2, 279);
        contentPane.add(separator_1);

        JButton btnConnect = new JButton("Połącz");
        btnConnect.setEnabled(false);
        btnConnect.setBounds(227, 201, 117, 25);
        contentPane.add(btnConnect);

        JButton btnDisconnect = new JButton("Rozłącz");
        btnDisconnect.setEnabled(false);
        btnDisconnect.setBounds(365, 201, 117, 25);
        contentPane.add(btnDisconnect);

        JLabel lblLogi = new JLabel("Logi klienta");
        lblLogi.setBounds(213, 300, 83, 15);
        contentPane.add(lblLogi);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(12, 291, 470, 1);
        contentPane.add(separator_2);

        JButton btnSetDateTime = new JButton("Zatwierdź");
        btnSetDateTime.setBounds(46, 136, 117, 25);
        contentPane.add(btnSetDateTime);

        JButton btnCreateClient = new JButton("Ustaw");
        btnCreateClient.setBounds(55, 254, 117, 25);
        contentPane.add(btnCreateClient);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(46, 327, 405, 216);
        contentPane.add(scrollPane);

        JTextArea logsTextArea = new JTextArea();
        logsTextArea.setLineWrap(true);
        scrollPane.setViewportView(logsTextArea);
        logsTextArea.setEditable(false);

		/*
		  Deklaracja walidatorow
		 */
        IpAddressValidator ipAddressValidator = new IpAddressValidator();
        DigitsValidator digitsValidator = new DigitsValidator();

		/*
		  Obsluga przyciskow
		 */
        btnSetDateTime.addActionListener(arg0 -> {
            String dateTimeString = yearsComboBox.getSelectedItem() + "-"
                    + monthsComboBox.getSelectedItem() + "-" + daysComboBox.getSelectedItem() + " "
                    + hoursComboBox.getSelectedItem() + ":" + minutesComboBox.getSelectedItem() + ":"
                    + secondsComboBox.getSelectedItem();
            new Thread(new ClockUpdater(new String[]{"bash", "-c", "date -s '" + dateTimeString + "'"})).start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            logsTextArea.append("[System] Pomyślnie ustawiono datę i czas.\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date dateTime = new Date(System.currentTimeMillis());
            logsTextArea.append("[System] Aktualna data i godzina: " + dateFormat.format(dateTime) + "\n");
        });

        btnCreateClient.addActionListener(arg0 -> {
            if (ipAddrTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Pole adresu IP nie może być puste", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else if (ipAddressValidator.validate(ipAddrTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Zły format adresu IP!", "Uwaga", JOptionPane.WARNING_MESSAGE);
            } else {
                System.setProperty("java.rmi.server.hostname", ipAddrTextField.getText());
                try {
                    client = new Client(logsTextArea, ipAddrTextField.getText());
                    logsTextArea
                            .append("[System] Utworzono klienta z adresem IP: " + ipAddrTextField.getText() + "\n");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                btnCreateClient.setEnabled(false);
                btnConnect.setEnabled(true);
            }
        });

        btnConnect.addActionListener(arg0 -> {
            if (serverIpAddrTextField.getText().isEmpty() || serverPortTextField.getText().isEmpty()
                    || serverNameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Pola adresu IP serwera, portu serwera i nazwy serwera nie mogą być puste!", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else if (ipAddressValidator.validate(serverIpAddrTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Zły format adresu IP!", "Uwaga", JOptionPane.WARNING_MESSAGE);
            } else if (digitsValidator.validate(serverPortTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Numer portu może zawierać tylko cyfry!", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                logsTextArea.append("[System] Konfiguracja połaczenia z serwerem...\n");
                try {
                    Registry registry = LocateRegistry.getRegistry(serverIpAddrTextField.getText(),
                            Integer.parseInt(serverPortTextField.getText()));
                    server = (IServer) registry.lookup(serverNameTextField.getText());
                    logsTextArea.append(server.registerClient(client));
                } catch (NumberFormatException | RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }
            }
            btnConnect.setEnabled(false);
            btnDisconnect.setEnabled(true);
        });

        btnDisconnect.addActionListener(arg0 -> {
            try {
                logsTextArea.append(server.unregisterClient(client));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            btnConnect.setEnabled(true);
            btnCreateClient.setEnabled(true);
        });
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.setVisible(true);
                    frame.setTitle("Klient synchronizacji zegarów");
                    ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icon/client.png")));
                    frame.setIconImage(icon.getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
