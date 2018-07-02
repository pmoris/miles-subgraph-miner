package com.uantwerp.algorithms.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class FileUtility {

	public static String getFile(String pathFile){
		String charFile = "";
		File file = new File(pathFile);
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			int content;
			while ((content = fis.read()) != -1) {
				charFile = charFile + (char) content;
			}
		} catch (IOException e) {
			charFile = "";
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				charFile = "";
			}
		}
		return charFile;
	}
	
	public static String readFile(String path)
    {
        byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
	
	public static void writeFile(String path, String message){
		try {
			File file = new File(path);
			file.delete();
			FileWriter writer =  new FileWriter(path,true);
			writer.write(message);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
