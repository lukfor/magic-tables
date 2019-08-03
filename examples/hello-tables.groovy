@GrabResolver(name='genepi', root='https://raw.github.com/genepi/maven-repository/mvn-repo/')
@Grab('lukfor:tables:0.0.2')

import lukfor.tables.*
import lukfor.tables.io.*
import lukfor.tables.rows.*
import lukfor.tables.columns.*
import lukfor.tables.columns.types.*

Table table = TableBuilder.fromCsvFile("example.csv").load();

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
