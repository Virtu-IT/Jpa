package be.abis.exercise.model;

import jakarta.persistence.*;

// BEFORE: a plain class without an id (name, telephoneNumber, vatNr, address); Address was a plain class too.
// NOW: Company is an entity with a generated id, and Address is an @Embeddable stored in the same table.
@Entity
@Table(name = "COMPANIES")
public class Company{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONO")
	private int companyNr;
	@Column(name = "CONAME")
	private String name;
	@Column(name = "COTEL")
	private String telephoneNumber;
	@Column(name = "COVAT")
	private String vatNr;
	@Embedded
	private Address address;

	public Company(){}
	public Company(String name, String telephoneNumber, String vatNr, Address address) {
		this.name = name;
		this.telephoneNumber = telephoneNumber;
		this.vatNr = vatNr;
		this.address = address;
	}

	public int getCompanyNr() {
		return companyNr;
	}
	public void setCompanyNr(int companyNr) {
		this.companyNr = companyNr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getVatNr() {
		return vatNr;
	}
	public void setVatNr(String vatNr) {
		this.vatNr = vatNr;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public String toString(){
		return name + (address != null ? " in " + address.getTown() : "");
	}

}
