package com.taylor.utilities;

import java.util.HashMap;

public class ASCIIReference {

	/** Maps the class contant vars to the eiscp command string. **/
	private static HashMap<Integer, String> asciiMap = null;

	private static ASCIIReference instance_ = null;

	private ASCIIReference() {
		initMap();
		instance_ = this;
	}

	/** Singleton method to ensure all is setup. **/
	public static ASCIIReference getInstance() {
		if (instance_ != null)
			return instance_;
		else
			return new ASCIIReference();
	}

	/**
	 * searches for the commandName that is associated with the passed command.
	 * 
	 * @param char
	 *            the char number to get a string for
	 * @return the commandNameMap_ key
	 **/
	public String getCharString(Integer charInt) {
		return getCharString(charInt, false);
	}
	
	public String getCharString(Integer charInt, boolean bracketNonPrintable) {
		
		String result = asciiMap.get(charInt);
		
		if (!isPrintable(charInt) && bracketNonPrintable) {
			result = "[" + result + "]";
		}
		
		if (result == null) {
			result = "[NOTFOUND]";
		}
		
		return result;
	}


	public boolean isPrintable(Integer charInt) {
		boolean result = true;
		
		if ((charInt <= 32) || (charInt == 127)) {
			result = false;
		}else {
			result = true;
		}
			
		
		return result;
	}
	
	/**
	 * Initializes all the class constants (commandNameMap_ & commandMap_ ) that
	 * help with processing the commands.
	 **/
	private void initMap() {
		
		asciiMap = new HashMap<Integer, String>();
		
		asciiMap.put(0, "NUL");
		asciiMap.put(1, "SOH");
		asciiMap.put(2, "STX");
		asciiMap.put(3, "ETX");
		asciiMap.put(4, "EOT");
		asciiMap.put(5, "ENQ");
		asciiMap.put(6, "ACK");
		asciiMap.put(7, "BEL");
		asciiMap.put(8, "BS");
		asciiMap.put(9, "TAB");
		asciiMap.put(10, "LF");
		asciiMap.put(11, "VT");
		asciiMap.put(12, "FF");
		asciiMap.put(13, "CR");
		asciiMap.put(14, "SO");
		asciiMap.put(15, "SI");
		asciiMap.put(16, "DLE");
		asciiMap.put(17, "DC1");
		asciiMap.put(18, "DC2");
		asciiMap.put(19, "DC3");
		asciiMap.put(20, "DC4");
		asciiMap.put(21, "NAK");
		asciiMap.put(22, "SYN");
		asciiMap.put(23, "ETB");
		asciiMap.put(24, "CAN");
		asciiMap.put(25, "EM");
		asciiMap.put(26, "EOF");
		asciiMap.put(27, "ESC");
		asciiMap.put(28, "FS");
		asciiMap.put(29, "GS");
		asciiMap.put(30, "RS");
		asciiMap.put(31, "US");
		asciiMap.put(32, "SPC");
		asciiMap.put(33, "!");
		asciiMap.put(34, "\"");
		asciiMap.put(35, "#");
		asciiMap.put(36, "$");
		asciiMap.put(37, "%");
		asciiMap.put(38, "&");
		asciiMap.put(39, "'");
		asciiMap.put(40, "(");
		asciiMap.put(41, ")");
		asciiMap.put(42, "*");
		asciiMap.put(43, "+");
		asciiMap.put(44, ",");
		asciiMap.put(45, "-");
		asciiMap.put(46, ".");
		asciiMap.put(47, "/");
		asciiMap.put(48, "0");
		asciiMap.put(49, "1");
		asciiMap.put(50, "2");
		asciiMap.put(51, "3");
		asciiMap.put(52, "4");
		asciiMap.put(53, "5");
		asciiMap.put(54, "6");
		asciiMap.put(55, "7");
		asciiMap.put(56, "8");
		asciiMap.put(57, "9");
		asciiMap.put(58, ":");
		asciiMap.put(59, ";");
		asciiMap.put(60, "<");
		asciiMap.put(61, "=");
		asciiMap.put(62, ">");
		asciiMap.put(63, "?");
		asciiMap.put(64, "@");
		asciiMap.put(65, "A");
		asciiMap.put(66, "B");
		asciiMap.put(67, "C");
		asciiMap.put(68, "D");
		asciiMap.put(69, "E");
		asciiMap.put(70, "F");
		asciiMap.put(71, "G");
		asciiMap.put(72, "H");
		asciiMap.put(73, "I");
		asciiMap.put(74, "J");
		asciiMap.put(75, "K");
		asciiMap.put(76, "L");
		asciiMap.put(77, "M");
		asciiMap.put(78, "N");
		asciiMap.put(79, "O");
		asciiMap.put(80, "P");
		asciiMap.put(81, "Q");
		asciiMap.put(82, "R");
		asciiMap.put(83, "S");
		asciiMap.put(84, "T");
		asciiMap.put(85, "U");
		asciiMap.put(86, "V");
		asciiMap.put(87, "W");
		asciiMap.put(88, "X");
		asciiMap.put(89, "Y");
		asciiMap.put(90, "Z");
		asciiMap.put(91, "[");
		asciiMap.put(92, "\\");
		asciiMap.put(93, "]");
		asciiMap.put(94, "^");
		asciiMap.put(95, "_");
		asciiMap.put(96, "`");
		asciiMap.put(97, "a");
		asciiMap.put(98, "b");
		asciiMap.put(99, "c");
		asciiMap.put(100, "d");
		asciiMap.put(101, "e");
		asciiMap.put(102, "f");
		asciiMap.put(103, "g");
		asciiMap.put(104, "h");
		asciiMap.put(105, "i");
		asciiMap.put(106, "j");
		asciiMap.put(107, "k");
		asciiMap.put(108, "l");
		asciiMap.put(109, "m");
		asciiMap.put(110, "n");
		asciiMap.put(111, "o");
		asciiMap.put(112, "p");
		asciiMap.put(113, "q");
		asciiMap.put(114, "r");
		asciiMap.put(115, "s");
		asciiMap.put(116, "t");
		asciiMap.put(117, "u");
		asciiMap.put(118, "v");
		asciiMap.put(119, "w");
		asciiMap.put(120, "x");
		asciiMap.put(121, "y");
		asciiMap.put(122, "z");
		asciiMap.put(123, "{");
		asciiMap.put(124, "|");
		asciiMap.put(125, "}");
		asciiMap.put(126, "~");
		asciiMap.put(127, "DEL");
			
		}

	}
