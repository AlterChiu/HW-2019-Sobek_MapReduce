package Ascii.Split.Kmeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import Ascii.Split.DeterminRoughAsciiFile;
import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntercept;
import main.MapReduceMainPage;
import usualTool.AtCommonMath;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;
import usualTool.AtKmeans;
import usualTool.RandomMaker;

public class SplitAscii_Kmeas extends DeterminRoughAsciiFile {
	private AsciiBasicControl originalDelicateAscii;
	private AsciiBasicControl originalDelicateAsciiKn;
	private List<List<Double[]>> classified;
	public double restTimeCoefficient = GlobalProperty.roughBufferCoefficient;

	public SplitAscii_Kmeas() throws IOException {
		this.originalDelicateAscii = new AsciiBasicControl(GlobalProperty.originalDelicate);
		this.originalDelicateAsciiKn = new AsciiBasicControl(GlobalProperty.originalDelicateKn);

		// reset the split folder
		MapReduceMainPage.initialize.createAfterTotalRun();
		this.classified = getKmeansClassified(GlobalProperty.K_meansInitialTime);

		// create the demFile and knFile
		for (int index = 0; index < classified.size(); index++) {
			String splitFolder = GlobalProperty.saveFolder_Split + index + "\\";
			determinUnitDelicateDem(splitFolder, index);
		}
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private void determinUnitDelicateDem(String splitFodler, int index) throws IOException {
		// determine the delicate asciiFile and knFile
		// the boundary(classifiedBoundary) of the delicate demFile here is the most
		// outer coordinate
		Map<String, Double> classifiedBoundary = getListStatics(this.classified.get(index));
		String[][] delicateAscii = new AsciiIntercept(this.originalDelicateAscii).getIntercept(
				classifiedBoundary.get("minX"), classifiedBoundary.get("maxX"), classifiedBoundary.get("minY"),
				classifiedBoundary.get("maxY"));
		String[][] delicateAsciiKn = new AsciiIntercept(this.originalDelicateAsciiKn).getIntercept(
				classifiedBoundary.get("minX"), classifiedBoundary.get("maxX"), classifiedBoundary.get("minY"),
				classifiedBoundary.get("maxY"));
		new AtFileWriter(delicateAscii, splitFodler + GlobalProperty.saveFile_DelicateDem).textWriter("    ");
		new AtFileWriter(delicateAsciiKn, splitFodler + GlobalProperty.saveFile_DelicateDemKn).textWriter("    ");

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	// <===========================================================>
	// <K-Means function>
	// <============================================================>
	// get the boundary of xyList in K-means
	private Map<String, Double> getListStatics(List<Double[]> staticsList) throws IOException {
		Map<String, Double> outMap = new TreeMap<String, Double>();
		List<Double> xList = new ArrayList<Double>();
		List<Double> yList = new ArrayList<Double>();
		staticsList.forEach(coordinate -> {
			xList.add(coordinate[0]);
			yList.add(coordinate[1]);
		});

		AtCommonMath xListStatics = new AtCommonMath(xList);
		AtCommonMath yListStatics = new AtCommonMath(yList);
		double delicateCellSize = Double
				.parseDouble(new AsciiBasicControl(GlobalProperty.originalDelicate).getProperty().get("cellSize"));

		double minX = xListStatics.getMin() - delicateCellSize;
		double maxX = xListStatics.getMax() + delicateCellSize;
		double minY = yListStatics.getMin() - delicateCellSize;
		double maxY = yListStatics.getMax() + delicateCellSize;

		// make it match the grid of rough asciiDem
		AsciiBasicControl roughAscii = new AsciiBasicControl(GlobalProperty.originalRough);

		double[] bottomCoordinate = roughAscii.getClosestCoordinate(minX, minY);
		double[] topCoordinate = roughAscii.getClosestCoordinate(maxX, maxY);

		outMap.put("minX", bottomCoordinate[0]);
		outMap.put("maxX", topCoordinate[0]);
		outMap.put("minY", bottomCoordinate[1]);
		outMap.put("maxY", topCoordinate[1]);
		return outMap;
	}

	// get the kmeans classified
	private List<List<Double[]>> getKmeansClassified(int floodInitialTimes) throws IOException {
		AsciiBasicControl ascii = new AsciiBasicControl(GlobalProperty.saveFile_Analysis_InitailFlood);
		double minX = Double.parseDouble(ascii.getProperty().get("bottomX"));
		double minY = Double.parseDouble(ascii.getProperty().get("bottomY"));
		double maxX = Double.parseDouble(ascii.getProperty().get("topX"));
		double maxY = Double.parseDouble(ascii.getProperty().get("topY"));
		double maxLength = Math.sqrt(Math.pow(maxX-minX, 2) + Math.pow(maxY-minY, 2));
		
		List<Double[]> analysisData = new ArrayList<Double[]>();

		// read the ascii content which value is under limit
		String[][] content = ascii.getAsciiGrid();
		for (int row = 0; row < content.length; row++) {
			for (int column = 0; column < content[0].length; column++) {
				double value = Double.parseDouble(ascii.getValue(column, row));
				if (value <= floodInitialTimes && value > 0) {
					double[] coordinate = ascii.getCoordinate(column, row);
					analysisData.add(new Double[] { coordinate[0], coordinate[1] });
				}
			}
		}
		AtKmeans kmeans = new AtKmeans(analysisData, GlobalProperty.splitSize);
	
		
		
		return kmeans.getClassifier();
	}
	
//	private List<Double[]> getKmeansStartPoint(List<Double[]> analysisData , double minX , double maxX , double minY , double maxY){
//		List<Double[]> outList = new ArrayList<Double[]>();
//		
//		double maxLength;
//		if(maxX - minX > maxY - minY) {
//			maxLength = maxX - minX;
//		}else {
//			maxLength = maxY - minY;
//		}
//		
//		RandomMaker random = new RandomMaker();
//		for(int index = 0 ; index< GlobalProperty.splitSize ; index++) {
//			int valueIndex = random.RandomInt(0, analysisData.size());
//			outList.add(analysisData.get(valueIndex));
//			
//			for(int checkIndxt =)
//		}
		
	
		
//	}
	// <=================================================================>

}
