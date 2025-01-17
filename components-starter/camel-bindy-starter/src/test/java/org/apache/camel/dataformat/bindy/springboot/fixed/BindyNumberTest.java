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

import java.math.BigDecimal;


import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;
import org.apache.camel.model.dataformat.BindyDataFormat;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        BindyNumberTest.class,
        BindyNumberTest.TestConfiguration.class
    }
)
public class BindyNumberTest {


    
    public static final String URI_DIRECT_MARSHALL = "direct:marshall";
    public static final String URI_DIRECT_UNMARSHALL = "direct:unmarshall";
    public static final String URI_MOCK_MARSHALL_RESULT = "mock:marshall-result";
    public static final String URI_MOCK_UNMARSHALL_RESULT = "mock:unmarshall-result";

    // *************************************************************************
    //
    // *************************************************************************

    @Produce(URI_DIRECT_MARSHALL)
    private ProducerTemplate mtemplate;

    @EndpointInject(URI_MOCK_MARSHALL_RESULT)
    private MockEndpoint mresult;

    @Produce(URI_DIRECT_UNMARSHALL)
    private ProducerTemplate utemplate;

    @EndpointInject(URI_MOCK_UNMARSHALL_RESULT)
    private MockEndpoint uresult;

    // *************************************************************************
    // TEST
    // *************************************************************************

    @Test
    @DirtiesContext
    public void testMarshall() throws Exception {
        DataModel rec = new DataModel();
        rec.field1 = new BigDecimal(123.45);
        rec.field2 = new BigDecimal(10.00);
        rec.field3 = new BigDecimal(10.00);
        rec.field4 = Double.valueOf(10.00);
        rec.field5 = Double.valueOf(10.00);

        mresult.expectedBodiesReceived("1234510.00   1010.00   10\r\n");

        mtemplate.sendBody(rec);
        mresult.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void testUnMarshall() throws Exception {

        utemplate.sendBody("1234510.00   1010.00   10");

        uresult.expectedMessageCount(1);
        uresult.assertIsSatisfied();

        // check the model
        Exchange exc = uresult.getReceivedExchanges().get(0);
        DataModel data = exc.getIn().getBody(DataModel.class);

        assertEquals(123.45D, data.field1.doubleValue(), 0D);
        assertEquals(10.00D, data.field2.doubleValue(), 0D);
        assertEquals(10.00D, data.field3.doubleValue(), 0D);
        assertEquals(10.00D, data.field4.doubleValue(), 0D);
        assertEquals(10.00D, data.field5.doubleValue(), 0D);
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
                    bindy.setClassType(DataModel.class);
                    bindy.setLocale("en");
                    bindy.type(BindyType.Fixed);

                    from(URI_DIRECT_MARSHALL)
                            .marshal(bindy)
                            .to(URI_MOCK_MARSHALL_RESULT);
                    from(URI_DIRECT_UNMARSHALL)
                            .unmarshal().bindy(BindyType.Fixed, DataModel.class)
                            .to(URI_MOCK_UNMARSHALL_RESULT);
                }
            };
        }
    }
    
    // *************************************************************************
    // DATA MODEL
    // *************************************************************************
    @FixedLengthRecord(length = 25, paddingChar = ' ')
    public static class DataModel {
        @DataField(pos = 1, length = 5, precision = 2, impliedDecimalSeparator = true)
        public BigDecimal field1;
        @DataField(pos = 6, length = 5, precision = 2)
        public BigDecimal field2;
        @DataField(pos = 11, length = 5)
        public BigDecimal field3;
        @DataField(pos = 16, length = 5, precision = 2)
        public Double field4;
        @DataField(pos = 21, length = 5)
        public Double field5;
    }
}
