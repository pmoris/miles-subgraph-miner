package com.uantwerp.algorithms.visualisation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.GraphParameters;

public class HTMLCreator {
	
	
	/**
	 * @param elementsJSON JSON String containing the vertex and edge elements
	 * @param outputTable String containing the output table as formatted for stdout or the output file
	 * @return
	 * @throws IOException
	 */
	public static String createHTML(String elementsJSON, String outputTable) throws IOException {
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

		// dagre layout
		is = HTMLCreator.class.getResourceAsStream("/dagre.min.js");
		String codeJsDagre = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-DAGRE.JS", codeJsDagre);
		
		is = HTMLCreator.class.getResourceAsStream("/cytoscape-dagre.js");
		String codeJsCytoDagre = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-CYTO-DAGRE.JS", codeJsCytoDagre);
		
		// klay layout
		is = HTMLCreator.class.getResourceAsStream("/klay.js");
		String codeJsKlay = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-KLAY.JS", codeJsKlay);
		
		is = HTMLCreator.class.getResourceAsStream("/cytoscape-klay.js");
		String codeJsCytoKlay = IOUtils.toString(is, StandardCharsets.UTF_8);
		htmlString = htmlString.replace("$INCLUDE-CYTO-KLAY.JS", codeJsCytoKlay);
				
		// insert JSON data	
		htmlString = htmlString.replace("$INCLUDE-ELEMENTS", elementsJSON);

		// include main side bar content and tweak content for frequent vs enriched subgraph mining
		String sidebar;
		if (GraphParameters.interestFile != null && GraphParameters.interestFile.length() != 0) {
			sidebar = createSidebar();
		} else {
			sidebar = createSidebarFrequent();
		}
		htmlString = htmlString.replace("$INCLUDE-SIDEBAR", sidebar);

		// include output table
		String table = createTable(outputTable);
		htmlString = htmlString.replace("$INCLUDE-TABLE", table);
	
		return htmlString;
	}

	private static String createSidebar() {
		StringBuilder sidebar = new StringBuilder();
		
		sidebar.append("<p>"
				+ MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were discovered.<br>"
				+ MiningState.supportedMotifsGraphSupport.size() + " subgraphs meet the support threshold " + GraphParameters.supportcutoff + ".<br>"
				+ MiningState.significantRawSubgraphCounter + " are significant before multiple testing correction (alpha = " + GraphParameters.pvalue + ").<br>"
				+ MiningState.significantAdjustedSubgraphCounter + " are significant after " + GraphParameters.correctionMethod + "-correction."
				+ "</p>");
		
		if (GraphParameters.allPValues == 1) sidebar.append(
				"<p>" + "Listing all supported subgraphs (no filtering on enrichment significance).</p>");
		else sidebar.append(
				"<p>" + "Listing all supported subgraphs that are significantly enriched after " + GraphParameters.correctionMethod + " correction.</p>");
		return sidebar.toString();
	}
	
	private static String createSidebarFrequent() {
		StringBuilder sidebar = new StringBuilder();
		
		sidebar.append("<p>"
				+ MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were discovered.<br>"
				+ MiningState.supportedMotifsGraphSupport.size() + "subgraphs meet the support threshold " + GraphParameters.supportcutoff + "."
				+ "</p>");
		return sidebar.toString();
	}

	private static String createTable(String message) {
		StringBuilder table = new StringBuilder();
		
		String[] lines = message.split("\\r?\\n");
		
		table.append("<thead><tr>");
		String[] headerSplit = lines[0].split("\t");
		for (String columnName : headerSplit) {
			table.append("<th>" + columnName + "</th>");
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
