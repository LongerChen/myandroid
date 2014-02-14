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
public class CopyOfJParser {
	/**
	 * 
	 * @param jObject
	 * @param c
	 * @return
	 */
	public <O> O jObjectToObject(JSONObject jObject, Class<O> c) {
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
	 * @param jArray
	 * @param c
	 * @return
	 */
	public <O> List<O> jArrayToList(JSONArray jArray, Class<O> c) {
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
					Develop.e(this, c.getName() + " Not Match JSONObject Type");
				} catch (JSONException e) {

					Develop.e(this, "JSONObject Not Found");
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
					Develop.e(this, "JSONObject Not Found");
				}
				if (jObject != null)
					list.add(jObjectToObject(jObject, c));
			}
		return list;
	}

	/**
	 * 
	 * @param o
	 * @param field
	 * @param value
	 */
	private void setFieldData(Object o, Field field, Object value) {
		if (!isBaseElement(field.getType()))
			if (field.getType() == List.class) {
				if (value.getClass() == JSONArray.class) {
					JSONArray jArray = (JSONArray) value;
					Class<?> gClass = getlListClass(field);
					value = jArrayToList(jArray, gClass);
				} else
					Develop.e(this, "JSONArray Not Found");
			} else {
				if (value.getClass() == JSONObject.class) {
					JSONObject jObject = (JSONObject) value;
					Class<?> fClass = field.getType();
					value = jObjectToObject(jObject, fClass);
				} else
					Develop.e(this, "JSONObject Not Found");
			}

		field.setAccessible(true);
		try {
			field.set(o, value);
		} catch (IllegalArgumentException e) {
			Develop.i(this, field.getName() + " not match " + value.getClass());
		} catch (IllegalAccessException e) {
			Develop.i(this, field.getName() + " is not accessible ");
		}
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private <O> O newInstance(Class<O> c) {
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			Develop.e(this, "instance fail");
			return null;
		} catch (IllegalAccessException e) {
			Develop.e(this, "instance fail");
			return null;
		}
	}

	private Class<?> getlListClass(Field field) {
		ParameterizedType pt = (ParameterizedType) field.getGenericType();
		return extractClassFromType(pt.getActualTypeArguments()[0]);
	}

	private Class<?> extractClassFromType(Type t) {
		if (t instanceof Class<?>) {
			return (Class<?>) t;
		}
		return (Class<?>) ((ParameterizedType) t).getRawType();
	}

	/**
	 * 
	 * @param jObject
	 * @param key
	 * @return
	 */
	private Object findValueByKey(JSONObject jObject, String key) {
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
		Develop.i(this, key + " and " + key2 + " not found");
		return null;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private boolean isBaseElement(Class<?> c) {
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
	 * @param c
	 * @return
	 */
	private List<Field> getAllField(Class<?> c) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(c.getDeclaredFields()));
		Class<?> superClass = c.getSuperclass();
		if (superClass != null)
			fields.addAll(getAllField(superClass));
		return fields;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private String toLowerCase(String string) {
		String result = "";
		for (char c : string.toCharArray())
			if (c >= 65 && c <= 90)
				result += c += 32;
			else
				result += c;
		return result;
	}
}
