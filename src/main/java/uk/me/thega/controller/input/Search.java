package uk.me.thega.controller.input;

public class Search {

	private String[] dataset;

	private String fileName;

	private boolean isInvalid;

	public String[] getDataset() {
		return dataset;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isInvalid() {
		return isInvalid;
	}

	public void setDataset(final String[] dataset) {
		this.dataset = dataset;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public void setInvalid(final boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Search: ").append(getFileName()).append(" ");
		if (dataset != null) {
			for (int i = 0; i <= dataset.length; i++) {
				sb.append(dataset[i]);
			}
		}
		sb.append(" isInvalid[").append(Boolean.toString(isInvalid())).append(']');
		return sb.toString();
	}
}
