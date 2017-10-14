package au.edu.unsw.soacourse.rest;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="licenses")
public class LicenseBean {
	
	@Id
	private String licenseNumber;
	@Column(name="name")
	private String name;
	@Column(name="address")
	private String address;
	@Column(name="class")
	private String lclass;
	@Column(name="email")
	private String email;
	@Column(name="expirydate")
	private Date expiryDate;
	
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLclass() {
		return lclass;
	}
	public void setLclass(String lclass) {
		this.lclass = lclass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	

}
