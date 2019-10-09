# pal-tracker experiments

This is a version of the PAL course `pal-tracker` that contains a number of
experients and variations from the reference version. These include:

* Handling of dates in both US and ISO format using a custom JSON
  deserialiser (on the `master` branch).
* An implementation of the `JdbcTimeEntryRepository` using [JDBI](http://jdbi.org)
  rather than `JDBCTemplate`.
  This is also on the `master` branch.
* A version of the application using Oracle instead of MySQL.
  This is on the `oracle` branch.
* A JPA-based repository.
  This is on the `jpa-repository` branch.