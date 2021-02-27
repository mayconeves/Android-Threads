package com.projeto.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class ThreadsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txvLabelThread;
    Button btnExibirImagem, btnProgressDialog;
    ImageView imageViewLogo;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threads_activity);

        txvLabelThread = findViewById(R.id.txvLabelThread);
        btnExibirImagem = findViewById(R.id.btnExibirImagem);
        btnProgressDialog = findViewById(R.id.btnExibirProgress);
        imageViewLogo = findViewById(R.id.imvLogo);

        btnExibirImagem.setOnClickListener(this);
        btnProgressDialog.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnExibirImagem:
                // URL DA IMAGEM
                String url = "https://www.w3schools.com/w3css/img_lights.jpg";

                // THREAD
                // Exemplo 1
                /*
                Runnable processo = new Runnable() {
                    @Override
                    public void run() {
                        // URL DA IMAGEM

                        try {
                            final Bitmap imagem = exibirImagemInternet(url);

                            // A THREAD NÃO TEM ACESSO A INTERFACE
                            // COMO EXIBIR A IMAGEM?
                            // R. ACESSANDO A THREAD PRINCIPAL(HANDLER)
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageViewLogo.setImageBitmap(imagem);
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                // Executar thread
                Thread tarefa = new Thread(processo);
                tarefa.start();
                */

                // THREAD
                // Exemplo 2
                new downloadImagem().execute(url);
                break;


            // THREAD
            // Exemplo 3
            case R.id.btnExibirProgress:
                exibirProgressDialog();
                break;
        }
    }

    /**
     * DOWNLOAD DA IMAGEM DA INTERNET
     * @param url
     * @return
     */
    protected Bitmap exibirImagemInternet(String url){
        Bitmap bitmap = null;

        try {
            URL src = new URL( url );
            InputStream inputStream = (InputStream) src.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /***
     *
     */
    private class  downloadImagem extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            return exibirImagemInternet( params[0] );
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            // Interacao com a interface
            imageViewLogo.setImageBitmap( bitmap );
            super.onPostExecute(bitmap);
        }
    }

    /**
     * EXIBIR PROGRESS DIALOG
     * EXEMPLO 3
     */
    private void exibirProgressDialog(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // ESTILO HORIZONTAL
        // progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // ESTILO SPINNER
        progressDialog.setMessage(getString(R.string.txt_aguarde));
        progressDialog.show();

        // THREAD PARA EXIBIR PROGRESSO
        new Thread(){
            int total = 0;
            @Override
            public void run() {
                super.run();

                // ATUALIZAR BARRA DE PROGRESSO
                while(total<100){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setProgress(total);
                        }
                    });

                    // PAUSAR
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    total++;
                }

                // FECHAR PROGRESSDIALOG
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), getString(R.string.txt_acao_concluida), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }
}