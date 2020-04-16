package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DrawGraphActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_graph);

        //Dit is een voorbeeld van hoe een grafiek werkt. Dit kan in eender welke activity verwerkt
        //worden.

        //twee punten, x en y aanmaken
        double x,y;
        x = 0;

        //de graphview uit de layout halen
        GraphView graph = (GraphView)findViewById(R.id.graph);

        //een serie punten voor op de graphview
        series = new LineGraphSeries<>();

        //een vooraf bepaald aantal datapunten
        int numDataPoints = 100;

        //we gaan voor elk punt dat we willen een nieuw datapunt aanmaken en toevoegen aan de serie
        for(int i = 0;i<numDataPoints;i++){
            //y in functie van x, voorbeeld sinus
            y = Math.sin(x);

            //nieuw datapunt aanmaken en toevoegen aan de serie datapunten
            series.appendData(new DataPoint(x,y),true,100);


            //we verhogen x met een kleine hoeveelheid.
            //de grafiek is een puntgrafiek, maar de punten liggen dicht genoeg bij elkaar dat dit
            //niet opvalt en op een lijn lijkt.
            x = x + 0.1;
        }

        //we voegen de serie toe aan de grafiek
        graph.addSeries(series);
    }
}
