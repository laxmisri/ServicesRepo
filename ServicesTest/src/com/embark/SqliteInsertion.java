package com.embark;
import java.sql.*;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
public class SqliteInsertion extends TimerTask {
	Logger logger = Logger.getLogger(SqliteInsertion.class);
 public String dataInsertion(JSONArray fileData){
	 JSONObject currentRecord  = new JSONObject();
	 //String curDir = System.getProperty("user.dir");
	 String url = "jdbc:sqlite:SalexDB.db";
	 Connection con = null;
	 Statement  stmt = null;
	 try {
		
			Class.forName("org.sqlite.JDBC");
		    con = DriverManager.getConnection(url);
		    stmt = con.createStatement();
		    for(int count = 0; count < fileData.length(); count++){
				currentRecord = (JSONObject) fileData.get(count);
				String insertQuery = "INSERT OR IGNORE INTO invoice (VOUCHER_KEY, PARTY_LEDGER_NAME, LEDGER_NAME, PARTY_NAME,EXTERNAL_VOUCHERKEY, STOCK_ITEM_NAME, RATE,BILLED_QTY, CREATE_DATE, INVOICE_DATE, LAST_MDF_DATE) VALUES("+currentRecord.getString("voucherKey")+", '"+currentRecord.getString("partyLedgerName")+"', '"+currentRecord.getString("ledgerName")+"', '"+currentRecord.getString("partyName")+"',"+currentRecord.getString("externalVoucherKey")+", '"+currentRecord.getString("stockItemName")+"', "+currentRecord.getString("rate")+", "+currentRecord.getString("billedQty")+",datetime('now', 'localtime'), date('"+currentRecord.getString("invoiceDate")+"'), datetime('now', 'localtime'))";
				stmt.execute(insertQuery );
				
				}
			 stmt.close();
			 con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 logger.info("succesfully inserted into table ");
	 return currentRecord.toString();
	 
	 }

@Override
public void run() {
	// TODO Auto-generated method stub
	
}
 }

