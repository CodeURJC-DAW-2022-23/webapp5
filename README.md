# Key Whale

## Table of contents

- [Phase 0](#phase-0)
    - [Members](#members)
    - [Entities](#entities)
    - [Users](#users)
    - [User permissions](#user-permissions)
    - [Pictures](#pictures)
    - [Charts](#charts)
    - [Complementary Technology](#complementary-technology)
    - [Algorithm](#algorithm)

## Phase 0

### Members

| Name                      | e-mail                            | GitHub username      |
| ------------------------- | --------------------------------- | ---------------      |
| Sergio Pérez Sampedro     | s.perezs.2020@alumnos.urjc.es     | lord-47              |
| Brais Cabo Felpete        | b.cabo.2020@alumnos.urjc.es       | BraisCabo            |
| Javier Gaspariño Muñoz    | j.gasparino.2020@alumnos.urjc.es  | JavierGasparinoMunoz |
| Iker Pizarro Fernández    | i.pizarro.2020@alumnos.urjc.es    | pizarroiker          |
| Sergio Octavio Mancebo    | s.octavio.2018@alumnos.urjc.es    | sergio-octavio       |

### Entities

- User
- Game
- Game Review
- Game Company
- Purchase

User purchase game. Game has reviews. Companies own games.

#### Users

- **Anonymous**: This type of users will be able to see the games of the sore. However, they will not be able to purchase games and he will not receive recomended games.

- **Registered**: They will be able to purchase games, change user profile picture and review purchased games.

- **Admin**: They will be able to add/delete and edit games. They will also be able to delete and edit reviews of the games and crete new game companies.

### User permissions

Users can write an assessment about a film, having also to rate it. They can modify their assessments at any moment.

### Pictures

The entity ‘Users’ may have one profile picture. The entity 'game company' will have a logo picture. The entity 'game' will have title picture and in game images.

### Charts

There will be a bar chart representing the user stars of the games.

### Complementary Technology

The web page will send emails to users notifying the purchase they have done.

### Algorithm

Search films by title. Furthermore, the application will show games recomendation based on user prefferences.


# Phase 1

## Web pages layout using HTML and CSS

### Unregistered users 

**Initial menu:** 
This is the main page where the user can enter his profile, view his cart, proceed to checkout and view the available games, and can enter their respective pages to view the specifications, screenshots of the game and its features.
 
![avatar](screenshots/main1.png) 
![avatar](screenshots/main2.png) 
![avatar](screenshots/main3.png) 
 
**Login:** 
Screen that will allow registered users to log in giving their email and password. All users have access to this screen even though only registered users will be able to end this process. 
 
![avatar](ImagesPhase3/Login.png) 

 **Login error:** 
Screen that will appear when the user is trying to login and he writes his credentials wrong. 

![avatar](ImagesPhase3/ErrorLogin.png) 

**Register:** 
Screen that will allow new users to sing up in Advice.me and access exclusive content and services. 
 
![avatar](ImagesPhase3/Register.png) 

**Taken User Name:** 
Screen that will apear when he tries to register with a nickname that is already taken. 

![avatar](ImagesPhase3/TakenUserName.png) 

**Film description:** 
Screen that will display information, image and assessments about the film. Also, at the bottom of the page related films will appear. 
 
![avatar](ImagesPhase3/FilmUnregistered.png) 

**Search film:** 
Screen that will display the films that contains the words that you searched. 
 
![avatar](ImagesPhase3/SearchFilms.png) 

### Registered users 

**Initial menu:** 
First screen of the webpage that contains a list of films splitted by genre and a circle graph that informs the most famous film genres of Advice.me. Moreover, this screen will have personaliced recomendations for the user and access to their account. 
 
![avatar](ImagesPhase3/MenuRegistered.png) 

**Profile:** 
Screen that will display the users information and profile picture. Also they will be able to access the modification profile information and change their profile picture. 
 
![avatar](ImagesPhase3/Profile.png) 

**Edit profile:** 
Screen that will allow the user to  modify their information. 
 
![avatar](ImagesPhase3/EditProfile.png) 

**Change password:** 
Screen that will allow the user to change his password. 

![avatar](ImagesPhase3/ChangePassword.png) 

**Error old password:** 
Screen that will appear when the user is trying to change the password and writes his old password wrong.  

![avatar](ImagesPhase3/ErrorOldPassword.png) 

**Film description:** 
Screen that will display information, images and assessments about the film. Also, at the bottom of the page related films will appear. Registered users can add an assessment to the film if they did not do it before. 
 
![avatar](ImagesPhase3/FilmRegistered.png) 

**Add comment:** 
Screen that will allow the user to upload a comment writing his opinion and adding a rating of the film. 

![avatar](ImagesPhase3/AddComment.png) 

**Edit comment:** 
Screen that will allow the user to modify their comments. 

![avatar](ImagesPhase3/EditComment.png) 

**Watch profile:** 
Screen that will allow the user to watch others profiles without the edit profile and the remove/edit comments buttons. Also the user can follow the other user with a button. 

![avatar](ImagesPhase3/WatchProfile.png) 

**Followers:** 
Screen that will allow the user to watch the users who follow him. 

![avatar](ImagesPhase3/Followers.png) 

**Following:** 
Screen that will allow the user to watch the users you follow. 

![avatar](ImagesPhase3/Following.png) 

**Search films:** 
Screen that will allow the user to watch a list of films that contains a certain word/words in their tittle. 

![avatar](ImagesPhase3/SearchFilmRegistered.png) 

### Admin 

**Initial menu:** 
First screen of the webpage that contains a list of films splitted by genre and a circle graph that informs the most famous film genres of Advice.me. Admins will have access to the "Post film" button. 
 
![avatar](ImagesPhase3/MenuAdmin.png) 

**Add film:** 
Screen that will allow admins to add films to the database. 
 
![avatar](ImagesPhase3/AddFilm.png) 

**Film description:** 
Screen that will display information, images and assessments about the film. Admins can eliminate the film and its assesments. Also, admins will be able to enter in the edit film page. 
 
![avatar](ImagesPhase3/FilmAdmin.png) 

**Edit film:** 
Screen that will allow admins to edit the information from any film and save it in the database. 
 
![avatar](ImagesPhase3/EditFilm.png)

**Search films:** 
Screen that will allow the user to watch a list of films that contains a certain word/words in their tittle. 
 
![avatar](ImagesPhase3/SearchFilmAdmin.png)

## Navigation Diagram
* Blue = Unregistered users.
* Yellow = Registered users.
* Green = Admins.
* Red = Error pages.

![diagram](ImagesPhase3/NavigationDiagram.png)

