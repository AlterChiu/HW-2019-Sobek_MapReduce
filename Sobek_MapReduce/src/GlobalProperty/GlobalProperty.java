package GlobalProperty;

public class GlobalProperty {
	//                                       DEM split size   =======>    5%
	//==========================================
	public static int splitSize = 20;
	//                                   		DEM split  time ========>  0.8 hour
	//==========================================
	public static double splitTime = 0.8*3600000;
	//                                         Total allow time
	//==========================================
	public static double totalAllowTime = 1*3600*1000;
	
	
	// 											userSetting
	//=================================================
	public static String workSpace = "S:\\HomeWork\\mapReduce\\";
	public static String sobekWorkSpace = "C:\\Sobek213\\";
	
	
	
	//                                            timeCount Property
	//=================================================
	public static String propertyFileName = "\\property.json";
	public static String delicateTotal = "delicateTotal";
	public static String roughTotal = "roughTotal";
	public static String straightSplit = "straightSplit";
	public static String horizontalSplit = "horizontalSplit";
	
	
	
	
	
	//											SOBEK Runtimes folder
	//=======================================================
	public static String sobekRuntimesFolder = "C:\\Sobek213";//--------user setting
	public static String sobekRuntimesForecastBat = sobekRuntimesFolder + "\\Sobek_Forecast.bat";
	public static  String sobekResultFolder = sobekRuntimesFolder + "Output\\";

	
	
	

	//
	//                                          Original DELICATE    user setting
	// original demFile ==============> level and kn including
	public static  String originalDelicate = workSpace + "OriginalDEM\\ZoneU1_20m.asc";
	public static  String originalDelicateKn = workSpace + "OriginalDEM\\ZoneU1_20m(kn).asc";
	public static  String originalDelicateNull = workSpace + "OriginalDEM\\ZoneU1_20mNULL.asc";
	
	
	//
	//                                        Original  ROUGH       user setting
	// original demFile ==============> level and kn including
	public static  String originalRough = workSpace + "OriginalDEM\\ZoneU1_40m.asc";
	public static  String originalRoughKn = workSpace + "OriginalDEM\\ZoneU1_40m(kn).asc";
	public static  String originalRoughNull = workSpace + "OriginalDEM\\ZoneU1_40mNULL.asc";
	
	

	// SOBEK model demFile Folder 
	public static  String sobekDelicateDem = sobekWorkSpace + "Active12.lit\\DEM\\HomeWork\\mapReduce\\firstDem.asc";
	public static  String sobekDelicateDemKn =  sobekWorkSpace + "Active12.lit\\DEM\\HomeWork\\mapReduce\\firstDem(kn).asc";
	public static  String sobekRoughDem = sobekWorkSpace + "Active12.lit\\DEM\\HomeWork\\mapReduce\\secondDem.asc";
	public static  String sobekRoughDemKn = sobekWorkSpace + "Active12.lit\\DEM\\HomeWork\\mapReduce\\secondDem(kn).asc";
	
	
	
	
	// 
	//                                      SAVE Folder
	//================================================
	//                                               total    
	public static  String totalFolder = workSpace + "total\\";
	//================================================
	//                                               total    Delicate
	public static  String totalDelicateSaveFolder = totalFolder + "\\delicate\\";
	//================================================
	//                                               total    Rough
	public static String totalRoughSaveFolder = totalFolder + "\\rough\\";
	
	//=================================================
	//                                             temptSaveName
	public static String demTempSaveName = "\\temptLevelDem.asc";
	public static String demTempSaveNameKn = "\\temptLevelDemKn.asc";
	public static String mergeDelicateDem = "\\delicateDem.asc";
	public static String mergerDelicateDemKn = "\\delicateDem(kn).asc";
	public static String mergeRoughDem = "\\roughDem.asc";
	public static String mergeRoughDemKn = "\\roughDem(kn).asc";
	
	//================================================
	//                                               split   Delicate
	public static String splitDelicateSaveFolder = workSpace + "split\\";
	public static  String  splitDelicateSaveFolder_Horizontal = splitDelicateSaveFolder + "horizontal\\";
	public static  String  splitDelicateSaveFolder_Straight = splitDelicateSaveFolder + "straight\\";

	//================================================
	//                                                    split analysis
	public static String splitAnalysis_Horizontal_Delicate = workSpace + "split\\Delicate_Horizontal_Analysis.txt";
	public static String splitAnalysis_Straight_Delicate = workSpace + "split\\Delicate_Straight_Analysis.txt";
	
	//===============================================
	//														merge  folder
	public static String mergeSaveFolder = workSpace + "merge\\";
	
	
	//===============================================
	//                                                      NULL DEM VALUE
	public static String[] nullDemValue = {"-99" , "-999"};
	
	
	
	
	
	//	===============================
	//	==============TEST==============
	//	===============================
	public static String testFolder = "S:\\HomeWork\\mapReduce\\testFolder\\";
	
	
}
