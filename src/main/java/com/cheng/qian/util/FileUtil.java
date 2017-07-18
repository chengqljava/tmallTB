package com.cheng.qian.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil {
	  public void WriteStringToFile(String filePath, String content) {
	        try {
	            PrintWriter pw = new PrintWriter(new FileWriter(filePath));
	            pw.println(content);
	            pw.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    // 判断文件夹是否存在
	    public static void judeDirExists(String fileName) {
	        File file = new File(fileName);
	        if (file.exists()) {
	            if (file.isDirectory()) {
	                System.out.println("dir exists");
	            } else {
	                System.out.println("the same name file exists, can not create dir");
	            }
	        } else {
	            System.out.println("dir not exists, create it ...");
	            file.mkdir();
	        }

	    }

	    // 判断文件是否存在
	    public static void judeFileExists(File file) {

	        if (file.exists()) {
	            System.out.println("file exists");
	        } else {
	            System.out.println("file not exists, create it ...");
	            try {
	                file.createNewFile();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }

	    }

}
