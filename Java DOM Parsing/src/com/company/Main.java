package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            File inputFile = new File("D:\\STUDENT\\domxml.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Node studentsNode=doc.getDocumentElement().getFirstChild();
            NodeList nList=studentsNode.getChildNodes();
            for(int i=0;i<nList.getLength();i++)
            {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) nNode;
                    Element elemSecond=(Element)nNode.getFirstChild();
                    System.out.println(elem.getAttribute("nrmMatricol")+" "+elem.getAttribute("nume")+" "+elem.getAttribute("prenume")+" "+elem.getAttribute("grupa")+" "+"An:"+elem.getAttribute("an")+" "+elemSecond.getAttribute("titluCurs")+" "+"AnCurs:"+elemSecond.getAttribute("anCurs")+" "+"valoareNota:"+elemSecond.getTextContent());
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
