package uk.me.thega.model;

/**
 * Implementing objects can be discontinued.
 */
public interface Discontinuable {

	/**
	 * Returns true if the item is discontinued.
	 * 
	 * @return true if discontinued.
	 */
	boolean isDiscontinued();

	/**
	 * Set if this item is discontinued.
	 * 
	 * @param isDiscontinued
	 *            true if discontinued.
	 */
	void setDiscontinued(final boolean isDiscontinued);
}
