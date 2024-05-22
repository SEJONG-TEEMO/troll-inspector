package sejong.teemo.trollinspector.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import sejong.teemo.trollinspector.util.ConfigProperties;

import java.io.IOException;

import static sejong.teemo.trollinspector.util.ConfigProperties.*;

@Service
@RequiredArgsConstructor
public class ElasticsearchClientService {

    private ElasticsearchClient esClient;

//    private final static String SCHEME = "http";
//    private final static String HOST_NAME = "localhost";
//    private final static int PORT = 9200;
    private final ConfigProperties configProperties;

    @PostConstruct
    public void init() {
        Elasticsearch elasticsearch = configProperties.getElasticsearch();
        RestClient restClient = RestClient.builder(new HttpHost(
                elasticsearch.getHost(), elasticsearch.getPort(), elasticsearch.getScheme()))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + configProperties.getElasticsearch().getApikey())
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
        }
    }
}


