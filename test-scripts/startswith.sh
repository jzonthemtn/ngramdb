#!/bin/bash

# Get n-grams that start with 'te' under context 'c2'.
curl -vvvv  "http://localhost:8080/api/startswith?c=c2&sw=te"
