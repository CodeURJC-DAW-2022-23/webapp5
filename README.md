# Key Whale

### Video: 

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
- Purchase

![avatar](screenshots/ER.jpg)

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
This is the main page where the user can view his cart, proceed to checkout and view the available games, and can enter their respective pages to view the specifications, screenshots of the game and its features.
 
![avatar](screenshots/main1.png) 
![avatar](screenshots/main2.png) 
![avatar](screenshots/main3.png) 

**Register:** 
Screen that will allow new users to sing up in KeyWhale. 
 
![avatar](screenshots/register.png) 

**Game:** 
Screen that will display information, images and features about the game. Also, at the bottom of the page reviews of the game will appear. 
 
![avatar](screenshots/game1.png) 
![avatar](screenshots/game2.png) 

**Search:** 
Screen that will display the games that contains the words that you searched. 
 
![avatar](screenshots/search.png)  

**Cart:** 
Page that shows all the games in the cart showing them, their price, and the total price of the cart.
 
![avatar](screenshots/cart.png)  

**Checkout:** 
Page containing a form for the billing information of the user who is going to buy on the site. Unregistered users should check the checkbox for register a new user.
 
![avatar](screenshots/checkout1.png)  
![avatar](screenshots/checkout2.png)   

### Registered users 

**Initial menu:** 
This is the main page where the user can enter his profile, view his cart, proceed to checkout and view the available games, and can enter their respective pages to view the specifications, screenshots of the game and its features.
 
![avatar](screenshots/main1.png) 
![avatar](screenshots/main2.png) 
![avatar](screenshots/main3.png) 
 
**Login:** 
Screen that will allow registered users to log in giving their email and password. All users have access to this screen even though only registered users will be able to end this process. 
 
![avatar](screenshots/login.png) 

**Register:** 
Screen that will allow new users to sing up in KeyWhale. 
 
![avatar](screenshots/register.png) 

**Profile:** 
Screen that will display information, profile logo and number of games purchased about the profile.
 
![avatar](screenshots/profile.png)

**Game:** 
Screen that will display information, images and features about the game. Also, at the bottom of the page reviews of the game will appear. 
 
![avatar](screenshots/game1.png) 
![avatar](screenshots/game2.png) 

**Search:** 
Screen that will display the games that contains the words that you searched. 
 
![avatar](screenshots/search.png)  

**Cart:** 
Page that shows all the games in the cart showing them, their price, and the total price of the cart.
 
![avatar](screenshots/cart.png)  

**Checkout:** 
Page containing a form for the billing information of the user who is going to buy on the site. 
 
![avatar](screenshots/checkout1.png)  
![avatar](screenshots/checkout2.png)   

### Admin

**Initial menu:** 
This is the main page where the admin can enter his profile, view the available games, and add or delete more games, enter their respective pages to view or change the specifications, screenshots of the game and its features.
 
![avatar](screenshots/main1.png) 
![avatar](screenshots/main2.png) 
![avatar](screenshots/main3.png) 
 

**Profile:** 
Screen that will display information and profile logo.
 
![avatar](screenshots/profile.png)

**Game:** 
Screen that will display information, images and features about the game. Also, at the bottom of the page reviews of the game will appear. The admin have control of all the page so he can change any info of the game, delete reviews that are out of context, etc. 
 
![avatar](screenshots/game1.png) 
![avatar](screenshots/game2.png) 

**Search:** 
Screen that will display the games that contains the words that you searched. 
 
![avatar](screenshots/search.png)  

**GameForm:** 
Screen that will allow admins to add a new game with all its features in KeyWhale.
 
![avatar](screenshots/GameForm.png)

## Navigation Diagram
* Blue = Unregistered users.
* Yellow = Registered users.
* Green = Admins.

![diagram](screenshots/NavigationDiagram.png)

# Phase 2

## Navigation

### Unregistered Users

**Initial menu:** 
This is the main page where a user can log in, register if he/she was not already registered, or without doing any of them, see the games but without the possibility to buy them.

![avatar](screenshots/MainPage.png)
![avatar](screenshots/Juegos.png)
![avatar](screenshots/MoreGames.png) 

### Registered Users

**Checkout:**
Page containing a form for the billing information of the user who is going to buy on the site.

![avatar](screenshots/Checkout1png.png)
![avatar](screenshots/Checkout3.png)

### Admin

**Admin Menu:**
This is the main admin page, which an admin will access when logged in.

