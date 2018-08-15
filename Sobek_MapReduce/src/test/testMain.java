package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Analysis.DelicateTotal.FloodInitail.InitailFloodTime;
import Ascii.DemMaker.RoughDemMaker;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiSplit;
import main.InitializeFolder;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class testMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileFunction ff = new FileFunction();
//
//		 SobekDem sobekDem = new SobekDem();
//		 sobekDem.addNewDem(GlobalProperty.originalDelicate,
//				GlobalProperty.originalDelicateKn);
//		sobekDem.start();
////
		
		new File(GlobalProperty.overViewPropertyFile).delete();
//		new RoughDemMaker().setRoughDem(
//				"S:\\HomeWork\\mapReduce\\OriginalDEM\\97Tainan(40mDEM)_mod(BJ_JS_JJS_TW_Annan_Erren)(mhby10).asc");
//		

	
	}

	private static void getTimes() throws IOException {
		long start = System.currentTimeMillis();
		new Runtimes();
		long end = System.currentTimeMillis();
		System.out.print(end - start + "\t");
	}

}
