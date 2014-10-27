package uk.me.thega.model.util;

import java.text.DecimalFormat;

/**
 * Util for calculating the size of a file as a string.
 *
 * @author pwhittlesea
 *
 */
public class SizeCalculator {

	/**
	 * Get the string representation for a file length in the format 0.00 N where N is the unit.
	 *
	 * @param bytes the number of bytes to represent.
	 * @return the string representation.
	 */
	public static String getStringSizeLengthFile(final double bytes) {
		if (bytes == 0) {
			return "-";
		}
		if (bytes < BYTES_IN) {
			return df.format(bytes) + " B";
		}
		final double kilobytes = (bytes / BYTES_IN);
		if (kilobytes < BYTES_IN) {
			return df.format(kilobytes) + " KB";
		}
		final double megabytes = (kilobytes / BYTES_IN);
		if (megabytes < BYTES_IN) {
			return df.format(megabytes) + " MB";
		}
		final double gigabytes = (megabytes / BYTES_IN);
		if (gigabytes < BYTES_IN) {
			return df.format(gigabytes) + " GB";
		}
		final double terabytes = (gigabytes / BYTES_IN);
		return df.format(terabytes) + " TB";
	}

	/** File systems divide by 1000. */
	private static final int BYTES_IN = 1000;

	/** formatter for the unit. */
	private static final DecimalFormat df = new DecimalFormat("0.0");

}
