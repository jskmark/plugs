package com.parrot.plugin;

import org.apache.cordova.*;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

public class Search extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray data,
			CallbackContext callbackContext) throws JSONException {

		if (action.equals("search")) {

			String searchStr = data.get(0).toString().toLowerCase();
			String message = "searching, " + searchStr;
			JSONArray result = new JSONArray();

			JSONParser parser = new JSONParser();

			try {
                                InputStream raw = this.cordova.getActivity().getAssets().open("www/questions.json");

                                Reader is = new BufferedReader(new InputStreamReader(raw, "UTF8"));
				JSONArray jsonArray = new JSONArray(
						(parser.parse(is)).toString());
				for (int i = 0, size = jsonArray.length(); i < size; i++) {
					JSONObject objectInArray = (JSONObject) jsonArray.get(i);
					if (searchStr.contains(objectInArray.get("topic")
							.toString().toLowerCase())) {
						System.out.println("in topic search");
						result = (JSONArray) objectInArray.get("questions");
						callbackContext.success(result.toString());
						return true;
					} else {

						JSONArray questions = (JSONArray) objectInArray
								.get("questions");
						for (int j = 0; j < questions.length(); j++) {
                                                        JSONObject question = (JSONObject) questions.get(j);
                                                        if (question.has("keywords")){
                                                                JSONArray keywords = (JSONArray) question.get("keywords");
		                                                if (keywords != null){
		                                                     for (int k = 0; k < keywords.length(); k++) {
		                                                         if (searchStr.contains(keywords.get(k).toString())) {
		                                                             result.put(question);
		                                                         }
		                                                     }
		                                                }
							}

						}
					}
				}

			} catch (Exception e) {
			    try{
			       String current = new java.io.File( "." ).getCanonicalPath();
                               callbackContext.success(this.cordova.getActivity().getAssets() + "2" + e.toString());
			    }catch (Exception e1){
				callbackContext.success(e1.toString() + "2" + e.toString());
                            }

			    return true;
			}

			callbackContext.success(result.toString());
			return true;

		} else {

			return false;

		}
	}
}
