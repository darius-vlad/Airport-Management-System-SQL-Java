import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.CallableStatement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BazaDeDate extends JFrame {

    private Connection connection;
    private Statement statement;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JTextField idField;
    private JTextField nameField;
    private String selectedEmployeeSalary;
    private String selectedEmployeeFunction;
	private String selectedEmployeeName;
	private String selectedEmployeeIdan;

    public BazaDeDate() {
        try {
            // Connection to the database (replace with actual details)
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_colocviu_partial", "root", "Argos_bd_2003");
            statement = connection.createStatement();

            // Create GUI
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Login and Employee Viewer");
            setSize(800, 600);

            cardPanel = new JPanel();
            cardLayout = new CardLayout();
            cardPanel.setLayout(cardLayout);

            createLoginPanel();
            createDataViewerPanel();
            createHomePanel();
            cardLayout.show(cardPanel, "Login");

            add(cardPanel);

            setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 // ... (previous code)

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        // Add a container for the welcome label and border
        JPanel welcomePanel = new JPanel(new BorderLayout());

        // Add a big text label at the top
        JLabel welcomeLabel = new JLabel("VA RUGAM INTRODUCETI DATELE PENTRU A VEDEA DETALII");
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setBorder(new EmptyBorder(10, 0, 20, 0)); // Add some padding

        // Add a black border under the text
        JPanel borderPanel = new JPanel();
        borderPanel.setBackground(Color.BLACK);
        borderPanel.setPreferredSize(new Dimension(loginPanel.getWidth(), 2)); // Set the width of the border

        // Add components to the welcomePanel
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(borderPanel, BorderLayout.CENTER); // Add the black border

        // Left side (login fields)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(300, 550));

        // Create constraints for the components
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Padding
        
        JPanel borderPanel1 = new JPanel();
        borderPanel.setBackground(Color.BLACK);
        borderPanel.setPreferredSize(new Dimension(5, 2));
        
        JLabel idLabel = new JLabel("ID Angajat:");
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(100, 25));
        constraints.gridx = 0;
        constraints.gridy = 0;
        leftPanel.add(idLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        leftPanel.add(idField, constraints);

        JLabel nameLabel = new JLabel("Nume Angajat:");
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(100, 25));
        constraints.gridx = 0;
        constraints.gridy = 1;
        leftPanel.add(nameLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        leftPanel.add(nameField, constraints);

        JButton loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2; // Span two columns
        leftPanel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Right side (business image)
        JPanel rightPanel = new JPanel();
        ImageIcon businessImageIcon = new ImageIcon("C:\\Users\\Darius\\Documents\\SQL\\avion.png");
        JLabel businessImageLabel = new JLabel(businessImageIcon);
        rightPanel.add(businessImageLabel);

        // Add components to loginPanel
        loginPanel.add(welcomePanel, BorderLayout.NORTH); // Use welcomePanel instead of welcomeLabel directly
        loginPanel.add(leftPanel, BorderLayout.WEST);
        loginPanel.add(rightPanel, BorderLayout.EAST);
        loginPanel.add(borderPanel1 , BorderLayout.CENTER);
        cardPanel.add(loginPanel, "Login");
    }

 
    
    //data view
    private void createDataViewerPanel() {
        JPanel dataViewerPanel = new JPanel(new BorderLayout());

        // Create a panel for the details and buttons
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));

        // Other components in the detailsPanel
        JLabel idLabel = new JLabel("Id Angajat");
        JLabel nameLabel = new JLabel("Nume: ");
        JLabel salaryLabel = new JLabel("Salariu: ");
        JLabel functionLabel = new JLabel("Functie: ");
        
        Font labelFont = new Font("Arial", Font.BOLD, 20);
        idLabel.setFont(labelFont);
        nameLabel.setFont(labelFont);
        salaryLabel.setFont(labelFont);
        functionLabel.setFont(labelFont);

        // Set border to reduce space between rows
        idLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        salaryLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        detailsPanel.add(idLabel);
        detailsPanel.add(nameLabel);
        detailsPanel.add(salaryLabel);
        detailsPanel.add(functionLabel);

        // Add detailsPanel to the center of dataViewerPanel
        dataViewerPanel.add(detailsPanel, BorderLayout.CENTER);
        JLabel pozaLabel = new JLabel();
        ImageIcon pozaIcon = new ImageIcon("C:\\Users\\Darius\\Documents\\SQL\\buletin.png");
        pozaLabel.setIcon(pozaIcon);
        dataViewerPanel.add(pozaLabel, BorderLayout.EAST);

        // Add a panel for buttons at the bottom
        JPanel buttonPanel = new JPanel();

        // Add a button to go back
        JButton backButton = new JButton("Inapoi");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Login");
            }
        });
        backButton.setBackground(Color.red);
        // Add a button to navigate to the "Home" page
        JButton homeButton = new JButton("Acasa");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Home");
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(homeButton);

        // Add buttonPanel to the bottom of dataViewerPanel
        dataViewerPanel.add(buttonPanel, BorderLayout.SOUTH);

        cardPanel.add(dataViewerPanel, "DataViewer");
    }

    
    private void createHomePanel() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());

        // Add an image at the top
        ImageIcon headerImageIcon = new ImageIcon("C:\\Users\\Darius\\Documents\\SQL\\avion_home.png");  // Replace with the actual path to your image
        JLabel headerImageLabel = new JLabel(headerImageIcon);
        headerImageLabel.setPreferredSize(new Dimension(800,300));
        homePanel.add(headerImageLabel, BorderLayout.NORTH);

        // Add buttons and other content in the center
        JPanel centerPanel = new JPanel(new FlowLayout()); // Use FlowLayout for horizontal arrangement

        JButton zboruriButton = new JButton("Zboruri");
        zboruriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createZboruriPanel(); // Call the method to create the Zboruri panel
                cardLayout.show(cardPanel, "Zboruri");
            }
        });


        JButton angajatiButton = new JButton("Angajati");
        angajatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAngajatiPanel();
            }
        });
        
        JButton aeronaveButton = new JButton("Aeronave");
        aeronaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAeronavePanel(); // Call the method to create the Aeronave panel
                cardLayout.show(cardPanel, "Aeronave");}
            });
        
        
        JButton exceptiiButton = new JButton("Exceptii");
        exceptiiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createExceptiiPage();
                cardLayout.show(cardPanel, "Exceptii");}
            });

        centerPanel.add(zboruriButton);
        centerPanel.add(angajatiButton);
        centerPanel.add(aeronaveButton);
        centerPanel.add(exceptiiButton);
        homePanel.add(centerPanel, BorderLayout.CENTER);

        // Add a panel for the button at the bottom
        JPanel buttonPanel = new JPanel();

        // Add a button to go back to the Data Viewer
        JButton backButton = new JButton("Inapoi la detalii angajat");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "DataViewer");
            }
        });
        backButton.setBackground(Color.red);
        buttonPanel.add(backButton);

        // Add buttonPanel to the bottom of homePanel
        homePanel.add(buttonPanel, BorderLayout.SOUTH);

        cardPanel.add(homePanel, "Home");
    }

    
    
    
    
    
    //creare panel zboruri
    private void createZboruriPanel() {
        JPanel zboruriPanel = new JPanel(new BorderLayout());

        // Create radio buttons for filtering
        JRadioButton distanceRadio = new JRadioButton("Distanta intre 500 si 1000");
        JRadioButton maViRadio = new JRadioButton("Zboruri Ma si Vi");

        // Create a checkbox for "Zboruri cu Boeing"
        JRadioButton boeingRadio = new JRadioButton("Zboruri cu Boeing");

        // Create a button group for radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(distanceRadio);
        radioButtonGroup.add(maViRadio);
        radioButtonGroup.add(boeingRadio);

        // Create a button for searching
        JButton searchButton = new JButton("Cauta");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to perform the search based on selected radio button and checkbox
                if (distanceRadio.isSelected()) {
                	createAvioaneDistantaPanel();
                	cardLayout.show(cardPanel,"AvioaneDistanta");
                } else if (maViRadio.isSelected()) {
                	createFlightsOnMaViZiPanel();
                	cardLayout.show(cardPanel, "FlightsOnMaViZi");
                } else if (boeingRadio.isSelected()) {
                	createZboruriCuBoeing();
                	cardLayout.show(cardPanel, "ZboruriCuBoeing");
                }
            }
        });

        // Retrieve the data for Zboruri from the database (replace with your actual query logic)
        try {
            String query = "SELECT * FROM Zboruri";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Numar Zbor");
            tableModel.addColumn("Plecare");
            tableModel.addColumn("Sosire");
            tableModel.addColumn("Distanta");
            tableModel.addColumn("Ora plecare");
            tableModel.addColumn("Ora sosire");
            tableModel.addColumn("Ziua");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("nrz"),
                        resultSet.getString("de_la"),
                        resultSet.getString("la"),
                        resultSet.getString("distanta"),
                        resultSet.getString("plecare"),
                        resultSet.getString("sosire"),
                        resultSet.getString("zi")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable zboruriTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(zboruriTable);

            // Add radio buttons, checkbox, and search button to the top of zboruriPanel
            JPanel searchPanel = new JPanel();
            searchPanel.add(distanceRadio);
            searchPanel.add(maViRadio);
            searchPanel.add(boeingRadio);
            searchPanel.add(searchButton);

            // Add the searchPanel to the top of zboruriPanel
            zboruriPanel.add(searchPanel, BorderLayout.PAGE_START);
            // Add the table to the center of zboruriPanel
            zboruriPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Home panel at the bottom
            JButton backButton = new JButton("Inapoi acasa");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Home");
                }
            });
            backButton.setBackground(Color.red);
            // Add backButton to the bottom of zboruriPanel
            zboruriPanel.add(backButton, BorderLayout.PAGE_END);

            // Add the Zboruri panel to the cardPanel
            cardPanel.add(zboruriPanel, "Zboruri");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    //distanta la zboruri
    private void createAvioaneDistantaPanel() {
        JPanel avioaneDistantaPanel = new JPanel(new BorderLayout());

        // Retrieve the data for Zboruri with distances between 500 and 1000 from the database
        try {
            String query = "SELECT * FROM Zboruri WHERE(distanta>=500 AND distanta<=1000)";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Numar Zbor");
            tableModel.addColumn("Plecare");
            tableModel.addColumn("Sosire");
            tableModel.addColumn("Distanta");
            tableModel.addColumn("Ora plecare");
            tableModel.addColumn("Ora sosire");
            tableModel.addColumn("Ziua");
             

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("nrz"),
                        resultSet.getString("de_la"),
                        resultSet.getString("la"),
                        resultSet.getString("distanta"),
                        resultSet.getString("plecare"),
                        resultSet.getString("sosire"),
                        resultSet.getString("zi")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable avioaneDistantaTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(avioaneDistantaTable);
            avioaneDistantaPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Zboruri panel
            JButton backButton = new JButton("Inapoi la Zboruri");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Zboruri");
                }
            });
            backButton.setBackground(Color.red);
            avioaneDistantaPanel.add(backButton, BorderLayout.SOUTH);

            // Add the Avioane Distanta panel to the cardPanel
            cardPanel.add(avioaneDistantaPanel, "AvioaneDistanta");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    //Zboruri marti vineri
    private void createFlightsOnMaViZiPanel() {
        JPanel flightsOnMaViZiPanel = new JPanel(new BorderLayout());

        // Retrieve the data for flights on Ma and Vi zi from the database (replace with your actual query logic)
        try {
            String query = "SELECT * FROM Zboruri WHERE zi IN ('Ma','Vi') ORDER BY plecare ASC";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Numar Zbor");
            tableModel.addColumn("Plecare");
            tableModel.addColumn("Sosire");
            tableModel.addColumn("Distanta");
            tableModel.addColumn("Ora plecare");
            tableModel.addColumn("Ora sosire");
            tableModel.addColumn("Ziua");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("nrz"),
                        resultSet.getString("de_la"),
                        resultSet.getString("la"),
                        resultSet.getString("distanta"),
                        resultSet.getString("plecare"),
                        resultSet.getString("sosire"),
                        resultSet.getString("zi")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable flightsOnMaViZiTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(flightsOnMaViZiTable);
            flightsOnMaViZiPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Zboruri panel
            JButton backButton = new JButton("Inapoi la Zboruri");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Zboruri");
                }
            });
            backButton.setBackground(Color.red);
            flightsOnMaViZiPanel.add(backButton, BorderLayout.SOUTH);

            // Add the Flights on Ma and Vi zi panel to the cardPanel
            cardPanel.add(flightsOnMaViZiPanel, "FlightsOnMaViZi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
  //Panel Zboruri -> zboruri cu boeing 
    private void createZboruriCuBoeing() {
        JPanel zboruriCuBoeingPanel = new JPanel(new BorderLayout());

        // Retrieve the data for "Zboruri cu Boeing" from the database
        try {
            String query = "SELECT * FROM Aeronave JOIN Zboruri ON Aeronave.gama_croaziera > Zboruri.distanta WHERE Aeronave.numeav LIKE 'BOEING%'";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Aeronava ID");
            tableModel.addColumn("Nume Aeronava");
            tableModel.addColumn("Gama Croaziera");
            tableModel.addColumn("Numar Zbor");
            tableModel.addColumn("Plecare");
            tableModel.addColumn("Sosire");
            tableModel.addColumn("Distanta");
            tableModel.addColumn("Ora plecare");
            tableModel.addColumn("Ora sosire");
            tableModel.addColumn("Ziua");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                		resultSet.getString("idav"),
                        resultSet.getString("numeav"),
                        resultSet.getString("gama_croaziera"),
                        resultSet.getString("nrz"),
                        resultSet.getString("de_la"),
                        resultSet.getString("la"),
                        resultSet.getString("distanta"),
                        resultSet.getString("plecare"),
                        resultSet.getString("sosire"),
                        resultSet.getString("zi")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable zboruriCuBoeingTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(zboruriCuBoeingTable);
            zboruriCuBoeingPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Zboruri panel
            JButton backButton = new JButton("Inapoi la Zboruri");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Zboruri");
                }
            });

            backButton.setBackground(Color.red);
            zboruriCuBoeingPanel.add(backButton, BorderLayout.SOUTH);

            // Add the "Zboruri cu Boeing" panel to the cardPanel
            cardPanel.add(zboruriCuBoeingPanel, "ZboruriCuBoeing");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    //aeronave panel
    private void createAeronavePanel() {
        JPanel aeronavePanel = new JPanel(new BorderLayout());

        // Create radio buttons for filtering
        JRadioButton acelasiPilotRadio = new JRadioButton("Acelasi Pilot");

        // Create a button for searching
        JButton searchButton = new JButton("Cauta");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to perform the search based on selected radio button
                if (acelasiPilotRadio.isSelected()) {
                    createAcelasiPilotPage();

                } else {
                    // Handle the case where no radio button is selected
                    // You may display a message or perform a default search
                    // For now, let's display a message dialog
                    JOptionPane.showMessageDialog(cardPanel, "Please select a search option.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Create a button to show data based on the specified query
        JButton showDataButton = new JButton("Arata numar piloti");
        showDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Execute the specified query and show the result on a new page
                showAeronaveCountPage();
            }
        });

        // Retrieve the data for Aeronave from the database (replace with your actual query logic)
        try {
            String query = "SELECT * FROM Aeronave";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID Aeronava");
            tableModel.addColumn("Nume");
            tableModel.addColumn("Gama Croaziera");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("idav"),
                        resultSet.getString("numeav"),
                        resultSet.getString("gama_croaziera")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable aeronaveTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(aeronaveTable);

            // Add radio buttons, checkbox, and search button to the top of aeronavePanel
            JPanel searchPanel = new JPanel();
            searchPanel.add(acelasiPilotRadio);
            searchPanel.add(searchButton);
            searchPanel.add(showDataButton); // Add the new button to the search panel

            // Add the searchPanel to the top of aeronavePanel
            aeronavePanel.add(searchPanel, BorderLayout.PAGE_START);
            // Add the table to the center of aeronavePanel
            aeronavePanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Home panel at the bottom
            JButton backButton = new JButton("Inapoi acasa");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Home");
                }
            });
             
            
            backButton.setBackground(Color.red);
            // Add showDataButton next to backButton at the bottom of aeronavePanel
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(backButton);
            buttonPanel.add(showDataButton);

            // Add buttonPanel to the bottom of aeronavePanel
            aeronavePanel.add(buttonPanel, BorderLayout.PAGE_END);

            // Add the Aeronave panel to the cardPanel
            cardPanel.add(aeronavePanel, "Aeronave");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    
    
    
    
    
    
    //Panel angajati -> panel piloti ->functie
    private void createAcelasiPilotPage() {
        JPanel acelasiPilotPanel = new JPanel(new BorderLayout());

        try {
            // SQL query to retrieve data based on the provided condition
            String query = "SELECT DISTINCT C1.idav AS idav1, C2.idav AS idav2 " +
                           "FROM Certificare C1 " +
                           "JOIN Certificare C2 ON C1.idan = C2.idan AND C1.idav < C2.idav";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Aeronava ID 1");
            tableModel.addColumn("Aeronava ID 2");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("idav1"),
                        resultSet.getString("idav2")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable acelasiPilotTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(acelasiPilotTable);
            acelasiPilotPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Aeronave panel
            JButton backButton = new JButton("Inapoi la zboruri");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Aeronave");
                }
            });
            backButton.setBackground(Color.red);
            acelasiPilotPanel.add(backButton, BorderLayout.SOUTH);

            // Add the Acelasi Pilot panel to the cardPanel
            cardPanel.add(acelasiPilotPanel, "AcelasiPilot");
            cardLayout.show(cardPanel, "AcelasiPilot"); // Show the Acelasi Pilot panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
  //Panel aeronave- show count
    private void showAeronaveCountPage() {
        JPanel aeronaveDataPanel = new JPanel(new BorderLayout());

        // Execute the specified query to get the data
        try {
            String query = "SELECT Aeronave.numeav, COUNT(Certificare.idan) AS numar_piloti " +
                           "FROM Aeronave " +
                           "LEFT JOIN Certificare ON Aeronave.idav = Certificare.idav " +
                           "GROUP BY Aeronave.numeav";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Nume aeronava");
            tableModel.addColumn("Numar piloti");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("numeav"),
                        resultSet.getString("numar_piloti")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable aeronaveDataTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(aeronaveDataTable);
            aeronaveDataPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Aeronave panel
            JButton backButton = new JButton("Inapoi la aeronave");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Aeronave");
                }
            });
            backButton.setBackground(Color.red);
            // Add backButton to the bottom of aeronaveDataPanel
            aeronaveDataPanel.add(backButton, BorderLayout.SOUTH);

            // Add the Aeronave Data panel to the cardPanel
            cardPanel.add(aeronaveDataPanel, "AeronaveData");
            cardLayout.show(cardPanel, "AeronaveData"); // Show the Aeronave Data panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
  
    
    
    
    
    
    
    //Creare Panel Angajati
    private void createAngajatiPanel() {
        JPanel angajatiPanel = new JPanel(new BorderLayout());

        // Retrieve the data for Angajati from the database
        try {
            String query = "SELECT * FROM Angajati";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID Angajat");
            tableModel.addColumn("Nume");
            tableModel.addColumn("Salariu");
            tableModel.addColumn("Functie");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("idan"),
                        resultSet.getString("numean"),
                        resultSet.getString("salariu"),
                        resultSet.getString("functie")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable angajatiTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(angajatiTable);
            angajatiPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to show only pilots
            JButton showPilotsButton = new JButton("Arata doar piloti");
            showPilotsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Call a method to create a new page showing only pilots
                    showOnlyPilotsPage();
                }
            });

            // Add the button to the top of angajatiPanel
            angajatiPanel.add(showPilotsButton, BorderLayout.NORTH);

            // Add a panel for buttons at the bottom
            JPanel buttonPanel = new JPanel();

            // Add a button to go back to the Home panel
            JButton backButton = new JButton("Inapoi Acasa");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Home");
                }
            });
             
            backButton.setBackground(Color.red);
            
            JButton showSalariuMed = new JButton("Arata salariu mediu");
            showSalariuMed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Call a method to create a new page showing only pilots
                    createAngajatiSalMed();
                }
            });

            
            
            // Add buttons to buttonPanel
            buttonPanel.add(backButton);
            buttonPanel.add(showPilotsButton);
            buttonPanel.add(showSalariuMed);
            

            // Add buttonPanel to the bottom of angajatiPanel
            angajatiPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add the Angajati panel to the cardPanel
            cardPanel.add(angajatiPanel, "Angajati");
            cardLayout.show(cardPanel, "Angajati"); // Show the Angajati panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
    
    
    //panel angajati - arata doar piloti
    private void showOnlyPilotsPage() {
        JPanel showOnlyPilotsPanel = new JPanel(new BorderLayout());

        // Retrieve the data for pilots from the database based on the condition
        try {
            String query = "SELECT * FROM Angajati WHERE functie = 'Pilot'";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID Angajat");
            tableModel.addColumn("Nume");
            tableModel.addColumn("Salariu");
            tableModel.addColumn("Functie");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("idan"),
                        resultSet.getString("numean"),
                        resultSet.getString("salariu"),
                        resultSet.getString("functie")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable pilotsTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(pilotsTable);
            showOnlyPilotsPanel.add(scrollPane, BorderLayout.CENTER);

            // Create a panel for buttons at the bottom
            JPanel buttonPanel = new JPanel(new FlowLayout());

            // Add a button to go back to the Angajati panel
            JButton backButton = new JButton("Inapoi la Angajati");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Angajati");
                }
            });
            backButton.setBackground(Color.red);
            // Add the button to execute the specified query
            JButton maxSalaryButton = new JButton("Arata salariu maxim");
            maxSalaryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Execute the specified query and show the result in a pop-up window
                    showMaxSalaryPopup();
                }
            });

            // Add a button to show information based on the new query
            JButton infoByQueryButton = new JButton("Informatii Ioan Alexandru");
            infoByQueryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Execute the specified query and show the result on a new page
                    showInfoIoanA();
                }
            });

            // Add buttons to the buttonPanel
            buttonPanel.add(backButton);
            buttonPanel.add(maxSalaryButton);
            buttonPanel.add(infoByQueryButton);

            // Add the buttonPanel to the bottom of showOnlyPilotsPanel
            showOnlyPilotsPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add the Show Only Pilots panel to the cardPanel
            cardPanel.add(showOnlyPilotsPanel, "ShowOnlyPilots");
            cardLayout.show(cardPanel, "ShowOnlyPilots"); // Show the Show Only Pilots panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    //panel angajati -> panel doar piloti - arata salariu max
    private void showMaxSalaryPopup() {
        try {
            String query = "SELECT MAX(salariu) FROM Angajati " +
                    "WHERE idan IN (SELECT idan FROM Certificare WHERE idav IN (SELECT idav FROM Aeronave WHERE numeav LIKE 'AIRBUS%'))";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                // Get the result from the query
                String maxSalary = resultSet.getString(1);

                // Show the result in a pop-up window
                JOptionPane.showMessageDialog(cardPanel, "Max Salary: " + maxSalary, "Max Salary", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

    
    //panel angajati -> panel piloti - show info Ioan Alexandru
    private void showInfoIoanA() {
        JPanel infoByQueryPanel = new JPanel(new BorderLayout());

        // Execute the specified query to get the data
        try {
            String query = "SELECT * FROM Aeronave WHERE idav IN " +
                    "(SELECT idav FROM Certificare WHERE idan IN " +
                    "(SELECT idan FROM Angajati WHERE numean = 'Ioan Alexandru'))";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID Aeronava");
            tableModel.addColumn("Nume aeronava");
            tableModel.addColumn("Gama Croaziera");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("idav"),
                        resultSet.getString("numeav"),
                        resultSet.getString("gama_croaziera")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable infoByQueryTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(infoByQueryTable);
            infoByQueryPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Show Only Pilots panel
            JButton backButton = new JButton("Inapoi la piloti");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "ShowOnlyPilots");
                }
            });
            backButton.setBackground(Color.red);
            // Add backButton to the bottom of infoByQueryPanel
            infoByQueryPanel.add(backButton, BorderLayout.SOUTH);

            // Add the Info By Query panel to the cardPanel
            cardPanel.add(infoByQueryPanel, "InfoByQuery");
            cardLayout.show(cardPanel, "InfoByQuery"); // Show the Info By Query panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    
    
    
    
    
    
    //panel angajati - salariu mediu
    private void createAngajatiSalMed() {
        JPanel angajatiDataPanel = new JPanel(new BorderLayout());

        // Execute the specified query
        try {
            String query = "SELECT functie, AVG(salariu) AS salariu_mediu FROM Angajati GROUP BY functie";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Functie");
            tableModel.addColumn("Salariu Mediu");

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("functie"),
                        resultSet.getString("salariu_mediu")
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable angajatiDataTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(angajatiDataTable);

            // Add the table to the center of angajatiDataPanel
            angajatiDataPanel.add(scrollPane, BorderLayout.CENTER);

            // Add a button to go back to the Angajati panel at the bottom
            JButton backButton = new JButton("Back to Angajati");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "Angajati");
                }
            });
            backButton.setBackground(Color.red);
            // Add backButton to the bottom of angajatiDataPanel
            angajatiDataPanel.add(backButton, BorderLayout.PAGE_END);

            // Add the Angajati Data panel to the cardPanel
            cardPanel.add(angajatiDataPanel, "AngajatiData");
            cardLayout.show(cardPanel, "AngajatiData");  // Show the newly created panel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    private void createExceptiiPage() {
        JPanel exceptiiPanel = new JPanel(new BorderLayout());

        try {
            // Logic to call the IntroduceExceptii stored procedure
            CallableStatement callableStatement = (CallableStatement) connection.prepareCall("{call IntroduceExceptii()}");
            callableStatement.execute();
            
            // Retrieve data from the exceptii table
            String query = "SELECT * FROM exceptii";
            ResultSet resultSet = statement.executeQuery(query);

            // Create a table model with the data
            DefaultTableModel tableModel = new DefaultTableModel();
            
            // Add columns to the table model based on the structure of your 'exceptii' table
            tableModel.addColumn("ID Aeronava");
            tableModel.addColumn("Nume Aeronava");
            tableModel.addColumn("Gama Croaziera");
            tableModel.addColumn("Exceptii");
            // Add more columns as needed

            // Populate the table model with data from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("idav"),
                        resultSet.getString("numeav"),
                        resultSet.getString("gama_croaziera"),
                        resultSet.getString("natura_exceptiei")
                        // Add more columns as needed
                };
                tableModel.addRow(rowData);
            }

            // Create a JTable with the table model
            JTable exceptiiTable = new JTable(tableModel);

            // Add the table to a JScrollPane for scrollability
            JScrollPane scrollPane = new JScrollPane(exceptiiTable);

            // Add the table to the center of exceptiiPanel
            exceptiiPanel.add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException (display an error message, log the exception, etc.)
        }

        // Add a button to go back to the Home panel at the bottom
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "Home");
            }
        });
        backButton.setBackground(Color.red);
        // Add backButton to the bottom of exceptiiPanel
        exceptiiPanel.add(backButton, BorderLayout.PAGE_END);

        // Add the Exceptii panel to the cardPanel
        cardPanel.add(exceptiiPanel, "Exceptii");
    }
    
    
    
    
    
    //Functia Login
    private void login() {
        try {
            String id = idField.getText();
            String name = nameField.getText();

            // Sample query (replace with your actual query logic)
            String query = "SELECT * FROM Angajati WHERE idan = '" + id + "' AND numean = '" + name + "'";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                // Successful login
                String selectedEmployeeName = resultSet.getString("numean");
                String selectedEmployeeSalary = resultSet.getString("salariu");
                String selectedEmployeeFunction = resultSet.getString("functie");
                String selectedEmployeeIdan = resultSet.getString("idan");

                updateDataViewerPanel(selectedEmployeeName, selectedEmployeeSalary, selectedEmployeeFunction, selectedEmployeeIdan);
                cardLayout.show(cardPanel, "DataViewer");
            } else {
                // Failed login
                JOptionPane.showMessageDialog(this, "Invalid ID or name. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    private void updateDataViewerPanel(String employeeName, String employeeSalary, String employeeFunction, String employeeIdan) {
        JPanel dataViewerPanel = (JPanel) cardPanel.getComponent(1); // Assuming the DataViewerPanel is the second component

        // Assuming detailsPanel is the first component of dataViewerPanel
        JPanel detailsPanel = (JPanel) dataViewerPanel.getComponent(0);

        // Assuming the components in detailsPanel are JLabels
        JLabel nameLabel = (JLabel) detailsPanel.getComponent(1);
        JLabel salaryLabel = (JLabel) detailsPanel.getComponent(2);
        JLabel functionLabel = (JLabel) detailsPanel.getComponent(3);
        JLabel idanLabel = (JLabel) detailsPanel.getComponent(0);

        // Set the labels with the retrieved information
        nameLabel.setText("Nume: " + employeeName);
        salaryLabel.setText("Salariu: " + employeeSalary);
        functionLabel.setText("Functie: " + employeeFunction);
        idanLabel.setText("Idan: " + employeeIdan);
    }
   
    
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BazaDeDate());
    }
}
