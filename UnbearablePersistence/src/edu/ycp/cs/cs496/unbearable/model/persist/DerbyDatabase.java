package edu.ycp.cs.cs496.unbearable.model.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs.cs496.unbearable.model.Login;


public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby JDBC driver");
		}
	}
	
	private interface ITransaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}
	//nothing
	private static final int MAX_ATTEMPTS = 10;

	
	

	
	

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:H:/stocksimulation.db;create=true");
		
		// Set autocommit to false to allow multiple the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}

	public<ResultType> ResultType executeTransaction(ITransaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	public<ResultType> ResultType doExecuteTransaction(ITransaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						System.out.println("SQLException error has occured!");
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	public void createTables() {
		executeTransaction(new ITransaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				try {
					// login table
					stmt = conn.prepareStatement(
							"create table logins (" +
							"   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"   username varchar(40) not null unique," +
							"   password varchar(40) not null" +
							")"
							);
					stmt.executeUpdate();
					// cash table
					stmt2 = conn.prepareStatement(
							"create table score (" +
							"   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"   cashAmount money not null" +
							")"
							);
					stmt2.executeUpdate();
					
					
					return true;
				} finally {
					DBUtil.closeQuietly(stmt);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
				}
			}
		});
	}
	
	public void loadInitialData() {
		executeTransaction(new ITransaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				

				PreparedStatement insertLogin = null;
				
				
				// initial user
				Login log = new Login();
				log.setUsername("admin");
				log.setPassword("admin");
				
				try {
					// login
					insertLogin = conn.prepareStatement("insert into logins (username, password) values (?, ?)");
					insertLogin.setString(1, log.getUsername());
					insertLogin.setString(2, log.getPassword());
					
					insertLogin.executeUpdate();
					
					
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertLogin);
						
				}
			}
		});
	}
	
	

	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();
		
		System.out.println("Loading initial data...");
		db.loadInitialData();
		
		System.out.println("Success!");
	}

	@Override
	public Login getLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Login postLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}
