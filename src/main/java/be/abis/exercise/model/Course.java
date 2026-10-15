package be.abis.exercise.model;

import jakarta.persistence.*;

// BEFORE: a plain class without any annotations (fields identical, no mapping to the DB):
//     public class Course {
//         private int courseId;
//         private String shortTitle; ...
@Entity
@Table(name = "COURSES")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CID")
	private int courseId;
	@Column(name = "CSTITLE")
	private String shortTitle;
	@Column(name = "CLTITLE")
	private String longTitle;
	@Column(name = "CDUR")
	private int numberOfDays;
	@Column(name = "CAPRICE")
	private double pricePerDay;

	public Course(){}

	public Course(int courseId, String shortTitle, String longTitle, int numberOfDays, double pricePerDay) {
		super();
		this.courseId = courseId;
		this.shortTitle = shortTitle;
		this.longTitle = longTitle;
		this.numberOfDays = numberOfDays;
		this.pricePerDay = pricePerDay;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getLongTitle() {
		return longTitle;
	}
	public void setLongTitle(String longTitle) {
		this.longTitle = longTitle;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public double getPricePerDay() {
		return pricePerDay;
	}
	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

}
