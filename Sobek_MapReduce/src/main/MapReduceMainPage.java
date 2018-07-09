package main;

import java.io.IOException;

import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;

public class MapReduceMainPage {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SplitTimeCount splitTimeCount = new SplitTimeCount();
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		InitializeFolder initialize = new InitializeFolder();
		
		System.out.println("========= Initialize Folder =================");
		initialize.createBeforeSplitCount();

		System.out.println("======= Delicate Time Count ================");
		System.out.print("total    ");
		totalTimeCount.DelicateTotalTimeCount();
		
		System.out.println("========== Set Split Size ==================");
		initialize.setSplitSize();
		initialize.createAfterSplitCount();

		System.out.println("========= Split Time Count ================");
		System.out.println("split_ Horizantal");
		splitTimeCount.setDelicateHorizantal();
		
		System.out.println("split_Straight");
		splitTimeCount.setDelicateStraight();

		System.out.println("======== Rough Time Count ================");
		System.out.print("total    ");
		totalTimeCount.RoughTotalTimeCount();

		System.out.println("================== determine the ascii section =========");
		System.out.println();
		

	}

}
