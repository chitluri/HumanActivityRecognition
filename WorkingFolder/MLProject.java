package org.gopi.weka;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MLProject {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = null;;
		try{
			in = new Scanner(new File("/Users/GopiKrishna/GitHub/HumanActivityRecognition/WorkingFolder/selectedFeatures.txt"));
		}
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		ArrayList<Integer> attributeList = new ArrayList<Integer>();
		while(in.hasNextLine()){
			String str = in.nextLine();
			int attributeNum = Integer.parseInt(str.split(" ")[0]);
			attributeList.add(attributeNum);
		}
		System.out.println("Size of array is: "+attributeList.size());
		try{
			in = new Scanner(new File("/Users/GopiKrishna/GitHub/HumanActivityRecognition/WorkingFolder/test/X_test.txt"));
		}
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		ArrayList<String> attributeValues = new ArrayList<String>();
		StringBuffer sb = new StringBuffer("");
		while(in.hasNext()){
			String[] str = in.nextLine().split(" +");
			System.out.println("No of attributes in original file"+str.length);
			for(Integer attrNum: attributeList){
				sb.append(str[attrNum]+" ");
			}
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		PrintWriter out = null;
		try{
			out = new PrintWriter("/Users/GopiKrishna/GitHub/HumanActivityRecognition/WorkingFolder/test/selectedAttrValues.txt");
			out.println(sb.toString());
			out.close();
		}
		catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		finally{
			out.close();
		}
		//System.out.println(sb.toString());
		
	}

}
