package Ascii.TimeControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import SOBEK.Runtimes;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiSplit;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import GlobalProperty.GlobalProperty;

public class SplitTimeCount {

	private ArrayList<String[][]> splitAsciiFile = null;
	private ArrayList<String[][]> splitAsciiFileKn = null;
	private String originalDemNullFile = null;
	

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
		this.saveOutFolderAdd = GlobalProperty.splitDelicateSaveFolder_Horizontal;
		this.originalDemNullFile = GlobalProperty.originalDelicateNull;
		startRuntimes();
	}

	public void setDelicateStraight() throws IOException {
		this.splitAsciiFile = new AsciiSplit(GlobalProperty.originalDelicate).straightSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.splitAsciiFileKn = new AsciiSplit(GlobalProperty.originalDelicateKn).straightSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.saveOutFolderAdd = GlobalProperty.splitDelicateSaveFolder_Straight;
		this.originalDemNullFile = GlobalProperty.originalDelicateNull;
		startRuntimes();
	}

	public void setRoughHorizantal() throws IOException {
		this.splitAsciiFile = new AsciiSplit(GlobalProperty.originalRough).horizontalSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.splitAsciiFileKn = new AsciiSplit(GlobalProperty.originalRoughKn).horizontalSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.saveOutFolderAdd = GlobalProperty.splitRoughSaveFolder_Horizontal;
		this.originalDemNullFile = GlobalProperty.originalRoughNull;
		
		startRuntimes();
	}

	public void setRoughStraight() throws IOException {
		this.splitAsciiFile = new AsciiSplit(GlobalProperty.originalRough).straightSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.splitAsciiFileKn = new AsciiSplit(GlobalProperty.originalRoughKn).straightSplit()
				.getSplitAsciiByEqualCut(GlobalProperty.splitSize);
		this.saveOutFolderAdd = GlobalProperty.splitRoughSaveFolder_Straight;
		this.originalDemNullFile = GlobalProperty.originalRoughNull;
		startRuntimes();
	}

	// <==========================================================>

	// <=====================>
	// < start to cal >
	// <=====================>
	// <==========================================================>
	private void startRuntimes() throws IOException {

		// start split
		// ==========================================================
		for (int times = 0; times < GlobalProperty.splitSize; times++) {
			
			// dem property
			AsciiBasicControl ascii = new AsciiBasicControl(this.splitAsciiFile.get(times));
			TreeMap<String, String> asciiProperty = ascii.getProperty();
			
			new AtFileWriter(this.splitAsciiFile.get(times), GlobalProperty.sobekDelicateDem).textWriter("    ");
			new AtFileWriter(this.splitAsciiFileKn.get(times), GlobalProperty.sobekDelicateDemKn).textWriter("    ");

			// run sobek model and calculate the time
			long startTime = System.currentTimeMillis();
			new Runtimes();
			long endTime = System.currentTimeMillis();
			System.out.println(endTime - startTime);

			// Adding some property to outPutFile
			JsonObject json;
			
			// if the file is exist
			try {
				json = new AtFileReader(saveOutFolderAdd + times + "\\" + GlobalProperty.sobekResultPropertyFileName)
						.getJsonObject();
				json.get("spendTime").getAsJsonArray().add(new JsonPrimitive((endTime - startTime) + ""));
				new AtFileWriter(this.splitAsciiFile.get(times), saveOutFolderAdd + times + "\\" + GlobalProperty.splitDemTempSaveName)
				.textWriter("    ");
				
				
			// if the file is't exist
			} catch (Exception e) {
				json = new JsonObject();
				json.addProperty("maxX", asciiProperty.get("topX"));
				json.addProperty("maxY", asciiProperty.get("topY"));
				json.addProperty("minX", asciiProperty.get("bottomX"));
				json.addProperty("minY", asciiProperty.get("bottomT"));
				json.addProperty("cellSize", asciiProperty.get("cellSize"));
				
				JsonArray jsonArray = new JsonArray();
				jsonArray.add(new JsonPrimitive((endTime - startTime) + ""));
				json.add("spendTime", jsonArray);

				new AtFileWriter(this.splitAsciiFile.get(times), saveOutFolderAdd + times + "\\" + GlobalProperty.splitDemTempSaveName)
						.textWriter("    ");
			}
			
			new AtFileWriter(json, saveOutFolderAdd + times + "\\" + GlobalProperty.sobekResultPropertyFileName).textWriter(" ");

		}
	}

	// <================================================================>

}
