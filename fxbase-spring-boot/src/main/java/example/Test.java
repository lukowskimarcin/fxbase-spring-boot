package example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype")
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
