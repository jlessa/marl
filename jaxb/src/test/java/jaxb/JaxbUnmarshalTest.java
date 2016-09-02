package jaxb;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import au.com.marlo.trainning.xmltrainning.user.*;

public class JaxbUnmarshalTest {

	@Ignore
	public void unmarshalUser() throws SAXException, JAXBException{
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new File("src/main/resources/xsd/User.xsd"));
		JAXBContext jc = JAXBContext.newInstance(User.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		//unmarshaller.setSchema(schema);
		
		User user = (User) unmarshaller.unmarshal(new File("src/main/resources/xml/User.xml"));
		
	}
	
}
