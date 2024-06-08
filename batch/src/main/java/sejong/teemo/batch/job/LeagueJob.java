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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.entity.UserInfo;
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
    private static final String I_STEP = "I";
    private static final String II_STEP = "II";
    private static final String III_STEP = "III";
    private static final String IV_STEP = "IV";

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job leagueInfoJob(JobRepository jobRepository) {
        return new JobBuilder(TIER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(leagueInfoStepOne(jobRepository))
                .next(leagueInfoStepTwo(jobRepository))
                .next(leagueInfoStepThree(jobRepository))
                .next(leagueInfoStepFour(jobRepository))
                .build();
    }

    @Bean
    @JobScope
    public Step leagueInfoStepOne(JobRepository jobRepository) {
        return new StepBuilder(TIER_JOB + I_STEP, jobRepository)
                .<List<UserInfoDto>, List<UserInfo>>chunk(CHUNK_SIZE, tm)
                .reader(leagueInfoItemReader())
                .processor(leagueInfoProcessor())
                .writer(leagueInfoItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step leagueInfoStepTwo(JobRepository jobRepository) {
        return new StepBuilder(TIER_JOB + II_STEP, jobRepository)
                .<List<UserInfoDto>, List<UserInfo>>chunk(CHUNK_SIZE, tm)
                .reader(leagueInfoItemReader())
                .processor(leagueInfoProcessor())
                .writer(leagueInfoItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step leagueInfoStepThree(JobRepository jobRepository) {
        return new StepBuilder(TIER_JOB + III_STEP, jobRepository)
                .<List<UserInfoDto>, List<UserInfo>>chunk(CHUNK_SIZE, tm)
                .reader(leagueInfoItemReader())
                .processor(leagueInfoProcessor())
                .writer(leagueInfoItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step leagueInfoStepFour(JobRepository jobRepository) {
        return new StepBuilder(TIER_JOB + IV_STEP, jobRepository)
                .<List<UserInfoDto>, List<UserInfo>>chunk(CHUNK_SIZE, tm)
                .reader(leagueInfoItemReader())
                .processor(leagueInfoProcessor())
                .writer(leagueInfoItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<List<UserInfoDto>> leagueInfoItemReader() {
        return new LeagueItemReader(batchService,"IRON", "I");
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
