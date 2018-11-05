package Analysis.Result.FloodCompare;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import GlobalProperty.GlobalProperty;

import asciiFunction.AsciiBasicControl;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;

public class ResultCompare {
	private List<AsciiBasicControl> originalAsciiList = new ArrayList<AsciiBasicControl>();
	private List<AsciiBasicControl> compareAsciiList = new ArrayList<AsciiBasicControl>();
	private List<Double> timesAnalysis = new ArrayList<Double>();
	private List<Double> valueAnalysis = new ArrayList<Double>();

	public ResultCompare(String compareFolder) {
		for (int index = 0; index < index + 1; index++) {
			try {
				String fileName = "dm1d" + String.format("%04d", index) + ".asc";
				if (new File(GlobalProperty.saveFolder_Total_Delicate + fileName).exists()) {
					originalAsciiList.add(new AsciiBasicControl(GlobalProperty.saveFolder_Total_Delicate + fileName));
					compareAsciiList.add(new AsciiBasicControl(compareFolder + fileName));
				} else {
					break;
				}
			} catch (Exception e) {
				break;
			}
		}
		comparision();
	}

	// <===========>
	// <get the analysis>
	// <================================================>
	public List<Double> getTimesDifference() {
		return this.timesAnalysis;
	}

	public List<Double> getValueDifference() {
		return this.valueAnalysis;
	}

	public double getTotalTimesDifference() {
		return new AtCommonMath(this.timesAnalysis).getSum();
	}

	public double getTotalValueDifference() {
		return new AtCommonMath(this.valueAnalysis).getSum();
	}

	public double getMeanTimesDifference() {
		return new AtCommonMath(this.timesAnalysis).getMean();
	}

	public double getMeanValueDifference() {
		return new AtCommonMath(this.valueAnalysis).getMean();
	}

	// <================================================>
	// this function will output the asciiFile of difference
	// original - simulation
	public void outputDifferenceAsciiSeries() throws IOException {
		String saveFolder = GlobalProperty.saveFolder_Analysis;
		for (int index = 0; index < this.originalAsciiList.size(); index++) {
			String fileName = "dm1d" + String.format("%04d", index) + ".asc";

			AsciiBasicControl simulationAscii = this.compareAsciiList.get(index);
			String simulationContent[][] = simulationAscii.getAsciiGrid();
			String simulationNull = simulationAscii.getProperty().get("noData");

			AsciiBasicControl originalAscii = this.originalAsciiList.get(index);

			for (int row = 0; row < simulationContent.length; row++) {
				for (int column = 0; column < simulationContent[0].length; column++) {
					String simulationValue = simulationContent[row][column];
					if (!simulationValue.equals(simulationNull)) {
						double[] coordinate = simulationAscii.getCoordinate(column, row);

						double temptSimulationValue = Double.parseDouble(simulationValue);
						double temptOriginalValue = Double
								.parseDouble(originalAscii.getValue(coordinate[0], coordinate[1]));
						simulationAscii.setValue(column, row, new BigDecimal(temptOriginalValue - temptSimulationValue)
								.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
					}
				}
			}
			new AtFileWriter(simulationAscii.getAsciiFile(), saveFolder + fileName).textWriter(" ");
		}
	}

	// <================================================>
	// < calcuate the difference between the original one and the simulation one >
	// <================================================>
	private void comparision() {
		for (int index = 0; index < this.originalAsciiList.size(); index++) {
			String simulationContent[][] = this.compareAsciiList.get(index).getAsciiGrid();
			String simulationNull = this.compareAsciiList.get(index).getProperty().get("noData");

			AsciiBasicControl originalAscii = this.originalAsciiList.get(index);
			int matchTimes = 0;
			int totalTimes = 0;
			double errorDepth = 0;

			for (int row = 0; row < simulationContent.length; row++) {
				for (int column = 0; column < simulationContent[0].length; column++) {
					String simulationValue = simulationContent[row][column];
					if (!simulationValue.equals(simulationNull)) {
						double[] coordinate = this.compareAsciiList.get(index).getCoordinate(column, row);

						double temptSimulationValue = Double.parseDouble(simulationValue);
						double temptOriginalValue = Double
								.parseDouble(originalAscii.getValue(coordinate[0], coordinate[1]));

						// calculate the times
						if (temptSimulationValue > 0 && temptOriginalValue > 0) {
							matchTimes++;
							totalTimes++;
						} else if (temptSimulationValue > 0 && temptOriginalValue < 0.0001) {
							totalTimes++;
						} else if (temptSimulationValue < 0.0001 && temptOriginalValue > 0) {
							totalTimes++;
						} else if (temptSimulationValue < 0.0001 && temptOriginalValue < 0.0001) {
							matchTimes++;
							totalTimes++;
						}

						// calculate the value
						errorDepth = errorDepth + Math.abs(temptSimulationValue - temptOriginalValue);
					}
				}
			}

			this.timesAnalysis.add((double) matchTimes / (double) totalTimes);
			this.valueAnalysis.add(errorDepth / totalTimes);
		}
	}
}
