package xmlcontrol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import control.Main;
import video.Video;

public class XMLWriter {

	public static final String DRIVER_PATH = "./src/xml/driver_info.xml";
	
	private DocumentBuilder myBuilder;
	private Transformer myTransformer;

	public XMLWriter() throws FileNotFoundException, SAXException, IOException, ParserConfigurationException, TransformerConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		myBuilder = factory.newDocumentBuilder();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		myTransformer = transformerFactory.newTransformer();
		myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
	}

	public void editMasterFile(ObservableList<Video> videoList) throws TransformerException{
		Document document = buildMasterDocument(videoList);
		writeFile(document, new File(XMLParser.FILE_PATH));
	}

	private Attr makeNode(Document document, String name, String value) {
		Attr attr = document.createAttribute(name);
		attr.setValue(value);
		return attr;
	}

	/**
	 * Generates the xml file for the drivers.
	 * @throws TransformerException 
	 */
	public void buildDriverFile(ObservableList<Video> videoList) throws TransformerException {
		Document document = buildDriverDocument(videoList);
		writeFile(document, new File("./src/xml/driver_info.xml"));
	}
	
	
	
	private Document buildDriverDocument(ObservableList<Video> videoList){
		return buildDocument(videoList, true);
	}
	
	private Document buildMasterDocument(ObservableList<Video> videoList){
		return buildDocument(videoList, false);
	}

	private Document buildDocument(ObservableList<Video> videoList, boolean forDriver) {
		Document document = myBuilder.newDocument();
		document.appendChild(document.createElement("videos"));
		for(Video video:videoList){
			Element videoNode = document.createElement("video");
			videoNode.setAttributeNode(makeNode(document, "title", video.getMyName()));
			videoNode.setAttributeNode(makeNode(document, "company", video.getMyCompany()));
			if(forDriver){
				videoNode.setAttributeNode(makeNode(document, "playsRemaining", ""+ video.getMyPlaysRemaining()/Main.NUM_DRIVERS));
			}
			else{
				videoNode.setAttributeNode(makeNode(document, "playsPurchased", ""+ video.getMyPlaysPurchased()));
				videoNode.setAttributeNode(makeNode(document, "playsRemaining", ""+ video.getMyPlaysRemaining()));
			}
			videoNode.setAttributeNode(makeNode(document, "length", ""+video.getMyLength()));
			document.getDocumentElement().appendChild(videoNode);	
		}
		return document;
	}

	private void writeFile(Document document, File xmlFile) throws TransformerException {
		xmlFile.setWritable(true);
		StreamResult result = new StreamResult(xmlFile);
		myTransformer.transform(new DOMSource(document), result);
		xmlFile.setReadOnly();
		System.out.println("File saved!");
	}
}
