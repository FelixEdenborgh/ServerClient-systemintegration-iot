import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.Scanner;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class ServerClient 
{
    private static ClientConfig config = new DefaultClientConfig();
    private static Client client = Client.create(config);
    private static WebResource service = client.resource(
            UriBuilder.fromUri("http://localhost:8080/ServerHallHost/").build());

    private static Scanner in = new Scanner(System.in);

    Gson gson = new Gson();

    
    
    public void listLatestTemp() 
    {
        String jsonString = service
                .path("rest/ServerHallDBService/temperature/latest")
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(jsonString);

        Temp[] tempArray = gson.fromJson(jsonString, Temp[].class);
        for (Temp b : tempArray) {
            System.out.println("Senaste temperaturen är " + b.getTemperature()
                    + " grader och loggades " + b.getDate());

        }

    }

    public void listLatestElforbrukning() 
    {
        String jsonString = service
                .path("rest/ServerHallDBService/econsumption/latest")
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(jsonString);

        ElForbrukning[] econsArray = gson.fromJson(jsonString, ElForbrukning[].class);
        for (ElForbrukning b : econsArray) {
            System.out.println("Senaste elförbrukningen är " + b.getelforbrukning()
                    + " kwh och loggades " + b.getDate());

        }

    }

    public void listLatestelkostnad() 
    {
        String jsonString = service
                .path("rest/ServerHallDBService/elkostnad/latest")
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(jsonString);

        ElKostnad[] elkostnadArray = gson.fromJson(jsonString, ElKostnad[].class);
        for (ElKostnad b : elkostnadArray) {
            System.out.println("Senaste elpriset är " + b.getelkostnad()
                    + " kr/kwh och loggades " + b.getDate());
        }

    }

    public void listGenomsnittligTemp() 
    {
        String jsonString = service
                .path("rest/ServerHallDBService/temperature/avg")
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(jsonString);
        GenomsnittTemp[] avgArray = gson.fromJson(jsonString, GenomsnittTemp[].class);
        
        for (GenomsnittTemp b : avgArray) {
            System.out.println("Avg temp: " + b.getAvg()
                    + "\nMax temp: " + b.getMax()
                    + "\nMin temp: " + b.getMin());

        }
    }

    public void listGenomsnittligElforbrukning() 
    {
        String jsonString = service
                .path("rest/ServerHallDBService/elforbrukning/avg")
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println(jsonString);
        GenomsnittElForbrukning[] avgArray = gson.fromJson(jsonString, GenomsnittElForbrukning[].class);

        for (GenomsnittElForbrukning b : avgArray) {
            System.out.println("Avg econs: " + b.getAvg()
                    + "\nMax elförbrukning: " + b.getMax()
                    + "\nMax elforbrukning pris: " + (b.getMax() * b.getelkostnad() + " kr/kwh")
                    + "\nMin elforbrukning: " + b.getMin()
                    + "\nMin elforbruking pris: " + (b.getMin() * b.getelkostnad() + " kr/kwh ")
                    + "\nWhere elkostnad is " + b.getelkostnad());

        }

    }

    public void addTemp(int i) 
    {
        Temp nyTemp = new Temp(i);
        ClientResponse response = service.path("rest/ServerHallDBService/temperature/add")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, nyTemp);

        System.out.println("Respons " + response.getEntity(String.class));

    }

    public void addelkostnad(int i) 
    {
        ElKostnad nyelkostnad = new ElKostnad(i);
        ClientResponse response = service.path("rest/ServerHallDBService/elkostnad/add")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, nyelkostnad);

        System.out.println("Respons " + response.getEntity(String.class));

    }

    public static void main(String[] args) 
    {
        ServerClient client = new ServerClient();

        while (true) {
            System.out.println("1. Vissa temperaturen");
            System.out.println("2. Vissa elförbrukningen");
            System.out.println("3. Vissa elpriset");
            System.out.println("4. Lägg till ny temperatur");
            System.out.println("5. Lägg till ny elkostnad");
            System.out.println("6. Rapport för senaste temp, elförbrukning och elkostnad");
            System.out.println("7. Rapport för genomsnittliga temperaturen");
            System.out.println("8. Rapport för genomsnittliga elförbrukning + elkostnad");
            System.out.println("Ange ditt val: ");

            String choice = in.nextLine();

            switch (choice) {

                case "1":
                    client.listLatestTemp();
                    break;

                case "2":
                    client.listLatestElforbrukning();
                    break;

                case "3":
                    client.listLatestelkostnad();
                    break;

                case "4":
                    System.out.println("Ange ny temperatur: ");
                    int i = in.nextInt();

                    client.addTemp(i);
                    break;

                case "5":
                    System.out.println("Ange ny elkostnad");
                    int b = in.nextInt();

                    client.addelkostnad(b);
                    break;

                case "6":
                    System.out.println("Rapport: ");
                    client.listLatestTemp();
                    client.listLatestElforbrukning();
                    client.listLatestelkostnad();
                    break;

                case "7":
                    System.out.println("Rapport: ");
                    client.listGenomsnittligTemp();
                    break;
                
                case "8":
                    System.out.println("Rapport");
                    client.listGenomsnittligElforbrukning();
                    break;                                 
            }

        }

    }
}
