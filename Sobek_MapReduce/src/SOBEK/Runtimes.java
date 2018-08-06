package SOBEK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileWriter;

public class Runtimes {
	private long simulateTime = 0;

	public Runtimes() throws IOException {

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
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		this.simulateTime = endTime - startTime;
	}
	
	public double getSimulateTime() {
		return this.simulateTime;
	}

}
