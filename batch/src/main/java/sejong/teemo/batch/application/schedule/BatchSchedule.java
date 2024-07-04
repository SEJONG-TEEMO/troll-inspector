package sejong.teemo.batch.application.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sejong.teemo.batch.application.job.info.DivisionInfo;
import sejong.teemo.batch.application.job.info.TierInfo;

import java.util.Arrays;

import static sejong.teemo.batch.application.job.info.TierInfo.*;
import static sejong.teemo.batch.application.job.info.DivisionInfo.*;

@Component
@Slf4j
public class BatchSchedule {

    private final Job leagueInfoJob;
    private final Job migrateDataJob;
    private final JobLauncher jobLauncher;

    public BatchSchedule(@Qualifier("leagueInfoJob") Job leagueInfoJob,
                         @Qualifier("migrateDataJob") Job migrateDataJobJob,
                         JobLauncher jobLauncher) {

        this.leagueInfoJob = leagueInfoJob;
        this.migrateDataJob = migrateDataJobJob;
        this.jobLauncher = jobLauncher;
    }

    private static final String TIER = "tier";
    private static final String DIVISION = "division";

    @Scheduled(cron = "0 0 0 * * SAT")
    public void schedulingBatchUserInfo() {
        Arrays.stream(TierInfo.values()).forEach(this::executeDivisionJob);
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    public void schedulingDataMigration() {
        try {
            jobLauncher.run(migrateDataJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeDivisionJob(TierInfo tierInfo) {
        for (DivisionInfo division : DivisionInfo.values()) {

            if (tierInfo.equals(CHALLENGER) && !division.equals(I)) {
                continue;
            }

            if (tierInfo.equals(GRANDMASTER) && !division.equals(I)) {
                continue;
            }

            if (tierInfo.equals(MASTER) && !division.equals(I)) {
                continue;
            }

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
