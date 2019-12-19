package com.bytatech.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Drivo entity.
 */
public class DrivoDTO implements Serializable {

    private Long id;

    private String regNo;

    private String ownerName;

    private String mobileNo;

    private String vehdecscr;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DrivoDTO drivoDTO = (DrivoDTO) o;
        if (drivoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), drivoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DrivoDTO{" +
            "id=" + getId() +
            ", regNo='" + getRegNo() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", mobileNo='" + getMobileNo() + "'" +
            ", vehdecscr='" + getVehdecscr() + "'" +
            "}";
    }
}
