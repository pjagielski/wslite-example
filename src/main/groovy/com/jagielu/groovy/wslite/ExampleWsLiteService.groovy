package com.jagielu.groovy.wslite

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import wslite.http.HTTPClientException
import wslite.http.HTTPRequest
import wslite.http.HTTPResponse
import wslite.soap.SOAPClient
import wslite.soap.SOAPFaultException
import wslite.soap.SOAPResponse

class ExampleWsLiteService {

    private static Log log = LogFactory.getLog(ExampleWsLiteService)

    SOAPClient client = new SOAPClient('http://www.hoqwlidaywebservice.com/Holidays/US/Dates/USHolidayDates.asmx')

    def getMothersDay(long _year) {
        def response = send('http://www.27seconds.com/Holidays/US/Dates/GetMothersDay') {
            body {
                GetMothersDay('xmlns':'http://www.27seconds.com/Holidays/US/Dates/') {
                    year(_year)
                }
            }
        }
        response.GetMothersDayResponse.GetMothersDayResult.text()
    }

    def send(String action, Closure cl) {
        withExceptionHandler {
            sendWithLogging(action, cl)
        }
    }

    private SOAPResponse sendWithLogging(String action, Closure cl) {
        SOAPResponse response = client.send(SOAPAction: action, cl)
        log(response?.httpRequest, response?.httpResponse)
        return response
    }

    private SOAPResponse withExceptionHandler(Closure cl) {
        try {
            cl.call()
        } catch (SOAPFaultException soapEx) {
            log(soapEx.httpRequest, soapEx.httpResponse)
            def message = soapEx.hasFault() ? soapEx.fault.text() : soapEx.message
            throw new InfrastructureException(message)
        } catch (HTTPClientException httpEx) {
            log(httpEx.request, httpEx.response)
            throw new InfrastructureException(httpEx.message)
        }
    }

    private void log(HTTPRequest request, HTTPResponse response) {
        log.debug("HTTPRequest $request with content:\n${request?.contentAsString}")
        log.debug("HTTPResponse $response with content:\n${response?.contentAsString}")
    }

}
