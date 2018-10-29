package Ascii.Delicate.Split.Revise;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asciiFunction.AsciiBasicControl;
import asciiFunction.AsciiIntersect;
import asciiFunction.AsciiMerge;
import asciiFunction.AsciiToPath;
import geo.path.IntersectLine;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;
import GlobalProperty.GlobalProperty;
import SOBEK.Runtimes;
import SOBEK.SobekDem;

public class DelicateReviseWork {
	// merge ascii
	private AsciiBasicControl mergeAscii;
	private Path2D mergedPath;

	// asciiFile
	// <===================================>
	private AsciiBasicControl declineAscii;
	private AsciiBasicControl declineKn;
	private double declineAsciiCenterX = 0;
	private double declineAsciiCenterY = 0;
	private double declineSpendTime = 0;

	private AsciiBasicControl extendAscii;
	private AsciiBasicControl extendKn;
	private double extendAsciiCenterX = 0;
	private double extendAsciiCenterY = 0;
	private double extendSpendTime = 0;
	// <===================================>

	// use for revise delicate asciiFile
	// <===================================>

	// the cut line coefficient
	private double cutLineXcoefficient = 0;
	private double cutLineYcoefficient = 0;
	private double cutLineIntercept = 0;

	// original coordinate
	private double cutLineCenterX = 0;
	private double cutLineCenterY = 0;

	//
	private double moveLineCenterX = 0;
	private double moveLineCenterY = 0;
	private double boundaryExtendX = 0;
	private double boundaryExtendY = 0;
	private double boundaryDeclineX = 0;
	private double boundaryDeclineY = 0;

	// <====================================>

	public DelicateReviseWork(AsciiBasicControl declineAscii, AsciiBasicControl extendAscii) throws IOException {
		this.declineAscii = declineAscii;
		this.extendAscii = extendAscii;
		this.mergeAscii = new AsciiBasicControl(
				new AsciiMerge(this.extendAscii, this.declineAscii).getMergeAsciiArray());
		this.mergedPath = new AsciiToPath(this.mergeAscii).getAsciiPath();
		getIntersectProerpty();
	}

	public DelicateReviseWork(String declineAsciiAdd, String extendAsciiAdd) throws IOException {
		this.declineAscii = new AsciiBasicControl(declineAsciiAdd);
		this.extendAscii = new AsciiBasicControl(extendAsciiAdd);
		this.mergeAscii = new AsciiBasicControl(
				new AsciiMerge(this.extendAscii, this.declineAscii).getMergeAsciiArray());
		this.mergedPath = new AsciiToPath(this.mergeAscii).getAsciiPath();
		getIntersectProerpty();
	}
	// <==========================================>

	/*
	 * 
	 */
	// <==========================================>
	// <for public function>
	// <==========================================>
	public AsciiBasicControl getExtendAscii() {
		return this.extendAscii;
	}

	public double getExtendSpendTime() {
		return this.extendSpendTime;
	}

	public AsciiBasicControl getDeclineAscii() {
		return this.declineAscii;
	}

	public double getDeclineSpendTime() {
		return this.declineSpendTime;
	}
	// <==========================================>

	/*
	 * intersection line movement
	 * 
	 */
	// <==========================================>
	public void startRevising(int maxTimes) throws IOException, InterruptedException {
		// setting the boundary points
		this.boundaryDeclineX = this.moveLineCenterX;
		this.boundaryDeclineY = this.moveLineCenterY;

		this.boundaryExtendX = this.extendAsciiCenterX;
		this.boundaryExtendY = this.extendAsciiCenterY;

		this.moveLineCenterX = (this.boundaryDeclineX + this.boundaryExtendX) / 2;
		this.moveLineCenterY = (this.boundaryDeclineY + this.boundaryExtendY) / 2;

		// setting the line
		double differX = this.moveLineCenterX - this.boundaryDeclineX;
		double differY = this.moveLineCenterY - this.boundaryDeclineY;
		this.cutLineIntercept = this.cutLineIntercept + this.cutLineXcoefficient * differX
				+ this.cutLineYcoefficient * differY;

		// setting new asciiFile
		IntersectLine intersect = new IntersectLine(this.mergedPath);
		List<Double[]> intersectPoints = intersect.getInterceptPoints(this.cutLineXcoefficient,
				this.cutLineYcoefficient, this.cutLineIntercept);

		this.declineAscii = clipAscii(this.declineAscii, intersectPoints);
		this.declineKn = new AsciiBasicControl(
				new AsciiIntersect(GlobalProperty.originalDelicateKn).getIntersect(declineAscii));

		this.extendAscii = clipAscii(this.extendAscii, intersectPoints);
		this.extendKn = new AsciiBasicControl(
				new AsciiIntersect(GlobalProperty.originalDelicateKn).getIntersect(extendAscii));

		// judge for the spend time
		spendTimeJudgment(maxTimes);
	}

