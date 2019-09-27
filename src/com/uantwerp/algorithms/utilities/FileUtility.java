package com.uantwerp.algorithms.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.uantwerp.algorithms.exceptions.SubGraphMiningException;

public abstract class FileUtility {
	
	/**
	 * 
	 * @param path	path to an input file
	 * @return content	a file object of the input file
	 * 
	 * throws an exception when the file is empty or when the path cannot be resolved
	 */
	public static File readFile(String path) {
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			// Throw exception if path cannot be resolved
			SubGraphMiningException.exceptionFileNotExists(path);
		}
		if (file.length() == 0)
			// Throw exception if file is empty
			SubGraphMiningException.exceptionEmptyFile(path);
		return file;
	}
	
	public static void writeFile(String path, String message) throws IOException {
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
