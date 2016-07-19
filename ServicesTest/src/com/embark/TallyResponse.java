package com.embark;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;


public class TallyResponse extends TimerTask {
	Logger logger = Logger.getLogger(TallyResponse.class);
	public String createResponse() throws Exception {
		String TXML = null;
		Date currentDate = new Date();
		SimpleDateFormat d = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = d.format(currentDate);
		logger.info("Current date:"+nowDate);
		TXML = "<ENVELOPE>"
                + "<HEADER>"
                + "<VERSION>1</VERSION>"
                + "<TALLYREQUEST>Export</TALLYREQUEST>"
                + "<TYPE>Data</TYPE>"
                + "<ID>Day Book</ID>"
                + "</HEADER>"
                + "<BODY>"
                + "<DESC>"
                    + "<STATICVARIABLES>"
                       + "<SVFROMDATE TYPE=\"Date\">"+nowDate+"</SVFROMDATE>"
                        + "<SVTODATE TYPE=\"Date\">"+nowDate+"</SVTODATE>"
                        + "<EXPLODEFLAG>Yes</EXPLODEFLAG>"
                        + "<SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>"                
                    + "</STATICVARIABLES>"
                + "</DESC>"                                
                + "</BODY>"
                + "</ENVELOPE>";

          return TXML;
}
	public String tallyToConsole() throws Exception {
		String Url = "http://172.25.16.38:9000/";

		String SOAPAction = "";

		String Voucher = this.createResponse();

		// Create the connection where we're going to send the file.
		URL url = new URL(Url);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;

		ByteArrayInputStream bin = new ByteArrayInputStream(Voucher.getBytes());
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		// Copy the SOAP file to the open connection.

		copy(bin, bout);

		byte[] b = bout.toByteArray();

		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		// Everything's set up; send the XML that was read in to b.
		OutputStream out = httpConn.getOutputStream();
		out.write(b);
		out.close();

		// Read the response and write it to standard out.

		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		String finalOutput = "";
		String inputLine;
		String curDir = System.getProperty("user.dir");
		File file = new File(curDir, "Embark.xml");
		file.createNewFile();
		FileWriter fileWriter = new FileWriter(file);
		while ((inputLine = in.readLine()) != null) {
			fileWriter.write(inputLine);
			finalOutput = finalOutput + inputLine;
		}
		fileWriter.flush();
		fileWriter.close();
		in.close();
		return finalOutput;
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {

		// do not allow other threads to read from the
		// input or write to the output while copying is
		// taking place

		 synchronized (in) {
			synchronized (out) {

				byte[] buffer = new byte[256];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1) {
						break;
					}
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}
	@Override
	public void run() {
	try {
		this.tallyToConsole();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
		
	}
}

