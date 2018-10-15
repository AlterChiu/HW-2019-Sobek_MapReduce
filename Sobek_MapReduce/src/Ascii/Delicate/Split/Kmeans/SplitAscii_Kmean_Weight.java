package Ascii.Delicate.Split.Kmeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import usualTool.AtKmeans;

public class SplitAscii_Kmean_Weight extends SplitAscii_Kmeas {

	public SplitAscii_Kmean_Weight() throws IOException {

	}

	@Override
	protected List<List<Double[]>> getKmeansClassified(int floodInitialTimes) throws IOException {
		AsciiBasicControl ascii = new AsciiBasicControl(GlobalProperty.saveFile_Analysis_InitailFlood);
		List<Double[]> analysisData = new ArrayList<Double[]>();
		System.out.println("dddde");

		// read the ascii content which value is under limit
		String[][] content = ascii.getAsciiGrid();
		for (int row = 0; row < content.length; row++) {
			for (int column = 0; column < content[0].length; column++) {
				double value = Double.parseDouble(ascii.getValue(column, row));
				if (value <= floodInitialTimes && value >= 0) {

					for (int repeat = 0; repeat < (value + 1); repeat++) {
						double[] coordinate = ascii.getCoordinate(column, row);
						analysisData.add(new Double[] { coordinate[0], coordinate[1] });
					}
				}
			}
		}
		AtKmeans kmeans = new AtKmeans(analysisData, GlobalProperty.splitSize);

		return kmeans.getClassifier();
	}

}
