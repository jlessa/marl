package jaxb;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Ignore;
import org.junit.Test;

import au.com.marlo.trainning.xmltrainning.user.User;

public class JaxbMarshalTest {

	@Ignore
	public void marshalUser() {
		User user = new User();
		user.setName("Teste");
		user.setAge(new BigInteger("20"));
		user.setGender("male");
		user.setDocumentID(new BigInteger("123220"));
		user.setAvailableAccountCredit(new BigDecimal("12312.43"));
		
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(user, System.out);
			//marshaller.marshal(user, new File("src/main/resources/xml/user2.xml"));
		} catch (JAXBException e) {
			System.err.println(e.getMessage());
		}			
		
	}

	
}
