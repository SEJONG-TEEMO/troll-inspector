package sejong.teemo.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.entity.UserInfo;
import sejong.teemo.batch.exception.FailedRetryException;
import sejong.teemo.batch.item.process.LeagueItemProcess;
import sejong.teemo.batch.item.reader.LeagueItemReader;
import sejong.teemo.batch.item.writer.LeagueItemWriter;
import sejong.teemo.batch.repository.JdbcRepository;
import sejong.teemo.batch.service.BatchService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class LeagueJob {

    private final PlatformTransactionManager tm;
    private final BatchService batchService;
    private final JdbcRepository jdbcRepository;

    private static final String TIER_JOB = "tierJob";
    private static final String STEP = "I";

    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job leagueInfoJob(JobRepository jobRepository) {
        return new JobBuilder(TIER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(leagueInfoStepOne(jobRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step leagueInfoStepOne(JobRepository jobRepository) {
        return new StepBuilder(TIER_JOB + STEP, jobRepository)
                .<List<UserInfoDto>, List<UserInfo>>chunk(CHUNK_SIZE, tm)
                .reader(leagueInfoItemReader(null, null))
                .processor(leagueInfoProcessor())
                .writer(leagueInfoItemWriter())
                .faultTolerant()
                .skip(FailedRetryException.class)
                .skipLimit(1)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<List<UserInfoDto>> leagueInfoItemReader(@Value("#{jobParameters['tier']}") String tier,
                                                              @Value("#{jobParameters['division']}") String division) {
        return new LeagueItemReader(batchService, tier, division);
    }

    @Bean
    @StepScope
    public ItemProcessor<List<UserInfoDto>, List<UserInfo>> leagueInfoProcessor() {
        return new LeagueItemProcess();
    }

    @Bean
    @StepScope
    public ItemWriter<List<UserInfo>> leagueInfoItemWriter() {
        return new LeagueItemWriter(jdbcRepository);
    }
}
