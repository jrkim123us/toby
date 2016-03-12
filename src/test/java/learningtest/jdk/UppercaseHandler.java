package learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
	Object target;
	public UppercaseHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = (String)method.invoke(target, args);
		if(result instanceof String && method.getName().startsWith("say")) {
			return ((String)result).toUpperCase();
		} else {
			return result;			
		}
	}

}
