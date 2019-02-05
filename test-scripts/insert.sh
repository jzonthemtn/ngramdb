#!/bin/bash

# Inserts two n-grams (test, asdf) of length 1 (bag of words) into context 'c2'.

curl -vvvv  -X POST "http://localhost:8080/api/insert?c=c2&n=test,asdf"
