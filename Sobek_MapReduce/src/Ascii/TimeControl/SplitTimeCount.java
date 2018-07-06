package Ascii.TimeControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import SOBEK.Runtimes;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiSplit;
import usualTool.AtCommonMath;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;
import GlobalProperty.GlobalProperty;

public class SplitTimeCount {

	private ArrayList<String[][]> splitAsciiFile = null;
	private ArrayList<String[][]> splitAsciiFileKn = null;
	private AsciiBasicControl temptAscii;
	private String analysisPropertyKey;

	// straight in default
	private String saveOutFolderAdd = GlobalProperty.splitDelicateSaveFolder_Straight;

	//
	// <=====================>
	// < setting the split way >
	// <=====================>
	// <=======================================================>
	public void setDelicateHorizantal() throws IOException {
		this.splitAsciiFile = new AsciiSplit(GlobalProperty.originalDelicate).horizontalSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.splitAsciiFileKn = new AsciiSplit(GlobalProperty.originalDelicateKn).horizontalSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		System.out.println("finished split");
		this.saveOutFolderAdd = GlobalProperty.splitDelicateSaveFolder_Horizontal;
		this.analysisPropertyKey = GlobalProperty.horizontalSplit;
		startRuntimes();
	}

	public void setDelicateStraight() throws IOException {
		this.splitAsciiFile = new AsciiSplit(GlobalProperty.originalDelicate).straightSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.splitAsciiFileKn = new AsciiSplit(GlobalProperty.originalDelicateKn).straightSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.saveOutFolderAdd = GlobalProperty.splitDelicateSaveFolder_Straight;
		this.analysisPropertyKey = GlobalProperty.straightSplit;
		startRuntimes();
	}

	// <=====================>
	// < start to cal >
	// <=====================>
	// <==========================================================>
	private void startRuntimes() throws IOException {

		// start split
		// ==========================================================
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			String splitFolder = saveOutFolderAdd + index + "\\";

			// if the property file of this split dem isn't exit , create a new one
			if (!new File(splitFolder + GlobalProperty.propertyFileName).exists()) {
				propertyCreater(index);
			}

			// change the sobek operation dem to the split dem
			String[][] outAscii = new AsciiMerge(this.splitAsciiFile.get(index) , GlobalProperty.originalDelicateNull).getMergedAscii();
			String[][] outAsciiKn = new AsciiMerge(this.splitAsciiFileKn.get(index) , GlobalProperty.originalDelicateNull).getMergedAscii();
			
			new AtFileWriter(outAscii, GlobalProperty.sobekDelicateDem).textWriter("    ");
			new AtFileWriter(outAsciiKn, GlobalProperty.sobekDelicateDemKn).textWriter("    ");
			
			// save the split dem at this split folder
			new AtFileWriter(outAscii, splitFolder + GlobalProperty.demTempSaveName)
					.textWriter("    ");
			new AtFileWriter(outAsciiKn, splitFolder + GlobalProperty.demTempSaveNameKn)
					.textWriter("    ");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// run sobek model and calculate the time
			long startTime = System.currentTimeMillis();
			new Runtimes(GlobalProperty.sobekRuntimesForecastBar_Delicate);
			long endTime = System.currentTimeMillis();

			// adding the spend time to property file
			JsonObject json = new AtFileReader(splitFolder + GlobalProperty.propertyFileName).getJsonObject();
			json.get("spendTime").getAsJsonArray().add(new JsonParser().parse(endTime - startTime + ""));
			new AtFileWriter(json, splitFolder + GlobalProperty.propertyFileName).textWriter("");

			// adding the average of spend time to the analysis property file
			propertyAnalysis(splitFolder);
		}
	}

	private void propertyAnalysis(String splitFolder)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		String folderIndex = new File(splitFolder).getName();
		List<Double> spendTimeList = new ArrayList<Double>();

		// get the analysis property file
		String analysisFile = GlobalProperty.workSpace + GlobalProperty.propertyFileName;
		JsonObject analysisJson = new AtFileReader(analysisFile).getJsonObject();
		JsonArray analysisArray = analysisJson.get(analysisPropertyKey).getAsJsonArray();

		// get the split property file
		JsonObject splitJson = new AtFileReader(splitFolder + GlobalProperty.propertyFileName).getJsonObject();

		// get the average of the spend time in split property
		splitJson.get("spendTime").getAsJsonArray().forEach(e -> spendTimeList.add(e.getAsDouble()));
		double mean = new AtCommonMath(spendTimeList).getMean();

		// insert the average spend time to the analysis property file
		ArrayList<String> temptList = new ArrayList<String>();
		analysisArray.forEach(e->temptList.add(e.getAsString()));
		temptList.remove(Integer.parseInt(folderIndex));
		temptList.add(Integer.parseInt(folderIndex), mean+"");
		JsonArray newArray = new JsonArray();
		temptList.forEach(e -> newArray.add(new JsonParser().parse(e + "")));
		
		analysisJson.add(analysisPropertyKey, newArray);
		new AtFileWriter(analysisJson, analysisFile).textWriter("");
	}

	private void propertyCreater(int index) throws IOException {
		// dem property
		this.temptAscii = new AsciiBasicControl(this.splitAsciiFile.get(index));
		TreeMap<String, String> asciiProperty = this.temptAscii.getProperty();

		// create new jsonProperty to the split dem folder
		JsonObject json = new JsonObject();
		json.addProperty("maxX", asciiProperty.get("topX"));
		json.addProperty("maxY", asciiProperty.get("topY"));
		json.addProperty("minX", asciiProperty.get("bottomX"));
		json.addProperty("minY", asciiProperty.get("bottomT"));
		json.addProperty("cellSize", asciiProperty.get("cellSize"));

		JsonArray jsonArray = new JsonArray();
		json.add("spendTime", jsonArray);

		new AtFileWriter(json, saveOutFolderAdd + index + "\\" + GlobalProperty.propertyFileName).textWriter("");
	}
}
