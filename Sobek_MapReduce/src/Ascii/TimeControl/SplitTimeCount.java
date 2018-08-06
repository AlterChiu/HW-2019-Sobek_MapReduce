package Ascii.TimeControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntercept;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiSplit;
import main.MapReduceMainPage;
import usualTool.AtCommonMath;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.AtKmeans;
import usualTool.FileFunction;
import GlobalProperty.GlobalProperty;

public class SplitTimeCount {

	private AsciiBasicControl originalDelicateAscii;
	private AsciiBasicControl originalDelicateAsciiKn;
	private AsciiBasicControl originalRoughAscii;
	private AsciiBasicControl originalRoughAsciiKn;
	private List<List<Double[]>> kmeansClassified;
	private JsonObject overviewPorperty;
	private double restTimeCofficience = 0.6;

	//
	// <=====================>
	// < setting the split way >
	// <=====================>
	// <=======================================================>
	public SplitTimeCount() throws IOException {
		this.originalDelicateAscii = new AsciiBasicControl(GlobalProperty.originalDelicate);
		this.originalDelicateAsciiKn = new AsciiBasicControl(GlobalProperty.originalDelicateKn);
		this.originalRoughAscii = new AsciiBasicControl(GlobalProperty.originalRough);
		this.originalRoughAsciiKn = new AsciiBasicControl(GlobalProperty.originalRoughKn);

		this.overviewPorperty = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
	}
	// <=======================================================>

	// main operation in this class
	// <=======================================================>
	public void runSplitDem() throws IOException {
		this.kmeansClassified = getKmeansClassified(GlobalProperty.splitSize);
		double restTime = GlobalProperty.totalAllowTime
				- this.overviewPorperty.get(GlobalProperty.overviewProperty_delicateTotal).getAsDouble()
						* restTimeCofficience;

		for (int index = 0; index < this.kmeansClassified.size(); index++) {
			// check the simulation time through Sobek;
			Boolean simulationTimeCheck = true;

			while (simulationTimeCheck) {
				// determine the unit dem
				determineUnitDem(index, restTime);

				// run sobek model
				SobekDem sobekDem = new SobekDem();
				sobekDem.addNewDem(GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_DelicateDem,
						GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_DelicateDemKn);
				sobekDem.addNewDem(GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_RoughDem,
						GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_RoughDemKn);
				sobekDem.start();

				Runtimes sobekRuntimes = new Runtimes();
				if (sobekRuntimes.getSimulateTime() > GlobalProperty.totalAllowTime) {
					this.restTimeCofficience = overLimitSimulation(sobekRuntimes.getSimulateTime());
				} else {
					simulationTimeCheck = false;
					// outPut result to the overview property file
					outPutResult(index);
					restTime = GlobalProperty.totalAllowTime
							- this.overviewPorperty.get(GlobalProperty.overviewProperty_delicateTotal).getAsDouble()
									* 0.6;
				}
			}
		}
	}
	// <==========================================================>

	// recreate the roughDem in unitDem, cause simulation time is over limit
	private double overLimitSimulation(double simulationTime) throws IOException {
		if (this.restTimeCofficience >= 1) {
			System.out.println("simulation time of the delicate dem is over limit");
			System.out.println("resetting the split number of the unitDem");
			
			// resetting the split number of the unitDem
			// and also set the variable to default
			GlobalProperty.splitSize++;
			MapReduceMainPage.initialize.createAfterTotalRun();
			this.kmeansClassified = getKmeansClassified(GlobalProperty.splitSize);
			this.restTimeCofficience = 0.6;
		} else {
			this.restTimeCofficience = this.restTimeCofficience * 1.1;
		}
		return this.restTimeCofficience;
	}

	// output the boundary of the unitDem
	private void outPutResult(int index) throws IOException {
		JsonObject outJsonObject = new JsonObject();

		Map<String, String> delicateProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_DelicateDem).getProperty();
		JsonObject delicateJson = getBoundaryJson(delicateProperty);

