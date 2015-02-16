package uk.me.thega.model.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link SizeCalculator}.
 *
 * @author pwhittlesea
 *
 */
public class SizeCalculatorTest {

	@Test
	public void testDataConversionByte() {
		assertEquals("1.0 B", runCalc(1));
	}

	@Test
	public void testDataConversionGByte() {
		assertEquals("1.0 GB", runCalc(1 * 1000 * 1000 * 1000));
	}

	@Test
	public void testDataConversionKByte() {
		assertEquals("1.0 KB", runCalc(1 * 1000));
	}

	@Test
	public void testDataConversionMByte() {
		assertEquals("1.0 MB", runCalc(1 * 1000 * 1000));
	}

	@Test
	public void testDataConversionTByte() {
		assertEquals("2.0 TB", runCalc(2L * 1000 * 1000 * 1000 * 1000));
	}

	@Test
	public void testDataConversionZero() {
		assertEquals("-", runCalc(0));
	}

	private String runCalc(final float a) {
		return SizeCalculator.getStringSizeLengthFile((long) a);
	}
}
