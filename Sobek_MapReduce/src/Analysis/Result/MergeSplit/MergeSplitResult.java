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
	}

	// <==================================================>
	// < Get mean value>
	// <==================================================>
	/*
	 * 
	 * 
	 * @throws IOException
	 */
	// <============================================================>
	public void getMeanValue() throws IOException {
		String splitFolderList[] = new File(GlobalProperty.saveFolder_Split).list();

		// read all timeStep of the ascii in each split folder
		for (String ascii : timeAsciiList.keySet()) {
			List<AsciiBasicControl> temptAsciiList = new ArrayList<AsciiBasicControl>();
			for (String splitFolder : splitFolderList) {
				temptAsciiList.add(new AsciiBasicControl(GlobalProperty.saveFolder_Split + splitFolder + "\\" + ascii));
			}

			// output the merged asciiFile
			new AtFileWriter(getMeanVlaueFunction(timeAsciiList.get(ascii), temptAsciiList).getAsciiFile(),
					GlobalProperty.saveFolder_Merge + ascii).textWriter("    ");
		}
	}

	private AsciiBasicControl getMeanVlaueFunction(AsciiBasicControl origainalAscii,
			List<AsciiBasicControl> splitAscii) {
		String nullValue = origainalAscii.getProperty().get("noData");
		String[][] asciiGrid = origainalAscii.getAsciiGrid();

		for (int row = 0; row < asciiGrid.length; row++) {
			for (int column = 0; column < asciiGrid[0].length; column++) {
				double[] corrdinate = origainalAscii.getCoordinate(column, row);
				List<Double> temptValueList = new ArrayList<Double>();

				if (!asciiGrid[row][column].equals(nullValue)) {
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
		}

		// output the recreate asciiFile
		return origainalAscii;
	}
	// <===================================================================>

	// <====================================================================>
	// < Get smooth value>
	// <====================================================================>
	/**
	 * @throws IOException
	 * 
	 * 
	 * 
	 */
	// <====================================================================>
	public void getSmoothValue() throws IOException {
		String splitFolderList[] = new File(GlobalProperty.saveFolder_Split).list();

		// read all timeStep of the ascii in each split folder
		for (String ascii : timeAsciiList.keySet()) {
			List<AsciiBasicControl> temptAsciiList = new ArrayList<AsciiBasicControl>();
			for (String splitFolder : splitFolderList) {
				temptAsciiList.add(new AsciiBasicControl(GlobalProperty.saveFolder_Split + splitFolder + "\\" + ascii));
			}

			// output the merged asciiFile
			new AtFileWriter(getSmoothValueFunction(timeAsciiList.get(ascii), temptAsciiList).getAsciiFile(),
					GlobalProperty.saveFolder_Merge + ascii).textWriter("    ");
		}
	}

	private AsciiBasicControl getSmoothValueFunction(AsciiBasicControl origainalAscii,
			List<AsciiBasicControl> splitAscii) {
		String nullValue = origainalAscii.getProperty().get("noData");
		String[][] asciiGrid = origainalAscii.getAsciiGrid();

		for (int row = 0; row < asciiGrid.length; row++) {
			for (int column = 0; column < asciiGrid[0].length; column++) {
				double[] corrdinate = origainalAscii.getCoordinate(column, row);
				List<Double> temptValueList = new ArrayList<Double>();
				List<Double> temptRateList = new ArrayList<Double>();

				if (!asciiGrid[row][column].equals(nullValue)) {
					for (AsciiBasicControl temptAscii : splitAscii) {
						// if the value on that position isn't null put it in to value list
						String temptNullValue = temptAscii.getProperty().get("noData");
						try {
							String temptValue = temptAscii.getValue(corrdinate[0], corrdinate[1]);
							if (!temptValue.equals(temptNullValue)) {
								temptValueList.add(Double.parseDouble(temptValue));
								temptRateList.add(getBoundaryRate(temptAscii, corrdinate[0], corrdinate[1]));
							}
						} catch (Exception e) {
						}
					}

					// get the smooth value of the tempt value list
					// temptLvalue * rate / (rateSum)
					// if there isn't any value in that list set the value to null value
					try {
						if (temptValueList.size() > 1) {
							List<Double> outValueList = new ArrayList<Double>();
							double sumRate = new AtCommonMath(temptRateList).getSum();
							for (int index = 0; index < temptValueList.size(); index++) {
								outValueList.add(temptValueList.get(index) * temptRateList.get(index) / sumRate);
							}
							origainalAscii.setValue(column, row, new AtCommonMath(outValueList).getSum() + "");
						} else if (temptValueList.size() == 1) {
							origainalAscii.setValue(column, row, temptValueList.get(0) + "");
						} else {
							origainalAscii.setValue(column, row, nullValue);
						}
					} catch (Exception e) {
						origainalAscii.setValue(column, row, nullValue);
					}
				}
			}
		}
		// output the recreate asciiFile
		return origainalAscii;
	}

	// get the rate of the position in this asciiDem
	// it calculate from the center of the grid
	// if target position is on the boundary
	private double getCenterRate(AsciiBasicControl ascii, double x, double y) {
		Map<String, String> property = ascii.getProperty();
		double maxX = Double.parseDouble(property.get("topX"));
		double maxY = Double.parseDouble(property.get("topY"));
		double minX = Double.parseDouble(property.get("bottomX"));
		double minY = Double.parseDouble(property.get("bottomY"));

		double centerX = (maxX + minX) / 2;
		double centerY = (maxY + minY) / 2;
		double maxLength = Math.sqrt(Math.pow((maxX - minX), 2) + Math.pow((maxY - minY), 2))/2;

		double length = Math.sqrt(Math.pow(Math.abs(x - centerX), 2) + Math.pow(Math.abs(y - centerY), 2));
		return (maxLength-length);
	}

	public double getBoundaryRate(AsciiBasicControl ascii, double x, double y) {
		Map<String, String> property = ascii.getProperty();
		double maxX = Double.parseDouble(property.get("topX"));
		double maxY = Double.parseDouble(property.get("topY"));
		double minX = Double.parseDouble(property.get("bottomX"));
		double minY = Double.parseDouble(property.get("bottomY"));

		List<Double> temptList = new ArrayList<Double>();
		temptList.add(maxX-x);
		temptList.add(x - minX);
		temptList.add(maxY - y);
		temptList.add(y - minY);

		return new AtCommonMath(temptList).getMin();
	}
	// <====================================================================>

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
