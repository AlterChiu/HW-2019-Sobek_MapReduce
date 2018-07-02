package Ascii.Merge.TimeCount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntercept;
import asciiFunction.AsciiMerge;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class DetectDem {
	private JsonObject property;
	private String splitModel;
	private ArrayList<String> splitTimeSpend = new ArrayList<String>();
	private ArrayList<String[]> selections = new ArrayList<String[]>();
	private String saveFolder;

	private double boundaryMinX;
	private double boundaryMaxX;
	private double boundaryMinY;
	private double boundaryMaxY;

	public DetectDem() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		this.property = new AtFileReader(GlobalProperty.workSpace + GlobalProperty.propertyFileName).getJsonObject();
	}

	public void DetermineSection(String splitModel) throws IOException {
		// determine the splitModel
		this.splitModel = splitModel;

		// determine the section by the maximum time
		determineSection();

		// create the delicate Dem of the sections
		createDelicateDem();

		// create the rough Dem which base on the delicate one
		createRoughDem();
	}

	private void determineSection() {
		// read the split time spend
		property.get(splitModel).getAsJsonArray().forEach(e -> splitTimeSpend.add(e.getAsString()));

		// double time limit
		double timeLimit = GlobalProperty.splitTime;
		double temptTime = 0.;
		ArrayList<String> temptList = new ArrayList<String>();

		// if summary time bigger than the limit or it's the end
		for (int index = 0; index < this.splitTimeSpend.size(); index++) {
			if ((temptTime + Double.parseDouble(splitTimeSpend.get(index))) > timeLimit
					|| index == this.splitTimeSpend.size() - 1) {
				selections.add(temptList.parallelStream().toArray(String[]::new));
				temptTime = 0;
				temptList.clear();
			}
			temptTime = temptTime + Double.parseDouble(splitTimeSpend.get(index));
			temptList.add(splitTimeSpend.get(index));
		}
	}

	private void createDelicateDem() throws IOException {
		String splitDemAdd = GlobalProperty.splitDelicateSaveFolder + this.splitModel + "\\";

		// clean the save folder
		this.saveFolder = GlobalProperty.mergeSaveFolder + this.splitModel + "\\";
		new FileFunction().delFolder(GlobalProperty.mergeSaveFolder + this.splitModel);
		new FileFunction().newFolder(GlobalProperty.mergeSaveFolder + this.splitModel);

		for (int index = 0; index < selections.size(); index++) {

			// get the selections collection
			String[] sectionArray = selections.get(index);

			// initialize the section dem, including the kn
			String[][] outAscii = new AsciiBasicControl(splitDemAdd + sectionArray[0] + GlobalProperty.demTempSaveName)
					.getAsciiFile();
			String[][] outAsciiKn = new AsciiBasicControl(
					splitDemAdd + sectionArray[0] + GlobalProperty.demTempSaveNameKn).getAsciiFile();

			// merge the dem in collection, and output to the merge
			for (int mergeIndex = 1; mergeIndex < sectionArray.length; mergeIndex++) {
				outAscii = new AsciiMerge(outAscii,
						splitDemAdd + sectionArray[mergeIndex] + GlobalProperty.demTempSaveName).getMergedAscii();
				outAsciiKn = new AsciiMerge(outAsciiKn,
						splitDemAdd + sectionArray[mergeIndex] + GlobalProperty.demTempSaveNameKn).getMergedAscii();
			}

			// output the merged ascii file
			new FileFunction().newFolder(saveFolder + index);
			new AtFileWriter(outAscii, saveFolder + index + GlobalProperty.mergeDelicateDem).textWriter("    ");
			new AtFileWriter(outAsciiKn, saveFolder + index + GlobalProperty.mergerDelicateDemKn).textWriter("    ");
		}
	}

	private void createRoughDem() throws IOException {
		// get the list of the merge folder
		String folderList[] = new File(GlobalProperty.mergeSaveFolder).list();
		for (String folder : folderList) {

			// get the revise boundary
			AsciiBasicControl delicateAscii = new AsciiBasicControl(
					GlobalProperty.mergeSaveFolder + folder + GlobalProperty.mergeDelicateDem);
			getBufferBoundary(delicateAscii);

			// write the rough dem to the merge folder
			AsciiIntercept interceptRough = new AsciiIntercept(GlobalProperty.originalRough);
			AsciiIntercept interceptRoughKn = new AsciiIntercept(GlobalProperty.originalRoughKn);
			new AtFileWriter(interceptRough.getIntercept(boundaryMinX, boundaryMaxX, boundaryMinY, boundaryMaxY),
					this.saveFolder + folder + GlobalProperty.demTempSaveName);
			new AtFileWriter(interceptRoughKn.getIntercept(boundaryMinX, boundaryMaxX, boundaryMinY, boundaryMaxY),
					this.saveFolder + folder + GlobalProperty.demTempSaveNameKn);
		}

	}

	private void getBufferBoundary(AsciiBasicControl splitAscii) throws IOException {
		Map<String, String> originalAsciiProperty = new AsciiBasicControl(GlobalProperty.originalDelicate)
				.getProperty();
		double originalMinX = Double.parseDouble(originalAsciiProperty.get("bottomX"));
		double originalMinY = Double.parseDouble(originalAsciiProperty.get("bottomY"));

		// get the ratio of spend time between delicate one and rough one
		// rough / delicate (spend time)
		double ratio = property.get(GlobalProperty.roughTotal).getAsDouble()
				/ property.get(GlobalProperty.delicateTotal).getAsDouble();

		// get the original boundary
		Map<String, String> asciiProperty = splitAscii.getProperty();
		double splitMinX = Double.parseDouble(asciiProperty.get("bottomX"));
		double splitMaxX = Double.parseDouble(asciiProperty.get("topX"));
		double splitMaxY = Double.parseDouble(asciiProperty.get("topY"));
		double splitMinY = Double.parseDouble(asciiProperty.get("bottomY"));

		// get the length should be extend
		double xLength = (splitMaxX - splitMinX) * ratio;
		double yLength = (splitMaxY - splitMinY) * ratio;

		// get the output boundary
		this.boundaryMinX = splitMinX - xLength * 0.5;
		this.boundaryMaxX = splitMaxX + xLength * 0.5;
		this.boundaryMinY = splitMinY - yLength * 0.5;
		this.boundaryMaxY = splitMaxY + yLength * 0.5;

		// revise the output boundary, to center the original total ascii
		if (boundaryMinX < originalMinX) {
			boundaryMinX = originalMinX;
			boundaryMaxX = boundaryMaxX + originalMinX - boundaryMinX;
		}
		if (boundaryMinY < originalMinY) {
			boundaryMinY = originalMinY;
			boundaryMaxY = boundaryMaxY + originalMinY - boundaryMinY;
		}

	}

}