![avatar](screenshots/AdminMainPage.png) 

**Game Form:**
Page containing a form to create games.

![avatar](screenshots/GameForm1.png) 
![avatar](screenshots/GameForm3.png) 

**Control Panel:**
On this page the admin can modify or delete any game he selects.

![avatar](screenshots/ControlPanel1.png) 
![avatar](screenshots/ControlPanel3.png) 

## Executions instructions

* To start you must download the code from the repository in github, to do this you must go to the link: CodeURJC-DAW-2022-23 /webapp5, once there, you will have to click on <> Code and click on Download ZIP. 

![avatar](screenshots/DownloadProyecto.png)

* In order to run it we use Visual Studio, so once the zip is downloaded and unzipped we open it with Visual Studio. To be able to download it, we looked for in Internet Visual Studio and we get in the main page, if we lowered in the page a little we will see the different versions, we will select the version of Visual Studio Code, if when making click the download does not begin, we began it making click in Downoad. Once it is downloaded we install it in our device.

![avatar](screenshots/Visual.png)
![avatar](screenshots/DownloadVisual.png)
![avatar](screenshots/DownloadVisual2.png)

* Before we can run it we need the version of Maven: 4.0.0, the version of Spring Boot 2.6.3, the version of Java 17 and the version of MySQL 8.0.22. Also if we are going to run it in Visual Studio we will need different extensions:

![avatar](screenshots/Extensions.png)
![avatar](screenshots/ExtensionPackJava.png)
![avatar](screenshots/ExtensionPackSpringBoot.png)
![avatar](screenshots/GitLens.png)

* Once this is done we can run it but for this we must first download Docker Desktop for this we look for this on the internet and access, on this page click on download and ready. 

![avatar](screenshots/DockerDesktop.png)

* Now, we can start Docker Desktop, and once started we go to Visual Studio Code, from here we look almost at the bottom of a file called dockerCommand, copy its contents and paste it into a terminal, this will connect us to the port where the web page and the database of the application. Now we click on the Spring Boot Dasboardy icon and then on Run.

![avatar](screenshots/dockerCommand.png)
![avatar](screenshots/ConexionTerminal.png)
![avatar](screenshots/Dashboard.png)
![avatar](screenshots/Run2.png)

## Diagram with the database entities

![diagram](screenshots/BaseDeDatos.png)

## Class diagram and templates

![diagram](screenshots/DiagramadeClases.png)

## Group members participation
### Brais Cabo Felpete
#### Textual description: 
Brais had done the data base, starter security, connection with the data base, login, register, sample data of the db, models, edit profile, see profile, game page, add games to cart, cart page, add review, review visualization, intial repositories and initial services.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Model creation, Data Base conection and sample data		   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ee0af6f1cd85e2e441085cf31dabd796945e5858 | 
| #2            | Intial App Security | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/aa2d8b6ce5840f01bf212a732e094ffdc45d4cd5 | 
| #3            | Login and Navbar done (registered + unregistered) | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/7a20c8bd4f81d111a7ec17f71b7b8d9bbfb5bf4e | 
| #4            | Register, edit profile and see profile	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/4b73d7932c6a5f1e10144a3d425212a16b1a1709 | 
| #5            | Game Page				   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ef0773341c2037ccdd6db699c46d8cf260728093 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [AjaxController.java](https://github.com/CodeURJC-DAW-2022-23/webapp5/blame/main/backend/src/main/java/app/controller/AjaxController.java)     | 
| #2            | [LoadButton.js](https://github.com/CodeURJC-DAW-2022-23/webapp5/blame/main/backend/src/main/resources/static/loadButton.js)     | 
| #3            | [UserController.java](https://github.com/CodeURJC-DAW-2022-23/webapp5/blame/main/backend/src/main/java/app/controller/UserController.java)     | 
| #4            | [GameRepository.java](https://github.com/CodeURJC-DAW-2022-23/webapp5/blame/main/backend/src/main/java/app/repository/GameRepository.java)     | 
| #5            | [Navbar.html](https://github.com/CodeURJC-DAW-2022-23/webapp5/blob/main/backend/src/main/resources/templates/Navbar.html)     | 






