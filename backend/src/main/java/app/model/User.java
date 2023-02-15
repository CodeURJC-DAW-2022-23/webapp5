package app.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class User{

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private String mail;
    private String encodedPassword;
    private String name;
    private String lastName;

    @Column(name = "aboutMe", columnDefinition = "TEXT")
    private String aboutMe;

    @Column(name = "billingInformation", columnDefinition = "TEXT")
    private String billingInformation;

    @ElementCollection(fetch=FetchType.EAGER)
	private List<String> roles;
    
    @OneToMany
	private List<Game> cart = new ArrayList<>();

    @Lob
	@JsonIgnore
	private Blob profilePirctureFile;
    private String profilePircture;

    private float totalPrice = 0;

    public User(){
        
    }

    public User(String name, String lastName, String encodedPassword,  String aboutMe) {
		this.name = name;
		this.encodedPassword = encodedPassword;
        this.aboutMe = aboutMe;
        this.lastName = lastName;
	}

    public User(String name, String lastName, String mail, String encodedPassword,  String aboutMe, String... roles) {
		this.name = name;
		this.mail = mail;
		this.encodedPassword = encodedPassword;
		this.roles = List.of(roles);
        this.aboutMe = aboutMe;
        this.lastName = lastName;
	}


    public List<Game> getCart() {
        return this.cart;
    }

    public void setCart(List<Game> cart) {
        this.cart = cart;
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getEncodedPassword() {
        return this.encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAboutMe() {
        return this.aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getBillingInformation() {
        return this.billingInformation;
    }

    public void setBillingInformation(String billingInformation) {
        this.billingInformation = billingInformation;
    }

    public List<String> getRoles() {
        for (String string : roles) {
            System.out.println(string);
        }
        return this.roles;
    }

    public void setRoles(String ... roles) {
        this.roles = List.of(roles);
    }

    public Blob getProfilePirctureFile() {
        return this.profilePirctureFile;
    }

    public void setProfilePirctureFile(Blob profilePirctureFile) {
        this.profilePirctureFile = profilePirctureFile;
    }


    public String getProfilePircture() {
        return this.profilePircture;
    }

    public void setProfilePircture(String profilePircture) {
        this.profilePircture = profilePircture;
    }

    public void addGameToCart(Game game){
        this.cart.add(game);
        this.totalPrice += game.getPrice();
    }

    public void removeGameFromCart(Game game){
        this.cart.remove(game);
        this.totalPrice -= game.getPrice();
    }

}
