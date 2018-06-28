package Ascii.TimeControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class TotalTimeCount {

	public void RoughTotalTimeCount() throws IOException {

		String originalDem = GlobalProperty.originalRough;
		String originalKnDem = GlobalProperty.originalRoughKn;

		String sobekResultFolder = GlobalProperty.sobekResultFolder;
		String saveFolder = GlobalProperty.totalRoughSaveFolder;

		String sobekOperateDem = GlobalProperty.sobekRoughDem;
		String sobelOperateKnDem = GlobalProperty.sobekRoughDemKn;

		FileFunction ff = new FileFunction();

		ff.copyFile(originalDem, sobekOperateDem);
		ff.copyFile(originalKnDem, sobelOperateKnDem);

		long startTime = System.currentTimeMillis();
		new Runtimes();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);

		
		//if the file is exist
		//===========================================
		try {
			ArrayList<String> spendTimeList = new ArrayList<String>(
					Arrays.asList(new AtFileReader(saveFolder + GlobalProperty.sobekResultPropertyFileName).getContain()));
			
			JsonArray jsonArray = new JsonArray();
			spendTimeList.forEach(element -> jsonArray.add(new JsonPrimitive(element)));
			jsonArray.add(new JsonPrimitive((endTime - startTime) + ""));
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("spendTime", jsonArray); 
			
			ff.moveFolder(sobekResultFolder, saveFolder);
			new AtFileWriter(jsonObject ,  saveFolder + GlobalProperty.sobekResultPropertyFileName).textWriter("");
			
			//if the file isn't exist
			//===========================================
		} catch (Exception e) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(new JsonPrimitive((endTime - startTime) + ""));
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("spendTime", jsonArray); 
			
			ff.moveFolder(sobekResultFolder, saveFolder);
			new AtFileWriter(jsonObject ,  saveFolder + GlobalProperty.sobekResultPropertyFileName).textWriter("");
		}
		

		new AtFileWriter(String.valueOf(endTime - startTime), saveFolder + GlobalProperty.sobekResultPropertyFileName)
				.textWriter("  ");
		;
	}

	public void DelicateTotalTimeCount() throws IOException {

		String originalDem = GlobalProperty.originalDelicate;
		String originalKnDem = GlobalProperty.originalDelicateKn;

		String sobekResultFolder = GlobalProperty.sobekResultFolder;
		String saveFolder = GlobalProperty.totalDelicateSaveFolder;

		String sobekOperateDem = GlobalProperty.sobekDelicateDem;
		String sobelOperateKnDem = GlobalProperty.sobekDelicateDemKn;

		FileFunction ff = new FileFunction();

		new AtFileWriter(new AtFileReader(originalDem).getContain(), sobekOperateDem).textWriter("");
		new AtFileWriter(new AtFileReader(originalKnDem).getContain(), sobelOperateKnDem).textWriter("");

		long startTime = System.currentTimeMillis();
		new Runtimes();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);

		ff.moveFolder(sobekResultFolder, saveFolder);

		new AtFileWriter(String.valueOf(endTime - startTime), saveFolder + GlobalProperty.sobekResultPropertyFileName)
				.textWriter("  ");
	}

}
