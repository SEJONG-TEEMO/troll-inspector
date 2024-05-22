package sejong.teemo.trollinspector.elasticsearch;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticsearchClientService {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticsearchClient getClient() {
        return this.elasticsearchClient;
    }
}


