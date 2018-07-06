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

	private String sobekResultFolder = GlobalProperty.sobekResultFolder;
	private String saveFolder;

	private String jsonKeyName;
	private String sobekRuntimesForecastBat;

	public void RoughTotalTimeCount() throws IOException {
		this.saveFolder = GlobalProperty.totalRoughSaveFolder;

		this.jsonKeyName = GlobalProperty.roughTotal;
		this.sobekRuntimesForecastBat = GlobalProperty.sobekRuntimesForecastBar_Rough;
		startRuntimes();
	}

	public void DelicateTotalTimeCount() throws IOException {
		this.saveFolder = GlobalProperty.totalDelicateSaveFolder;

		this.jsonKeyName = GlobalProperty.delicateTotal;
		this.sobekRuntimesForecastBat = GlobalProperty.sobekRuntimesForecastBar_Delicate;
		startRuntimes();
	}

	private void startRuntimes() throws IOException {
		// copy file from the original folder to sobek operation folder
		FileFunction ff = new FileFunction();
		ff.copyFile(GlobalProperty.originalDelicate, GlobalProperty.sobekDelicateDem);
		ff.copyFile(GlobalProperty.originalDelicateKn, GlobalProperty.sobekDelicateDemKn);

		ff.copyFile(GlobalProperty.originalRough, GlobalProperty.sobekRoughDem);
		ff.copyFile(GlobalProperty.originalRoughKn, GlobalProperty.sobekRoughDemKn);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// run sobek model
		long startTime = System.currentTimeMillis();
		new Runtimes(sobekRuntimesForecastBat);
		long endTime = System.currentTimeMillis();

		// output the property file
		JsonObject readJson = new AtFileReader(GlobalProperty.workSpace + GlobalProperty.propertyFileName)
				.getJsonObject();
		readJson.addProperty(this.jsonKeyName, endTime - startTime);

		// copy the result file to folder
		ff.copyFolder(this.sobekResultFolder, this.saveFolder);
	}

}
