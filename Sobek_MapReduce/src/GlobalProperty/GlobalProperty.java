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
	public static String sobekProjectName = sobekWorkSpace + "Active12.lit\\";

	// modelSetting
	// =================================================
	public static String caseFolder = sobekProjectName + "5\\";
	public static String caseNetWork_NTW = caseFolder + "NETWORK.NTW";
	public static String caseFrictionDescription = caseFolder + "FRICTION.DAT";
	public static String caseNetWork_D12 = caseFolder + "NETWORK.D12";
	public static String caseNetWork_D12_Template = workSpace + "NETWORK_Pt2.D12";

	// timeCount Property
	// ================================================
	public static String propertyFileName = "\\property.json";
	public static String overViewPropertyFile = workSpace + propertyFileName;
	public static String overviewProperty_delicateTotal = "SpendTime_delicateTotal";
	public static String overviewProperty_roughTotal = "SpendTime_roughTotal";
	public static String overviewProperty_merge = "mergeSplit";
	public static String overviewProperty_mergeDelicateBoundary = "DelicateBoundary";
	public static String overviewProperty_mergeRoughBoundary = "RoughBoundary";

	// SOBEK Runtimes folder
	// =======================================================
	public static String sobekRuntimesBatFile = sobekWorkSpace + "\\MapReduce.bat";
	public static String sobekRuntimesForecastXml = sobekWorkSpace + "MapReduce_Delicate.xml";
	public static String sobekResultFolder = sobekWorkSpace + "\\Output\\";

	//
	// Original DELICATE user setting
	// original demFile ==============> level and kn including
	public static String originalDelicate = workSpace + "OriginalDEM\\ZoneU1_20m.asc";
	public static String originalDelicateKn = workSpace + "OriginalDEM\\ZoneU1_20m(kn).asc";
	public static String originalDelicateNull = workSpace + "OriginalDEM\\ZoneU1_20mNULL.asc";

	//
	// Original ROUGH user setting
	// original demFile ==============> level and kn including
	public static String originalRough = workSpace + "OriginalDEM\\ZoneU1_40m.asc";
	public static String originalRoughKn = workSpace + "OriginalDEM\\ZoneU1_40m(kn).asc";
	public static String originalRoughNull = workSpace + "OriginalDEM\\ZoneU1_40mNULL.asc";

	//
	// Revise DemFolder
	// dem boundary by maxD0 of the total delicate result
	public static String analysisDemFolder = workSpace + "\\Analysis\\";
	public static String analysisDem_InitailFloodTimes = analysisDemFolder + "initialFloodTimes.asc";

	//
	// SAVE Folder
	// ================================================
	// total
	public static String saveFolder_Total = workSpace + "\\total\\";
	public static String saveFolder_Total_Delicate = saveFolder_Total + "\\delicate\\";
	public static String saveFolder_Total_Rough = saveFolder_Total + "\\rough\\";

	
	//=================================================
	//merge folder
	public static String saveFolder_Merge = workSpace + "\\merge\\";

	// =================================================
	// temptSaveName
	public static String saveFile_DelicateDem = "\\delicateDem.asc";
	public static String saveFile_DelicateDemKn = "\\delicateDem(kn).asc";
	public static String saveFile_RoughDem = "\\roughDem.asc";
	public static String saveFile_RoughDemKn = "\\roughDem(kn).asc";


	// ===============================================
	// NULL DEM VALUE
	public static String[] nullDemValue = { "-99", "-999" };

	// ===============================
	// ==============TEST==============
	// ===============================
	public static String testFolder = "S:\\HomeWork\\mapReduce\\testFolder\\";

}