### Sergio Octavio Mancebo
#### Textual description:
Sergio has created some features of the Administrator Control Panel, such as deleting the game from the website. The functionality of being able to remove your own comment from a game.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Delete game on the website	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/f6358ea84cc6ea5addcb63ebcba96f8dbb9a51f4 | 
| #2            | Delete game comment	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/f04114f662d5d49f639eb869b34b77cc98a6aaf3 | 
| #3            | control panel settings, edit game and button to delete game 	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/3cb2636ed03261040175b1ceabfa68ed207bc73a | 
| #4            | searching games + side search 	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/b7789e6ef9db29554fee0b513c85de820f6866f9 | 
| #5            |  add movie ratings, Created delete comment button  	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ae373389c7a6d4da37453fedfe46132cdacfed08 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [AddGameController.java](backend/src/main/java/app/controller/AddGameController.java)     | 
| #2            | [GameController.java](backend/src/main/java/app/controller/GameController.java)     | 
| #3            | [product-info.html](backend/src/main/resources/templates/product-info.html)     | 
| #4            | [ReviewService.java](backend/src/main/java/app/service/ReviewService.java)     | 
| #5            | [controlPanel.html](backend/src/main/resources/templates/controlPanel.html)     |

### Iker Pizarro Fernández
#### Textual description:
Iker has created some funcionalities for the administrator, such as create a new game and edit a game from the website. He also participates in the recommendation algorythm doing the controller and the frontend part.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Create Game  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/df0f6d4ae4ffe805f5cad6031df9a4ec81e452fa | 
| #2            | Create Game with Images | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/001bb156675f9a26bd2923d81363647aaabef9a3  | 
| #3            | Edit Game Backend | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/6743ce40950435145b253a1ab13f405d0179ace7 |
| #4            | Edit Game Frontend   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/c529ef3d2a69ad90790dcdec236c707411d8b317 | 
| #5            | Algorythm Controller | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/750e2d81a02e84ebb36d3edce42d37436c2658af | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            |  [AddGameController.java](backend/src/main/java/app/controller/AddGameController.java)     | 
| #2            |  [newGame.html](backend/src/main/resources/templates/newGame.html)  | 
| #3            |  [editGame.html](backend/src/main/java/app/controller/editGame.html)    | 
| #4            |  [product-info.html](backend/src/main/resources/templates/product-info.html)    | 
| #5            |  [IndexController.java](backend/src/main/java/app/controller/IndexController.java)  |




### Sergio Pérez Sampedro
#### Textual description:
Sergio has conect the back wich the front. This includes the grafics, games of database and algorithm.
 
#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Add the graphic  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/c5870e3477cd2ec81c469b09ce8e5b6d607a8f30 | 
| #2            | Conect the back to the main without bugs  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/4f5765431d7e8721ed0d8db98fcb60b681ac34d4 | 
| #3            | Load more games  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/8f3fff474fcd426ebf4e9ee7613c91a01f464727 | 
| #4            | Recommended algorithm	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/4e6684922fb3b6fc8a4c9bde6ed29222d2635c5e | 
| #5            | All star done	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/d86dc821d7b320504c543d0d791e632482eccc3e | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [product-info.html](backend/src/main/resources/templates/product-info.html)     | 
| #2            | [index.html](backend/src/main/resources/templates/index.html)     | 
| #3            | [IndexController.java](backend/src/main/java/app/controller/IndexController.java)     | 
| #4            | [GameRepository.java](/backend/src/main/java/app/repository/GameRepository.java)     | 
| #5            | [GameService.java](/backend/src/main/java/app/service/GameService.java)     |

### Javier Gaspariño Muñoz
#### Textual description:
Javier has mainly created the checkout functionalities and also the loading plus games and reviews.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Checkout Backend and Frontend, first update   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/67cf44dcb955aade41d7bc003198b77d0e9996a8 | 
| #2            | Fix in the checkout links	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/726084764f1b83f61d5fd290577df2e14a3448e3 | 
| #3            | Save data entered at checkout at a later time	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/770201f4aac34a5eecdd5a708e2c6e87df5a1812#diff-ebbfe20c1e31991e367ec7ea57103db132958dc5591a554261426f43209736d8 | 
| #4            | Sending mail with information after checkout	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/f9dc524b2f87c5a11a96ba59bc463e27460b18c5 | 
| #5            | Option to upload more games and more reviews	   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/2c82ab98886febb3a83ed25b291b340ed898c578#diff-753f0f62b5bf636c1770d655e62d292c8eb00e56e85939e0b8d3079209869f28 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [CheckoutController.java](backend/src/main/java/app/controller/CheckoutController.java)     | 
| #2            | [checkout.html](backend/src/main/resources/templates/checkout.html)     | 
| #3            | [EmailDetails.java](backend/src/main/java/app/Email/EmailDetails.java)     | 
| #4            | [EmailServiceImpl.java](backend/src/main/java/app/Email/EmailServiceImpl.java)     | 
| #5            | [moreGames.html](backend/src/main/resources/templates/moreGames.html)     |

