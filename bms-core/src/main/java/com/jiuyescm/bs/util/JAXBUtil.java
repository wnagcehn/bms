package com.jiuyescm.bs.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.jiuyescm.common.tool.JsonPluginsUtil;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class JAXBUtil {
    
	private static final Logger logger = Logger.getLogger(JsonPluginsUtil.class.getName());

	
    private static ConcurrentHashMap<String, JAXBContext> contextMap = new ConcurrentHashMap<String, JAXBContext>();
    
    public static <T> T formXML(Class<T> clazz, String message) {
        JAXBContext context = contextMap.get(clazz.getPackage().getName());
        Unmarshaller u;
        JAXBElement<T> element;
        try {
            if (context == null) {
                context = JAXBContext.newInstance(clazz.getPackage().getName());
                contextMap.put(clazz.getPackage().getName(), context);
            }
            
            u = context.createUnmarshaller();
            element = u.unmarshal(new StreamSource(new StringReader(message)), clazz);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return element.getValue();

    }
    
    @SuppressWarnings("unchecked")
    public static <T> T formXMLSingle(Class<T> clazz, String message) {
        JAXBContext context = contextMap.get(clazz.getPackage().getName());
        Unmarshaller u;
        try {
            if (context == null) {
                context = JAXBContext.newInstance(clazz.getPackage().getName());
                contextMap.put(clazz.getPackage().getName(), context);
            }
            u = context.createUnmarshaller();
            return (T) u.unmarshal(new StreamSource(new StringReader(message)));

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> String toXML(Object target, String encoding, String schemaLocation, QName qName, NamespacePrefixMapper npMapper) {
        try {
            JAXBContext context = contextMap.get(target.getClass().getPackage().getName());
            if (context == null) {
                context = JAXBContext.newInstance(target.getClass().getPackage().getName());
                contextMap.put(target.getClass().getPackage().getName(), context);
            }
            
            JAXBElement<T> element = new JAXBElement(qName, target.getClass(), null, target);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if(encoding == null)
            	encoding = "UTF-8";
            
            m.setProperty(Marshaller.JAXB_ENCODING, encoding);
            
            if(schemaLocation != null)
            	m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
            
            
            m.setProperty("com.sun.xml.bind.namespacePrefixMapper", npMapper);

            ByteArrayOutputStream ot = new ByteArrayOutputStream();
            m.marshal(element, ot);
            byte[] buf = ot.toByteArray();
            return new String(buf, 0, buf.length, encoding);
        } catch (Exception e) {
			//e.printStackTrace();
        	logger.info(e);
        }
        return null;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> String toXML(Object target, String schemaLocation, QName qName, NamespacePrefixMapper npMapper) {
        try {

            JAXBContext context = contextMap.get(target.getClass().getPackage().getName());
            if (context == null) {
                context = JAXBContext.newInstance(target.getClass().getPackage().getName());
                contextMap.put(target.getClass().getPackage().getName(), context);
            }

            JAXBElement<T> element = new JAXBElement(qName, target.getClass(), null, target);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            if(schemaLocation != null)
            	m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
            
           // m.setProperty("com.sun.xml.bind.namespacePrefixMapper", npMapper);

            ByteArrayOutputStream ot = new ByteArrayOutputStream();
            m.marshal(element, ot);
            byte[] buf = ot.toByteArray();
            return new String(buf, 0, buf.length, "UTF-8");
        } catch (Exception e) {
        	//e.printStackTrace();
        	logger.info(e);
        }
        return null;
    }

    public static Date fromXMLCalendar(XMLGregorianCalendar xmlCalendar) {
        if (xmlCalendar == null)
            return null;
        Calendar cal = xmlCalendar.toGregorianCalendar();
        return cal.getTime();
    }

    public static XMLGregorianCalendar toXMLCalendar(Date date) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        XMLGregorianCalendar cal = null;
        try {
            cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            cal.setYear(calendar.get(Calendar.YEAR));
            cal.setMonth(calendar.get(Calendar.MONTH) + 1);
            cal.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            cal.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            cal.setMinute(calendar.get(Calendar.MINUTE));
            cal.setSecond(calendar.get(Calendar.SECOND));
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        return cal;
    }

	//clazz 绫诲繀椤绘湁@XmlRootElement(name="")鏍硅妭锟�
	public static String marshal(Object obj, Class<?> clazz) {
		String result = null;
		
		try {
		    JAXBContext jc = contextMap.get(clazz.getPackage().getName());
            if (jc == null) {
                jc = JAXBContext.newInstance(clazz.getPackage().getName());
                contextMap.put(clazz.getPackage().getName(), jc);
            }
				
			Marshaller m = jc.createMarshaller();
			
			StringWriter writer = new StringWriter();
			
			m.marshal(obj, writer);
			
			result = writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException("Can't marshal the XML file, error message: " + e.getMessage());
		}
				
		return result;
	}
	
    @SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> clazz, String message) {

        Unmarshaller u;
        try {
            JAXBContext context = contextMap.get(clazz.getPackage().getName());
            if (context == null) {
                context = JAXBContext.newInstance(clazz.getPackage().getName());
                contextMap.put(clazz.getPackage().getName(), context);
            }
            u = context.createUnmarshaller();
            return (T)u.unmarshal(new StreamSource(new StringReader(message)));

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
		
    public static void main(String... s) {
        XMLGregorianCalendar cal = JAXBUtil.toXMLCalendar(new Date());
        System.out.print(cal.getXMLSchemaType());
    }
}
