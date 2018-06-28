package Ascii.Merge.TimeCount;

import java.io.IOException;

import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiMerge;

public class InitailizeDem {
	
	private String delicateFolder = null;
	private String roughFolder = null;

	private JsonObject delicateAnalysisProperty = null;
	private JsonObject roughAnalysisProperty = null;
	
	public InitailizeDem(ComposeProperty compose) {
		this.delicateFolder = compose.getDelicateFolder();
		this.roughFolder = compose.getRoughFolder();
		this.delicateAnalysisProperty = compose.getDelicateAnalysisProperty();
		this.roughAnalysisProperty = compose.getRoughAnalysisProperty();
	}
	
	// ================================================================
		// ========================== INITIALIZATION===========================
		// =================================================================

		// <=================>
		// <-----------DEM------------>
		// <=================>

		// get the delicate initial dem
		// =======================================================
		public String[][] getDelicateInitial(int start, int end) throws IOException {
			String[][] tempt = new AsciiBasicControl(
					this.delicateFolder + "\\" + start + "\\" + GlobalProperty.sobekResultPropertyFileName).getAsciiGrid();
			for (int index = start + 1; index <= end; index++) {
				tempt = new AsciiMerge(tempt,
						this.delicateFolder + "\\" + index + "\\" + GlobalProperty.sobekResultPropertyFileName)
								.getMergedAscii();
			}
			return tempt;
		}

		// get the rough initial dem
		// ========================================================
		public String[][] getRoughInitail(int delicateStart, int delicateEnd) throws IOException {
			// from begin
			// ===================================================
			String[][] startDem = null;
			if (delicateStart == 0) {
				startDem = new AsciiBasicControl(GlobalProperty.originalRoughNull).getAsciiFile();
			} else {
				String firstRoughDemAdd = this.roughFolder + "\\" + (delicateStart - 1) + "\\"
						+ GlobalProperty.splitDemTempSaveName;
				String secondRoughDemAdd = this.roughFolder + "\\" + delicateStart + "\\"
						+ GlobalProperty.splitDemTempSaveName;

				startDem = new AsciiMerge(firstRoughDemAdd, secondRoughDemAdd).getMergedAscii();
			}

			// from end
			// ===================================================
			String[][] endDem = null;
			if (delicateEnd == GlobalProperty.splitSize - 1) {
				endDem = new AsciiBasicControl(GlobalProperty.originalRoughNull).getAsciiFile();
			} else {
				String firstRoughDemAdd = this.roughFolder + "\\" + (delicateEnd - 1) + "\\"
						+ GlobalProperty.splitDemTempSaveName;
				String secondRoughDemAdd = this.roughFolder + "\\" + delicateEnd + "\\"
						+ GlobalProperty.splitDemTempSaveName;
				String thirdRoughDemAdd = this.roughFolder + "\\" + (delicateEnd + 1) + "\\"
						+ GlobalProperty.splitDemTempSaveName;

				String temptAscii[][] = new AsciiMerge(firstRoughDemAdd, secondRoughDemAdd).getMergedAscii();
				endDem = new AsciiMerge(temptAscii, thirdRoughDemAdd).getMergedAscii();
			}
			return new AsciiMerge(startDem, endDem).getMergedAscii();
		}

		// <=================>
		// <-----------TIME------------>
		// <=================>

		// ============================================================
		public double getRoughTime(int delicateStart, int delicateEnd) {

			// end rough spend time
			// ===================================================
			double startSum = 0.0;
			if (delicateStart == 0) {
				startSum = 0;
			} else {
				startSum = startSum + this.roughAnalysisProperty.get((delicateStart - 1) + "").getAsDouble();
				startSum = startSum + this.delicateAnalysisProperty.get(delicateStart + "").getAsDouble();
			}

			// start rough spend time
			// ===================================================
			double endSum = 0.0;
			if (delicateEnd == GlobalProperty.splitSize - 1) {
				endSum = 0;
			} else {
				endSum = endSum + this.roughAnalysisProperty.get((delicateEnd - 1) + "").getAsDouble();
				endSum = endSum + this.delicateAnalysisProperty.get(delicateEnd + "").getAsDouble();
				endSum = endSum + this.roughAnalysisProperty.get((delicateEnd + 1) + "").getAsDouble();
			}

			return endSum + startSum;
		}

		double getDelicateTime(int start, int end) throws IOException {
			double sum = 0.0;
			for (int index = start; index <= end; index++) {
				sum = sum + this.delicateAnalysisProperty.get(index + "").getAsDouble();
			}
			return sum;
		}

}
