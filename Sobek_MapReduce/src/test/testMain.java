package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Analysis.DelicateTotal.FloodInitail.InitailFloodTime;
import Analysis.Result.FloodCompare.ResultCompare;
import Ascii.DemMaker.RoughDemMaker;
import Ascii.TimeControl.SplitTimeCount;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiSplit;
import main.InitializeFolder;
import usualTool.AtFileWriter;
import usualTool.AtKmeans;
import usualTool.FileFunction;

public class testMain {
	public static InitializeFolder initialize = new InitializeFolder();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ResultCompare result = new ResultCompare();
		List<Double> eachValues = result.getValueDifference();
		System.out.println(result.getMeanValueDifference());
	}

	private static void getTimes() throws IOException {
		long start = System.currentTimeMillis();
		new Runtimes();
		long end = System.currentTimeMillis();
		System.out.print(end - start + "\t");
	}

}
