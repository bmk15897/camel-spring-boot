/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dataformat.bindy.springboot.csv;


import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.dataformat.bindy.format.factories.DefaultFactoryRegistry;
import org.apache.camel.dataformat.bindy.model.car.Car;
import org.apache.camel.dataformat.bindy.model.car.Car.Colour;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.spring.boot.CamelAutoConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;


@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        BindyCarQuoteAndCommaDelimiterTest.class,
        BindyCarQuoteAndCommaDelimiterTest.TestConfiguration.class
    }
)
public class BindyCarQuoteAndCommaDelimiterTest {

    @Autowired
    ProducerTemplate template;
    
    @EndpointInject("mock:in")
    MockEndpoint mockIn;
    
    @EndpointInject("mock:out")
    MockEndpoint mockOut;
    
    private static final String HEADER = "\"stockid\";\"make\";\"model\";\"deriv\";\"series\";\"registration\";\"chassis\";\"engine\";\"year\""
                                         + ";\"klms\";\"body\";\"colour\";\"enginesize\";\"trans\";\"fuel\";\"options\";\"desc\";\"status\";\"Reserve_price\";\"nvic\"";
    private static final String ROW = "\"SS552\";\"TOYOTA\";\"KLUGER\";\"CV 4X4\";\"MCU28R UPGRADE\";\"TBA\";\"\";\"\";\"2005\";\"155000.0\";\"4D WAGON\""
                                      + ";\"BLACK\";\"3.3 LTR\";\"5 Sp Auto\";\"MULTI POINT FINJ\";\"POWER MIRRORS, POWER STEERING, POWER WINDOWS, CRUISE CONTROL,"
                                      + " ENGINE IMMOBILISER, BRAKE ASSIST, DUAL AIRBAG PACKAGE, ANTI-LOCK BRAKING, CENTRAL LOCKING REMOTE CONTROL, ALARM SYSTEM/REMOTE"
                                      + " ANTI THEFT, AUTOMATIC AIR CON / CLIMATE CONTROL, ELECTRONIC BRAKE FORCE DISTRIBUTION, CLOTH TRIM, LIMITED SLIP DIFFERENTIAL,"
                                      + " RADIO CD WITH 6 SPEAKERS\";\"Dual Airbag Package, Anti-lock Braking, Automatic Air Con / Climate Control, Alarm System/Remote"
                                      + " Anti Theft, Brake Assist, Cruise Control, Central Locking Remote Control, Cloth Trim, Electronic Brake Force Distribution,"
                                      + " Engine Immobiliser, Limited Slip Differential, Power Mirrors, Power Steering, Power Windows, Radio CD with 6 Speakers"
                                      + " CV GOOD KLMS AUTO POWER OPTIONS GOOD KLMS   \";\"Used\";\"0.0\";\"EZR05I\"\n";

    
    
    @Bean("defaultFactoryRegistry") 
    DefaultFactoryRegistry getDefaultFactoryRegistry() {
        return new DefaultFactoryRegistry();
    }
    
    @Test
    public void testBindyUnmarshalQuoteAndCommaDelimiter() throws Exception {
        
        mockOut.expectedMessageCount(1);

        template.sendBody("direct:out", HEADER + "\n" + ROW);

        mockOut.assertIsSatisfied();

        Car rec1 = mockOut.getReceivedExchanges().get(0).getIn().getBody(Car.class);

        assertEquals("SS552", rec1.getStockid());
        assertEquals("TOYOTA", rec1.getMake());
        assertEquals("KLUGER", rec1.getModel());
        assertEquals(2005, rec1.getYear());
        assertEquals(Double.valueOf("155000.0"), rec1.getKlms(), 0.0001);
        assertEquals("EZR05I", rec1.getNvic());
        assertEquals("Used", rec1.getStatus());
        assertEquals(Car.Colour.BLACK, rec1.getColour());
    }

    @Test
    public void testBindyMarshalQuoteAndCommaDelimiter() throws Exception {
        
        mockIn.expectedMessageCount(1);

        Car car = getCar();

        template.sendBody("direct:in", car);

        mockIn.assertIsSatisfied();

        String body = mockIn.getReceivedExchanges().get(0).getIn().getBody(String.class);
        assertEquals(ROW, body);
    }

    private Car getCar() {
        Car car = new Car();
        car.setStockid("SS552");
        car.setMake("TOYOTA");
        car.setModel("KLUGER");
        car.setDeriv("CV 4X4");
        car.setSeries("MCU28R UPGRADE");
        car.setRegistration("TBA");
        car.setChassis("");
        car.setEngine("");
        car.setYear(2005);
        car.setKlms(155000);
        car.setBody("4D WAGON");
        car.setColour(Colour.BLACK);
        car.setEnginesize("3.3 LTR");
        car.setTrans("5 Sp Auto");
        car.setFuel("MULTI POINT FINJ");
        car.setOptions("POWER MIRRORS, POWER STEERING, POWER WINDOWS, CRUISE CONTROL,"
                       + " ENGINE IMMOBILISER, BRAKE ASSIST, DUAL AIRBAG PACKAGE, ANTI-LOCK BRAKING, CENTRAL LOCKING REMOTE CONTROL, ALARM SYSTEM/REMOTE"
                       + " ANTI THEFT, AUTOMATIC AIR CON / CLIMATE CONTROL, ELECTRONIC BRAKE FORCE DISTRIBUTION, CLOTH TRIM, LIMITED SLIP DIFFERENTIAL,"
                       + " RADIO CD WITH 6 SPEAKERS");
        car.setDesc("Dual Airbag Package, Anti-lock Braking, Automatic Air Con / Climate Control, Alarm System/Remote"
                    + " Anti Theft, Brake Assist, Cruise Control, Central Locking Remote Control, Cloth Trim, Electronic Brake Force Distribution,"
                    + " Engine Immobiliser, Limited Slip Differential, Power Mirrors, Power Steering, Power Windows, Radio CD with 6 Speakers"
                    + " CV GOOD KLMS AUTO POWER OPTIONS GOOD KLMS   ");
        car.setStatus("Used");
        car.setNvic("EZR05I");
        return car;
    }

    
    // *************************************
    // Config
    // *************************************

    @Configuration
    public static class TestConfiguration {

        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() {

                    Class<?> type = org.apache.camel.dataformat.bindy.model.car.Car.class;
                    BindyCsvDataFormat dataFormat = new BindyCsvDataFormat();
                    dataFormat.setClassType(type);
                    dataFormat.setLocale("en");

                    from("direct:out")
                            .unmarshal().bindy(BindyType.Csv, type)
                            .to("mock:out");
                    from("direct:in")
                            .marshal(dataFormat)
                            .to("mock:in");
                }
            };
        }
    }
    
    

}
