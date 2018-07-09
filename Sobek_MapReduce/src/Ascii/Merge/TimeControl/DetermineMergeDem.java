package Ascii.Merge.TimeControl;

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

public class DetermineMergeDem {
	private JsonObject property;
	private String workFolder;
	private String splitType;
	private ArrayList<String> splitTimeSpend = new ArrayList<String>();

	private double boundaryMinX;
	private double boundaryMaxX;
	private double boundaryMinY;
	private double boundaryMaxY;

	public DetermineMergeDem() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		this.property = new AtFileReader(GlobalProperty.workSpace + GlobalProperty.propertyFileName).getJsonObject();
	}

	public void setStraighSplit() throws IOException {
		this.workFolder = GlobalProperty.splitSaveFolder_Straight;
		this.splitType = GlobalProperty.straightSplit;

		// determine the section by the maximum time
		determineSection();
	}

	public void setHorizontalSplit() throws IOException {
		this.workFolder = GlobalProperty.splitSaveFolder_Horizontal;
		this.splitType = GlobalProperty.horizontalSplit;

		// determine the section by the maximum time
		determineSection();
	}

	private void determineSection() throws IOException {
		// read the split time spend
		property.get(splitType).getAsJsonArray().forEach(e -> splitTimeSpend.add(e.getAsString()));

		// if summary time bigger than the limit or it's the end
		for (int index = 0; index < splitTimeSpend.size(); index++) {
			double restTime = GlobalProperty.totalAllowTime - Double.parseDouble(splitTimeSpend.get(index));
			AsciiBasicControl delicateAscii = new AsciiBasicControl(
					this.workFolder + index + GlobalProperty.temptDelicateDem);
			getBufferBoundary(delicateAscii, restTime);

			// get the rough dem by giving boundary
			AsciiIntercept interceptRough = new AsciiIntercept(GlobalProperty.originalRough);
			AsciiIntercept interceptRoughKn = new AsciiIntercept(GlobalProperty.originalRoughKn);
			new AtFileWriter(
					new AsciiMerge(interceptRough.getIntercept(boundaryMinX, boundaryMaxX, boundaryMinY, boundaryMaxY),
							GlobalProperty.originalRoughNull).getMergedAscii(),
					this.workFolder + index + GlobalProperty.temptRoughDem);
			new AtFileWriter(
					new AsciiMerge(
							interceptRoughKn.getIntercept(boundaryMinX, boundaryMaxX, boundaryMinY, boundaryMaxY),
							GlobalProperty.originalRoughNull).getMergedAscii(),
					this.workFolder + index + GlobalProperty.temptRoughDemKn);
		}

	}

	private void getBufferBoundary(AsciiBasicControl splitAscii, double restTime) throws IOException {
		Map<String, String> originalAsciiProperty = new AsciiBasicControl(GlobalProperty.originalRough).getProperty();
		double originalWidth = Double.parseDouble(originalAsciiProperty.get("column"))
				* Integer.parseInt(originalAsciiProperty.get("cellsize"));
		double originalHeight = Double.parseDouble(originalAsciiProperty.get("row"))
				* Integer.parseInt(originalAsciiProperty.get("cellsize"));
		double originalMaxX = Double.parseDouble(originalAsciiProperty.get("topX"));
		double originalMaxY = Double.parseDouble(originalAsciiProperty.get("topY"));
		double originalMinX = Double.parseDouble(originalAsciiProperty.get("bottomX"));
		double originalMinY = Double.parseDouble(originalAsciiProperty.get("bottomY"));

		// get the ratio of spend time between delicate one and rough one
		// rough / delicate (spend time)
		double ratio = restTime / property.get(GlobalProperty.roughTotal).getAsDouble();

		// get the original boundary
		Map<String, String> asciiProperty = splitAscii.getProperty();
		double splitMinX = Double.parseDouble(asciiProperty.get("bottomX"));
		double splitMaxX = Double.parseDouble(asciiProperty.get("topX"));
		double splitMaxY = Double.parseDouble(asciiProperty.get("topY"));
		double splitMinY = Double.parseDouble(asciiProperty.get("bottomY"));

		// get the length should be extend
		double bufferWidth = originalWidth * ratio;
		double bufferHeight = originalHeight * ratio;

		// get the output boundary
		this.boundaryMinX = splitMinX - bufferWidth * 0.5;
		this.boundaryMaxX = splitMaxX + bufferWidth * 0.5;
		this.boundaryMinY = splitMinY - bufferHeight * 0.5;
		this.boundaryMaxY = splitMaxY + bufferHeight * 0.5;

		// revise the output boundary, to center the original total ascii
		if (boundaryMinX < originalMinX) {
			boundaryMinX = originalMinX;
			boundaryMaxX = boundaryMaxX + originalMinX - boundaryMinX;
		}
		if (boundaryMinY < originalMinY) {
			boundaryMinY = originalMinY;
			boundaryMaxY = boundaryMaxY + originalMinY - boundaryMinY;
		}
		if (boundaryMaxX > originalMaxX) {
			boundaryMaxX = originalMaxX;
			boundaryMinX = boundaryMinX + originalMaxX - boundaryMinX;
		}
		if (boundaryMaxY > originalMaxY) {
			boundaryMaxY = originalMaxY;
			boundaryMinY = boundaryMinY + originalMaxY - boundaryMaxY;
		}

	}

}
