# Fo'REST
## Contents
---
- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Getting Started](#getting-started)

- [Usage](#usage)

- [User API](#user-api)

- [License](#license)
---
## [Project Description](#project-description)
A java-based ORM for seamless access to a database. Users can persist classes and objects through the use of methods and annotations. This effectively relieves users from the burden of using a Query Language.

## [Technologies Used](#technologies-used)
* PostgreSQL - version 42.4.0  
* Java - version 8.0  
* Apache commons - version 2.9.0
* Maven

## [Features](#features)  
* Easy to integrate API through annotations and credential set-up.
* All queries are handled via method calls, which means no need for understanding a Query Language
* Annotations are plain in name and use, allowing for little confusion on interpretation. 
* Credential set-up is done through a Configuration object, which stores data like the root and password to the database.
* Operations are executed through a Session object, which performs CRUD operations on the database.

To-do list: [`for future iterations`]
* Mapping of join columns inside of entities.    
* Implementation of aggregate functions.  
* Implementation of Multiplicity Annotations such as OneToMany, ManyToOne, OneToOne, and ManyToMany.
* Support multiple different SQL Vendors
* Add a `DEFAULT` constraint

## [Getting Started](#getting-started) 
  
## [Usage](#usage)  

## [User API](#user-api)

## [License](#license)

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
