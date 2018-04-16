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

	abstract public String getDefaultIp();
	abstract public int getDefaultPort();
	abstract public int getSocketSendWait();
	abstract public int getSocketReceiveWait();
	abstract public int getSocketReceiveTimeout();
	abstract public boolean getDebug();
	abstract public boolean getInfo();

	public int getRepeatDelay() {
		return repeatDelay;
	}
	
	public CommunicationDevice() {
		init(getDefaultIp(), getDefaultPort(), getSocketSendWait());
	}
	
	public CommunicationDevice(String ipAddress) {
		init(ipAddress, getDefaultPort(), getSocketSendWait());
	}
	
	public CommunicationDevice(String ipAddress, int port) {
		init(ipAddress, port, getSocketSendWait());
	}
	
	public CommunicationDevice(String ipAddress, int port, int delay) {
		init(ipAddress, port, delay);
	}
	
	public void init(String ipAddress, int port, int delay) {
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

	
	public String sendCommand(String command) {
		return sendCommand(command, 1);
	}

	public String sendCommand(String command, int repeat) {
		return sendCommand(command, repeat, command);
	}
	
	public String sendCommand(String command, int repeat, String displayCommand) {
		return processSendCommand(command, repeat, getSocketSendWait(), displayCommand);
	}
	
	public String sendCommand(String command, int repeat, int delay) {
		return sendCommand(command, repeat, delay, command);
	}

	public String sendCommand(String command, int repeat, int delay, String displayCommand) {
		return processSendCommand(command, repeat, delay, displayCommand);
	}
	
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
	 * This method reads responses (possibly more than one) after a query
	 * command. It can end early when it finds the respose you are waitng for by
	 * passing in the command you called.
	 * 
	 * @param command
	 *            is used to end the response processing early when it finds the
	 *            command - if you want all responses processed pass in -1
	 * @return an array of the data portion of the response messages only -
	 *         There might be more than one response message received.
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
