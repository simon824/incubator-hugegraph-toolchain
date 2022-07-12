/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.baidu.hugegraph.loader.builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.baidu.hugegraph.loader.executor.LoadContext;
import com.baidu.hugegraph.loader.mapping.InputStruct;
import com.baidu.hugegraph.loader.mapping.VertexMapping;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.baidu.hugegraph.structure.schema.SchemaLabel;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import com.baidu.hugegraph.util.E;

public class VertexBuilder extends ElementBuilder<Vertex> {

    private final VertexMapping mapping;
    private final VertexLabel vertexLabel;
    private final Collection<String> nonNullKeys;

    public VertexBuilder(LoadContext context, InputStruct struct,
                         VertexMapping mapping) {
        super(context, struct);
        this.mapping = mapping;
        this.vertexLabel = this.getVertexLabel(this.mapping.label());
        this.nonNullKeys = this.nonNullableKeys(this.vertexLabel);
        // Ensure the id field is matched with id strategy
        this.checkIdField();
    }

    @Override
    public VertexMapping mapping() {
        return this.mapping;
    }

    @Override
    public List<Vertex> build(String[] names, Object[] values) {
        // values 的第三个值是属性的 key，values 的第四个值是属性的 value

        if (this.mapping.existsDmKvAttributes()) {
            String[] s = this.mapping.dmKvAttributes().split(",");
            String[] finalNames = new String[names.length - 1];
            Object[] finalValues = new Object[values.length - 1];
            int nameTag = -1;
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(s[0])) {
                    finalNames[i] = (String) values[2];
                    nameTag = i; // 属性键所在的位置 （注意sql select 时要将属性键放在属性值前面）
                } else if (names[i].equals(s[1])) {
                    finalValues[nameTag] = values[i]; // 属性值所在的位置要与键对应
                    nameTag = -2; // 标记找到了属性值
                } else {
                    if (!"id".equals(names[i])) { //剔除 id 字段，id 为 mysql 的 primaryKey ，图的属性键里没有定义
                        // id 字段。
                        if (nameTag == -2) { //属性值所在字段放到了与属性键对应的位置，所以空了一位，i需要减1不然会越界。
                            finalNames[i - 1] = names[i];
                            finalValues[i - 1] = values[i];
                        } else {
                            finalNames[i] = names[i];
                            finalValues[i] = values[i];
                        }
                    }
                }
            }

            names = Arrays.stream(finalNames).filter(Objects::nonNull).toArray(String[]::new);
            values = Arrays.stream(finalValues).filter(Objects::nonNull).toArray(Object[]::new);
        }
        VertexKVPairs kvPairs = this.newKVPairs(this.vertexLabel,
                                                this.mapping.unfold());
        kvPairs.extractFromVertex(names, values);
        return kvPairs.buildVertices(true);
    }

    @Override
    public SchemaLabel schemaLabel() {
        return this.vertexLabel;
    }

    @Override
    protected Collection<String> nonNullableKeys() {
        return this.nonNullKeys;
    }

    @Override
    protected boolean isIdField(String fieldName) {
        return fieldName.equals(this.mapping.idField());
    }

    private void checkIdField() {
        String name = this.vertexLabel.name();
        if (this.vertexLabel.idStrategy().isCustomize()) {
            E.checkState(this.mapping.idField() != null,
                         "The id field can't be empty or null when " +
                         "id strategy is '%s' for vertex label '%s'",
                         this.vertexLabel.idStrategy(), name);
        } else if (this.vertexLabel.idStrategy().isPrimaryKey()) {
            E.checkState(this.mapping.idField() == null,
                         "The id field must be empty or null when " +
                         "id strategy is '%s' for vertex label '%s'",
                         this.vertexLabel.idStrategy(), name);
        } else {
            // The id strategy is automatic
            throw new IllegalArgumentException(
                    "Unsupported AUTOMATIC id strategy for hugegraph-loader");
        }
    }
}
