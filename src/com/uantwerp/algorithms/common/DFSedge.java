package com.uantwerp.algorithms.common;

public class DFSedge {

	private Integer SourceId;
	private String SourceLabel;
	private Integer TargetId;
	private String TargetLabel;
	private boolean isOrdered;
	private boolean isBackward;

	public DFSedge(int sourceId, String sourceLabel, int targetId, String targetLabel, boolean isOrdered) {
		super();
		SourceId = sourceId;
		SourceLabel = sourceLabel;
		TargetId = targetId;
		TargetLabel = targetLabel;
		this.isOrdered = isOrdered;
		this.isBackward = false;
	}

	public DFSedge(Integer sourceId, String sourceLabel, Integer targetId, String targetLabel, boolean isOrdered,
			boolean isBackward) {
		super();
		SourceId = sourceId;
		SourceLabel = sourceLabel;
		TargetId = targetId;
		TargetLabel = targetLabel;
		this.isOrdered = isOrdered;
		this.isBackward = isBackward;
	}

	public DFSedge(int sourceId, String sourceLabel, int targetId, String targetLabel) {
		super();
		SourceId = sourceId;
		SourceLabel = sourceLabel;
		TargetId = targetId;
		TargetLabel = targetLabel;
		this.isOrdered = true;
		this.isBackward = false;
	}

	public int getSourceId() {
		return SourceId;
	}

	public String getSourceLabel() {
		return SourceLabel;
	}

	public int getTargetId() {
		return TargetId;
	}

	public String getTargetLabel() {
		return TargetLabel;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public boolean isBackward() {
		return isBackward;
	}

	public void setSourceId(Integer sourceId) {
		SourceId = sourceId;
	}

	public void setSourceLabel(String sourceLabel) {
		SourceLabel = sourceLabel;
	}

	public void setTargetId(Integer targetId) {
		TargetId = targetId;
	}

	public void setTargetLabel(String targetLabel) {
		TargetLabel = targetLabel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SourceId == null) ? 0 : SourceId.hashCode());
		result = prime * result + ((SourceLabel == null) ? 0 : SourceLabel.hashCode());
		result = prime * result + ((TargetId == null) ? 0 : TargetId.hashCode());
		result = prime * result + ((TargetLabel == null) ? 0 : TargetLabel.hashCode());
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
		DFSedge other = (DFSedge) obj;
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

	@Override
	public String toString() {
		return SourceId + SourceLabel + "-" + TargetId + TargetLabel;
		// return "DFSedge [SourceId=" + SourceId + ", SourceLabel=" +
		// SourceLabel + ", TargetId=" + TargetId
		// + ", TargetLabel=" + TargetLabel + "]";
	}

	public String strDfsEdge() {
		return SourceId + SourceLabel + "-" + TargetId + TargetLabel;
	}

}
