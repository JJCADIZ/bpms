package com.example.bpm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

public class ConnectionClass {
	//String ip = "182.50.133.110";
	String ip = "sql5026.mywindowshosting.com";
	String classs = "net.sourceforge.jtds.jdbc.Driver";
	String db = "DB_A12009_ftsjartillaga";
	String un = "DB_A12009_ftsjartillaga_admin";
	String password = "KApitantutan101";
	
	@SuppressLint("NewApi")
	public Connection CONN() {
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	.permitAll().build();
	StrictMode.setThreadPolicy(policy);
	Connection conn = null;
	String ConnURL = null;
	try {
	Class.forName(classs);
	ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
	+ "databaseName=" + db + ";user=" + un + ";password="
	+ password + ";";
	conn = DriverManager.getConnection(ConnURL);
	} catch (SQLException se) {
	Log.e("ERRO", se.getMessage());
	} catch (ClassNotFoundException e) {
	Log.e("ERRO", e.getMessage());
	} catch (Exception e) {
	Log.e("ERRO", e.getMessage());
	}
	return conn;
	}
}
