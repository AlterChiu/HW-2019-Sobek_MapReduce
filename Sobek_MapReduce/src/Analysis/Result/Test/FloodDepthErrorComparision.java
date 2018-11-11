package Analysis.Result.Test;

import usualTool.AtFileWriter;
import usualTool.FileFunction;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;

public class FloodDepthErrorComparision {
	private static FileFunction ff = new FileFunction();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String workSpace = GlobalProperty.pictureFolder + "FloodDepthErrorComparision\\";
		ff.newFolder(workSpace);

		String changedPicFolder = workSpace + "changed\\";
		ff.newFolder(changedPicFolder);

		String totalFolder = GlobalProperty.saveFolder_Total_Delicate;
		for (String folder : new File(GlobalProperty.saveFolder_Split).list()) {

			String temptFolder = GlobalProperty.saveFolder_Split + folder + "\\";
			for (String asciiFile : new File(totalFolder).list()) {
				AsciiBasicControl originalAscii = new AsciiBasicControl(totalFolder + asciiFile);
				AsciiBasicControl splitAscii = new AsciiBasicControl(temptFolder + asciiFile);
				String nullValue = splitAscii.getProperty().get("noData");

				String content[][] = splitAscii.getAsciiGrid();
				for (int row = 0; row < content.length; row++) {
					for (int column = 0; column < content[0].length; column++) {
						if (!content[row][column].equals(nullValue)) {
							double coordinate[] = splitAscii.getCoordinate(column, row);

							// original - split
							double difference = Double.parseDouble(originalAscii.getValue(coordinate[0], coordinate[1]))
									- Double.parseDouble(content[row][column]);
							splitAscii.setValue(column, row,
									new BigDecimal(difference).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
						}
					}
				}
				new AtFileWriter(splitAscii.getAsciiFile(), changedPicFolder + folder + "_" + asciiFile)
						.textWriter(" ");
			}
		}

	}

}
