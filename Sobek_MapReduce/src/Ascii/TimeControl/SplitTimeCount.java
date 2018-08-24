package Ascii.TimeControl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Analysis.Result.FloodCompare.ResultCompare;
import Ascii.Split.Kmeans.SplitAscii_Kmeas;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;

import main.MapReduceMainPage;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;
import GlobalProperty.GlobalProperty;

public class SplitTimeCount {

	private JsonObject overviewPorperty;
	private double restTimeCoefficient;
	private SplitAscii_Kmeas splitMethod;

	//
	// <=====================>
	// < setting the split >
	// <=====================>
	// <=======================================================>
	public SplitTimeCount() throws IOException {
		this.overviewPorperty = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		this.splitMethod = new SplitAscii_Kmeas();
		this.restTimeCoefficient = GlobalProperty.roughBufferCoefficient;
	}

	// <=======================================================>
	// main operation in this class
	// <=======================================================>
	public void runSplitDem() throws IOException {
		Boolean restar = false;

		// loop for the classified
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			String classifiedSaveFolder = GlobalProperty.saveFolder_Split + index + "\\";

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
				if (reStartJudgement(index, sobekRuntimes.getSimulateTime())) {
					restar = true;
					break;
				} else {
					index = index - 1;
				}
			} else {
				// copy the result of delicate demFile to the temptSave folder
				moveRsult(classifiedSaveFolder);
				// outPut result to the overview property file
				outPutResult(index, sobekRuntimes.getSimulateTime(), this.restTimeCoefficient);
				// reset the buffer coefficient
				this.restTimeCoefficient = GlobalProperty.roughBufferCoefficient;
			}
		}

		// if the buffer size is over limit
		// restart the process
		if (restar) {
			// reset the buffer coefficient
			this.restTimeCoefficient = GlobalProperty.roughBufferCoefficient;
			runSplitDem();
		}
	}

	// recreate the roughDem in unitDem, cause simulation time is over limit
	private Boolean reStartJudgement(int index, double simulationTime) throws IOException {
		Boolean restart = false;
		if (this.restTimeCoefficient >= GlobalProperty.splitSize) {
			System.out.println("simulation time of the delicate dem is over limit");
			System.out.println("resetting the split number of the unitDem");

			// resetting the split number of the unitDem
			// and also set the variable to default
			GlobalProperty.splitSize = GlobalProperty.splitSize + 1;
			MapReduceMainPage.initialize.createAfterTotalRun();
			this.splitMethod = new SplitAscii_Kmeas();
			restart = true;
		} else {
			this.restTimeCoefficient = this.restTimeCoefficient * GlobalProperty.errorConvergence;
			this.splitMethod.determinRoughAsciiFile(GlobalProperty.saveFolder_Split + index + "//",
					restTimeCoefficient);
		}
		return restart;
	}
	// <==========================================================>

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	// <==========================================================>
	// < output Result Function>
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

	// output the boundary of the unitDem
	private void outPutResult(int index, double simulationTime, double bufferCoefficient) throws IOException {
		JsonObject outJsonObject = new JsonObject();

		// comparison
		ResultCompare resultCompare = new ResultCompare(GlobalProperty.saveFolder_Split + index + "//");

		// delicate
		Map<String, String> delicateProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem).getProperty();
		JsonObject delicateJson = getBoundaryJson(delicateProperty);

		// rough
		// coefficient, split spend time, flood time error, flood depth error
		JsonArray roughJsonArray = new JsonArray();
		Map<String, String> roughProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_RoughDem).getProperty();
		JsonObject temptRoughJson = getBoundaryJson(roughProperty);
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_BufferCoefficient,
				new BigDecimal(bufferCoefficient).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_SplitSpendTime, simulationTime);
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodTimesError,
				resultCompare.getMeanTimesDifference());
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodDepthError,
				resultCompare.getMeanValueDifference());
		roughJsonArray.add(temptRoughJson);

		// add the property to the overview jsonFile
		// spend time of the unit simulation the boundary of unitDem(delicate and rough)
		// outer JsonFile will contain, max spend time, max buffer coefficient,
		// minError(time and value)
		outJsonObject.addProperty(GlobalProperty.overviewProperty_MaxSpendTime, simulationTime);
		outJsonObject.addProperty(GlobalProperty.overviewProperty_MaxBufferCoefficient, bufferCoefficient);
		outJsonObject.add(GlobalProperty.overviewProperty_SplitDelicateBoundary, delicateJson);
		outJsonObject.add(GlobalProperty.overviewProperty_SplitRoughBoundary, roughJsonArray);
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
	// <==========================================================================>
}
