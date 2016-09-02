import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import au.com.marlo.trainning.xmltrainning.user.ObjectFactory;
import au.com.marlo.trainning.xmltrainning.user.User;

public class Main {
	public static void main (String ...args){
		User user = new ObjectFactory().createUser();
		user.setName("Teste");
		user.setAge(new BigInteger("20"));
		user.setGender("male");
		user.setDocumentID(new BigInteger("123220"));
		user.setAvailableAccountCredit(new BigDecimal("12312.43"));
		
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(User.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(user,System.out);
			
			//marshaller.marshal(user, new File("src/main/resources/xml/user2.xml"));
		} catch (Exception e) {
			System.err.println("Erro: " + e.getMessage());
		}			
	}
}
