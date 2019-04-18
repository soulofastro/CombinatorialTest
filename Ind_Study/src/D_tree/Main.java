package D_tree;

/*
 *  NOTE: Only use spaces when absolutely necessary. The line parser treats everything between ',' as separate objects.
 *  	  Treat all objects as case sensitive when naming and comparing.
 * 
 *  Item text file format: 
 *  0:Item Name,assignment limit,[CONDITIONS]
 *  1:Item Name,assignment limit,[CONDITIONS]
 *  2:Item Name,assignment limit,[CONDITIONS]
 *  etc.
 *  
 *  Location text file format:
 *  0:Location Name,[CONDITIONS]
 *  1:Location Name,[CONDITIONS]
 *  2:Location Name,[CONDITIONS]
 *  etc.
 */

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		CombinatorialTest.run();
	}

}
