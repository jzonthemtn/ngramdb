#!/bin/bash

# Get the top 10 n-grams from context (c2) having length 1 (bag of words).
curl -vvvv "http://localhost:8080/api/top?c=c2&l=10&n=1"
