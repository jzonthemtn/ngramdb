# ngramdb

[![Build Status](https://travis-ci.org/mtnfog/ngramdb.svg?branch=master)](https://travis-ci.org/mtnfog/ngramdb)

ngramdb uses Apache Ignite to provide a distributed means of storing and querying N-grams (or bags of words) organized under contexts. The N-grams can be queried by the most frequently occurring N-grams or by N-grams starting with a given string. Additionally, ngramdb supports calculating the similarity (cosine, Euclidean) between two contexts. ngramdb has a REST API for persisting and querying the N-grams.

This project is an experimental work in progress.

## Building and Running

To build:

`mvn clean install`

To run:

```
cd ngramdb-app/target
java -jar ngramdb.jar
```

To submit n-grams:

`curl -X POST "http://localhost:8080/api/insert?c=context&n=test1,test2"`

Raw plain text can be submitted to the `/raw` endpoint and ngramdb will extract the n-grams from the text. Extracted n-grams will cross sentence boundaries. For more control over the extraction, handle it outside of ngramdb and `post` the n-grams to `/insert`. Other example requests are in the `test-scripts` directory.

See `ignite.xml` and `application.properties` for available settings.

## License

ngramdb is licensed under the Apache License, version 2.0.

Copyright 2019 Mountain Fog, Inc.
