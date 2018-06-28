package Ascii.Merge.TimeCount;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileReader;

public class ComposeProperty {

	private String delicateFolder = null;
	private String roughFolder = null;
	private JsonObject delicateAnalysisProperty = null;
	private JsonObject roughAnalysisProperty = null;

	public void setDelicateFolder(String tempt) {
		this.delicateFolder = tempt;
	}

	public void setRoughFolder(String tempt) {
		this.roughFolder = tempt;
	}

	public void setDelicateAnalysisProperty(String tempt)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		this.delicateAnalysisProperty = new AtFileReader(GlobalProperty.splitAnalysis_Straight_Delicate)
				.getJsonObject();
	}

	public void setRoughAnalysisProperty(String tempt)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		this.roughAnalysisProperty = new AtFileReader(GlobalProperty.splitAnalysis_Straight_Rough).getJsonObject();
	}

	public String getDelicateFolder() {
		return this.delicateFolder;
	}

	public String getRoughFolder() {
		return this.roughFolder;
	}

	public JsonObject getDelicateAnalysisProperty() {
		return this.delicateAnalysisProperty;
	}

	public JsonObject getRoughAnalysisProperty(){
		return this.roughAnalysisProperty;
	}

}
