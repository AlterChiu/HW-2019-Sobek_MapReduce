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
		SplitAscii_Kmeas split  = new SplitAscii_Kmeas();
		split.outputClassified("E:\\HomeWork\\mapReduce\\modelTest\\classified_Original.csv");

	}
}
