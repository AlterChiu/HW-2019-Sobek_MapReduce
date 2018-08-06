package main;

import java.io.IOException;

import Analysis.DelicateTotal.FloodInitail.InitailFloodTime;
import Ascii.Merge.TimeControl.DetermineMergeDem;
import Ascii.Merge.TimeControl.MergeTimeCount;
import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;

public class MapReduceMainPage {
	public static InitializeFolder initialize = new InitializeFolder();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		SplitTimeCount splitTimeCount = new SplitTimeCount();
		DetermineMergeDem determineMerge = new DetermineMergeDem();
		MergeTimeCount mergeTimeCount = new MergeTimeCount();
		
		
		System.out.println("========= Initialize Folder =================");
		initialize.createBeforeTotalRun();
		initialize.setNetWork_Pt2File();

		System.out.println("======= Delicate Time Count ================");
		System.out.print("total\t");
		totalTimeCount.DelicateTotalTimeCount();
		initialize.createAfterTotalRun();
		
		// prepare for splitting unitDem
		InitailFloodTime initialFlood = new InitailFloodTime(GlobalProperty.saveFolder_Total_Delicate);
		initialFlood.outPutFile(GlobalProperty.analysisDem_InitailFloodTimes);
		
		
		System.out.println("======== Rough Time Count ================");
		System.out.print("total\t");
		totalTimeCount.RoughTotalTimeCount();
		
		System.out.println("========== Set Split Size ==================");
		initialize.setSplitSize();
		initialize.createAfterTotalRun();

		System.out.println("========= Classified the dem ===============");
		
		
		
		

		

		
	}

}
