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
	public static FileFunction ff = new FileFunction();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String fileAdd = "S:\\HomeWork\\mapReduce\\test\\";
		
		List<String[]> demList = new ArrayList<String[]>();
		demList.add(new String[] { fileAdd + "Z1U1_20m.asc" , fileAdd + "Z1U1_40m.asc"});
		demList.add(new String[] { fileAdd + "Z1U2_20m.asc" , fileAdd + "Z1U2_40m.asc"});
		
		List<String[]> knList = new ArrayList<String[]>();
		knList.add(new String[] { fileAdd + "Z1U1_20m(kn).asc" , fileAdd + "Z1U1_40m(kn).asc"});
		knList.add(new String[] { fileAdd + "Z1U2_20m(kn).asc" , fileAdd + "Z1U2_40m(kn).asc"});

		for(int index = 0 ; index< demList.size() ; index++) {
			SobekDem sobekDem = new SobekDem();
			sobekDem.addDelicateDem(demList.get(index)[0], knList.get(index)[0]);
			sobekDem.addRoughDem(demList.get(index)[1], knList.get(index)[1]);
			sobekDem.start();
			
			Runtimes sobekRuntimes = new Runtimes();
			System.out.println(sobekRuntimes.getSimulateTime());
			ff.moveFolder(GlobalProperty.sobekResultFolder, fileAdd + index);
		}
		
		
	
	}

	private static void getTimes() throws IOException {
		long start = System.currentTimeMillis();
		new Runtimes();
		long end = System.currentTimeMillis();
		System.out.print(end - start + "\t");
	}

}
