package app.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

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

	@ElementCollection(fetch=FetchType.LAZY)
	@Column(length = 50000)
	private List<String> gameplayImages = new ArrayList<>();

	@Lob
	@JsonIgnore
	private Blob titleImageFile;
	private String titleImage;

	@Lob
	@JsonIgnore
	@ElementCollection(fetch=FetchType.LAZY)
	private List<Blob> gameplayImagesFiles = new ArrayList<>();

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();

	@Column(length = 50000)
	private String minimumRequirements;

	public Game() {

	}

	public Game(String name, String description, float price, String category, Blob titleImageFile, String titleImage, List<Blob> gameplayImagesFiles, List<String> gameplayImages, String minimumRequirements) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.titleImageFile = titleImageFile;
		this.titleImage = titleImage;
		this.gameplayImagesFiles = gameplayImagesFiles;
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


	public Blob getTitleImageFile() {
		return this.titleImageFile;
	}

	public void setTitleImageFile(Blob titleImageFile) {
		this.titleImageFile = titleImageFile;
	}

	public String getTitleImage() {
		return this.titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}

	public List<Blob> getGameplayImagesFiles() {
		return this.gameplayImagesFiles;
	}

	public void setGameplayImagesFiles(List<Blob> gameplayImagesFiles) {
		this.gameplayImagesFiles = gameplayImagesFiles;
	}

	public List<String> getGameplayImages() {
		return this.gameplayImages;
	}

	public void setGameplayImages(List<String> gameplayImages) {
		this.gameplayImages = gameplayImages;
	}

	public void addGamplayImage(Blob gameplayImage) {
		this.gameplayImagesFiles.add(gameplayImage);
	}

	public void addReview(Review review) {
		this.reviews.add(review);
	}


}
