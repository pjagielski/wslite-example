package com.jagielu.groovy.wslite

class Runner {

    public static void main(String[] args) {
        def service = new ExampleWsLiteService()
        println service.getMothersDay(2013)
    }

}