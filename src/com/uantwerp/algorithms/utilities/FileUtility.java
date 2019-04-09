package com.uantwerp.algorithms.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.uantwerp.algorithms.exceptions.SubGraphMiningException;

public abstract class FileUtility {
	
	/**
	 * 
	 * @param path	path to an input file
	 * @return content	the content of the input file in String format
	 * 
	 * throws an exception when the file is empty or when the path cannot be resolved
	 */
	public static String readFile(String path)
    {
        byte[] encoded;
        try {
			encoded = Files.readAllBytes(Paths.get(path));
			String content = new String(encoded, Charset.defaultCharset());
			// Throw exception when supplied file is empty
			if (!AlgorithmUtility.checkNotEmptyFileContent(content))
				SubGraphMiningException.exceptionEmptyFile(path);
			return content;
		} catch (IOException e) {
			// Throw exception if path cannot be resolved
			SubGraphMiningException.exceptionFileNotExists(path);
		}
		return null;	// should never be reached
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
