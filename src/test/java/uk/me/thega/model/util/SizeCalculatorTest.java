package uk.me.thega.model.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class SizeCalculatorTest {

	@Test
	public void testDataConversion() {
		assertEquals("-", runCalc(0));
		assertEquals("1 byte", runCalc(1));
		assertEquals("10.00 bytes", runCalc(10));
		assertEquals("1.00 Kb", runCalc(SizeCalculator.sizeKb));
		assertEquals("1.00 KB", runCalc(SizeCalculator.sizeKB));
		assertEquals("1.00 MB", runCalc(SizeCalculator.sizeMB));
		assertEquals("1.00 GB", runCalc(SizeCalculator.sizeGB));
		assertEquals(">1 TB", runCalc(SizeCalculator.sizeTB));
	}

	private String runCalc(final float a) {
		return SizeCalculator.getStringSizeLengthFile((long) a);
	}
}
