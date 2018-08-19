package Ascii.Split.Kmeans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntercept;
import main.MapReduceMainPage;
import usualTool.AtCommonMath;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.AtKmeans;

public class SplitAscii_Kmeas {
	private AsciiBasicControl originalDelicateAscii;
	private AsciiBasicControl originalDelicateAsciiKn;
	private AsciiBasicControl originalRoughAscii;
	private AsciiBasicControl originalRoughAsciiKn;
	private List<List<Double[]>> classified;
	private JsonObject overviewPorperty;
	public double restTimeCoefficient = 1.2;

	public SplitAscii_Kmeas() throws IOException {
		this.originalDelicateAscii = new AsciiBasicControl(GlobalProperty.originalDelicate);
		this.originalDelicateAsciiKn = new AsciiBasicControl(GlobalProperty.originalDelicateKn);
		this.originalRoughAscii = new AsciiBasicControl(GlobalProperty.originalRough);
		this.originalRoughAsciiKn = new AsciiBasicControl(GlobalProperty.originalRoughKn);

		this.overviewPorperty = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();

		// reset the split folder
		MapReduceMainPage.initialize.createAfterTotalRun();
		this.classified = getKmeansClassified(GlobalProperty.K_meansInitialTime);

		// create the demFile and knFile
		for (int index = 0; index < classified.size(); index++) {
			determinUnitDelicateDem(index);
			determineRoughUnitDem(index, this.restTimeCoefficient);
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
	private void determinUnitDelicateDem(int index) throws IOException {
		// determine the delicate asciiFile and knFile
		// the boundary(classifiedBoundary) of the delicate demFile here is the most
		// outer coordinate
		Map<String, Double> classifiedBoundary = getListStatics(this.classified.get(index));
		String[][] delicateAscii = new AsciiIntercept(this.originalDelicateAscii).getIntercept(
				classifiedBoundary.get("minX"), classifiedBoundary.get("maxX"), classifiedBoundary.get("minY"),
				classifiedBoundary.get("maxY"));
		String[][] delicateAsciiKn = new AsciiIntercept(this.originalDelicateAsciiKn).getIntercept(
				classifiedBoundary.get("minX"), classifiedBoundary.get("maxX"), classifiedBoundary.get("minY"),
				classifiedBoundary.get("maxY"));
		new AtFileWriter(delicateAscii, GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem)
				.textWriter("    ");
		new AtFileWriter(delicateAsciiKn,
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDemKn).textWriter("    ");

	}

	// <==================================================================>
	// < Create DemFile Boundary>
	// <==================================================================>
	// determine the unitDem , roughDem is defined by the delicateDem
	public void determineRoughUnitDem(int index, double restTimeCoefficient) throws IOException {
		String splitFolder = GlobalProperty.saveFolder_Split + index;
		double restTime = GlobalProperty.totalAllowTime
				- this.overviewPorperty.get(GlobalProperty.overviewProperty_delicateTotal).getAsDouble()
						* restTimeCoefficient / GlobalProperty.splitSize;

		// determine the rough asciiFile and knFile
		// the boundary of the rough demFile here is calculate by the rest time
		// there is no necessary to point the boundary exact
		Map<String, Double> roughBoundary = getBufferBoundary(
				new AsciiBasicControl(splitFolder + GlobalProperty.saveFile_DelicateDem), restTime);
		String[][] roughAscii = new AsciiIntercept(this.originalRoughAscii).getIntercept(roughBoundary.get("minX"),
				roughBoundary.get("maxX"), roughBoundary.get("minY"), roughBoundary.get("maxY"));
		String[][] roughAsciiKn = new AsciiIntercept(this.originalRoughAsciiKn).getIntercept(roughBoundary.get("minX"),
				roughBoundary.get("maxX"), roughBoundary.get("minY"), roughBoundary.get("maxY"));

		roughAscii = setOverlappingNull(splitFolder + GlobalProperty.saveFile_DelicateDem, roughAscii);
		roughAsciiKn = setOverlappingNull(splitFolder + GlobalProperty.saveFile_DelicateDem, roughAsciiKn);

		new AtFileWriter(roughAscii, splitFolder + GlobalProperty.saveFile_RoughDem).textWriter("    ");
		new AtFileWriter(roughAsciiKn, splitFolder + GlobalProperty.saveFile_RoughDemKn).textWriter("    ");
	}

	private String[][] setOverlappingNull(String delicateAsciiFile, String[][] roughAsciiFile) throws IOException {
		AsciiBasicControl delicateAscii = new AsciiBasicControl(delicateAsciiFile);
		AsciiBasicControl roughAscii = new AsciiBasicControl(roughAsciiFile);
		String roughNullValue = roughAscii.getProperty().get("noData");

		String[][] asciiContent = delicateAscii.getAsciiGrid();
		for (int row = 1; row < asciiContent.length - 1; row++) {
			for (int column = 1; column < asciiContent[0].length - 1; column++) {
				double[] coordinate = delicateAscii.getCoordinate(column, row);
				try {
					roughAscii.setValue(coordinate[0], coordinate[1], roughNullValue);
				} catch (Exception e) {
				}
			}
		}
		return roughAscii.getAsciiFile();
	}

	// use for determine the rough ascii boundary
	private Map<String, Double> getBufferBoundary(AsciiBasicControl splitAscii, double restTime) throws IOException {
		Map<String, String> originalAsciiProperty = this.originalRoughAscii.getProperty();

		double originalMaxX = Double.parseDouble(originalAsciiProperty.get("topX"));
		double originalMaxY = Double.parseDouble(originalAsciiProperty.get("topY"));
		double originalMinX = Double.parseDouble(originalAsciiProperty.get("bottomX"));
		double originalMinY = Double.parseDouble(originalAsciiProperty.get("bottomY"));
		double originalWidth = originalMaxX - originalMinX;
		double originalHeight = originalMaxY - originalMinY;

		// get the ratio of spend time between delicate one and rough one
		// rough / delicate (spend time)
		double ratio = restTime / overviewPorperty.get(GlobalProperty.overviewProperty_roughTotal).getAsDouble();

		// get the original boundary
		Map<String, String> asciiProperty = splitAscii.getProperty();
		double splitMinX = Double.parseDouble(asciiProperty.get("bottomX"));
		double splitMaxX = Double.parseDouble(asciiProperty.get("topX"));
		double splitMaxY = Double.parseDouble(asciiProperty.get("topY"));
		double splitMinY = Double.parseDouble(asciiProperty.get("bottomY"));

		// get the length should be extend
		double bufferWidth = originalWidth * ratio;
		double bufferHeight = originalHeight * ratio;

		// get the output boundary
		double boundaryMinX = splitMinX - bufferWidth * 0.5;
		double boundaryMaxX = splitMaxX + bufferWidth * 0.5;
		double boundaryMinY = splitMinY - bufferHeight * 0.5;
		double boundaryMaxY = splitMaxY + bufferHeight * 0.5;

		Map<String, Double> outBoundary = new TreeMap<String, Double>();
		outBoundary.put("minX", boundaryMinX);
		outBoundary.put("minY", boundaryMinY);
		outBoundary.put("maxX", boundaryMaxX);
		outBoundary.put("maxY", boundaryMaxY);

		return outBoundary;
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

		double minX = xListStatics.getMin() - delicateCellSize;
		double maxX = xListStatics.getMax() + delicateCellSize;
		double minY = yListStatics.getMin() - delicateCellSize;
		double maxY = yListStatics.getMax() + delicateCellSize;

		// make it match the grid of rough asciiDem
		AsciiBasicControl roughAscii = new AsciiBasicControl(GlobalProperty.originalRough);

		double[] bottomCoordinate = roughAscii.getClosestCoordinate(minX, minY);
		double[] topCoordinate = roughAscii.getClosestCoordinate(maxX, maxY);

		outMap.put("minX", bottomCoordinate[0]);
		outMap.put("maxX", topCoordinate[0]);
		outMap.put("minY", bottomCoordinate[1]);
		outMap.put("maxY", topCoordinate[1]);
		return outMap;
	}

	// get the kmeans classified
	private List<List<Double[]>> getKmeansClassified(int floodInitialTimes) throws IOException {
		AsciiBasicControl ascii = new AsciiBasicControl(GlobalProperty.saveFile_Analysis_InitailFlood);
		List<Double[]> analysisData = new ArrayList<Double[]>();

		// read the ascii content which value is under limit
		String[][] content = ascii.getAsciiGrid();
		for (int row = 0; row < content.length; row++) {
			for (int column = 0; column < content[0].length; column++) {
				double value = Double.parseDouble(ascii.getValue(column, row));
				if (value <= floodInitialTimes && value > 0) {
					double[] coordinate = ascii.getCoordinate(column, row);
					analysisData.add(new Double[] { coordinate[0], coordinate[1] });
				}
			}
		}
		AtKmeans kmeans = new AtKmeans(analysisData, GlobalProperty.splitSize);
		return kmeans.getClassifier();
	}
	// <=================================================================>

}
