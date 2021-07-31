#!/usr/bin/env bash

set -e -u

function mvn_deploy() {
    mvn clean source:jar javadoc:jar deploy --settings="$(dirname $0)/settings.xml"
}

if [ "$TRAVIS_REPO_SLUG" == "ipinfo/java" ] &&                                \
   [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] &&                               \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] &&                                   \
   [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Publishing Maven snapshot..."
  mvn_deploy
  echo "Maven snapshot published."
fi
