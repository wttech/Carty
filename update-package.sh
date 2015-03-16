#!/bin/bash

git checkout master
mvn clean package
git checkout gh-pages
mv target/*.zip packages
git add packages
git commit -a -m 'Updated SNAPSHOT package'
git push origin gh-pages
git checkout master
