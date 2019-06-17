package com.uantwerp.algorithms.visualisation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.GraphParameters;

public class HTMLCreator {
	
	public static String createHTML(String elementsJSON, String message, double bonferroni, int sig) throws IOException {
		InputStream is = HTMLCreator.class.getResourceAsStream("/template.html");
		String htmlString = IOUtils.toString(is, StandardCharsets.UTF_8);

		// insert css
		is = HTMLCreator.class.getResourceAsStream("/style.css");
		String cssString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-STYLE.CSS", cssString);

		is = HTMLCreator.class.getResourceAsStream("/skeleton.css");
		String skeletonString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-SKELETON.CSS", skeletonString);

		is = HTMLCreator.class.getResourceAsStream("/normalize.css");
		String normalizeString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-NORMALIZE.CSS", normalizeString);

		// insert js code
		is = HTMLCreator.class.getResourceAsStream("/cytoscape.min.js");
		String cytoscapeJsString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-CYTOSCAPE.MIN.JS", cytoscapeJsString);

		is = HTMLCreator.class.getResourceAsStream("/code.js");
		String codeJsString = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-CODE.JS", codeJsString);
		
		is = HTMLCreator.class.getResourceAsStream("/FileSaver.min.js");
		String codeJsFileSaver = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-FILESAVER.JS", codeJsFileSaver);

		// insert JSON data	
		htmlString = htmlString.replace("$INCLUDE-ELEMENTS", elementsJSON);

		// include main side bar content
		String sidebar = createSidebar(bonferroni, sig);
		htmlString = htmlString.replace("$INCLUDE-SIDEBAR", sidebar);

		// include output table
		String table = createTable(message);
		htmlString = htmlString.replace("$INCLUDE-TABLE", table);
	
		return htmlString;
	}

	private static String createSidebar(double bonferroni, int sig) {
		StringBuilder sidebar = new StringBuilder();
		
		sidebar.append("<p>" + MiningState.checkedMotifsGroupSupport.size() + " subgraphs were checked."
				+ "\nOf which " + MiningState.supportedMotifsGraphSupport.size() + " are frequent and "
				+ MiningState.supportedMotifsPValues.size() + " are significant before Bonferroni-correction.");
		sidebar.append("<p>" + "Significantly enriched subgraphs after Bonferroni-correction: " + sig + " (using a Bonferonni-corrected P-value cutoff = " + bonferroni + ").</p>");
		if (GraphParameters.allPValues == 1) sidebar.append(
				"<p>" + "Showing all frequent subgraphs regardless of whether they pass the Bonferroni-adjusted significance threshold.</p>");
		else sidebar.append(
				"<p>" + "The following table shows all frequent subgraphs that pass the Bonferroni-adjusted significance threshold for enrichment:</p>");
		return sidebar.toString();
	}

	private static String createTable(String message) {
		StringBuilder table = new StringBuilder();
		
		String[] lines = message.split("\\r?\\n");
		
		table.append("<thead><tr>");
		String[] headerSplit = lines[0].split("\t");
		for (String columnName : headerSplit) {
			table.append("<td>" + columnName + "</td>");
		}
		
		table.append("</thead><tbody>");
		for (int i = 1; i < lines.length; i++) {
			table.append("<tr>");
			String[] rowSplit = lines[i].split("\t");
			for (String element : rowSplit) {
				table.append("<td>" + element + "</td>");
			}
			table.append("</tr>");
		}
		table.append("</tbody>");
				
		return table.toString();
	}

}
