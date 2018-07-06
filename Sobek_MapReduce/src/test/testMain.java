package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import asciiFunction.AsciiMerge;
import main.InitializeFolder;
import usualTool.AtFileWriter;

public class testMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//	

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
		
		
	}

}
