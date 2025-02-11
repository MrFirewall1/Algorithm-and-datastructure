import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

public class cityLatitude {
    static class City {
        String name;
        double latitude;
        City(String name, double latitude) {
            this.name = name;
            this.latitude = latitude;
        }
        @Override
        public String toString() {
            return this.name + ": Latitude " + latitude;
        }
    }

    private static int totalMerges = 0;

    public static void main(String[] args) throws IOException {
        List<City> cities = readCitiesFromCSV("C:\\Users\\ilias\\Downloads\\simplemaps_worldcities_basicv1.77\\worldcities.csv");
        mergeSort(cities, 0, cities.size() - 1);
        System.out.println("Total number of merges needed: " + totalMerges);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of cities you want to see: ");
        int numCities = scanner.nextInt();
        numCities = Math.min(numCities, cities.size());

        for (int i = 0; i < numCities; i++) {
            System.out.println(cities.get(i));
        }
        scanner.close();
    }

    public static List<City> readCitiesFromCSV(String filename) throws IOException {
        List<City> cities = new ArrayList<>();
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 9) {
                    try {
                        String city = values[0].replace("\"", "");
                        String latString = values[2].replace("\"", "");

                        double lat = format.parse(latString).doubleValue();

                        cities.add(new City(city, lat));
                    } catch (Exception e) {
                        System.err.println("Skipping row due to exception: " + Arrays.toString(values));
                        e.printStackTrace();
                    }
                }
            }
        }
        return cities;
    }
    

    public static void mergeSort(List<City> cities, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(cities, left, mid);
            mergeSort(cities, mid + 1, right);
            merge(cities, left, mid, right);
        }
    }

    private static void merge(List<City> cities, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        City[] L = new City[n1];
        City[] R = new City[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = cities.get(left + i);
        for (int j = 0; j < n2; ++j)
            R[j] = cities.get(mid + 1 + j);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i].latitude < R[j].latitude) {
                cities.set(k, L[i]);
                i++;
            } else {
                cities.set(k, R[j]);
                j++;
            }
            k++;
            totalMerges++;
        }

        while (i < n1) {
            cities.set(k, L[i]);
            i++;
            k++;
            totalMerges++;
        }

        while (j < n2) {
            cities.set(k, R[j]);
            j++;
            k++;
            totalMerges++;
        }
    }
}
