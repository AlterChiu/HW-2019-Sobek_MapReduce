package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class InitializeFolder {
	private FileFunction ff = new FileFunction();

	public void createBeforeSplitCount() throws IOException {
		// ====================split==================
		ff.newFolder(GlobalProperty.workSpace + "split\\");
		ff.newFolder(GlobalProperty.workSpace + "split\\horizontal\\");
		ff.newFolder(GlobalProperty.workSpace + "split\\straight\\");
		// =============Analysis Property===============
		if (!new File(GlobalProperty.workSpace + GlobalProperty.propertyFileName).exists()) {
			JsonObject outJson = new JsonObject();
			outJson.addProperty(GlobalProperty.delicateTotal, 0);
			outJson.addProperty(GlobalProperty.roughTotal, 0);

			new AtFileWriter(outJson, GlobalProperty.workSpace + GlobalProperty.propertyFileName).textWriter("");
		}

		// ====================merge================
		ff.newFolder(GlobalProperty.workSpace + "merge\\");

		// ==================total===================
		ff.newFolder(GlobalProperty.workSpace + "total\\");
		ff.newFolder(GlobalProperty.workSpace + "total\\delicate\\");
		ff.newFolder(GlobalProperty.workSpace + "total\\rough\\");
	}

	public void createAfterSplitCount() throws IOException {

		// ====================split==================
		for (int i = 0; i < GlobalProperty.splitSize; i++) {
			ff.newFolder(GlobalProperty.workSpace + "split\\horizontal\\" + i);
			ff.newFolder(GlobalProperty.workSpace + "split\\straight\\" + i);
		}
		
		//=================== overview property =============
		JsonObject outJson = new AtFileReader(GlobalProperty.overviewProperty).getJsonObject();
		
		JsonArray horizontalArray = new JsonArray();
		JsonArray straightArray = new JsonArray();
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			straightArray.add(new JsonParser().parse("0"));
			horizontalArray.add(new JsonParser().parse("0"));
		}
		outJson.add(GlobalProperty.straightSplit, straightArray);
		outJson.add(GlobalProperty.horizontalSplit, horizontalArray);
		
		new AtFileWriter(outJson, GlobalProperty.workSpace + GlobalProperty.propertyFileName).textWriter("");
	}
	
	public void setSplitSize() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject object = new AtFileReader(GlobalProperty.overviewProperty).getJsonObject();
		long totalTimeCount = object.get("delicateTotal").getAsLong();
		GlobalProperty.splitSize = new BigDecimal(totalTimeCount / GlobalProperty.splitTime)
				.setScale(0, BigDecimal.ROUND_UP).intValue();
	}


}
