#!/bin/bash

./gradlew build || exit 1
#./gradlew cloverAggregateReports || exit 1
./gradlew cloverGenerateReport || exit 1
ls -l
scripts/coverage_summary.sh
ls -l /
ls -l /coverage-out/
cp -r build/reports/clover/html/* /coverage-out/
ls -l /coverage-out/ || exit 1
