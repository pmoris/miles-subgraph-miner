package com.uantwerp.algorithms.visualisation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class HTMLCreator {
	
	public static String createHTML(String outputPath, String elementsJSON) throws IOException {
//		File htmlTemplate = new File("resources/template.html");
		
		
//		System.out.println(this.getClass());
		System.out.println(("classpath is: " + System.getProperty("java.class.path")));
//		System.out.println(getClass().getResource("/template.html"));
//		System.out.println(this.getClass().getResource("/template.html"));
		System.out.println(HTMLCreator.class.getResource("/template.html"));

		// static version     URL songPath = MyClass.class.getResource("background.midi");
		//https://stackoverflow.com/questions/8275499/how-to-call-getclass-from-a-static-method-in-java
//		ClassLoader.getResource("template.html");
		
		InputStream is = HTMLCreator.class.getResourceAsStream("/template.html");
//		InputStream is = getClass().getResourceAsStream("/template.html"); 
		String htmlString = IOUtils.toString(is, StandardCharsets.UTF_8);
//		String htmlString = FileUtils.readFileToString(htmlTemplate);
		
		// insert css
		is = HTMLCreator.class.getResourceAsStream("/style.css");
		String cssString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-STYLE.CSS", cssString);
		
		// insert js code
		is = HTMLCreator.class.getResourceAsStream("/cytoscape.min.js");
		String cytoscapeJsString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-CYTOSCAPE.MIN.JS", cytoscapeJsString);
		
		is = HTMLCreator.class.getResourceAsStream("/code.js");
		String codeJsString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-CODE.JS", codeJsString);
		
		// insert JSON data	
		htmlString = htmlString.replace("$INCLUDE-ELEMENTS", elementsJSON);

		return htmlString;
	}

}
