package Ascii.Rough.Determine;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;

public class DeterminRoughAsciiFile {
	private JsonObject overviewPorperty;

	// <==================================================================>
	// < Create DemFile Boundary>
	// <==================================================================>
	// determine the unitDem , roughDem is defined by the delicateDem
	public void determinRoughAsciiFile(String targetFolder, double restTimeCoefficient, int index) throws IOException {
		this.overviewPorperty = new AtFileReader(GlobalProperty.overViewPropertyFile).getJson().getAsJsonObject();
		double restTime = GlobalProperty.totalAllowTime
				- (overviewPorperty.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
						.get(GlobalProperty.overviewProperty_SplitDelicateBoundary).getAsJsonObject()
						.get(GlobalProperty.overviewProperty_SpendTime_Split).getAsDouble() * restTimeCoefficient);

		// determine the rough asciiFile and knFile
		// the boundary of the rough demFile here is calculate by the rest time
		// there is no necessary to point the boundary exact
		Map<String, Double> roughBoundary = getBufferBoundary(
				new AsciiBasicControl(targetFolder + GlobalProperty.saveFile_DelicateDem), restTime);
		AsciiBasicControl roughAscii = new AsciiBasicControl(GlobalProperty.originalRough)
				.getIntersectAscii(roughBoundary);
		AsciiBasicControl roughAsciiKn = new AsciiBasicControl(GlobalProperty.originalRoughKn)
				.getIntersectAscii(roughBoundary);

		if (GlobalProperty.clipFunction_convergence_Rough) {
			roughAscii = setOverlappingNull(targetFolder + GlobalProperty.saveFile_DelicateDem, roughAscii);
			roughAsciiKn = setOverlappingNull(targetFolder + GlobalProperty.saveFile_DelicateDemKn, roughAsciiKn);
		}

		new AtFileWriter(roughAscii.getAsciiFile(), targetFolder + GlobalProperty.saveFile_RoughDem).textWriter(" ");
		new AtFileWriter(roughAsciiKn.getAsciiFile(), targetFolder + GlobalProperty.saveFile_RoughDemKn)
				.textWriter(" ");
	}

	private AsciiBasicControl setOverlappingNull(String delicateAsciiFile, AsciiBasicControl roughAscii)
			throws IOException {
		AsciiBasicControl delicateAscii = new AsciiBasicControl(delicateAsciiFile);
		String roughNullValue = roughAscii.getProperty().get("noData");

		String[][] asciiContent = delicateAscii.getAsciiGrid();
		for (int row = 2; row < asciiContent.length - 2; row++) {
			for (int column = 2; column < asciiContent[0].length - 2; column++) {
				try {
					double[] coordinate = delicateAscii.getCoordinate(column, row);
					roughAscii.setValue(coordinate[0], coordinate[1], roughNullValue);
				} catch (Exception e) {
				}
			}
		}
		return roughAscii;
	}

	// use for determine the rough ascii boundary
	private Map<String, Double> getBufferBoundary(AsciiBasicControl splitAscii, double restTime) throws IOException {
		AsciiBasicControl originalRoughAscii = new AsciiBasicControl(GlobalProperty.originalRough);

		Map<String, String> originalAsciiProperty = originalRoughAscii.getProperty();
		double cellSize = Double.parseDouble(originalRoughAscii.getProperty().get("cellSize"));

		double originalMaxX = Double.parseDouble(originalAsciiProperty.get("topX")) + 0.5 * cellSize;
		double originalMaxY = Double.parseDouble(originalAsciiProperty.get("topY")) + 0.5 * cellSize;
		double originalMinX = Double.parseDouble(originalAsciiProperty.get("bottomX")) - 0.5 * cellSize;
		double originalMinY = Double.parseDouble(originalAsciiProperty.get("bottomY")) - 0.5 * cellSize;
		double originalWidth = originalMaxX - originalMinX;
		double originalHeight = originalMaxY - originalMinY;

		// get the ratio of spend time between delicate one and rough one
		// rough / delicate (spend time)
		double ratio = restTime
				/ overviewPorperty.get(GlobalProperty.overviewProperty_SpendTime_roughTotal).getAsDouble();

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
		double boundaryMinX = splitMinX - bufferWidth * 0.5;
		double boundaryMaxX = splitMaxX + bufferWidth * 0.5;
		double boundaryMinY = splitMinY - bufferHeight * 0.5;
		double boundaryMaxY = splitMaxY + bufferHeight * 0.5;

		// checking the boundary position
		// forX
		if (boundaryMinX < originalMinX) {
			boundaryMaxX = boundaryMaxX + (originalMinX - boundaryMinX);
			boundaryMinX = originalMinX;
		} else if (boundaryMaxX > originalMaxX) {
			boundaryMinX = boundaryMinX - (boundaryMaxX - originalMaxX);
			boundaryMaxX = originalMaxX;
		}
		// forY
		if (boundaryMinY < originalMinY) {
			boundaryMaxY = boundaryMaxY + (originalMinY - boundaryMinY);
			boundaryMinY = originalMinY;
		} else if (boundaryMaxY > originalMaxY) {
			boundaryMinY = boundaryMinY - (boundaryMaxY - originalMaxY);
			boundaryMaxY = originalMaxY;
		}

		Map<String, Double> outBoundary = new TreeMap<String, Double>();
		outBoundary.put("minX", boundaryMinX);
		outBoundary.put("minY", boundaryMinY);
		outBoundary.put("maxX", boundaryMaxX);
		outBoundary.put("maxY", boundaryMaxY);

		return outBoundary;
	}

}
