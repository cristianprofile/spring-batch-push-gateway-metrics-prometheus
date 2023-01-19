package com.example.batchprocessing;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {


	public PersonItemProcessor(Counter counter) {
		this.counter = counter;
	}

	private  Counter counter;

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	public PersonItemProcessor() {
	}

	@Override
	public Person process(final Person person) throws Exception {
		counter.increment();
		final String firstName = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		final Person transformedPerson = new Person(firstName, lastName);

		log.info("Converting (" + person + ") into (" + transformedPerson + ")"+counter.count());

		return transformedPerson;
	}

}
