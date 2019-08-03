# Example

Simple java API to read, transform, sort, filter and aggregate tables.

```{tables}
params.input = "$baseDir/example.csv"
print(params)
```

## Reading data

```{tables}
table = fromCsvFile(params.input).load()
```

## Summary

```{tables}
table.printSummary()
```

## Data

```{tables}
table.print()
```
