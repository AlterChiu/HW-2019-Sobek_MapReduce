package Analysis.Result.SelectRoughBoundary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import usualTool.AtFileReader;
import usualTool.FileFunction;

public class SelectRoughBoundary {
	/**
	 * 
	 * User setting for selecting the specific rough boundary
	 * 
	 * @param selectedList
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	public void settingSelectedOrder(List<Integer> selectedList)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		if (selectedList.size() != GlobalProperty.splitSize) {
			System.out.println("the selected number isn't match to the split number");
			autoSelected();
		} else {
			userSelected(selectedList);
		}
	}

	/**
	 * 
	 * select the result of rough boundary which
	 * 
	 */
	private void userSelected(List<Integer> selectedList) {
		for (int index = 0; index < selectedList.size(); index++) {
			new FileFunction().copyFolder(
					GlobalProperty.saveFolder_convergence + index + "//" + selectedList.get(index),
					GlobalProperty.saveFolder_Split + index);
		}
	}

	/**
	 * 
	 * 
	 * for auto detect the rough boundary which base on the error and spendTimes
	 * 
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void autoSelected() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		JsonObject overViewObject = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			JsonArray resultList = overViewObject.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject()
					.get(GlobalProperty.overviewProperty_SplitRoughBoundary).getAsJsonArray();

			// checking all the result of the rough boundary
			// then select the best one to be final
			// if there is no roughBoundary for the classified demFile
			// make it to run delicate one only
			int selectedRoughIndex = -1;
			double selectedCoefficient = (1 - GlobalProperty.resultError_FloodTimesError)
					* GlobalProperty.resultError_ErrorDifference;

			for (int roughIndex = 0; roughIndex < resultList.size(); roughIndex++) {
				JsonObject roughObject = resultList.get(roughIndex).getAsJsonObject();
				double spendTime = roughObject.get(GlobalProperty.overviewProperty_SpendTime_Split).getAsDouble();
				double errorValue = roughObject.get(GlobalProperty.overviewProperty_FloodDepthError).getAsDouble();
				double errorTimes = roughObject.get(GlobalProperty.overviewProperty_FloodTimesError).getAsDouble();

				if (spendTime <= 1.1 * GlobalProperty.totalAllowTime) {

					// calculate each coefficient
					double checkCoefficient;
					if (errorTimes >= 0.95) {
						checkCoefficient = errorValue;
					} else {
						checkCoefficient = errorValue * ((0.95 - errorTimes) * 10 + 1);
					}

					// if the check coefficient is smaller
					// choose it, only store the rough index
					if (selectedCoefficient > checkCoefficient) {
						selectedRoughIndex = roughIndex;
						selectedCoefficient = checkCoefficient;
					}
				}
			}

			// check for selecting rough boundary
			if (selectedRoughIndex != -1) {
				System.out.println("Rough Boundary " + index + " Auto Selected : " + selectedRoughIndex);
				new FileFunction().copyFolder(
						GlobalProperty.saveFolder_convergence + index + "//" + selectedRoughIndex,
						GlobalProperty.saveFolder_Split + index);
			} else {
				System.out.println("Rough Boundary" + index + " Auto Selected : no selected");
			}
		}

	}

}
