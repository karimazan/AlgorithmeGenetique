package tp2bio;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class Main {
    public static void main(String[] args) {
        String[] scenarioNames = {"Scenario 1", "Scenario 2", "Scenario 3", "Scenario 4"};

        double[][] scenarios = {
                {0.75, 0.005, 30},
                {0.75, 0.005, 50},
                {0.90, 0.01, 30},
                {0.90, 0.01, 50}
        };

        // Population initiale
        int[] populationInitiale = {18, 6, 11, 27};

        // Création des séries de données pour les graphiques
        XYSeriesCollection fitnessDataset = new XYSeriesCollection();
        XYSeriesCollection avgFitnessDataset = new XYSeriesCollection();
        XYSeriesCollection maxFitnessDataset = new XYSeriesCollection();
        XYSeriesCollection executionTimeDataset = new XYSeriesCollection();
        XYSeriesCollection numCrossoversDataset = new XYSeriesCollection();
        XYSeriesCollection numMutationsDataset = new XYSeriesCollection();
        XYSeriesCollection newSolutionsDataset = new XYSeriesCollection();

        for (int i = 0; i < scenarios.length; i++) {
            double[] scenario = scenarios[i];
            String scenarioName = scenarioNames[i];
            double pc = scenario[0];
            double pm = scenario[1];
            int maxGeneration = (int) scenario[2];

            AlgorithmeGenetique ag = new AlgorithmeGenetique(populationInitiale, pc, pm, maxGeneration);
            ag.run();

            XYSeries fitnessSeries = new XYSeries("Fitness - " + scenarioName);
            XYSeries avgFitnessSeries = new XYSeries("Average Fitness - " + scenarioName);
            XYSeries maxFitnessSeries = new XYSeries("Max Fitness - " + scenarioName);
            XYSeries executionTimeSeries = new XYSeries("Execution Time - " + scenarioName);
            XYSeries numCrossoversSeries = new XYSeries("Number of Crossovers - " + scenarioName);
            XYSeries numMutationsSeries = new XYSeries("Number of Mutations - " + scenarioName);
            XYSeries newSolutionsSeries = new XYSeries("New Solutions - " + scenarioName);

            // Ajout des données du scénario aux séries
            for (int j = 0; j < ag.getFitnessVector().size(); j++) {
                fitnessSeries.add(j + 1, ag.getFitnessVector().get(j));
                avgFitnessSeries.add(j + 1, ag.getAverageFitness());
                maxFitnessSeries.add(j + 1, ag.getMaxFitness());
                executionTimeSeries.add(j + 1, ag.getExecutionTime());
                numCrossoversSeries.add(j + 1, ag.getNumCrossovers());
                numMutationsSeries.add(j + 1, ag.getNumMutations());
                //newSolutionsSeries.add(j + 1, ag.getNewSolutions());
            }

            fitnessDataset.addSeries(fitnessSeries);
            avgFitnessDataset.addSeries(avgFitnessSeries);
            maxFitnessDataset.addSeries(maxFitnessSeries);
            executionTimeDataset.addSeries(executionTimeSeries);
            numCrossoversDataset.addSeries(numCrossoversSeries);
            numMutationsDataset.addSeries(numMutationsSeries);
            newSolutionsDataset.addSeries(newSolutionsSeries);

            System.out.println("Temps d'exécution de l'algorithme : " + ag.getExecutionTime() + " ms");
            System.out.println("Vecteur des fitness : " + ag.getFitnessVector());
            System.out.println("Fitness moyenne : " + ag.getAverageFitness());
            System.out.println("Fitness maximale : " + ag.getMaxFitness());
            System.out.println("Nombre de croisements effectués : " + ag.getNumCrossovers());
            System.out.println("Nombre de mutations effectuées : " + ag.getNumMutations());
            System.out.println();
        }

        // Création des graphiques
        createAndShowChart(fitnessDataset, "Fitness", "Génération", "Fitness", "Comparaison des Scénarios - Fitness");
        createAndShowChart(avgFitnessDataset, "Average Fitness", "Génération", "Average Fitness", "Comparaison des Scénarios - Average Fitness");
        createAndShowChart(maxFitnessDataset, "Max Fitness", "Génération", "Max Fitness", "Comparaison des Scénarios - Max Fitness");
        createAndShowChart(executionTimeDataset, "Execution Time", "Génération", "Execution Time (ms)", "Comparaison des Scénarios - Execution Time");
        createAndShowChart(numCrossoversDataset, "Number of Crossovers", "Génération", "Number of Crossovers", "Comparaison des Scénarios - Number of Crossovers");
        createAndShowChart(numMutationsDataset, "Number of Mutations", "Génération", "Number of Mutations", "Comparaison des Scénarios - Number of Mutations");
        createAndShowChart(newSolutionsDataset, "New Solutions", "Génération", "Number of New Solutions", "Comparaison des Scénarios - Number of New Solutions");
    }

    private static void createAndShowChart(XYDataset dataset, String title, String xAxisLabel, String yAxisLabel, String chartTitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



class AlgorithmeGenetique {
    private int[] population;
    private double pc;
    private double pm;
    private int maxGeneration;

    // Variables pour les statistiques
    private long startTime;
    private long endTime;
    private List<Double> fitnessVector = new ArrayList<>();
    private int numCrossovers = 0;
    private int numMutations = 0;


    public AlgorithmeGenetique(int[] populationInitiale, double pc, double pm, int maxGeneration) {
        this.population = populationInitiale;
        this.pc = pc;
        this.pm = pm;
        this.maxGeneration = maxGeneration;
    }

    public void run() {
        startTime = System.currentTimeMillis();

        int generation = 0;
        while (generation < maxGeneration) {
            // Croisement
            if (Math.random() < pc) {
                croisement();
                numCrossovers++;
            }

            // Mutation
            if (Math.random() < pm) {
                mutation();
                numMutations++;
            }

            // Sélection
            selectionRoulette();


            // Calcul de la fitness moyenne et maximale
            double sumFitness = 0;
            double maxFitness = Double.MIN_VALUE;
            for (int x : population) {
                double fitness = fitness(x);
                sumFitness += fitness;
                if (fitness > maxFitness) {
                    maxFitness = fitness;
                }
            }
            fitnessVector.add(sumFitness / population.length);

            // Incrémentation de la génération
            generation++;
        }

        endTime = System.currentTimeMillis();
    }

    private void croisement() {
        Random rand = new Random();
        for (int i = 0; i < population.length; i += 2) {
            if (i + 1 < population.length) {
                int parent1 = population[i];
                int parent2 = population[i + 1];

                int mask = rand.nextInt((1 << 5) - 1); // Génère une chaîne aléatoire de 5 bits
                int child1 = (parent1 & mask) | (parent2 & ~mask);
                int child2 = (parent1 & ~mask) | (parent2 & mask);

                // Assurez-vous que les enfants sont dans l'intervalle [1, 30]
                child1 = Math.max(1, Math.min(30, child1));
                child2 = Math.max(1, Math.min(30, child2));

                population[i] = child1;
                population[i + 1] = child2;
            }
        }
    }


    private void mutation() {
        // Parcourir chaque individu
        for (int i = 0; i < population.length; i++) {
            // Parcourir chaque bit
            for (int j = 0; j < 5; j++) {
                // Effectuer une mutation avec une probabilité pm
                if (Math.random() < pm) {
                    // Inverser le bit j de l'individu i
                    population[i] ^= (1 << j);

                    // Assurez-vous que l'individu est dans l'intervalle [1, 30]
                    population[i] = Math.max(1, Math.min(30, population[i]));
                }
            }
        }
    }

    private void selectionRoulette() {
        // Calculer la somme totale de la fitness
        double totalFitness = 0;
        for (int x : population) {
            totalFitness += fitness(x);
        }

        // Créer une nouvelle population
        int[] newPopulation = new int[population.length];

        // Sélectionner des individus pour la nouvelle population
        for (int i = 0; i < population.length; i++) {
            double rand = Math.random() * totalFitness;
            double cumulativeFitness = 0;
            for (int j = 0; j < population.length; j++) {
                cumulativeFitness += fitness(population[j]);
                if (cumulativeFitness >= rand) {
                    newPopulation[i] = population[j];
                    break;
                }
            }
        }

        // Remplacer l'ancienne population par la nouvelle
        population = newPopulation;
    }



    private double fitness(int x) {
        return -Math.pow(x, 2) + 4 * x;
    }

    // Méthodes pour obtenir les statistiques
    public long getExecutionTime() {
        return endTime - startTime;
    }

    public List<Double> getFitnessVector() {
        return fitnessVector;
    }

    public double getAverageFitness() {
        double sum = 0;
        for (double fitness : fitnessVector) {
            sum += fitness;
        }
        return sum / fitnessVector.size();
    }

    public double getMaxFitness() {
        double max = Double.MIN_VALUE;
        for (double fitness : fitnessVector) {
            if (fitness > max) {
                max = fitness;
            }
        }
        return max;
    }

    public int getNumCrossovers() {
        return numCrossovers;
    }

    public int getNumMutations() {
        return numMutations;
    }
}

