package sejong.teemo.trollinspector.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import sejong.teemo.trollinspector.util.ConfigProperties;

import static sejong.teemo.trollinspector.util.ConfigProperties.Elasticsearch;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = "sejong.teemo.trollinspector.repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    private final ConfigProperties configProperties;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
    }

    @Bean
    public RestClient elasticsearchRestClient() {
        Elasticsearch elasticsearch = configProperties.elasticsearch();
        return RestClient.builder(new HttpHost(
                        elasticsearch.host(), elasticsearch.port(), elasticsearch.scheme()))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + elasticsearch.apikey())
                })
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticsearchTemplate elasticsearchOperations() {
        return new ElasticsearchTemplate(elasticsearchClient(elasticsearchRestClient()));
    }
}