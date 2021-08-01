#!/bin/bash

set -eu

echo "Publishing Javadoc and JDiff..."

cd ./target
git clone -b gh-pages git@github.com:ipinfo/java gh-pages
cd gh-pages
cp -rf ../apidocs/* .
git add --all
git commit -m "Deploy javadocs to GitHub Pages."
git push -f origin gh-pages

echo "Javadoc and JDiff published to gh-pages."
