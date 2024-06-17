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

    private final TierInfo[] tierInfos = {
            IRON,
            BRONZE,
            SILVER,
            GOLD,
            PLATINUM,
            EMERALD,
            DIAMOND,
            MASTER,
            GRAND_MASTER,
            CHALLENGER
    };

    private final DivisionInfo[] divisions = {I, II, III, IV};

    private static final String TIER = "tier";
    private static final String DIVISION = "division";

    @Scheduled(cron = "0 0 0 * * SAT")
    public void schedulingBatchUserInfo() {
        Arrays.stream(tierInfos).forEach(this::executeDivisionJob);
    }

    private void executeDivisionJob(TierInfo tierInfo) {
        for (DivisionInfo division : divisions) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(TIER, tierInfo.mapToString())
                    .addString(DIVISION, division.mapToString())
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
