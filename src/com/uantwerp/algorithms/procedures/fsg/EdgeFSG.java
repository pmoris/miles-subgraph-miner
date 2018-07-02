package com.uantwerp.algorithms.procedures.fsg;

public class EdgeFSG {

	private String SourceId;
	private String SourceLabel;
	private String TargetId;
	private String TargetLabel;

	public EdgeFSG() {
		super();
	}

	public EdgeFSG(String sourceId, String sourceLabel, String targetId, String targetLabel) {
		super();
		SourceId = sourceId;
		SourceLabel = sourceLabel;
		TargetId = targetId;
		TargetLabel = targetLabel;
	}

	public String getSourceId() {
		return SourceId;
	}

	public void setSourceId(String sourceId) {
		SourceId = sourceId;
	}

	public String getSourceLabel() {
		return SourceLabel;
	}

	public void setSourceLabel(String sourceLabel) {
		SourceLabel = sourceLabel;
	}

	public String getTargetId() {
		return TargetId;
	}

	public void setTargetId(String targetId) {
		TargetId = targetId;
	}

	public String getTargetLabel() {
		return TargetLabel;
	}

	public void setTargetLabel(String targetLabel) {
		TargetLabel = targetLabel;
	}

	@Override
	public String toString() {
		return SourceId + SourceLabel + "-" + TargetId + TargetLabel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SourceId == null) ? 0 : SourceId.hashCode());
		result = prime * result + ((SourceLabel == null) ? 0 : SourceLabel.hashCode());
		result = prime * result + ((TargetId == null) ? 0 : TargetId.hashCode()) * 2;
		result = prime * result + ((TargetLabel == null) ? 0 : TargetLabel.hashCode()) * 2;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeFSG other = (EdgeFSG) obj;
		if (SourceId == null) {
			if (other.SourceId != null)
				return false;
		} else if (!SourceId.equals(other.SourceId))
			return false;
		if (SourceLabel == null) {
			if (other.SourceLabel != null)
				return false;
		} else if (!SourceLabel.equals(other.SourceLabel))
			return false;
		if (TargetId == null) {
			if (other.TargetId != null)
				return false;
		} else if (!TargetId.equals(other.TargetId))
			return false;
		if (TargetLabel == null) {
			if (other.TargetLabel != null)
				return false;
		} else if (!TargetLabel.equals(other.TargetLabel))
			return false;
		return true;
	}

}
