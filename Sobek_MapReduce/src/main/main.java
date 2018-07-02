package main;

import java.io.IOException;

import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;
import usualTool.FileFunction;

public class main {
	public static FileFunction ff = new FileFunction();
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SplitTimeCount splitTimeCount = new SplitTimeCount();
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		System.out.println("======================= Initialize Folder =================");
		new InitializeFolder();
		
		System.out.println("========================= Delicate ======================");
		System.out.println("total");
		totalTimeCount.DelicateTotalTimeCount();

		System.out.println("split_ Horizantal");
		splitTimeCount.setDelicateHorizantal();

		System.out.println("split_Straight");
		splitTimeCount.setDelicateStraight();

		 System.out.println("====================== Rough ======================");
		 System.out.println("total");
		 totalTimeCount.RoughTotalTimeCount();
		 
		 
		 System.out.println("================== determine the ascii section =========");
		 
		 
		 
		 
		 

	}

}
