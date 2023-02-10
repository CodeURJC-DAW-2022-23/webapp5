package app.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	@Column(length = 50000)
	private String description;
	private float price;
	private String category;

	@Lob
	@JsonIgnore
	private Blob titleImage;

	@Lob
	@JsonIgnore
	private List<Blob> gameplayImages =  new ArrayList<>();

	@OneToMany
	private List<Review> reviews = new ArrayList<>();

	@Column(length = 50000)
	private String minimumRequirements;

	public Game() {

	}

	public Game(String name, String description, float price, String category, Blob titleImage, List<Blob> gameplayImages, String minimumRequirements) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.titleImage = titleImage;
		this.gameplayImages = gameplayImages;
		this.minimumRequirements = minimumRequirements;
	}


	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Blob getTitleImage() {
		return this.titleImage;
	}

	public void setTitleImage(Blob titleImage) {
		this.titleImage = titleImage;
	}

	public List<Blob> getGameplayImages() {
		return this.gameplayImages;
	}

	public void setGameplayImages(List<Blob> gameplayImages) {
		this.gameplayImages = gameplayImages;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public String getMinimumRequirements() {
		return this.minimumRequirements;
	}

	public void setMinimumRequirements(String minimumRequirements) {
		this.minimumRequirements = minimumRequirements;
	}

}
