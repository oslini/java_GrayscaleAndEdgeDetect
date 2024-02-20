/**
* Author: Angela Burns
* October 3, 2018
* Lab16
*/
package lab16;

public class ImageDriver {

	public static void main(String[] args) {
		
		//import file  
		String testImage = "test.ppm";
		
		Image original = new Image(testImage);
		Image grey = new Image(testImage);
		Image edge = new Image(testImage);
		
		original.draw();
		
		grey.getGrayscaleImage();
		grey.draw();
		
		edge.getEdgeImage();
		edge.draw();
		
		
	}

}
