package com.collabsphere.springbatchdemo.config;

import com.collabsphere.springbatchdemo.processor.LegoPartProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.xpagesbeast.lego.model.csv.LegoPart;

@EnableBatchProcessing
@Configuration
public class LegoBatchConfig {

    //Mongo CRUD Operations
    @Autowired
    private MongoTemplate dbCrudOperations;

    // SpringBatch step builder factory
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // SpringBatch job builder factory
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    public LegoBatchConfig(){

    }

    /**
     * Lego Part CSV Reader
     * @return
     */
    @Bean
    public ItemReader<LegoPart> brickPartReader(){
        FlatFileItemReader<LegoPart> reader=new FlatFileItemReader<LegoPart>();
        //loading file
        reader.setResource(new ClassPathResource("data/parts.csv"));
        reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper<LegoPart>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("partNum","name","partCatId","partMaterial");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<LegoPart>() {{
                setTargetType(LegoPart.class);
            }});
        }});


        return reader;
    }

    /**
     * Mongo Item Writer
     */

    @Bean
    public ItemWriter<LegoPart> writerBrickParts(){
        MongoItemWriter<LegoPart> writer=new MongoItemWriter<>();
        writer.setTemplate(dbCrudOperations);
        writer.setCollection("parts");  //similar to Notes View
        return writer;
    }

    /**
     *
     */
    //2. Item Processor
    @Bean
    public ItemProcessor<LegoPart, LegoPart> processor(){
        return new LegoPartProcessor();
    }

    /**
     * Add a Step; we will wire this step to a job
     */
    @Bean
    public Step stepReadBrickPartNumbers() {
        return stepBuilderFactory.get("stepReadBrickPartNumbers")
                .<LegoPart,LegoPart>chunk(100)
                .reader(brickPartReader())
                .processor(processor())
                .writer(writerBrickParts()).build();
    }

    /**
     * Add a Job that will be called by Spring
     */
    @Bean
    public Job jobImportLegoParts() {
        return jobBuilderFactory.get("jobImportLegoParts")
                .incrementer(new RunIdIncrementer()) //unique job instance generator
                .start(stepReadBrickPartNumbers())
                .build();
    }

}
