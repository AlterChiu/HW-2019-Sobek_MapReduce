package ExtraFunction.DEM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;

public class NodeChecker extends SobekDem {
	public NodeChecker() throws IOException {
		// TODO Auto-generated method stub
		this.clearNetWork();
	}

	public void getDifference(String demFile, String saveFile) throws IOException {
		AsciiBasicControl ascii = new AsciiBasicControl(demFile);
		String[][] nodeContent = new AtFileReader(GlobalProperty.saveFile_SobekNodes).getStr();
		List<String[]> outList = new ArrayList<String[]>();
		outList.add(new String[] { "nodeID", "demLevel", "x", "y", "topLevel", "bottomLevel" });

		// make the street level of nodes to the upper demLevel
		String nullValue = ascii.getProperty().get("noData");
		// List<String[]> temptList = new ArrayList<String[]>();

		// check for the nodes.DAT
		for (int line = 0; line < nodeContent.length; line++) {
			try {
				if (nodeContent[line][4].equals("3")) {
					String nodeName = nodeContent[line][2].split("\'")[1];
					Double[] coordinate = nodeList.get(nodeName);
					String value = ascii.getValue(coordinate[0], coordinate[1]);

					// check for the demFile is contain this node or not
					if (!value.equals(nullValue)) {

						// check for the streetLevel of mainHole, it must higher than bottomLevel
						double topLevel = Double.parseDouble(nodeContent[line][12]);
						double bottomLevel = Double.parseDouble(nodeContent[line][10]);
						if (Double.parseDouble(value) < bottomLevel) {
							outList.add(new String[] { nodeName, value, coordinate[0] + "", coordinate[1] + "",
									topLevel + "", bottomLevel + "" });
						}
					}
				}
			} catch (Exception e) {
			}
		}
		new AtFileWriter(outList.parallelStream().toArray(String[][]::new), saveFile).csvWriter();
	}

}
