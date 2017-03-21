/*
*Wenyang Wu
*CNT4714
*Project 3
*03/05/17
*/
package MySQLGUI;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ResultSetTableModel extends AbstractTableModel 
{
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	// Constructs the object that will contain the results from the SQL query
	public ResultSetTableModel( String driver, String query ) throws SQLException, ClassNotFoundException
	{
		if ( checkQuery( query ) )
		{
			setQuery( query );
		}
		else
		{
			setUpdate( query );
		}	
	}
	
	// 
	public static boolean checkQuery(String proposedQuery)
	{
		if ( proposedQuery.toLowerCase().contains("update") || proposedQuery.toLowerCase().contains("insert") || 
				proposedQuery.toLowerCase().contains("delete") ) { return false; }
		else { return true; }
	}
	
	// Get the class that represents column type
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass( int column ) throws IllegalStateException
	{
		try 
		{
			String className = metaData.getColumnClassName( column + 1 );
			return Class.forName( className );
		}
		catch ( Exception exception ) { exception.printStackTrace(); }
      
		return Object.class;
	}

	// Gets the number of columns in ResultSet
	public int getColumnCount() throws IllegalStateException
	{
		try { return metaData.getColumnCount(); }
		catch ( SQLException sqlException ) { sqlException.printStackTrace(); }
      
		return 0;
	}

	// Gets the name of a particular column in ResultSet
	public String getColumnName( int column ) throws IllegalStateException
	{
		try { return metaData.getColumnName( column + 1 ); }
		catch ( SQLException sqlException ) { sqlException.printStackTrace(); }
      
		return "";
	}

	// Returns the number of rows in ResultSet
	public int getRowCount() throws IllegalStateException
	{ 
		return numberOfRows;
	}

	// Obtains a value in particular row and column
	public Object getValueAt( int row, int column ) throws IllegalStateException
	{
		try 
		{
			resultSet.next();
			resultSet.absolute( row + 1 );
			return resultSet.getObject( column + 1 );
		}
		catch ( SQLException sqlException ) { sqlException.printStackTrace(); }
      
		return "";
	}
	
	// set new database update-query string
	public void setUpdate( String query ) throws SQLException, IllegalStateException 
	{
		// specify query and execute it
	    sqlClient.getStatement().executeUpdate( query );
	    JOptionPane.showMessageDialog( null, "Your query:\n\n" + query + "\n\nwas completed successfully.",
				"Success", JOptionPane.INFORMATION_MESSAGE );
	    fireTableStructureChanged();
	}
	
	// Sets up a new database query string
	public void setQuery( String query ) throws SQLException, IllegalStateException 
	{
		resultSet = sqlClient.getStatement().executeQuery( query );
		metaData = resultSet.getMetaData();
		resultSet.last();
		numberOfRows = resultSet.getRow();
		
		fireTableStructureChanged();
	}
}