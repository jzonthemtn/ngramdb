#!/bin/bash

# Inserts two n-grams (the dog, dog ran).
curl -vvvv -X POST "http://localhost:8080/api/insert?c=c2" --data-urlencode "n=the dog,dog ran"
