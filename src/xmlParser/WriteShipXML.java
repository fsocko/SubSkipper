package xmlParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;



public class WriteShipXML
{
		
			public void marshalingExample(Ships shipWrap) throws JAXBException {
				JAXBContext jaxbContext = JAXBContext.newInstance(Ships.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				File file = new File("shipData/testFile.xml");
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(shipWrap, System.out);
				jaxbMarshaller.marshal(shipWrap, file);
			}	
		
		//catch(IOException | JAXBException ex){}
}
