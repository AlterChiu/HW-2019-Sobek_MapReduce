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
		ff.newFolder(GlobalProperty.splitSaveFolder);
		ff.newFolder(GlobalProperty.splitSaveFolder_Horizontal);
		ff.newFolder(GlobalProperty.splitSaveFolder_Straight);
		// =============Analysis Property===============
		if (!new File(GlobalProperty.workSpace + GlobalProperty.propertyFileName).exists()) {
			JsonObject outJson = new JsonObject();
			outJson.addProperty(GlobalProperty.delicateTotal, 0);
			outJson.addProperty(GlobalProperty.roughTotal, 0);

			new AtFileWriter(outJson, GlobalProperty.overViewPropertyFile).textWriter("");
		}

		// ====================merge================
		ff.newFolder(GlobalProperty.mergeSaveFolder);

		// ==================total===================
		ff.newFolder(GlobalProperty.totalFolder);
		ff.newFolder(GlobalProperty.totalFolder_Delicate);
		ff.newFolder(GlobalProperty.totalFolder_Rough);
	}

	public void createAfterSplitCount() throws IOException {

		// ====================split==================
		for (int i = 0; i < GlobalProperty.splitSize; i++) {
			ff.newFolder(GlobalProperty.splitSaveFolder_Horizontal + i);
			ff.newFolder(GlobalProperty.splitSaveFolder_Straight + i);
			ff.newFolder(GlobalProperty.mergeSaveFolder_Horizontal + i);
			ff.newFolder(GlobalProperty.mergeSaveFolder_Straight + i);
		}

		// =================== overview property =============
		JsonObject outJson = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();

		JsonObject horizontalSplitArray = new JsonObject();
		JsonObject straightSplitArray = new JsonObject();
		JsonObject horizontalMergeArray = new JsonObject();
		JsonObject straigjtMergeArray = new JsonObject();

		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			horizontalSplitArray.addProperty(index + "", 0);
			straightSplitArray.addProperty(index + "", 0);
			horizontalMergeArray.addProperty(index + "", 0);
			straigjtMergeArray.addProperty(index + "", 0);
		}
		outJson.add(GlobalProperty.straightSplit, straightSplitArray);
		outJson.add(GlobalProperty.horizontalSplit, horizontalSplitArray);
		outJson.add(GlobalProperty.straightMerge, straigjtMergeArray);
		outJson.add(GlobalProperty.horizontalMerge, horizontalMergeArray);

		new AtFileWriter(outJson, GlobalProperty.overViewPropertyFile).textWriter("");

		// ====================merge======================

	}

	public void setSplitSize() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject object = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		long totalTimeCount = object.get("delicateTotal").getAsLong();
		GlobalProperty.splitSize = new BigDecimal(totalTimeCount / GlobalProperty.splitTime)
				.setScale(0, BigDecimal.ROUND_UP).intValue();
	}

}
