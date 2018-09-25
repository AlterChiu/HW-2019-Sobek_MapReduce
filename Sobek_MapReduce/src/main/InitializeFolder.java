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
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class InitializeFolder {
	private FileFunction ff = new FileFunction();

	/**
	 * 
	 * 
	 * 
	 * 
	 */
	public void resetWorkSpace() {
		try {
			ff.copyFile(GlobalProperty.saveFile_SobekFriction, GlobalProperty.caseFrictionDescription);
			ff.copyFile(GlobalProperty.saveFile_SobekNetWorkD12, GlobalProperty.caseNetWork_D12);
			ff.copyFile(GlobalProperty.saveFile_SobekNetWorkNtw, GlobalProperty.caseNetWork_NTW);
			ff.copyFile(GlobalProperty.saveFile_SobekNodes, GlobalProperty.caseNodeDescription);
		} catch (Exception e) {

		}
		ff.delete(GlobalProperty.saveFolder_Total);
		ff.delete(GlobalProperty.saveFolder_Analysis);
		ff.delete(GlobalProperty.saveFolder_Split);
		ff.delete(GlobalProperty.saveFolder_Merge);
		ff.delete(GlobalProperty.saveFolder_Sobek);
		ff.delete(GlobalProperty.saveFolder_convergence);
		ff.delete(GlobalProperty.overViewPropertyFile);
	}

	/**
	 * 
	 * 
	 * @throws IOException
	 */
	public void createBeforeTotalRun() throws IOException {
		// =============Analysis Property===============
		if (!new File(GlobalProperty.workSpace + GlobalProperty.propertyFileName).exists()) {
			JsonObject outJson = new JsonObject();
			outJson.addProperty(GlobalProperty.overviewProperty_SpendTime_delicateTotal, 0);
			outJson.addProperty(GlobalProperty.overviewProperty_SpendTime_roughTotal, 0);

			new AtFileWriter(outJson, GlobalProperty.overViewPropertyFile).textWriter("");
		}

		// ==================total===================
		ff.newFolder(GlobalProperty.saveFolder_Total);
		ff.newFolder(GlobalProperty.saveFolder_Total_Delicate);
		ff.newFolder(GlobalProperty.saveFolder_Total_Rough);

		// =============== Sobek Model ================
		ff.newFolder(GlobalProperty.saveFolder_Sobek);
		ff.copyFile(GlobalProperty.caseFrictionDescription, GlobalProperty.saveFile_SobekFriction);
		ff.copyFile(GlobalProperty.caseNetWork_D12, GlobalProperty.saveFile_SobekNetWorkD12);
		ff.copyFile(GlobalProperty.caseNetWork_NTW, GlobalProperty.saveFile_SobekNetWorkNtw);
		ff.copyFile(GlobalProperty.caseNodeDescription, GlobalProperty.saveFile_SobekNodes);

		// =============== Merge Save Folder =============
		ff.newFolder(GlobalProperty.saveFolder_Merge);

		// =============== Analysis Folder ===============
		ff.newFolder(GlobalProperty.saveFolder_Analysis);
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @throws IOException
	 */
	public void createAfterTotalRun() throws IOException {
		ff.delete(GlobalProperty.saveFolder_Split);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// ====================Split==================
		ff.newFolder(GlobalProperty.saveFolder_Split);
		for (int i = 0; i < GlobalProperty.splitSize; i++) {
			ff.newFolder(GlobalProperty.saveFolder_Split + i);
		}

		// clear the overview property
		JsonObject json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		for (int index = 0; index < index + 1; index++) {
			try {
				if (!json.get(GlobalProperty.overviewProperty_Split + index).isJsonNull()) {
					json.remove(GlobalProperty.overviewProperty_Split + index);
				}
			} catch (Exception e) {
				break;
			}
		}
		new AtFileWriter(json, GlobalProperty.overViewPropertyFile).textWriter("");
	}

	public void setSplitSize() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject object = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		double totalTimeCount = object.get(GlobalProperty.overviewProperty_SpendTime_delicateTotal).getAsDouble();
		GlobalProperty.splitSize = new BigDecimal(totalTimeCount / GlobalProperty.splitTime)
				.setScale(0, BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * 
	 * 
	 * 
	 * 
	 */
	public void createAfterSplitRun() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject object = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		// clear rough covergence result
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			JsonArray nullArray = new JsonArray();
			object.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
					.add(GlobalProperty.overviewProperty_SplitRoughBoundary, nullArray);
		}

		// clear the buffer coefficient for each classified
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			JsonObject temptObject = object.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
					.get(GlobalProperty.overviewProperty_SplitDelicateBoundary).getAsJsonObject();
			temptObject.remove(GlobalProperty.overviewProperty_BufferCoefficient_Difference);
			temptObject.remove(GlobalProperty.overviewProperty_BufferCoefficient_Max);
			temptObject.remove(GlobalProperty.overviewProperty_BufferCoefficient_Min);
		}

		new AtFileWriter(object, GlobalProperty.overViewPropertyFile).textWriter("");

		ff.delete(GlobalProperty.saveFolder_convergence);
		ff.newFolder(GlobalProperty.saveFolder_convergence);
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			ff.newFolder(GlobalProperty.saveFolder_convergence + index);
		}
	}

	// <==================================================================>
	/**
	 * 
	 * create for the SobekModel
	 * 
	 * 
	 * @throws IOException
	 */
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
		new AtFileWriter(pt12List.parallelStream().toArray(String[][]::new),
				GlobalProperty.saveFile_SobekNetWorkD12_Pt2).textWriter(" ");
	}

}
