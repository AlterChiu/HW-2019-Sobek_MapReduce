package Ascii.Delicate.Split.Kmeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Ascii.Rough.Determine.DeterminRoughAsciiFile;
import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import main.MapReduceMainPage;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;
import usualTool.AtKmeans;

public class SplitAscii_Kmeas extends DeterminRoughAsciiFile {
	private AsciiBasicControl originalDelicateAscii;
	private AsciiBasicControl originalDelicateAsciiKn;
	private List<List<Double[]>> classified;

	public SplitAscii_Kmeas() throws IOException {
		this.originalDelicateAscii = new AsciiBasicControl(GlobalProperty.originalDelicate);
		this.originalDelicateAsciiKn = new AsciiBasicControl(GlobalProperty.originalDelicateKn);
		// reset the split folder
		MapReduceMainPage.initialize.createAfterTotalRun();
		this.classified = getKmeansClassified(GlobalProperty.K_meansInitialTime);

		// create the demFile and knFile
		for (int index = 0; index < classified.size(); index++) {
			String splitFolder = GlobalProperty.saveFolder_Split + index + "\\";
			determinUnitDelicateDem(splitFolder, index);
		}
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	protected void determinUnitDelicateDem(String splitFodler, int index) throws IOException {
		// determine the delicate asciiFile and knFile
		// the boundary(classifiedBoundary) of the delicate demFile here is the most
		// outer coordinate
		Map<String, Double> classifiedBoundary = getListStatics(this.classified.get(index));
		AsciiBasicControl delicateAscii = this.originalDelicateAscii.getIntersectAscii(classifiedBoundary);
		AsciiBasicControl delicateAsciiKn = this.originalDelicateAsciiKn.getIntersectAscii(classifiedBoundary);
		new AtFileWriter(delicateAscii.getAsciiFile(), splitFodler + GlobalProperty.saveFile_DelicateDem)
				.textWriter(" ");
		new AtFileWriter(delicateAsciiKn.getAsciiFile(), splitFodler + GlobalProperty.saveFile_DelicateDemKn)
				.textWriter(" ");

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	// <===========================================================>
	// <K-Means function>
	// <============================================================>
	// get the boundary of xyList in K-means
	private Map<String, Double> getListStatics(List<Double[]> staticsList) throws IOException {
		Map<String, Double> outMap = new TreeMap<String, Double>();
		List<Double> xList = new ArrayList<Double>();
		List<Double> yList = new ArrayList<Double>();
		staticsList.forEach(coordinate -> {
			xList.add(coordinate[0]);
			yList.add(coordinate[1]);
		});

		AtCommonMath xListStatics = new AtCommonMath(xList);
		AtCommonMath yListStatics = new AtCommonMath(yList);
		double delicateCellSize = Double
				.parseDouble(new AsciiBasicControl(GlobalProperty.originalDelicate).getProperty().get("cellSize"));

		double minX = xListStatics.getMin() - delicateCellSize * 0.5;
		double maxX = xListStatics.getMax() + delicateCellSize * 0.5;
		double minY = yListStatics.getMin() - delicateCellSize * 0.5;
		double maxY = yListStatics.getMax() + delicateCellSize * 0.5;

		// make it match the grid of rough asciiDem
		AsciiBasicControl roughAscii = new AsciiBasicControl(GlobalProperty.originalRough);
		double roughCellSize = Double.parseDouble(roughAscii.getProperty().get("cellSize"));

		double[] bottomCoordinate = roughAscii.getClosestCoordinate(minX, minY);
		double[] topCoordinate = roughAscii.getClosestCoordinate(maxX, maxY);

		minX = bottomCoordinate[0] - 0.5 * roughCellSize - delicateCellSize;
		minY = bottomCoordinate[1] - 0.5 * roughCellSize - delicateCellSize;
		maxX = topCoordinate[0] + 0.5 * roughCellSize + delicateCellSize;
		maxY = topCoordinate[1] + 0.5 * roughCellSize + delicateCellSize;

		outMap.put("minX", minX);
		outMap.put("maxX", maxX);
		outMap.put("minY", minY);
		outMap.put("maxY", maxY);
		return outMap;
	}

	// get the kmeans classified
	protected List<List<Double[]>> getKmeansClassified(int floodInitialTimes) throws IOException {
		AsciiBasicControl ascii = new AsciiBasicControl(GlobalProperty.saveFile_Analysis_InitailFlood);
		List<Double[]> analysisData = new ArrayList<Double[]>();

		// read the ascii content which value is under limit
		String[][] content = ascii.getAsciiGrid();
		for (int row = 0; row < content.length; row++) {
			for (int column = 0; column < content[0].length; column++) {
				double value = Double.parseDouble(ascii.getValue(column, row));
				if (value <= floodInitialTimes && value >= 0) {
					double[] coordinate = ascii.getCoordinate(column, row);
					analysisData.add(new Double[] { coordinate[0], coordinate[1] });
				}
			}
		}
		AtKmeans kmeans = new AtKmeans(analysisData, GlobalProperty.splitSize);

		return kmeans.getClassifier();
	}

	public void outputClassified(String saveAdd) throws IOException {
		List<String[]> outList = new ArrayList<String[]>();
		outList.add(new String[] { "x", "y", "index" });

		for (int index = 0; index < this.classified.size(); index++) {
			for (Double[] coordinate : this.classified.get(index)) {
				outList.add(new String[] { coordinate[0] + "", coordinate[1] + "", index + "" });
			}
		}
		new AtFileWriter(outList.parallelStream().toArray(String[][]::new), saveAdd).csvWriter();
	}

}
