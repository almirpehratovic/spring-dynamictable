package com.springtutor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="book")
public class Book {
	private int id;
	private String title;
	private String description;
	private Date releaseDate;
	private float price;
	private float rating;
	private int numberOfReviewers;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="title")
	@NotEmpty(message="Title is mandatory")
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="description")
	@NotEmpty(message="Description is mandatory")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@Column(name="release_date")
	@NotNull(message="Release Date is mandatory")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Column(name="price")
	@NotNull(message="Price is mandatory")
	@Min(value=0, message="Minimal price is 0")
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	@Column(name="rating")
	@Min(value=1, message="Minimal rating is 1")
	@Max(value=10, message="Maximal rating is 10")
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	@Column(name="number_of_reviewers")
	public int getNumberOfReviewers() {
		return numberOfReviewers;
	}
	public void setNumberOfReviewers(int numberOfReviewers) {
		this.numberOfReviewers = numberOfReviewers;
	}
	
	
}
