# lukfor-tables


Simple java API to read, transform, sort, filter and aggregate tables.

## Reading data

```java
Table table = TableBuilder.fromCsvFile("data/dummy.csv").load();
Table table = TableBuilder.fromCsvFile("data/dummy.csv").withSeparator('\t').load();
table.printSummary();
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
Object o = table.get(rowIndex, "colum_name")
Object o = table.get(rowIndex, columnIndex)
```

`getRow` provides typesafe methods:
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
table.getColumns().getMissings()
table.getColumns().getUniqueValues()
table.getColumns().getNames()
table.getColumns().getTypes()
table.getColumns().getSize()
table.getColumns().get(name).print()
table.getColumns().get(name).getSummary()
table.getColumns().get(name).getMean()
table.getColumns().get(name).getMin()
table.getColumns().get(name).getMax()
table.getColumns().get(name).getMedian()
table.getColumns().get(name).getMissings()
table.getColumns().get(name).getUniqueValues()
table.getRows().getAll("colum_name", "value")
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
table.fillMissings("value");
table.getColumns().get("colum_name").fillMissings("value");
table.getColumns().replaceValue("old","new");
```



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
table.melt(..) *todo*
table.merge(table2, LEFT);
table.merge | RIGHT | OUTER | INNER) *todo*
table.pivot(..) *todo*
table.concat(table2) (or append?) *todo*
```

## Sorting data

```java
table.getRows().sortAscBy("colum_name");
table.getRows().sortDescBy("colum_name");
```

## Aggregating data

```java
table.groupBy("colum_name", Aggregation.SUM); *todo*
table.groupBy("colum_name", Aggregation.COUNT)
table.groupBy("colum_name", Aggregation.MAX)  *todo*
table.groupBy("colum_name", Aggregation.MIN)  *todo*
table.groupBy("colum_name", Aggregation.MEAN)  *todo*
table.groupBy("colum_name", Aggregation.MEDIAN)  *todo*
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

import lukfor.tables.*
import lukfor.tables.io.*
import lukfor.tables.rows.*
import lukfor.tables.columns.*
import lukfor.tables.columns.types.*

Table table = TableBuilder.fromCsvFile("data/dummy.csv", ',' as char);

table.getColumns().append(new StringColumn("id_2"), new IValueBuilder() {
  public String buildValue(Row row) throws IOException {
    return row.getInteger("id") * 2 + "";
  }
});

TableWriter.writeToCsv(table, "results/dummy.csv", ',' as char);
```
