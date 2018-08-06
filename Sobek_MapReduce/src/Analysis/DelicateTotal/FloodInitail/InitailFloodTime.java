package Analysis.DelicateTotal.FloodInitail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import asciiFunction.AsciiBasicControl;
import usualTool.AtFileWriter;

public class InitailFloodTime {
	private AsciiBasicControl outAscii;
	private List<AsciiBasicControl> analysisAscii = new ArrayList<AsciiBasicControl>();

	//<================>
	//<construct>
	//<================>
	public InitailFloodTime(String selectedFolder) throws IOException {
		this.outAscii = new AsciiBasicControl(selectedFolder + "dm1d0000.asc");
		// get the list of dm1d
		for (int index = 1; index < index + 1; index++) {
			try {
				String fileName = "dm1d" + String.format("%04d", index) + ".asc";
				if (new File(selectedFolder + fileName).exists()) {
					analysisAscii.add(new AsciiBasicControl(selectedFolder + fileName));
				} else {
					break;
				}
			} catch (Exception e) {
				break;
			}
		}
		setTheInitailTime();
	}
	//<=======================================>

	//<=======================================>
	//<setting the initial time from first hour to the last one>
	//<and the times set 0 to the area never flood>
	private void setTheInitailTime() {
		int totalRow = Integer.parseInt(this.outAscii.getProperty().get("row"));
		int totalColumn = Integer.parseInt(this.outAscii.getProperty().get("column"));
		String nullValue = this.outAscii.getProperty().get("noData");

		for (int row = 0; row < totalRow; row++) {
			for (int column = 0; column < totalColumn; column++) {
				if (!this.outAscii.getValue(column, row).equals(nullValue)) {

					// check the whole time series to find out what the first times is flood
					for (int index = 0; index < this.analysisAscii.size(); index++) {
						if (Double.parseDouble(this.analysisAscii.get(index).getValue(column, row)) > 0) {
							this.outAscii.setValue(column, row, index + 1 + "");
							break;
						}
						if(index == this.analysisAscii.size()-1) {
							this.outAscii.setValue(column, row, "0");
						}
					}
				}
			}
		}
	}

	public void outPutFile(String saveAdd) throws IOException {
		new AtFileWriter(this.outAscii.getAsciiFile(), saveAdd).textWriter("    ");
	}

	public String[][] getTheInitailFloodTime() {
		return this.outAscii.getAsciiFile();
	}

}
