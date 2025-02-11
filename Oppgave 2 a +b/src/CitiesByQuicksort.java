import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CitiesByQuicksort {

    private static Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter a number to sort and compare the cities from the dataset: ");
        int numberOfCitiesToSort = scanner.nextInt();
        scanner.close();

        String csvFile = "C:/Users/samih/Downloads/simplemaps_worldcities_basicv1.77/worldcities.csv";
        List<City> allCities = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null && allCities.size() < numberOfCitiesToSort) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("^\"|\"$", "").trim();
                }
                try {
                    double latitude = Double.parseDouble(values[2]);
                    double longitude = Double.parseDouble(values[3]);
                    int population = values[9].isEmpty() ? 0 : Integer.parseInt(values[9].replaceAll("[^\\d.]", ""));
                    allCities.add(new City(values[0], values[4], latitude, longitude, values[5], values[6], values[7], values[8], population, values.length > 10 ? values[10] : ""));
                } catch (NumberFormatException e) {
                    System.out.println("error while parsing file: " + Arrays.toString(values));
                }
            }
        } catch (IOException e) {
            System.out.println("Cant read from CSV file " + e.getMessage());
        }

        if (allCities.size() < numberOfCitiesToSort) {
            System.out.println("not enough cities to meet the requiariment. Just " + allCities.size() + " cities was readable.");
        } else {

            List<City> nonRandomizedCities = new ArrayList<>(allCities);
            iterativeQuickSort(nonRandomizedCities);
            long nonRandomizedComparisons = City.getComparisonCount();
            System.out.println("total comparisons without randomized: " + nonRandomizedComparisons);


            List<City> randomizedCities = new ArrayList<>(allCities);
            Collections.shuffle(randomizedCities, random);
            City.resetComparisonCount();
            iterativeQuickSort(randomizedCities);
            long randomizedComparisons = City.getComparisonCount();
            System.out.println("Total comparisons after randomized: " + randomizedComparisons);


            for (int i = 0; i < numberOfCitiesToSort; i++) {
                System.out.println("data from the cities:  " + randomizedCities.get(i));
            }


            if (randomizedComparisons != nonRandomizedComparisons) {
                System.out.println("Total comparisons changed.");
            } else {
                System.out.println("total comparisons didnt changed.");
            }
        }
    }

    private static void iterativeQuickSort(List<City> cities) {
        City.resetComparisonCount();

        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        stack.push(cities.size() - 1);

        while (!stack.isEmpty()) {
            int end = stack.pop();
            int start = stack.pop();

            if (start < end) {
                int p = randomizeByPivot(cities, start, end);
                stack.push(p + 1);
                stack.push(end);
                stack.push(start);
                stack.push(p - 1);
            }
        }
    }

    private static int randomizeByPivot(List<City> cities, int start, int end) {
        City pivot = cities.get(end);
        int i = start - 1;

        for (int p = start; p < end; p++) {
            if (cities.get(p).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(cities, i, p);
            }
        }

        Collections.swap(cities, i + 1, end);
        return i + 1;
    }
}

class City implements Comparable<City> {
    private final String name;
    private final String country;
    private final double latitude;
    private final double longitude;
    private final String iso2;
    private final String iso3;
    private final String adminName;
    private final String capital;
    private final int population;
    private final String id;

    private static long comparisonCount = 0;

    public City(String name, String country, double latitude, double longitude, String iso2, String iso3, String adminName, String capital, int population, String id) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.adminName = adminName;
        this.capital = capital;
        this.population = population;
        this.id = id;
    }

    public static long getComparisonCount() {
        return comparisonCount;
    }

    public static void resetComparisonCount() {
        comparisonCount = 0;
    }

    @Override
    public int compareTo(City other) {
        comparisonCount++;
        return Double.compare(this.latitude, other.latitude);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %f, %f, %s, %s, %s, %s, %d, %s]",
                this.name, this.country, this.latitude, this.longitude,
                this.iso2, this.iso3, this.adminName, this.capital,
                this.population, this.id);
    }
}
