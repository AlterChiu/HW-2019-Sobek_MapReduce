package main;

import java.io.IOException;

import Analysis.DelicateTotal.FloodInitail.InitailFloodTime;
import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;

public class MapReduceMainPage {
	public static InitializeFolder initialize = new InitializeFolder();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		SplitTimeCount splitTimeCount = new SplitTimeCount();

		System.out.println("========= Initialize Folder =================");
		initialize.createBeforeTotalRun();
		initialize.setNetWork_Pt2File();

		System.out.println("======= Delicate Time Count ================");
		totalTimeCount.DelicateTotalTimeCount();
		initialize.createAfterTotalRun();

		// prepare for splitting unitDem
		InitailFloodTime initialFlood = new InitailFloodTime(GlobalProperty.saveFolder_Total_Delicate);
		initialFlood.outPutFile(GlobalProperty.analysisDem_InitailFloodTimes);

		System.out.println("========= Rough Time Count ===============");
		totalTimeCount.RoughTotalTimeCount();

		System.out.println("========= Set Split Size ===================");
		initialize.setSplitSize();
		initialize.createAfterTotalRun();

		System.out.println("========= Classified the dem ===============");
		splitTimeCount.runSplitDem();
		
		System.out.println("========= Flood Result Analysis =============");

	}

}
