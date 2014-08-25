package uk.me.thega.model.util;

import java.text.DecimalFormat;

public class SizeCalculator {

	static float sizeGB;
	static float sizeKb;
	static float sizeKB;
	static float sizeMB;
	static float sizeTB;
	private static DecimalFormat df;

	static {
		df = new DecimalFormat("0.00");

		sizeKb = 1024.0f;
		final float sizeMb = sizeKb * sizeKb;
		final float sizeGb = sizeMb * sizeKb;
		final float sizeTb = sizeGb * sizeKb;

		final float sizeB = 8.0f;
		sizeKB = sizeKb * sizeB;
		sizeMB = sizeMb * sizeB;
		sizeGB = sizeGb * sizeB;
		sizeTB = sizeTb * sizeB;
	}

	public static String getStringSizeLengthFile(final long size) {
		if (size == 0.0f) {
			return "-";
		} else if (size == 1.0f) {
			return "1 byte";
		} else if (size < sizeKb) {
			return df.format(size) + " bytes";
		} else if (size < sizeKB) {
			return df.format(size / sizeKb) + " Kb";
		} else if (size < sizeMB) {
			return df.format(size / sizeKB) + " KB";
		} else if (size < sizeGB) {
			return df.format(size / sizeMB) + " MB";
		} else if (size < sizeTB) {
			return df.format(size / sizeGB) + " GB";
		}
		return ">1 TB";
	}

}
