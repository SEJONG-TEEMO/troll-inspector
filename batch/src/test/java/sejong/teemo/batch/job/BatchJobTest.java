package sejong.teemo.batch.job;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.batch.container.TestContainer;
import sejong.teemo.batch.repository.JdbcRepository;
import sejong.teemo.batch.service.BatchService;

import static org.assertj.core.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
public class BatchJobTest extends TestContainer {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BatchService batchService;

    @Autowired
    private JdbcRepository jdbcRepository;

    @Test
    void 아이언_티어의_유저_저장() throws Exception {
        // given

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
