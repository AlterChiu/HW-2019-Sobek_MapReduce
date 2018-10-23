package Ascii.Delicate.Split.Revise;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntersect;
import asciiFunction.AsciiMerge;

public class DelicateReviseWork {
	private AsciiBasicControl mergeAscii;

	private AsciiBasicControl declineAscii;
	private double declineAsciiCenterX = 0;
	private double declineAsciiCenterY = 0;

	private AsciiBasicControl extendAscii;
	private double extendAsciiCenterX = 0;
	private double extendAsciiCenterY = 0;

	private double cutLineXcoefficient = 0;
	private double cutLineYcoefficient = 0;
	private double cutLineIntercept = 0;

	private double cutLineCenterX = 0;
	private double cutLineCenterY = 0;
	private double moveLineCenterX = 0;
	private double moveLineCenterY = 0;

	public DelicateReviseWork(AsciiBasicControl declineAscii, AsciiBasicControl extendAscii) throws IOException {
		this.declineAscii = declineAscii;
		this.extendAscii = extendAscii;
		this.mergeAscii = new AsciiBasicControl(
				new AsciiMerge(this.extendAscii, this.declineAscii).getMergeAsciiArray());
		getIntersectProerpty();
	}

	public DelicateReviseWork(String declineAsciiAdd, String extendAsciiAdd) throws IOException {
		this.declineAscii = new AsciiBasicControl(declineAsciiAdd);
		this.extendAscii = new AsciiBasicControl(extendAsciiAdd);
		this.mergeAscii = new AsciiBasicControl(
				new AsciiMerge(this.extendAscii, this.declineAscii).getMergeAsciiArray());
		getIntersectProerpty();
	}
	
	
	
	
	// <==========================================>
	// <get center of ascii>
	// <==========================================>
	/*
	 * 
	 * 
	 */
	private void getIntersectProerpty() {
		AsciiIntersect intersect = new AsciiIntersect(this.declineAscii);

		double[] declineAsciiCneter = this.declineAscii.getCoordinate(
				Integer.parseInt(this.declineAscii.getProperty().get("column")) / 2,
				Integer.parseInt(this.declineAscii.getProperty().get("row")) / 2);
		this.declineAsciiCenterX = declineAsciiCneter[0];
		this.declineAsciiCenterY = declineAsciiCneter[1];

		double[] extendAsciiCenter = this.extendAscii.getCoordinate(
				Integer.parseInt(this.extendAscii.getProperty().get("column")) / 2,
				Integer.parseInt(this.extendAscii.getProperty().get("row")) / 2);
		this.extendAsciiCenterX = extendAsciiCenter[0];
		this.extendAsciiCenterY = extendAsciiCenter[1];

		// determine the cut line by these two asciis
		double differY = (declineAsciiCneter[1] - extendAsciiCenter[1]);
		double differX = (declineAsciiCneter[0] - extendAsciiCenter[0]);

		this.cutLineCenterX = (declineAsciiCneter[0] + extendAsciiCenter[0]) / 2;
		this.cutLineCenterY = (declineAsciiCneter[1] - extendAsciiCenter[1]) / 2;
		this.moveLineCenterX = (this.cutLineCenterX + this.declineAsciiCenterX) / 2;
		this.moveLineCenterY = (this.cutLineCenterY + this.declineAsciiCenterY) / 2;

		// vertical cut line
		if (differY == 0) {
			this.cutLineXcoefficient = 1;
			this.cutLineYcoefficient = 0;
			this.cutLineIntercept = -1 * this.cutLineCenterX;

			// horizontal cut line
		} else if (differX == 0) {
			this.cutLineXcoefficient = 0;
			this.cutLineYcoefficient = 1;
			this.cutLineIntercept = -1 * this.cutLineCenterY;

			// normal, the slope of the cut line
			// will vertical to the line of two asciiFile center
		} else {
			this.cutLineXcoefficient = -1 * (differX / differY);
			this.cutLineYcoefficient = 1;
			this.cutLineIntercept = -1 * (this.cutLineCenterY - this.cutLineXcoefficient * this.cutLineCenterX);
		}
	}

}
