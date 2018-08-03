package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		ff.newFolder(GlobalProperty.mergeSaveFolder_Horizontal);
		ff.newFolder(GlobalProperty.mergeSaveFolder_Straight);

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

	public void setNetWork_Pt2File() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(GlobalProperty.caseNetWork_D12)));
		String tempt;
		List<String[]> totalList = new ArrayList<String[]>();

		// tempt line
		List<String> temptList = new ArrayList<String>();
		while ((tempt = br.readLine()) != null) {
			if (tempt.trim().length() > 2 && !tempt.contains("D121.0") && !tempt.contains("DOMN")
					&& !tempt.contains("domn")) {

				// split line by space and check the first element is equal to last one or not
				Arrays.asList(tempt.trim().split(" +")).forEach(e -> temptList.add(e));

				if (temptList.get(0).toLowerCase().equals(temptList.get(temptList.size() - 1))) {
					totalList.add(temptList.parallelStream().toArray(String[]::new));
					temptList.clear();
				}
			}
		}
		br.close();

		// filter pt12 from total
		List<String[]> pt12List = new ArrayList<String[]>();
		totalList.stream().forEach(e -> {
			if (e[0].equals("PT12"))
				pt12List.add(e);
		});
		new AtFileWriter(pt12List.parallelStream().toArray(String[][]::new), GlobalProperty.caseNetWork_D12_Template)
				.textWriter(" ");
	}

	public void setSplitSize() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject object = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		long totalTimeCount = object.get("delicateTotal").getAsLong();
		GlobalProperty.splitSize = new BigDecimal(totalTimeCount / GlobalProperty.splitTime)
				.setScale(0, BigDecimal.ROUND_UP).intValue();
	}

}
