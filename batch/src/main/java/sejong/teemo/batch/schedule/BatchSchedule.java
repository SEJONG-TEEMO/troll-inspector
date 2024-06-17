package sejong.teemo.batch.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sejong.teemo.batch.job.info.DivisionInfo;
import sejong.teemo.batch.job.info.TierInfo;

import java.util.Arrays;

import static sejong.teemo.batch.job.info.DivisionInfo.*;
import static sejong.teemo.batch.job.info.TierInfo.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchSchedule {

    private final Job leagueInfoJob;
    private final JobLauncher jobLauncher;

    private static final String TIER = "tier";
    private static final String DIVISION = "division";

    @Scheduled(cron = "0 0 0 * * SAT")
    public void schedulingBatchUserInfo() {
        Arrays.stream(TierInfo.values()).forEach(this::executeDivisionJob);
    }

    private void executeDivisionJob(TierInfo tierInfo) {
        for (DivisionInfo division : DivisionInfo.values()) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(TIER, tierInfo.name())
                    .addString(DIVISION, division.name())
                    .toJobParameters();

            try {
                jobLauncher.run(leagueInfoJob, jobParameters);
            } catch (JobExecutionAlreadyRunningException | JobRestartException |
                     JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
