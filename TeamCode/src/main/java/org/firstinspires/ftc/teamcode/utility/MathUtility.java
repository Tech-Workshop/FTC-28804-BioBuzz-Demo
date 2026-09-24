package org.firstinspires.ftc.teamcode.utility;

import java.util.Collections;
import java.util.List;

public class MathUtility {
	public static double clamp(double num, double min, double max) {
		return Math.max(min, Math.min(num, max));
	}

	public static double secondsElapsed(double scheduledTime) {
		return (System.nanoTime()-scheduledTime)/1000000000;
	}

	//Normalize angle from -pi to pi
	public static double normalizeAngle(double angle) {
		if (Math.abs(angle) <= Math.PI) {
			return angle;
		}

		if (angle > (2 * Math.PI)) {
			angle -= (2 * Math.PI);
		} else if (angle < (-2 * Math.PI)) {
			angle += (2 * Math.PI);
		}

		if (angle > Math.PI) {
			angle = -((2 * Math.PI) - angle);
		} else if (angle < -Math.PI) {
			angle = angle + (2 * Math.PI);
		}

		return angle;
	}

	public static double getCorrectedImuHeading(double imuHeadingRadians, double imuOffsetRadians) {
		double correctedHeadingRadians = imuHeadingRadians + imuOffsetRadians;
		if (correctedHeadingRadians > Math.PI) {
			correctedHeadingRadians -= 2 * Math.PI;
		} else if (correctedHeadingRadians < -Math.PI) {
			correctedHeadingRadians += 2 * Math.PI;

		}
		return correctedHeadingRadians;
	}

	public static double differenceRadians(double angleRadians1, double angleRadians2) {
		double diff = (angleRadians2 - angleRadians1) % (2 * Math.PI);
		return Math.min(diff, 2 * Math.PI - diff);
	}

	public static double calculateFilteredAverage(List<Double> data) {
		if (data == null || data.size() < 3) return 0.0; // Need minimum data

		// 1. Sort the data
		Collections.sort(data);

		// 2. Determine how many to trim
		int trimCount = (int) Math.round(data.size() * 0.25);

		// 3. Create sublist excluding outliers
		List<Double> trimmedList = data.subList(trimCount, data.size() - trimCount);

		// 4. Calculate average
		double sum = 0;
		for (double val : trimmedList) {
			sum += val;
		}
		return sum / trimmedList.size();
	}
}
