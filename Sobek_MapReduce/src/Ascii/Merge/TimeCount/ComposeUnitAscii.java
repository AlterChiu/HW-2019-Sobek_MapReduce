package Ascii.Merge.TimeCount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiMerge;
import usualTool.AtFileReader;

public class ComposeUnitAscii {


	// merge model setting
	// ===================================================
	public ComposeUnitAscii setStraightMerge()
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		compose.setDelicateFolder(GlobalProperty.splitDelicateSaveFolder_Straight);
		compose.setDelicateAnalysisProperty(GlobalProperty.splitAnalysis_Straight_Delicate);

		return this;
	}

	public ComposeUnitAscii setHorizontalMerge()
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		compose.setDelicateFolder(GlobalProperty.splitDelicateSaveFolder_Horizontal);
		compose.setRoughFolder(GlobalProperty.splitRoughSaveFolder_Horizontal);

		compose.setDelicateAnalysisProperty(GlobalProperty.splitAnalysis_Horizontal_Delicate);
		compose.setRoughAnalysisProperty(GlobalProperty.splitAnalysis_Horizontal_Rough);

		
		return this;
	}

	private void RunTimes() {
		

	}

	// ===========================================================

	private void timeCount() throws IOException {
		double delicateTotalTime = compose.getDelicateAnalysisProperty().get("originalTotal").getAsDouble();
		int mergeUnitSize = new BigDecimal(delicateTotalTime / GlobalProperty.splitTime).intValue() + 1;
		double splitUnitTime = compose.getRoughAnalysisProperty().get("splitSum").getAsDouble() / mergeUnitSize;

		ArrayList<Integer[]> mergeZoneLimit = getDelicateMergeZoneLimit(splitUnitTime);
		for (Integer[] delicateZoneLimit : mergeZoneLimit) {
			InitailizeDem initailize = new InitailizeDem(compose);
			
			String[][] delicateInitailDem = initailize.getDelicateInitial(delicateZoneLimit[0], delicateZoneLimit[1]);
			double delicateInitialTime = initailize.getDelicateTime(delicateZoneLimit[0], delicateZoneLimit[1]);
			
			String[][] roughInitailDem = initailize.getRoughInitail(delicateZoneLimit[0], delicateZoneLimit[1]);
			double roughInitialTime = initailize.getRoughTime(delicateZoneLimit[0], delicateZoneLimit[1]);
			
		}

	}

	// 					Get the index of start and end in Merge zone (Delicate)
	//===================================================================
	private ArrayList<Integer[]> getDelicateMergeZoneLimit(double splitUnitTime) {
		double temptUnitTime = 0;
		int startIndex = 0;
		ArrayList<Integer[]> mergeZone = new ArrayList<Integer[]>();
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			temptUnitTime = temptUnitTime + compose.getDelicateAnalysisProperty().get(index + "").getAsDouble();

			if (temptUnitTime > splitUnitTime) {
				temptUnitTime = 0;
				mergeZone.add(new Integer[] { startIndex, index });
				startIndex = index + 1;
			}

			if (index == GlobalProperty.splitSize - 1) {
				mergeZone.add(new Integer[] { startIndex, index });
			}
		}
		return mergeZone;
	}

	
	//													Get the index of start and end in Merge zone (Rough)
	//=======================================================================
	private ArrayList<Integer[]> getBufferLimit(int start, int end, double spendTime) {
		BufferDem buffer = new BufferDem(compose);
		
		TreeMap<String, String> startBuffer = buffer.getStartBuffer(start, spendTime);
		TreeMap<String, String> endBuffer = buffer.getEndBuffer(end, spendTime);

		if (Double.parseDouble(startBuffer.get("spendTime")) < GlobalProperty.totalAllowTime
				&& Integer.parseInt(startBuffer.get("bufferLimit")) == 0) {
			startBuffer = buffer.getEndBuffer(end, Double.parseDouble(startBuffer.get("spendTime")));
		}
		if (Double.parseDouble(endBuffer.get("spendTime")) < GlobalProperty.totalAllowTime
				&& Integer.parseInt(endBuffer.get("bufferLimit")) == GlobalProperty.splitSize - 1) {
			endBuffer = buffer.getEndBuffer(end, Double.parseDouble(endBuffer.get("spendTime")));
		}
		
		
	}

	

}