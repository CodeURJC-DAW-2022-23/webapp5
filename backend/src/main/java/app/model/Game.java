package app.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private Long id;

	private String name;
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	private float price;
	private String category;
	private String os;
	private String processor;
	private String memory;
	private String graphics;
	private String directX;
	private String network;
	private String hardDrive;
	private String soundCard;

	@ElementCollection(fetch=FetchType.LAZY)
	@Column(name = "gameplayImages", columnDefinition = "TEXT")
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

	public Game() {

	}

	public Game(String category, String name, float price, String os, String processor, String memory, String directX, String network, String hardDrive, String soundCard, String graphics, String description) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.os = os;
		this.graphics = graphics;
		this.processor = processor;
		this.memory = memory;
		this.directX = directX;
		this.network = network;
		this.hardDrive = hardDrive;
		this.soundCard = soundCard;
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemory() {
		return this.memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
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

	public String getOs() {
		return this.os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getProcessor() {
		return this.processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getGraphics() {
		return this.graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
	}

	public String getDirectX() {
		return this.directX;
	}

	public void setDirectX(String directX) {
		this.directX = directX;
	}

	public String getNetwork() {
		return this.network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getHardDrive() {
		return this.hardDrive;
	}

	public void setHardDrive(String hardDrive) {
		this.hardDrive = hardDrive;
	}

	public String getSoundCard() {
		return this.soundCard;
	}

	public void setSoundCard(String soundCard) {
		this.soundCard = soundCard;
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

	public void editGame(Game game){
		this.setName(game.getName());
		this.setCategory(game.getCategory());
		this.setDirectX(game.getDirectX());
		this.setSoundCard(game.getSoundCard());
		this.setProcessor(game.getProcessor());
		this.setMemory(game.getMemory());
		this.setGraphics(game.getGraphics());
		this.setHardDrive(game.getHardDrive());
		this.setOs(game.getOs());
		this.setPrice(game.getPrice());
		this.setDescription(game.getDescription());
		this.setNetwork(game.getNetwork());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Game)) {
			return false;
		}
		Game game = (Game) o;
		return Objects.equals(id, game.id) && Objects.equals(name, game.name) && Objects.equals(description, game.description) && price == game.price && Objects.equals(category, game.category) && Objects.equals(os, game.os) && Objects.equals(processor, game.processor) && Objects.equals(memory, game.memory) && Objects.equals(graphics, game.graphics) && Objects.equals(directX, game.directX) && Objects.equals(network, game.network) && Objects.equals(hardDrive, game.hardDrive) && Objects.equals(soundCard, game.soundCard) && Objects.equals(gameplayImages, game.gameplayImages) && Objects.equals(titleImageFile, game.titleImageFile) && Objects.equals(titleImage, game.titleImage) && Objects.equals(gameplayImagesFiles, game.gameplayImagesFiles) && Objects.equals(reviews, game.reviews);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, price, category, os, processor, memory, graphics, directX, network, hardDrive, soundCard, gameplayImages, titleImageFile, titleImage, gameplayImagesFiles, reviews);
	}



}