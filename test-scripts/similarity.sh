#!/bin/bash

# Inserts some n-grams of length 1 (bag of words) into two contexts ('c1' and 'c2').
curl -vvvv -X POST "http://localhost:8080/api/insert?c=c1&n=The,fox,jumped,over,me"
curl -vvvv -X POST "http://localhost:8080/api/insert?c=c2&n=The,dog,jumped,over,me"

# Calculates the cosine similarity of the contexts.
curl -vvvv "http://localhost:8080/api/similarity?c1=c1&c2=c2"
