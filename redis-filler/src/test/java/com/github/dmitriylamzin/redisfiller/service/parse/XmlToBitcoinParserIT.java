package com.github.dmitriylamzin.redisfiller.service.parse;

import com.github.dmitriylamzin.dto.Bitcoin;
import com.github.dmitriylamzin.redisfiller.domain.BitcoinQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlToBitcoinParserIT {

  private final static int NUMBER_OF_PARSED_BITCOINS = 4;

  private final static boolean IS_READY = true;

  @Test
  public void shouldParseFourBitcoins() {

    XmlToBitcoinParser xmlToBitcoinParser = new XmlToBitcoinParser();

    BitcoinQueue bitcoinQueue = xmlToBitcoinParser.
            parseFile(this.getClass().getClassLoader().getResource("test-data.xml").getFile());

    Bitcoin retrievedBitcoin;
    List<Bitcoin> retrievedBitcoins = new ArrayList<>();
    while ((retrievedBitcoin = bitcoinQueue.read()) != null) {
      retrievedBitcoins.add(retrievedBitcoin);
    }

    assertEquals(NUMBER_OF_PARSED_BITCOINS, retrievedBitcoins.size());

    retrievedBitcoins.stream()
            .peek(bitcoin -> assertTrue(bitcoin.getId() != 0))
            .forEach(bitcoin -> assertNotNull(bitcoin.getBitcoinValue()));
  }

  @Test
  public void shouldSetQueueToIsReady() {

    XmlToBitcoinParser xmlToBitcoinParser = new XmlToBitcoinParser();

    BitcoinQueue bitcoinQueue = xmlToBitcoinParser.
            parseFile(this.getClass().getClassLoader().getResource("test-data.xml").getFile());

    assertTrue(bitcoinQueue.isReady());

  }
}
