package com.supimpa.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fon")
public class TestController {
	@GetMapping()
	public String fon() {
		return "fon";
	}
}
