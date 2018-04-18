package com.taylor.utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public abstract class CommunicationDevice {
		
	private String ipAddress;
	private int port;
	private Socket socket;
	private int repeatDelay;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	/**
	 * @return default IP Address to connect to
	 */
	abstract public String getDefaultIp();
	
	/**
	 * @return default port to connect to
	 */
	abstract public int getDefaultPort();
	
	/**
	 * @return miliseconds to wait before sending subsequent messages
	 */
	abstract public int getSocketSendWait();
	
	/**
	 * @return miliseconds to wait before looking for a response
	 */
	abstract public int getSocketReceiveWait();
	
	/**
	 * @return miliseconds to wait for a response before timeout
	 */
	abstract public int getSocketReceiveTimeout();
	
	/**
	 * @return display debug output true/false
	 */
	abstract public boolean getDebug();
	
	/**
	 * @return display info output true/false (subset of debug)
	 */
	abstract public boolean getInfo();

	public int getRepeatDelay() {
		return repeatDelay;
	}
	
	/**
	 * default constructor - uses default ip, port and delay
	 */
	public CommunicationDevice() {
		init(getDefaultIp(), getDefaultPort(), getSocketSendWait());
	}
	
	/**
	 * Constructor - used default port and delay
	 * @param ipAddress
	 */
	public CommunicationDevice(String ipAddress) {
		init(ipAddress, getDefaultPort(), getSocketSendWait());
	}
	
	/**
	 * Constructor - uses default delay
	 * @param ipAddress
	 * @param port
	 */
	public CommunicationDevice(String ipAddress, int port) {
		init(ipAddress, port, getSocketSendWait());
	}
	
	/**
	 * Constructor
	 * @param ipAddress ip address to connect to
	 * @param port network port to connect to
	 * @param delay delay between subsequent commands
	 */
	public CommunicationDevice(String ipAddress, int port, int delay) {
		init(ipAddress, port, delay);
	}
	
	/**
	 * Initialize the object
	 * @param ipAddress - ipaddress for the device
	 * @param port - socket port for the device
	 * @param delay - command repeat delay
	 */
	protected void init(String ipAddress, int port, int delay) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.repeatDelay = delay;
	}
	
	/**
	 * Connects to the receiver by opening a socket connection through the eISCP
	 * port.
	 **/
	public boolean connect() {
		
		boolean result = false;

		try {
			if (socket == null || !socket.isConnected()) {
				
				// 1. creating a socket to connect to the server
				socket = new Socket(ipAddress, port);
				if (getDebug() || getInfo()) System.out.println("Connected to " + ipAddress + ":" + port);
				
				// Timeout must be set to something other than zero or read command will block indefinitly
				socket.setSoTimeout(getSocketReceiveTimeout());
				
				// 2. get Input and Output streams
				outputStream = new DataOutputStream(socket.getOutputStream());
				inputStream = new DataInputStream(socket.getInputStream());

				// System.out.println("out_Init");
				outputStream.flush();
				
				if (socket.isConnected()) result = true;
			}
			else if (socket.isConnected()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * test the connection to the receiver by opening a socket connection
	 * through the eISCP port AND THEN CLOSES it if it was not already open.
	 * this method can be used when you need to specificly specify the IP and
	 * PORT. If the default port is used then you could also use the
	 * {@link #testConnection(String) testConnection} method (that used the
	 * default port) or the {@link #testConnection() testConnection} method
	 * (that used the default IP and port).
	 *
	 * @return true if already connected or can connect, and false if can't
	 *         connect
	 **/
	public boolean testConnection() {
		
		boolean result = false;

		if (socket.isConnected()) {
			// if we're already connected - test done!
			result = true;
		}
		else {
			result = connect();
			
		}
		return result;
	}

	/**
	 * Closes the socket connection.
	 * 
	 * @return true if the closed succesfully
	 **/
	public boolean disconnect() {

		boolean acted = false;

		// 4: Closing connection
		try {
			
			// close the inputStream
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
				acted = true;
			}
			
			// close the outputStream
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
				acted = true;
			}
			
			if (socket != null) {
				socket.close();
				socket = null;
				acted = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acted;
	}

	/**
	 * Send command to the device
	 * @param command command to send 
	 * @return response from device
	 */
	public String sendCommand(String command) {
		return sendCommand(command, 1);
	}

	/**
	 * Send command to the device
	 * @param command command to send
	 * @param repeat number of times to repeat command
	 * @return response from device
	 */
	public String sendCommand(String command, int repeat) {
		return sendCommand(command, repeat, command);
	}
	
	/**
	 * Send command to the device
	 * @param command command to send
	 * @param repeat number of times to repeat command
	 * @param displayCommand "friendly" command name to display
	 * @return response from device
	 */
	public String sendCommand(String command, int repeat, String displayCommand) {
		return processSendCommand(command, repeat, getSocketSendWait(), displayCommand);
	}
	
	/**
	 * Send command to the device
	 * @param command command to send
	 * @param repeat number of times to repeat command
	 * @param delay delay betweeen subsequent commands being sent
	 * @return response from device
	 */
	public String sendCommand(String command, int repeat, int delay) {
		return sendCommand(command, repeat, delay, command);
	}

	/**
	 * Send command to the device
	 * @param command command to send
	 * @param repeat number of times to repeat command
	 * @param delay delay betweeen subsequent commands being sent
	 * @param displayCommand "friendly" command to display
	 * @return response from device
	 */
	public String sendCommand(String command, int repeat, int delay, String displayCommand) {
		return processSendCommand(command, repeat, delay, displayCommand);
	}
	
	/**
	 * Send command to the device
	 * @param command command to send
	 * @param repeat number of times to repeat command
	 * @param delay delay betweeen subsequent commands being sent
	 * @param displayCommand "friendly" command to display
	 * @return response from device
	 */
	private String processSendCommand(String command, int repeat, int delay, String displayCommand) {

		if (connect()) {
			try {
				if (getDebug()) {
					System.out.println("Sending " + displayCommand + " (" + command + ") " + repeat + " time(s)");
					Util.displayStringDebug(command);
				} else if (getInfo()) {
					System.out.println("Sending " + displayCommand + (repeat==1 ? "" : repeat + " time(s)"));
				}
							
				for (int i = 0; i < repeat; i++) {
					
					if (i > 0) {
						// sleep before sending another command if this isn't our first 
						Util.sleep(delay);
					}
					
					// write the message to the output stream & flush
					outputStream.writeBytes(command);
					outputStream.flush();
					
					if (getDebug() || getInfo()) System.out.println("Send #1: " + Util.getTimestamp());
				}
	
				if (getDebug() || getInfo()) System.out.println("Sent!");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}

		String response = readResponse(); 
		
		return response;
	}


	/**
	 * Reads response from device
	 * 
	 * @return response from device
	 **/
	public String readResponse() {	

		// wait the specified time before attempting to read from the socket
		Util.sleep(getSocketReceiveWait());

		String response = new String();
		
		if (socket.isConnected()) {
			try {
				if (getDebug()) System.out.println("\nReading Response Packet");
				
				// build a buffer for the data received in the response
				StringBuffer responseBuffer = new StringBuffer();
				int bytesReceived = 0;
				
				socket.setSoTimeout(getSocketReceiveTimeout());
				
				// Read from the inputStream and write to the responseBuffer StringBuffer
				boolean continueReading = true;
				int packetCounter = 0;
				do {
					try {
						byte[] bufferBytes = new byte[1024]; 
						int bufferLength = inputStream.read(bufferBytes);
						if (bufferLength <= 0) {
							continueReading = false;
							continue;
						}
						
						String bufferString = new String(bufferBytes).substring(0, bufferLength);
						bytesReceived += bufferLength;
						
						responseBuffer.append(bufferString);
						
						if (bufferLength < bufferBytes.length) {
							continueReading = false;
						}
						
						if (getDebug()) System.out.println("packet[" + ++packetCounter + "] " + bufferLength + " bytes :" + Util.displayAsciiCodes(bufferString, false, true));
					} catch (SocketTimeoutException e) {
						if (getDebug()) System.out.println("Done reading response - Bytes received: " + bytesReceived + "\n\n");
						continueReading = false;
					} catch (Exception e) {
						e.printStackTrace();;
						continueReading = false;
					}
				} while(continueReading);
				

				/* Response is done... process it into dataMessages */
				// *******************************************
				char[] responseChars = responseBuffer.toString().substring(0, bytesReceived).toCharArray();
				response = new String(responseChars);
				
								
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("!!Not Connected to Receive ");
		}
		
		return response;
	}

}
