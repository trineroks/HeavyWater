package com.test;

import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

import com.naivebayes.NaiveBayesRunnable;

@Path("/predict")
public class API {
	@GET
	@Produces("application/json")
	public Response getResponse() throws JSONException {
		
		JSONObject json = new JSONObject();
		String r = "This is a response";
		
		json.put("Response", r);
		
		String result = "@Produces(\"application/json\") Output: \n\n Response: \n\n" + json;
		return Response.status(200).entity(result).build();
	}
	
	@Path("{s}")
	@GET
	@Produces("application/json")
	public Response getResponse(@PathParam("s") String s) throws JSONException {
		JSONObject json = new JSONObject();

		String output = NaiveBayesRunnable.getInstance().getCategory(s);
		json.put("prediction", output);
		
		String result = "@Produces(\"application/json\") Output: \n\n Category Prediction: \n\n"+json;
		return Response.status(200).entity(result).build();
	}
	
	@Path("post")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response categorize(@FormParam("input") String s) throws JSONException {
		JSONObject json = new JSONObject();

		String output = NaiveBayesRunnable.getInstance().getCategory(s);
		json.put("prediction", output);
		
		String result = "@Produces(\"application/json\") Output: \n\n Category Prediction: \n\n"+json;
		return Response.status(200).entity(result).build();
	}
}
