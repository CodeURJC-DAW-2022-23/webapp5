package app.model;

import java.sql.Blob;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    private String mail;
    private String encodedPassword;
    private String name;
    private String lastName;

    @Column(length = 50000)
    private String aboutMe;

    @Column(length = 50000)
    private String billingInformation;

    @ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

    @Lob
	@JsonIgnore
	private Blob profilePirctureFile;
    private String profilePircture;

    public User(){
        
    }

    public User(String mail, String encodedPassword, String name, String lastName, String aboutMe, List<String> roles, Blob profilePirctureFile, String profilePircture) {
        super();
        this.mail = mail;
        this.encodedPassword = encodedPassword;
        this.name = name;
        this.lastName = lastName;
        this.aboutMe = aboutMe;
        this.roles = roles;
        this.profilePirctureFile = profilePirctureFile;
        this.profilePircture = profilePircture;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
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

}
