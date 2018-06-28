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
	
	
	//											SOBEK Runtimes folder
	//=======================================================
	public static String sobekRuntimesFolder = "C:\\Sobek213";
	public static String sobekRuntimesForecastBat = sobekRuntimesFolder + "\\Sobek_Forecast.bat";
	public static  String sobekResultFolder = sobekRuntimesFolder + "Output\\";
	public static String sobekResultPropertyFileName = "\\unitProperty.txt";
	
	
	

	//
	//                                          Original DELICATE
	// original demFile ==============> level and kn including
	public static  String originalDelicate = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_20m.asc";
	public static  String originalDelicateKn = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_20m(kn).asc";
	public static  String originalDelicateNull = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_20mNULL.asc";
	
	
	//
	//                                        Original  ROUGH
	// original demFile ==============> level and kn including
	public static  String originalRough = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_40m.asc";
	public static  String originalRoughKn = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_40m(kn).asc";
	public static  String originalRoughNull = "S:\\HomeWork\\mapReduce\\OriginalDEM\\ZoneU1_40mNULL.asc";
	
	

	// SOBEK model demFile Folder 
	public static  String sobekDelicateDem = "C:\\code\\javaWorkspace\\Fews\\SOBEK_Ascii\\Active12.lit\\DEM\\HomeWork\\mapReduce\\firstDem.asc";
	public static  String sobekDelicateDemKn = "C:\\code\\javaWorkspace\\Fews\\SOBEK_Ascii\\Active12.lit\\DEM\\HomeWork\\mapReduce\\firstDem(kn).asc";
	public static  String sobekRoughDem = "C:\\code\\javaWorkspace\\Fews\\SOBEK_Ascii\\Active12.lit\\DEM\\HomeWork\\mapReduce\\secondDem.asc";
	public static  String sobekRoughDemKn = "C:\\code\\javaWorkspace\\Fews\\SOBEK_Ascii\\Active12.lit\\DEM\\HomeWork\\mapReduce\\secondDem(kn).asc";
	
	
	
	
	// 
	//                                      SAVE Folder
	public static String saveFolder = "S:\\HomeWork\\mapReduce\\";
	//================================================
	//                                               total    Delicate
	public static  String totalDelicateSaveFolder = saveFolder + "total\\delicate\\";
	//================================================
	//                                               total    Rough
	public static String totalRoughSaveFolder = saveFolder + "total\\rough\\";
	
	//=================================================
	//                                             temptSaveName
	public static String splitDemTempSaveName = "\\temptLevelDem.asc";
	
	//================================================
	//                                               split   Delicate
	public static  String  splitDelicateSaveFolder_Horizontal = saveFolder + "split\\horizontal\\delicate\\";
	public static  String  splitDelicateSaveFolder_Straight = saveFolder + "split\\straight\\delicate\\";
	
	//================================================
	//                                             	split   Rough
	public static  String  splitRoughSaveFolder_Horizontal =  saveFolder + "split\\horizontal\\rough\\";
	public static  String  splitRoughSaveFolder_Straight =  saveFolder + "split\\straight\\rough\\";	
	
	//================================================
	//                                                    split analysis
	public static String splitAnalysis_Horizontal_Delicate = saveFolder + "split\\Delicate_Horizontal_Analysis.txt";
	public static String splitAnalysis_Horizontal_Rough = saveFolder + "split\\Rough_Horizontal_Analysis.txt";
	public static String splitAnalysis_Straight_Delicate = saveFolder + "split\\Delicate_Straight_Analysis.txt";
	public static String splitAnalysis_Straight_Rough = saveFolder + "split\\Rough_Straight_Analysis.txt";
	
	
	
	//===============================================
	//                                                      NULL DEM VALUE
	public static String[] nullDemValue = {"-99" , "-999"};
	
	
	
	
	
//	===============================
//	==============TEST==============
//	===============================
	public static String testFolder = "S:\\HomeWork\\mapReduce\\testFolder\\";
	
	
}
