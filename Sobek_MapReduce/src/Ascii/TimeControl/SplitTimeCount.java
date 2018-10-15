package Ascii.TimeControl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Analysis.Result.FloodCompare.ResultCompare;
import Ascii.Delicate.Split.Kmeans.SplitAscii_Kmeas;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;

import main.MapReduceMainPage;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;
import GlobalProperty.GlobalProperty;

public class SplitTimeCount {

	//
	// <=====================>
	// < setting the split >
	// <=====================>
	// <=======================================================>
	public SplitTimeCount() throws IOException {
		new SplitAscii_Kmeas();
	}

	// <=======================================================>
	// main operation in this class
	// <=======================================================>

	public void runSplitDem(int index) throws IOException, InterruptedException {
		// loop for the classified
		String classifiedSaveFolder = GlobalProperty.saveFolder_Split + index + "\\";

		// run sobek model
		SobekDem sobekDem = new SobekDem();
		sobekDem.addDelicateDem(classifiedSaveFolder + GlobalProperty.saveFile_DelicateDem,
				classifiedSaveFolder + GlobalProperty.saveFile_DelicateDemKn);
		if(GlobalProperty.nodeFunction_convergence_Delicate) {
			sobekDem.setDelicateNode();
		}
		sobekDem.start();

		// check for the simulation time is over limit or not
		Runtimes sobekRuntimes = new Runtimes();
		sobekRuntimes.RuntimesNoLimit();
		System.out.println("simulation time unitDem_" + index + " : " + sobekRuntimes.getSimulateTime());

		// move the result to classified folder
		moveRsult(classifiedSaveFolder);
		// outPut result to the overview property file
		outPutResult(index, sobekRuntimes.getSimulateTime());
	}

	public void runSplitDem() throws IOException, InterruptedException {
		Boolean restar = false;

		// loop for the classified
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			String classifiedSaveFolder = GlobalProperty.saveFolder_Split + index + "\\";

			// run sobek model
			SobekDem sobekDem = new SobekDem();
			sobekDem.addDelicateDem(classifiedSaveFolder + GlobalProperty.saveFile_DelicateDem,
					classifiedSaveFolder + GlobalProperty.saveFile_DelicateDemKn);
			sobekDem.start();

			// check for the simulation time is over limit or not
			Runtimes sobekRuntimes = new Runtimes();
			sobekRuntimes.RuntimesNoLimit();
			System.out.println("simulation time unitDem_" + index + " : " + sobekRuntimes.getSimulateTime());
			if (sobekRuntimes.getSimulateTime() > GlobalProperty.totalAllowTime) {
				// resetting the split number of the unitDem
				// and also set the variable to default
				GlobalProperty.splitSize = GlobalProperty.splitSize + 1;
				MapReduceMainPage.initialize.createAfterTotalRun();
				new SplitAscii_Kmeas();

				// restart this function
				restar = true;
				System.out.println("=================resplit unitDem================");
				break;

			} else {
				// move the result to classified folder
				moveRsult(classifiedSaveFolder);
				// outPut result to the overview property file
				outPutResult(index, sobekRuntimes.getSimulateTime());
			}
		}

		// restart the process
		if (restar) {
			// reset the buffer coefficient
			runSplitDem();
		}
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
	private void outPutResult(int index, double simulationTime) throws IOException {
		JsonObject overviewProperty = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		JsonObject outJsonObject = new JsonObject();
		JsonArray roughArray = new JsonArray();

		// delicate
		Map<String, String> delicateProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem).getProperty();
		JsonObject delicateJson = getBoundaryJson(delicateProperty);
		delicateJson.addProperty(GlobalProperty.overviewProperty_SpendTime_Split, simulationTime);

		// add the property to the overview jsonFile
		// spend time of the unit simulation the boundary of unitDem(delicate and rough)
		// outer JsonFile will contain, max spend time, max buffer coefficient,
		// minError(time and value)
		outJsonObject.add(GlobalProperty.overviewProperty_SplitDelicateBoundary, delicateJson);
		outJsonObject.add(GlobalProperty.overviewProperty_SplitRoughBoundary, roughArray);
		overviewProperty.add(GlobalProperty.overviewProperty_Split + index, outJsonObject);

		new AtFileWriter(overviewProperty, GlobalProperty.overViewPropertyFile).textWriter("");
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
