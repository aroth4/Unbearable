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
		Connection conn = DriverManager.getConnection("jdbc:derby:H:/unbearable.db;create=true");
		
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
	
				try {
					// login table
					stmt = conn.prepareStatement(
							"create table login (" +
							"   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
							"   username varchar(40) not null unique," +
							"   password varchar(40) not null" +
							")"
							);
					stmt.executeUpdate();
					
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
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
					insertLogin = conn.prepareStatement("insert into login (username, password) values (?, ?)");
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
    // login
	@Override
	public Login getLogin(final String username, final String password) {
		return executeTransaction(new ITransaction<Login>() {
			@Override
			public Login execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
		
				try {
					stmt = conn.prepareStatement(
						"select login.*" +
						" from login" + 
						" where login.username = ?" +
						" and login.password = ?"
					);
					stmt.setString(1, username);
					stmt.setString(2, password);
					
					resultSet = stmt.executeQuery();
					
					if (resultSet.next()){
						
						return new Login(username,password);
					}else{
						return null;
					}
					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	// responsible for registration
	@Override
	public Login postLogin(final String username, final String password) {
		return executeTransaction(new ITransaction<Login>() {
			@Override
			public Login execute(Connection conn) throws SQLException {
				Login existing = doFindLogin(username, conn);
				if (existing != null) {
					System.out.println("That username has already been registered!");
					return null;
				}
				
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement("insert into login (username, password) values (?, ?)");
					System.out.println("It registered: "+username+" and " + password);
					stmt.setString(1, username);
					stmt.setString(2, password);
					
					stmt.executeUpdate();
				
					return new Login(username,password);
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
	
	// method to determine if username exist in database
	private Login doFindLogin(final String username, Connection conn)
			throws SQLException {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Login accountLogin = null;
		try {
			stmt = conn.prepareStatement(
				"select login.*" +
				" from login" + 
				" where login.username = ?"
			);
			stmt.setString(1, username);
			
			resultSet = stmt.executeQuery();
			
			if (resultSet.next()){;
				accountLogin = new Login();
				accountLogin.setUsername(username);
				return accountLogin;
			}else{
				return accountLogin;
			}
			
		} finally {
			DBUtil.closeQuietly(resultSet);
			DBUtil.closeQuietly(stmt);
		}
	}
	

}
