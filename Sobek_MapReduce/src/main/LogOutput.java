package main;

import java.io.IOException;

import Analysis.Result.FloodCompare.ResultCompare;
import GlobalProperty.GlobalProperty;
import usualTool.AtFileWriter;
import main.MapReduceMainPage;

public class LogOutput {

	public LogOutput(ResultCompare comparision) throws IOException {
		System.out.println("Flood times difference analysis : " + comparision.getMeanTimesDifference());
		MapReduceMainPage.logFile
				.add("Flood times difference analysis mean value : " + comparision.getMeanTimesDifference());
		for (int index = 0; index < comparision.getTimesDifference().size(); index++) {
			MapReduceMainPage.logFile.add(
					"Flood times difference analysis " + index + " : " + comparision.getTimesDifference().get(index));
		}
		System.out.println("Flood depth difference analysis : " + comparision.getMeanValueDifference());
		for (int index = 0; index < comparision.getValueDifference().size(); index++) {
			MapReduceMainPage.logFile.add(
					"Flood depth difference analysis " + index + " : " + comparision.getValueDifference().get(index));
		}
		MapReduceMainPage.logFile.add("Flood depth difference analysis : " + comparision.getMeanValueDifference());
		MapReduceMainPage.logFile.add("==============================================");
		MapReduceMainPage.logFile.add("splitSize : " + GlobalProperty.splitSize);
		MapReduceMainPage.logFile.add("Initial Flood Times : " + GlobalProperty.K_meansInitialTime);
		MapReduceMainPage.logFile.add("node change Delicate total : " + GlobalProperty.nodeFunction_DelicateTotal);
		MapReduceMainPage.logFile.add("node change Rough total : " + GlobalProperty.nodeFunction_RoughTotal);
		MapReduceMainPage.logFile
				.add("node change Delicate convergence : " + GlobalProperty.nodeFunction_convergence_Delicate);
		MapReduceMainPage.logFile
				.add("node change Rough convergence : " + GlobalProperty.nodeFunction_convergence_Rough);
		MapReduceMainPage.logFile.add("convergence Rough Clip : " + GlobalProperty.clipFunction_convergence_Rough);

		// output the log file
		new AtFileWriter(MapReduceMainPage.logFile.parallelStream().toArray(String[]::new), GlobalProperty.logFile)
				.tabWriter();

	}

}
