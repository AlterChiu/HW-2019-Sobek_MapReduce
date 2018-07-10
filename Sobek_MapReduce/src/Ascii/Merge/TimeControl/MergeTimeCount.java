package Ascii.Merge.TimeControl;

import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;
import SOBEK.Runtimes;

public class MergeTimeCount {
	private String workFolder;
	private String resultSaveFolder;
	private FileFunction ff = new FileFunction();
	private String jsonKeyName;

	public void setHorizontalTimeCount() throws IOException {
		this.workFolder = GlobalProperty.splitSaveFolder_Horizontal;
		this.jsonKeyName = GlobalProperty.horizontalMerge;
		this.resultSaveFolder = GlobalProperty.mergeSaveFolder_Horizontal;
		Runtimes();
	}

	public void setStraightTimeCount() throws IOException {
		this.workFolder = GlobalProperty.splitSaveFolder_Straight;
		this.jsonKeyName = GlobalProperty.straightMerge;
		this.resultSaveFolder = GlobalProperty.mergeSaveFolder_Straight;
		Runtimes();
	}

	private void Runtimes() throws IOException {
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			// move merge dem to the sobek runtimes folder
			ff.copyFile(this.workFolder + index + GlobalProperty.temptDelicateDem, GlobalProperty.sobekDelicateDem);
			ff.copyFile(this.workFolder + index + GlobalProperty.temptDelicateDemKn, GlobalProperty.sobekDelicateDemKn);
			ff.copyFile(this.workFolder + index + GlobalProperty.temptRoughDem, GlobalProperty.sobekRoughDem);
			ff.copyFile(this.workFolder + index + GlobalProperty.temptRoughDemKn, GlobalProperty.sobekRoughDemKn);

			// it should be save manual in sobekModel , after saving then the program will
			// continue
			if (index == 0) {
				Scanner scan = new Scanner(System.in);
				System.out.println("Please Check The Sobek Model and Save the new Dem");
				System.out.println("if already done, please enter the 'update'");
				while (!scan.next().equals("update")) {
					System.out.println("error input");
				}
				scan.close();
			}

			// start the sobek model
			double startTime = System.currentTimeMillis();
			new Runtimes(GlobalProperty.sobekRuntimesForecastBar_Merge);
			double endTime = System.currentTimeMillis();

			// get the result back to the merge folder
			ff.copyFolder(GlobalProperty.sobekResultFolder, this.resultSaveFolder);
			
			// set the time count to the overview analysis property file
			JsonObject json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
			json.get(this.jsonKeyName).getAsJsonObject().addProperty(index + "", endTime - startTime);
			new AtFileWriter(json , GlobalProperty.overViewPropertyFile).textWriter("");
		}
	}

}
