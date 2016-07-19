package com.embark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SqliteDataFetching extends TimerTask{
	Logger logger = Logger.getLogger(SqliteDataFetching.class);
	public String displayTable() {
		String displayStatus = "";
		Connection con = null;
		Statement stmt = null;
		JSONArray jsonOutput = new JSONArray();
		try{
			Class.forName("org.sqlite.JDBC");
			 con = DriverManager.getConnection("jdbc:sqlite:SalexDB.db");
			 stmt = con.createStatement();
			 logger.info("Connected to the database successfully");
			     ResultSet rs=stmt.executeQuery("select * from invoice");
		     while (rs.next()) {
		    	 JSONObject js = new JSONObject();
				 js.put("ID",rs.getString("ID"));
				 js.put("VOUCHER_KEY",rs.getString("VOUCHER_KEY"));
				 js.put("PARTY_LEDGER_NAME", rs.getString("PARTY_LEDGER_NAME"));
				 js.put("LEDGER_NAME",rs.getString("LEDGER_NAME"));
				 js.put("PARTY_NAME", rs.getString("PARTY_NAME"));
				 js.put("EXTERNAL_VOUCHERKEY",rs.getString("EXTERNAL_VOUCHERKEY"));
				 js.put("STOCK_ITEM_NAME", rs.getString("STOCK_ITEM_NAME"));
				 js.put("RATE", rs.getString("RATE"));
				 js.put("BILLED_QTY", rs.getString("BILLED_QTY"));
				 js.put("CREATE_DATE", rs.getString("CREATE_DATE"));
				 js.put("INVOICE_DATE", rs.getString("INVOICE_DATE"));
				 js.put("LAST_MDF_DATE", rs.getString("LAST_MDF_DATE"));
				 jsonOutput.put(js);
			 }
		     rs.close();
		     stmt.close();
    con.close();
    logger.info("Fetched Data:"+ jsonOutput.toString());
    logger.info("Data Display successfully!");
    return jsonOutput.toString();
    
    } 
	  catch(Exception e){
		logger.info("Error---> dataDisplay---> " + e.getMessage());
		e.printStackTrace();
	}
	
    return displayStatus.toString();
    
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.displayTable();
	}
}
