package com.borgge.stat.loganalyz;
import java.io.File;
import java.io.IOException;


public class Config {
	
	
	
	
	
	
	

	public static void main(String[] args) throws IOException {
//		Properties prop = new Properties();
//		FileInputStream fis = new FileInputStream("config.properties");
//		prop.load(fis);
//		prop.list(System.out);
//		
//		System.out.println(prop.get("remote_ip"));
		
		
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE,-1);
//		SimpleDateFormat dateformat1 = new SimpleDateFormat(
//				"yyyy-MM-dd");
//		System.out.println(dateformat1.format(c.getTime()));
		
		File fis = new File("./log/");
		System.out.println(fis.toString());
		
		
	}

}
