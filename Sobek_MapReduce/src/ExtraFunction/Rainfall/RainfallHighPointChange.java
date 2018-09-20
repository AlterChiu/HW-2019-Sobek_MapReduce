package ExtraFunction.Rainfall;

import java.io.IOException;
import java.math.BigDecimal;

import usualTool.AtFileReader;
import usualTool.AtFileWriter;

public class RainfallHighPointChange {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileAdd = "E:\\HomeWork\\mapReduce\\RainfallData\\200y_12_6_TN_PD.BUI";
		String content[][] = new AtFileReader(fileAdd).getStr();

		for (int column = 0; column < content[2910].length; column++) {
			double start = Double.parseDouble(content[2910][column]);
			double high = Double.parseDouble(content[2915][column]);
			double end = Double.parseDouble(content[2921][column]);

			int eventStart = 2910;
			double levelUp = (high - start) / 2;
			double levelDown = (end - high) / 9;
			for (int line = 0; line < 3; line++) {
				content[eventStart + line][column] = new BigDecimal(start + levelUp * line)
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
			for (int line = 3; line < 12; line++) {
				content[eventStart + line][column] = new BigDecimal(high + levelDown * (line - 2))
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
		}

		new AtFileWriter(content, "E:\\HomeWork\\mapReduce\\RainfallData\\200y_12_3_TN_PD.BUI").textWriter(" ");

	}

}
