package GlobalProperty;

public class GlobalProperty {
	// DEM split size =======> 5%
	// ==========================================
	public static int splitSize = 1;
	// DEM split time ========> 0.7 hour
	// ==========================================
	public static double splitTime = 0.7 * 3600000;
	// Total allow time
	// ==========================================
	public static double totalAllowTime = 0.9 * 3600 * 1000;

	// userSetting
	// =================================================
	public static String workSpace = "S:\\HomeWork\\mapReduce\\";
	public static String sobekWorkSpace = "C:\\Sobek213\\";
	public static String sobekCaseName = "\\Active12.lit\\";
	
	// timeCount Property
	// ================================================
	public static String propertyFileName = "\\property.json";
	public static String overviewProperty = workSpace + propertyFileName;
	public static String delicateTotal = "delicateTotal";
	public static String roughTotal = "roughTotal";
	public static String straightSplit = "straightSplit";
	public static String horizontalSplit = "horizontalSplit";

	// SOBEK Runtimes folder
	// =======================================================
	public static String sobekRuntimesBatFile = sobekWorkSpace + "\\MapReduce.bat";
	public static String sobekRuntimesForecastBar_Delicate = "MapReduce_Delicate.xml";
	public static String sobekRuntimesForecastBar_Rough = "MapReduce_Rough.xml";
	public static String sobekRuntimesForecastBar_Merge = "MapReduce_Merge.xml";
	
	
	
	
	public static String sobekResultFolder = sobekWorkSpace + "\\Output\\";

	//
	// Original DELICATE user setting
	// original demFile ==============> level and kn including
	public static String originalDelicate = workSpace + "\\OriginalDEM\\ZoneU1_20m.asc";
	public static String originalDelicateKn = workSpace + "\\OriginalDEM\\ZoneU1_20m(kn).asc";
	public static String originalDelicateNull = workSpace + "\\OriginalDEM\\ZoneU1_20mNULL.asc";

	//
	// Original ROUGH user setting
	// original demFile ==============> level and kn including
	public static String originalRough = workSpace + "\\OriginalDEM\\ZoneU1_40m.asc";
	public static String originalRoughKn = workSpace + "\\OriginalDEM\\ZoneU1_40m(kn).asc";
	public static String originalRoughNull = workSpace + "\\OriginalDEM\\ZoneU1_40mNULL.asc";

	// SOBEK model demFile Folder
	public static String sobekDelicateDem = sobekWorkSpace + sobekCaseName + "\\DEM\\mapReduce\\firstDem.asc";
	public static String sobekDelicateDemKn = sobekWorkSpace + sobekCaseName + "\\DEM\\mapReduce\\firstDem(kn).asc";
	public static String sobekRoughDem = sobekWorkSpace + sobekCaseName + "\\DEM\\mapReduce\\secondDem.asc";
	public static String sobekRoughDemKn = sobekWorkSpace + sobekCaseName + "\\DEM\\mapReduce\\secondDem(kn).asc";

	//
	// SAVE Folder
	// ================================================
	// total
	public static String totalFolder = workSpace + "\\total\\";
	// ================================================
	// total Delicate
	public static String totalDelicateSaveFolder = totalFolder + "\\delicate\\";
	// ================================================
	// total Rough
	public static String totalRoughSaveFolder = totalFolder + "\\rough\\";

	// =================================================
	// temptSaveName
	public static String temptDelicateDem = "\\delicateDem.asc";
	public static String temptDelicateDemKn = "\\delicateDem(kn).asc";
	public static String temptRoughDem = "\\roughDem.asc";
	public static String temptRoughDemKn = "\\roughDem(kn).asc";

	// ================================================
	// split Delicate
	public static String splitSaveFolder = workSpace + "\\split\\";
	public static String splitSaveFolder_Horizontal = splitSaveFolder + "\\horizontal\\";
	public static String splitSaveFolder_Straight = splitSaveFolder + "\\straight\\";

	// ================================================
	// split analysis
	public static String splitAnalysis_Horizontal_Delicate = workSpace + "\\split\\Delicate_Horizontal_Analysis.txt";
	public static String splitAnalysis_Straight_Delicate = workSpace + "\\split\\Delicate_Straight_Analysis.txt";

	// ===============================================
	// merge folder
	public static String mergeSaveFolder = workSpace + "\\merge\\";

	// ===============================================
	// NULL DEM VALUE
	public static String[] nullDemValue = { "-99", "-999" };

	// ===============================
	// ==============TEST==============
	// ===============================
	public static String testFolder = "S:\\HomeWork\\mapReduce\\testFolder\\";

}
