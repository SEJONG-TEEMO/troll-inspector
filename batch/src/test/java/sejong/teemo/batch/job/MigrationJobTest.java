package sejong.teemo.batch.job;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import sejong.teemo.batch.TestBatchConfig;
import sejong.teemo.batch.application.job.MigrationJob;
import sejong.teemo.batch.common.config.RestClientConfig;
import sejong.teemo.batch.container.TestContainer;
import sejong.teemo.batch.common.property.RiotApiProperties;
import sejong.teemo.batch.domain.repository.JdbcRepository;
import sejong.teemo.batch.application.service.BatchService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        MigrationJob.class,
        JdbcRepository.class,
        RestClientConfig.class,
        BatchService.class,
        TestBatchConfig.class,
})
@EnableConfigurationProperties(value = RiotApiProperties.class)
@SpringBatchTest
@Sql("/data-test.sql")
public class MigrationJobTest extends TestContainer {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BatchService batchService;

    @Autowired
    private JdbcRepository jdbcRepository;

    @Test
    @Disabled
    void 데이터_마이그레이션_배치_테스트() throws Exception {
        // given

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
