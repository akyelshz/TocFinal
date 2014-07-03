import java.io.*;
import java.net.*;
import org.json.*;


public class TocFinal {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws IOException, JSONException {
		String ProvidedURL = "http://www.datagarage.io/api/5386c065e7259bb37d9270e5";
		int TopK = 3;
		int LCombination= 2;
		URL MyURL = new URL(ProvidedURL);
		HttpURLConnection MyConnection = (HttpURLConnection) MyURL.openConnection();
		MyConnection.setRequestProperty("Accept-Charset", "UTF-8");
		MyConnection.connect();
		JSONTokener MyTokener = new JSONTokener(new InputStreamReader((InputStream)MyConnection.getContent()));
		JSONArray MyJsonArray = new JSONArray(MyTokener);
		JSONObject MyJsonObject = new JSONObject(MyJsonArray);
	}

}
