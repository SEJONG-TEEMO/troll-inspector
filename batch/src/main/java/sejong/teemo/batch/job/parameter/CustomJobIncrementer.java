package sejong.teemo.batch.job.parameter;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;

public class CustomJobIncrementer implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        return null;
    }
}
