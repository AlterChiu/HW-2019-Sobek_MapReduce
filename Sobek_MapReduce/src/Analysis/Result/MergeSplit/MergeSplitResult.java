package Analysis.Result.MergeSplit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;

public class MergeSplitResult {
	private Map<String, AsciiBasicControl> timeAsciiList;

	public MergeSplitResult() throws IOException {
		// to get how many result are there in original total
		this.timeAsciiList = setInitailAscii();

		String splitFolderList[] = new File(GlobalProperty.saveFolder_Split).list();
		// read all timeStep of the ascii in each split folder
		for (String ascii : timeAsciiList.keySet()) {
			List<AsciiBasicControl> temptAsciiList = new ArrayList<AsciiBasicControl>();
			for (String splitFolder : splitFolderList) {
				temptAsciiList.add(new AsciiBasicControl(GlobalProperty.saveFolder_Split + splitFolder + "\\" + ascii));
			}
			
			// output the merged asciiFile
			new AtFileWriter(getMeanVlaue(timeAsciiList.get(ascii), temptAsciiList).getAsciiFile(),
					GlobalProperty.saveFolder_Merge + ascii).textWriter("    ");
		}

	}

	private AsciiBasicControl getMeanVlaue(AsciiBasicControl origainalAscii, List<AsciiBasicControl> splitAscii) {
		String nullValue = origainalAscii.getProperty().get("noData");
		String[][] asciiGrid = origainalAscii.getAsciiGrid();

		for (int row = 0; row < asciiGrid.length; row++) {
			for (int column = 0; column < asciiGrid[0].length; column++) {
				double[] corrdinate = origainalAscii.getCoordinate(column, row);
				List<Double> temptValueList = new ArrayList<Double>();

				for (AsciiBasicControl temptAscii : splitAscii) {
					// if the value on that position isn't null put it in to value list
					String temptNullValue = temptAscii.getProperty().get("noData");
					try {
						String temptValue = temptAscii.getValue(corrdinate[0], corrdinate[1]);
						if (!temptValue.equals(temptNullValue)) {
							temptValueList.add(Double.parseDouble(temptValue));
						}
					} catch (Exception e) {
					}
				}
				// get the mean value of the tempt value list
				// if there isn't any value in that list set the value to null value
				try {
					origainalAscii.setValue(column, row, new AtCommonMath(temptValueList).getMean() + "");
				} catch (Exception e) {
					origainalAscii.setValue(column, row, nullValue);
				}
			}
		}

		// output the recreate asciiFile
		return origainalAscii;
	}

	// initialize the map of the asciiResult
	private Map<String, AsciiBasicControl> setInitailAscii() throws IOException {
		String[] asciiList = new File(GlobalProperty.saveFolder_Total_Delicate).list();
		Map<String, AsciiBasicControl> temptMap = new TreeMap<String, AsciiBasicControl>();
		for (String asciiFile : asciiList) {
			temptMap.put(asciiFile, new AsciiBasicControl(GlobalProperty.saveFolder_Total_Delicate + asciiFile));
		}
		return temptMap;
	}

}
