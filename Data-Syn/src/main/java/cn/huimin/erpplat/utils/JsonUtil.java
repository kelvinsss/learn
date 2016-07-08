package cn.huimin.erpplat.utils;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonUtil {

	
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(HashMap.class, new JsonDeserializer<HashMap>() {
		public HashMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			HashMap resultMap = new HashMap<>();
			JsonObject jsonObject = json.getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
			for (Map.Entry<String, JsonElement> entry : entrySet) {
				resultMap.put(entry.getKey(),entry.getValue());
			}
			return resultMap;
		}
	}).setDateFormat("yyyy-MM-dd HH:mm:ss")
	.create();
	
	/**
	 * 
	 * @Title: toObject
	 * @Description: 将json串转换为简单对象
	 * @param @param <T>
	 * @param @param json
	 * @param @param clazz
	 * @return T
	 * @throws
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}

	/**
	 * 
	 * @Title: toJson
	 * @Description: 将对象转换为json串
	 * @param @param obj
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String toJson(Object obj) {
		return GSON.toJson(obj);
	}
	

}
