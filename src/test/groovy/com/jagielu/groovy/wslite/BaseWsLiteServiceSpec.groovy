package com.jagielu.groovy.wslite

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.SimpleNamespaceContext
import org.custommonkey.xmlunit.XMLUnit
import org.custommonkey.xmlunit.XpathEngine
import org.custommonkey.xmlunit.exceptions.XpathException
import org.junit.Assert
import spock.lang.Specification
import wslite.soap.SOAPResponse

abstract class BaseWsLiteSpec extends Specification {

    protected abstract Map namespaces()

    protected XpathEngine engine

    void setup() {
        engine = XMLUnit.newXpathEngine()
        engine.setNamespaceContext(new SimpleNamespaceContext(namespaces()))
    }

    protected String prepareAndParseXml(Closure xmlBuilder) {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        builder.xml(xmlBuilder)
        return writer.toString()
    }

    protected void assertXpathEvaluatesTo(String expectedValue,
                                          String xpathExpression, String xml) throws XpathException {
        def doc = XMLUnit.buildControlDocument(xml)
        Assert.assertEquals(expectedValue,
                engine.evaluate(xpathExpression, doc))
    }

    protected SOAPResponse mockResponse(String resp) {
        def envelope = new XmlSlurper().parseText(resp)
        new SOAPResponse(envelope: envelope)
    }

}
