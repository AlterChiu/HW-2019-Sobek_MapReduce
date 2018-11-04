package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Analysis.Result.FloodCompare.ResultCompare;
import Ascii.Delicate.Split.Kmeans.SplitAscii_Kmean_Weight;
import Ascii.Delicate.Split.Kmeans.SplitAscii_Kmeas;
import ExtraFunction.DEM.NodeChecker;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import main.InitializeFolder;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class testMain {
	public static InitializeFolder initialize = new InitializeFolder();
	public static FileFunction ff = new FileFunction();

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String fileAdd = "E:\\mapReduce\\modelTest\\split\\0\\";
		String saveAdd = fileAdd + GlobalProperty.saveFile_MaxFloodResult;

		List<AsciiBasicControl> asciiList = new ArrayList<AsciiBasicControl>();
		for (int index = 0; index <= 12; index++) {
			asciiList.add(new AsciiBasicControl(fileAdd + "dm1d" + String.format("%04d", index) + ".asc"));
		}

		// create the maxD0 while there isn't exist
		AsciiBasicControl outAscii = asciiList.get(0);
		int row = Integer.parseInt(outAscii.getProperty().get("row"));
		int column = Integer.parseInt(outAscii.getProperty().get("column"));
		String nullValue = outAscii.getProperty().get("noData");

		for (int temptRow = 0; temptRow < row; temptRow++) {
			for (int temptColumn = 0; temptColumn < column; temptColumn++) {
				List<Double> gridValue = new ArrayList<Double>();
				if (!outAscii.getValue(temptColumn, temptRow).equals(nullValue)) {
					for (AsciiBasicControl temptAscii : asciiList) {
						gridValue.add(Double.parseDouble(temptAscii.getValue(temptColumn, temptRow)));
					}
					outAscii.setValue(temptColumn, temptRow, new AtCommonMath(gridValue).getMax() + "");
				}

			}
		}
		new AtFileWriter(outAscii.getAsciiFile(), saveAdd).textWriter(" ");

	}
}
