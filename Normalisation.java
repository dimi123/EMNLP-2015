/**
 * Normalisation of success rate in order to account for image complexity.
 *
 * @author  Dimitra Gkatzia
 * @version 1.0
 * @since   2015-01-15
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Normalisation {

	//This is the average of all success rates. CAUTION! This value reflects the REAL dataset. If you use it for a different 
	//dataset, please specify it accordingly. 
	private static final double AVERAGE_ALL_RATINGS = 0.77;


	/**
	 * This method is used to create a hashmap that includes the image ids and the success rate of each image. 
	 * The information is given by a text file (see imageSuccessRate.txt as an example).
	 * @param filename 
	 * @return  HashMap<String, Double> This returns a HashMap, key = image id and value the success rate of the image.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static  HashMap<String, Double> readFileSuccessRate(String filename) throws IOException{
		HashMap <String, Double> successRates = new HashMap<String, Double>();
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {

				String [] data = line.split(",");

				successRates.put(data[0], Double.parseDouble(data[1]));
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
		}
		return successRates;
	}


	/**
	 * This method is used to create an arraylist of Img. The information is given by a text file (see excel.csv for example).
	 * @param filename 
	 * @return ArrayList <Img> This returns an Arraylist of object types Img.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static ArrayList <Img> createImgList(String filename) throws IOException{
		ArrayList <Img> images = new ArrayList<Img>();
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				String [] data =  line.split(",");
				double score = (Double.parseDouble(data[15]) - AVERAGE_ALL_RATINGS) * (Double.parseDouble(data[15]) - AVERAGE_ALL_RATINGS);
				Img img = new Img(data[5], score);
				images.add(img);
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
		}
		return images;
	}


	/**
	 * This method is used to create an arraylist of Img. The information is given by a text file (see excel.csv for example).
	 * @param successRates 
	 * @param images
	 * @return Nothing
	 */
	public static void printNormalisedRatings(HashMap <String, Double> successRates, ArrayList <Img> images ){
		String newRatings = "";
		@SuppressWarnings("rawtypes")
		Iterator it = successRates.entrySet().iterator();
		while (it.hasNext()) {

			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
			String imgName = (String) pairs.getKey();
			double score =  (double) pairs.getValue();

			double shift = Math.abs(score - AVERAGE_ALL_RATINGS); //shift of average value

			double sum = 0;

			for(Img img: images){
				if(img.getName().equals(imgName)){
					sum += img.getScore();

				}
			}

			double sigma = Math.sqrt(sum);
			double normalisedRating = shift/sigma;

			newRatings += imgName + "," + Double.parseDouble(new DecimalFormat("##.####").format(normalisedRating)) + "\n";
			it.remove(); // avoids a ConcurrentModificationException
		}
		System.out.println(newRatings);
	}


	/**
	 * This is the main method. It prints the normalised success rate for each image!
	 * @return Nothing.
	 * @exception IOException On input error.
	 * @see IOException
	 */
	public static void main(String args []) throws IOException{

		HashMap <String, Double> successRates = readFileSuccessRate("imgSuccessRate.txt");
		ArrayList <Img> images = createImgList("excel.csv");
		printNormalisedRatings( successRates, images);

	}
}
