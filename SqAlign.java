package SqAlignment;
import java.util.*;
import java.io.*;
import java.util.Scanner;

class Alignment{
	static void BestScore(String filename,int match, int mismatch, int gap) throws FileNotFoundException {
		
		List<String> seq=new ArrayList<String>();
		
		try(Scanner sc=new Scanner(new File(filename))){
			while (sc.hasNextLine()) {
				String line=sc.nextLine();
				//System.out.println(line);
				seq.add(line);
			}
		}
		
		String x=seq.get(1);
		String y=seq.get(4);
		
		int n=x.length();
		int m=y.length();
		
		//construct the matrix 
		int X[][]=new int[n+m+1][n+m+1];
		for (int[] x1:X) {
			Arrays.fill(x1, 0);
		}
		//construct the table and initialize for 2 cases: (S,-) and (-,T)
		for (int i=0;i<=(n+m);i++ ) {
			X[i][0]=i*gap;
			X[0][i]=i*gap;
		}
		//int score=0;
		//fill in the whole table
		for (int i=1; i<=n;i++) {
			for (int j=1;j<=m;j++) {
				if (x.charAt(i-1)==y.charAt(j-1)) {
					X[i][j]=X[i-1][j-1];
				}
				else {
					X[i][j]=Math.min(Math.min(X[i-1][j-1]+mismatch, X[i-1][j]+gap),X[i][j-1]+gap);
					
				}
			}
		}
		int length=n;
		int leftover=length-X[n][m];
		int bestScore=leftover*match-X[n][m];
		
		//trace back
		int l=n+m;
		int i=n;
		int j=m;
		int xpos=l;
		int ypos=l;
		
		int s[]=new int[l+1];
		int t[]=new int[l+1];
		
		while (!(i==0||j==0)) {
			if (x.charAt(i-1)==y.charAt(j-1)) {
				s[xpos--]=(int)x.charAt(i-1);
				t[ypos--]=(int)y.charAt(j-1);
				i--;
				j--;
			}
			else if(X[i-1][j-1]+mismatch==X[i][j]) {
				s[xpos--]=(int)x.charAt(i-1);
				t[ypos--]=(int)y.charAt(j-1);
				i--;
				j--;
			}
			else if(X[i][j-1]+gap==X[i][j]) {
				s[xpos--]=(int) '_';
				t[ypos--]=(int)y.charAt(j-1);
				j--;
			}
			else if(X[i-1][j]+gap==X[i][j]) {
				s[xpos--]=(int) x.charAt(i-1);
				t[ypos--]=(int) '_';
				i--;
				
			}
		}
		while (xpos>0) {
			if(i>0) {
				s[xpos--]=(int)x.charAt(--i);
			}
			else {
				s[xpos--]=(int) '_';
			}
		}
		while (ypos>0) {
			if(j>0) {
				t[ypos--]=(int)y.charAt(--j);
			}
			else {
				t[ypos--]=(int) '_';
			}
		}
		
		int k=1;
		for( i=l;i>=1;i--) {
			if((char)t[i]=='_'&&(char)s[i]=='_') {
				k=i+1;
				break;
			}
			
		}
		//print out
		System.out.print("BestScore: ");
		System.out.println(bestScore);
		System.out.println("The best alignment is: ");
		for (i=k;i<=l;i++) {
			System.out.print((char)s[i]);
		}
		System.out.print("\n");
		for(i=k;i<=l;i++) {
			System.out.print((char)t[i]);
		}
		System.out.print("\n");
		return;
	}
public static void main (String[] args) throws FileNotFoundException {
	int mismatchPen=1;
	int gapPen=1;
	int match=1;
	//short fasta sq
	System.out.println("Short sequences: ");
	BestScore("/Users/nghihuynh/test.fasta",match,mismatchPen,gapPen);
	System.out.println();
	//medium fasta sq
	System.out.println("Medium sequences: ");
	BestScore("/Users/nghihuynh/tes1.fasta",match,mismatchPen,gapPen);
	System.out.println();
	//long fasta sq
	System.out.println("Long sequences: ");
	BestScore("/Users/nghihuynh/tes2.fasta",match,mismatchPen,gapPen);

}
}
