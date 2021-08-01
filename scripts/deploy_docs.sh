#!/bin/bash

set -eu

echo "Publishing Javadoc and JDiff..."

cd ./target
git clone -b gh-pages https://github.com/ipinfo/java gh-pages > /dev/null
cd gh-pages
cp -rf ../apidocs/* .

git add --all
git commit -m "Deploy javadocs to GitHub Pages Travis build: ${TRAVIS_BUILD_NUMBER}" -m "Commit: ${TRAVIS_COMMIT}"
git push -fq origin gh-pages > /dev/null

echo "Javadoc and JDiff published to gh-pages."
