package com.uantwerp.algorithms.visualisation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.utilities.OutputUtility;

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
		
		// insert directed/undirected arrow type
		if (GraphParameters.undirected == 0)
			htmlString = htmlString.replace("$INCLUDE-DIRECTED", "\"mid-target-arrow-shape\": \"triangle-backcurve\",");
		else
			htmlString = htmlString.replace("$INCLUDE-DIRECTED", "");

		// include main side bar content and tweak content for frequent vs enriched subgraph mining
		String sidebar;
		if (GraphParameters.interestFile != null && GraphParameters.interestFile.length() != 0) {
			sidebar = createSidebar();
		} else {
			sidebar = createSidebarFrequent();
		}
		htmlString = htmlString.replace("$INCLUDE-SIDEBAR", sidebar);

		// include output table
		String table = createHTMLTable(outputTable);
		htmlString = htmlString.replace("$INCLUDE-TABLE", table);
	
		return htmlString;
	}

	private static String createSidebar() {
		StringBuilder sidebar = new StringBuilder();
		
		sidebar.append("<p>"
				+ MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were discovered.<br>"
				+ MiningState.supportedMotifsGraphSupport.size() + " subgraphs meet the support threshold " + GraphParameters.supportcutoff + ".<br>"
				+ MiningState.significantRawSubgraphCounter + " are significant before multiple testing correction (alpha = " + GraphParameters.pvalue + ").<br>"
				+ MiningState.significantAdjustedSubgraphCounter + " are significant after " + OutputUtility.correctionMethodPrettyPrint() + " correction."
				+ "</p>");
		
		if (GraphParameters.allPValues == 1) sidebar.append(
				"<p>" + "Listing all supported subgraphs (no filtering on enrichment significance).</p>");
		else sidebar.append(
				"<p>" + "Listing all supported subgraphs that are significantly enriched after " + OutputUtility.correctionMethodPrettyPrint() + " correction.</p>");
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

	private static String createHTMLTable(String message) {
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
			for (int j = 0; j < rowSplit.length; j++) {
				// round p-values for enriched columns (not required for frequent subgraph mining)
				String element;
				double pvalue;
				if (j >= 3) {
					pvalue = Math.round(Double.parseDouble(rowSplit[j]) * 100000d) / 100000d;
					element = Double.toString(pvalue);
				}
				else {
					element = rowSplit[j];
				}
				table.append("<td>" + element + "</td>");
			}
			table.append("</tr>");
		}
		table.append("</tbody>");
				
		return table.toString();
	}

}
