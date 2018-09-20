package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SobekDem sobek = new SobekDem();
		sobek.addRoughDem(GlobalProperty.originalRough, GlobalProperty.originalRoughKn);
		sobek.start();
		sobek.setNode();
		
		Runtimes run = new Runtimes();
		System.out.println(run.getSimulateTime());
	}

}
