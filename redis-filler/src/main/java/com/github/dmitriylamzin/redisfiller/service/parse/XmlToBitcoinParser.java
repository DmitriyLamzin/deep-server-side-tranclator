package com.github.dmitriylamzin.redisfiller.service.parse;

import com.github.dmitriylamzin.dto.Bitcoin;
import com.github.dmitriylamzin.redisfiller.domain.BitcoinQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XmlToBitcoinParser {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private boolean isId;
  private boolean isBitcoin;
  private boolean isRecord;

  public BitcoinQueue parseFile(String xmlFile) {
    BitcoinQueue bitcoinQueue = new BitcoinQueue();

    try {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLEventReader eventReader =
              factory.createXMLEventReader(
                      new FileReader(xmlFile));
      while(eventReader.hasNext()){
        XMLEvent event = eventReader.nextEvent();
        switch(event.getEventType()){
          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            String elementName = startElement.getName().getLocalPart();
            if (elementName.equals("dataset")) {
              logger.debug("Start Element : dataset");
              break;
            } else if (elementName.equals("record")) {
              logger.debug("Start Element : record");
              isRecord = true;
              bitcoinQueue.put(buildBitcoinObject(new Bitcoin(), eventReader));
              break;
            }
            break;
          case  XMLStreamConstants.END_ELEMENT:
            EndElement endElement = event.asEndElement();
            elementName = endElement.getName().getLocalPart();
            if (elementName.equals("dataset")) {
              logger.debug("end of file");
              bitcoinQueue.setIsReady(true);
            }
            break;
        }
      }
    } catch (FileNotFoundException e) {
      logger.error("the provided file: " + xmlFile + " is not found", e);
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    return bitcoinQueue;
  }

  private Bitcoin buildBitcoinObject(Bitcoin bitcoin, XMLEventReader eventReader) {
    while (isRecord && eventReader.hasNext()){
      try {
        XMLEvent event = eventReader.nextEvent();
        switch(event.getEventType()) {
          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            String elementName = startElement.getName().getLocalPart();
            if (elementName.equalsIgnoreCase("id")) {
              logger.debug("Start Element : id");
              isId = true;
            } else if (elementName.equalsIgnoreCase("bitcoin")) {
              logger.debug("Start Element : bitcoin");
              isBitcoin = true;
            }
            break;
          case XMLStreamConstants.CHARACTERS:
            Characters characters = event.asCharacters();
            if (isId) {
              logger.info("Id: " + characters.getData());
              bitcoin.setId(Long.parseLong(characters.getData()));
              isId = false;
            }
            if (isBitcoin) {
              logger.info("Bitcoin data: " + characters.getData());
              bitcoin.setBitcoinValue(characters.getData());
              isBitcoin = false;
            }
            break;
          case XMLStreamConstants.END_ELEMENT:
            EndElement endElement = event.asEndElement();
            elementName = endElement.getName().getLocalPart();
            if (elementName.equalsIgnoreCase("record")) {
              logger.debug("end of bitcoin object");
              isRecord = false;
              return bitcoin;

            }
            break;
          }
        } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    }
    return bitcoin;
  }
}
