/**
* Author: Angela Burns
* October 3, 2018
* Lab16
*/
package lab16;

import java.awt.Color;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.princeton.cs.introcs.StdDraw;

public class Image{
	
	private static final int MAX_CANVAS_DIMENSION = 500;
	private int numColumns;
	private int numRows;
	private Color[][] pixels;
	
	
	public Image(String filename)
	{
		loadFile(filename);
		setupCanvas();
	}

	
	// PPM: portable pixmap format (a simple image format)
	private void loadFile(String filename){
		
		Scanner inFile = null;
		try{
			inFile = new Scanner(new FileInputStream(filename));
			
		} 
		catch (FileNotFoundException e){
			System.out.println("Cannot open file: " + filename);
			System.exit(0);
		}

		// file starts with P3
		String magic = inFile.nextLine();
		if (!magic.equals("P3")){
			System.out.println("invalid PPM file");
			System.exit(0);
		}

		// throw away comment lines
		while (inFile.hasNext("#.*")){
			inFile.nextLine();
		}

		// read in width of image
		numColumns = inFile.nextInt();
		
		// read in height of image
		numRows = inFile.nextInt();
		System.out.println("image size: " + numColumns + " by " + numRows);
		pixels = new Color[numRows][numColumns];

		// read in max color value
		int maxVal = inFile.nextInt();

		// read in image data
		// triplets of red, green blue
		for (int row = 0; row < numRows; row++){
			for (int col = 0; col < numColumns; col++){
				int r, g, b;
				r = inFile.nextInt();
				g = inFile.nextInt();
				b = inFile.nextInt();
				pixels[row][col] = new Color(r, g, b); // red, green, blue
			}
		}
	}

	private void setupCanvas(){
		
		int largestDimension = Math.max(numRows, numColumns);
		
		int pixelColumns = (int) (MAX_CANVAS_DIMENSION * numColumns / largestDimension);
		int pixelRows = (int) (MAX_CANVAS_DIMENSION * numRows / largestDimension);
		StdDraw.setCanvasSize(pixelColumns, pixelRows); // (width, height)
		StdDraw.setXscale(0, numColumns);
		
		StdDraw.setYscale(numRows, 0);
		
		StdDraw.enableDoubleBuffering();
	}

//copy constructor *for gray*
	public Image(Image other){
		numRows = other.numRows;
		numColumns = other.numColumns;
		pixels = new Color[numRows][numColumns];
		for (int row = 0; row < numRows; row++)
		{
			{
				for (int col = 0; col < numColumns; col++)
				{
					pixels[row][col] = other.pixels[row][col];
				}
			}
		}
	}

	
	public void draw(){
		StdDraw.clear();
		for (int row = 0; row < numRows; row++){
			
				for (int col = 0; col < numColumns; col++){
					StdDraw.setPenColor(pixels[row][col]);
					StdDraw.filledSquare(col + 0.5, row + 0.5, 0.5);
				}
		}
		StdDraw.show();
		StdDraw.pause(10);
	}
	
	
	public Image getGrayscaleImage() {
		
		Image gray = new Image (this);

		Scanner scan = new Scanner(System.in);

		for( int i = 0; i < numRows; i++ ) {
		    for( int j = 0; j < numColumns; j++ ) {

				int p = pixels[i][j].getRGB();
				Color color = new Color(p);
				
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				int a = (p>>24)&0xff;

				double redGS   = 0.299;
		        double greenGS = 0.587;
		        double blueGS  = 0.114;	
		        
		      //calculate average
		        red = green = blue = (int)(redGS * red + greenGS * green + blueGS * blue);
		        int avg = (red + green + blue)/3;
			//System.out.println("r: "+ red + " g " + green + " b "+ blue);
				p = (a<<24) | (avg << 16) | (avg<<8) | avg;
				pixels[i][j]= new Color(p);
		    }
		}
		System.out.println("\nPress enter to show the grayscale");
		scan.nextLine();
		return gray;
	}

	
	
	public Image getEdgeImage() {
		
		Scanner scan = new Scanner(System.in);
		Image gray = new Image (this);

		
		for (int i = 1; i < numRows-1; i++){
				for (int j = 1; j < numColumns-1; j++){
					
					double leftSide = pixels[i][j-1].getBlue();
					double rightSide = pixels[i][j+1].getBlue();
					double upSide = pixels[i+1][j].getBlue();
					double downSide = pixels[i-1][j].getBlue();
										
					double xTotal = (-0.5 * leftSide) + (0.5 * rightSide);
					double yTotal = (-0.5 * downSide) + (0.5 * upSide);
					
					int edge = (int)Math.sqrt(Math.pow(xTotal, 2) + Math.pow(yTotal, 2));
								
					pixels[i][j] = new Color (edge, edge,edge);
					
				}
		}
		System.out.println("Press enter to show the edges");
		scan.nextLine();
		return gray;
        }
	
}
