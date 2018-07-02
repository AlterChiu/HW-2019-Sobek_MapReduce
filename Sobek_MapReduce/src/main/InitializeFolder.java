package main;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileWriter;
import usualTool.FileFunction;

public class InitializeFolder {

	public InitializeFolder() throws IOException {
		FileFunction ff = new FileFunction();
		// ====================split==================
		ff.newFolder(GlobalProperty.workSpace + "split\\");
		ff.newFolder(GlobalProperty.workSpace + "split\\horizontal\\");
		ff.newFolder(GlobalProperty.workSpace + "split\\straight\\");
		for (int i = 0; i < GlobalProperty.splitSize; i++) {
			ff.newFolder(GlobalProperty.workSpace + "split\\horizontal\\" + i);
			ff.newFolder(GlobalProperty.workSpace + "split\\straight\\" + i);
		}

		// ====================merge================
		ff.newFolder(GlobalProperty.workSpace + "merge\\");

		// ==================total===================
		ff.newFolder(GlobalProperty.workSpace + "total\\");
		ff.newFolder(GlobalProperty.workSpace + "total\\delicate\\");
		ff.newFolder(GlobalProperty.workSpace + "total\\rough\\");

		// =============Analysis Property===============
		if (!new File(GlobalProperty.workSpace + GlobalProperty.propertyFileName).exists()) {
			JsonObject outJson = new JsonObject();
			outJson.addProperty(GlobalProperty.delicateTotal, 0);
			outJson.addProperty(GlobalProperty.roughTotal, 0);

			JsonArray horizontalArray = new JsonArray();
			JsonArray straightArray = new JsonArray();
			for (int index = 0; index < GlobalProperty.splitSize; index++) {
				straightArray.add(new JsonParser().parse("0"));
				horizontalArray.add(new JsonParser().parse("0"));
			}
			
			outJson.add(GlobalProperty.straightSplit, straightArray);
			outJson.add(GlobalProperty.horizontalSplit, horizontalArray);
			
			new AtFileWriter(outJson , GlobalProperty.workSpace + GlobalProperty.propertyFileName).textWriter("");;
		}
		

	}

}
