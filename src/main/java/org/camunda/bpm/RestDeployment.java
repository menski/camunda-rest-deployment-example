/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class RestDeployment {

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.err.println("No process files specified");
      System.exit(1);
    }

    CloseableHttpClient httpClient = HttpClients.createDefault();

    HttpPost httpPost = new HttpPost("http://localhost:8080/engine-rest/deployment/create");

    StringBody deploymentName = new StringBody("myDeployment", ContentType.TEXT_PLAIN);
    StringBody enableDuplicateFiltering = new StringBody("true", ContentType.TEXT_PLAIN);
    StringBody deployChangedOnly = new StringBody("true", ContentType.TEXT_PLAIN);

    MultipartEntityBuilder builder = MultipartEntityBuilder.create()
      .addPart("deployment-name", deploymentName)
      .addPart("enable-duplicate-filtering", enableDuplicateFiltering)
      .addPart("deploy-changed-only", deployChangedOnly);


    for (String resource : args) {
      File resourceFile = new File(resource);
      FileBody fileBody = new FileBody(resourceFile);
      builder.addPart(resourceFile.getName(), fileBody);
    }

    HttpEntity httpEntity = builder.build();
    httpPost.setEntity(httpEntity);

    HttpResponse response = httpClient.execute(httpPost);

    logResponse(response);
  }

  public static void logResponse(HttpResponse response) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
  }
}
