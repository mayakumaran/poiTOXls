package com.bytatech.batch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bytatech.service.dto.DrivoData;

@RestController
@RequestMapping("/api")
public class SingleBatchController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	ItemWriter<DrivoData> itemWriter;

	ItemReader<DrivoData> itemReader;
	@Autowired
	ItemProcessor<DrivoData, DrivoData> itemProcessor;

	@PersistenceContext
	private EntityManager manager;

	@PostMapping("/test")

	public void load(@RequestBody MultipartFile file) throws JobParametersInvalidException,
			JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {

		Map<String, JobParameter> maps = new HashMap<>();
		maps.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters parameters = new JobParameters(maps);

		ByteArrayResource resource = new ByteArrayResource(file.getBytes());

		Job job = createJob(jobBuilderFactory, stepBuilderFactory, itemReader(resource), itemProcessor, itemWriter);

		JobExecution jobExecution = jobLauncher.run(job, parameters);
		
		Query query1 = manager.createNativeQuery(
				"DELETE FROM BATCH_JOB_EXECUTION_CONTEXT WHERE JOB_EXECUTION_ID  = " + 1);
		
		query1.executeUpdate();
		
		System.out.println("JobExecution: ............" + jobExecution.getStatus());

		System.out.println("Batch is Running...");
		while (jobExecution.isRunning()) {
			System.out.println("...");
		}

		/*
		 * Query query2 = manager.
		 * createNativeQuery("DELETE FROM BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID   = "
		 * + 0);
		 * 
		 * 
		 * Query query3 = manager.
		 * createNativeQuery("DELETE FROM BATCH_JOB_INSTANCE  WHERE JOB_INSTANCE_ID   = "
		 * + 0);
		 */

		/*
		 * query2.executeUpdate(); query3.executeUpdate();
		 */

	}

	public Job createJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<DrivoData> itemReader, ItemProcessor<DrivoData, DrivoData> itemProcessor,
			ItemWriter<DrivoData> itemWriter) {

		Step step = stepBuilderFactory.get("ETL-file-load").<DrivoData, DrivoData>chunk(100).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).build();

		return jobBuilderFactory.get("ETL-Load").incrementer(new RunIdIncrementer()).start(step).build();
	}

	public FlatFileItemReader<DrivoData> itemReader(Resource resource) {

		FlatFileItemReader<DrivoData> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}

	public LineMapper<DrivoData> lineMapper() {

		DefaultLineMapper<DrivoData> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] { "regNo", "ownerName", "mobileNo", "vehdecscr" });

		BeanWrapperFieldSetMapper<DrivoData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(DrivoData.class);

		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		return defaultLineMapper;
	}
}
