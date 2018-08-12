package Ascii.TimeControl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class TotalTimeCount {

	private String sobekResultFolder = GlobalProperty.sobekResultFolder;
	private String saveFolder;

	private String jsonKeyName;

	private String demFile;
	private String knFile;

	public void RoughTotalTimeCount() throws IOException {
		this.saveFolder = GlobalProperty.saveFolder_Total_Rough;

		this.jsonKeyName = GlobalProperty.overviewProperty_roughTotal;
		this.demFile = GlobalProperty.originalRough;
		this.knFile = GlobalProperty.originalRoughKn;
		startRuntimes();
	}

	public void DelicateTotalTimeCount() throws IOException {
		this.saveFolder = GlobalProperty.saveFolder_Total_Delicate;

		this.jsonKeyName = GlobalProperty.overviewProperty_delicateTotal;
		this.demFile = GlobalProperty.originalDelicate;
		this.knFile = GlobalProperty.originalDelicateKn;
		startRuntimes();
	}

	private void startRuntimes() throws IOException {
		// copy file from the original folder to sobek operation folder
		FileFunction ff = new FileFunction();
		SobekDem sobekDem = new SobekDem();
		sobekDem.addNewDem(demFile, knFile);
		sobekDem.start();

		// run sobek model
		Runtimes sobekRuntimes = new Runtimes();
		System.out.println(sobekRuntimes.getSimulateTime());

		// output the property file
		JsonObject readJson = new AtFileReader(GlobalProperty.workSpace + GlobalProperty.propertyFileName)
				.getJsonObject();
		readJson.addProperty(this.jsonKeyName, sobekRuntimes.getSimulateTime());
		new AtFileWriter(readJson, GlobalProperty.workSpace + GlobalProperty.propertyFileName).textWriter("");

		// copy the result file to folder
		ff.copyFolder(this.sobekResultFolder, this.saveFolder);
	}

}
