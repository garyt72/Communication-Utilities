package com.taylor.utilities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class Util {

	public static final ASCIIReference asciiReference = ASCIIReference.getInstance();
	
	public static String convertAsciiToBase10(String str) {
		return convertAsciiToBase10(str, false);
	}

	public static String convertAsciiToBase10(String str, boolean dumpOut) {
		char[] chars = str.toCharArray();
		StringBuffer base10 = new StringBuffer();
		if (dumpOut) System.out.print(" Base10: ");
		for (int i = 0; i < chars.length; i++) {
			base10.append((int) chars[i]);
			if (i + 1 < chars.length) base10.append(";");
			if (dumpOut)
				System.out.print("  " + (("" + (int) chars[i]).length() == 1 ? "0" : "") + (int) chars[i] + " ");
		}
		if (dumpOut) System.out.println("");
		return base10.toString();
	}

	public static String displayAsciiCodes(Byte b) {
		return displayAsciiCodes(new String(b.toString()));
	}
	
	public static String displayAsciiCodes(char c) {
		return displayAsciiCodes(String.valueOf(c));
	}
	
	/**
	 * Converts an ascii decimal String to a hex String.
	 * 
	 * @param str
	 *            holding the string to convert to HEX
	 * @return a string holding the HEX representation of the passed in decimal
	 *         str.
	 **/
	public static String displayAsciiCodes(String str) {
		return displayAsciiCodes(str, false, false);
	}

	/**
	 * Converts an ascii decimal String to a hex String.
	 * 
	 * @param str
	 *            holding the string to convert to HEX
	 * @param dumpOut
	 *            flag to turn some debug output on/off
	 * @return a string holding the HEX representation of the passed in str.
	 **/
	public static String displayAsciiCodes(String str, boolean dumpOut, boolean brackets) {
		char[] chars = str.toCharArray();
		String asciiCode = "";
		StringBuffer dumpOutput = new StringBuffer();
		StringBuffer returnOutput = new StringBuffer();

		// output the ASCII code version of the string
		if (dumpOut) System.out.print("  ASCII:");
		for (int i = 0; i < chars.length; i++) {
			asciiCode = asciiReference.getCharString((int) chars[i], brackets);
			// left pad to 4 characters with spaces
			dumpOutput.append(String.format("%1$5s", asciiCode));
			returnOutput.append(asciiCode);
		}
		if (dumpOut) System.out.println(dumpOutput.toString());


		return returnOutput.toString();
	}

	/**
	 * Converts an ascii decimal String to a hex String.
	 * 
	 * @param str
	 *            holding the string to convert to HEX
	 * @return a string holding the HEX representation of the passed in decimal
	 *         str.
	 **/
	public static String convertStringToHex(String str) {
		return convertStringToHex(str, false);
	}

	/**
	 * Converts an ascii decimal String to a hex String.
	 * 
	 * @param str
	 *            holding the string to convert to HEX
	 * @param dumpOut
	 *            flag to turn some debug output on/off
	 * @return a string holding the HEX representation of the passed in str.
	 **/
	public static String convertStringToHex(String str, boolean dumpOut) {
		char[] chars = str.toCharArray();
		String out_put = "";

		// output the HEX version of the string
		if (dumpOut) System.out.print("    Hex: ");
		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			out_put = Integer.toHexString((int) chars[i]);
			if (out_put.length() == 1) hex.append("0");
			hex.append(out_put);
			if (dumpOut) System.out.print("0x" + (out_put.length() == 1 ? "0" : "") + out_put + " ");
		}
		if (dumpOut) System.out.println("");

		/*
		 * if (dumpOut) System.out.print(" Base10: "); for(int i = 0; i <
		 * chars.length; i++) {
		 * System.out.print("   "+convertHexNumberStringToDecimal(chars[i])); }
		 * if (dumpOut) System.out.println("");
		 */

		return hex.toString();
	}

	/**
	 * Converts an HEX number String to its decimal equivalent.
	 * 
	 * @param str
	 *            holding the Hex Number string to convert to decimal
	 * @return an int holding the decimal equivalent of the passed in HEX
	 *         numberStr.
	 **/
	public static Integer convertBigEndianHexStringToInteger(String str) {
		return convertBigEndianHexStringToInteger(str, false);
	}

	/**
	 * Converts an HEX number String to its decimal equivalent.
	 * 
	 * @param str
	 *            holding the Hex Number string to convert to decimal
	 * @param dumpOut
	 *            boolean flag to turn some debug output on/off
	 * @return an int holding the decimal equivalent of the passed in HEX
	 *         numberStr.
	 **/
	public static Integer convertBigEndianHexStringToInteger(String str, boolean dumpOut) {
		char[] chars = str.toCharArray();
		String out_put = "";

		if (dumpOut) System.out.println("\n      AsciiHex: 0x" + str);
		if (dumpOut) System.out.print("       Decimal: ");

		StringBuffer hex = new StringBuffer();
		String hexInt = new String();
		for (int i = 0; i < chars.length; i++) {
			out_put = Integer.toHexString((int) chars[i]);
			if (out_put.length() == 1) hex.append("0");
			hex.append(out_put);
			if (dumpOut) System.out.print((out_put.length() == 1 ? "0" : "") + out_put);
		}
		hexInt = "" + (Integer.parseInt(hex.toString(), 16));
		if (dumpOut) System.out.println("");
		if (dumpOut) System.out.println("      Decimal: " + hexInt.toString());

		return Integer.parseInt(hexInt.toString());

		// return Integer.decode("0x"+str);
	}

	/**
	 * Converts a hex byte to an ascii String.
	 * 
	 * @param hex
	 *            byte holding the HEX string to convert back to decimal
	 * @return a string holding the HEX representation of the passed in str.
	 **/
	public static String convertHexToString(byte hex) {
		byte[] bytes = { hex };
		return convertHexToString(new String(bytes), false);
	}

	/**
	 * Converts a hex String to an ascii String.
	 * 
	 * @param hex
	 *            the HEX string to convert back to decimal
	 * @return a string holding the HEX representation of the passed in str.
	 **/
	public static String convertHexToString(String hex) {
		return convertHexToString(hex, false);
	}

	/**
	 * Converts a hex String to an ascii String.
	 * 
	 * @param hex
	 *            the HEX string to convert backk to decimal
	 * @param dumpOut
	 *            boolean flag to turn some debug output on/off
	 * @return a string holding the HEX representation of the passed in str.
	 **/
	public static String convertHexToString(String hex, boolean dumpOut) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		String out_put = "";

		if (dumpOut) System.out.print("    Hex: ");
		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			out_put = hex.substring(i, (i + 2));
			if (dumpOut) System.out.print("0x" + out_put + " ");
			// convert hex to decimal
			int decimal = Integer.parseInt(out_put, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		if (dumpOut) System.out.println("    Decimal : " + temp.toString());

		return sb.toString();
	}
	
	public static void displayStringDebug(String string) {
	
		
		char[] chars = string.toCharArray();

		// output the ASCII code version of the string
		System.out.print("  CHAR#:");
		StringBuffer ascii = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {

			// left pad to 4 characters with spaces
			ascii.append(String.format("%1$5d", i));
		}
		System.out.println(ascii.toString());

		
		convertStringToHex(string, true);
		convertAsciiToBase10(string, true);
		displayAsciiCodes(string, true, false);					

	
	}
	
	/**
	 * A method to simply abstract the Try/Catch required to put the current
	 * thread to sleep for the specified time in ms.
	 *
	 * @param waitTime
	 *            the sleep time in milli seconds (ms).
	 * @return boolean value specifying if the sleep completed (true) or was
	 *         interupted (false).
	 */
	public static boolean sleep(long waitTime) {
		boolean retVal = true;
		/*
		 * BLOCK for the spec'd time
		 */
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException iex) {
			retVal = false;
		}
		return retVal;
	}

	public static String getTimestamp() {
		return new Timestamp(new Date().getTime()).toString();
	}
	
	public static void printEnv() {
		Map<String, String> env = System.getenv();
		Iterator<String> keys = env.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.println("Env: " + key + "=" + env.get(key));
		}
	}

}