# Phase 3

### Example users credentials

**email**: user1lastname1@gmail.com  -  **Password**: 12345678.

**Username**: admin1adminLastName1@gmail.com  -  **Password**: 12345678.

## API REST Documentation

https://github.com/CodeURJC-DAW-2022-23/webapp5/blob/main/code/backend/api-docs/api-docs.html

https://rawcdn.githack.com/CodeURJC-DAW-2022-23/webapp5/a64e3b3b5d8f8c6f92db4c878d4a8b2ae7100b1e/code/backend/api-docs/api-docs.html

## Instructions for executing the dockerized app
In order to execute the dockerized app you should follow the next steps:
1.	Install Docker Desktop or Docker in linux.
2.	Open any shell of type "bash".
3.	Clone this repository using the command "git clone https://github.com/CodeURJC-DAW-2022-23/webapp5.git".
4.	Execute the command cd/webapp5/code/docker.
5.	Execute the command "docker-compose up" on the shell.
6.	When the app is running, open any browser and search "https://localhost:8443".

## Instructions for constructing the docker image
In order to create the docker image you should follow the next steps:
1.	Install Docker Desktop or Docker in linux.
2.	Create and account on Dockerhub or connect to your account.
3.	Open any shell of type "bash".
4.	Clone this repository using the command "git clone https://github.com/CodeURJC-DAW-2022-23/webapp5.git".
5.	Execute the command cd/webapp5/code/docker.
6.	Execute the command "./create_image.sh DockerhubUsername/ImageName".

## Class diagram and templates

![diagram](screenshots/DiagramaDeClases3.png)

## Group members participation
### Brais Cabo Felpete
#### Textual description:
Brais has created the postman collection, everything necesary to dockerize the application and the security of the Api Rest.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Postman Collection | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/4d95a15a725d74cd0e0506cce1de7a8bdd8170f5 |
| #2            | DockerFile Creation  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/3524e64406457b8f26686f8bf8d5a1410784ac89  | 
| #3            | Docker-Compose | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/c67963e6ae822ee53a09b7d1f833f0e649e8f463 | 
| #4            | Api Rest Security | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/955e908e5a60719a830d9aca754e02c71e2ce358 | 
| #5            |  |  | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [Dockerfile](code/docker/Dockerfile) | 
| #2            | [docker-compose.yml](code/docker/docker-compose.yml)     | 
| #3            | [railway.toml](code/railway.toml) | 
| #4            | [KeyWhale.postman_collection.json](KeyWhale.postman_collection.json) | 
| #5            | [RestSecurityConfig.java](code/backend/src/main/java/app/security/RestSecurityConfig.java) |


### Sergio Octavio Mancebo

#### The five most important commits:
Sergio has created some of the methods of the games in the rest api, also some of the user and the review.

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Fix GameRestController errors		   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/80aeab57a302918b23d3e3515a859682c647704b| 
| #2            | UserRestController GetMapping	 | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/16e7aae9c098bd2a16d02663f9be0e7b800d8fde  | 
| #3            | GameDetails Last Version 	| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/f8f3e151be44edf8036383e6d5e6cdf80575faaa | 
| #4            | Fix ReviewRestController getMapping id errors| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/94b51d1d3ccfb3967dde4cde2803bb1402475fd6 | 
| #5            | RestSecurityConfig fix errors| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/e580597a7d3716014a71a2746cc1248d7fa79bb7 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [GameRestController](code/backend/src/main/java/app/controller/restController/GameRestController.java) | 
| #2            | [UserRestController](code/backend/src/main/java/app/controller/restController/UserRestController.java)   | 
| #3            | [RestControllerConfig](code/backend/src/main/java/app/security/RestSecurityConfig.java) | 
| #4            | [Readme](README.md) |




