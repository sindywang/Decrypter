import java.util.Arrays;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.io.*;

public class Decrypter {
	public static void main(String[] args) throws IOException {
		/** This is the main method
		 @param args[0] This is the key
		 @param args[1] This is the input file
		 @param args[2] This is the file that the output will be saved at
		 @return none
		 */
		//Getting key from user input
		String key = args[0];
		
		//Creating seed with given key
		int hash = key.hashCode();
		Random rand = new Random(hash);
		
		//Reading files
		File file = new File(args[1]);
		Scanner in = new Scanner(new FileReader(file));
		String rawInput = in.nextLine();

		//Passing in arguments to methods
		String alpha = newAbc(rand);
		String rightLetters= switchingLetters(alpha,rawInput);
		String output = transposition(key,rightLetters);
		
		//Writing files and saving to the file user gives
		FileWriter outputFile = new FileWriter(new File(args[2]));
		outputFile.write(output);
		outputFile.close();
	}
	
	public static String newAbc(Random seed){
		/**This method is used to create a new "alphabet" for the decryption.
		 @param seed This is the key provided from user
		 @return abc This is the new "alphabet"
		 */
		
		int num1,num2;
		String pos1,pos2;
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ "; 
		for (int i=0;i < 100;i++){
			num1 = seed.nextInt(27);
			num2 = seed.nextInt(27);
			
			pos1 = abc.substring(num1,num1+1);
			pos2 = abc.substring(num2,num2+1);
			
			//Switching letters from the two random positions 100 times
			abc = abc.substring(0, num1) + pos2 + abc.substring(num1+1);
			abc = abc.substring(0, num2) + pos1 + abc.substring(num2+1);
		}
		return abc;
	}
	
	public static String switchingLetters(String alpha,String input){
		/**
		 This method is used to switch the letters in the decryption into the right
		 places
		 @param alpha This is the new "alphabet"
		 @param input This is the input from user
		 @return input This is the undone substitution input
		 */
		
		String[] abc = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"," "};
		int count = 0;
		for (String letter:abc){
			//Undoing the substitution 
			input = input.replace(alpha.substring(count,count +1), letter);
			count ++;
		}
		return input;
	}
	
	public static String transposition(String key1,String unSub){
		/**This method is used to undo the transposition from the decryption.
		 @param key1 This is the key provided by user
		 @param unSub This is the input that has gone through the unsubbing process
		 @return lastOutput This is the final message that the user will see in their 
		 output file 
		 */
		
		int num1,num2,count=0;
		String lastOutput = "";
		//Creating 2D arrays
		String[][] inputArray = new String [8][8];
		String[][] outputArray;
		int[] colNumbers = {0,1,2,3,4,5,6,7};
		//Creating seed with key
		int hash = key1.hashCode();
		Random rand = new Random(hash);
		
		for (int i=0;i < 100;i++){
			num1 = rand.nextInt(8);
			num2 = rand.nextInt(8);
			//Swapping numbers from two random positions 100 times to get new order of numbers
			int temp = colNumbers[num1];
			colNumbers[num1] = colNumbers[num2];
			colNumbers[num2] = temp;
		}
		
		//Checking if length of input string is greater or equal to 64
		if (unSub.length() >= 64){
			//Getting numbers of 64 bytes
			int size = unSub.length()/64;
			//Creating 2D array depending on number of 64 bytes
			outputArray = new String[8*size][8*size];
			
			for (int i = 0; i < size;i++){
				//Dividing string into equal 64 byte strings
				String sections = unSub.substring(i*64,64+i*64);
				
					for (int j = 0; j < 8; j++){
						//Dividing the 64 byte strings into 8 byte strings
						String rowstring = sections.substring(j*8,8+j*8);
						
						for (int k = 0; k < 8; k++){
							//Putting each letter into the 2D array
							inputArray[j][k] = rowstring.substring(k,k+1);
						}	
					}
					while (count < 8){
						for (int num:colNumbers){
							//Ordering the array by 0,1,2,3..
							outputArray[num] = inputArray[count];
							count++;
						}
					}
					for (int a = 0; a < outputArray.length; a++){
						for (int b = 0; b < outputArray.length; b++){
							//Putting strings from array into lastOutput in the right order
							lastOutput += outputArray[b][a];
						}
					}
					//Changing to upper case
					lastOutput = lastOutput.toUpperCase();
			}
		}
		return lastOutput;
	}
}