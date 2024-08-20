package api;

import entity.Grade;
import entity.Team;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class MongoGradeDB implements GradeDB {
    private static final String API_URL = "http://vm003.teach.cs.toronto.edu:20112";
    // load API_TOKEN from env variable.
    private static final String API_TOKEN = System.getenv("password");
    private static final String USERNAME = System.getenv("username");

    public static String getApiToken() {
        return API_TOKEN;
    }
    public static String getUsername() {
        return USERNAME;
    }

    @Override
    public Grade getGrade(String username, String course) {
        // If the username field is empty, check the user's own grades.
        if (username == null || username.isEmpty()) {
            username = USERNAME;
        }

        // Build the request to get the grade.
        // Note: The API requires the username to be passed as a header.
        // Note: The API requires the course to be passed as a query parameter.
        // TODO: what is the difference between a header and parameter?
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("%s/grade?course=%s", API_URL, course))
                .addHeader("Authorization", API_TOKEN)
                .addHeader("username", username)
                .addHeader("Content-Type", "application/json")
                .build();

        // Hint: look at the API documentation to understand what the response looks like.
        try {
            Response response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt("status_code") == 200) {
                JSONObject grade = responseBody.getJSONObject("grade");
                return Grade.builder()
                        .username(grade.getString("username"))
                        .course(grade.getString("course"))
                        .grade(grade.getInt("grade"))
                        .build();
            } else {
                throw new RuntimeException(responseBody.getString("message"));
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public Grade logGrade(String course, int grade) throws JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("course", course);
        requestBody.put("grade", grade);
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(String.format("%s/grade", API_URL))
                .method("POST", body)
                .addHeader("Authorization", API_TOKEN)
                .addHeader("username", USERNAME)
                // TODO: leave it blank on purpose and ask students to fix it?
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt("status_code") == 200) {
                return null;
            } else {
                throw new RuntimeException(responseBody.getString("message"));
            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Team formTeam(String name) throws JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("POST", body)
                .addHeader("Authorization", API_TOKEN)
                .addHeader("username", USERNAME)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt("status_code") == 200) {
                JSONObject team = responseBody.getJSONObject("team");
                JSONArray membersArray = team.getJSONArray("members");
                String[] members = new String[membersArray.length()];
                for (int i = 0; i < membersArray.length(); i++) {
                    members[i] = membersArray.getString(i);
                }

                return Team.builder()
                        .name(team.getString("name"))
                        .members(members)
                        .build();
            } else {
                throw new RuntimeException(responseBody.getString("message"));
            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Team joinTeam(String name) throws JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("PUT", body)
                .addHeader("Authorization", API_TOKEN)
                .addHeader("username", USERNAME)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt("status_code") == 200) {
                return null;
            } else {
                throw new RuntimeException(responseBody.getString("message"));
            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void leaveTeam() throws JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject requestBody = new JSONObject();
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(String.format("%s/leaveTeam", API_URL))
                .method("PUT", body)
                .addHeader("Authorization", API_TOKEN)
                .addHeader("username", USERNAME)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt("status_code") == 200) {
                return;
            } else {
                throw new RuntimeException(responseBody.getString("message"));
            }
        }
        catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // TODO: Implement this method
    //       Hint: Read apiDocuments/getMyTeam.md and refer to the above
    //             methods to help you write this code (copy-and-paste + edit as needed).
    public Team getMyTeam() {
        return null;
    }
}
