/*
*Wenyang Wu
*CNT4714
*Project 3
*03/05/17
*/
package MySQLGUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class sqlClient {
	
	private static Connection connection;
	private static Statement statement;
	
	private static boolean connectedToDatabase = false;
	
	private static ResultSetTableModel tableModel;
	
	static String [] dbConnectComboBoxOptions_JDBC_Drivers  = new String[] { "com.mysql.jdbc.Driver" };
	static String [] dbConnectComboBoxOptions_Database_URLs = new String[] { "jdbc:mysql://localhost:3306/project3" };
	
	static JFrame windowFrame = new JFrame();
	
	static JPanel guiContainer = new JPanel();
	static JPanel guiTopLeft   = new JPanel();
	static JPanel guiTopRight  = new JPanel();
	static JPanel guiMiddle    = new JPanel();
	static JPanel guiBottom    = new JPanel();
	
	static JButton sqlControlsButton_Connect_to_Database = new JButton("Connect to Database");
	static JButton sqlControlsButton_Clear_Command       = new JButton("Clear Command");
	static JButton sqlControlsButton_Execute_SQL_Command = new JButton("Execute SQL Command");
	static JButton sqlResultsButton_Clear_Result_Window  = new JButton("Clear Result Window");
	
	static JTextField dbConnectTextField_Username = new JTextField();
	
	static JPasswordField dbConnectTextField_Password = new JPasswordField();
	
	static JTextArea sqlCommandsTextArea_Commands = new JTextArea(10,30);
	
	static JScrollPane sqlCommandsTextArea = new JScrollPane();
	static JScrollPane sqlResults          = new JScrollPane();
	
	static JLabel dbConnectLabel_Title        = new JLabel("Enter Database Information", SwingConstants.LEFT);
	static JLabel dbConnectLabel_Blank        = new JLabel("", SwingConstants.LEFT);
	static JLabel dbConnectLabel_JDBC_Driver  = new JLabel("JDBC Driver", SwingConstants.LEFT);
	static JLabel dbConnectLabel_Database_URL = new JLabel("Database URL", SwingConstants.LEFT);
	static JLabel dbConnectLabel_Username     = new JLabel("Username", SwingConstants.LEFT);
	static JLabel dbConnectLabel_Password     = new JLabel("Password", SwingConstants.LEFT);
	static JLabel sqlCommandsLabel_Title      = new JLabel("Enter a SQL Command", SwingConstants.LEFT);
	static JLabel sqlControlsLabel_Status     = new JLabel("Not Connected", SwingConstants.LEFT);
	static JLabel sqlResultsLabel_Title       = new JLabel("SQL Execution Result", SwingConstants.LEFT);
	
	static JComboBox<String> dbConnectComboBox_JDBC_Driver  = new JComboBox<>(dbConnectComboBoxOptions_JDBC_Drivers);
	static JComboBox<String> dbConnectComboBox_Database_URL = new JComboBox<>(dbConnectComboBoxOptions_Database_URLs);
	
	static SpringLayout guiContainerLayout = new SpringLayout();
	static SpringLayout guiTopLeftLayout   = new SpringLayout();
	static SpringLayout guiTopRightLayout  = new SpringLayout();
	static SpringLayout guiMiddleLayout    = new SpringLayout();
	static SpringLayout guiBottomLayout    = new SpringLayout();
	
	// Sets up the GUI for user interaction
	public static void main(String[] args)
	{
		// Sets up the User Interface
		setGuiTopLeft();
		setGuiTopRight();
		setGuiMiddle();
		setGuiBottom();
		setGui();
		
		// Activates the controls
		attachEventListeners();
	}
	
	// Adds Parts of the GUI to the main window, lays them out and shows up
	public static void setGui()
	{
		guiContainer.setLayout(guiContainerLayout);
		guiContainer.add(guiTopLeft);
		guiContainer.add(guiTopRight);
		guiContainer.add(guiMiddle);
		guiContainer.add(guiBottom);
		
		guiContainerLayout.putConstraint(SpringLayout.WEST, guiTopLeft, 5, SpringLayout.NORTH, guiContainer);
		guiContainerLayout.putConstraint(SpringLayout.NORTH, guiTopLeft, 5, SpringLayout.WEST, guiContainer);
		
		guiContainerLayout.putConstraint(SpringLayout.WEST, guiTopRight, 10, SpringLayout.EAST, guiTopLeft);
		guiContainerLayout.putConstraint(SpringLayout.NORTH, guiTopRight, 5, SpringLayout.NORTH, guiContainer);
		
		guiContainerLayout.putConstraint(SpringLayout.NORTH, guiMiddle, 10, SpringLayout.SOUTH, guiTopLeft);
		guiContainerLayout.putConstraint(SpringLayout.WEST, guiMiddle, 5, SpringLayout.WEST, guiContainer);
		
		guiContainerLayout.putConstraint(SpringLayout.NORTH, guiBottom, 10, SpringLayout.SOUTH, guiMiddle);
		guiContainerLayout.putConstraint(SpringLayout.WEST, guiBottom, 5, SpringLayout.WEST, guiContainer);
		
		windowFrame.add(guiContainer);		
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowFrame.setSize(815,675);
		windowFrame.setTitle("MySQL/SQL Client GUI - (WW - Spring 2017)");
		windowFrame.setLocationRelativeTo(null);
		windowFrame.setVisible(true);
	}
	
	// Sets up the top left part of the GUI which manages the database connection components
	public static void setGuiTopLeft()
	{
		guiTopLeft.setLayout(guiTopLeftLayout);
		guiTopLeft.setPreferredSize(new Dimension(390, 225));
		
		dbConnectLabel_Title.setPreferredSize(new Dimension(390, 30));
		dbConnectLabel_JDBC_Driver.setPreferredSize(new Dimension(90, 30));
		dbConnectLabel_Database_URL.setPreferredSize(new Dimension(90, 30));
		dbConnectLabel_Username.setPreferredSize(new Dimension(90, 30));
		dbConnectLabel_Password.setPreferredSize(new Dimension(90, 30));
		dbConnectComboBox_JDBC_Driver.setPreferredSize(new Dimension(295, 30));
		dbConnectComboBox_Database_URL.setPreferredSize(new Dimension(295, 30));
		dbConnectTextField_Username.setPreferredSize(new Dimension(295, 30));
		dbConnectTextField_Password.setPreferredSize(new Dimension(295, 30));
		
		guiTopLeft.add(dbConnectLabel_Title);
		guiTopLeft.add(dbConnectLabel_JDBC_Driver);
		guiTopLeft.add(dbConnectLabel_Database_URL);
		guiTopLeft.add(dbConnectLabel_Username);
		guiTopLeft.add(dbConnectLabel_Password);
		guiTopLeft.add(dbConnectComboBox_JDBC_Driver);
		guiTopLeft.add(dbConnectComboBox_Database_URL);
		guiTopLeft.add(dbConnectTextField_Username);
		guiTopLeft.add(dbConnectTextField_Password);
		
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectLabel_JDBC_Driver, 5,
				SpringLayout.SOUTH, dbConnectLabel_Title);
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectLabel_Database_URL, 5,
				SpringLayout.SOUTH, dbConnectLabel_JDBC_Driver);
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectLabel_Username, 5,
				SpringLayout.SOUTH, dbConnectLabel_Database_URL);
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectLabel_Password, 5,
				SpringLayout.SOUTH, dbConnectLabel_Username);
		
		guiTopLeftLayout.putConstraint(SpringLayout.WEST, dbConnectComboBox_JDBC_Driver, 5,
				SpringLayout.EAST, dbConnectLabel_JDBC_Driver);
		guiTopLeftLayout.putConstraint(SpringLayout.WEST, dbConnectComboBox_Database_URL, 5,
				SpringLayout.EAST, dbConnectLabel_Database_URL);
		guiTopLeftLayout.putConstraint(SpringLayout.WEST, dbConnectTextField_Username, 5,
				SpringLayout.EAST, dbConnectLabel_Username);
		guiTopLeftLayout.putConstraint(SpringLayout.WEST, dbConnectTextField_Password, 5,
				SpringLayout.EAST, dbConnectLabel_Password);

		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectComboBox_JDBC_Driver, 5,
				SpringLayout.SOUTH, dbConnectLabel_Title);
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectComboBox_Database_URL, 5,
				SpringLayout.SOUTH, dbConnectComboBox_JDBC_Driver);
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectTextField_Username, 5,
				SpringLayout.SOUTH, dbConnectComboBox_Database_URL);
		guiTopLeftLayout.putConstraint(SpringLayout.NORTH, dbConnectTextField_Password, 5,
				SpringLayout.SOUTH, dbConnectTextField_Username);
	}
	
	// Sets up the top right part of the GUI which manages the SQL Commands
	public static void setGuiTopRight()
	{
		guiTopRight.setLayout(guiTopRightLayout);
		guiTopRight.setPreferredSize(new Dimension(390, 225));
		
		sqlCommandsLabel_Title.setPreferredSize(new Dimension(390, 30));
		sqlCommandsTextArea.setPreferredSize(new Dimension(390, 190));
		
		guiTopRight.add(sqlCommandsLabel_Title);
		guiTopRight.add(sqlCommandsTextArea);
		
		sqlCommandsTextArea.getViewport().add(sqlCommandsTextArea_Commands,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		guiTopRightLayout.putConstraint(SpringLayout.NORTH, sqlCommandsTextArea, 5,
				SpringLayout.SOUTH, sqlCommandsLabel_Title);
	}
	
	// Sets up the middle part of the GUI which manages the main controls and database connection status
	public static void setGuiMiddle()
	{
		guiMiddle.setPreferredSize(new Dimension(790, 50));
		guiMiddle.setLayout(guiMiddleLayout);
		
		sqlControlsLabel_Status.setPreferredSize(new Dimension(290, 40));
		sqlControlsButton_Connect_to_Database.setPreferredSize(new Dimension(185, 40));
		sqlControlsButton_Clear_Command.setPreferredSize(new Dimension(125, 40));
		sqlControlsButton_Execute_SQL_Command.setPreferredSize(new Dimension(175, 40));
		
		guiMiddle.add(sqlControlsLabel_Status);
		guiMiddle.add(sqlControlsButton_Connect_to_Database);
		guiMiddle.add(sqlControlsButton_Clear_Command);
		guiMiddle.add(sqlControlsButton_Execute_SQL_Command);
		
		guiMiddleLayout.putConstraint(SpringLayout.NORTH, sqlControlsLabel_Status, 5,
				SpringLayout.NORTH, guiMiddle);
		guiMiddleLayout.putConstraint(SpringLayout.NORTH, sqlControlsButton_Connect_to_Database, 5,
				SpringLayout.NORTH, guiMiddle);
		guiMiddleLayout.putConstraint(SpringLayout.NORTH, sqlControlsButton_Clear_Command, 5,
				SpringLayout.NORTH, guiMiddle);
		guiMiddleLayout.putConstraint(SpringLayout.NORTH, sqlControlsButton_Execute_SQL_Command, 5,
				SpringLayout.NORTH, guiMiddle);
		
		guiMiddleLayout.putConstraint(SpringLayout.WEST, sqlControlsLabel_Status, 0,
				SpringLayout.WEST, guiMiddle);
		guiMiddleLayout.putConstraint(SpringLayout.WEST, sqlControlsButton_Connect_to_Database, 5,
				SpringLayout.EAST, sqlControlsLabel_Status);
		guiMiddleLayout.putConstraint(SpringLayout.WEST, sqlControlsButton_Clear_Command, 5,
				SpringLayout.EAST, sqlControlsButton_Connect_to_Database);
		guiMiddleLayout.putConstraint(SpringLayout.WEST, sqlControlsButton_Execute_SQL_Command, 5,
				SpringLayout.EAST, sqlControlsButton_Clear_Command);

		sqlControlsLabel_Status.setForeground(Color.RED);
		sqlControlsButton_Connect_to_Database.setBackground(Color.BLUE);
		sqlControlsButton_Connect_to_Database.setForeground(Color.WHITE);
		sqlControlsButton_Execute_SQL_Command.setBackground(Color.GREEN);
	}
	
	// Sets up the bottom part of the GUI which displays the SQL Command results
	public static void setGuiBottom()
	{
		guiBottom.setPreferredSize(new Dimension(790, 335));
		guiBottom.setLayout(guiBottomLayout);
		
		sqlResultsLabel_Title.setPreferredSize(new Dimension(800, 30));
		sqlResults.setPreferredSize(new Dimension(790, 250));
		sqlResultsButton_Clear_Result_Window.setPreferredSize(new Dimension(200, 40));
		
		guiBottom.add(sqlResultsLabel_Title);
		guiBottom.add(sqlResults);
		guiBottom.add(sqlResultsButton_Clear_Result_Window);
		
		guiBottomLayout.putConstraint(SpringLayout.NORTH, sqlResults, 5,
				SpringLayout.SOUTH, sqlResultsLabel_Title);
		guiBottomLayout.putConstraint(SpringLayout.NORTH, sqlResultsButton_Clear_Result_Window, 5,
				SpringLayout.SOUTH, sqlResults);
		
		sqlResultsButton_Clear_Result_Window.setBackground(Color.YELLOW);
	}
	
	// Sets up event listeners for the GUI buttons
	public static void attachEventListeners()
	{
		// Database connection button, which connects and also disconnects from database
		sqlControlsButton_Connect_to_Database.addActionListener( new ActionListener()
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed( ActionEvent e )
			{
				if ( !isConnectedToDatabase() )
				{					
					if ( ( !dbConnectTextField_Username.getText().isEmpty() ) &&
							( !dbConnectTextField_Password.getText().isEmpty() ) )
					{
						try { connectToDatabase(); }
						catch (ClassNotFoundException e1) { e1.printStackTrace(); }
						catch (SQLException e1) { e1.printStackTrace(); }
					}
					
					else
						JOptionPane.showMessageDialog( null, "Please make sure to enter a username and password.",
								"Username and Password Required", JOptionPane.ERROR_MESSAGE );
				}
				
				else { disconnectFromDatabase(); }
			}
		});
		
		// Clear Button that removes all text from the SQL Command text area
		sqlControlsButton_Clear_Command.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e ) { sqlCommandsTextArea_Commands.setText(""); }
		});
		
		// Button that executes the SQL Command in the text area
		sqlControlsButton_Execute_SQL_Command.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				executeQueryAndShowResults(
					(String)dbConnectComboBox_JDBC_Driver.getSelectedItem(),
					sqlCommandsTextArea_Commands.getText()	
				);
			}
		});
		
		// Resets the viewport in the sqlResults JPanel
		sqlResultsButton_Clear_Result_Window.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e ) { sqlResults.setViewportView(null); }
		});
	}
	
	// Establishes a Connection to the Database using the provided user name and password
	@SuppressWarnings("deprecation")
	public static void connectToDatabase() throws SQLException, ClassNotFoundException
	{
		Class.forName( (String)dbConnectComboBox_JDBC_Driver.getSelectedItem() );
		
		connection = DriverManager.getConnection( (String)dbConnectComboBox_Database_URL.getSelectedItem(),
				dbConnectTextField_Username.getText(), dbConnectTextField_Password.getText() );
		
		setStatement(connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY ));
		
		connectedToDatabase = true;
		
		sqlControlsButton_Connect_to_Database.setBackground(Color.RED);
		sqlControlsButton_Connect_to_Database.setText("Disconnect from Database");
		sqlControlsLabel_Status.setText("Connected to " + (String)dbConnectComboBox_Database_URL.getSelectedItem());
	}
	
	// Disconnects from the Database and updates the status
	public static void disconnectFromDatabase()
	{
		try
		{
			getStatement().close();
			connection.close();
		}
		catch ( SQLException sqlException )
		{
			sqlException.printStackTrace();
		}
		finally
		{
			connectedToDatabase = false;
			sqlControlsButton_Connect_to_Database.setBackground(Color.BLUE);
			sqlControlsButton_Connect_to_Database.setText("Connect to Database");
			sqlControlsLabel_Status.setText("No Connection Now");
		}
	}
	
	// Run the SQL query and then populate results in sqlResults Panel
	public static void executeQueryAndShowResults( String JDBC_DRIVER, String NEW_QUERY ) 
	{
		if ( isConnectedToDatabase() )
		{
			try 
			{
				tableModel = new ResultSetTableModel( JDBC_DRIVER, NEW_QUERY );
				JTable resultTable = new JTable( tableModel );
				sqlResults.getViewport().add( resultTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
			}
			catch ( ClassNotFoundException classNotFound ) { JOptionPane.showMessageDialog( null,
					"MySQL driver not found", "Driver not found", JOptionPane.ERROR_MESSAGE ); }
			catch ( SQLException sqlException ) { JOptionPane.showMessageDialog( null, sqlException.getMessage(),
					"Database error", JOptionPane.ERROR_MESSAGE ); }
		}
		else
		{
			JOptionPane.showMessageDialog( null, 
					"Not Connected to a Database\nPlease connect to a database before executing a command.",
					"No Database Connection", JOptionPane.ERROR_MESSAGE );
		}
	}

	// Getters and Setters for Variables/Objects
	public static boolean isConnectedToDatabase() { return connectedToDatabase; }
	public static Statement getStatement() { return statement; }
	public static void setStatement(Statement statement) { sqlClient.statement = statement; }
}

