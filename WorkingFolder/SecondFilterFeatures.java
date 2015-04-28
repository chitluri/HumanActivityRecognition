package org.gopi.weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class SecondFilterFeatures {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String PATH = "/Users/GopiKrishna/GitHub/HumanActivityRecognition/WorkingFolder";
		String[] features = {
				"tBodyAcc-",
				"tGravityAcc-",
				"tBodyAccJerk-",
				"tBodyGyro-",
				"tBodyGyroJerk-",
				"tBodyAccMag-",
				"tGravityAccMag-",
				"tBodyAccJerkMag-",
				"tBodyGyroMag-",
				"tBodyGyroJerkMag-",
				"fBodyAcc-",
				"fBodyAccJerk-",
				"fBodyGyro-",
				"fBodyAccMag-",
				"fBodyBodyAccJerkMag-",
				"fBodyBodyGyroMag-",
				"fBodyBodyGyroJerkMag-",
				"angle"
		};
		//generateFullFeaturesList(features, PATH);
		
		String[] subFeatures = {"mean()","std()","mad()","max()","min()","sma()","energy()","iqr()","entropy()","arCoeff()","correlation()","maxInds()","meanFreq()","skewness()","kurtosis()","bandsEnergy()","angle()"};
		
		String removedFeatures[] = {"fBodyAccJerk-","fBodyGyro-", "tGravityAccMag-", "tGravityAcc-", "tBodyAccMag-", "tBodyGyroJerkMag-", "energy()"};
		/*for(int i=0; i<features.length; i++){
			removedFeatures[removedFeatures.length-1] = subFeatures[i];
			System.out.println("Removing Feature: "+subFeatures[i]+"\n"+runClassifiersAfterRemoving(removedFeatures, PATH+"/train/allTrainingData.arff", PATH+"/test/allTestingData.arff", PATH));
		}*/
		//String removedFeatures[] = {"tGravityAcc-", "tBodyAccMag-", "tGravityAccMag-", "tBodyGyroJerkMag-", "fBodyAccJerk-", "fBodyGyro-", "fBodyAccMag-", "fBodyBodyGyroMag-"};
		//String removedFeatures[] = {"tBodyAcc-", "tGravityAcc-", "tBodyGyro-", "tBodyAccMag-", "tBodyGyroMag-", "fBodyAcc-", "fBodyAccJerk-", "fBodyGyro-"};
		
		System.out.println("Removing Feature: "+Arrays.toString(removedFeatures)+"\n"+runClassifiersAfterRemoving(removedFeatures, PATH+"/train/allTrainingData2.arff", PATH+"/test/allTestingData2.arff", PATH));
		
		/*for(String feature: features){
			System.out.println("Removing Feature: "+feature+"\nPercentage Correctness: "+runClassifiersAfterRemoving(new String[]{feature}, PATH+"/train/allTrainingData.arff", PATH+"/test/allTestingData.arff", PATH));
		}*/
		
		/*
		for(int i=0; i< features.length; i++){
			for(int j=i+1; j<features.length; j++){
				String selectedFeatures[] = {features[i], features[j]};
				System.out.println("Removing following features: "+selectedFeatures);
				System.out.println("Percentage Correctness: "+runClassifiersAfterRemoving(selectedFeatures, PATH+"/train/allTrainingData.arff", PATH+"/test/allTestingData.arff", PATH));
			}
		}*/

	}
	
	

	private static double runClassifiersAfterRemoving(String[] features, String trainFile, String testFile, String PATH)throws Exception {
		// TODO Auto-generated method stub
		Scanner trainScan = new Scanner(new File(trainFile));
		Scanner testScan = new Scanner(new File(testFile));
		
		PrintWriter trainingWriter = new PrintWriter(PATH+"/train/tempTrainingData2.arff");
		PrintWriter testingWriter = new PrintWriter(PATH+"/test/tempTestingData2.arff");
		
		HashSet<Integer> attributeRemovalList = new HashSet<Integer>();
		int lineNum = 0;
		while(trainScan.hasNext()){
			lineNum++;
			String str = trainScan.nextLine();
			if(!str.contains(",") || str.contains("'")){
				if(!containsAnyFeature(str,features)){
					trainingWriter.println(str);
				}
				else{
					attributeRemovalList.add(lineNum-2);
					continue;
				}
			}
			else{
				String[] strArray = str.split(",");
				//System.out.println("No of attributes in original file"+str.length);
				StringBuffer valuesSB = new StringBuffer("");
				for(int i=0; i<strArray.length; i++){
					if(!attributeRemovalList.contains(i+1)){
						valuesSB.append(strArray[i]+",");
					}
					else{
						continue;
					}
				}
				trainingWriter.println(valuesSB);
			}
		}
		trainingWriter.close();
		
		System.out.println("Number of features removed in this iteration: "+attributeRemovalList.size());
		
		lineNum = 0;
		while(testScan.hasNext()){
			lineNum++;
			String str = testScan.nextLine();
			if(!str.contains(",") || str.contains("'")){
				if(!containsAnyFeature(str, features)){
					testingWriter.println(str);
				}
				else{
					//attributeRemovalList.add(lineNum-2);
					continue;
				}
			}
			else{
				String[] strArray = str.split(",");
				//System.out.println("No of attributes in original file"+str.length);
				StringBuffer valuesSB = new StringBuffer("");
				for(int i=0; i<strArray.length; i++){
					if(!attributeRemovalList.contains(i+1)){
						valuesSB.append(strArray[i]+",");
					}
					else{
						continue;
					}
				}
				testingWriter.println(valuesSB);
			}
		}
		
		testingWriter.close();
		
		
		
		BufferedReader br = new BufferedReader(new FileReader(PATH+"/train/tempTrainingData2.arff"));
		//BufferedReader br2 = new BufferedReader(new FileReader(PATH+"/test/tempTesitngData.arff"));
		
		Instances train = new Instances(br);
		//Instances test = new Instances(br2);
		train.setClassIndex(train.numAttributes()-1);
		//test.setClassIndex(test.numAttributes()-1);
		
		br.close();
		//br2.close();
	
		SMO smo = new SMO();
		Evaluation eval = new Evaluation(train);
		eval.crossValidateModel(smo, train, 10, new Random(1));
		System.out.println("SMO: "+Arrays.deepToString(eval.confusionMatrix()));
		System.out.println(eval.pctCorrect());
		
		NaiveBayes nb = new NaiveBayes();
		eval = new Evaluation(train);
		eval.crossValidateModel(nb, train, 10, new Random(1));
		System.out.println("NaiveBayes: "+Arrays.deepToString(eval.confusionMatrix()));
		System.out.println(eval.pctCorrect());
		
		J48 j48 = new J48();
		eval = new Evaluation(train);
		eval.crossValidateModel(j48, train, 10, new Random(1));
		System.out.println("J48: "+Arrays.deepToString(eval.confusionMatrix()));
		System.out.println(eval.pctCorrect());
		
		IBk ibk = new IBk();
		eval = new Evaluation(train);
		eval.crossValidateModel(ibk, train, 10, new Random(1));
		System.out.println("IBK: "+Arrays.deepToString(eval.confusionMatrix()));
		System.out.println(eval.pctCorrect());
		
		OneR oneR = new OneR();
		eval = new Evaluation(train);
		eval.crossValidateModel(oneR, train, 10, new Random(1));
		System.out.println("OneR: "+Arrays.deepToString(eval.confusionMatrix()));
		System.out.println(eval.pctCorrect());
		
		return 0.00000000000001;
		
	}



	private static boolean containsAnyFeature(String str, String[] features) {
		// TODO Auto-generated method stub
		for(String feature: features){
			if(str.contains(feature)){
				return true;
			}
		}
		return false;
	}



	public static void generateFullFeaturesList(String[] features, String PATH)throws Exception{
		Scanner in = new Scanner(new File(PATH+"/features.txt"));
		ArrayList<String> attributeList = new ArrayList<String>();
		while(in.hasNextLine()){
			String str = in.nextLine();
			for(String feature: features){
				if(str.contains(feature)){
					attributeList.add(str);
				}
			}
		}
		
		System.out.println(attributeList.size());
		StringBuffer sb = new StringBuffer("@relation HumanActivityRecognition\n\n");
		for(String attribute: attributeList){
			sb.append("@attribute "+attribute.replace(" ","").replace(",","-")+" numeric\n");
		}
		sb.append("@attribute class {'1','2','3','4','6'} \n");
		sb.append("\n@data \n");
		PrintWriter trainingWriter = new PrintWriter(PATH+"/train/allTrainingData2.arff");
		PrintWriter testingWriter = new PrintWriter(PATH+"/test/allTestingData2.arff");
		trainingWriter.println(sb);
		testingWriter.println(sb);
		sb = null;
		
		Scanner trainScan = new Scanner(new File(PATH+"/train/X_train.txt"));
		Scanner trainClassScan = new Scanner(new File(PATH+"/train/y_train.txt"));
		Scanner testScan = new Scanner(new File(PATH+"/test/X_test.txt"));
		Scanner testClassScan = new Scanner(new File(PATH+"/test/y_test.txt"));
		StringBuffer trainSB = new StringBuffer("");
		StringBuffer testSB = new StringBuffer("");
		
		
		while(trainScan.hasNext()){
			String[] str = trainScan.nextLine().split(" +");
			//System.out.println("No of attributes in original file"+str.length);
			for(String attribute: attributeList){
				int attrNum = Integer.parseInt(attribute.split(" ")[0]);
				trainSB.append(str[attrNum]+",");
			}
			String label = trainClassScan.nextLine();
			if(label.equals("5")){
				label = "4";
			}
			trainSB.append(label+"\n");
		}
		trainSB.deleteCharAt(trainSB.length()-1);
		trainingWriter.println(trainSB);
		trainSB = null;
		trainingWriter.close();
		
		
		
		while(testScan.hasNext()){
			String[] str = testScan.nextLine().split(" +");
			//System.out.println("No of attributes in original file"+str.length);
			for(String attribute: attributeList){
				int attrNum = Integer.parseInt(attribute.split(" ")[0]);
				testSB.append(str[attrNum]+",");
			}
			String label = testClassScan.nextLine();
			if(label.equals("5")){
				label = "4";
			}
			testSB.append(label+"\n");
		}
		testSB.deleteCharAt(testSB.length()-1);
		testingWriter.println(testSB);
		testSB = null;
		testingWriter.close();
		
		
	}

}
