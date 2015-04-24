#!/bin/bash

#Clean 561 attributes in X_test removilng all the frequency related data
cat test/X_test.txt | awk '{out=$1; for(i=2;i<=265;i++){out=out","$i};for(j=555;j<=NF;j++){out=out","$j}; print out}' > test/X_test_clean.txt

#Create header file from features.txt
printf "@relation HumanActivityRecognition\n\n@attribute Subject {'1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'}\n" > test/header.arff &&  cat features.txt | tr ',' '-' | awk '{ print "@attribute "$1$2" numeric" }' | sed '266,554d' >> test/header.arff && printf "@attribute class {'1','2','3','4','5','6'}\n\n@data\n" >> test/header.arff

#Create data.csv from subject_test.txt X_test_clean.txt y_test.txt
paste -d':' test/subject_test.txt test/X_test_clean.txt test/y_test.txt | tr '\r' '&' | tr ':' ',' |  sed 's/&//' > test/data.csv

#Creating the final ARFF file which should be ready to be loaded into weka
cat test/header.arff test/data.csv > test/testingDataTime.arff

#Remove intermediate files
rm test/X_test_clean.txt test/header.arff test/data.csv
