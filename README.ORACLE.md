# Using Oracle with PCF

This branch contains a modified version of `pal-tracker` that uses Oracle
instead of MySQL/MariaDB.

The changes in the Java code are mainly due to the slightly different
behaviour of Oracle with respect to MySQL. In particular:

* It is not valid to use the keyword `date` as a column name, so this was changed
  to `entry_date`.
* The default textual date format that Oracle will parse is `DD-MMM-YYYY`,
  e.g. `25-DEC-2019` rather than `2019-12-25`.
* In "raw" JDBC numeric values are retrieved as `BigDecimal` types and dates
  as `java.sql.Timestamp`.
  
These changes (apart from the column name) primarily affect the test code.
The main program code is almost completely unchanged.
In particular, there is no Oracle-specific code required in order to have
the application running under PCF find and bind to an Oracle database.

## Local development and testing

Local development and testing was done with a locally running
[Oracle Express Edition (XE)](https://www.oracle.com/database/technologies/xe-downloads.html)
instance.
This can be installed directly on Windows or RedHat-like Linux (e.g. Oracle
Enterprise Linux, Fedora, Centos).
On MacOS I built and ran it in a Docker container using the instructions from
[this GitHub repository](https://github.com/fuzziebrain/docker-oracle-xe).

The `databases/tracker/create-oracle-users.sql` script can be used to
set up `tracker_dev` and `tracker_test` users in the local database.
Note that you should run this using a DBA account against the
"Pluggable Database" (PDB) instance (`XEPDB1`) rather the the core `XE` instance.

The table definitions in `V1__initial_schema.sql` have been translated to
be as close to the original MySQL definition as possible, using a trigger
and sequence to do the auto-increment of the ID column.

Flyway can be used to run the migrations but the `migrate-database.sh`
script has *not* been converted for use with Oracle as the `cf ssh` mechanism
would be unlikely to be used with a user-provided service for an externally
hosted Oracle database.
In such cases, flyway could be used directly against the database.

## Oracle JDBC library

Note that the Oracle JDBC library (the dependency `com.oracle:ojdbc8:18.3`)
will not be found in any of the public Maven repositories, because of Oracle's
licensing conditions.
I downloaded and installed a copy in my local Maven cache using the command:

```bash
mvn install:install-file \
  -Dfile=$HOME/Downloads/ojdbc8.jar \
  -DgroupId=com.oracle \
  -DartifactId=ojdbc8 \
  -Dversion=18.3 \
  -Dpackaging=jar
```

The addition of `mavenLocal()` to the `repositories` section in the Gradle
build file enables Gradle to pick it up from this location.
In an enterprise setting, it is likely that the library would be hosted in
an internal Maven repository.

## User-provided service

The simplest user-provided service definition that works with this version
of the application merely supplies a `jdbcUrl` with embedded username
and password, as follows:

```bash
cf create-user-provided-service tracker-database \
  -p '{"jdbcUrl": "jdbc:oracle:thin:tracker/supersecret@//db-host.somewhere.com:1521/DBSID"}'
```

Many other mechanisms could be used, including providing a value for
`spring.datasource.url` via properities, a config server or the environment.