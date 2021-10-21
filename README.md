# Deck of Cards Games

<div id="top"></div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#limitations">Limitations</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This project is a REST API for a basic cards game.
It was developed as a homework assignment, which a partial requirement for the LogmeIn selection process.

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

* [Java](https://www.java.com/)
* [Spring](https://spring.io/)
* [H2](https://www.h2database.com/html/main.html)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

* Java 11 (or higher)

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/wnoizumi/cardgames.git
   ```
2. Enter the project dir
   ```sh
   cd cardgames
   ```
3. Run Maven install
   ```sh
   mvnw install
   ```
   
### Running Automated Tests

1. Run Maven test in the project dir
   ```sh
   mvnw test
   ```
   
<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE -->
## Basic Usage

For the sake of simplicity, the project is configured to run with a development profile using the in-memory H2 database. 
To startup the project in an embedded server, please follow the steps below:

1. Run Spring-boot run
   ```sh
   mvnw spring-boot:run
   ```
2. The API will be available at 8080 port. In case you need to use another port, please change the server.port property in the [application.properties](https://github.com/wnoizumi/cardgames/blob/main/src/main/resources/application.properties) file.
   ```sh
   server.port=8080
   ```
3. Explore the API using swagger or another tool (e.g., curl, postman, etc)
  - All the APIs will be under [localhost:8080/api](http://localhost:8080/api):
    - [localhost:8080/api/games](http://localhost:8080/api/games)  
    - [localhost:8080/api/decks](http://localhost:8080/api/decks)
    - [localhost:8080/api/players](http://localhost:8080/api/players)
  - (Recommended) To navigate the API using swagger, pleace access: [localhost:8080/swagger-ui](http://localhost:8080/swagger-ui);  
  - To inspect the H2 database, pleace access: [localhost:8080/h2-console](http://localhost:8080/h2-console). The database credentials are defined in the [application.properties](https://github.com/wnoizumi/cardgames/blob/main/src/main/resources/application.properties) file.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- MAPPING OF FEATURES -->
## Features

Next, I present a mapping of features to the API. 

- **Create a game:** POST Request to [localhost:8080/api/games](http://localhost:8080/api/games)  
- **Delete a game:** DELETE Request to [localhost:8080/api/games/{uuid}](http://localhost:8080/api/games/{uuid}) 
- **Create a deck:** POST Request to [localhost:8080/api/decks](http://localhost:8080/api/decks) 
- **Add a deck to a game deck:** POST Request to [localhost:8080/api/games/{uuid}/decks](http://localhost:8080/api/games/{uuid}/decks) 
- **Add player to a game:** POST Request to [localhost:8080/api/players](http://localhost:8080/api/players) 
- **Remove player from a game:** DELETE Request to [localhost:8080/api/players/{uuid}](http://localhost:8080/api/players/{uuid}) 
- **Deal a card to a player in a game from the game deck:** POST Request to [localhost:8080/api/players/{uuid}/cards](http://localhost:8080/api/players/{uuid}/cards) 
- **Get the list of cards for a player:** GET Request to [localhost:8080/api/players/{uuid}/cards](http://localhost:8080/api/players/{uuid}/cards) 
- **Get the list of players in a game along with the total added value of all the cards each player holds:** GET Request to [localhost:8080/api/players/](http://localhost:8080/api/players/) passing the game uuid as a query parameter
- **Get the count of how many cards per suit are left undealt in the game deck:**  GET Request to [localhost:8080/api/games/{uuid}/suits](http://localhost:8080/api/games/{uuid}/suits)
- **Get the count of each card (suit and value) remaining in the game deck sorted by suit and face value from high value to low value:** GET Request to [localhost:8080/api/games/{uuid}/suitsFaces](http://localhost:8080/api/games/{uuid}/suitsFaces)
- **Shuffle the game deck:**  PATCH Request to [localhost:8080/api/games/{uuid}](http://localhost:8080/api/games/{uuid}) passing shuffle as the operation in the body 

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- LIMITATIONS -->
## Limitations

Next, I present the known limitations and trade-offs considered during the development of this project. 

- [] 
- [] 
- [] 
    - [] 

<p align="right">(<a href="#top">back to top</a>)</p>
