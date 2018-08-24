package Ascii.ErrorConvergence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import Analysis.Result.FloodCompare.ResultCompare;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import Ascii.Split.DeterminRoughAsciiFile;
import asciiFunction.AsciiBasicControl;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class ErrorConvergence extends DeterminRoughAsciiFile {
	private double temptBufferCoefficient;

	public ErrorConvergence() {
	}

	public void start() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			// option for stopping the convergence
			Boolean keepConvergence = true;

			// reset the buffer coefficient to the selected delicate demFile
			resetBufferCoefficient(index);

			// to convergence the error value
			// using try and error
			while (this.temptBufferCoefficient <= GlobalProperty.splitSize || keepConvergence) {
				// initial the save folder for the convergence folder
				String targetFolder = initialTempFolder(index);

				// adjust the buffer coefficient
				this.temptBufferCoefficient = this.temptBufferCoefficient * GlobalProperty.errorConvergence;

				// create the rough demFile for the adjusted coefficient
				determinRoughAsciiFile(targetFolder, this.temptBufferCoefficient);

				// start SobekRuntimes
				SobekDem sobekDem = new SobekDem();
				sobekDem.addDelicateDem(targetFolder + GlobalProperty.saveFile_DelicateDem,
						targetFolder + GlobalProperty.saveFile_DelicateDemKn);
				sobekDem.addRoughDem(targetFolder + GlobalProperty.saveFile_RoughDem,
						targetFolder + GlobalProperty.saveFile_RoughDemKn);
				sobekDem.start();
				new Runtimes();

				// move the result to the targetFolder
				moveRsult(targetFolder);

				// save the overview property
				outPutResult(index, this.temptBufferCoefficient);
			}
		}

	}

	private void resetBufferCoefficient(int index)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject overviewJson = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		this.temptBufferCoefficient = overviewJson.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_MaxBufferCoefficient).getAsDouble();
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
				ff.moveFile(GlobalProperty.sobekResultFolder + result, targetFolder + result);
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
		String folder = GlobalProperty.saveFolder_convergence;
		folder = folder + index + "\\" + new File(folder).list().length;
		new FileFunction().newFolder(folder);

		return folder;
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
	private void outPutResult(int index, double bufferCoefficient) throws IOException {
		JsonObject overviewJson = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		JsonArray outJsonArray = overviewJson.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
				.get(GlobalProperty.overviewProperty_SplitRoughBoundary).getAsJsonArray();

		// comparison
		ResultCompare resultCompare = new ResultCompare(GlobalProperty.saveFolder_Split + index + "//");

		// rough
		// coefficient, split spend time, flood time error, flood depth error
		Map<String, String> roughProperty = new AsciiBasicControl(
				GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_RoughDem).getProperty();
		JsonObject temptRoughJson = getBoundaryJson(roughProperty);
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_BufferCoefficient,
				new BigDecimal(bufferCoefficient).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodTimesError,
				resultCompare.getMeanTimesDifference());
		temptRoughJson.addProperty(GlobalProperty.overviewProperty_FloodDepthError,
				resultCompare.getMeanValueDifference());
		outJsonArray.add(temptRoughJson);

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
