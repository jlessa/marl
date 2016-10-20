package au.com.gbstore.dbtranslator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
/**Entry model.
 * @author lteixeira
 *
 */
@Entity
public class Entry {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long entryId;
@Temporal(TemporalType.TIMESTAMP)
private Date insertDate;
@Temporal(TemporalType.TIMESTAMP)
private Date processDate;
@OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch=FetchType.LAZY)
private List<StoreOrder> storeOrderList = new ArrayList<StoreOrder>();

/**Entry model constructor.
 */
public Entry() {
	}
	
	/** get InsertDate and convert it to XMLGregorianCalendar.
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public XMLGregorianCalendar getInsertDate() throws DatatypeConfigurationException {
		if(insertDate == null)
			return null;
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(insertDate);
		return  DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
	}
	
	/** set InsertDate.
	 * @param insertDate
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	
	/**get ProcessDate and convert it to XMLGregorianCalendar.
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public XMLGregorianCalendar getProcessDate() throws DatatypeConfigurationException {
		if(processDate==null)
			return null;
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(processDate);
		return  DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
	}
	
	/**set ProcessDate.
	 * @param processDate
	 */
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
	/**get EntryId.
	 * @return
	 */
	public Long getEntryId() {
		return entryId;
	}
	/**set EntryId.
	 * @param entryId
	 */
	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}
	/** get StoreOrderList.
	 * @return
	 */
	public List<StoreOrder> getStoreOrderList() {
		return storeOrderList;
	}
	/**set StoreOrderList.
	 * @param storeOrderList
	 */
	public void setStoreOrderList(List<StoreOrder> storeOrderList) {
		this.storeOrderList = storeOrderList;
	}	
}
