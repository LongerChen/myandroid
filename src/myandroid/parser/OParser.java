package myandroid.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OParser {
	public static <O> String objectToString(O o, String offset) {
		if(o==null)
			return "isNull";
		String s = "";
		List<Field> fields = getAllField(o.getClass());
		for (Field field : fields) {
			field.setAccessible(true);
			if (isBaseElement(field.getType()))
				s += getFieldString(field, o, offset);
			else if (field.getType() == List.class) {
				s += offset +field.getType().getSimpleName() + "<" + getlListClass(field).getSimpleName() + ">" + field.getName() + " : \n";
				if (isBaseElement(getlListClass(field))) {
					try {
						List<Object> list = (List<Object>) field.get(o);
						s += offset + "[\n";
						for (Object object : list)
							s += offset +"＋[" + object.getClass().getSimpleName() +"] : " + object.toString() +"\n";
						s += offset + "]\n";
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					try {
						s += offset + "[\n" + listToString(field.get(o), offset + "＋") + offset + "]\n";
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else {
				try {
					Object object = field.get(o);
					s += offset + "[" + field.getType().getSimpleName() + "]"
							+ field.getName() + " : ";
					if (object != null) {
						s += "\n";
						s += objectToString(object, offset + "＋");
					} else {
						s += "null\n";
						s += classToString(field.getType(), offset + "＋");
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return s;
	}

	public static <O> String listToString(O o, String offset) {
		String s = "";
		List<Object> list = (List<Object>) o;
		for (Object object : list)
			s += objectToString(object, offset);
		return s;

	}

	private static String classToString(Class<?> c, String offset) {
		String s = "";
		List<Field> fields = getAllField(c);
		for (Field field : fields)
			if (isBaseElement(field.getType()))
				s += offset + "[" + field.getType().getSimpleName() + "]"
						+ field.getName() + " : null\n";
			else {
				s += offset + "[" + field.getType().getSimpleName() + "]"
						+ field.getName() + " : null\n";
				s += classToString(field.getType(), offset + "＋");
			}

		return s;
	}

	private static String getFieldString(Field field, Object o, String offset) {
		String s = offset + "[" + field.getType().getSimpleName() + "]"
				+ field.getName() + " : ";
		try {
			s += field.get(o);
		} catch (IllegalArgumentException e) {
			s += "null";
		} catch (IllegalAccessException e) {
			s += "un Accessable";
		}
		return s + "\n";
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isBaseElement(Class<?> c) {
		if (c == Integer.TYPE)
			return true;
		else if (c == Float.TYPE)
			return true;
		else if (c == String.class)
			return true;
		else if (c == Double.TYPE)
			return true;
		else if (c == Long.TYPE)
			return true;
		else if (c == Boolean.TYPE)
			return true;
		else if (c == Short.TYPE)
			return true;
		else if (c == Character.TYPE)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private static List<Field> getAllField(Class<?> c) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(c.getDeclaredFields()));
		Class<?> superClass = c.getSuperclass();
		if (superClass != null)
			fields.addAll(getAllField(superClass));
		return fields;
	}

	private static Class<?> getlListClass(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		return extractClassFromType(pt.getActualTypeArguments()[0]);
	}

	private static Class<?> extractClassFromType(Type t) {
		if (t instanceof Class<?>) {
			return (Class<?>) t;
		}
		return (Class<?>) ((ParameterizedType) t).getRawType();
	}
}
