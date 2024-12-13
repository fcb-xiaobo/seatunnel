/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.engine.server.rest.servlet;

import com.hazelcast.spi.impl.NodeEngineImpl;
import org.apache.commons.io.IOUtils;
import org.apache.seatunnel.engine.server.rest.service.JobInfoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.seatunnel.engine.server.rest.RestConstant.CONFIG_FORMAT;
import static org.apache.seatunnel.engine.server.rest.RestConstant.HOCON;
public class SubmitJobByUploadFileServlet extends BaseServlet {
    private final JobInfoService jobInfoService;

    private List nums= Arrays.asList(0,1,2,51);


    public SubmitJobByUploadFileServlet(NodeEngineImpl nodeEngine) {
        super(nodeEngine);
        this.jobInfoService = new JobInfoService(nodeEngine);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String fullContent = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);

//        int startIndex = s1.indexOf("env");
//        int endIndex = s1.lastIndexOf("}");
//        String content=s1.substring(startIndex,endIndex+1);

        String[] split = fullContent.split("\n");
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            if(!nums.contains(i)){
                sb.append(split[i]).append("\n");
            }
        }



        System.out.println(sb.toString());

        Map<String, String> requestParams = getParameterMap(req);
        if (HOCON.equalsIgnoreCase(requestParams.get(CONFIG_FORMAT))) {
            writeJson(resp, jobInfoService.submitJob(requestParams, sb.toString().getBytes()));
        } else {
            writeJson(resp, jobInfoService.submitJob(requestParams, requestBody(req)));
        }
    }
}
