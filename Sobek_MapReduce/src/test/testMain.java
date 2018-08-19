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

		SobekDem sobekDem = new SobekDem();
		sobekDem.addDelicateDem(GlobalProperty.originalDelicate, GlobalProperty.originalDelicateKn);
		sobekDem.addRoughDem(GlobalProperty.originalRough, GlobalProperty.originalRoughKn);
		sobekDem.start();
		
//		sobekDem.addDelicateDem("S:\\HomeWork\\mapReduce\\split\\0\\delicateDem.asc", "S:\\HomeWork\\mapReduce\\split\\0\\delicateDem(kn).asc");
//		sobekDem.addRoughDem("S:\\HomeWork\\mapReduce\\split\\0\\roughDem.asc", "S:\\HomeWork\\mapReduce\\split\\0\\roughDem(kn).asc");
//		sobekDem.start();

//		Runtimes sobekRuntimes = new Runtimes();
//		System.out.println(sobekRuntimes.getSimulateTime());
	}

	private static void getTimes() throws IOException {
		long start = System.currentTimeMillis();
		new Runtimes();
		long end = System.currentTimeMillis();
		System.out.print(end - start + "\t");
	}

}
