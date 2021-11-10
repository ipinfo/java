#!/bin/bash

set -eu

echo "Publishing Maven snapshot..."

mvn clean source:jar javadoc:jar deploy

echo "Maven snapshot published."