	private void spendTimeJudgment(int times) throws IOException, InterruptedException {
		double differX = this.moveLineCenterX - this.boundaryDeclineX;
		double differY = this.moveLineCenterY - this.boundaryDeclineY;

		Boolean judgement = true;
		for (int index = 0; index < times && judgement; index++) {
			double declineTime = sobekRuntimes(this.declineAscii, this.declineKn);
			double extendTime = sobekRuntimes(this.extendAscii, this.extendKn);

			if (declineTime > GlobalProperty.splitTime || extendTime > GlobalProperty.splitTime) {
				// move to decline side
				if (declineTime > GlobalProperty.splitTime) {
					this.boundaryExtendX = this.moveLineCenterX;
					this.boundaryExtendY = this.moveLineCenterY;

					this.moveLineCenterX = (this.boundaryDeclineX + this.boundaryExtendX) / 2;
					this.moveLineCenterY = (this.boundaryDeclineY + this.boundaryExtendY) / 2;

					differX = this.moveLineCenterX - this.boundaryDeclineX;
					differY = this.moveLineCenterY - this.boundaryDeclineY;
					this.cutLineIntercept = this.cutLineIntercept + this.cutLineXcoefficient * differX
							+ this.cutLineYcoefficient * differY;

				}
				// move to extend side
				else if (extendTime > GlobalProperty.splitTime) {
					this.boundaryExtendX = this.moveLineCenterX;
					this.boundaryExtendY = this.moveLineCenterY;

					this.moveLineCenterX = (this.boundaryDeclineX + this.boundaryExtendX) / 2;
					this.moveLineCenterY = (this.boundaryDeclineY + this.boundaryExtendY) / 2;

					differX = this.moveLineCenterX - this.boundaryDeclineX;
					differY = this.moveLineCenterY - this.boundaryDeclineY;
					this.cutLineIntercept = this.cutLineIntercept + this.cutLineXcoefficient * differX
							+ this.cutLineYcoefficient * differY;
				}
			} else {
				judgement = false;
			}
		}
	}
	// <=====================================================>

	/*
	 * 
	 */
//<=======================================================>
	private AsciiBasicControl clipAscii(AsciiBasicControl ascii, List<Double[]> intersectBoundary) throws IOException {

		// intersect boundary setting
		List<Double> xCollection = new ArrayList<Double>();
		List<Double> yCollection = new ArrayList<Double>();
		intersectBoundary.forEach(point -> {
			xCollection.add(point[0]);
			yCollection.add(point[1]);
		});

		// get the max and min of boundary
		AtCommonMath xStatics = new AtCommonMath(xCollection);
		AtCommonMath yStatics = new AtCommonMath(yCollection);
		double minX = xStatics.getMin();
		double maxX = xStatics.getMax();
		double minY = yStatics.getMin();
		double maxY = yStatics.getMax();

		// get asciiProperty
		Map<String, String> property = ascii.getProperty();
		double cellSize = Double.parseDouble(property.get("cellSize"));
		double asciiMaxX = Double.parseDouble(property.get("topX")) + 0.5 * cellSize;
		double asciiMinX = Double.parseDouble(property.get("bottomX")) - 0.5 * cellSize;
		double asciiMaxY = Double.parseDouble(property.get("topY")) + 0.5 * cellSize;
		double asciiMinY = Double.parseDouble(property.get("bottomY")) - 0.5 * cellSize;

		if (minX > asciiMinX) {
			minX = asciiMinX;
		}
		if (maxX < asciiMaxX) {
			maxX = asciiMaxX;
		}
		if (minY > asciiMinY) {
			minY = asciiMinY;
		}
		if (maxY < asciiMaxY) {
			maxY = asciiMaxY;
		}

		// return clip asciiFile
		return new AsciiBasicControl(ascii.getClipAsciiFile(minX, minY, maxX, maxY));
	}

	// <==========================================>

	/*
	 * 
	 * 
	 */
	// <==========================================>
	// <get center of ascii>
	// <==========================================>
	private void getIntersectProerpty() {
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

	/*
	 */
	// <========================================>
	// <Running SobekModel>
	// <========================================>
	private double sobekRuntimes(AsciiBasicControl ascii, AsciiBasicControl kn)
			throws IOException, InterruptedException {
		String temptSaveAscii = GlobalProperty.saveFolder_tempt + GlobalProperty.saveFile_DelicateDem;
		String temptSaveKn = GlobalProperty.saveFolder_tempt + GlobalProperty.saveFile_DelicateDemKn;

		new AtFileWriter(ascii.getAsciiFile(), temptSaveAscii).textWriter(" ");
		new AtFileWriter(kn.getAsciiFile(), temptSaveKn).textWriter(" ");

		SobekDem sobekDem = new SobekDem();
		sobekDem.addDelicateDem(temptSaveAscii, temptSaveKn);
		sobekDem.start();

		Runtimes sobekRuntimes = new Runtimes();
		sobekRuntimes.RuntimesSetLimit();
		return sobekRuntimes.getSimulateTime();
	}

}
