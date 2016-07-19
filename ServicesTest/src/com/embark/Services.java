package com.embark;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.ws.rs.core.MediaType;

public class Services {
	
	
	Logger logger = Logger.getLogger(Services.class);
    public boolean sendData() throws JSONException, IOException{
	int responseCode = 0;
	try {
		String localData = "";
		SqliteDataFetching dataFetching = new SqliteDataFetching();
		localData = dataFetching.displayTable();
		String host = "172.25.16.38";
		String url = "http://"+host+":8050/Embark/rest/webservice/insertDataInHibernate";
		URL obj = new URL(url);
		HttpURLConnection httpconn = (HttpURLConnection) obj.openConnection();
		httpconn.setRequestMethod("POST");
		httpconn.setRequestProperty("User-Agent","");
		httpconn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		httpconn.setRequestProperty("Media-Type", MediaType.APPLICATION_JSON);
		String urlParameters = localData;
		httpconn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(httpconn.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		logger.info("httpconn ...."+httpconn);
		responseCode = httpconn.getResponseCode();
		logger.info("\nSending 'POST' request to URL : " + url);
		logger.info("Post parameters : " + urlParameters);
		logger.info("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(
		new InputStreamReader(httpconn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			}
		in.close();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	if(responseCode == 200){
		return true;
	}else{
		return false;
	}
	}
public void loggInfo(String data) throws IOException, InterruptedException{
	try{			
		File file = new File("C:\\Embark_LOGGER.txt");
		file.createNewFile();
		FileWriter fileWriter = new FileWriter(file,true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
        bufferWritter.write(data+ " \n");
        bufferWritter.close();
			
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public void executeQueries() throws IOException, InterruptedException{
	try{
				Services st = new Services();
			    st.loggInfo("Connected to the database successfully");
		         TallyResponse tr = new TallyResponse();
		        	tr.run();
		        	//time.schedule(tr,0,4000);
		        	XmlConversion c1 = new XmlConversion();
		        	c1.run();
		 			//time.schedule(c1, 0, 4000);
		 			boolean response =  st.sendData();
		 		    if(response == true){
			 			DeleteDBData delete = new DeleteDBData();
			 			delete.run();
			 			//time.schedule(delete,0,4000);
			 			logger.info("Data Deleted Successfully from The Table");
			 			st.loggInfo("Data Deleted Successfully from The Table");
		 			}else{
		 				logger.info("Data Not Deleted From The Local Database");
		 			}
		 		   
		 		logger.info("Response" +response);
		 		st.loggInfo("Response: " +response);
		 		Thread.sleep(180*60*1000); // 3 Hours
		 		executeQueries();
		         
		         }
		catch(Exception e){
			Services st = new Services();
			st.loggInfo("Error: "+e.toString());
			Thread.currentThread().interrupt();
			e.printStackTrace();
			
			}
}
public static void main(String[] args) throws IOException, InterruptedException {
	Logger logger = Logger.getLogger(Services.class);
    BasicConfigurator.configure();
    logger.info("This is my first log4j's statement");
		// TODO Auto-generated method stub
		Connection con = null;
		Statement stmt = null;
		try{
			//CREATE DATABASE IN SQLITE
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:SalexDB.db");
			 stmt = con.createStatement();			 
			 logger.info("Connected to the database successfully");
			 //CREATE TABLE IN SQLITE
			 String userTable = "CREATE TABLE IF NOT EXISTS USERCONFIGURATION "+ 
			         "(IP VARCHAR ,"+
						"PORTNUMBER INTEGER, "+
								"DIVISIONID INTEGER Unique, "+
								"INTERVAL INTEGER, "+
								"LASTSYNCTIME DATE)";
			 stmt.executeUpdate(userTable);
			 logger.info("Created userConfiguration Table successfully");
			 String invoice = "CREATE TABLE IF NOT EXISTS invoice "+ 
			           "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
						"VOUCHER_KEY INTEGER, "+
								"PARTY_LEDGER_NAME VARCHAR, "+
								"LEDGER_NAME VARCHAR, "+
								"PARTY_NAME VARCHAR, "+
								"EXTERNAL_VOUCHERKEY Unique, "+
								"STOCK_ITEM_NAME VARCHAR,"+ 
								"RATE INTEGER,"+
								"BILLED_QTY INTEGER, "+
								"CREATE_DATE DATE,"+
								"INVOICE_DATE DATE,"+
								"LAST_MDF_DATE DATE)";
			 stmt.executeUpdate(invoice);
			 logger.info("Created Tally Invoice Table successfully");
			 //INSERT DATA INTO THE USERCONFIGURATION TABLE
			 String insertData = "INSERT OR IGNORE INTO USERCONFIGURATION(IP,PORTNUMBER,DIVISIONID,INTERVAL,LASTSYNCTIME)values('172.25.16.38',9000,1234,3,datetime('now', 'localtime'))";
			 stmt.executeUpdate(insertData);
			 logger.info("Data successfully inserted into the userConfiguration Table");
			 stmt.close();
		     con.close();
		     Services st = new Services();
		     st.executeQueries();
			         }
			catch(Exception e){
				Services st = new Services();
				st.loggInfo("Error: "+e.toString());
				Thread.currentThread().interrupt();
				e.printStackTrace();
				
				}
	}
		     
}


