
package com.bytatech.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.bytatech.service.dto.DrivoData;

@RestController
@RequestMapping("/api")
public class BatchLoadController {

	private final Logger log = LoggerFactory.getLogger(BatchLoadController.class);

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;

	@Autowired
	ItemWriter<DrivoData> itemWriter;

	@Autowired
	ItemProcessor<DrivoData, DrivoData> itemProcessor;

	@PostMapping("/load-drivo")
	public BatchStatus load(@RequestBody MultipartFile file) throws JobParametersInvalidException,
			JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {

		log.info("...............file................:   " + file);

		Map<String, JobParameter> maps = new HashMap<>();

		maps.put("time", new JobParameter(System.currentTimeMillis()));

		JobParameters parameters = new JobParameters(maps);

		ByteArrayResource[] resources = null;

		JobExecution jobExecution = null;

		for (int i = 0; i < 1; i++) {

			resources = new ByteArrayResource[1];

			resources[i] = new ByteArrayResource(file.getBytes());

			System.out.println("file resource   " + i + "  " + resources[i]);

			Job job = createJob(jobBuilderFactory, stepBuilderFactory, multiResourceItemReader(resources),
					itemProcessor, itemWriter);
			jobExecution = jobLauncher.run(job, parameters);

			System.out.println("JobExecution: " + jobExecution.getStatus());

			System.out.println("Batch is Running..." + jobExecution.isRunning());

		}
		while (jobExecution.isRunning()) {
			System.out.println("...");
		}
		return jobExecution.getStatus();
	}

	public Job createJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			ItemReader<DrivoData> itemReader, ItemProcessor<DrivoData, DrivoData> itemProcessor,
			ItemWriter<DrivoData> itemWriter) {

		Step step = stepBuilderFactory.get("ETL-file-load").<DrivoData, DrivoData>chunk(1000).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).build();

		return jobBuilderFactory.get("ETL-Load").incrementer(new RunIdIncrementer()).start(step).build();
	}

	public MultiResourceItemReader<DrivoData> multiResourceItemReader(ByteArrayResource[] inputResources) {
		MultiResourceItemReader<DrivoData> resourceItemReader = new MultiResourceItemReader<DrivoData>();
		resourceItemReader.setResources(inputResources);
		resourceItemReader.setDelegate(itemReader());
		return resourceItemReader;
	}

	public FlatFileItemReader<DrivoData> itemReader() {

		FlatFileItemReader<DrivoData> flatFileItemReader = new FlatFileItemReader<>();

		flatFileItemReader.setName("XLSX-Reader");
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

	@PostMapping("/upload-file")
	public void uploadFile(@RequestBody MultipartFile file) throws IOException {
		System.out.println("file        ............        :" + file);
		byte[] bytes = file.getBytes();
		Path path = Paths.get("src/main/resources/" + file.getOriginalFilename());
		Files.write(path, bytes);
	}


}
