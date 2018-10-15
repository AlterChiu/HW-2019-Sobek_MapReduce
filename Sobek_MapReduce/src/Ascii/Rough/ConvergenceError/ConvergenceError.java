package Ascii.Rough.ConvergenceError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import Analysis.Result.FloodCompare.ResultCompare;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import Ascii.Delicate.Split.DeterminRoughAsciiFile;
import asciiFunction.AsciiBasicControl;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class ConvergenceError extends DeterminRoughAsciiFile {
	private double temptBufferCoefficient;
	private double coefficeintDisplace;

	public void start(int index) throws IOException, InterruptedException {
		// reset the buffer coefficient to the selected delicate demFile
		this.temptBufferCoefficient = settingBufferLimit(index) - GlobalProperty.errorConvergence_difference;
		System.out.println("max buffer coefficient unitDem_" + index + " : " + this.temptBufferCoefficient);
		this.coefficeintDisplace = settingBufferDisplace(index);
		System.out.println("difference buffer coefficient unitDem_" + index + " : " + this.coefficeintDisplace);
		outPutCoefficientProperty(index);

		// to convergence the error value
		// using try and error
		Boolean exitBoolean = false;
		while (GlobalProperty.errorConvergence_Min < temptBufferCoefficient) {
			// initial the save folder for the convergence folder
			String targetFolder = initialTempFolder(index);

			// create the rough demFile for the adjusted coefficient
			determinRoughAsciiFile(targetFolder, this.temptBufferCoefficient, index);

			// check for the rough boundary
			// if it's equal to the original rough in twice then skip it
			try {
				if (checkRoughBoundary(targetFolder)) {
					if (exitBoolean) {
						new FileFunction().delete(targetFolder);
						break;
					} else {
						exitBoolean = true;
					}
				}
			} catch (Exception e) {
			}
			// start SobekRuntimes
			SobekDem sobekDem = new SobekDem();
			sobekDem.addDelicateDem(targetFolder + GlobalProperty.saveFile_DelicateDem,
					targetFolder + GlobalProperty.saveFile_DelicateDemKn);
			sobekDem.addRoughDem(targetFolder + GlobalProperty.saveFile_RoughDem,
					targetFolder + GlobalProperty.saveFile_RoughDemKn);
			if(GlobalProperty.nodeFunction_convergence_Delicate) {
				sobekDem.setDelicateNode();
			}
			if(GlobalProperty.nodeFunction_convergence_Rough) {
				sobekDem.setRoughNode();
			}
			
			sobekDem.start();

			Runtimes runtimes = new Runtimes();
			runtimes.RuntimesSetLimit();
			// move the result to the targetFolder
			moveRsult(targetFolder);

			// save the overview property
			outPutResult(index, this.temptBufferCoefficient, runtimes.getSimulateTime());

			// adjust the buffer coefficient
			this.temptBufferCoefficient = this.temptBufferCoefficient - this.coefficeintDisplace;
		}
		outPutCoefficientMin(index);
		System.out.println("min buffer coefficient unitDem_" + index + " : " + this.temptBufferCoefficient);
	}

	public void start()
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException, InterruptedException {
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			start(index);
		}
	}

	private boolean checkRoughBoundary(String targetFolder) throws IOException {
		AsciiBasicControl temptAscii = new AsciiBasicControl(targetFolder + GlobalProperty.saveFile_RoughDem);
		int temptRow = Integer.parseInt(temptAscii.getProperty().get("row"));
		int temptColumn = Integer.parseInt(temptAscii.getProperty().get("column"));

		AsciiBasicControl originalAscii = new AsciiBasicControl(GlobalProperty.originalRough);
		int originalRow = Integer.parseInt(originalAscii.getProperty().get("row"));
		int originalColumn = Integer.parseInt(originalAscii.getProperty().get("column"));

		if (temptRow == originalRow && temptColumn == originalColumn) {
			return true;
		} else {
			return false;
		}
	}

	private double settingBufferDisplace(int index)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		double delicatePartSpend = json.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SplitDelicateBoundary).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SpendTime_Split).getAsDouble();
		double roughTotalSpend = json.get(GlobalProperty.overviewProperty_SpendTime_roughTotal).getAsDouble();

		return roughTotalSpend / delicatePartSpend * GlobalProperty.errorConvergence_difference;
	}

	private double settingBufferLimit(int index)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		double delicateSpend = json.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SplitDelicateBoundary).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SpendTime_Split).getAsDouble();

		return GlobalProperty.totalAllowTime / delicateSpend;
	}

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
	private void moveRsult(String targetFolder) {
		FileFunction ff = new FileFunction();
		String[] outPutList = new File(GlobalProperty.sobekResultFolder).list();
		for (String result : outPutList) {
			if (result.contains(".asc")) {
				ff.moveFile(GlobalProperty.sobekResultFolder + "\\" + result, targetFolder + "\\" + result);
			}
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @param index
	 * @return
	 */
	private String initialTempFolder(int index) {
		FileFunction ff = new FileFunction();

		// create the folder
		String folder = GlobalProperty.saveFolder_convergence;
		folder = folder + index;
		folder = folder + "\\" + new File(folder).list().length + "\\";
		ff.newFolder(folder);

		// copy delicate demFile
		ff.copyFile(GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem,
				folder + GlobalProperty.saveFile_DelicateDem);
		ff.copyFile(GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDemKn,
				folder + GlobalProperty.saveFile_DelicateDemKn);

		return folder;
	}

	private void outPutCoefficientProperty(int index)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		JsonObject temptObject = json.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SplitDelicateBoundary).getAsJsonObject();

		temptObject.addProperty(GlobalProperty.overviewProperty_BufferCoefficient_Max, this.temptBufferCoefficient);
		temptObject.addProperty(GlobalProperty.overviewProperty_BufferCoefficient_Difference, this.coefficeintDisplace);
		new AtFileWriter(json, GlobalProperty.overViewPropertyFile).textWriter("");
	}

	private void outPutCoefficientMin(int index)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		JsonObject temptObject = json.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SplitDelicateBoundary).getAsJsonObject();

		temptObject.addProperty(GlobalProperty.overviewProperty_BufferCoefficient_Min, this.temptBufferCoefficient);
		new AtFileWriter(json, GlobalProperty.overViewPropertyFile).textWriter("");
		;
	}

	// <==================================================================>
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param index
	 * @param bufferCoefficient
	 * @throws IOException
	 */
	private void outPutResult(int index, double bufferCoefficient, double simulationTime) throws IOException {
		String resultFolder = GlobalProperty.saveFolder_convergence;
		resultFolder = resultFolder + index;
		resultFolder = resultFolder + "\\" + (new File(resultFolder).list().length - 1) + "\\";

		JsonObject overviewJson = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		JsonArray outJsonArray = overviewJson.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SplitRoughBoundary).getAsJsonArray();

		// rough
		// coefficient, split spend time, flood time error, flood depth error
		Map<String, String> roughProperty = new AsciiBasicControl(resultFolder + GlobalProperty.saveFile_RoughDem)
				.getProperty();
		JsonObject temptRoughJson = getBoundaryJson(roughProperty);
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_BufferCoefficient,
				new BigDecimal(bufferCoefficient).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_SpendTime_Split, simulationTime);

		// comparison
		// if there is error setting the value of comparison in {-1,9999}
		try {
			ResultCompare resultCompare = new ResultCompare(resultFolder);
			temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodTimesError,
					resultCompare.getMeanTimesDifference());
			temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodDepthError,
					resultCompare.getMeanValueDifference());
			outJsonArray.add(temptRoughJson);
		} catch (Exception e) {
			temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodTimesError,
					GlobalProperty.resultError_FloodTimesError);
			temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodDepthError,
					GlobalProperty.resultError_ErrorDifference);
			outJsonArray.add(temptRoughJson);
		}
		new AtFileWriter(overviewJson, GlobalProperty.overViewPropertyFile).textWriter("");
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
}
