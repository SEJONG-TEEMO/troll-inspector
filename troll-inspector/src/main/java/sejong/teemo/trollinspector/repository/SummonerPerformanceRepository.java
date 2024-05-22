package sejong.teemo.trollinspector.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.teemo.trollinspector.domain.SummonerPerformance;

import java.util.List;

@Repository
@Primary
public interface SummonerPerformanceRepository extends ElasticsearchRepository<SummonerPerformance, String> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"username.keyword\": \"?0\"}}]}}")
    List<SummonerPerformance> findByUsername(@Param("username") String username);
    List<SummonerPerformance> findByLane(@Param("lane") String lane);

}
