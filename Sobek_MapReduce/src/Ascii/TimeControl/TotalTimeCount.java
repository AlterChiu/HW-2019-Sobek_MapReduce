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
	private String originalDem;
	private String originalKnDem;

	private String sobekResultFolder;
	private String saveFolder;

	private String sobekOperateDem;
	private String sobelOperateKnDem;

	private String jsonKeyName;

	public void RoughTotalTimeCount() throws IOException {
		this.originalDem = GlobalProperty.originalRough;
		this.originalKnDem = GlobalProperty.originalRoughKn;

		this.sobekResultFolder = GlobalProperty.sobekResultFolder;
		this.saveFolder = GlobalProperty.totalRoughSaveFolder;

		this.sobekOperateDem = GlobalProperty.sobekRoughDem;
		this.sobelOperateKnDem = GlobalProperty.sobekRoughDemKn;
		
		this.sobekResultFolder = GlobalProperty.sobekResultFolder;
		this.saveFolder = GlobalProperty.totalRoughSaveFolder;
		
		this.jsonKeyName = GlobalProperty.roughTotal;
		
		startRuntimes();
	}

	public void DelicateTotalTimeCount() throws IOException {

		this.originalDem = GlobalProperty.originalDelicate;
		this.originalKnDem = GlobalProperty.originalDelicateKn;

		this.sobekResultFolder = GlobalProperty.sobekResultFolder;
		this.saveFolder = GlobalProperty.totalDelicateSaveFolder;

		this.sobekOperateDem = GlobalProperty.sobekDelicateDem;
		this.sobelOperateKnDem = GlobalProperty.sobekDelicateDemKn;
		
		this.sobekResultFolder = GlobalProperty.sobekResultFolder;
		this.saveFolder = GlobalProperty.totalDelicateSaveFolder;

		this.jsonKeyName = GlobalProperty.delicateTotal;
		
		startRuntimes();
	}

	private void startRuntimes() throws IOException {
		// copy file from the original folder to sobek operation folder
		FileFunction ff = new FileFunction();
		ff.copyFile(originalDem, sobekOperateDem);
		ff.copyFile(originalKnDem, sobelOperateKnDem);

		// run sobek model
		long startTime = System.currentTimeMillis();
		new Runtimes();
		long endTime = System.currentTimeMillis();

		// output the property file
		JsonObject readJson = new AtFileReader(GlobalProperty.workSpace + GlobalProperty.propertyFileName)
				.getJsonObject();
		readJson.addProperty(this.jsonKeyName, endTime - startTime);
		
		// copy the result file to folder
		ff.copyFolder(this.sobekResultFolder, this.saveFolder);
	}

}
