
package jp.android.booksample.speechrecognitionsample;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ListActivity implements OnClickListener {

    /*
     * 音声認識ダイアログを表示するためのインテント、リクエストIDです。
     */
    private static final int REQUEST_SPEECHRECOGNIZE = 1;

    /*
     * リストの内容を保持するアダプタオブジェクトです。
     */
    private ArrayAdapter<String> listAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // イベントハンドラの設定
        Button operate_additem = (Button) findViewById(R.id.operate_additem);
        operate_additem.setOnClickListener(this);
        Button operate_deleteitem = (Button) findViewById(R.id.operate_deleteitem);
        operate_deleteitem.setOnClickListener(this);
        
        // リスト用のアダプタを設定
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        setListAdapter(listAdapter);
        
    }
    
    /**
     * 他のアクティビティからの処理結果が戻ったときに、それを処理するメソッドです。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SPEECHRECOGNIZE && resultCode == RESULT_OK) {
            // 音声認識ダイアログの処理が成功した場合
            // 結果のリストを取得
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results.size() > 0) {
                // リストに項目を追加
                listAdapter.add(results.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * ボタンのクリックイベントに応答するイベントハンドラです。
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operate_additem:
                // 追加ボタン
                // 音声認識ダイアログの表示を行うインテントの作成
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, this.getTitle());
                try {
                    // 音声認識ダイアログの表示を行う
                    startActivityForResult(intent, REQUEST_SPEECHRECOGNIZE);
                } catch (ActivityNotFoundException e) {
                    // 音声認識が利用できなかった場合、その旨を表示する
                    Toast.makeText(this, R.string.wording_disablespeechrecognition, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.operate_deleteitem:
                // 削除ボタン
                // もし項目がリストにあれば削除する
                if (listAdapter.getCount() > 0) {
                    listAdapter.remove(listAdapter.getItem(listAdapter.getCount() - 1));
                }
                break;
            default:
                break;
        }
    }

    
}
