package lifi.lifi_museum;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;

public class ArtsListingActivity extends AppCompatActivity implements ResultCallBack {
    ListView listView ;
    ConnectServer server;
    AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_arts_listing);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "La Joconde",
                "Le Cri",
                "Portrait d'Adele Bloch-Bauer",
                "Bal du moulin de la Galette",
                "Dora Maar au Chat",
                "Garçon à la pipe",
                "Guernica",
                "Le rêve"
        };

        final String[] description = new String[] {
                // La joconde
                "L'histoire de La Joconde demeure obscure : ni l'identité du modèle, ni la commande du portrait, ni le temps pendant lequel Léonard y travailla, voire le conserva, ni encore les circonstances de son entrée dans la collection royale française ne sont des faits clairement établis.\n" +
                "Deux événements de la vie conjugale de Francesco del Giocondo et de Lisa Gherardini pourraient avoir suscité la réalisation de ce portrait : l'acquisition d'une maison personnelle en 1503 et la naissance d'un second fils, Andrea, en décembre 1502, venu réparer le deuil d'une fille décédée en 1499. Le léger voile sombre qui couvre la chevelure, parfois tenu pour un signe de deuil, est en fait d'un usage assez commun et le signe d'une conduite vertueuse. Aucun élément du costume ne semble remarquable ou signifiant, ni les manches jaunes de la robe, ni la chemise froncée, ni l'écharpe finement drapée sur les épaules ; rien ne désigne ici un rang aristocratique.",
                // Le Cri
                " Ce tableau est une peinture à l'huile et à la pastel d'Evard Munch réalisé en 1893. L'artiste l'a nommé « le cri ».\n" +
                        "\n" +
                        "L'artiste a associé une note dans un de ses journaux a propos de cette oeuvre: « J'étais en train de marcher le long de la route avec deux amis - le soleil se couchait - soudain le ciel devint rouge sang – j'ai fait une pause, me sentant épuisé, et me suis appuyé contre la grille - il y avait du sang et des langues de feu au-dessus du fjord bleu-noir et de la ville - mes amis ont continué à marcher, et je suis resté là tremblant d'anxiété - et j'ai entendu un cri infini déchirer la Nature ».\n" +
                        "\n" +
                        "C'est une composition qui appartient au domaine de l'art figuratif . Elle possède un point de fuite situé à gauche et une grosse bande verticale le long du tableau à droite. On peut donc déduire le sens de lecture de droite vers la gauche. Le spectateur est inclus dans le tableau en se trouvant devant le personnage central ce qui nous donne l'impression de vivre la scène.\n" +
                        "On peut distinguer plusieurs parties distinctes sur ce tableau.\n" +
                        "\n" +
                        "En premier la partie inférieure gauche du tableau. C'est elle qui donne le point de fuite grâce à la barrière et au sol du pont qui sont peint de façon rectiligne. Au niveau du point de fuite on aperçoit deux ombres représentant des personnes qui semblent s'éloigner du personnage central (représentant l'artiste d'après la note associée à l'oeuvre). On retrouve dans cette partie des couleurs rougeâtres assez sombre.\n" +
                        "\n" +
                        "Ensuite le ciel contrairement à la partie précédente est extrêmement sinueux les courbes sont horizontales et en le regardant on comprend aisément l'expression «  langues de feu » que l'artiste a employé.\n" +
                        "\n" +
                        "Enfin la partie centrale de l'oeuvre qui illustre le fjord, le précipice situé à droite et les montagnes au fond. Celle ci comme la partie précédente est extrêmement sinueuse mais ici les courbes sont verticales et nous donnent une impression de vertige. Cette zone centrale crée un contraste de couleur entre le bleu sombre du fjord qui vient choquer contre le rouge ardent du ciel qui est appuyé par l'alternance des courbes.\n" +
                        "\n" +
                        "On peut remarquer que l'artiste a échangé les couleurs du ciel et de la terre comme pour troubler le spectateur et appuyer la sensation de « cri » déjà représenté par toutes ces courbes qui déforment l'image. Le fjord est tellement courbé qu'il en tombe dans le précipice à droite de l'image.\n" +
                        "\n" +
                        "On ne semble pas pouvoir échapper au cri de la Nature, à moins de se boucher les oreilles; ce que fait le personnage central . Le spectateur ne peut échapper au vertige des courbes: à droite la barre verticale à gauche le point de fuite. Ce vertige s'exprime de l'angoisse provoquée par le personnage central, comme le pressentiment d'un malheur que les deux personnages à l'arrière-plan semblent ignorer.",
                // Portrait d'Adele Bloch-Bauer
                "A la demande de Ferdinand Bloch-Bauer, Klimt débute en 1904 le portrait de son épouse Adèle ; il l'achève en 1907, année de son exposition. Le collier porté par Adèle Bloch-Bauer dans cette œuvre est offert à Maria lors de son mariage, à 21 ans, avec Fritz Altmann, jeune et talentueux chanteur d'opéra, à la synagogue de Turnergasse - sa sœur Louise s'était mariée à la grande synagogue de Vienne.",
                // Bal du moulin de la Galette
                "Cette oeuvre est sans doute la plus importante de Renoir au milieu des années 1870 et fut exposée à l'exposition du groupe impressionniste de 1877. Si le peintre choisit de représenter quelques uns de ses amis, il s'attache avant tout à rendre l'atmosphère véhémente et joyeuse de cet établissement populaire de la Butte Montmartre. L'étude de la foule en mouvement dans une lumière à la fois naturelle et artificielle est traitée par des touches vibrantes et colorées. Le sentiment d'une certaine dissolution des formes fut l'une des causes des réactions négatives des critiques de l'époque.\n" +
                        "\n" +
                        "Ce tableau, par son sujet ancré dans la vie parisienne contemporaine, son style novateur mais aussi son format imposant, signe de l'ambition de la démarche de Renoir, est un des chefs-d'oeuvre des débuts de l'impressionnisme.",
                // Dora Maar au Chat
                "Ce tableau de Picasso s’est vendu 95 millions de dollars (75,3 millions d’euros) à New-York en 2006. Il représente Dora Maar, qui a été la compagne du grand peintre entre 1935 et 1943. Elle a pris part au travail du maître, entre autres en photographiant le chef d'oeuvre Guernica dans l’atelier de la rue des Grands-Augustins, où il a été réalisé.\n",
                // Garçon à la pipe
                "Un homme, habillé tout en bleu, une pipe à la main gauche. Des fleurs oranges sont présentes dans ses cheveux bruns. Son regard semble exprimer de la tristesse, de l'ennui. En arrière-plan, des fleurs sont présentes sur le mur marron. ",
                // Guernica
                "GUERNICA a été réalisé par Picasso en 1937. Celui-ci s'est inspiré du bombardement de la petite ville basque de GUERNICA, le 26 avril 1937, par l'aviation allemande au service de Franco. Guernica était une commande du gouvernement républicain espagnol pour le pavillon à l'Exposition Universelle de Paris de juillet 1937.\n" +
                        "\n" +
                        "        Picasso, à travers ce tableau, symbolise l'horreur des conflits humains. Il représente cette horreur grâce à l'emploi de formes très crues montrant la cruauté humaine. Le bombardement de GUERNICA fit 2000 victimes essentiellement des femmes et des enfants.\n" +
                        "        \"La peinture n'est pas faite pour décorer les appartements; c’est une arme offensive et défensive contre l’ennemi\", c'est ce que déclara Picasso à propos de Guernica. Ce tableau fut composé en quelques jours juste après le bombardement du village espagnol de Guernica par la Légion Condor. Ce tableau fut longtemps conservait au Museum of Modern Art de New York mais en 1981, il retourna en Espagne au musée du Prado. Il se trouve aujourd'hui au Musée National Reine Sophie à Madrid. ",
                // Le rêve
                "Detaille est spécialisé, comme son ami de Neuville, dans la peinture militaire, célébrant les \"glorieux vaincus\" de 1870-1871. Ce grand tableau présenté au Salon de 1888 est cependant une prise de position politique directe. Les jeunes conscrits aux manoeuvres, probablement en Champagne, rêvent à la revanche future. C'est le programme implicite du \"brave général\" Boulanger, dont la popularité est alors à son apogée. Les boulangistes fédèrent tous les mécontentements et les déceptions engendrées par une première décennie de pouvoir républicain. De la même manière, les soldats de Detaille associent les souvenirs de la gloire française : les soldats victorieux de la Révolution et de l'Empire se taillent la part du lion, mais ne sont oubliés ni leurs camarades de la Restauration, dont le drapeau blanc l'a emporté aussi au Trocadéro ou à Alger, ni, hachés par la mitraille, les \"braves gens\" de Reichshoffen ou les réchappés de Gravelotte, glorieusement vaincus. L'aspect boulangiste du tableau est vite oublié. Le spectateur voit dans ce grand morceau de peinture héroïque la célébration de l'armée, \"arche sainte\" du pays.\n" +
                        "\n" +
                        "Detaille est médaillé et son tableau acheté par l'Etat qui le présente à l'Exposition Universelle de 1889. Tous les républicains s'accordent devant cette exaltation de l'armée nationale au moment où la République institue le service militaire pour tous les jeunes citoyens (loi du 15 juillet 1889)."
        };

        final int[] images = new int[] {
                R.drawable.joconde,
                R.drawable.lecri,
                R.drawable.portraitadele,
                R.drawable.baldumoulin,
                R.drawable.doramaarauchat,
                R.drawable.garconalapipe,
                R.drawable.guernica,
                R.drawable.lereve
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                String descriptionValue = (String) description[position];
                int imageValue = (int) images[position];

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

                // redirect to new page
                Intent redirectIntent = new Intent(ArtsListingActivity.this, DetailsActivity.class);
                redirectIntent.putExtra("position", itemPosition);
                redirectIntent.putExtra("value", itemValue);
                redirectIntent.putExtra("description", descriptionValue);
                redirectIntent.putExtra("image", imageValue);
                startActivity(redirectIntent);

            }

        });


        server = ConnectServer.getInstance();
        server.get_oeuvres(aq, this);



    }

    @Override
    public void ResultCallBack() {

        Log.d("Oeuvre 1", server.getOeuvres().get(0) + "");
        OeuvreManager oeuvreManager = new OeuvreManager(this); // gestionnaire de la table "oeuvre"
        ImageManager imageManager = new ImageManager(this); // gestionnaire de la table "oeuvre"
        oeuvreManager.open(); // ouverture de la table en lecture/écriture
        imageManager.open();
        oeuvreManager.dropTableOeuvre();
        imageManager.dropTableImage();
        oeuvreManager.createTableOeuvre();
        imageManager.createTableImage();
        System.out.println("---------------Oeuvre SIZE :" + server.getOeuvres().size() + " ---------------");
        for (int i =0;i<server.getOeuvres().size();i++){
            oeuvreManager.addOeuvre(server.getOeuvres().get(i));
            for(int j=0;j<server.getOeuvres().get(i).getImages().size();j++){
                imageManager.addImage(server.getOeuvres().get(i).getImages().get(j), server.getOeuvres().get(i).getId());
            }
        }
        Cursor cursorOeuvre = oeuvreManager.getOeuvres();
        Cursor cursorImage = imageManager.getImages();
        if (cursorOeuvre.moveToFirst())
        {
            do {
                Log.d("OEUVRE",
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_ID_OEUVRE)) + ",\n" +
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_NOM_OEUVRE)) + "\n"
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_DESCRIPTION_OEUVRE)) + ",\n" +
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_UPDATEAT_OEUVRE)) + ""
                );
            }
            while (cursorOeuvre.moveToNext());
        }
        if (cursorImage.moveToFirst())
        {
            do {
                String urlImage = cursorImage.getString(cursorImage.getColumnIndex(ImageManager.KEY_URL_IMAGE));
                Log.d("IMAGE",
                    cursorImage.getString(cursorImage.getColumnIndex(ImageManager.KEY_URL_IMAGE)) + ",\n" +
                    cursorImage.getString(cursorImage.getColumnIndex(ImageManager.KEY_FOREIGNKEY_OEUVRE_IMAGE)) + "\n"
                );

                server.get_images(aq, this, urlImage);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (cursorImage.moveToNext());

        }
        cursorImage.close();
        cursorOeuvre.close(); // fermeture du curseur
        // fermeture du gestionnaire
        oeuvreManager.close();
        imageManager.close();
    }
//    @Override
//    public void ResultCallBackImage(){
//
//    }

}
