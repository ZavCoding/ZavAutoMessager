package se.ranzdo.bukkit.methodcommand;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReflectionUtil {
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getAnnotation(Class<T> clazz, Method method, int parameterIndex) {
		for(Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
			if(annotation.annotationType() == clazz)  {
				return (T) annotation;
			}
		}
		return null;
	}
}
