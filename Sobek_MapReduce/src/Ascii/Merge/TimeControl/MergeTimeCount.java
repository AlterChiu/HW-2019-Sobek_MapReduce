package Ascii.Merge.TimeControl;

import java.io.File;

import GlobalProperty.GlobalProperty;
import usualTool.FileFunction;

public class MergeTimeCount {
	private String workFolder;
	private FileFunction ff = new FileFunction();

	public void setHorizontalTimeCount() {
		this.workFolder = GlobalProperty.splitSaveFolder_Horizontal;
	}

	public void setStraightTimeCount() {
		this.workFolder = GlobalProperty.splitSaveFolder_Straight;
	}

	private void Runtimes() {

		for (String folder : new File(this.workFolder).list()) {
			ff.copyFile(this.workFolder + folder + GlobalProperty.temptDelicateDem, GlobalProperty.sobekDelicateDem);
			ff.copyFile(this.workFolder + folder + GlobalProperty.temptDelicateDemKn,
					GlobalProperty.sobekDelicateDemKn);
			ff.copyFile(this.workFolder + folder + GlobalProperty.temptRoughDem, GlobalProperty.sobekRoughDem);
			ff.copyFile(this.workFolder + folder + GlobalProperty.temptRoughDemKn, GlobalProperty.sobekRoughDemKn);
			
			
			
			
		}
	}

}
