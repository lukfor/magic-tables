# lukfor-tables


Simple java API to read, transform, sort, filter and aggregate tables.

## Reading data

```java
Table table = TableBuilder.fromCsvFile("data/dummy.csv", ',');
table.printSummary();
```

```java
Table table = TableBuilder.fromXlsFile("data/dummy.xls");
```

Default `TableBuilder` tries to find the correct datatype for each column. But you can also change the type of a column manually:

```java
table.setColumnType("id", ColumnType.INTEGER);
table.setColumnType("population", ColumnType.DOUBLE);
table.setColumnType("city", ColumnType.STRING);
```

## Inspecting data

```java
Object o = table.get(rowIndex, "colum_name")
Object o = table.get(rowIndex, columnIndex)
```

`getRow` provided typesafe methods:
```java
table.getRow(rowIndex).getObject("colum_name");
table.getRow(rowIndex).getInteger("colum_name");
table.getRow(rowIndex).getDouble("colum_name");
table.getRow(rowIndex).getString("colum_name");
```

```java
table.print()
table.printFirst(n)
table.printLast(n)
table.getColumns().getNames()
table.getColumns().getTypes()
table.getColumns().getSize()
table.getColumns().get(name).print()
table.getColumns().get(name).getSummary()
table.getColumns().get(name).getMean()
table.getColumns().get(name).getMin()
table.getColumns().get(name).getMax()
table.getColumns().get(name).getMedian()
table.getColumns().get(name).getMissingValues()
table.getColumns().get(name).getUniqueValues() table.getRows().getAll("colum_name", "value")
table.getRows().getAllByRegEx("colum_name", "value|value2")
table.getRows().getSize()
```

## Cleaning data


```java
table.getRows().selectByRegEx("colum_name", "a|b")
table.getRows().select(filter)
table.getRows().select(bitmask)
```

```java
table.getRows().dropByRegEx("colum_name", "a|b")
table.getRows().drop(filter)
table.getRows().drop(bitmask)
```

Special functions:

```java
table.getRows().dropMissings();
table.getRows().dropMissings("colum_name");
table.getRows().dropDuplicates()
table.getColumns().fillMissings("value");
table.getColumns().get("colum_name").fillMissings("value");
``



## Transforming data

```java
table.getColumns().select("colum_name1","colum_name2","colum_name3", ...)
table.getColumns().selectByRegEx("col*");
table.getColumns().select(filter);
table.getColumns().drop("colum_name1","colum_name2","colum_name3", ...)
table.getColumns().dropByRegEx("col*");
table.getColumns().drop(filter);
table.getColumns().append("colum_name", builder())
table.getRows().append(row)
```

## Joining and reshaping

```java
table.melt(..) (new table?)
table.merge(table2, LEFT | RIGHT | OUTER | INNER) (new table?) ok
table.pivot(..) ????
table.concat(table2) (new table?) ok
```

## Sorting data

```java
table.getRows().sortAscBy("colum_name");
table.getRows().sortDescBy("colum_name");
```

## Aggregating data

```java
table.groupBy("colum_name", Aggregation.SUM);
table.groupBy("colum_name", Aggregation.COUNT)
table.groupBy("colum_name", Aggregation.MAX)
table.groupBy("colum_name", Aggregation.MIN)
table.groupBy("colum_name", Aggregation.MEAN)
table.groupBy("colum_name", Aggregation.MEDIAN)
table.groupBy(function(), function())
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

import genepi.tables.*
import genepi.tables.io.*
import genepi.tables.rows.*
import genepi.tables.columns.*
import genepi.tables.columns.types.*

Table table = TableBuilder.fromCsvFile("data/dummy.csv", ',' as char);

table.getColumns().append(new StringColumn("id_2"), new IValueBuilder() {
  public String buildValue(Row row) throws IOException {
    return row.getInteger("id") * 2 + "";
  }
});

TableWriter.writeToCsv(table, "results/dummy.csv", ',' as char);
```
