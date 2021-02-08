package org.acme.rest.client;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WiremockInternalResource implements QuarkusTestResourceLifecycleManager {

  private WireMockServer wireMockServer;

  @Override
  public Map<String, String> start() {
    wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
    wireMockServer.start();

    stubFor(
        get(urlEqualTo("/v2/name/GR"))
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                .withBody(
                    "[{" +
                        "\"name\": \"Ελλάδα\"," +
                        "\"capital\": \"Αθήνα\"" +
                        "}]"
                )));
    return Collections.singletonMap(
        "org.acme.rest.client.CountriesService/mp-rest/url", wireMockServer.baseUrl()
    );
  }

  @Override
  public void stop() {
    if (null != wireMockServer) {
      wireMockServer.stop();
    }
  }
}
