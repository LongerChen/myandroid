package myandroid.parser;

import google.youtube.data.Video;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import myandroid.tools.Develop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CopyOfParser {
	public <O> O parse(String source, Class<O> c) {
		JSONObject jsonObject = null;
		O o = null;
		
		try {
			o = c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		for(Field f:c.getFields())
			Develop.e(this, f.getName());
		for(Field f:c.getDeclaredFields()){
			Develop.e(this, f.getName());
			if(f.getName().equals("items"))
				Develop.e(this,"------" + ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);
		}
		for(Field f:c.getSuperclass().getFields())
			Develop.e(this, f.getName());
		for(Field f:c.getSuperclass().getDeclaredFields()){
			Develop.e(this, f.getName());
			f.setAccessible(true);
			try {
				f.set(o, "asdsda");
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Develop.e(this,"Get:::---------" +  f.get(o));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(f.getName().equals("items"))
				Develop.e(this,"------" + ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]);
		}
		try {
			jsonObject = new JSONObject(source);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		List<Method> methods = getSetMethod(c);
		for (Method m : methods) {
			String methodKey = toLowerCase(m.getName().substring(3));
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String jsonKey = keys.next();
//				 Develop.e(this, "methodKey:::" + methodKey);
//				 Develop.e(this, "jsonKey:::" + jsonKey);
				if (toLowerCase(jsonKey).equals(methodKey)) {
					try {
//						Develop.e(this, jsonObject.get(jsonKey).getClass().getName());
						setData(m, o, jsonObject.get(jsonKey));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}

		}
		return o;
	}
	
	private void startParser() {
		// TODO Auto-generated method stub

	}
	
	private Object findValueByKey(JSONObject jObject,String key) {
		Iterator<?> keys = jObject.keys();
		while (keys.hasNext()) {
			String jsonKey = keys.next().toString();
			if(toLowerCase(jsonKey).equals(toLowerCase(key))){
				try {
					return jObject.get(jsonKey);
				} catch (JSONException e) {
					
				}
			}
		}
		return null;
	}

	private <O> void setData(Method method, O o, Object object) {
//		Develop.i(this, o.getClass().getName());
		try {
//			Develop.e(this, object.getClass().getName());
			if(object.getClass().equals(JSONObject.class))
				method.invoke(o, parse(object.toString(), method.getParameterTypes()[0]));
			if(object.getClass().equals(JSONArray.class)){
				JSONArray jsonArray = (JSONArray) object;
				List<Video> videos = new ArrayList<Video>();
				for(int i=0;i<jsonArray.length();i++){
					try {
						videos.add(parse(jsonArray.getString(i), Video.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//				Develop.i(this, method.getParameterTypes()[0].getDeclaringClass());
//				Develop.i(this, method.getParameterTypes()[0].getEnclosingClass());
//				Develop.i(this, method.getParameterTypes()[0].getName());
//				Field f = method.getParameterTypes()[0];
				Develop.i(this, method.getGenericParameterTypes()[0]);
				Develop.i(this, o.getClass().getGenericSuperclass());
				
				method.invoke(o, videos);
			}
			else
				method.invoke(o, object);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<Method> getSetMethod(Class<?> c) {
		List<Method> methods = new ArrayList<Method>();
		for (Method m : c.getMethods())
			if (m.getName().startsWith("set")) {
				methods.add(m);
				// Develop.e(this, m.getName());
			}
		return methods;
	}

	private <O> Method getMethod(Class<O> c, String name,
			Class<?>... parameterTypes) {
		try {
			return c.getMethod(name, parameterTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

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
