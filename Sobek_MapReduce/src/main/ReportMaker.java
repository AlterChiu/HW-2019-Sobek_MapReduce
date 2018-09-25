package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import Drawing.Excel.ChartImplemetns;
import Drawing.Excel.ExcelBasicControl;
import GlobalProperty.GlobalProperty;
import usualTool.AtFileReader;

public class ReportMaker {
	private int splitSize;

	public ReportMaker() {
		this.splitSize = new File(GlobalProperty.saveFolder_Split).list().length;
	}

	/**
	 * 
	 * @param saveAdd
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	// <=================================================================>
	public void outPutConvergenceReport(String saveAdd)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject overViewObject = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		List<Map<String, List<String>>> dataTree = outPutConvergenceReport_Data(overViewObject);
		outPutConvergenceReport_Picture(dataTree, saveAdd);
	}

	/**
	 * for excel picturing
	 * 
	 * @param dataTree
	 * @param saveAdd
	 * @throws IOException
	 */
	private void outPutConvergenceReport_Picture(List<Map<String, List<String>>> dataTree, String saveAdd)
			throws IOException {
		ExcelBasicControl excel = new ExcelBasicControl();
		for (int index = 0; index < dataTree.size(); index++) {
			Map<String, List<String>> temptMap = dataTree.get(index);
			int dataSize = temptMap.get("minX").size();

			// set sheet
			excel.newSheet("convergence_" + index);
			excel.selectSheet("convergence_" + index);

			// set title
			excel.setValue(0, 0, "index");
			excel.setValue(0, 1, "minX");
			excel.setValue(0, 2, "maxX");
			excel.setValue(0, 3, "minY");
			excel.setValue(0, 4, "maxY");
			excel.setValue(0, 5, "bufferCoefficeint");
			excel.setValue(0, 6, "spendTime");
			excel.setValue(0, 7, "floodTimesError");
			excel.setValue(0, 8, "floodDepthError");

			// set value
			for (int convergence = 0; convergence < dataSize; convergence++) {
				excel.setValue(1 + convergence, 0, 1 + convergence + "");
				excel.setValue(1 + convergence, 1, temptMap.get("minX").get(convergence));
				excel.setValue(1 + convergence, 2, temptMap.get("maxX").get(convergence));
				excel.setValue(1 + convergence, 3, temptMap.get("minY").get(convergence));
				excel.setValue(1 + convergence, 4, temptMap.get("maxY").get(convergence));
				excel.setValue(1 + convergence, 5, temptMap.get("BufferCoefficeint").get(convergence));
				excel.setValue(1 + convergence, 6, temptMap.get("spendTime").get(convergence));
				excel.setValue(1 + convergence, 7, temptMap.get("FloodTimesError").get(convergence));
				excel.setValue(1 + convergence, 8, temptMap.get("FloodDepthError").get(convergence));
			}

			// set picture_(c,spendTime)
			ChartImplemetns spendTimeChart = new ChartImplemetns();
			spendTimeChart.setSmooth(true);
			spendTimeChart.setYValueList(1, 6, 1 + dataSize, 6, "SpendTime");
			spendTimeChart.setXBarValue(1, 5, 1 + dataSize, 5);
			spendTimeChart.setStartPoint(0, 10);
			excel.chartCreater(spendTimeChart);

			// set picture_(c,floodTimes)
			ChartImplemetns floodTimesChart = new ChartImplemetns();
			floodTimesChart.setSmooth(true);
			floodTimesChart.setYValueList(1, 7, 1 + dataSize, 7, "FloodTimesError");
			floodTimesChart.setXBarValue(1, 5, 1 + dataSize, 5);
			floodTimesChart.setStartPoint(20, 10);
			excel.chartCreater(spendTimeChart);

			// set picture_(c,floodDepth)
			ChartImplemetns floodDepthChart = new ChartImplemetns();
			floodDepthChart.setSmooth(true);
			floodDepthChart.setYValueList(1, 8, 1 + dataSize, 8, "FloodDepthError");
			floodDepthChart.setXBarValue(1, 5, 1 + dataSize, 5);
			floodDepthChart.setStartPoint(30, 10);
			excel.chartCreater(spendTimeChart);
		}

		excel.Output(saveAdd);
	}

	/**
	 * 
	 * for read result data
	 * @param overViewObject
	 * @return
	 */
	private List<Map<String, List<String>>> outPutConvergenceReport_Data(JsonObject overViewObject) {
		List<Map<String, List<String>>> outList = new ArrayList<Map<String, List<String>>>();
		for (int index = 0; index < this.splitSize; index++) {
			JsonArray jsonArray = overViewObject.get(GlobalProperty.overviewProperty_Split).getAsJsonObject()
					.get(GlobalProperty.overviewProperty_SplitRoughBoundary).getAsJsonArray();

			Map<String, List<String>> temptMap = new TreeMap<>();
			List<String> minXList = new ArrayList<String>();
			List<String> maxXList = new ArrayList<String>();
			List<String> minYList = new ArrayList<String>();
			List<String> maxYList = new ArrayList<String>();
			List<String> spendTime = new ArrayList<String>();
			List<String> bufferCoefficient = new ArrayList<String>();
			List<String> timesError = new ArrayList<String>();
			List<String> depthError = new ArrayList<String>();

			for (JsonElement element : jsonArray) {
				JsonObject temptObject = element.getAsJsonObject();
				String floodTimesError = temptObject.get(GlobalProperty.overviewProperty_FloodTimesError).getAsString();
				if (floodTimesError.equals(GlobalProperty.overviewProperty_FloodTimesError)) {
					minXList.add(temptObject.get("minX").getAsString());
					minYList.add(temptObject.get("minY").getAsString());
					maxXList.add(temptObject.get("maxX").getAsString());
					maxYList.add(temptObject.get("maxY").getAsString());
					spendTime.add(temptObject.get(GlobalProperty.overviewProperty_SpendTime_Split).getAsString());
					bufferCoefficient
							.add(temptObject.get(GlobalProperty.overviewProperty_BufferCoefficient).getAsString());
					timesError.add(floodTimesError);
					depthError.add(temptObject.get(GlobalProperty.overviewProperty_FloodDepthError).getAsString());
				}
			}

			temptMap.put("minX", minXList);
			temptMap.put("minY", minYList);
			temptMap.put("maxX", maxXList);
			temptMap.put("maxY", maxYList);
			temptMap.put("spendTime", spendTime);
			temptMap.put("bufferCoefficient", bufferCoefficient);
			temptMap.put("timesError", timesError);
			temptMap.put("depthError", depthError);
			outList.add(temptMap);
		}
		return outList;
	}
	// <===========================================================================>
}
