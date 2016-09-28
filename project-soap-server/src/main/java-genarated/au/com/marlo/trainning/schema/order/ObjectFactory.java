
package au.com.marlo.trainning.schema.order;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import au.com.marlo.trainning.xmltrainning.request.Request;
import au.com.marlo.trainning.xmltrainning.response.Response;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the au.com.marlo.trainning.schema.order package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OrderInquiryResponse_QNAME = new QName("http://www.marlo.com.au/trainning/schema/Order", "orderInquiryResponse");
    private final static QName _OrderInquiry_QNAME = new QName("http://www.marlo.com.au/trainning/schema/Order", "orderInquiry");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: au.com.marlo.trainning.schema.order
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marlo.com.au/trainning/schema/Order", name = "orderInquiryResponse")
    public JAXBElement<Response> createOrderInquiryResponse(Response value) {
        return new JAXBElement<Response>(_OrderInquiryResponse_QNAME, Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Request }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.marlo.com.au/trainning/schema/Order", name = "orderInquiry")
    public JAXBElement<Request> createOrderInquiry(Request value) {
        return new JAXBElement<Request>(_OrderInquiry_QNAME, Request.class, null, value);
    }

}
