package edu.upc.eetac.dsa.better;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.ws.rs.core.Form;

import edu.upc.eetac.dsa.better.client.BeeterClient;
import edu.upc.eetac.dsa.better.client.BeeterClientException;

public class CreateActivity extends AppCompatActivity {

    EditText etSubject = null;
    EditText etContent = null;
    Button btCreate = null;
    private CreateStingTask mCreateStingTask = null;
    private final static String TAG = LoginActivity.class.toString();
    static final int reqnum2 = 2;


    class CreateStingTask extends AsyncTask<Void, Void, Boolean> {
        private Form form;

        public CreateStingTask(Form form) {
            this.form = form;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result = false;
            try
            {
                result = BeeterClient.getInstance().CreateSting(form);

            } catch (BeeterClientException e) {
                // TODO: Handle gracefully
                Log.d(TAG, e.getMessage());
            }

            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            if (result == true) {
                Intent intent = new Intent(CreateActivity.this, StingsListActivity.class);
                startActivityForResult(intent, reqnum2);
                setResult(RESULT_OK);
                finish();
            }

            if (result == false) {
                Intent i = getIntent();
                setResult(RESULT_CANCELED, i);
                finish();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        etSubject = (EditText)findViewById(R.id.Subject);
        etContent = (EditText)findViewById(R.id.Content);
        btCreate = (Button)findViewById(R.id.Create);

        btCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(etSubject.getText().length()!=0 && etContent.getText().length()!=0)
                {
                    String subject = etSubject.getText().toString();
                    String content = etContent.getText().toString();
                    Form form = new Form();
                    form.param("subject", subject);
                    form.param("content", content);

                    // Execute AsyncTask
                    mCreateStingTask = new CreateStingTask(form);
                    mCreateStingTask.execute((Void) null);
                }


                else
                {
                    Log.d(TAG, "Debes escribir en ambos campos para crear el Sting");
                }
            }

        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == reqnum2) {

            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Sting creado");
            }

            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "no se ha podido crear el Sting");
            }
        }

        startActivity(new Intent(CreateActivity.this, StingsListActivity.class));

    }

}
