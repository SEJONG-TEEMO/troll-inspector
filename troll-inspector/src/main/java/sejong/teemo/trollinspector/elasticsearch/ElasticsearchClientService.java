package sejong.teemo.trollinspector.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchClientService {

    private ElasticsearchClient esClient;

    private final static String SCHEME = "http";
    private final static String HOST_NAME = "localhost";
    private final static int PORT = 9200;
    @Value("${secret.elasticsearch.rest-client.apikey}")
    private String APIKEY;

    @PostConstruct
    public void init() {
        RestClient restClient = RestClient.builder(new HttpHost(HOST_NAME, PORT, SCHEME))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + APIKEY)
                })
                .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.esClient = new ElasticsearchClient(transport);
    }

    public ElasticsearchClient getClient() {
        return this.esClient;
    }

    @PreDestroy
    public void destroy() {
        try {
            this.esClient._transport().close();
        } catch (IOException e) {
            // 로그 처리
        }
    }
}


