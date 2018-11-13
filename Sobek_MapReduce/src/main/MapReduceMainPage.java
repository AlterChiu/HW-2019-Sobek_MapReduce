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
import Ascii.Delicate.Split.Revise.DelicateReviseDetecting;
import Ascii.Rough.ConvergenceError.ConvergenceError;
import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;
import usualTool.AtFileWriter;

public class MapReduceMainPage {
	public static InitializeFolder initialize = new InitializeFolder();
	public static List<String> logFile = new ArrayList<String>();

	public static void main(String[] args)
			throws IOException, JsonIOException, JsonSyntaxException, InterruptedException {
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
//		// prepare for splitting unitDem
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
//		// for manual function
//		// it could only run for the specific unit demFile but no restart function
//		// for(int index = 0 ; index<GlobalProperty.splitSize ; index++) {
//		// splitTimeCount.runSplitDem(index);
//		// }
//		splitTimeCount.runSplitDem();

		System.out.println("============= Delicate Dem Revise ================");
		initialize.createBeforeDelicateConvergence();
		DelicateReviseDetecting delicateRevise = new DelicateReviseDetecting();
		if (delicateRevise.isOvertime())
			delicateRevise.autoRevise();

		System.out.println("====== Determine the Rough Dem for each Classified =======");
		initialize.createAfterSplitRun();
		ConvergenceError splitDemConvergence = new ConvergenceError();
		// for manual function
		// for(int index = 0 ; index<GlobalProperty.splitSize ; index++) {
		// splitDemConvergence.start(index);
		// }
		splitDemConvergence.start();

		System.out.println("============ Flood Result Analysis ================");
		SelectRoughBoundary roughBoundarySelected = new SelectRoughBoundary();
		roughBoundarySelected.autoSelected();
		MergeSplitResult resultMerge = new MergeSplitResult();
		resultMerge.getSmoothValue();

		// get the comparision of result
		ResultCompare comparision = new ResultCompare(GlobalProperty.saveFolder_Merge);
		comparision.outputDifferenceAsciiSeries();

		System.out.println("================= Log output ====================");
		new LogOutput(comparision);

	}

}
