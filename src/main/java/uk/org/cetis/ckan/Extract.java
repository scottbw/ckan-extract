package uk.org.cetis.ckan;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Extract {
	
	public static void main(String[] args) throws Exception{
		getOrganisations();
	}
	
	public static void getOrganisations() throws IOException{
		String s = "http://heidi-ckan.dev.jisc-betas.net/api/3/action/organization_list?all_fields=true&include_extras=true";
		JSONArray organisations = getJSON(s);
		int publ = 0;
		int priv = 0;
		for (int i = 0; i < organisations.length(); i++){
			JSONObject org = organisations.getJSONObject(i);
			//System.out.println(org.getString("display_name"));
			JSONObject extras = org.getJSONArray("extras").getJSONObject(0);
			//System.out.println(org.getString("display_name")+","+extras.getString("value"));
			String sector = extras.getString("value");
			if (sector.equalsIgnoreCase("private")){
				priv++;
			} else {
				publ++;
			}
		}
		System.out.println(organisations.length() + " organisations; "+priv+" private sector and "+publ+" public sector");
		
		int[] counts = {0,0,0,0,0,0,0,0,0,0,0};
		
		String g = "http://heidi-ckan.dev.jisc-betas.net/api/3/action/group_list?all_fields=true";
		JSONArray groups = getJSON(g);
		int packages = 0;
		for (int i = 0; i < groups.length(); i++){
			JSONObject group = groups.getJSONObject(i);
			packages += group.getInt("package_count");
			//System.out.println(group.getString("display_name")+ ","+group.getInt("package_count"));
			counts[group.getInt("package_count")]++;
		}
		System.out.println(groups.length()+" teams");
		System.out.println("Average datasets per team is "+ packages/(groups.length()-1));
		
		for (int i = 0; i < counts.length; i++){
			System.out.println(i + ":" + counts[i]);
		}
		
		String p = "http://heidi-ckan.dev.jisc-betas.net/api/3/action/package_list";
		JSONArray datasets = getJSON(p);
		System.out.println(datasets.length()+" datasets");
		for (int i = 0; i < datasets.length(); i++){
			String d = "http://heidi-ckan.dev.jisc-betas.net/api/3/action/package_show?id="+ datasets.get(i);
			JSONObject dataset = getJSONObject(d);
			String org = dataset.getJSONObject("organization").getString("title");
			//System.out.println(dataset.getString("title") + " was used by " + dataset.getJSONArray("groups").length() +" groups ");
			System.out.println(org +","+dataset.getString("title") + "," + dataset.getJSONArray("groups").length());

		}
	}
	
	public static JSONArray getJSON(String s) throws IOException{
		URL url = new URL(s);
		Scanner scan = new Scanner( url.openStream() );
		String str = new String();
		while (scan.hasNext()){
			str += scan.nextLine();
		}
		scan.close();
		
	    JSONObject obj = new JSONObject(str);
	    JSONArray objs = obj.getJSONArray("result");

		return objs;
	}
	
	public static JSONObject getJSONObject(String s) throws IOException{
		URL url = new URL(s);
		Scanner scan = new Scanner( url.openStream() );
		String str = new String();
		while (scan.hasNext()){
			str += scan.nextLine();
		}
		scan.close();
		
	    JSONObject obj = new JSONObject(str);
	    JSONObject objs = obj.getJSONObject("result");

		return objs;
	}

}