### Iker Pizarro Fernández
#### Textual description: 
Iker has created some method of the review rest api and the game.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | ReviewRestController First Creation and GetMapping		   | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ecdd779cf5e670b8d524a278014c849b1b4bfda7 | 
| #2            | ReviewRestController Post Mapping	 | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/42067bcfad5ce6838b1e5ac9f0e4a509d91aa29d  | 
| #3            | ReviewRestController Last Version with DeleteMapping	| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/3febca6d4894979c5bb6ae59eb53ca37254e0748 | 
| #4            | GameRestController First Creation (New game and Show game)| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/4300fef5a840ffc0615f8eb58e7173692b13f5da | 
| #5            | GameRestController Last version (Edit game and Delete game)| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/14eb011f5282e8cd9e525aed1e40fd98a4a0fffd | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [ReviewRestController](code/backend/src/main/java/app/controller/restController/ReviewRestController.java) | 
| #2            | [GameRestController](code/backend/src/main/java/app/controller/restController/GameRestController.java)   | 
| #3            | [Readme](README.md) | 
| #4            | | 
| #5            | | 

### Sergio Pérez Sampedro
#### Textual description: 
Sergio has documented the application with open-api.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | 	  Docs final in local |  https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/8ab874203f8282441395f94a9d66f09ea3d6d536 | 
| #2            |  Documentation| https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/04011510cbfa7199c1c1022e9e222808c32cf743 | 
| #3            | conect to open API|  https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/73ecd7b1534d0fc86ed4cc8786366893df417ca0| 
| #4            |  | | 
| #5            | | | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            |[GameRestController](code/backend/src/main/java/app/controller/restController/GameRestController.java) | 
| #2            |[ReviewRestController](code/backend/src/main/java/app/controller/restController/ReviewRestController.java)     | 
| #3            | [UserRestController](code/backend/src/main/java/app/controller/restController/UserRestController.java)| 
| #4            | [LoginRestController](code/backend/src/main/java/app/controller/restController/LoginRestController.java)| 
| #5            | [Readme](README.md) | 

### Javier Gaspariño Muñoz
#### Textual description: 
Javier has created the class diagram updated with rest api and necesary methods for the user rest api.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | UserRestController First Creation and Changes | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/602f03779c8c08938a3da5c7b11c5dc35f0a7009 | 
| #2            | UserRestController Functions and More Changes | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/664ef2ac3158651ce3cd7e858d4c6c72571390eb | 
| #3            | UserRestController Ultimate Changes | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/71c876c20ec859c71d7d1b67f984fa2f4b0bd0d9 | 
| #4            | UserProfile | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/e0e05ff92be04c1f84f163c7b81545f9c48bc8ce | 
| #5            | Class Diagram Updated Added | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/517eb57ccc78b8034b3e431eb30cb6e060b4c3b0 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [UserRestController](code/backend/src/main/java/app/controller/restController/UserRestController.java) | 
| #2            | [UserProfile](code/backend/src/main/java/app/model/modelRest/UserProfile.java) |
| #3            | [ClassDiagram](screenshots/DiagramaDeClases3.png)| 
| #4            | [Readme](README.md) | 

# Phase 4

## Preparation of the development environment

1. Install Docker Desktop.
2. Create and account on Dockerhub.
3. Open any shell of type "bash".
4. Clone this repository using the command "git clone https://github.com/CodeURJC-DAW-2022-23/webapp5.git".
5. Use command "cd webapp5/code/docker" .
6. Execute the command "docker-compose up" on the shell.
7. Open another shell in the root of the project.
8. Execute the command "cd code/frontend".
9. Execute the command "npm install".
10. Execute the command "npm start".

## SPA class diagram and templates

![diagram](screenshots/DiagramaClases3.png)

## Group members participation
### Brais Cabo Felpete
#### Textual description:
Brais has done the dockerfile to deploy the angular app, in the angular project he seted the project up and did the register and the navbar. He also implemented the register, the app routing, the security and the component to edit the user.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Docker Deployment  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/e8485fb155545a3ee90ab826b286ea44467c1cf8 |
| #2            | App Routing | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/66330871e3426344c839093b7cdc8d054a1ee0ee | 
| #3            | Register Page | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/bcdd6f965cc4c2a14274b78d3a24b472b9caefb9 | 
| #4            | Login | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/bcdd6f965cc4c2a14274b78d3a24b472b9caefb9 | 
| #5            | Edit Profile | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/281f73454c09c08278e825306e6028a0bad08de7 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [Dockerfile](code/docker/Dockerfile) | 
| #2            | [docker-compose.yml](code/docker/docker-compose.yml) | 
| #3            | [GameComponent](code/frontend/src/app/components/games/game-page.component.ts) |
| #4            | [MainPage](code/frontend/src/app/components/games/games.component.ts) |
| #5            | [Register](code/frontend/src/app/components/auth/register.component.html) |


