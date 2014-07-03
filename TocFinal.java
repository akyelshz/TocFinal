import java.io.*;
import java.net.*;

import org.json.*;


public class TocFinal {
	public static String [] MyGlobalString;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 */
	
	//***SAVE TO GLOBAL STRING***//
	public static String [] ToMyGlobalString(URL ProvidedURL) throws IOException
	{
		InputStream MyInputStream = ProvidedURL.openStream();
		InputStreamReader MyStreamReader = new InputStreamReader(MyInputStream,"UTF-8");
		BufferedReader MyBufferedReader = new BufferedReader(MyStreamReader);
		StringBuffer MyStringBuffer = new StringBuffer();
		
		String AuxStr;
		int y = 0, z = 0;
		
		for(;;)
		{
			MyStringBuffer.append(AuxStr = MyBufferedReader.readLine());
			if(AuxStr.contains("}"))break;
		}
		String MyOutputStr = MyStringBuffer.toString();
		for(int i = 0; i < MyOutputStr.length(); i++)
		{
			if(MyOutputStr.charAt(i)=='{') y = i;
			if(MyOutputStr.charAt(i)=='}') z = i;
		}
		MyOutputStr =  MyOutputStr.substring(y+1,z);
		String []StrSplit = MyOutputStr.split(",");
		String []StringResult = new String[StrSplit.length];
		for(int i = 0; i < StrSplit.length; i++)
		{
			int aux = 0;
			if(StrSplit[i].contains("\""))
			{
				int Counter = 0;
				for(y = 0; y < StrSplit[i].length(); y++)
				{
					if(StrSplit[i].charAt(y) == '\"' && Counter < 2)
					{
						Counter++;
						aux = y;
					}
				}
				StringResult[i] = StrSplit[i].substring(1,aux);
			}
			else
			{
				for(y = 0; y < StrSplit[i].length(); y++)
				{
					if(StrSplit[i].charAt(y) == ':')
					{
						StringResult[i] = StrSplit[i].substring(0, y);
					}
				}
			}
		}		
		return StringResult;
	}
	
	//***MAIN STARTS HERE***//
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
		MyConnection.connect();
		MyGlobalString = ToMyGlobalString(MyURL);
		//System.out.println(MyGlobalString);
	}

}
