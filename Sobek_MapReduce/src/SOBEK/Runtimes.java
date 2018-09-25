package SOBEK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileWriter;

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

	public double getSimulateTime() {
		return this.simulateTime;
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
