/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.mapper.object;

import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.MapperTests;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class NullValueObjectMappingTests {

    @Test public void testNullValueObject() throws IOException {
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type")
                .startObject("properties").startObject("obj1").field("type", "object").endObject().endObject()
                .endObject().endObject().string();

        DocumentMapper defaultMapper = MapperTests.newParser().parse(mapping);

        ParsedDocument doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("obj1").endObject()
                .field("value1", "test1")
                .endObject()
                .copiedBytes());

        assertThat(doc.doc().get("value1"), equalTo("test1"));

        doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .nullField("obj1")
                .field("value1", "test1")
                .endObject()
                .copiedBytes());

        assertThat(doc.doc().get("value1"), equalTo("test1"));

        doc = defaultMapper.parse("type", "1", XContentFactory.jsonBuilder()
                .startObject()
                .startObject("obj1").field("field", "value").endObject()
                .field("value1", "test1")
                .endObject()
                .copiedBytes());

        assertThat(doc.doc().get("obj1.field"), equalTo("value"));
        assertThat(doc.doc().get("value1"), equalTo("test1"));
    }
}
