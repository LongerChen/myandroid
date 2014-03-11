package myandroid.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import myandroid.tools.Develop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Tony
 */
public class JParser {

	/**
	 * 
	 * @param source
	 *            JsonString
	 * @param source
	 *            Class
	 * @return try to put Json data into class and return object
	 */
	public static <O> O toObject(String source, Class<O> c) {
		JSONObject jObject = null;
		try {
			jObject = new JSONObject(source);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return toObject(jObject, c);
	}

	/**
	 * 
	 * @param source
	 *            JsonObject
	 * @param source
	 *            Class
	 * @return try to put Json data into class and return object
	 */
	public static <O> O toObject(JSONObject jObject, Class<O> c) {
		O o = newInstance(c);
		if (o == null)
			return null;
		List<Field> fields = getAllField(c);
		for (Field field : fields) {
			Object value = findValueByKey(jObject, field.getName());
			if (value != null)
				setFieldData(o, field, value);
		}
		return o;
	}

	/**
	 * 
	 * @param source
	 *            JsonArray
	 * @param source
	 *            Class
	 * @return try to put Json data into class and return list object
	 */
	public static <O> List<O> toList(JSONArray jArray, Class<O> c) {
		List<O> list = new ArrayList<O>();
		int length = jArray.length();
		if (isBaseElement(c))
			for (int i = 0; i < length; i++) {
				O o = null;
				try {
					if (c != jArray.get(i).getClass())
						throw new ClassCastException();
					else
						o = (O) jArray.get(i);
				} catch (ClassCastException e) {
					Develop.e(JParser.class, c.getName()
							+ " Not Match JSONObject Type");
				} catch (JSONException e) {
					Develop.e(JParser.class, "JSONObject Not Found");
				}
				if (o != null)
					list.add(o);
			}
		else
			for (int i = 0; i < length; i++) {
				JSONObject jObject = null;
				try {
					jObject = jArray.getJSONObject(i);
				} catch (JSONException e) {
					Develop.e(JParser.class, "JSONObject Not Found");
				}
				if (jObject != null)
					list.add(toObject(jObject, c));
			}
		return list;
	}

	/**
	 * 
	 * @param object
	 *            to put data
	 * @param which
	 *            field to put data
	 * @param what
	 *            value to put in field in object
	 */
	private static void setFieldData(Object o, Field field, Object value) {
		if (!isBaseElement(field.getType()))
			if (field.getType() == List.class) {
				if (value.getClass() == JSONArray.class) {
					JSONArray jArray = (JSONArray) value;
					Class<?> gClass = getlListClass(field);
					value = toList(jArray, gClass);
				} else
					Develop.e(JParser.class, "JSONArray Not Found");
			} else {
				if (value.getClass() == JSONObject.class) {
					JSONObject jObject = (JSONObject) value;
					Class<?> fClass = field.getType();
					value = toObject(jObject, fClass);
				} else
					Develop.e(JParser.class, "JSONObject Not Found");
			}

		field.setAccessible(true);
		try {
			field.set(o, value);
		} catch (IllegalArgumentException e) {
			Develop.i(JParser.class,
					field.getName() + " not match " + value.getClass());
			tryToParseType(o, field, value);
		} catch (IllegalAccessException e) {
			Develop.i(JParser.class, field.getName() + " is not accessible ");
		}
	}

	private static void tryToParseType(Object o, Field field, Object value) {
		try {
			if (field.getType() == Integer.TYPE)
				field.set(o, Integer.parseInt(value.toString()));
			if (field.getType() == Short.TYPE)
				field.set(o, Short.parseShort(value.toString()));
			if (field.getType() == Long.TYPE)
				field.set(o, Long.parseLong(value.toString()));
			if (field.getType() == Float.TYPE)
				field.set(o, Float.parseFloat(value.toString()));
			if (field.getType() == Double.TYPE)
				field.set(o, Double.parseDouble(value.toString()));
			if (field.getType() == Boolean.TYPE)
				field.set(o, Boolean.parseBoolean(value.toString()));
			if (field.getType() == String.class)
				field.set(o, String.valueOf(value));
			Develop.i(JParser.class, "try to parse："
					+ value.getClass().getSimpleName() + " > "
					+ field.getType().getSimpleName() + "：Success");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param source
	 *            Class
	 * @return new class object
	 */
	private static <O> O newInstance(Class<O> c) {
		try {
			return c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param List
	 *            field
	 * @return List field class
	 */
	private static Class<?> getlListClass(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		return extractClassFromType(pt.getActualTypeArguments()[0]);
	}

	/**
	 * 
	 * @param t
	 * @return GenericType
	 */
	private static Class<?> extractClassFromType(Type t) {
		if (t instanceof Class<?>)
			return (Class<?>) t;
		return (Class<?>) ((ParameterizedType) t).getRawType();
	}

	/**
	 * 
	 * @param source
	 *            JsonObject
	 * @param value
	 *            key
	 * @return value
	 */
	private static Object findValueByKey(JSONObject jObject, String key) {
		Iterator<?> keys = jObject.keys();
		// --檢查相同key--//
		while (keys.hasNext()) {
			String jsonKey = keys.next().toString();
			if (toLowerCase(jsonKey).equals(toLowerCase(key)))
				try {
					return jObject.get(jsonKey);
				} catch (JSONException e) {

				}
		}
		// --檢查相同key(名稱第一位省去)--//
		keys = jObject.keys();
		String key2 = key.substring(1);
		while (keys.hasNext()) {
			String jsonKey = keys.next().toString();
			if (toLowerCase(jsonKey).equals(toLowerCase(key2)))
				try {
					return jObject.get(jsonKey);
				} catch (JSONException e) {

				}
		}
		Develop.i(JParser.class, key + " and " + key2 + " not found");
		return null;
	}

	/**
	 * 
	 * @param source
	 *            Class
	 * @return isBaseElement
	 */
	private static boolean isBaseElement(Class<?> c) {
		if (c == Integer.TYPE)
			return true;
		else if (c == Integer.class)
			return true;
		else if (c == Float.class)
			return true;
		else if (c == Double.class)
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
	 * @param source
	 *            Class
	 * @return all class field
	 */
	private static List<Field> getAllField(Class<?> c) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(c.getDeclaredFields()));
		Class<?> superClass = c.getSuperclass();
		if (superClass != null)
			fields.addAll(getAllField(superClass));
		return fields;
	}

	/**
	 * 
	 * @param source
	 *            String
	 * @return lower String
	 */
	private static String toLowerCase(String string) {
		String result = "";
		for (char c : string.toCharArray())
			if (c >= 65 && c <= 90)
				result += c += 32;
			else
				result += c;
		return result;
	}
}
