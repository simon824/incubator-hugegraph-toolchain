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

package com.baidu.hugegraph.api.graph;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;

import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.client.RestResult;
import com.baidu.hugegraph.exception.NotAllCreatedException;
import com.baidu.hugegraph.structure.constant.HugeType;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.google.common.collect.ImmutableMap;

public class VertexAPI extends GraphAPI {

    public VertexAPI(RestClient client, String graph) {
        super(client, graph);
    }

    @Override
    protected String type() {
        return HugeType.VERTEX.string();
    }

    public Vertex create(Vertex vertex) {
        RestResult result = this.client.post(this.path(), vertex);
        return result.readObject(Vertex.class);
    }

    public List<String> create(List<Vertex> vertices) {
        MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("Content-Encoding", BATCH_ENCODING);
        RestResult result = this.client.post(this.batchPath(), vertices,
                                             headers);
        List<String> ids = result.readList(String.class);
        if (vertices.size() != ids.size()) {
            throw new NotAllCreatedException(
                      "Not all vertices are successfully created, " +
                      "expect '%s', the actual is '%s'",
                      ids, vertices.size(), ids.size());
        }
        return ids;
    }

    public Vertex append(Vertex vertex) {
        String path = RestClient.buildPath(this.path(), vertex.id());
        Map<String, Object> params = ImmutableMap.of("action", "append");
        RestResult result = this.client.put(path, vertex, params);
        return result.readObject(Vertex.class);
    }

    public Vertex eliminate(Vertex vertex) {
        String path = RestClient.buildPath(this.path(), vertex.id());
        Map<String, Object> params = ImmutableMap.of("action", "eliminate");
        RestResult result = this.client.put(path, vertex, params);
        return result.readObject(Vertex.class);
    }

    public Vertex get(String name) {
        RestResult result = this.client.get(this.path(), name);
        return result.readObject(Vertex.class);
    }

    public List<Vertex> list(int limit) {
        Map<String, Object> parmas = ImmutableMap.of("limit", limit);
        RestResult result = this.client.get(this.path(), parmas);
        return result.readList(this.type(), Vertex.class);
    }

    public void delete(String name) {
        this.client.delete(this.path(), name);
    }
}
