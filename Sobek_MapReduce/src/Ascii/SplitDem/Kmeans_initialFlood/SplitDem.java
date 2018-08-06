package Ascii.SplitDem.Kmeans_initialFlood;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import usualTool.AtFileReader;

public class SplitDem {
	private AsciiBasicControl asciiDem;
	private JsonObject overviewProperty;

	public SplitDem(String asciiDem) throws IOException {
		this.asciiDem = new AsciiBasicControl(asciiDem);
		this.overviewProperty = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
	}

	private Map<String, Double> getBufferBoundary(AsciiBasicControl splitAscii, double restTime) throws IOException {
		Map<String, String> originalAsciiProperty = new AsciiBasicControl(GlobalProperty.originalRough).getProperty();

		double originalMaxX = Double.parseDouble(originalAsciiProperty.get("topX"));
		double originalMaxY = Double.parseDouble(originalAsciiProperty.get("topY"));
		double originalMinX = Double.parseDouble(originalAsciiProperty.get("bottomX"));
		double originalMinY = Double.parseDouble(originalAsciiProperty.get("bottomY"));
		double originalWidth = originalMaxX - originalMinX;
		double originalHeight = originalMaxY - originalMinY;

		// get the ratio of spend time between delicate one and rough one
		// rough / delicate (spend time)
		double ratio = restTime / overviewProperty.get(GlobalProperty.overviewProperty_roughTotal).getAsDouble();

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

		Map<String, Double> outBoundary = new TreeMap<String, Double>();
		outBoundary.put("minX", boundaryMinX);
		outBoundary.put("minY", boundaryMinY);
		outBoundary.put("maxX", boundaryMaxX);
		outBoundary.put("maxY", boundaryMaxY);

		return outBoundary;
	}

}
