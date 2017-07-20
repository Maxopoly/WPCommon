package com.github.maxopoly.WPCommon.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.Logger;

public class ConnectionUtils {

	public static String generateKeyHash(String serverAdress, byte[] sharedSecret, byte[] serverPubKeyBytes)
			throws IOException {
		try {
			String mc = new BigInteger(digestOperation("SHA-1", new byte[][] { serverAdress.getBytes("ISO_8859_1"),
					sharedSecret, serverPubKeyBytes })).toString(16);
			return mc;
		} catch (Exception e) {
			throw new IOException("Failed to gen encryption hash");
		}
	}

	private static byte[] digestOperation(String algorithm, byte[]... data) {
		try {
			MessageDigest messagedigest = MessageDigest.getInstance(algorithm);

			for (byte[] abyte : data) {
				messagedigest.update(abyte);
			}

			return messagedigest.digest();
		} catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			nosuchalgorithmexception.printStackTrace();
			return null;
		}
	}

	public static String sendPost(String content, String url, Logger logger) throws IOException {
		try {
			byte[] contentBytes = content.getBytes("UTF-8");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(contentBytes, 0, contentBytes.length);
			wr.close();
			int responseCode = con.getResponseCode();
			if ((responseCode / 100) != 2) { // we want a 200 something response code
				throw new IOException("POST to " + url + " returned bad response code " + responseCode);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (Exception e) {
			logger.error("Exception occured", e);
			throw new IOException("Failed to send POST to " + url + " : " + e.getClass());
		}
	}

	public static String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		int responseCode = con.getResponseCode();
		if ((responseCode / 100) != 2) { // we want a 200 something response code
			throw new IOException("GET to " + url + " returned bad response code " + responseCode);
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();

	}

}