		Map<String, String> roughProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_RoughDem).getProperty();
		JsonObject roughJson = getBoundaryJson(roughProperty);

		outJsonObject.add(GlobalProperty.overviewProperty_mergeDelicateBoundary, delicateJson);
		outJsonObject.add(GlobalProperty.overviewProperty_mergeRoughBoundary, roughJson);
		this.overviewPorperty.add(GlobalProperty.overviewProperty_merge + "_" + index, outJsonObject);
		new AtFileWriter(this.overviewPorperty, GlobalProperty.overViewPropertyFile).textWriter("");
	}

	// use while get the boundary of unitDem
	private JsonObject getBoundaryJson(Map<String, String> asciiProperty) {
		JsonObject json = new JsonObject();
		double cellSize = Double.parseDouble(asciiProperty.get("cellSize"));
		json.addProperty("minX", Double.parseDouble(asciiProperty.get("bottomX")) - 0.5 * cellSize);
		json.addProperty("maxX", Double.parseDouble(asciiProperty.get("topX")) + 0.5 * cellSize);
		json.addProperty("minY", Double.parseDouble(asciiProperty.get("bottomY")) - 0.5 * cellSize);
		json.addProperty("maxY", Double.parseDouble(asciiProperty.get("topY")) + 0.5 * cellSize);
		return json;
	}

	// determine the unitDem , roughDem is defined by the delicateDem
	private void determineUnitDem(int index, double restTime) throws IOException {
		// determine the delicate ascii and kn file
		double delicateCellSize = Double.parseDouble(this.originalDelicateAscii.getProperty().get("cellSize"));
		Map<String, Double> classifiedBoundary = getListStatics(this.kmeansClassified.get(index));
		String[][] delicateAscii = new AsciiIntercept(this.originalDelicateAscii).getIntercept(
				classifiedBoundary.get("minX") - delicateCellSize, classifiedBoundary.get("maxX") + delicateCellSize,
				classifiedBoundary.get("minY") - delicateCellSize, classifiedBoundary.get("maxY") + delicateCellSize);
		String[][] delicateAsciiKn = new AsciiIntercept(this.originalDelicateAsciiKn).getIntercept(
				classifiedBoundary.get("minX") - delicateCellSize, classifiedBoundary.get("maxX") + delicateCellSize,
				classifiedBoundary.get("minY") - delicateCellSize, classifiedBoundary.get("maxY") + delicateCellSize);
		new AtFileWriter(delicateAscii, GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_DelicateDem)
				.textWriter("    ");
		new AtFileWriter(delicateAsciiKn,
				GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_DelicateDemKn).textWriter("    ");

		// determine the rough ascii and kn file
		double roughCellSize = Double.parseDouble(this.originalRoughAscii.getProperty().get("cellSize"));
		Map<String, Double> roughBoundary = getBufferBoundary(new AsciiBasicControl(delicateAscii), restTime);
		String[][] roughAscii = new AsciiIntercept(this.originalRoughAscii).getIntercept(
				roughBoundary.get("minX") - roughCellSize, roughBoundary.get("maxX") + roughCellSize,
				roughBoundary.get("minY") - roughCellSize, roughBoundary.get("maxY") + roughCellSize);
		String[][] roughAsciiKn = new AsciiIntercept(this.originalRoughAsciiKn).getIntercept(
				roughBoundary.get("minX") - roughCellSize, roughBoundary.get("maxX") + roughCellSize,
				roughBoundary.get("minY") - roughCellSize, roughBoundary.get("maxY") + roughCellSize);
		new AtFileWriter(roughAscii, GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_RoughDem)
				.textWriter("    ");
		new AtFileWriter(roughAsciiKn, GlobalProperty.saveFolder_Merge + index + GlobalProperty.saveFile_RoughDemKn)
				.textWriter("    ");
	}

	private Map<String, Double> getListStatics(List<Double[]> staticsList) {
		Map<String, Double> outMap = new TreeMap<String, Double>();
		List<Double> xList = new ArrayList<Double>();
		List<Double> yList = new ArrayList<Double>();
		staticsList.forEach(coordinate -> {
			xList.add(coordinate[0]);
			yList.add(coordinate[1]);
		});

		AtCommonMath xListStatics = new AtCommonMath(xList);
		AtCommonMath yListStatics = new AtCommonMath(yList);
		outMap.put("minX", xListStatics.getMin());
		outMap.put("maxX", xListStatics.getMax());
		outMap.put("minY", yListStatics.getMin());
		outMap.put("maxY", yListStatics.getMax());
		return outMap;
	}

	// get the kmeans classified
	private List<List<Double[]>> getKmeansClassified(int floodInitialTimes) throws IOException {
		AsciiBasicControl ascii = new AsciiBasicControl(GlobalProperty.analysisDem_InitailFloodTimes);
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

		// revise the output boundary, to center the original total ascii
		if (boundaryMinX < originalMinX) {
			boundaryMinX = originalMinX;
			boundaryMaxX = boundaryMaxX + originalMinX - boundaryMinX;
		}
		if (boundaryMinY < originalMinY) {
			boundaryMinY = originalMinY;
			boundaryMaxY = boundaryMaxY + originalMinY - boundaryMinY;
		}
		if (boundaryMaxX > originalMaxX) {
			boundaryMaxX = originalMaxX;
			boundaryMinX = boundaryMinX + originalMaxX - boundaryMinX;
		}
		if (boundaryMaxY > originalMaxY) {
			boundaryMaxY = originalMaxY;
			boundaryMinY = boundaryMinY + originalMaxY - boundaryMaxY;
		}

		Map<String, Double> outBoundary = new TreeMap<String, Double>();
		outBoundary.put("minX", boundaryMinX);
		outBoundary.put("minY", boundaryMinY);
		outBoundary.put("maxX", boundaryMaxX);
		outBoundary.put("maxY", boundaryMaxY);

		return outBoundary;
	}
}
