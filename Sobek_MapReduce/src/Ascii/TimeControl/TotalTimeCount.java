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
	private String saveFolder;
	private String jsonKeyName;

	public void RoughTotalTimeCount() throws IOException {
		this.saveFolder = GlobalProperty.saveFolder_Total_Rough;
		this.jsonKeyName = GlobalProperty.overviewProperty_roughTotal;
		
		SobekDem sobekDem = new SobekDem();
		sobekDem.addRoughDem(GlobalProperty.originalRough, GlobalProperty.originalRoughKn);
		sobekDem.start();
		sobekDem.setNode();

		startRuntimes();
	}

	public void DelicateTotalTimeCount() throws IOException {
		this.saveFolder = GlobalProperty.saveFolder_Total_Delicate;
		this.jsonKeyName = GlobalProperty.overviewProperty_delicateTotal;
		
		SobekDem sobekDem = new SobekDem();
		sobekDem.addDelicateDem(GlobalProperty.originalDelicate, GlobalProperty.originalDelicateKn);
		sobekDem.start();

		startRuntimes();
	}

	private void startRuntimes() throws IOException {
		// run sobek model
		Runtimes sobekRuntimes = new Runtimes();
		System.out.println(sobekRuntimes.getSimulateTime());

		// output the property file
		JsonObject readJson = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		readJson.addProperty(this.jsonKeyName, sobekRuntimes.getSimulateTime());
		new AtFileWriter(readJson, GlobalProperty.overViewPropertyFile).textWriter("");

		// copy the result file to folder
		String[] outPutList = new File(GlobalProperty.sobekResultFolder).list();
		FileFunction ff = new FileFunction();
		for (String result : outPutList) {
			if (result.contains(".asc")) {
				ff.moveFile(GlobalProperty.sobekResultFolder + result, this.saveFolder + result);
			}
		}
	}

}
