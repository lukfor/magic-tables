# lukfor-tables


Simple java API to read, transform, sort, filter and aggregate tables.

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
@Grab('lukfor:tables:0.0.1')

import lukfor.tables.*
import lukfor.tables.io.*
import lukfor.tables.rows.*
import lukfor.tables.columns.*
import lukfor.tables.columns.types.*

Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();

table.getColumns().append(new StringColumn("id_2"), new IValueBuilder() {
  public String buildValue(Row row) throws IOException {
    return row.getInteger("id") * 2 + "";
  }
});

TableWriter.writeToCsv(table, "results/dummy.csv", ',' as char);
```
