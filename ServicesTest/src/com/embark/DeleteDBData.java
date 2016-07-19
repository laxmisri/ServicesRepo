package com.embark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class DeleteDBData extends TimerTask {
	Logger logger = Logger.getLogger(DeleteDBData.class);
	public String deleteData(){
	Connection con = null;
	 Statement  stmt = null;
	 try{
		 Class.forName("org.sqlite.JDBC");
		 con = DriverManager.getConnection("jdbc:sqlite:SalexDB.db");
		 stmt = con.createStatement();
		 String delete = "DELETE FROM invoice";
		 stmt.executeUpdate(delete);
	 }
	 catch(Exception e){
		
	 }
	 return "";
}

	@Override
	public void run() {
		try {
			this.deleteData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}