package Ascii.Delicate.Split.Revise;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntersect;
import usualTool.AtFileReader;
import usualTool.AtFileWriter;

public class DelicateReviseDetecting {
	private JsonObject json;
	private Map<Integer, Double> spenTimeMap = new TreeMap<Integer, Double>();
	private Set<Integer> overTimeIndex = new TreeSet<Integer>();
	private Map<Integer, Set<Integer>> overlappingMap = new TreeMap<>();

	public DelicateReviseDetecting() throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		initialliszeVariable();
	}

	/*
	 * 
	 * 
	 */
	// <=============================================>
	// <for user work>
	// <=============================================>
	public void autoRevise() throws IOException, InterruptedException {
		for (int declineIndex : this.overTimeIndex) {
			seliectedRevise(declineIndex);
		}
	}

	public void seliectedRevise(int index) throws IOException, InterruptedException {
		String declineAsciiFile = GlobalProperty.saveFolder_Split + index + GlobalProperty.saveFile_DelicateDem;

		// check for the selected index is overtime or not
		// and also check for is there any other demFile is overlapping with it
		Set<Integer> overLappingList = this.overlappingMap.get(index);
		if (this.overTimeIndex.contains(index) && overLappingList.size() > 0) {

			// detect which the demFile use the less time
			double minSpendTime = 0;
			int minIndex = 0;
			for (int detect : overLappingList) {
				if (this.spenTimeMap.get(detect) > minSpendTime) {
					minSpendTime = this.spenTimeMap.get(detect);
					minIndex = detect;
				}
			}
			String extendAsciiFile = GlobalProperty.saveFolder_Split + minIndex + GlobalProperty.saveFile_DelicateDem;

			// judgment for is there any other available decline asciiFile
			if (minSpendTime > GlobalProperty.splitTime) {
				System.out.println("selectDem " + index + "no available decline asciiFile");
			} else {
				// output revised asciiFile
				DelicateReviseWork reviseWork = new DelicateReviseWork(declineAsciiFile, extendAsciiFile);
				reviseWork.startRevising(GlobalProperty.delicateAscii_Max_ReviseTimes);
				new AtFileWriter(reviseWork.getDeclineAscii().getAsciiFile(), declineAsciiFile).textWriter(" ");
				new AtFileWriter(reviseWork.getExtendAscii().getAsciiFile(), extendAsciiFile).textWriter(" ");

				System.out.println("selectDem " + index + "revise complete");
			}
		}
	}
	// <=============================================>

	// <=============================================>
	// < private function for preparation>
	// <=============================================>
	/*
	 * 
	 * detect overLapping
	 */
	private void detectingOverLapping() throws IOException {
		// initial overlapping map
		for (int index = 0; index < GlobalProperty.splitSize; index++) {
			this.overlappingMap.put(index, new TreeSet<Integer>());
		}

		// get the overlapping
		for (int index = 0; index < GlobalProperty.splitSize - 1; index++) {
			String splitFolder = GlobalProperty.saveFolder_Split + index + "\\";
			AsciiBasicControl temptAscii = new AsciiBasicControl(splitFolder + GlobalProperty.saveFile_DelicateDem);
			AsciiIntersect intersect = new AsciiIntersect(temptAscii);

			for (int detect = index + 1; detect < GlobalProperty.splitSize; detect++) {
				String folder = GlobalProperty.saveFolder_Split + detect + "\\";
				if (intersect.isIntersect(new AsciiBasicControl(folder + GlobalProperty.saveFile_DelicateDem))) {
					Set<Integer> temptList = overlappingMap.get(index);
					Set<Integer> targetList = overlappingMap.get(detect);

					// if the list is null create a new one
					if (temptList == null) {
						temptList = new TreeSet<Integer>();
					}
					if (targetList == null) {
						targetList = new TreeSet<Integer>();
					}

					// set is intersect demFile
					temptList.add(detect);
					targetList.add(index);
					overlappingMap.put(index, temptList);
					overlappingMap.put(detect, targetList);
				}
			}
		}
	}

	/*
	 * 
	 * 
	 */
	private void initialliszeVariable()
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, IOException {
		this.json = new AtFileReader(GlobalProperty.overViewPropertyFile).getJsonObject();
		for (int index = 0; index < GlobalProperty.splitSize; index++) {

			// get the delicate split demFile index and it's spend time in demMap
			JsonObject temptJson = this.json.get(GlobalProperty.overviewProperty_Split + index).getAsJsonObject();
			Double temptTime = temptJson.get(GlobalProperty.overviewProperty_SpendTime_Split).getAsDouble();
			spenTimeMap.put(index, temptTime);

			// if the spend time is over splitTime default value
			// save the index
			if (temptTime > GlobalProperty.splitTime) {
				overTimeIndex.add(index);
			}
		}

		// detect which demFile is intersect
		detectingOverLapping();
	}

	// <==================================================>

	// <==================================================>
	// <For user function>
	// <===================================================>
	/*
	 * 
	 * 
	 */
	public void clear() {
		this.overlappingMap.clear();
		this.overTimeIndex.clear();
		this.spenTimeMap.clear();
	}

	/*
	 * 
	 * 
	 */
	public Boolean isOvertime() {
		if (this.overTimeIndex.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 
	 * 
	 */
	public void printOverTime() {
		if (this.overTimeIndex.size() > 0) {
			System.out.println("overtime index ");
			this.overTimeIndex.forEach(e -> System.out.println(e + "\t"));
		} else {
			System.out.println("no section is overtime");
		}
	}
	// <=================================================>

}
