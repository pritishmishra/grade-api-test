package api;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Grade;
import entity.Team;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * MongoGradeDataBase class.
 */
public class MongoGradeDataBase implements GradeDataBase {
    // Defining some constants.
    private static final String API_URL = "http://vm003.teach.cs.toronto.edu:20112";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String STATUS_CODE = "status_code";
    private static final String GRADE = "grade";
    private static final String MESSAGE = "message";
    private static final String NAME = "name";
    // load getPassword() from env variable.
    private static final int SUCCESS_CODE = 200;

    public static String getPassword() {
        return System.getenv("password");
    }

    /**
     * Return the username for API calls.
     * @param username the username from the input field.
     * @return the username from the input field if it is not empty, otherwise return the default username.
     */
    public static String getUsername(String username) {
        String result = username;
        if (username == null || username.isEmpty()) {
            result = System.getenv(USERNAME);
        }
        return result;
    }

    @Override
    public Grade getGrade(String username, String course) {

        // Build the request to get the grade.
        // Note: The API requires the username to be passed as a header.
        // Note: The API requires the course to be passed as a query parameter.
        // TODO: what is the difference between a header and parameter?
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("%s/grade?course=%s", API_URL, course))
                .addHeader(PASSWORD, getPassword())
                .addHeader(USERNAME, getUsername(username))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        // Hint: look at the API documentation to understand what the response looks like.
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONObject grade = responseBody.getJSONObject(GRADE);
                return Grade.builder()
                        .username(grade.getString(USERNAME))
                        .course(grade.getString("course"))
                        .grade(grade.getInt(GRADE))
                        .build();
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Grade[] getGrades(String username) {

        // Build the request to get the grade.
        // Note: The API requires the username to be passed as a header.
        // Note: The API requires the course to be passed as a query parameter.
        // TODO: what is the difference between a header and parameter?
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("%s/grades", API_URL))
                .addHeader(PASSWORD, getPassword())
                .addHeader(USERNAME, getUsername(username))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        // Hint: look at the API documentation to understand what the response looks like.
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONArray grades = responseBody.getJSONArray("grades");
                final Grade[] result = new Grade[grades.length()];
                for (int i = 0; i < grades.length(); i++) {
                    final JSONObject grade = grades.getJSONObject(i);
                    result[i] = Grade.builder()
                            .username(grade.getString(USERNAME))
                            .course(grade.getString("course"))
                            .grade(grade.getInt(GRADE))
                            .build();
                }
                return result;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Grade logGrade(String course, int grade) throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put("course", course);
        requestBody.put(GRADE, grade);
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/grade", API_URL))
                .method("POST", body)
                .addHeader(PASSWORD, getPassword())
                .addHeader(USERNAME, getUsername(null))
                // TODO: leave it blank on purpose and ask students to fix it?
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                return null;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Team formTeam(String name) throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(NAME, name);
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("POST", body)
                .addHeader(PASSWORD, getPassword())
                .addHeader(USERNAME, getUsername(null))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONObject team = responseBody.getJSONObject("team");
                final JSONArray membersArray = team.getJSONArray("members");
                final String[] members = new String[membersArray.length()];
                for (int i = 0; i < membersArray.length(); i++) {
                    members[i] = membersArray.getString(i);
                }

                return Team.builder()
                        .name(team.getString(NAME))
                        .members(members)
                        .build();
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Team joinTeam(String name) throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(NAME, name);
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("PUT", body)
                .addHeader(PASSWORD, getPassword())
                .addHeader(USERNAME, getUsername(null))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                return null;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public void leaveTeam() throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/leaveTeam", API_URL))
                .method("PUT", body)
                .addHeader(PASSWORD, getPassword())
                .addHeader(USERNAME, getUsername(null))
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) != SUCCESS_CODE) {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
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
