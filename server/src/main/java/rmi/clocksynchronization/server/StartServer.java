package rmi.clocksynchronization.server;

import java.awt.EventQueue;
import java.awt.Font;
import java.net.SocketException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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

import rmi.clocksynchronization.utils.ClockUpdater;
import rmi.clocksynchronization.utils.DigitsValidator;
import rmi.clocksynchronization.utils.IpAddressValidator;
import rmi.clocksynchronization.utils.SystemIpAddress;

class StartServer extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final StartServer frame = new StartServer();
    private final JTextArea logsTextArea = new JTextArea();
    private Registry registry;
    private Server server = null;
    private SynchronizationService synchronizationService;

    /**
     * Create the frame.
     */
    private StartServer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 450);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 265, 418);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Ustaw datę i czas");
        lblNewLabel.setBounds(80, 6, 126, 15);
        panel.add(lblNewLabel);

        JLabel lblKonfiguracjaSerwera = new JLabel("Konfiguracja serwera");
        lblKonfiguracjaSerwera.setBounds(60, 203, 152, 15);
        panel.add(lblKonfiguracjaSerwera);

        JButton btnSetDateTime = new JButton("Zatwierdź");
        btnSetDateTime.setBounds(80, 154, 117, 25);
        panel.add(btnSetDateTime);

        JComboBox<String> minutesComboBox = new JComboBox<>();
        minutesComboBox.setBounds(117, 118, 44, 24);
        panel.add(minutesComboBox);
        minutesComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                        "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                        "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));

        JLabel lblSekunda = new JLabel("Sekunda");
        lblSekunda.setBounds(178, 91, 62, 15);
        panel.add(lblSekunda);

        JComboBox<String> secondsComboBox = new JComboBox<>();
        secondsComboBox.setBounds(193, 118, 44, 24);
        panel.add(secondsComboBox);
        secondsComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
                        "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                        "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));

        JComboBox<String> hoursComboBox = new JComboBox<>();
        hoursComboBox.setBounds(42, 118, 44, 24);
        panel.add(hoursComboBox);
        hoursComboBox.setModel(
                new DefaultComboBoxModel<>(new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08",
                        "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));

        JLabel lblGodzina = new JLabel("Godzina");
        lblGodzina.setBounds(32, 91, 58, 15);
        panel.add(lblGodzina);

        JLabel lblMinuta = new JLabel("Minuta");
        lblMinuta.setBounds(112, 91, 49, 15);
        panel.add(lblMinuta);

        JComboBox<String> yearsComboBox = new JComboBox<>();
        yearsComboBox.setBounds(32, 55, 64, 24);
        panel.add(yearsComboBox);
        yearsComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"2019", "2018", "2017", "2016", "2015",
                "2014", "2013", "2012", "2011", "2010", "2009"}));

        JLabel lblRok = new JLabel("Rok");
        lblRok.setBounds(42, 33, 26, 15);
        panel.add(lblRok);

        JComboBox<String> monthsComboBox = new JComboBox<>();
        monthsComboBox.setBounds(117, 55, 44, 24);
        panel.add(monthsComboBox);
        monthsComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));

        JLabel lblMiesic = new JLabel("Miesiąc");
        lblMiesic.setBounds(117, 33, 53, 15);
        panel.add(lblMiesic);

        JLabel lblDzie = new JLabel("Dzień");
        lblDzie.setBounds(190, 33, 40, 15);
        panel.add(lblDzie);

        JComboBox<String> daysComboBox = new JComboBox<>();
        daysComboBox.setBounds(196, 55, 44, 24);
        panel.add(daysComboBox);
        daysComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                "25", "26", "27", "28", "29", "30", "31"}));

        JSeparator separator = new JSeparator();
        separator.setBounds(12, 191, 239, 2);
        panel.add(separator);

        JTextField ipAddrTextField = new JTextField();
        ipAddrTextField.setBounds(60, 245, 152, 19);
        panel.add(ipAddrTextField);
        try {
            ipAddrTextField.setText(new SystemIpAddress().getInet4AddresString());
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        JLabel lblNewLabel_1 = new JLabel("Adres IP");
        lblNewLabel_1.setBounds(103, 230, 58, 15);
        panel.add(lblNewLabel_1);

        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(117, 276, 30, 15);
        panel.add(lblPort);

        JTextField portTextField = new JTextField();
        portTextField.setBounds(69, 296, 126, 19);
        panel.add(portTextField);

        JLabel lblNazwa = new JLabel("Nazwa");
        lblNazwa.setBounds(112, 327, 48, 15);
        panel.add(lblNazwa);

        JTextField nameTextField = new JTextField();
        nameTextField.setBounds(22, 344, 229, 19);
        panel.add(nameTextField);
        nameTextField.setColumns(10);

        JButton btnStartServer = new JButton("Start");
        btnStartServer.setBounds(12, 375, 117, 25);
        panel.add(btnStartServer);
        JSeparator separator_1 = new JSeparator();
        separator_1.setOrientation(SwingConstants.VERTICAL);
        separator_1.setBounds(263, 0, 2, 418);
        panel.add(separator_1);

        JButton btnStopServer = new JButton("Stop");
        btnStopServer.setEnabled(false);
        btnStopServer.setBounds(141, 375, 117, 25);
        panel.add(btnStopServer);

        JLabel lblSynchronizacjaZegarw = new JLabel("Synchronizacja zegarów");
        lblSynchronizacjaZegarw.setBounds(381, 12, 173, 15);
        contentPane.add(lblSynchronizacjaZegarw);

        JLabel lblIntewaSynchronizacjis = new JLabel("Intewał synchronizacji [s]");
        lblIntewaSynchronizacjis.setBounds(270, 39, 180, 15);
        contentPane.add(lblIntewaSynchronizacjis);

        JTextField syncFreqTextField = new JTextField();
        syncFreqTextField.setBounds(458, 37, 174, 19);
        contentPane.add(syncFreqTextField);

        JButton btnStartSync = new JButton("Start");
        btnStartSync.setEnabled(false);
        btnStartSync.setBounds(378, 70, 117, 25);
        contentPane.add(btnStartSync);

        JButton btnStopSync = new JButton("Stop");
        btnStopSync.setEnabled(false);
        btnStopSync.setBounds(515, 70, 117, 25);
        contentPane.add(btnStopSync);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(242, 107, 402, 12);
        contentPane.add(separator_2);

        JLabel lblLogiSystemowe = new JLabel("Logi serwera");
        lblLogiSystemowe.setBounds(419, 120, 93, 15);
        contentPane.add(lblLogiSystemowe);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(277, 147, 355, 259);
        contentPane.add(scrollPane);

        logsTextArea.setLineWrap(true);
        scrollPane.setViewportView(logsTextArea);
        logsTextArea.setFont(new Font("Dialog", Font.PLAIN, 12));
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

        btnStartServer.addActionListener(arg0 -> {
            if (ipAddrTextField.getText().isEmpty() || portTextField.getText().isEmpty()
                    || nameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Pola adresu IP, portu i nazwy nie mogą być puste!", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else if (ipAddressValidator.validate(ipAddrTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Zły format adresu IP!", "Uwaga", JOptionPane.WARNING_MESSAGE);
            } else if (digitsValidator.validate(portTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Numer portu może zawierać tylko cyfry!", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                System.setProperty("java.rmi.server.hostname", ipAddrTextField.getText());
                try {
                    server = new Server(logsTextArea, nameTextField.getText());
                    registry = LocateRegistry.createRegistry(Integer.parseInt(portTextField.getText()));
                    registry.rebind(nameTextField.getText(), server);
                    logsTextArea.append("[System] Serwer " + nameTextField.getText() + " działa\n");
                    btnStartServer.setEnabled(false);
                    btnStopServer.setEnabled(true);
                    btnStartSync.setEnabled(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btnStopServer.addActionListener(arg0 -> {
            try {
                UnicastRemoteObject.unexportObject(registry, true);
                logsTextArea.append("[System] Serwer " + nameTextField.getText() + " wyłączony\n");
                btnStartServer.setEnabled(true);
            } catch (NoSuchObjectException e) {
                e.printStackTrace();
            }
        });

        btnStartSync.addActionListener(arg0 -> {
            if (syncFreqTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Podaj interwał synchronizacji zegarów!", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else if (digitsValidator.validate(syncFreqTextField.getText())) {
                JOptionPane.showMessageDialog(frame, "Interwał synchronizacji może zawierać tylko cyfry!", "Uwaga",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                syncFreqTextField.setEditable(false);
                btnStartServer.setEnabled(false);
                btnStopServer.setEnabled(false);
                btnStopSync.setEnabled(true);
                btnStartSync.setEnabled(false);
                server.setClockSyncFreq(Integer.parseInt(syncFreqTextField.getText()));
                logsTextArea.append("[Serwer: " + nameTextField.getText() + "] Synchronizacja zegarów włączona.\n");
                synchronizationService = new SynchronizationService(server, nameTextField.getText(), logsTextArea);
                synchronizationService.start();
            }
        });

        btnStopSync.addActionListener(arg0 -> {
            syncFreqTextField.setEditable(true);
            logsTextArea.append("[Serwer: " + nameTextField.getText() + "] Synchronizacja zegarów wyłączona.\n");
            synchronizationService.stop();
            btnStartSync.setEnabled(true);
            btnStopServer.setEnabled(true);
            btnStopSync.setEnabled(false);
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
                    frame.setTitle("Serwer synchronizacji zegarów");
                    ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("icon/server.png")));
                    frame.setIconImage(icon.getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
