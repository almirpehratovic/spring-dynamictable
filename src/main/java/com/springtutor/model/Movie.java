package com.springtutor.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="movie")
public class Movie {
	private int id;
	private String name;
	private Date releaseDate;
	private String description;
	private String wiki;
	private String actors;
	private String director;
	private byte[] image;
	private String trailerUrl;
	private int version;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="name")
	@NotEmpty(message="{validation.field.notempty}")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="{validation.field.notempty}")
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@Column(name="release_date")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Column(name="description")
	@NotEmpty(message="{validation.field.notempty}")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Basic(fetch=FetchType.LAZY)
	@Lob
	@Column(name="image")
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	@Column(name="trailer_url")
	@NotEmpty(message="{validation.field.notempty}")
	public String getTrailerUrl() {
		return trailerUrl;
	}
	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}
	
	@Version
	@Column(name="version")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	@Column(name="actors")
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	
	@Column(name="director")
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	
	@Column(name="wiki")
	public String getWiki() {
		return wiki;
	}
	public void setWiki(String wiki) {
		this.wiki = wiki;
	}
	public String toString() {
		return "[" + id + " " + name + "]"; 
	}
	
}
