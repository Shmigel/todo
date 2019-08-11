package com.shmigel.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.todo.model.Task;
import com.shmigel.todo.model.User;
import com.shmigel.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoDemoApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(TodoDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User u1 = new User("QWe", "ewq");
		u1.addTask(new Task("Add"));

		User u2 = new User("Sh", "hs");
		u2.addTask(new Task("Create"));
		u2.addTask(new Task("Fill"));

		userService.save(u1);
		userService.save(u2);
	}
}
