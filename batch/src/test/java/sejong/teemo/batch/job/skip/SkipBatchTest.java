package sejong.teemo.batch.job.skip;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import sejong.teemo.batch.TestBatchConfig;
import sejong.teemo.batch.config.RestClientConfig;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedRetryException;
import sejong.teemo.batch.job.info.DivisionInfo;
import sejong.teemo.batch.job.LeagueJob;
import sejong.teemo.batch.job.info.TierInfo;
import sejong.teemo.batch.repository.JdbcRepository;
import sejong.teemo.batch.service.BatchService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {
        TestBatchConfig.class,
        LeagueJob.class,
        PlatformTransactionManager.class,
        BatchService.class,
        JdbcRepository.class,
        RestClientConfig.class,
})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SkipBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private BatchService batchService;

    @Test
    void item_reader에서_에러가_터지면_배치는_skip_한다() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("tier", TierInfo.IRON.mapToString())
                .addString("division", "IV")
                .toJobParameters();

        String division = DivisionInfo.IV.mapToString();
        String tier = TierInfo.IRON.mapToString();
        String queue = "RANKED_SOLO_5x5";

        given(batchService.callApiUserInfo(eq(division), eq(tier), eq(queue), eq(1)))
                .willThrow(new FailedRetryException(ExceptionProvider.RETRY_FAILED));

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}
