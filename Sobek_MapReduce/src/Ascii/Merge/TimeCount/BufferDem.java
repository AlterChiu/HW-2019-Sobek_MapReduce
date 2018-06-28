package Ascii.Merge.TimeCount;

import java.util.TreeMap;

import com.google.gson.JsonObject;

import GlobalProperty.GlobalProperty;

public class BufferDem {
	private JsonObject roughAnalysisProperty = null;
	
	public BufferDem(ComposeProperty compose) {
		this.roughAnalysisProperty = compose.getRoughAnalysisProperty();
	}
	
	//=======================
	// <get the rough dem buffer area>
	//=======================
	
	//==============================================
	
	
	public TreeMap<String, String> getStartBuffer(int start, double spendTime) {
		TreeMap<String, String> outTree = new TreeMap<String, String>();
		double temptSpendTime = spendTime;
		int startBuffer = 0;
		for (int index = start - 2; index >= 0; index--) {
			temptSpendTime = temptSpendTime + this.roughAnalysisProperty.get(index + "").getAsDouble();
			if (temptSpendTime > GlobalProperty.totalAllowTime) {
				startBuffer = index;
			}
		}

		outTree.put("spendTime", temptSpendTime + "");
		outTree.put("bufferLimit", startBuffer + "");
		return outTree;
	}

	public TreeMap<String, String> getEndBuffer(int end, double spendTime) {
		TreeMap<String, String> outTree = new TreeMap<String, String>();
		double temptSpendTime = spendTime;
		int endBuffer = GlobalProperty.splitSize - 1;
		for (int index = end + 2; index < GlobalProperty.splitSize; index++) {
			temptSpendTime = temptSpendTime + this.roughAnalysisProperty.get(index + "").getAsDouble();
			if (temptSpendTime > GlobalProperty.totalAllowTime) {
				endBuffer = index;
			}
		}
		outTree.put("spendTime", temptSpendTime + "");
		outTree.put("bufferLimit", endBuffer + "");
		return outTree;
	}

}
