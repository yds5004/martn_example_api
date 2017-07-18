package net.ncue.home.dao;

import net.ncue.home.util.Parameter;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Created by dsyoon on 17. 7. 18.
 */
public class HomeDao {

    public JSONObject getData(Parameter parameter) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"msg\":\"").append("데이터 전달 드립니다.").append("\"");
        sb.append(",\"query\":\"").append(parameter.getQuery()).append("\"");
        sb.append("}");
        return JSONObject.fromObject(JSONSerializer.toJSON(sb.toString()));
    }
}
