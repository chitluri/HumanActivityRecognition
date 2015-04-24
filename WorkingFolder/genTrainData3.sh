#!/bin/bash

#Clean 561 attributes in X_train and remove frequency dependent features
cat train/X_train.txt | awk '{out=$1; for(i=2;i<=265;i++){out=out","$i};for(j=555;j<=NF;j++){out=out","$j}; print out}' > train/X_train_clean.txt

#Create header file from features.txt
printf "@relation HumanActivityRecognition\n\n@attribute Subject {'1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'}\n" > train/header.arff &&  cat features.txt | tr ',' '-' | awk '{ print "@attribute "$1$2" numeric" }' | sed '266,554d' >> train/header.arff && printf "@attribute class {'1','2','3','4','5','6'}\n\n@data\n" >> train/header.arff

#Create data.csv from subject_train.txt X_train_clean.txt y_train.txt
paste -d':' train/subject_train.txt train/X_train_clean.txt train/y_train.txt | tr '\r' '&' | tr ':' ',' |  sed 's/&//' > train/data.csv

#Creating the final ARFF file which should be ready to be loaded into weka
cat train/header.arff train/data.csv > train/trainingDataTime.arff

#Remove intermediate files
rm train/X_train_clean.txt train/header.arff train/data.csv
