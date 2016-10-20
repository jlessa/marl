package au.com.gbstore.dbtranslator.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**Reads the properties of .properties file.
 * @author lteixeira
 *
 */
public class PropertiesValue {

	private static final String PROP_FILE_NAME = "persistenceUnit.properties";
/**Read the .properties file.
 * Gets the persistence.unit.name property
 * And return it
 * @return persistenceUnitName
 */
public String getPersistenceUnit(){
	
	Properties prop = new Properties();
	InputStream stream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);
	try {
	if(stream  != null){
		
			prop.load(stream);
		
	}else{
		throw new FileNotFoundException(PROP_FILE_NAME + " file not found");
	}
	} catch (IOException e) {
		e.printStackTrace();
	}
	String persistenceUnitName = prop.getProperty("persistence.unit.name");
	return persistenceUnitName;
}
	
}
