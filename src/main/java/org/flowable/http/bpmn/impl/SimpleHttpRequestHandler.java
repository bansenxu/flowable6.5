/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.http.bpmn.impl;

import com.bootdo.ext.util.IConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.flowable.bpmn.model.FlowableHttpRequestHandler;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.http.HttpRequest;
import org.flowable.http.delegate.HttpRequestHandler;

/**
 * @author 249875211@qq.com
 */
@Slf4j
public class SimpleHttpRequestHandler extends FlowableHttpRequestHandler implements HttpRequestHandler {

    public SimpleHttpRequestHandler() {
        super.setImplementationType(org.flowable.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        super.setImplementation(getClass().getName());
    }

    private static final long serialVersionUID = 1L;

    protected String appendToken(String url, String token) {

        StringBuffer sb = new StringBuffer(url);
        if (-1 < sb.indexOf("?")) {
            sb.append("&").append(IConstants.KEY_MAP_TOKEN_02).append("=").append(token)
            /*.append("&").append(IConstants.KEY_MAP_TOKEN).append("=Bearer ").append(token)*/;
        } else {
            sb.append("?").append("&").append(IConstants.KEY_MAP_TOKEN_02).append("=").append(token)
            /*.append("&").append(HttpHeaders.AUTHORIZATION).append("=Bearer").append(token)*/;
            ;
        }
        return sb.toString();
    }

    @Override
    public void handleHttpRequest(VariableContainer execution, HttpRequest httpRequest, HttpClient client) {

        String headers = httpRequest.getHeaders();

        Object obj = execution.getVariable(IConstants.KEY_MAP_TOKEN);
        if (null != obj) {
//            httpRequest.setUrl(appendToken(httpRequest.getUrl(), (String) obj));
            StringBuffer sb = new StringBuffer();
            if(null!=headers)
            {
                sb.append(headers).append("\r\n");
            }

            sb.append(IConstants.KEY_MAP_TOKEN).append(": ").append(obj).append("\r\n");
            sb.append(IConstants.KEY_MAP_TOKEN_02).append(": ").append(obj).append("\r\n");

            log.info("set headers :::\n{}",sb.toString());
            httpRequest.setHeaders(sb.toString());
        }
    }

}