### Sergio Octavio Mancebo
#### Textual description: 


#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            |  |  | 
| #2            |  |  | 
| #3            |  |  | 
| #4            |  |  | 
| #5            |  |  | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            |  | 
| #2            |  | 
| #3            |  | 
| #4            |  |
| #5            |  | 



### Iker Pizarro Fernández
#### Textual description: 
Iker has made the edit-game and add-game front with Angular.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Add Game Front | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/a16d707c8004a322223b3491e721bf24889f9e28#diff-eb715df54195b66df020ea446cf297ad03657233f1d17269bee9d20502a7e75f | 
| #2            | Add Game Front Final Release  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ffbc429d9ad8e827bded2bb0c9fa86d990acfe5e | 
| #3            | Edit Game Front | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/9b8a2ef6a55856efe58dae181b0888acf61bee85 | 
| #4            | Edit Game Front Final Release  | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ae9e463df5fdfe173c0fc352264220bc0aa03202#diff-44db907bfdc2a278fb08caa5a50449ccaf6654550f0cb91012a061be29182787 | 
| #5            | Edit Game Images Fix | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/6ca4957b7fcdb7eddf5dd81d7604313df13b6e18 | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [Add game ts](code/frontend/src/app/components/games/add-game.component.ts) | 
| #2            | [Add game html](code/frontend/src/app/components/games/add-game.component.html) | 
| #3            | [Edit game ts](code/frontend/src/app/components/games/edit-game.component.ts) | 
| #4            | [Edit game html](code/frontend/src/app/components/games/edit-game.component.html) | 
| #5            | [Game Service ts](code/frontend/src/app/services/game.service.ts) | 
 

### Sergio Pérez Sampedro
#### Textual description: 
Sergio has made the carrusel the cart and video.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | 	  Video |   | 
| #2            |  Carrusel | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/5528ac9bfe5be0a9597c15931d35817d9f30fbd4 | 
| #3            | Cart |  https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/422991cd842e4834c3da9c2120cb7885557e08c1 | 
| #4            |  | | 
| #5            | | | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            |[ReviewRestController](code/backend/src/main/java/app/controller/restController/ReviewRestController.java) | 
| #2            |[game-page.component](code/frontend/src/app/components/games/game-page.component.html)     | 
| #3            | [game.service](code/frontend/src/app/services/game.service.ts)| 
| #4            | [cart.component](code/frontend/src/app/components/users/cart.component.html)| 
| #5            | [Readme](README.md) | 


### Javier Gaspariño Muñoz
#### Textual description: 
Javier has made the navbar cart improvements, the checkout. He also collaborated in the edit game and made the class diagram.

#### The five most important commits:

| Commit number | Description                                      | Link                                                                                            |
| ------------- | ------------------------------------------------ | ----------------------------------------------------------------------------------------------- |
| #1            | Navbar Cart Improvement | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/327dccd752e1f9d340c7aa626487c3dceafccfb8 | 
| #2            | Checkout | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/2e4868ffd6959bb6fb9170eed566eb452faf351b#diff-6b8da21fcb1421857b98413e6f40d52d14596e393cf766d992ef47c7404a7104 | 
| #3            | Edit Game Components and Service creation | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/9b8a2ef6a55856efe58dae181b0888acf61bee85 | 
| #4            | Updates and Finish Edit Game | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/ae9e463df5fdfe173c0fc352264220bc0aa03202 | 
| #5            | Class Diagram Phase 4 | https://github.com/CodeURJC-DAW-2022-23/webapp5/commit/596175b91f985003cd3d980c1da9270c0231852b | 


#### The five most participated files:

| File number | File               |
| ------------- | ------------------ |
| #1            | [Navbar ts](code/frontend/src/app/components/navbar/navbar.component.ts) | 
| #2            | [Edit game ts](code/frontend/src/app/components/games/edit-game.component.ts) | 
| #3            | [Checkout html](code/frontend/src/app/components/users/checkout.component.html) | 
| #4            | [Checkout ts](code/frontend/src/app/components/users/checkout.component.ts) | 
| #5            | [Class Diagram](screenshots/DiagramaClases3.png) | 
