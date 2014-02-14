package myandroid.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import myandroid.tools.Develop;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Tony
 */
public class XParser {
	/**
	 * 
	 * @param jObject
	 * @param c
	 * @return
	 */
	public static <O> O xmlToObject(NodeList nodeList, Class<O> c) {
		O o = newInstance(c);
		if (o == null)
			return null;
		List<Field> fields = getAllField(c);
		for (Field field : fields) {
			if (field.getType() == String.class) {
				String value = findValueByKey(nodeList, field.getName());
				setFieldData(o, field, value);
			} else if (field.getType() == List.class) {
				Develop.i(new XParser(), getlListClass(field).getName());
				for(int i=0;i<nodeList.getLength();i++)
					Develop.e(new XParser(), nodeList.item(i).getTextContent());
				Develop.i(new XParser(), Develop.divider);
				setFieldData(o, field,
						xmlToList(nodeList, getlListClass(field)));
			} else {
				NodeList value = findNodeByKey(nodeList, field.getName());
				setFieldData(o, field, xmlToObject(value, field.getType()));
			}
		}
		// for (int i = 0; i < nodeList.getLength(); i++) {
		// Develop.e(XParser.class, nodeList.item(i).getNodeName());
		// }
		return o;
	}

	/**
	 * 
	 * @param jArray
	 * @param c
	 * @return
	 */
	public static <O> List<O> xmlToList(NodeList nodeList, Class<O> c) {
		List<O> list = new ArrayList<O>();
		if (c == String.class)
			for (int i = 0; i < nodeList.getLength(); i++)
				list.add((O) nodeList.item(i).getTextContent());
		else
			for (int i = 0; i < nodeList.getLength(); i++)
				list.add(xmlToObject(nodeList.item(i).getChildNodes(), c));
		return list;
	}

	/**
	 * 
	 * @param o
	 * @param field
	 * @param value
	 */
	private static void setFieldData(Object o, Field field, Object value) {
		field.setAccessible(true);
		if (value != null)
			try {
				field.set(o, value);
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
	 * @param c
	 * @return
	 */
	private static <O> O newInstance(Class<O> c) {
		try {
			return c.newInstance();
		} catch (Exception e) {
			// e.printStackTrace();
			Develop.e(new XParser(), "instance fail:::" + c.getName());
			return null;
		}
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

	/**
	 * 
	 * @param jObject
	 * @param key
	 * @return
	 */
	private static String findValueByKey(NodeList nodeList, String key) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String nodeName = toLowerCase(node.getNodeName());
			String keyName = toLowerCase(key);
			if (nodeName.equals(keyName)) {
				String value = node.getTextContent();
				return value;
			}
		}
		return null;
	}

	private static NodeList findNodeByKey(NodeList nodeList, String key) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String nodeName = toLowerCase(node.getNodeName());
			String keyName = toLowerCase(key);
			if (nodeName.equals(keyName)) {
				NodeList nodelist = node.getChildNodes();
				Develop.i(new XParser(), "find:" + key + ":" + nodelist);
				return nodelist;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param c
	 * @return
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

	/**
	 * 
	 * @param string
	 * @return
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
