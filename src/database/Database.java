package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	private Scanner scan;
	private final int GOV_CLEAR = 4;
	private final int NURSE_CLEAR = 2;
	private final int DOCTOR_CLEAR = 3;
	private final int PAT_CLEAR = 1;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
		scan = new Scanner(System.in);
		init();
	}

	private void init() {
		if (openConnection("db51", "marcus")) {
			System.out.println("connected");
		}

	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	private boolean openConnection(String userName, String password) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
			// String url = "jdbc:MySql://localhost:3306/databaseName";
			// conn =
			// DriverManager.getConnection("jdbc:MySql://localhost:3306/Datasakerhet","root","");

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	private void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public String Command(String command, String userId) {

		String[] commands = command.split(":");

		if (commands[0].equals("delete") && getClearance(userId) == GOV_CLEAR) {
			return deleteFile(userId, commands);
		} else if (commands[0].equals("read")) {
			return readFile(userId, commands);
		} else if (commands[0].equals("write")
				&& getClearance(userId) > PAT_CLEAR
				&& getClearance(userId) < GOV_CLEAR) {

			return writeFile(userId, commands);

		} else if (commands[0].equals("create")
				&& getClearance(userId) == DOCTOR_CLEAR) {
			return createFile(userId, commands);
		} else {
			return "invalid command or you don't have clearance";
		}

	}

	private String readFile(String userId, String[] commands) {

		String filename = commands[1];
		boolean clear = false;
		if (getClearance(userId) < GOV_CLEAR) {
			String group = getGroup(userId);
			try {
				String sql = "select name from journals where doc_pNbr  = ? or nurse_pNbr = ? or patient_pNbr = ? or jor_groupName = ? ";
				stmt = conn.prepareStatement(sql);

				stmt.setString(1, userId);
				stmt.setString(2, userId);
				stmt.setString(3, userId);
				stmt.setString(4, group);
				ResultSet rsa = stmt.executeQuery();
				ArrayList<String> list = new ArrayList<String>();

				while (rsa.next()) {

					list.add(rsa.getString(1));

				}
				if (list.contains(filename)) {
					clear = true;
				} else {
					return "filen finns ej eller så har du ej clearance";
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			clear = true;
		}
		if (clear) {
			String file = "";
			try {
				String sql = "select text from journals where name = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, filename);
				rs = stmt.executeQuery();

				while (rs.next()) {
					file = rs.getString(1);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return file;
		}

		return "Något blev fel";
	}

	private String writeFile(String userId, String[] commands) {
		String filename = commands[1];
		String text = commands[2];

		boolean clear = false;
		try {
			String sql = "select name from journals where doc_pNbr  = ? or nurse_pNbr = ?  ";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, userId);
			stmt.setString(2, userId);

			ResultSet rsa = stmt.executeQuery();
			ArrayList<String> list = new ArrayList<String>();

			while (rsa.next()) {

				list.add(rsa.getString(1));

			}
			if (list.contains(filename)) {
				clear = true;
			} else {
				return "filen finns ej eller så har du ej clearance";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (clear) {

			String s = text;
			try {
				String sql = "update journals set text = ? where name = ?;";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, s);
				stmt.setString(2, filename);
				stmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "uppdate successfull";
		}
		return "fuck något blev fel";

	}

	private String createFile(String userId, String[] commands) {
		String fileName = commands[1];
		String nurse = commands[2];
		String patient = commands[3];
		String text = commands[4];
		String group = getGroup(userId);
		try {
			String sql = "insert into journals values(?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fileName);
			stmt.setString(2, group);
			stmt.setString(3, userId);
			stmt.setString(4, patient);
			stmt.setString(5, nurse);
			stmt.setString(6, text);
			int i = stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return "filen är skapad";
	}

	private String deleteFile(String userId, String[] commands) {
		String fileName = commands[1];
		if (dbContainsFile(fileName)) {
			try {
				String sql = "delete from journals journals where name = ? ";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, fileName);
				int i = stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return fileName + " har tagits bort";
		}
		return "Filen finns ej";
	}

	public boolean dbContainsFile(String filename) {
		try {
			String sql = "select name from journals where name = ? ";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, filename);
			rs = stmt.executeQuery();
			String s = "";
			while (rs.next()) {
				s = rs.getString(1);

			}
			if (s == filename) {

				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public int getClearance(String userId) {
		int i = -1;
		try {
			String sql = "select clearance from user where pNbr = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				i = rs.getInt(1);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	public String getGroup(String userId) {
		String s = "";
		try {
			String sql = "select groupName from user where pNbr =?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	private void log(String log) {

	}

}
