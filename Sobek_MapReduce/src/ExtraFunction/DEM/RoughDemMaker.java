package ExtraFunction.DEM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiGridChange;
import usualTool.AtFileWriter;
import usualTool.AtListFunction;

public class RoughDemMaker {

	public void setRoughDem(String roughTemplate) throws IOException {
		AsciiBasicControl roughAscii = new AsciiBasicControl(roughTemplate);
		AsciiBasicControl delicateAscii = new AsciiBasicControl(GlobalProperty.originalDelicate);
		double cellSize = Double.parseDouble(delicateAscii.getProperty().get("cellSize"));

		String roughNull = roughAscii.getProperty().get("noData");
		String delicateNull = delicateAscii.getProperty().get("noData");

		// clip the rough demFile by the boundary of the delicate one
		double minX = Double.parseDouble(delicateAscii.getProperty().get("bottomX")) - 0.5 * cellSize;
		double minY = Double.parseDouble(delicateAscii.getProperty().get("bottomY")) - 0.5 * cellSize;
		double maxX = Double.parseDouble(delicateAscii.getProperty().get("topX")) + 0.5 * cellSize;
		double maxY = Double.parseDouble(delicateAscii.getProperty().get("topY")) + 0.5 * cellSize;
		roughAscii = new AsciiBasicControl(roughAscii.getClipAsciiFile(minX, minY, maxX, maxY));

		String roughContent[][] = roughAscii.getAsciiGrid();
		for (int row = 0; row < roughContent.length; row++) {
			for (int column = 0; column < roughContent[0].length; column++) {
				double[] coordinate = roughAscii.getCoordinate(column, row);
				String temptValue = delicateAscii.getValue(coordinate[0], coordinate[1]);

				// if the value in this position is null (delicate one)
				// set the rough one to null
				if (temptValue.equals(delicateNull)) {
					roughAscii.setValue(column, row, roughNull);
				}
			}
		}

		new AtFileWriter(roughAscii.getAsciiFile(), GlobalProperty.originalRough).textWriter("    ");
		setRoughDem();
	}

	public void setRoughDem() throws IOException {
		new AtFileWriter(new AsciiGridChange(GlobalProperty.originalDelicate).getChangedContent(2),
				GlobalProperty.originalRough).textWriter("    ");
		setRoughKn();
	}

	private void setRoughKn() throws IOException {
		AsciiBasicControl delicateKn = new AsciiBasicControl(GlobalProperty.originalDelicateKn);
		AsciiBasicControl roughKn = new AsciiBasicControl(GlobalProperty.originalRough);
		String roughNull = roughKn.getProperty().get("noData");
		String delicateNull = delicateKn.getProperty().get("noData");
		String[][] roughContent = roughKn.getAsciiGrid();

		for (int row = 0; row < roughContent.length; row++) {
			for (int column = 0; column < roughContent[0].length; column++) {
				double[] coordinate = roughKn.getCoordinate(column, row);
				String temptValue = delicateKn.getValue(coordinate[0], coordinate[1]);
				if (!temptValue.equals(delicateNull)) {
					roughKn.setValue(column, row, temptValue);
				} else {
					roughKn.setValue(column, row, roughNull);
				}
			}
		}

		new AtFileWriter(roughKn.getAsciiFile(), GlobalProperty.originalRoughKn).textWriter("    ");
	}
}
