package main;

import java.io.IOException;

import Ascii.Merge.TimeControl.DetermineMergeDem;
import Ascii.Merge.TimeControl.MergeTimeCount;
import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;

public class MapReduceMainPage {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		InitializeFolder initialize = new InitializeFolder();
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		SplitTimeCount splitTimeCount = new SplitTimeCount();
		DetermineMergeDem determineMerge = new DetermineMergeDem();
		MergeTimeCount mergeTimeCount = new MergeTimeCount();
		
		
		System.out.println("========= Initialize Folder =================");
		initialize.createBeforeSplitCount();
		initialize.setNetWork_Pt2File();

		System.out.println("======= Delicate Time Count ================");
		System.out.print("total\t");
		totalTimeCount.DelicateTotalTimeCount();
		
		System.out.println("========== Set Split Size ==================");
		initialize.setSplitSize();
		initialize.createAfterSplitCount();

		System.out.println("========= Split Time Count ================");
		System.out.println("Split Horizantal");
		splitTimeCount.setDelicateHorizantal();
		
		System.out.println("split Straight");
		splitTimeCount.setDelicateStraight();

		System.out.println("======== Rough Time Count ================");
		System.out.print("total\t");
		totalTimeCount.RoughTotalTimeCount();

		System.out.println("================== determine the ascii section =========");
		System.out.println("Merge Horizantal");
		determineMerge.setHorizontalSplit();
		mergeTimeCount.setHorizontalTimeCount();

		System.out.println("Merge Straight");
		determineMerge.setHorizontalSplit();
		mergeTimeCount.setStraightTimeCount();
	}

}
