package com.apps.aplikasiapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView  rvPlayer;
    private PlayerAdapter adapter;
    private ArrayList<Player> players;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvPlayer=findViewById(R.id.rv_player);
        adapter = new PlayerAdapter(this);
        players = new ArrayList<>();
        gson = new Gson();
        ambilData();

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false); //tampilan ke samping
        //GridLayoutManager gr = new GridLayoutManager(this,2); //tampilan dua baris sejajar ke bawah
        //StaggeredGridLayoutManager st = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL); //tampilan dua baris ke bawah tidak sejajar
        DividerItemDecoration divider = new DividerItemDecoration(this, lm.getOrientation());

        rvPlayer.setLayoutManager(lm);
        rvPlayer.setAdapter(adapter);
        rvPlayer.addItemDecoration(divider);

        adapter.setListener(new OnClikListener() {
            @Override
            public void aksiKlik(int position) {
                //cara untk berpindah halaman
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);//berpindah intent dari satu layout ke layout lainnya
                intent.putExtra("idPlayer", players.get(position).getIdPlayer());
                startActivity(intent);
            }
        });
    }
    public void ambilData() {
        //meminta request dengan volley
        //Jika request berhasil, data akan di tampilkan ke dalam rv
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.thesportsdb.com/api/v1/json/1/searchplayers.php?t=Barcelona";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PlayerResult result =gson.fromJson(response,PlayerResult.class);

                players =result.getPlayer(); //menanmpilkan data dari adapter
                adapter.setPlayers(players);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(stringRequest);
    }
}