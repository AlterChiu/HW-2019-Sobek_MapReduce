package SOBEK;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;

public class Runtimes {

	public Runtimes() throws IOException {

		// run the sobek modle
		// and the model.exe should be under this index
		List<String> command = new ArrayList<String>();
		command.add("cmd");
		command.add("/c");
		command.add("start");
		command.add("/wait");
//		command.add("sbkbatch.ini");
//		command.add("/batch");
		command.add(GlobalProperty.sobekRuntimesForecastBat);
	
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(new File(GlobalProperty.sobekRuntimesFolder));
		builder.command(command.parallelStream().toArray(String[]::new));
		builder.start();
	}

}
