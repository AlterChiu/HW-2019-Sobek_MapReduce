package SOBEK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class Runtimes {
	private long simulateTime = 0;

	public void RuntimesSetLimit() throws IOException, InterruptedException {

		long startTime = System.currentTimeMillis();
		List<String> command = new ArrayList<String>();
		command.add("cmd");
		command.add("/c");
		command.add("start");
		command.add("/wait");
		command.add(GlobalProperty.sobekRuntimesBatFile);
		command.add("exit");

		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(new File(GlobalProperty.sobekWorkSpace));
		builder.command(command);
		Process process = builder.start();

		// if simulate time over shutDownLismit
		// then close the process
		Thread.sleep(60000);
		while (System.currentTimeMillis() - startTime < GlobalProperty.shutDownLimit) {
			if (process.isAlive()) {
				Thread.sleep(60000);
			} else {
				break;
			}
		}

		try {
			shutDownCommand();
			Thread.sleep(1000);
			shutDownCommand();
		} catch (Exception e) {
		}

		long endTime = System.currentTimeMillis();
		this.simulateTime = endTime - startTime;
	}

	public void RuntimesNoLimit() throws IOException, InterruptedException {

		long startTime = System.currentTimeMillis();
		List<String> command = new ArrayList<String>();
		command.add("cmd");
		command.add("/c");
		command.add("start");
		command.add("/wait");
		command.add(GlobalProperty.sobekRuntimesBatFile);
		command.add("exit");

		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(new File(GlobalProperty.sobekWorkSpace));
		builder.command(command);
		Process process = builder.start();
		process.waitFor();

		long endTime = System.currentTimeMillis();
		this.simulateTime = endTime - startTime;
	}

	/*
	 * 
	 */
	// <=========================================>
	// <user function>
	// <=========================================>
	public double getSimulateTime() {
		return this.simulateTime;
	}

	public void moveResult(String targetFolder) throws IOException {
		// mover ascFile to targetFolder
		FileFunction ff = new FileFunction();
		List<AsciiBasicControl> asciiList = new ArrayList<AsciiBasicControl>();

		String[] outPutList = new File(GlobalProperty.sobekResultFolder).list();
		for (String result : outPutList) {
			if (result.contains(".asc")) {
				asciiList.add(new AsciiBasicControl(GlobalProperty.sobekResultFolder + "\\" + result));
				ff.moveFile(GlobalProperty.sobekResultFolder + "\\" + result, targetFolder + "\\" + result);
			}
		}

		// create the maxD0 while there isn't exist
		try {
			AsciiBasicControl outAscii = asciiList.get(0);
			int row = Integer.parseInt(outAscii.getProperty().get("row"));
			int column = Integer.parseInt(outAscii.getProperty().get("column"));
			String nullValue = outAscii.getProperty().get("noData");

			for (int temptRow = 0; temptRow < row; temptRow++) {
				for (int temptColumn = 0; temptColumn < column; temptColumn++) {
					List<Double> gridValue = new ArrayList<Double>();
					if (!outAscii.getValue(temptColumn, temptRow).equals(nullValue)) {
						for (AsciiBasicControl temptAscii : asciiList) {
							gridValue.add(Double.parseDouble(temptAscii.getValue(temptColumn, temptRow)));
						}
						outAscii.setValue(temptColumn, temptRow, new AtCommonMath(gridValue).getMax() + "");
					}
				}
			}
			new AtFileWriter(outAscii.getAsciiFile(), targetFolder + GlobalProperty.saveFile_MaxFloodResult)
					.textWriter(" ");
		} catch (Exception e) {
		}
	}

	private void shutDownCommand() throws IOException, InterruptedException {
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
