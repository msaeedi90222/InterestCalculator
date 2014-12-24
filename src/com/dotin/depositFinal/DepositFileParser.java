package com.dotin.depositFinal;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


public class DepositFileParser {
    private static void processNodeList(NodeList nodeList){
        for (int i=0 ; i< nodeList.getLength() ; i++){
            Node elementNode = nodeList.item(i);
            if(elementNode.getNodeType() == Node.ELEMENT_NODE){
                //get node name and value
                System.out.println("\n Node name = " + elementNode.getNodeName() + "[open]");
                System.out.println("Node Content = " + elementNode.getTextContent());
                if (elementNode.hasAttributes()){
                    NamedNodeMap nodeMap = elementNode.getAttributes();
                    for (int j=0;j<nodeMap.getLength();j++){
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                    }
                }
                if (elementNode.hasChildNodes()){
                    processNodeList(elementNode.getChildNodes());
                }
            }
        }
    }
    public static class DepositComparator implements Comparator<Deposits>{
        @Override
        public int compare(Deposits deposit1, Deposits deposit2){
            return deposit1.interestValue.compareTo(deposit2.interestValue);
        }
    }



    public static void main(String[] args){
        String filePath = "deposits.xml";
        try {

            List<Deposits> depositInterestList = new ArrayList<Deposits>();
            File xmlFile = new File(filePath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            NodeList nodeList = document.getElementsByTagName("depositFinal");
            for (int i=0 ; i<nodeList.getLength() ; i ++){
                Node docNode = nodeList.item(i);
                if (docNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element depositList = (Element)docNode;
                    String customerNumber = depositList.getElementsByTagName("customerNumber").item(0).getTextContent();
                    String depositType = depositList.getElementsByTagName("depositType").item(0).getTextContent();
                    String depositBalance = depositList.getElementsByTagName("depositBalance").item(0).getTextContent();

                    String durationInDays = depositList.getElementsByTagName("durationInDays").item(0).getTextContent();

                        Class deposit = Class.forName("com.dotin.depositFinal."+depositType);
                        Deposits d;
                        BigDecimal balance = new BigDecimal(depositBalance);

                        if (balance.compareTo(BigDecimal.ZERO) == 0) {
                            System.out.println("there is no any money in this account!");
                        }
                        if (Integer.parseInt(durationInDays)==0 || Integer.parseInt(durationInDays) < 0){
                            System.out.println("Zero day remained!");
                            continue;
                        }
                        if (depositType.equals("ShortTerm")) {
                            d = (ShortTerm) deposit.newInstance();
                        } else if (depositType.equals("LongTerm")) {
                            d = (LongTerm) deposit.newInstance();
                        } else if (depositType.equals("Qarz")) {
                            d = (Qarz) deposit.newInstance();
                        } else {
                            System.out.println("It's no a valid depositFinal!");
                            d = new Qarz();
                            //blah blah
                        }
                        d.setCustomerNumber(Integer.valueOf(customerNumber));
                        d.setDepositBalance(new BigDecimal(depositBalance));
                        d.setDurationInDays(Integer.valueOf(durationInDays));
                        d.setInterest();
                        d.calculateInterest();
                        depositInterestList.add(d);
                }
            }

            Collections.sort(depositInterestList);

            for(int i = 0; i< depositInterestList.size(); i++){
                System.out.print(depositInterestList.get(i).customerNumber);
                System.out.print('#');
                System.out.println(depositInterestList.get(i).interestValue);

            }

        } catch (FileNotFoundException e) {
            System.out.println("Sorry! Deposit XML File is not accessible or maybe removed from System.");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.out.println("It's not a valid depositFinal type!");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
