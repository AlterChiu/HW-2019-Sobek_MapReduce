package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asciiFunction.AsciiBasicControl;
import usualTool.AtCommonMath;

public class GetMaxD0 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// String folder =
		// "E:\\HomeWork\\mapReduce\\報告用-別刪\\20180919_Tainan_FEWS_12_6\\Analysis\\";
		// AsciiBasicControl ascii = new AsciiBasicControl(
		// "E:\\HomeWork\\mapReduce\\報告用-別刪\\20180919_Tainan_FEWS_12_6\\Analysis\\dm1d0006.asc");
		// String[][] content = ascii.getAsciiGrid();
		//
		// List<AsciiBasicControl> asciiList = new ArrayList<AsciiBasicControl>();
		// for (int index = 0; index <= 12; index++) {
		// String fileName = "dm1d" + String.format("%04d", index) + ".asc";
		// asciiList.add(new AsciiBasicControl(folder + fileName));
		// }
		//
		// for (int row = 0; row < content.length; row++) {
		// for (int column = 0; column < content[0].length; column++) {
		// if (!content[row][column].equals(ascii.getProperty().get("noData"))) {
		// List<Double> valueList = new ArrayList<Double>();
		// for (AsciiBasicControl temptAscii : asciiList) {
		// valueList.add(Double.parseDouble(temptAscii.getValue(column, row)));
		// }
		// ascii.setValue(column, row, new AtCommonMath(valueList).getMax() + "");
		// }
		// }
		// }
		//
		// new AtFileWriter(ascii.getAsciiFile(), folder + "dm1dMax.asc").textWriter("
		// ");

		AsciiBasicControl simulateAscii = new AsciiBasicControl(
				"E:\\HomeWork\\mapReduce\\報告用-別刪\\20180919_Tainan_FEWS_12_6\\Analysis\\dm1dMax.asc");
		AsciiBasicControl originalAscii = new AsciiBasicControl(
				"E:\\HomeWork\\mapReduce\\報告用-別刪\\20180919_Tainan_FEWS_12_6\\total\\delicate\\dm1maxd0.asc");

		String content[][] = originalAscii.getAsciiGrid();
		List<Double> depthError = new ArrayList<Double>();

		for (int row = 0; row < content.length; row++) {
			for (int column = 0; column < content[0].length; column++) {
				if (!content[row][column].equals(originalAscii.getProperty().get("noData"))) {
					depthError.add(Math.abs(Double.parseDouble(simulateAscii.getValue(column, row))
							- Double.parseDouble(originalAscii.getValue(column, row))));
				}
			}
		}
		System.out.println("error : " + new AtCommonMath(depthError).getMean());
	}

}
