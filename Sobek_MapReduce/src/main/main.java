package main;

import java.io.IOException;

import Ascii.TimeControl.SplitTimeCount;
import Ascii.TimeControl.TotalTimeCount;
import GlobalProperty.GlobalProperty;
import usualTool.FileFunction;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SplitTimeCount splitTimeCount = new SplitTimeCount();
		TotalTimeCount totalTimeCount = new TotalTimeCount();
		System.out.println("============================Folder Creater=================");
		for (int i = 0; i < GlobalProperty.splitSize; i++) {
			FileFunction ff = new FileFunction();
			ff.newFolder("S:\\mapReduce\\split\\horizontal\\delicate\\" + i);
			ff.newFolder("S:\\mapReduce\\split\\straight\\delicate\\" + i);
			ff.newFolder("S:\\mapReduce\\split\\straight\\rough\\" + i);
			ff.newFolder("S:\\mapReduce\\split\\horizontal\\rough\\" + i);
		}
		System.out.println("=========================Delicate=========================");

		System.out.println("total");
		totalTimeCount.DelicateTotalTimeCount();

		System.out.println("split_ Horizantal");
		splitTimeCount.setDelicateHorizantal();

		System.out.println("split_Straight");
		splitTimeCount.setDelicateStraight();

		 System.out.println("=========================Rough=========================");
		
		 System.out.println("total");
		 totalTimeCount.RoughTotalTimeCount();
		
		 System.out.println("split_ Horizantal");
		 splitTimeCount.setRoughHorizantal();
		
		 System.out.println("split_Straight");
		 splitTimeCount.setRoughStraight();

	}

}
