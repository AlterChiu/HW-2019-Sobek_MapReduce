package Ascii.TimeControl;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import usualTool.AtCommonMath;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;

public class AnalysisSplitAscii{
	
	
//	analysis save folder
//	===========================================================
	private String delicateTotalProperty = GlobalProperty.totalDelicateSaveFolder + "\\" + GlobalProperty.sobekResultPropertyFileName;
	private String roughTotalProperty = GlobalProperty.totalRoughSaveFolder + "\\" + GlobalProperty.sobekResultPropertyFileName;



//	// out put analysis file
//	===================================================================
	public void delicateHorizontalAnalysis() throws JsonSyntaxException, JsonIOException, IOException {
		JsonObject json = folderSelect( GlobalProperty.splitDelicateSaveFolder_Horizontal);
		json.addProperty("originalTotal", (fileSelect(this.delicateTotalProperty)));
		new AtFileWriter(json , GlobalProperty.splitAnalysis_Horizontal_Delicate).textWriter("");
	}
	

	public void delicateStraightAnalysis() throws JsonSyntaxException, JsonIOException, IOException {
		JsonObject json = folderSelect(GlobalProperty.splitDelicateSaveFolder_Straight);
		json.addProperty("originalTotal", fileSelect(this.delicateTotalProperty));
		new AtFileWriter(json , GlobalProperty.splitAnalysis_Straight_Delicate).textWriter("");
	}
	
	public void roughHorizontalAnalysis() throws JsonSyntaxException, JsonIOException, IOException {
		JsonObject json = folderSelect(GlobalProperty.splitRoughSaveFolder_Horizontal);
		json.addProperty("originalTotal",(fileSelect(this.roughTotalProperty)));
		new AtFileWriter(json , GlobalProperty.splitAnalysis_Horizontal_Rough).textWriter("");
	}
	

	public void roughStraightAnalysis() throws JsonSyntaxException, JsonIOException, IOException {
		JsonObject json = folderSelect(GlobalProperty.splitRoughSaveFolder_Straight);
		json.addProperty("originalTotal", (fileSelect(this.roughTotalProperty)));
		new AtFileWriter(json , GlobalProperty.splitAnalysis_Straight_Rough).textWriter("");
	}
	
	
	
	
	
	
	
//	for total file analysis
//	====================================================================
	private Double fileSelect(String fileName) throws IOException {
		JsonObject jsonObject = new AtFileReader(fileName).getJsonObject();
		ArrayList<Double> temptList = new ArrayList<Double>();
		jsonObject.get("timeSpend").getAsJsonArray().forEach(element -> temptList.add(element.getAsDouble()));
		
		return new AtCommonMath(temptList).getMean();
	}
	
	
	
	// for split folder analysis
	// ==================================================================
	private  JsonObject folderSelect(String folderName) throws JsonSyntaxException, JsonIOException, IOException {
		String fileNameList[] = new File(folderName).list();
		JsonObject json = new JsonObject();
		
		// read the split folder
		ArrayList<Double> timeSpend = new ArrayList<Double>();
		for (String fileName : fileNameList) {
			JsonObject property = new AtFileReader(folderName +"\\" +  fileName + "\\" + GlobalProperty.sobekResultPropertyFileName).getJsonObject();
			ArrayList<Double> temptList = new ArrayList<Double>();
			property.get("spendTime").getAsJsonArray().forEach(element -> temptList.add(element.getAsDouble()));
			
			timeSpend.add(new AtCommonMath(temptList).getMean());
		}
		
		// make the out put json file
		for(int index = 0; index<timeSpend.size();index++) {
			json.addProperty(index + "", timeSpend.get(index));
		}
		json.addProperty("splitSum", new AtCommonMath(timeSpend).getSum());
		
		return json;
	}

}
