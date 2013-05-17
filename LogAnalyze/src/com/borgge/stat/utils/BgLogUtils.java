package com.borgge.stat.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.joda.time.DateTime;


public class BgLogUtils {

	
	public static String earliest_date = "2000-01-01";
	
	
	public static Logger getLogger() throws SecurityException, IOException {
		Logger log = Logger.getLogger("logParser");
		FileHandler fileHandler = new FileHandler("./logPaser_" + getTodayTimeStr()
				+ ".log");
		fileHandler.setLevel(Level.ALL);
		fileHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				return getTodayTimeStr() + " " + record.getLevel() + ":"
						+ record.getMessage() + "\n";
			}
		});
		log.addHandler(fileHandler);
		return log;
	}
	
	
	public static String getTodayTimeStr() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd_HH-mm-ss");
		return dateformat1.format(new Date());
	}
	
	public static String getToday() {
		return (new DateTime().toString("yyyy-MM-dd")); 
	}
	
	public static String getYesterday() {
		return getYesterday(new DateTime().toString("yyyy-MM-dd")); 
	}
	
	public static String getYesterday(String date) {
		return dateBeforeSomeDate(date, 1);
	}
	
	public static String getLastMonth() {
		return getLastMonth(new DateTime().toString("yyyy-MM-dd")); 
	}
	
	public static String getLastMonth(String date) {
		return dateBeforeSomeDate(date, 30);
	}


	private static String dateBeforeSomeDate(String date, int offset) {
		DateTime dt = new DateTime(date);
		return dt.minusDays(offset).toString("yyyy-MM-dd");
	}
	
	public static String getLastWeek() {
		return getLastWeek(new DateTime().toString("yyyy-MM-dd"));
	}
	
	public static String getLastWeek(String date) {
		return dateBeforeSomeDate(date, 7);
	}
	
	public static String getLastSomeDay(String start, int length) {
		return dateBeforeSomeDate(start, length);
	}
	
	
	
	static int buffersize = 2048;
	    /** 
	     * Uncompress the incoming file. 
	     * @param inFileName Name of the file to be uncompressed 
	     */ 
	
    public static void deCompressTGZFile(String file) {  
        File tmp = deCompressGZFile(new File(file));
        deCompressTARFile(tmp);
    }  
  
    private static File deCompressGZFile(File file) {  
        FileOutputStream out = null;  
        GzipCompressorInputStream gzIn = null;  
        try {  
            FileInputStream fin = new FileInputStream(file);  
            BufferedInputStream in = new BufferedInputStream(fin);  
            File outFile = new File(file.getParent() + File.separator  
                    + "tmp.tar");  
            out = new FileOutputStream(outFile);  
            gzIn = new GzipCompressorInputStream(in);  
            final byte[] buffer = new byte[buffersize];  
            int n = 0;  
            while (-1 != (n = gzIn.read(buffer))) {  
                out.write(buffer, 0, n);  
            }  
            return outFile;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
            try {  
                out.close();  
                gzIn.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private static void deCompressTARFile(File file) {  
        String basePath = file.getParent() + File.separator;  
        TarArchiveInputStream is = null;  
        try {  
            is = new TarArchiveInputStream(new FileInputStream(file));  
            while (true) {  
                TarArchiveEntry entry = is.getNextTarEntry();  
                if (entry == null) {  
                    break;  
                }  
                if (entry.isDirectory()) {// 这里貌似不会运行到，跟ZipEntry有点不一样  
                    new File(basePath + entry.getName()).mkdirs();  
                } else {  
                    FileOutputStream os = null;  
                    try {  
                        File f = new File(basePath + entry.getName());  
                        if (!f.getParentFile().exists()) {  
                            f.getParentFile().mkdirs();  
                        }  
                        if (!f.exists()) {  
                            f.createNewFile();  
                        }  
                        os = new FileOutputStream(f);  
                        byte[] bs = new byte[buffersize];  
                        int len = -1;  
                        while ((len = is.read(bs)) != -1) {  
                            os.write(bs, 0, len);  
                        }  
                        os.flush();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    } finally {  
                        os.close();  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                is.close();  
                file.delete();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
//	    public static void doUncompressFile(String inFileName) { 
//
//	        try { 
//
//	            if (!getExtension(inFileName).equalsIgnoreCase("gz")) { 
//	                System.err.println("File name must have extension of \".gz\""); 
//	                System.exit(1); 
//	            } 
//
//	            System.out.println("Opening the compressed file."); 
//	            GZIPInputStream in = null; 
//	            try { 
//	                in = new GZIPInputStream(new FileInputStream(inFileName)); 
//	            } catch(FileNotFoundException e) { 
//	                System.err.println("File not found. " + inFileName); 
//	                System.exit(1); 
//	            } 
//
//	            System.out.println("Open the output file."); 
//	            String outFileName = getFileName(inFileName); 
//	            FileOutputStream out = null; 
//	           try { 
//	                out = new FileOutputStream(outFileName); 
//	            } catch (FileNotFoundException e) { 
//	                System.err.println("Could not write to file. " + outFileName); 
//	                System.exit(1); 
//	            } 
//
//	            System.out.println("Transfering bytes from compressed file to the output file."); 
//	            byte[] buf = new byte[1024]; 
//	            int len; 
//	            while((len = in.read(buf)) > 0) { 
//	                out.write(buf, 0, len); 
//	            } 
//
//	            System.out.println("Closing the file and stream"); 
//	            in.close(); 
//	            out.close(); 
//	        
//	        } catch (IOException e) { 
//	            e.printStackTrace(); 
//	            System.exit(1); 
//	        } 
//
//	    } 

	    /** 
	     * Used to extract and return the extension of a given file. 
	     * @param f Incoming file to get the extension of 
	     * @return <code>String</code> representing the extension of the incoming 
	     *         file. 
	     */ 
	    public static String getExtension(String f) { 
	        String ext = ""; 
	        int i = f.lastIndexOf('.'); 

	        if (i > 0 &&  i < f.length() - 1) { 
	            ext = f.substring(i+1); 
	        }      
	        return ext; 
	    } 

	    /** 
	     * Used to extract the filename without its extension. 
	     * @param f Incoming file to get the filename 
	     * @return <code>String</code> representing the filename without its 
	     *         extension. 
	     */ 
	    public static String getFileName(String f) { 
	        String fname = ""; 
	        int i = f.lastIndexOf('.'); 

	        if (i > 0 &&  i < f.length() - 1) { 
	            fname = f.substring(0,i); 
	        }      
	        return fname; 
	    } 

	    /** 
	     * Sole entry point to the class and application. 
	     * @param args Array of String arguments. 
	     */ 
	    public static void main(String[] args) { 
	    
	       
//	            doUncompressFile("E:\\AUTORUN.INF.gz"); 
	       

	    } 
}
