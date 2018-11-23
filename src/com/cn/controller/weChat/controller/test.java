package com.cn.controller.weChat.controller;

/**
 * Created by YancyPeng on 2018/10/22.
 */
public class test {
//        public static void main(String[] args) {
//
//            JSONObject jsonObject = JSONObject.parseObject("{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"replyType\":\"text\",\"text\":\"绝代风华\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.baidu.com/\"}]}]}");
//
//            test1(jsonObject);
//        }
//        public static void test1(JSONObject jsonObject){
//            System.out.println("开始前jsonObject的值为：-------------------------" + jsonObject.toJSONString());
//            String authorizerAppid = jsonObject.getString("authorizerAppid");
//            JSONArray button = new JSONArray();
//            JSONObject arrayObject = new JSONObject();
//            JSONObject subObject = new JSONObject();
//            if (!(button = jsonObject.getJSONArray("button")).isEmpty()) {
//                for (int i = 0; i < button.size(); i++) { // 遍历jsonArray
//                    arrayObject = button.getJSONObject(i);
//                    if (arrayObject.getJSONArray("sub_button") != null) { // 如果存在二级菜单，继续遍历二级菜单
//                        JSONArray subButton = arrayObject.getJSONArray("sub_button");
//                        for (int j = 0; j < subButton.size(); j++) {
//                            subObject = subButton.getJSONObject(j);
//                            setSendParam(subObject, authorizerAppid);
//                        }
//                    } else {// 如果不存在二级菜单，查看点击类型
//                        setSendParam(arrayObject, authorizerAppid);
//                    }
//                }
//            }
//            System.out.println("结束后 jsonObject的值为：-------------------------" + jsonObject.toJSONString());
//
//        }
//
//        public static void setSendParam(JSONObject jsonObject, String authorizerAppid){
//            if (jsonObject.getString("type").equals("click")) {// 如果为点击click事件，查看消息类型
//                String replyId = RandomUtils.generateUuidStr();
//                String replyType = jsonObject.getString("replyType");
//                if (replyType.equals("text")) { // 如果为text类型
//
//                    // 删除请求体的相关内容，拼接成调用微信接口需要的参数
//                    jsonObject.remove("replyType");
//                    jsonObject.remove("text");
//                    jsonObject.put("key", replyId);
//
//                    System.out.println("删除后的jsonObject1 的内容为-------------------------------------------" + jsonObject.toJSONString());
//                } else {  // 是图片
//
//                    jsonObject.remove("replyType");
//                    jsonObject.remove("imageUrl");
//                    jsonObject.remove("mediaId");
//                    jsonObject.put("key", replyId);
//                }
//            } // 如果是view事件，不做任何处理
//        }

}
