package cn.com.taiji.sys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.com.taiji.sys.json.SysModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SysApplication {

	@Bean
	public ObjectMapper jacksonObjectMapper() {
		return new ObjectMapper().registerModule(new JodaModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.configure(SerializationFeature.INDENT_OUTPUT, true)
				.setDateFormat(new ISO8601DateFormat()).registerModule(new SysModule());
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SysApplication.class, args);
	}
}
