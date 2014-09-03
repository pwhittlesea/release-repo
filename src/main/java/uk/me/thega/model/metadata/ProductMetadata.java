package uk.me.thega.model.metadata;

import javax.xml.bind.annotation.XmlRootElement;

import uk.me.thega.model.Discontinuable;
import uk.me.thega.model.Named;

@XmlRootElement
public class ProductMetadata implements Discontinuable, Named {

	private boolean discontinued = false;

	private String name;

	public ProductMetadata() {
	}

	public ProductMetadata(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(final boolean isDiscontinued) {
		this.discontinued = isDiscontinued;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
