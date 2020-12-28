import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
	static char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPRQSTUVWXYZ0123456789!@#$%^&*()".toCharArray();
	static int counter = 0;
	static long start, end, time;

	public static String md5(String input)
	{
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hash = no.toString(16);
			while (hash.length() < 32) 
			{
				hash = "0" + hash;
			}
			return hash;
		} catch (NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
	}

	static String generate(StringBuilder sb, int n, String z, int m) {	
		if (m == 0){
			System.out.println("Minimum value > 0");
			System.exit(0);
		}

		if (n == sb.length()) {
			System.out.println(sb.toString() + " | #" + counter);
			counter++;
			return sb.toString();
		}
        
		for (char letter : alphabet) {
			sb.setCharAt(n, letter);
			String mainFunc = generate(sb, n + 1, z, m);
			if (md5(mainFunc.toString()).equals(z)){
				System.out.println("Hash Found: " + sb.toString());
				break;
			}
		}
		return sb.toString();
	}

	public static void startGen(String x, int m){
		StringBuilder sb = new StringBuilder();
		sb.setLength(m);
		generate(sb, 0, x, m);
	}

	public static void main(String[] args) {
		Scanner myObj = new Scanner(System.in);
		System.out.print("Enter Hash to Decrypt: ");
		String hashVal = myObj.nextLine();
		if (hashVal.length() != 32){
			System.out.println("Pleas enter correct ND5 Hash");
			System.exit(0);
		}
		System.out.print("Do you have a Wordlist (y/N): ");
		String wordlist = myObj.nextLine();
		if (wordlist.toString().toLowerCase().equals("y")){
			System.out.print("Enter wordlist location: ");
			String wordlistLoc = myObj.nextLine();
			try {
				File file = new File(wordlistLoc);
				if (file.exists()){
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line = br.readLine();
					while (line != null){
						System.out.println(line);
						if (md5(line).equals(hashVal)){
							System.out.println("[+] Hash Found: "+line);
							System.exit(1);
						}
						line = br.readLine();
					}
					System.out.println("Woops... Hash not Found!");
				} else {
					System.out.println("File not Found");
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {	
			System.out.print("Enter Maximum Length: ");
			int maxVal = Integer.parseInt(myObj.nextLine());
			startGen(hashVal, maxVal);
		}
	}	
}

