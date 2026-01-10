import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@SuppressWarnings("deprecation")
public class GeminiClient
{
	private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=" + Defines.GeminiAPIKey;


	public static Object3D generateObjectFromPrompt( String userPrompt ) throws Exception
	{
		String systemInstruction = "You are an expert 3D modeler. Create a high-quality 3D model representing: '" + userPrompt + "'. "
			+ "Target approximately 3000 to 5000 vertices for good detail. "
			+ "Focus on ORGANIC SHAPES, SMOOTH CURVES, and natural geometry. "
			+ "Do NOT produce blocky, minecraft-style, or voxelated structures. "
			+ "Output strictly in Wavefront OBJ format (vertices 'v' and faces 'f'). "
			+ "Do NOT use markdown code blocks. Return ONLY the raw OBJ data.";

		String jsonInputString = "{" + "\"contents\": [{" + "\"parts\":[{\"text\": \"" + escapeJson(systemInstruction) + "\"}]"	+ "}]" + "}";
		URL url                = new URL(API_URL);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		try( OutputStream os = conn.getOutputStream() )
		{
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		int code = conn.getResponseCode();
		
		if( code != 200 )
		{
			StringBuilder errorResponse = new StringBuilder();

			try( BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8)) )
			{
				String line;
				while( (line = br.readLine()) != null )
				{
					errorResponse.append(line);
				}
			}
			throw new RuntimeException("Erreur API Gemini (" + code + ") : " + errorResponse.toString());
		}

		StringBuilder response = new StringBuilder();

		try( BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) )
		{
			String responseLine;

			while( (responseLine = br.readLine()) != null )
				response.append(responseLine.trim()).append("\n");
		}

		String jsonResponse = response.toString();
		String objData      = extractTextFromGeminiResponse(jsonResponse);

		if( objData == null || objData.isEmpty() )
			throw new Exception("Gemini a renvoyé une réponse vide ou invalide.");

		objData = objData.replaceAll("```obj", "").replaceAll("```", "").trim();

		return ObjectLoader.loadFromString(objData);
	}

	private static String escapeJson( String s )
	{
		if( s == null ) return "";
		return s.replace("\\", "\\\\")
		        .replace("\"", "\\\"")
		        .replace("\b", "\\b")
		        .replace("\f", "\\f")
		        .replace("\n", "\\n")
		        .replace("\r", "\\r")
		        .replace("\t", "\\t");
	}

	private static String extractTextFromGeminiResponse( String json )
	{
		String marker = "\"text\": \"";
		int startIndex = json.indexOf(marker);
		
		if( startIndex == -1 )
			return null;

		startIndex += marker.length();
		
		StringBuilder result = new StringBuilder();
		boolean escaped      = false;
		
		for( int i=startIndex; i<json.length(); i++ )
		{
			char c = json.charAt(i);
			
			if( escaped )
			{
				if( c == 'n' )
					result.append('\n');
				else if( c == '\"' )
					result.append('\"');
				else if( c == '\\' )
					result.append('\\');
				else if( c == 'r' ){}
				else
					result.append(c);

				escaped = false;
			}
			else
			{
				if( c == '\\' )
					escaped = true;
				else if( c == '\"' )
					break;
				else
					result.append(c);
			}
		}
		
		return result.toString();
	}
}
