package com.example.vkfind;

import static com.example.vkfind.utils.NetworkUtils.generateUrl;
import static com.example.vkfind.utils.NetworkUtils.getResponse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    private void showResultTextView(){
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView(){
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    class VkQvery extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute(){
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponse(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String response) {
            String id = null;
            String firstName = null;
            String lastName = null;
            String birthdayDate = null;
            if(response != null && !response.equals("")){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    JSONObject userInfo = jsonArray.getJSONObject(0);
                    id = userInfo.getString("id");
                    firstName = userInfo.getString("first_name");
                    lastName = userInfo.getString("last_name");
                    birthdayDate = userInfo.getString("bdate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String responseInfo = "id: " + id + "\n" + "Имя: " + firstName +
                        "\n" + "Фамилия: " +  lastName + "\n" + "Дата рождения: " + birthdayDate;
                result.setText(responseInfo);
                showResultTextView();
            } else {
                showErrorTextView();
            }
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_searchField);
        searchButton = findViewById(R.id.b_id);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!searchField.getText().toString().equals("")) {
                URL generatedUrl = generateUrl(searchField.getText().toString());
                new VkQvery().execute(generatedUrl);
            } else {
                result.setVisibility(View.INVISIBLE);
            }
        }
        };
        searchButton.setOnClickListener(onClickListener);
    }
}
