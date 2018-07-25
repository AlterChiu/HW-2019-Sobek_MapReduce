package SOBEK;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import GlobalProperty.GlobalProperty;
import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiBoundary;
import usualTool.AtFileWriter;

public class SobekDem {
	List<String> demList = new ArrayList<String>();
	List<String> knList = new ArrayList<String>();
	List<String> networkFile = new ArrayList<String>();
	List<String> frictionFile = new ArrayList<String>();
	List<String[]> ptList = new ArrayList<String[]>();
	Map<Integer, List<String>> domnList = new TreeMap<Integer, List<String>>();
	AsciiBasicControl currentAscii;
	Map<String, String> currentAsciiProperty;

	public void addNewDem(String demFile, String knFile) {
		demList.add(demFile);
		knList.add(knFile.toUpperCase());
	}

	public void start() throws IOException {
		clearFriction();
		clearNetWork();
		setFriction();
		setNetwork();
	}

	/*
	 * 
	 * 
	 */
	// <======================================>
	// <NetWork D12>
	// <======================================>
	// <==============================================================>

	// <=========================>
	// <Read PT2 temptLate>
	// <=========================>
	private void getD12Template() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(GlobalProperty.caseNetWork_D12)));
		String tempt;

		List<String> temptList = new ArrayList<String>();
		while ((tempt = br.readLine()) != null) {
			if (tempt.trim().length() > 2 && !tempt.contains("D121.0")) {
				Arrays.asList(tempt.trim().split(" +")).forEach(e -> temptList.add(e));

				if (temptList.get(0).toLowerCase().equals(temptList.get(temptList.size() - 1))) {
					ptList.add(temptList.parallelStream().toArray(String[]::new));
					temptList.clear();
				}
			}
		}
		br.close();
	}

	// <=========================>
	// <Read PT2 temptLate>
	// <=========================>
	private void getPT12List() {
		List<String[]> pt12List = new ArrayList<String[]>();
		for (String[] node : this.ptList) {
			if (node[0].equals("PT12")) {
				pt12List.add(node);
			}
		}
		this.ptList = pt12List;
	}

	// <=========================>
	// <Set order of the domn>
	// <=========================>
	private void setDomnList() throws IOException {
		for (int index = 0; index < this.demList.size(); index++) {
			currentAscii = new AsciiBasicControl(this.demList.get(index));
			Map<String, String> property = currentAscii.getProperty();

			List<String> domLine = new ArrayList<String>();
			StringBuilder sb_domn = new StringBuilder();
			sb_domn.append("DOMN id \'D2_");
			sb_domn.append((index + 1) + "1' nm 'D2_");
			sb_domn.append((index + 1));
			domLine.add(sb_domn.toString());

			StringBuilder sb_Gfls = new StringBuilder();
			sb_Gfls.append("  GFLS nc " + property.get("column"));
			sb_Gfls.append(" nr " + property.get("row"));
			sb_Gfls.append(" x0 " + property.get("bottomX"));
			sb_Gfls.append(" y0 " + property.get("bottomY"));
			sb_Gfls.append(" dx " + property.get("cellSize"));
			sb_Gfls.append(" dy " + property.get("cellSize"));
			sb_Gfls.append(" cp 0 fnm '");
			sb_Gfls.append(this.knList.get(index) + "\' gfls");
			domLine.add(sb_Gfls.toString());

			domnList.put(index, domLine);

		}
	}

	// <=========================>
	// <Set the relation of the domn>
	// <=========================>
	private void setDomnRelation(int index) throws IOException {
		for (int target = index + 1; target < this.demList.size(); target++) {
			AsciiBasicControl targetAscii = new AsciiBasicControl(this.demList.get(target));
			Map<String, String> targetProperty = targetAscii.getProperty();

			// check the target ascii is contain of not
			double boundaryMaxX = Double.parseDouble(targetProperty.get("topX"));
			double boundaryMaxY = Double.parseDouble(targetProperty.get("topY"));
			double boundaryMinX = Double.parseDouble(targetProperty.get("bottomX"));
			double boundaryMinY = Double.parseDouble(targetProperty.get("bottomY"));
			if (currentAscii.isContain(boundaryMaxX, boundaryMinX, boundaryMaxY, boundaryMinY)) {
				List<String> domContent = this.domnList.get(index);

				// if is content get the interceptArea
				Map<String, String> intercept = new AsciiBoundary(this.currentAscii).getBoundary(targetAscii);
				double interceptMaxX = Double.parseDouble(intercept.get("maxX"));
				double interceptMaxY = Double.parseDouble(intercept.get("maxY"));
				double interceptMinX = Double.parseDouble(intercept.get("minX"));
				double interceptMinY = Double.parseDouble(intercept.get("minY"));

				// get the isChild tag
				StringBuilder isChild = new StringBuilder();
				isChild.append("  ISCHILDOF ci 'D2_");
				isChild.append((index + 1) + "\'");
				domContent.add(isChild.toString());

				// get the childBlock tag
				StringBuilder childBlock = new StringBuilder();
				int[] topPosition = targetAscii.getPosition(interceptMaxX, interceptMaxY);
				int[] bottomPosition = targetAscii.getPosition(interceptMinX, interceptMinY);
				childBlock.append("    CHILDBLOCK");
				childBlock.append(" ltx " + targetProperty.get("bottomX"));
				childBlock.append(" lty " + targetProperty.get("topY"));
				childBlock.append(" rbx " + targetProperty.get("topX"));
				childBlock.append(" rby " + targetProperty.get("bottomY"));
				childBlock.append(" ltC " + (bottomPosition[0] + 1));
				childBlock.append(" ltR " + (topPosition[1] + 1));
				childBlock.append(" rbC " + (topPosition[0] + 1));
				childBlock.append(" rbR" + (bottomPosition[1] + 1));
				childBlock.append(" childblock");
				domContent.add(childBlock.toString());
				
				// get the parentBlock tag
				StringBuilder parentBlock = new StringBuilder();
				topPosition = currentAscii.getPosition(interceptMaxX, interceptMaxY);
				bottomPosition = currentAscii.getPosition(interceptMinX, interceptMinY);
				parentBlock.append("    PARENTBLOCK");
				parentBlock.append(" ltC " + (bottomPosition[0] + 1));
				parentBlock.append(" ltR " + (topPosition[1] + 1));
				parentBlock.append(" rbC " + (topPosition[0] + 1));
				parentBlock.append(" rbR" + (bottomPosition[1] + 1));
				parentBlock.append(" parentblock");
				domContent.add(parentBlock.toString());
				domContent.add("  ischildof");
				
				// put the new domn to the original one
				this.domnList.put(index, domContent);
			}
		}
	}

	private void setDomnPt(int index) {

	}

	// <================================================================>
	/*
	 * 
	 * 
	 */
	private void clearFriction() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(GlobalProperty.caseFrictionDescription)));
		String tempt;
		while ((tempt = br.readLine()) != null) {
			if (!tempt.contains("'D2_")) {
				frictionFile.add(tempt);
			}
		}
		br.close();
	}

	private void setFriction() throws IOException {
		for (int index = 0; index < this.knList.size(); index++) {
			StringBuilder sb = new StringBuilder();
			sb.append("D2FR id \'D2_");
			sb.append((index + 1) + "\' ci \'D2_");
			sb.append((index + 1) + "\'  mf 4 mt cp 2 \'");
			sb.append(this.knList.get(index).toUpperCase());
			sb.append("\'  mw cp 0 0 0 d2fr");

			this.frictionFile.add(0, sb.toString());
		}
		new AtFileWriter(this.frictionFile.parallelStream().toArray(String[]::new),
				GlobalProperty.caseFrictionDescription).textWriter("");
	}
	// <===================================================================>

	// <====================================================================>
	/*
	 * 
	 * 
	 */
	private void clearNetWork() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(GlobalProperty.caseNetWork_NTW)));

		String temptLine;
		Boolean D2CheckPoint = true;

		while ((temptLine = br.readLine()) != null) {
			if (!temptLine.contains("\"\",\"\",0,0,\"\",\"\",0,0,0,0,0,0,0,0,\"D2_")) {

				// checking save or not => total length
				if (temptLine.contains("[")) {
					D2CheckPoint = true;
				}
				if (temptLine.contains("[D2Grid description]")) {
					D2CheckPoint = false;
				}
				if (D2CheckPoint) {
					this.networkFile.add(temptLine);
				}
			}
		}
		br.close();
	}

	private void setNetwork() throws IOException {
		this.networkFile.add("");
		this.networkFile.add("[D2Grid description]");
		this.networkFile.add("\"1.20\"");
		this.networkFile.add(this.demList.size() + "");

		for (int index = 0; index < this.demList.size(); index++) {
			AsciiBasicControl temptAscii = new AsciiBasicControl(this.demList.get(index));
			Map<String, String> properties = temptAscii.getProperty();
			double cellSize = Double.parseDouble(properties.get("cellSize"));
			String row = properties.get("row");
			String column = properties.get("column");
			String xll = new BigDecimal(Double.parseDouble(properties.get("bottomX")) - cellSize * 0.5)
					.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			String yll = new BigDecimal(Double.parseDouble(properties.get("bottomY")) - cellSize * 0.5)
					.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			String showX = new BigDecimal(Double.parseDouble(properties.get("bottomX")) - cellSize * 0.5)
					.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
			String showY = new BigDecimal(Double.parseDouble(properties.get("topY")) - cellSize * 0.5)
					.setScale(3, BigDecimal.ROUND_HALF_UP).toString();

			StringBuilder sb_1 = new StringBuilder();
			sb_1.append("\"\",\"\",0,0,\"\",\"\",0,0,0,0,0,0,0,0,\"D2_");
			sb_1.append(index + 1);
			sb_1.append("\",\"\",\"\",0,37,\"FLS_GRID\",\"\",");
			sb_1.append(showX);
			sb_1.append("," + showY);
			sb_1.append(",0,0,\"SYS_DEFAULT\",0,\"\",\"\",\"\",0,0,\"\",\"\",0,0,0,0,\"\",0");
			this.networkFile.add(1, sb_1.toString());

			StringBuilder sb_2 = new StringBuilder();
			sb_2.append("\"D2_");
			sb_2.append((index + 1) + "\",\"FLS_GRID\",\"");
			sb_2.append((index + 1) + "\",\"\",0,\"");
			sb_2.append(demList.get(index));
			sb_2.append("\",");
			sb_2.append(column + ",");
			sb_2.append(row + ",");
			sb_2.append(xll + ",");
			sb_2.append(yll + ",");
			sb_2.append(properties.get("cellSize") + ",");
			sb_2.append(properties.get("cellSize") + ",0,0,1,0,5,1,0,-1,0");

			this.networkFile.add(sb_2.toString());
		}
		new AtFileWriter(this.networkFile.parallelStream().toArray(String[]::new), GlobalProperty.caseNetWork_NTW)
				.textWriter("");
	}
	// <====================================================================>

}
