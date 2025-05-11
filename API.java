import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class API {
    public static void main(String[] args) {
        try {
            String urlString = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
            URL url = new URL(urlString);

            // Setup connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IlJFRzEyMzQ3IiwibmFtZSI6Ik1vaGQgTmFiaWwgS2hhbiIsImVtYWlsIjoibW9oZG5hYmlsMjIxMjA0QGFjcm9wb2xpcy5pbiIsInN1YiI6IndlYmhvb2stdXNlciIsImlhdCI6MTc0Njk2NDQyNCwiZXhwIjoxNzQ2OTY1MzI0fQ.o6NYtRLaIaedm_jI30EnPDzQnG_2YfiBpK6NYXuQtwE");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // JSON payload using text block (Java 15+)
            String jsonBody = "{\n" +
                    "  \"finalQuery\": \"SELECT \n" +
                    "      p.AMOUNT AS SALARY,\n" +
                    "      CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,\n" +
                    "      TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,\n" +
                    "      d.DEPARTMENT_NAME\n" +
                    "  FROM \n" +
                    "      PAYMENTS p\n" +
                    "  JOIN \n" +
                    "      EMPLOYEE e ON p.EMP_ID = e.EMP_ID\n" +
                    "  JOIN \n" +
                    "      DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID\n" +
                    "  WHERE \n" +
                    "      DAY(p.PAYMENT_TIME) <> 1\n" +
                    "  ORDER BY \n" +
                    "      p.AMOUNT DESC\n" +
                    "  LIMIT 1;\"\n" +
                    "}";

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            // Read response
            int statusCode = conn.getResponseCode();
            System.out.println("Status Code: " + statusCode);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    (statusCode >= 200 && statusCode < 300) ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                System.out.println("Response Body: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}