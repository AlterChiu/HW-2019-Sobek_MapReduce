package GlobalProperty;

public class GlobalProperty {
	// ==========================================
	public static int splitSize = 2;
	public static int K_meansInitialTime = 4;
	public static int delicateAscii_Max_ReviseTimes = 3;
	public static double errorConvergence_difference = 0.1;
	public static double errorConvergence_Min = 0.8;

	public static Boolean nodeFunction_DelicateTotal = false;
	public static Boolean nodeFunction_RoughTotal = true;
	public static Boolean nodeFunction_convergence_Delicate = false;
	public static Boolean nodeFunction_convergence_Rough = true;

	public static Boolean clipFunction_convergence_Rough = true;
	// ==========================================
	public static double splitTime = 0.8 * 3600000;
	public static double shutDownLimit = 1.3 * 3600000;
	// Total allow time
	// ==========================================
	public static double totalAllowTime = 3600 * 1000;

	// userSetting
	// =================================================
	public static String workSpace = "E:\\mapReduce\\modelTest\\";
	public static String logFile = workSpace + "log.txt";
	public static String sobekWorkSpace = "F:\\Sobek213\\";

	// modelSetting
	// =================================================
	public static String caseFolder = sobekWorkSpace + "TANZ1U01.lit\\4\\";
	public static String caseNetWork_NTW = caseFolder + "NETWORK.NTW";
	public static String caseFrictionDescription = caseFolder + "FRICTION.DAT";
	public static String caseNodeDescription = caseFolder + "NODES.DAT";
	public static String caseNetWork_D12 = caseFolder + "NETWORK.D12";

	// timeCount Property
	// ================================================
	public static String propertyFileName = "\\property.json";
	public static String overViewPropertyFile = workSpace + propertyFileName;
	public static String overviewProperty_SpendTime_delicateTotal = "SpendTime_delicateTotal";
	public static String overviewProperty_SpendTime_roughTotal = "SpendTime_roughTotal";
	public static String overviewProperty_SpendTime_Split = "spendTime";

	public static String overviewProperty_Split = "mergeSplit_";
	public static String overviewProperty_SplitDelicateBoundary = "DelicateBoundary";
	public static String overviewProperty_SplitRoughBoundary = "RoughBoundary";

	public static String overviewProperty_BufferCoefficient = "BufferCoefficient";
	public static String overviewProperty_BufferCoefficient_Max = "BufferCoefficient_Max";
	public static String overviewProperty_BufferCoefficient_Difference = "BufferCoefficient_Difference";
	public static String overviewProperty_BufferCoefficient_Min = "BufferCoefficient_Min";

	public static String overviewProperty_FloodTimesError = "FloodTimesError";
	public static String overviewProperty_FloodDepthError = "FloodDepthError";

	// result error property
	public static double resultError_FloodTimesError = -1;
	public static double resultError_ErrorDifference = 9999;

	// SOBEK Runtimes folder
	// =======================================================
	public static String sobekRuntimesBatFile = sobekWorkSpace + "\\MapReduce.bat";
	public static String sobekRuntimesForecastXml = sobekWorkSpace + "MapReduce_Delicate.xml";
	public static String sobekResultFolder = sobekWorkSpace + "\\Output\\";

	//
	// Original DELICATE user setting
	// original demFile ==============> level and kn including
//	public static String originalDelicate = workSpace + "OriginalDEM\\highResolutionDem.asc";
//	public static String originalDelicateKn = workSpace + "OriginalDEM\\highResolutionDem(kn).asc";
	public static String originalDelicate = workSpace + "OriginalDEM\\ZoneU1_20m.asc";
	public static String originalDelicateKn = workSpace + "OriginalDEM\\ZoneU1_20m(kn).asc";

	//
	// Original ROUGH user setting
	// original demFile ==============> level and kn including
//	public static String originalRough = workSpace + "OriginalDEM\\lowResolutionDem.asc";
//	public static String originalRoughKn = workSpace + "OriginalDEM\\lowResolutionDem(kn).asc";
	public static String originalRough = workSpace + "OriginalDEM\\ZoneU1_40m.asc";
	public static String originalRoughKn = workSpace + "OriginalDEM\\ZoneU1_40m(kn).asc";

	//
	// SAVE Folder
	// ================================================
	// total
	public static String saveFolder_Total = workSpace + "\\total\\";
	public static String saveFolder_Total_Delicate = saveFolder_Total + "\\delicate\\";
	public static String saveFolder_Total_Rough = saveFolder_Total + "\\rough\\";

	// =================================================
	// split folder
	public static String saveFolder_Split = workSpace + "\\split\\";

	// split folder for error convergence
	public static String saveFolder_convergence = workSpace + "\\convergence\\";

	// ================================================
	// Analysis
	//
	// Revise DemFolder
	// dem boundary by maxD0 of the total delicate result
	public static String saveFolder_Analysis = workSpace + "\\Analysis\\";
	public static String saveFile_Analysis_InitailFlood = saveFolder_Analysis + "initialFloodTimes.asc";

	// =================================================
	// merge folder
	public static String saveFolder_Merge = workSpace + "\\merge\\";

	// =================================================
	// temptSaveName
	public static String saveFolder_tempt = workSpace + "\\tempt\\";
	public static String saveFile_DelicateDem = "\\delicateDem.asc";
	public static String saveFile_DelicateDemKn = "\\delicateDem(kn).asc";
	public static String saveFile_RoughDem = "\\roughDem.asc";
	public static String saveFile_RoughDemKn = "\\roughDem(kn).asc";

	// =================================================
	// Sobek model file
	public static String saveFolder_Sobek = workSpace + "\\sobekModel\\";
	public static String saveFile_SobekNetWorkNtw = saveFolder_Sobek + "NETWORK.NTW";
	public static String saveFile_SobekNetWorkD12 = saveFolder_Sobek + "NETWORK.D12";
	public static String saveFile_SobekFriction = saveFolder_Sobek + "FRICTION.DAT";
	public static String saveFile_SobekNetWorkD12_Pt2 = saveFolder_Sobek + "NETWORK_Pt2.D12";
	public static String saveFile_SobekNodes = saveFolder_Sobek + "NODES.DAT";

	// ===============================================
	// NULL DEM VALUE
	public static String[] nullDemValue = { "-99", "-999" };

	// ===============================
	// ==============TEST==============
	// ===============================
	public static String testFolder = "E:\\mapReduce\\testFolder\\";

}
