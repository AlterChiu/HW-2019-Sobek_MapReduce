package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiSplit;
import main.InitializeFolder;
import usualTool.AtFileWriter;

public class testMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//
		String fileAdd = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_20m.asc";
		String saveAdd = "S:\\HomeWork\\mapReduce\\test\\";

		ArrayList<String[][]> splitAscii = new AsciiSplit(fileAdd).getSplitAsciiByEqualSizeBuffer(2);
		for (int index = 0; index < splitAscii.size(); index++) {
			new AtFileWriter(splitAscii.get(index) , saveAdd + index + ".asc").textWriter("    ");;
		}

	}

}
