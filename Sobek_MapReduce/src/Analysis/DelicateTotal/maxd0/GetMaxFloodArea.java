package Analysis.DelicateTotal.maxd0;

import asciiFunction.AsciiBasicControl;
import usualTool.AtFileWriter;

import java.io.IOException;
import java.util.Map;

import GlobalProperty.GlobalProperty;

public class GetMaxFloodArea {

	public GetMaxFloodArea() throws IOException {
		AsciiBasicControl originalDelicate = new AsciiBasicControl(GlobalProperty.originalDelicate);
		AsciiBasicControl originalRough = new AsciiBasicControl(GlobalProperty.originalRough);
		AsciiBasicControl maxFloodArea = new AsciiBasicControl(GlobalProperty.totalDelicateMaxFloodArea);

		Map<String, String> maxFloodProperty = maxFloodArea.getProperty();
		int totalRow = Integer.parseInt(maxFloodProperty.get("row"));
		int totalColumn = Integer.parseInt(maxFloodProperty.get("column"));
		String nullValue = maxFloodProperty.get("noData");

		for (int row = 0; row < totalRow; row++) {
			for (int column = 0; column < totalColumn; column++) {
				// if the max flood depth is under 5 or is null
				// make the delicate dem value to the rough one
				if (!maxFloodArea.getValue(column, row).equals(nullValue)
						&& Double.parseDouble(maxFloodArea.getValue(column, row)) <= 0.05) {

					// get the coordinate the position
					// and set the delicate value to the rough one;
					double coordinate[] = maxFloodArea.getCoordinate(column, row);
					originalDelicate.setValue(coordinate[0], coordinate[1],
							originalRough.getValue(coordinate[0], coordinate[1]));
				}
			}
		}

		// out put the delicate ascii to the revise Dem folder
		new AtFileWriter(originalDelicate.getAsciiFile(), GlobalProperty.reviceDem_MaxFloodArea).textWriter("    ");
	}
}
