package com.taylor.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class AbstractConfig {

	private static final String CONFIG_ENV_VARIABLE = "config_file";
	
	protected String filePath;
	protected Document configDoc;
	protected boolean debug;
	protected boolean info;
	protected Map<String, String> commands;
	
	protected abstract String getDefaultFilename();
	
	public String getFilePath() {
		return filePath;
	}
	
	protected Document getConfigDocument() {
		return configDoc;
	}
	
	public boolean getDebug() {
		return debug;
	}
	
	public boolean getInfo() {
		return info;
	}
	
	protected Map<String, String> getAbstractCommands() {
		return commands;
	}
	
	protected String getEnvFilePath() {
		return System.getenv(CONFIG_ENV_VARIABLE);
	}
	
	protected AbstractConfig() throws Exception {
		String configFilePath = getEnvFilePath();
		init(configFilePath);
	}
	
	protected AbstractConfig(String configFilePath) throws Exception {
		init(configFilePath);
	}
	
	/**
	 * Initializes this Object
	 * @param configFilePath
	 * @throws Exception 
	 */
	protected void init(String configFilePath) throws Exception{

		// if passed value is null, set to default file name
		if (configFilePath == null) {
			configFilePath = getDefaultFilename();
		}
		// set the local var
		this.filePath = configFilePath;
		Document configDocument = getConfigFile(configFilePath);
		this.configDoc = configDocument;
		parseConfigFile(configDocument);
	}
	
	/**
	 * Gets an XML Document from the passed configFilePath
	 * @param configFilePath - path of the XML file to read
	 * @return XML Document from configFilePath
	 * @throws Exception 
	 */
	private Document getConfigFile(String configFilePath) throws Exception {		
		Document configDocument = null;

		try {
			File xmlFile = new File(configFilePath);
			if (!xmlFile.exists()){
				throw new FileNotFoundException(configFilePath + " not found");
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			configDocument = dBuilder.parse(xmlFile);
			configDocument.getDocumentElement().normalize();
		} catch (Exception e) {
			// display a message
			StringBuilder output = new StringBuilder();
			
			// if the environment variable specifying the path isn't found - let the user know
			if (getEnvFilePath() == null) {
				output.append("Environment variable \"" + CONFIG_ENV_VARIABLE + "\" not found, " 
						+ "using default file \"" + configFilePath + "\" located in script directory\n");
			}
			else {
				output.append("Environment variable \"" + CONFIG_ENV_VARIABLE + "\" found, "
						+ "using specified file \"" + configFilePath + "\"");
			}
			
			output.append("  Error reading config file: " + getFilePath() + " (" + e.getMessage() + ")");
			
			throw new Exception(output.toString());
			
		}
		
		return configDocument;
	}

	private void parseConfigFile(Document configDocument) {
		
		if (getDebug()) {
			System.out.println("Root element :" + configDocument.getDocumentElement().getNodeName());
		}
	
		// get the debug value
		try {
			// assume false until we can prove otherwise
			debug = false;
			Element element = (Element) configDocument.getElementsByTagName("debug").item(0);
			String tempValue = element.getAttribute("value").toLowerCase();
			if (tempValue.equals("true")) {
				debug = true;
			} 
		} catch (Exception e) {
			// no action needed - just avoiding a crash if <debug> wasn't found
		}
		
		// get the info value
		try {
			// assume false until we can prove otherwise
			info = false;
			Element element = (Element) configDocument.getElementsByTagName("info").item(0);
			String tempValue = element.getAttribute("value").toLowerCase();
			if (tempValue.equals("true")) {
				info = true;
			} 
		} catch (Exception e) {
			// no action needed - just avoiding a crash if <info> wasn't found
		}
		
		// get the commands
		try {
			commands = new HashMap<String, String>();
			NodeList nodes = configDocument.getElementsByTagName("command");
			for(int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				commands.put(element.getAttribute("name"),  element.getAttribute("value"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
