package Ascii.TimeControl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntercept;

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
	private List<List<Double[]>> classified;
	private JsonObject overviewPorperty;
	private double restTimeCoefficient = 1.2;

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
		Boolean reStart = false;

		// classified function
		// < K-means Method >
		this.classified = getKmeansClassified(GlobalProperty.K_meansInitialTime);
		double restTime = GlobalProperty.totalAllowTime
				- this.overviewPorperty.get(GlobalProperty.overviewProperty_delicateTotal).getAsDouble()
						* restTimeCoefficient / GlobalProperty.splitSize;

		// loop for the classified
		classifiedLoop: for (int index = 0; index < GlobalProperty.splitSize; index++) {
			String classifiedSaveFolder = GlobalProperty.saveFolder_Split + index + "\\";

			// check the simulation time through Sobek;
			Boolean simulationTimeCheck = true;
			while (simulationTimeCheck) {
				// determine the unitDem
				determineUnitDem(index, restTime);

				// run sobek model
				SobekDem sobekDem = new SobekDem();
				sobekDem.addDelicateDem(classifiedSaveFolder + GlobalProperty.saveFile_DelicateDem,
						classifiedSaveFolder + GlobalProperty.saveFile_DelicateDemKn);
				sobekDem.addRoughDem(classifiedSaveFolder + GlobalProperty.saveFile_RoughDem,
						classifiedSaveFolder + GlobalProperty.saveFile_RoughDemKn);
				sobekDem.start();

				// check for the simulation time is over limit or not
				Runtimes sobekRuntimes = new Runtimes();
				System.out.println("simulation time unitDem_" + index + " : " + sobekRuntimes.getSimulateTime());
				if (sobekRuntimes.getSimulateTime() > GlobalProperty.totalAllowTime) {
					// resetting some variable
					// if the restTime coefficient is over 1, restart the whole process
					reStart = reStartJudgement(sobekRuntimes.getSimulateTime());
					if (reStart) {
						break classifiedLoop;
					}
				} else {
					simulationTimeCheck = false;
					// outPut result to the overview property file
					outPutResult(index, sobekRuntimes.getSimulateTime());
				}
			}

			// copy the result of delicate demFile to the temptSave folder
			moveRsult(classifiedSaveFolder);
		}
		// if need to reStart and run this function again
		if (reStart) {
			runSplitDem();
		} else {
			System.out.println("split unitDem timeControl done");
		}
	}
	// <==========================================================>

	private void moveRsult(String classfiedFolder) {
		FileFunction ff = new FileFunction();
		String[] outPutList = new File(GlobalProperty.sobekResultFolder).list();
		for (String result : outPutList) {
			if (result.contains(".asc")) {
				ff.moveFile(GlobalProperty.sobekResultFolder + result, classfiedFolder + result);
			}
		}
	}

	// recreate the roughDem in unitDem, cause simulation time is over limit
	private Boolean reStartJudgement(double simulationTime) throws IOException {
		Boolean restart = false;
		if (this.restTimeCoefficient >= 1.2) {
			System.out.println("simulation time of the delicate dem is over limit");
			System.out.println("resetting the split number of the unitDem");

			// resetting the split number of the unitDem
			// and also set the variable to default
			GlobalProperty.splitSize = GlobalProperty.splitSize + 1;
			MapReduceMainPage.initialize.createAfterTotalRun();
			this.classified = getKmeansClassified(GlobalProperty.splitSize);
			this.restTimeCoefficient = 1.2;
			restart = true;
		} else {
			this.restTimeCoefficient = this.restTimeCoefficient * 1.1;
		}
		return restart;
	}

	// output the boundary of the unitDem
	private void outPutResult(int index, double simulationTime) throws IOException {
		JsonObject outJsonObject = new JsonObject();

		// delicate
		Map<String, String> delicateProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem).getProperty();
		JsonObject delicateJson = getBoundaryJson(delicateProperty);

		// rough
		Map<String, String> roughProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_RoughDem).getProperty();
		JsonObject roughJson = getBoundaryJson(roughProperty);

		// add the property to the overview jsonFile
		// spend time of the unit simulation the boundary of unitDem(delicate and rough)
		outJsonObject.addProperty(GlobalProperty.overviewProperty_SplitSpendTime, simulationTime);
		outJsonObject.add(GlobalProperty.overviewProperty_SplitDelicateBoundary, delicateJson);
		outJsonObject.add(GlobalProperty.overviewProperty_SplitRoughBoundary, roughJson);
		this.overviewPorperty.add(GlobalProperty.overviewProperty_Split + index, outJsonObject);
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
		Map<String, Double> classifiedBoundary = getListStatics(this.classified.get(index));
		String[][] delicateAscii = new AsciiIntercept(this.originalDelicateAscii).getIntercept(
				classifiedBoundary.get("minX") - delicateCellSize, classifiedBoundary.get("maxX") + delicateCellSize,
				classifiedBoundary.get("minY") - delicateCellSize, classifiedBoundary.get("maxY") + delicateCellSize);
		String[][] delicateAsciiKn = new AsciiIntercept(this.originalDelicateAsciiKn).getIntercept(
				classifiedBoundary.get("minX") - delicateCellSize, classifiedBoundary.get("maxX") + delicateCellSize,
				classifiedBoundary.get("minY") - delicateCellSize, classifiedBoundary.get("maxY") + delicateCellSize);
		new AtFileWriter(delicateAscii, GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem)
				.textWriter("    ");
		new AtFileWriter(delicateAsciiKn,
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDemKn).textWriter("    ");

		// determine the rough ascii and kn file
		double roughCellSize = Double.parseDouble(this.originalRoughAscii.getProperty().get("cellSize"));
		Map<String, Double> roughBoundary = getBufferBoundary(new AsciiBasicControl(delicateAscii), restTime);
		String[][] roughAscii = new AsciiIntercept(this.originalRoughAscii).getIntercept(
				roughBoundary.get("minX") - roughCellSize, roughBoundary.get("maxX") + roughCellSize,
				roughBoundary.get("minY") - roughCellSize, roughBoundary.get("maxY") + roughCellSize);
		String[][] roughAsciiKn = new AsciiIntercept(this.originalRoughAsciiKn).getIntercept(
				roughBoundary.get("minX") - roughCellSize, roughBoundary.get("maxX") + roughCellSize,
				roughBoundary.get("minY") - roughCellSize, roughBoundary.get("maxY") + roughCellSize);

		// set roughDem to null value if it is overlapping to delicateDem
		roughAscii = setOverlappingNull(new AsciiBasicControl(delicateAscii), new AsciiBasicControl(roughAscii));
		roughAsciiKn = setOverlappingNull(new AsciiBasicControl(delicateAscii), new AsciiBasicControl(roughAsciiKn));

		new AtFileWriter(roughAscii, GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_RoughDem)
				.textWriter("    ");
		new AtFileWriter(roughAsciiKn, GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_RoughDemKn)
				.textWriter("    ");
	}

	// set the roughDem to null if it is overlapping
	private String[][] setOverlappingNull(AsciiBasicControl delicateAscii, AsciiBasicControl roughAscii) {
		String[][] delicateContent = delicateAscii.getAsciiGrid();
		String roughNull = roughAscii.getProperty().get("noData");

		int bufferGrid = new BigDecimal(Double.parseDouble(roughAscii.getProperty().get("cellSize"))
				- Double.parseDouble(delicateAscii.getProperty().get("cellSize"))).setScale(3, BigDecimal.ROUND_HALF_UP)
						.intValue();

		for (int row = bufferGrid; row < delicateContent.length - bufferGrid; row++) {
			for (int column = bufferGrid; column < delicateContent[0].length - bufferGrid; column++) {

				double coordinate[] = delicateAscii.getCoordinate(column, row);
				roughAscii.setValue(coordinate[0], coordinate[1], roughNull);
			}
		}

		return roughAscii.getAsciiFile();
	}

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
}
