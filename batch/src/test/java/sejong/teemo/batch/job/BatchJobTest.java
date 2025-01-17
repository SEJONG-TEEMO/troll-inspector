package sejong.teemo.batch.job;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.batch.container.TestContainer;
import sejong.teemo.batch.application.job.info.DivisionInfo;
import sejong.teemo.batch.application.job.info.TierInfo;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@SpringBatchTest
public class BatchJobTest extends TestContainer {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job leagueInfoJob;

    @Test
    @Disabled
    void 아이언_티어의_유저_저장() throws Exception {
        // given

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @Disabled
    void 하나의_티어의_잡을_수행한다() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("tier", TierInfo.IRON.name())
                .addString("division", DivisionInfo.IV.name())
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
