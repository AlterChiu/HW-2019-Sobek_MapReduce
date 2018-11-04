package Ascii.TimeControl;

import java.io.IOException;
import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;

public class TotalTimeCount {
	private String saveFolder;
	private String jsonKeyName;

	public void RoughTotalTimeCount() throws IOException, InterruptedException {
		this.saveFolder = GlobalProperty.saveFolder_Total_Rough;
		this.jsonKeyName = GlobalProperty.overviewProperty_SpendTime_roughTotal;

		SobekDem sobekDem = new SobekDem();
		sobekDem.addRoughDem(GlobalProperty.originalRough, GlobalProperty.originalRoughKn);
		sobekDem.start();
		if (GlobalProperty.nodeFunction_RoughTotal) {
			sobekDem.setRoughNode();
		}

		startRuntimes();
	}

	public void DelicateTotalTimeCount() throws IOException, InterruptedException {
		this.saveFolder = GlobalProperty.saveFolder_Total_Delicate;
		this.jsonKeyName = GlobalProperty.overviewProperty_SpendTime_delicateTotal;

		SobekDem sobekDem = new SobekDem();
		sobekDem.addDelicateDem(GlobalProperty.originalDelicate, GlobalProperty.originalDelicateKn);
		sobekDem.start();
		if (GlobalProperty.nodeFunction_DelicateTotal) {
			sobekDem.setDelicateNode();
		}

		startRuntimes();
	}

	private void startRuntimes() throws IOException, InterruptedException {
		// run sobek model
		Runtimes sobekRuntimes = new Runtimes();
		sobekRuntimes.RuntimesNoLimit();
		System.out.println(sobekRuntimes.getSimulateTime());

		// output the property file
		JsonObject readJson = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		readJson.addProperty(this.jsonKeyName, sobekRuntimes.getSimulateTime());
		new AtFileWriter(readJson, GlobalProperty.overViewPropertyFile).textWriter("");

		// copy the result file to folder
		sobekRuntimes.moveResult(this.saveFolder);
	}

}
