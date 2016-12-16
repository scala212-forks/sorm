#SORM

SORM is an object-relational mapping framework designed to eliminate boilerplate and maximize productivity. It is absolutely abstracted from relational side, automatically creating database tables, emitting queries, inserting, updating and deleting records. This all functionality is presented to the user with a simple API around standard Scala's case classes. 

For more info, tutorials and documentation please visit the [official site](http://sorm-framework.org).

##Supported databases

* MySQL
* PostgreSQL
* H2
* HSQLDB
* Oracle (experimental)

##Supported Scala versions

2.12 (use org.sorm-framework/sorm for Scala 2.10 and 2.11) github.com/scala212-forks

##Maven

SORM is distributed in Maven Central, here's a dependency to the latest release version:

    <dependency>
      <groupId>com.github.scala212-forks</groupId>
      <artifactId>sorm</artifactId>
      <version>0.3.22-SNAPSHOT</version>
    </dependency>

##SBT

    libraryDependencies += "com.github.scala212-forks" %% "sorm" % "0.3.22-SNAPSHOT"

---

[![Build Status](https://travis-ci.org/scala212-forks/sorm.png?branch=master)](https://travis-ci.org/scala212-forks/sorm)
