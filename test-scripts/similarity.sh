#!/bin/bash
curl -vvvv    "http://localhost:8080/api/insert?c=c1&n=The,fox,jumped,over,me" -X POST
curl -vvvv    "http://localhost:8080/api/insert?c=c2&n=The,dog,jumped,over,me" -X POST
curl -vvvv    "http://localhost:8080/api/similarity?c1=c1&c2=c2"
