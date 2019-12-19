package com.bytatech.service.dto;

public class DrivoData {

   
	private String regNo;
	private String ownerName;
	private String mobileNo;
	private String vehdecscr;
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getVehdecscr() {
		return vehdecscr;
	}
	public void setVehdecscr(String vehdecscr) {
		this.vehdecscr = vehdecscr;
	}
	@Override
	public String toString() {
		return "Drivo [regNo=" + regNo + ", ownerName=" + ownerName + ", mobileNo=" + mobileNo + ", vehdecscr="
				+ vehdecscr + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		result = prime * result + ((regNo == null) ? 0 : regNo.hashCode());
		result = prime * result + ((vehdecscr == null) ? 0 : vehdecscr.hashCode());
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
		DrivoData other = (DrivoData) obj;
		if (mobileNo == null) {
			if (other.mobileNo != null)
				return false;
		} else if (!mobileNo.equals(other.mobileNo))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		if (regNo == null) {
			if (other.regNo != null)
				return false;
		} else if (!regNo.equals(other.regNo))
			return false;
		if (vehdecscr == null) {
			if (other.vehdecscr != null)
				return false;
		} else if (!vehdecscr.equals(other.vehdecscr))
			return false;
		return true;
	}
	
}
