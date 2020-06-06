# Magic Tables :tophat::rabbit::chart_with_upwards_trend:



Simple DSL and java API to read, transform, sort, filter and aggregate tables.


## Getting started

### Dependency

Add the following repository to your pom.xml or gradle file:

```
<repository>
  <id>bintray-lukfor-maven</id>
  <name>bintray</name>
  <url>https://dl.bintray.com/lukfor/maven</url>
</repository>
```

Add the following dependecy:

```
<dependency>
  <groupId>lukfor</groupId>
  <artifactId>magic-tables</artifactId>
  <version>0.2.0</version>
</dependency>
```

### Scripts (Experimental)

Run a magic-tables script on the command line:

```
magic-tables examples/hello-tables.mtbl
```

Combine markdown with magic-tables and produce a html report:

```
magic-tables examples/hello-tables.mtbl.md
```


## Reading data

```java
Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
Table table = TableBuilder.fromCsvFile("data/dummy.csv").withSeparator('\t').load();
table.printSummary();
table.print()
```

```java
Table table = TableBuilder.fromXlsFile("data/dummy.xls").load();
```

By default `TableBuilder` tries to find the correct datatype for each column. But you can also change the type of a column manually:

```java
table.setColumnType("id", ColumnType.INTEGER);
table.setColumnType("population", ColumnType.DOUBLE);
table.setColumnType("city", ColumnType.STRING);
```

In addition you can disable `columnTypeDetection` and load all columns as strings:

```java
Table table = TableBuilder.fromXlsFile("data/dummy.xls").withColumnTypeDetection(false).load();
```

## Inspecting data

```java
Object o = table.get(rowIndex, "column_name")
Object o = table.get(rowIndex, columnIndex)
```

`getRow` provides typesafe methods:
```java
table.getRow(rowIndex).getObject("column_name");
table.getRow(rowIndex).getInteger("column_name");
table.getRow(rowIndex).getDouble("column_name");
table.getRow(rowIndex).getString("column_name");
```

```java
table.print() (prints first 25 rows)
table.printFirst(n)
table.printLast(n)
table.printAll()
table.printBetween(index1, index2)
table.getColumns().getMissings()
table.getColumns().getUniqueValues()
table.getColumns().getNames()
table.getColumns().getTypes()
table.getColumns().getSize()
table.getColumn("column_name").print()
table.getColumn("column_name").getSummary()
table.getColumn("column_name").getMean()
table.getColumn("column_name").getMin()
table.getColumn("column_name").getMax()
table.getColumn("column_name").getMissings()
table.getColumn("column_name").getUniqueValues()
table.getRows().getAll("column_name", "value")
table.getRows().getAllByRegEx("column_name", "value|value2")
table.getRows().getSize()
```

## Cleaning data


```java
table.getRows().selectByRegEx("column_name", "a|b")
table.getRows().select(filter)
table.getRows().select(bitmask)
table.getRows().dropByRegEx("column_name", "a|b")
table.getRows().drop(filter)
table.getRows().drop(bitmask)
```

```java
table.getColumns().select("name1","name2","name3", ...)
table.getColumns().selectByRegEx("col_.*");
table.getColumns().select(filter);
table.getColumns().drop("name1","name2","name3", ...)
table.getColumns().dropByRegEx("col*");
table.getColumns().drop(filter);
```

Special functions:

```java
table.getRows().dropMissings();
table.getRows().dropMissings("column_name");
table.getRows().dropDuplicates()
table.fillMissings("value");
table.getColumn("column_name").fillMissings("value");
table.replaceValue("old","new");
table.getColumn("column_name").replaceValue("old","new");
table.getColumn("column_name").apply(function);
```

## Transforming data

```java
table.getColumns().append("column_name", builder())
table.getColumns().rename("column_name", "new_name")
table.getRows().append(row)
```

## Sorting data

```java
table.getRows().sortAscBy("column_name");
table.getRows().sortDescBy("column_name");
```

## Joining and reshaping

```java
table.append(table2)

table.melt(..) *todo*
table.merge(table2, LEFT);
table.merge( RIGHT | OUTER | INNER) *todo*
table.pivot(..) *todo*
```

## Aggregating data

```java
table.groupBy("column_name").count()
table.groupBy("column_name").sum("value_name")
table.groupBy("column_name").min("value_name")
table.groupBy("column_name").max("value_name")
table.groupBy("column_name").mean("value_name")
table.groupBy(mapper(), aggregator())
```

## Writing data

```java
TableWriter.writeToCsv(table, "id.csv");
```

```java
TableWriter.writeToXls(table, "id.csv");
```


## Groovy Support

```sh
$ curl -s "https://get.sdkman.io" | bash
```

```sh
sdk install groovy
```

```groovy
@GrabResolver(name='genepi', root='https://raw.github.com/genepi/maven-repository/mvn-repo/')
@Grab('lukfor:magic-tables:0.0.2')

import lukfor.tables.*
import lukfor.tables.io.*
import lukfor.tables.rows.*
import lukfor.tables.columns.*
import lukfor.tables.columns.types.*

Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();

table.getColumns().append(new IntegerColumn("id_2"), new IBuildValueFunction() {
  public Integer buildValue(Row row) throws IOException {
    return row.getInteger("id") * 2;
  }
});

table.printSummary();
table.print();

table.getRows().selectByRegEx("b", "r|z");
table.print();

table.getColumns().selectByRegEx("id.*");
table.print();
```
