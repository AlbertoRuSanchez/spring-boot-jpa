# Spring JPA example

## ¿Why this project?

In order to have a good base of knowledge of Spring framework, it is mandatory to know about the interaction between the application and database with Spring Data JPA module. Nowadays, the use of JPA is really extended and if you want to reach any Java job opportunity out there, better you know how to deal with it.

## ¿What is Spring Data JPA?

Spring Data JPA is a module from Spring framework that helps you to implement in your application, a persistence layer to work with objects mapped from database tables, and all the base necessary to interact with your database trying to avoid boilerplate code and guiding you to a better segregation of the classes to access data.

In old apps, we had to write every sentence manually to communicate to the database, letting to developers all the responsibility to make it clean and coherent but, sadly and frequently, ending in caos.

For Details, check the official [Spring Data JPA Docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#preface)

## Example of implementation

#### To start the application

To start the application you need to install docker on your machine, move in a terminal to the folder 'docker' and set

`docker compose up`

And of course you need also maven and jdk11.

### Database creation from entity classes

Using this property you can create/update tables and relationships between them based on the entities' configuration.

`
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.hibernate.ddl-auto=update
`

It means that you can manage the database from your application avoiding to connect to the server directly for it.

#### DDBB Relational Schema
[![dbschema.png](https://i.postimg.cc/LspMHxvM/dbschema.png)](https://postimg.cc/ZWVQHF1f)

This is the example of table relations that I chose. It is simple, but enough to implement one relation of every kind.

    - @OneToOne
    - @OneToMany
    - @ManyToOne
    - @ManyToMany


### Some interesting JPA annotations

#### Cascade
When you persist and object to database, you can configure it to auto-update the parent. For example, if you create a new customer object that has a relationship to invoice by OneToMany, with cascade you can set a list of invoices to the customer, then save de customer on database, and table invoice will get persisted with those invoices.

There are several types of cascade configurations, depending on the operation to database that you want it to fire the automation. They must be included in the parent and they are:

- CascadeType.ALL
- CascadeType.PERSIST
- CascadeType.MERGE
- CascadeType.REMOVE
- CascadeType.REFRESH
- CascadeType.DETACH

For example: 

`    
@OneToMany (mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
private Set<Invoice invoices = new HashSet<>();
`

#### Fetching collections

In relationships between entities, we can configure the way that the data from the external entity will get fetched to the object.

We have EAGER and LAZY.

EAGER will field the data as soon as the object gets initialized. For example:

`@OneToMany (fetch = FetchType.EAGER)`

LAZY will field the data just on demand of that data. This is by default in OneToMany or ManyToMany relationships. For example:

`@OneToMany (fetch = FetchType.LAZY)`










