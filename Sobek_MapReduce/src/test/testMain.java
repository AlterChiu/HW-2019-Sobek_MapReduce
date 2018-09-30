package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ExtraFunction.DEM.NodeChecker;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;
import asciiFunction.AsciiBasicControl;
import main.InitializeFolder;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class testMain {
	public static InitializeFolder initialize = new InitializeFolder();
	public static FileFunction ff = new FileFunction();

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		NodeChecker checker = new NodeChecker();
		checker.getDifference(GlobalProperty.originalDelicate, GlobalProperty.workSpace + "nodeDelicate.csv");

	}

	private static void test() throws IOException, InterruptedException {
		List<String> simulateShutDown = new ArrayList<String>();
		simulateShutDown.add("cmd");
		simulateShutDown.add("/c");
		simulateShutDown.add("taskkill");
		simulateShutDown.add("/IM");
		simulateShutDown.add("simulate.exe");
		simulateShutDown.add("/T");
		ProcessBuilder simulateBuilder = new ProcessBuilder();
		simulateBuilder.command(simulateShutDown);
		Process simulateProcess = simulateBuilder.start();
		simulateProcess.waitFor();
	}

}
