/*
 * THEORY OF COMPUTATION - HW4
 * 資工系104乙班
 * 沙江益 - Aquile R. Sanchez
 * F74007094
 * File Name: TocFinal.java
*/

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class TocFinal {
	///***GLOBAL VARIABLES DECLARATION***///
	public static String [] MyGlobalString;
	public static int TopK;
	public static int LCombination;
	public static HashMap<String,String> MyHashMap;
	public static int [] MyGlobalStringLength;
	public static int [] MyGlobalStringLength2;
	
	///***CLASS DECLARATION***///
	public static class MyClass
	{
		int [] Array;
		String Content;
		public MyClass(String Cont)
		{
			Content = new String(Cont);
			Array = new int[10];
			for(int i = 0; i < 10; i++)
			{
				Array[i] = -1;
			}
		}
	}
	public static ArrayList<MyClass> CompleteData;
	public static void Combination (JSONObject MyObject,int k, int s) throws JSONException
	{
		if(k == LCombination)
		{
			String TmpStr = "", Result;
			Result = String.valueOf(MyObject.get(MyGlobalString[MyGlobalStringLength2[0]]));
			if(Result.equals("")) return ;
			TmpStr = MyGlobalString[MyGlobalStringLength2[0]] + ":" + Result;
			for(int i = 1;i<LCombination;i++)
			{
				Result = String.valueOf(MyObject.get(MyGlobalString[MyGlobalStringLength2[i]]));
				if(Result.equals("")) return ;
				TmpStr += "," + MyGlobalString[MyGlobalStringLength2[i]] + ":" + Result;
			}
			if(MyHashMap.get(TmpStr) == null)
			{
				MyHashMap.put(TmpStr, "1");
				MyClass MyNewClass = new MyClass(TmpStr);
				for(int i = 0; i < LCombination; i++) MyNewClass.Array[i] = MyGlobalStringLength2[i];
				CompleteData.add(MyNewClass);
			}
			else
			{
				String TempStrHM = MyHashMap.get(TmpStr);
				int CountHM = Integer.valueOf(TempStrHM);
				CountHM++;
				MyHashMap.put(TmpStr,String.valueOf(CountHM));
			}
			return ;
		}
		for(int i = s; i < MyGlobalString.length; i++)
			if(MyGlobalStringLength[i]==0)
			{
				MyGlobalStringLength[i] = 1;
				MyGlobalStringLength2[k] = i;
				Combination(MyObject, k+1, i+1);
				MyGlobalStringLength[i] = 0;
			}
	}	
	
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws IOException, JSONException {
		//String ProvidedURL = "http://www.datagarage.io/api/5386c065e7259bb37d9270e5";
		//TopK = 3;
		//LCombination = 2;
		String ProvidedURL = args[0];
		TopK = Integer.parseInt(args[1]);
		LCombination = Integer.parseInt(args[2]);
		URL MyURL = new URL(ProvidedURL);
		HttpURLConnection MyConnection = (HttpURLConnection) MyURL.openConnection();
		MyConnection.setRequestProperty("Accept-Charset", "UTF-8");
		MyConnection.connect();
		JSONTokener MyTokener = new JSONTokener(new InputStreamReader((InputStream)MyConnection.getContent()));
		JSONArray MyJsonArray = new JSONArray(MyTokener);
		@SuppressWarnings("unused")
		JSONObject MyJsonObject = new JSONObject(MyJsonArray);
		MyConnection.connect();
		MyGlobalString = ToMyGlobalString(MyURL);
		MyGlobalStringLength = new int[MyGlobalString.length];
		MyGlobalStringLength2 = new int[MyGlobalString.length];
		CompleteData = new ArrayList();
		MyHashMap = new HashMap<String,String>();
		for(int i = 0; i < MyJsonArray.length(); i++)
		{
			JSONObject MyObject = MyJsonArray.getJSONObject(i);
			Arrays.fill(MyGlobalStringLength,0);
			Combination(MyObject,0,0);
		}				
		
		Collections.sort(CompleteData,new Comparator<Object>()
		{
			public int compare(Object Object1, Object Object2)
			{
				MyClass MyClass_1 = (MyClass)Object1;
				MyClass MyClass_2 = (MyClass)Object2;
				int Aux_A = Integer.valueOf(MyHashMap.get(MyClass_1.Content));
				int Aux_B = Integer.valueOf(MyHashMap.get(MyClass_2.Content));
				if(Aux_A != Aux_B) return Aux_B - Aux_A;
				for(int i = 0; i < LCombination; i++) 
					if(MyClass_2.Array[i]!=MyClass_1.Array[i])
						return MyClass_1.Array[i] - MyClass_2.Array[i]; 
					return 0;
			}
		});
		int TOPK = 0;
		@SuppressWarnings("unused")
		String TempStr, TempStr1 = "";
		for(int i = 0; i < CompleteData.size() && TOPK < TopK + 1; i++)
		{
			if(TOPK == 0)
			{
				TempStr = CompleteData.get(i).Content;
				System.out.println(TempStr + ";" + MyHashMap.get(TempStr));
				TempStr1 = new String(TempStr);
				TOPK ++;
			}
			else
			{
				TempStr = CompleteData.get(i).Content;
				if(MyHashMap.get(TempStr).equals(MyHashMap.get(TempStr)))
				{
					if(TOPK <= TopK) System.out.println(TempStr + ";" + MyHashMap.get(TempStr));		
					TempStr1 = new String(TempStr);
					if(TOPK + 1 <= TopK) TOPK ++;
				}
				else
				{
					if(TOPK + 1 <= TopK) System.out.println(TempStr+";"+MyHashMap.get(TempStr));
					TempStr1 = new String(TempStr);
					TOPK++;
				}
			}
		}
	}
}
