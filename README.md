spring_reference_project
========================

A Spring reference project

Overview
--------

NOTE: There are many things that are in common with the jee_referece_project. In the future the common resources are going to be placed in a separeate project.

The purpose of this project is to "steal" code from it.
Basically tries to accumulate concepts, ideas and corresponding solutions inspired mostly by real life situations.

There is an emphasis on pointing out important parts ([POI](#poi)): non-trivial/hard-to-remember solutions to problems, unintuitive or strange behaviour, some useful features which are likely to be unknown to most - or really anything that can be considered important when working on an application.

The project itself has no purpose regarding functionality. It is buildable and deployable however. There's really no point in listing the features, what's interesting is the [Technologies](#technologies) that are being used in this project.

Technologies
------------

- Spring
  - XML configuraion, annotation driven resolving, EJB integration
- EJB integration
  - MDB (JMS consumer)
- AOP
  - Spring AOP (proxy)
  - AspectJ (Load time weaving)
- RESTful web services (Spring MVC)
  - Exception handling (Via ```@ControllerAdvice```)
- REST client
- Persistence
  - Spring Data JPA Repository
  - JPA
      - Entity
          - OneToOne
          - OneToMany, ManyToOne
          - ManyToMany
          - IdClass
      - Metamodel
      - CRUD
      - Criteria API
      - NamedQuery
      - Explicit optimistic lock
- Drools
- Guvnor (untested)
- JMS message producer
- Distributed transaction
- Json serializer/deserializer

TODO technologies
-----------------
- Use Spring template for JMS message producer
- Spring batch (JBatch)
- UI
- Security
- Entity inheritence
- SOAP
- RMI

POI
---
The ```spring.reference.meta``` package contains various structures to help point out important/interesting parts of the project.

A prime example is the ```@POI``` annotation and the ```POITag``` enum. The ```@POI``` annotation is used to annotate points of interest, and the ```POITag``` enum is simply a list of keywords a subset of which can be used inside ```@POI```.

There are other similar structures as well. (```@NOTE```, ```@TODO``` etc.)

The concept here is to add typesafe meta information. The main advantage is (compared to using simple comments instead) is that they can be refactored and also the project can be queried for certain keywords easily with the help of any IDE.

Required to deploy
------------------
- Queue
  - queue/admin
- Datasource
  - java:jboss/datasources/AdminDS (Referenced from persistence.xml)
- JDBC driver
  - org.h2.jdbcx.JdbcDataSource

  When deployed from IDE
  ----------------------
  Make sure that the ```target/generated-sources/annotations``` (contains the JPA metamodels) is on the source path.

Tested on
---------
JBoss AS 7.1.1
