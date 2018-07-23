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
import usualTool.AtFileWriter;

public class SobekDem {
	List<String> demList = new ArrayList<String>();
	List<String> knList = new ArrayList<String>();
	List<String> networkFile = new ArrayList<String>();
	List<String> frictionFile = new ArrayList<String>();
	List<String[]> ptList = new ArrayList<String[]>();
	Map<String, List<String>> domnList = new TreeMap<String, List<String>>();

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

	private void getPT12List() {
		List<String[]> pt12List = new ArrayList<String[]>();
		for (String[] node : this.ptList) {
			if (node[0].equals("PT12")) {
				pt12List.add(node);
			}
		}
		this.ptList = pt12List;
	}

	private void setDomnList() throws IOException {
		for (int index = 0; index < this.demList.size(); index++) {
			AsciiBasicControl ascii = new AsciiBasicControl(this.demList.get(index));
			Map<String,String> property = ascii.getProperty();
			
			
			List<String> domLine = new ArrayList<String>();
			StringBuilder sb_domn = new StringBuilder();
			sb_domn.append("DOMN id \'D2_");
			sb_domn.append((index+1) + "1' nm 'D2_");
			sb_domn.append((index+1));
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
			
			domnList.put(index+"", domLine);		
		}
		
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
