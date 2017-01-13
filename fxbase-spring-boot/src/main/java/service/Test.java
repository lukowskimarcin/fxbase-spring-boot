package service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Test {
	private static int count = 0;
	
	private int x;
	
	public Test() {
		count++;
		x= count;
	}

	
	@Override
	public String toString() {
		return ""+  x;
	}
}
