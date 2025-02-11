import java.io.*;
import java.util.*;

public class Citys {

    static class Coordinates implements Comparable<Coordinates> {
        String name;
        double latitude;
        double longitude;

        public Coordinates(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public int compareTo(Coordinates other) {
            if (this.latitude != other.latitude) {
                return Double.compare(this.latitude, other.latitude);
            } else {
                return Double.compare(this.longitude, other.longitude);
            }
        }

        @Override
        public String toString() {
            return name + " (" + latitude + ", " + longitude + ")";
        }
    }

    public static void quickSort(Coordinates[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(Coordinates[] arr, int low, int high) {
        Coordinates pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                Coordinates temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Coordinates temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public static Coordinates[] readCitiesFromFile(String fileName) throws IOException {
        List<Coordinates> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    String name = fields[0].replaceAll("\"", "").trim();
                    double latitude = Double.parseDouble(fields[2].replaceAll("\"", "").trim());
                    double longitude = Double.parseDouble(fields[3].replaceAll("\"", "").trim());
                    cities.add(new Coordinates(name, latitude, longitude));
                }
            }
        }
        return cities.toArray(new Coordinates[0]);
    }

    public static void main(String[] args) {
        try {
            Coordinates[] cities = readCitiesFromFile("src/simplemaps_worldcities_basicv1.77/worldcities.csv");
            quickSort(cities, 0, cities.length - 1);
            Scanner scanner = new Scanner(System.in);
            System.out.println("How many cities would you like to print?");
            int numberToDisplay = scanner.nextInt();
            for (int i = 0; i < numberToDisplay && i < cities.length; i++) {
                System.out.println((i + 1) + ". " + cities[i]);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a valid integer.");
        }
    }
}
