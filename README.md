# Magic Tables :tophat::rabbit::chart_with_upwards_trend:

![build](https://github.com/lukfor/magic-tables/workflows/build/badge.svg)
[ ![Download](https://api.bintray.com/packages/lukfor/maven/magic-tables/images/download.svg) ](https://bintray.com/lukfor/maven/magic-tables/_latestVersion)

> Simple java API to read, transform, sort, filter and aggregate tables.


## Setup

Add the following repository to your pom.xml or gradle file:

```
<repository>
  <id>bintray-lukfor-maven</id>
  <name>bintray</name>
  <url>https://dl.bintray.com/lukfor/maven</url>
</repository>
```

Add the following dependency:

```
<dependency>
  <groupId>com.github.lukfor</groupId>
  <artifactId>magic-tables</artifactId>
  <version>0.2.1</version>
</dependency>
```

## Usage


### Reading data

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

### Writing data

```java
TableWriter.writeToCsv(table, "id.csv");
```

```java
TableWriter.writeToXls(table, "id.csv");
```

### Inspecting data

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
table.print() //(prints first 25 rows)
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

### Cleaning data


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

### Transforming data

```java
table.getColumns().append("column_name", builder())
table.getColumns().rename("column_name", "new_name")
table.getRows().append(row)
```

### Sorting data

```java
table.getRows().sortAscBy("column_name");
table.getRows().sortDescBy("column_name");
```

### Joining and reshaping

```java
table.append(table2)

table1.merge(table2, column); //left join on table1.column = table.column
table1.merge(table2, column1, column2); //left join on table1.column1 = table2.column2
```

Work in progress:

```java
table.melt(..)
table.merge(table, column,  LEFT | RIGHT | OUTER | INNER
```

### Aggregating data

```java
table.groupBy("column_name").count()
table.groupBy("column_name").sum("value_name")
table.groupBy("column_name").min("value_name")
table.groupBy("column_name").max("value_name")
table.groupBy("column_name").mean("value_name")
table.groupBy(mapper(), aggregator())
```


## License

`magic-tables` is MIT Licensed.

