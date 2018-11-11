package main;

import java.io.IOException;
import java.math.BigDecimal;

import Analysis.Result.FloodCompare.ResultCompare;
import GlobalProperty.GlobalProperty;
import usualTool.AtFileWriter;
import main.MapReduceMainPage;

public class LogOutput {

	public LogOutput(ResultCompare comparision) throws IOException {
		// output for flood times
		System.out.println("Flood times difference analysis : " + comparision.getMeanTimesDifference());
		MapReduceMainPage.logFile
				.add("Flood times difference analysis mean value : " + comparision.getMeanTimesDifference());
		for (int index = 1; index < comparision.getTimesDifference().size(); index++) {
			MapReduceMainPage.logFile.add("Flood times difference analysis " + String.format("%3s", index) + " : "
					+ new BigDecimal(comparision.getTimesDifference().get(index)).setScale(3, BigDecimal.ROUND_HALF_UP)
							.toString());
		}

		// output for flood depth
		System.out.println("Flood depth difference analysis mean value : " + comparision.getMeanValueDifference());
		MapReduceMainPage.logFile
				.add("Flood depth difference analysis mean value : " + comparision.getMeanValueDifference());
		for (int index = 1; index < comparision.getValueDifference().size(); index++) {
			MapReduceMainPage.logFile.add("Flood depth difference analysis " + String.format("%3s", index) + " : "
					+ new BigDecimal(comparision.getValueDifference().get(index)).setScale(3, BigDecimal.ROUND_HALF_UP)
							.toString());
		}

		// output for the global setting
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
