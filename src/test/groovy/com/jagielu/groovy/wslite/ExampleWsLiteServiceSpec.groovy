package com.jagielu.groovy.wslite

import org.w3c.dom.Document
import wslite.soap.SOAPClient

class ExampleWsLiteServiceSpec extends BaseWsLiteSpec {

    ExampleWsLiteService service = new ExampleWsLiteService()
    SOAPClient client

    void setup() {
        client = Mock(SOAPClient)
        service.client = client
        service.metaClass.log = { req, resp -> }
    }

    def "should pass year to GetMothersDay WS"() {
        given:
            def year = 2013

        when:
            def date = service.getMothersDay(year)

        then:
            1 * client.send(_, _) >> { Map params, Closure requestBuilder ->
                Document doc = buildAndParseXml(requestBuilder)
                assertXpathEvaluatesTo("$year", '//ns:GetMothersDay/ns:year', doc)
                return mockResponse(Responses.mothersDay)
            }

            date == "2013-05-12T00:00:00"
    }

    @Override
    protected Map namespaces() {
        return [ns: 'http://www.27seconds.com/Holidays/US/Dates/']
    }

}
