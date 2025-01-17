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
package org.apache.camel.dataformat.bindy.springboot.fixed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;
import org.apache.camel.dataformat.bindy.annotation.Link;
import org.apache.camel.model.dataformat.BindyDataFormat;
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
        BindySimpleFixedLengthWithLinkTest.class,
        BindySimpleFixedLengthWithLinkTest.TestConfiguration.class
    }
)
public class BindySimpleFixedLengthWithLinkTest {

    public static final String URI_DIRECT_UNMARSHALL = "direct:unmarshall";
    public static final String URI_MOCK_UNMARSHALL_RESULT = "mock:unmarshall-result";
    public static final String URI_DIRECT_MARSHALL = "direct:marshall";
    public static final String URI_MOCK_MARSHALL_RESULT = "mock:marshall-result";

    private static final String TEST_RECORD = "AAABBBCCC\r\n";

    @EndpointInject(URI_MOCK_UNMARSHALL_RESULT)
    private MockEndpoint unmarshallResult;
    @EndpointInject(URI_MOCK_MARSHALL_RESULT)
    private MockEndpoint marshallResult;
    
    @Autowired
    ProducerTemplate template;

    // *************************************************************************
    // TESTS
    // *************************************************************************

    @Test
    public void testUnmarshallMessage() throws Exception {

        unmarshallResult.expectedMessageCount(1);

        template.sendBody(URI_DIRECT_UNMARSHALL, TEST_RECORD);

        unmarshallResult.assertIsSatisfied();

        // check the model
        Exchange exchange = unmarshallResult.getReceivedExchanges().get(0);
        Order order = exchange.getIn().getBody(Order.class);

        assertEquals("AAA", order.fieldA);
        assertEquals("CCC", order.fieldC);
        assertEquals("BBB", order.subRec.fieldB);
    }

    @Test
    public void testMarshallMessage() throws Exception {

        marshallResult.expectedMessageCount(1);

        Order order = new Order();
        order.setFieldA("AAA");
        order.setFieldC("CCC");
        SubRec subRec = new SubRec();
        subRec.setFieldB("BBB");
        order.setSubRec(subRec);

        template.sendBody(URI_DIRECT_MARSHALL, order);

        marshallResult.assertIsSatisfied();

        // check the model
        Exchange exchange = marshallResult.getReceivedExchanges().get(0);
        String asString = exchange.getIn().getBody(String.class);
        assertThat(asString, is("AAABBBCCC\r\n"));
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
                    BindyDataFormat bindy = new BindyDataFormat();
                    bindy.setClassType(Order.class);
                    bindy.setLocale("en");
                    bindy.type(BindyType.Fixed);

                    from(URI_DIRECT_UNMARSHALL)
                            .unmarshal(bindy)
                            .to(URI_MOCK_UNMARSHALL_RESULT);

                    from(URI_DIRECT_MARSHALL)
                            .marshal(bindy)
                            .to(URI_MOCK_MARSHALL_RESULT);
                }
            };
        }
    }
    
    // *************************************************************************
    // DATA MODEL
    // *************************************************************************
    @FixedLengthRecord
    public static class Order {
        // 'AAA'
        @DataField(pos = 1, length = 3)
        private String fieldA;

        @Link
        private SubRec subRec;

        // 'CCC'
        @DataField(pos = 7, length = 3)
        private String fieldC;

        public String getFieldA() {
            return fieldA;
        }

        public void setFieldA(String fieldA) {
            this.fieldA = fieldA;
        }

        public String getFieldC() {
            return fieldC;
        }

        public void setFieldC(String fieldC) {
            this.fieldC = fieldC;
        }

        public SubRec getSubRec() {
            return subRec;
        }

        public void setSubRec(SubRec subRec) {
            this.subRec = subRec;
        }

    }

    @Link
    @FixedLengthRecord
    public static class SubRec {

        @DataField(pos = 4, length = 3)
        private String fieldB;

        public String getFieldB() {
            return fieldB;
        }

        public void setFieldB(String fieldB) {
            this.fieldB = fieldB;
        }

    }

}
