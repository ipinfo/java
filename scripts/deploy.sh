#!/bin/bash

set -eu

echo "Publishing Maven snapshot..."

mvn clean source:jar javadoc:jar deploy --settings="$(dirname $0)/settings.xml"

echo "Maven snapshot published."
