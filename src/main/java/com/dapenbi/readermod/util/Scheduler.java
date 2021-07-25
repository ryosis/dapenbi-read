package com.dapenbi.readermod.util;

import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dapenbi.readermod.nio.ControlFile;


@Component
public class Scheduler {
	@Value("${doc.path}")
	private String docPath;
	
	@Value("${doc.txt}")
	private String docTxt;
	
	private Logger log;	

	@Scheduled(cron = "* * * * * *") public void cronJobSch() {
		log = Logger.getLogger("com.dapenbi.readermod.util()");

		ControlFile controlFileObj = new ControlFile(docPath);
		try {
			controlFileObj.setLockFile();
			if (controlFileObj.isFileLocked()) {
				controlFileObj.setUnlockFile();
				File file = new File(docPath);
				String str = FileUtils.readFileToString(file, "utf-8");
				String lines[] = str.split("\\r?\\n");
				if (!(lines[0].equals(""))) 
					for (int i = 0; i < lines.length; i++) {
						String arrlines[] = lines[i].split(",");
						System.out.println("baris "+i+ " : "+lines[i]);
						String errMsg = errorData(arrlines[0], arrlines[1], arrlines[2], arrlines[3], arrlines[4], arrlines[5], arrlines[6]);
						if(!errMsg.equals("")) { 
							log.log(Level.SEVERE, errMsg);
						} else {
							File fileNip = new File(docTxt+arrlines[0]+".txt");
							PrintWriter pwNip = new PrintWriter(fileNip);
							pwNip.println("Nip : "+arrlines[0]);
							pwNip.println("Name : "+arrlines[1]);
							pwNip.println("Date of Birth : "+arrlines[2]);
							pwNip.println("Gender : "+arrlines[3]);
							pwNip.println("Address : "+arrlines[4]);
							pwNip.println("ID Number : "+arrlines[5]);
							pwNip.println("Phone Number : "+arrlines[6]);
							pwNip.close();
							if (arrlines[3].equals("F")) {
								log.info("SMS sent to Nip : " + arrlines[0] + " is Female");
							} else {
								log.info("SMS sent to Nip : " + arrlines[0] + " is Male");
							}
						}
					}
								
				PrintWriter pw = new PrintWriter(file);
				pw.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private String errorData(String nip,String name,String dob,String gender,String address,String idnum,String phone) {
		String errMessage = "";
		if(nip.equals("") || nip == null) {
			errMessage += "Nip is Empty;";
		}
		
		if(name.equals("") || name.equals("null")) {
			errMessage += "Name is Empty;";
		} else if(!(name.matches("^[a-zA-Z]+$"))) {
			errMessage += "Name should character;";
		}
		
		if(dob.equals("")) {
			errMessage += "Date of birth is Empty;";
		} else {					
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date now = new Date();
				Date objDob = sdf.parse(dob);
				int result = objDob.compareTo(now);
				
				if (result == 0) {
					errMessage +=  "";
		        } else if (result > 0) {
		        	errMessage +=  "";
		        } else if (result < 0) {
		        	String strNow = sdf.format(now);
		        	if(!strNow.equals(dob))
		        		errMessage += "Date of birth should equal or future;";
		        } else {
		        	errMessage += "Date error";
		        }				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!(gender.equals("F") || gender.equals("M"))) {
			errMessage += "False gender value;";
		}
		
		if(address.length() < 20) {
			errMessage += "Address minimum 20;";
		}
		
		if(idnum.equals("") || idnum.equals("null")) {
			errMessage += "ID Number is Empty;";
		} else if(!(idnum.matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$"))) {
			errMessage += "ID Number should contains character and number;";
		}
		
		if(phone.equals("")) {
			errMessage += "Phone number is Empty;";
		} else if(!((phone.substring(0,2)).equals("+6"))) {
			errMessage += "Phone number format false, eg +62;";
		}
		return errMessage;
	}
}