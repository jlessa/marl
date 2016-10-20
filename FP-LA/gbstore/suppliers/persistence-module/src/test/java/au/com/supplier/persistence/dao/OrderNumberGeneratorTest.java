package au.com.supplier.persistence.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderNumberGeneratorTest {
	
	OrderNumberGenerator generator = new OrderNumberGenerator();
	
	/**Test if this method generates a orderNumber
	 * @throws Exception 
	 */
	@Test
	public void getNewOrderNumber() throws Exception{
		Long on = generator.generateNewOrderNumber();
		assertNotNull(on);
	}
	
}
