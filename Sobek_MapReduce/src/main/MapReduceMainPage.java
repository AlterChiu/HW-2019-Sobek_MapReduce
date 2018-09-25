package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import Analysis.DelicateTotal.FloodInitail.InitailFloodTime;
import Analysis.Result.FloodCompare.ResultCompare;
import Analysis.Result.MergeSplit.MergeSplitResult;
import Analysis.Result.SelectRoughBoundary.SelectRoughBoundary;
import Ascii.ConvergenceError.ConvergenceError;
import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;

public class MapReduceMainPage {
	public static InitializeFolder initialize = new InitializeFolder();

	public static void main(String[] args) throws IOException, JsonIOException, JsonSyntaxException, InterruptedException {
		// TODO Auto-generated method stub
//		System.out.println("============= Initialize Folder =================");
//		initialize.resetWorkSpace();
//		initialize.createBeforeTotalRun();
//		initialize.setNetWork_Pt2File();
//
//		System.out.println("=========== Delicate Time Count ================");
//		TotalTimeCount totalTimeCount = new TotalTimeCount();
//		totalTimeCount.DelicateTotalTimeCount();
//		initialize.createAfterTotalRun();
//
//		System.out.println("============ Rough Time Count ================");
//		totalTimeCount.RoughTotalTimeCount();
//
////		 prepare for splitting unitDem
//		System.out.println("=========== Get The InitailFlood Time ==============");
//		InitailFloodTime initialFlood = new InitailFloodTime(GlobalProperty.saveFolder_Total_Delicate);
//		initialFlood.outPutFile(GlobalProperty.saveFile_Analysis_InitailFlood);
//
//		System.out.println("============== Set Split Size ===================");
//		initialize.setSplitSize();
//		initialize.createAfterTotalRun();
//
//		System.out.println("======== Classified the Delicate Dem ================");
//		SplitTimeCount splitTimeCount = new SplitTimeCount();
//		splitTimeCount.runSplitDem();

		System.out.println("====== Determine the Rough Dem for each Classified =======");
		initialize.createAfterSplitRun();
		ConvergenceError splitDemConvergence = new ConvergenceError();
		splitDemConvergence.start();

		System.out.println("============ Flood Result Analysis ================");
		SelectRoughBoundary roughBoundarySelected = new SelectRoughBoundary();
		roughBoundarySelected.autoSelected();
		MergeSplitResult resultMerge = new MergeSplitResult();
		resultMerge.getSmoothValue();

		ResultCompare comparision = new ResultCompare(GlobalProperty.saveFolder_Merge);
		comparision.outputDifferenceAsciiSeries();
		System.out.println("Flood times difference analysis : " + comparision.getMeanTimesDifference());
		System.out.println("Flood depth difference analysis : " + comparision.getMeanValueDifference());
	}

}
