#!/usr/bin/env bash

set -e -u

if [ "$TRAVIS_REPO_SLUG" == "ipinfo/java" ] &&                                \
   [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] &&                               \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] &&                                   \
   [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Publishing Javadoc and JDiff..."
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"

  cd ./target

  git clone -q -b gh-pages https://${GH_TOKEN}@github.com/ipinfo/java gh-pages > /dev/null
  cd gh-pages
  cp -rf ../apidocs/* .

  git add --all
  git commit -m "Deploy javadocs to GitHub Pages Travis build: ${TRAVIS_BUILD_NUMBER}" -m "Commit: ${TRAVIS_COMMIT}"
  git push -fq origin gh-pages > /dev/null

  echo "Javadoc and JDiff published to gh-pages."
fi
