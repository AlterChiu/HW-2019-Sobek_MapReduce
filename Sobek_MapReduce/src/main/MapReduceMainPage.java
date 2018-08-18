package main;

import java.io.IOException;

import Analysis.DelicateTotal.FloodInitail.InitailFloodTime;
import Analysis.Result.MergeSplit.MergeSplitResult;
import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;

public class MapReduceMainPage {
	public static InitializeFolder initialize = new InitializeFolder();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("========= Initialize Folder =================");
		initialize.resetWorkSpace();
		initialize.createBeforeTotalRun();
		initialize.setNetWork_Pt2File();

		System.out.println("======= Delicate Time Count ================");
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		totalTimeCount.DelicateTotalTimeCount();
		initialize.createAfterTotalRun();
		
		System.out.println("======== Rough Time Count ================");
		totalTimeCount.RoughTotalTimeCount();

		// prepare for splitting unitDem
		System.out.println("====== Get The InitailFlood Time ==============");
		InitailFloodTime initialFlood = new InitailFloodTime(GlobalProperty.saveFolder_Total_Delicate);
		initialFlood.outPutFile(GlobalProperty.saveFile_Analysis_InitailFlood);

		System.out.println("========= Set Split Size ===================");
		initialize.setSplitSize();
		initialize.createAfterTotalRun();

		System.out.println("======== Classified the dem ================");
		SplitTimeCount splitTimeCount = new SplitTimeCount();
		splitTimeCount.runSplitDem();

		System.out.println("======= Flood Result Analysis ===============");
		new MergeSplitResult();
		
		
	}

}
